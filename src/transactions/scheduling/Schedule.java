package transactions.scheduling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import transactions.scheduling.objects.Action;
import transactions.scheduling.objects.Action.Type;
import transactions.scheduling.objects.Transaction;
import transactions.scheduling.objects.Dependency;

public class Schedule {

	/** Menge aller Aktionen. {@link Schedule#buildActions} */
	public final List<Action> actions = new ArrayList<>();

	/** Menge aller Transaktionen. {@link Schedule#buildTransactions} */
	public final List<Transaction> transactions = new ArrayList<>();

	/** Menge aller Konflikte/Abhängigkeiten. {@link Schedule#buildDependencies} */
	public final List<Dependency> dependencies = new ArrayList<>();

	public Schedule(String input) {
		buildActions(input);
		buildTransactions();
		buildDependencies();
	}

	/**
	 * Constructs the list of actions.
	 */
	private void buildActions(String input) {
		String[] line = input.substring(1, input.length() - 1).split(",");
		for (String a : line) {
			Action.Type type = a.charAt(0) == 'r' ? Action.Type.READ : Action.Type.WRITE;
			int transID = Character.getNumericValue(a.charAt(1));
			char objectID = a.charAt(3);
			actions.add(new Action(type, transID, objectID));
		}
	}

	/**
	 * Constructs the list of transactions. Has to be executed after
	 * {@link Schedule#buildActions}.
	 */
	private void buildTransactions() {
		for (int transaction = 1; transaction <= getHighestTransactionID(); transaction++) {
			List<Action> actionsInTransaction = new ArrayList<>();
			for (Action a : actions) {
				if (a.myTransactionID == transaction)
					actionsInTransaction.add(a);
			}
			if (!actionsInTransaction.isEmpty())
				transactions.add(new Transaction(transaction, actionsInTransaction));
		}
	}

	/**
	 * Constructs the list of dependencies from all actions.Has to be executed after
	 * {@link Schedule#buildActions}.
	 */
	private void buildDependencies() {
		for (int i = 0; i < actions.size(); i++) {
			Action a = actions.get(i);
			for (int j = i + 1; j < actions.size(); j++) {
				Action b = actions.get(j);
				if (a.myType != Action.Type.READ || b.myType != Action.Type.READ) {
					if (a.myTransactionID != b.myTransactionID && a.myObject == b.myObject) {
						dependencies.add(new Dependency(a, b));
					}
				}
			}
		}
		Collections.sort(dependencies);
	}

	/**
	 * Finds all anomalies in this schedule and lists them so they can get printed.
	 */
	public String anomaliesToString() {
		String res = "";
		for (int i = 0; i < actions.size(); i++) {
			Action a = actions.get(i);
			List<Action> possAnomalies = actions.stream().skip(i).filter(e -> e.myObject == a.myObject).toList();
			if (possAnomalies.size() >= 3) {
				Action b = possAnomalies.get(1);
				Action c = possAnomalies.get(2);
				if (c.myTransactionID == a.myTransactionID && b.myTransactionID != a.myTransactionID) {
					// Non-Repeatable-Read R1 W2 R1
					if (a.myType == Type.READ && b.myType == Type.WRITE && c.myType == Type.READ)
						res += "Non-Repeatable-Read in: " + a + ", " + b + ", " + c;
					// Lost Update R1 W2 W1
					if (a.myType == Type.READ && b.myType == Type.WRITE && c.myType == Type.WRITE)
						res += "Lost Update in: " + a + ", " + b + ", " + c;

					// Dirty Read W1 R2 W1
					if (a.myType == Type.WRITE && b.myType == Type.READ && c.myType == Type.WRITE)
						res += "Dirty Read in: " + a + ", " + b + ", " + c;

					// Dirty Write W1 W2 W1
					if (a.myType == Type.WRITE && b.myType == Type.WRITE && c.myType == Type.WRITE)
						res += "Dirty Write in: " + a + ", " + b + ", " + c;
				}
			}
		}
		return res.isBlank() ? "Keine Anomalien erkannt." : res;
	}

	// HELPER-METHODS-------------------------------------------------------------------------------------

	/**
	 * Returns the highest transactionID of an action in this schedule.
	 * 
	 * @see {@link Schedule#buildTransactions}
	 */
	private int getHighestTransactionID() {
		int max = -1;
		for (Action a : actions)
			max = a.myTransactionID > max ? a.myTransactionID : max;
		return max;
	}
}

package transactions.scheduling.objects;

import java.util.List;

public class Transaction {

	public final int myTransactionID;
	public final List<Action> myActions;

	/**
	 * Builds a transaction out of a list of actions.
	 */
	public Transaction(int transactionID, List<Action> actions) {
		this.myTransactionID = transactionID;
		this.myActions = actions;
		for (Action a : myActions)
			if (a.myTransactionID != myTransactionID)
				throw new AssertionError("The transactionID of every action in the transaction has to match the ID of the transaction.");
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Transaction t)
			return myTransactionID == t.myTransactionID && myActions.equals(t.myActions);
		return false;
	}

	@Override
	public String toString() {
		return "<T" + myTransactionID + ":" + myActions.toString() + ">";
	}
}

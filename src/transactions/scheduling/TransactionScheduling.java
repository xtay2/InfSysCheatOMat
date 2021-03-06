package transactions.scheduling;

import java.util.ArrayList;
import java.util.List;

import main.Main;

public class TransactionScheduling {

	/** All valid Schedules, that get the user writes into the command line. */
	private static List<Schedule> schedules;

	/**
	 * Initialises and executes this module. Gets called by {@link Main}.
	 */
	public static void init() {
		schedules = new ArrayList<>();
		System.out.println("\nTRANSAKTIONS-SCHEDULES");
		System.out.println("Gib Schedules im Schema \"(r1(x),r2(x),r3(y),w2(x),w3(y))\" ein. Wenn du fertig bist, schreib \"compute\".");
		for (int i = 1; true; i++) {
			String in = Main.readInput("S" + i + "=").replace(" ", "");
			if ("compute".equalsIgnoreCase(in))
				break;
			if (in.matches("\\((\\w\\d\\(\\w\\),?)+\\)"))
				schedules.add(new Schedule(in));
			else {
				System.err.println("Die Eingabe passt nicht zum Schema. Beispiele: \"(r1(x))\", \"(r1(x)\", \"w1(x))\"");
				i--;
			}
		}
		compute();
	}

	/**
	 * Prints all relevant stats about the {@link TransactionScheduling#schedules}.
	 */
	private static void compute() {
		System.out.println("\nTransaktionsmengen:");
		for (int i = 0; i < schedules.size(); i++)
			System.out.println("S" + (i + 1) + ": " + schedules.get(i).transactions);
		String identical = findIdenticalTransactions();
		if (identical != null) {
			System.out.println(identical);
			System.out.println("\nAbhängigkeitsmengen:");
			for (int i = 0; i < schedules.size(); i++)
				System.out.println("S" + (i + 1) + ": " + schedules.get(i).dependencies);
			System.out.println(findConflictEquivalences());
		} else
			System.out.println("Es gibt keine identischen Transaktionsmengen.");
		
		System.out.println("\nAnomalien:");
		for (int i = 0; i < schedules.size(); i++)
			System.out.println("S" + (i + 1) + ": " + schedules.get(i).anomaliesToString());		
	}

	/**
	 * Returns a String-Representation that later gets printed. It tells, if the
	 * schedules given in {@link TransactionScheduling#schedules} have identical
	 * transaction-lists.
	 */
	private static String findIdenticalTransactions() {
		String res = "";
		for (int i = 0; i < schedules.size(); i++) {
			for (int j = i; j < schedules.size(); j++) {
				if (i != j && schedules.get(i).transactions.equals(schedules.get(j).transactions))
					res += "Schedule " + (i + 1) + " und " + (j + 1) + " haben identische Transaktionsmengen.\n";
			}
		}
		return res.isEmpty() ? null : res;
	}

	private static String findConflictEquivalences() {
		String res = "";
		for (int i = 0; i < schedules.size(); i++) {
			Schedule a = schedules.get(i);
			for (int j = i + 1; j < schedules.size(); j++) {
				Schedule b = schedules.get(j);
				if (a.dependencies.equals(b.dependencies))
					res += "Schedule " + (i + 1) + " und " + (j + 1) + " sind Konfliktäquivalent.\n";
				else {
					res += "\nSchedule " + (i + 1) + " und " + (j + 1) + " haben mindestens einen Konflikt!\n";
					res += "Unterschiede:\n";
					res += "Nur S" + (i + 1) + " enthält: " + a.dependencies.stream().filter(e -> !b.dependencies.contains(e)).toList() + "\n";
					res += "Nur S" + (j + 1) + " enthält: " + b.dependencies.stream().filter(e -> !a.dependencies.contains(e)).toList() + "\n";
					res += "\n";
				}
			}
		}
		return res;
	}
}

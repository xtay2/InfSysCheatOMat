
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import transactions.scheduling.TransactionScheduling;

public class Main {

	/**
	 * Takes the initial user input and selects the modules.
	 */
	public static void main(String[] args) {
		System.out.println("Willkommen beim Infsys-Cheat-O-Mat!\n");
		while (true) {
			System.out.println("Module:");
			System.out.println("1) Transaktions-Scheduling.");
			String module = readLine("\nWähle ein Modul durch Angeben der Zahl: ");
			findModule(module.strip());
			readLine("Gib \"exit\" ein, um das Programm zu schließen, oder drück ENTER um eine weitere Aufgabe zu lösen: ");
			System.out.println("\n\n\n");
		}
	}

	/**
	 * Every module gets called here.
	 */
	private static void findModule(String module) {
		try {
			switch (Integer.valueOf(module)) {
			case 1 -> TransactionScheduling.init();
			//case 2 -> Hier Modul.init() aufrufen.
			default -> System.err.println("Unbekanntes Modul!\n");
			}
		} catch (NumberFormatException e) {
			System.err.println("Bitte gib eine valide Zahl an!");
		}
	}

	/**
	 * Reads a line from the console. When the input is "exit" the program
	 * terminates.
	 * 
	 * @param msg is the message thats asking for the input.
	 * @return the input or an empty string, if an error occured.
	 */
	public static String readLine(String msg) {
		System.out.print(msg);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			String res = reader.readLine();
			if ("exit".equalsIgnoreCase(res)) {
				System.err.println("Programm beendet.");
				System.exit(0);
			}
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}

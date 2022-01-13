package transactions.scheduling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) {
		System.out.println("Willkommen beim Infsys-Cheat-O-Mat!\n");
		while (true) {
			String module = readLine("Wähle ein Modul: " + "\n -\"schedule\" für Transaktions-Scheduling." + "\n");
			findModule(module);
			readLine("Gib \"exit\" ein, um das Programm zu schließen, oder drück ENTER um eine weitere Aufgabe zu lösen: ");
		}
	}

	private static void findModule(String module) {
		if ("schedule".equalsIgnoreCase(module)) {
			TransactionScheduling.init();
		}
	}

	/**
	 * Reads a line from the console. When the input is "exit" the program terminates.
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

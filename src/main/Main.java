package main;

import java.util.Arrays;
import java.util.Scanner;

import input.Disassembler.LanguageType;
import relalgebra.RelConverter;
import transactions.scheduling.TransactionScheduling;

public class Main {

	/**
	 * Takes the initial user input and selects the modules.
	 */
	public static void main(String[] args) {
		System.out.println("Willkommen zum InfSys-cheat-o-Mat.");
		try {
			while (true) {
				System.out.println("Module:");
				System.out.println("1) Transaktions-Scheduling.");
				System.out.println("2) SQL zu Rel. Algebra Converter.");
				System.out.println("3) Rel. Algebra zu SQL Converter.");
				String module = readInput("\nWähle ein Modul durch Angeben der Zahl: ");
				findModule(module.strip());
				readInput("Gib \"exit\" ein, um das Programm zu schließen, oder drück ENTER um eine weitere Aufgabe zu lösen: ");
				System.out.println("\n\n\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Every module gets called here.
	 */
	private static void findModule(String module) {
		try {
			switch (Integer.valueOf(module)) {
			case 1 -> TransactionScheduling.init();
			case 2 -> RelConverter.init(LanguageType.SQL, LanguageType.REL);
			case 3 -> RelConverter.init(LanguageType.REL, LanguageType.SQL);
			default -> System.err.println("Unbekanntes Modul!\n");
			}
		} catch (NumberFormatException e) {
			System.err.println("Bitte gib eine valide Zahl an!");
		}
	}

	static Scanner s = new Scanner(System.in);

	/**
	 * Reads a line from the console. When the input is "exit" the program
	 * terminates.
	 * 
	 * @param msg is the message thats asking for the input.
	 * @return the input or an empty string, if an error occured.
	 */
	public static String readInput(String msg) {
		System.out.println(msg);
		byte[] b = s.nextLine().getBytes();
		// Kleiner Fix weil Reader schrecklich sind.
		if (b.length >= 4 && b[0] == b[2] && b[1] == b[3])
			b = Arrays.copyOfRange(b, 2, b.length);
		String res = new String(b);
		if ("exit".equalsIgnoreCase(res)) {
			System.err.println("Beende Program.");
			s.close();
			System.exit(0);
		}
		return res;
	}
}

package relalgebra;

import input.Disassembler;
import input.Disassembler.LanguageType;
import main.Main;

public class RelConverter {

	public static void init(LanguageType from, LanguageType to) {
		System.out.println("\nCONVERTER " + from + " ZU " + to + ".");
		String input = Main.readInput("Gib SQL-Anfrage an:");

		String converted = Disassembler.convert(input, from, to);
		String conBack = Disassembler.convert(converted, to, from);

		System.out.println("Converted: " + converted + "\nAccuracy: " + accuracy(input, conBack) + "%\n");
	}

	private static int accuracy(String s1, String s2) {
		s1 = Disassembler.removeUmlaut(s1.replaceAll("[ |_]", ""));
		s2 = Disassembler.removeUmlaut(s2.replaceAll("[ |_]", ""));
		return (int) Math.round(100 * similarity(s1, s2));
	}

	/**
	 * Calculates the similarity (a number within 0 and 1) between two strings.
	 */
	private static double similarity(String s1, String s2) {
		String longer = s1, shorter = s2;
		if (s1.length() < s2.length()) { // longer should always have greater length
			longer = s2;
			shorter = s1;
		}
		int longerLength = longer.length();
		if (longerLength == 0) {
			return 1.0;
		}
		return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

	}

	private static int editDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				costs[s2.length()] = lastValue;
		}
		return costs[s2.length()];
	}
}

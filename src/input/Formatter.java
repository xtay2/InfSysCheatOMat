package input;

public class Formatter {

	static String txt;

	public static String format(String txt) {
		Formatter.txt = txt;
		removeDoubleSpace();
		removeDoubleBrackets();
		stripBrackets();
		return Formatter.txt.strip();
	}

	/**
	 * Entferne Klammern vorne und hinten:
	 * 
	 * <pre>
	 * Für (a + b) 
	 * Nicht für (a + b) * (a - c)
	 * </pre>
	 */
	private static void stripBrackets() {
		if (txt.charAt(0) != '(')
			return;
		String t = txt.substring(1, txt.length() - 1);
		int b = 0;
		for (char c : t.toCharArray()) {
			if (c == '(')
				b++;
			else if (c == ')') {
				b--;
				if (b == -1)
					return;
			}
		}
		txt = t;
	}

	private static void removeDoubleBrackets() {
		int l;
		do {
			l = txt.length();
			for (int i = 0; i < txt.length(); i++) {
				if (txt.charAt(i) == '(' && txt.charAt(i + 1) == '(') {
					int brack = 0;
					boolean jo = false;
					int j = i + 1;
					for (; j < txt.length(); j++) {
						if (txt.charAt(j) == '(') {
							brack++;
						} else if (txt.charAt(j) == ')') {
							if (brack == 0 && txt.charAt(j - 1) == ')') {
								jo = true;
								break;
							}
							brack--;
						}
					}
					if (jo && txt.charAt(j - 1) == ')') {
						delCharAt(j);
						delCharAt(i);
					}
				}
			}
		} while (l > txt.length());
	}

	private static void removeDoubleSpace() {
		while (txt.contains("  "))
			txt = txt.replace("  ", " ");
	}

	/** Lösche einen Character aus txt an einem vorgegebenen Index. */
	static void delCharAt(int index) {
		txt = txt.substring(0, index) + txt.substring(index + 1);
	}
}

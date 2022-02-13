package reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import input.AliasManager;
import relalgebra.valueholder.abstr.*;
import relalgebra.valueholder.infix.*;
import relalgebra.valueholder.prefix.*;
import relalgebra.valueholder.special.*;
import relalgebra.valueholder.special.Placeholder.Name;
import relalgebra.valueholder.special.Placeholder.Set;

public class RelationalReader {

	static String line;

	/**
	 * Reads the userinput and converts it into an
	 * object-oriented-expression-notation.
	 */
	public static ValueHolder read(String input) {
		line = input.replace(" ", ""); // KEINE LEERZEICHEN
		ValueHolder v = detect();
		return v;
	}

	private static ValueHolder detect() {
		ValueHolder v = switch (line.charAt(0)) {
		case 'ϱ' -> buildRenaming();
		case 'σ' -> buildSelection(false);
		case '(' -> buildBracketed();
		case 'π' -> buildProjection();
		default -> buildSet();
		};
		if (line.isEmpty() || line.charAt(0) == ')')
			return v;
		return recursiveDetect(v);
	}

	/**
	 * Wird gecallt wenn der erste Buchstabe der Zeile ein InfixOperator ist, und
	 * der erste Operand davor durch {@link RelationalReader#detect()} ermittelt
	 * wurde.
	 */
	private static BinaryOperation recursiveDetect(ValueHolder fst) {
		BinaryOperation v = switch (line.charAt(0)) {
		case '×' -> buildCrossProduct(fst);
		case '⋈' -> buildJoin(fst);
		case '∪' -> buildUnion(fst);
		case '÷' -> buildQuotient(fst);
		case '∖' -> buildDifference(fst);
		case '∩' -> buildIntersection(fst);
		default -> throw new AssertionError("No infix-operator detected. Remaining: " + line);
		};
		if (line.isEmpty() || line.charAt(0) == ')')
			return v;
		return recursiveDetect(v);
	}

	// PRIVATE HELPERS /////////////////////////////////////////////////////

	private static void stripFst() {
		if (line.length() == 1)
			line = "";
		else
			line = line.substring(1);
	}

	/**
	 * Returns the index of the last character in this {@link BoolExpression}
	 * (inclusive). Returns 0, when there is no {@link BoolExpression}.
	 */
	private static int findEndOfBool() {
		String temp = line, temp2 = "";
		if (hasBoolExpression(temp)) {
			return 0;
		}

		while (!temp2.equals(temp)) {
			temp2 = temp;
			if (anyIsFirst(temp, List.of('σ', 'ϱ', '(', 'π')))
				break;
			temp = temp.substring(1);
		}
		return line.length() - temp.length();
	}

	private static boolean hasBoolExpression(String temp) {
		if (temp.contains(">") && temp.indexOf("(") > temp.indexOf(">")) {
			return false;
		}
		if (temp.contains("<") && temp.indexOf("(") > temp.indexOf("<")) {
			return false;
		}
		if (temp.contains("=") && temp.indexOf("(") > temp.indexOf("=")) {
			return false;
		}
		if (temp.contains("≠") && temp.indexOf("(") > temp.indexOf("≠")) {
			return false;
		}
		if (temp.contains(">=") && temp.indexOf("(") > temp.indexOf(">=")) {
			return false;
		}
		if (temp.contains("<=") && temp.indexOf("(") > temp.indexOf("<=")) {
			return false;
		}
		return true;

	}

	/**
	 * Returns true if any of the chars in the list is the first char of s.
	 */
	static boolean anyIsFirst(String s, List<Character> chars) {
		return !s.isEmpty() && chars.stream().anyMatch(c -> s.charAt(0) == c);
	}

	/**
	 * Detects, if the start of line is a {@link BoolExpression}. If there is no
	 * {@link BoolExpression}, null gets returned.
	 */
	private static BoolExpression buildBoolExp() {
		int endOfBool = findEndOfBool(); // Start of Content
		if (endOfBool <= 0)
			return null;
		String content = line.substring(0, endOfBool);
		line = line.substring(endOfBool);
		return new BoolExpression(content);
	}

	private static Set buildSet() {
		String name = "";
		while (!line.isEmpty() && Character.isAlphabetic(line.charAt(0))) {
			name += line.charAt(0);
			stripFst();
		}
		return new Set(name);
	}

	private static Arguments buildArgs() {
		String current = "";
		List<Name> args = new ArrayList<>();
		char fst;
		while ((fst = line.charAt(0)) != '(') {
			if (fst == ',') {
				args.add(new Name(current));
				current = "";
			} else
				current += fst;
			stripFst();
		}
		args.add(new Name(current));
		return new Arguments(args);
	}

	private static BracketedExpression buildBracketed() {
		stripFst(); // OpenBrack
		BracketedExpression b = new BracketedExpression(detect());
		stripFst();// CloseBrack
		return b;
	}

	// PRIVATE PREFIX BUILDER /////////////////////////////////////////////////////

	/**
	 * isInProj ist true wenn dieser Ausdruck in einer {@link Projection} steht.
	 * (Default: false)
	 */
	private static Selection buildSelection(boolean isInProj) {
		stripFst(); // Operator
		BoolExpression bool = buildBoolExp();
		stripFst(); // OpenBrack
		ValueHolder content = detect();
		stripFst(); // CloseBrack
		return new Selection(content, bool, isInProj);
	}

	private static Projection buildProjection() {
		stripFst(); // Operator
		Arguments args = buildArgs();
		stripFst(); // OpenBrack
		ValueHolder content;
		if (line.charAt(0) == 'σ')
			content = buildSelection(true);
		else
			content = detect();
		stripFst(); // CloseBrack
		return new Projection(content, args);
	}

	private static Renaming buildRenaming() {
		stripFst(); // Operator
		HashMap<Name, Name> rename = new HashMap<>();
		while (line.charAt(0) != '(') {

			String name = line.substring(0, line.indexOf('→'));
			line = line.substring(line.indexOf('→') + 1);

			final int c = line.indexOf(','), b = line.indexOf('(');
			String alias = line.substring(0, c == -1 ? b : Math.min(b, c));
			line = line.substring(alias.length());
			rename.put(new Name(name), new Name(alias));

			if (line.charAt(0) == ',')
				stripFst();
		}
		stripFst(); // OpenBrack
		ValueHolder content = detect();
		stripFst(); // CloseBrack
		Renaming r = new Renaming(content, rename);
		AliasManager.register(r);
		return r;
	}
	// line.indexOf(',') == -1 ? line.indexOf('(') : Math.min(
	// line.indexOf(','),line.indexOf('(')) ;

	// PRIVATE INFIX BUILDER /////////////////////////////////////////////////////

	/** {@link ThetaJoin} or {@link NaturalJoin} */
	private static BinaryOperation buildJoin(ValueHolder fst) {
		stripFst(); // Operator
		BoolExpression b = buildBoolExp();
		if (b == null)
			return new NaturalJoin(fst, detect());
		return new ThetaJoin(fst, detect(), b);
	}

	private static CrossProduct buildCrossProduct(ValueHolder fst) {
		stripFst(); // Operator
		return new CrossProduct(fst, detect());
	}

	private static Difference buildDifference(ValueHolder fst) {
		stripFst(); // Operator
		return new Difference(fst, detect());
	}

	private static Intersection buildIntersection(ValueHolder fst) {
		stripFst(); // Operator
		return new Intersection(fst, detect());
	}

	private static Quotient buildQuotient(ValueHolder fst) {
		stripFst(); // Operator
		return new Quotient(fst, detect());
	}

	private static Union buildUnion(ValueHolder fst) {
		stripFst(); // Operator
		return new Union(fst, detect());
	}

}
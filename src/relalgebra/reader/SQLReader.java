package relalgebra.reader;

import java.util.ArrayList;
import java.util.List;

import input.AliasManager;
import relalgebra.valueholder.abstr.ValueHolder;
import relalgebra.valueholder.infix.CrossProduct;
import relalgebra.valueholder.infix.Difference;
import relalgebra.valueholder.infix.Intersection;
import relalgebra.valueholder.infix.NaturalJoin;
import relalgebra.valueholder.infix.Quotient;
import relalgebra.valueholder.infix.ThetaJoin;
import relalgebra.valueholder.infix.Union;
import relalgebra.valueholder.prefix.Projection;
import relalgebra.valueholder.prefix.Selection;
import relalgebra.valueholder.special.Arguments;
import relalgebra.valueholder.special.BoolExpression;
import relalgebra.valueholder.special.BracketedExpression;
import relalgebra.valueholder.special.Placeholder.Name;
import relalgebra.valueholder.special.Placeholder.Set;

public class SQLReader {

	static String[] line;

	/**
	 * Reads the userinput and converts it into an
	 * object-oriented-expression-notation.
	 */
	public static ValueHolder read(String input) {
		line = input.split(" ");
		ValueHolder v = detect();
		return v;
	}

	// PRIVATE HELPERS /////////////////////////////////////////////////////

	private static void stripLine(int c) {
		String[] nLine = new String[line.length - c];
		System.arraycopy(line, c, nLine, 0, nLine.length);
		line = nLine;
	}

	/** Returns */
	private static boolean fstIs(String v) {
		return line.length > 0 && line[0].equalsIgnoreCase(v);
	}

	/**
	 * Detects, if the start of line is a {@link BoolExpression}.
	 */
	private static BoolExpression buildBoolExp() {
		String exp = "";
		while (line.length > 0 && !fstIs("WHERE") && !fstIs("(SELECT") && !matchesAnyConnector(line[0])) {
			exp += line[0];

			// Wenn es mit Crossproduct endet.
			if (exp.endsWith(",")) {
				exp = exp.substring(0, exp.length() - 1);
				line[0] = ",";
				break;
			}

			int b = 0;
			for (char c : line[0].toCharArray()) {
				if (c == '(')
					b++;
				else if (c == ')') {
					if (b == 0) {
						stripLine(1); // Entferne Klammer
						break;
					}
					b--;
				}
			}
			stripLine(1);
		}
		return new BoolExpression(exp);
	}

	// PRIVATE BUILDER /////////////////////////////////////////////////////

	private static ValueHolder detect() {
//		System.out.println(Arrays.toString(line));
		return detect(false);
	}

	/**
	 * Ein WHERE muss auf ein gesamtes Kreuzprodukt/Join gecallt werden, sollte
	 * detect also gerade die Teile einer Verbindung zusammenbauen, darf buildwhere
	 * nicht gecallt werden.
	 */
	private static ValueHolder detect(boolean inConnection) {
		ValueHolder a;
		if (line[0].startsWith("(")) {
			a = buildSecondPart(buildBracketed());
			if (!inConnection && line.length > 0 && fstIs("WHERE"))
				return buildWhere(a);
			return a;
		} else if (fstIs("SELECT")) {
			a = buildSelect();
			return a;
		}
		a = buildSet(); // Set, Join, or CrossProduct
		if (!inConnection && line.length > 0 && fstIs("WHERE"))
			return buildWhere(a);
		return a;
	}

	/**
	 * Versuche einen zweiten Teil and v heranzuhängen, wenn das nicht klappt wird v
	 * zurückgegeben.
	 */
	private static ValueHolder buildSecondPart(ValueHolder v) {
		if (line.length > 0) {
			if (line[0].startsWith(",")) {
				stripLine(1);
				v = new CrossProduct(v, detect(true));
			} else if (fstIs("NATURAL"))
				v = buildNatJoin(v);
			else if (fstIs("JOIN"))
				v = buildThetaJoin(v);
			else if (fstIs("INTERSECT"))
				v = buildIntersect(v);
			else if (fstIs("UNION"))
				v = buildUnion(v);
			else if (fstIs("EXCEPT"))
				v = buildExcept(v);
		}
		return v;
	}

	private static BracketedExpression buildBracketed() {
		line[0] = line[0].substring(1); // OpenBrack
		for (int i = 0; i < line.length; i++) {
			String p = line[i];
			int b = 1;
			for (int j = 0; j < p.length(); j++) {
				char c = p.charAt(j);
				if (c == '(')
					b++;
				else if (c == ')') {
					b--;
					if (b == 0) {
						line[i] = p.substring(0, j) + p.substring(j + 1);
						return new BracketedExpression(detect(true));
					}
				}
			}
		}
		throw new IllegalArgumentException("Missing closed bracket.");
	}

	static boolean matchesAnyConnector(String s) {
		return List.of(",", "NATURAL", "JOIN", "ON", "INTERSECT", "UNION", "EXCEPT").stream().anyMatch(e -> e.equalsIgnoreCase(s));
	}

	/** [SET] (ALIAS) (, [SET] (ALIAS)...) */
	private static ValueHolder buildSet() {
		String name = line[0].strip(), alias = null;
		Set s = new Set(name.replaceAll("\\W", ""));
		stripLine(1);
		// Lösche Alias
		if (!name.endsWith(",") && !fstIs("WHERE") && !fstIs("(SELECT") && !fstIs("NATURAL") && !fstIs("JOIN")) {
			alias = line[0].strip();
			stripLine(1);
			AliasManager.register(name.replaceAll("\\W", ""), alias.replaceAll("\\W", ""));
		}
		if (name.endsWith(",") || (alias != null && alias.endsWith(",")))
			return new CrossProduct(s, detect(true));
		return buildSecondPart(s);
	}

	/** [SELECT] (Distinct) [* | a(, b...)] [FROM] [Set] */
	private static Projection buildSelect() {
		stripLine(line[1].equalsIgnoreCase("DISTINCT") ? 2 : 1); // SELECT (DISTINCT)
		Arguments args;
		if (fstIs("*")) {
			stripLine(1); // *
			args = new Arguments();
		} else
			args = buildArgs();
		stripLine(1); // FROM
		return new Projection(detect(), args);
	}

	/** (a(, b...)) */
	private static Arguments buildArgs() {
		List<Name> args = new ArrayList<>();
		while (!fstIs("FROM")) {
			// Strip optionales Komma am Ende
			args.add(new Name(line[0].stripTrailing().endsWith(",") ? line[0].substring(0, line[0].length() - 1) : line[0]));
			stripLine(1);
		}
		return new Arguments(args);
	}

	/** [WHERE] (EXISTS) [BoolExpression] */
	private static ValueHolder buildWhere(ValueHolder a) {
		stripLine(1); // WHERE
		if (fstIs("EXISTS")) {
			stripLine(1);
			return new Quotient(a, detect());
		}
		return new Selection(a, buildBoolExp(), false);
	}

	/** [JOIN] [Set] [ON] [BoolExpression] */
	private static ThetaJoin buildThetaJoin(ValueHolder fst) {
		stripLine(1); // JOIN
		ValueHolder set = detect(true);
		stripLine(1); // ON
		return new ThetaJoin(fst, set, buildBoolExp());
	}

	/** [NATURAL] [JOIN] [Set] */
	private static NaturalJoin buildNatJoin(ValueHolder fst) {
		stripLine(2); // NATURAL JOIN
		return new NaturalJoin(fst, detect(true));
	}

	/** [UNION] [Set] */
	private static Union buildUnion(ValueHolder fst) {
		stripLine(1); // UNION
		return new Union(fst, detect());
	}

	/** [INTERSECT] [Set] */
	private static Intersection buildIntersect(ValueHolder fst) {
		stripLine(1); // INTERSECT
		return new Intersection(fst, detect());
	}

	/** [EXCEPT] [Set] */
	private static Difference buildExcept(ValueHolder fst) {
		stripLine(1); // EXCEPT
		return new Difference(fst, detect());
	}
}

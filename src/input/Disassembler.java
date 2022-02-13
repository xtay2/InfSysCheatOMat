package input;

import reader.RelationalReader;
import reader.SQLReader;

public class Disassembler {

	public enum LanguageType {
		SQL("SQL"), REL("RELATIONAL ALGEBRA");

		final String txt;

		LanguageType(String string) {
			txt = string;
		}

		public static LanguageType fromString(String s) {
			return switch (s.toUpperCase()) {
			case "SQL" -> SQL;
			case "REL" -> REL;
			default -> throw new IllegalArgumentException("Unexpected value: " + s.toUpperCase());
			};
		}

		@Override
		public String toString() {
			return txt;
		}
	}

	public static String convert(String line, LanguageType given, LanguageType expected) {

		line = removeUmlaut(line);

//		System.out.println("Converting " + given + " to " + expected);	
//		System.out.println(line);

		line = line.strip().replaceAll(",(?! )", ", ");

		if (given == LanguageType.REL && expected == LanguageType.SQL)
			line = AliasManager.renameRelToSQL(RelationalReader.read(line).toSQL());
		else if (given == LanguageType.SQL && expected == LanguageType.REL)
			line = AliasManager.renameSQLToRel(SQLReader.read(line).toRel());
		else
			throw new AssertionError("There is no case for " + given + " to " + expected + ".");

		line = Formatter.format(line);

//		System.out.println(line);

		return line;
	}

	public static String removeUmlaut(String line) {
		return line.replaceAll("[ä|Ä]", "ae").replaceAll("[ö|Ö]", "oe").replaceAll("[ü|Ü]", "ue").replaceAll("ß", "ss");
	}

}

package relalgebra.valueholder.special;

import input.Disassembler.LanguageType;
import relalgebra.valueholder.abstr.ValueHolder;

/**
 * Symbole: ∧, ∨, ¬
 */
public class BoolExpression extends ValueHolder {

	private final String exp;

	public BoolExpression(String exp) {
		this.exp = exp.replace("=", " = ");
	}

	@Override
	public String toSQL() {
		return convert(LanguageType.SQL);
	}

	@Override
	public String toRel() {
		return convert(LanguageType.REL);
	}

	/**
	 * Converts the expression from the construtor into the given
	 * {@link LanguageType}. Gets called by the toX() Methods.
	 */
	private String convert(LanguageType to) {
		// @formatter:off
		String[] rel   = { " ∧ ", " ∨ ", " ¬", " ≠ ", ", " };
		String[] sql   = { " AND ", " OR ", " NOT ", " <> ", " AND " };
		// @formatter:on
		
		String temp = exp;
		
		for (int i = 0; i < rel.length; i++) {
			if (LanguageType.SQL == to)
				temp = temp.replace(rel[i].strip(), sql[i]);
			else if (LanguageType.REL == to)
				temp = temp.replace(sql[i].strip(), rel[i]);
		}
		return temp;
	}
}

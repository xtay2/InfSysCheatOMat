package relalgebra.valueholder.infix;

import relalgebra.valueholder.abstr.BinaryOperation;
import relalgebra.valueholder.abstr.ValueHolder;

/**
 * Nimmt zwei {@link ValueHolder}.
 * 
 * Symbol: ⋈
 */
public class NaturalJoin extends BinaryOperation {

	public NaturalJoin(ValueHolder a, ValueHolder b) {
		super(a, b);
	}

	@Override
	public String toSQL() {
		return "(" + a.toSQL() + " NATURAL JOIN " + b.toSQL() + ")";
	}

	@Override
	public String toRel() {
		return "(" + a.toRel() + " ⋈ " + b.toRel() + ")";
	}
}

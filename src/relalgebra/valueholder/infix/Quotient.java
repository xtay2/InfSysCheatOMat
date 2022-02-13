package relalgebra.valueholder.infix;

import relalgebra.valueholder.abstr.BinaryOperation;
import relalgebra.valueholder.abstr.ValueHolder;

/**
 * Nimmt zwei {@link ValueHolder}.
 * 
 * Symbol: ÷
 */
public class Quotient extends BinaryOperation {

	public Quotient(ValueHolder a, ValueHolder b) {
		super(a, b);
	}

	@Override
	public String toSQL() {
		return a.toSQL() + " WHERE EXISTS " + b.toSQL();
	}

	@Override
	public String toRel() {
		return a.toRel() + " ÷ " + b.toRel();
	}
}

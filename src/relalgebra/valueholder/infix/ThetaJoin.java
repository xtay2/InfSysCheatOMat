package relalgebra.valueholder.infix;

import relalgebra.valueholder.abstr.BinaryOperation;
import relalgebra.valueholder.abstr.ValueHolder;
import relalgebra.valueholder.special.BoolExpression;

/**
 * Nimmt zwei {@link ValueHolder}.
 * 
 * Symbol: ⋈
 */
public class ThetaJoin extends BinaryOperation {
	
	private final BoolExpression exp;

	public ThetaJoin(ValueHolder a, ValueHolder b, BoolExpression exp) {
		super(a, b);
		this.exp = exp;
	}

	@Override
	public String toSQL() {
		return "(" + a.toSQL() + " JOIN " + b.toSQL() + " ON " + exp.toSQL() + ")";
	}

	@Override
	public String toRel() {
		return "(" +a.toRel() + " ⋈_" + exp.toRel() + "_ " + b.toRel() + ")";
	}
}

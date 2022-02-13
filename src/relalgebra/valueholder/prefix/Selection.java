package relalgebra.valueholder.prefix;

import relalgebra.valueholder.abstr.UnaryOperation;
import relalgebra.valueholder.abstr.ValueHolder;
import relalgebra.valueholder.special.BoolExpression;

/**
 * Besteht aus einer {@link BoolExpression} und einem {@link ValueHolder}.
 * 
 * Symbol: σ
 */
public class Selection extends UnaryOperation {

	private final BoolExpression exp;

	private final boolean isInProj;

	public Selection(ValueHolder content, BoolExpression exp, boolean isInProj) {
		super(content);
		this.exp = exp;
		this.isInProj = isInProj;
	}

	@Override
	public String toSQL() {
		return (isInProj ? "" : "(SELECT * FROM ") + content.toSQL() + " WHERE " + exp.toSQL() + (isInProj ? "" : ")");
	}

	@Override
	public String toRel() {
		return "σ_" + exp.toRel() + "_(" + content.toRel() + ")";
	}

}

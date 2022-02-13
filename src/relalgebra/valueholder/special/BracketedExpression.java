package relalgebra.valueholder.special;

import relalgebra.valueholder.abstr.UnaryOperation;
import relalgebra.valueholder.abstr.ValueHolder;

public class BracketedExpression extends UnaryOperation {

	
	public BracketedExpression(ValueHolder content) {
		super(content);
	}

	@Override
	public String toSQL() {
		return "(" + content.toSQL() + ")";
	}

	@Override
	public String toRel() {
		return  "(" + content.toRel() + ")";
	}

}

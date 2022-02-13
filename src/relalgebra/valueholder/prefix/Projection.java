package relalgebra.valueholder.prefix;

import relalgebra.valueholder.abstr.UnaryOperation;
import relalgebra.valueholder.abstr.ValueHolder;
import relalgebra.valueholder.special.Arguments;

/** 
 * Besteht aus beliebig vielen {@link Arguments} und einem {@link ValueHolder}.
 * 
 * Symbol: π
 */
public class Projection extends UnaryOperation {

	private final Arguments args;

	public Projection(ValueHolder content, Arguments args) {
		super(content);
		this.args = args;
		if (args == null)
			throw new AssertionError("Names should rather be empty than null.");
	}

	@Override
	public String toSQL() {
		if (args.isEmpty())
			return "(SELECT DISTINCT * FROM " + content.toSQL() + ")";
		return "(SELECT DISTINCT " + args.toSQL() + " FROM " + content.toSQL() + ")";
	}

	@Override
	public String toRel() {
		if (args.isEmpty())
			return content.toRel();
		return "π_" + args.toRel() + "_(" + content.toRel() + ")";
	}
}

package relalgebra.valueholder.abstr;

/**
 * Alle Operationen, die auf nur einen {@link ValueHolder} angewendet werden.
 */
public abstract class BinaryOperation extends ValueHolder {

	protected final ValueHolder a, b;

	protected BinaryOperation(ValueHolder a, ValueHolder b) {
		this.a = a;
		this.b = b;
		if (a == null || b == null)
			throw new AssertionError("Neither of a or b can be null.");
	}

	@Override
	public final String debug(int i) {
		return "   ".repeat(i) + getClass().getSimpleName() + "\n" + a.debug(i + 1) + "\n" + b.debug(i + 1);
	}

}

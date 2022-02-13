package relalgebra.valueholder.abstr;

/**
 * Alle Operationen, die auf zwei {@link ValueHolder} angewendet werden.
 */
public abstract class UnaryOperation extends ValueHolder {

	protected final ValueHolder content;

	protected UnaryOperation(ValueHolder content) {
		this.content = content;
		if (content == null)
			throw new AssertionError("Content cannot be null.");
		if (this.getClass() == content.getClass())
			throw new AssertionError(content + " and " + this + " should get merged to one operation.");
	}

	@Override
	public final String debug(int i) {
		return "   ".repeat(i) + getClass().getSimpleName() + "\n" + content.debug(i + 1);
	}

}

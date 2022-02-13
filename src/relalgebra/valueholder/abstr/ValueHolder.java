package relalgebra.valueholder.abstr;

public abstract class ValueHolder {

	/** Gibt eine textuelle SQL-Code-Representation zurück. */
	public abstract String toSQL();

	/** Gibt eine textuelle Representation in Relationaler Algera zurück. */
	public abstract String toRel();

	public String debug(int i) {
		return "   ".repeat(i) + getClass().getSimpleName();
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Use one of the other toX()-methods instead.");
	}

}
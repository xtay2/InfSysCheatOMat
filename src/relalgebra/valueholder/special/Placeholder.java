package relalgebra.valueholder.special;

import relalgebra.valueholder.abstr.ValueHolder;

public abstract class Placeholder extends ValueHolder {

	/** Ist ein {@link Placeholder} für eine Menge. */
	public static class Set extends Placeholder {
		public Set(String name) {
			super(name);
		}
	}

	/** Ist ein {@link Placeholder} für einen Zeilennamen. */
	public static class Name extends Placeholder {
		public Name(String name) {
			super(name);
		}
	}

	private final String name;

	public Placeholder(String name) {
		this.name = name;
		if (name == null)
			throw new AssertionError("Name cannot be null.");
	}

	@Override
	public String toSQL() {
		return toString();
	}

	@Override
	public String toRel() {
		return toString();
	}

	/** Returns the raw name */
	@Override
	public String toString() {
		return name;
	}
}

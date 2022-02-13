package relalgebra.valueholder.special;

import java.util.ArrayList;
import java.util.List;

import relalgebra.valueholder.abstr.ValueHolder;
import relalgebra.valueholder.special.Placeholder.Name;

public class Arguments extends ValueHolder {

	private final List<Name> args;

	/** No arguments */
	public Arguments() {
		this.args = new ArrayList<>();
	}

	public Arguments(List<Name> args) {
		this.args = args;
	}

	public boolean isEmpty() {
		return args.isEmpty();
	}

	public int size() {
		return args.size();
	}

	/**
	 * Gibt eine Aufzählungsrepräsentation zurück, wie z.B:
	 * 
	 * A, B, C
	 */
	private String listArgs() {
		String listStr = args.toString();
		return listStr.substring(1, listStr.length() - 1);
	}

	@Override
	public String toSQL() {
		return listArgs();
	}

	@Override
	public String toRel() {
		return listArgs();
	}

	public static Arguments merge(Arguments a, Arguments b) {
		List<Name> args = new ArrayList<>();
		args.addAll(a.args);
		args.addAll(b.args);
		return new Arguments(args);
	}
}

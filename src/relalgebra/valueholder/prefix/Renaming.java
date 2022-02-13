package relalgebra.valueholder.prefix;

import java.util.HashMap;
import java.util.Map;

import relalgebra.valueholder.abstr.UnaryOperation;
import relalgebra.valueholder.abstr.ValueHolder;
import relalgebra.valueholder.special.Placeholder.Name;
import relalgebra.valueholder.special.Placeholder.Set;

/**
 * Besteht aus einem {@link ValueHolder} einem alten und einem neuen
 * {@link Name}.
 * 
 * Symbole: ϱ, →
 */
public class Renaming extends UnaryOperation {

	// Name -> NewName
	private HashMap<Name, Name> changes;

	public Renaming(ValueHolder content, HashMap<Name, Name> changes) {
		super(content);
		this.changes = changes;
	}

	@Override
	public String toSQL() {
		return content.toSQL();
	}

	@Override
	public String toRel() {
		String c = "";
		for (Name k : changes.keySet())
			c += k + " → " + changes.get(k) + ", ";
		return "ϱ_" + c.substring(0, c.length() - 2) + "_(" + content + ")";
	}

	public String getSetName() {
		if (content instanceof Set s)
			return s.toString();
		throw new AssertionError("This isn't implemented yet.");
	}

	public Map<Name, Name> getChanges() {
		return changes;
	}
}

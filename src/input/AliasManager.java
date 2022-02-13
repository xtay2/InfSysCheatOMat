package input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import relalgebra.valueholder.prefix.Renaming;
import relalgebra.valueholder.special.Placeholder.Name;

public abstract class AliasManager {

	// NAME -> ALIAS (SQL)
	private static Map<String, String> aMap = new HashMap<>();

	// RENAMING (REL)
	private static List<Renaming> rList = new ArrayList<>();

	/**
	 * Ersetzt ganz zum Schluss alle Vorkommnisse von
	 * 
	 * <pre>
	 * "Set" zu "Set alias" 
	 * "NewName" zu "alias.OldName"
	 * </pre>
	 * 
	 * Diese Methode nimmt einen fast vollständig konvertierten "Rel to SQL"-String.
	 * Wird gecallt in {@link Disassembler#convert()}.
	 */
	public static String renameRelToSQL(String input) {
		for (Renaming r : rList) {
			String setName = r.getSetName();
			String alias = generateAlias(setName);

			Map<Name, Name> changes = r.getChanges();
			for (Name key : changes.keySet()) {
				String oldName = key.toString(), newName = changes.get(key).toString();
				input = input.replace(newName, alias + "." + oldName);
			}
			input = input.replaceFirst(setName, setName + " " + alias);
		}
		return input;
	}

	private static String generateAlias(String setName) {
		return setName.toLowerCase().substring(0, Math.min(setName.length(), 3));
	}

	/**
	 * Ersetzt ganz zum Schluss alle Vorkommnisse von "alias.Sub" zu "Name.Sub".
	 * 
	 * Diese Methode nimmt einen fast vollständig konvertierten "SQL to Rel"-String.
	 * Wird gecallt in {@link Disassembler#convert()}.
	 */
	public static String renameSQLToRel(String input) {
		for (String key : aMap.keySet())
			input = input.replace(aMap.get(key) + ".", key + ".");
		aMap.clear();
		return input;
	}

	public static void register(String name, String alias) {
		if (name.equals(alias))
			throw new IllegalArgumentException("Name cannot be equal to alias, both were " + name + ".");
		if (aMap.containsValue(alias)) {
			String key = aMap.keySet().stream().filter(k -> aMap.get(k).equals(alias)).findFirst().get();
			throw new IllegalArgumentException(key.equals(name) ? name + " was already mapped to " + alias
					: "One alias cannot be mapped to different names. \n" + alias + " was mapped to " + key + ", tried to map it to "
							+ name);
		}
		if (aMap.containsKey(name)) {
			throw new AssertionError("This isn't implemented yet.");
		} else
			aMap.put(name, alias);
	}

	public static void register(Renaming renaming) {
		if (!rList.contains(renaming))
			rList.add(renaming);
		else
			throw new IllegalArgumentException("The renaming \"" + renaming.toRel() + "\" was already registered.");
	}
}

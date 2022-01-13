package transactions.scheduling.objects;

public class Action {

	/* Defines a read, or write Action. */
	public enum Type {
		READ, WRITE;
		
		@Override
		public String toString() {
			return this == READ ? "r" : "w";
		}
	}

	public final Type myType;
	public final int myTransactionID;
	public final char myObject;

	/**
	 * Eine Aktion ist etwas wie zum Beispiel r1(x). Sie besteht aus:
	 * 
	 * @param type          read oder write (Beispiel: r)
	 * @param transactionID ist die Nummer der Transaktion. (Beispiel: 1)
	 * @param object        dem Objekt, dass bearbeitet wird, identifiziert durch
	 *                      einen Buchstaben. (Beispiel: x)
	 */
	public Action(Type type, int transactionID, char object) {
		this.myType = type;
		this.myTransactionID = transactionID;
		this.myObject = object;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Action a)
			return myType == a.myType && myTransactionID == a.myTransactionID && myObject == a.myObject;
		return false;
	}

	@Override
	public String toString() {
		return myType.toString() + subscript(String.valueOf(myTransactionID)) + "(" + myObject + ")";
	}

	/**
	 * Subscripts all numbers in a string.
	 */
	public static String subscript(String str) {
		str = str.replaceAll("0", "₀");
		str = str.replaceAll("1", "₁");
		str = str.replaceAll("2", "₂");
		str = str.replaceAll("3", "₃");
		str = str.replaceAll("4", "₄");
		str = str.replaceAll("5", "₅");
		str = str.replaceAll("6", "₆");
		str = str.replaceAll("7", "₇");
		str = str.replaceAll("8", "₈");
		str = str.replaceAll("9", "₉");
		return str;
	}
}

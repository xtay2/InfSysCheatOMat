package transactions.scheduling.objects;

public record Dependency(Action a, Action b) implements Comparable<Dependency> {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Dependency d)
			return a.equals(d.a) && b.equals(d.b);
		return false;
	}

	@Override
	public String toString() {
		return "<" + a.myType + b.myType + a.myTransactionID + "," + b.myTransactionID + "(" + a.myObject + ")>";
	}

	@Override
	public int compareTo(Dependency d) {
		// Nach Objekt vergleichen.
		if (a.myObject == d.a.myObject) {
			if (b.myObject == d.b.myObject) {
				// Nach ID vergleichen.
				if (a.myTransactionID == d.a.myTransactionID) {
					if (b.myTransactionID == d.b.myTransactionID) {
						// Nach Typ vergleichen.
						if (a.myType == d.a.myType) {
							if (b.myType == d.b.myType) {
								return 0;
							}
							return b.myType.compareTo(d.b.myType);
						}
						return a.myType.compareTo(d.a.myType);
					}
					return b.myTransactionID < d.b.myTransactionID ? -1 : 1;
				}
				return a.myTransactionID < d.a.myTransactionID ? -1 : 1;
			}
			return b.myObject < d.b.myObject ? -1 : 1;
		}
		return a.myObject < d.a.myObject ? -1 : 1;
	}
}

package t2;

public enum Gender {

	female, male;
	
	public String toString() {
		if (this == female) {
			return "Female";
		} else {
			return "Male";
		}
	}
}
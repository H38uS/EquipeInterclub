package mosioj.equipesInterclub.swimmer;

/**
 * Possible master categories to associate to a swimmer.
 * 
 * @author Jordan Mosio
 *
 */
public enum Category {

	C1, C2, C3, C4, C5, C6, C7, C8, C9, C10, C11, C12, C13, C14;
	
	/**
	 * 
	 * @param age
	 * @return The category corresponding to this age.
	 */
	public static Category readCategory(int age) {
		
		if (age < 25)
			return C1;
		
		int start = 30;
		for (int i = 0; i < values().length; i++) {
			if (age < start) {
				return values()[i];
			}
			start += 5;
		}
		
		return C14;
	}
}
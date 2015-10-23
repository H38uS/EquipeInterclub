package mosioj.equipesInterclub.swimmer;

/**
 * Une nage.
 * 
 * @author Jordan Mosio
 *
 */
public enum Race {

	/** Pap. */
	_50PAP(10, "50 Papillon"), _100PAP(100, "100 Papillon"), //

	/** Dos. */
	_50DOS(20, "50 Dos"), _100DOS(80, "100 Dos"), //

	/** Brasse. */
	_50BRASSE(30, "50 Brasse"), _100BRASSE(150, "100 Brasse"), //
	
	/** NL. */
	_50NL(10, "50 Nage Libre"), _100NL(80, "100 Nage Libre"), _400NL(400, "400 Nage Libre"), //
	
	/** 4N. */
	_2004N(150, "200 4 Nages");

	/**
	 * The base malus to apply, in milliseconds.
	 */
	private final int malus;
	
	private final String defaultRepresentation;
	
	/**
	 * Class constructor.
	 * 
	 * @param pMalus
	 * @param pString
	 */
	private Race(int pMalus, String pString) {
		malus = pMalus;
		defaultRepresentation = pString;
	}
	
	/**
	 * 
	 * @return The base malus to apply, in milliseconds.
	 */
	public int getMalus() {
		return malus;
	}
	
	@Override
	public String toString() {
		return name().substring(1);
	}
	
	/**
	 * 
	 * @param value
	 * @return The race equivalent of this string.
	 */
	public static Race getRace(String value) {

		for (Race race : values()) {
			if (value.startsWith(race.defaultRepresentation)) {
				return race;
			}
		}
		
		return null;
	}
}

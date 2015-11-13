package mosioj.equipesInterclub.swimmer;

/**
 * Une nageuse.
 * 
 * @author Jordan Mosio
 * 
 */
public class Nageuse extends Swimmer {

	/**
	 * Class constructor.
	 * 
	 * @param pSurname Le nom de famille.
	 * @param pName Le prénom.
	 * @param race La nage.
	 * @param points Les points de cette nage.
	 */
	public Nageuse(final String pSurname, final String pName, final Race race, final int points) {
		super(pSurname, pName, race, points);
	}

	/**
	 * Duplicate a swimmer without any sex to a female.
	 * 
	 * @param swimmer
	 */
	public Nageuse(Swimmer swimmer) {
		super(swimmer.surname, swimmer.name);
		races.putAll(swimmer.races);
	}

	/**
	 * 
	 * @param familyName
	 * @param name
	 */
	public Nageuse(String familyName, String name) {
		super(familyName, name);
	}

	@Override
	public boolean isAWoman() {
		return true;
	}

	@Override
	public boolean isAMan() {
		return false;
	}
}

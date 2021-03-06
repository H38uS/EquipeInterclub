package mosioj.equipesInterclub.swimmer;

/**
 * Un nageur.
 * 
 * @author Jordan Mosio
 * 
 */
public class Nageur extends Swimmer {

	/**
	 * Class constructor.
	 * 
	 * @param pSurname Le nom de famille.
	 * @param pName Le prénom.
	 * @param race La nage.
	 * @param points Les points de cette nage.
	 */
	public Nageur(final String pSurname, final String pName, final Race race, final int points) {
		super(pSurname, pName, race, points);
	}
	

	/**
	 * Duplicate a swimmer without any sex to a male.
	 * 
	 * @param swimmer
	 */
	public Nageur(Swimmer swimmer) {
		super(swimmer.surname, swimmer.name);
		races.putAll(swimmer.races);
		times.putAll(swimmer.times);
	}

	/**
	 * 
	 * @param familyName
	 * @param name
	 */
	public Nageur(String familyName, String name) {
		super(familyName, name);
	}


	@Override
	public boolean isAWoman() {
		return false;
	}

	@Override
	public boolean isAMan() {
		return true;
	}

}

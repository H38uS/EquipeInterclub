package mosioj.equipesInterclub.swimmer;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Un nageur, avec les courses qu'il peut faire (et ses points).
 * 
 * @author Jordan Mosio
 * 
 */
public class Swimmer extends SportMember {

	/**
	 * Les nages du nageur, avec les points associés. perfs, à voir.
	 */
	protected final Map<Race, Integer> races = new HashMap<Race, Integer>();

	/**
	 * Le nom de famille.
	 */
	public final String surname;

	/**
	 * Le prénom.
	 */
	public final String name;

	/**
	 * Le hash code du nageur.
	 */
	private final int hash;

	/**
	 * Class constructor.
	 * 
	 * @param pSurname Le nom de famille.
	 * @param pName Le prénom.
	 * @param race La nage.
	 * @param points Les points de cette nage.
	 */
	public Swimmer(final String pSurname, final String pName, final Race race, final int points) {
		races.put(race, points);
		surname = pSurname;
		name = pName;
		hash = (surname + name).hashCode();
	}

	/**
	 * Class constructor.
	 * 
	 * @param pSurname Le nom de famille.
	 * @param pName Le prénom.
	 */
	public Swimmer(final String pSurname, final String pName) {
		surname = pSurname;
		name = pName;
		hash = (surname + name).hashCode();
	}

	/**
	 * Ajoute une nage.
	 * 
	 * @param race La nage.
	 * @param points Les points que le nageur fait à cette nage.
	 */
	public void addRace(Race race, int points) {
		races.put(race, points);
	}

	/**
	 * 
	 * @return True ssi il s'agit d'une femme.
	 */
	public boolean isAWoman() {
		return false;
	}

	/**
	 * 
	 * @return True ssi il s'agit d'un homme.
	 */
	public boolean isAMan() {
		return false;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0} {1}", name, surname);
	}

	@Override
	public String getFFNToString() {
		String escapedName = name;
		if (name.indexOf("è") >= 0)
			escapedName = name.substring(0, name.indexOf("è"));
		return MessageFormat.format("{0}%20{1}", surname, escapedName);
	}

	/**
	 * 
	 * @param race La nage.
	 * @return Les points que le nageur fait.
	 */
	public int getPoints(Race race) {
		if (races.containsKey(race)) {
			return races.get(race);
		}
		return 900; // valeurs par défaut
	}

	@Override
	public int hashCode() {
		return hash;
	}

	/**
	 * 
	 * @return Le maximum de points que pourrait faire ce nageur.
	 */
	public int getMaxPoints() {
		int maxPoints = 0;
		for (Race race : races.keySet()) {
			Integer points = races.get(race);
			if (points > maxPoints) {
				maxPoints = points;
			}
		}
		return maxPoints;
	}

}

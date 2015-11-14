package mosioj.equipesInterclub.swimmer;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import mosioj.equipesInterclub.solution.TimeToPointsConverter;
import mosioj.equipesInterclub.swimmer.performance.Time;

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
	 * Les nages du nageur, avec les temps associés.
	 */
	protected final Map<Race, Time> times = new HashMap<Race, Time>();

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
		this(pSurname, pName);
		races.put(race, points);
	}

	/**
	 * Class constructor.
	 * 
	 * @param pSurname Le nom de famille.
	 * @param pName Le prénom.
	 */
	public Swimmer(final String pSurname, final String pName) {

		if (pSurname == null || pSurname.isEmpty()) {
			throw new IllegalStateException("Le nom de famille ne peut pas être null !");
		}
		if (pName == null || pName.isEmpty()) {
			throw new IllegalStateException("Le prénom ne peut pas être null !");
		}

		surname = pSurname;
		name = pName;
		hash = (surname + name).hashCode();
	}

	/**
	 * Ajoute une nage.
	 * 
	 * @param race La nage.
	 * @param time Le temps que le nageur fait à cette nage.
	 */
	public void addRace(Race race, Time time) {
		TimeToPointsConverter conveter = TimeToPointsConverter.getInstance();
		int points = conveter.getPoints(race, time, getMemberCategory(false), isAWoman());
		races.put(race, points);
		times.put(race, time);
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
		escapedName = escapedName.replaceAll("è", "e");
		escapedName = escapedName.replaceAll("é", "e");
		escapedName = escapedName.replaceAll("ï", "i");
		String escapedSurname = surname;
		escapedSurname = escapedSurname.replaceAll("é", "e");
		escapedSurname = escapedSurname.replaceAll(" ", "%20");
		return MessageFormat.format("{0}%20{1}", escapedSurname, escapedName);
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
		return 900; // valeur par défaut
	}

	/**
	 * 
	 * @param race
	 * @return Le temps que le nageur fait à cette nage.
	 */
	public Time getTime(Race race) {
		if (times.containsKey(race)) {
			return times.get(race);
		}
		return null;
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

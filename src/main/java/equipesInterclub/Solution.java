package equipesInterclub;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import equipesInterclub.swimmer.Swimmer;

/**
 * Une solution à ce problème.
 * 
 * @author Jordan Mosio
 * 
 */
public class Solution {

	/**
	 * Les nageurs de cette solution.
	 */
	private Swimmer[] swimmers;

	/**
	 * Les nages nécessaire.
	 */
	private Map<Race, Integer> races = new HashMap<Race, Integer>();

	/**
	 * Class constructor.
	 * 
	 * @param pSwimmers Les nageurs.
	 */
	public Solution(final List<Swimmer> pSwimmers) {

		races.put(Race._400NL, 0);
		races.put(Race._100PAP, 1);
		races.put(Race._50DOS, 2);
		races.put(Race._100BRASSE, 3);
		races.put(Race._50NL, 4);
		races.put(Race._50PAP, 5);
		races.put(Race._100DOS, 6);
		races.put(Race._50BRASSE, 7);
		races.put(Race._100NL, 8);
		races.put(Race._2004N, 9);

		swimmers = new Swimmer[races.keySet().size()];

		// On y met deux filles
		int nbWoman = 0;
		Swimmer[] woman = new Swimmer[2];
		for (Swimmer swimmer : pSwimmers) {
			if (nbWoman == 2) {
				break;
			}
			if (swimmer.isAWoman()) {
				swimmers[nbWoman] = swimmer;
				woman[nbWoman] = swimmer;
				nbWoman++;
			}
		}

		// Et on y complète
		for (int i = 2; i < races.size(); i++) {
			for (Swimmer swimmer : pSwimmers) {
				if (containsSwimmer(swimmer)) {
					continue;
				}
				swimmers[i] = swimmer;
			}
		}
	}

	/**
	 * Class constructor.
	 * 
	 * @param pRaces Les nages.
	 * @param pSwimmers Les nageurs.
	 */
	private Solution(final Map<Race, Integer> pRaces, final Swimmer[] pSwimmers) {
		races = pRaces;
		swimmers = new Swimmer[pSwimmers.length];
		System.arraycopy(pSwimmers, 0, swimmers, 0, pSwimmers.length);
	}

	/**
	 * 
	 * @return True ssi il y a au moins deux filles.
	 */
	public boolean isValid() {
		int nbWoman = 0;
		for (Swimmer swimmer : swimmers) {
			if (swimmer != null && swimmer.isAWoman()) {
				nbWoman++;
			}
		}
		return nbWoman > 1;
	}

	/**
	 * 
	 * @param swimmer Le nageur recherché.
	 * @return True ssi le nageur fait déjà parti de la solution.
	 */
	public boolean containsSwimmer(Swimmer swimmer) {
		for (int i = 0; i < swimmers.length; i++) {
			if (swimmer.equals(swimmers[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return Le nombre de nage possible.
	 */
	public int getNbRaces() {
		return swimmers.length;
	}

	/**
	 * Affiche la solution.
	 * 
	 * @param input Les données initiales.
	 */
	public void print(List<Swimmer> input) {

		
		int points = 0;
		List<StringBuilder> lines = new ArrayList<StringBuilder>();

		// Récupération des points sur chaque nage
		for (Race race : races.keySet()) {

			Swimmer swimmer = swimmers[races.get(race)];
			int pointsRace = swimmer.getPoints(race);
			points += pointsRace;

			StringBuilder message = new StringBuilder();
			message.append(race);
			message.append(" : ");
			message.append(swimmer);
			message.append(" (");
			message.append(pointsRace);
			message.append(" points) ");
			lines.add(message);
		}

		// Alignement
		align(lines);

		// Récupération du maximum possible de chaque nageur
		int index = 0;
		for (Race race : races.keySet()) {

			Swimmer swimmer = swimmers[races.get(race)];

			int point = swimmer.getMaxPoints();
			int pointsRace = swimmer.getPoints(race);

			lines.get(index).append("- Max du nageur ");
			lines.get(index).append(point);
			lines.get(index).append(" points");
			if (pointsRace == point) {
				lines.get(index).append(" (atteint)");
			}
			lines.get(index).append(", ");
			index++;
		}

		// Alignement
		align(lines);
		
		// Récupération du max possible sur la nage
		index = 0;
		for (Race race : races.keySet()) {

			int maxPointsRace = 0;
			for (Swimmer swimmer : input) {
				int pointsRace = swimmer.getPoints(race);
				if (pointsRace > maxPointsRace) {
					maxPointsRace = pointsRace;
				}
			}
			
			Swimmer swimmer = swimmers[races.get(race)];
			int point = swimmer.getMaxPoints();

			lines.get(index).append("Max sur la nage ");
			lines.get(index).append(maxPointsRace);
			if (maxPointsRace == point) {
				lines.get(index).append(" (atteint)");
			}
			lines.get(index).append(".");
			index++;
		}

		// Affichage
		for (StringBuilder line : lines) {
			System.out.println(line);
		}
		System.out.println(MessageFormat.format("==> Points totaux : {0}", points));
	}

	/**
	 * Aligne les lignes en ajoutant des espaces à la fin.
	 * 
	 * @param lines Les lignes.
	 */
    private void align(List<StringBuilder> lines) {
	    int maxLength = 0;
		for (StringBuilder line : lines) {
			int length = line.length();
			if (length > maxLength) {
				maxLength = length;
			}
		}
		for (StringBuilder line : lines) {
			int initialLength = line.length();
			for (int i = 0; i < maxLength - initialLength; i++) {
				line.append(" ");
			}
		}
    }

	/**
	 * 
	 * @return Les points de cette solution.
	 */
	public int getPoints() {
		int points = 0;
		for (Race race : races.keySet()) {
			points += swimmers[races.get(race)].getPoints(race);
		}
		return points;
	}

	/**
	 * 
	 * @param swimmer Le nageur.
	 * @param i Le numéro de la nage.
	 * @return Une nouvelle solution, en remplaçant le nageur par celui en paramètre, à la position
	 *         i.
	 */
	public Solution deriveSol(Swimmer swimmer, int i) {
		Solution derive = new Solution(races, swimmers);
		derive.swimmers[i] = swimmer;
		return derive;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(swimmers);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Solution))
			return false;
		Solution other = (Solution) obj;
		if (!Arrays.equals(swimmers, other.swimmers))
			return false;
		return true;
	}
}

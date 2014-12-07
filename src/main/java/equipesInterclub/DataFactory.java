package equipesInterclub;

import java.util.ArrayList;
import java.util.List;

import equipesInterclub.swimmer.Nageur;
import equipesInterclub.swimmer.Nageuse;
import equipesInterclub.swimmer.Sexe;
import equipesInterclub.swimmer.Swimmer;

/**
 * Crée les différentes données du problème depuis le Excel.
 * 
 * @author Jordan Mosio
 * 
 */
public final class DataFactory {

	/**
	 * Interdit.
	 */
	private DataFactory() {
	}

	/**
	 * 
	 * @param input Les lignes en input.
	 * @return La liste des nageurs disponibles.
	 */
	public static List<Swimmer> getSwimmers(List<List<String>> input) {

		List<Swimmer> swimmers = new ArrayList<Swimmer>();
		Swimmer current = null;
		for (List<String> line : input) {

			// Entête => on passe
			String surname = line.get(0);
			if ("NOM".equals(surname)) {
				continue;
			}

			// On complete l'information sur le nageur
			if (line.size() == 2) {
				current.addRace(Race.valueOf("_" + line.get(0)), Integer.valueOf(line.get(1)));
			}

			// Nouveau nageur
			if (line.size() == 5) {
				String name = line.get(1);
				Race race = Race.valueOf("_" + line.get(2));
				int points = Integer.valueOf(line.get(3));
				current = Sexe.valueOf(line.get(4)) == Sexe.F ? new Nageuse(surname,
				                                                            name,
				                                                            race,
				                                                            points)
				        : new Nageur(surname,
				                     name,
				                     race,
				                     points);
				swimmers.add(current);
			}
		}

		return swimmers;
	}
}

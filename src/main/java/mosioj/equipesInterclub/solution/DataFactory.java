package mosioj.equipesInterclub.solution;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import mosioj.equipesInterclub.swimmer.Nageur;
import mosioj.equipesInterclub.swimmer.Nageuse;
import mosioj.equipesInterclub.swimmer.Race;
import mosioj.equipesInterclub.swimmer.Sexe;
import mosioj.equipesInterclub.swimmer.Swimmer;
import mosioj.equipesInterclub.swimmer.performance.Time;

/**
 * Crée les différentes données du problème depuis le Excel.
 * 
 * @author Jordan Mosio
 * 
 */
public final class DataFactory {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(DataFactory.class);

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

		LOGGER.info("Reading swimmers...");
		List<Swimmer> swimmers = new ArrayList<Swimmer>();
		Swimmer current = null;
		for (List<String> line : input) {

			LOGGER.trace(line);

			// Entête => on passe
			String familyName = line.get(0);
			if ("NOM".equals(familyName)) {
				continue;
			}

			if (line.size() != 6) {
				LOGGER.debug("Line has not the right size, skipping it. Row is: " + line);
				continue;
			}

			// On complete l'information sur le nageur
			if (familyName == null) {
				current.addRace(Race.valueOf("_" + line.get(2)), new Time(line.get(5)));
			} else {
				// Nouveau nageur
				String name = line.get(1);
				Race race = Race.valueOf("_" + line.get(2));
				Time time = new Time(line.get(5));
				Sexe sex = Sexe.valueOf(line.get(4));
				current = sex == Sexe.F ? new Nageuse(familyName, name) : new Nageur(familyName, name);
				current.birthYear = (int) Double.parseDouble(line.get(3));
				current.addRace(race, time);
				swimmers.add(current);
				LOGGER.info("Found swimmer: " + current);
			}
		}
		LOGGER.info("Done !");

		return swimmers;
	}
}

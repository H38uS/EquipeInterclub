package equipesInterclub;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import equipesInterclub.swimmer.Swimmer;
import equipesInterclub.util.ExcelReader;

/**
 * Construit la meilleure solution !!!
 * 
 * @author Jordan Mosio
 * 
 */
public class SolutionBuilder {

	/**
	 * La profondeur maximale de recherche.
	 */
	private static final int MAX_DEPTH = 6;

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(SolutionBuilder.class);

	/**
	 * Les nageurs disponibles.
	 */
	private List<Swimmer> swimmers;

	/**
	 * La meilleure solution.
	 */
	private Solution bestSolution;

	/**
	 * Les points de la meilleure solution.
	 */
	private int bestPoints = 0;

	/**
	 * L'historique des solutions déjà explorée.
	 */
	private final Set<Solution> seen = new HashSet<Solution>();
	
	/**
	 * Class constructor.
	 * 
	 * @param data Les données du problème.
	 * @throws Exception Si le fichier n'existe pas.
	 */
	public SolutionBuilder(final File data) throws Exception {
		LOGGER.info("Construction de la solution initiale...");
		swimmers = DataFactory.getSwimmers(ExcelReader.readLines(data));
		bestSolution = new Solution(swimmers);
		LOGGER.info("Ok. Solution initiale: ");
		bestSolution.print(swimmers);
	}

	/**
	 * 
	 * @return Les données de ce problème.
	 */
	public List<Swimmer> getSwimmers() {
		return swimmers;
	}
	
	/**
	 * 
	 * @return La meilleure solution.
	 */
	public Solution search() {

		LOGGER.info("Recherche de la meilleure composition...");
		for (int i = 0; i < bestSolution.getNbRaces(); i++) {

			for (Swimmer swimmer : swimmers) {

				// Le nageur est déjà présent
				if (bestSolution.containsSwimmer(swimmer)) {
					continue;
				}

				// On tente avec ce nageur sur cette nage !
				Solution solution = bestSolution.deriveSol(swimmer, i);

				// Recherche en profondeur... On fait encore quelques essais derrière :)
				solution = search(solution, 0);
				int points = solution.getPoints();

				// Est-ce qu'elle est mieux ?
				if (solution.isValid() && points > bestPoints) {
					LOGGER.debug(MessageFormat.format("Nouvelle solution trouvée ! Points : {0} / BestPoints : {1}.",
					                                  points,
					                                  bestPoints));
					bestSolution = solution;
					bestPoints = points;
				}
			}
		}

		LOGGER.info("Recherche terminée ! ");
		return bestSolution;
	}

	/**
	 * Recherche en profondeur.
	 * 
	 * @param current La solution au début de cette recherche.
	 * @param level Le niveau.
	 * @return La meilleure solution dans cette branche.
	 */
	public Solution search(Solution current, int level) {

		if (level == MAX_DEPTH) {
			// On s'arrête
			return current;
		}

		for (int i = 0; i < bestSolution.getNbRaces(); i++) {

			for (Swimmer swimmer : swimmers) {

				// Le nageur est déjà présent
				if (current.containsSwimmer(swimmer)) {
					continue;
				}

				// On tente avec ce nageur sur cette nage !
				Solution solution = current.deriveSol(swimmer, i);

				if (seen.contains(solution)) {
					continue;
				}
				seen.add(solution);

				// Recherche en profondeur, niveau +1 :)
				solution = search(solution, level + 1);

				int points = solution.getPoints();
				if (solution.isValid() && points > current.getPoints()) {
					current = solution;
				}
			}
		}
		return current;
	}
}

package mosioj.equipesInterclub.swimmer.performance;

import java.util.Comparator;

public class PerfRaceComparator implements Comparator<Performance> {

	/**
	 * 
	 */
	public int compare(Performance p1, Performance p2) {
		
		if (p1 == null && p2 == null)
			return 0;
		
		if (p1 == null && p2 != null)
			return -1;
		
		if (p1 != null && p2 == null)
			return 1;
		
		if (p1.race == null && p2.race != null)
			return -1;
		if (p1.race != null && p2.race == null)
			return 1;
		
		if (p1.race != p2.race)
			return p1.race.compareTo(p2.race);
		
		// Nages égales, on trie sur le temps
		return p1.time.compareTo(p2.time);
	}

}

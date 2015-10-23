package mosioj.equipesInterclub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import mosioj.equipesInterclub.ffn.FFNReader;
import mosioj.equipesInterclub.swimmer.Nageur;
import mosioj.equipesInterclub.swimmer.Nageuse;
import mosioj.equipesInterclub.swimmer.Swimmer;
import mosioj.equipesInterclub.swimmer.performance.PerfChooser;
import mosioj.equipesInterclub.swimmer.performance.Performance;
import mosioj.equipesInterclub.util.ExcelReader;

public class SwimerPerformanceFactory {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(FFNReader.class);

	/**
	 * Properties used.
	 */
	private final Properties prop;

	/**
	 * Class constructor.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public SwimerPerformanceFactory() throws FileNotFoundException, IOException {
		File file = new File(getClass().getResource("/my.properties").getFile());
		LOGGER.debug("Reading properties in : " + file.getAbsolutePath());
		prop = new Properties();
		prop.load(new FileInputStream(file));
	}

	/**
	 * Entry point.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Read the swimmers from the Excel Sheet
		SwimerPerformanceFactory spf = new SwimerPerformanceFactory();
		LOGGER.info("Computing swimmers...");
		List<Swimmer> swimmers = spf.readSwimmers();
		LOGGER.info("Got the swimmers !");

		// Read performances from the web
		FFNReader reader = new FFNReader();
		Map<Swimmer, List<Performance>> performances = new LinkedHashMap<Swimmer, List<Performance>>();
		for (Swimmer swimmer : swimmers) {

			LOGGER.info(MessageFormat.format("Reading performances of \"{0}\" on the FFN Website...", swimmer));

			// Determine if this is a female or a male
			List<Performance> perfs = reader.readRecord(swimmer);
			// FIXME récupérer aussi les grands bain
			// TODO appliquer un bonus au grand bain (le même que le malus ?)
			Swimmer newOne = perfs.get(0).isAWoman ? new Nageuse(swimmer) : new Nageur(swimmer);

			LOGGER.info("Got the performances ! Selecting the best one for each race...");
			PerfChooser chooser = new PerfChooser(perfs);
			perfs = chooser.chooseIt();
			performances.put(newOne, perfs);
		}

		LOGGER.info("Writing results to output file...");
		spf.flushResults(performances);
	}

	/**
	 * Writes the data to the output file.
	 * 
	 * @param swimmers
	 * @throws IOException
	 */
	private void flushResults(Map<Swimmer, List<Performance>> performances) throws IOException {

		File output = new File(prop.getProperty("OUTPUT_FILE"));
		if (output.exists()) {
			LOGGER.info("Output file exists... Removing it.");
			output.delete();
		}
		LOGGER.info("Output is : " + output.getAbsolutePath());

		// Initializing
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		int rowIndex = 0;

		// Writing header
		Row row = sheet.createRow(rowIndex++);
		int cellnum = 0;
		String[] headers = { "NOM", "PRENOM", "NAGE", "POINTS", "Homme / Femme", "TEMPS" };
		for (String header : headers) {
			Cell cell = row.createCell(cellnum++);
			cell.setCellValue(header);
		}

		// Writing performances of each swimmer
		for (Swimmer swimmer : performances.keySet()) {
			boolean firstPerf = true;
			for (Performance perf : performances.get(swimmer)) {
				
				row = sheet.createRow(rowIndex++);
				cellnum = 0;
				
				// Nom / Prénom si c'est la première ligne du nageur
				Cell cell = row.createCell(cellnum++);
				if (firstPerf) {
					cell.setCellValue(swimmer.surname);
				}
				cell = row.createCell(cellnum++);
				if (firstPerf) {
					cell.setCellValue(swimmer.name);
				}
				
				// The performances
				cell = row.createCell(cellnum++);
				cell.setCellValue(perf.race.toString());
				
				// Leave one cell empty for points
				cell = row.createCell(cellnum++);
				
				// The male / female attribute
				cell = row.createCell(cellnum++);
				if (firstPerf) {
					cell.setCellValue(perf.isAWoman ? "F" : "H");
				}

				// The time of the performance
				cell = row.createCell(cellnum++);
				cell.setCellValue(perf.time.toString());
				
				firstPerf = false;
			}
		}

		// Writing the results
		FileOutputStream os = new FileOutputStream(output);
		wb.write(os);
	}

	/**
	 * 
	 * @return The list of swimmers read in the file with no races.
	 * @throws Exception
	 */
	private List<Swimmer> readSwimmers() throws Exception {

		final List<Swimmer> swimmers = new ArrayList<Swimmer>();

		File input = new File(prop.getProperty("INPUT_FILE"));
		if (!input.exists()) {
			throw new IllegalStateException("File " + input.getAbsolutePath() + " does not exist.");
		}
		LOGGER.info("Parsing " + input.getAbsolutePath());

		List<List<String>> lines = ExcelReader.readLines(input);
		int index = 0;
		for (List<String> line : lines) {

			index++;
			if (line.size() != 2) {
				LOGGER.warn("Line " + index + " does not have 2 columns but " + line.size() + ".");
				continue;
			}

			String name = line.get(0);
			if (name.isEmpty()) {
				LOGGER.warn("Line " + index + " have an empty name !");
				continue;
			}

			String surname = line.get(1);
			if (surname.isEmpty()) {
				LOGGER.warn("Line " + index + " have an empty surname !");
				continue;
			}

			Swimmer current = new Swimmer(surname, name);
			LOGGER.info("Found a new entry : " + current);
			swimmers.add(current);
		}

		return swimmers;
	}

}

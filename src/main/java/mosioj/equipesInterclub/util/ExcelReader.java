package mosioj.equipesInterclub.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Lit le fichier excel.
 * 
 * @author Jordan Mosio
 * 
 */
public final class ExcelReader {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(ExcelReader.class);

	/**
	 * Interdit.
	 */
	private ExcelReader() {
	}

	/**
	 * 
	 * @param file Le fichier à lire.
	 * @param sheetName The sheet name.
	 * @return La liste des lignes lues.
	 * @throws Exception Si le fichier n'existe pas.
	 */
	public static List<List<String>> readLines(File file, String sheetName) throws Exception {

		LOGGER.debug(MessageFormat.format("Reading file {0}...", file));
		List<List<String>> result = new ArrayList<List<String>>();
		if (!file.exists()) {
			String message = MessageFormat.format("Le fichier {0} n''existe pas !", file);
			throw new FileNotFoundException(message);
		}

		FileInputStream fis = new FileInputStream(file);
		String fileName = file.getName();
		Workbook wb = null;
		if (fileName.toLowerCase().endsWith("xlsx")) {
			wb = new XSSFWorkbook(fis);
		} else if (fileName.toLowerCase().endsWith("xls")) {
			wb = new HSSFWorkbook(fis);
		}

		// On récupère la sheet
		Sheet sheet = wb.getSheet(sheetName);

		// every sheet has rows, iterate over them
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {

			List<String> line = new ArrayList<String>();
			result.add(line);

			// Get the row object
			Row row = rowIterator.next();

			// Every row has columns, get the column iterator and iterate over them
			Iterator<Cell> cellIterator = row.cellIterator();

			while (cellIterator.hasNext()) {

				// Get the Cell object
				Cell cell = cellIterator.next();

				// check the cell type and process accordingly
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					line.add(cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					line.add(cell.getNumericCellValue() + "");
					break;
				case Cell.CELL_TYPE_FORMULA:
					String value = "";
					switch (cell.getCachedFormulaResultType()) {
					case Cell.CELL_TYPE_NUMERIC:
						value = cell.getNumericCellValue() + "";
						break;
					case Cell.CELL_TYPE_STRING:
						value = cell.getRichStringCellValue().getString();
						break;
					default:
						throw new Exception("Unsupported data type: " + cell.getCachedFormulaResultType());
					}
					line.add(value);
					break;
				case Cell.CELL_TYPE_BLANK:
					line.add(null);
					break;
				default:
					throw new Exception("Unsupported data type: " + cell.getCellType());
				}
			} // end of cell iterator
		} // end of rows iterator

		return result;
	}
	
	/**
	 * Converts rows into columns, and columns into rows.
	 * 
	 * @param initial
	 * @return
	 */
	public static List<List<String>> transpose(List<List<String>> initial) {

		List<List<String>> res = new ArrayList<List<String>>();
		// Initialization
		for (int col = 0; col < initial.get(0).size(); col++) {
			res.add(new ArrayList<String>());
		}
		
		for (List<String> row : initial) {
			for (int col = 0; col < row.size(); col++) {
				List<String> current = res.get(col);
				current.add(row.get(col));
			}
		}
		
		return res;
	}
}

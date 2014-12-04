package equipesInterclub;

import java.io.File;
import java.io.IOException;

import equipesInterclub.util.ExcelReader;

/**
 * Hello world!
 * 
 */
public class App {

	public static void main(String[] args) {
		Object o = new Object();
		try {
			File root = new File(o.getClass().getResource("/").getFile());
	        ExcelReader.readLines(new File(root, "POINTS NAGEURS.xls"));
        } catch (IOException e) {
	        e.printStackTrace();
        }
	}
}

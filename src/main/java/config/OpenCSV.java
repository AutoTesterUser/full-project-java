package main.java.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class OpenCSV {

	@Context(description = "Obtiene los datos a partir de la tercera fila")
	public static Object[][] getCSVParametersDescription(String path, int rows, int cols)
			throws InterruptedException, CsvValidationException, IOException {

		String curDir = System.getProperty("user.dir");
		String filePath = curDir + File.separator + "data" + File.separator + path;

		FileReader file = new FileReader(filePath);

		try (CSVReader reader = new CSVReader(file)) {
			String[] cell;

			int x = rows;
			int y = cols;

			String[][] data = new String[x][y];

			int cont = -1;
			boolean skip = true;
			int contRow = 0;
			while ((cell = reader.readNext()) != null) {
				if (!skip) {
					cont = cont + 1;
					for (int i = 0; i < y; i++) {
						data[cont][i] = cell[i];
					}
				}
				contRow = contRow + 1;
				if (contRow == 2) {
					skip = false;
				}
			}
			return data;
		}
	}
}

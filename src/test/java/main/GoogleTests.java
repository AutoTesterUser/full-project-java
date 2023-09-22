package test.java.main;

import main.java.config.OpenCSV;
import main.java.pageSteps.ResultsSteps;
import main.java.pageSteps.SearchSteps;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.opencsv.exceptions.CsvValidationException;

import test.java.BaseTest;

public class GoogleTests extends BaseTest {

	@Test(priority = 1, enabled = true, dataProvider = "dataGoogleSearch")
	public void obtenerSegundoResultado(String[] args) {

		test.assignCategory("Google Search");
		test = test.createNode("Obtener el segundo resultado dada una búsqueda en Google");

		SearchSteps.normalSearch(args[0]);
		ResultsSteps.getResult(args[1]);

	}

	@Test(priority= 2, enabled = true, dataProvider = "dataGoogleSearch")
	public void traducirChino(String[] args){

		test.assignCategory("Google Search");
		test = test.createNode("Traducir un texto a Chino en el traductor de Google");

		SearchSteps.luckySearch(args[2]);
	}
	
	@DataProvider(name = "dataGoogleSearch")
	public Object[][] dataGoogleSearch() throws CsvValidationException, InterruptedException, IOException {
		return OpenCSV.getCSVParametersDescription("search" + File.separator + "CSVDataSearch.csv", 1, 4);
	}
}

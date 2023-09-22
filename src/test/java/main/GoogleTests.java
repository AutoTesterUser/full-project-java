package test.java.main;

import org.testng.annotations.Test;

import main.java.pageSteps.ResultsSteps;
import main.java.pageSteps.SearchSteps;
import test.java.BaseTest;

public class GoogleTests extends BaseTest {

	@Test(priority = 1, enabled = true)
	public void obtenerSegundoResultado() {

		test.assignCategory("Google Search");
		step = test.createNode("Obtener el segundo resultado dada una búsqueda en Google");

		SearchSteps.normalSearch("TestGroup");
		ResultsSteps.getResult("2");

	}
}

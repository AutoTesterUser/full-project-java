package main.java.pageSteps;

import main.java.config.Context;
import main.java.config.Utils;
public class ResultsSteps {

	@Context(description = "Obtener el resultado de la búsqueda")
	public static void getResult(String index) {
		
		Utils.stepStarted();
		//ResultsEvents.resultVerify(index);
		Utils.outputInfo("Hola esta es una prueba");
	}
}

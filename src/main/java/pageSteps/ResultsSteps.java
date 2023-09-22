package main.java.pageSteps;

import main.java.config.Context;
import main.java.config.Utils;
import main.java.pageEvents.ResultsEvents;
public class ResultsSteps {

	@Context(step = "Obtener el resultado de la búsqueda")
	public static void getResult(String index) {
		
		Utils.stepStarted();
		ResultsEvents.resultVerify(index);
	}
}

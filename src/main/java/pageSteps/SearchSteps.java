package main.java.pageSteps;

import main.java.config.Context;
import main.java.config.Utils;
import main.java.pageEvents.SearchEvents;
import test.java.BaseTest;

public class SearchSteps extends BaseTest{

	@Context(step = "Realizar una búsqueda con Google")
	public static void normalSearch(String text) {
		Utils.stepStarted();

		SearchEvents.sendText(text);
		SearchEvents.googleSearch();		
	}
	
	@Context(step = "Realizar una búsqueda con la opción 'Voy a tener suerte'")
	public static void luckySearch(String text) {
		Utils.stepStarted();

		SearchEvents.sendText(text);
		SearchEvents.luckySearch();
		
	}
}

package main.java.pageEvents;

import main.java.config.Context;
import main.java.config.Utils;
import main.java.pageObjects.SearchElements;
import main.java.utils.Events;

public class SearchEvents implements SearchElements{

	@Context(description = "Ingresa un texto en la barra de búsqueda", page = "Google Search")
	public static void sendText(String text) {
		try {
			Events.sendKeys(inputSearch, text);
			
		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
		}
	}
	
	@Context(description = "Selecciona el botón buscar con Google", page = "Google Search", depends = "sendText")
	public static void googleSearch() {
		try {
			Events.clickButton(buttonSearch);
		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
		}
	}	
	
	@Context(description = "Selecciona el botón buscar con Voy a tener suerte", page = "Google Search", depends = "sendText")
	public static void luckySearch() {
		try {
			Events.clickButton(buttonLuckySearch);
		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
		}
	}
}

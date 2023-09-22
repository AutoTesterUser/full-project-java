package main.java.pageEvents;

import java.util.List;
import java.util.Objects;

import org.openqa.selenium.WebElement;

import main.java.config.Context;
import main.java.config.Utils;
import main.java.config.Validation;
import main.java.pageObjects.ResultsElements;
import main.java.utils.Events;

public class ResultsEvents implements ResultsElements {

	@Context(description = "Muestra por consola el resultado según la ubicación", page = "Google Search", depends = "fastSearch")
	public static void resultVerify(String index) {

		try {
			String currentPage = Objects.requireNonNull(Events.getAtributte(htmlDocument, "itemtype")).split("/")[3];
			Utils.outputInfo("La página actual es: " + currentPage);
			Validation.trueBooleanCondition(currentPage.contains("Results"),
					"Se ha redireccionado a la página correcta", "No se a redireccionado a la página correcta");

			List<WebElement> titles = Events.getElementList(listTitles);
			String resultTitle;
			for (int i = 0; i < Objects.requireNonNull(titles).size(); i++) {
				if (i == Integer.parseInt(index)) {
					resultTitle = titles.get(i).getText();
					Utils.outputInfo("El " + index + " resultado es: " + resultTitle);
				}
			}

		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
		}
	}
}

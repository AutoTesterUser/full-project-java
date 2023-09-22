package main.java.config;

public class Validation {

	/**
	 * Este método reemplaza cualquier método de la clase Assert.
	 */

	@Context(description = "Valida si una condicion booleana es verdadera enviando un mensaje en caso de éxito y un mensaje en caso de error")
	public static void trueBooleanCondition(boolean condition, String successMessage, String errorMessage) {
		if (condition) {
			Utils.outputInfo(successMessage);
		}
		else {
			Utils.eventFailed(errorMessage);
		}
	}
}

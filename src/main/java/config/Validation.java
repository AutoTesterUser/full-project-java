package main.java.config;

public class Validation {

	@Context(description = "Valida una condicion booleana enviando un mensaje en caso de éxito y un mensaje en caso de error")
	public static void trueBooleanCondition(boolean condition, String successMessage, String errorMessage) {
		if (condition) {
			Utils.outputInfo(successMessage);
		}
		else {
			Utils.eventFailed(errorMessage + ": " + condition);
		}
	}
}

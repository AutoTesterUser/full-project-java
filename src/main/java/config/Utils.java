package main.java.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import test.java.BaseTest;

public class Utils extends BaseTest {

	public static int counter = 0;

	@Context(description = "Envía información por la consola y en el reporte de pruebas")
	public static void outputInfo(String output) {
		step.log(Status.INFO, output);
		System.out.println(output);
	}

	@Context(description = "Envía por consola y al reporte el evento que se ha iniciado")
	public static void stepStarted() {
		try {
			counter++;
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			String className = stackTrace[2].getClassName();
			Class<?> classClass = Class.forName(className);

			List<Class<?>> parameterTypes = new ArrayList<>();
			Method[] methods = classClass.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName() == stackTrace[2].getMethodName()) {
					Parameter[] parameters = method.getParameters();
					for (Parameter parameter : parameters) {
						parameterTypes.add(parameter.getType());
					}
				}
			}
			Method method = classClass.getMethod(stackTrace[2].getMethodName(),
					parameterTypes.toArray(new Class<?>[0]));

			String value = Utils.convertToPascalCase(getCurrentMethod());
			if (method.isAnnotationPresent(Context.class)) {
				Context annotation = method.getAnnotation(Context.class);
				value = annotation.description();
			}
			step.log(Status.INFO,
					MarkupHelper.createLabel("Paso " + counter + " iniciado: " + value, ExtentColor.BLUE));
			System.out.println("Paso " + counter + " iniciado: " + value);
		} catch (Exception e) {
			step.log(Status.INFO, e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	@Context(description = "Obtiene el nombre del método en curso")
	public static String getCurrentMethod() {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			return stackTrace[3].getMethodName();
		} catch (Exception e) {
			return null;
		}
	}

	@Context(description = "Captura la pantalla en caso de una prueba fallida y guarda la imagen")
	public static String takeScreenshot(String methodName) {
		String fileName = System.getProperty("user.dir") + File.separator + "screenshots" + File.separator + "failed"
				+ File.separator + methodName + ".png";
		File f = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);
		try {
			File newFile = new File(fileName);
			FileUtils.copyFile(f, newFile);
			return newFile.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	@Context(description = "Se ejecuta si algún método de la prueba falla")
	public static void eventFailed(String errorMessage) {
		try {
			step.log(Status.WARNING,
					"No se ha completado el evento '" + getCurrentMethod() + "' a causa de: " + errorMessage);
			System.out
					.println("No se ha completado el evento '" + getCurrentMethod() + "' a causa de: " + errorMessage);

			String logText = "MÉTODO FALLIDO: " + getCurrentMethod();
			Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
			step.log(Status.FAIL, m);
			String path = takeScreenshot(getCurrentMethod());
			step.fail(MediaEntityBuilder.createScreenCaptureFromPath(path).build());
			Assert.fail();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Context(description = "Obtiene el nombre de la variable de un elemento")
	public static String elementName(String variable) {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			String className = stackTrace[3].getClassName();
			Class<?> classClass = Class.forName(className);

			String interfaceName = classClass.getInterfaces()[0].getName();
			Class<?> interfaceClass = Class.forName(interfaceName);

			Field[] fields = interfaceClass.getDeclaredFields();

			String targetValue = variable;

			for (Field field : fields) {
				if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					continue;
				}

				field.setAccessible(true);
				Object value = field.get(null);

				if (targetValue.equals(value)) {
					String variableName = field.getName();
					return variableName;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return "";
	}

	@Context(description = "Convierte un texto de camelCase a PascalCase con espacios")
	public static String convertToPascalCase(String camelCase) {
		String[] words = camelCase.split("(?=[A-Z])");

		StringBuilder pascalCaseWithSeparator = new StringBuilder();
		for (String word : words) {
			String formattedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
			pascalCaseWithSeparator.append(formattedWord).append(" ");
		}

		if (pascalCaseWithSeparator.length() > 0) {
			pascalCaseWithSeparator.deleteCharAt(pascalCaseWithSeparator.length() - 1);
		}

		return pascalCaseWithSeparator.toString();
	}
}

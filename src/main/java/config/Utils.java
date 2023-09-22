package main.java.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import org.testng.ITestResult;
import test.java.BaseTest;

@SuppressWarnings("deprecation")
public class Utils extends BaseTest {

	public static int counter = 0;
	public static String exceptionMessage = "";
	public static String errorMethod = "";
	public static String errorClass = "";
	public static String errorLineNumber = "";
	public static Exception e;

	@Context(description = "Envía información por la consola y en el reporte de pruebas")
	public static void outputInfo(String output) {
		test.log(Status.PASS, output);
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
				if (method.getName().equals(stackTrace[2].getMethodName())) {
					Parameter[] parameters = method.getParameters();
					for (Parameter parameter : parameters) {
						parameterTypes.add(parameter.getType());
					}
				}
			}
			Method method = classClass.getMethod(stackTrace[2].getMethodName(),
					parameterTypes.toArray(new Class<?>[0]));

			String value = Utils.convertToPascalCase(Objects.requireNonNull(getCurrentMethod()));
			if (method.isAnnotationPresent(Context.class)) {
				Context annotation = method.getAnnotation(Context.class);
				value = annotation.step();
			}
			test.log(Status.INFO,
					MarkupHelper.createLabel("Paso " + counter + " iniciado: " + value, ExtentColor.BLUE));
			System.out.println("Paso " + counter + " iniciado: " + value);
		} catch (Exception e) {
			test.log(Status.INFO, e.getMessage());
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

	@Context(description = "Captura la pantalla y guarda la imagen en Base64")
	public static String takeScreenshot(String methodName, String folder) {
		String imagePath = System.getProperty("user.dir") + File.separator + "screenshots" + File.separator + folder
				+ File.separator + methodName + ".png";
		File f = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);

		try {
			File image = new File(imagePath);
			FileUtils.copyFile(f, image);
			byte[] imageBytes = readImageToBytes(image.getAbsolutePath());
            return encodeBytesToBase64(imageBytes);
		} catch (IOException e) {
			Utils.outputInfo(e.getMessage());
			return "";
		}
	}

	@Context(description = "Convierte la imagen a una cadena de bytes")
	private static byte[] readImageToBytes(String imagePath) throws IOException {
		File imageFile = new File(imagePath);
		byte[] imageBytes;

		try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
			imageBytes = new byte[(int) imageFile.length()];
			fileInputStream.read(imageBytes);
		}
		return imageBytes;
	}

	@Context(description = "Codifica los bytes a Base64")
	private static String encodeBytesToBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	@Context(description = "Se ejecuta si algún método de la prueba falla")
	public static void eventFailed(String errorMessage) {
		try {
			StackTraceElement[] exception = Thread.currentThread().getStackTrace();
			Exception exceptionClass;
			exceptionClass = new Exception(errorMessage);
			e = exceptionClass;

			exceptionMessage = errorMessage;
			errorMethod = exception[2].getMethodName();
			errorClass = exception[2].getClassName();
			errorLineNumber = String.valueOf(exception[2].getLineNumber());
			Assert.fail();
		} catch (Exception e) {
			Utils.outputInfo(e.getMessage());
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

            for (Field field : fields) {
				if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					continue;
				}

				field.setAccessible(true);
				Object value = field.get(null);

				if (variable.equals(value)) {
					return field.getName();
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

	@Context(description = "Obtiene el id del elemento o en su defecto el nombre de la variable")
	public static String getName(WebElement element, String xpathElement) {
		String elementName;
		try {
			elementName = element.getAttribute("id");
			if (elementName.equalsIgnoreCase("")) {
				elementName = Utils.elementName(xpathElement);
				return elementName;
			}
		} catch (Exception e) {
			elementName = Utils.elementName(xpathElement);
			return elementName;
		}
		return elementName;
	}

	@Context(description = "Reemplaza el contenido del log para una nueva ejecución")
	public static void reemplazarLog() {
		try {
			File archivo = new File(System.getProperty("user.dir") + File.separator + "logs" + File.separator + "application.log");
			FileWriter escritor = new FileWriter(archivo);
			escritor.write("");
			escritor.close();
			System.out.println("Se ha reemplazado el archivo log con la información de la nueva ejecución.");
		} catch (Exception e) {
			System.out.println("Ocurrió un error al reemplazar el contenido del archivo.");
		}
	}

	@Context(description = "Construye el LOG en caso de fallo")
	public static void errorLOG(ITestResult result) {
		Throwable exception = result.getThrowable();

		if (exception != null) {

			String eventErrorMethodName = exception.getStackTrace()[3].getMethodName();
			test.log(Status.FAIL, "No se ha completado el evento " + eventErrorMethodName + " a causa de: " + Utils.exceptionMessage.split(",")[0]);
			System.out.println("No se ha completado el evento " + eventErrorMethodName + " a causa de: " + Utils.exceptionMessage.split(",")[0]);

			StringBuilder stackTrace = new StringBuilder("El error se produjo en la línea " + Utils.errorLineNumber + " en el método " +
					Utils.errorMethod + " de la clase " + Utils.errorClass + ". Se recomienda depurar el código para tener más información.");

			for (int i = 4; i < 7; i++){
				stackTrace.append("\n").append("Método anterior fue llamado desde la línea ").append(exception.getStackTrace()[i].getLineNumber()).append(" en el método ").append(exception.getStackTrace()[i].getMethodName()).append(" de la clase ").append(exception.getStackTrace()[i].getClassName());
			}

			test.createNode("Error Log").log(Status.INFO, MarkupHelper.createCodeBlock(stackTrace.toString()));
			test.fail(e);
			System.out.println(stackTrace);

			String testName = result.getName();
			test.log(Status.FAIL, MarkupHelper.createLabel("Prueba Fallida: " + testName, ExtentColor.RED));
			String base64 = Utils.takeScreenshot(testName, "failed");
			test.fail("Clic para visualizar evidencia", MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
			System.out.println("Prueba Fallida: " + testName);
		}
		else {
			test.log(Status.WARNING, "Ocurrió un error inesperado, por favor revisar el archivo application.log ");
			System.out.println("Ocurrió un error inesperado, por favor revisar el archivo application.log ");
		}
	}


	@Context(description = "Vacía la carpeta reportes para obtener los reportes de una nueva ejecución")
	public static void reemplazarReportes() {
		try {
			String rutaCarpeta = System.getProperty("user.dir") + File.separator + "report" + File.separator;
			File carpeta = new File(rutaCarpeta);

			if (carpeta.exists() && carpeta.isDirectory()) {
				File[] archivos = carpeta.listFiles();

				Date fechaHoy = new Date();

				fechaHoy.setHours(0);
				fechaHoy.setMinutes(0);
				fechaHoy.setSeconds(0);

                assert archivos != null;
                for (File archivo : archivos) {
					if (archivo.isFile()) {
						Date fechaModificacion = new Date(archivo.lastModified());
						if (fechaModificacion.before(fechaHoy)) {
							if (archivo.delete()) {
								System.out.println("Archivo eliminado: " + archivo.getName());
							} else {
								System.out.println("No se pudo eliminar el archivo: " + archivo.getName());
							}
						}
					}
				}
			} else {
				System.out.println("La carpeta no existe o no es un directorio válido.");
			}
		} catch (Exception e) {
			System.out.println("Ocurrió un error al reemplazar el contenido del archivo.");
		}
	}
}

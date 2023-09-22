package main.java.utils;

import java.util.List;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import main.java.config.Context;
import main.java.config.Utils;
import main.java.config.Validation;
import test.java.BaseTest;

public class Events extends BaseTest {

	@Context(description = "Obtiene el texto de un elemento")
	public static String getText(String xpathElement) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);

			WebElement element = driver.findElement(By.xpath(xpathElement));
			wait.until(ExpectedConditions.visibilityOf(element));
			return element.getText();
		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
			return null;
		}
	}

	@Context(description = "Obtiene el valor dado un atributo de un elemento")
	public static String getAtributte(String xpathElement, String atributte) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);

			WebElement element = driver.findElement(By.xpath(xpathElement));
			wait.until(ExpectedConditions.visibilityOf(element));
			return element.getAttribute(atributte);
		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
			return null;
		}
	}

	@Context(description = "Escribe dentro de un campo de texto ubicado según xpath")
	public static void sendKeys(String xpathElement, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);

			WebElement element = BaseTest.driver.findElement(By.xpath(xpathElement));
			wait.until(ExpectedConditions.visibilityOf(element));

			String inputName = "";
			try {
				inputName = Utils.getName(element, xpathElement);
				if (Objects.equals(inputName, "") || inputName.isEmpty()) {
					inputName = Utils.elementName(xpathElement);
				}
			}
			catch(Exception e){
				inputName = Utils.elementName(xpathElement);
			}
			finally{
				if (element.isDisplayed() && element.isEnabled()) {
					int caracteres = element.getAttribute("value").toCharArray().length;
					for (int i = 0; i < caracteres; i++) {
						element.sendKeys(Keys.BACK_SPACE);
					}
					element.sendKeys(text);
					Utils.outputInfo("Se ha ingresado el texto '" + text + "' en el campo: " + inputName);
					Validation.trueBooleanCondition(element.getAttribute("value").contains(text),
							"El texto se ha ingresado correctamente", "El texto no se ha ingresado correctamente");
				} else {
					Utils.eventFailed("El campo '" + inputName + "' no se encuentra habilitado o desplegado");
				}
			}
		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
		}
	}

	@Context(description = "Selecciona un botón")
	public static void clickButton(String xpathElement) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);

			WebElement element = BaseTest.driver.findElement(By.xpath(xpathElement));
			wait.until(ExpectedConditions.visibilityOf(element));

			String buttonName = "";
			try {
				buttonName = Utils.getName(element, xpathElement);
				if (Objects.equals(buttonName, "") || buttonName.isEmpty()) {
					buttonName = Utils.elementName(xpathElement);
				}
			}
			catch(Exception e){
				buttonName = Utils.elementName(xpathElement);
			}
			finally {
				if (element.isEnabled()) {
					element.click();
					Utils.outputInfo("Se ha hecho clic en el botón: " + buttonName);
				} else {
					Utils.eventFailed("El botón '" + buttonName + "' no está desplegado o habilitado");
				}
			}
		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
		}
	}

	@Context(description = "Presiona la tecla ENTER en el contexto de ingresar texto en un campo")
	public static void enter(String inputXpath) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);
			Actions actions = new Actions(driver);

			WebElement element = BaseTest.driver.findElement(By.xpath(inputXpath));
			wait.until(ExpectedConditions.visibilityOf(element));

			actions.moveToElement(element).build().perform();
			actions.sendKeys(Keys.ENTER);
			Utils.outputInfo("Se ha presionado la tecla ENTER");

		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
		}
	}

	@Context(description = "Devuelve una lista de elementos")
	public static List<WebElement> getElementList(String listXpath) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);

			List<WebElement> elements = BaseTest.driver.findElements(By.xpath(listXpath));
			wait.until(ExpectedConditions.visibilityOfAllElements(elements));
			return elements;

		} catch (Exception e) {
			Utils.eventFailed(e.getMessage());
			return null;
		}
	}
}

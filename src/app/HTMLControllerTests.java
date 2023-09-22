package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HTMLControllerTests {

	public static String[][] testsArray = new String[1][1];

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
	
		getTests();
		buildHTML();
	}

	public static void getTests() throws ParserConfigurationException, SAXException, IOException {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse("testng.xml");
			Element element = document.getDocumentElement();
			
			NodeList classes = document.getElementsByTagName("class");
			testsArray = new String[classes.getLength()][2];

			Node suiteElement = element.getAttributeNode("name");
			System.out.println("Nombre del elemento <suite>: " + suiteElement.getNodeValue());

			NodeList testElements = element.getElementsByTagName("test");

			for (int i = 0; i < testElements.getLength(); i++) {				
				
				Element testElement = (Element) testElements.item(i);
				String testName = testElement.getAttribute("name");
				System.out.println("Nombre del elemento <test>: " + testName);

				NodeList classElements = testElement.getElementsByTagName("class");
				testsArray[i][0] = testName;

				for (int j = 0; j < classElements.getLength(); j++) {
					Element classElement = (Element) classElements.item(j);
					Class<?> clase = Class.forName(classElement.getAttribute("name"));
					System.out.println("Nombre de la clase: " + clase.getSimpleName());
					testsArray[i][j+1] = clase.getSimpleName();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static String readHTML(String filePath) {
		StringBuilder htmlContent = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
			String linea;
			while((linea = br.readLine()) !=null) {
				htmlContent.append(linea).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlContent.toString();
	}
	
	public static void writeHTML(String filePath, String contenido) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))){
			bw.write(contenido);
			System.out.println("Archivo HTML generado exitosamente");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void buildHTML() throws IOException {

		try {
			
			String htmlPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "app" + File.separator + "TestsExecution.html";
			
			String marcadorInicio = "<!-- INICIO INSERCION DATA -->";
			String marcadorFin = "<!-- FIN INSERCION DATA -->";
			
			String contenidoHTML = readHTML(htmlPath);
			
			int inicio = contenidoHTML.indexOf(marcadorInicio);
			int fin = contenidoHTML.indexOf(marcadorFin) + marcadorFin.length();
			
			if (inicio != -1 && fin != -1) {
				StringBuilder nuevoContenidoHTML = new StringBuilder(contenidoHTML.substring(0,inicio));
				
				nuevoContenidoHTML.append(marcadorInicio);
				nuevoContenidoHTML.append("\n");
				
				for (int i = 0; i < testsArray.length; i++) {
					
					String[] test = testsArray[i];
					String nameTest = test[0];
					nuevoContenidoHTML.append("<div class=\"funcionalidad\">");
					nuevoContenidoHTML.append("<div class=\"Nodo\">");
					nuevoContenidoHTML.append("<label>");
					nuevoContenidoHTML.append(
							"<input type=\"checkbox\" id=\" " + nameTest + " \" onchange=\"sincronizarPruebas('" + "pruebas" + nameTest + "', this)\"> "
									+ nameTest);
					nuevoContenidoHTML.append("</label>");
					nuevoContenidoHTML.append(
							"<button class=\"toggle-button\" onclick=\"togglePruebas('" + "pruebas" + nameTest +"')\" ><img src=\"https://cdn-icons-png.flaticon.com/128/318/318225.png\" data-src=\"https://cdn-icons-png.flaticon.com/128/318/318225.png\" alt=\"flecha hacia abajo \" title=\"flecha hacia abajo \" width=\"20\" height=\"20\" class=\"lzy lazyload--done\" srcset=\"https://cdn-icons-png.flaticon.com/128/318/318225.png 4x\"></button>");
					nuevoContenidoHTML.append("</div>");
					nuevoContenidoHTML.append("<div class=\"pruebas\" id=\"" + "pruebas" + nameTest+ "\">");
					
					for (int j = 1; j < test.length; j++) {
						String className = test[j];
						nuevoContenidoHTML.append("<label class=\"prueba\"><input type=\"checkbox\"> " + className + "</label>");	
					}
					
					nuevoContenidoHTML.append("</div>");
				
				}		
				
				nuevoContenidoHTML.append("\n");
				nuevoContenidoHTML.append(marcadorFin);
				nuevoContenidoHTML.append(contenidoHTML.substring(fin));
				
				writeHTML(htmlPath, nuevoContenidoHTML.toString());
			}
			else {
				System.out.println("No se encontraron marcadores de posición");
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}

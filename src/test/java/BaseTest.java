package test.java;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;
import main.java.config.Context;
import main.java.config.Utils;
import main.java.utils.Constants;

public class BaseTest {

	public static WebDriver driver;
	public static ExtentReports extent;
	public static ExtentSparkReporter spark;
	public static ExtentTest test;
	public static String extentDate;
	public static ExtentTest step;
	
	
	@BeforeTest(description = "Se configura el controlador y el navegador")
	@Parameters(value = { "browserName" })
	public void beforeTestMethod(String browserName) {

		setUpDriver(browserName);
		driver.manage().window().maximize();
		driver.get(Constants.url);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	@BeforeMethod(description = "Se configura el reporte de pruebas")
	public void beforeMethod(Method testMethod, ITestResult result) {
		String className = result.getTestClass().getRealClass().getSimpleName();
        extent = new ExtentReports();
		spark = new ExtentSparkReporter("report" + File.separator + className + "Report.html");
		spark.config().enableOfflineMode(true);
		spark.config().setDocumentTitle("Reporte de automatización de pruebas");
		spark.config().setEncoding("UTF-8");
		spark.config().setReportName("Resultado Pruebas Automatizadas");
		spark.config().setTimelineEnabled(true);
		extent.attachReporter(spark);
		extent.setSystemInfo("Proyecto", "Google Search Project");
		extent.setSystemInfo("Automatizador", "Automate Tester");
		extent.setSystemInfo("os", System.getProperty("os.name"));
		test = extent.createTest(Utils.convertToPascalCase(testMethod.getName()));
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		String methodName = result.getMethod().getMethodName();
		String passedPath = System.getProperty("user.dir") + File.separator + "screenshots"
				+ File.separator + "passed" + File.separator + methodName + ".png";
		String skkipedPath = System.getProperty("user.dir") + File.separator + "screenshots"
				+ File.separator + "skkiped" + File.separator + methodName + ".png";

		if (result.getStatus() == ITestResult.SUCCESS) {
			step.log(Status.PASS, MarkupHelper.createLabel("PRUEBA SUPERADA: '" + methodName, ExtentColor.GREEN));
			step.pass(MediaEntityBuilder.createScreenCaptureFromPath(passedPath).build());
		} else if (result.getStatus() == ITestResult.SKIP) {
			step.log(Status.SKIP, MarkupHelper.createLabel("PRUEBA INCOMPLETA: '" + methodName, ExtentColor.AMBER));
			step.skip(MediaEntityBuilder.createScreenCaptureFromPath(skkipedPath).build());
		}
		driver.quit();
	}
	
	@AfterTest
    public void afterTest() {
       
		extent.flush();
    }
	
	@Context(description = "Configura el tipo y las características del controlador")
	public void setUpDriver(String browserName) {

		if (browserName.contains("chrome")) {
			
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.setHeadless(false);
			driver = new ChromeDriver(options);
			driver.manage().window().setSize(new Dimension(1600, 900));
			
		} else if (browserName.contains("edge")) {
			
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browserName.contains("firefox")) {
			
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		}
	}
}

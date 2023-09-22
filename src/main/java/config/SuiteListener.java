package main.java.config;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import test.java.BaseTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SuiteListener extends BaseTest implements ITestListener, IAnnotationTransformer{
	@Override
	public void onTestStart(ITestResult result) {
		ITestListener.super.onTestStart(result);
		Utils.reemplazarLog();
		Utils.reemplazarReportes();
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	@Override
	public void onStart(ITestContext context) {
		ITestListener.super.onStart(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		ITestListener.super.onFinish(context);

		String fullName = context.getAllTestMethods()[0].getTestClass().getName();
		String simpleName = fullName.substring(fullName.lastIndexOf('.') + 1);
		String filePath = "report" + File.separator + simpleName + "Report.html";

		File originalFile = new File(spark.getFile().getAbsolutePath());
		File newFile = new File(filePath);

		try {
			Files.copy(originalFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			originalFile.deleteOnExit();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

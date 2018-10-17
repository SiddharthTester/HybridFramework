package com.Framework.TestBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.Framework.ExcelReader.Excel_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Test_Base {
	
	public static final Logger logger  = Logger.getLogger(Excel_Reader.class.getName());
	public WebDriver d;
	public static Properties propertiesfile;
	public File file;
	public FileInputStream fis;
	public Excel_Reader excellreader;
	public static ExtentReports extent;
	public static ExtentTest test;
	public ITestResult result;
	
	
	static {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		extent = new ExtentReports(System.getProperty("user.dir") + "/src/main/java/com/hybridFramework/report/test" + formater.format(calendar.getTime()) + ".html", false);
	}
	
	public void getBrowser(String browser) {
		if (System.getProperty("os.name").contains("Windows")) {
			if (browser.equalsIgnoreCase("firefox")) {
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/Drivers/geckodriver.exe");
				d= new FirefoxDriver();
			}else if (browser.equalsIgnoreCase("chrome")) {
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/Drivers/chromedriver.exe");
				d= new ChromeDriver();
			}
		}else if (System.getProperty("os.name").contains("Mac")) {
			if (browser.equalsIgnoreCase("firefox")) {
				System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"/Drivers/geckodriver");
				d= new FirefoxDriver();
			}else if(browser.equalsIgnoreCase("chrome")){
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/drivers/chromedriver");
				d = new ChromeDriver();		
		}
		}
	}
	
	public void loadPropertiesFile() throws IOException {
		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		propertiesfile = new Properties();
		file = new File(System.getProperty("user.dir")+"/src/main/java/com/Framework/ConfigFile/config.Properties");
		fis = new FileInputStream(file);
		propertiesfile.load(fis);	
		logger.info("config.Properties got executed");
		
		file = new File(System.getProperty("user.dir")+"/src/main/java/TestProperties/LoginPage.properties");
		fis = new FileInputStream(file);
		propertiesfile.load(fis);
		logger.info("LoginPage.Properties got executed");
//		System.out.println("loading prop---- Login");
	}
	
   public String getScreenShot(String imageName) throws IOException{
		
		if(imageName.equals("")){
			imageName = "blank";
		}
		File image = ((TakesScreenshot)d).getScreenshotAs(OutputType.FILE);
		String imagelocation = System.getProperty("user.dir")+"/src/main/java/com/Framework/TestScreenShots/";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		String actualImageName = imagelocation+imageName+"_"+formater.format(calendar.getTime())+".png";
		File destFile = new File(actualImageName);
		FileUtils.copyFile(image, destFile);
		return actualImageName;
	}
   
   public WebElement waitForElement(WebDriver d,long time,WebElement element){
		WebDriverWait wait = new WebDriverWait(d, time);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public WebElement waitForElementWithPollingInterval(WebDriver d,long time,WebElement element){
		WebDriverWait wait = new WebDriverWait(d, time);
		wait.pollingEvery(5, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	public void impliciteWait(long time){
		d.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}
	
	public void getresult(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(LogStatus.PASS, result.getName() + " test is pass");
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP, result.getName() + " test is skipped and skip reason is:-" + result.getThrowable());
		} else if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.FAIL, result.getName() + " test is failed" + result.getThrowable());
			String screen = getScreenShot("");
			test.log(LogStatus.FAIL, test.addScreenCapture(screen));
		} else if (result.getStatus() == ITestResult.STARTED) {
			test.log(LogStatus.INFO, result.getName() + " test is started");
		}
	}
	
	@AfterMethod()
	public void afterMethod(ITestResult result) throws IOException {
		getresult(result);
	}

	@BeforeMethod()
	public void beforeMethod(Method result) {
		test = extent.startTest(result.getName());
		test.log(LogStatus.INFO, result.getName() + " test Started");
	}
	
	@AfterClass(alwaysRun = true)
	public void endTest() {
		//driver.quit();
		extent.endTest(test);
		extent.flush();
	}
	
	public WebElement getLocator(String locator) throws Exception {
//		System.out.println(locator);
        String[] split = locator.split(":");
		String locatorType = split[0];
		String locatorValue = split[1];
	
//		System.out.println("Dhara gaya" + "=" + By.xpath(locatorValue));
		//System.out.println("locatorType:-"+locatorType);
		//System.out.println("locatorValue:-"+locatorValue);
		if (locatorType.toLowerCase().equals("id"))
			return d.findElement(By.id(locatorValue));
		else if (locatorType.toLowerCase().equals("name"))
			return d.findElement(By.name(locatorValue));
		else if ((locatorType.toLowerCase().equals("classname"))
				|| (locatorType.toLowerCase().equals("class")))
			return d.findElement(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("tagname"))
				|| (locatorType.toLowerCase().equals("tag")))
			return d.findElement(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("linktext"))
				|| (locatorType.toLowerCase().equals("link")))
			return d.findElement(By.linkText(locatorValue));
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return d.findElement(By.partialLinkText(locatorValue));
		else if ((locatorType.toLowerCase().equals("cssselector"))
				|| (locatorType.toLowerCase().equals("css")))
			return d.findElement(By.cssSelector(locatorValue));
		else if (locatorType.toLowerCase().equals("xpath"))
			return d.findElement(By.xpath(locatorValue));
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}
	
	public  List<WebElement> getLocators(String locator) throws Exception {
//		System.out.println(locator);
		String[] split = locator.split(":");
		String locatorType = split[0];
		String locatorValue = split[1];
//		System.out.println("locatorType:-"+locatorType);
//		System.out.println("locatorValue:-"+locatorValue);

		if (locatorType.toLowerCase().equals("id"))
			return d.findElements(By.id(locatorValue));
		else if (locatorType.toLowerCase().equals("name"))
			return d.findElements(By.name(locatorValue));
		else if ((locatorType.toLowerCase().equals("classname"))
				|| (locatorType.toLowerCase().equals("class")))
			return d.findElements(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("tagname"))
				|| (locatorType.toLowerCase().equals("tag")))
			return d.findElements(By.className(locatorValue));
		else if ((locatorType.toLowerCase().equals("linktext"))
				|| (locatorType.toLowerCase().equals("link")))
			return d.findElements(By.linkText(locatorValue));
		else if (locatorType.toLowerCase().equals("partiallinktext"))
			return d.findElements(By.partialLinkText(locatorValue));
		else if ((locatorType.toLowerCase().equals("cssselector"))
				|| (locatorType.toLowerCase().equals("css")))
			return d.findElements(By.cssSelector(locatorValue));
		else if (locatorType.toLowerCase().equals("xpath"))
			return d.findElements(By.xpath(locatorValue));
		else
			throw new Exception("Unknown locator type '" + locatorType + "'");
	}
	
	public WebElement getWebElement(String locator) throws Exception{
		return getLocator(propertiesfile.getProperty(locator));
	}
	
	public List<WebElement> getWebElements(String locator) throws Exception{
		return getLocators(propertiesfile.getProperty(locator));
	}
	
	public String[][] getData(String excelname, String sheetname) {
		String excellocation = System.getProperty("user.dir")+"/src/main/java/com/Framework/TestData/"+excelname;
		 excellreader = new Excel_Reader();
		 excellreader.getExcelData(excellocation, sheetname);
		 return excellreader.getExcelData(excellocation, sheetname);
	}
	
      public static void main(String[] args) throws Exception {
		Test_Base test=new Test_Base();
//		test.getBrowser("firefox");
		test.loadPropertiesFile();
		System.out.println(test.propertiesfile.getProperty("username"));
		test.getWebElement("password");
		

	}

}

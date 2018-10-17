package com.Framework.Helper.AssertionHelper;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.Framework.Helper.LoggerHelper.LoggerHelper;

public class AssertionHelper {
	private static final Logger log = LoggerHelper.getLogger(AssertionHelper.class);
	
	public static synchronized boolean verifyElementPresent(WebElement element){
		boolean isDisplayed = false;
		try {
			isDisplayed =element.isDisplayed();
			log.info(element.getText()+" is Diaplayed");
		} catch (Exception e) {
			log.error("Element Not Found"+e);
			// TODO: handle exception
		}
		return isDisplayed;
	}
	
	public static synchronized boolean verifyElementNotPresent(WebElement element) {
		boolean isDisplayed =false;
		try {
			element.isDisplayed();
			log.info(element.getText()+" isDisplayed");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Element not present "+e);
			isDisplayed =true;
		}
		return isDisplayed;
	}
	
	public static synchronized boolean verifyTextEquals(WebElement element, String expectedText) {
		boolean flag= false;
		try {
			String actualText =element.getText();
			if (actualText.equals(expectedText)) {
				log.info("Actual Text is :-" +actualText+ "Expected Text is :-" + expectedText);
				return flag =true;
			} else {
				log.error("Actual Text is :-" +actualText+ "Expected Text is :-" + expectedText);
				return flag;
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Actual Text is :-" +element.getText()+ "Expected Text is :-" + expectedText);
			log.info("Text Not Matching"+e);
		}
		return flag;	
	}
}

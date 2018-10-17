package com.Framework.Helper.LoggerHelper;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import Utils.ResourceHelper;

public class LoggerHelper {
private static boolean root = false;
	

	public static  Logger getLogger(Class clas){
		if (root) {
			return Logger.getLogger(clas);
			
		}
		//String log4jLOcation = System.getProperty("user.dir")+"/src/main/resources/log4j.properties";
		PropertyConfigurator.configure(ResourceHelper.getResourcePath("/src/main/resources/log4j.properties"));
		root = true;
		return Logger.getLogger(clas);
	}

}

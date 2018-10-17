package com.Framework;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.Framework.TestBase.Test_Base;

public class TestScript extends Test_Base{
	

	@DataProvider(name="testdata")
	public Object[][] Datasource() {
		return getData("Excel_Test_Data.xlsx", "TestData");
	}
	
	@Test(dataProvider="testdata")
	public void TestLogin(String Username, String Password, String runmode) {
		System.out.println(Username);
		System.out.println(Password);
		System.out.println(runmode);
	}

}

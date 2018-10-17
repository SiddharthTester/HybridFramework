package com.Framework.ExcelReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel_Reader {
	
	public static final Logger logger  = Logger.getLogger(Excel_Reader.class.getName());
	
	public String [][] getExcelData(String excellocation,String sheetname){
		try {
			String [][] DataSets=null;
			
			//To read character-stream data in file location
			FileInputStream fis = new FileInputStream(new File(excellocation));
			
			//To hold the reference of excell file
			XSSFWorkbook workbook =new XSSFWorkbook(fis);
			
			//To fetch the desired sheet
			XSSFSheet sheet=workbook.getSheet(sheetname);
			
			//To count active rows
			int totalRow=sheet.getLastRowNum() + 1;
			
			//To count no. of active column
			int totalColumn=sheet.getRow(0).getLastCellNum();
			
			//Creats array of rows and column
			DataSets = new String[totalRow-1][totalColumn];
			
			//It will iterate each row one by one
			Iterator<Row> rowIterator =sheet.iterator();
			
			int r=0;
			int c=0;
			
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (r++ != 0 ) {
					int k=c;
					c++;
					Iterator<Cell> cellIterator =row.cellIterator();
					int j=0;
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_NUMERIC:
							System.out.println(k);
							System.out.println(j);
							DataSets[k][j++] = cell.getStringCellValue();
							System.out.println(cell.getStringCellValue());
							break;
							
						case Cell.CELL_TYPE_STRING:
							System.out.println(k);
							System.out.println(j);
							DataSets[k][j++] = cell.getStringCellValue();
							System.out.println(cell.getStringCellValue());
							break;
							
						case Cell.CELL_TYPE_BOOLEAN:
							System.out.println(k);
							System.out.println(j);
							DataSets[k][j++] = cell.getStringCellValue();
							System.out.println(cell.getStringCellValue());
							break;
							
						case Cell.CELL_TYPE_FORMULA:
							System.out.println(k);
							System.out.println(j);
							DataSets[k][j++] = cell.getStringCellValue();
							System.out.println(cell.getStringCellValue());
							break;
							}
					}
					System.out.println("");
				}		
			}
			fis.close();
			return DataSets;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;	
	}
	
	public static void main(String[] args) {
		String excellocation= System.getProperty("user.dir")+"/src/main/java/com/Framework/TestData/Excel_Test_Data.xlsx";
		String sheetname = "TestData";
		Excel_Reader excel= new Excel_Reader();
		excel.getExcelData(excellocation, sheetname);				
	}
	

}

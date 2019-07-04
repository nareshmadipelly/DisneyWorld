package com.automation.utilities;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

public class Report_Excel {
	
	public static void updateReportExcel(File filePathx, JSONArray jsonArray) throws IOException{
		XSSFWorkbook excelWorkBook = new XSSFWorkbook();
		XSSFSheet excelWorkSheet = excelWorkBook.createSheet("ResultSheet");
		
		Row heading = excelWorkSheet.getRow(0);
		for(int i=0; i< jsonArray.length(); i++){
			Iterator<String> iterate = jsonArray.getJSONObject(0).keys();
			int x = 0;
			heading = excelWorkSheet.createRow(0);
			while(iterate.hasNext()){
				String key = (String)iterate.next();
				Cell cell = heading.createCell(x);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(key);
				x++;
			}
		}
		
		for(int i=0; i< jsonArray.length(); i++){
			Iterator<String> iterate = jsonArray.getJSONObject(0).keys();
			int x = 0;
			Row row = excelWorkSheet.createRow(i + 1);
			while(iterate.hasNext()){
				String key = (String)iterate.next();
				Cell cell = row.createCell(x);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(jsonArray.getJSONObject(i).get(key).toString());
				x++;
			}
		}
		FileOutputStream out = new FileOutputStream(filePathx.getAbsolutePath()+"/ExcelReport.xlsx");
		excelWorkBook.write(out);
		out.flush();
		out.close();
	}
	
	public static synchronized void updateExcel(String no, String key, String value, String file_Name,String SheetName) throws IOException{
		File filepath = new File("./Files/"+file_Name);
		filepath.setWritable(true);
		InputStream filePath = new FileInputStream(filepath);
		
		XSSFWorkbook excelWorkBook = new XSSFWorkbook(filePath);
		 
		
		XSSFSheet excelWorkSheet = excelWorkBook.getSheet(SheetName);
		
		//Madhu
		excelWorkBook.setSheetName(excelWorkBook.getSheetIndex(SheetName), "Resultsheet");
		
		Row heading = excelWorkSheet.getRow(0);
		int columnValue = getRowNumber(key,heading);
		System.out.println("column value ----->"+columnValue);
		Row row = excelWorkSheet.getRow(Integer.valueOf(no));
		System.out.println("row value value ----->"+Integer.valueOf(no));
		XSSFCellStyle greenStyle = excelWorkBook.createCellStyle();
		XSSFFont greenFont = excelWorkBook.createFont();
		XSSFColor greenColor = new XSSFColor(Color.BLACK);
		greenFont.setColor(greenColor);
		greenStyle.setFont(greenFont);
		XSSFCellStyle redStyle = excelWorkBook.createCellStyle();
		XSSFFont redFont = excelWorkBook.createFont();
		XSSFColor redColor = new XSSFColor(Color.RED);
		redFont.setColor(redColor);
		redFont.setBold(true);
		redStyle.setFont(redFont);
//		int pass = value.indexOf("Pass"), count=0;
//		List<Integer> passIndex = new ArrayList<Integer>();
//		while(pass != -1) {
//			passIndex.add(pass);
//			pass = value.indexOf("Pass", ++pass);
//			count++;
//		}
//		for(int i = 0; i<passIndex.size();i++){
//			text.applyFont(passIndex.get(i), passIndex.get(i)+4, greenFont);
//			System.out.println(text);
//		}
//		count=0;
//		int fail = value.indexOf("Fail");
//		List<Integer> failIndex = new ArrayList<Integer>();
//		while(fail != -1) {
//			failIndex.add(fail);
//			fail = value.indexOf("Fail", ++fail);
//			count++;
//		}
//		for(int i = 0; i<failIndex.size();i++){
//			text.applyFont(failIndex.get(i), failIndex.get(i)+4, redFont);
//			System.out.println(text);
//		}
		
		if(row == null){
			row = excelWorkSheet.createRow(Integer.valueOf(no));
			Cell cell = row.createCell(0);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(no);
			if(row.getCell(columnValue) == null){
				row.createCell(columnValue);
				row.getCell(columnValue).setCellType(Cell.CELL_TYPE_STRING);
				if(value.indexOf("Fail")!=-1){
					row.getCell(columnValue).setCellStyle(redStyle);
					row.getCell(columnValue).setCellValue(value);
				}
				else{
					row.getCell(columnValue).setCellStyle(greenStyle);
					row.getCell(columnValue).setCellValue(value);
				}
			} else {
				row.getCell(columnValue);
				row.getCell(columnValue).setCellType(Cell.CELL_TYPE_STRING);
				if(value.indexOf("Fail")!=-1){
					row.getCell(columnValue).setCellStyle(redStyle);
					row.getCell(columnValue).setCellValue(value);
				}
				else{
					row.getCell(columnValue).setCellStyle(greenStyle);
					row.getCell(columnValue).setCellValue(value);
				}
			}
		} else {
			if(row.getCell(columnValue) == null){
				row.createCell(columnValue);
				row.getCell(columnValue).setCellType(Cell.CELL_TYPE_STRING);
				if(value.indexOf("Fail")!=-1){
					row.getCell(columnValue).setCellStyle(redStyle);
					row.getCell(columnValue).setCellValue(value);
				}
				else{
					row.getCell(columnValue).setCellStyle(greenStyle);
					row.getCell(columnValue).setCellValue(value);
				}
			} else {
				row.getCell(columnValue);
				row.getCell(columnValue).setCellType(Cell.CELL_TYPE_STRING);
				if(value.indexOf("Fail")!=-1){
					row.getCell(columnValue).setCellStyle(redStyle);
					row.getCell(columnValue).setCellValue(value);
				}
				else{
					row.getCell(columnValue).setCellStyle(greenStyle);
					row.getCell(columnValue).setCellValue(value);
				}
			}
		}
		filePath.close();
		FileOutputStream out = new FileOutputStream("./Files/"+file_Name);
		excelWorkBook.write(out);
		out.flush();
		out.close();
	}
	
	static int getRowNumber(String key, Row row){
		int i = 0; 
		for (i=0; i<row.getLastCellNum();i++){
			if(row.getCell(i).getStringCellValue().equals(key))
				break;
		}
		return i;
	}
}

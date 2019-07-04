package com.automation.utilities;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class UpdateDB_Excel {

	LinkedHashMap<Integer, List<Object>> excelCoreData;
	ArrayList<Object> itemList = null;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMMM-dd");

	HashMap<String, String> excelData = new LinkedHashMap<String, String>();
	ArrayList<HashMap<String, String>> excelDataArray = new ArrayList<HashMap<String, String>>();;
	
	/**
	 * Returns the result data read from excel
	 * @param fileName
	 * @param sheetName
	 * @param rowNumber
	 * @return the result data read from excel
	 * @throws IOException
	 */
	public HashMap<String, String> getResultData(String fileName, String sheetName, int rowNumber) throws IOException {
		InputStream filePath = new FileInputStream(FilePaths.finalResultOutput + "/" + fileName);
		XSSFWorkbook workBook = new XSSFWorkbook(filePath);
		XSSFSheet sheet = workBook.getSheet(sheetName);
		Iterator<Row> rowIterator = sheet.iterator();
		int totalCol = getColumnCount(fileName, sheetName);

		while (rowIterator.hasNext()) {
			XSSFRow row = (XSSFRow) rowIterator.next();
			int currentRow = row.getRowNum();
			if (currentRow == rowNumber) {
				for (int colCount = 0; colCount < totalCol; colCount++) {
					excelData = getExcelValue(row, sheet, colCount, false, excelData, 1);
				}
				System.out.println(excelData);
			}
		}
		return excelData;
	}

	/**
	 * Returns the header row data
	 * @param sheet
	 * @return
	 */
	public List<String> getExcelHeaderData(XSSFSheet sheet) {
		Iterator<Row> rowIterator = sheet.iterator();
		List<String> keys = new LinkedList<String>();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.print(cell.getBooleanCellValue() + "\t\t");
					break;
				case Cell.CELL_TYPE_NUMERIC:
					System.out.print(cell.getNumericCellValue() + "\t\t");
					break;
				case Cell.CELL_TYPE_STRING:
					System.out.print(cell.getStringCellValue() + "\t\t");
					keys.add(cell.getStringCellValue());
					break;
				}
			}
			break;
		}
		return keys;
	}
	
	/**
	 * Returns the total number of rows
	 * @param fileName
	 * @param sheetName
	 * @return
	 * @throws IOException
	 */
	public static int getRowsCount(String fileName, String sheetName) throws IOException {
		InputStream filePath = new FileInputStream(FilePaths.finalResultOutput + "/" + fileName);
		XSSFWorkbook workBook = new XSSFWorkbook(filePath);
		XSSFSheet sheet = workBook.getSheet(sheetName);
		Iterator<Row> rowIterator = sheet.iterator();
		int totalRowNum = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			totalRowNum = row.getRowNum();
		}
		return totalRowNum;
	}
	
	/**
	 * Returns the number of column
	 * @param fileName
	 * @param sheetName
	 * @return
	 * @throws IOException
	 */
	public static int getColumnCount(String fileName, String sheetName) throws IOException {
		InputStream filePath = new FileInputStream(FilePaths.finalResultOutput + "/" + fileName);
		XSSFWorkbook workBook = new XSSFWorkbook(filePath);
		XSSFSheet sheet = workBook.getSheet(sheetName);
		try {

			int columnCount = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum();
			return columnCount;
		}

		finally {
			System.out.println("Column count executed successfully");
		}
	}
	/**
	 * This method return the Excel result data as HasMap object
	 * @param row
	 * @param excelWorkSheet
	 * @param i
	 * @param doubleFlag
	 * @param excelData
	 * @param subIteration
	 * @return
	 */
	HashMap<String, String> getExcelValue(Row row, XSSFSheet excelWorkSheet, int i, boolean doubleFlag,
			HashMap<String, String> excelData, int subIteration) {
		String cellValue = "";
		try {
			switch (row.getCell(i).getCellType()) {
			case Cell.CELL_TYPE_STRING:
				cellValue = row.getCell(i).getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(row.getCell(i))) {
					cellValue = String.valueOf(dateFormat.format(row.getCell(i).getDateCellValue()));
				} else {
					if (doubleFlag) {
						DecimalFormat formate = new DecimalFormat("#.00");
						double value = row.getCell(i).getNumericCellValue();
						cellValue = String.valueOf(formate.format(value));
					} else
						cellValue = String.valueOf((int) row.getCell(i).getNumericCellValue());
				}
				break;
			case Cell.CELL_TYPE_BLANK:
				cellValue = "";
				break;
			case Cell.CELL_TYPE_FORMULA:
				switch (row.getCell(i).getCachedFormulaResultType()) {
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(row.getCell(i))) {
						cellValue = String.valueOf(dateFormat.format(row.getCell(i).getDateCellValue()));
					} else {
						if (doubleFlag) {
							DecimalFormat formate = new DecimalFormat("#.00");
							double value = row.getCell(i).getNumericCellValue();
							cellValue = String.valueOf(formate.format(value));
						} else
							cellValue = String.valueOf((int) row.getCell(i).getNumericCellValue());
						break;
					}
				case Cell.CELL_TYPE_STRING:
					cellValue = row.getCell(i).getStringCellValue();
					break;
				}
			}
		} catch (NullPointerException e) {
			cellValue = "";
		}
		String heading = excelWorkSheet.getRow(0).getCell(i).toString();
		excelData.put(heading, cellValue.trim());
		return excelData;
	}

}

package com.automation.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataExtraction {

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMMM-dd");
	boolean testRun = false;
	int testRunCol = 0;
	int headingRow = 0;
	ArrayList<HashMap<String, String>> excelDataArray;
	HashMap<Integer, String> pageData;
	Set<String> uniqueScenario = new LinkedHashSet<>();
	
	public ArrayList<HashMap<String, String>> getExcelDataExtraction(String fileName, String sheetName,
			boolean doubleFlag) throws IOException {
		try {
			InputStream filePath = new FileInputStream(FilePaths.testDataSheetPath + "/" + fileName);
			XSSFWorkbook excelWorkBook = new XSSFWorkbook(filePath);
			XSSFSheet excelWorkSheet = excelWorkBook.getSheet(sheetName);
			Iterator<Row> iterateRow = excelWorkSheet.iterator();
			excelDataArray = new ArrayList<HashMap<String, String>>();
			headingRow = getHeadingRow(excelWorkSheet);
			pageData = new HashMap<Integer, String>();
			if (headingRow > 0) {
				Row row1 = excelWorkSheet.getRow(headingRow - 1);
				Row row2 = excelWorkSheet.getRow(headingRow);
				int pageCount = 0;
				for (int r2 = 0; r2 < row2.getLastCellNum(); r2++) {
					if (row1.getCell(r2).getCellType() == 1) {
						pageData.put(pageCount,
								row1.getCell(r2).getStringCellValue() + ";" + row2.getCell(r2).getStringCellValue());
						pageCount++;
					} else if (row1.getCell(r2).getCellType() == 3) {
						pageData.put(pageCount - 1,
								pageData.get(pageCount - 1) + ";" + row2.getCell(r2).getStringCellValue());
					}
				}
				for (int val = 0; val < pageData.size(); val++) {
					if (!pageData.get(val).split(";")[0].equalsIgnoreCase("common"))
						uniqueScenario.add(pageData.get(val).split(";")[0]);
				}
				System.out.println(uniqueScenario);
			}
			int columnCount = excelWorkSheet.getRow(headingRow).getLastCellNum();
			Row row0 = excelWorkSheet.getRow(headingRow);
			int sNoColumnNo = 0;
			for (int firstIteration = 0; firstIteration < columnCount; firstIteration++) {
				if (row0.getCell(firstIteration).getStringCellValue().equals("S_No")) {
					sNoColumnNo = firstIteration;
					break;
				}
			}
			HashMap<String, String> excelDataDup = new HashMap<String, String>();
			int iterateFlag = 0;
			while (iterateRow.hasNext()) {
				Row row = iterateRow.next();
				HashMap<String, String> excelData = new HashMap<String, String>();

				for (int i = 0; i < columnCount; i++) {
					if (row.getRowNum() == headingRow) {
						if (row.getCell(i).getStringCellValue().equals("Test_Run")) {
							testRun = true;
							testRunCol = i;
							break;
						}
					} else {
						try {
							if (!testRun) {
								excelData = getExcelValue(row, excelWorkSheet, i, doubleFlag, excelData, 0);
							} else if (testRun && row.getCell(testRunCol).toString().equalsIgnoreCase("Yes")) {
								if ((row.getCell(sNoColumnNo).getCellType() == 0)
										|| (row.getCell(sNoColumnNo).getStringCellValue() != null
												&& !row.getCell(sNoColumnNo).getStringCellValue().equals(""))) {
									excelData.putAll(getExcelValue(row, excelWorkSheet, i, doubleFlag, excelData, 0));
								} else {
									excelData.putAll(getExcelValue(row, excelWorkSheet, i, doubleFlag, excelData,
											iterateFlag + 1));
								}
							}
						} catch (NullPointerException e) {
						}
					}
				}
				if (row.getRowNum() > headingRow) {
					if (testRun &&row.getCell(testRunCol)!=null&& row.getCell(testRunCol).getStringCellValue().equalsIgnoreCase("yes")) {
						if ((row.getCell(sNoColumnNo).getCellType() == 0)
								|| (row.getCell(sNoColumnNo).getStringCellValue() != null
										&& !row.getCell(sNoColumnNo).getStringCellValue().equals(""))) {
							iterateFlag = 0;
							excelData.put("IteratorCount", String.valueOf(iterateFlag));
							excelDataDup.clear();
							excelDataDup.putAll(excelData);
							excelDataArray.add(excelData);
						} else {
							iterateFlag++;
							excelData.put("IteratorCount", String.valueOf(iterateFlag));
							excelDataDup.putAll(excelData);
							for (int iA = 0; iA < excelDataArray.size(); iA++) {
								if (excelDataArray.get(iA).get("S_No").equals(excelDataDup.get("S_No")))
									excelDataArray.get(iA).putAll(excelDataDup);
							}
						}
					} else if (!testRun)
						excelDataArray.add(excelData);
				}
			}
			filePath.close();
			System.out.println(excelDataArray);
			return excelDataArray;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public List<String> getScenarioDriver(String testID) throws IOException {
		List<String> scenario = new ArrayList<String>();
		for (int i = 0; i < excelDataArray.size(); i++) {
			if (excelDataArray.get(i).get("S_No").equalsIgnoreCase(testID)) {
				for (int x = 0; x <= Integer.valueOf(excelDataArray.get(i).get("IteratorCount")); x++) {
					boolean newIteration = true;
					for (int x1 = 0; x1 < pageData.size(); x1++) {
						String temp = pageData.get(x1);
						String tempArray[] = temp.split(";");
						boolean flag = false;
						for (int x2 = 1; x2 < tempArray.length; x2++) {
							String temp2 = tempArray[x2];
							if (x > 0)
								temp2 = tempArray[x2] + "$*$" + x;
							if (excelDataArray.get(i).containsKey(temp2) && !tempArray[0].equalsIgnoreCase("common")) {
								if (!excelDataArray.get(i).get(temp2).equals("")) {
									if(newIteration && x !=0){
										scenario.add(tempArray[0] + ";navigateTo");
										scenario.add(tempArray[0] + ";start");
										System.out.println(tempArray[0] + " - navigateBack()");
										System.out.println(tempArray[0] + " - started()");
									} else {
										scenario.add(tempArray[0] + ";start");
										System.out.println(tempArray[0] + " - started()");
									}
									flag = true;
									newIteration = false;
									break;
								} else
									flag = false;
								/*newIteration = false;*/
							}
						}
						/*if(!flag  && !tempArray[0].equalsIgnoreCase("common"))
							if(getValidPage(i,x,x1+1) && !newIteration){
								scenario.add(tempArray[0] + ";start");
								System.out.println(tempArray[0] + " - started()");
							}*/
					}
				}
			}
		}
		return scenario;
	}
	boolean getValidPage(int i, int x, int x3){
		boolean flag = false;
		for (int x1 = x3; x1 < pageData.size(); x1++) {
			String temp = pageData.get(x1);
			String tempArray[] = temp.split(";");
			for (int x2 = 1; x2 < tempArray.length; x2++) {
				String temp2 = tempArray[x2];
				if (x > 0)
					temp2 = tempArray[x2] + "$*$" + x;
				if (excelDataArray.get(i).containsKey(temp2) && !tempArray[0].equalsIgnoreCase("common")) {
					if (!excelDataArray.get(i).get(temp2).equals("")) {
						flag = true;
						break;
					} else
						flag = false;
				}
			}
			if(flag)
				break;
		}
		return flag;
	}

	public static int getColumnCount(String fileName, String sheetName, boolean doubleFlag) throws IOException {
		try {
			InputStream filePath = new FileInputStream(FilePaths.testDataSheetPath + "/" + fileName);
			XSSFWorkbook excelWorkBook = new XSSFWorkbook(filePath);
			XSSFSheet excelWorkSheet = excelWorkBook.getSheet(sheetName);
			int columnCount = excelWorkSheet.getRow(excelWorkSheet.getFirstRowNum()).getLastCellNum();
			System.out.println("Total Columns" + columnCount);
			return columnCount;
		}

		finally {
			System.out.println("Column count executed successfully");
		}
	}

	public static String getUrl(String fileName, String sheetName, boolean doubleFlag) throws IOException {

		String urltxt = null;
		InputStream filePath = new FileInputStream(FilePaths.testDataSheetPath + "/" + fileName);
		XSSFWorkbook excelWorkBook = new XSSFWorkbook(filePath);
		XSSFSheet excelWorkSheet = excelWorkBook.getSheet(sheetName);
		CellReference ref = new CellReference("A1");
		Row r = excelWorkSheet.getRow(ref.getRow());
		if (r != null) {
			Cell c = r.getCell(ref.getCol());
			System.out.println(c.toString());
			urltxt = c.toString();
		}
		return urltxt;

	}

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
						cellValue = String.valueOf((long) row.getCell(i).getNumericCellValue());
					// System.out.println(cellValue);
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
							cellValue = String.valueOf((long) row.getCell(i).getNumericCellValue());
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
		String heading = excelWorkSheet.getRow(headingRow).getCell(i).toString();
		if (subIteration > 0) {
			if ((!heading.equals("S_No") && !heading.equals("Test_Run")) /*&& !cellValue.trim().equals("")*/)
				excelData.put(heading + "$*$" + subIteration, cellValue.trim());
		} else
			excelData.put(heading, cellValue.trim());
		return excelData;
	}

	int getScenarioIndex(String scenarioName) {
		int index = 0;
		for (String str : uniqueScenario) {
			if (str.equals(scenarioName))
				break;
			else
				index++;
		}
		return index;
	}

	String getScenarioNameByIndex(int scenarioIndex) {
		String name = "";
		int index = 0;
		for (String str : uniqueScenario) {
			if (scenarioIndex == index) {
				name = str;
				break;
			} else
				index++;
		}
		return name;
	}

	int getHeadingRow(XSSFSheet excelWorkSheet) {
		int rowValue = 0;
		Iterator<Row> iterateRow = excelWorkSheet.iterator();
		try {
			while (iterateRow.hasNext()) {
				boolean resetFlag = false;
				Row row = iterateRow.next();
				int columnValue = 0;
				if (row.getLastCellNum() > columnValue)
					columnValue = row.getLastCellNum();
				for (int i = 0; i < columnValue; i++) {
					switch (row.getCell(i).getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (row.getCell(i).getStringCellValue().equalsIgnoreCase("s_no")) {
							resetFlag = true;
						}
						break;
					}
					if (resetFlag)
						break;
				}
				if (resetFlag) {
					break;
				} else {
					rowValue++;
				}
			}
		} catch (NullPointerException e) {
		}
		return rowValue;
	}
}

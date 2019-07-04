package com.automation.utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class ConnectDB {
	public static final String fileName = "ExcelReport.xlsx";
	public static final String sheetName = "Sheet0";
	
	/***
	 * getConnection method returns  a Connection object
	 * @return returns a connection object
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://10.74.38.159:3306/ito_automation", "itoautomation",
				"itoautomation");
		return conn;
	}
	/**
	 * This method used to insert data into Database
	 * @param fileName
	 * @param sheetName
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void insertDataIntoDB(String fileName,String sheetName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {

		Connection dbConn = null;
		try {
			dbConn = getConnection();
			if (dbConn != null) {
				System.out.println(String.format("Connected to database %s " + "successfully.", dbConn.getCatalog()));
			}
			dbConn.setAutoCommit(false);

			UpdateDB_Excel excelData = new UpdateDB_Excel();
			System.out.println("Get The Complete Excel Result Result");

			PreparedStatement ps = dbConn.prepareStatement(
					"INSERT INTO automation_results_new (OrderOrgSystem, SSPOrderSubmission, Description, session, totalSteps, ISOCValidation, SubModule, MonthlyAmountwithoutPromos, Provisioning, CPEMValidation, BndnetPrcflag, VisionValidation, NCOGOrderSubmission, feature, accountNo, browserName, startTime, BundleNet, passed, TestSessionID, ISOC, MonthlyAmount, testName, BndPrcflag, EndTime, comments, IVAPPValidation, FROValidation, vzID, JiraID, JIRAAssignee, failed, DatapointValue, finalScreenShot, timeTaken, DatapointType, JIRAAppName, testId, ErrorMessage, BundlePrice, BillingCompleted, status, SSPValidation, ApplicationCode) "
							+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			int totalRows = UpdateDB_Excel.getRowsCount(fileName, sheetName);
			int i = 1;

			for (int rowCount = 1; rowCount <= totalRows; rowCount++) {
				for (Map.Entry<String, String> m : excelData.getResultData(fileName, sheetName, rowCount).entrySet()) {
					ps.setString(i, m.getValue());
					i++;
				}
				ps.executeUpdate();
				i = 1;
			}

		} catch (SQLException sqlException) {
			System.out.println("SQLException: " + sqlException.getMessage());
			System.out.println("SQLState: " + sqlException.getSQLState());
			System.out.println("VendorError: " + sqlException.getErrorCode());
		} finally {
			System.out.println("Trying to close the Connection...");
			dbConn.commit();
			dbConn.close();
			System.out.println("Whether Database Connection Closed? = " + dbConn.isClosed());
		}

	}
	/**
	 * This method is used to retrive data from database
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void retriveDataFromDB()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Connection dbConn = null;
		try {
			dbConn = getConnection();
			if (dbConn != null) {
				System.out.println(String.format("Connected to database %s " + "successfully.", dbConn.getCatalog()));
			}
			Statement st = dbConn.createStatement();
			String sql = "select * from automation_results_new";
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();

			int noOfColumn = rsmd.getColumnCount();

			if (rs != null) {
				while (rs.next()) {

					for (int i = 1; i <= noOfColumn; i++) {
						System.out.print(rs.getString(i) + "\t\t");
					}
					System.out.println("\n");
				}

			}

		} catch (SQLException sqlException) {
			System.out.println("SQLException: " + sqlException.getMessage());
			System.out.println("SQLState: " + sqlException.getSQLState());
			System.out.println("VendorError: " + sqlException.getErrorCode());
		} finally {
			System.out.println("Trying to close the Connection...");
			dbConn.close();
			System.out.println("Whether Database Connection Closed? = " + dbConn.isClosed());
		}

	}
	/**
	 * This method is used to update data into database based on update statement. For example,delete some record.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void updateDataInDB()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Connection dbConn = null;
		try {
			dbConn = getConnection();
			if (dbConn != null) {
				System.out.println(String.format("Connected to database %s " + "successfully.", dbConn.getCatalog()));
			}
			dbConn.setAutoCommit(false);
			Statement st = dbConn.createStatement();
			String sql = "delete from automation_results_new where feature=Simplex";
			st.executeUpdate(sql);

		} catch (SQLException sqlException) {
			System.out.println("SQLException: " + sqlException.getMessage());
			System.out.println("SQLState: " + sqlException.getSQLState());
			System.out.println("VendorError: " + sqlException.getErrorCode());
		} finally {
			System.out.println("Trying to close the Connection...");
			dbConn.commit();
			dbConn.close();
			System.out.println("Whether Database Connection Closed? = " + dbConn.isClosed());
		}

	}
	/**
	 * This method will readExcelData and insert data into DB.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void readExcelDataAndUpdateDB()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		//updateDataInDB();
		insertDataIntoDB(fileName,sheetName);
		//retriveDataFromDB();
	}

}

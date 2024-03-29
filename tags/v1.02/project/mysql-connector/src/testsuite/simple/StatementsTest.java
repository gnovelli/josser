/*
 Copyright (C) 2002-2004 MySQL AB

 This program is free software; you can redistribute it and/or modify
 it under the terms of version 2 of the GNU General Public License as 
 published by the Free Software Foundation.

 There are special exceptions to the terms and conditions of the GPL 
 as it is applied to this software. View the full text of the 
 exception in file EXCEPTIONS-CONNECTOR-J in the directory of this 
 software distribution.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA



 */
package testsuite.simple;

import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import testsuite.BaseTestCase;

import com.mysql.jdbc.NotImplemented;
import com.mysql.jdbc.SQLError;

/**
 * DOCUMENT ME!
 * 
 * @author Mark Matthews
 * @version $Id: StatementsTest.java 20 2008-01-17 12:47:41Z gnovelli $
 */
public class StatementsTest extends BaseTestCase {
	private static final int MAX_COLUMN_LENGTH = 255;

	private static final int MAX_COLUMNS_TO_TEST = 40;

	private static final int MIN_COLUMN_LENGTH = 10;

	private static final int STEP = 8;

	/**
	 * Runs all test cases in this test suite
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(StatementsTest.class);
	}

	/**
	 * Creates a new StatementsTest object.
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
	public StatementsTest(String name) {
		super(name);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void setUp() throws Exception {
		super.setUp();

		this.stmt.executeUpdate("DROP TABLE IF EXISTS statement_test");

		this.stmt.executeUpdate("DROP TABLE IF EXISTS statement_batch_test");

		this.stmt
				.executeUpdate("CREATE TABLE statement_test (id int not null primary key auto_increment, strdata1 varchar(255) not null, strdata2 varchar(255))");

		this.stmt.executeUpdate("CREATE TABLE statement_batch_test "
				+ "(id int not null primary key auto_increment, "
				+ "strdata1 varchar(255) not null, strdata2 varchar(255), "
				+ "UNIQUE INDEX (strdata1))");

		for (int i = 6; i < MAX_COLUMNS_TO_TEST; i += STEP) {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS statement_col_test_"
					+ i);

			StringBuffer insertBuf = new StringBuffer(
					"INSERT INTO statement_col_test_");
			StringBuffer stmtBuf = new StringBuffer(
					"CREATE TABLE IF NOT EXISTS statement_col_test_");
			stmtBuf.append(i);
			insertBuf.append(i);
			stmtBuf.append(" (");
			insertBuf.append(" VALUES (");

			boolean firstTime = true;

			for (int j = 0; j < i; j++) {
				if (!firstTime) {
					stmtBuf.append(",");
					insertBuf.append(",");
				} else {
					firstTime = false;
				}

				stmtBuf.append("col_");
				stmtBuf.append(j);
				stmtBuf.append(" VARCHAR(");
				stmtBuf.append(MAX_COLUMN_LENGTH);
				stmtBuf.append(")");
				insertBuf.append("'");

				int numChars = 16;

				for (int k = 0; k < numChars; k++) {
					insertBuf.append("A");
				}

				insertBuf.append("'");
			}

			stmtBuf.append(")");
			insertBuf.append(")");
			this.stmt.executeUpdate(stmtBuf.toString());
			this.stmt.executeUpdate(insertBuf.toString());
		}

		// explicitly set the catalog to exercise code in execute(),
		// executeQuery() and
		// executeUpdate()
		// FIXME: Only works on Windows!
		// this.conn.setCatalog(this.conn.getCatalog().toUpperCase());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void tearDown() throws Exception {
		this.stmt.executeUpdate("DROP TABLE statement_test");

		for (int i = 0; i < MAX_COLUMNS_TO_TEST; i += STEP) {
			StringBuffer stmtBuf = new StringBuffer(
					"DROP TABLE IF EXISTS statement_col_test_");
			stmtBuf.append(i);
			this.stmt.executeUpdate(stmtBuf.toString());
		}

		try {
			this.stmt.executeUpdate("DROP TABLE statement_batch_test");
		} catch (SQLException sqlEx) {
			;
		}

		super.tearDown();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public void testAccessorsAndMutators() throws SQLException {
		assertTrue("Connection can not be null, and must be same connection",
				this.stmt.getConnection() == this.conn);

		// Set max rows, to exercise code in execute(), executeQuery() and
		// executeUpdate()
		Statement accessorStmt = null;

		try {
			accessorStmt = this.conn.createStatement();
			accessorStmt.setMaxRows(1);
			accessorStmt.setMaxRows(0); // FIXME, test that this actually
			// affects rows returned
			accessorStmt.setMaxFieldSize(255);
			assertTrue("Max field size should match what was set", accessorStmt
					.getMaxFieldSize() == 255);

			try {
				accessorStmt.setMaxFieldSize(Integer.MAX_VALUE);
				fail("Should not be able to set max field size > max_packet_size");
			} catch (SQLException sqlEx) {
				;
			}

			accessorStmt.setCursorName("undef");
			accessorStmt.setEscapeProcessing(true);
			accessorStmt.setFetchDirection(java.sql.ResultSet.FETCH_FORWARD);

			int fetchDirection = accessorStmt.getFetchDirection();
			assertTrue("Set fetch direction != get fetch direction",
					fetchDirection == java.sql.ResultSet.FETCH_FORWARD);

			try {
				accessorStmt.setFetchDirection(Integer.MAX_VALUE);
				fail("Should not be able to set fetch direction to invalid value");
			} catch (SQLException sqlEx) {
				;
			}

			try {
				accessorStmt.setMaxRows(50000000 + 10);
				fail("Should not be able to set max rows > 50000000");
			} catch (SQLException sqlEx) {
				;
			}

			try {
				accessorStmt.setMaxRows(Integer.MIN_VALUE);
				fail("Should not be able to set max rows < 0");
			} catch (SQLException sqlEx) {
				;
			}

			int fetchSize = this.stmt.getFetchSize();

			try {
				accessorStmt.setMaxRows(4);
				accessorStmt.setFetchSize(Integer.MAX_VALUE);
				fail("Should not be able to set FetchSize > max rows");
			} catch (SQLException sqlEx) {
				;
			}

			try {
				accessorStmt.setFetchSize(-2);
				fail("Should not be able to set FetchSize < 0");
			} catch (SQLException sqlEx) {
				;
			}

			assertTrue(
					"Fetch size before invalid setFetchSize() calls should match fetch size now",
					fetchSize == this.stmt.getFetchSize());
		} finally {
			if (accessorStmt != null) {
				try {
					accessorStmt.close();
				} catch (SQLException sqlEx) {
					;
				}

				accessorStmt = null;
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public void testAutoIncrement() throws SQLException {
		try {
			this.stmt = this.conn.createStatement(
					java.sql.ResultSet.TYPE_FORWARD_ONLY,
					java.sql.ResultSet.CONCUR_READ_ONLY);
			this.stmt.setFetchSize(Integer.MIN_VALUE);
			this.stmt
					.executeUpdate("INSERT INTO statement_test (strdata1) values ('blah')");

			int autoIncKeyFromApi = -1;
			this.rs = this.stmt.getGeneratedKeys();

			if (this.rs.next()) {
				autoIncKeyFromApi = this.rs.getInt(1);
			} else {
				fail("Failed to retrieve AUTO_INCREMENT using Statement.getGeneratedKeys()");
			}

			this.rs.close();

			int autoIncKeyFromFunc = -1;
			this.rs = this.stmt.executeQuery("SELECT LAST_INSERT_ID()");

			if (this.rs.next()) {
				autoIncKeyFromFunc = this.rs.getInt(1);
			} else {
				fail("Failed to retrieve AUTO_INCREMENT using LAST_INSERT_ID()");
			}

			if ((autoIncKeyFromApi != -1) && (autoIncKeyFromFunc != -1)) {
				assertTrue(
						"Key retrieved from API ("
								+ autoIncKeyFromApi
								+ ") does not match key retrieved from LAST_INSERT_ID() "
								+ autoIncKeyFromFunc + ") function",
						autoIncKeyFromApi == autoIncKeyFromFunc);
			} else {
				fail("AutoIncrement keys were '0'");
			}
		} finally {
			if (this.rs != null) {
				try {
					this.rs.close();
				} catch (Exception ex) { /* ignore */
					;
				}
			}

			this.rs = null;
		}
	}

	/**
	 * Tests all variants of numerical types (signed/unsigned) for correct
	 * operation when used as return values from a prepared statement.
	 * 
	 * @throws Exception
	 */
	public void testBinaryResultSetNumericTypes() throws Exception {
		/*
		 * TINYINT 1 -128 127 SMALLINT 2 -32768 32767 MEDIUMINT 3 -8388608
		 * 8388607 INT 4 -2147483648 2147483647 BIGINT 8 -9223372036854775808
		 * 9223372036854775807
		 */

		String unsignedMinimum = "0";

		String tiMinimum = "-128";
		String tiMaximum = "127";
		String utiMaximum = "255";

		String siMinimum = "-32768";
		String siMaximum = "32767";
		String usiMaximum = "65535";

		String miMinimum = "-8388608";
		String miMaximum = "8388607";
		String umiMaximum = "16777215";

		String iMinimum = "-2147483648";
		String iMaximum = "2147483647";
		String uiMaximum = "4294967295";

		String biMinimum = "-9223372036854775808";
		String biMaximum = "9223372036854775807";
		String ubiMaximum = "18446744073709551615";

		try {
			this.stmt
					.executeUpdate("DROP TABLE IF EXISTS testBinaryResultSetNumericTypes");
			this.stmt
					.executeUpdate("CREATE TABLE testBinaryResultSetNumericTypes(rowOrder TINYINT, ti TINYINT,"
							+ "uti TINYINT UNSIGNED, si SMALLINT,"
							+ "usi SMALLINT UNSIGNED, mi MEDIUMINT,"
							+ "umi MEDIUMINT UNSIGNED, i INT, ui INT UNSIGNED,"
							+ "bi BIGINT, ubi BIGINT UNSIGNED)");
			PreparedStatement inserter = this.conn
					.prepareStatement("INSERT INTO testBinaryResultSetNumericTypes VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			inserter.setInt(1, 0);
			inserter.setString(2, tiMinimum);
			inserter.setString(3, unsignedMinimum);
			inserter.setString(4, siMinimum);
			inserter.setString(5, unsignedMinimum);
			inserter.setString(6, miMinimum);
			inserter.setString(7, unsignedMinimum);
			inserter.setString(8, iMinimum);
			inserter.setString(9, unsignedMinimum);
			inserter.setString(10, biMinimum);
			inserter.setString(11, unsignedMinimum);
			inserter.executeUpdate();

			inserter.setInt(1, 1);
			inserter.setString(2, tiMaximum);
			inserter.setString(3, utiMaximum);
			inserter.setString(4, siMaximum);
			inserter.setString(5, usiMaximum);
			inserter.setString(6, miMaximum);
			inserter.setString(7, umiMaximum);
			inserter.setString(8, iMaximum);
			inserter.setString(9, uiMaximum);
			inserter.setString(10, biMaximum);
			inserter.setString(11, ubiMaximum);
			inserter.executeUpdate();

			PreparedStatement selector = this.conn
					.prepareStatement("SELECT * FROM testBinaryResultSetNumericTypes ORDER by rowOrder ASC");
			this.rs = selector.executeQuery();

			assertTrue(this.rs.next());

			assertTrue(this.rs.getString(2).equals(tiMinimum));
			assertTrue(this.rs.getString(3).equals(unsignedMinimum));
			assertTrue(this.rs.getString(4).equals(siMinimum));
			assertTrue(this.rs.getString(5).equals(unsignedMinimum));
			assertTrue(this.rs.getString(6).equals(miMinimum));
			assertTrue(this.rs.getString(7).equals(unsignedMinimum));
			assertTrue(this.rs.getString(8).equals(iMinimum));
			assertTrue(this.rs.getString(9).equals(unsignedMinimum));
			assertTrue(this.rs.getString(10).equals(biMinimum));
			assertTrue(this.rs.getString(11).equals(unsignedMinimum));

			assertTrue(this.rs.next());

			assertTrue(this.rs.getString(2) + " != " + tiMaximum, this.rs
					.getString(2).equals(tiMaximum));
			assertTrue(this.rs.getString(3) + " != " + utiMaximum, this.rs
					.getString(3).equals(utiMaximum));
			assertTrue(this.rs.getString(4) + " != " + siMaximum, this.rs
					.getString(4).equals(siMaximum));
			assertTrue(this.rs.getString(5) + " != " + usiMaximum, this.rs
					.getString(5).equals(usiMaximum));
			assertTrue(this.rs.getString(6) + " != " + miMaximum, this.rs
					.getString(6).equals(miMaximum));
			assertTrue(this.rs.getString(7) + " != " + umiMaximum, this.rs
					.getString(7).equals(umiMaximum));
			assertTrue(this.rs.getString(8) + " != " + iMaximum, this.rs
					.getString(8).equals(iMaximum));
			assertTrue(this.rs.getString(9) + " != " + uiMaximum, this.rs
					.getString(9).equals(uiMaximum));
			assertTrue(this.rs.getString(10) + " != " + biMaximum, this.rs
					.getString(10).equals(biMaximum));
			assertTrue(this.rs.getString(11) + " != " + ubiMaximum, this.rs
					.getString(11).equals(ubiMaximum));

			assertTrue(!this.rs.next());
		} finally {
			this.stmt
					.executeUpdate("DROP TABLE IF EXISTS testBinaryResultSetNumericTypes");
		}
	}

	/**
	 * Tests stored procedure functionality
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
	public void testCallableStatement() throws Exception {
		if (versionMeetsMinimum(5, 0)) {
			CallableStatement cStmt = null;
			String stringVal = "abcdefg";
			int intVal = 42;

			try {
				try {
					this.stmt.executeUpdate("DROP PROCEDURE testCallStmt");
				} catch (SQLException sqlEx) {
					if (sqlEx.getMessage().indexOf("does not exist") == -1) {
						throw sqlEx;
					}
				}

				this.stmt.executeUpdate("DROP TABLE IF EXISTS callStmtTbl");
				this.stmt
						.executeUpdate("CREATE TABLE callStmtTbl (x CHAR(16), y INT)");

				this.stmt
						.executeUpdate("CREATE PROCEDURE testCallStmt(n INT, x CHAR(16), y INT)"
								+ " WHILE n DO"
								+ "    SET n = n - 1;"
								+ "    INSERT INTO callStmtTbl VALUES (x, y);"
								+ " END WHILE;");

				int rowsToCheck = 15;

				cStmt = this.conn.prepareCall("{call testCallStmt(?,?,?)}");
				cStmt.setInt(1, rowsToCheck);
				cStmt.setString(2, stringVal);
				cStmt.setInt(3, intVal);
				cStmt.execute();

				this.rs = this.stmt.executeQuery("SELECT x,y FROM callStmtTbl");

				int numRows = 0;

				while (this.rs.next()) {
					assertTrue(this.rs.getString(1).equals(stringVal)
							&& (this.rs.getInt(2) == intVal));

					numRows++;
				}

				this.rs.close();
				this.rs = null;

				cStmt.close();
				cStmt = null;

				System.out.println(rowsToCheck + " rows returned");

				assertTrue(numRows == rowsToCheck);
			} finally {
				try {
					this.stmt.executeUpdate("DROP PROCEDURE testCallStmt");
				} catch (SQLException sqlEx) {
					if (sqlEx.getMessage().indexOf("does not exist") == -1) {
						throw sqlEx;
					}
				}

				this.stmt.executeUpdate("DROP TABLE IF EXISTS callStmtTbl");

				if (cStmt != null) {
					cStmt.close();
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public void testClose() throws SQLException {
		Statement closeStmt = null;
		boolean exceptionAfterClosed = false;

		try {
			closeStmt = this.conn.createStatement();
			closeStmt.close();

			try {
				closeStmt.executeQuery("SELECT 1");
			} catch (SQLException sqlEx) {
				exceptionAfterClosed = true;
			}
		} finally {
			if (closeStmt != null) {
				try {
					closeStmt.close();
				} catch (SQLException sqlEx) {
					/* ignore */
				}
			}

			closeStmt = null;
		}

		assertTrue(
				"Operations not allowed on Statement after .close() is called!",
				exceptionAfterClosed);
	}

	public void testEnableStreamingResults() throws Exception {
		Statement streamStmt = this.conn.createStatement();
		((com.mysql.jdbc.Statement) streamStmt).enableStreamingResults();
		assertEquals(streamStmt.getFetchSize(), Integer.MIN_VALUE);
		assertEquals(streamStmt.getResultSetType(), ResultSet.TYPE_FORWARD_ONLY);
	}

	public void testHoldingResultSetsOverClose() throws Exception {
		Properties props = new Properties();
		props.setProperty("holdResultsOpenOverStatementClose", "true");

		Connection conn2 = getConnectionWithProps(props);

		Statement stmt2 = null;
		PreparedStatement pstmt2 = null;

		try {
			stmt2 = conn2.createStatement();

			this.rs = stmt2.executeQuery("SELECT 1");
			this.rs.next();
			this.rs.getInt(1);
			stmt2.close();
			this.rs.getInt(1);

			stmt2 = conn2.createStatement();
			stmt2.execute("SELECT 1");
			this.rs = stmt2.getResultSet();
			this.rs.next();
			this.rs.getInt(1);
			stmt2.execute("SELECT 2");
			this.rs.getInt(1);

			pstmt2 = conn2.prepareStatement("SELECT 1");
			this.rs = pstmt2.executeQuery();
			this.rs.next();
			this.rs.getInt(1);
			pstmt2.close();
			this.rs.getInt(1);

			pstmt2 = conn2.prepareStatement("SELECT 1");
			this.rs = pstmt2.executeQuery();
			this.rs.next();
			this.rs.getInt(1);
			pstmt2.executeQuery();
			this.rs.getInt(1);
			pstmt2.execute();
			this.rs.getInt(1);

			pstmt2 = ((com.mysql.jdbc.Connection) conn2)
					.clientPrepareStatement("SELECT 1");
			this.rs = pstmt2.executeQuery();
			this.rs.next();
			this.rs.getInt(1);
			pstmt2.close();
			this.rs.getInt(1);

			pstmt2 = ((com.mysql.jdbc.Connection) conn2)
					.clientPrepareStatement("SELECT 1");
			this.rs = pstmt2.executeQuery();
			this.rs.next();
			this.rs.getInt(1);
			pstmt2.executeQuery();
			this.rs.getInt(1);
			pstmt2.execute();
			this.rs.getInt(1);

			stmt2 = conn2.createStatement();
			this.rs = stmt2.executeQuery("SELECT 1");
			this.rs.next();
			this.rs.getInt(1);
			stmt2.executeQuery("SELECT 2");
			this.rs.getInt(1);
			this.rs = stmt2.executeQuery("SELECT 1");
			this.rs.next();
			this.rs.getInt(1);
			stmt2.executeUpdate("SET @var=1");
			this.rs.getInt(1);
			stmt2.execute("SET @var=2");
			this.rs.getInt(1);
		} finally {
			if (stmt2 != null) {
				stmt2.close();
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public void testInsert() throws SQLException {
		try {
			boolean autoCommit = this.conn.getAutoCommit();

			// Test running a query for an update. It should fail.
			try {
				this.conn.setAutoCommit(false);
				this.stmt.executeUpdate("SELECT * FROM statement_test");
			} catch (SQLException sqlEx) {
				assertTrue("Exception thrown for unknown reason", sqlEx
						.getSQLState().equalsIgnoreCase("01S03"));
			} finally {
				this.conn.setAutoCommit(autoCommit);
			}

			// Test running a update for an query. It should fail.
			try {
				this.conn.setAutoCommit(false);
				this.stmt
						.executeQuery("UPDATE statement_test SET strdata1='blah' WHERE 1=0");
			} catch (SQLException sqlEx) {
				assertTrue("Exception thrown for unknown reason", sqlEx
						.getSQLState().equalsIgnoreCase(
								SQLError.SQL_STATE_ILLEGAL_ARGUMENT));
			} finally {
				this.conn.setAutoCommit(autoCommit);
			}

			for (int i = 0; i < 10; i++) {
				int updateCount = this.stmt
						.executeUpdate("INSERT INTO statement_test (strdata1,strdata2) values ('abcdefg', 'poi')");
				assertTrue("Update count must be '1', was '" + updateCount
						+ "'", (updateCount == 1));
			}

			this.stmt
					.executeUpdate("INSERT INTO statement_test (strdata1, strdata2) values ('a', 'a'), ('b', 'b'), ('c', 'c')");
			this.rs = this.stmt.getGeneratedKeys();

			if (this.rs.next()) {
				this.rs.getInt(1);
			}

			this.rs.close();
			this.rs = this.stmt.executeQuery("SELECT LAST_INSERT_ID()");

			int updateCountFromServer = 0;

			if (this.rs.next()) {
				updateCountFromServer = this.rs.getInt(1);
			}

			System.out.println("Update count from server: "
					+ updateCountFromServer);
		} finally {
			if (this.rs != null) {
				try {
					this.rs.close();
				} catch (Exception ex) { /* ignore */
					;
				}
			}

			this.rs = null;
		}
	}

	/**
	 * Tests multiple statement support
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public void testMultiStatements() throws Exception {
		if (versionMeetsMinimum(4, 1)) {
			Connection multiStmtConn = null;
			Statement multiStmt = null;

			try {
				Properties props = new Properties();
				props.setProperty("allowMultiQueries", "true");

				multiStmtConn = getConnectionWithProps(props);

				multiStmt = multiStmtConn.createStatement();

				multiStmt
						.executeUpdate("DROP TABLE IF EXISTS testMultiStatements");
				multiStmt
						.executeUpdate("CREATE TABLE testMultiStatements (field1 VARCHAR(255), field2 INT, field3 DOUBLE)");
				multiStmt
						.executeUpdate("INSERT INTO testMultiStatements VALUES ('abcd', 1, 2)");

				multiStmt
						.execute("SELECT field1 FROM testMultiStatements WHERE field1='abcd';"
								+ "UPDATE testMultiStatements SET field3=3;"
								+ "SELECT field3 FROM testMultiStatements WHERE field3=3");

				this.rs = multiStmt.getResultSet();

				assertTrue(this.rs.next());

				assertTrue("abcd".equals(this.rs.getString(1)));
				this.rs.close();

				// Next should be an update count...
				assertTrue(!multiStmt.getMoreResults());

				assertTrue("Update count was " + multiStmt.getUpdateCount()
						+ ", expected 1", multiStmt.getUpdateCount() == 1);

				assertTrue(multiStmt.getMoreResults());

				this.rs = multiStmt.getResultSet();

				assertTrue(this.rs.next());

				assertTrue(this.rs.getDouble(1) == 3);

				// End of multi results
				assertTrue(!multiStmt.getMoreResults());
				assertTrue(multiStmt.getUpdateCount() == -1);
			} finally {
				if (multiStmt != null) {
					multiStmt
							.executeUpdate("DROP TABLE IF EXISTS testMultiStatements");

					multiStmt.close();
				}

				if (multiStmtConn != null) {
					multiStmtConn.close();
				}
			}
		}
	}

	/**
	 * Tests that NULLs and '' work correctly.
	 * 
	 * @throws SQLException
	 *             if an error occurs
	 */
	public void testNulls() throws SQLException {
		try {
			this.stmt.executeUpdate("DROP TABLE IF EXISTS nullTest");
			this.stmt
					.executeUpdate("CREATE TABLE IF NOT EXISTS nullTest (field_1 CHAR(20), rowOrder INT)");
			this.stmt
					.executeUpdate("INSERT INTO nullTest VALUES (null, 1), ('', 2)");

			this.rs = this.stmt
					.executeQuery("SELECT field_1 FROM nullTest ORDER BY rowOrder");

			this.rs.next();

			assertTrue("NULL field not returned as NULL", (this.rs
					.getString("field_1") == null)
					&& this.rs.wasNull());

			this.rs.next();

			assertTrue("Empty field not returned as \"\"", this.rs.getString(
					"field_1").equals("")
					&& !this.rs.wasNull());

			this.rs.close();
		} finally {
			if (this.rs != null) {
				try {
					this.rs.close();
				} catch (Exception ex) {
					// ignore
				}
			}

			this.stmt.executeUpdate("DROP TABLE IF EXISTS nullTest");
		}
	}

	public void testParsedConversionWarning() throws Exception {
		if (versionMeetsMinimum(4, 1)) {
			try {
				Properties props = new Properties();
				props.setProperty("useUsageAdvisor", "true");
				Connection warnConn = getConnectionWithProps(props);

				this.stmt
						.executeUpdate("DROP TABLE IF EXISTS testParsedConversionWarning");
				this.stmt
						.executeUpdate("CREATE TABLE testParsedConversionWarning(field1 VARCHAR(255))");
				this.stmt
						.executeUpdate("INSERT INTO testParsedConversionWarning VALUES ('1.0')");

				PreparedStatement badStmt = warnConn
						.prepareStatement("SELECT field1 FROM testParsedConversionWarning");

				this.rs = badStmt.executeQuery();
				assertTrue(this.rs.next());
				this.rs.getFloat(1);
			} finally {
				this.stmt
						.executeUpdate("DROP TABLE IF EXISTS testParsedConversionWarning");
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public void testPreparedStatement() throws SQLException {
		this.stmt
				.executeUpdate("INSERT INTO statement_test (id, strdata1,strdata2) values (999,'abcdefg', 'poi')");
		this.pstmt = this.conn
				.prepareStatement("UPDATE statement_test SET strdata1=?, strdata2=? where id=999");
		this.pstmt.setString(1, "iop");
		this.pstmt.setString(2, "higjklmn");

		// pstmt.setInt(3, 999);
		int updateCount = this.pstmt.executeUpdate();
		assertTrue("Update count must be '1', was '" + updateCount + "'",
				(updateCount == 1));

		this.pstmt.clearParameters();

		this.pstmt.close();

		this.rs = this.stmt
				.executeQuery("SELECT id, strdata1, strdata2 FROM statement_test");

		assertTrue(this.rs.next());
		assertTrue(this.rs.getInt(1) == 999);
		assertTrue("Expected 'iop', received '" + this.rs.getString(2) + "'",
				"iop".equals(this.rs.getString(2)));
		assertTrue("Expected 'higjklmn', received '" + this.rs.getString(3)
				+ "'", "higjklmn".equals(this.rs.getString(3)));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public void testPreparedStatementBatch() throws SQLException {
		this.pstmt = this.conn.prepareStatement("INSERT INTO "
				+ "statement_batch_test (strdata1, strdata2) VALUES (?,?)");

		for (int i = 0; i < 1000; i++) {
			this.pstmt.setString(1, "batch_" + i);
			this.pstmt.setString(2, "batch_" + i);
			this.pstmt.addBatch();
		}

		int[] updateCounts = this.pstmt.executeBatch();

		for (int i = 0; i < updateCounts.length; i++) {
			assertTrue("Update count must be '1', was '" + updateCounts[i]
					+ "'", (updateCounts[i] == 1));
		}
	}

	public void testStatementRewriteBatch() throws SQLException {
		Properties props = new Properties();
		props.setProperty("rewriteBatchedStatements", "true");
		Connection multiConn = getConnectionWithProps(props);
		createTable("testStatementRewriteBatch", "(pk_field INT PRIMARY KEY NOT NULL AUTO_INCREMENT, field1 INT)");
		Statement multiStmt = multiConn.createStatement();
		multiStmt.addBatch("INSERT INTO testStatementRewriteBatch(field1) VALUES (1)");
		multiStmt.addBatch("INSERT INTO testStatementRewriteBatch(field1) VALUES (2)");
		multiStmt.addBatch("INSERT INTO testStatementRewriteBatch(field1) VALUES (3)");
		multiStmt.addBatch("INSERT INTO testStatementRewriteBatch(field1) VALUES (4)");
		multiStmt.addBatch("UPDATE testStatementRewriteBatch SET field1=5 WHERE field1=1");
		multiStmt.addBatch("UPDATE testStatementRewriteBatch SET field1=6 WHERE field1=2 OR field1=3");
		
		int[] counts = multiStmt.executeBatch();
		ResultSet genKeys = multiStmt.getGeneratedKeys();
		
		for (int i = 1; i < 5; i++) {
			genKeys.next();
			assertEquals(i, genKeys.getInt(1));
		}
		
		assertEquals(counts.length, 6);
		assertEquals(counts[0], 1);
		assertEquals(counts[1], 1);
		assertEquals(counts[2], 1);
		assertEquals(counts[3], 1);
		assertEquals(counts[4], 1);
		assertEquals(counts[5], 2);
		
		this.rs = multiStmt.executeQuery("SELECT field1 FROM testStatementRewriteBatch ORDER BY field1");
		assertTrue(this.rs.next());
		assertEquals(this.rs.getInt(1), 4);
		assertTrue(this.rs.next());
		assertEquals(this.rs.getInt(1), 5);
		assertTrue(this.rs.next());
		assertEquals(this.rs.getInt(1), 6);
		assertTrue(this.rs.next());
		assertEquals(this.rs.getInt(1), 6);

		createTable("testStatementRewriteBatch", "(pk_field INT PRIMARY KEY NOT NULL AUTO_INCREMENT, field1 INT)");
		props.clear();
		props.setProperty("rewriteBatchedStatements", "true");
		props.setProperty("sessionVariables", "max_allowed_packet=1024");
		multiConn = getConnectionWithProps(props);
		multiStmt = multiConn.createStatement();
		
		for (int i = 0; i < 1000; i++) {
			multiStmt.addBatch("INSERT INTO testStatementRewriteBatch(field1) VALUES (" + i + ")");
		}
		
		multiStmt.executeBatch();
		genKeys = multiStmt.getGeneratedKeys();
		
		for (int i = 1; i < 1000; i++) {
			genKeys.next();
			assertEquals(i, genKeys.getInt(1));
		}
		
		createTable("testStatementRewriteBatch", "(pk_field INT PRIMARY KEY NOT NULL AUTO_INCREMENT, field1 INT)");
		
		props.clear();
		props.setProperty("useServerPrepStmts", "false");
		props.setProperty("rewriteBatchedStatements", "true");
		multiConn = getConnectionWithProps(props);
		PreparedStatement pStmt = multiConn.prepareStatement("INSERT INTO testStatementRewriteBatch(field1) VALUES (?)", 
				Statement.RETURN_GENERATED_KEYS);
		
		for (int i = 0; i < 1000; i++) {
			pStmt.setInt(1, i);
			pStmt.addBatch();
		}
		
		pStmt.executeBatch();
		genKeys = pStmt.getGeneratedKeys();
		
		for (int i = 1; i < 1000; i++) {
			genKeys.next();
			assertEquals(i, genKeys.getInt(1));
		}
		
		createTable("testStatementRewriteBatch", "(pk_field INT PRIMARY KEY NOT NULL AUTO_INCREMENT, field1 INT)");
		props.setProperty("useServerPrepStmts", "false");
		props.setProperty("rewriteBatchedStatements", "true");
		props.setProperty("sessionVariables", "max_allowed_packet=1024");
		multiConn = getConnectionWithProps(props);
		pStmt = multiConn.prepareStatement("INSERT INTO testStatementRewriteBatch(field1) VALUES (?)", 
				Statement.RETURN_GENERATED_KEYS);
		
		for (int i = 0; i < 1000; i++) {
			pStmt.setInt(1, i);
			pStmt.addBatch();
		}
		
		pStmt.executeBatch();
		genKeys = pStmt.getGeneratedKeys();
		
		for (int i = 1; i < 1000; i++) {
			genKeys.next();
			assertEquals(i, genKeys.getInt(1));
		}
	}
	
	/**
	 * DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public void testSelectColumns() throws SQLException {
		for (int i = 6; i < MAX_COLUMNS_TO_TEST; i += STEP) {
			long start = System.currentTimeMillis();
			this.rs = this.stmt
					.executeQuery("SELECT * from statement_col_test_" + i);

			if (this.rs.next()) {
				;
			}

			long end = System.currentTimeMillis();
			System.out.println(i + " columns = " + (end - start) + " ms");
		}
	}

	public void testStreamChange() throws Exception {
		createTable("testStreamChange",
				"(field1 varchar(32), field2 int, field3 TEXT, field4 BLOB)");
		this.pstmt = this.conn
				.prepareStatement("INSERT INTO testStreamChange VALUES (?, ?, ?, ?)");

		try {
			this.pstmt.setString(1, "A");
			this.pstmt.setInt(2, 1);

			char[] cArray = { 'A', 'B', 'C' };
			Reader r = new CharArrayReader(cArray);
			this.pstmt.setCharacterStream(3, r, cArray.length);

			byte[] bArray = { 'D', 'E', 'F' };
			ByteArrayInputStream bais = new ByteArrayInputStream(bArray);
			this.pstmt.setBinaryStream(4, bais, bArray.length);

			assertEquals(1, this.pstmt.executeUpdate());

			this.rs = this.stmt
					.executeQuery("SELECT field3, field4 from testStreamChange where field1='A'");
			this.rs.next();
			assertEquals("ABC", this.rs.getString(1));
			assertEquals("DEF", this.rs.getString(2));

			char[] ucArray = { 'C', 'E', 'S', 'U' };
			this.pstmt.setString(1, "CESU");
			this.pstmt.setInt(2, 3);
			Reader ucReader = new CharArrayReader(ucArray);
			this.pstmt.setCharacterStream(3, ucReader, ucArray.length);
			this.pstmt.setBinaryStream(4, null, 0);
			assertEquals(1, this.pstmt.executeUpdate());

			this.rs = this.stmt
					.executeQuery("SELECT field3, field4 from testStreamChange where field1='CESU'");
			this.rs.next();
			assertEquals("CESU", this.rs.getString(1));
			assertEquals(null, this.rs.getString(2));
		} finally {
			if (this.rs != null) {
				this.rs.close();
				this.rs = null;
			}

			if (this.pstmt != null) {
				this.pstmt.close();
				this.pstmt = null;
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @throws SQLException
	 *             DOCUMENT ME!
	 */
	public void testStubbed() throws SQLException {
		try {
			this.stmt.getResultSetHoldability();
		} catch (NotImplemented notImplEx) {
			;
		}
	}

	// Server-side prepared statements can only reset streamed data
	// in-toto, not piecemiel.

	public void testTruncationOnRead() throws Exception {
		this.rs = this.stmt.executeQuery("SELECT '" + Long.MAX_VALUE + "'");
		this.rs.next();

		try {
			this.rs.getByte(1);
			fail("Should've thrown an out-of-range exception");
		} catch (SQLException sqlEx) {
			assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
					.equals(sqlEx.getSQLState()));
		}

		try {
			this.rs.getShort(1);
			fail("Should've thrown an out-of-range exception");
		} catch (SQLException sqlEx) {
			assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
					.equals(sqlEx.getSQLState()));
		}

		try {
			this.rs.getInt(1);
			fail("Should've thrown an out-of-range exception");
		} catch (SQLException sqlEx) {
			assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
					.equals(sqlEx.getSQLState()));
		}

		this.rs = this.stmt.executeQuery("SELECT '" + Double.MAX_VALUE + "'");

		this.rs.next();

		try {
			this.rs.getByte(1);
			fail("Should've thrown an out-of-range exception");
		} catch (SQLException sqlEx) {
			assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
					.equals(sqlEx.getSQLState()));
		}

		try {
			this.rs.getShort(1);
			fail("Should've thrown an out-of-range exception");
		} catch (SQLException sqlEx) {
			assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
					.equals(sqlEx.getSQLState()));
		}

		try {
			this.rs.getInt(1);
			fail("Should've thrown an out-of-range exception");
		} catch (SQLException sqlEx) {
			assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
					.equals(sqlEx.getSQLState()));
		}

		try {
			this.rs.getLong(1);
			fail("Should've thrown an out-of-range exception");
		} catch (SQLException sqlEx) {
			assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
					.equals(sqlEx.getSQLState()));
		}

		try {
			this.rs.getLong(1);
			fail("Should've thrown an out-of-range exception");
		} catch (SQLException sqlEx) {
			assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
					.equals(sqlEx.getSQLState()));
		}

		PreparedStatement pStmt = null;

		System.out
				.println("Testing prepared statements with binary result sets now");

		try {
			this.stmt
					.executeUpdate("DROP TABLE IF EXISTS testTruncationOnRead");
			this.stmt
					.executeUpdate("CREATE TABLE testTruncationOnRead(intField INTEGER, bigintField BIGINT, doubleField DOUBLE)");
			this.stmt.executeUpdate("INSERT INTO testTruncationOnRead VALUES ("
					+ Integer.MAX_VALUE + ", " + Long.MAX_VALUE + ", "
					+ Double.MAX_VALUE + ")");
			this.stmt.executeUpdate("INSERT INTO testTruncationOnRead VALUES ("
					+ Integer.MIN_VALUE + ", " + Long.MIN_VALUE + ", "
					+ Double.MIN_VALUE + ")");

			pStmt = this.conn
					.prepareStatement("SELECT intField, bigintField, doubleField FROM testTruncationOnRead ORDER BY intField DESC");
			this.rs = pStmt.executeQuery();

			this.rs.next();

			try {
				this.rs.getByte(1);
				fail("Should've thrown an out-of-range exception");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
						.equals(sqlEx.getSQLState()));
			}

			try {
				this.rs.getInt(2);
				fail("Should've thrown an out-of-range exception");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
						.equals(sqlEx.getSQLState()));
			}

			try {
				this.rs.getLong(3);
				fail("Should've thrown an out-of-range exception");
			} catch (SQLException sqlEx) {
				assertTrue(SQLError.SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE
						.equals(sqlEx.getSQLState()));
			}
		} finally {
			this.stmt
					.executeUpdate("DROP TABLE IF EXISTS testTruncationOnRead");
		}

	}
}

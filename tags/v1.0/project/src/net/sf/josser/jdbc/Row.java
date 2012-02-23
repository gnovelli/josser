/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:       JOSSER
 *
 * Description: JOSSER - A Java Tool capable to parse DMOZ RDF dumps and export them to 
 *              any JDBC compliant relational database 
 *               
 * Row.java
 *
 * Created on 22 October 2005, 22.00 by Giovanni Novelli
 *
 ****************************************************************************************
 * JOSSER is available under the terms of the GNU General Public License Version 2.    
 *                                                                                      
 * The author does NOT allow redistribution of modifications of JOSSER under the terms 
 * of the GNU General Public License Version 3 or any later version.                   
 *                                                                                     
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY     
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A     
 * PARTICULAR PURPOSE.                                                                 
 *                                                                                     
 * For more details read file LICENSE
 *****************************************************************************************
 *
 * $Revision: 92 $
 * $Id: Row.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/Row.java $
 *
 *****************************************************************************************
 */
package net.sf.josser.jdbc;

import java.sql.PreparedStatement;

import net.sf.josser.Josser;
import net.sf.josser.util.Static;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public abstract class Row implements IRow {
	private String tablename = "";

	public String getFields() {
		return null;
	}

	public abstract void setValues();

	public abstract String getValues();

	public int store() {
		int result = 0;
		String fields = null;
		String values = null;
		String sql = null;
		PreparedStatement stmt = null;
		fields = this.getFields();
		values = this.getValues();
		sql = "INSERT INTO " + this.getTablename() + " " + fields + "\n"
				+ "VALUES ( " + values + " );";
		try {
			stmt = Josser.getConnection().prepareStatement(sql);
			this.setValues();
			result = stmt.executeUpdate();
			stmt.close();
		} catch (final Exception e) {
			e.printStackTrace(System.err);
		}
		return result;
	}

	protected abstract void setStmt(PreparedStatement stmt);

	protected abstract PreparedStatement getStmt();

	public PreparedStatement getPreparedStatement() {
		if (this.getStmt() == null) {
			String fields = null;
			String values = null;
			String sql = null;
			fields = this.getFields();
			values = this.getValues();
			sql = "INSERT INTO " + this.getTablename() + " " + fields + "\n"
					+ "VALUES ( " + values + " );";
			try {
				this.setStmt(Josser.getConnection().prepareStatement(sql));
				return this.getStmt();
			} catch (final Exception e) {
				e.printStackTrace(System.err);
				return this.getStmt();
			}
		} else {
			return this.getStmt();
		}
	}

	public int addBatch() {
		int result = 0;
		if (Static.isFiltermatching()) {
			try {
				this.setValues();
				this.getStmt().addBatch();
			} catch (final Exception e) {
				e.printStackTrace(System.err);
				result = -1;
			}
		}
		return result;
	}

	public int executeBatch() {
		int result = 0;
		try {
			if (this.getStmt() != null) {
				this.getStmt().executeBatch();
			}
		} catch (final Exception e) {
			e.printStackTrace(System.err);
			result = -1;
		}
		return result;
	}

	public int batchClear() {
		int result = 0;
		try {
			if (this.getStmt() != null) {
				this.getStmt().clearBatch();
			}
		} catch (final Exception e) {
			e.printStackTrace(System.err);
			result = -1;
		}
		return result;
	}

	public int batchStore() {
		int result = 0;
		try {
			this.getStmt().executeBatch();
		} catch (final Exception e) {
			e.printStackTrace(System.err);
			result = -1;
		}
		return result;
	}

	/**
	 * @param tablename
	 *            The tablename to set.
	 */
	public void setTablename(final String tablename) {
		this.tablename = tablename;
	}

	/**
	 * @return Returns the tablename.
	 */
	public String getTablename() {
		return this.tablename;
	}
}

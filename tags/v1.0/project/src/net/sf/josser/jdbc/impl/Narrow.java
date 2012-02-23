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
 * Narrow.java
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
 * $Id: Narrow.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/impl/Narrow.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;

import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Narrow extends Row {
	private String narrow = null;

	private int priority = 0;

	private int catid = 0;

	private int ncatid = 0;

	private static PreparedStatement stmt = null;

	public Narrow() {
		this.setTablename("dmoz_narrows");
	}

	public Narrow(final String letterbar, final int catid, final int ncatid) {
		this.setTablename("dmoz_narrows");
		this.setNarrow(letterbar);
		this.setCatid(catid);
		this.setNcatid(ncatid);
	}

	@Override
	public String getFields() {
		String temp = "(";
		temp = temp + " narrow,";
		temp = temp + " priority, ";
		temp = temp + " catid, ";
		temp = temp + " ncatid ";
		temp = temp + ")";
		return temp;
	}

	@Override
	public void setValues() {
		try {
			this.getPreparedStatement().setString(1, this.getNarrow());
			this.getPreparedStatement().setInt(2, this.getPriority());
			this.getPreparedStatement().setInt(3, this.getCatid());
			this.getPreparedStatement().setInt(4, this.getNcatid());
		} catch (final Exception e) {
		}
	}

	@Override
	public String getValues() {
		String temp = "";
		temp = temp + "?,";
		temp = temp + "?,";
		temp = temp + "?,";
		temp = temp + "?";
		return temp;
	}

	/**
	 * @param catid
	 *            The catid to set.
	 */
	public void setCatid(final int catid) {
		this.catid = catid;
	}

	/**
	 * @return Returns the catid.
	 */
	public int getCatid() {
		return this.catid;
	}

	/**
	 * @param ncatid
	 *            The ncatid to set.
	 */
	public void setNcatid(final int ncatid) {
		this.ncatid = ncatid;
	}

	/**
	 * @return Returns the ncatid.
	 */
	public int getNcatid() {
		return this.ncatid;
	}

	/**
	 * @param narrow
	 *            The narrow to set.
	 */
	public void setNarrow(final String narrow) {
		this.narrow = narrow;
	}

	/**
	 * @return Returns the narrow.
	 */
	public String getNarrow() {
		return this.narrow;
	}

	/**
	 * @param stmt
	 *            The stmt to set.
	 */
	@Override
	protected void setStmt(final PreparedStatement stmt) {
		Narrow.stmt = stmt;
	}

	/**
	 * @return Returns the stmt.
	 */
	@Override
	protected PreparedStatement getStmt() {
		return Narrow.stmt;
	}

	/**
	 * @param priority
	 *            The priority to set.
	 */
	public void setPriority(final int priority) {
		this.priority = priority;
	}

	/**
	 * @return Returns the priority.
	 */
	public int getPriority() {
		return this.priority;
	}
}

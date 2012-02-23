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
 * Related.java
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
 * $Id: Related.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/impl/Related.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;

import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Related extends Row {
	private String related = null;

	private int catid = 0;

	private int rcatid = 0;

	private static PreparedStatement stmt = null;

	public Related() {
		this.setTablename("dmoz_related");
	}

	public Related(final String related, final int catid, final int rcatid) {
		this.setTablename("dmoz_related");
		this.setRelated(related);
		this.setCatid(catid);
		this.setRcatid(rcatid);
	}

	@Override
	public String getFields() {
		String temp = "(";
		temp = temp + " related,";
		temp = temp + " catid, ";
		temp = temp + " rcatid";
		temp = temp + ")";
		return temp;
	}

	@Override
	public void setValues() {
		try {
			this.getPreparedStatement().setString(1, this.getRelated());
			this.getPreparedStatement().setInt(2, this.getCatid());
			this.getPreparedStatement().setInt(3, this.getRcatid());
		} catch (final Exception e) {
			e.printStackTrace(System.err);
		}
	}

	@Override
	public String getValues() {
		String temp = "";
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
	 * @param rcatid
	 *            The rcatid to set.
	 */
	public void setRcatid(final int rcatid) {
		this.rcatid = rcatid;
	}

	/**
	 * @return Returns the rcatid.
	 */
	public int getRcatid() {
		return this.rcatid;
	}

	/**
	 * @param related
	 *            The related to set.
	 */
	public void setRelated(final String related) {
		this.related = related;
	}

	/**
	 * @return Returns the related.
	 */
	public String getRelated() {
		return this.related;
	}

	/**
	 * @param stmt
	 *            The stmt to set.
	 */
	@Override
	protected void setStmt(final PreparedStatement stmt) {
		Related.stmt = stmt;
	}

	/**
	 * @return Returns the stmt.
	 */
	@Override
	protected PreparedStatement getStmt() {
		return Related.stmt;
	}
}

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
 * NewsGroup.java
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
 * $Id: NewsGroup.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/impl/NewsGroup.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;

import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class NewsGroup extends Row {
	private String type = null;

	private String newsGroup = null;

	private int catid = 0;

	private static PreparedStatement stmt = null;

	public NewsGroup() {
		this.setTablename("dmoz_newsgroups");
	}

	public NewsGroup(final String type, final String newsGroup, final int catid) {
		this.setTablename("dmoz_newsgroups");
		this.setType(type);
		this.setNewsGroup(newsGroup);
		this.setCatid(catid);
	}

	@Override
	public String getFields() {
		String temp = "(";
		temp = temp + " type,";
		temp = temp + " newsGroup,";
		temp = temp + " catid ";
		temp = temp + ")";
		return temp;
	}

	@Override
	public void setValues() {
		try {
			this.getPreparedStatement().setString(1, this.getType());
			this.getPreparedStatement().setString(2, this.getNewsGroup());
			this.getPreparedStatement().setInt(3, this.getCatid());
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
	 * @param newsGroup
	 *            The newsGroup to set.
	 */
	public void setNewsGroup(final String newsGroup) {
		this.newsGroup = newsGroup;
	}

	/**
	 * @return Returns the newsGroup.
	 */
	public String getNewsGroup() {
		return this.newsGroup;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @param stmt
	 *            The stmt to set.
	 */
	@Override
	protected void setStmt(final PreparedStatement stmt) {
		NewsGroup.stmt = stmt;
	}

	/**
	 * @return Returns the stmt.
	 */
	@Override
	protected PreparedStatement getStmt() {
		return NewsGroup.stmt;
	}
}

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
 * AltLang.java
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
 * $Id: AltLang.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/impl/AltLang.java $
 *
 *****************************************************************************************
 */
package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;

import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class AltLang extends Row {
	private String language = null;

	private String resource = null;

	private int catid = 0;

	private int rcatid = 0;

	private static PreparedStatement stmt = null;

	public AltLang() {
		this.setTablename("dmoz_altlangs");
	}

	public AltLang(final String language, final String resource,
			final int catid, final int rcatid) {
		this.setTablename("dmoz_altlangs");
		this.setLanguage(language);
		this.setResource(resource);
		this.setCatid(catid);
		this.setRcatid(rcatid);
	}

	@Override
	public String getFields() {
		String temp = "(";
		temp = temp + " language,";
		temp = temp + " resource,";
		temp = temp + " catid,";
		temp = temp + " rcatid ";
		temp = temp + ")";
		return temp;
	}

	@Override
	public void setValues() {
		try {
			this.getPreparedStatement().setString(1, this.getLanguage());
			this.getPreparedStatement().setString(2, this.getResource());
			this.getPreparedStatement().setInt(3, this.getCatid());
			this.getPreparedStatement().setInt(4, this.getRcatid());
		} catch (final Exception e) {
			e.printStackTrace(System.err);
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
	 * @param language
	 *            The language to set.
	 */
	public void setLanguage(final String language) {
		this.language = language;
	}

	/**
	 * @return Returns the language.
	 */
	public String getLanguage() {
		return this.language;
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
	 * @param resource
	 *            The resource to set.
	 */
	public void setResource(final String resource) {
		this.resource = resource;
	}

	/**
	 * @return Returns the resource.
	 */
	public String getResource() {
		return this.resource;
	}

	/**
	 * @param stmt
	 *            The stmt to set.
	 */
	@Override
	protected void setStmt(final PreparedStatement stmt) {
		AltLang.stmt = stmt;
	}

	/**
	 * @return Returns the stmt.
	 */
	@Override
	protected PreparedStatement getStmt() {
		return AltLang.stmt;
	}
}

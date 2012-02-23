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
 * LetterBar.java
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
 * $Id: LetterBar.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/impl/LetterBar.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;

import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class LetterBar extends Row {
	private String letterbar = null;

	private int catid = 0;

	private int lcatid = 0;

	private static PreparedStatement stmt = null;

	public LetterBar() {
		this.setTablename("dmoz_letterbars");
	}

	public LetterBar(final String letterbar, final int catid, final int lcatid) {
		this.setTablename("dmoz_letterbars");
		this.setLetterbar(letterbar);
		this.setCatid(catid);
		this.setLcatid(lcatid);
	}

	@Override
	public String getFields() {
		String temp = "(";
		temp = temp + " letterbar,";
		temp = temp + " catid, ";
		temp = temp + " lcatid ";
		temp = temp + ")";
		return temp;
	}

	@Override
	public void setValues() {
		try {
			this.getPreparedStatement().setString(1, this.getLetterbar());
			this.getPreparedStatement().setInt(2, this.getCatid());
			this.getPreparedStatement().setInt(3, this.getLcatid());
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
	 * @param lcatid
	 *            The lcatid to set.
	 */
	public void setLcatid(final int lcatid) {
		this.lcatid = lcatid;
	}

	/**
	 * @return Returns the lcatid.
	 */
	public int getLcatid() {
		return this.lcatid;
	}

	/**
	 * @param letterbar
	 *            The letterbar to set.
	 */
	public void setLetterbar(final String letterbar) {
		this.letterbar = letterbar;
	}

	/**
	 * @return Returns the letterbar.
	 */
	public String getLetterbar() {
		return this.letterbar;
	}

	/**
	 * @param stmt
	 *            The stmt to set.
	 */
	@Override
	protected void setStmt(final PreparedStatement stmt) {
		LetterBar.stmt = stmt;
	}

	/**
	 * @return Returns the stmt.
	 */
	@Override
	protected PreparedStatement getStmt() {
		return LetterBar.stmt;
	}
}

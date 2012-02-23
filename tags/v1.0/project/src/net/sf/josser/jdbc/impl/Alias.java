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
 * Alias.java
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
 * $Id: Alias.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/impl/Alias.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;

import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Alias extends Row {
	private int catid = 0;

	private String alias = null;

	private String title = null;

	private String target = null;

	private int tcatid = 0;

	private static PreparedStatement stmt = null;

	public Alias() {
		this.setTablename("dmoz_aliases");
	}

	public Alias(final int catid, final String alias, final String title,
			final String target, final int tcatid) {
		this.setTablename("dmoz_aliases");
		this.setCatid(catid);
		this.setAlias(alias);
		this.setTitle(title);
		this.setTarget(target);
		this.setTcatid(tcatid);
	}

	@Override
	public String getFields() {
		String temp = "(";
		temp = temp + " catid,";
		temp = temp + " Alias,";
		temp = temp + " Title,";
		temp = temp + " Target,";
		temp = temp + " tcatid";
		temp = temp + ")";
		return temp;
	}

	@Override
	public void setValues() {
		try {
			this.getPreparedStatement().setInt(1, this.getCatid());
			this.getPreparedStatement().setString(2, this.getAlias());
			this.getPreparedStatement().setString(3, this.getTitle());
			this.getPreparedStatement().setString(4, this.getTarget());
			this.getPreparedStatement().setInt(5, this.getTcatid());
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
		temp = temp + "?,";
		temp = temp + "?";
		return temp;
	}

	/**
	 * @param alias
	 *            The alias to set.
	 */
	public void setAlias(final String alias) {
		this.alias = alias;
	}

	/**
	 * @return Returns the alias.
	 */
	public String getAlias() {
		return this.alias;
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
	 * @param target
	 *            The target to set.
	 */
	public void setTarget(final String target) {
		this.target = target;
	}

	/**
	 * @return Returns the target.
	 */
	public String getTarget() {
		return this.target;
	}

	/**
	 * @param tcatid
	 *            The tcatid to set.
	 */
	public void setTcatid(final int tcatid) {
		this.tcatid = tcatid;
	}

	/**
	 * @return Returns the tcatid.
	 */
	public int getTcatid() {
		return this.tcatid;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param stmt
	 *            The stmt to set.
	 */
	@Override
	protected void setStmt(final PreparedStatement stmt) {
		Alias.stmt = stmt;
	}

	/**
	 * @return Returns the stmt.
	 */
	@Override
	protected PreparedStatement getStmt() {
		return Alias.stmt;
	}
}

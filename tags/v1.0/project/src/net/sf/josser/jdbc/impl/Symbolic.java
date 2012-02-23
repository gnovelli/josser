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
 * Symbolic.java
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
 * $Id: Symbolic.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/impl/Symbolic.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;

import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Symbolic extends Row {
	private String resource = null;

	private String symbolic = null;

	private int priority = 0;

	private int catid = 0;

	private int scatid = 0;

	private static PreparedStatement stmt = null;

	public Symbolic() {
		this.setTablename("dmoz_symbolics");
	}

	public Symbolic(final String resource, final String symbolic,
			final int catid, final int scatid) {
		this.setTablename("dmoz_symbolics");
		this.setResource(resource);
		this.setSymbolic(symbolic);
		this.setCatid(catid);
		this.setScatid(scatid);
	}

	@Override
	public String getFields() {
		String temp = "(";
		temp = temp + " resource,";
		temp = temp + " symbolic,";
		temp = temp + " priority, ";
		temp = temp + " catid, ";
		temp = temp + " scatid ";
		temp = temp + ")";
		return temp;
	}

	@Override
	public void setValues() {
		try {
			this.getPreparedStatement().setString(1, this.getResource());
			this.getPreparedStatement().setString(2, this.getSymbolic());
			this.getPreparedStatement().setInt(3, this.getPriority());
			this.getPreparedStatement().setInt(4, this.getCatid());
			this.getPreparedStatement().setInt(5, this.getScatid());
		} catch (final Exception e) {
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
	 * @param scatid
	 *            The scatid to set.
	 */
	public void setScatid(final int scatid) {
		this.scatid = scatid;
	}

	/**
	 * @return Returns the scatid.
	 */
	public int getScatid() {
		return this.scatid;
	}

	/**
	 * @param symbolic
	 *            The symbolic to set.
	 */
	public void setSymbolic(final String symbolic) {
		this.symbolic = symbolic;
	}

	/**
	 * @return Returns the symbolic.
	 */
	public String getSymbolic() {
		return this.symbolic;
	}

	/**
	 * @param stmt
	 *            The stmt to set.
	 */
	@Override
	protected void setStmt(final PreparedStatement stmt) {
		Symbolic.stmt = stmt;
	}

	/**
	 * @return Returns the stmt.
	 */
	@Override
	protected PreparedStatement getStmt() {
		return Symbolic.stmt;
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
}

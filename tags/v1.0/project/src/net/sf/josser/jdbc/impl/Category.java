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
 * Category.java
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
 * $Id: Category.java 92 2007-07-17 13:42:11Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/jdbc/impl/Category.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.jdbc.impl;

import java.sql.PreparedStatement;

import net.sf.josser.jdbc.Row;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Category extends Row {
	// Mandatory fields
	private String Topic = null;

	private int catid = 0;

	private String lastUpdate = null;

	private String Title = null;

	// Optional fields
	private String aolsearch = null;

	private String dispname = null;

	private String charset = null;

	private String Description = null;

	// Optional fields with mandatory value
	private int fatherid = 0;

	private static PreparedStatement stmt = null;

	public Category() {
		this.setTablename("dmoz_categories");
		this.setCatid(0);
		this.setTopic(null);
		this.setLastUpdate(null);
		this.setTitle(null);
		this.setFatherid(0);
	}

	public Category(final int catid, final String Topic,
			final String lastUpdate, final String Title) {
		this.setTablename("dmoz_categories");
		this.setCatid(catid);
		this.setTopic(Topic);
		this.setLastUpdate(lastUpdate);
		this.setTitle(Title);
		this.setFatherid(0);
	}

	public Category(final int catid, final String Topic,
			final String lastUpdate, final String Title, final int fatherid) {
		this.setCatid(catid);
		this.setTopic(Topic);
		this.setLastUpdate(lastUpdate);
		this.setTitle(Title);
		this.setFatherid(fatherid);
	}

	public Category(final int catid, final String Topic,
			final String lastUpdate, final String Title, final int fatherid,
			final int priority) {
		this.setCatid(catid);
		this.setTopic(Topic);
		this.setLastUpdate(lastUpdate);
		this.setTitle(Title);
		this.setFatherid(fatherid);
	}

	@Override
	public String getFields() {
		String temp = "(";
		temp = temp + " Topic,";
		temp = temp + " catid,";
		temp = temp + " aolsearch,";
		temp = temp + " dispname,";
		temp = temp + " charset,";
		temp = temp + " Description,";
		temp = temp + " lastUpdate,";
		temp = temp + " fatherid,";
		temp = temp + " Title ";
		temp = temp + ")";
		return temp;
	}

	@Override
	public void setValues() {
		try {
			this.getPreparedStatement().setString(1, this.getTopic());
			this.getPreparedStatement().setInt(2, this.getCatid());
			if ((this.getAolsearch() != null)
					&& (this.getAolsearch().length() > 0)) {
				this.getPreparedStatement().setString(3, this.getAolsearch());
			} else {
				this.getPreparedStatement().setString(3, "");
			}
			if ((this.getDispname() != null)
					&& (this.getDispname().length() > 0)) {
				this.getPreparedStatement().setString(4, this.getDispname());
			} else {
				this.getPreparedStatement().setString(4, "");
			}
			if ((this.getCharset() != null) && (this.getCharset().length() > 0)) {
				this.getPreparedStatement().setString(5, this.getCharset());
			} else {
				this.getPreparedStatement().setString(5, "");
			}
			if ((this.getDescription() != null)
					&& (this.getDescription().length() > 0)) {
				this.getPreparedStatement().setString(6, this.getDescription());
			} else {
				this.getPreparedStatement().setString(6, "");
			}
			if ((this.getLastUpdate() != null)
					&& (this.getLastUpdate().length() > 0)) {
				this.getPreparedStatement().setString(7, this.getLastUpdate());
			} else {
				this.getPreparedStatement().setString(7, "");
			}
			if (this.getFatherid() != 0) {
				this.getPreparedStatement().setInt(8, this.getFatherid());
			} else {
				this.getPreparedStatement().setInt(8, 0);
			}
			this.getPreparedStatement().setString(9, this.getTitle());
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
		temp = temp + "?,";
		temp = temp + "?,";
		temp = temp + "?,";
		temp = temp + "?,";
		temp = temp + "?";
		return temp;
	}

	/**
	 * @param aolsearch
	 *            The aolsearch to set.
	 */
	public void setAolsearch(final String aolsearch) {
		this.aolsearch = aolsearch;
	}

	/**
	 * @return Returns the aolsearch.
	 */
	public String getAolsearch() {
		return this.aolsearch;
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
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(final String description) {
		this.Description = description;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.Description;
	}

	/**
	 * @param dispname
	 *            The dispname to set.
	 */
	public void setDispname(final String dispname) {
		this.dispname = dispname;
	}

	/**
	 * @return Returns the dispname.
	 */
	public String getDispname() {
		return this.dispname;
	}

	/**
	 * @param fatherid
	 *            The fatherid to set.
	 */
	public void setFatherid(final int fatherid) {
		this.fatherid = fatherid;
	}

	/**
	 * @return Returns the fatherid.
	 */
	public int getFatherid() {
		return this.fatherid;
	}

	/**
	 * @param lastUpdate
	 *            The lastUpdate to set.
	 */
	public void setLastUpdate(final String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return Returns the lastUpdate.
	 */
	public String getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(final String title) {
		this.Title = title;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return this.Title;
	}

	/**
	 * @param topic
	 *            The topic to set.
	 */
	public void setTopic(final String topic) {
		this.Topic = topic;
	}

	/**
	 * @return Returns the topic.
	 */
	public String getTopic() {
		return this.Topic;
	}

	/**
	 * @param stmt
	 *            The stmt to set.
	 */
	@Override
	protected void setStmt(final PreparedStatement stmt) {
		Category.stmt = stmt;
	}

	/**
	 * @return Returns the stmt.
	 */
	@Override
	protected PreparedStatement getStmt() {
		return Category.stmt;
	}

	/**
	 * @param charset
	 *            The charset to set.
	 */
	public void setCharset(final String charset) {
		this.charset = charset;
	}

	/**
	 * @return Returns the charset.
	 */
	public String getCharset() {
		return this.charset;
	}
}

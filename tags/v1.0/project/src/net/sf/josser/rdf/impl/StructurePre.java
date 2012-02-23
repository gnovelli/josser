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
 * StructurePre.java
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
 * $Revision: 97 $
 * $Id: StructurePre.java 97 2007-07-21 02:24:56Z gnovelli $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/rdf/impl/StructurePre.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.rdf.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import net.sf.josser.rdf.Parser;
import net.sf.josser.Josser;
import net.sf.josser.util.Static;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class StructurePre extends Parser {
	private boolean processed = false;

	private String path = null;

	private boolean processingCategory = false;

	private boolean processingCategoryDescription = false;

	private int ccatid = 0;

	private String ctopic = "";

	private int counter = 0;

	public StructurePre(final String path) {
		this.setPath(path);
		this.setProcessingCategory(false);
		this.setProcessingCategoryDescription(false);
		Static.initTopicsHashtable();
	}

	@Override
	public void parse(final int grouplines) {
		int numlines = 0;
		final Date date = new Date();
		final long start_time = date.getTime();
		try {
			// BufferedReader in = new BufferedReader(new
			// FileReader(this.path));
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(this.getPath()), "UTF8"));
			String line;
			while ((line = in.readLine()) != null) {
				numlines = numlines + 1;
				this.process(line);
				if ((numlines % grouplines) == 0) {
					final Date current_date = new Date();
					final long elapsed_time = current_date.getTime()
							- start_time;
				}
			}
			in.close();
			final Date current_date = new Date();
			final long elapsed_time = current_date.getTime() - start_time;
		} catch (final IOException e) {
			e.printStackTrace(System.err);
		}
	}

	protected void processCategory(final String line) {
		this.setProcessed(true);
		String[] tokens = null;
		if (line.startsWith("  <catid>")) {
			int catid = 0;
			tokens = null;
			tokens = line.split("  <catid>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</catid>");
				if (tokens.length == 1) {
					catid = Integer.parseInt(tokens[0]);
					this.setCcatid(catid);
				}
			}
		} else if (line.startsWith("  <d:Title>")) {
		} else if (line.startsWith("  <d:Description>")) {
			tokens = line.split("  <d:Description>");
			if (tokens.length == 2) {
				if (tokens[1].endsWith("</d:Description>")) {
					tokens = tokens[1].split("</d:Description>");
					if (tokens.length == 1) {
					} else {
					}
				} else {
					this.setProcessingCategoryDescription(true);
				}
			}
		} else if (line.startsWith("  <lastUpdate>")) {
		} else if (line.startsWith("  <aolsearch>")) {
		} else if (line.startsWith("  <dispname>")) {
		} else if (line.startsWith("  <letterbar r:resource=\"")) { // letterbar
		} else if (line.startsWith("  <editor r:resource=\"")) { // editor
		} else if (line.startsWith("  <related r:resource=\"")) { // related
		} else if (line.startsWith("  <altlang r:resource=\"")) { // altlang
		} else if (line.startsWith("  <newsGroup r:resource=\"")) { // altlang
		} else if (line.startsWith("</Topic>")) {
			this.setProcessingCategory(false);
                        final Integer icatid = new Integer(this.getCcatid());
			Static.getTopicsHashtable().addTopic(this.getCtopic(), icatid);
			// If current category matches topicfilter then count current
			// category
			if (this.getCtopic().startsWith(Josser.getTopicfilter())) {
				this.setCounter(this.getCounter() + 1);
			}
		} else if (line.endsWith("</d:Description>")) {
			tokens = null;
			tokens = line.split("</d:Description>");
			if (tokens.length == 2) {
			}
			this.setProcessingCategoryDescription(false);
		} else if (this.isProcessingCategoryDescription()) {
		} else if (line.startsWith("  <symbolic")) {
		} else if (line.startsWith("  <narrow")) {
		} else if (line.startsWith("  <d:charset")) {
		} else {
			this.setProcessed(false);
		}
	}

	protected void processCategoryStart(final String line) {
		this.setProcessed(true);
		String Topic = null;
		String[] tokens = null;
		tokens = line.split("<Topic r:id=\"");
		if (tokens.length == 2) {
			tokens = tokens[1].split("\">");
			if (tokens.length == 1) {
				Topic = tokens[0];
				this.setProcessingCategory(true);
				this.setCtopic(Topic);
			}
		} else {
			this.setProcessed(false);
		}
	}

	public void process(final String line) {
		this.setProcessed(false);
		if (this.isProcessingCategory()) {
			this.processCategory(line);
		} else {
			if (!this.isProcessingCategory()) {
				if (line.startsWith("<Topic r:id=\"")) {
					this.processCategoryStart(line);
				}
			}
		}
	}

	/**
	 * @param path
	 *            The path to set.
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @return Returns the path.
	 */
	@Override
	public String getPath() {
		return this.path;
	}

	/**
	 * @param ccatid
	 *            The ccatid to set.
	 */
	void setCcatid(final int ccatid) {
		this.ccatid = ccatid;
	}

	/**
	 * @return Returns the ccatid.
	 */
	int getCcatid() {
		return this.ccatid;
	}

	/**
	 * @param counter
	 *            The counter to set.
	 */
	void setCounter(final int counter) {
		this.counter = counter;
	}

	/**
	 * @return Returns the counter.
	 */
	int getCounter() {
		return this.counter;
	}

	/**
	 * @param ctopic
	 *            The ctopic to set.
	 */
	void setCtopic(final String ctopic) {
		this.ctopic = ctopic;
	}

	/**
	 * @return Returns the ctopic.
	 */
	String getCtopic() {
		return this.ctopic;
	}

	/**
	 * @param processed
	 *            The processed to set.
	 */
	protected void setProcessed(final boolean processed) {
		this.processed = processed;
	}

	/**
	 * @return Returns the processed.
	 */
	protected boolean isProcessed() {
		return this.processed;
	}

	/**
	 * @param processingCategory
	 *            The processingCategory to set.
	 */
	protected void setProcessingCategory(final boolean processingCategory) {
		this.processingCategory = processingCategory;
	}

	/**
	 * @return Returns the processingCategory.
	 */
	protected boolean isProcessingCategory() {
		return this.processingCategory;
	}

	/**
	 * @param processingCategoryDescription
	 *            The processingCategoryDescription to set.
	 */
	protected void setProcessingCategoryDescription(
			final boolean processingCategoryDescription) {
		this.processingCategoryDescription = processingCategoryDescription;
	}

	/**
	 * @return Returns the processingCategoryDescription.
	 */
	protected boolean isProcessingCategoryDescription() {
		return this.processingCategoryDescription;
	}

	@Override
	public int batchClear() {
		return 0;
	}

	@Override
	public int batchStore() {
		return 0;
	}
}

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
 * Content.java
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
 * $Id: Content.java 97 2007-07-21 02:24:56Z gnovelli $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/rdf/impl/Content.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.rdf.impl;

import net.sf.josser.jdbc.impl.Category;
import net.sf.josser.jdbc.impl.ExternalPage;
import net.sf.josser.Josser;
import net.sf.josser.util.Static;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Content extends Structure {

	private boolean processingExternalPage = false;

	private ExternalPage externalPageRow = null;

	private boolean processingExternalPageDescription = false;

	private String externalPageDescription = null;

	public Content(final String path) {
		super(path);
		this.setCategoryRow(new Category());

		this.setProcessingExternalPage(false);
		this.setExternalPageRow(null);
		this.setProcessingExternalPageDescription(false);
		this.setExternalPageDescription(null);
		this.setPhase(0);
	}

	@Override
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
				this.getCategoryRow().setTopic(Topic);
				Static
						.setFiltermatching(Topic.startsWith(Josser
								.getTopicfilter()));
				if ((this.getPhase() == 0)
						&& Topic.startsWith(Josser.getTopicfilter())) {
					this.setPhase(1);
				} else if ((this.getPhase() == 1)
						&& !Topic.startsWith(Josser.getTopicfilter())) {
					this.setPhase(2);
				}
			}
		} else {
			this.setProcessed(false);
		}
	}

	@Override
	public void processCategory(final String line) {
		this.setProcessed(true);
		String[] tokens = null;
		if (line.startsWith("  <catid>")) {
			int catid = 0;
			tokens = line.split("  <catid>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</catid>");
				if (tokens.length == 1) {
					catid = Integer.parseInt(tokens[0]);
					this.getCategoryRow().setCatid(catid);
				}
			}
		} else if (line.startsWith("</Topic>")) {
			this.setProcessingCategory(false);
		} else if (line.startsWith("  <link r:resource=\"")) {
			/*
			 * FIXME At the moment parsing is done on nodes of type ExternalPage
			 */
		} else if (line.startsWith("  <link1 r:resource=\"")) {
			/*
			 * FIXME At the moment parsing is done on nodes of type ExternalPage
			 */
		} else if (line.startsWith("  <rss r:resource=\"")) {
			/*
			 * FIXME At the moment parsing is done on nodes of type ExternalPage
			 */
		} else if (line.startsWith("  <atom r:resource=\"")) {
			/*
			 * FIXME At the moment parsing is done on nodes of type ExternalPage
			 */
		} else if (line.startsWith("  <rss1 r:resource=\"")) {
			/*
			 * FIXME At the moment parsing is done on nodes of type ExternalPage
			 */
		} else if (line.startsWith("  <pdf r:resource=\"")) {
			/*
			 * FIXME At the moment parsing is done on nodes of type ExternalPage
			 */
		} else if (line.startsWith("  <pdf1 r:resource=\"")) {
			/*
			 * FIXME At the moment parsing is done on nodes of type ExternalPage
			 */
		} else {
			this.setProcessed(false);
		}
	}

	protected void processExternalPage(final String line) {
		this.setProcessed(true);
		String[] tokens = null;
		if (line.startsWith("  <d:Title>")) {
			String Title = null;
			tokens = line.split("  <d:Title>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</d:Title>");
				if (tokens.length == 1) {
					Title = tokens[0];
					this.getExternalPageRow().setTitle(Title);
				}
			}
		} else if (line.startsWith("</ExternalPage>")) {
			this.setProcessingExternalPage(false);
			this.getExternalPageRow().addBatch();
		} else if (line.startsWith("  <d:Description>")) {
			tokens = line.split("  <d:Description>");
			if (tokens.length == 2) {
				if (tokens[1].endsWith("</d:Description>")) {
					tokens = tokens[1].split("</d:Description>");
					if (tokens.length == 1) {
						this.setExternalPageDescription(tokens[0]);
					} else {
						this.setExternalPageDescription("");
					}
					this.getExternalPageRow().setDescription(
							this.getExternalPageDescription());
				} else {
					this.setProcessingExternalPageDescription(true);
					this.setExternalPageDescription(tokens[1]);
				}
			}
		} else if (line.endsWith("  </d:Description>")) {
			tokens = line.split("  </d:Description>");
			if (tokens.length == 2) {
				this.setExternalPageDescription(this
						.getExternalPageDescription()
						+ tokens[0]);
			}
			this.setProcessingExternalPageDescription(false);
			this.getExternalPageRow().setDescription(
					this.getExternalPageDescription());
		} else if (this.isProcessingExternalPageDescription()) {
			this.setExternalPageDescription(this.getExternalPageDescription()
					+ line);
		} else if (line.startsWith("  <topic>")) {
			/*
			 * FIXME At the moment parsing of Topic is done once in nodes of
			 * type Topic and not in nodes of type ExternalPage
			 */
		} else if (line.startsWith("  <priority>")) {
			int priority = 0;
			tokens = line.split("  <priority>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</priority>");
				if (tokens.length == 1) {
					priority = Integer.parseInt(tokens[0]);
					this.getExternalPageRow().setPriority(priority);
				}
			}
		} else if (line.startsWith("  <mediadate>")) {
			String mediadate = null;
			tokens = line.split("  <mediadate>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</mediadate>");
				if (tokens.length == 1) {
					mediadate = tokens[0];
					this.getExternalPageRow().setMediadate(mediadate);
				}
			}
		} else if (line.startsWith("  <ages>")) {
			String ages = null;
			tokens = line.split("  <ages>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</ages>");
				if (tokens.length == 1) {
					ages = tokens[0];
					this.getExternalPageRow().setAges(ages);
				}
			}
		} else if (line.startsWith("  <type>")) {
			String type = null;
			tokens = line.split("  <type>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</type>");
				if (tokens.length == 1) {
					type = tokens[0];
					this.getExternalPageRow().setType(type);
				}
			}
		} else {
			this.setProcessed(false);
		}
	}

	protected void processExternalPageStart(final String line) {
		this.setProcessed(true);
		String about = null;
		String[] tokens = null;
		tokens = line.split("<ExternalPage about=\"");
		if (tokens.length == 2) {
			tokens = tokens[1].split("\">");
			if (tokens.length == 1) {
				about = tokens[0];
				this.setExternalPageRow(new ExternalPage());
				this.getExternalPageRow().setCatid(
						this.getCategoryRow().getCatid());
				this.getExternalPageRow().setLink(about);
				this.setProcessingExternalPage(true);
			} else {
				this.setExternalPageRow(new ExternalPage());
				this.getExternalPageRow().setLink("");
				this.setProcessingExternalPage(true);
			}
		} else {
			this.setProcessed(false);
		}
	}

	@Override
	public void process(final String line) {
		this.setProcessed(false);
		if (this.isProcessingCategory() || this.isProcessingExternalPage()) {
			if (this.isProcessingCategory()) {
				this.processCategory(line);
			} else if (this.isProcessingExternalPage()) {
				this.processExternalPage(line);
			}
			if (!this.isProcessed() && (line.length() > 0)) {
			}
		} else {
			if (!this.isProcessingExternalPage()) {
				if (line.startsWith("<ExternalPage about=\"")) {
					this.processExternalPageStart(line);
				}
			}
			if (!this.isProcessingCategory()) {
				if (line.startsWith("<Topic r:id=\"")) {
					this.processCategoryStart(line);
				}
			}
			if (!this.isProcessed() && (line.length() > 0)) {
			}
		}
	}

	@Override
	public int batchStore() {
		int result = 0;
		result += this.getExternalPageRow().executeBatch();
		return result;
	}

	@Override
	public int batchClear() {
		return this.getExternalPageRow().batchClear();
	}

	/**
	 * @param externalPageDescription
	 *            The externalPageDescription to set.
	 */
	protected void setExternalPageDescription(
			final String externalPageDescription) {
		this.externalPageDescription = externalPageDescription;
	}

	/**
	 * @return Returns the externalPageDescription.
	 */
	protected String getExternalPageDescription() {
		return this.externalPageDescription;
	}

	/**
	 * @param externalPageRow
	 *            The externalPageRow to set.
	 */
	protected void setExternalPageRow(final ExternalPage externalPageRow) {
		this.externalPageRow = externalPageRow;
	}

	/**
	 * @return Returns the externalPageRow.
	 */
	protected ExternalPage getExternalPageRow() {
		return this.externalPageRow;
	}

	/**
	 * @param processingExternalPage
	 *            The processingExternalPage to set.
	 */
	protected void setProcessingExternalPage(
			final boolean processingExternalPage) {
		this.processingExternalPage = processingExternalPage;
	}

	/**
	 * @return Returns the processingExternalPage.
	 */
	protected boolean isProcessingExternalPage() {
		return this.processingExternalPage;
	}

	/**
	 * @param processingExternalPageDescription
	 *            The processingExternalPageDescription to set.
	 */
	protected void setProcessingExternalPageDescription(
			final boolean processingExternalPageDescription) {
		this.processingExternalPageDescription = processingExternalPageDescription;
	}

	/**
	 * @return Returns the processingExternalPageDescription.
	 */
	protected boolean isProcessingExternalPageDescription() {
		return this.processingExternalPageDescription;
	}
}

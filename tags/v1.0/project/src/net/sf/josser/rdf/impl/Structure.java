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
 * Structure.java
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
 * $Id: Structure.java 97 2007-07-21 02:24:56Z gnovelli $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/rdf/impl/Structure.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.rdf.impl;

import net.sf.josser.jdbc.impl.Alias;
import net.sf.josser.jdbc.impl.AltLang;
import net.sf.josser.jdbc.impl.Category;
import net.sf.josser.jdbc.impl.Editor;
import net.sf.josser.jdbc.impl.LetterBar;
import net.sf.josser.jdbc.impl.Narrow;
import net.sf.josser.jdbc.impl.NewsGroup;
import net.sf.josser.jdbc.impl.Related;
import net.sf.josser.jdbc.impl.Symbolic;
import net.sf.josser.rdf.Parser;
import net.sf.josser.Josser;
import net.sf.josser.util.Static;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Structure extends Parser {
	private Alias aliasRow = null;

	private AltLang altlangRow = null;

	private String categoryDescription = null;

	private Category categoryRow = null;

	private int ccatid = 0;

	private String ctopic = "";

	private Editor editorRow = null;

	private LetterBar letterbarRow = null;

	private Narrow narrowRow = null;

	private NewsGroup newsGroupRow = null;

	private Symbolic symbolicRow = null;

	private String path = null;

	private boolean processed = false;

	private boolean processingAlias = false;

	private boolean processingCategory = false;

	private boolean processingCategoryDescription = false;

	private Related relatedRow = null;

	public Structure(final String path) {
		this.setPath(path);

		this.setProcessingCategory(false);
		this.setCategoryRow(null);
		this.setProcessingCategoryDescription(false);
		this.setLetterbarRow(new LetterBar());
		this.setNarrowRow(new Narrow());
		this.setEditorRow(new Editor());
		this.setRelatedRow(new Related());
		this.setAltlangRow(new AltLang());
		this.setNewsGroupRow(new NewsGroup());
		this.setSymbolicRow(new Symbolic());

		this.setProcessingAlias(false);
		this.setAliasRow(new Alias());
		this.setPhase(0);
	}

	@Override
	public int batchClear() {
		int result = 0;
		result += this.getAliasRow().batchClear();
		result += this.getAltlangRow().batchClear();
		result += this.getCategoryRow().batchClear();
		result += this.getEditorRow().batchClear();
		result += this.getLetterbarRow().batchClear();
		result += this.getNarrowRow().batchClear();
		result += this.getNewsGroupRow().batchClear();
		result += this.getRelatedRow().batchClear();
		result += this.getSymbolicRow().batchClear();
		return result;
	}

	@Override
	public int batchStore() {
		int result = 0;
		result += this.getAliasRow().executeBatch();
		result += this.getAltlangRow().executeBatch();
		result += this.getCategoryRow().executeBatch();
		result += this.getEditorRow().executeBatch();
		result += this.getLetterbarRow().executeBatch();
		result += this.getNarrowRow().executeBatch();
		result += this.getNewsGroupRow().executeBatch();
		result += this.getRelatedRow().executeBatch();
		result += this.getSymbolicRow().executeBatch();
		return result;
	}

	/**
	 * @return Returns the aliasRow.
	 */
	protected Alias getAliasRow() {
		return this.aliasRow;
	}

	/**
	 * @return Returns the altlangRow.
	 */
	protected AltLang getAltlangRow() {
		return this.altlangRow;
	}

	/**
	 * @return Returns the categoryDescription.
	 */
	protected String getCategoryDescription() {
		return this.categoryDescription;
	}

	/**
	 * @return Returns the categoryRow.
	 */
	protected Category getCategoryRow() {
		return this.categoryRow;
	}

	/**
	 * @return Returns the ccatid.
	 */
	int getCcatid() {
		return this.ccatid;
	}

	/**
	 * @return Returns the ctopic.
	 */
	String getCtopic() {
		return this.ctopic;
	}

	/**
	 * @return Returns the editorRow.
	 */
	protected Editor getEditorRow() {
		return this.editorRow;
	}

	/**
	 * @return Returns the letterbarRow.
	 */
	protected LetterBar getLetterbarRow() {
		return this.letterbarRow;
	}

	/**
	 * @return Returns the newsGroupRow.
	 */
	protected NewsGroup getNewsGroupRow() {
		return this.newsGroupRow;
	}

	/**
	 * @return Returns the path.
	 */
	@Override
	public String getPath() {
		return this.path;
	}

	/**
	 * @return Returns the relatedRow.
	 */
	protected Related getRelatedRow() {
		return this.relatedRow;
	}

	/**
	 * @return Returns the processed.
	 */
	protected boolean isProcessed() {
		return this.processed;
	}

	/**
	 * @return Returns the processingAlias.
	 */
	protected boolean isProcessingAlias() {
		return this.processingAlias;
	}

	/**
	 * @return Returns the processingCategory.
	 */
	protected boolean isProcessingCategory() {
		return this.processingCategory;
	}

	/**
	 * @return Returns the processingCategoryDescription.
	 */
	protected boolean isProcessingCategoryDescription() {
		return this.processingCategoryDescription;
	}

	public void process(final String line) {
		this.setProcessed(false);
		if (this.isProcessingCategory() || this.isProcessingAlias()) {
			if (this.isProcessingCategory()) {
				this.processCategory(line);
			} else if (this.isProcessingAlias()) {
				this.processAlias(line);
			}
			if (!this.isProcessed() && (line.length() > 0)) {
			}
		} else {
			if (!this.isProcessingAlias()) {
				if (line.startsWith("<Alias r:id=\"")) {
					this.processAliasStart(line);
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

	public void processAlias(final String line) {
		this.setProcessed(true);
		if (line.startsWith("  <d:Title>")) {
			String Title = null;
			String[] tokens = null;
			tokens = line.split("  <d:Title>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</d:Title>");
				if (tokens.length == 1) {
					Title = tokens[0];
					this.getAliasRow().setTitle(Title);
				}
			}
		} else if (line.startsWith("  <Target r:resource=\"")) {
			// FIXME Do nothing until nothing change in actual format
		} else if (line.startsWith("</Alias>")) {
			this.setProcessingAlias(false);
			// this.aliasRow.store(con);
			this.getAliasRow().addBatch();
		} else {
			this.setProcessed(false);
		}
	}

	protected void processAliasStart(final String line) {
		this.setProcessed(true);
		String Alias = null;
		String Target = null;
		String[] tokens = null;
		tokens = line.split("<Alias r:id=\"");
		if (tokens.length == 2) {
			tokens = tokens[1].split(":");
			if (tokens.length == 2) {
				Alias = tokens[0];
				this.setProcessingAlias(true);
				this.setProcessingCategory(false);
				this.getAliasRow().setAlias(Alias);
				tokens = tokens[1].split("\">");
				if (tokens.length == 1) {
					Target = tokens[0];
					this.getAliasRow().setTarget(Target);
					this.getAliasRow().setTcatid(
							Static.findTopic(this.getAliasRow().getTarget()));
				}
			}
		} else {
			this.setProcessed(false);
		}
	}

	public void processCategory(final String line) {
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
					this.getCategoryRow().setCatid(catid);
					this.getLetterbarRow().setCatid(catid);
					this.getNarrowRow().setCatid(catid);
					this.getEditorRow().setCatid(catid);
					this.getRelatedRow().setCatid(catid);
					this.getAltlangRow().setCatid(catid);
					this.getNewsGroupRow().setCatid(catid);
					this.getSymbolicRow().setCatid(catid);

					this.getAliasRow().setCatid(catid);

					this.setCcatid(catid);
				}
			}
		} else if (line.startsWith("  <d:Title>")) {
			String Title = null;
			tokens = null;
			tokens = line.split("  <d:Title>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</d:Title>");
				if (tokens.length == 1) {
					Title = tokens[0];
					this.getCategoryRow().setTitle(Title);
				}
			}
		} else if (line.startsWith("  <d:Description>")) {
			tokens = line.split("  <d:Description>");
			if (tokens.length == 2) {
				if (tokens[1].endsWith("</d:Description>")) {
					tokens = tokens[1].split("</d:Description>");
					if (tokens.length == 1) {
						this.setCategoryDescription(tokens[0]);
					} else {
						this.setCategoryDescription("");
					}
					this.getCategoryRow().setDescription(
							this.getCategoryDescription());
				} else {
					this.setProcessingCategoryDescription(true);
					this.setCategoryDescription(tokens[1]);
				}
			}
		} else if (line.startsWith("  <lastUpdate>")) {
			String lastUpdate = null;
			tokens = null;
			tokens = line.split("  <lastUpdate>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</lastUpdate>");
				if (tokens.length == 1) {
					lastUpdate = tokens[0];
					this.getCategoryRow().setLastUpdate(lastUpdate);
				}
			}
		} else if (line.startsWith("  <aolsearch>")) {
			String aolsearch = null;
			tokens = null;
			tokens = line.split("  <aolsearch>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</aolsearch>");
				if (tokens.length == 1) {
					aolsearch = tokens[0];
					this.getCategoryRow().setAolsearch(aolsearch);
				}
			}
		} else if (line.startsWith("  <dispname>")) {
			String dispname = null;
			tokens = null;
			tokens = line.split("  <dispname>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</dispname>");
				if (tokens.length == 1) {
					dispname = tokens[0];
					this.getCategoryRow().setDispname(dispname);
				}
			}
		} else if (line.startsWith("  <letterbar r:resource=\"")) { // letterbar
			String letterbar = null;
			tokens = null;
			tokens = line.split("  <letterbar r:resource=\"");
			if (tokens.length == 2) {
				tokens = tokens[1].split("\"/>");
				if (tokens.length == 1) {
					letterbar = tokens[0];
					this.getLetterbarRow().setLetterbar(letterbar);
					this.getLetterbarRow().setLcatid(
							Static.findTopic(this.getLetterbarRow()
									.getLetterbar()));
					// this.letterbarRow.store(con);
					this.getLetterbarRow().addBatch();
				}
			}
		} else if (line.startsWith("  <editor r:resource=\"")) { // editor
			String editor = null;
			tokens = null;
			tokens = line.split("  <editor r:resource=\"");
			if (tokens.length == 2) {
				tokens = tokens[1].split("\"/>");
				if (tokens.length == 1) {
					editor = tokens[0];
					this.getEditorRow().setEditor(editor);
					// this.editorRow.store(con);
					this.getEditorRow().addBatch();
				}
			}
		} else if (line.startsWith("  <related r:resource=\"")) { // related
			String related = null;
			tokens = null;
			tokens = line.split("  <related r:resource=\"");
			if (tokens.length == 2) {
				tokens = tokens[1].split("\"/>");
				if (tokens.length == 1) {
					related = tokens[0];
					this.getRelatedRow().setRelated(related);
					this.getRelatedRow()
							.setRcatid(
									Static.findTopic(this.getRelatedRow()
											.getRelated()));
					// this.relatedRow.store(con);
					this.getRelatedRow().addBatch();
				}
			}
		} else if (line.startsWith("  <altlang r:resource=\"")) { // altlang
			String language = null;
			String resource = null;
			tokens = null;
			tokens = line.split("  <altlang r:resource=\"");
			if (tokens.length == 2) {
				tokens = tokens[1].split(":");
				if (tokens.length == 2) {
					language = tokens[0];
					tokens = tokens[1].split("\"/>");
					if (tokens.length == 1) {
						resource = tokens[0];
						this.getAltlangRow().setLanguage(language);
						this.getAltlangRow().setResource(resource);
						this.getAltlangRow().setRcatid(
								Static.findTopic(this.getAltlangRow()
										.getResource()));
						// this.altlangRow.store(con);
						this.getAltlangRow().addBatch();
					}
				}
			}
		} else if (line.startsWith("  <newsGroup r:resource=\"")) { // altlang
			String type = null;
			String newsGroup = null;
			tokens = null;
			tokens = line.split("  <newsGroup r:resource=\"");
			if (tokens.length == 2) {
				tokens = tokens[1].split(":");
				if (tokens.length == 2) {
					type = tokens[0];
					tokens = tokens[1].split("\"/>");
					if (tokens.length == 1) {
						newsGroup = tokens[0];
						this.getNewsGroupRow().setType(type);
						this.getNewsGroupRow().setNewsGroup(newsGroup);
						// this.newsGroupRow.store(con);
						this.getNewsGroupRow().addBatch();
					}
				}
			}
		} else if (line.startsWith("</Topic>")) {
			this.setProcessingCategory(false);
			this.getCategoryRow().setFatherid(
					Static.findParent(this.getCtopic()));
			// this.categoryRow.store(con);
			this.getCategoryRow().addBatch();
		} else if (line.endsWith("</d:Description>")) {
			tokens = null;
			tokens = line.split("</d:Description>");
			if (tokens.length == 2) {
				this.setCategoryDescription(this.getCategoryDescription()
						+ tokens[0]);
			}
			this.setProcessingCategoryDescription(false);
			this.getCategoryRow().setDescription(this.getCategoryDescription());
		} else if (this.isProcessingCategoryDescription()) {
			this.setCategoryDescription(this.getCategoryDescription() + line);
		} else if (line.startsWith("  <symbolic")) {
			String start = null;
			tokens = null;
			int priority = 0;
			if (line.startsWith("  <symbolic r:resource=\"")) {
				start = "  <symbolic r:resource=\"";
				priority = 0;
			} else if (line.startsWith("  <symbolic1 r:resource=\"")) {
				start = "  <symbolic1 r:resource=\"";
				priority = 1;
			} else if (line.startsWith("  <symbolic2 r:resource=\"")) {
				start = "  <symbolic2 r:resource=\"";
				priority = 2;
			} else {
			}
			String symbolic = null;
			String resource = null;
			tokens = line.split(start);
			if (tokens.length == 2) {
				tokens = tokens[1].split("\"/>");
				if (tokens.length == 1) {
					tokens = tokens[0].split(":");
					symbolic = tokens[0];
					resource = tokens[1];
					this.getSymbolicRow().setSymbolic(symbolic);
					this.getSymbolicRow().setResource(resource);
					this.getSymbolicRow().setPriority(priority);
					this.getSymbolicRow().setScatid(
							Static.findTopic(this.getSymbolicRow()
									.getResource()));
					// this.symbolicRow.store(con);
					this.getSymbolicRow().addBatch();
				}
			}
		} else if (line.startsWith("  <narrow")) {
			String start = null;
			tokens = null;
			int priority = 0;
			if (line.startsWith("  <narrow r:resource=\"")) {
				start = "  <narrow r:resource=\"";
				priority = 0;
			} else if (line.startsWith("  <narrow1 r:resource=\"")) {
				start = "  <narrow1 r:resource=\"";
				priority = 1;
			} else if (line.startsWith("  <narrow2 r:resource=\"")) {
				start = "  <narrow2 r:resource=\"";
				priority = 2;
			} else {
			}
			String narrow = null;
			tokens = line.split(start);
			if (tokens.length == 2) {
				tokens = tokens[1].split("\"/>");
				if (tokens.length == 1) {
					narrow = tokens[0];
					this.getNarrowRow().setNarrow(narrow);
					this.getNarrowRow().setPriority(priority);
					this.getNarrowRow().setNcatid(
							Static.findTopic(this.getNarrowRow().getNarrow()));
					// this.narrowRow.store(con);
					this.getNarrowRow().addBatch();
				}
			}
		} else if (line.startsWith("  <d:charset")) {
			String charset = null;
			tokens = null;
			tokens = line.split("  <d:charset>");
			if (tokens.length == 2) {
				tokens = tokens[1].split("</d:charset>");
				if (tokens.length == 1) {
					charset = tokens[0];
					this.getCategoryRow().setCharset(charset);
				}
			}
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
				this.setProcessingAlias(false);
				this.setProcessingCategoryDescription(false);
				this.setCategoryDescription(null);
				this.setCategoryRow(new Category());
				this.getCategoryRow().setTopic(Topic);
				this.setCtopic(Topic);
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

	/**
	 * @param aliasRow
	 *            The aliasRow to set.
	 */
	protected void setAliasRow(final Alias aliasRow) {
		this.aliasRow = aliasRow;
	}

	/**
	 * @param altlangRow
	 *            The altlangRow to set.
	 */
	protected void setAltlangRow(final AltLang altlangRow) {
		this.altlangRow = altlangRow;
	}

	/**
	 * @param categoryDescription
	 *            The categoryDescription to set.
	 */
	protected void setCategoryDescription(final String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	/**
	 * @param categoryRow
	 *            The categoryRow to set.
	 */
	protected void setCategoryRow(final Category categoryRow) {
		this.categoryRow = categoryRow;
	}

	/**
	 * @param ccatid
	 *            The ccatid to set.
	 */
	void setCcatid(final int ccatid) {
		this.ccatid = ccatid;
	}

	/**
	 * @param ctopic
	 *            The ctopic to set.
	 */
	void setCtopic(final String ctopic) {
		this.ctopic = ctopic;
	}

	/**
	 * @param editorRow
	 *            The editorRow to set.
	 */
	protected void setEditorRow(final Editor editorRow) {
		this.editorRow = editorRow;
	}

	/**
	 * @param letterbarRow
	 *            The letterbarRow to set.
	 */
	protected void setLetterbarRow(final LetterBar letterbarRow) {
		this.letterbarRow = letterbarRow;
	}

	/**
	 * @param newsGroupRow
	 *            The newsGroupRow to set.
	 */
	protected void setNewsGroupRow(final NewsGroup newsGroupRow) {
		this.newsGroupRow = newsGroupRow;
	}

	/**
	 * @param path
	 *            The path to set.
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @param processed
	 *            The processed to set.
	 */
	protected void setProcessed(final boolean processed) {
		this.processed = processed;
	}

	/**
	 * @param processingAlias
	 *            The processingAlias to set.
	 */
	protected void setProcessingAlias(final boolean processingAlias) {
		this.processingAlias = processingAlias;
	}

	/**
	 * @param processingCategory
	 *            The processingCategory to set.
	 */
	protected void setProcessingCategory(final boolean processingCategory) {
		this.processingCategory = processingCategory;
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
	 * @param relatedRow
	 *            The relatedRow to set.
	 */
	protected void setRelatedRow(final Related relatedRow) {
		this.relatedRow = relatedRow;
	}

	/**
	 * @param narrowRow
	 *            The narrowRow to set.
	 */
	public void setNarrowRow(final Narrow narrowRow) {
		this.narrowRow = narrowRow;
	}

	/**
	 * @return Returns the narrowRow.
	 */
	public Narrow getNarrowRow() {
		return this.narrowRow;
	}

	/**
	 * @param symbolicRow
	 *            The symbolicRow to set.
	 */
	public void setSymbolicRow(final Symbolic symbolicRow) {
		this.symbolicRow = symbolicRow;
	}

	/**
	 * @return Returns the symbolicRow.
	 */
	public Symbolic getSymbolicRow() {
		return this.symbolicRow;
	}
}

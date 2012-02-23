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
 * IParser.java
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
 * $Revision: 20 $
 * $Id: IParser.java 20 2008-01-17 12:47:41Z gnovelli $
 * $HeadURL: https://josser.svn.sourceforge.net/svnroot/josser/branches/ui/project/src/net/sf/josser/rdf/IParser.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.rdf;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public interface IParser {
	public abstract String getPath();

	public abstract void parse(int grouplines);

	public abstract void process(String line);

	public abstract int batchClear();

	public abstract int batchStore();
}
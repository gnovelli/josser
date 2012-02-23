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
 * IRow.java
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
 * $Id: IRow.java 20 2008-01-17 12:47:41Z gnovelli $
 * $HeadURL: https://josser.svn.sourceforge.net/svnroot/josser/tags/v1.02/project/src/net/sf/josser/jdbc/IRow.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.jdbc;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public interface IRow {
	public abstract String getFields();

	public abstract void setValues();

	public abstract String getValues();

	public abstract int store();

	public abstract int batchClear();

	public abstract int batchStore();

	public abstract int addBatch();

	public abstract int executeBatch();
}
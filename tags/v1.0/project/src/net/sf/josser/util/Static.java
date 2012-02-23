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
 * Static.java
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
 * $Revision: 94 $
 * $Id: Static.java 94 2007-07-17 13:48:05Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/util/Static.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.util;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Static {
	// filtermatching is used to globally track if topicfilter matches current
	// category and related rdf records
	private static boolean filtermatching = false;

	private static Topics topicsHashtable = null;

	/**
	 * @param topicsHashtable
	 *            The topicsHashtable to set.
	 */
	public static void setTopicsHashtable(final Topics topicsHashtable) {
		Static.topicsHashtable = topicsHashtable;
	}

	public static void initTopicsHashtable() {
		Static.setTopicsHashtable(new Topics());
	}

        public static void initTopicsHashtable(final int capacity) {
		Static.setTopicsHashtable(new Topics(capacity));
	}

	/**
	 * @return Returns the topicsHashtable.
	 */
	public static Topics getTopicsHashtable() {
		return Static.topicsHashtable;
	}

	public static int findParent(final String topic) {
		return Static.topicsHashtable.findParent(topic);
	}

	public static int findTopic(final String topic) {
		return Static.topicsHashtable.findTopic(topic);
	}

	/**
	 * @param filtermatching
	 *            The filtermatching to set.
	 */
	public static void setFiltermatching(final boolean filtermatching) {
		Static.filtermatching = filtermatching;
	}

	/**
	 * @return Returns the filtermatching.
	 */
	public static boolean isFiltermatching() {
		return Static.filtermatching;
	}
}

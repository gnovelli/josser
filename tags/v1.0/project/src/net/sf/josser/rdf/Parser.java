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
 * Parser.java
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
 * $Id: Parser.java 97 2007-07-21 02:24:56Z gnovelli $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sf/josser/rdf/Parser.java $
 *
 *****************************************************************************************
 */

package net.sf.josser.rdf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import net.sf.josser.Josser;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public abstract class Parser implements IParser {
	private int phase;

	public abstract String getPath();

	public abstract int batchClear();

	public abstract int batchStore();

	public void parse(final int grouplines) {
		try {
			if (!Josser.getConnection().isClosed()) {
				try {
					// BufferedReader in = new BufferedReader(new
					// FileReader(this.path));
					final BufferedReader in = new BufferedReader(
							new InputStreamReader(new FileInputStream(this
									.getPath()), "UTF8"));
					String line;
					int numlines = 0;
					final Date date = new Date();
					final long start_time = date.getTime();
					while (((line = in.readLine()) != null)
							&& (this.getPhase() < 2)) {
						numlines = numlines + 1;
						this.process(line);
						final boolean print = (numlines % grouplines) == 0;
						if (print) {
							this.store(numlines, start_time);
						}
					}
					this.store(numlines, start_time);
					in.close();
				} catch (final IOException e) {
				}
			}
		} catch (final Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void store(final int numlines, final long start_time) {
		long elapsed_time = 0;
		long parsed_time = 0;
		long committed_time = 0;
		Date date = new Date();
		parsed_time = date.getTime();
		elapsed_time = (parsed_time - start_time);
		int nerr = this.batchStore();
		if (nerr < 0) {
		}
		try {
			Josser.getConnection().commit();
		} catch (final Exception e) {
			e.printStackTrace(System.err);
		}
		nerr = this.batchClear();
		if (nerr < 0) {
		}
		date = new Date();
		committed_time = date.getTime();
		elapsed_time = (committed_time - start_time);
		System.out.println("COMMITTED: " + numlines + " @ " + elapsed_time / 1000 + " seconds.");
	}

	/**
	 * @param phase
	 *            The phase to set.
	 */
	public void setPhase(final int phase) {
		this.phase = phase;
	}

	/**
	 * @return Returns the phase.
	 */
	public int getPhase() {
		return this.phase;
	}
}

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
 * Josser.java
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
 * $Revision: 83 $
 * $Id: Josser.java 83 2007-07-05 13:40:21Z admin $
 * $HeadURL: file:///var/svn/dmoz/trunk/netbeans/josser/src/net/sourceforge/josser/util/Josser.java $
 *
 *****************************************************************************************
 */

package net.sf.josser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import net.sf.josser.rdf.impl.Dump;

/**
 * @author Copyright © Giovanni Novelli. All rights reserved.
 */
public class Josser {
	public static String getTopicfilter() {
		return Josser.top;
	}

	/**
	 * @return Returns the connection to JDBC database specified in properties file.
	 */
	public static Connection getConnection() {
		try {
			if (Josser.connection == null) {
				Josser.connection = Josser.connect();
			} else if (Josser.connection.isClosed()) {
				Josser.connection = Josser.connect();
			}
		} catch (final Exception e) {
			e.printStackTrace(System.err);
		}
		return Josser.connection;
	}

        public static void main(final String[] args) {
		Josser.initProperties();
		final boolean test = Josser.checkConnection();
		if (test) {
			final Dump dmoz = new Dump(Josser.getPath());
			dmoz.parse(Josser.getEngine(), Josser.getRChunk(), Josser.getWChunk());
		} else {
			System.exit(1);
		}
	}

	private static Connection connection;
	static private String driver = null;
	static private String db = null;
	static private String host = null;
        static private String username = null;
        static private String password = null;
	static private int port = 0;
	private static String engine = null;
	private static String path = null;
	private static int wchunk = 0;
	private static int rchunk = 0;
	private static String top = null;
	private static Properties properties = null;

	private static void initProperties() {
		Josser.properties = new Properties();
		try {
			Josser.properties.load(new FileInputStream("josser.properties"));
		} catch (final IOException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
		String property = null;
		property = Josser.properties.getProperty("driver");
		Josser.setDriver(property);

		property = Josser.properties.getProperty("path");
		Josser.setPath(property);
		property = Josser.properties.getProperty("engine");
		Josser.setEngine(property);
		property = Josser.properties.getProperty("rchunk");
		Josser.setRChunk(Integer.parseInt(property));
		property = Josser.properties.getProperty("wchunk");
		Josser.setWChunk(Integer.parseInt(property));

		property = Josser.properties.getProperty("host");
		Josser.setHost(property);
		property = Josser.properties.getProperty("db");
		Josser.setDB(property);
		property = Josser.properties.getProperty("username");
		Josser.setUsername(property);
		property = Josser.properties.getProperty("password");
		Josser.setPassword(property);
		property = Josser.properties.getProperty("port");
		Josser.setPort(Integer.parseInt(property));
		property = Josser.properties.getProperty("top");
		Josser.setTopicfilter(property);
	}

        private static String getJDBC_URL() {
		String jdbc_url = null;
		if (Josser.getEngine().compareToIgnoreCase("mysql") == 0) {
			jdbc_url = "jdbc:mysql://" + Josser.getHost() + "/" + Josser.getDB()
					+ "?user=" + Josser.getUsername() + "&password="
					+ Josser.getPassword()
					+ "&useUnicode=true&characterEncoding=UTF-8";
		} else if (Josser.getEngine().compareToIgnoreCase("postgresql") == 0) {
			jdbc_url = "jdbc:postgresql://" + Josser.getHost() + "/" + Josser.getDB()
					+ "?user=" + Josser.getUsername() + "&password="
					+ Josser.getPassword();
		}
                return jdbc_url;
        }

	private static Connection connect() {
		String jdbcclass = null;
		jdbcclass = Josser.getDriver();
                String jdbc_url = Josser.getJDBC_URL();
		try {
			Class.forName(jdbcclass);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace(System.err);
			return null;
		}

		try {
			Josser.setConnection(DriverManager.getConnection(jdbc_url));
			Josser.getConnection().setAutoCommit(false);
			return Josser.getConnection();
		} catch (final SQLException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	private static void disconnect() {
		try {
			Josser.getConnection().close();
		} catch (final SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	private static void setConnection(final Connection connection) {
		Josser.connection = connection;
	}

	private static void setEngine(final String engine) {
		Josser.engine = engine;
	}

	private static String getEngine() {
		return Josser.engine;
	}

        private static void setPath(final String path) {
		Josser.path = path;
	}

	private static String getPath() {
		return Josser.path;
	}
	
        private static void setWChunk(final int wchunk) {
		Josser.wchunk = wchunk;
	}

	private static int getWChunk() {
		return Josser.wchunk;
	}

        private static void setRChunk(final int rchunk) {
		Josser.rchunk = rchunk;
	}

	private static int getRChunk() {
		return Josser.rchunk;
	}

        private static void setDB(final String db) {
		Josser.db = db;
	}

        private static String getDB() {
		return Josser.db;
	}

        private static void setHost(final String host) {
		Josser.host = host;
	}

        private static String getHost() {
		return Josser.host;
	}

        private static void setPassword(final String password) {
		Josser.password = password;
	}

        private static String getPassword() {
		return Josser.password;
	}

        private static void setPort(final int port) {
		Josser.port = port;
	}

        private static int getPort() {
		return Josser.port;
	}

        private static void setUsername(final String username) {
		Josser.username = username;
	}

        private static String getUsername() {
		return Josser.username;
	}

        private static void setDriver(final String driver) {
		Josser.driver = driver;
	}

        private static String getDriver() {
		return Josser.driver;
	}

	private static boolean checkConnection() {
		boolean result = true;
		Josser.connection = Josser.getConnection();
		try {
			Josser.connection.close();
		} catch (final Exception e) {
			e.printStackTrace(System.err);
			result = false;
		}
		return result;
	}

	private static void setTopicfilter(final String topicfilter) {
		Josser.top = topicfilter;
	}
}

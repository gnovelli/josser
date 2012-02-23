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
 * $Revision: 44 $
 * $Id: Josser.java 44 2009-11-17 18:57:21Z gnovelli $
 * $HeadURL: https://josser.svn.sourceforge.net/svnroot/josser/branches/ui/project/src/net/sf/josser/Josser.java $
 *
 *****************************************************************************************
 */
package net.sf.josser;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return Josser.connection;
    }

    public static void main(String[] args) {
        Josser.execute();
    }
    public static void execute() {
        Josser.initProperties();
        boolean test = Josser.checkConnection();
        if (test) {
            System.out.println(
                    "Successfully connected to " +
                    Josser.getEngine().toUpperCase() +
                    " database " +
                    Josser.getDB() +
                    "@" +
                    Josser.getHost() +
                    " as user " +
                    Josser.getUsername());
            Dump dmoz = new Dump(Josser.getPath());
            System.out.println(
                    "Going to parse RDF DMOZ dumps located under path" +
                    " " +
                    Josser.getPath() +
                    " for all entries under category " +
                    Josser.getTopicfilter());
            dmoz.parse(Josser.getEngine(), Josser.getRChunk(), Josser.getWChunk());
            Josser.disconnect();
        } else {
            System.exit(1);
        }
    }

    public static Connection connection;
    public static String driver = null;
    public static String db = null;
    public static String host = null;
    public static String username = null;
    public static String password = null;
    public static int port = 0;
    public static String engine = null;
    public static String path = null;
    public static int wchunk = 0;
    public static int rchunk = 0;
    public static String top = null;
    public static Properties properties = null;

    public static void initProperties() {
        initProperties("josser.properties");
    }

    public static void initProperties(String configuration) {
        Josser.properties = new Properties();
        try {
            Josser.properties.load(new FileInputStream(configuration));
        } catch (IOException e) {
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

    public static String getJDBC_URL() {
        String jdbc_url = null;
        if (Josser.getEngine().compareToIgnoreCase("mysql") == 0) {
            jdbc_url = "jdbc:mysql://" + Josser.getHost() + "/" + Josser.getDB() + "?user=" + Josser.getUsername() + "&password=" + Josser.getPassword() + "&useUnicode=true&characterEncoding=UTF-8";
        } else if (Josser.getEngine().compareToIgnoreCase("postgresql") == 0) {
            jdbc_url = "jdbc:postgresql://" + Josser.getHost() + "/" + Josser.getDB() + "?user=" + Josser.getUsername() + "&password=" + Josser.getPassword();
        }
        return jdbc_url;
    }

    public static String getJDBC_URL_NODB() {
        String jdbc_url = null;
        if (Josser.getEngine().compareToIgnoreCase("mysql") == 0) {
            jdbc_url = "jdbc:mysql://" + Josser.getHost() + "/" + "?user=" + Josser.getUsername() + "&password=" + Josser.getPassword() + "&useUnicode=true&characterEncoding=UTF-8";
        } else if (Josser.getEngine().compareToIgnoreCase("postgresql") == 0) {
            jdbc_url = "jdbc:postgresql://" + Josser.getHost() + "/" + "?user=" + Josser.getUsername() + "&password=" + Josser.getPassword();
        }
        return jdbc_url;
    }


    public static Connection connect() {
        String jdbcclass = null;
        jdbcclass = Josser.getDriver();
        String jdbc_url = Josser.getJDBC_URL();
        try {
            Class.forName(jdbcclass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
            return null;
        }

        try {
            Josser.setConnection(DriverManager.getConnection(jdbc_url));
            Josser.getConnection().setAutoCommit(false);
            return Josser.getConnection();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static Connection connect_nodb() {
        String jdbcclass = null;
        jdbcclass = Josser.getDriver();
        String jdbc_url = Josser.getJDBC_URL_NODB();
        try {
            Class.forName(jdbcclass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
            return null;
        }

        try {
            Josser.setConnection(DriverManager.getConnection(jdbc_url));
            Josser.getConnection().setAutoCommit(false);
            return Josser.getConnection();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }


    public static boolean disconnect() {
        boolean result;
        try {
            Josser.getConnection().close();
            Josser.connection = null;
            result=true;
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            result=false;
        }
        return result;
    }

    public static void setConnection(Connection connection) {
        Josser.connection = connection;
    }

    public static void setEngine(String engine) {
        Josser.engine = engine;
    }

    public static String getEngine() {
        return Josser.engine;
    }

    public static void setPath(String path) {
        Josser.path = path;
    }

    public static String getPath() {
        return Josser.path;
    }

    public static void setWChunk(int wchunk) {
        Josser.wchunk = wchunk;
    }

    public static int getWChunk() {
        return Josser.wchunk;
    }

    public static void setRChunk(int rchunk) {
        Josser.rchunk = rchunk;
    }

    public static int getRChunk() {
        return Josser.rchunk;
    }

    public static void setDB(String db) {
        Josser.db = db;
    }

    public static String getDB() {
        return Josser.db;
    }

    public static void setHost(String host) {
        Josser.host = host;
    }

    public static String getHost() {
        return Josser.host;
    }

    public static void setPassword(String password) {
        Josser.password = password;
    }

    public static String getPassword() {
        return Josser.password;
    }

    public static void setPort(int port) {
        Josser.port = port;
    }

    public static int getPort() {
        return Josser.port;
    }

    public static void setUsername(String username) {
        Josser.username = username;
    }

    public static String getUsername() {
        return Josser.username;
    }

    public static void setDriver(String driver) {
        Josser.driver = driver;
    }

    public static String getDriver() {
        return Josser.driver;
    }

    public static boolean checkConnection() {
        boolean result = true;
        Josser.connection = Josser.getConnection();
        try {
            Josser.disconnect();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            result = false;
        }
        return result;
    }
    public static void setTopicfilter(String topicfilter) {
        Josser.top = topicfilter;
    }
}


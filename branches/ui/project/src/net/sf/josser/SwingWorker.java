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
 * SwingWorker.java
 *
 * Created on 23 March 2009, 22.00 by Giovanni Novelli
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
 * $Revision: 42 $
 * $Id: SwingWorker.java 42 2009-03-26 19:59:08Z gnovelli $
 * $HeadURL: https://josser.svn.sourceforge.net/svnroot/josser/branches/ui/project/src/net/sf/josser/SwingWorker.java $
 *
 *****************************************************************************************
 */

package net.sf.josser;
import javax.swing.SwingUtilities;

abstract class SwingWorker {
    private Object value;
    private static class ThreadVar {
        private Thread thread;
        ThreadVar(Thread t) { thread = t; }
        synchronized Thread get() { return thread; }
        synchronized void clear() { thread = null; }
    }
    private ThreadVar threadVar;
    protected synchronized Object getValue() {
        return value;
    }
    private synchronized void setValue(Object x) {
        value = x;
    }
    public abstract Object construct();
    public void finished() {
    }

    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }
    public Object get() {
        while (true) {
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace(System.err);
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {
           public void run() { finished(); }
        };

        Runnable doConstruct = new Runnable() {
            public void run() {
                try {
                    setValue(construct());
                }
                finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };

        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}

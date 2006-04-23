/*
 * Created on 17.01.2005
 *
 * Kevin Goslar,
 * Chair for Computer Networks,
 * TU-Dresden
 */
package org.peertrust.inference.prolog.yprolog;


/** The class IO simply centralises all IO for easy modification */
class IO {

    public final static void error(String caller,String mesg) {
        System.out.print(
            "ERROR: in " + caller + " : " + mesg + "\n");
    }

    // fatal error ...
    public final static void fatalerror(String caller,String mesg) {
        System.out.print(
            "FATAL ERROR: in " + caller + " : " + mesg + "\n");
        System.exit(1);
    }

    public final static void result(String s) {
        System.out.print(s+"\n");
    }

    public final static void diagnostic(String s) {
        System.out.print("*** "+s+"\n");
    }

    public final static void trace(String s) {
        System.out.print(s+"\n");
    }

    public final static void prologprint(String s) {
        System.out.print(s);
    }
}


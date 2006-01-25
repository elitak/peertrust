/**
* This class is distributed under the revised BSD license.
* 
* Copyright (c) 1996-2005 by:
* Michael Winikoff, RMIT University, Melbourne, Australia
* Jean Vaucher, Montreal university, Canada
* Boris van Schooten, University of Twente, Netherlands
* All rights reserved.
* 
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* 
*     * Redistributions of source code must retain the above copyright notice,
*      this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright notice,
*       this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the organizations nor the names of its contributors
*       may be used to endorse or promote products derived from this software
*       without specific prior written permission.
* 
* * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.peertrust.inference.prolog.yprolog;

/*
 * Created on 17.01.2005
 *
 * Kevin Goslar,
 * Chair for Computer Networks,
 * TU-Dresden
 */

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


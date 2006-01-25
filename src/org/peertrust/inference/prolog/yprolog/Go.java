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

// A simple program which interfaces to the XProlog engine.
// Author: J. Vaucher
// Author: Michael Winikoff (winikoff@cs.mu.oz.au)
// Date: 6/3/97
//
// Usage: java Go util.pro
// 
// 

import java.io.*;
import java.util.*;

public class Go 
{
    static public void main(String args[]) {
        try {
            YProlog eng = new YProlog();
			int i=0;
			Vector queries_to_exec = new Vector(20,20);
			Vector tbl_queries_to_exec = new Vector(20,20);
            while (i<args.length) {
				boolean is_k_switch=false;
				if (args[i].equals("-?") || args[i].startsWith("-h")) {
					System.out.println(
 "\nUsage: yprolog.Go [switches] [list of *.pro files]\n"
+"Switches: -d  <query to execute, output in prolog format>\n"
+"          -t  <query to execute, output to table>\n"
+"          -r        <filename of tab-separated db record to read>\n"
+"          -r<char>  <filename of <char>-separated db record to read>\n"
+"          -k        <keyspec>  <filename of tab-separated db record to read>\n"
+"          -k<char>  <keyspec> <filename of <char>-separated db record to read>\n");
					System.exit(0);
					i++;
				} else if (args[i].equals("-d")) {
					if (args.length > i+1) {
						queries_to_exec.add(args[i+1]);
					} else {
						System.out.println("Option -e must have argument.");
					}
					i+=2;
				} else if (args[i].equals("-t")) {
					if (args.length > i+1) {
						tbl_queries_to_exec.add(args[i+1]);
					} else {
						System.out.println("Option -t must have argument.");
					}
					i+=2;
				} else if (args[i].startsWith("-r")
				||        (is_k_switch=args[i].startsWith("-k")) ) {
					if (args.length > i + (is_k_switch ? 2 : 1) ) {
						String separator = args[i].substring(2);
						if (separator.equals("")) separator="\t";
						String keyspec=null;
						if (is_k_switch) {
							keyspec = args[i+1];
							i++;
						}
						File file = new File(args[i+1]);
						String functor = file.getName();
						// remove everything behind a ".",
						// i.e. try to chop file extension
						if (functor.indexOf(".") >= 0) {
							functor=functor.substring(0,functor.indexOf("."));
						}
						System.out.println("Consulting "
							+(separator.equals("\t") ? "tab" :"'"+separator+"'")
							+"-separated table "
							+(is_k_switch ? "with keyspec '"+keyspec+"' ":"")
							+"as functor '"+functor+"'...");
						eng.assertStream(
							new BufferedReader(new FileReader(file)),
							0, separator, functor, keyspec );
					} else {
						System.out.println("Option -[rk] must have argument(s).");
					}
					i+=2;
				} else {
					System.out.println("Consulting '"+args[i]+"'...");
	                eng.consultFile(args[i]);
					i++;
				}
            }
			if (queries_to_exec.size()==0 && tbl_queries_to_exec.size()==0) {
				// no queries -> start from console
	            interactive(eng,System.in,System.out);
			} else {
				// queries, simulate keyboard input
				System.out.println("Dumping console output"
				+" to 'a.out'; dumping query output to 'a<n>.out'.");
				String kbinput = "";
				if (queries_to_exec.size()>0)
					System.out.println("Executing queries...");
				for (Enumeration e=queries_to_exec.elements();
				e.hasMoreElements(); ) {
					kbinput += e.nextElement()+"\nd\n";
				}
				if (tbl_queries_to_exec.size()>0)
					System.out.println("Executing table queries...");
				for (Enumeration e=tbl_queries_to_exec.elements();
				e.hasMoreElements(); ) {
					kbinput += e.nextElement()+"\nt\n";
				}
				kbinput += "quit\n";
				// note, we can supply the encoding with the printstream
				interactive(eng,
					new BufferedReader(new StringReader(kbinput)),
					new PrintStream(new FileOutputStream("a.out")) );
			}
        }
        //catch (FileNotFoundException x) {
        //    System.out.println("Can't find: " +args[0]);
        //}
        catch (Exception f) { 
            f.printStackTrace();
        }
    }

    public static void interactive(YProlog eng,
	InputStream in, PrintStream out) {
		BufferedReader F = new BufferedReader(new InputStreamReader( in ));
		interactive(eng,F,out);
	}

    public static void interactive(YProlog eng,
	BufferedReader F, PrintStream out) {
        String program; String query ;
		eng.yp_eng.fini=false;
        try {
            out.println("\n" + YProlog.VERSION );
			int dump_idx=1;
            while (! eng.yp_eng.fini ) 
                try     
                {
                    out.println();
                    out.print("?- "); out.flush(); 
                    query = F.readLine();

					boolean res = eng.yp_eng.setQuery( query );
					boolean ansinteractive=true;
					while (! eng.yp_eng.fini) {
						if (!res) { 
							out.println(">>> No. ");
							break;
						}
						if (ansinteractive) {
							out.println();
						}
						out.println(">>> Answer: " + eng.yp_eng.answer()
										 + "   ( " + eng.yp_eng.getTime() + " ms.)" );
						if (ansinteractive) {
							String dumpfilename = "a"+dump_idx+".out";
							out.print("More? 'y' ';' = yes, 'a' = all, "
							+"'d' 't' = dump all results to '"+dumpfilename
							+"' : ");
							out.flush();
							String ans =  F.readLine().trim();
							if (ans.equals("y") | ans.equals(";")) {
								res = eng.yp_eng.more();
							} else if (ans.equals("a")) {
								res = eng.yp_eng.more();
								ansinteractive=false;
							} else if (ans.equals("d")) {
								out.println("Dumping answers as Prolog "+
									"clauses to '"+dumpfilename+"'...");
								FileWriter fw = new FileWriter(dumpfilename);
								String ans_str=eng.queryToString(query,0,"\n");
								fw.write(ans_str,0,ans_str.length());
								fw.write('\n');
								fw.close();
								dump_idx++;
								// we interrupted the query/more flow, so we
								// can't continue
								break;
							} else if (ans.equals("t")) {
								out.println("Dumping answers as tab separated"
									+" table to '"+dumpfilename+"'...");
								PrintStream ps = new PrintStream(
									new FileOutputStream(dumpfilename) );
								eng.queryToStream(ps, query,0, "\t","\n");
								ps.close();
								dump_idx++;
								// we interrupted the query/more flow, so we
								// can't continue
								break;
							} else {
								break;
							}
						} else {
							res = eng.yp_eng.more();
						}
					}	   
				}
	            catch (ParseException x) {
	                out.println("Parsing Problem:");
					out.println(x.getMessage());
	            }
	            catch (TokenMgrError x) {
	                out.println("Token Manager Problem:");
					out.println(x.getMessage());
	            }
                                
        } 
        catch (Exception f) { 
            f.printStackTrace();
        }
    }

}


/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.peertrust.inference;

import java.applet.Applet;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import net.jxta.edutella.util.Configurable;
import net.jxta.edutella.util.Configurator;
import net.jxta.edutella.util.Option;

import org.apache.log4j.Logger;
import org.peertrust.Vocabulary;
import org.peertrust.meta.Tree;

import com.ifcomputer.minerva.Minerva;
import com.ifcomputer.minerva.MinervaAtom;
import com.ifcomputer.minerva.MinervaCompound;
import com.ifcomputer.minerva.MinervaList;
import com.ifcomputer.minerva.MinervaLong;
import com.ifcomputer.minerva.MinervaSystemError;
import com.ifcomputer.minerva.MinervaTerm;
import com.ifcomputer.minerva.MinervaVariable;

/**
 * $Id: MinervaProlog.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * @author olmedilla
 * @date 05-Dec-2003 Last changed $Date: 2004/08/07 12:51:54 $ by $Author:
 *       dolmedilla $
 * @description
 */
public class MinervaProlog implements InferenceEngine, Configurable {
    private final String META_INTERPRETER_FILENAME = "interpreter";

    private final String RDF_PARSER_FILENAME = "rdfParser";

    private final String RDF_PARSER_PREDICATE = "read_RDF_file";

    private final String PROLOG_PARSER_FILENAME = "load";

    private final String PROLOG_PARSER_PREDICATE = "read_prolog_file";

    private final String INIT_PREDICATE = "init";

    private Minerva engine;

    private Hashtable varList = new Hashtable();

    private Configurator config;

    private String files = "";

    private String rdfFiles = "";

    private boolean modeApplet;

    //	private String predicates = "" ;

    private static Logger log = Logger.getLogger(MinervaProlog.class);

    public MinervaProlog(Configurator config) {
        log.debug("Created");

        modeApplet = false;
        String[] minervaArgs = new String[2];

        minervaArgs[0] = "-c";
        minervaArgs[1] = "minervagui.mca";

        try {
            engine = new Minerva(minervaArgs);
        } catch (IOException e) {
            log.error("Minerva I/O initialization exception:", e);
        } catch (MinervaSystemError e) {
            log.error("minerva: Minerva initialization exception: ", e);
        }
        this.config = config;
        log.debug("Engine initialized");
    }

    public MinervaProlog(Applet applet, Configurator config) {
        modeApplet = true;
        String[] minervaArgs = new String[2];

        minervaArgs[0] = "-c";
        minervaArgs[1] = "minervagui.mca";

        try {
            engine = new Minerva(applet, minervaArgs);
        } catch (IOException e) {
            log.error("Minerva I/O initialization exception:", e);
        } catch (MinervaSystemError e) {
            log.error("minerva: Minerva initialization exception: ", e);
        }
        this.config = config;
        log.debug("Engine initialized");
    }

    public void init() {
        try {
            engine.load(META_INTERPRETER_FILENAME);

            engine.execute(INIT_PREDICATE);

            engine.load(RDF_PARSER_FILENAME);

            log.debug("Program loaded");

            StringTokenizer filesString = new StringTokenizer(rdfFiles, ":");
            String tmp;
            while (filesString.hasMoreTokens()) {
                tmp = config.getValue(Vocabulary.BASE_FOLDER_TAG)
                        + filesString.nextToken();

                MinervaAtom atom;
                if (modeApplet)
                    atom = new MinervaAtom("file://" + tmp);
                else
                    atom = new MinervaAtom(tmp);

                log.debug("Loading RDF file " + tmp
                        + " into the inference engine");

                engine.execute(RDF_PARSER_PREDICATE, atom);

                log.debug("RDF file " + tmp + " loaded");
            }

            engine.load(PROLOG_PARSER_FILENAME);

            filesString = new StringTokenizer(files, ":");
            while (filesString.hasMoreTokens()) {
                tmp = config.getValue(Vocabulary.BASE_FOLDER_TAG)
                        + filesString.nextToken();

                MinervaAtom atom;
                if (modeApplet)
                    atom = new MinervaAtom("file://" + tmp);
                else
                    atom = new MinervaAtom(tmp);

                log.debug("Loading file " + tmp + " into the inference engine");
                engine.execute(PROLOG_PARSER_PREDICATE, new MinervaAtom(tmp));
                log.debug("File " + tmp + " loaded");
            }

            //			StringTokenizer predicatesString = new
            // StringTokenizer(predicates,":") ;
            //		
            //			while (predicatesString.hasMoreTokens())
            //				engine.execute(predicatesString.nextToken()) ;
        } catch (MinervaSystemError e) {
            log.error("Error loading files in Minerva", e);
        } catch (IOException e) {
            log.error("I/O error loading files in Minerva", e);
        }

    }

    void initParse() {
        varList.clear();
    }

    Object[] extractTerms(String query) {
        Vector terms = new Vector();
        int numberBrackets = 0;
        int numberSquareBrackets = 0;
        StringBuffer currentTerm = new StringBuffer("");
        for (int i = 0; i < query.length(); i++) {
            char car = query.charAt(i);
            if (car == '(')
                numberBrackets++;
            else if (car == '[')
                numberSquareBrackets++;
            else if (car == ')')
                numberBrackets--;
            else if (car == ']')
                numberSquareBrackets--;
            else if ((car == ',') && (numberBrackets == 0)
                    && (numberSquareBrackets == 0)) {
                terms.add(currentTerm.toString());
                currentTerm = new StringBuffer("");
                continue;
            }

            currentTerm.append(car);
        }

        if (currentTerm.length() > 0)
            terms.add(currentTerm.toString());

        return terms.toArray();
    }

    public MinervaTerm parse(String originalQuery) {
        String query = originalQuery.trim();
        StringBuffer tmp = new StringBuffer("");
        MinervaTerm term = null;

        char initChar = query.charAt(0);

        if (initChar == '[') {
            int indexL = query.lastIndexOf(']');

            // is a list
            Object[] list = extractTerms(query.substring(1, query.length() - 1));
            MinervaTerm[] minervaArgs = new MinervaTerm[list.length];

            for (int i = 0; i < list.length; i++)
                minervaArgs[i] = parse((String) list[i]);
            term = MinervaList.create(minervaArgs);

            //			StringTokenizer list = new StringTokenizer (query.substring(1,
            // query.length() - 1), ",") ;
            //			int numElements = list.countTokens() ;
            //			MinervaTerm [] minervaArgs = new MinervaTerm[numElements] ;
            //				
            //			for (int i = 0 ; i < numElements ; i++)
            //				minervaArgs[i] = parseTerm (list.nextToken()) ;
            //			term = MinervaList.create(minervaArgs) ;
        } else if ((initChar >= '0') && (initChar <= '9')) {
            // is a number
            term = new MinervaLong(new Long(query).longValue());
        } else {
            int indexEd = query.lastIndexOf('@');
            int index$ = query.lastIndexOf('$');
            int indexPred = query.lastIndexOf(')');
            int index;
            if ((indexEd > index$) && (indexEd > indexPred)) {
                //	predicate with @
                term = new MinervaCompound("@", parse(query.substring(0,
                        indexEd)), parse(query.substring(indexEd + 1, query
                        .length())));
            } else if ((index$ > indexEd) && (index$ > indexPred)) {
                // predicate with $
                term = new MinervaCompound("$", parse(query
                        .substring(0, index$)), parse(query.substring(
                        index$ + 1, query.length())));
            } else if ((indexPred > indexEd) && (indexPred > index$)) {
                index = query.indexOf('(');
                // it is a predicate with arguments
                String predicate = query.substring(0, index);
                Object[] args = extractTerms(query.substring(index + 1, query
                        .length() - 1));
                if (args.length > 0) {
                    MinervaTerm[] minervaArgs = new MinervaTerm[args.length];

                    for (int i = 0; i < args.length; i++)
                        minervaArgs[i] = parse((String) args[i]);
                    term = new MinervaCompound(predicate, minervaArgs);
                } else {
                    System.err
                            .println("minerva: error at '"
                                    + query
                                    + "'. Predicates without arguments are not allowed");
                    term = new MinervaAtom("nil");
                }
            } else {
                if (((query.charAt(0) >= 'A') && (query.charAt(0) <= 'Z'))
                        || (query.charAt(0) == '_')) {
                    // it is a variable
                    term = (MinervaVariable) varList.get(query);
                    if (term == null) {
                        term = new MinervaVariable();
                        varList.put(query, term);
                    }
                } else {
                    // it is an atom
                    term = new MinervaAtom(query);
                    // log.debug("Atom found: " + query) ;
                }
            }
        }
        return term;
    }

    public String unparse(MinervaTerm term) {
        String query = null;
        StringBuffer tmp = new StringBuffer("");

        switch (term.typeOf()) {
        case MinervaTerm.ATOM:
            query = ((MinervaAtom) term).stringValue();
            // log.debug("Result atom parsed:" + query) ;
            break;
        case MinervaTerm.COMPOUND:
            MinervaCompound compound = (MinervaCompound) term;
            String functor = compound.getFunctor();
            if ((functor.equals("@")) || (functor.equals("$")))
                query = unparse(compound.getArg(0)) + functor
                        + unparse(compound.getArg(1));
            else {
                tmp.append(functor + "(");
                for (int i = 0; i < compound.getArity(); i++)
                    tmp.append(unparse(compound.getArg(i)) + ",");
                tmp.setCharAt(tmp.length() - 1, ')');
                query = tmp.toString();
            }
            break;
        case MinervaTerm.LIST:
            tmp.append("[");
            Enumeration enum = ((MinervaList) term).getEnumeration();
            while (enum.hasMoreElements())
                tmp.append(unparse((MinervaTerm) enum.nextElement()) + ",");
            tmp.setCharAt(tmp.length() - 1, ']');
            query = tmp.toString();
            break;
        case MinervaTerm.LONG:
            query = Long.toString(((MinervaLong) term).longValue());
            break;
        case MinervaTerm.VARIABLE:
            MinervaVariable var = ((MinervaVariable) term);
            if (var.getValue() == null)
                query = var.toString();
            else
                query = unparse(var.getValue());
            break;
        default:
            query = "unknown";
        }
        return query;
    }

    public boolean execute(String query) {
        log.debug("parse: " + query);
        MinervaTerm minQuery = parse(query);
        boolean ret = false;

        if (minQuery.typeOf() == MinervaTerm.COMPOUND) {
            MinervaCompound term = (MinervaCompound) minQuery;
            MinervaTerm[] terms = new MinervaTerm[term.getArity()];
            for (int i = 0; i < terms.length; i++)
                terms[i] = term.getArg(i);
            try {
                log.debug("Sending to the engine:" + minQuery.toString());
                ret = engine.execute(term.getFunctor(), terms);
            } catch (MinervaSystemError e) {
                log.error("Error executing a query at Minerva", e);
                ret = false;
            }
        } else
            log.error("The query " + query
                    + " is not an executable Prolog predicate");

        return ret;
    }

    // processTree(tree(Id,Goal,Queries,Proof,Requester),TreeResults)
    public LogicAnswer[] processTree(LogicQuery logicQuery) {
        log.debug("Process logic query: " + logicQuery.getGoal() + " - "
                + logicQuery.getSubgoals());

        String query = "tree(" + logicQuery.getGoal() + ","
                + logicQuery.getSubgoals() + "," + "[],"
                + logicQuery.getRequester() + ")";

        log.debug("Query: " + query);
        initParse();
        MinervaTerm minQuery = parse(query);

        log.debug("Minerva Query: " + minQuery.toString());

        MinervaVariable resultVar = new MinervaVariable();

        try {
            log.debug("Sending to the engine:" + "processTree(" + minQuery
                    + ",Return)");
            engine.execute("processTree", minQuery, resultVar);
            log.debug("Receiving from the engine:"
                    + resultVar.getValue().toString());
        } catch (MinervaSystemError e) {
            log.error("Error processing the tree in Minerva", e);
        }

        if (resultVar.getValue() == null) {
            log.debug("No answers");
            return null;
        }

        String result = unparse(resultVar);
        log.debug("Parsed results: " + result);

        Object[] answerStrings = extractTerms(result.substring(1, result
                .length() - 1));

        if (answerStrings.length > 0) {
            LogicAnswer[] answers = new LogicAnswer[answerStrings.length];
            for (int i = 0; i < answerStrings.length; i++) {
                String currentTreeString = (String) answerStrings[i];

                log.debug("Current tree string: " + currentTreeString);

                Object[] answerArgs = extractTerms(currentTreeString.substring(
                        "tree(".length(), currentTreeString.length() - 1));

                String delegator = (String) answerArgs[3];

                log.debug("Delegator: " + delegator);

                if (delegator.equals("nil"))
                    delegator = null;

                answers[i] = new LogicAnswer((String) answerArgs[0],
                        (String) answerArgs[4], (String) answerArgs[1],
                        (String) answerArgs[2], delegator);
            }
            return answers;
        } else
            return null;
    }

    public void unifyTree(Tree tree, String unifiedGoal) {
        String query = "old(" + tree.getGoal() + ","
                + tree.getLastExpandedGoal() + "," + tree.getSubqueries() + ")";

        log.debug("Unify new query: " + unifiedGoal + " and old query: "
                + query);

        initParse();
        MinervaTerm minQuery = parse(query);
        MinervaTerm minNewQuery = parse(unifiedGoal);

        //System.out.println ("Parsed tree: " + minQuery) ;
        //System.out.println ("Parsed new query: " + minNewQuery) ;

        MinervaVariable resultVar = new MinervaVariable();

        try {
            engine.execute("unification", minQuery, minNewQuery, resultVar);
        } catch (MinervaSystemError e) {
            System.err.println("Minerva: error unificating in Minerva: "
                    + e.getMessage());
        }

        String result = unparse(resultVar);

        log.debug("Unified parsed results: " + result);

        Object[] treeStrings = extractTerms(result.substring("new(".length(),
                result.length() - 1));

        tree.setLastExpandedGoal(null);
        tree.setGoal((String) treeStrings[0]);
        tree.setSubqueries((String) treeStrings[1]);
    }

    /**
     * @see net.jxta.edutella.util.Configurable#getOptions()
     */
    public Option[] getOptions() {
        Option filesOpt = new Option('l', "prolog.files", "Files",
                "Files to load", false);
        Option rdfFilesOpt = new Option('r', "prolog.rdfFiles", "RDF Files",
                "RDF Files to load", false);
        //		Option predicatesOpt = new Option(
        //									'r',
        //									"prolog.predicates",
        //									"Predicates",
        //									"Predicates to call",
        //									false);

        //		return new Option[] {filesOpt, predicatesOpt};
        return new Option[] { filesOpt, rdfFilesOpt };
    }

    /**
     * @see net.jxta.edutella.util.Configurable#getPropertyPrefix()
     */
    public String getPropertyPrefix() {
        return "prolog";
    }

    public void setFiles(String files) {
        this.files = files;
    }

    protected String getFiles() {
        return files;
    }

    public void setRdfFiles(String files) {
        this.rdfFiles = files;
    }

    protected String getRdfFiles() {
        return rdfFiles;
    }

    //	public void setPredicates(String predicates) {
    //		this.predicates = predicates ;
    //	}
    //
    //	protected String getPredicates() {
    //		return predicates;
    //	}

    public static void main(String[] args) throws MinervaSystemError,
            IOException {
        parseTest();
        //		MinervaProlog engine = new MinervaProlog() ;
        //		MinervaVariable var = new MinervaVariable() ;
        //
        //		engine.engine.load("javaMetaI_1") ;
        //		
        //		engine.engine.execute("init") ;
        //		
        //		System.out.println ("Result: " + engine.engine.execute("namePeer",
        // var) + " - Var: " + engine.unparse(var) ) ;
        //		
        //		System.out.println ("Finished") ;
    }

    static void parseTest() {
        MinervaProlog engine = new MinervaProlog(null);

        //String query = "prueba(Var, atom, 20, [atom, Var2, [32, Var2, Var,
        // Segunda], tree(juego)], final(Segunda))" ;
        //String query = "prueba(tree(_) @ x, Var, atom, 20, [atom, Var2, [32,
        // Var2, Var, Segunda], tree(juego)], final(Segunda))" ;
        //String query = "tree(prueba @ x $ n, maria(X,j @ t) @ test) $ final"
        // ;
        //String query =
        // "tree(document(project7,V12039161),[query(policy1(document(project7,V12039161,V8970080)),no),query(policy2(document(project7,V12039161,V8970080)),no),query(get_record(project7,V12039161),no)],[r(document(project7,V12039161),[policy1(document(project7,V12039161,V8970080)),policy2(document(project7,V12039161,V8970080))],[get_record(project7,V12039161)])@company7],manuel)"
        // ;
        System.out.println("Result execute: "
                + engine.execute("write('prueba')"));

        String query = "[tree(employee(alice7,microsoft)@microsoft@alice7,[],[signed(r(employee(alice7,microsoft)@microsoft,[],[]),microsoft,signature(microsoft))@alice7,proved_by(alice7)@company7],manuel)]";
        System.out.println("Initial: " + query);

        MinervaTerm term = engine.parse(query);
        System.out.println("Minerva: " + term.toString());

        String query2 = engine.unparse(term);
        System.out.println("Query: " + query2);
    }
}
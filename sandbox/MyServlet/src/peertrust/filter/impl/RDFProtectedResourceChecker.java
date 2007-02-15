package peertrust.filter.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.eyeball.Eyeball;
import com.hp.hpl.jena.eyeball.Report;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

import peertrust.common.CredentialFactory;
import peertrust.common.interfaces.ICredential;
import peertrust.filter.interfaces.IProtectedResourceChecker;

/**
 * Implementation for the IProtectedResourceChecker interface. Checks if a resource
 * is protected or if the client fulfills the policies protecting it. Also loads
 * the policies for each resource from an rdf file (uses the Jena API for that).
 * @author Sebastian Wittler
 */
public class RDFProtectedResourceChecker implements IProtectedResourceChecker {
	// Model that contains the policies for each protected resource
	private Model modelData=null;
	// Logger
	private static Logger log = Logger.getLogger(RDFProtectedResourceChecker.class);

	/**
	 * Load the policies for the protected resources from a rdf file.
	 * @param strSchemaFile The file with the rdf schema to verify the file to load.
	 * @param strModelFile The rdf file with the policies protecting the resources
	 */
	public void loadPolicies(String strSchemaFile,String strModelFile) throws JenaException {
		// Load the rdf schema for the rdf file (to verify it)
		Model modelSchema=FileManager.get().loadModel("file:"+strSchemaFile);//"file:D:\\jena\\schema.rdf");//"http://localhost:8081/MyServlet/schema.rdf");
		// Load the rdf file with the policies
		modelData=FileManager.get().loadModel("file:"+strModelFile);//"file:D:\\jena\\model.rdf");//"http://localhost:8081/MyServlet/model.rdf");
		// Check if the file satisfies the schema
		Reasoner reasoner=ReasonerRegistry.getRDFSReasoner();
		reasoner=reasoner.bindSchema(modelSchema);
		InfModel modelInf=ModelFactory.createInfModel(reasoner,modelData.union(modelSchema));
		ValidityReport validity=modelInf.validate();
		if(!validity.isValid())
		    throw new JenaException("Policy rdf file not correct!");
//TODO: anders Error

		Eyeball eyeball=new Eyeball(modelInf);
		Report report=eyeball.inspect(modelData);
		if(!report.valid()) {
			log.debug("Policy rdf file not correct");
			StmtIterator iter=report.model().listStatements();
			while(iter.hasNext())
				log.debug("report: "+iter.nextStatement());
//TODO: anders Error
		    throw new JenaException("Policy rdf file not correct");
		}
	}

	/**
	 * @see IProtectedResourceChecker.getPolicyForResource
	 */
	public Set getPolicyForResource(String strResource) throws JenaException {
		// Policies that should be returned
//TODO: Policies, not Credentials
		Set<ICredential> setCredentials=new LinkedHashSet<ICredential>();
		// RDF nodes that have to be visited
		Set<String> setStillToVisit=new LinkedHashSet<String>();

		// Get the rdf root node
//TODO: namespace konfigurierbar?
		Resource resRoot=modelData.getResource("http://sourceforge.net/projects/peertrust/#Root");
		ResIterator iter=modelData.listSubjectsWithProperty(RDF.type,resRoot);
		Resource subjectRoot=iter.hasNext() ? (Resource)iter.next() : null;
		iter.close();
		if(subjectRoot==null)
			throw new JenaException("Root can't be found in rdf file");
//TODO: Error anders

		// Get all the starting points (nodes which should be searched first) and
		// add them to the set of still to visit nodes
		Property propStartPoint=modelData.getProperty("http://sourceforge.net/projects/peertrust/#startingPoint");
		StmtIterator iterStmt=modelData.listStatements(subjectRoot,propStartPoint,(RDFNode)null);
		while(iterStmt.hasNext())
			setStillToVisit.add(iterStmt.nextStatement().getObject().toString());
		
		if(setStillToVisit.isEmpty())
			throw new JenaException("No starting points in root");
//TODO: Error anders

		// Visit nodes in still to visit set until all nodes have been visited
		boolean found=false;
		while(!setStillToVisit.isEmpty())
			if(visitNode(setCredentials,setStillToVisit,strResource))
				found=true;
		//Return null, if no policies were found, otherwise return policies
		return ((setCredentials.size()==0)&&(!found)) ? null : setCredentials;
	}

	/**
	 * Visits the next rdf node in the still to visit set. This is a helper method
	 * of the getPolicyForResource method above
	 * @param setCredentials The policies that should be returned for the resource
	 * @param setStillToVisit The set of nodes still to visit
	 * @param strResource The resource for which policies are searcched for
	 * @return If policies were found
	 * @throws JenaException
	 */
	private boolean visitNode(Set<ICredential> setCredentials,Set<String> setStillToVisit,String strResource)
		throws JenaException {
//TODO: comment, code changes maybe
		if(setStillToVisit.isEmpty())
			return false;
		Iterator iter=setStillToVisit.iterator();
		String strURI=(String)iter.next();
		setStillToVisit.remove(strURI);
		
		Property propPolicy=modelData.getProperty("http://sourceforge.net/projects/peertrust/#policy");
		Property propFilter=modelData.getProperty("http://sourceforge.net/projects/peertrust/#filter");
		Property propExcFilterFile=modelData.getProperty("http://sourceforge.net/projects/peertrust/#exception_filter_file");
		Property propSubItem=modelData.getProperty("http://sourceforge.net/projects/peertrust/#subitem");

		Resource res=modelData.getResource(strURI);

		if((strURI==null)||(strURI.length()==0))
			throw new JenaException("Property name missing");
		String strFileName=strURI;
		if(!strResource.startsWith(strFileName))
			return false;
		
		log.debug("Filename matches to "+strResource);
		
		boolean bAssignPolicy=true;
		
		StmtIterator iterStmt=modelData.listStatements(res,propFilter,(RDFNode)null);
		int counter=0;
		boolean bApplied=false;
		while(iterStmt.hasNext()) {
			counter++;
			String strFilter=iterStmt.nextStatement().getObject().toString();
			log.debug("Filter: "+strFilter);
			if(strFilter.startsWith("*"))
				strFilter=strFilter.substring(1);
			if(strResource.endsWith(strFilter))
				bApplied=true;
		}
		if((!bApplied)&&(counter>0))
			bAssignPolicy=false;

		iterStmt=modelData.listStatements(res,propExcFilterFile,(RDFNode)null);
		bApplied=false;
		while(iterStmt.hasNext()) {
			String strExcFilterFiles=iterStmt.nextStatement().getObject().toString();
			log.debug("ExcFilterFiles: "+strExcFilterFiles);
			if(strResource.equals(strExcFilterFiles))
				bApplied=true;
		}
		if(bApplied)
			bAssignPolicy=false;

		iterStmt=modelData.listStatements(res,propPolicy,(RDFNode)null);
		while(iterStmt.hasNext()) {
			String strPolicy=iterStmt.nextStatement().getObject().toString();
			log.debug("Policy read: "+strPolicy);
			ICredential credential=CredentialFactory.getInstance().createCredential(strPolicy);
			if(bAssignPolicy) {
				log.debug("Assign policy "+strPolicy);
				iter=setCredentials.iterator();
				bAssignPolicy=true;
				while(iter.hasNext()) {
					ICredential cred=(ICredential)iter.next();
					if(cred.equalsCredential(credential))
						bAssignPolicy=false;
				}
				if(bAssignPolicy)
					setCredentials.add(credential);
				log.debug("Policy already found: "+!bAssignPolicy);
			}
		}

		iterStmt=modelData.listStatements(res,propSubItem,(RDFNode)null);
		while(iterStmt.hasNext())
			setStillToVisit.add(iterStmt.nextStatement().getObject().toString());
		return true;
	}
}

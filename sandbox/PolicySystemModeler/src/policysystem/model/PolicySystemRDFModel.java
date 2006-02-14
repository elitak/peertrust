package policysystem.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Vector;

import org.apache.log4j.Logger;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class PolicySystemRDFModel
				implements ProjectConfigChangeListener
{
	static final public String POLICY_SYSTEM_RES="PolicySystem"; 
	static final public String POLICY_SYSTEM_RES_POLICIES="Policies";
	static final public String POLICY_SYSTEM_RES_RESOURCES="Resources";
	static final public String POLICY_SYSTEM_RES_ASSERTIONS="Assertions";
	static final public String POLICY_SYSTEM_RES_CONFLICT_RESOLUTION=
											"Inheritance Conflict Resolition";
	
	public static final String NS_KB_SCHEMA=
			"http://www.l3s.de/peertrust/modeler/schema/#";
	public static final String NS_KB_DATA=NS_KB_SCHEMA;
	
	public static final String NS_RDF=
			"http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	
	public static final String RDF_TYPE=NS_RDF+"type";
	
	public static final String KB_POLICY_SYSTEM=NS_KB_SCHEMA+"PolicySystem";
	
	public static final String KB_POLICIES=NS_KB_SCHEMA+"policies";
	
	/////classes
	public static final String LNAME_CLASS_RESOURCE="Resource";
	public static Resource CLASS_RESOURCE=null;
	
	public static final String LNAME_CLASS_POLICY="Policy";
	public static Resource CLASS_POLICY=null;
	
	public static final String LNAME_CLASS_OVERRIDDING_RULE="OverridingRule";
	public static Resource CLASS_OVERRIDDING_RULE=null;
	
	public static final String LNAME_PROP_HAS_NAME="name";
	static Property PROP_HAS_NAME=null;
	
	public static final String LNAME_PROP_HAS_MAPPING="hasMapping";
	static Property PROP_HAS_MAPPING=null;
	
	public static final String LNAME_PROP_HAS_SUPER="hasParent";
	static Property PROP_HAS_SUPER=null;
	
	public static final String LNAME_PROP_HAS_VALUE="hasValue";
	static Property PROP_HAS_VALUE=null;
	
	public static final String LNAME_PROP_IS_PROTECTED_BY="isProtectedBy";
	static Property PROP_IS_PROTECTED_BY=null;
	public static final String LNAME_PROP_HAS_OVERRINDING_RULES="hasOverridingRule";
	static Property PROP_HAS_OVERRIDING_RULES=null;
	
	////filter
	public static final String LNAME_PROP_HAS_FILTER="hasFilter";
	static Property PROP_HAS_FILTER=null;
	
	public static final String LNAME_PROP_HAS_CONDITION="hasCondition";
	static Property PROP_HAS_CONDITION=null;
	
	public static final String LNAME_PROP_HAS_IDENTITY="identity";
	static Property PROP_HAS_IDENTITY=null;
	
	public static final String LNAME_CLASS_FILTER="Filter";
	public static Resource CLASS_FILTER=null;
	
	
	private static final PolicySystemRDFModel 
			policySystemRDFModel= new PolicySystemRDFModel();
	
	private Model rdfModel;
	private String rdfModelFile;
	
	private Model schema = null;//ModelLoader.loadModel("file:data/rdfsDemoSchema.rdf");
	private String rdfSchemaFile;
	private Logger  logger;

	private PolicySystemRDFModel()
	{
		super();
		rdfModel=ModelFactory.createDefaultModel();
		schema=ModelFactory.createDefaultModel();
		ProjectConfig.getInstance().addProjectConfigChangeListener(this);
		logger=Logger.getLogger(PolicySystemRDFModel.class);
		
	}
	
	private void createPolicySystemRDFModel() 
											throws IOException 
	{
		ProjectConfig config=ProjectConfig.getInstance();
		String pjtFile=config.getProjectFile();
		
		if(pjtFile==null)
		{
			logger.warn("pjt file not set");
			return;
		}
		
		Properties properties= new Properties();
		properties.load(new FileInputStream(pjtFile));
		this.rdfModelFile=properties.getProperty("rdfModelFile");
		this.rdfSchemaFile=properties.getProperty("rdfSchemaFile");
		logger.info("rdfModelFile="+rdfModelFile+
					" rdfs"+rdfSchemaFile);
		
		InputStream iStream=new FileInputStream(rdfModelFile);
		rdfModel.read(iStream,"");
		rdfModel.write(System.out);
		
		iStream=new FileInputStream(rdfSchemaFile);
		schema.read(iStream,"");
		schema.write(System.out);
		getProperties();
		getResources();
		
	}
	synchronized public void getProperties()
	{
		PROP_HAS_NAME=schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_NAME);
		PROP_HAS_MAPPING=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_MAPPING);
		PROP_HAS_SUPER=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_SUPER);
		PROP_HAS_VALUE=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_VALUE);
		PROP_IS_PROTECTED_BY=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_IS_PROTECTED_BY);
		PROP_HAS_OVERRIDING_RULES=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_OVERRINDING_RULES);
		///filter
		PROP_HAS_FILTER=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_FILTER);
		PROP_HAS_CONDITION=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_CONDITION);
		PROP_HAS_IDENTITY=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_IDENTITY);
		
		if(	PROP_HAS_CONDITION==null ||
				PROP_HAS_FILTER==null ||	
				PROP_HAS_IDENTITY==null ||
				PROP_HAS_MAPPING==null ||
				PROP_HAS_NAME==null ||
				PROP_HAS_OVERRIDING_RULES==null ||
				PROP_HAS_SUPER==null ||
				PROP_HAS_VALUE==null)
		{
			logger.error("model error all prop must be non null");
		}
		return;
	}
	
	synchronized public void getResources()
	{
		CLASS_RESOURCE=schema.getResource(NS_KB_SCHEMA+LNAME_CLASS_RESOURCE);
		CLASS_POLICY=schema.getResource(NS_KB_SCHEMA+LNAME_CLASS_POLICY);
		CLASS_OVERRIDDING_RULE=
			schema.getResource(NS_KB_SCHEMA+LNAME_CLASS_OVERRIDDING_RULE);
		CLASS_FILTER=schema.getResource(NS_KB_SCHEMA+LNAME_CLASS_FILTER);
		//System.out.println("CLASS_RES:"+CLASS_RESOURCE);
		if(		CLASS_FILTER==null ||
				CLASS_OVERRIDDING_RULE==null ||	
				CLASS_POLICY==null ||
				CLASS_RESOURCE==null)
		{
			logger.error("model error all model resource must be non null");
		}
	}
	
	
	
	static public Model getRdfModel() {
		if(policySystemRDFModel==null)
		{
			return null;
		}
		
		return policySystemRDFModel.rdfModel;
		//return rdfModel;
	}


	static public Model getSchema() {
		if(policySystemRDFModel==null)
		{
			return null;
		}
		
		return policySystemRDFModel.schema;
		//return schema;
	}


	synchronized public Vector getPolicySystems()
	{
		Property typeProp=rdfModel.createProperty(RDF_TYPE);
		Resource typeRes=rdfModel.createResource(KB_POLICY_SYSTEM);
		//String propVal=null;//RS_NAME_POLICY_SYSTEM;//null;
		
		Selector psSel=new SimpleSelector(null,typeProp,typeRes);
		StmtIterator it=rdfModel.listStatements(psSel);
		//it=rdfModel.listStatements();
		Vector resources=new Vector();
		Statement st;
		for(;it.hasNext();){
			st=it.nextStatement();
			resources.add(st.getSubject());
			//System.out.println("=====>it:"+st.getSubject());
		}
		return resources;
	}
	
	synchronized public Vector getPolicySystemPolicies(Resource psRes)
	{
		Property typeProp=rdfModel.createProperty(KB_POLICIES);
		Resource typeRes=null;//rdfModel.createResource(RS_NAME_POLICY_SYSTEM);
		
		Selector psSel=new SimpleSelector(psRes,typeProp,typeRes);
		StmtIterator it=rdfModel.listStatements(psSel);
		Vector resources=new Vector();
		Statement st;
		for(;it.hasNext();){
			st=it.nextStatement();
			resources.add(st.getSubject());
			//System.out.println("=====>it:"+st);
		}
		return resources;
	}
	
	synchronized static public PolicySystemRDFModel getInstance()
	{
		return policySystemRDFModel;
	}
	
	static public void main(String[] args) throws IOException{
//		//String rdfPath="/home/pat_dev/Ontologies/policies/policy_system.rdf";
//		String rdfPath="/home/pat_dev/Ontologies/ptmodeler/export/PeertrustModelerTools.rdf";
//		Vector ps=PolicySystemRDFModel.getInstance(rdfPath).getPolicySystems();
//		System.out.println("ps:"+ps);
	}	
	
	public void saveTo(String fileName) throws Exception
	{
		if(fileName==null){
			return;
		}
		FileOutputStream outStream= new FileOutputStream(fileName);
		rdfModel.write(outStream);
		return;
	}
		
/////////////////////////////////////////////////////////////////////////////
///////////////////////UTIL FUNCS////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
	static public HierarchyNodeCreationMechanism 
		resourceHierarchyCreationMechanism=
			new HierarchyNodeCreationMechanism(){

				public ModelObjectWrapper createNode(
											Model rdfModel, 
											String nodeName) 
				{
					if(rdfModel==null || nodeName==null || NS_KB_DATA==null)
					{
						return null;
					}
					Resource res=rdfModel.createResource(NS_KB_DATA+nodeName);
					res.addProperty(PROP_HAS_MAPPING,nodeName+"mapping");
					res.addProperty(PROP_HAS_NAME,nodeName);
					res.addProperty(PROP_HAS_VALUE,nodeName+"value");
					PSResourceImpl psRes= new PSResourceImpl(res);
					return psRes;
				}

				public ModelObjectWrapper createLink(
									Model rdfModel, 
									Resource startResource, 
									Resource targetResource) {
					if(	rdfModel==null || 
						startResource==null || 
						targetResource==null ||
						PROP_HAS_SUPER==null)
					{
						System.out.println("Statement:model="+rdfModel+ 
										" res1="+startResource +
										" res2:"+targetResource);
						return null;
					}
					
					Statement stm=
						rdfModel.createStatement(
										startResource,
										PROP_HAS_SUPER,
										targetResource);
					rdfModel.add(stm);
					System.out.println("Statement:"+stm);
					return new PSHierarchyRelationshipImpl(stm);
				}
		
	};
	
	static final public String getStringProperty(
									Resource resource, 
									Property prop) 
	{
		if(	resource ==null || 
			prop==null)
		{
			getInstance().logger.warn(
					"param resource or prop is null: resource="+resource+
					"prop="+prop);
			return null;
		}
		
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			getInstance().logger.info("no statement ["+resource+"; "+prop+";?]");
			return null;
		}
		return stm.getString();
	}
	
	static final public Boolean getBooleanProperty(
			Resource resource, 
			Property prop) 
	{
		if(	resource ==null || 
			prop==null)
		{
			getInstance().logger.warn(
					"params must not be null: res="+resource+
					" prop:"+prop);
			return null;
		}
		
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			getInstance().logger.info("no statement ["+resource+"; "+prop+";?]");
			return null;
		}
		
		return new Boolean(stm.getBoolean());
	}
	
	static final public void setStringProperty(
				Resource resource, 
				Property prop,
				String objValue) 
	{
		if(resource ==null || prop==null )
		{
			getInstance().logger.warn(
					"params must not be null: res="+resource+
					" prop:"+prop+ " objValue="+objValue);
			return;
		}
		
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			getInstance().logger.info("no statement ["+resource+"; "+prop+";?]");
			return;
		}
		stm.changeObject(objValue);
		return;
	}
	
	static final public void setBooleanProperty(
										Resource resource, 
										Property prop,
										boolean objValue) 
		{
		if(resource ==null || prop==null )
		{
			getInstance().logger.warn(
					"params must not be null: res="+resource+
					" prop:"+prop);
			return ;
		}
		
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			
			return;
		}
		stm.changeObject(objValue);
		return;
	}
	
	static final public Vector getMultipleProperty(
												Resource resource, 
												Property prop) 
	{
		if(resource ==null || prop==null)
		{
			getInstance().logger.warn(
					"params must not be null: res="+resource+
					" prop:"+prop);
			return null;
		}
		
		if(policySystemRDFModel==null)
		{
			getInstance().logger.warn("model instance not created");
			return null;//new PSPolicy[]{};
		}
		Selector psSel=
				new SimpleSelector(
							resource,
							prop,//PROP_IS_PROTECTED_BY,
							(RDFNode)null);
		StmtIterator it=policySystemRDFModel.rdfModel.listStatements(psSel);
		Vector policies=new Vector();
		Statement stm;
		while(it.hasNext())
		{
			stm=it.nextStatement();
			policies.add(new PSPolicyImpl((Resource)stm.getObject()));
		}
		
		return policies;
	}
	
	static final public Vector getMultipleStatement(
											Resource resource, 
											Property prop) 
	{
		if(resource ==null || prop==null)
		{
			getInstance().logger.warn("params resource and prop must all be non null");
			return null;
		}
		
		if(policySystemRDFModel==null)
		{
			getInstance().logger.warn("model singeton not created yet");
			return null;//new PSPolicy[]{};
		}
		
		Selector psSel=
			new SimpleSelector(
						resource,
						prop,//PROP_IS_PROTECTED_BY,
						(RDFNode)null);
		StmtIterator it=policySystemRDFModel.rdfModel.listStatements(psSel);
		Vector policies=new Vector();
		Statement stm;
		while(it.hasNext())
		{
			stm=it.nextStatement();
			policies.add(stm);
		}
		
		return policies;
	}
	
	
	static final public void addMultipleProperty(
											Resource subject, 
											Property prop,
											Resource object) 
	{
		if(	subject ==null || 
			prop==null || 
			object==null)
		{
			getInstance().logger.warn("params subject prop objects must all not be null");
			return;
		}
		
		if(policySystemRDFModel==null)
		{
			getInstance().logger.warn("Model singleton not created");
			return;
		}
		
		Statement stm=
			policySystemRDFModel.rdfModel.createStatement(
														subject,
														prop,
														object);
		if(stm!=null)
		{
			policySystemRDFModel.rdfModel.add(stm);
		}
		return;
	}
	
	static final public boolean isSubClassOf(
										Resource res,
										Resource cls)
	{
		if(res==null || cls==null || policySystemRDFModel==null)
		{
			getInstance().logger.warn("A param is null:res="+res+" cls="+cls);
			return false;
		}
		return 
			policySystemRDFModel.rdfModel.contains(res,RDFS.subClassOf,cls);
	}
	
	static final public ModelObjectWrapper createModelObjectWrapper(Resource res)
	{
		if(isSubClassOf(res,CLASS_RESOURCE))
		{
			return new PSResourceImpl(res);				
		}
		else if(isSubClassOf(res,CLASS_POLICY))
		{
			return new PSPolicyImpl(res);
		}
		else if(isSubClassOf(res,CLASS_OVERRIDDING_RULE))
		{
			throw new RuntimeException("overrinding rull not suported");
		}
		else
		{
			
			RuntimeException ex=
				new RuntimeException("cannot handle this:"+res.getLocalName());
			getInstance().logger.error(ex);
			throw ex;
		}
	}
	
	static final public PSResource getResource(
									String identity,  
									boolean forceCreation) 
	{
		if(identity==null)
		{
			getInstance().logger.warn(
					"param resource identity must not be null");
			return null;
		}
		
		if(policySystemRDFModel==null)
		{
			getInstance().logger.warn("model instance not created");
			return null;
		}
		
		Selector psSel=
			new SimpleSelector(
				(Resource)null,
				PROP_HAS_IDENTITY,
				(String)identity);
		StmtIterator it=policySystemRDFModel.rdfModel.listStatements(psSel);
		
		Statement stm;
		Resource res=null;
		if(it.hasNext())
		{
			stm=it.nextStatement();
			res=stm.getSubject();
		}
		
		if(it.hasNext())
		{
			getInstance().logger.error(
					"Model contents several resources with this identity:"+
					identity);
		}
		
		if(res==null && forceCreation==true)
		{
			//res=policySystemRDFModel.rdfModel.cre
			res=
				policySystemRDFModel.rdfModel.createResource(NS_KB_DATA+identity);
			//res.addProperty(PROP_HAS_MAPPING,nodeName+"mapping");
			res.addProperty(PROP_HAS_NAME,identity);
			res.addProperty(PROP_HAS_IDENTITY,identity);
			res.addProperty(RDF.type,CLASS_RESOURCE);
		}
		return new PSResourceImpl(res);
	}
	
	
//	static final public Resource getResource(
//			String identity,  
//			boolean forceCreation) 
//{
//if(identity==null)
//{
//getInstance().logger.warn(
//"param resource identity must not be null");
//return null;
//}
//
//if(policySystemRDFModel==null)
//{
//getInstance().logger.warn("model instance not created");
//return null;
//}
//
//Selector psSel=
//new SimpleSelector(
//(Resource)null,
//PROP_HAS_IDENTITY,
//(String)identity);
//StmtIterator it=policySystemRDFModel.rdfModel.listStatements(psSel);
//
//Statement stm;
//Resource res=null;
//if(it.hasNext())
//{
//stm=it.nextStatement();
//res=stm.getSubject();
//}
//
//if(it.hasNext())
//{
//getInstance().logger.error(
//"Model contents several resources with this identity:"+
//identity);
//}
//
//if(res==null && forceCreation==true)
//{
////res=policySystemRDFModel.rdfModel.cre
//res=
//policySystemRDFModel.rdfModel.createResource(NS_KB_DATA+identity);
////res.addProperty(PROP_HAS_MAPPING,nodeName+"mapping");
//res.addProperty(PROP_HAS_NAME,identity);
//res.addProperty(PROP_HAS_IDENTITY,identity);
//res.addProperty(RDF.type,CLASS_RESOURCE);
//}
//return res;
//}

//////////////////////////////////////////////////////////////////////////////
////////////////////PROJECT CHANGED LISTENER/////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
	synchronized public void projectConfigChanged(ProjectConfig config) 
	{
		System.out.println("Model loaded="+rdfModelFile + 
					" schema="+rdfSchemaFile);
		try {
			createPolicySystemRDFModel();
		} catch (IOException e) {
			getInstance().logger.error("Error while creating the model",e);//e.printStackTrace();
		}
	};
	
}

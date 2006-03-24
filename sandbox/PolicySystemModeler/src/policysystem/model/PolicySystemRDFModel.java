package policysystem.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.model.RDFModelManipulator;

import policysystem.model.abtract.ModelObjectWrapper;
import policysystem.model.abtract.PSFilter;
import policysystem.model.abtract.PSModelChangeEvent;
import policysystem.model.abtract.PSModelChangeEventListener;
import policysystem.model.abtract.PSOverrindingRule;
import policysystem.model.abtract.PSPolicy;
import policysystem.model.abtract.PSPolicySystem;
import policysystem.model.abtract.PSResource;


import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelChangedListener;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class PolicySystemRDFModel
				implements 	ProjectConfigChangeListener,
							PSPolicySystem
{
	static final public Vector EMPTY_VECTOR=new Vector(0);
	static final public String POLICY_SYSTEM_RES="PolicySystem"; 
	static final public String POLICY_SYSTEM_RES_POLICIES="Policies";
	static final public String POLICY_SYSTEM_RES_RESOURCES="Resources";
	static final public String POLICY_SYSTEM_RES_ASSERTIONS="Assertions";
	static final public String POLICY_SYSTEM_RES_CONFLICT_RESOLUTION=
											"Inheritance Conflict Resolition";
	
	public static final String NS_KB_SCHEMA=
			"http://www.l3s.de/peertrust/modeler/schema/#";
	public static final String NS_KB_DATA=
		"http://www.l3s.de/peertrust/modeler/data/#";
	
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
	
	////
	public static final String LNAME_CLASS_OVERRIDDING_RULE="OverridingRule";
	public static Resource CLASS_OVERRIDDING_RULE=null;
	
	public static final String LNAME_PROP_HAS_OVERRIDDER="hasOverrider";//TOFO correct in model
	static Property PROP_HAS_OVERRIDDER=null;
	
	public static final String LNAME_PROP_HAS_OVERRIDDEN="hasOverridden";
	static Property PROP_HAS_OVERRIDDEN=null;
	
	public static final String LNAME_PROP_HAS_NAME="name";
	private static Property PROP_HAS_NAME=null;
	
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
	private Vector modelChangeListeners;
	private ModelEventAdapter modelEventAdapter;
	
	private PolicySystemRDFModel()
	{
		super();
		rdfModel=ModelFactory.createDefaultModel();
		schema=ModelFactory.createDefaultModel();
		ProjectConfig.getInstance().addProjectConfigChangeListener(this);
		logger=Logger.getLogger(PolicySystemRDFModel.class);
		modelChangeListeners= new Vector();
		modelEventAdapter= new ModelEventAdapter(this);
		
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
		createPropertyTypes();
		createResourceTypes();
		
	}
	synchronized public void createPropertyTypes()
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
		PROP_HAS_OVERRIDDEN=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_OVERRIDDEN);
		PROP_HAS_OVERRIDDER=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_OVERRIDDER);
		
		if(	PROP_HAS_CONDITION==null ||
				PROP_HAS_FILTER==null ||	
				PROP_HAS_IDENTITY==null ||
				PROP_HAS_MAPPING==null ||
				PROP_HAS_NAME==null ||
				PROP_HAS_OVERRIDING_RULES==null ||
				PROP_HAS_OVERRIDDEN==null ||
				PROP_HAS_OVERRIDDER==null ||
				PROP_HAS_SUPER==null ||
				PROP_HAS_VALUE==null )
		{
			RuntimeException ex = 
				new RuntimeException("model error all prop must be non null");
			logger.error(ex);
			throw ex;
		}
		return;
	}
	
	synchronized public void createResourceTypes()
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
		return;
	}
	
	
	
	public Model getRdfModel() {
		if(rdfModel==null)
		{
			logger.warn("RDf model is null");
			return null;
		}
		
		return rdfModel;
	}


	public Model getSchema() {
		if(policySystemRDFModel==null)
		{
			return null;
		}
		
		System.out.println("policySystemRDFModel.schema:"+policySystemRDFModel.schema);
		return schema;
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
	
	public void clearRDFModel()
	{
		rdfModel.removeAll();
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
	static final public HierarchyNodeCreationMechanism 
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
	
	static final public ModelObjectWrapper getResourceProperty(
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
		Resource res=(Resource)stm.getObject();
		return getInstance().createModelObjectWrapper(res,null);
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
		//getInstance().firePSModelChangeEvent(null);
		return;
	}
	
	static final public void setResourceProperty(
			Resource resource, 
			Property prop,
			Resource objValue) 
	{
		if(resource ==null || prop==null || objValue==null)
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
			stm=getInstance().rdfModel.createStatement(resource,prop,objValue);
			//getInstance().firePSModelChangeEvent(null);
			return;
		}
		
		stm.changeObject(objValue);
		//getInstance().firePSModelChangeEvent(null);
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
		//getInstance().firePSModelChangeEvent(null);
		return;
	}
	
	final public Vector getMultipleProperty(
								ModelObjectWrapper modelObjectWrapper, 
								Property prop) 
	{
		if(modelObjectWrapper ==null || prop==null)
		{
			logger.warn(
					"params must not be null: modelObjectWrapper="+modelObjectWrapper+
					" prop:"+prop);
			return new Vector();
		}
		
		Object wrappee=modelObjectWrapper.getModelObject();
		if(wrappee==null)
		{
			logger.warn(
					"Wrapped model object is null; wrapper="+modelObjectWrapper+
					" return empty vector");
			return EMPTY_VECTOR;
			
		}
		if(!(modelObjectWrapper.getModelObject() instanceof Resource))
		{
			logger.warn(
					"Model object not a resource["+wrappee.getClass()+
					" return empty Vector");
			return EMPTY_VECTOR;
		}
		
		Resource resource=(Resource)wrappee;
		
		
		if(policySystemRDFModel==null)
		{
			logger.warn("model instance not created");
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
		RDFNode object;
		while(it.hasNext())
		{
			stm=it.nextStatement();
			
			//policies.add(new PSPolicyImpl((Resource)stm.getObject()));
			object=stm.getObject();
			if(object.isResource()){
				wrappee=createModelObjectWrapper(
								(Resource)object,
								modelObjectWrapper);
				policies.add(wrappee);
			}
			else if(object.isLiteral())
			{
				policies.add(((Literal)object).getValue());
			}
			else
			{
				
				policies.add(object);
			}
		}
		
		return policies;
	}
	
	final public Vector getOverriddingRule(
						ModelObjectWrapper modelObjectWrapper) 
	{
	
		if(policySystemRDFModel==null)
		{
			logger.warn("model singeton not created yet");
			return null;//new PSPolicy[]{};
		}
		
		Selector ruleSel;
		if(modelObjectWrapper !=null)
		{//return resource overridding rules
			Resource resource=
				(Resource)modelObjectWrapper.getModelObject();
			ruleSel=
				new SimpleSelector(
					resource,
					PROP_HAS_OVERRIDING_RULES,//RDF.type,//PROP_IS_PROTECTED_BY,
					(Resource)null);
			StmtIterator it=rdfModel.listStatements(ruleSel);
			Vector resRules=new Vector();
			Statement stm;
			while(it.hasNext())
			{
				stm=it.nextStatement();
				resRules.add(
						createModelObjectWrapper(
								(Resource)stm.getObject(),
								modelObjectWrapper));
			}
			return resRules;
		}
		else
		{
			ruleSel=
				new SimpleSelector(
					(Resource)null,
					RDF.type,//PROP_IS_PROTECTED_BY,
					CLASS_OVERRIDDING_RULE);
			StmtIterator it=rdfModel.listStatements(ruleSel);
			Vector allRules=new Vector();
			Statement stm;
			while(it.hasNext())
			{
				stm=it.nextStatement();
				allRules.add(
						createModelObjectWrapper(
								(Resource)stm.getSubject(),
								modelObjectWrapper));
			}
			return allRules;
		}

	}
	
	final public Vector getPolicies() 
	{
	
		if(policySystemRDFModel==null)
		{
			logger.warn("model singeton not created yet");
			return new Vector();//new PSPolicy[]{};
		}
	
		Selector polSel=
			new SimpleSelector(
			null,
			RDF.type,//PROP_IS_PROTECTED_BY,
			CLASS_POLICY);
		//StmtIterator it=rdfModel.listStatements(polSel);
		ResIterator it=rdfModel.listSubjectsWithProperty(RDF.type,CLASS_POLICY);
		Vector policies=new Vector();
		Statement stm;
		while(it.hasNext())
		{
			//stm=it.nextStatement();
			policies.add(
					createModelObjectWrapper(it.nextResource(),null));
		}
	
		return policies;
	}
	
	final public Vector getMultipleStatement(
											Resource resource, 
											Property prop) 
	{
		if(resource ==null || prop==null)
		{
			logger.warn("params resource and prop must all be non null");
			return null;
		}
		
		if(policySystemRDFModel==null)
		{
			logger.warn("model singeton not created yet");
			return null;//new PSPolicy[]{};
		}
		
		Selector psSel=
			new SimpleSelector(
						resource,
						prop,//PROP_IS_PROTECTED_BY,
						(RDFNode)null);
		StmtIterator it=rdfModel.listStatements(psSel);
		Vector policies=new Vector();
		Statement stm;
		while(it.hasNext())
		{
			stm=it.nextStatement();
			policies.add(stm);
		}
		
		return policies;
	}
	
	
	final public void addMultipleProperty(
									Resource subject, 
									Property prop,
									Resource object) 
	{
		if(	subject ==null || 
			prop==null || 
			object==null)
		{
			logger.warn("params subject prop objects must all not be null");
			return;
		}
		
		if(policySystemRDFModel==null)
		{
			logger.warn("Model singleton not created");
			return;
		}
		
		Statement stm=
			rdfModel.createStatement(
									subject,
									prop,
									object);
		if(stm!=null)
		{
			rdfModel.add(stm);
			firePSModelChangeEvent(null);
		}
		return;
	}
	
	final public void removeStringProperty(
			Resource subject, 
			Property prop,
			String object) 
	{
		if(	subject ==null || 
			prop==null || 
			object==null)
		{
			logger.warn("params subject prop objects must all not be null");
			return;
		}
	
		if(policySystemRDFModel==null)
		{
			logger.warn("Model singleton not created");
			return;
		}
	
		try 
		{
//			Statement stm=
//				rdfModel.createStatement(
//									subject,
//									prop,
//									object);
			StmtIterator it=
				rdfModel.listStatements(subject,prop,object);
			Statement stm;
			while(it.hasNext())
			{
				stm=it.nextStatement();
				rdfModel.remove(stm);
				logger.info("removing:"+stm);
			}
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return;
	}
	
	final public void addMultipleStringProperty(
			Resource subject, 
			Property prop,
			String object) 
	{
		if(	subject ==null || 
			prop==null || 
			object==null)
		{
			logger.warn("params subject prop objects must all not be null");
			return;
		}
	
		if(policySystemRDFModel==null)
		{
			logger.warn("Model singleton not created");
			return;
		}
	
		Statement stm=
			rdfModel.createStatement(
								subject,
								prop,
								object);
		if(stm!=null)
		{
			rdfModel.add(stm);
			//firePSModelChangeEvent(null);
		}
		return;
	}
	
	
	final public boolean isSubClassOf(
										Resource res,
										Resource cls)
	{
		if(res==null || cls==null || policySystemRDFModel==null)
		{
			getInstance().logger.warn("A param is null:res="+res+" cls="+cls);
			return false;
		}
		return 
			policySystemRDFModel.rdfModel.contains(res,RDF.type,cls);
	}
	
	final public ModelObjectWrapper createModelObjectWrapper(
												Resource res,
												ModelObjectWrapper guarded)
	{
		if(isSubClassOf(res,CLASS_RESOURCE))
		{
			return new PSResourceImpl(res);				
		}
		else if(isSubClassOf(res,CLASS_POLICY))
		{
			return new PSPolicyImpl(res,guarded);
		}
		else if(isSubClassOf(res,CLASS_FILTER))
		{
			return new PSFilterImpl(res);
		}		
		else if(isSubClassOf(res,CLASS_OVERRIDDING_RULE))
		{
			return new PSOverriddingRuleImpl(res,(PSResource)guarded);
			//throw new RuntimeException("overrinding rull not suported");
		}
		else
		{
			
			
			RuntimeException ex=
				new RuntimeException(
						"\ncannot handle this:"+res.getLocalName()+
						"\n\t type="+res.getProperty(RDF.type)+
						"\n\t URI"+res.getURI()+
						"\n\t "+res.getLocalName());
			System.out.println("\nStatemenList:");
			StmtIterator it=
				getInstance().rdfModel.listStatements(
									(Resource)null,RDF.type,(RDFNode)null);
			for(;it.hasNext();)
			{
				System.out.println("\n\t"+it.nextStatement());
			}
			getInstance().logger.error(ex);
			throw ex;
		}
	}
	
	final public PSResource getResource(
									String identity,  
									boolean forceCreation,
									Selector selector) 
	{
		if(identity==null)
		{
			logger.warn(
					"param resource identity must not be null");
			return null;
		}
		
		if(policySystemRDFModel==null)
		{
			logger.warn("model instance not created");
			return null;
		}
		
		
		
		Selector psSel=selector;
		if(selector==null)
		{
			logger.warn("No selector provided using default selector");
			psSel=
				new SimpleSelector(
						(Resource)null,
						PROP_HAS_IDENTITY,
						(String)identity);
		}
		StmtIterator it=policySystemRDFModel.rdfModel.listStatements(psSel);
		
		Statement stm;
		Resource res=null;
		if(it.hasNext())
		{
			stm=it.nextStatement();
			res=stm.getSubject();
		}
//		else
//		{
//			logger.warn("No Model entry found for:"+identity);
//			return null;
//		}
		
		if(it.hasNext())
		{
			logger.warn(
					"Model contents several resources with this identity:"+
					identity);
		}
		
		if(res==null && forceCreation==true)
		{
			//res=policySystemRDFModel.rdfModel.cre
//			res=
//				policySystemRDFModel.rdfModel.createResource(NS_KB_DATA+identity);
			//res.addProperty(PROP_HAS_MAPPING,nodeName+"mapping");
			String id=identity;
			String root=
				ProjectConfig.getInstance().getProperty(ProjectConfig.ROOT_DIR);
			if(root==null)
			{
				logger.warn("root is null file resource creation skipped:"+
							id);
				return null;
			}
			if(!root.equals(id))
			{//if not root get rel path
				if(!id.startsWith(root))
				{
					logger.warn(
							"resource not root schild: \n\tid  ="+id+" " +
							"\n\troot="+root);
					return null;
				}
				int start=root.length();
				if(root.endsWith("/"))
				{
					start=start+1;
				}
				id=id.substring(start);
			}
			res=
				policySystemRDFModel.rdfModel.createResource(NS_KB_DATA+id);
			res.addProperty(RDFS.label,id);
			res.addProperty(PROP_HAS_IDENTITY,id);
			res.addProperty(RDF.type,CLASS_RESOURCE);
		}
		return new PSResourceImpl(res);
	}
	
/////////////////////////////////////////////////////////////////////////////
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
	}
////////////////////////////////////////////////////////////////////////////////
///////////////POLICY SYSTEM INTERFACE//////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	public PSFilter createFilter(
							String label, 
							String[] conditions, 
							PSPolicy[] policies) 
	{
		if(label==null || conditions==null || policies==null)
		{
			logger.warn("args must not be null");
			return null;
		}
		final int COND_LEN=conditions.length;
		final int POL_LEN=policies.length;
//		if(COND_LEN<=0 || POL_LEN<=0)
//		{
//			logger.warn("conditions odr policies must not be empty");
//			return null;
//		}
		
		if(rdfModel==null || NS_KB_DATA==null)
		{
			logger.warn("rdfModel and NS_KB_DATA must not be null");
			return null;
		}
		
		Resource filter=rdfModel.createResource(NS_KB_DATA+label);
		if(filter.getProperty(RDFS.label)!=null)
		{
			logger.warn("rule with this name already exists:"+label);
			return null;
		}
		
		filter.addProperty(RDFS.label,label);
		for(int i=0;i<COND_LEN;i++)
		{
			filter.addProperty(PROP_HAS_CONDITION,conditions[i]);
		}
		for(int i=0;i<POL_LEN;i++)
		{
			filter.addProperty(PROP_IS_PROTECTED_BY,conditions[i]);
		}
		filter.addProperty(RDF.type,CLASS_FILTER);
		//firePSModelChangeEvent(null);
		return new PSFilterImpl(filter);

	}

	public PSOverrindingRule createOverriddingRule(
							String label, 
							PSPolicy overridder, 
							PSPolicy overridden) 
	{
		if(label==null /*|| overridden==null || overridder==null*/)
		{
			logger.warn("args must not be null");
			return null;
		}
		if(rdfModel==null || NS_KB_DATA==null)
		{
			logger.warn("rdfModel and NS_KB_DATA must not be null");
			return null;
		}
		Resource orule=rdfModel.createResource(NS_KB_DATA+label);
		if(orule.getProperty(RDFS.label)!=null)
		{
			logger.warn("rule with this name already exists:"+label);
			return null;
		}
		
		orule.addProperty(RDFS.label,label);
		orule.addProperty(RDF.type,CLASS_OVERRIDDING_RULE);
		
		if(overridden!=null)
		{
			orule.addProperty(PROP_HAS_OVERRIDDEN,overridden.getModelObject());
		}
		if(overridder!=null)
		{
			orule.addProperty(PROP_HAS_OVERRIDDER,overridder.getModelObject());
		}
		//firePSModelChangeEvent(null);
		return new PSOverriddingRuleImpl(orule,null);
	}

	public PSPolicy createPolicy(String label, String value) 
	{
		
		if(label==null || value==null )
		{
			logger.warn("args must not be null");
			return null;
		}
		if(rdfModel==null || NS_KB_DATA==null)
		{
			logger.warn("rdfModel and NS_KB_DATA must not be null");
			return null;
		}
		Resource pol=rdfModel.createResource(NS_KB_DATA+label);
		
		if(pol.getProperty(PROP_HAS_VALUE)!=null)
		{
			logger.warn("policy with this name already exists:"+label);
			return null;
		}
		
		pol.addProperty(PROP_HAS_VALUE,value);
		pol.addProperty(RDFS.label,label);
		pol.addProperty(RDF.type,CLASS_POLICY);
		//firePSModelChangeEvent(null);
		PSPolicyImpl pp=new PSPolicyImpl(pol,null);
		logger.info("resource created:"+pp);
		return pp;
	}

	public PSResource createResource(String label, String identity) 
	{
		if(label==null || identity==null )
		{
			logger.warn("args must not be null");
			return null;
		}
		
		if(rdfModel==null || NS_KB_DATA==null)
		{
			logger.warn("rdfModel and NS_KB_DATA must not be null");
			return null;
		}
		
		Resource res=rdfModel.createResource(NS_KB_DATA+identity);
		if(res.getProperty(PROP_HAS_IDENTITY)!=null)
		{
			logger.warn("policy with this identity already exists:"+label);
			return null;
		}
		
		res.addProperty(RDFS.label,label);
		res.addProperty(PROP_HAS_IDENTITY,identity);
		res.addProperty(RDF.type,CLASS_RESOURCE);
		//firePSModelChangeEvent(null);
		return new PSResourceImpl(res);
		
	}

	public Vector getDirectChilds(ModelObjectWrapper parent) 
	{
		if(parent ==null)
		{
			return new Vector();
		}
		if(parent instanceof PSResource)
		{
			Resource res=(Resource)parent.getModelObject();
			if(res==null)
			{
				return new Vector();
			}
			SimpleSelector sel=
				new SimpleSelector(null,PROP_HAS_SUPER,res);
			StmtIterator it=rdfModel.listStatements(sel);
			Vector c=new Vector();
			ModelObjectWrapper mow;
			while(it.hasNext())
			{
				res=it.nextStatement().getSubject();
				mow=createModelObjectWrapper(res,null);
				c.add(mow);
			}
			return c;
		}
		else
		{
			logger.warn("No hierarchy for "+parent+ 
						" class="+parent.getClass());
			return new Vector();
		}
	}

	public Vector getDirectParents(ModelObjectWrapper child) 
	{
		if(child ==null)
		{
			return new Vector();
		}
		if(child instanceof PSResource)
		{
			Resource res=(Resource)child.getModelObject();
			if(res==null)
			{
				return new Vector();
			}
			SimpleSelector sel=
				new SimpleSelector(res,PROP_HAS_SUPER,(Resource)null);
			StmtIterator it=rdfModel.listStatements(sel);
			Vector p=new Vector();
			ModelObjectWrapper mow;
			while(it.hasNext())
			{
				res=(Resource)it.nextStatement().getObject();
				mow=createModelObjectWrapper(res,null);
				p.add(mow);
			}
			return p;
		}
		else
		{
			logger.warn("No hierarchy for "+child+ 
						" class="+child.getClass());
			return new Vector();
		}

	}

	public Vector getFilters(PSResource resource) 
	{
		Resource res;
		if(resource==null)
		{//getting all filters
			res=null;
		}else{
			res=(Resource)resource.getModelObject();
			if(res==null)
			{
				logger.warn("wrapped resource is null returning empty vector");
				return new Vector();
			}
		}
		
		Vector f=new Vector();
		if(res==null)
		{
			ResIterator rIt= 
				rdfModel.listSubjectsWithProperty(RDF.type,CLASS_FILTER);
			ModelObjectWrapper mow;
			while(rIt.hasNext())
			{
				res=rIt.nextResource();
				mow=createModelObjectWrapper(res,null);
				f.add(mow);
			}
		}
		else
		{
			NodeIterator nIt= 
				rdfModel.listObjectsOfProperty(res,PROP_HAS_FILTER);
			ModelObjectWrapper mow;
			while(nIt.hasNext())
			{
				res=(Resource)nIt.nextNode();
				mow=createModelObjectWrapper(res,resource);
				f.add(mow);
			}
		}
		return f;		
	}

	public Vector getPathToAncestorRoots(ModelObjectWrapper node)
	{
		Vector curParent;
		Vector paths=new Vector();
		Vector completedPath=new Vector();
		curParent=getDirectParents(node);
		for(int i=curParent.size()-1;i>=0;i--)
		{
			Vector path=new Vector();
			path.add(0,curParent.get(i));
			paths.add(path);
		}
		logger.info("PATHS.SIZE:"+paths.size());
		for(;paths.size()>0;)
		{
			Vector path=(Vector)paths.remove(0);
			ModelObjectWrapper mow=(ModelObjectWrapper)path.get(0);
			curParent=getDirectParents(mow);
			if(curParent.size()>0)
			{
				for(Iterator it=curParent.iterator();it.hasNext();)
				{
					Vector xPath=new Vector(path);
					mow=(PSResource)it.next();
					xPath.add(0,mow);
					paths.add(xPath);
				}
			}
			else
			{
				completedPath.add(path);
			}
		}
		return completedPath;
	}
	
	public final Vector computePathPolicies(Vector path)
	{
		logger.info("getting policies for path:"+path);
		if(path==null)
		{
			return new Vector();
		}
		
		final int MAX=path.size()-1;
		if(MAX<0)
		{
			return new Vector();
		}
		
		Vector policies= new Vector();
		Vector oRules;
		Vector lPolicies;
		///add root policies
		policies.addAll(
				getLocalPolicies(
						(PSResource)path.get(0)));
		logger.info("Policy at 0:"+policies);
		///follow path; add polcies and do overriding
		PSResource curRes;
		for(int i=1;i<=MAX;i++)
		{
			curRes=(PSResource)path.get(i);
			oRules=getOverriddingRule(curRes);
			for(Iterator it=oRules.iterator();it.hasNext();)
			{
				PSOverrindingRule rule=
					(PSOverrindingRule)it.next();
				rule.performOverridding(policies);
			}
			lPolicies=getLocalPolicies(curRes);
			logger.info("Policy at "+i+" for "+curRes+" "+lPolicies);
			policies.addAll(lPolicies);
			
		}
		
		return policies;
	}
	
	public Vector getInheritedPolicies(PSResource psResource) 
	{
		if(psResource==null)
		{
			logger.warn("param psResurce must not be null");
			return new Vector(); 
		}
		
		Vector policies=new Vector();
		Vector paths=getPathToAncestorRoots(psResource);
		logger.info("PATHS:"+paths);
		for(int i=paths.size()-1;i>=0;i--)
		{
			Vector path=(Vector)paths.get(i);
			
			policies.addAll(computePathPolicies(path));
		}
		
		return policies;
	}

	public Vector getLocalPolicies(PSResource psResource) 
	{
		if(psResource ==null)
		{
			logger.warn("param psResource must not be null");
			return new Vector();
		}
		
		Resource res=(Resource)psResource.getModelObject();
		if(res==null)
		{
			logger.warn("wrapper resource is null, returning empty vector");
			return new Vector();
		}
		logger.info("Getting Policies for Resource:"+res);
		NodeIterator it=
			rdfModel.listObjectsOfProperty(res,PROP_IS_PROTECTED_BY);
		Resource pol;
		Vector lPolicies= new Vector();
		for(;it.hasNext();)
		{
			res=(Resource)it.nextNode();
			lPolicies.add(createModelObjectWrapper(res,psResource));
		}
		return lPolicies;
	}

	public Vector getOverriddingRules(PSResource resource) 
	{
		return getOverriddingRule(resource);
	}

	public Vector getRoots(Class modelObjectWrapperClass) 
	{
		if(modelObjectWrapperClass==null)
		{
			logger.warn("wrapper class is null returning empty vector");
			return new Vector();
		}
		Class[] iis=modelObjectWrapperClass.getInterfaces();
		List iisList=Arrays.asList(iis);
		if(iisList.contains(PSResource.class))
		{
//			SimpleSelector sel=
//				new SimpleSelector(null,RDF.type,CLASS_RESOURCE);
			ResIterator it=
				rdfModel.listSubjectsWithProperty(RDF.type,CLASS_RESOURCE);
			Vector roots= new Vector();
			Resource res;
			for(;it.hasNext();)
			{
				res=it.nextResource();
				if(!res.hasProperty(PROP_HAS_SUPER))
				{
					roots.add( createModelObjectWrapper(res,null));
				}
			}
			return roots;
		}
		else
		{
			logger.warn("no hierarchy element class:"+modelObjectWrapperClass+
					"\n\treturning empty vector");
			return new Vector();
		}
	}
	public Vector getResources()
	{
		ResIterator it=
			rdfModel.listSubjectsWithProperty(RDF.type,CLASS_RESOURCE);
		Vector allRes= new Vector();
		Resource res;
		for(;it.hasNext();)
		{
			res=it.nextResource();
			allRes.add( createModelObjectWrapper(res,null));
		}
		return allRes;
	}

	public void addPSModelChangeEventListener(PSModelChangeEventListener l) 
	{
		if(l==null)
		{
			logger.warn("model changed listener to add is null; skipping");
			return;
		}
		modelChangeListeners.add(l);
	}

	public void removePSModelChangeEventListener(PSModelChangeEventListener l) 
	{
		if(l==null)
		{
			logger.warn("model changed listener to remove is null; skipping");
			return;
		}
		modelChangeListeners.remove(l);
	}
	public void firePSModelChangeEvent(PSModelChangeEvent event) 
	{
		PSModelChangeEventListener l;
		for(Iterator it=modelChangeListeners.iterator(); 
			it.hasNext();)
		{
			l=(PSModelChangeEventListener)it.next();
			l.onPSModelChange(event);
		}
	}
	
	
	class ModelEventAdapter implements ModelChangedListener
	{
		private PolicySystemRDFModel psModel;
		
		public ModelEventAdapter(	
						PolicySystemRDFModel psModel
									)
		{
			this.psModel=psModel;
			psModel.getRdfModel().register(this);
		}
		
		public void addedStatement(Statement arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void addedStatements(List arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void addedStatements(Model arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void addedStatements(Statement[] arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void addedStatements(StmtIterator arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void notifyEvent(Model arg0, Object arg1) 
		{
			firePSModelChangeEvent(null);
		}

		public void removedStatement(Statement arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void removedStatements(List arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void removedStatements(Model arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void removedStatements(Statement[] arg0) 
		{
			firePSModelChangeEvent(null);
		}

		public void removedStatements(StmtIterator arg0) 
		{
			firePSModelChangeEvent(null);
		}
		
	}
}






/////////////////////////////////////////////////////////////////////////////


//////////////////////////////////////////////////////////////////////////////



package org.peertrust.modeler.policysystem.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.model.abtract.PSApplyingPolicyResolver;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeVeto;
import org.peertrust.modeler.policysystem.model.abtract.PSModelElementIDGenerator;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEvent;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEventListener;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;
import org.peertrust.modeler.policysystem.model.abtract.PSResourceIdentityMaker;



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
	
	public static final String LNAME_PROP_HAS_OVERRINDING_RULES=
													"hasOverridingRule";
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
	
	/**
	 * A resource property that specifies whether a given resource
	 * may havechild (e.g. a directory) or not (e.g. a file) 
	 */
	static Property PROP_CAN_HAVE_CHILD=null;
	public static final String LNAME_PROP_CAN_HAVE_CHILD="canHaveChild";
	
	/**
	 * A resource property which specifies whether the resource is virtual
	 * or not
	 */
	static Property PROP_IS_VIRTUAL=null;
	public static final String LNAME_PROP_IS_VIRTUAL="isVirtual";
	
	/**
	 * A resource property which specifies the root
	 */
	static Property PROP_HAS_ROOT=null;
	public static final String LNAME_PROP_HAS_ROOT="hasRoot";
	
	
	private static final PolicySystemRDFModel 
			policySystemRDFModel= new PolicySystemRDFModel();
	
	private Model rdfModel;
	private String rdfModelFile;
	
	private Model schema = null;//ModelLoader.loadModel("file:data/rdfsDemoSchema.rdf");
	private String rdfSchemaFile;
	private static final  Logger  logger=
				Logger.getLogger(PolicySystemRDFModel.class);
	private Vector modelChangeListeners;
	private ModelEventAdapter modelEventAdapter;
	private boolean blockModelEvent;
	
	private Hashtable identityMakers;
	private PSModelElementIDGenerator idGenerator;
	private PSApplyingPolicyResolver apResolver;
	
	private final Map propMap;
	private PolicySystemRDFModel()
	{
		super();
		rdfModel=ModelFactory.createDefaultModel();
		schema=ModelFactory.createDefaultModel();
		ProjectConfig.getInstance().addProjectConfigChangeListener(this);
		//logger=Logger.getLogger(PolicySystemRDFModel.class);
		modelChangeListeners= new Vector();
		modelEventAdapter= new ModelEventAdapter(this);
		blockModelEvent=false;
		
		///identity makers 
		makeIdentityMakers();
		
		//id generator
		this.idGenerator= new PSModelElementIDGeneratorImpl();
		
		//applying policy resolver
		this.apResolver= new PSFilterBasedAPR(this);
		
		
		
		//property map
		propMap= new Hashtable();
		//makePropertyLookupTable(propMap);
	}
	
	
	private void makeIdentityMakers()
	{
		identityMakers= new Hashtable();
		identityMakers.put(File.class,new PSFileIdentityMaker());
		PSResourceIdentityMaker iMaker=
			new PSStringBasedRIM();
		identityMakers.put(String.class,iMaker);
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
		makePropertyLookupTable(propMap);
		
	}
	synchronized public void createPropertyTypes()
	{
		PROP_HAS_NAME=RDFS.label;//schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_NAME);
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
		PROP_CAN_HAVE_CHILD=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_CAN_HAVE_CHILD);
		PROP_IS_VIRTUAL=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_IS_VIRTUAL);
		PROP_HAS_ROOT=
			schema.getProperty(NS_KB_SCHEMA,LNAME_PROP_HAS_ROOT);
		
		if(		PROP_HAS_CONDITION==null ||
				PROP_HAS_FILTER==null ||	
				PROP_HAS_IDENTITY==null ||
				PROP_HAS_MAPPING==null ||
				PROP_HAS_NAME==null ||
				PROP_HAS_OVERRIDING_RULES==null ||
				PROP_HAS_OVERRIDDEN==null ||
				PROP_HAS_OVERRIDDER==null ||
				PROP_HAS_SUPER==null ||
				PROP_HAS_VALUE==null ||
				PROP_CAN_HAVE_CHILD==null ||
				PROP_HAS_ROOT==null||
				PROP_IS_PROTECTED_BY==null)
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
	
	private void makePropertyLookupTable(Map propMap)
	{
		if(propMap==null)
		{
			throw new IllegalArgumentException(
					"Argument propMap must not be null");
		}
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,
				PROP_IS_PROTECTED_BY);
		
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_HAS_CONDITION,
				PROP_HAS_CONDITION);
		
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER,
				PROP_HAS_FILTER);
		
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_HAS_NAME,
				PROP_HAS_NAME);
		
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDDEN,
				PROP_HAS_OVERRIDDEN);
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDDER,
				PROP_HAS_OVERRIDDER);
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE,
				PROP_HAS_OVERRIDING_RULES);
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_HAS_SUPER,
				PROP_HAS_SUPER);
		propMap.put(
				Vocabulary.PS_MODEL_PROP_NAME_HAS_ROOT,
				PROP_HAS_ROOT);
	}
	
	
	public Model getRdfModel() {
		if(rdfModel==null)
		{
			logger.warn("RDf model is null");
			return null;
		}
		
		return rdfModel;
	}


	public Model getSchema() 
	{
		if(policySystemRDFModel==null)
		{
			return null;
		}
		
		return schema;
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

				public PSModelObject createNode(
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
					PSResourceImpl psRes= 
						new PSResourceImpl(
								res,
								PolicySystemRDFModel.getInstance());
					return psRes;
				}

				public PSModelObject createLink(
									Model rdfModel, 
									Resource startResource, 
									Resource targetResource) {
					if(	rdfModel==null || 
						startResource==null || 
						targetResource==null ||
						PROP_HAS_SUPER==null)
					{
						logger.info("PSModelStatement:model="+rdfModel+ 
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
					System.out.println("PSModelStatement:"+stm);
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
			logger.warn(
					"\nparam resource or prop is null:"+
					"\n\tresource="+resource+
					"\n\tprop="+prop);
			return null;
		}
		
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			logger.info("no statement ["+resource+"; "+prop+";?]");
			return null;
		}
		return stm.getString();
	}
	
	
	static final public PSModelObject getResourceProperty(
									Resource resource, 
									Property prop) 
	{
		if(	resource ==null || 
				prop==null)
		{
			logger.warn(
					"param resource or prop is null: resource="+resource+
					"prop="+prop);
			return null;
		}
	
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			logger.info("no statement ["+resource+"; "+prop+";?]");
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
			logger.warn(
					"params must not be null: res="+resource+
					" prop:"+prop);
			return null;
		}
		
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			logger.info("no statement ["+resource+"; "+prop+";?]");
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
			logger.warn(
					"params must not be null: res="+resource+
					" prop:"+prop+ " objValue="+objValue);
			return;
		}
		
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			logger.info("no statement ["+resource+"; "+prop+";?]");
			return;
		}
		stm.changeObject(objValue);
		//getInstance().firePSModelChangeEvent(null);
		return;
	}
	
	public void setResourceProperty(
			Resource resource, 
			Property prop,
			Resource objValue) 
	{
		if(resource ==null || prop==null || objValue==null)
		{
			logger.warn(
					"params must not be null: res="+resource+
					" prop:"+prop+ " objValue="+objValue);
			return;
		}
		
		Statement stm=resource.getProperty(prop);
		if(stm==null)
		{
			logger.info("no statement ["+resource+"; "+prop+";?]");
			stm=rdfModel.createStatement(resource,prop,objValue);
			rdfModel.add(stm);
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
			logger.warn(
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
								PSModelObject modelObjectWrapper, 
								Property prop) 
	{
		logger.info(
				"\n\tGetting "+prop +" for "+
				modelObjectWrapper.getModelObject());
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
			return new Vector();
			
		}
		if(!(wrappee instanceof Resource))
		{
			logger.warn(
					"Model object not a resource["+wrappee.getClass()+
					" return empty Vector");
			return new Vector();
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
						PSModelObject modelObjectWrapper) 
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
	
//		Selector polSel=
//			new SimpleSelector(
//			null,
//			RDF.type,//PROP_IS_PROTECTED_BY,
//			CLASS_POLICY);
		//StmtIterator it=rdfModel.listStatements(polSel);
		ResIterator it=rdfModel.listSubjectsWithProperty(RDF.type,CLASS_POLICY);
		Vector policies=new Vector();
		//Statement stm;
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
//			PSModelStatement stm=
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
			logger.warn("A param is null:res="+res+" cls="+cls);
			return false;
		}
		return rdfModel.contains(res,RDF.type,cls);
	}
	
	final public PSModelObject createModelObjectWrapper(
												Resource res,
												PSModelObject guarded)
	{
		if(isSubClassOf(res,CLASS_RESOURCE))
		{
			return new PSResourceImpl(res,this);				
		}
		else if(isSubClassOf(res,CLASS_POLICY))
		{
			return new PSPolicyImpl(this,res,guarded);
		}
		else if(isSubClassOf(res,CLASS_FILTER))
		{
			return new PSFilterImpl(this,res);
		}		
		else if(isSubClassOf(res,CLASS_OVERRIDDING_RULE))
		{
			return new PSOverriddingRuleImpl(this,res,(PSResource)guarded);
			//throw new RuntimeException("overrinding rull not suported");
		}
		else
		{
			
			logger.warn(
						"\ncannot handle this:"+res.getLocalName()+
						"\n\t type="+res.getProperty(RDF.type)+
						"\n\t URI"+res.getURI()+
						"\n\t "+res.getLocalName());
			return null;//res;
		}
	}
	

	public PSResource getPSResource(
						Object realResource,//String identity,  
						boolean forceCreation) 
	{
		logger.info(
				"\ncall getPSResource:"+
				"\n\trealResource="+realResource+
				"\n\tforceCreation="+forceCreation);
		if(realResource==null)
		{
			logger.warn(
				"real resource is null");
			return null;
		}
		
		if(policySystemRDFModel==null)
		{
			logger.warn("model instance not created");
			return null;
		}
		
		
		PSResourceIdentityMaker iMaker=
			(PSResourceIdentityMaker)
					identityMakers.get(realResource.getClass());
		if(iMaker==null)
		{			
			return null;
		}
		
		URI identity=iMaker.relativeURI(realResource);
		Selector psSel=
			new SimpleSelector(
					(Resource)null,
					PROP_HAS_IDENTITY,
					identity);
		
		blockModelEvent=true;
		StmtIterator it=rdfModel.listStatements(psSel);
		
		Statement stm;
		Resource res=null;
		if(it.hasNext())
		{
			stm=it.nextStatement();
			res=stm.getSubject();
			if(it.hasNext())
			{
				logger.warn(
					"Model contents several resources with this identity:"+
					identity);
			}
		}
		else
		{
			if(res==null && forceCreation==true)
			{
				//TODO use uri
				/*String*/URI root=
					ProjectConfig.getInstance().getRootFor(identity);
					//ProjectConfig.getInstance().getProperty(ProjectConfig.ROOT_DIR);
				if(root==null)
				{
					logger.warn("root is null file resource creation skipped:"+
						identity);
					blockModelEvent=false;
					return null;
				}
					String label=iMaker.getLabel(realResource);
					res=
						rdfModel.createResource(
							idGenerator.generateID(PSResource.class,label));//	NS_KB_DATA+"PSResource"+System.currentTimeMillis());
					res.addProperty(RDFS.label,label);
					res.addProperty(
						PROP_HAS_IDENTITY,identity);
					res.addProperty(
							PROP_CAN_HAVE_CHILD,
							iMaker.canHaveChild(realResource));
					
					logger.info(
						"New PSResource:"+
						"\n\tidentity......:"+identity+
						" PROP_HAS_IDENTITY:"+PROP_HAS_IDENTITY);
					res.addProperty(RDF.type,CLASS_RESOURCE);
				}
		}
			blockModelEvent=false;
			if(res==null)
			{
				logger.warn(
						"\nCould not create or selectresource:"+
						"\n\tidentity="+identity+
						"\n\trealResource="+realResource);
				return null;
			}
			else
			{
				PSResource psRes=new PSResourceImpl(res,this);
				if(!iMaker.isRoot(realResource))
				{
					PSResource parentRes= 
						getPSResource(iMaker.getParent(realResource),false);
					psRes.setParent(parentRes);
				}
				return psRes;
			}
		}
	
	public PSResource createPSResource(Object realResource) 
	{
		if(realResource==null)
		{
			throw new IllegalArgumentException(
							"argument realResource must not be null");
		}
	
		if(policySystemRDFModel==null)
		{
			throw new RuntimeException("model instance not created");
		}
	
	
		PSResourceIdentityMaker iMaker=
		(PSResourceIdentityMaker)
				identityMakers.get(realResource.getClass());
		if(iMaker==null)
		{			
			throw new RuntimeException(
					"No PSResourceIdentityMaker for this class:"+
					"\n\rclass="+realResource.getClass());
		}
	
		URI identity=iMaker.relativeURI(realResource);
		Selector psSel=
		new SimpleSelector(
				(Resource)null,
				PROP_HAS_IDENTITY,
				identity);
		
		blockModelEvent=true;
		StmtIterator it=rdfModel.listStatements(psSel);
	
		Statement stm;
		Resource res=null;
		if(it.hasNext())
		{//already in the model
			stm=it.nextStatement();
			res=stm.getSubject();
			if(it.hasNext())
			{
				logger.warn(
					"Model contents several resources with this identity:"+
					identity);
			}
			return new PSResourceImpl(res,this);
		}
		else
		{//need to be created
			ProjectConfig projectConfig=ProjectConfig.getInstance();
			URI root=
				projectConfig.getRootFor(iMaker.getAbsoluteURI(realResource));
				//ProjectConfig.getInstance().getProperty(ProjectConfig.ROOT_DIR);
			if(root==null)
			{
				logger.warn("root is null file resource creation skipped:"+
					identity);
				blockModelEvent=false;
				return null;
			}
			String label=iMaker.getLabel(realResource);
			res=
					rdfModel.createResource(
						idGenerator.generateID(PSResource.class,label));//	NS_KB_DATA+"PSResource"+System.currentTimeMillis());
				res.addProperty(RDFS.label,label);
				res.addProperty(
					PROP_HAS_IDENTITY,identity);
				res.addProperty(
						PROP_CAN_HAVE_CHILD,
						iMaker.canHaveChild(realResource));
				logger.info(
					"New PSResource:"+
					"\n\tidentity......:"+identity+
					" PROP_HAS_IDENTITY:"+PROP_HAS_IDENTITY);
				res.addProperty(RDF.type,CLASS_RESOURCE);
			
			blockModelEvent=false;
			return new PSResourceImpl(res,this);
		}
	}
	
/////////////////////////////////////////////////////////////////////////////
////////////////////PROJECT CHANGED LISTENER/////////////////////////////////
/////////////////////////////////////////////////////////////////////////////
	synchronized public void projectConfigChanged(ProjectConfig config) 
	{
		logger.info("Model loaded="+rdfModelFile + 
					" schema="+rdfSchemaFile);
		try 
		{
			createPolicySystemRDFModel();
		} 
		catch (IOException e) 
		{
			logger.error("Error while creating the model",e);
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
		return new PSFilterImpl(this,filter);

	}

	public PSOverridingRule createOverriddingRule(
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
		return new PSOverriddingRuleImpl(this,orule,null);
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
		PSPolicyImpl pp=new PSPolicyImpl(this,pol,null);
		logger.info("resource created:"+pp);
		return pp;
	}

	public PSResource createPSResource(String label, String identity) 
	{
		System.out.println("CreatingREsource:"+identity);
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
		
		//Resource res=rdfModel.createResource(NS_KB_DATA+identity);
		//TODO ID sequencer for resource
		Resource res=
			rdfModel.createResource(
						NS_KB_DATA+"PSResource/"+identity);//System.currentTimeMillis());
		if(res.getProperty(PROP_HAS_IDENTITY)!=null)
		{
			logger.warn("policy with this identity already exists:"+label);
			return null;
		}
		
		//res.addProperty(RDFS.label,label);
		res.addProperty(RDFS.label,label);
		res.addProperty(PROP_HAS_IDENTITY,identity);
		res.addProperty(RDF.type,CLASS_RESOURCE);
		//firePSModelChangeEvent(null);
		return new PSResourceImpl(res,this);
		
	}

	public List getDirectChildren(PSModelObject parent) 
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
			PSModelObject mow;
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

	public PSModelObject getDirectParent(PSModelObject child) 
	{
		if(child ==null)
		{
			return null;
		}
		if(child instanceof PSResource)
		{
			Resource res=(Resource)child.getModelObject();
			if(res==null)
			{
				return null;//new Vector();
			}
			SimpleSelector sel=
				new SimpleSelector(res,PROP_HAS_SUPER,(Resource)null);
			StmtIterator it=rdfModel.listStatements(sel);
			//Vector p=new Vector();
			PSModelObject mow;
			if(it.hasNext())//while(it.hasNext())
			{
				res=(Resource)it.nextStatement().getObject();
				mow=createModelObjectWrapper(res,null);
				return mow;
				//p.add(mow);
			}
			//return p;
			return null;
		}
		else
		{
			logger.warn("No hierarchy for "+child+ 
						" class="+child.getClass());
			return null;//new Vector();
		}

	}

	public List getFilters(PSResource resource) 
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
			PSModelObject mow;
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
			PSModelObject mow;
			while(nIt.hasNext())
			{
				res=(Resource)nIt.nextNode();
				mow=createModelObjectWrapper(res,resource);
				f.add(mow);
			}
		}
		return f;		
	}

	public List getPathToAncestorRoots(PSModelObject node)
	{
		PSModelObject curParent;
		Vector completedPath=new Vector();
		curParent=node;//getDirectParent(node);
		
		while(curParent!=null)
		{
				completedPath.add(0,curParent);
				curParent=getDirectParent(curParent);			
		}
		return completedPath;
	}
	
	public List getInheritedPolicies(PSResource psResource) 
	{
		return apResolver.getApplyingPolicies(psResource);
	}


	public List getOverriddingRules(PSResource resource) 
	{
		return getOverriddingRule(resource);
	}

	public List getRoots(Class modelObjectWrapperClass) 
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
	public List getResources()
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
		if(!blockModelEvent)
		{
			PSModelChangeEventListener l;
			for(Iterator it=modelChangeListeners.iterator(); 
				it.hasNext();)
			{
				l=(PSModelChangeEventListener)it.next();
				l.onPSModelChange(event);
			}
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
			psModel.firePSModelChangeEvent(null);
		}

		public void addedStatements(List arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void addedStatements(Model arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void addedStatements(Statement[] arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void addedStatements(StmtIterator arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void notifyEvent(Model arg0, Object arg1) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void removedStatement(Statement arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void removedStatements(List arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void removedStatements(Model arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void removedStatements(Statement[] arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}

		public void removedStatements(StmtIterator arg0) 
		{
			psModel.firePSModelChangeEvent(null);
		}
		
	}
	/**
	 * Finds the statement of the following kind:
	 * [? prop object] where the property prop and the obj are given 
	 * representing one of the given properties.
	 * @param object -- the statement object
	 * @param props -- a vector holding the statement properties
	 * @param linkedObjectType -- the type of the linked object to find and return
	 * @return a vector of ps model object linked to the specify object throught the 
	 * 	the given vectors
	 */
	private Vector getLinkedObjForTheseProps(
										Resource obj, 
										Vector props,
										Class linkedObjectType)
	{
		Vector linkedObjects=new Vector();
		Resource res;
		PSModelObject modelObject;
		for(Iterator it=props.iterator();it.hasNext();)
		{	
			Property prop=(Property)it.next();
			Selector psSel=
				new SimpleSelector(
							null,
							prop,//PROP_IS_PROTECTED_BY,
							obj//(RDFNode)psModelObject.getModelObject()
							);
			StmtIterator stmIt=rdfModel.listStatements(psSel);
			for(;stmIt.hasNext();)
			{
				res=stmIt.nextStatement().getSubject();
				modelObject=createModelObjectWrapper(res,null);
				if(modelObject!=null)
				{
					if(linkedObjectType==null)
					{
						linkedObjects.add(modelObject);
					}
					else if(linkedObjectType.isInstance(modelObject))
					{
						linkedObjects.add(modelObject);
					}					
					else
					{
						logger.warn("slectorreturn unexpeted type:"+modelObject.getClass());
					}
				}
			}
		}
		return linkedObjects;
	}

	/**
	 * Utility method return all the model object linked o the given policy 
	 * of the given type
	 * @param psModelObject
	 * @param linkedObjectType
	 * @return
	 */
	private Vector getToPolicyLinkedModelObjects(
								PSPolicy psModelObject, 
								Class linkedObjectType)
	{
		logger.warn("not implemented");
		//getMultipleProperty(psModelObject,PROP)
		Vector props= new Vector();
		if(linkedObjectType==null)
		{
			//prop=null;
			props.add(PROP_IS_PROTECTED_BY);
			props.add(PROP_HAS_OVERRIDDEN);
			props.add(PROP_HAS_OVERRIDDER);
		}
		else if(linkedObjectType==PSResource.class)
		{
			props.add(PROP_IS_PROTECTED_BY);
		}
		else if(linkedObjectType==PSFilter.class)
		{
			props.add(PROP_IS_PROTECTED_BY);
		}
		else if(linkedObjectType==PSOverridingRule.class)
		{
			props.add(PROP_HAS_OVERRIDDEN);
			props.add(PROP_HAS_OVERRIDDER);
		}
		else
		{
			return props;//an empty vector
		}
		
		Object res=psModelObject.getModelObject();
		if(res instanceof Resource)
		{
			return getLinkedObjForTheseProps((Resource)res,props,linkedObjectType);
		}
		else
		{
			return new Vector();
		}
		
	}
	
	private Vector getToFilterLinkedModelObjects(
									PSFilter psModelObject, 
									Class linkedObjectType)
	{
	
		Vector props= new Vector();
		if(linkedObjectType==null)
		{
			//prop=null;
			props.add(PROP_HAS_FILTER);
		}
		else if(linkedObjectType==PSResource.class)
		{
			props.add(PROP_HAS_FILTER);
		}
		else
		{
			return props;//an empty vector
		}
		
		Object res=psModelObject.getModelObject();
		if(res instanceof Resource)
		{
			return getLinkedObjForTheseProps(
										(Resource)res,
										props,
										linkedObjectType);
		}
		else
		{
			return new Vector();
		}
	
	}
	
	private Vector getToORuleFilterLinkedModelObjects(
			PSOverridingRule psModelObject, 
			Class linkedObjectType)
	{

		Vector props= new Vector();
		if(linkedObjectType==null)
		{
			//prop=null;
			props.add(PROP_HAS_OVERRIDING_RULES);
		}
		else if(linkedObjectType==PSResource.class)
		{
			props.add(PROP_HAS_OVERRIDING_RULES);
		}
		else
		{
			return props;//an empty vector
		}
		
		Object res=psModelObject.getModelObject();
		if(res instanceof Resource)
		{
			return getLinkedObjForTheseProps(
						(Resource)res,
						props,
						linkedObjectType);
		}
		else
		{
			return new Vector();
		}
	
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem#getLinkedModelObjects(org.peertrust.modeler.policysystem.model.abtract.PSModelObject, java.lang.Class)
	 */
	public Vector getLinkedModelObjects(
								PSModelObject psModelObject, 
								Class linkedObjectjType) 
	{
		if(psModelObject==null)
		{
			logger.warn("argumennt psModelObject must not be null");
			return new Vector();
		}
		
		if(psModelObject instanceof PSPolicy)
		{
			return getToPolicyLinkedModelObjects(
										(PSPolicy)psModelObject,
										linkedObjectjType);
		}
		else if(psModelObject instanceof PSFilter)
		{
			return getToFilterLinkedModelObjects(
										(PSFilter)psModelObject,
										linkedObjectjType);
		}
		else if(psModelObject instanceof PSOverridingRule)
		{
			return getToORuleFilterLinkedModelObjects(
									(PSOverridingRule)psModelObject,
									linkedObjectjType);
		}		
		else
		{
			return new Vector();
		}		
	}

	private PSModelChangeVeto removePSPolicy(PSPolicy psPolicy)
	{
		Vector linkedRes=getLinkedModelObjects(psPolicy,PSResource.class);
		Vector linkedORules=getLinkedModelObjects(psPolicy,PSOverridingRule.class);
		Vector linkedFilter=getLinkedModelObjects(psPolicy,PSFilter.class);
		System.out.println("Veto:"+linkedFilter+ " "+linkedORules+ " "+linkedRes);
		if(		linkedRes.isEmpty() && 
				linkedORules.isEmpty() &&
				linkedFilter.isEmpty())
		{
			Resource res=(Resource)psPolicy.getModelObject();
			rdfModel.removeAll(res,null,null);
			return null;
		}
		else
		{
			PSModelChangeVeto veto=new PSModelChangeVetoImpl();
			PSModelObject modelObject;
			PSModelStatement stm;
			///for resources
			for(Iterator it = linkedRes.iterator();it.hasNext();)
			{
				modelObject=(PSModelObject)it.next();
				stm=
					new PSModelStatementImpl(
								modelObject,
								Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,
								psPolicy);
				veto.addPSModelStatement(stm);
			}
			
			//for filters
			for(Iterator it = linkedFilter.iterator();it.hasNext();)
			{
				modelObject=(PSModelObject)it.next();
				stm=
					new PSModelStatementImpl(
								modelObject,
								Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY,
								psPolicy);
				veto.addPSModelStatement(stm);
			}
			
			//for overriding rules
			PSOverridingRule oRule;
			for(Iterator it = linkedORules.iterator();it.hasNext();)
			{
				oRule=(PSOverridingRule)it.next();
				if(oRule.getHasOverridden().getLabel().equals(psPolicy.getLabel()) )
				{
				
					stm=
						new PSModelStatementImpl(
									oRule,
									Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDDEN,
									psPolicy);
				}
				else
				{
					stm=
						new PSModelStatementImpl(
									oRule,
									Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDDER,
									psPolicy);
				}
				veto.addPSModelStatement(stm);
			}
			return veto;
		}
	}
	
	private PSModelChangeVeto removePSFilter(PSFilter psFilter)
	{
		Vector linkedRes=getLinkedModelObjects(psFilter,PSResource.class);
		if(linkedRes.isEmpty())
		{
			Resource res=(Resource)psFilter.getModelObject();
			rdfModel.removeAll(res,null,null);
			return null;
		}
		else
		{
			PSModelChangeVeto veto=new PSModelChangeVetoImpl();
			PSModelObject modelObject;
			PSModelStatement stm;
			///for resources
			for(Iterator it = linkedRes.iterator();it.hasNext();)
			{
				modelObject=(PSModelObject)it.next();
				stm=
					new PSModelStatementImpl(
								modelObject,
								Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER,
								psFilter);
				veto.addPSModelStatement(stm);
			}
			
			return veto;
		}
		
	}
	
	private PSModelChangeVeto removePSOverridingRule(
									PSOverridingRule oRule)
	{
		Vector linkedRes=getLinkedModelObjects(oRule,PSResource.class);
		if(linkedRes.isEmpty())
		{
			Resource res=(Resource)oRule.getModelObject();
			rdfModel.removeAll(res,null,null);
			return null;
		}
		else
		{
			PSModelChangeVeto veto=new PSModelChangeVetoImpl();
			PSModelObject modelObject;
			PSModelStatement stm;
			///for resources
			for(Iterator it = linkedRes.iterator();it.hasNext();)
			{
				modelObject=(PSModelObject)it.next();
				stm=
					new PSModelStatementImpl(
								modelObject,
								Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE,
								oRule);
				veto.addPSModelStatement(stm);
			}
			
			return veto;
		}
	}
	
	private PSModelChangeVeto removePSresource(PSResource psResource)
	{
		return new PSModelChangeVetoImpl();
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSPolicySystem#removeModelObject(org.peertrust.modeler.policysystem.model.abtract.PSModelObject)
	 */
	public PSModelChangeVeto removeModelObject(PSModelObject psModelObject) 
	{
		if(psModelObject==null)
		{
			return null;
		}
		
		if(psModelObject instanceof PSPolicy)
		{
			return removePSPolicy((PSPolicy)psModelObject);
		}
		else if(psModelObject instanceof PSFilter)
		{
			return removePSFilter((PSFilter)psModelObject);
		} 
		else if(psModelObject instanceof PSOverridingRule)
		{
			return removePSOverridingRule(
					(PSOverridingRule)psModelObject);
		}
		else if(psModelObject instanceof PSResource)
		{
			return removePSresource((PSResource)psModelObject);
		}
		else
		{
			return null;
		}
	}

	private PSModelChangeVeto removeResourceLinkedStm(
									PSResource psResource, 
									String psProp, 
									Object psObject)
	{
//		Statement stm= null;
		Resource subject= (Resource)psResource.getModelObject();
		//Resource object = (Resource)psObject.getModelObject();
		
		Property prop=null;
		if(psProp==null)
		{
			logger.warn("Property type must not be null");
			return null;
		}
		else if(psProp.equals(Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY))
		{
			prop=PROP_IS_PROTECTED_BY;
		}
		else if(psProp.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE))
		{
			prop=PROP_HAS_OVERRIDING_RULES;
		}
		else if(psProp.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER))
		{
			prop=PROP_HAS_FILTER;
		}
		else
		{
			logger.warn("Cannot remove this property type from a resaource:"+psProp);
			return null;
		}		
		logger.info("STM "+
						"\n\tsubject="+subject+
						"\n\tprop="+prop+
						"\n\tobject="+psObject);
		if(psObject instanceof PSModelObject)
		{
			Resource object = 
				(Resource)((PSModelObject)psObject).getModelObject();
			rdfModel.removeAll(subject,prop,object);
			return null;
		}
		else if(psObject instanceof String)
		{
			removeStringProperty(subject,prop,(String)psObject);
			return null;
		}
		else
		{
			return null;
		}
	}
	
	private PSModelChangeVeto removeFilterLinkedStm(
											PSFilter psFilter, 
											String psProp, 
											Object psObject)
	{
		Statement stm= null;
		Resource subject= (Resource)psFilter.getModelObject();
		
	
		Property prop=null;
		if(psProp==null)
		{
			logger.warn("Property type must not be null");
			return null;
		}
		else if(psProp.equals(Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY))
		{
		prop=PROP_IS_PROTECTED_BY;
		}
		else if(psProp.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE))
		{
		prop=PROP_HAS_OVERRIDING_RULES;
		}
		else if(psProp.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER))
		{
		prop=PROP_HAS_FILTER;
		}
		else
		{
		logger.warn("Cannot remove this property type from a resaource:"+psProp);
		return null;
		}		
		logger.info("STM "+
		"\n\tsubject="+subject+
		"\n\tprop="+prop+
		"\n\tobject="+psObject);
		if(psObject instanceof PSModelObject)
		{
			Resource object = 
				(Resource)((PSModelObject)psObject).getModelObject();
			rdfModel.removeAll(subject,prop,object);
			return null;
		}
		else if(psObject instanceof String)
		{
			removeStringProperty(subject,prop,(String)psObject);
			return null;
		}
		else
		{
			return null;
		}
	}
	
	private PSModelChangeVeto addFilterLinkedStm(
			PSFilter psFilter, 
			String psProp, 
			Object psObject)
	{
		Resource subject= (Resource)psFilter.getModelObject();
		Property prop=lookupModelProperty(psProp);
		
		logger.info("STM TO ADD:"+
					"\n\tsubject="+subject+
					"\n\tprop="+prop+
					"\n\tobject="+psObject);
		if(prop==null)
		{
			return null;
		}
		else if(	(prop==PROP_IS_PROTECTED_BY) )
		{
			if(psObject instanceof PSPolicy)
			{
				Resource object = 
					(Resource)((PSModelObject)psObject).getModelObject();
				rdfModel.add(subject,prop,object);
				return null;
			}
//			if(psObject instanceof PSFilter)
//			{
//				Resource object = 
//					(Resource)((PSModelObject)psObject).getModelObject();
//				rdfModel.add(subject,prop,object);
//				return null;
//			}
			else
			{
				/*logger.warn*/throw new RuntimeException(
						"\n\tCannot protect with a "+(psObject==null?null:psObject.getClass()));
				//return null;
			}
		}
		else if((prop==PROP_HAS_CONDITION) || 
				(prop==PROP_HAS_NAME) )
		{
			if(psObject instanceof String)
			{
				rdfModel.add(subject,prop,(String)psObject);
				//removeStringProperty(subject,prop,(String)psObject);
				return null;
			}
			else
			{
				logger.warn(
						"condition or name are of type string not "+
						psObject.getClass());
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	private PSModelChangeVeto addResourceLinkedStm(
			PSResource psResource, 
			String psProp, 
			Object psObject)
	{
		Resource subject= (Resource)psResource.getModelObject();
		Property prop=lookupModelProperty(psProp);
		
		logger.info("STM TO ADD:"+
					"\n\tsubject="+subject+
					"\n\tprop="+prop+
					"\n\tobject="+psObject);
		if(prop==null)
		{
			return null;
		}
		else if(	(prop==PROP_IS_PROTECTED_BY) )
		{
			if(psObject instanceof PSPolicy)
			{
				Resource object = 
					(Resource)((PSModelObject)psObject).getModelObject();
				rdfModel.add(subject,prop,object);
				return null;
			}
			else
			{
				logger.warn("Cannot protect with a "+psObject.getClass());
				return null;
			}
		}
		else if(	(prop==PROP_HAS_OVERRIDING_RULES) )
		{
			if(psObject instanceof PSOverridingRule)
			{
				Resource object = 
					(Resource)((PSModelObject)psObject).getModelObject();
				rdfModel.add(subject,prop,object);
				return null;
			}
			else
			{
				logger.warn("Not an overriding rule but a "+psObject.getClass());
				return null;
			}
		}
		else if(	(prop==PROP_HAS_FILTER) )
		{
			if(psObject instanceof PSFilter)
			{
				Resource object = 
					(Resource)((PSModelObject)psObject).getModelObject();
				rdfModel.add(subject,prop,object);
				return null;
			}
			else
			{
				logger.warn("Not a Filter but a "+psObject.getClass());
				return null;
			}
		}
		else if(	(prop==PROP_HAS_SUPER) )
		{
			if(psObject instanceof PSResource)
			{
				if(psResource==psObject)
				{
					logger.warn("child and parent must not be equals");
					return null;
				}
				else
				{
					Resource object = 
						(Resource)((PSModelObject)psObject).getModelObject();
					if(subject.hasProperty(prop))
					{
						logger.error("Resource has allready aparent set:"+
								"respurce:"+subject+
								"parent..:"+object);
						return null;
					}
					else
					{
						
						rdfModel.add(subject,prop,object);
						return null;
					}
				}
			}
			else
			{
				logger.warn("Cannot protect with a "+psObject.getClass());
				return null;
			}
		}
		else if((prop==PROP_HAS_CONDITION) || 
				(prop==PROP_HAS_NAME) )
		{
			if(psObject instanceof String)
			{
				rdfModel.add(subject,prop,(String)psObject);
				//removeStringProperty(subject,prop,(String)psObject);
				return null;
			}
			else
			{
				logger.warn(
						"condition or name are of type string not "+
						psObject.getClass());
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	public PSModelChangeVeto removeStatement(PSModelStatement psStm) 
	{
		PSModelObject subject = psStm.getSubject();
		if(subject==null)
		{
			logger.warn("Statement subject is null:"+psStm);
			return null;
		}
		else if(subject instanceof PSResource)
		{
			return removeResourceLinkedStm(
								(PSResource)subject,
								psStm.getProperty(),
								psStm.getObject());
			
		}
		else if(subject instanceof PSFilter)
		{
			return removeFilterLinkedStm(
								(PSFilter)subject,
								psStm.getProperty(),
								psStm.getObject());
			
		}
		else if(subject instanceof PSOverridingRule)
		{
			logger.warn("not supported");			
		}
		else
		{
			logger.warn("Removing this statement");
		}
		
		return null;
	}

	public PSModelChangeVeto addStatement(PSModelStatement psStm) 
	{
		PSModelObject subject = psStm.getSubject();
		if(subject==null)
		{
			logger.warn("Statement subject is null:"+psStm);
			return null;
		}
		else if(subject instanceof PSResource)
		{
			return addResourceLinkedStm(
								(PSResource)subject,
								psStm.getProperty(),
								psStm.getObject());
			
		}
		else if(subject instanceof PSFilter)
		{
			return addFilterLinkedStm(
								(PSFilter)subject,
								psStm.getProperty(),
								psStm.getObject());
			
		}
		else if(subject instanceof PSOverridingRule)
		{
			logger.warn("not supported");	
		}
		else
		{
			logger.warn("Removing this statement");
		}
		
		return null;
	}

	public List<Object> getModelObjectProperties(
						PSModelObject modelObject, 
						String propertyKey) 
	{
		if(modelObject==null || propertyKey==null)
		{
			logger.warn(
				"args of getModelObjectProperties must not be null");
			return null;
		}
		Object res=modelObject.getModelObject();
		if(res==null)
		{
			logger.warn("modelobject wrappeed resource is null");
			return null;
		}
		else if(!(res instanceof Resource))
		{
			logger.warn("Wrapped object is nota resource but a "+res.getClass());
			return null;
		}
		
		Property prop=lookupModelProperty(propertyKey);
		if(prop==null)
		{
			logger.warn("Property is null for property:"+propertyKey);
			return null;
		}
		//logger.info("Getting "+prop+" for "+modelObject);
		logger.info(
				"\nGetting property"+
				"\n\tResource:"+res+
				"\n\tpropertyKey"+propertyKey+
				"\n\tproperty:"+prop);
		return getMultipleProperty(modelObject,prop);
	}
	
	/***
	 * Get the rdf property mapped by this property key
	 * @param propKey -- the property key
	 * @return 
	 */
	private Property lookupModelProperty(String propKey)
	{
		Property prop=null;
		if(propKey!=null)
		{
			prop=(Property)propMap.get(propKey);
		}
		logger.debug(
				"\nlookupModelProperty(String propKey)"+
				"\n\tpropKey="+propKey+
				"\n\treturne property="+prop);
		return prop;

//		if(propKey==null)
//		{
//			return null;
//		}
//		else if(propKey.equals(Vocabulary.PS_MODEL_PROP_NAME_IS_PROTECTED_BY))
//		{
//			return PROP_IS_PROTECTED_BY;
//		}
//		else if(propKey.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_CONDITION))
//		{
//			return PROP_HAS_CONDITION;
//		}
//		else if(propKey.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_FILTER))
//		{
//			return PROP_HAS_FILTER;
//		}
//		else if(propKey.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_NAME))
//		{
//			return PROP_HAS_NAME;
//		}
//		else if(propKey.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDDEN))
//		{
//			return PROP_HAS_OVERRIDDEN;
//		}
//		else if(propKey.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDDER))
//		{
//			return PROP_HAS_OVERRIDDER;
//		}
//		else if(propKey.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_OVERRIDING_RULE))
//		{
//			return PROP_HAS_OVERRIDING_RULES;
//		}
//		else if(propKey.equals(Vocabulary.PS_MODEL_PROP_NAME_HAS_SUPER))
//		{
//			return PROP_HAS_SUPER;
//		}
//		else
//		{
//			logger.warn("unknown property");
//			return null;
//		}
	}

	public PSModelChangeVeto alterFilterProperty(
			PSFilter filter, 
			String propertyKey, 
			Object object)
	{
		Property prop= lookupModelProperty(propertyKey);
		if(prop==null)
		{
			return null;
		}
		else if( (prop==PROP_IS_PROTECTED_BY))
		{
//			if(object instanceof PSPolicy)
//			{
//				setResourceProperty(
//						(Resource)filter.getModelObject(),
//						prop,
//						(Resource)((PSPolicy)object).getModelObject());
//				return null;
//			}
//			else
//			{
//				logger.warn("cannot protect filter with "+object.getClass());
//				return null;
//			}
			logger.warn("Altering multiple prop is not allow");
			return null;
		}
		else if((prop==PROP_HAS_NAME))
		{
			if(object instanceof String)
			{
				setStringProperty(
							(Resource)filter.getModelObject(),
							prop,
							(String)object);
				return null;
			}
			else
			{
				logger.warn("label is not a string but a "+object.getClass());
				return null;
			}
		}
		return null;
	}
	
	public PSModelChangeVeto alterPolicyProperty(
										PSPolicy policy, 
										String propertyKey, 
										Object object)
	{
		Property prop= 
			lookupModelProperty(propertyKey);
		if(prop==null)
		{
			return null;
		}
		else if( prop==PROP_HAS_FILTER)
		{
//			if(object instanceof PSFilter)
//			{
//				setResourceProperty(
//						(Resource)policy.getModelObject(),
//						prop,
//						(Resource)((PSPolicy)object).getModelObject());
//				return null;
//			}
//			else
//			{
//				logger.warn("object is not a filter but a "+object.getClass());
//				return null;
//			}
			logger.warn(
				"Altering policy filter is not allow since it is a multiple prop");;
			return null;
		}
		else if((prop==PROP_HAS_NAME)|| (prop==PROP_HAS_VALUE))
		{
			if(object instanceof String)
			{
				setStringProperty(
						(Resource)policy.getModelObject(),
						prop,
						(String)object);
				return null;
			}
			else
			{
				logger.warn("expecting string for property"+prop+
							" but got "+object.getClass());
				return null;
			}
		}
		return null;
	}
	
	public PSModelChangeVeto alterORuleProperty(
									PSOverridingRule oRule, 
									String propertyKey, 
									Object object)
	{
		Property prop= 
			lookupModelProperty(propertyKey);
		if(prop==null)
		{
		return null;
		}
		else if( prop==PROP_HAS_OVERRIDDEN || prop==PROP_HAS_OVERRIDDER)
		{
			if(object instanceof PSPolicy)
			{
				setResourceProperty(
						(Resource)oRule.getModelObject(),
						prop,
						(Resource)((PSPolicy)object).getModelObject());
				return null;
			}
			else
			{
				logger.warn("object is not a policy but a "+object.getClass());
				return null;
			}

		}
		else if((prop==PROP_HAS_NAME))
		{
			if(object instanceof String)
			{
				setStringProperty(
					(Resource)oRule.getModelObject(),
					prop,
					(String)object);
				return null;
			}
			else
			{
				logger.warn("expecting string for property"+prop+
				" but got "+object.getClass());
				return null;
			}
		}
		return null;
	}
	
	public PSModelChangeVeto alterPSResourceProperty(
											PSResource psRes, 
											String propertyKey, 
											Object object)
	{
		Property prop= 
		lookupModelProperty(propertyKey);
		if(prop==null)
		{
			return null;
		}
		else if( prop==PROP_IS_PROTECTED_BY)
		{
		if(object instanceof PSPolicy)
		{
			setResourceProperty(
				(Resource)psRes.getModelObject(),
				prop,
				(Resource)((PSPolicy)object).getModelObject());
			return null;
			}
			else
			{
				logger.warn("object is not a policy but a "+object.getClass());
				return null;
			}
		
		}
		else if(	(prop==PROP_HAS_NAME) || 
					(prop==PROP_HAS_IDENTITY) || 
					(prop==PROP_HAS_IDENTITY) ||
					(prop==PROP_HAS_NAME))
		{
			if(object instanceof String)
			{
				setStringProperty(
				(Resource)psRes.getModelObject(),
				prop,
				(String)object);
				return null;
			}
			else
			{
				logger.warn("expecting string for property"+prop+
				" but got "+object.getClass());
				return null;
			}
		}
		else
		{
			logger.error(
					"not supported property"+
					"\n\tprop="+prop+
					"\n\tcontext="+psRes);
			return null;
		}
		
	}
	
	public PSModelChangeVeto alterModelObjectProperty(
						PSModelObject modelObject, 
						String propertyKey, 
						Object object) 
	{
		if(modelObject==null || propertyKey==null || object==null)
		{
			logger.warn("params must not be null");
			return null;
		}
		
		if(modelObject instanceof PSPolicy)
		{
			return alterPolicyProperty(
						(PSPolicy)modelObject,propertyKey,object);
		}
		else if(modelObject instanceof PSFilter)
		{
			return alterFilterProperty(
						(PSFilter)modelObject,propertyKey,object);
		}
		else if(modelObject instanceof PSOverridingRule)
		{
			return alterORuleProperty(
						(PSOverridingRule)modelObject,propertyKey,object);
		}
		else if(modelObject instanceof PSResource)
		{
			return alterPSResourceProperty(
					(PSResource)modelObject,propertyKey,object);
		}
		else
		{
			return null;
		}
		
	}


	public PSResourceIdentityMaker getPSResourceIdentityMaker(Class type) 
	{
		if(type==null)
		{
			return null;
		}
		else
		{
			return (PSResourceIdentityMaker)identityMakers.get(type);
		}
		
	}
	
}







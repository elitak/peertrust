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

package org.peertrust.protege.plugin;
//import java.util.*;
import edu.stanford.smi.protege.model.*;
import java.util.*;
/**
 * PolicyFrameworkModel provides a framework which ease the integration of security attributes with your ontology.
 * @author Patrice Congo 
 * @date 30-Oct-2004
 * Last changed  $Date: 2005/04/01 10:04:05 $
 * by $Author: dolmedilla $
 * $Id: PolicyFrameworkModel.java,v 1.1 2005/04/01 10:04:05 dolmedilla Exp $
 * 
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PolicyFrameworkModel {
	/** container for a policy and its caracterising data.*/
	class PolicyData{
		/** the policy value*/
		String policy;
		/** the policy type; m for mandator or D for default.*/
		String policyType;
		/** the policy name */
		String policyName;
		/** the class in which the policy has been defined.*/
		String definingClassName;
		/** the instance representing the policy.*/
		Instance policyInst;
		
		/** the instance representing the overriden policy*/ 
		Instance policyOverridden;
		
		boolean isLocallyDefined;
		
		/**
		 * create a new PolicyData object the policyInstance the defining class and its locality flag.
		 * 
		 * @param policyInst
		 * @param definingCls
		 * @param isLocal
		 */
		public PolicyData(Instance policyInst,String definingCls, boolean isLocal){
			//System.out.println("POLSLOT:"+policyTypeSlot+ "ww"+policyInst);
        	///Type
			//System.out.println("policyTypeSlot:"+policyTypeSlot);
			Object obj = policyInst.getOwnSlotValue(createPolicyTypeSlot());//policyTypeSlot);
        	if(obj==null){
        		policyType="";
        	}else{
        		policyType=obj.toString();
        	}
        	
        	//policy
        	obj  = policyInst.getOwnSlotValue(createPolicySlot());//policySlot);
        	if(obj==null){
        		policy="";
        	}else{
        		policy=obj.toString();
        	}
        	//System.out.println("PolicySlotName="+policySlotName);
        	//obj  = policyInst.getOwnSlotValue(getPolicySlotName());
        	obj=policyInst.getName();
        	if(obj==null){
        		policyName="";
        	}else{
        		policyName=obj.toString();
        	}
        	
        	policyOverridden  = (Instance)policyInst.getOwnSlotValue(getPolicySlotOverridden());//getPolicyOverriden(policyInst);//policyInst.getOwnSlotValue(getPolicySlotName());
//        	if(obj==null){
//        		policyName="";
//        	}else{
//        		policyName=obj.toString();
//        	}
        	
        	this.policyInst=policyInst;
        	this.definingClassName=definingCls;
        	this.isLocallyDefined=isLocal;
		}
		
		
		public PolicyData(	String policy, 
							String policyType,
							String policyName,
							String definingClassName,
							boolean isLocallyDefined){
			this.policy=policy;
			this.policyType=policyType;
			this.policyName=policyName;
			this.definingClassName=definingClassName;
			this.isLocallyDefined=isLocallyDefined;
			return;
		}
		
		/** 
		 * build a dummy policy data object.
		 */
		public PolicyData(){
			this(null,null,null,null,false);
		}
		
		
		public String toString(){
			if(policy==null && policyType==null && definingClassName==null){
				return "null";
			}else{
				return policy +" "+ policyType+"@" + definingClassName;
			}
		}
		
		
		public boolean equals(Object toComp) {
			if(toComp==null){
				return false;
			}else if(this==toComp){
				return true;
			}else if(toComp instanceof PolicyData){
				return this.compareWithPolicyData((PolicyData)toComp); 
			}else if(toComp instanceof Instance){
				//System.out.println("this.inst:"+this.policyInst +" toCompInst:"+toComp);
				return this.policyInst.equals((Instance)toComp);
			}else{
				//System.out.println("++++++TYPE:"+toComp.getClass().getClass());
				return false;
			}
			
		}
		
		private boolean compareWithPolicyData(PolicyData polData){
			return 	(polData.isLocallyDefined==this.isLocallyDefined)&&
					polData.policyInst.equals(this.policyInst) &&
					polData.policyOverridden.equals(this.policyOverridden);
		}
	}
	///default values
	static public final String DEFAULT_POLICY_META_CLS_NAME="PolicyTaggedCls";
	static public final String DEFAULT_POLICY_SLOT_VALUE="PolicySlotValue";
	//static public final String DEFAULT_POLICY_SLOT_NAME="PolicySlotName";
	static public final String DEFAULT_POLICY_SLOT_OVERRIDDEN="PolicySlotOverridden";
	static public final String DEFAULT_POLICY_SLOT_OVERRIDDING="PolicySlotOverridding";
	
	static public final String DEFAULT_POLICY_TYPE_SLOT_NAME="PolicyType";
	static public final String DEFAULT_POLICY_TAGGED_META_SLOT_NAME="PolicyTaggedSlot";
	
	static public final String DEFAULT_POLICY_CLS_NAME="PolicyCls";
	static public final String DEFAULT_POLICY_CLS_POLICY_SLOT_NAME="Attached Policy";
	
	
	static public final String TYPE_MANDATORY="M";
	static public final String TYPE_DEFAULT="D";
	
	///
	
	private String policyMetaClsName;
	private Cls policyMetaCls;
	
	private String policySlotValue;
	private Slot policySlot;
	
	private String policyTypeSlotName;
	private Slot policyTypeSlot;
	
	private String policyTaggedSlotName;
	private Cls policyTaggedMetaSlot;
	
	private KnowledgeBase kb;
	
	private Cls policyCls;
	private Slot clsPolicySlot;
	
	private Slot policySlotName;
	private Slot policySlotOverridden;
	private Slot policySlotOverridding;
	
	public PolicyFrameworkModel(KnowledgeBase kb){
		this(	kb,
				DEFAULT_POLICY_META_CLS_NAME,
				DEFAULT_POLICY_SLOT_VALUE,
				DEFAULT_POLICY_TYPE_SLOT_NAME,
				DEFAULT_POLICY_TAGGED_META_SLOT_NAME);		
	}
	
	
	public PolicyFrameworkModel(
						KnowledgeBase kb,
						String policyMetaClsName,
						String policySlotValue,
						String policyTypeSlotName,
						String policyTaggedSlotName){
		this.kb=kb;
		this.policyMetaClsName=policyMetaClsName;
		this.policySlotValue=policySlotValue;
		this.policyTypeSlotName=policyTypeSlotName;
		this.policyTaggedSlotName= policyTaggedSlotName;
		buildModel();
		return;
	}
	
	private void buildModel(){
		addPolicyCls();
		addPolicyTaggedMetaSlot();
		addPolicyMetaCls();				
		return;
	}
	
	
	public KnowledgeBase getKnowledgeBase(){
		return kb;
	}
	

	
	public boolean isPolicyTaggedSlot(Slot slot){
		//String slotName=slot.getDirectType().getName();
		//System.out.println("slotName:"+slotName);
		Collection c= slot.getDirectTypes();
		Cls dTypeCls;
		if(c.contains(policyTaggedMetaSlot)){
			System.out.println("FOUND POLICY TAGGE META CLASS IN C");
		}
		
		for(Iterator it=c.iterator();it.hasNext();){
			dTypeCls=(Cls)it.next();
			if(dTypeCls.getName().equals(PolicyFrameworkModel.DEFAULT_POLICY_TAGGED_META_SLOT_NAME)){//policyMetaClsName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Create a slot to hold policies for policy tagged Clses and for Slot
	 */
	protected Slot createPolicySlot(){
		Slot lPolicySlot=kb.getSlot(DEFAULT_POLICY_SLOT_VALUE);
		if(lPolicySlot==null){
			Cls stdMetaCls= kb.getCls(Model.Cls.STANDARD_SLOT);
			lPolicySlot= kb.createSlot(DEFAULT_POLICY_SLOT_VALUE,stdMetaCls);
			lPolicySlot.setAllowsMultipleValues(true);
			this.policySlot=lPolicySlot;
		}
		this.policySlot=lPolicySlot;		
		return policySlot;
	}
	

	
	/**
	 * Create a slot to hold a reference to the policy which is being overridden.
	 */
	protected Slot createPolicySlotOverridden(){
		Slot lPolicySlotOverridden=kb.getSlot(DEFAULT_POLICY_SLOT_OVERRIDDEN);
		if(lPolicySlotOverridden==null){
			Cls stdMetaCls= kb.getCls(Model.Cls.STANDARD_SLOT);
			lPolicySlotOverridden= kb.createSlot(DEFAULT_POLICY_SLOT_OVERRIDDEN,stdMetaCls);
			lPolicySlotOverridden.setAllowsMultipleValues(false);
			lPolicySlotOverridden.setValueType(ValueType.INSTANCE);
			try{
				//can cause exception if policy cls has not been created
				lPolicySlotOverridden.setAllowedClses(Arrays.asList( new Object[] {kb.getCls(DEFAULT_POLICY_CLS_NAME)}));
			}catch(Throwable th){
				th.printStackTrace();
			}
		}
		this.policySlotOverridden=lPolicySlotOverridden;
		
		return policySlotOverridden;
	}
	
	/**
	 * Create a slot to hold a reference to the policy which is overridding this policy.
	 */
	protected Slot createPolicySlotOverridding(){
		Slot lPolicySlotOverridding=kb.getSlot(DEFAULT_POLICY_SLOT_OVERRIDDING);
		if(lPolicySlotOverridding==null){
			Cls stdMetaCls= kb.getCls(Model.Cls.STANDARD_SLOT);
			lPolicySlotOverridding= kb.createSlot(DEFAULT_POLICY_SLOT_OVERRIDDING,stdMetaCls);
			lPolicySlotOverridding.setAllowsMultipleValues(true);
			lPolicySlotOverridding.setValueType(ValueType.INSTANCE);
			try{
				//can cause exception if policy cls has not been created
				lPolicySlotOverridding.setAllowedClses(Arrays.asList( new Object[] {kb.getCls(DEFAULT_POLICY_CLS_NAME)}));
			}catch(Throwable th){
				th.printStackTrace();
			}
		}
		this.policySlotOverridding=lPolicySlotOverridding;
		
		return policySlotOverridding;
	}
	
	/**
	 * Create a slot to hold the policy type (M for manadatory and D for default).
	 */
	protected Slot createPolicyTypeSlot(){
		Slot policyTypeSlot=kb.getSlot(DEFAULT_POLICY_TYPE_SLOT_NAME);
		if(policyTypeSlot==null){
			Cls stdMetaCls= kb.getCls(Model.Cls.STANDARD_SLOT);
			policyTypeSlot=kb.createSlot(DEFAULT_POLICY_TYPE_SLOT_NAME,stdMetaCls);
			///Vector values= new Vector(10);
			String values[]={"M","D"};
			policyTypeSlot.setAllowedValues(Arrays.asList(values));
			
			policyTypeSlot.setDefaultValues(Arrays.asList( new String[] {"M"}));
			policyTypeSlot.setMinimumCardinality(1);
			policyTypeSlot.setMaximumCardinality(1);
		}
		this.policyTypeSlot=policyTypeSlot;
		return policyTypeSlot;
	}
	
	/**
	 * add a policy meta cls to the model which is used to create clses which have policies.
	 */
	public void addPolicyMetaCls(){
		if(kb.getCls(DEFAULT_POLICY_META_CLS_NAME)!=null){
			return;
		}
		Cls stdCls=kb.getCls(Model.Cls.STANDARD_CLASS);
		Vector parents= new Vector(5);
		parents.add(stdCls);
		Cls policyMetaCls=kb.createCls(DEFAULT_POLICY_META_CLS_NAME,parents);
		
		//policyMetaCls.addDirectTemplateSlot(createPolicySlot());
		//policyMetaCls.addDirectTemplateSlot(createPolicyTypeSlot());
		policyMetaCls.addDirectTemplateSlot(createClsPolicySlot());
		//policyMetaCls.addDirectTemplateSlot(createPolicySlotName());
		//policyMetaCls.setTemplateSlotMaximumCardinality(clsPolicySlot,255);
		policyMetaCls.setTemplateSlotMinimumCardinality(clsPolicySlot,0);
		this.policyMetaCls=policyMetaCls;		
		return;
	}
	/**
	 * add a policy cls to the model. it is conatins a poliy and a policy type
	 */
	public void addPolicyCls(){
		if(kb.getCls(DEFAULT_POLICY_CLS_NAME)!=null){
			return;
		}
		Cls stdCls=kb.getCls(Model.Cls.SYSTEM_CLASS);
		Vector parents= new Vector(5);
		parents.add(stdCls);
		Cls policyCls=kb.createCls(DEFAULT_POLICY_CLS_NAME,parents);
		
		policyCls.addDirectTemplateSlot(createPolicySlot());
		policyCls.addDirectTemplateSlot(createPolicyTypeSlot());
//		policyCls.addDirectTemplateSlot(createPolicySlotName());
		policyCls.addDirectTemplateSlot(createPolicySlotOverridden());
		policyCls.addDirectTemplateSlot(createPolicySlotOverridding());
		//System.out.println("//////////////////////////////////////OVERRIDDING:"+policySlotOverridding);
		policyCls.setTemplateSlotAllowsMultipleValues(policyTypeSlot,false);
		policyCls.setTemplateSlotAllowsMultipleValues(policySlot,false);
//		policyCls.setTemplateSlotAllowsMultipleValues(policySlotName,false);
		policyCls.setTemplateSlotAllowsMultipleValues(policySlotOverridden, false);
		policyCls.setTemplateSlotAllowsMultipleValues(policySlotOverridding,true);
		
		this.policyCls=policyCls;		
		return;
	}
	
	/**
	 * create the policy slot :ClsPolicy for Class with policies.
	 * @return
	 */
	protected Slot createClsPolicySlot(){
		Slot clsPolicySlot=kb.getSlot(DEFAULT_POLICY_CLS_POLICY_SLOT_NAME);
		//System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW:"+clsPolicySlot+"  "+ this.clsPolicySlot);
		if(clsPolicySlot==null){
			Cls stdMetaCls= kb.getCls(Model.Cls.STANDARD_SLOT);
			clsPolicySlot=kb.createSlot(DEFAULT_POLICY_CLS_POLICY_SLOT_NAME,stdMetaCls);
			clsPolicySlot.setValueType( ValueType.INSTANCE);
			clsPolicySlot.setAllowedClses( Arrays.asList(new Cls[]{policyCls}));
			clsPolicySlot.setAllowsMultipleValues(true);
		}
		this.clsPolicySlot=clsPolicySlot;
		//System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW:"+clsPolicySlot+"  "+ this.clsPolicySlot);
		return clsPolicySlot;
	}
	/**
	 * get the policy Class :Policy[:policyType, policySlotName]
	 * @return
	 */
	public Cls getPolicyCls(){
		if(policyCls==null){
			policyCls= kb.getCls(DEFAULT_POLICY_CLS_NAME);
		}
		return policyCls;
	}
	
	/**
	 * add the meta slot  :PolicyTaggedMetaSlot to the model. This slot is used to defined
	 * slots that have policies.
	 */
	public void addPolicyTaggedMetaSlot(){
		if(kb.getCls(DEFAULT_POLICY_TAGGED_META_SLOT_NAME)!=null){
			return;
		}
		Cls stdCls=kb.getCls(Model.Cls.STANDARD_SLOT);
		Vector parents= new Vector(5);
		parents.add(stdCls);
		Cls policyCls=kb.createCls(DEFAULT_POLICY_TAGGED_META_SLOT_NAME,parents);
		//policyCls.addDirectTemplateSlot(createPolicySlot());
		//policyCls.addDirectTemplateSlot(createPolicyTypeSlot());
		policyCls.addDirectTemplateSlot(createClsPolicySlot());
		this.policyTaggedMetaSlot=policyCls;
		return;
	}
	

	
	/**
	 *To add a slot with policy to a class. 
	 * @param cls
	 * @return the added slot.
	 */
	public Slot addPolicyTaggedSlot(Cls cls){
		
		if(cls==null){
			return null;
		}		
		//////
		Cls policyMetaSlot=kb.getCls(DEFAULT_POLICY_TAGGED_META_SLOT_NAME);
		if(policyMetaSlot==null){
			return null;
		}
		
		Slot policySlot= kb.createSlot("PSlot"+System.currentTimeMillis(),policyMetaSlot);
		cls.addDirectTemplateSlot(policySlot);
		return	policySlot;		
	}
	
	
	public Cls getPolicyMetaCls() {
		//return policyMetaCls;
		if(policyMetaCls==null){
			policyMetaCls = kb.getCls(DEFAULT_POLICY_META_CLS_NAME);
		}
		return policyMetaCls;
	}
	
	public Slot getPolicySlot() {
		if(policySlot==null){
			policySlot= kb.getSlot(DEFAULT_POLICY_SLOT_VALUE);
		}
		return policySlot;
	}
	
	
	public Slot getPolicySlotOverridden(){
		if(policySlotOverridden==null){
			policySlotOverridden= kb.getSlot(DEFAULT_POLICY_SLOT_OVERRIDDEN);
		}
		return policySlotOverridden;
	}
	
	public String getPolicyType(Cls cls){
		String clsName= cls.getName();
		if(clsName==null){
			return "NO POLICY TYPE";
		}
		Slot policyTypeSlot=kb.getSlot(DEFAULT_POLICY_TYPE_SLOT_NAME);
		//System.out.println("clssss:"+cls.getName());
		Collection metaSlots = cls.getDirectType().getDirectTemplateSlots();
		//System.out.println("PolicyTypeSlot:"+policyTypeSlot);
		if (metaSlots.contains(policyTypeSlot)) {
		        Instance clsInst = kb.getInstance(cls.getName());		        
		        if (clsInst != null) {
		        	Object val=clsInst.getOwnSlotValue(policyTypeSlot);
		        	if(val!=null){
		                return val.toString();
		        	}else{
		        		return "BAD POLICY TYPE";
		        	}
		        }
		}	
		return "NO POLICY TYPE";		
	}
	
	

	
	public void setPolicyType(Cls cls, String type){
		Slot policyTypeSlot=kb.getSlot(DEFAULT_POLICY_TYPE_SLOT_NAME);
		Collection metaSlots = cls.getDirectType().getDirectTemplateSlots();
		
		if (metaSlots.contains(policyTypeSlot)) {
		        Instance clsInst = kb.getInstance(cls.getName());		        
		        if (clsInst != null) {
		        	clsInst.setOwnSlotValue(policyTypeSlot,type);		        	
		        }
		}	
	}
	
	
	public void setPolicyName(Instance inst, String name) throws IllegalArgumentException{
		
		inst.setName(name);
		
		
	}
	
	public void setPolicyOverridden(Instance inst, Instance overridden){
		if(inst==null){
			return;
		}else{
			Slot policyTypeSlot=kb.getSlot(DEFAULT_POLICY_SLOT_OVERRIDDEN);
			
			Collection metaSlots = inst.getDirectType().getDirectTemplateSlots();
			
			
			//remove old overridden relation.
			Instance oldOverridden= (Instance)inst.getOwnSlotValue(policyTypeSlot);
			if(oldOverridden!=null){
				oldOverridden.removeOwnSlotValue(kb.getSlot(DEFAULT_POLICY_SLOT_OVERRIDDING),inst);
			}
	
			//set up new overridden relation
			if (metaSlots.contains(policyTypeSlot)) {
				//SET Overring
				inst.setOwnSlotValue(policyTypeSlot,overridden);	
				//ADD overriddings
				if(overridden!=null){
					try{
						overridden.addOwnSlotValue(kb.getSlot(DEFAULT_POLICY_SLOT_OVERRIDDING),inst);
					}catch(Throwable th){
						th.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public String getPolicyType(Slot slot){
		if(slot==null){
			return "NO POLICY TYPE";
		}
		Slot policyTypeSlot=kb.getSlot(DEFAULT_POLICY_TYPE_SLOT_NAME);
		//System.out.println("clssss:"+cls.getName());
		Collection metaSlots = slot.getDirectType().getDirectTemplateSlots();
		//System.out.println("PolicyTypeSlot:"+policyTypeSlot);
		if (metaSlots.contains(policyTypeSlot)) {
		        Instance clsInst = kb.getInstance(slot.getName());		        
		        if (clsInst != null) {
		        	Object val=clsInst.getOwnSlotValue(policyTypeSlot);
		        	if(val!=null){
		                return val.toString();
		        	}else{
		        		return "BAD POLICY TYPE";
		        	}
		        }
		}	
		return "NO POLICY TYPE";
	}
	
	/**
	 * To get the policies that are definied in a specific class. 
	 * The flague iSLocallyDefinedValue indicates whether the policies are local to a specific class or inheritated.
	 * Since that class ist not neccesarely equals to the cls provides as argument,
	 * its value have to be rovided externaly through a parameter. 
	 * @param cls
	 * @param specialSlotName
	 * @param isLocalyDefinedValue
	 * @return
	 */
	public Collection getLocalPolicy(Cls cls, String specialSlotName,boolean isLocalyDefinedValue){
		if(cls.isDeleted() ){
			return new Vector(2);
		}
		if(cls.isRoot()){
			return new Vector(2);
		}
		
		//Slot specialSlot = kb.getSlot(specialSlotName);
		Slot specialSlot = kb.getSlot(DEFAULT_POLICY_CLS_POLICY_SLOT_NAME);
		
		String definingClassName= cls.getName();
		
		String policyType = getPolicyType(cls);
		//		 Check that the meta-class of cls is the correct one and that it has
		//		 our special slot
		//System.out.println("cls.getDirectType:"+cls.getDirectType());
		Cls directType= cls.getDirectType();
		if(directType==null){
			return new Vector(2);
		}
		Collection metaSlots = directType.getDirectTemplateSlots();
		Vector polVector= new Vector(20);
		
		if (metaSlots.contains(specialSlot)) {
		        Instance clsInst = kb.getInstance(cls.getName());
		        if (clsInst != null) {
		                // getSlots copied from InstanceStorer.java
		        		//InstanceStorer 
		                Iterator i = clsInst.getOwnSlotValues(specialSlot).iterator();//cls.getTemplateSlots().iterator();//getSlots(clsInst).iterator();
		                //System.out.println("HAS NEXT:"+i.hasNext());
		                policyTypeSlot= createPolicyTypeSlot();
		                policySlot=createPolicySlot();
		                while (i.hasNext()) {
		                	DefaultSimpleInstance policyInst = (DefaultSimpleInstance)i.next();
		                	
		                	/*System.out.println("POLSLOT:"+policyTypeSlot+ "ww"+policyInst);
		                	String typeString = ""+policyInst.getOwnSlotValue(policyTypeSlot);//.toString();
		                	String polString  = ""+policyInst.getOwnSlotValue(policySlot);
		                	System.out.println(	"SLOTNAME:::::::::::::::::"+polString+ 
		                						"  Type:"+typeString);
	                		
	                		polVector.add(new PolicyData(polString,typeString,definingClassName,isLocalyDefinedValue));
	                		/**/
		                	polVector.add(new PolicyData(policyInst,definingClassName,isLocalyDefinedValue));
//		                		System.out.println("SLOTNAME:::::::::::::::::"+aPolicy+ "  Type:"+aPolicy.getClass().getName());
//		                		
//		                		polVector.add(new PolicyData(aPolicy.toString(),policyType,definingClassName,isLocalyDefinedValue));
		                }
		                //return clsInst.getOwnSlotValues(specialSlot);
		        }
		}
		
		return polVector;
	}
	
	/** 
	 * To change the Value of a policy.
	 * @param polInst
	 * @param specialSlotName
	 * @param oldPolicy
	 * @param newPolicy
	 */
	public void changeLocalPolicy(Instance polInst, String specialSlotName,String oldPolicy, String newPolicy){
		Slot specialSlot = kb.getSlot(specialSlotName);
		
		        if (polInst != null) {
		        	if(newPolicy==null){
		        		kb.deleteInstance(polInst);
		        	}else{
		        		polInst.setOwnSlotValue(specialSlot, newPolicy);
		        	}
		        }

	}
	
	/**
	 * Create a new policy for the specified class.
	 * @param cls
	 * @param specialSlotName
	 * @param policy
	 * @param type
	 */
	public void createLocalPolicy(Cls cls, String specialSlotName,String policy, String type){
		Slot specialSlot = kb.getSlot(specialSlotName);
		 //System.out.println("polInst:"+cls);    
			String definingClassName= cls.getName();
			String policyType = getPolicyType(cls);
			//		 Check that the meta-class of cls is the correct one and that it has
			//		 our special slot
			Collection metaSlots = cls.getDirectType().getDirectTemplateSlots();
			Vector polVector= new Vector(20);
			//System.out.println("collection:"+metaSlots+ "\tspecial:"+specialSlot + "\tsp_name:"+specialSlotName);
			if (metaSlots.contains(specialSlot)) {
			        Instance clsInst = kb.getInstance(cls.getName());
			             
			        if (clsInst != null) {
			        	long timeInMillis= System.currentTimeMillis();
			        	Instance polInst= kb.createInstance("ClsPolicy"+timeInMillis,getPolicyCls(),true);
//			        	polInst.addOwnSlotValue(getPolicySlotName(),"p"+timeInMillis);
			        	//System.out.println("instcreate:"+polInst);
			        	//clsInst.removeOwnSlotValue(specialSlot,oldPolicy);
			        	//clsInst.addOwnSlotValue(specialSlot,newPolicy);
			        	clsInst.addOwnSlotValue(specialSlot,polInst);
			        }
			}
	}
	
	/**
	 * Create a new policy for the specified slot.
	 * @param slot
	 * @param specialSlotName
	 * @param policy
	 * @param type
	 */
	public void createLocalPolicy(Slot slot, String specialSlotName,String policy, String type){
		Slot specialSlot = kb.getSlot(specialSlotName);
		 String definingClassName= slot.getName();
			String policyType = getPolicyType(slot);
			//		 Check that the meta-class of cls is the correct one and that it has
			//		 our special slot
			Collection metaSlots = slot.getDirectType().getDirectTemplateSlots();
			Vector polVector= new Vector(20);
			//System.out.println("collection:"+metaSlots+ "\tspecial:"+specialSlot + "\tsp_name:"+specialSlotName);
			if (metaSlots.contains(specialSlot)) {
			        Instance clsInst = kb.getInstance(slot.getName());			             
			        if (clsInst != null) {
			        	long timeInMillis=System.currentTimeMillis();
			        	Instance polInst= kb.createInstance("SlotPolicy"+timeInMillis,getPolicyCls(),true);
//			        	polInst.addOwnSlotValue(getPolicySlotName(),"p"+timeInMillis);
			        	clsInst.addOwnSlotValue(specialSlot,polInst);
			        }
			}
	}
	
	/**
	 * Get all policy that apply to the specified class.
	 * @param cls
	 * @return
	 */
	public Collection getAllPolicies(Cls cls){
		Vector allPolicies= new Vector(20);
		allPolicies.addAll(getLocalPolicy(cls,PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE,true));
		
		for(Iterator it= cls.getSuperclasses().iterator(); it.hasNext();){
			Cls aCls=(Cls)it.next();
			allPolicies.addAll(getLocalPolicy(aCls,PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE,false));
		}	
		
		return allPolicies;
	}
	
	/**
	 * Get the policy that another policy in the specified class can overwrite. 
	 * @param cls
	 * @return
	 */
	public Collection getOverriddablePolicies(Cls cls){
		//Vector allPolicies= new Vector(20);
		Vector mayBeOverridden= new Vector(20);
		Vector alreadyOverridden= new Vector(10);
		
		for(Iterator it=getAllPolicies(cls).iterator();
			it.hasNext();){
			PolicyData polData=(PolicyData)it.next();
			//if(polData.isLocallyDefined){
				alreadyOverridden.add(polData.policyOverridden);
			//}else{
			if(!polData.isLocallyDefined && polData.policyType=="D"){
				mayBeOverridden.add(polData.policyInst);
			}			
		}
		
		mayBeOverridden.removeAll(alreadyOverridden);
		return mayBeOverridden;
	}
	/**
	 * To get the policy that apply to the slot dirrect type.
	 * @param slot
	 * @param specialSlotName
	 * @param isLocalyDefinedValue
	 * @return
	 */
	public Collection getLocalPolicy(Slot slot, String specialSlotName,boolean isLocalyDefinedValue){
		Vector polVector= new Vector(20);
		if(slot==null){
			return polVector;
		}
		/////////////**Slot specialSlot = kb.getSlot(specialSlotName);
		Slot specialSlot = kb.getSlot(DEFAULT_POLICY_CLS_POLICY_SLOT_NAME);
		String definingClassName= slot.getName();
		String policyType = getPolicyType(slot);
		//		 Check that the meta-class of cls is the correct one and that it has
		//		 our special slot
		Collection metaSlots = slot.getDirectType().getDirectTemplateSlots();
		//Vector polVector= new Vector(20);
		
		if (metaSlots.contains(specialSlot)) {
		        Instance clsInst = kb.getInstance(slot.getName());
		        if (clsInst != null) {
		                // getSlots copied from InstanceStorer.java
		        		//InstanceStorer 
		                Iterator i = clsInst.getOwnSlotValues(specialSlot).iterator();//cls.getTemplateSlots().iterator();//getSlots(clsInst).iterator();
		                //System.out.println("HAS NEXT:"+i.hasNext());
		                while (i.hasNext()) {
		                		///////////////////**Object aPolicy=i.next();
		                	DefaultSimpleInstance policyInst = (DefaultSimpleInstance)i.next();
		                		//System.out.println("SLOTNAME:::::::::::::::::"+aPolicy+ "  Type:"+aPolicy.getClass().getName());
		                		////////////////////**polVector.add(new PolicyData(aPolicy.toString(),policyType,"policyName",definingClassName,isLocalyDefinedValue));
		                	///System.out.println("INSTANCE:"+policyInst);	
		                	polVector.add(new PolicyData(policyInst,definingClassName,isLocalyDefinedValue));
		                }
		                //return clsInst.getOwnSlotValues(specialSlot);
		        }
		}
		
		return polVector;
	}
	
	
//	public Collection changeSlotLocalPolicy(Slot slot, String newPolicy, String oldPolicy){
//		Vector polVector= new Vector(10);
//		if(slot==null){
//			return polVector;
//		}
//		Slot specialSlot = getPolicySlot();//kb.getSlot(specialSlotName);
//		
//		String definingClassName= slot.getName();
//		String policyType = getPolicyType(slot);
//		//		 Check that the meta-class of cls is the correct one and that it has
//		//		 our special slot
//		Collection metaSlots = slot.getDirectType().getDirectTemplateSlots();
//		//Vector polVector= new Vector(20);
//		
//		if (metaSlots.contains(specialSlot)) {
//		        Instance clsInst = kb.getInstance(slot.getName());
//		        if (clsInst != null) {
//		            if(oldPolicy!=null){
//		            	clsInst.removeOwnSlotValue(specialSlot,oldPolicy);
//		            }
//		            
//		            if(newPolicy!=null){
//		            	clsInst.addOwnSlotValue(specialSlot,newPolicy);
//		            }
//		        	///Iterator i = clsInst.getOwnSlotValues(specialSlot).iterator();//cls.getTemplateSlots().iterator();//getSlots(clsInst).iterator();		                
//		        }
//		}
//		
//		return polVector;
//	}
	/**
	 * To get all the policies that apply to a slot.
	 */
	public Collection getAllPolicies(Slot slot){
		Vector allPolicies= new Vector(20);
		if(slot!=null){
			allPolicies.addAll(getLocalPolicy(slot,PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE,true));
			
			for(Iterator it= slot.getSuperslots().iterator(); it.hasNext();){
				Slot aSlot=(Slot)it.next();
				allPolicies.addAll(getLocalPolicy(aSlot,PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE,false));
			}	
		}
		
		return allPolicies;
		
	}
	
	
}

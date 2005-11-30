/**
 * NegotiateTrust.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jan 25, 2005 (02:00:55 EST) WSDL2Java emitter.
 */

package ro.pub.egov.linux.ionut.TrustNegotiation_wsdl;

public class NegotiateTrust  implements java.io.Serializable {
    private ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer source;
    private ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer target;
    private java.lang.String[] trace;
    private java.lang.String goal;
    private java.lang.String proof;
    private int status;
    private long reqQueryId;
    private int messageType;

    public NegotiateTrust() {
    }

    public NegotiateTrust(
           java.lang.String goal,
           int messageType,
           java.lang.String proof,
           long reqQueryId,
           ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer source,
           int status,
           ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer target,
           java.lang.String[] trace) {
           this.source = source;
           this.target = target;
           this.trace = trace;
           this.goal = goal;
           this.proof = proof;
           this.status = status;
           this.reqQueryId = reqQueryId;
           this.messageType = messageType;
    }


    /**
     * Gets the source value for this NegotiateTrust.
     * 
     * @return source
     */
    public ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer getSource() {
        return source;
    }


    /**
     * Sets the source value for this NegotiateTrust.
     * 
     * @param source
     */
    public void setSource(ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer source) {
        this.source = source;
    }


    /**
     * Gets the target value for this NegotiateTrust.
     * 
     * @return target
     */
    public ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer getTarget() {
        return target;
    }


    /**
     * Sets the target value for this NegotiateTrust.
     * 
     * @param target
     */
    public void setTarget(ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.Peer target) {
        this.target = target;
    }


    /**
     * Gets the trace value for this NegotiateTrust.
     * 
     * @return trace
     */
    public java.lang.String[] getTrace() {
        return trace;
    }


    /**
     * Sets the trace value for this NegotiateTrust.
     * 
     * @param trace
     */
    public void setTrace(java.lang.String[] trace) {
        this.trace = trace;
    }

    public java.lang.String getTrace(int i) {
        return this.trace[i];
    }

    public void setTrace(int i, java.lang.String _value) {
        this.trace[i] = _value;
    }


    /**
     * Gets the goal value for this NegotiateTrust.
     * 
     * @return goal
     */
    public java.lang.String getGoal() {
        return goal;
    }


    /**
     * Sets the goal value for this NegotiateTrust.
     * 
     * @param goal
     */
    public void setGoal(java.lang.String goal) {
        this.goal = goal;
    }


    /**
     * Gets the proof value for this NegotiateTrust.
     * 
     * @return proof
     */
    public java.lang.String getProof() {
        return proof;
    }


    /**
     * Sets the proof value for this NegotiateTrust.
     * 
     * @param proof
     */
    public void setProof(java.lang.String proof) {
        this.proof = proof;
    }


    /**
     * Gets the status value for this NegotiateTrust.
     * 
     * @return status
     */
    public int getStatus() {
        return status;
    }


    /**
     * Sets the status value for this NegotiateTrust.
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Gets the reqQueryId value for this NegotiateTrust.
     * 
     * @return reqQueryId
     */
    public long getReqQueryId() {
        return reqQueryId;
    }


    /**
     * Sets the reqQueryId value for this NegotiateTrust.
     * 
     * @param reqQueryId
     */
    public void setReqQueryId(long reqQueryId) {
        this.reqQueryId = reqQueryId;
    }


    /**
     * Gets the messageType value for this NegotiateTrust.
     * 
     * @return messageType
     */
    public int getMessageType() {
        return messageType;
    }


    /**
     * Sets the messageType value for this NegotiateTrust.
     * 
     * @param messageType
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NegotiateTrust)) return false;
        NegotiateTrust other = (NegotiateTrust) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              this.source.equals(other.getSource()))) &&
            ((this.target==null && other.getTarget()==null) || 
             (this.target!=null &&
              this.target.equals(other.getTarget()))) &&
            ((this.trace==null && other.getTrace()==null) || 
             (this.trace!=null &&
              java.util.Arrays.equals(this.trace, other.getTrace()))) &&
            ((this.goal==null && other.getGoal()==null) || 
             (this.goal!=null &&
              this.goal.equals(other.getGoal()))) &&
            ((this.proof==null && other.getProof()==null) || 
             (this.proof!=null &&
              this.proof.equals(other.getProof()))) &&
            this.status == other.getStatus() &&
            this.reqQueryId == other.getReqQueryId() &&
            this.messageType == other.getMessageType();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSource() != null) {
            _hashCode += getSource().hashCode();
        }
        if (getTarget() != null) {
            _hashCode += getTarget().hashCode();
        }
        if (getTrace() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTrace());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTrace(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getGoal() != null) {
            _hashCode += getGoal().hashCode();
        }
        if (getProof() != null) {
            _hashCode += getProof().hashCode();
        }
        _hashCode += getStatus();
        _hashCode += new Long(getReqQueryId()).hashCode();
        _hashCode += getMessageType();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NegotiateTrust.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://linux.egov.pub.ro/ionut/TrustNegotiation.wsdl", ">negotiateTrust"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("", "source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://linux.egov.pub.ro/ionut/TrustNegotiation.wsdl", "peer"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("target");
        elemField.setXmlName(new javax.xml.namespace.QName("", "target"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://linux.egov.pub.ro/ionut/TrustNegotiation.wsdl", "peer"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trace");
        elemField.setXmlName(new javax.xml.namespace.QName("", "trace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("goal");
        elemField.setXmlName(new javax.xml.namespace.QName("", "goal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("proof");
        elemField.setXmlName(new javax.xml.namespace.QName("", "proof"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reqQueryId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reqQueryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "messageType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

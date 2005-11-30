/**
 * GetNotificationURIResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jan 25, 2005 (02:00:55 EST) WSDL2Java emitter.
 */

package ro.pub.egov.linux.ionut.TrustNegotiation_wsdl;

public class GetNotificationURIResponse  implements java.io.Serializable {
    private java.lang.String namespaceURI;
    private java.lang.String localPart;

    public GetNotificationURIResponse() {
    }

    public GetNotificationURIResponse(
           java.lang.String localPart,
           java.lang.String namespaceURI) {
           this.namespaceURI = namespaceURI;
           this.localPart = localPart;
    }


    /**
     * Gets the namespaceURI value for this GetNotificationURIResponse.
     * 
     * @return namespaceURI
     */
    public java.lang.String getNamespaceURI() {
        return namespaceURI;
    }


    /**
     * Sets the namespaceURI value for this GetNotificationURIResponse.
     * 
     * @param namespaceURI
     */
    public void setNamespaceURI(java.lang.String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }


    /**
     * Gets the localPart value for this GetNotificationURIResponse.
     * 
     * @return localPart
     */
    public java.lang.String getLocalPart() {
        return localPart;
    }


    /**
     * Sets the localPart value for this GetNotificationURIResponse.
     * 
     * @param localPart
     */
    public void setLocalPart(java.lang.String localPart) {
        this.localPart = localPart;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetNotificationURIResponse)) return false;
        GetNotificationURIResponse other = (GetNotificationURIResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.namespaceURI==null && other.getNamespaceURI()==null) || 
             (this.namespaceURI!=null &&
              this.namespaceURI.equals(other.getNamespaceURI()))) &&
            ((this.localPart==null && other.getLocalPart()==null) || 
             (this.localPart!=null &&
              this.localPart.equals(other.getLocalPart())));
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
        if (getNamespaceURI() != null) {
            _hashCode += getNamespaceURI().hashCode();
        }
        if (getLocalPart() != null) {
            _hashCode += getLocalPart().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetNotificationURIResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://linux.egov.pub.ro/ionut/TrustNegotiation.wsdl", ">getNotificationURIResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("namespaceURI");
        elemField.setXmlName(new javax.xml.namespace.QName("", "namespaceURI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localPart");
        elemField.setXmlName(new javax.xml.namespace.QName("", "localPart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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

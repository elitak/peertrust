/**
 * TrustNegotiationNotificationMessageWrapperType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jan 25, 2005 (02:00:55 EST) WSDL2Java emitter.
 */

package ro.pub.egov.linux.ionut.TrustNegotiation_wsdl;

public class TrustNegotiationNotificationMessageWrapperType  implements java.io.Serializable {
    private ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType trustNegotiationNotificationMessage;

    public TrustNegotiationNotificationMessageWrapperType() {
    }

    public TrustNegotiationNotificationMessageWrapperType(
           ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType trustNegotiationNotificationMessage) {
           this.trustNegotiationNotificationMessage = trustNegotiationNotificationMessage;
    }


    /**
     * Gets the trustNegotiationNotificationMessage value for this TrustNegotiationNotificationMessageWrapperType.
     * 
     * @return trustNegotiationNotificationMessage
     */
    public ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType getTrustNegotiationNotificationMessage() {
        return trustNegotiationNotificationMessage;
    }


    /**
     * Sets the trustNegotiationNotificationMessage value for this TrustNegotiationNotificationMessageWrapperType.
     * 
     * @param trustNegotiationNotificationMessage
     */
    public void setTrustNegotiationNotificationMessage(ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.TrustNegotiationNotificationMessageType trustNegotiationNotificationMessage) {
        this.trustNegotiationNotificationMessage = trustNegotiationNotificationMessage;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TrustNegotiationNotificationMessageWrapperType)) return false;
        TrustNegotiationNotificationMessageWrapperType other = (TrustNegotiationNotificationMessageWrapperType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.trustNegotiationNotificationMessage==null && other.getTrustNegotiationNotificationMessage()==null) || 
             (this.trustNegotiationNotificationMessage!=null &&
              this.trustNegotiationNotificationMessage.equals(other.getTrustNegotiationNotificationMessage())));
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
        if (getTrustNegotiationNotificationMessage() != null) {
            _hashCode += getTrustNegotiationNotificationMessage().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TrustNegotiationNotificationMessageWrapperType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://linux.egov.pub.ro/ionut/TrustNegotiation.wsdl", "TrustNegotiationNotificationMessageWrapperType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trustNegotiationNotificationMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://linux.egov.pub.ro/ionut/TrustNegotiation.wsdl", "TrustNegotiationNotificationMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://linux.egov.pub.ro/ionut/TrustNegotiation.wsdl", "TrustNegotiationNotificationMessageType"));
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

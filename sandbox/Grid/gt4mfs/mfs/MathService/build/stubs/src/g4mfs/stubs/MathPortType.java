/**
 * MathPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jan 25, 2005 (02:00:55 EST) WSDL2Java emitter.
 */

package g4mfs.stubs;

public interface MathPortType extends java.rmi.Remote {
    public g4mfs.stubs.AddResponse add(int parameters) throws java.rmi.RemoteException;
    public g4mfs.stubs.SubstractResponse substract(int parameters) throws java.rmi.RemoteException;
    public int getValueRP(g4mfs.stubs.GetValueRP parameters) throws java.rmi.RemoteException;
    public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element queryResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.InvalidQueryExpressionFaultType, org.oasis.wsrf.properties.QueryEvaluationErrorFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType, org.oasis.wsrf.properties.UnknownQueryExpressionDialectFaultType;
    public org.oasis.wsrf.properties.SetResourcePropertiesResponse setResourceProperties(org.oasis.wsrf.properties.SetResourceProperties_Element setResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.UnableToModifyResourcePropertyFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType, org.oasis.wsrf.properties.SetResourcePropertyRequestFailedFaultType, org.oasis.wsrf.properties.InvalidSetResourcePropertiesRequestContentFaultType;
    public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element getMultipleResourcePropertiesRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName getResourcePropertyRequest) throws java.rmi.RemoteException, org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType, org.oasis.wsrf.properties.ResourceUnknownFaultType;
    public ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.NegotiateTrustResponse negotiateTrust(ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.NegotiateTrust request) throws java.rmi.RemoteException;
    public ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.GetNotificationURIResponse getNotificationURI(ro.pub.egov.linux.ionut.TrustNegotiation_wsdl.GetNotificationURI request) throws java.rmi.RemoteException;
    public org.oasis.wsn.SubscribeResponse subscribe(org.oasis.wsn.Subscribe subscribeRequest) throws java.rmi.RemoteException, org.oasis.wsn.TopicNotSupportedFaultType, org.oasis.wsn.InvalidTopicExpressionFaultType, org.oasis.wsn.SubscribeCreationFailedFaultType, org.oasis.wsn.ResourceUnknownFaultType, org.oasis.wsn.TopicPathDialectUnknownFaultType;
    public org.oasis.wsn.GetCurrentMessageResponse getCurrentMessage(org.oasis.wsn.GetCurrentMessage getCurrentMessageRequest) throws java.rmi.RemoteException, org.oasis.wsn.TopicNotSupportedFaultType, org.oasis.wsn.InvalidTopicExpressionFaultType, org.oasis.wsn.NoCurrentMessageOnTopicFaultType, org.oasis.wsn.ResourceUnknownFaultType;
}

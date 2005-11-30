/**
 * MathFactoryServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jan 25, 2005 (02:00:55 EST) WSDL2Java emitter.
 */

package g4mfs.stubs.service;

public class MathFactoryServiceLocator extends org.apache.axis.client.Service implements g4mfs.stubs.service.MathFactoryService {

    public MathFactoryServiceLocator() {
    }


    public MathFactoryServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MathFactoryServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MathFactoryPortTypePort
    private java.lang.String MathFactoryPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getMathFactoryPortTypePortAddress() {
        return MathFactoryPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MathFactoryPortTypePortWSDDServiceName = "MathFactoryPortTypePort";

    public java.lang.String getMathFactoryPortTypePortWSDDServiceName() {
        return MathFactoryPortTypePortWSDDServiceName;
    }

    public void setMathFactoryPortTypePortWSDDServiceName(java.lang.String name) {
        MathFactoryPortTypePortWSDDServiceName = name;
    }

    public g4mfs.stubs.MathFactoryPortType getMathFactoryPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MathFactoryPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMathFactoryPortTypePort(endpoint);
    }

    public g4mfs.stubs.MathFactoryPortType getMathFactoryPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            g4mfs.stubs.bindings.MathFactoryPortTypeSOAPBindingStub _stub = new g4mfs.stubs.bindings.MathFactoryPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getMathFactoryPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMathFactoryPortTypePortEndpointAddress(java.lang.String address) {
        MathFactoryPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (g4mfs.stubs.MathFactoryPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                g4mfs.stubs.bindings.MathFactoryPortTypeSOAPBindingStub _stub = new g4mfs.stubs.bindings.MathFactoryPortTypeSOAPBindingStub(new java.net.URL(MathFactoryPortTypePort_address), this);
                _stub.setPortName(getMathFactoryPortTypePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("MathFactoryPortTypePort".equals(inputPortName)) {
            return getMathFactoryPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.globus.org/gt4ide/example/MathFactoryService/service", "MathFactoryService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.globus.org/gt4ide/example/MathFactoryService/service", "MathFactoryPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("MathFactoryPortTypePort".equals(portName)) {
            setMathFactoryPortTypePortEndpointAddress(address);
        }
        else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

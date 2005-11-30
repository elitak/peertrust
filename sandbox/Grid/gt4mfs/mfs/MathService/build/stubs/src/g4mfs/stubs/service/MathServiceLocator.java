/**
 * MathServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Jan 25, 2005 (02:00:55 EST) WSDL2Java emitter.
 */

package g4mfs.stubs.service;

public class MathServiceLocator extends org.apache.axis.client.Service implements g4mfs.stubs.service.MathService {

    public MathServiceLocator() {
    }


    public MathServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MathServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MathPortTypePort
    private java.lang.String MathPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getMathPortTypePortAddress() {
        return MathPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MathPortTypePortWSDDServiceName = "MathPortTypePort";

    public java.lang.String getMathPortTypePortWSDDServiceName() {
        return MathPortTypePortWSDDServiceName;
    }

    public void setMathPortTypePortWSDDServiceName(java.lang.String name) {
        MathPortTypePortWSDDServiceName = name;
    }

    public g4mfs.stubs.MathPortType getMathPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MathPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMathPortTypePort(endpoint);
    }

    public g4mfs.stubs.MathPortType getMathPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            g4mfs.stubs.bindings.MathPortTypeSOAPBindingStub _stub = new g4mfs.stubs.bindings.MathPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getMathPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMathPortTypePortEndpointAddress(java.lang.String address) {
        MathPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (g4mfs.stubs.MathPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                g4mfs.stubs.bindings.MathPortTypeSOAPBindingStub _stub = new g4mfs.stubs.bindings.MathPortTypeSOAPBindingStub(new java.net.URL(MathPortTypePort_address), this);
                _stub.setPortName(getMathPortTypePortWSDDServiceName());
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
        if ("MathPortTypePort".equals(inputPortName)) {
            return getMathPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.globus.org/gt4ide/example/MathService/service", "MathService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.globus.org/gt4ide/example/MathService/service", "MathPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("MathPortTypePort".equals(portName)) {
            setMathPortTypePortEndpointAddress(address);
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

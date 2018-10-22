/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.8  Built on : May 19, 2018 (07:06:38 BST)
 */
package edu.itq.soa.buro;


/**
 *  ExtensionMapper class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ExtensionMapper {
    public static java.lang.Object getTypeObject(
        java.lang.String namespaceURI, java.lang.String typeName,
        javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
        if ("http://itq.edu/soa/credit".equals(namespaceURI) &&
                "curp_type1".equals(typeName)) {
            return edu.itq.soa.buro.Curp_type1.Factory.parse(reader);
        }

        if ("http://itq.edu/soa/credit".equals(namespaceURI) &&
                "apellidoMaterno_type1".equals(typeName)) {
            return edu.itq.soa.buro.ApellidoMaterno_type1.Factory.parse(reader);
        }

        if ("http://itq.edu/soa/credit".equals(namespaceURI) &&
                "nombres_type1".equals(typeName)) {
            return edu.itq.soa.buro.Nombres_type1.Factory.parse(reader);
        }

        if ("http://itq.edu/soa/credit".equals(namespaceURI) &&
                "apellidoPaterno_type1".equals(typeName)) {
            return edu.itq.soa.buro.ApellidoPaterno_type1.Factory.parse(reader);
        }

        if ("http://itq.edu/soa/credit".equals(namespaceURI) &&
                "curp_type1".equals(typeName)) {
            return edu.itq.soa.buro.Curp_type1.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}

/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.8  Built on : May 19, 2018 (07:06:38 BST)
 */
package com.soa.amortizacion;


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
        if ("http://soa.com/amortizacion".equals(namespaceURI) &&
                "registro_type0".equals(typeName)) {
            return com.soa.amortizacion.Registro_type0.Factory.parse(reader);
        }

        if ("http://soa.com/amortizacion".equals(namespaceURI) &&
                "tabla_type0".equals(typeName)) {
            return com.soa.amortizacion.Tabla_type0.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}

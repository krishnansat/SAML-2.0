/*
 * Copyright 2008 Members of the EGEE Collaboration.
 * Copyright 2008 University Corporation for Advanced Internet Development, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.ws.wssecurity.impl;


import org.opensaml.ws.wssecurity.Reference;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

/**
 * ReferenceMarshaller.
 * 
 */
public class ReferenceMarshaller extends AbstractWSSecurityObjectMarshaller {

    /** {@inheritDoc} */
    protected void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException {
        Reference reference = (Reference) xmlObject;
        
        if (!DatatypeHelper.isEmpty(reference.getURI())) {
            domElement.setAttributeNS(null, Reference.URI_ATTRIB_NAME, reference.getURI());
        }
        
        if (!DatatypeHelper.isEmpty(reference.getValueType())) {
            domElement.setAttributeNS(null, Reference.VALUE_TYPE_ATTRIB_NAME, reference.getValueType());
        }
        
        XMLHelper.marshallAttributeMap(reference.getUnknownAttributes(), domElement);
        
    }

}

/*
 * Copyright [2007] [University Corporation for Advanced Internet Development, Inc.]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.encryption;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBaseTestCase;
import org.opensaml.xml.mock.SignableSimpleXMLObject;

/**
 * Test the encrypted key resolver which dereferences RetrievalMethods.
 */
public class ChainingEncryptedKeyResolverTest extends XMLObjectBaseTestCase {
    
    /** The resolver instance to be tested. */
    private ChainingEncryptedKeyResolver resolver;
    
    
    /** {@inheritDoc} */
    protected void setUp() throws Exception {
        super.setUp();
        
        resolver = new ChainingEncryptedKeyResolver();
        resolver.getResolverChain().add(new InlineEncryptedKeyResolver());
        resolver.getResolverChain().add(new SimpleRetrievalMethodEncryptedKeyResolver());
    }
    
    /** Test error case of empty resolver chain. */
    public void testEmptyChain() {
        String filename =  "/data/org/opensaml/xml/encryption/ChainingEncryptedKeyResolverSingleInline.xml";
        SignableSimpleXMLObject sxo =  (SignableSimpleXMLObject) unmarshallElement(filename);
        assertNotNull(sxo);
        assertNotNull(sxo.getSimpleXMLObjects().get(0));
        assertNotNull(sxo.getSimpleXMLObjects().get(0).getEncryptedData());
        
        EncryptedData encData = (EncryptedData) sxo.getSimpleXMLObjects().get(0).getEncryptedData();
        
        assertNotNull(encData.getKeyInfo());
        assertFalse(encData.getKeyInfo().getEncryptedKeys().isEmpty());
        assertTrue(encData.getKeyInfo().getRetrievalMethods().isEmpty());
        
        List<EncryptedKey> allKeys = getEncryptedKeys(sxo);
        assertFalse(allKeys.isEmpty());
        
        resolver.getRecipients().add("foo");
        
        //Make the resolver chain empty before resolving
        resolver.getResolverChain().clear();
        
        try {
            generateList(encData, resolver);
            fail("Resolver called with empty chain, should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // do nothing, error expected
        }        
    }
    
    /** One recipient specified to resolver, EncryptedKey in instance inline. */
    public void testSingleEKInline() {
        String filename =  "/data/org/opensaml/xml/encryption/ChainingEncryptedKeyResolverSingleInline.xml";
        SignableSimpleXMLObject sxo =  (SignableSimpleXMLObject) unmarshallElement(filename);
        assertNotNull(sxo);
        assertNotNull(sxo.getSimpleXMLObjects().get(0));
        assertNotNull(sxo.getSimpleXMLObjects().get(0).getEncryptedData());
        
        EncryptedData encData = (EncryptedData) sxo.getSimpleXMLObjects().get(0).getEncryptedData();
        
        assertNotNull(encData.getKeyInfo());
        assertFalse(encData.getKeyInfo().getEncryptedKeys().isEmpty());
        assertTrue(encData.getKeyInfo().getRetrievalMethods().isEmpty());
        
        List<EncryptedKey> allKeys = getEncryptedKeys(sxo);
        assertFalse(allKeys.isEmpty());
        
        resolver.getRecipients().add("foo");
        
        List<EncryptedKey> resolved = generateList(encData, resolver);
        assertEquals("Incorrect number of resolved EncryptedKeys found", 1, resolved.size());
        
        assertTrue("Unexpected EncryptedKey instance found", resolved.get(0) == allKeys.get(0));
    }
    
    /** One recipient specified to resolver, EncryptedKey in instance via RetrievalMethod . */
    public void testSingleEKRetrievalMethod() {
        String filename =  "/data/org/opensaml/xml/encryption/ChainingEncryptedKeyResolverSingleRetrievalMethod.xml";
        SignableSimpleXMLObject sxo =  (SignableSimpleXMLObject) unmarshallElement(filename);
        assertNotNull(sxo);
        assertNotNull(sxo.getSimpleXMLObjects().get(0));
        assertNotNull(sxo.getSimpleXMLObjects().get(0).getEncryptedData());
        
        EncryptedData encData = (EncryptedData) sxo.getSimpleXMLObjects().get(0).getEncryptedData();
        
        assertNotNull(encData.getKeyInfo());
        assertTrue(encData.getKeyInfo().getEncryptedKeys().isEmpty());
        assertFalse(encData.getKeyInfo().getRetrievalMethods().isEmpty());
        
        List<EncryptedKey> allKeys = getEncryptedKeys(sxo);
        assertFalse(allKeys.isEmpty());
        
        resolver.getRecipients().add("foo");
        
        List<EncryptedKey> resolved = generateList(encData, resolver);
        assertEquals("Incorrect number of resolved EncryptedKeys found", 1, resolved.size());
        
        assertTrue("Unexpected EncryptedKey instance found", resolved.get(0) == allKeys.get(0));
    }
    
    /** One recipient specified to resolver, EncryptedKeys in instance inline and via RetrievalMethod . */
    public void testMultiEKWithOneRecipient() {
        String filename =  "/data/org/opensaml/xml/encryption/ChainingEncryptedKeyResolverMultiple.xml";
        SignableSimpleXMLObject sxo =  (SignableSimpleXMLObject) unmarshallElement(filename);
        assertNotNull(sxo);
        assertNotNull(sxo.getSimpleXMLObjects().get(0));
        assertNotNull(sxo.getSimpleXMLObjects().get(0).getEncryptedData());
        
        EncryptedData encData = (EncryptedData) sxo.getSimpleXMLObjects().get(0).getEncryptedData();
        
        assertNotNull(encData.getKeyInfo());
        assertFalse(encData.getKeyInfo().getEncryptedKeys().isEmpty());
        assertFalse(encData.getKeyInfo().getRetrievalMethods().isEmpty());
        
        List<EncryptedKey> allKeys = getEncryptedKeys(sxo);
        assertFalse(allKeys.isEmpty());
        
        resolver.getRecipients().add("foo");
        
        List<EncryptedKey> resolved = generateList(encData, resolver);
        assertEquals("Incorrect number of resolved EncryptedKeys found", 2, resolved.size());
        
        assertTrue("Unexpected EncryptedKey instance found", resolved.get(0) == allKeys.get(0));
        assertTrue("Unexpected EncryptedKey instance found", resolved.get(1) == allKeys.get(3));
    }
    
    /** Two recipients specified to resolver, EncryptedKeys in instance inline and via RetrievalMethod . */
    public void testMultiEKWithTwoRecipients() {
        String filename =  "/data/org/opensaml/xml/encryption/ChainingEncryptedKeyResolverMultiple.xml";
        SignableSimpleXMLObject sxo =  (SignableSimpleXMLObject) unmarshallElement(filename);
        assertNotNull(sxo);
        assertNotNull(sxo.getSimpleXMLObjects().get(0));
        assertNotNull(sxo.getSimpleXMLObjects().get(0).getEncryptedData());
        
        EncryptedData encData = (EncryptedData) sxo.getSimpleXMLObjects().get(0).getEncryptedData();
        
        assertNotNull(encData.getKeyInfo());
        assertFalse(encData.getKeyInfo().getEncryptedKeys().isEmpty());
        assertFalse(encData.getKeyInfo().getRetrievalMethods().isEmpty());
        
        List<EncryptedKey> allKeys = getEncryptedKeys(sxo);
        assertFalse(allKeys.isEmpty());
        
        resolver.getRecipients().add("foo");
        resolver.getRecipients().add("baz");
        
        List<EncryptedKey> resolved = generateList(encData, resolver);
        assertEquals("Incorrect number of resolved EncryptedKeys found", 4, resolved.size());
        
        assertTrue("Unexpected EncryptedKey instance found", resolved.get(0) == allKeys.get(0));
        assertTrue("Unexpected EncryptedKey instance found", resolved.get(1) == allKeys.get(2));
        assertTrue("Unexpected EncryptedKey instance found", resolved.get(2) == allKeys.get(3));
        assertTrue("Unexpected EncryptedKey instance found", resolved.get(3) == allKeys.get(5));
    }
    
    /**
     * Extract all the EncryptedKey's from the SignableSimpleXMLObject.
     * 
     * @param sxo the mock object to process
     * @return a list of EncryptedKey elements
     */
    private List<EncryptedKey> getEncryptedKeys(SignableSimpleXMLObject sxo) {
        List<EncryptedKey> allKeys = new ArrayList<EncryptedKey>();
        allKeys.addAll(sxo.getSimpleXMLObjects().get(0).getEncryptedData().getKeyInfo().getEncryptedKeys());
        for (XMLObject xmlObject : sxo.getUnknownXMLObjects()) {
           if (xmlObject instanceof EncryptedKey)  {
               allKeys.add((EncryptedKey) xmlObject);
           }
        }
        return allKeys;
    }

    /**
     * Resolve EncryptedKeys and put them in an ordered list.
     * 
     * @param encData the EncryptedData context
     * @param ekResolver the resolver to test
     * @return list of resolved EncryptedKeys
     */
    private List<EncryptedKey> generateList(EncryptedData encData, EncryptedKeyResolver ekResolver) {
        List<EncryptedKey> resolved = new ArrayList<EncryptedKey>();
        for (EncryptedKey encKey : ekResolver.resolve(encData)) {
            resolved.add(encKey);
        }
        return resolved;
    }


}

/*
 * Copyright [2005] [University Corporation for Advanced Internet Development, Inc.]
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

/**
 * 
 */

package org.opensaml.saml1.core.validator;

import org.opensaml.saml1.core.AttributeStatement;
import org.opensaml.xml.validation.ValidationException;

/**
 * Checks {@link org.opensaml.saml1.core.AttributeStatement} for Schema compliance.
 */
public class AttributeStatementSchemaValidator extends SubjectStatementSchemaValidator<AttributeStatement> {

    /** {@inheritDoc} */
    public void validate(AttributeStatement attributeStatement) throws ValidationException {        
        super.validate(attributeStatement);

        if (attributeStatement.getAttributes().size() == 0) {
            throw new ValidationException("No Attribute Element present");
        }
    }
}
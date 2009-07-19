/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.domain;

import java.sql.Timestamp;

import org.seasar.doma.domain.TimestampDomain;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class TimestampDomainTest extends TestCase {

    public void testEquals() throws Exception {
        TimestampDomain domain = new TimestampDomain(Timestamp
                .valueOf("2009-05-08 16:31:10"));
        TimestampDomain domain2 = new TimestampDomain(Timestamp
                .valueOf("2009-05-08 16:31:10"));
        assertTrue(domain.equals(domain2));

        domain.setNull();
        assertFalse(domain.equals(domain2));

        domain2.setNull();
        assertTrue(domain.equals(domain2));

        domain.set(Timestamp.valueOf("2009-05-08 16:31:10"));
        assertFalse(domain.equals(domain2));
    }
}
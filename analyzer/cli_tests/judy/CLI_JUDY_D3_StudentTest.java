/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;

import junit.framework.TestCase;

/** 
 * Test case for the HelpFormatter class 
 *
 * @author Slawek Zachcial
 * @author John Keyes ( john at integralsource.com )
 * @author brianegge
 */
public class CLI_JUDY_D3_StudentTest extends TestCase
{
    private static final String EOL = System.getProperty("line.separator");
    
   public void testAIR_Div_910 (){
		HelpFormatter formatter = new HelpFormatter();
		int result = formatter.findWrapPos("hello\n",  5, 0);
		assertEquals(6, result);
	}
	
	public void testFindWrapPos1(){
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(formatter.findWrapPos("\ta\r\t", 2, 1), 2);
		assertEquals(formatter.findWrapPos("\n1\n233", 2, 3), 5);
		assertEquals(formatter.findWrapPos("A\n\rB", 10, -3), 2);
		assertEquals(formatter.findWrapPos("ABC", 2, 1), -1);
		assertEquals(formatter.findWrapPos("A\nB\nD", 2, 2), 3);
		assertEquals(formatter.findWrapPos("\n\r\t", 2, 1), 3);
		assertEquals(formatter.findWrapPos("\ta\r\t", 4, 4), -1);
		assertEquals(formatter.findWrapPos("\ta\r\t", 1, 2), 3);
		 try {
            formatter.findWrapPos("AB\tD", 0, -3);
            fail();
        } catch (java.lang.StringIndexOutOfBoundsException ex){}
    }
}

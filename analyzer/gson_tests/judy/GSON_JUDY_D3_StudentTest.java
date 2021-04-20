/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.stream;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("resource")
public final class GSON_JUDY_D3_StudentTest extends TestCase {
	
	public void test253_JTI_JTD(){
		StringWriter stringWriter = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(stringWriter);
		jsonWriter.setHtmlSafe(true);
		assertEquals(true, jsonWriter.isHtmlSafe());
	}
	
	public void test269_JTI_JTD(){
		StringWriter stringWriter = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(stringWriter);
		jsonWriter.setSerializeNulls(false);
		assertEquals(false, jsonWriter.getSerializeNulls());
	}
	
	public void test199_EGE_IPC() {
		try {
			JsonWriter writer = new JsonWriter(null);
		} catch (NullPointerException e) {
			assertEquals("out == null", e.getMessage());
		}	
	}
	
	public void test559_EGE() throws IOException {
		StringWriter writer1 = new StringWriter();
		JsonWriter writer = new JsonWriter(writer1);
		
		try {
			writer.beginObject();
			writer.name("test").value(2);
			writer.close();
		} catch (IOException e) {
			assertEquals("Incomplete document", e.getMessage());
		}			
	}
	
	public void test565_JIR_Ifge_Ifne_Ifgt() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(writer);
		jsonWriter.setHtmlSafe(true);
		jsonWriter.beginArray();
		jsonWriter.value(">");
		jsonWriter.value("<");
		jsonWriter.value("&");
		jsonWriter.value("=");
		jsonWriter.value("'");
		jsonWriter.endArray();
		
		assertEquals("[\"\\u003e\",\"\\u003c\",\"\\u0026\",\"\\u003d\",\"\\u0027\"]", 
				writer.toString());
	}
}

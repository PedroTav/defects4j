package com.google.gson.stream;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

@SuppressWarnings("resource")
public final class GSON_JUMBLE_D2_StudentTest extends TestCase{
	
	public void test2() {
		try {
			JsonWriter writer = new JsonWriter(null);
		} catch (NullPointerException e) {
			assertEquals("out == null", e.getMessage());
		}	
	}
	
	public void test8() throws IOException {
	    StringWriter writer1 = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(writer1);
	    jsonWriter.beginArray();
	    jsonWriter.beginObject();
	    try {
	      jsonWriter.endArray();
	    } catch (IllegalStateException e) {
	    	assertEquals("Nesting problem.", e.getMessage());
	    }
	  }
	
	public void test9() throws IOException {
		StringWriter writer1 = new StringWriter();
		JsonWriter writer = new JsonWriter(writer1);
		
		try {
			writer.beginObject();
			writer.name("test");
			writer.endObject();
			writer.close();
		} catch (IllegalStateException e) {
			assertEquals("Dangling name: test", e.getMessage());
		}			
	}
	
	public void test10() throws IOException {
		StringWriter writer1 = new StringWriter();
		JsonWriter writer = new JsonWriter(writer1);
		
		try {
			writer.beginObject();
			writer.name("test").value(2);
			writer.endObject();
			writer.close();
			writer.name("test2");
		} catch (IllegalStateException e) {
			assertEquals("JsonWriter is closed.", e.getMessage());
		}			
	}
	
	public void test11() throws IOException {
		StringWriter writer1 = new StringWriter();
		JsonWriter writer = new JsonWriter(writer1);
		
		try {
			writer.beginObject();
			writer.name((String) null).value(2);
		} catch (NullPointerException e) {
			assertEquals("name == null", e.getMessage());
		}			
	}
	
	public void test15() throws IOException {
		StringWriter writer1 = new StringWriter();
		JsonWriter writer = new JsonWriter(writer1);
		
		writer.setLenient(false);
		double val = 7.0/0.0;
		try {
			writer.value(val);		
		} catch (IllegalArgumentException e) {
			assertEquals("Numeric values must be finite, but was " + val, e.getMessage());
		}	
	}
	
	public void test19() throws IOException {
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
	
	public void test24() throws IOException {
	    StringWriter writer1 = new StringWriter();
	    JsonWriter writer = new JsonWriter(writer1);
	    writer.beginArray();
	    writer.endArray();
	    try {
	      writer.beginArray();
	    } catch (IllegalStateException e) {
	    	assertEquals("JSON must have only one top-level value.", e.getMessage());
	    }
	  }
	public void test44() {
		StringWriter writer1 = new StringWriter();
		JsonWriter writer = new JsonWriter(writer1);
		
		writer.setLenient(true);
		assertEquals(true, writer.isLenient());
		
		writer.setLenient(false);
		assertEquals(false, writer.isLenient());
	}
	
	public void test45() {
		StringWriter writer1 = new StringWriter();
		JsonWriter writer = new JsonWriter(writer1);
		
		writer.setHtmlSafe(true);
		assertEquals(true, writer.isHtmlSafe());
		
		writer.setHtmlSafe(false);
		assertEquals(false, writer.isHtmlSafe());
	}
	
	public void test46() {
		StringWriter writer1 = new StringWriter();
		JsonWriter writer = new JsonWriter(writer1);
		
		writer.setSerializeNulls(true);
		assertEquals(true, writer.getSerializeNulls());
		
		writer.setSerializeNulls(false);
		assertEquals(false, writer.getSerializeNulls());
	}
	
	public void test51() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		writer1.beginArray();
	    writer1.value(1.0);
	    writer1.value(1.0);
		Object test = writer1.endArray();
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test535664() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		Object test = writer1.beginObject();
		assertEquals(writer2.getClass(), test.getClass());
		writer1.name("a").value(5);
	    writer1.name("b").value(false);
	    test = writer1.endObject();
	    assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test84() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		JsonWriter nullV = writer2.nullValue();
		Object test = writer1.value((String) null);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test85() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		Object test = writer1.value("val");
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test87() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		JsonWriter nullV = writer2.nullValue();
		Object test = writer1.jsonValue(null);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test88() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		Object test = writer1.jsonValue("val");
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test91() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		writer1.setSerializeNulls(false);
		writer1.beginObject();
		writer1.name("name");
		
		Object test = writer1.nullValue();
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test92() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		Object test = writer1.nullValue();
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test94() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		Object test = writer1.value(true);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test96() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		Object test = writer1.value((Boolean) null);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test98() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
				
		Object test = writer1.value((Boolean) true);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test102() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
		
		double val = 25.2;
		Object test = writer1.value(val);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test103() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
				
		Object test = writer1.value((long) 5);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test105() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
				
		Object test = writer1.value((Number) null);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void test110() throws IOException {
		StringWriter writer = new StringWriter();
		JsonWriter writer1 = new JsonWriter(writer);
		JsonWriter writer2 = new JsonWriter(writer);
				
		Object test = writer1.value((Number) 5);
		assertEquals(writer2.getClass(), test.getClass());
	}
	
	public void testHtmlSafe() throws IOException {
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
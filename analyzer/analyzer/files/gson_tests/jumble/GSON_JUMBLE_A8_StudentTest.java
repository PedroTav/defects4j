package com.google.gson.stream;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("resource")
public final class GSON_JUMBLE_A8_StudentTest extends TestCase {

	public void testNullWriter() throws IOException {
		StringWriter stringWriter = null;
    	try {
    	  JsonWriter jsonWriter = new JsonWriter(stringWriter);
    	} 
    	catch (NullPointerException expected) {
    		assertEquals("out == null", expected.getMessage());
    	}
	}

	public void testNoBeginArray() throws IOException {
		StringWriter stringWriter = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(stringWriter);
		try {
			jsonWriter.endArray();
		}
		catch (IllegalStateException expected) {
    		assertEquals("Nesting problem.", expected.getMessage());
    	}
	}

	public void testNameWithoutValue() throws IOException {
	    StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
	    jsonWriter.name("a");
	    try {
	    	jsonWriter.endObject();
	    	fail();
	    } catch (IllegalStateException expected) {
	    	assertEquals("Dangling name: a", expected.getMessage());
	    }
  	}

  	public void testClosedWriterThrowsOnFlush() throws IOException {
  	  StringWriter stringWriter = new StringWriter();
  	  JsonWriter writer = new JsonWriter(stringWriter);
  	  writer.beginArray();
  	  writer.endArray();
  	  writer.close();
  	  try {
  	  	writer.flush();
  	  	fail();
  	  } catch (IllegalStateException expected) {
  	  	assertEquals("JsonWriter is closed.", expected.getMessage());
  	  }
  	}

  	public void testNullName() throws IOException {
    	StringWriter stringWriter = new StringWriter();
    	JsonWriter jsonWriter = new JsonWriter(stringWriter);
    	jsonWriter.beginObject();
    	try {
    	  jsonWriter.name(null);
    	  fail();
    	} catch (NullPointerException expected) {
    		assertEquals("name == null", expected.getMessage());
    	}
  	}

  	public void testNonFiniteDoubles() throws IOException {
	    StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginArray();
	    try {
	      jsonWriter.value(Double.NaN);
	      fail();
	    } catch (IllegalArgumentException expected) {
	      assertTrue(expected.getMessage().contains("Numeric values must be finite, but was "));
	    }
	    try {
	      jsonWriter.value(Double.NEGATIVE_INFINITY);
	      fail();
	    } catch (IllegalArgumentException expected) {
	      assertTrue(expected.getMessage().contains("Numeric values must be finite, but was "));
	    }
	    try {
	      jsonWriter.value(Double.POSITIVE_INFINITY);
	      fail();
	    } catch (IllegalArgumentException expected) {
	      assertTrue(expected.getMessage().contains("Numeric values must be finite, but was "));
	    }
  	}

  	public void testCloseWithIncompleteDocument() throws IOException {
  		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginArray();
	    jsonWriter.value(-0.0);
	    jsonWriter.value(1.0);
	    jsonWriter.value(Double.MAX_VALUE);
	    jsonWriter.value(Double.MIN_VALUE);
	    jsonWriter.value(0.0);
	    jsonWriter.value(-0.5);
	    jsonWriter.value(2.2250738585072014E-308);
	    jsonWriter.value(Math.PI);
	    jsonWriter.value(Math.E);
	    try {
	    	jsonWriter.close();
  		}
  		catch(IOException expected) {
  			assertEquals("Incomplete document", expected.getMessage());
  		}
  	}

  	public void testMultipleTopLevelValues() throws IOException {
    	StringWriter stringWriter = new StringWriter();
    	JsonWriter jsonWriter = new JsonWriter(stringWriter);
    	jsonWriter.beginArray().endArray();
    	try {
      		jsonWriter.beginArray();
      		fail();
    	} catch (IllegalStateException expected) {
      		assertEquals("JSON must have only one top-level value.", expected.getMessage());
    	}
  	}
  	
  	public void testHTMLSafeReplacementChars() throws IOException {
	    StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.setHtmlSafe(true);
	    jsonWriter.beginArray();
	    jsonWriter.value("<a");
	    jsonWriter.value(">");
	    jsonWriter.value("&");
	    jsonWriter.value("=");
	    jsonWriter.value("\'");
	    jsonWriter.endArray();
	    assertEquals("["
        + "\"\\u003ca\""
        + ","
        + "\"\\u003e\""
        + ","
        + "\"\\u0026\""
        + ","
        + "\"\\u003d\""
        + ","
        + "\"\\u0027\"]", stringWriter.toString());
  	}
	
  	public void testSetIndent() throws IOException {
	    StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	   	jsonWriter.setIndent("     ");
	   	jsonWriter.setIndent("");
	    jsonWriter.beginObject();
	    for (int i = 0; i < 20; i++) {
	      jsonWriter.name("a");
	      jsonWriter.beginObject();
	    }
	    for (int i = 0; i < 20; i++) {
	      jsonWriter.endObject();
	    }
	    jsonWriter.endObject();
	    assertEquals("{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":"
	        + "{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{"
	        + "}}}}}}}}}}}}}}}}}}}}}", stringWriter.toString());
	}

	public void testLenient() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.setLenient(true);
	    assertEquals(jsonWriter.isLenient(), true);
	}

	public void testHtmlSafe() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.setHtmlSafe(true);
	    assertEquals(jsonWriter.isHtmlSafe(), true);
	}

	public void testSerializeNulls() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.setSerializeNulls(false);
	    assertEquals(jsonWriter.getSerializeNulls(), false);
	}

	public void testEndArrayReturnValue() throws IOException {
 		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginArray(); 
	    assertEquals(jsonWriter.endArray(), jsonWriter);
	}

	public void testbeginObjectReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    assertEquals(jsonWriter.beginObject(), jsonWriter);
	}

	public void testEndObjectReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
	    assertEquals(jsonWriter.endObject(), jsonWriter);
	}
	
	public void testPushWhenStackNeedsToBeResized() throws IOException {
	    StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
	    for (int i = 0; i < 40; i++) {
	      jsonWriter.name("a");
	      jsonWriter.beginObject();
	    }
	    for (int i = 0; i < 40; i++) {
	      jsonWriter.endObject();
	    }
	    jsonWriter.endObject();
	    assertEquals("{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":"
	        + "{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":"
	        + "{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":"
	        + "{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{\"a\":{"
	        + "}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}", stringWriter.toString());
	}

	public void testValueReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
	    jsonWriter.name("c");
	    JsonWriter jsonWriter1 = jsonWriter.value((String)null);
	    assertEquals(jsonWriter, jsonWriter1);
	    jsonWriter.name("d");
	    JsonWriter jsonWriter2 = jsonWriter.value("d");
	    assertEquals(jsonWriter, jsonWriter2);
	    jsonWriter.endObject();
	}

	public void testJsonValueReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
		jsonWriter.name("a");
	    JsonWriter jsonWriter1 = jsonWriter.jsonValue("{\"b\":true}");
	    assertEquals(jsonWriter, jsonWriter1);
	    jsonWriter.name("b");
	    JsonWriter jsonWriter2 = jsonWriter.jsonValue(null);
	    assertEquals(jsonWriter, jsonWriter2);
	    jsonWriter.endObject();
	}

	public void testNullValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    JsonWriter jsonWriter1 = null;
	    jsonWriter.setSerializeNulls(false);
	    jsonWriter.beginObject();
	    jsonWriter.name("a");
	    jsonWriter1 = jsonWriter.value((String)null);
	    try {
	    	jsonWriter.value((String)null);
	    }
	    catch(Exception e) {

	    }
	    jsonWriter.endObject();
	    assertEquals(jsonWriter1, jsonWriter);
	}

	public void testValuebooleanInputReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    JsonWriter jsonWriter1 = null;
	    jsonWriter.setSerializeNulls(false);
	    jsonWriter.beginObject();
	    jsonWriter.name("a");
	    jsonWriter1 = jsonWriter.value(true);
	    jsonWriter.endObject();
	    assertEquals(jsonWriter1, jsonWriter);
	}

	public void testValueBooleanInputReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
	    jsonWriter.name("c");
	    JsonWriter jsonWriter1 = jsonWriter.value((Boolean)true);
	    assertEquals(jsonWriter, jsonWriter1);
	    jsonWriter.name("d");
	    JsonWriter jsonWriter2 = jsonWriter.value((Boolean)null);
	    assertEquals(jsonWriter, jsonWriter2);
	    jsonWriter.endObject();
	}

	public void testValueDoubleInputReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
	    jsonWriter.name("c");
	    JsonWriter jsonWriter1 = jsonWriter.value(2.2);
	    assertEquals(jsonWriter, jsonWriter1);
	    jsonWriter.endObject();
	}

	public void testValueLongInputReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
	    jsonWriter.name("c");
	    JsonWriter jsonWriter1 = jsonWriter.value((long)22);
	    assertEquals(jsonWriter, jsonWriter1);
	    jsonWriter.endObject();
	}

	public void testValueNumberInputReturnValue() throws IOException {
		StringWriter stringWriter = new StringWriter();
	    JsonWriter jsonWriter = new JsonWriter(stringWriter);
	    jsonWriter.beginObject();
	    jsonWriter.name("c");
	    JsonWriter jsonWriter1 = jsonWriter.value((Number)2);
	    assertEquals(jsonWriter, jsonWriter1);
	    jsonWriter.name("d");
	    JsonWriter jsonWriter2 = jsonWriter.value((Number)null);
	    assertEquals(jsonWriter, jsonWriter2);
	    jsonWriter.endObject();
	}
}
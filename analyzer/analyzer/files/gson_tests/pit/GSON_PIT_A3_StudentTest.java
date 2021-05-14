package com.google.gson.stream;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Assert;
import org.junit.Test;

public class GSON_PIT_A3_StudentTest extends TestCase {

  // Test for DEFAULTS group Start //

  @Test
  public void testEndArray() throws IOException {
    // 297.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    jsonWriter.value(0);
    jsonWriter.value(1);
    jsonWriter.value(-1);
    JsonWriter result = jsonWriter.endArray();
    Assert.assertNotNull(result);
  }

  @Test
  public void testBeginObject() throws IOException {
    // 308.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    JsonWriter result = jsonWriter.beginObject();
    Assert.assertNotNull(result);
  }

  @Test
  public void testEndObject() throws IOException {
    // 317.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    JsonWriter result = jsonWriter.endObject();
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithString() throws IOException {
    // 420.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    JsonWriter result = jsonWriter.value("A");
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithNullString() throws IOException {
    // 413.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    String test = null;
    JsonWriter result = jsonWriter.value(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testJsonValueWithString() throws IOException {
    // 437.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.name("a");
    JsonWriter result = jsonWriter.jsonValue("{\"b\":true}");
    Assert.assertNotNull(result);
  }

  @Test
  public void testJsonValueWithNullString() throws IOException {
    // 432.2
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginObject();
    jsonWriter.name("a");
    String test = null;
    JsonWriter result = jsonWriter.jsonValue(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testNullValue() throws IOException {
    // 451.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setSerializeNulls(false);
    jsonWriter.name("test_name");
    JsonWriter result = jsonWriter.nullValue();
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithBool() throws IOException {
    // 483.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    Boolean test = true;
    JsonWriter result = jsonWriter.value(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithbool() throws IOException {
    // 468.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    boolean test = true;
    JsonWriter result = jsonWriter.value(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithNullBool() throws IOException {
    // 456.1 478.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    Boolean test = null;
    JsonWriter result = jsonWriter.value(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithDouble() throws IOException {
    // 500.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    double test = 1.213;
    JsonWriter result = jsonWriter.value(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithLong() throws IOException {
    // 512.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    long test = 5366623;
    JsonWriter result = jsonWriter.value(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithNumber() throws IOException {
    // 535.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    Number test = 1;
    JsonWriter result = jsonWriter.value(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testValueWithNullNumber() throws IOException {
    // 523.2 524.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    Number test = null;
    JsonWriter result = jsonWriter.value(test);
    Assert.assertNotNull(result);
  }

  @Test
  public void testStringEqualTo128() throws IOException {
    // 572.1
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.beginArray();
    String test = Character.toString((char) 128);
    jsonWriter.value(test);
    jsonWriter.endArray();
    Assert.assertEquals("[\"\u0080\"]", stringWriter.toString());
  }

  // Test for DEFAULTS group End //

  // Test for STRONGER group Start //

  
  /*
  // Broken tests?

  @Test(expected = NullPointerException.class)
  public void testJsonWriterConstructor() throws IOException {
    // 198.2
    StringWriter stringWriter1 = null;
    JsonWriter jsonWriter1 = new JsonWriter(stringWriter1);

  }

  @Test(expected = IOException.class)
  public void testClose_Line587() throws IOException {
    // 558.6, 558.7
    StringWriter stringWriter1 = new StringWriter();
    JsonWriter jsonWriter1 = new JsonWriter(stringWriter1);
    jsonWriter1.close();

  }
  */

  @Test
  public void testSetIndent() throws IOException {
    // 213.2
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setIndent("");
    jsonWriter.beginObject();
    jsonWriter.name("a");
    jsonWriter.jsonValue("{\"b\":true}");
    jsonWriter.name("c");
    jsonWriter.value(1);
    jsonWriter.endObject();
    Assert.assertEquals("{\"a\":{\"b\":true},\"c\":1}", stringWriter.toString());
  }

  // Test for STRONGER group End //


}

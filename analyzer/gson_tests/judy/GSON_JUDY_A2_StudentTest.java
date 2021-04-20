package com.google.gson.stream;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Struct;

import org.junit.After;
import org.junit.Before;

@SuppressWarnings("resource")
public final class GSON_JUDY_A2_StudentTest extends TestCase {

  private final PrintStream standardOut = System.out;
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @Before
  public void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @After
  public void tearDown() {
    System.setOut(standardOut);
  }

  public void testFirstTest() throws IOException
  {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setIndent("   ");

    jsonWriter.beginArray();
    jsonWriter.value(true);
    jsonWriter.value(false);
    jsonWriter.value(5.0);
    jsonWriter.nullValue();
    jsonWriter.beginObject();
    jsonWriter.name("a").value(6.0);
    jsonWriter.name("b").value(7.0);
    jsonWriter.endObject();
    jsonWriter.beginArray();
    jsonWriter.value(8.0);
    jsonWriter.value(9.0);
    jsonWriter.endArray();
    jsonWriter.endArray();

    String expected = "[\n"
        + "   true,\n"
        + "   false,\n"
        + "   5.0,\n"
        + "   null,\n"
        + "   {\n"
        + "      \"a\": 6.0,\n"
        + "      \"b\": 7.0\n"
        + "   },\n"
        + "   [\n"
        + "      8.0,\n"
        + "      9.0\n"
        + "   ]\n"
        + "]";
    assertEquals(expected, stringWriter.toString());
  }

  public void testLine495_IFLE()
  {
    // impossible to retrieve
  }

  public void testLine529_EOC() throws IOException {
    StringWriter string1 = new StringWriter();
    JsonWriter writer1 = new JsonWriter(string1);
    try {
      writer1.value(Double.POSITIVE_INFINITY);
      fail();
    } catch (IllegalArgumentException expected) {
    }

    // Equivalent ? this operator is the Equals Comparsion vs == 
    // cannot compare the actual reference to the hard coded "infinity"
  }

  public void testLine529_EOC_2() throws IOException {
    StringWriter string1 = new StringWriter();
    JsonWriter writer1 = new JsonWriter(string1);
    Number number = Double.POSITIVE_INFINITY;
    try {
      writer1.value(number);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testLine269_JTD()
  {
    StringWriter string = new StringWriter();
    JsonWriter writer = new JsonWriter(string);
    writer.setSerializeNulls(true);
    assertEquals(true, writer.getSerializeNulls());
  }

  public void testLine558_IFLE() throws IOException
  {
    StringWriter string = new StringWriter();
    JsonWriter writer = new JsonWriter(string);
    try {
      writer.close();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testLine558_IFGE() throws IOException
  {
    StringWriter string = new StringWriter();
    JsonWriter writer = new JsonWriter(string);
    writer.beginObject();
    try {
      writer.close();
      fail();
    } catch (IOException expected) {
    }
  }

  public void testLine253_JTD()
  {
    StringWriter string = new StringWriter();
    JsonWriter writer = new JsonWriter(string);
    writer.setHtmlSafe(true);
    assertEquals(true, writer.isHtmlSafe());
  }

  // public void testLine214_JTD()
  // {
  //   StringWriter string = new StringWriter();
  //   JsonWriter writer = new JsonWriter(string);
  //   writer.setIndent("   ");
  //   assertEquals(true, writer.isHtmlSafe());
  // }

  // public void testLine529_IFGT() throws IOException
  // {
  //   StringWriter string = new StringWriter();
  //   JsonWriter writer = new JsonWriter(string);
  //   writer.setLenient(false);
  //   Number number = 123;
  //   try {
  //     writer.value(number);
  //     fail();
  //   } catch (IOException expected) {
  //   }
  // }

  // public void testLine495_IFGT() throws IOException
  // {
  //   StringWriter string = new StringWriter();
  //   JsonWriter writer = new JsonWriter(string);
  //   writer.setLenient(false);
  //   double number = 123;
  //   try {
  //     writer.value(number);
  //     fail();
  //   } catch (IOException expected) {
  //   }
  // }

  public void testLine199_IPC() throws NullPointerException
  {
    StringWriter string = null;
    try {
      JsonWriter writer = new JsonWriter(string);
      fail();
    } catch (NullPointerException expected) {
    }
  }

  public void testLine355_AIR_ADD() throws IOException
  {
    // private property stack , impossible to retrieve
  }
}

package com.google.gson.stream;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

@SuppressWarnings("resource")
public final class GSON_MAJOR_B5_StudentTest extends TestCase {

    public void testMut7() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        jsonWriter.beginObject();
        jsonWriter.name("teste").value("\u001F<");
        jsonWriter.endObject();
        jsonWriter.close();

        assertEquals("{\"teste\":\"\\u001f<\"}", stringWriter.toString());
    }

    public void testMut29() {
        try {
            new JsonWriter(null);
            fail();
        } catch (NullPointerException ex) {
            assertEquals("out == null", ex.getMessage());
        }
    }

    public void testMut30() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        jsonWriter.setIndent("   ");
        jsonWriter.setIndent("");
        jsonWriter.beginObject();
        jsonWriter.name("teste").value(123);
        jsonWriter.endObject();
        jsonWriter.close();

        assertEquals("{\"teste\":123}", stringWriter.toString());
    }

    public void testMut75() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        String expected = "";
        for (int i = 0; i < 35; i++) {
            jsonWriter.beginArray();
            expected += "[";
        }
        for (int i = 0; i < 35; i++) {
            jsonWriter.endArray();
            expected += "]";
        }
        assertEquals(expected, stringWriter.toString());
    }

    public void testMut114() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        // disable null values serialization
        jsonWriter.setSerializeNulls(false);
        jsonWriter.beginObject();
        jsonWriter.name("a");
        // because null values shouldn't be serialized, this pair name/value is not
        // created
        jsonWriter.jsonValue(null);
        jsonWriter.endObject();
        assertEquals("{}", stringWriter.toString());
    }

    public void testMut141() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        // enable relaxed syntax
        jsonWriter.setLenient(true);
        jsonWriter.beginObject();
        jsonWriter.name("a");
        jsonWriter.value(123.0d);
        jsonWriter.endObject();
        assertEquals("{\"a\":123.0}", stringWriter.toString());
    }

    public void testMut169() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.beginObject();
        jsonWriter.name("a");
        jsonWriter.value(123);
        jsonWriter.flush();
        jsonWriter.endObject();
        assertEquals("{\"a\":123}", stringWriter.toString());
    }

    public void testMut171() throws IOException {
        final String FILENAME = "/tmp/demo";
        // create file writer
        FileWriter fileWriter = new FileWriter(FILENAME);
        // create file reader
        BufferedReader bufFileReader = new BufferedReader(new FileReader(FILENAME));
        // init JsonWriter
        JsonWriter jsonWriter = new JsonWriter(fileWriter);
        // Open 10 arrays and compute the expected string
        String expectedStr = "";
        for (int i = 0; i < 10; i++) {
            jsonWriter.beginArray();
            expectedStr += "[";
        }
        // Flush the content to the file
        jsonWriter.flush();
        // Read a line of the file. Since the content was flushed, we should
        // read 10 left brackets
        String jsonStr = bufFileReader.readLine();
        // Assert
        assertEquals(expectedStr, jsonStr);
    }

    public void testMut172() throws IOException {
        final String FILENAME = "/tmp/demo";

        // create file writer
        FileWriter fileWriter = new FileWriter(FILENAME);
        // create file reader
        BufferedReader bufFileReader = new BufferedReader(new FileReader(FILENAME));
        // init JsonWriter
        JsonWriter jsonWriter = new JsonWriter(fileWriter);
        // write a simple document with an empty object
        jsonWriter.beginObject();
        jsonWriter.endObject();
        // close the json document. it should close the file stream as well
        jsonWriter.close();
        // Read a line of the file. by closing the file stream, the content is flushed
        String jsonStr = bufFileReader.readLine();
        // Assert the file content has the expected text
        assertEquals("{}", jsonStr);
        // Assert that the file was closed. An attempt to write to it should
        // throw an exception. See
        // https://docs.oracle.com/javase/8/docs/api/java/io/OutputStreamWriter.html#close--
        try {
            fileWriter.write("test");
            fail("File stream was not closed!");
        } catch (IOException e) {

        }

    }

    public void testMut177() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.beginObject();
        jsonWriter.name("a");
        try {
            jsonWriter.close();
            fail();
        } catch (IOException ex) {
            assertEquals("Incomplete document", ex.getMessage());
        }
    }

    public void testMut182() {
        StringWriter sw = new StringWriter();
        try {
            JsonWriter testWriter = new JsonWriter(sw);

            testWriter.close();

            fail();
        } catch (IOException e) {
            assertEquals("Incomplete document", e.getMessage());
        }
    }

    public void testMut198() {
        StringWriter sw = new StringWriter();
        try {
            JsonWriter testWriter = new JsonWriter(sw);

            testWriter.beginArray();
            testWriter.close();

            fail();
        } catch (IOException e) {
            assertEquals("Incomplete document", e.getMessage());
        }
    }

    public void testMut216() {
        StringWriter sw = new StringWriter();
        try {
            JsonWriter testWriter = new JsonWriter(sw);

            testWriter.value("\u0080");
        } catch (IOException e) {
            fail();
        }

    }

    public void testMut219() {
        StringWriter sw = new StringWriter();
        try {
            JsonWriter testWriter = new JsonWriter(sw);

            testWriter.value("\u0080");
            testWriter.flush();
        } catch (IOException e) {
            fail();
        }

        assertEquals(sw.toString(), "\"\u0080\"");
    }

    public void testMut263() {
        StringWriter sw = new StringWriter();
        try {
            JsonWriter testWriter = new JsonWriter(sw);

            testWriter.beginArray();
            testWriter.name("aa");
            testWriter.value("a");
            fail();
        } catch (IOException e) {
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Nesting problem.", e.getMessage());
        }
    }
}

package com.google.gson.stream;

import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;
import java.io.StringWriter;


public class GSON_MAJOR_A1_StudentTest extends TestCase {

    public void testMutant29()  {
        try {
            JsonWriter jsonWriter = new JsonWriter(null);
            Assert.assertTrue(false);
        }catch (NullPointerException e){
            Assert.assertTrue(true);
        }
    }

    public void testMutant30_31_34() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        try {
            jsonWriter.setIndent("");
            jsonWriter.beginObject();
            jsonWriter.name("a").value(true);
            jsonWriter.name("b").value(false);
            jsonWriter.name("c").value(5.0);
            jsonWriter.name("e").nullValue();
            jsonWriter.name("f").beginArray();
            jsonWriter.value(6.0);
            jsonWriter.value(7.0);
            jsonWriter.endArray();
            jsonWriter.name("g").beginObject();
            jsonWriter.name("h").value(8.0);
            jsonWriter.name("i").value(9.0);
            jsonWriter.endObject();
            jsonWriter.endObject();
            String expected = "{"+"\"a\":true," + "\"b\":false," + "\"c\":5.0," + "\"e\":null," + "\"f\":[" + "6.0," + "7.0" + "]," + "\"g\":{" + "\"h\":8.0," + "\"i\":9.0" + "}" + "}";
            assertEquals(expected, stringWriter.toString());
        } catch (IOException e) {

        }
    }

    public void testMutant141() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setLenient(true);
        jsonWriter.beginArray();
        jsonWriter.value(5.0);
        jsonWriter.endArray();
        assertEquals("[5.0]", stringWriter.toString());
    }

    public void testMutant177_198() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        try {
            writer.beginArray();
            writer.value(1);
            writer.close();
            fail();
        }catch(IOException exception) {

        }

    }

    public void testMutant179_182_190_193_197() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        try {
            writer.close();
            fail();
        }catch(IOException exception) {

        }

    }


    public void testMutant196() throws IOException {

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        try {
            jsonWriter.beginObject();
            jsonWriter.close();
        }catch(IOException exception) {
            Assert.assertTrue(true);
        }

    }

}
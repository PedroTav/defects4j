package com.google.gson.stream;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringWriter;

public class GSON_JUMBLE_A5_StudentTest extends TestCase {

    //M FAIL: com.google.gson.stream.JsonWriter:199: CP[9] "out == null" -> "___jumble___"
    public void test1(){
        try {
            JsonWriter jsonWriter = new JsonWriter(null);
        } catch (Exception e) {
            String message = "out == null";
            assertEquals(message, e.getMessage());
        }
    }

    //M FAIL: com.google.gson.stream.JsonWriter:339: CP[28] "Nesting problem." -> "___jumble___"
    public void test2(){
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.endObject();

        } catch (Exception e) {
            String message = "Nesting problem.";
            assertEquals(message, e.getMessage());
        }
    }

    //M FAIL: com.google.gson.stream.JsonWriter:253: removed assignment to htmlSafe
    //M FAIL: com.google.gson.stream.JsonWriter:261: changed return value (ireturn)
    public void test3(){
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setHtmlSafe(true);
        assertEquals(true, jsonWriter.isHtmlSafe());
    }

    //M FAIL: com.google.gson.stream.JsonWriter:496: CP[52] "Numeric values must be finite, but was " -> "___jumble___"
    public void test4(){
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.value(Double.NaN);
        } catch (Exception e) {
            String message = "Numeric values must be finite, but was NaN";
            assertEquals(message, e.getMessage());
        }
    }


    //M FAIL: com.google.gson.stream.JsonWriter:387: CP[39] "name == null" -> "___jumble___"
    public void test5(){
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.name(null);
        } catch (Exception e) {
            String message = "name == null";
            assertEquals(message, e.getMessage());
        }
    }

    //M FAIL: com.google.gson.stream.JsonWriter:157: CP[94] "\u003c" -> "___jumble___"
    //M FAIL: com.google.gson.stream.JsonWriter:158: CP[95] "\u003e" -> "___jumble___"
    //M FAIL: com.google.gson.stream.JsonWriter:159: CP[96] "\u0026" -> "___jumble___"
    //M FAIL: com.google.gson.stream.JsonWriter:160: CP[97] "\u003d" -> "___jumble___"
    //M FAIL: com.google.gson.stream.JsonWriter:161: CP[98] "\u0027" -> "___jumble___"
    //FAIL: com.google.gson.stream.JsonWriter:242: changed return value (ireturn)
    //FAIL: com.google.gson.stream.JsonWriter:565: negated conditional
    //FAIL: com.google.gson.stream.JsonWriter:157: 60 (<) -> 61 (=)
    //FAIL: com.google.gson.stream.JsonWriter:157: removed array assignment
    //FAIL: com.google.gson.stream.JsonWriter:158: 62 (>) -> 63 (?)
    //FAIL: com.google.gson.stream.JsonWriter:158: removed array assignment
    //FAIL: com.google.gson.stream.JsonWriter:159: 38 (&) -> 39 (')
    //FAIL: com.google.gson.stream.JsonWriter:159: removed array assignment
    //FAIL: com.google.gson.stream.JsonWriter:160: 61 (=) -> 62 (>)
    //FAIL: com.google.gson.stream.JsonWriter:160: removed array assignment
    //FAIL: com.google.gson.stream.JsonWriter:161: 39 (') -> 40 (()
    //FAIL: com.google.gson.stream.JsonWriter:161: removed array assignment
    public void test6() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setHtmlSafe(true);
        jsonWriter.beginArray();
        jsonWriter.value(">");
        jsonWriter.value("<");
        jsonWriter.value("=");
        jsonWriter.value("\'");
        jsonWriter.value("&");
        jsonWriter.endArray();
        jsonWriter.close();
        assertEquals("[\"\\u003e\","
                + "\"\\u003c\","
                + "\"\\u003d\","
                + "\"\\u0027\","
                + "\"\\u0026\"]", stringWriter.toString());
    }

    //M FAIL: com.google.gson.stream.JsonWriter:559: CP[66] "Incomplete document" -> "___jumble___"
    public void test7(){
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.close();
        } catch (Exception e) {
            String message = "Incomplete document";
            assertEquals(message, e.getMessage());
        }
    }

    //M FAIL: com.google.gson.stream.JsonWriter:242: changed return value (ireturn)
    public void test8(){
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setLenient(true);
        assertEquals(true, jsonWriter.isLenient());
    }

    //M FAIL: com.google.gson.stream.JsonWriter:277: changed return value (ireturn)
    //M FAIL: com.google.gson.stream.JsonWriter:269: removed assignment to serializeNulls
    public void test9(){
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setSerializeNulls(false);
        assertEquals(false, jsonWriter.getSerializeNulls());
    }

    //M FAIL: com.google.gson.stream.JsonWriter:342: CP[33] "Dangling name: " -> "___jumble___"
    public void test10(){
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = new JsonWriter(stringWriter);
            writer.beginArray();
            writer.name("teste");
            writer.endArray();
            writer.close();
            writer.endArray();
        }catch (Exception e){
            String message = "Dangling name: teste";
            assertEquals(message, e.getMessage());
        }
    }

    //M FAIL: com.google.gson.stream.JsonWriter:632: CP[78] "JSON must have only one top-level val
    public void test11(){
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = new JsonWriter(stringWriter);
            writer.beginArray();
            writer.endArray();
            writer.close();
            writer.endArray();
        }catch (Exception e){
            String message = "JsonWriter is closed.";
            assertEquals(message, e.getMessage());
        }

    }

    //M FAIL: com.google.gson.stream.JsonWriter:632: CP[78] "JSON must have only one top-level value." -> "___jumble___"
    public void test12(){
        try {
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = new JsonWriter(stringWriter);
            writer.setLenient(false);
            writer.beginObject();
            writer.endObject();
            writer.beginArray();
            writer.endArray();
        }catch (Exception e){
            String message = "JSON must have only one top-level value.";
            assertEquals(message, e.getMessage());
        }
    }

    //M FAIL: com.google.gson.stream.JsonWriter:478: changed return value (areturn)
     public void test13() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        Boolean teste = null;

        writer.setLenient(true);
        assertEquals(writer.nullValue(), writer.value(teste));
    }


    //M FAIL: com.google.gson.stream.JsonWriter:214: removed assignment to indent
    //M FAIL: com.google.gson.stream.JsonWriter:215: removed assignment to separator
    public void test14() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.setIndent("  ");
        writer.setIndent("");
        writer.beginObject();
        writer.name("a").value(true);
        writer.name("b").value(false);
        writer.name("c").value(5.0);
        writer.name("e").nullValue();
        writer.name("f").beginArray();
        writer.value(6.0);
        writer.value(7.0);
        writer.endArray();
        writer.name("g").beginObject();
        writer.name("h").value(8.0);
        writer.name("i").value(9.0);
        writer.endObject();
        writer.endObject();

        String expected = "{"
                + "\"a\":true,"
                + "\"b\":false,"
                + "\"c\":5.0,"
                + "\"e\":null,"
                + "\"f\":["
                + "6.0,"
                + "7.0"
                + "],"
                + "\"g\":{"
                + "\"h\":8.0,"
                + "\"i\":9.0"
                + "}"
                + "}";
        assertEquals(expected, stringWriter.toString());
    }

    //M FAIL: com.google.gson.stream.JsonWriter:355: * -> /
    //M FAIL: com.google.gson.stream.JsonWriter:356: 0 -> 1
    //M FAIL: com.google.gson.stream.JsonWriter:356: 0 -> 1
    //FAIL: com.google.gson.stream.JsonWriter:356: removed method call to arraycopy
    //FAIL: com.google.gson.stream.JsonWriter:357: removed assignment to stack
    public void test15() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        for (int i = 0; i < 40; i++) {
            writer.beginArray();
        }
        for (int i = 0; i < 40; i++) {
            writer.endArray();
        }
        assertEquals("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]", stringWriter.toString());
    }

    //M FAIL: com.google.gson.stream.JsonWriter:450: removed assignment to deferredName
    //FAIL: com.google.gson.stream.JsonWriter:269: removed assignment to serializeNulls
    public void teste16() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        writer.setSerializeNulls(false);
        writer.beginArray();
        writer.beginObject();
        writer.name("a").nullValue();
        writer.endObject();
        writer.endArray();
        assertEquals("[{}]", stringWriter.toString());

    }


    //M FAIL: com.google.gson.stream.JsonWriter:527: removed method call to writeDeferredName
    public  void teste17() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        writer.setSerializeNulls(false);
        writer.beginArray();
        writer.beginObject();
        writer.name("b").value(new Boolean(true));
        writer.name("b").value(new Double(5));
        writer.endObject();
        writer.endArray();
        assertEquals("[{\"b\":true,\"b\":5.0}]", stringWriter.toString());
    }

    //M FAIL: com.google.gson.stream.JsonWriter:591: - -> +
    public void teste18() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        char c = '\u2028';
        writer.value(c+" aççç");
        assertEquals("\"\\u2028 aççç\"", stringWriter.toString());
    }

    //......M FAIL: com.google.gson.stream.JsonWriter:297: changed return value (areturn)
    public void test19() throws IOException {
        //......M FAIL: com.google.gson.stream.JsonWriter:297: changed return value (areturn)
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        writer.beginArray();
        JsonWriter writer2 = writer.endArray();

        assertEquals(writer,writer2);
    }

    //M FAIL: com.google.gson.stream.JsonWriter:308: changed return value (areturn)
    //M FAIL: com.google.gson.stream.JsonWriter:317: changed return value (areturn)
    public void test20() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        //M FAIL: com.google.gson.stream.JsonWriter:308: changed return value (areturn)
        JsonWriter writer2 = writer.beginObject();
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:317: changed return value (areturn)
        writer2 = writer.endObject();
        assertEquals(writer,writer2);
    }



    //M FAIL: com.google.gson.stream.JsonWriter:415: changed return value (areturn)
    //M FAIL: com.google.gson.stream.JsonWriter:420: changed return value (areturn)
    public void test21() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        //M FAIL: com.google.gson.stream.JsonWriter:420: changed return value (areturn)
        writer.beginArray();
        JsonWriter writer2 = writer.value(">");
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:415: changed return value (areturn)
        writer2 = writer.value((String) null);
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:432: changed return value (areturn)
        writer2 = writer.jsonValue(null);
        assertEquals(writer,writer2);


        //M FAIL: com.google.gson.stream.JsonWriter:437: changed return value (areturn)
        writer2 = writer.jsonValue("str");
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:468: changed return value (areturn)
        writer2 = writer.value(true);
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:483: changed return value (areturn)
        writer2 = writer.value(new Boolean(true));
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:500: changed return value (areturn)
        double aux = 5.1;
        writer2 = writer.value(aux);
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:512: changed return value (areturn)
        long aux1 = 5;
        writer2 = writer.value(aux1);
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:524: changed return value (areturn)
        Number aux2 = null;
        writer2 = writer.value(aux2);
        assertEquals(writer,writer2);

        //M FAIL: com.google.gson.stream.JsonWriter:535: changed return value (areturn)
        aux2 = 5;
        writer2 = writer.value(aux2);
        assertEquals(writer,writer2);
    }

    //M FAIL: com.google.gson.stream.JsonWriter:451: changed return value (areturn)
    public void test22() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        writer.setSerializeNulls(false);
        writer.beginArray();
        writer.beginObject();
        JsonWriter writer2 = writer.name("a").nullValue();
        assertEquals(writer,writer2);
    }


    /*public void teste23() throws IOException {
        //4294967295

        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        long max = 2147483647;
        String aux = "";

        for (int i = 0; i < max; i++) {
            writer.beginArray();
            aux = aux + "[";
        }
        assertEquals(aux, stringWriter.toString());
    }

    public void teste24() throws IOException {
        try {
            Writer teste = Writer.nullWriter();
            JsonWriter writer = new JsonWriter(teste);
            writer.flush();
            teste.append("c");
        }catch (Exception e){
            assertEquals("Stream closed", e.getMessage());
        }
    }*/

}
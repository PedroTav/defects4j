package com.google.gson.stream;

import junit.framework.TestCase;

import javax.swing.plaf.synth.SynthLookAndFeel;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;

@SuppressWarnings("resource")
public final class GSON_PIT_B1_StudentTest extends TestCase {

    ///////////////////////
    /////// DEFAULT ///////
    ///////////////////////
    
    // 242, 261, 277, 297, 308, 317, 350, 355, 356, 415, 420, 432, 437, 451, 456, 468, 478, 480, 483, 500, 512, 524, 527, 535, 546, 555, 565, 572, 584, 590, 591

    // kill mutants in lines 242, 261, 277 
    public void testIsProperties() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        // Lenient
        assertFalse(writer.isLenient());
        writer.setLenient(true);
        assertTrue(writer.isLenient());
        // Html Safe
        assertFalse(writer.isHtmlSafe());
        writer.setHtmlSafe(true);
        assertTrue(writer.isHtmlSafe());
        // Serializable
        assertTrue(writer.getSerializeNulls());
        writer.setSerializeNulls(false);
        assertFalse(writer.getSerializeNulls());
    }

    // kill mutants in lines 297, 308, 317, 350 
    public void testArrayAndObject() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        assertEquals(writer, writer.beginObject());
        writer.name("a");
        writer.beginArray();
        assertEquals(writer, writer.endArray());
        assertNotNull(writer.endObject());
        assertEquals("{\"a\":[]}", stringWriter.toString());
    }

    // kill mutants in lines 355, 356 
    public void testStackOverflow() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginObject();
        writer.name("a");
        for (int i = 0; i < 40; i++) {
            writer.beginArray();
        }
        String value = "{\"a\":".concat(new String(new char[40]).
                replace("\0", "["));
        assertEquals(value, stringWriter.toString());

        for (int i = 0; i < 40; i++) {
            writer.endArray();
        }
        value = value.concat(new String(new char[40]).replace("\0", "]"));
        assertEquals(value, stringWriter.toString());
        writer.endObject();
        writer.close();
    }

    // kill mutants in lines 415, 420, 432, 437, 451, 456, 468, 478, 480, 483 
    // 500, 512, 524, 527, 535
    public void testNullValue() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginObject();
        writer.name("a");
        writer.beginArray();

        assertNotNull(writer.value((String) null));
        assertEquals(writer, writer.value("1"));

        assertNotNull(writer.jsonValue(null));
        assertEquals(writer, writer.jsonValue("1"));

        assertEquals(writer, writer.nullValue());
        writer.setSerializeNulls(false);
        writer.beginObject();
        writer.name("a");
        assertEquals(writer, writer.nullValue());
        writer.endObject();
        writer.setSerializeNulls(true);

        assertEquals(writer, writer.value(true));

        assertNotNull(writer.value((Boolean) null));
        writer.beginObject();
        writer.name("a");
        assertEquals(writer, writer.value((Boolean) true));
        assertTrue(stringWriter.toString().endsWith("\"a\":true"));
        writer.endObject();

        assertEquals(writer, writer.value((double) 2));

        assertEquals(writer, writer.value((long) 2));

        assertEquals(writer, writer.value((Number) null));
        writer.beginObject();
        writer.name("a");
        assertEquals(writer, writer.value((Number) 2));
        assertTrue(stringWriter.toString().endsWith("\"a\":2"));
        writer.endObject();

        writer.endArray();
        writer.endObject();
    }

    // Kill mutants in lines 565, 591
    public void testString() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        writer.beginObject();
        writer.name("\t\ta<\t€abc123");
        writer.nullValue();
        writer.endObject();
        assertEquals("{\"\\t\\ta<\\t€abc123\":null}", stringWriter.toString());
    }
    
    ////////////////////////
    /////// STRONGER ///////
    ////////////////////////
    
    // 198, 213, 354, 431, 523, 558(x2), 565  
  
    // Kill mutant in line 572, 590, 167
    public void testStackSize() throws IOException {
        StringWriter stringWriter = new StringWriter(128);
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginObject();
        writer.name("a");
        writer.beginArray();

        Runtime rt = Runtime.getRuntime();
        System.gc();
        double before = rt.totalMemory()-rt.freeMemory();

        for (int i = 0; i < 40; i++) {
            writer.beginArray();
        }

        System.gc();
        double after = rt.totalMemory()-rt.freeMemory();

        assertEquals(after,before+(4*32));
    }
    
    // kill mutant in line 198, 199, 387, 496, 531
    public void testException() throws IOException {
        try {
            new JsonWriter(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("out == null", e.getMessage());
        }

        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginObject();

        try {
            writer.name(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("name == null", e.getMessage());
        }

        writer.name("a");

        try {
            writer.value(NaN);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Numeric values must be finite, but was NaN", e.getMessage());
        }

        try {
            writer.value((Number) POSITIVE_INFINITY);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Numeric values must be finite, but was Infinity", e.getMessage());
        }
    }

    // kill mutant in line 213,
    public void testIndent() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer.beginObject().name("1");

        writer.setIndent(" ");
        writer.beginObject().name("a").value(1).endObject();
        assertEquals("{\n \"1\": {\n  \"a\": 1\n }", stringWriter.toString());

        writer.setIndent("");

        writer.name("b").beginArray().endArray().endObject();
        assertEquals("{\n \"1\": {\n  \"a\": 1\n },\"b\":[]}", stringWriter.toString());
    }
    
    // Kill mutants in lines 431, 446, 447
    public void testNullValues2() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        writer.beginObject().setSerializeNulls(false);
        writer.name("a").jsonValue(null);
        assertEquals("{", stringWriter.toString());

        try {
            writer.nullValue();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Nesting problem.", e.getMessage());
        }

        writer.name("b").nullValue().endObject();
        assertEquals("{}", stringWriter.toString());
    }

    ///////////////////
    /////// ALL ///////
    ///////////////////
      
    // 167, 242, 261, 277, 297, 308, 317, 342, 346, 350, 355, 356, 387, 415, 420, 432, 437, 446, 447, 451, 456, 468, 478, 483, 496, 500, 512, 524, 531, 535, 543, 558, 565, 572, 579, 584, 590, 559

    // kill mutant in lines 342, 346
    public void testClose() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);

        writer.setIndent(" ");
        writer.beginObject().name("a").beginArray().endArray();
        writer.name("b").nullValue();
        assertEquals("{\n \"a\": [],\n \"b\": null", stringWriter.toString());

        try {
            writer.name("c").endObject();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Dangling name: c", e.getMessage());
        }

    }

    // Kill mutants in lines 543, 558
    public void testFlushAndClose() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        JsonWriter writer2 = new JsonWriter(stringWriter);

        writer.name("a");
        writer.flush();
        try {
            writer.close();
            fail();
        } catch (IOException e) {
            assertEquals("Incomplete document", e.getMessage());
        }

        writer2.beginObject().name("a");

        try {
            writer2.close();
            fail();
        } catch (IOException e) {
            assertEquals("Incomplete document", e.getMessage());
        }

        writer2.nullValue();

        try {
            writer2.close();
            fail();
        } catch (IOException e) {
            assertEquals("Incomplete document", e.getMessage());
        }
        writer2.endObject().flush();
        writer2.close();
    }

    // Kill mutant in line 565
    public void testString2() throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter w = new JsonWriter(sw);

        w.setHtmlSafe(true);
        w.beginObject().name("<>").value(2).endObject().close();
        assertEquals("{\"\\u003c\\u003e\":2}", sw.toString());
    }
    
    // Equivalent mutants: 546, 555, 584 (x2)
    
    // Equivalent Mutants: 572(2 mutants) «««KILLED WITH testStackSize»»»
    // Equivalent Mutants: 590(2 mutants) «««KILLED WITH testStackSize»»»
    // Equivalent mutant: 167 «««KILLED WITH testStackSize»»»
}

package com.google.gson.stream;

import junit.framework.TestCase;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;

@SuppressWarnings("resource")
public final class GSON_JUDY_B4_StudentTest extends TestCase {
  
  /**
   * This test aims to kill the following mutant(s):
   * 
   * Target   -> Boolean 
   * Operator -> OMD 
   * Lines    -> -1  
   * 
   * -----------------------------------------------
   * 
   * This test should have killed the following 
   * mutant(s):
   * 
   * Target   -> String | Boolean | Number
   * Operator -> OMD    | OMD     | OMD
   * Lines    -> -1     | -1      | -1
   * 
   * -----------------------------------------------
   * 
   * This test does not kill any mutant. By manually
   * testing, we know for sure that these mutants 
   * should be killed with the current test.
   * 
   */
  public void testValueOverloadingMethods() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    
    try {
      jsonWriter.beginObject();
      jsonWriter.name("a").value("String");
      jsonWriter.name("b").value(true);
      jsonWriter.name("c").value((Boolean)null);
      jsonWriter.name("d").value(Double.MAX_VALUE);
      jsonWriter.name("e").value(Long.MAX_VALUE);
      jsonWriter.name("f").value(new BigInteger("9223372036854775808"));
      jsonWriter.endObject();

      String expected = "{\"a\":\"String\",\"b\":true,\"c\":null,\"d\":1.7976931348623157E308,\"e\":9223372036854775807,\"f\":9223372036854775808}";
      assertEquals(expected, stringWriter.toString());
    } catch (Exception e) {
      fail();
    }
  }

    /**
     * This test aims to kill the following mutant:
     * 
     * Operator -> JIR_Ifeq 
     * Lines    -> 146
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifeq | JIR_Ifge
     * Lines    -> 146      | 146
     * 
     */
    public void testReplacementCharUnicodeLimit() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      jsonWriter.beginArray();    
      jsonWriter.value("");
      jsonWriter.endArray();
      assertEquals("[\"\\u001f\"]", stringWriter.toString());
    }  

    /**
     * This test aims to kill the following mutant:
     * 
     * Operator -> EGE 
     * Lines    -> 199
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> EGE | IPC
     * Lines    -> 199 | 199
     * 
     */
    public void testJsonWriterGeneralizationCaughtErrors() throws IOException {
      try { new JsonWriter(null); } 
      catch (NullPointerException e) {
        assertEquals("out == null", e.getMessage());
      }      
    } 

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 213
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Ifgt | JIR_Ifge
     * Lines    -> 213      | 213
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. By manually
     * testing, we know for sure that these mutants 
     * should be killed with the current test.
     * 
     */
    public void testSetIndent() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      jsonWriter.setIndent("   ");

      jsonWriter.beginObject();
      jsonWriter.name("a").value(true);
      jsonWriter.name("b").value(false);
      jsonWriter.endObject();

      String expected = "{\n   \"a\": true,\n   \"b\": false\n}";
      assertEquals(expected, stringWriter.toString());
    }

    /**
     * This test aims to kill the following mutant:
     * 
     * Operator -> JTD 
     * Lines    -> 214
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JTD | JIR_Ifge
     * Lines    -> 214 | 213
     * 
     */
    public void testSetIndentKeywordRemoval() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      jsonWriter.setIndent("   ");

      jsonWriter.beginObject();
      jsonWriter.name("a").value(true);

      jsonWriter.setIndent("");

      jsonWriter.name("b").value(false);
      jsonWriter.endObject();

      String expected = "{\n   \"a\": true,\"b\":false}";
      assertEquals(expected, stringWriter.toString());
    }

    /**
     * This test aims to kill the following mutant:
     * 
     * Operator -> JTD
     * Lines    -> 253
     * 
     * --------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JTD | JTI
     * Lines    -> 253 | 253
     * 
     */
    public void testSetHtmlSafe() throws IOException {
      JsonWriter jsonWriter = new JsonWriter(new StringWriter());
      jsonWriter.beginArray();

      jsonWriter.setHtmlSafe(true);
      assertTrue(jsonWriter.isHtmlSafe());
    }

    /**
     * This test aims to kill the following mutant:
     * 
     * Operator -> JTD
     * Lines    -> 269
     * 
     * --------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JTD | JTI
     * Lines    -> 269 | 269
     * 
     */
    public void testSetSerializeNulls() throws IOException {
      JsonWriter jsonWriter = new JsonWriter(new StringWriter());
      
      jsonWriter.setSerializeNulls(false);
      assertFalse(jsonWriter.getSerializeNulls());
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 346
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 346
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Iflt | JIR_Ifgt
     * Lines    -> 346      | 346
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that these 
     * mutants should be killed with the current test.
     * 
     */
    public void testCloseContextNonEmpty() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      
      jsonWriter.setIndent("   ");
      
      jsonWriter.beginArray();
      jsonWriter.beginObject();
      jsonWriter.endObject();
      jsonWriter.endArray();
      
      String expected = "[\n   {}\n]";
      assertEquals(expected, stringWriter.toString());
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 354
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Iflt | JIR_Ifle
     * Lines    -> 354      | 354
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that these 
     * mutants should be killed with the current test.
     * 
     */
    public void testPushArrayIndexException() throws IOException {
      StringWriter stringWriter = new StringWriter();
      
      try {
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.beginObject();
        jsonWriter.endObject();      
        assertEquals("{}", stringWriter.toString());
      } catch (ArrayIndexOutOfBoundsException e) {
        fail();
      }
    }

    /**
     * This test aims to kill the following mutant:
     * 
     * Operator -> AIR_LeftOperand
     * Lines    -> 355
     * 
     * --------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifle | AIR_LeftOperand | AIR_RightOperand | AIR_Div | AIR_Rem | AIR_Sub
     * Lines    -> 354      | 355             | 355              | 355     | 355     | 355
     * 
     */
    public void testStackPushBoundaries() throws IOException {
      String expected = "";
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);

      for(int idx = 0; idx <= 32; idx++) {
        jsonWriter.beginArray();
        expected += "[";
      }
      
      for(int idx = 0; idx <= 32; idx++) {
        jsonWriter.endArray();
        expected += "]";
      }
      assertEquals(expected, stringWriter.toString());
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 366
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testPeekStackSizeGreater() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      
      jsonWriter.beginArray();
      jsonWriter.endArray();

      assertEquals("[]", stringWriter.toString());      
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 392
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testNameStackSizeGreater() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);      

      jsonWriter.beginObject();
      jsonWriter.name("a").value(true);
      jsonWriter.endObject();

      String expected = "{\"a\":true}";
      assertEquals(expected, stringWriter.toString());     
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 543
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Ifgt | JIR_Iflt
     * Lines    -> 543      | 543
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. By manually
     * testing, we know for sure that these mutants 
     * should be killed with the current test.
     * 
     */
    public void testFlushException() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      
      jsonWriter.beginObject();
      jsonWriter.endObject();
      jsonWriter.close();

      try {
        jsonWriter.flush();
        fail();
      } catch (IllegalStateException e) {}
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Target   -> size > 1
     * Operator -> JIR_Ifge
     * Lines    -> 558
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Ifge | JIR_Ifle
     * Lines    -> 558      | 558
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. By manually
     * testing, we know for sure that these mutants 
     * should be killed with the current test.
     * 
     */
    public void testCloseIncompleteDocumentFirstCondition() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);

      jsonWriter.beginArray();
      jsonWriter.endArray();
            
      try {
        jsonWriter.close();
      } catch (IOException e) {
        fail();
      }
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Target   -> size == 1
     * Operator -> JIR_Iflt
     * Lines    -> 558
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifle | JIR_Ifle
     * Lines    -> 558      | 558
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 558
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testCloseIncompleteDocumentSecondCondition() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
            
      try {
        jsonWriter.close();
        fail();  
      } catch (IOException e) {}
    }

    /**
     * This test aims to kill the following mutant:
     * 
     * Operator -> EGE
     * Lines    -> 559
     * 
     * --------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> EGE
     * Lines    -> 559
     * 
     */
    public void testCloseGeneralMutation() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
  
      jsonWriter.beginObject();
  
      try {
        jsonWriter.close();
      } catch (Exception e) {
        assertEquals(IOException.class, e.getClass());
      }
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifeq
     * Lines    -> 569
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt | JIR_Ifgt
     * Lines    -> 577      | 579
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Ifeq | JIR_Ifgt | JIR_Ifgt | JIR_Ifgt | JIR_Iflt | JIR_Ifgt | JIR_Ifeq
     * Lines    -> 569      | 572      | 577      | 579      | 579      | 590      | 590
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testStringUnicodeFormats() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);

      try {
        jsonWriter.beginObject();
        jsonWriter.name("1••").value("");
        jsonWriter.endObject();
        assertEquals(stringWriter.toString().length(), "{\"1••\":\"\"}".length());
      } catch (Exception e) {
        fail();
      }
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 584
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Ifgt | JIR_Ifeq
     * Lines    -> 584      | 584
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. By manually
     * testing, we know for sure that these mutants 
     * should be killed with the current test.
     * 
     */
    public void testStringReplacementLast() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      jsonWriter.beginObject();
      jsonWriter.name("a\f").value(20);
      jsonWriter.endObject();

      assertEquals("{\"a\\f\":20}", stringWriter.toString());
    }

    /**
     * This test aims to kill the following mutant:
     * 
     * Operator -> AIR_LeftOperand
     * Lines    -> 591
     * 
     * --------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> AIR_LeftOperand | AIR_Add
     * Lines    -> 591             | 591
     * 
     */
    public void testStringWhenLastLessThenLength() throws Exception {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      jsonWriter.beginObject();
   
      jsonWriter.name("\t••\t11\t1••\t•").value(20);
      jsonWriter.endObject();
      if(stringWriter.toString().equals("{\"\t\t\t\t•\":20}")) {
        fail();
      }   
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifeq
     * Lines    -> 602
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testNewlineIndent() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      jsonWriter.setIndent("   ");

      jsonWriter.beginObject();
      jsonWriter.name("a").value(true);
      jsonWriter.endObject();

      String expected = "{\n   \"a\": true\n}";
      assertEquals(expected, stringWriter.toString());
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifle
     * Lines    -> 615
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testBeforeNameContextEmptyObject() throws IOException {
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      jsonWriter.beginObject();
      jsonWriter.name("Value: ");
      
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append('\u2027');
      try {
        jsonWriter.value(stringBuilder.toString());
      } catch (IllegalStateException expected) {
        fail();
      }
    }

}
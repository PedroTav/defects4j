package com.google.gson.stream;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.Writer;


@SuppressWarnings("resource")
public final class GSON_JUMBLE_B8_StudentTest extends TestCase{


    /*  M FAIL: com.google.gson.stream.JsonWriter:355: * -> /
    *   M FAIL: com.google.gson.stream.JsonWriter:356: 0 -> 1
    *   M FAIL: com.google.gson.stream.JsonWriter:356: removed method call to arraycopy
    *   M FAIL: com.google.gson.stream.JsonWriter:357: removed assignment to stack
    *   M FAIL: com.google.gson.stream.JsonWriter:297: changed return value (areturn) 
    */
    public void testPush() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        for (int i = 0; i < 33; i++) {
            jsonWriter = jsonWriter.beginArray();
        }
        for (int i = 0; i < 33; i++) {
            jsonWriter = jsonWriter.endArray();
        }
        assertEquals("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]", stringWriter.toString());
    }
    
    /* M FAIL: com.google.gson.stream.JsonWriter:565: negated conditional
    *  M FAIL: com.google.gson.stream.JsonWriter:159: CP[96] "\u0026" -> "___jumble___"
    *  M FAIL: com.google.gson.stream.JsonWriter:253: removed assignment to htmlSafe
    *  M FAIL: com.google.gson.stream.JsonWriter:159: 38 (&) -> 39 (')   
    *  M FAIL: com.google.gson.stream.JsonWriter:159: removed array assignment
    *  M FAIL: com.google.gson.stream.JsonWriter:420: changed return value (areturn)
    *  killed: 6
    */
    public void testHtml() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
       
        jsonWriter.setHtmlSafe(true);
        
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.value("&");
        jsonWriter = jsonWriter.endArray();

        assertEquals("[\"\\u0026\"]", stringWriter.toString());
    }

    /* M FAIL: com.google.gson.stream.JsonWriter:157: CP[94] "\u003c" -> "___jumble___"
    *  M FAIL: com.google.gson.stream.JsonWriter:157: 60 (<) -> 61 (=)
    *  M FAIL: com.google.gson.stream.JsonWriter:157: removed array assignment
    */

    public void testHtml2() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
       
        jsonWriter.setHtmlSafe(true);
        
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.value("<");
        jsonWriter = jsonWriter.endArray();

        assertEquals("[\"\\u003c\"]", stringWriter.toString());
    }


    /* M FAIL: com.google.gson.stream.JsonWriter:158: CP[95] "\u003e" -> "___jumble___"
    *  M FAIL: com.google.gson.stream.JsonWriter:158: 62 (>) -> 63 (?)
    *  M FAIL: com.google.gson.stream.JsonWriter:158: removed array assignment
    *  M FAIL: com.google.gson.stream.JsonWriter:160: 61 (=) -> 62 (>)
    */

    public void testHtml3() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
       
        jsonWriter.setHtmlSafe(true);
        
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.value(">");
        jsonWriter = jsonWriter.endArray();

        assertEquals("[\"\\u003e\"]", stringWriter.toString());
    }


     /* M FAIL: com.google.gson.stream.JsonWriter:160: CP[97] "\u003d" -> "___jumble___"
    *  M FAIL: com.google.gson.stream.JsonWriter:160: removed array assignment
    */

    public void testHtml4() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
       
        jsonWriter.setHtmlSafe(true);
        
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.value("=");
        jsonWriter = jsonWriter.endArray();

        assertEquals("[\"\\u003d\"]", stringWriter.toString());
    }

    /* M FAIL: com.google.gson.stream.JsonWriter:161: CP[98] "\u0027" -> "___jumble___"
    *  M FAIL: com.google.gson.stream.JsonWriter:161: 39 (') -> 40 (()
    *  M FAIL: com.google.gson.stream.JsonWriter:161: removed array assignment
    */

    public void testHtml5() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
       
        jsonWriter.setHtmlSafe(true);
        
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.value("\'");
        jsonWriter = jsonWriter.endArray();

        assertEquals("[\"\\u0027\"]", stringWriter.toString());
    }

     
    /* M FAIL: com.google.gson.stream.JsonWriter:591: - -> +
    */
    public void testString() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setHtmlSafe(true);       

        jsonWriter = jsonWriter.beginArray();
        String xyz = "\'\'ç";
        
        jsonWriter = jsonWriter.value(xyz);
        jsonWriter = jsonWriter.endArray();

        assertEquals("[\"\\u0027\\u0027ç\"]", stringWriter.toString());
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:242: changed return value (ireturn)
    */
    public void testIsLenient() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setLenient(true);

        Boolean leni = jsonWriter.isLenient();
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.value(leni);
        jsonWriter = jsonWriter.endArray();

        assertEquals("[true]", stringWriter.toString());
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:261: changed return value (ireturn)
    */
    public void testHtmlFree() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setHtmlSafe(true);

        Boolean leni = jsonWriter.isHtmlSafe();
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.value(leni);
        jsonWriter = jsonWriter.endArray();

        assertEquals("[true]", stringWriter.toString());
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:269: removed assignment to serializeNulls
    *   M FAIL: com.google.gson.stream.JsonWriter:277: changed return value (ireturn)
    */
    public void testSerializeNullsTrue() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        jsonWriter.setSerializeNulls(true);

        assertEquals(true, jsonWriter.getSerializeNulls());
    }

    public void testSerializeNullsFalse() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        jsonWriter.setSerializeNulls(false);

        assertEquals(false, jsonWriter.getSerializeNulls());
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:308: changed return value (areturn)
    */ 
    public void testMultipleTopLevelValues() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter = jsonWriter.beginObject().endObject();
        try {
            jsonWriter = jsonWriter.beginObject();
          fail();
        } catch (IllegalStateException expected) {
        }
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:199: CP[9] "out == null" -> "___jumble___"
    */   
    public void testJsonWriterExceptionPrint() throws IOException {
        Writer stringWriter = null;
        try {
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            fail();
        } catch (NullPointerException expected) {
            String message = "out == null";
            assertEquals(message, expected.getMessage());
        }
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:387: CP[39] "name == null" -> "___jumble___"
    */   
    public void testNameExceptionPrint() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        try {
            jsonWriter = jsonWriter.name(null);
            fail();
        } catch (NullPointerException expected) {
            String message = "name == null";
            assertEquals(message, expected.getMessage());
        }
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:496: CP[52] "Numeric values must be finite, but was " -> "___jumble___"
    */   
    public void testValueExceptionPrint() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setLenient(false);
        double value = Double.NEGATIVE_INFINITY;
        try {
            jsonWriter = jsonWriter.value(value);
            fail();
        } catch (IllegalArgumentException expected) {
            String message = "Numeric values must be finite, but was " + value;
            assertEquals(message, expected.getMessage());
        }
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:559: CP[66] "Incomplete document" -> "___jumble___"
    */   
    public void testCloseExceptionPrint() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        try {
            jsonWriter.close();
            fail();
        } catch (IOException expected) {
            String message = "Incomplete document";
            assertEquals(message, expected.getMessage());
        }
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:632: CP[78] "JSON must have only one top-level value." -> "___jumble___"
        M FAIL: com.google.gson.stream.JsonWriter:317: changed return value (areturn)
        M FAIL: com.google.gson.stream.JsonWriter:350: changed return value (areturn)
    */   
    public void testBeforeValueExceptionPrint() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter = jsonWriter.beginObject().endObject();
        jsonWriter.setLenient(false);
        try {
            jsonWriter = jsonWriter.beginObject();
            fail();
        } catch (IllegalStateException expected) {
            String message = "JSON must have only one top-level value.";
            assertEquals(message, expected.getMessage());
        }
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:367: CP[38] "JsonWriter is closed." -> "___jumble___"
    */ 
    public void testClosedWriterThrowsOnNamePrint() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = new JsonWriter(stringWriter);
        writer = writer.beginArray();
        writer = writer.endArray();
        writer.close();
        try {
            writer = writer.name("a");
          fail();
        } catch (IllegalStateException expected) {
            String message = "JsonWriter is closed.";
            assertEquals(message, expected.getMessage());
        }
      }

    /*  M FAIL: com.google.gson.stream.JsonWriter:339: CP[28] "Nesting problem." -> "___jumble___"
    */ 
    public void testBadNestingObjectPrint() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.beginObject();
        try {
            jsonWriter = jsonWriter.endArray();
          fail();
        } catch (IllegalStateException expected) {
            String message = "Nesting problem.";
            assertEquals(message, expected.getMessage());
        }
      }
    
      /*  M FAIL: com.google.gson.stream.JsonWriter:342: CP[33] "Dangling name: " -> "___jumble___"
    */ 
    public void testNameWithoutValuePrint() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter = jsonWriter.beginObject();
        jsonWriter = jsonWriter.name("a");
        try {
            jsonWriter = jsonWriter.endObject();
          fail();
        } catch (IllegalStateException expected) {
            String message = "Dangling name: a";    
            assertEquals(message, expected.getMessage());
        }
      }

    /*  M FAIL: com.google.gson.stream.JsonWriter:450: removed assignment to deferredName
        M FAIL: com.google.gson.stream.JsonWriter:432: changed return value (areturn)
        M FAIL: com.google.gson.stream.JsonWriter:451: changed return value (areturn)
    */ 
    public void testNullValueDeferredName() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setSerializeNulls(false);
        String ola = "";

        jsonWriter = jsonWriter.beginObject();
        jsonWriter = jsonWriter.name("a");
        jsonWriter = jsonWriter.jsonValue(null);
        jsonWriter = jsonWriter.endObject();
        
        assertEquals("{}", stringWriter.toString()); 
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:480: removed method call to writeDeferredName
        M FAIL: com.google.gson.stream.JsonWriter:483: changed return value (areturn)
    */
    public void testValueWriteDeferredName() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        Boolean val = true;
        jsonWriter = jsonWriter.beginObject();
        jsonWriter = jsonWriter.name("b").value(val);
        jsonWriter = jsonWriter.endObject();

        assertEquals("{\"b\":true}", stringWriter.toString());
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:528: removed method call to writeDeferredName
        M FAIL: com.google.gson.stream.JsonWriter:535: changed return value (areturn)
    */
    public void testValueWriteDeferredNameNumber() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setLenient(true);

        Double x = Double.valueOf(Double.NaN);
        jsonWriter = jsonWriter.beginObject();
        jsonWriter = jsonWriter.name("b").value(x);
        jsonWriter = jsonWriter.endObject();

        assertEquals("{\"b\":NaN}", stringWriter.toString());
    }

    /*  M FAIL: com.google.gson.stream.JsonWriter:456: changed return value (areturn)
        M FAIL: com.google.gson.stream.JsonWriter:468: changed return value (areturn)
        M FAIL: com.google.gson.stream.JsonWriter:500: changed return value (areturn)
    */
  public void testPrettyPrintArrayWithoutIndent() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setIndent("");

    jsonWriter = jsonWriter.beginArray();
    jsonWriter = jsonWriter.value(true);
    jsonWriter = jsonWriter.value(false);
    jsonWriter = jsonWriter.value(5.0);
    jsonWriter = jsonWriter.nullValue();
    jsonWriter = jsonWriter.beginObject();
    jsonWriter = jsonWriter.name("a").value(6.0);
    jsonWriter = jsonWriter.name("b").value(7.0);
    jsonWriter = jsonWriter.endObject();
    jsonWriter = jsonWriter.beginArray();
    jsonWriter = jsonWriter.value(8.0);
    jsonWriter = jsonWriter.value(9.0);
    jsonWriter = jsonWriter.endArray();
    jsonWriter = jsonWriter.endArray();

    String expected = "["
        + "true,"
        + "false,"
        + "5.0,"
        + "null,"
        + "{"
        + "\"a\":6.0,"
        + "\"b\":7.0"
        + "},"
        + "["
        + "8.0,"
        + "9.0"
        + "]"
        + "]";
    assertEquals(expected, stringWriter.toString());
  }
  
    /*
        M FAIL: com.google.gson.stream.JsonWriter:512: changed return value (areturn)
    */
  public void testTopLevelValueTypesReturn() throws IOException {
    StringWriter string3 = new StringWriter();
    JsonWriter writer3 = new JsonWriter(string3);
    writer3 = writer3.value(123);
    writer3.close();
    assertEquals("123", string3.toString());
  }

      /*
        M FAIL: com.google.gson.stream.JsonWriter:415: changed return value (areturn)
    */
  public void testNullStringValueReturn() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter = jsonWriter.beginObject();
    jsonWriter = jsonWriter.name("a");
    jsonWriter = jsonWriter.value((String) null);
    jsonWriter = jsonWriter.endObject();
    assertEquals("{\"a\":null}", stringWriter.toString());
  }

    /*
        M FAIL: com.google.gson.stream.JsonWriter:437: changed return value (areturn)
    */
  public void testJsonValueReturn() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter = jsonWriter.beginObject();
    jsonWriter = jsonWriter.name("a");
    jsonWriter = jsonWriter.jsonValue("{\"b\":true}");
    jsonWriter = jsonWriter.name("c");
    jsonWriter = jsonWriter.value(1);
    jsonWriter = jsonWriter.endObject();
    assertEquals("{\"a\":{\"b\":true},\"c\":1}", stringWriter.toString());
  }

   /*
        M FAIL: com.google.gson.stream.JsonWriter:478: changed return value (areturn)
    */
  public void testBoxedBooleansReturn() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter = jsonWriter.beginArray();
    jsonWriter = jsonWriter.value((Boolean) true);
    jsonWriter = jsonWriter.value((Boolean) false);
    jsonWriter = jsonWriter.value((Boolean) null);
    jsonWriter = jsonWriter.endArray();
    assertEquals("[true,false,null]", stringWriter.toString());
  }

     /*
        M FAIL: com.google.gson.stream.JsonWriter:524: changed return value (areturn)
    */
    public void testValueNumberNullReturn() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        Number x = null;
        jsonWriter = jsonWriter.beginArray();
        jsonWriter = jsonWriter.value(x);
        jsonWriter = jsonWriter.endArray();
        assertEquals("[null]", stringWriter.toString());
      }

       /* M FAIL: com.google.gson.stream.JsonWriter:167: 32 ( ) -> 33 (!)
      *
        o que esta mutação faz é na linha 167: private int[] stack = new int[32];
        em vez de criar a stack com o valor 32, cria a stack com valor 33
        (o valor entre parentesis indica qual os caracters ASCII de 32 - > " " e 33 -> "|")
        Esta alteração de código não vai alterar o resultado final de qualquer operação que se
        faça utilizando a classe JsonWriter.java, visto que quando o valor da stack ultrapassa o seu
        limite inicial, ao ser chamada a função push responsável por meter variáveis na stack, 
        irá duplicar o tamanho da stack o que permitirá meter na stack a quantidade que for precisa.
        O valor do tamanho inicial só irá intervenir com o quão mais rápido será preciso correr o código
        dentro da condição no push
        if (stackSize == stack.length) {
            int[] newStack = new int[stackSize * 2];
            System.arraycopy(stack, 0, newStack, 0, stackSize);
            stack = newStack; 
      */

    /* M FAIL: com.google.gson.stream.JsonWriter:355: 2 -> 3
    *
            a linha de código 355 (int[] newStack = new int[stackSize * 2];) será realizada sempre que a 
            condição (stackSize == stack.length) for
            verdadeira. Isto acontece quando a stack alcançou o seu tamanho máximo. Quando isto acontece
            será duplicado o seu tamanho para que permita receber mais elementos. O que esta mutação faz
            é ao invés de duplicar o seu tamanho, triplica. Isto não alterará a funcionalidade do programa,
            visto que apenas meteria a stack maior do que antes.
    */

   /* M FAIL: com.google.gson.stream.JsonWriter:168: removed assignment to stackSize
        na linha 168, stackSize é criado e inicializado  (private int stackSize = 0;).
        o que esta mutação faz é retirar a sua inicialização. No entanto ao fazer isto, não vai interferir
        em nada com nenhum método da classe JsonWriter.java, porque logo a seguir a stackSize ser instanciada
        vai ser corrido o método (push(EMPTY_DOCUMENT);) que vai acabar sempre com o mesmo resultado: com 
        stackSize = 1, o que torna equivalente esta mutação
   */

     /* M FAIL: com.google.gson.stream.JsonWriter:214: removed assignment to indent
        este mutante é equivalente porque o que ele faz é não inicializar a função indent. No entanto ao
        olharmos para a linha 214 o que é realizado lá é (this.indent = null;) que é examente a mesma coisa
        que se não se inicializa-se a variável indent
   */

   /* M FAIL: com.google.gson.stream.JsonWriter:215: removed assignment to separator
        este mutante é equivalente o que esta linha faz (this.separator = ":";). Ao se retirar a 
        inicialização do this separator, não irá mudar nada pois quando se cria um objecto da classe
        JsonWriter, ele é criado com o valor separator = ":", como podemos ver na linha 182 
        (private String separator = ":";), que é exatamente igual ao que é feito na linha 215 
   */

   /* M FAIL: com.google.gson.stream.JsonWriter:549: removed method call to flush
            flush has the function to write any character previouly saved in the buffer with the 
            method write(). This only happens when the destination is another character or byte
            stream, so only when it is used a Writer, or a OutputStream, will the flush be usefull, 
            which is not the case as it is used the StringWriter. And by achiving nothing by using it
            it proves this mutant is equivalent
   */

   /* M FAIL: com.google.gson.stream.JsonWriter:558: removed method call to close
            what this mutation do in removing the call of (out.close();) out is a StringWriter
            and calling StringWriter.close is useless as it says in the documentation that it
            has no purpose and it is pointless, so this proves this mutant is equivalent
   */

      /*  don't kill any mutant
        increment coverage only
    */
    public void testFlush() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter = jsonWriter.beginArray();
        assertEquals("[", stringWriter.toString());
        jsonWriter.flush();
        jsonWriter = jsonWriter.value("a");
        jsonWriter.flush();
        assertEquals("[\"a\"", stringWriter.toString());
        jsonWriter = jsonWriter.endArray();
        jsonWriter.close();
        assertEquals("[\"a\"]", stringWriter.toString());
        StringBuffer stringWriter2 = stringWriter.getBuffer();
        assertEquals(stringWriter.toString(), stringWriter2.toString());
    }
}        

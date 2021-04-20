package com.google.gson.stream;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;



import static com.google.gson.stream.JsonScope.EMPTY_ARRAY;
import static com.google.gson.stream.JsonScope.NONEMPTY_ARRAY;

public class GSON_PIT_A9_StudentTest  extends TestCase {

    public void testEndArray() throws IOException { //verifica que o endArray não seja null

        StringWriter o = new StringWriter();
        JsonWriter a = new JsonWriter(o);
        a.beginArray();
        Assert.assertNotNull(a.endArray());

    }

    public void testBeginObject() throws IOException { //verifica que o beginObject não seja null

        StringWriter o = new StringWriter();
        JsonWriter a = new JsonWriter(o);
        Assert.assertNotNull(a.beginObject());

    }

    public void testendObject() throws IOException { //verifica que o endObject não seja null

        StringWriter o = new StringWriter();
        JsonWriter a = new JsonWriter(o);
        a.beginObject();
        Assert.assertNotNull(a.endObject());

    }

    public void testvalue() throws IOException {

        //for null strings

        String ab = null;
        StringWriter o = new StringWriter();
        JsonWriter a = new JsonWriter(o);
        //a.beginObject();
        Assert.assertNotNull(a.value(ab)); //para JsonWriter.value

        //for non-null strings

        String bc = "Hello";
        StringWriter f = new StringWriter();
        JsonWriter g = new JsonWriter(f);
        Assert.assertNotNull(g.value(bc));

        //for boolean
        boolean gb = true;
        StringWriter r = new StringWriter();
        JsonWriter b = new JsonWriter(r);
        //a.beginObject();
        Assert.assertNotNull(b.value(gb)); //para JsonWriter.value

        //for nullbools; note Boolean vs boolean types are different
        Boolean bnull = null;
        StringWriter d = new StringWriter();
        JsonWriter jnull = new JsonWriter(d);
        Assert.assertNotNull(jnull.value(bnull));

        //for non-null Bool ; true
        Boolean bnormal = true;
        StringWriter e = new StringWriter();
        JsonWriter jnormal = new JsonWriter(e);
        Assert.assertNotNull(jnormal.value(bnormal));
        assertEquals(false,jnormal.isLenient()); //kills line 242
        //kills variant on line 242
        jnormal.setLenient(true);
        assertEquals(true,jnormal.isLenient());

        assertEquals(false,jnormal.isHtmlSafe()); //kills line 261
        //kills variant on line 261
        jnormal.setHtmlSafe(true);
        assertEquals(true,jnormal.isHtmlSafe());

        assertEquals(true,jnormal.getSerializeNulls()); //kills line 277
        //kills variant on line 277
        jnormal.setSerializeNulls(false);
        assertEquals(false,jnormal.getSerializeNulls());

        //for Number class null

        Number gh = null;
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        Assert.assertNotNull(jsonWriter.value(gh)); //kills 524

        //for Number class not null

        Number hj = 3;
        StringWriter st = new StringWriter();
        JsonWriter jt = new JsonWriter(st);
        Assert.assertNotNull(jt.value(hj)); //kills 535

        //for double values
        double dob = 2.3;
        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);
        Assert.assertNotNull(jw.value(dob)); //kills 500

        //for long values
        long lon = 5L;
        StringWriter sw2 = new StringWriter();
        JsonWriter jw2 = new JsonWriter(sw2);
        Assert.assertNotNull(jw2.value(lon)); //kills 512


    }





    public void testJsonvalue() throws IOException {

        //for null values

        String ab = null;
        StringWriter o = new StringWriter();
        JsonWriter a = new JsonWriter(o);
        //a.beginObject();
        Assert.assertNotNull(a.jsonValue(ab)); //para JsonWriter.jsonValue

        //for non null values

        String bc = "Hello";
        StringWriter f = new StringWriter();
        JsonWriter j = new JsonWriter(f);
        //a.beginObject();
        Assert.assertNotNull(j.jsonValue(bc)); //para JsonWriter.jsonValue



        //for non null values

        String dc = null;
        StringWriter h = new StringWriter();
        JsonWriter js = new JsonWriter(h);
        js.beginObject( );
        js.name("Hello");
        js.setSerializeNulls(false);
        Assert.assertNotNull(js.jsonValue(dc)); //para JsonWriter.jsonValue
        

        }


    public void testvalueKillRemoveCalls() throws IOException { //kills mutant line 480
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        jsonWriter.setIndent("   ");
        Boolean b = true;
        jsonWriter.beginObject();
        jsonWriter.name("a").value(b);
        jsonWriter.endObject();
        assertEquals("{\n" +
                "   \"a\": true\n" +
                "}", stringWriter.toString());

        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);
        jsonWriter.setIndent("   ");
        Number num = 3;
        jw.beginObject();
        jw.name("a").value(num);
        jw.endObject();
        assertEquals("{\"a\":"+"3}", sw.toString()); //kills line 527


        StringWriter ot = new StringWriter(); //kills line 213
        JsonWriter jt = new JsonWriter(ot);
        jt.setIndent("");
        String qq = "Hello";
        jt.beginObject();
        jt.name("o").jsonValue(qq);
        try {
            jt.close();
            //fail();
        }catch(IOException expected){}
        jt.flush();
        assertEquals("{\"o\":Hello", ot.toString());



    }



}

package org.apache.commons.lang.time;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import jdk.nashorn.internal.parser.DateParser;
import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.commons.lang.StringUtils;

public class LANG_JUMBLE_A8_StudentTest extends TestCase {

    private static final long MILLIS_TEST;
    static {
        GregorianCalendar cal = new GregorianCalendar(2000, 6, 5, 4, 3, 2);
        cal.set(Calendar.MILLISECOND, 1);
        MILLIS_TEST = cal.getTime().getTime();
    }

    DateFormat dateTimeParser = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
    DateFormat dateParser = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    TimeZone defaultZone = TimeZone.getDefault();

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LANG_JUMBLE_A8_StudentTest.class);
        // suite.setName("StudentTest Tests");
        return suite;
    }

    public LANG_JUMBLE_A8_StudentTest(String s) {
        super(s);
    }


    //Metodo apoio
    private void assertDate(Date date, int year, int month, int day, int hour, int min, int sec, int mil) throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        assertEquals(year, cal.get(Calendar.YEAR));
        assertEquals(month, cal.get(Calendar.MONTH));
        assertEquals(day, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(hour, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(min, cal.get(Calendar.MINUTE));
        assertEquals(sec, cal.get(Calendar.SECOND));
        assertEquals(mil, cal.get(Calendar.MILLISECOND));
    }

    //Kills 232: 13 -> 14 AND 233: 13 -> 14 (M01 and M02)
    public void testIsSameLocalTime() {
        GregorianCalendar cal1 = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
        GregorianCalendar cal2 = new GregorianCalendar(TimeZone.getTimeZone("GMT-1"));

        cal1.set(2006, 8, 9, 13, 45, 0);
        cal1.set(Calendar.MILLISECOND, 14);
        cal1.set(Calendar.SECOND, 0);

        cal2.set(2006, 8, 9, 13, 45, 0);
        cal2.set(Calendar.MILLISECOND, 14);
        cal2.set(Calendar.SECOND, 0);
        assertTrue(DateUtils.isSameLocalTime(cal1, cal2));
    }


    //Matei - linha 275: -1 -> 1 (M04)
    public void testMutantMethodParseDate() throws ParseException {
        try {
            DateUtils.parseDate("197212AB", new String[]{});
            fail();
        } catch (ParseException ex) {
            assertEquals(ex.getErrorOffset(), -1);
        }
    }


    //M05 - M FAIL: org.apache.commons.lang.time.DateUtils:303: 2 -> 3
    //M06 - M FAIL: org.apache.commons.lang.time.DateUtils:303: changed return value (areturn)
    public void testMutantMethodAddMonths() throws Exception {
        Date base = new Date(MILLIS_TEST);

        Date result = DateUtils.addMonths(base, 1);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 7, 5, 4, 3, 2, 1);

        result = DateUtils.addMonths(base, -1);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 5, 5, 4, 3, 2, 1);
    }

    //M08 - M FAIL: org.apache.commons.lang.time.DateUtils:390: 14 -> 15
    //M09 - M FAIL: org.apache.commons.lang.time.DateUtils:390: changed return value (areturn)
    public void testMutantAddMilliseconds() throws Exception {
        Date base = new Date(MILLIS_TEST);

        Date result = DateUtils.addMilliseconds(base, 1);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 6, 5, 4, 3, 2, 2);
    }



    //M10 - M FAIL: org.apache.commons.lang.time.DateUtils:657: 30 -> 31
    public void testModifyMinute() throws Exception{
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();

        testCalendar.set(2007, 6, 2, 8, 9, 30);
        testCalendar.set(Calendar.MILLISECOND, 320);
        Date date = testCalendar.getTime();

        // done=false; round=true; sec=30 (MATOU o de SEGUNDOS)
        assertEquals("Test round seconds",
                dateTimeParser.parse("July 2, 2007 08:10:00.000"),
                DateUtils.round(date, Calendar.MINUTE));
    }

    //M11 - M FAIL: org.apache.commons.lang.time.DateUtils:667: 30 -> 31
    public void testModifyHour() throws Exception{
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();

        testCalendar.set(2007, 6, 2, 8, 30, 30);
        testCalendar.set(Calendar.MILLISECOND, 320);
        Date date = testCalendar.getTime();

        assertEquals("Test round HOUR",
                dateTimeParser.parse("July 2, 2007 09:00:00.000"),
                DateUtils.round(date, Calendar.HOUR));

    }

    //M16 - M FAIL: org.apache.commons.lang.time.DateUtils:693: 1 -> 0
    public void testModifyRoundAdd0() throws ParseException {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();

        testCalendar.set(2007, 6, 1, 1, 9, 30);
        testCalendar.set(Calendar.MILLISECOND, 320);
        Date date = testCalendar.getTime();
        // Kill line 693: 1 -> 0
        assertEquals("Test round semi month add 1",
                dateTimeParser.parse("July 1, 2007 00:00:00.000"),
                DateUtils.round(date, DateUtils.SEMI_MONTH));
    }

    //M14 - M FAIL: org.apache.commons.lang.time.DateUtils:692: -15 -> -14
    //M15 - M FAIL: org.apache.commons.lang.time.DateUtils:693: 2 -> 3
    public void testModifyRound2() throws ParseException {
        Date date1 = dateTimeParser.parse("February 28, 2002 12:34:56.789");
        // Kill 692: -15 -> -14 AND 693: 2 -> 3
        assertEquals("Test -15 and Month mutant",
                dateParser.parse("March 1, 2002"),
                DateUtils.round(date1, DateUtils.SEMI_MONTH));
    }

    //M13 - M FAIL: org.apache.commons.lang.time.DateUtils:692: 5 -> -1
    public void testModifyRoundError() throws ParseException {
        dateTimeParser.setTimeZone(defaultZone);
        Date date0 = dateTimeParser.parse("February 3, 2002 12:34:56.789");

        try{
            DateUtils.round(date0, DateUtils.SEMI_MONTH);
        }
        catch (ArrayIndexOutOfBoundsException ex){
            fail();
        }
    }


    //M20 - M FAIL: org.apache.commons.lang.time.DateUtils:721: 7 -> 8
    public void testModifyRoundSemiMonth() throws ParseException {
        Date date3 = dateTimeParser.parse("February 24, 2002 12:34:56.789");
        assertEquals("line 721: 7 -> 8",
                dateParser.parse("March 1, 2002"),
                DateUtils.round(date3, DateUtils.SEMI_MONTH));
    }

    //M21 - M FAIL: org.apache.commons.lang.time.DateUtils:730: 12 -> 13
    public void testModifyRoundAM_PM() throws ParseException {
        Date dateAmPm1 = dateTimeParser.parse("February 3, 2020 12:10:00.000");
        assertEquals("Test line 730: 12 -> 13",
                dateTimeParser.parse("February 3, 2020 12:00:00.000"),
                DateUtils.round(dateAmPm1, Calendar.AM_PM));
    }


    //M28 - M FAIL: org.apache.commons.lang.time.DateUtils:73: 2 -> 3
    //M32 - M FAIL: org.apache.commons.lang.time.DateUtils:73: removed array assignment
    //M35 - M FAIL: org.apache.commons.lang.time.DateUtils:73: removed array assignment
    public void testFieldMonth() throws Exception {
        Date base = new Date(MILLIS_TEST);
        //Date result = DateUtils.add(base, Calendar.YEAR, 0);
        assertEquals("Test fields",
                dateTimeParser.parse("January 1, 2001 0:00:00.000"),
                DateUtils.round(base, Calendar.YEAR));
    }

    //M29 - M FAIL: org.apache.commons.lang.time.DateUtils:73: 3 -> 4
    //M30 - M FAIL: org.apache.commons.lang.time.DateUtils:73: 1 -> 0
    //M33 - M FAIL: org.apache.commons.lang.time.DateUtils:73: 2 -> 3
    //M34 - M FAIL: org.apache.commons.lang.time.DateUtils:73: 0 -> 1
    public void testFieldEra() throws Exception {
        Date dateAmPm1 = dateTimeParser.parse("June 5, 2020 4:03:02.000");
        assertEquals("Test fields",
                dateTimeParser.parse("January 1, 1 0:00:00.000"),
                DateUtils.round(dateAmPm1, Calendar.ERA));
    }


    //M38 - M FAIL: org.apache.commons.lang.time.DateUtils:258: CP[14] "Date and Patterns must not be null" -> "___jumble___"
    public void testCP14() throws Exception {
        try {
            DateUtils.parseDate(null, null);
            fail();
        } catch (ParseException ex) {}
        catch (IllegalArgumentException ex) {
            assertTrue(StringUtils.equals("Date and Patterns must not be null", ex.getMessage()));
        }
    }

    //M39 - M FAIL: org.apache.commons.lang.time.DateUtils:275: CP[27] "Unable to parse the date: " -> "___jumble___"
    public void testCP27() throws Exception {
        try {
            DateUtils.parseDate("197212AB",  new String[]{});
            fail();
        } catch (ParseException ex) {
            assertTrue(ex.getMessage().startsWith("Unable to parse the date: "));
        }
    }


    //M37 - M FAIL: org.apache.commons.lang.time.DateUtils:147: CP[4] "The date must not be null" -> "___jumble___"
    public void testCP04() throws Exception {
        try{
            DateUtils.isSameDay((Date) null, null);
            fail();}
        catch (IllegalArgumentException ex){
            assertTrue(StringUtils.equals("The date must not be null", ex.getMessage()));
        }
    }

    //M40 - M FAIL: org.apache.commons.lang.time.DateUtils:529: CP[39] "Could not round " -> "___jumble___"
    public void testCP39() throws Exception {
        try{
            DateUtils.round("", Calendar.DAY_OF_MONTH);
            fail();}
        catch (ClassCastException ex){
            assertTrue(ex.getMessage().startsWith("Could not round"));
        }
    }


    //M41 - M FAIL: org.apache.commons.lang.time.DateUtils:614: CP[44] "Could not truncate " -> "___jumble___"
    public void testCP44() throws Exception {
        try{
            DateUtils.truncate("", Calendar.DAY_OF_MONTH);
            fail();}
        catch (ClassCastException ex){
            assertTrue(ex.getMessage().startsWith("Could not truncate"));
        }
    }

    //M42 - M FAIL: org.apache.commons.lang.time.DateUtils:629: CP[47] "Calendar value too large for accurate calculations" -> "___jumble___"
    public void testCP47() throws Exception {
        Date dateEnd=new Date(Long.MAX_VALUE);
        try{
            DateUtils.truncate(dateEnd, Calendar.DATE);
            fail();}
        catch (ArithmeticException ex){
            assertTrue(ex.getMessage().startsWith("Calendar value too large for accurate calculations"));
        }
    }

    //M43 - M FAIL: org.apache.commons.lang.time.DateUtils:751: CP[58] "The field " -> "___jumble___"
    public void testCP58() throws Exception {
        Date base = new Date(MILLIS_TEST);
        try{
            DateUtils.round(base, -9856);
            fail();}
        catch (IllegalArgumentException ex){
            assertTrue(ex.getMessage().startsWith("The field"));
        }
    }

    //M44 - M FAIL: org.apache.commons.lang.time.DateUtils:751: CP[60] " is not supported" -> "___jumble___"
    public void testCP60() throws Exception {
        Date base = new Date(MILLIS_TEST);
        try{
            DateUtils.round(base, -9856);
            fail();}
        catch (IllegalArgumentException ex){
            assertTrue(ex.getMessage().endsWith(" is not supported"));
        }
    }


    //M45 - M FAIL: org.apache.commons.lang.time.DateUtils:863: CP[62] "The range style " -> "___jumble___"
    public void testCP62() throws Exception {
        Date base = new Date(MILLIS_TEST);
        try{
            DateUtils.iterator(base, -9856);
            fail();}
        catch (IllegalArgumentException ex){
            assertTrue(ex.getMessage().startsWith("The range style "));
        }
    }

    //M46 - M FAIL: org.apache.commons.lang.time.DateUtils:863: CP[63] " is not valid." -> "___jumble___"
    public void testCP63() throws Exception {
        Date base = new Date(MILLIS_TEST);
        try{
            DateUtils.iterator(base, -9856);
            fail();}
        catch (IllegalArgumentException ex){
            assertTrue(ex.getMessage().endsWith(" is not valid."));
        }
    }

    //M47 - M FAIL: org.apache.commons.lang.time.DateUtils:914: CP[67] "Could not iterate based on " -> "___jumble___"
    public void testCP67() throws Exception {
        try{
            DateUtils.iterator("", DateUtils.RANGE_WEEK_CENTER);
            fail();}
        catch (ClassCastException ex){
            assertTrue(ex.getMessage().startsWith("Could not iterate based on"));
        }
    }

}


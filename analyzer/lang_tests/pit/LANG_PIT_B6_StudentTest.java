package org.apache.commons.lang.time;

import junit.framework.AssertionFailedError;
import sun.util.calendar.JulianCalendar;


import java.awt.geom.GeneralPath;
import java.text.ParseException;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TimeZone;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.sql.SQLOutput;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TimeZone;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.lang.SystemUtils;

public class LANG_PIT_B6_StudentTest extends TestCase {
    private static final long MILLIS_TEST;
    static {
        GregorianCalendar cal = new GregorianCalendar(2000, 6, 5, 4, 3, 2);
        cal.set(Calendar.MILLISECOND, 1);
        MILLIS_TEST = cal.getTime().getTime();
    }

    DateFormat dateParser = null;
    DateFormat dateTimeParser = null;
    DateFormat timeZoneDateParser = null;
    Date dateAmPm1 = null;
    Date dateAmPm2 = null;
    Date dateAmPm3 = null;
    Date dateAmPm4 = null;
    Date date0 = null;
    Date date1 = null;
    Date date2 = null;
    Date date3 = null;
    Date date4 = null;
    Date date5 = null;
    Date date6 = null;
    Date date7 = null;
    Date date8 = null;
    Calendar calAmPm1 = null;
    Calendar calAmPm2 = null;
    Calendar calAmPm3 = null;
    Calendar calAmPm4 = null;
    Calendar cal1 = null;
    Calendar cal2 = null;
    Calendar cal3 = null;
    Calendar cal4 = null;
    Calendar cal5 = null;
    Calendar cal6 = null;
    Calendar cal7 = null;
    Calendar cal8 = null;
    TimeZone zone = null;
    TimeZone defaultZone = null;

    protected void setUp() throws Exception {
        super.setUp();

        dateParser = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        dateTimeParser = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);

        dateAmPm1 = dateTimeParser.parse("February 3, 2002 01:10:00.000");
        dateAmPm2 = dateTimeParser.parse("February 3, 2002 11:10:00.000");
        dateAmPm3 = dateTimeParser.parse("February 3, 2002 13:10:00.000");
        dateAmPm4 = dateTimeParser.parse("February 3, 2002 19:10:00.000");
        date0 = dateTimeParser.parse("February 3, 2002 12:34:56.789");
        date1 = dateTimeParser.parse("February 12, 2002 12:34:56.789");
        date2 = dateTimeParser.parse("November 18, 2001 1:23:11.321");
        defaultZone = TimeZone.getDefault();
        zone = TimeZone.getTimeZone("MET");
        TimeZone.setDefault(zone);
        dateTimeParser.setTimeZone(zone);
        date3 = dateTimeParser.parse("March 30, 2003 05:30:45.000");
        date4 = dateTimeParser.parse("March 30, 2003 01:10:00.000");
        date5 = dateTimeParser.parse("March 30, 2003 01:40:00.000");
        date6 = dateTimeParser.parse("March 30, 2003 02:10:00.000");
        date7 = dateTimeParser.parse("March 30, 2003 02:40:00.000");
        date8 = dateTimeParser.parse("October 26, 2003 05:30:45.000");
        dateTimeParser.setTimeZone(defaultZone);
        TimeZone.setDefault(defaultZone);
        calAmPm1 = Calendar.getInstance();
        calAmPm1.setTime(dateAmPm1);
        calAmPm2 = Calendar.getInstance();
        calAmPm2.setTime(dateAmPm2);
        calAmPm3 = Calendar.getInstance();
        calAmPm3.setTime(dateAmPm3);
        calAmPm4 = Calendar.getInstance();
        calAmPm4.setTime(dateAmPm4);
        cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        TimeZone.setDefault(zone);
        cal3 = Calendar.getInstance();
        cal3.setTime(date3);
        cal4 = Calendar.getInstance();
        cal4.setTime(date4);
        cal5 = Calendar.getInstance();
        cal5.setTime(date5);
        cal6 = Calendar.getInstance();
        cal6.setTime(date6);
        cal7 = Calendar.getInstance();
        cal7.setTime(date7);
        cal8 = Calendar.getInstance();
        cal8.setTime(date8);
        TimeZone.setDefault(defaultZone);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


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

    /**
     * This checks that this is a 7 element iterator of Calendar objects
     * that are dates (no time), and exactly 1 day spaced after each other.
     */
    private static void assertWeekIterator(Iterator it, Calendar start) {
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.DATE, 6);

        assertWeekIterator(it, start, end);
    }

    /**
     * Convenience method for when working with Date objects
     */
    private static void assertWeekIterator(Iterator it, Date start, Date end) {
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(start);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(end);

        assertWeekIterator(it, calStart, calEnd);
    }

    /**
     * This checks that this is a 7 divisble iterator of Calendar objects
     * that are dates (no time), and exactly 1 day spaced after each other
     * (in addition to the proper start and stop dates)
     */
    private static void assertWeekIterator(Iterator it, Calendar start, Calendar end) {
        Calendar cal = (Calendar) it.next();
        assertEquals("", start, cal, 0);
        Calendar last = null;
        int count = 1;
        while (it.hasNext()) {
            //Check this is just a date (no time component)
            assertEquals("", cal, DateUtils.truncate(cal, Calendar.DATE), 0);

            last = cal;
            cal = (Calendar) it.next();
            count++;

            //Check that this is one day more than the last date
            last.add(Calendar.DATE, 1);
            assertEquals("", last, cal, 0);
        }
        if (count % 7 != 0) {
            throw new AssertionFailedError("There were " + count + " days in this iterator");
        }
        assertEquals("", end, cal, 0);
    }

    /**
     * Used to check that Calendar objects are close enough
     * delta is in milliseconds
     */
    private static void assertEquals(String message, Calendar cal1, Calendar cal2, long delta) {
        if (Math.abs(cal1.getTime().getTime() - cal2.getTime().getTime()) > delta) {
            throw new AssertionFailedError(
                    message + " expected " + cal1.getTime() + " but got " + cal2.getTime());
        }
    }

    public void testRoundLang_641() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 6, 2, 8, 0, 0);
        testCalendar.set(Calendar.MILLISECOND,500);
        Date date = testCalendar.getTime();
        assertEquals("Minute Round Up Failed",
                dateTimeParser.parse("July 2, 2007 08:00:01.000"),
                DateUtils.round(date, Calendar.SECOND));
    }

    public void testRoundLang_650() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 6, 2, 8, 0, 30);
        Date date = testCalendar.getTime();
        assertEquals("Minute Round Up Failed",
                dateTimeParser.parse("July 2, 2007 08:01:00.000"),
                DateUtils.round(date, Calendar.MINUTE));
    }

    public void testRoundLang_659() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 6, 2, 8, 30, 30);
        Date date = testCalendar.getTime();
        assertEquals("Minute Round Up Failed",
                dateTimeParser.parse("July 2, 2007 09:00:00.000"),
                DateUtils.round(date, Calendar.HOUR));
    }

    public void testRoundLang_712() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 2, 23, 8, 30, 30);
        Date date = testCalendar.getTime();

        assertEquals(testCalendar.get(Calendar.DATE) + "",
                dateParser.parse("March 16, 2007"),
                DateUtils.round(date, DateUtils.SEMI_MONTH));
    }


    public void testRoundLang_721() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 2, 17, 8, 30, 30);
        testCalendar.set(Calendar.HOUR_OF_DAY,12);

        Date date = testCalendar.getTime();

        assertEquals("round semimonth-1 failed",
                dateTimeParser.parse("March 17, 2007 12:00:00.000"),
                DateUtils.round(date, Calendar.AM_PM));
    }


    public void testRoundLang_724() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 2, 17, 8, 30, 30);
        testCalendar.set(Calendar.HOUR_OF_DAY,18);

        Date date = testCalendar.getTime();

        assertEquals("round semimonth-1 failed",
                dateTimeParser.parse("March 17, 2007 12:00:00.000"),
                DateUtils.round(date, Calendar.AM_PM));
    }

    //DONE
    public void testAddMonths_299() throws Exception
    {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMonths(base, 2);
        assertNotSame(base, result);
        assertDate(result, 2000, 8, 5, 4, 3, 2, 1);
    }

    //DONE
    public void testAddMilliseconds_383() throws Exception
    {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMilliseconds(base, 600);
        assertNotSame(base, result);
        assertDate(result, 2000, 6, 5, 4, 3, 2, 601);
        //iterator_820();
    }



    /**
     * Due to the program specificities the condition to reach this line is never met. There is no coverage on this line
     * and it is impossible to kill this mutant.
     * @throws Exception
     */
    public void testRoundLang_683() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 7, 16, 0, 0, 0);

        Date date = testCalendar.getTime();
        int max = testCalendar.getActualMaximum(Calendar.MONTH);
        int min = testCalendar.getActualMinimum(Calendar.MONTH);
        assertEquals(max + " " + min + " " + ((max - min) / 2),
                dateTimeParser.parse("August 16, 2007 00:00:00.000"),
                DateUtils.round(date, Calendar.DAY_OF_MONTH));
    }

    /**
     * Equivalent mutant. Applying boundary mutation to >= will result in the same behaviour has the original program.
     * @throws Exception
     */
    public void testRoundLang_708() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 1, 16, 8, 30, 30);
        Date date = testCalendar.getTime();

        assertEquals(testCalendar.get(Calendar.DATE) + "",
                dateParser.parse("February 16, 2007"),
                DateUtils.round(date, DateUtils.SEMI_MONTH));

        Date date1 = dateTimeParser.parse("December 15, 2020 12:34:56.789");
        assertEquals("",
                dateParser.parse("December 16, 2020"),
                DateUtils.round(date1, DateUtils.SEMI_MONTH));

        Date date2 = dateTimeParser.parse("December 1, 2020 12:34:56.789");
        assertEquals("",
                dateParser.parse("December 1, 2020"),
                DateUtils.round(date2, DateUtils.SEMI_MONTH));

        Date date3 = dateTimeParser.parse("December 14, 2020 12:34:56.789");
        assertEquals("",
                dateParser.parse("December 16, 2020"),
                DateUtils.round(date3, DateUtils.SEMI_MONTH));

        Date date4 = dateTimeParser.parse("December 17, 2020 12:34:56.789");
        assertEquals("",
                dateParser.parse("December 16, 2020"),
                DateUtils.round(date4, DateUtils.SEMI_MONTH));
    }


    /**
     * Even on the boundary conditions (HOURS = 11 ==> condition is offset > ((max-min)/2) where max=23; min=0 and
     * offset = HOURS - min = 11) the mutant survives. Probably an equivalent mutant.
     * @throws Exception
     */
    public void testRoundLang_735() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 10, 16, 0, 0, 0);

        Date date = testCalendar.getTime();
        int max = testCalendar.getActualMaximum(Calendar.DATE);
        int min = testCalendar.getActualMinimum(Calendar.DATE);
        assertEquals(max + " " + min + " " + ((max - min) / 2),
                dateTimeParser.parse("November 16, 2007 00:00:00.000"),
                DateUtils.round(date, Calendar.DAY_OF_MONTH));

        Date dec1711 = dateTimeParser.parse("December 17, 2020 11:29:29.499");
        assertEquals("",
                dateParser.parse("December 16, 2020"),
                DateUtils.round(dec1711, DateUtils.SEMI_MONTH));

        Date dec1710 = dateTimeParser.parse("December 17, 2020 10:29:29.499");
        assertEquals("",
                dateParser.parse("December 16, 2020"),
                DateUtils.round(dec1710, DateUtils.SEMI_MONTH));

        Date dec1712 = dateTimeParser.parse("December 17, 2020 12:29:29.499");
        assertEquals("",
                dateParser.parse("December 16, 2020"),
                DateUtils.round(dec1712, DateUtils.SEMI_MONTH));
    }

    public void testIterator_820() throws Exception
    {
        Date d1 = dateParser.parse("January 12, 2021");
        Calendar c1 = Calendar.getInstance();

        c1.setTime(d1);
        Iterator it = DateUtils.iterator(c1, DateUtils.RANGE_MONTH_MONDAY);
        Calendar last = null;
        int count = 0;
        while(it.hasNext()) {
            last = (Calendar) it.next();
            count++;
        }
        assertEquals(count , 35);
        assertEquals(c1.get(Calendar.MONTH), last.get(Calendar.MONTH));
    }

    /**
     * Equivalent => StartCutOf is always 1 the next condition (startCutoff > Calendar.SATURDAY) makes it equal to 1
     * in every case.
     * @throws Exception
     */
    public void testIterator855() throws Exception {
        Calendar cal = Calendar.getInstance();
        Iterator it = DateUtils.iterator(cal, DateUtils.RANGE_WEEK_SUNDAY);
        int count = 0;
        while(it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals(count , 7);
//        assertWeekIterator(it, 7);

    }


    public void testIterator_855() throws Exception
    {
        Date d1 = dateParser.parse("January 12, 2021");
        Calendar c1 = Calendar.getInstance();

        c1.setTime(d1);
        Iterator it = DateUtils.iterator(c1, DateUtils.RANGE_WEEK_CENTER);
        Calendar last = null;
        int count = 0;
        while(it.hasNext()) {
            last = (Calendar) it.next();
            count++;
        }
        assertEquals(count , 7);
        assertEquals(c1.get(Calendar.MONTH), last.get(Calendar.MONTH));
    }


    // Linha 859 => NO WAY

    /**
     * Se dia = Domingo ==> StartCutOff = 1 e a única linha que lhe pode reduzir o valor é a linha 847 que faz com que start
     * CutOff = -2 com este valor a linha 856 coloca o startCutOff = 5 logo não entra na condição da linha 858
     *
     * Se dia = Terça-Feira ==> startCutOff = 2 implica que fique igual a 0 com a soma de «7 fica igual a 7 e nunca será menor que 7
     */

    /**
     * Mutant 859
     * Has startCutoff only gets subtracted by a positive constant or assingned
     * to a an acceptable value between 1 and 7 including, it will never be
     * bigger than 7, except if it enters the 855 condition with the initial value
     * of 1 or more (which is impossible), never passing the 858 condition and resulting
     * in never reaching 859
     */

    //Same has 859
    public void testIterator_861() throws Exception
    {
        Date d1 = dateParser.parse("January 11, 2021");
        Calendar c1 = Calendar.getInstance();

        c1.setTime(d1);
        Iterator it = DateUtils.iterator(c1, DateUtils.RANGE_WEEK_RELATIVE);
        Calendar last = null;
        int count = 0;
        while(it.hasNext()) {
            last = (Calendar) it.next();
            count++;
        }
        assertEquals(count , 7);
        assertEquals(c1.get(Calendar.MONTH), last.get(Calendar.MONTH));
    }
    ///

    /* STRONGER CONFIG
    mudar other params para --outputFormats XML,HTML, --mutators STRONGER
    * >> Generated 238 mutations Killed 222 (93%)
    >> Ran 362 tests (1.52 tests per mutation)
    * */

    // works
    //DONE
    public void testAdd_398() throws Exception
    {
        try {
            DateUtils.add((Date) null, Calendar.MONTH, 2);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testIsSameDay_142() throws Exception
    {
        Date date = new GregorianCalendar(2004, 6, 9, 13, 45).getTime();
        try {
            DateUtils.isSameDay((Date) null, date);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            DateUtils.isSameDay(date, (Date) null);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            DateUtils.isSameDay((Date) null, (Date) null);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testIsSameDay_166() throws Exception
    {
        try {
            DateUtils.isSameDay(null, Calendar.getInstance());
        } catch (IllegalArgumentException ex) {
            try {
                DateUtils.isSameDay(Calendar.getInstance(), null);
            } catch (IllegalArgumentException exp) {}
        }
    }

    public void testIsSameInstant_187() throws Exception
    {
        try {
            DateUtils.isSameInstant(null, new Date());
        } catch (IllegalArgumentException ex) {
            try {
                DateUtils.isSameInstant(new Date(), null);
            } catch (IllegalArgumentException exp) {}
        }
    }

    public void testIsSameInstant_205() throws Exception
    {
        try {
            DateUtils.isSameInstant(null, Calendar.getInstance());
        } catch (IllegalArgumentException ex) {
            try {
                DateUtils.isSameInstant(Calendar.getInstance(), null);
            } catch (IllegalArgumentException exception) {}
        }
    }

    public void testIsSameLocalTime_225() throws Exception
    {
        try {
            DateUtils.isSameLocalTime(null, Calendar.getInstance());
        } catch (IllegalArgumentException ex) {
            try {
                DateUtils.isSameLocalTime(Calendar.getInstance(), null);
            } catch (IllegalArgumentException exception) {}
        }
    }

    /**
     * Equivalent -> truncate sends the same exception
     * @throws Exception
     */
    public void testIterator_805() throws Exception
    {
        try {
            DateUtils.iterator((Calendar) null, DateUtils.RANGE_MONTH_SUNDAY);
        } catch (IllegalArgumentException ex) { }
    }

    /* ALL
     other params
     --outputFormats XML,HTML, --mutators STRONGER,RETURN_VALS,REMOVE_CONDITIONALS,INLINE_CONSTS,CONSTRUCTOR_CALLS,NON_VOID_METHOD_CALLS,REMOVE_INCREMENTS
    >> Generated 636 mutations Killed 570 (90%)
    >> Ran 1004 tests (1.58 tests per mutation)
     */

    public void testIsSameDay_169() throws Exception {
        Date date1 = new GregorianCalendar(2004, 1, 1, 13, 45).getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar1.set(Calendar.ERA, GregorianCalendar.AD);

        Date date2 = new GregorianCalendar(2004, 1, 1, 13, 45).getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar2.set(Calendar.ERA, GregorianCalendar.BC);
        assertEquals(false, DateUtils.isSameDay(calendar1, calendar2));
    }

    public void testIsSameDay_170() throws Exception {
        Date date1 = new GregorianCalendar(2005, 1, 1, 13, 45).getTime();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = new GregorianCalendar(2004, 1, 1, 13, 45).getTime();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals(false, DateUtils.isSameDay(calendar1, calendar2));
    }

    public void testIsSameLocalTime_228() throws Exception {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.29");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 1, 2020 12:29:29.30");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals(false, DateUtils.isSameLocalTime(calendar1, calendar2));
        assertEquals(false, DateUtils.isSameLocalTime(calendar2, calendar1));
    }

    public void testIsSameLocalTime_228_1() throws Exception {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:16.30");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 1, 2020 12:29:16.30");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals(true, DateUtils.isSameLocalTime(calendar1, calendar2));

        Date date3 = dateTimeParser.parse("January 1, 2020 12:29:16.30");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(date3);

        Date date4 = dateTimeParser.parse("January 1, 2020 12:29:15.30");
        Calendar calendar4 = Calendar.getInstance();
        calendar4.setTime(date4);
        assertEquals(false, DateUtils.isSameLocalTime(calendar3, calendar4));
    }

    public void testIsSameLocalTime_229() throws Exception {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 1, 2020 12:29:28.499");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals(false, DateUtils.isSameLocalTime(calendar1, calendar2));
        assertEquals(false, DateUtils.isSameLocalTime(calendar2, calendar1));
    }

    public void testIsSameLocalTime_230() throws Exception {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 1, 2020 12:30:29.499");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals(false, DateUtils.isSameLocalTime(calendar1, calendar2));
    }

    public void testIsSameLocalTime_232() throws Exception {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("March 2, 2020 12:29:29.499");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals(false, DateUtils.isSameLocalTime(calendar1, calendar2));
    }

    public void testIsSameLocalTime_233() throws Exception {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 1, 2021 12:29:29.499");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals(false, DateUtils.isSameLocalTime(calendar1, calendar2));
    }

    public void testIsSameLocalTime_234() throws Exception {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar1.set(Calendar.ERA, GregorianCalendar.AD);

        Date date2 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        calendar2.set(Calendar.ERA, GregorianCalendar.BC);
        assertEquals(false, DateUtils.isSameLocalTime(calendar1, calendar2));
    }

    public class MyAmazingCalendar extends GregorianCalendar {
        public MyAmazingCalendar() {
            super();
        }
    }

    public void testIsSameLocalTime_235() throws Exception {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        MyAmazingCalendar calendar1 = new MyAmazingCalendar();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals(false, DateUtils.isSameLocalTime(calendar1, calendar2));
    }

    //Equivalent. Despite the value set on line 258, the line 265 will set the index back to 0 and the final result is always the same
    public void testParseDate_258() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(1972, 11, 3);
        String[] parsers = new String[] {"yyyy'-'DDD", "yyyy'-'MM'-'dd", "yyyyMMdd"};
        assertEquals(cal.getTime(), DateUtils.parseDate("19721203", parsers));
    }

    public void testParseDate_267_All() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(1972, 11, 3);
        String[] parsers = new String[] {"yyyy'-'DDD", "yyyy'-'MM'-'dd", "yyyyMMdd"};
        try {
            DateUtils.parseDate("", parsers);
            fail();
        } catch (ParseException ex) {
            try {
                DateUtils.parseDate("b19721203a", parsers);
                fail();
            } catch (ParseException exception) {
                assertEquals(cal.getTime(), DateUtils.parseDate("1972-12-03", parsers));
            }

        }
    }

    public void testParseDate_271() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(1972, 11, 3);
        String[] parsers = new String[] {"yyyy'-'DDD", "yyyy'-'MM'-'dd", "yyyyMMdd"};
        try {
            DateUtils.parseDate("", parsers);
        } catch (ParseException ex) {
                assertEquals(-1, ex.getErrorOffset());
        }
    }

    public void testParseDate_271_2() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(1972, 11, 3);
        String[] parsers = new String[] {"yyyy'-'DDD", "yyyy'-'MM'-'dd", "yyyyMMdd"};
        try {
            DateUtils.parseDate("zzz", parsers);
        } catch (ParseException ex) {
            assertEquals("Unable to parse the date: " + "zzz", ex.getMessage());
        }
    }

    //Equivalent: the weeks (month or year) have 7 days so using one or another to add to the date will
    // result in the same final date
    public void testAddWeeks_313() throws Exception {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addWeeks(base, 15);
        assertNotSame(base, result);
        assertDate(result, 2000, 9, 18, 4, 3, 2, 1);
    }

    //Same has the previous method. The only difference would be if we were getting the Day, in that case the
    // days are different, but adding days will always be unitary.
    public void testAddDays_327() throws Exception {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addDays(base, 15);
        assertNotSame(base, result);
        assertDate(result, 2000, 6, 20, 4, 3, 2, 1);
    }

    public void testIterator_901() throws Exception {
        Object focus = new Object();
        try {
            DateUtils.iterator(focus, 1);
            fail();
        } catch (ClassCastException ex) { }
    }

    public void testIterator_904() throws Exception {
        String focus = "Not valid object";
        try {
            DateUtils.iterator(focus, 1);
            fail();
        } catch (ClassCastException ex) {
            assertEquals(ex.getMessage(), "Could not iterate based on " + focus);
        }
    }


    // should work but it does not
    public void testModify_641() throws Exception
    {
        // if (!round || millisecs < 500) -> if (true || millisecs < 500)
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.900");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Date date2 = dateTimeParser.parse("January 1, 2020 12:29:30.000");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals("", calendar2, DateUtils.round(calendar1, Calendar.SECOND));

        // if (!round || millisecs < 500) -> if (!round || true)
        date1 = dateTimeParser.parse("January 1, 2020 12:29:29.600");
        calendar1.setTime(date1);
        date2 = dateTimeParser.parse("January 1, 2020 12:29:30.0");
        calendar2.setTime(date2);
        assertEquals("", calendar2, DateUtils.round(calendar1, Calendar.SECOND));

    }

    // should work but it does not
    public void testModify_650() throws Exception
    {
        // if (!done && (!round || seconds < 30)) ->  if (true && (!round || seconds < 30))
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.000");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Date date2 = dateTimeParser.parse("January 1, 2020 12:29:29.000");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals("", calendar2, DateUtils.truncate(calendar1, Calendar.SECOND));

        // if (!done && (!round || seconds < 30)) ->  if (!done && (true|| seconds < 30))
        date1 = dateTimeParser.parse("January 1, 2020 12:29:50.000");
        calendar1.setTime(date1);
        date2 = dateTimeParser.parse("January 1, 2020 12:30:00.000");
        calendar2.setTime(date2);
        assertEquals("", calendar2, DateUtils.round(calendar1, Calendar.MINUTE));
    }

    // should work but it does not
    public void testModify_659() throws Exception
    {
        // if (!done && (!round || minutes < 30)) ->  if (true && (!round || minutes < 30))
        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:00.000");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Date date2 = dateTimeParser.parse("January 1, 2020 12:29:00.000");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals("", calendar2, DateUtils.round(calendar1, Calendar.MINUTE));
        assertEquals("", calendar2, DateUtils.truncate(calendar1, Calendar.MINUTE));

        // if (!done && (!round || minutes < 30)) ->  if (!done && (true|| minutes < 30))
        date1 = dateTimeParser.parse("January 1, 2020 12:50:00.000");
        calendar1.setTime(date1);
        date2 = dateTimeParser.parse("January 1, 2020 13:00:00.000");
        calendar2.setTime(date2);
        assertEquals("", calendar2, DateUtils.round(calendar1, Calendar.HOUR));
        date2 = dateTimeParser.parse("January 1, 2020 12:00:00.000");
        calendar2.setTime(date2);
        assertEquals("", calendar2, DateUtils.truncate(calendar1, Calendar.HOUR));

    }

    /*
     * mutant 664 is equivalent
     *  if (date.getTime() != time) then the value of date and val are updated to have the value of time
     *  well if date.getTime() == time then it means the values of date and val are already the same of time
     * therefore replacing the condition with true outputs the same values for date and val -> equivalent mutant
     * */

    public void testModify_664() throws Exception
    {
        Date date1 = dateTimeParser.parse("January 1, 2020 12:50:50.600");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 1, 2020 13:00:00.000");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        assertEquals("", calendar2, DateUtils.round(calendar1, Calendar.HOUR));
    }


    /*
     * 680.1 replace if (val.get(Calendar.DATE) == 1) with if (true)
     * this mutant is equivalent since the condition is always true -> same reason for NO COVERAGE in 683 and 684 mutants
     * */

    // replacing >= 15 with >= 16 is the same as replacing it with > 15 which as proven to be equivalent
    public void testModify_708() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 6, 16, 0, 0, 0);
        Date date = testCalendar.getTime();
        assertEquals("Minute Round Up Failed",
                dateTimeParser.parse("July 16, 2007 00:00:00.000"),
                DateUtils.round(date, DateUtils.SEMI_MONTH));
    }

    public void testModify_712() throws Exception
    {
        TimeZone.setDefault(defaultZone);
        dateTimeParser.setTimeZone(defaultZone);
        Calendar testCalendar = Calendar.getInstance();
        testCalendar.set(2007, 6, 9, 0, 0, 0);
        Date date = testCalendar.getTime();
        assertEquals("Minute Round Up Failed",
                dateTimeParser.parse("July 16, 2007 00:00:00.000"),
                DateUtils.round(date, DateUtils.SEMI_MONTH));
    }

    public void testRound_513() throws Exception {

        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 2, 2020 00:00:00.000");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        assertEquals(calendar2.getTime(),
                DateUtils.round((Object) calendar1, Calendar.DATE));

        assertEquals(date2,
                DateUtils.round((Object) date1, Calendar.DATE));

        String date3 = "January 1, 2020 12:29:29.499";

        try {
            DateUtils.round((Object) date3, Calendar.DATE);
        } catch (Exception e) {
            assertEquals(e.getClass().toString(), "class java.lang.ClassCastException");
        }
    }

    /**
     * Every classes extends Object, having a toString() method that is called
     * whenever a parse is made to a String. As so, except in cases where the variable
     * is assigned to one of other than String class, toString is called to make teh parse
     * making mutants on 522 and 607 equivalent
     */

    public void testTruncate_598() throws Exception {

        Date date1 = dateTimeParser.parse("January 1, 2020 12:29:29.499");
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Date date2 = dateTimeParser.parse("January 1, 2020 00:00:00.000");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        assertEquals(calendar2.getTime(),
                DateUtils.truncate((Object) calendar1, Calendar.DATE));

        assertEquals(date2,
                DateUtils.truncate((Object) date1, Calendar.DATE));

        String date3 = "January 1, 2020 12:29:29.499";

        try {
            DateUtils.truncate((Object) date3, Calendar.DATE);
        } catch (Exception e) {
            assertEquals(e.getClass().toString(), "class java.lang.ClassCastException");
        }
    }

    public void testIterator_820_2() throws Exception
    {
        Date d1 = dateParser.parse("February 11, 2021");
        Calendar c1 = Calendar.getInstance();

        c1.setTime(d1);
        Iterator it = DateUtils.iterator(c1, DateUtils.RANGE_MONTH_MONDAY);
        Calendar last = null;
        int count = 0;
        while(it.hasNext()) {
            last = (Calendar) it.next();
            count++;
        }
        assertEquals(count , 28);
        assertEquals(c1.get(Calendar.MONTH), last.get(Calendar.MONTH));
    }

    /**
     * 853 is the same as 522
     * 858 is equivalent, as explained by 859 above
     */

    public void testIterator_868_2() throws Exception
    {
        Date d1 = dateParser.parse("January 1, 2021");
        Calendar c1 = Calendar.getInstance();

        c1.setTime(d1);
        Iterator it = DateUtils.iterator(c1, DateUtils.RANGE_WEEK_CENTER);
        Calendar last = null;
        int count = 0;
        while(it.hasNext()) {
            last = (Calendar) it.next();
            count++;
        }
        assertEquals(count , 7);
        assertEquals(c1.get(Calendar.MONTH), last.get(Calendar.MONTH));
    }

    public void testIterator_871() throws Exception
    {
        Date d1 = dateParser.parse("December 31, 2020");
        Calendar c1 = Calendar.getInstance();

        c1.setTime(d1);
        Iterator it = DateUtils.iterator(c1, DateUtils.RANGE_WEEK_CENTER);
        Calendar last = null;
        int count = 0;
        while(it.hasNext()) {
            last = (Calendar) it.next();
            count++;
        }
        assertEquals(count , 7);
        assertEquals(0, last.get(Calendar.MONTH));
    }

    /*
     * 670 is equivalent. replacing roundup = false with offset = true is the same since this value is always redefined before being read.
     * The only case where this would not happed it would be if the round was made with MILLISECONDS, since it would enter the first condition.
     * This is not a problem since in this case the program returns in a previous line.
     * */


    /*
     * 696 is equivalent. replacing offset = 0 with offset = 1 is the same since this value is always redefined before being read.
     * */


    /*
     * 742 makes no sense. if a stringbuffer is being used because of the concatenation it needs to be converted to string
     * in order to pass it to the exception constructor toString needs to be called...
     * */


    /*
     * 735 is equivalent. Replacing roundUp = offset > ((max - min) / 2) with roundUp = offset > ((max + min) / 2)
     * outputs the same exact final result. THe same reason for mutant 735 replacing with roundUp = offset >= ((max - min) / 2) previously
     * encountered
     * */

}

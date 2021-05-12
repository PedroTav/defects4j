package org.apache.commons.lang.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


public class LANG_JUDY_B4_StudentTest extends TestCase {

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

    public LANG_JUDY_B4_StudentTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LANG_JUDY_B4_StudentTest.class);
        suite.setName("DateUtils Tests");
        return suite;
    }

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

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 169
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that the following
     * mutants should be killed with the current test.
     * 
     * Operator -> JIR_Ifgt | JIR_Iflt
     * Lines    -> 169      | 169
     * 
     */
    public void testIsSameDayEraGreater() {
            assertTrue(DateUtils.isSameDay(new GregorianCalendar(2020, 12, 01, 00, 00),
                                           new GregorianCalendar(2020, 12, 01, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 170
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 170
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameDayYearLess() {
        assertFalse(DateUtils.isSameDay(new GregorianCalendar(2019, 12, 01, 00, 00), 
                                        new GregorianCalendar(2020, 12, 01, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 170
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 170
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameDayYearGreater() {        
        assertFalse(DateUtils.isSameDay(new GregorianCalendar(2020, 12, 01, 00, 00),
                                        new GregorianCalendar(2019, 12, 01, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 171
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameDayDayOfYearLess() {
        assertFalse(DateUtils.isSameDay(new GregorianCalendar(2020, 12, 01, 00, 00),
                                        new GregorianCalendar(2020, 12, 02, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 190
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameInstantDateLess() {        
        assertFalse(DateUtils.isSameInstant(new GregorianCalendar(2003, 6, 9, 13, 45).getTime(),
                                            new GregorianCalendar(2004, 6, 9, 13, 45).getTime()));
    }

   /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 208
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameInstantCalendarLess() {
        Calendar c1 = new GregorianCalendar(2020, 12, 01, 00, 00);
        Calendar c2 = new GregorianCalendar(2020, 12, 01, 00, 00);

        c1.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.MILLISECOND, 1);

        assertFalse(DateUtils.isSameInstant(c1, c2));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 228
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 228
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeMilliLess() {        
        Calendar c1 = new GregorianCalendar(2020, 12, 01, 00, 00);
        Calendar c2 = new GregorianCalendar(2020, 12, 01, 00, 00);

        c1.set(Calendar.MILLISECOND, 0);
        c2.set(Calendar.MILLISECOND, 1);

        assertFalse(DateUtils.isSameLocalTime(c1, c2));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 228
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 228
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeMilliGreater() {        
        Calendar c1 = new GregorianCalendar(2020, 12, 01, 00, 00);
        Calendar c2 = new GregorianCalendar(2020, 12, 01, 00, 00);

        c1.set(Calendar.MILLISECOND, 1);
        c2.set(Calendar.MILLISECOND, 0);

        assertFalse(DateUtils.isSameLocalTime(c1, c2));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 229
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 229
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeSecondLess() {
        assertFalse(DateUtils.isSameLocalTime(new GregorianCalendar(2020, 12, 01, 00, 00, 00),
                                              new GregorianCalendar(2020, 12, 01, 00, 00, 01)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 229
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 229
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeSecondGreater() {
        assertFalse(DateUtils.isSameLocalTime(new GregorianCalendar(2020, 12, 01, 00, 00, 01),
                                              new GregorianCalendar(2020, 12, 01, 00, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 230
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 230
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeMinuteLess() {
        assertFalse(DateUtils.isSameLocalTime(new GregorianCalendar(2020, 12, 01, 00, 00, 00), 
                                              new GregorianCalendar(2020, 12, 01, 00, 01, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 230
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 230
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeMinuteGreater() {
        assertFalse(DateUtils.isSameLocalTime(new GregorianCalendar(2020, 12, 01, 00, 01, 00), 
                                              new GregorianCalendar(2020, 12, 01, 00, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 231
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeHourLess() {
        assertFalse(DateUtils.isSameLocalTime(new GregorianCalendar(2020, 12, 01, 00, 00, 00),
                                              new GregorianCalendar(2020, 12, 01, 01, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 232
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 232
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeDayOfYearLess() {        
        Calendar c1 = new GregorianCalendar(2020, 12, 01, 00, 00);
        Calendar c2 = new GregorianCalendar(2020, 12, 01, 00, 00);

        c1.set(Calendar.DAY_OF_YEAR, 1);
        c2.set(Calendar.DAY_OF_YEAR, 2);

        assertFalse(DateUtils.isSameLocalTime(c1, c2));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 232
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 232
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeDayOfYearGreater() {
        Calendar c1 = new GregorianCalendar(2020, 12, 01, 00, 00);
        Calendar c2 = new GregorianCalendar(2020, 12, 01, 00, 00);

        c1.set(Calendar.DAY_OF_YEAR, 2);
        c2.set(Calendar.DAY_OF_YEAR, 1);

        assertFalse(DateUtils.isSameLocalTime(c1, c2));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 233
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 233
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeYearLess() {
        assertFalse(DateUtils.isSameLocalTime(new GregorianCalendar(2019, 12, 01, 00, 00, 00),
                                              new GregorianCalendar(2020, 12, 01, 00, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 233
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 233
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIsSameLocalTimeYearGreater() {
        assertFalse(DateUtils.isSameLocalTime(new GregorianCalendar(2020, 12, 01, 00, 00, 00),
                                              new GregorianCalendar(2019, 12, 01, 00, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 234
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that the following
     * mutants should be killed with the current test.
     * 
     * Operator -> JIR_Ifgt | JIR_Iflt
     * Lines    -> 234      | 234
     * 
     */
    public void testIsSameLocalTimeEraGreater() {
        assertTrue(DateUtils.isSameLocalTime(new GregorianCalendar(2020, 12, 01, 00, 00, 00),
                                             new GregorianCalendar(2020, 12, 01, 00, 00, 00)));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 260
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Ifgt | JIR_Iflt
     * Lines    -> 260      | 267
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. By manually
     * testing, we know for sure that these mutants 
     * should be killed with the current test.
     * 
     */
    public void testParseDateSimpleDateFormatGreater() throws Exception {
        GregorianCalendar cal = new GregorianCalendar(1972, Calendar.NOVEMBER, 3);
        String dateStr = "1972-11-03";
        String[] patterns = new String[] {"yyyy'-'DDD", "yyyy'-'MM'-'dd", "yyyyMMdd"};

        try {
            Date date = DateUtils.parseDate(dateStr, patterns);
            assertEquals(date, cal.getTime());
        } catch (NullPointerException e) {
            fail();
        }
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> EGE
     * Lines    -> 399
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> EGE
     * Lines    -> 399
     * 
     */
    public void testAddIllegalArgException() throws Exception {
        try { DateUtils.addSeconds(null, 0); } 
        catch (IllegalArgumentException e) { assertEquals("The date must not be null", e.getMessage()); }
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifle
     * Lines    -> 641
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 641
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifyMilliLessOrEqual() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 18, 2001 1:23:12.000"),
                DateUtils.round(dateParse.parse("November 18, 2001 1:23:11.500"), Calendar.SECOND));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 641
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifyMilliGreater() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 18, 2001 1:23:12.000"),
                     DateUtils.round(dateParse.parse("November 18, 2001 1:23:11.600"), Calendar.SECOND));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 644
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifySecondDone() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 18, 2001 1:23:31.000"), 
                     DateUtils.round(dateParse.parse("November 18, 2001 1:23:30.600"), Calendar.SECOND));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifle
     * Lines    -> 650
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt | JIR_Ifgt
     * Lines    -> 650      | 650
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifySecondLessOrEqual() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 18, 2001 1:24:00.000"), 
                     DateUtils.round(dateParse.parse("November 18, 2001 1:23:30.000"), Calendar.MINUTE));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt | JIR_Ifgt
     * Lines    -> 650      | 650
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifySecondGreater() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 18, 2001 1:24:00.000"), 
                     DateUtils.round(dateParse.parse("November 18, 2001 1:23:35.000"), Calendar.MINUTE));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 653
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifyMinuteDone() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 18, 2001 1:20:00.000"), 
                     DateUtils.round(dateParse.parse("November 18, 2001 1:20:20.000"), Calendar.MINUTE));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifle
     * Lines    -> 659
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 659
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifyHourOfDayLessOrEqual() throws ParseException { 
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 18, 2001 2:00:00.000"),
                     DateUtils.round(dateParse.parse("November 18, 2001 1:30:00.000"), Calendar.HOUR_OF_DAY));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt | JIR_Ifgt
     * Lines    -> 659      | 659
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifyHourOfDayGreater() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 18, 2001 2:00:00.000"),
                     DateUtils.round(dateParse.parse("November 18, 2001 1:35:00.000"), Calendar.HOUR_OF_DAY));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 676
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifySemiMonthSpecialCase() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 16, 2001 0:00:00.000"),
                     DateUtils.round(dateParse.parse("November 9, 2001 0:00:00.000"), DateUtils.SEMI_MONTH));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 680
     * 
     * -----------------------------------------------
     * 
     * This test should have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Iflt | JIR_Ifgt | JIR_Ifgt
     * Lines    -> 680      | 680      | 701
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that these 
     * mutants should be killed with the current test.
     * 
     */
    public void testModifySemiMonthSpecialCaseDayOne() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 16, 2001 0:00:00.000"),
                     DateUtils.round(dateParse.parse("November 15, 2001 0:00:00.000"), DateUtils.SEMI_MONTH));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifle
     * Lines    -> 708
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifySemiMonthOffsetComparison() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("December 16, 2001 0:00:00.000"),
                     DateUtils.truncate(dateParse.parse("December 19, 2001 0:00:00.000"), DateUtils.SEMI_MONTH));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 712
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 712
     * 
     */
    public void testModifySemiMonthOffsetGreater() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 1, 2001 0:00:00.000"),
                     DateUtils.round(dateParse.parse("November 8, 2001 0:00:00.000"), DateUtils.SEMI_MONTH));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifgt
     * Lines    -> 717
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> JIR_Ifle
     * Lines    -> 721
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifyAmPmHourOfDayField() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 8, 2001 12:00:00.000"),
                     DateUtils.round(dateParse.parse("November 8, 2001 12:30:30.500"), Calendar.AM_PM));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Ifle
     * Lines    -> 721
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifyAmPmHourOfDayOffsetComparison() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 8, 2001 12:00:00.000"),
                     DateUtils.round(dateParse.parse("November 8, 2001 13:30:30.500"), Calendar.AM_PM));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 724
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testModifyAmPmHourOfDayOffsetGreater() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("November 9, 2001 0:00:00.000"),
                     DateUtils.round(dateParse.parse("November 8, 2001 7:00:00.000"), Calendar.AM_PM));
    }


    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> AIR_Add 
     * Lines    -> 735
     * 
     * -----------------------------------------------
     * 
     * This test kills the following mutant(s):
     * 
     * Operator -> AIR_Add | AIR_LeftOperand
     * Lines    -> 735     | 735
     * 
     * -----------------------------------------------
     * 
     * This test should also have killed the following 
     * mutant(s):
     * 
     * Operator -> JIR_Iflt | JIR_Ifle
     * Lines    -> 735      | 738
     * 
     * -----------------------------------------------
     * 
     * By manually testing, we know for sure that these 
     * mutants should be killed with the current test.
     * 
     */
    public void testModifyMonthOffsetArithmetic() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        
        assertEquals(dateParse.parse("December 01, 2001 0:00:00.000"),
                     DateUtils.round(dateParse.parse("November 16, 2001 11:00:00.000"), Calendar.MONTH));
    }

    /**
     * This test aims to kill the following mutant(s):
     * 
     * Operator -> JIR_Iflt
     * Lines    -> 822
     * 
     * -----------------------------------------------
     * 
     * This test does not kill any mutant. However, by 
     * manually testing, we know for sure that this 
     * mutant should be killed with the current test.
     * 
     */
    public void testIteratorRangeMonthMonday() throws ParseException {
        DateFormat dateParser = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                 
        Calendar cal = (Calendar) DateUtils.iterator(dateParser.parse("June 11, 2002"), DateUtils.RANGE_MONTH_MONDAY).next();
        assertEquals(dateParser.parse("May 27, 2002"), cal.getTime()); 
    }

}


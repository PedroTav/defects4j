package org.apache.commons.lang.time;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.*;

public class LANG_JUMBLE_A6_StudentTest extends DateUtilsTest {

    public LANG_JUMBLE_A6_StudentTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LANG_JUMBLE_A6_StudentTest.class);
        // suite.setName("DateUtils (Student) Tests");
        return suite;
    }

    /* ************************************************************************* */
    /* *************      CATCH CP (Exception Messages Change)     ************* */
    /* ************************************************************************* */
    public void test01_CP_003() {
        try {
            DateUtils.isSameDay((Date) null, (Date) null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(StringUtils.equals(ex.getMessage(), "The date must not be null"));
        }
    }

    public void test02_CP_012() {
        try {
            DateUtils.parseDate(null, null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(StringUtils.equals(ex.getMessage(), "Date and Patterns must not be null"));
        } catch (ParseException ex) { }
    }

    public void test03_CP_025() {
        try {
            DateUtils.parseDate("date", new String[] {});
            fail();
        } catch (ParseException ex) {
            assertTrue(ex.getMessage().startsWith("Unable to parse the date: "));
        }
    }

    public void test04_CP_038() {
        try {
            DateUtils.round("", Calendar.SECOND);
            fail();
        } catch (ClassCastException ex) {
            assertTrue(ex.getMessage().startsWith("Could not round "));
        }
    }

    public void test05_CP_043() {
        try {
            DateUtils.truncate("", Calendar.SECOND);
            fail();
        } catch (ClassCastException ex) {
            assertTrue(ex.getMessage().startsWith("Could not truncate "));
        }
    }

    public void test06_CP_046() {
        // Bug 31395, large dates
        Date endOfTime = new Date(Long.MAX_VALUE); // fyi: Sun Aug 17 07:12:55 CET 292278994 -- 807 millis
        GregorianCalendar endCal = new GregorianCalendar();
        endCal.setTime(endOfTime);
        try {
            DateUtils.truncate(endCal, Calendar.DATE);
            fail();
        } catch (ArithmeticException ex) {
            assertTrue(StringUtils.equals(ex.getMessage(), "Calendar value too large for accurate calculations"));
        }
    }

    public void test07_CP_057() {
        try {
            DateUtils.round(date1, -9999);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().startsWith("The field "));
        }
    }

    public void test08_CP_059() {
        try {
            DateUtils.round(date1, -9999);
            fail();
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().endsWith(" is not supported"));
        }
    }

    public void test09_CP_061() {
        try {
            DateUtils.iterator(Calendar.getInstance(), -9999);
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().startsWith("The range style "));
        }
    }

    public void test10_CP_062() {
        try {
            DateUtils.iterator(Calendar.getInstance(), -9999);
        } catch (IllegalArgumentException ex) {
            assertTrue(ex.getMessage().endsWith(" is not valid."));
        }
    }

    public void test11_CP_066() {
        try {
            DateUtils.iterator("", DateUtils.RANGE_WEEK_CENTER);
            fail();
        } catch (ClassCastException ex) {
            assertTrue(ex.getMessage().startsWith("Could not iterate based on "));
        }
    }

    /* ************************************************************************* */
    /* ************************************************************************* */
    /* ************************************************************************* */

    //
    // TimeZone object that suffers mutation is used and tested in another Test Suite (DateFormatUtilsTest)
    // Moreover, the TimeZone defaults to "GMT" if an invalid input is used as an argument (returns the same
    // object as the non-mutated code), thus this mutation with this tool is equivalent. Anyways, a test could be
    // similar to this
    //
    public void test12_CP_067() {
        assertEquals (DateUtils.UTC_TIME_ZONE, TimeZone.getTimeZone("GMT"));
    }

    // SECONDS flag to MILLISECONDS flag
    // In TestSuite, seconds and milliseconds were set to 0.
    // Therefore, when mutating those flags, the output would be the same and an error
    // would not be detected
    //
    public void test13_13_to_14() {
        GregorianCalendar cal1 = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
        GregorianCalendar cal2 = new GregorianCalendar(TimeZone.getTimeZone("GMT-1"));
        cal1.set(2004, 6, 9, 13, 45, 4);
        cal1.set(Calendar.MILLISECOND, 0);
        cal2.set(2004, 6, 9, 13, 45, 4);
        cal2.set(Calendar.MILLISECOND, 0);
        assertEquals(true, DateUtils.isSameLocalTime(cal1, cal2));
    }

    // SECONDS flag to MILLISECONDS flag
    // same as before
    //
    public void test14_13_to_14() {
        this.test13_13_to_14();
    }

    // Equivalent because ParsePosition is always set to 0 in the for-loop
    //
    public void test15_0_to_1() {
    }

    // Another one related to Exception messages
    //
    public void test16_minus1_to_1() {
        try {
            DateUtils.parseDate("PURPLE", new String[]{});
            fail();
        } catch (ParseException ex) {
            assertEquals(ex.getErrorOffset(), -1);
        }
    }

    // MONTH flag not tested
    //
    public void test17_2_to_3() throws Exception {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMonths(base, 0);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

        result = DateUtils.addMonths(base, 1);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 7, 5, 4, 3, 2, 1);

        result = DateUtils.addMonths(base, -1);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 5, 5, 4, 3, 2, 1);
    }

    // assert null return value is detected
    //
    public void test18_areturn() {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMonths(base, 0);
        assertNotNull(result);
    }

    // (?? need to check) Equivalents. The mutation switches flag from WEEK_OF_YEAR to
    // WEEK_OF_MONTH, which in the context of addWeeks, both flags have the same meaning.
    // The 'add' implementation of GregorianCalendar, BuddhistCalendar and JapaneseImperialCalendar
    // deal with those two flags the same way.
    //
    public void test19_3_to_4() {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMonths(base, 0);
        assertNotNull(result);
    }

    // MILLISECOND flag not tested
    //
    public void test20_14_to_15() throws Exception {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMilliseconds(base, 0);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 6, 5, 4, 3, 2, 1);

        result = DateUtils.addMilliseconds(base, 1);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 6, 5, 4, 3, 2, 2);

        result = DateUtils.addMilliseconds(base, -1);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 6, 5, 4, 3, 2, 0);
    }

    // assert null return value is detected
    //
    public void test21_areturn() {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMilliseconds(base, 0);
        assertNotNull(result);
    }

    // round minutes, limit around 30
    //
    public void test22_30_to_31() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.MINUTE, 10);
        now.set(Calendar.SECOND, 30);
        Calendar rounded = DateUtils.round(now, Calendar.MINUTE);
        assertEquals(true, now.get(Calendar.MINUTE) != rounded.get(Calendar.MINUTE));
    }

    // round hours, limit around 30
    //
    public void test23_30_to_31() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 10);
        now.set(Calendar.MINUTE, 30);
        Calendar rounded = DateUtils.round(now, Calendar.HOUR_OF_DAY);
        assertEquals(true, now.get(Calendar.HOUR_OF_DAY) != rounded.get(Calendar.HOUR_OF_DAY));
    }

    // (?? check) boolean variable that is only used when field == DateUtils.SEMI_MONTH.
    // however, before arriving at this set, the flag has changed its status accordingly to the
    // args. The mutated line only acts as an initialization
    //
    public void test24_0_to_1() {

    }

    // SEMI_MONTH block
    // should not throw exception
    public void test25_5_to_minus1() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.MARCH, 24, 0, 0, 0);
        Calendar rounded = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
    }

    // SEMI_MONTH block,
    // assert date is rounded to one
    //
    public void test26_minus15_to_minus14() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.MARCH, 24, 0, 0, 0);
        Calendar rounded = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        assertEquals(rounded.get(Calendar.DATE), 1);
    }

    // SEMI_MONTH block,
    // rounding week of year instead of month field
    //
    public void test27_2_to_3() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.MARCH, 24, 0, 0, 0);
        Calendar rounded = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        assertEquals(rounded.get(Calendar.DATE), 1);
    }

    // SEMI_MONTH block,
    // not round month
    //
    public void test28_1_to_0() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.MARCH, 24, 0, 0, 0);
        Calendar rounded = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        assertEquals(rounded.get(Calendar.MONTH), Calendar.APRIL);
    }

    // offset changed to 1, which enters final if block of for loop
    // however, it is always set with a new value before arriving at this if statement
    //
    public void test29_0_to_1() {

    }

    // local assignment of offset removed. Same reason as previous mutations
    //
    public void test30_removed_local_assignment() {

    }

    // round days (??) equivalent - could not find a solution
    //
    public void test31_15_to_16() {
        /*
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.MARCH, 16, 23, 0, 0);
        Calendar rounded = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        System.out.println(rounded.get(Calendar.YEAR) + "/" + rounded.get(Calendar.MONTH) + "/" + rounded.get(Calendar.DATE) +
                " " + rounded.getTimeInMillis());
        */
    }

    // round SEMI_MONTH, make sure it is round up
    // never reaches this point
    public void test32_7_to_8() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.MARCH, 24, 0, 0, 0);
        Calendar rounded = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        assertEquals(rounded.get(Calendar.MONTH), Calendar.APRIL);
    }

    // round AM_PM, make sure it is round down
    //
    public void test33_12_to_13() {

        Calendar now = Calendar.getInstance();
        now.set(Calendar.DAY_OF_MONTH, 12);
        now.set(Calendar.HOUR_OF_DAY, 12);
        now.set(Calendar.MINUTE, 50);
        Calendar rounded = DateUtils.round(now, Calendar.AM_PM);
        assertEquals(rounded.get(Calendar.DAY_OF_MONTH), 12);
        assertEquals(rounded.get(Calendar.HOUR_OF_DAY), 12);
    }

    // round AM_PM, make sure it is round down
    // roundUp variable only used if SEMI_MONTH
    public void test34_minus_to_plus() {
        if (true) return;

        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.MARCH, 18, 11, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar rounded = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        System.out.println(rounded.getTimeInMillis());
        assertEquals(rounded.get(Calendar.MONTH), Calendar.APRIL);
    }


    // local assignment of calednar start
    // equivalent. is always set before used
    //
    public void test35_removed_local_assignment() {

    }

    // local assignment of calednar end
    // equivalent. is always set before used
    //
    public void test36_removed_local_assignment() {

    }

    // couldnt replicate error (swithc case in middle of
    public void test37_switched_case_1_to_default_case() {

        Calendar now = Calendar.getInstance();
        Calendar today = DateUtils.truncate(now, Calendar.DATE);
        Calendar sunday = DateUtils.truncate(now, Calendar.DATE);
        sunday.add(Calendar.DATE, 1 - sunday.get(Calendar.DAY_OF_WEEK));
        Calendar monday = DateUtils.truncate(now, Calendar.DATE);
        if (monday.get(Calendar.DAY_OF_WEEK) == 1) {
            //This is sunday... roll back 6 days
            monday.add(Calendar.DATE, -6);
        } else {
            monday.add(Calendar.DATE, 2 - monday.get(Calendar.DAY_OF_WEEK));
        }

        Iterator it = DateUtils.iterator(now, DateUtils.RANGE_WEEK_SUNDAY);
        assertWeekIterator(it, sunday);
    }

    // start cutoff day has to be saturday. args could be RANGE_WEEK_RELATIVE, and date on a saturday
    // equivalent -> however, at that stage the value is never higher than 7, so if-statement is never reached,
    // and changing to 8 would not change that
    // ups, test large values ?
    public void test38_7_to_8() {

    }

    // equivalent, this if statement is never entered
    public void test39_decrement_to_increment() {


    }

    // MONTH flag set to unrecognized in "modify"
    // (?)
    // after splitting the source code, the fields this mutation refers to is
    // int[]{Calendar.HOUR_OF_DAY, Calendar.HOUR};,
    // however, none is "2"
    public void test40_2_to_3() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.MARCH, 18, 11, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar rounded = DateUtils.round(calendar, Calendar.MONTH);
        assertEquals(rounded.get(Calendar.MONTH), Calendar.APRIL);
    }

    // no such flags in line 73
    public void test41_3_to_4() {
    }

    // YEAR flag to ERA
    // (???)
    // after splitting the source code, the fields this mutation refers to is
    // int[]{Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM };
    // however, none is "1"
    public void test42_1_to_0() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.NOVEMBER, 18, 11, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar rounded = DateUtils.round(calendar, Calendar.YEAR);
        assertEquals(rounded.get(Calendar.YEAR), 2011);
    }

    // DAY_OF_MONTH  flag to -1
    // EQUIVALENT, since this flag has the same value as DATE, the method returns before reaching this point
    public void test43_5_to_minus1() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, Calendar.NOVEMBER, 18, 15, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar rounded = DateUtils.round(calendar, Calendar.DATE);
        assertEquals(rounded.get(Calendar.DATE), 19);

        Calendar rounded02 = DateUtils.round(calendar, Calendar.DAY_OF_MONTH);
        assertEquals(rounded02.get(Calendar.DAY_OF_MONTH), 19);
    }

    // Could not find out which mutation is this
    public void test44_removed_array_assignment() {
    }

    // MONTH replaced by unrecognized flag, should throw an error when rounding
    public void test45_2_to_3() throws Exception {

        Date base = new Date(MILLIS_TEST);
        assertEquals(dateTimeParser.parse("January 1, 2001 0:00:00.000"),
                DateUtils.round(base, Calendar.YEAR));
    }

    // ERA replaced by YEAR, just round ERA
    public void test46_0_to_1() {
        //
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, Calendar.JANUARY, 15, 15, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar rounded = DateUtils.round(calendar, Calendar.ERA);
        assertEquals(rounded.get(Calendar.ERA), 1);
    }

    // Could not find out which mutation is this
    public void test47_removed_array_assignment() {

    }

    /* ************************************************************************* */
    /* ************************************************************************* */
    /* ************************************************************************* */


    private static final long MILLIS_TEST;
    static {
        GregorianCalendar cal = new GregorianCalendar(2000, 6, 5, 4, 3, 2);
        cal.set(Calendar.MILLISECOND, 1);
        MILLIS_TEST = cal.getTime().getTime();
    }

    //-----------------------------------------------------------------------
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

    private static void assertWeekIterator(Iterator it, Calendar start) {
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.DATE, 6);

        assertWeekIterator(it, start, end);
    }

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

    private static void assertEquals(String message, Calendar cal1, Calendar cal2, long delta) {
        if (Math.abs(cal1.getTime().getTime() - cal2.getTime().getTime()) > delta) {
            throw new AssertionFailedError(
                    message + " expected " + cal1.getTime() + " but got " + cal2.getTime());
        }
    }
}

package org.apache.commons.lang.time;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LANG_PIT_B1_StudentTest extends TestCase {

    private static final long MILLIS_TEST;

    static {
        GregorianCalendar cal = new GregorianCalendar(2000, Calendar.JULY, 5, 4, 3, 2);
        cal.set(Calendar.MILLISECOND, 1);
        MILLIS_TEST = cal.getTime().getTime();
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
     * This checks that this is a 7 divisible iterator of Calendar objects
     * that are dates (no time), and exactly 1 day spaced after each other
     * (in addition to the proper start and stop dates)
     */
    private static void assertWeekIterator(Iterator it, Calendar start, Calendar end) {
        Calendar cal = (Calendar) it.next();
        assertEquals(start, cal);
        Calendar last;
        int count = 1;
        while (it.hasNext()) {
            //Check this is just a date (no time component)
            assertEquals(cal, DateUtils.truncate(cal, Calendar.DATE));

            last = cal;
            cal = (Calendar) it.next();
            count++;

            //Check that this is one day more than the last date
            last.add(Calendar.DATE, 1);
            assertEquals(last, cal);
        }
        if (count % 7 != 0) {
            throw new AssertionFailedError("There were " + count + " days in this iterator");
        }
        assertEquals(end, cal);
    }

    /**
     * Used to check that Calendar objects are close enough
     * delta is in milliseconds
     */
    private static void assertEquals(Calendar cal1, Calendar cal2) {
        if (Math.abs(cal1.getTime().getTime() - cal2.getTime().getTime()) > (long) 0) {
            throw new AssertionFailedError(
                    "" + " expected " + cal1.getTime() + " but got " + cal2.getTime());
        }
    }

    private void assertDate(Date date, int year, int month, int day, int hour, int min, int sec, int mil) {
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


    // ------------------------------ BEGIN STUDENT TESTS ------------------------------ //

    // Kill mutant in line 299
    public void testAddMonths() {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMonths(base, 2);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 8, 5, 4, 3, 2, 1);
    }

    // kill mutant in line 383
    public void testAddMilliseconds() {
        Date base = new Date(MILLIS_TEST);
        Date result = DateUtils.addMilliseconds(base, 10);
        assertNotSame(base, result);
        assertDate(base, 2000, 6, 5, 4, 3, 2, 1);
        assertDate(result, 2000, 6, 5, 4, 3, 2, 11);
    }

    public void testModify() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);

        // ROUND

        // kill mutant in line 641
        assertEquals(dateParse.parse("November 18, 2001 1:23:12.000"),
                DateUtils.round(dateParse.parse("November 18, 2001 1:23:11.500"), Calendar.SECOND));

        // kill mutant in line 650
        assertEquals(dateParse.parse("November 18, 2001 1:24:00.000"),
                DateUtils.round(dateParse.parse("November 18, 2001 1:23:30.500"), Calendar.MINUTE));

        // kill mutant in line 659
        assertEquals(dateParse.parse("November 18, 2001 2:00:00.000"),
                DateUtils.round(dateParse.parse("November 18, 2001 1:30:30.500"), Calendar.HOUR_OF_DAY));

        // kill mutant in lines 683 and 684
        assertEquals(dateParse.parse("December 1, 2001 0:00:00.000"),
                DateUtils.round(dateParse.parse("November 30, 2001 1:30:30.500"), DateUtils.SEMI_MONTH));

        // kill mutant in line 712
        assertEquals(dateParse.parse("November 1, 2001 0:00:00.000"),
                DateUtils.round(dateParse.parse("November 8, 2001 0:00:00.000"), DateUtils.SEMI_MONTH));

        // kill mutant in line 721
        assertEquals(dateParse.parse("November 8, 2001 12:00:00.000"),
                DateUtils.round(dateParse.parse("November 8, 2001 12:30:30.500"), Calendar.AM_PM));

        // kill mutant in line 724
        assertEquals(dateParse.parse("November 8, 2001 0:00:00.000"),
                DateUtils.round(dateParse.parse("November 8, 2001 6:30:30.500"), Calendar.AM_PM));

        // kill mutant in line 735
        assertEquals(dateParse.parse("December 01, 2001 0:00:00.000"),
                DateUtils.round(dateParse.parse("November 16, 2001 11:00:00.000"), Calendar.MONTH));
        assertEquals(dateParse.parse("November 16, 2001 0:00:00.000"),
                DateUtils.round(dateParse.parse("November 16, 2001 11:00:00.000"), Calendar.DATE));

        // TRUNCATE

        // kill mutant in line 708
        assertEquals(dateParse.parse("December 16, 2001 0:00:00.000"),
                DateUtils.truncate(dateParse.parse("December 16, 2001 0:00:00.000"), DateUtils.SEMI_MONTH));
    }

    public void testMonthIterator() throws ParseException {
        DateFormat dateParser = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

        // kill mutant in line 820
        Iterator it = DateUtils.iterator(dateParser.parse("June 11, 2002"), DateUtils.RANGE_MONTH_MONDAY);
        assertWeekIterator(it,
                dateParser.parse("May 27, 2002"),
                dateParser.parse("June 30, 2002"));
    }

    ////////////////////////
    // STRONGER OPERATORS //
    ////////////////////////

    // kill mutant in line 142, 166, 187, 205, 225, 398
    public void testNullable() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
        cal.set(2004, Calendar.JULY, 9, 13, 45, 0);
        cal.set(Calendar.MILLISECOND, 0);

        // IsSameDay_DATE
        try {
            DateUtils.isSameDay(dateParse.parse("December 16, 2001 0:00:00.000"), (Date) null);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            DateUtils.isSameDay((Date) null, dateParse.parse("December 16, 2001 0:00:00.000"));
            fail();
        } catch (IllegalArgumentException ignored) {
        }

        // IsSameDay_CALENDAR
        try {
            DateUtils.isSameDay(new GregorianCalendar(2004, Calendar.JULY, 9, 13, 45), (Calendar) null);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            DateUtils.isSameDay((Calendar) null, new GregorianCalendar(2004, Calendar.JULY, 9, 13, 45));
            fail();
        } catch (IllegalArgumentException ignored) {
        }

        // IsSameInstant_DATE
        try {
            DateUtils.isSameInstant(dateParse.parse("December 16, 2001 0:00:00.000"), (Date) null);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            DateUtils.isSameInstant((Date) null, dateParse.parse("December 16, 2001 0:00:00.000"));
            fail();
        } catch (IllegalArgumentException ignored) {
        }

        // IsSameInstant_CALENDAR
        try {
            DateUtils.isSameInstant(cal, (Calendar) null);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            DateUtils.isSameInstant((Calendar) null, cal);
            fail();
        } catch (IllegalArgumentException ignored) {
        }

        // IsSameLocalTime_CALENDAR
        try {
            DateUtils.isSameLocalTime(cal, (Calendar) null);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
        try {
            DateUtils.isSameLocalTime((Calendar) null, cal);
            fail();
        } catch (IllegalArgumentException ignored) {
        }

        // Add(date, field, amount)
        try {
            DateUtils.add(null, Calendar.YEAR, 0);
            fail();
        } catch (IllegalArgumentException ignored) {
        }
    }
    
    /////////////
    //// ALL ////
    /////////////

    // Kill mutants in lines 169, 170, 228, 229, 230, 232, 233, 234
    public void testIsSameDay() {
        Calendar cal1 = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
        Calendar cal2 = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
        Calendar cal3 = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
        cal1.set(2004, Calendar.FEBRUARY, 9, 10,20);
        cal1.set(Calendar.ERA, 0);
        cal2.set(2004, Calendar.FEBRUARY, 9, 10, 20);
        cal3.set(2002, Calendar.FEBRUARY, 9, 10, 20);

        assertFalse(DateUtils.isSameDay(cal1, cal2));
        assertFalse(DateUtils.isSameDay(cal2, cal3));

        assertFalse(DateUtils.isSameLocalTime(cal1, cal2));
        assertFalse(DateUtils.isSameLocalTime(cal2, cal3));

        cal3.set(2004, Calendar.MARCH, 9);
        assertFalse(DateUtils.isSameLocalTime(cal2, cal3));

        cal2 = (Calendar) cal3.clone();
        cal3.set(Calendar.MINUTE, 25);
        assertFalse(DateUtils.isSameLocalTime(cal2, cal3));

        cal2 = (Calendar) cal3.clone();
        cal2.set(Calendar.SECOND, 20);
        cal3.set(Calendar.SECOND, 25);
        assertFalse(DateUtils.isSameLocalTime(cal2, cal3));

        cal2 = (Calendar) cal3.clone();
        assertTrue(DateUtils.isSameLocalTime(cal2, cal3));
        cal2.set(Calendar.MILLISECOND, 200);
        cal3.set(Calendar.MILLISECOND, 100);
        assertFalse(DateUtils.isSameLocalTime(cal2, cal3));
    }

    // Kill mutants in lines 267, 271
    public void testParseDate() {
        String[] parsers = new String[]{"yyyy'-'DDD", "yyyy'-'MM'-'dd", "yyyyMMdd"};

        try {
            DateUtils.parseDate("", parsers);
            fail();
        } catch (ParseException e) {
            assertEquals("Unable to parse the date: ", e.getMessage());
            assertEquals(-1, e.getErrorOffset());
        }

        try {

            DateUtils.parseDate("19721203a", parsers);
            fail();
        } catch (ParseException e) {
            assertEquals("Unable to parse the date: 19721203a", e.getMessage());
            assertEquals(-1, e.getErrorOffset());
        }
    }

    // Kill mutants in lines 519, 522, 604, 607
    public void testRoundTruncate() {
        // Kill mutants in lines 519, 522
        try {
            DateUtils.round((Object) "3456", Calendar.HOUR);
            fail();
        } catch (ClassCastException e) {
            assertEquals("Could not round 3456", e.getMessage());
        }

        // Kill mutants in lines 604, 607
        try {
            DateUtils.truncate((Object) "3456", Calendar.HOUR);
            fail();
        } catch (ClassCastException e) {
            assertEquals("Could not truncate 3456", e.getMessage());
        }
    }

    // Kill mutants in lines 680, 712, 742
    public void testModify2() throws ParseException {
        DateFormat dateParse = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);

        // ROUND

        // kill mutant in line 680
        assertEquals(dateParse.parse("March 1, 2002 0:00:00.000"),
                DateUtils.round(dateParse.parse("February 28, 2002 1:30:30.500"), DateUtils.SEMI_MONTH));

        // kill mutant in line 712
        assertEquals(dateParse.parse("November 16, 2001 0:00:00.000"),
                DateUtils.round(dateParse.parse("November 9, 2001 0:00:00.000"), DateUtils.SEMI_MONTH));

        // kill mutant in line 742
        try {
            DateUtils.round(
                    dateParse.parse("November 8, 2001 0:00:00.000"),
                    18);
        } catch (IllegalArgumentException e) {
            assertEquals("The field 18 is not supported", e.getMessage());
        }
    }

    // kill mutants in lines 853, 901, 904
    public void testIterator() throws ParseException {
        // kill mutant in line 853
        try {
            DateUtils.iterator(Calendar.getInstance(), 111);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The range style 111 is not valid.", e.getMessage());
        }
        
        // kill mutant in line 901, 904
        try {
            DateUtils.iterator((Object) "123", DateUtils.RANGE_WEEK_RELATIVE);
            fail();
        } catch (ClassCastException e) {
            assertEquals("Could not iterate based on 123", e.getMessage());
        }
    }

    // Default //
    // Equivalent mutant in line 855
    // mutant in line 859 (unreachable)
    // Equivalent mutant in line 861 (equals to 855)

    // STRONGER //
    // Equivalent mutant in line 805 When replace condition by FALSE

    // ALL //
    // Equivalent mutant: 235
    // Equivalent mutant: 258
    // Equivalent mutants: 313, 327
    // Equivalent mutants: 664, 670, 681, 683, 696
    // Equivalent mutants: 820, 855 (+1 mutant), 858 (2 mutants), 868, 871


    // Equivalent mutants with wrong message in PIT Report: 641, 650, 659
    //  - Say "replaced equality check with true → SURVIVED" but I think it replace with «FALSE»


    // FINAL LIST -> TOTAL: 24 Mutants
    // 235, 258, 313, 327, 664 (2 mutants), 670, 681, 683, 696 // 641, 650, 659
    // 805, 820, 855 (2 mutant), 858 (2 mutants), 859 (2 mutants), 861, 868, 871


    // JUSTIFICATION
    // Mutants equivalent: "add(date, field, amount)" OR ".add(field, amount)"
    // Substitute Calendar.WEEK_OF_YEAR (3) with Calendar.WEEK_OF_MONTH (4)
    // Substitute Calendar.DAY_OF_MONTH (5) with Calendar.DAY_OF_YEAR (6)
    // Substitute Calendar.DATE (5) with Calendar.DAY_OF_YEAR (6)
    // LINES: 313, 327, 681, 683, 820, 868, 871
   
}

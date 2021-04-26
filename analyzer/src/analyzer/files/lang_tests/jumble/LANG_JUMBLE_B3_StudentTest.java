package org.apache.commons.lang.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;

public class LANG_JUMBLE_B3_StudentTest extends TestCase {
    // DateUtils:229: 13 -> 14
    public void testIsSameLocalTime_Student() {
        GregorianCalendar cal1 = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
        GregorianCalendar cal2 = new GregorianCalendar(TimeZone.getTimeZone("GMT-1"));
        cal1.set(2004, 6, 9, 13, 45, 1);
        cal1.set(Calendar.MILLISECOND, 0);
        cal2.set(2004, 6, 9, 13, 45, 1);
        cal2.set(Calendar.MILLISECOND, 0);
        assertEquals(true, DateUtils.isSameLocalTime(cal1, cal2));
    }

    // DateUtils:299: 2 -> 3
    public void testAddMonths_Student() {
        Calendar calendar = new GregorianCalendar(2020, 6, 10, 5, 50, 30);
        Calendar calendar2 = new GregorianCalendar(2020, 7, 10, 5, 50, 30);
        Date date = calendar.getTime();
        Date date2 = calendar2.getTime();
        Date new_date = DateUtils.addMonths(date, 1);

        assertEquals(true, DateUtils.isSameInstant(new_date, date2));
    }

    // DateUtils:383: 14 -> 15
    public void testAddMilliSeconds_Student() {
        Calendar calendar = new GregorianCalendar(2020, 6, 10, 5, 50, 30);
        Calendar calendar2 = new GregorianCalendar(2020, 6, 10, 5, 50, 30);
        calendar.set(Calendar.MILLISECOND, 10);
        calendar2.set(Calendar.MILLISECOND, 20);
        Date date = calendar.getTime();
        Date date2 = calendar2.getTime();
        Date new_date = DateUtils.addMilliseconds(date, 10);

        assertEquals(true, DateUtils.isSameInstant(new_date, date2));
    }

    // DateUtils:650: 30 -> 31
    public void testModifySecond_Student() {
        Calendar calendar = new GregorianCalendar(2020, 6, 10, 5, 10, 30);
        Calendar calendar2 = new GregorianCalendar(2020, 6, 10, 5, 11, 0);

        Date date = calendar.getTime();
        Date date2 = calendar2.getTime();
        Date new_date = DateUtils.round(date, Calendar.MINUTE);

        assertEquals(true, DateUtils.isSameInstant(new_date, date2));
    }

    // DateUtils:659: 30 -> 31
    public void testModifyMinute_Student() {
        Calendar calendar = new GregorianCalendar(2020, 6, 10, 5, 30, 0);
        Calendar calendar2 = new GregorianCalendar(2020, 6, 10, 6, 0, 0);

        Date date = calendar.getTime();
        Date date2 = calendar2.getTime();
        Date new_date = DateUtils.round(date, Calendar.HOUR);

        assertEquals(true, DateUtils.isSameInstant(new_date, date2));
    }

    // DateUtils:683: -15 -> -14
    public void testNegativeField_Student() {
        Calendar calendar = new GregorianCalendar(2020, 10, 25, 20, 40, 50);
        Calendar calendar2 = new GregorianCalendar(2020, 11, 1, 0, 0, 0);

        Calendar new_calendar = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        Date date2 = calendar2.getTime();
        Date new_date = new_calendar.getTime();

        assertEquals(new_date, date2);
    }


    // Line 712 | 7 -> 8
    public void testOffsetComparison_Student() {
        Calendar calendar = new GregorianCalendar(2020, 10, 9, 20, 40, 50);
        Calendar calendar2 = new GregorianCalendar(2020, 10, 16, 0, 0, 0);

        Calendar new_calendar = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        Date date2 = calendar2.getTime();
        Date new_date = new_calendar.getTime();

        assertEquals(new_date, date2);
    }

    // Line 721 | 12 -> 13
    public void testModifiedOffset_Student() {
        Calendar calendar = new GregorianCalendar(2020, 0, 2, 12, 40, 50);
        Calendar calendar2 = new GregorianCalendar(2020, 0, 2, 12, 0, 0);

        Calendar new_calendar = DateUtils.round(calendar, Calendar.AM_PM);
        Date date2 = calendar2.getTime();
        Date new_date = new_calendar.getTime();

        assertEquals(new_date, date2);
    }

    // TODO line 708
    /*
    public void testModifiedOffset_Student() {
        Calendar calendar = new GregorianCalendar(2020, 10, 22, 20, 40, 50);
        Calendar calendar2 = new GregorianCalendar(2020, 10, 1, 0, 0, 0);

        System.out.println("MINE");
        Calendar new_calendar = DateUtils.round(calendar, DateUtils.SEMI_MONTH);
        Date date2 = calendar2.getTime();
        Date new_date = new_calendar.getTime();

        Calendar calendari = new GregorianCalendar(2020, 0, 1, 20, 40, 50);

        for (int i = 0; i < 366; i++) {
            Date datei = calendari.getTime();
            System.out.print(datei + " -> ");

            Calendar new_calendari = DateUtils.round(calendari, DateUtils.SEMI_MONTH);
            Date new_datei = new_calendari.getTime();
            System.out.println(new_datei);

            calendari.add(Calendar.DATE, 1);
        }

        System.out.println(calendar.getTime());
        System.out.println(date2);
        System.out.println(new_date);

        // assertEquals(new_date, date2);
    }
*/
    // Line 735 | - -> +
    public void testMaxMinSum_Student() {
        Calendar calendar = new GregorianCalendar(2020, 6, 17, 12, 30, 30);
        Calendar calendar2 = new GregorianCalendar(2020, 7, 1, 0, 0, 0);

        Calendar new_calendar = DateUtils.round(calendar, Calendar.MONTH);

        Date date2 = calendar2.getTime();
        Date new_date = new_calendar.getTime();

        assertEquals(new_date, date2);
    }

    // DateUtils:834: switched case 1 with default case
    // There is no default case and case 1 only breaks, so it is equivalent

    // DateUtils:858: 7 -> 8 
    // startCutoff ranges from values [-2,7], and the line in question is only accessible when startcutof > 7, which is impossible. The mutation does not modify execution path
    // DateUtils:859: -= -> +=
}

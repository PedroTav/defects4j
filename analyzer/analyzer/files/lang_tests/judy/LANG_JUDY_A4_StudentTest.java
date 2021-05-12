package org.apache.commons.lang.time;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class LANG_JUDY_A4_StudentTest extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LANG_JUDY_A4_StudentTest.class);
        // suite.setName("Student Tests");
        return suite;
    }

    //----------------------------------------------------------------------------

    public void testPNCLine206() {
        try {
            DateUtils.isSameInstant((Calendar) null, (Calendar) null);
        } catch (IllegalArgumentException e) {
            assertEquals("The date must not be null", e.getMessage());
        }
    }

    public void testJIR_IfgtAndJIR_IfltLine235() {
        Calendar first = Calendar.getInstance();
        Calendar second = (Calendar) first.clone();
        boolean result = DateUtils.isSameLocalTime(first, second);
        assertTrue(result);
    }

    public void testJIR_IfltLine196() {
        Date first = new Date();
        Date second = (Date) first.clone();
        boolean result = DateUtils.isSameInstant(first, second);
        assertTrue(result);
    }

    public void testJIR_IfltLine661() {
        //Equivalent because the operator is already lesser than <
    }

    public void testEGELine416() {
        try {
            DateUtils.add(null, 2, 2);
        } catch (IllegalArgumentException e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    public void testJIR_IfgtAndJIR_IfltLine700() {
        Calendar actual = Calendar.getInstance();
        actual.set(Calendar.DATE, 1);
        Calendar result = DateUtils.round(actual, Calendar.HOUR_OF_DAY);
        assertFalse(DateUtils.isSameInstant(actual, result));
    }

    public void testJIR_IfgtLine268() {
        //Equivalent because there is not one case where i<0
    }

    public void testJIR_IfltLine755() {
        //???
    }

    public void testJIR_IfltAndJIR_IfgtLine174() {
        Calendar first = Calendar.getInstance();
        Calendar second = (Calendar) first.clone();
        boolean result = DateUtils.isSameDay(first, second);
        assertTrue(result);
    }

    public void testJIR_IfleLine920() {
        //Equivalent because there is no way to make a test proving that instanceof can't be replaced with <=
    }

    public void testJIR_IfeqLine692() {
        //Can't know where the mutation operator is applied
    }

    public void testJIR_IfltLine670() {
        //Equivalent because the operator is already <
    }

    public void testJIR_IfltLine214() {
        Calendar first = Calendar.getInstance();
        Calendar second = (Calendar) first.clone();
        boolean result = DateUtils.isSameInstant(first, second);
        assertTrue(result);
    }

    public void testJIR_IfltAndJIR_IfleLine537() {
        //Equivalent mutants because instance of can't be replaced with < and <=
    }

    public void testJIR_IfleLine679() {
        Calendar actual = Calendar.getInstance();
        Calendar result = DateUtils.truncate(actual, Calendar.HOUR);
        assertFalse(DateUtils.isSameInstant(actual, result));
    }

    public void testJIR_IfltAndIfleLine623() {
        //Equivalent mutant because instanceof cant be replaced by < and <=
    }

    public void testJIR_IfltLine673() {
        Calendar actual = Calendar.getInstance();
        Calendar result = DateUtils.truncate(actual, Calendar.MINUTE);
        assertFalse(DateUtils.isSameInstant(actual, result));
    }

    public void testJIR_IfgtLine670() {
        Calendar actual = Calendar.getInstance();
        actual.set(Calendar.SECOND, 29);
        Calendar result = DateUtils.truncate(actual, Calendar.MONTH);
        assertFalse(DateUtils.isSameInstant(actual, result));
    }

    public void testJIR_IfgtLine661() {
        Calendar actual = Calendar.getInstance();
        actual.set(Calendar.MILLISECOND, 499);
        Calendar result = DateUtils.round(actual, Calendar.MONTH);
        assertFalse(DateUtils.isSameInstant(actual, result));
    }

    public void testJIR_IfltLine744() {
        Calendar actual = Calendar.getInstance();
        actual.set(Calendar.HOUR_OF_DAY, 7);
        Calendar result = DateUtils.round(actual, Calendar.HOUR_OF_DAY);
        assertFalse(DateUtils.isSameInstant(actual, result));
    }

    public void testJIR_IfleLine684() {
        //Equivalent mutant because you can't never set a date equal to a date.getDate() manually.
    }

    public void testJIR_IfgtLine882() {
        Iterator iterator = DateUtils.iterator(new Date(), DateUtils.RANGE_WEEK_CENTER);
        assertTrue(iterator.hasNext());
    }

    public void testJIR_IfeqLine267() {
        //Mutation operator applied on a for but no specified in what statement.
    }


    public void testAIR_LeftOperandLine755() {
        Calendar actual = Calendar.getInstance();
        Calendar result = DateUtils.round(actual, Calendar.DAY_OF_MONTH);
        assertTrue(result.before(actual));
    }

    public void testJIR_IfltLine696() {
        Calendar actual = Calendar.getInstance();
        Calendar result = DateUtils.round(actual, Calendar.MONTH);
        assertTrue(result.before(actual));
    }

    public void testAIR_AddLine755() {
        Calendar actual = Calendar.getInstance();
        Calendar result = DateUtils.round(actual, Calendar.MONTH);
        assertTrue(result.get(Calendar.DAY_OF_MONTH) != actual.get(Calendar.DAY_OF_MONTH));
    }

    public void testJIR_IfgtLine721() {
        Calendar actual = Calendar.getInstance();
        Calendar result = DateUtils.round(actual, Calendar.MONTH);
        assertTrue(result.before(actual));
    }

    public void testJIR_IfltLine732() {
        Calendar actual = Calendar.getInstance();
        actual.set(Calendar.DATE, 8);
        Calendar result = DateUtils.round(actual, Calendar.HOUR_OF_DAY);
        assertFalse(DateUtils.isSameInstant(actual, result));
    }

    public void testJIR_IfeqLine691() {
        //Can't know where the mutation operator is applied
    }

    public void testJIR_IfgtLine876() {
        Iterator iterator = DateUtils.iterator(new Date(), DateUtils.RANGE_WEEK_CENTER);
        assertTrue(iterator.hasNext());
    }

    public void testJIR_IfleLine535() {
        //Equivalent mutants because instance of can't be replaced with <=
    }

    public void testJIR_IfltLine664() {
        Calendar actual = Calendar.getInstance();
        Calendar result = DateUtils.truncate(actual, Calendar.SECOND);
        assertTrue(result.get(Calendar.SECOND) == actual.get(Calendar.SECOND));
    }

    public void testEOCLine228() {
        Calendar calendar = Calendar.getInstance();
        boolean result = DateUtils.isSameLocalTime(calendar, calendar);
        assertTrue(result);
    }

    public void testJIR_IfeqLine876() {
        Iterator iterator = DateUtils.iterator(new Date(), DateUtils.RANGE_WEEK_CENTER);
        assertTrue(iterator.hasNext());
    }

    public void testJIR_IfleLine695() {
        //Equivalent mutant because operator && can not be replaced by <=
    }

    public void testJIR_IfgtLine737() {
        Calendar actual = Calendar.getInstance();
        Calendar result = DateUtils.truncate(actual, Calendar.AM_PM);
        assertTrue(result.get(Calendar.DATE) == actual.get(Calendar.DATE));
    }

    //-------------------DateUtils$DateIterator------------------------------//

    public void testEOALine945() {
        Calendar start = Calendar.getInstance();
        Calendar end = (Calendar) start.clone();
        DateUtils.DateIterator iterator = new DateUtils.DateIterator(start, end);
        end.set(Calendar.DAY_OF_MONTH, 23);
        assertTrue(iterator.hasNext());
    }

    public void testJIR_IfleLine964() {
        //Equivalent mutant because equals can not be replaced with <= in Calendar objects
    }

    public void testEOALine944() {
        Calendar start = Calendar.getInstance();
        Calendar end = (Calendar) start.clone();
        DateUtils.DateIterator iterator = new DateUtils.DateIterator(start, end);
        start.set(Calendar.DAY_OF_MONTH, 1);
        assertTrue(iterator.hasNext());
    }

}

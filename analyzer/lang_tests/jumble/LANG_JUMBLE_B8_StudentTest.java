package org.apache.commons.lang.time;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
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


public class LANG_JUMBLE_B8_StudentTest extends TestCase {

    public LANG_JUMBLE_B8_StudentTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LANG_JUMBLE_B8_StudentTest.class);
        // suite.setName("Student Tests");
        return suite;
    }

    
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    // Killed
    // Line 143 - Constant Pool String Operator -S
    public void testMutantSameDayException() {
        try {
            DateUtils.isSameDay((Date) null, (Date) null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("The date must not be null", ex.getMessage());
        }
    }

    // Killed
    /**
    * Line 254 - Constant Pool String Operator -S
    * Line 271 - Constant Pool String Operator -S
    * Line 271 - Inline Constants Operator -k 
    */
    
    public void testMutantParseDateExceptions() {
        try {
            DateUtils.parseDate((String) null, (String[]) null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("Date and Patterns must not be null", ex.getMessage());
        } catch (ParseException ex) {}

        try {
            DateUtils.parseDate("------", new String[]{"a"});
            fail();
        } catch (ParseException ex) {
            assertEquals("Unable to parse the date: ------", ex.getMessage());
            assertEquals(-1, ex.getErrorOffset()); // kills line 271 from increment Operator
            
        }catch (IllegalArgumentException ex) {}
    }
    
    // Killed
    // line 522 - Constant Pool String Operator -S 
    public void testMutantRound() {
        try {
            DateUtils.round("------", Calendar.DATE);
            fail();
        } catch (ClassCastException ex) {
            assertEquals("Could not round ------", ex.getMessage());
        }
    }

    // Killed
    // line 607 - Constant Pool String Operator -S
    public void testMutantTruncate() {
        try {
            DateUtils.truncate("------", Calendar.MONDAY);
            fail();
        } catch (ClassCastException ex) {
            assertEquals("Could not truncate ------", ex.getMessage());
        }
    }


    /**
     * modify is a private funtion. Need to call some public methods 
     * that call this private method
     * 
     * Killed: 
     * Line 742 -  Constant Pool String Operator -S
     * Line 742 -  Constant Pool String Operator -S
     * Line 622 - Constant Pool String Operator -S
     * 
     */

    public void testMutantModify() {
        int field = 500000;        
        Calendar stupidYear = Calendar.getInstance();
        stupidYear.set(2007, 6, 2, 8, 8, 30);
        try {
            DateUtils.truncate(stupidYear, field);
            fail();
        }catch(IllegalArgumentException ex){
            assertEquals("The field " + field + " is not supported", ex.getMessage());
        }
        stupidYear.set(Calendar.YEAR, 280000001);
        
        try {
            field = Calendar.MONTH;
            DateUtils.truncate(stupidYear, field);
            fail();
        }catch(ArithmeticException ex){
            assertEquals("Calendar value too large for accurate calculations", ex.getMessage());
        }
           
    }
    
    // Killed:
    // line 853 - Constant Pool String Operator -S
    // line 853 - Constant Pool String Operator -S
    // line 904 - Constant Pool String Operator -S
    public void testMutantIteratorCPS() {

        Calendar calendar = Calendar.getInstance();
        int unknownRange = 8900; 
        try {
            DateUtils.iterator(calendar, unknownRange);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals("The range style " + unknownRange + " is not valid.", ex.getMessage());
        }

        String nonIterableObject = "Non Iterable Object";
        try {
            DateUtils.iterator(nonIterableObject, unknownRange);
            fail();
        } catch (ClassCastException ex) {
            assertEquals("Could not iterate based on " + nonIterableObject, ex.getMessage());
        }
    
    }


    // For some reason, the input on DataUtilsTest that test
    // exactly this are not able to kill the mutant, i.e., two dates 
    // and times exactly the same are not caught
    // Killed:
    // Line 228 - Increment Operator -k
    // Line 228 - Increment Operator -k
    public void testMutanIsSameLocalTime_Cal() {

        GregorianCalendar calendar1 = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"));
        GregorianCalendar calendar2 =  new GregorianCalendar(TimeZone.getTimeZone("GMT-1"));
        

        calendar1.set(2020, 12, 16, 8, 20, 25);
        calendar1.set(Calendar.MILLISECOND, 21);
        calendar2.set(2020, 12, 16, 8, 20, 25);
        calendar2.set(Calendar.MILLISECOND, 21);

        assertEquals(true, DateUtils.isSameLocalTime(calendar1,calendar2));
      
    }


    // Killed:
    // Line 299 - Increment Operator -k
    // Line 299 - Return Value Operator -r 
    public void testMutantAddMonth() {

        GregorianCalendar cal1 = new GregorianCalendar(2020, 3, 21);
        GregorianCalendar cal2 = new GregorianCalendar(2020, 5, 21);

        Date d3 = DateUtils.addMonths(cal1.getTime(), 2);
        GregorianCalendar cal3 = new GregorianCalendar();
        cal3.setTime(d3);
        assertEquals(cal2.get(Calendar.MONTH),cal3.get(Calendar.MONTH));
    }
    
    // Killed: 
    // Line  735 - Binary Arithmetic Operation 

    public void testMutantModifyIncrement() {
        Calendar cal1 = Calendar.getInstance();
        cal1.set(2020, 5, 16, 11, 30, 52);
        cal1.set(Calendar.MILLISECOND, 499);
        Calendar cal3 = DateUtils.round(cal1, Calendar.MONTH);
        assertEquals(6, cal3.get(Calendar.MONTH));
    }

    // Killed: 
    // Line 383 - Return Value Operator -r
    // Line 383 - Inline Constants Operator -k

    public void testMutantAddMilliseconds() {
        GregorianCalendar cal1 = new GregorianCalendar(2020, 12, 16, 8, 5, 12);
        cal1.set(Calendar.MILLISECOND, 400);

        Date date1 = cal1.getTime();
        GregorianCalendar cal2 = new GregorianCalendar();

        Date n_date = DateUtils.addMilliseconds(date1, 500);

        cal2.setTime(n_date);
        assertEquals(900, cal2.get(Calendar.MILLISECOND));
    }

    // Killed:
    // Line 650 - Inline Constants Operator -k
    // Line 659 - Inline Constants Operator -k
    /* Same reason. The round goes wrong if we 
     change the functions used to truncate.
     To round down the truncate functions are used
     To rounfup the next code is used. 
     Changing the middle of the minutes and seconds to 31 (mutant)
     the round up that that should happen will be considered a round down
     and the date is truncated.
    */
    public void testMutantModifyRoundIncrement(){

        Calendar tseconds = Calendar.getInstance();
        tseconds.set(2020, 12, 17, 11, 30, 30);
        tseconds.set(Calendar.MILLISECOND, 600);

        Calendar n = DateUtils.round(tseconds, Calendar.MINUTE);
        Calendar n2 = DateUtils.round(tseconds, Calendar.HOUR_OF_DAY);

        assertEquals(n.get(Calendar.MINUTE), 31);
        assertEquals(n2.get(Calendar.HOUR_OF_DAY), 12);
        assertEquals(n2.get(Calendar.DAY_OF_MONTH), 17);

    }

    /**

    Killed:
    Line 683 - Inline Constants Operator -k
    Line 683 - Inline Constants Operator -k
    Line 684 - Inline Constants Operator -k
    Line 684 - Inline Constants Operator -k
    Line 712 - Inline Constants Operator -k

    */
 public void testMutantRoundSemiMonth(){

        Calendar a = Calendar.getInstance();
        a.set(2020, 2, 9, 11, 30, 30);
        a.set(Calendar.MILLISECOND, 600);
        
        Calendar n2 = DateUtils.round(a,  DateUtils.SEMI_MONTH);

        assertEquals(2, n2.get(Calendar.MONTH));
        assertEquals(16, n2.get(Calendar.DATE));
        assertEquals(2020, n2.get(Calendar.YEAR));

        a.set(2020, 2, 24, 11, 30, 30);

        n2 = DateUtils.round(a,  DateUtils.SEMI_MONTH);
        assertEquals(3, n2.get(Calendar.MONTH));
        assertEquals(1, n2.get(Calendar.DATE));
        assertEquals(2020, n2.get(Calendar.YEAR));



        a.set(2020, 2, 15, 11, 30, 30);

        n2 = DateUtils.round(a,  DateUtils.SEMI_MONTH);
        assertEquals(2, n2.get(Calendar.MONTH));
        assertEquals(16, n2.get(Calendar.DATE));
        assertEquals(2020, n2.get(Calendar.YEAR));

    } 

    // Killed:
    // Line 721 Inline Constants Operator -k
    public void testMutantRoundAMPM(){

        Calendar a = Calendar.getInstance();
        a.set(2020, 2, 9, 12, 30, 30);
        a.set(Calendar.MILLISECOND, 600);
        
        Calendar n2 = DateUtils.round(a, Calendar.AM_PM);

        assertEquals(2, n2.get(Calendar.MONTH));
        assertEquals(9, n2.get(Calendar.DATE));
        assertEquals(2020, n2.get(Calendar.YEAR));
    }   

    

    private void assertDate(Date date, int era, int year, int month, int day, int hour, int min, int sec, int mil) throws Exception {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        assertEquals(year, cal.get(Calendar.YEAR));
        assertEquals(month, cal.get(Calendar.MONTH));
        assertEquals(day, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(hour, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(min, cal.get(Calendar.MINUTE));
        assertEquals(sec, cal.get(Calendar.SECOND));
        assertEquals(mil, cal.get(Calendar.MILLISECOND));
        assertEquals(era, cal.get(Calendar.ERA));

    }


    // Killed:
    // line 73 - Inline Constants Operator -k
    // line 73 - Inline Constants Operator -k
    // line 73 - Inline Constants Operator -k
    // line 73 - Inline Constants Operator -k
    // line 73 - Inline Constants Operator -k
    // line 73 - Inline Constants Operator -k
    public void testMutantLine73(){

        try{
        Calendar a = Calendar.getInstance();
        a.set(2020, 5, 17, 12, 30, 30);
        a.set(Calendar.MILLISECOND, 600);
    
        Calendar n1 = DateUtils.round(a, Calendar.MONTH);        
        Calendar n2 = DateUtils.round(a, Calendar.YEAR);
        Calendar n3 = DateUtils.round(a, Calendar.DATE);
        Calendar n4 = DateUtils.round(a, Calendar.ERA);


        assertDate(n1.getTime(), 1, 2020, 6, 1, 0, 0, 0, 0);
        assertDate(n2.getTime(), 1, 2020, 0, 1, 0, 0, 0, 0);
        assertDate(n3.getTime(), 1, 2020, 5, 18, 0, 0, 0, 0);
        assertDate(n4.getTime(), 1, 1, 0, 1, 0, 0, 0, 0);
        //Calendar n5 = DateUtils.round(a, -1);
        
        } catch(IllegalArgumentException ex){
            assertEquals("The field " + -1 + " is not supported", ex.getMessage());
       } catch(NullPointerException ex){
           fail();
       }catch(Exception ex){
           fail();
       }
    
    }


}


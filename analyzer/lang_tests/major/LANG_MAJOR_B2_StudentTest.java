/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.PrintStream;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.lang.SystemUtils;

/**
 * Unit tests {@link org.apache.commons.lang.time.DateUtils}.
 *
 * @author <a href="mailto:sergek@lokitech.com">Serge Knystautas</a>
 * @author <a href="mailto:steve@mungoknotwise.com">Steven Caswell</a>
 */
public class LANG_MAJOR_B2_StudentTest extends TestCase {
    DateFormat dateParser = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
    DateFormat dateTimeParser = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);

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

    public void writeToLog(String info){
        try {
            FileWriter myWriter = new FileWriter("/root/test_logs.txt", true);
            myWriter.append(info);
            myWriter.append("\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Student Tests

    //1
    public void testSameDay1Null()
    {
        try {
            Date day2 = new Date();
            DateUtils.isSameDay(null, day2);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //2
    public void testSameDay2Null()
    {
        try {
            Date day1 = new Date();
            DateUtils.isSameDay(day1, null);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //4
    public void testSameDayDefault()
    {
        Date day1 = new Date();
        day1.setTime(0);
        Date day2 = new Date();
        boolean res = DateUtils.isSameDay(day1, day2);

        assertEquals(res, false);       
    }

    //8
    public void testSameCalendarDay1Null()
    {
        try {
            Calendar day2 = Calendar.getInstance();
            DateUtils.isSameDay(null, day2);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //9
    public void testSameCalendarDay2Null()
    {
        try {
            Calendar day1 = Calendar.getInstance();
            DateUtils.isSameDay(day1, null);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //15 & 16
    public void testSameEra()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        day1.set(1,1,1);
        day1.set(Calendar.ERA, GregorianCalendar.BC);        
        
        day2.set(1,1,1);
        day2.set(Calendar.ERA, GregorianCalendar.AD);

        res = DateUtils.isSameDay(day1, day2);

        assertEquals(res, false);     

        res = DateUtils.isSameDay(day2, day1);

        assertEquals(res, false);       
    }

    //18 & 19
    public void testSameYear()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        day1.set(1,1,1);
        day1.set(Calendar.YEAR, 1);        
        
        day2.set(1,1,1);
        day2.set(Calendar.YEAR, 2);

        res = DateUtils.isSameDay(day1, day2);

        assertEquals(res, false);     

        res = DateUtils.isSameDay(day2, day1);

        assertEquals(res, false);       
    }

    //21
    public void testEraYearSame()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC);      
        
        day2.set(Calendar.DAY_OF_YEAR, 1);
        day2.set(Calendar.YEAR, 2);
        day2.set(Calendar.ERA, GregorianCalendar.AD);

        res = DateUtils.isSameDay(day1, day2);

        assertEquals(false, res);     
    }

    //26
    public void testSameDayOfYear()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        day1.set(Calendar.DAY_OF_YEAR, 2);
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC);      
        
        day2.set(Calendar.DAY_OF_YEAR, 1);
        day2.set(Calendar.YEAR, 1);
        day2.set(Calendar.ERA, GregorianCalendar.BC);

        res = DateUtils.isSameDay(day1, day2);

        assertEquals(res, false);     
    }

    //32
    public void testSameInstantDay1Null()
    {
        try {
            Date day2 = new Date();
            DateUtils.isSameInstant(null, day2);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //33
    public void testSameInstantDay2Null()
    {
        try {
            Date day1 = new Date();
            DateUtils.isSameInstant(day1, null);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //39
    public void testSameInstantCheckDay1Larger()
    {
        Date day1 = new Date();
        Date day2 = new Date();
        boolean res;

        day1.setTime(2);
        day2.setTime(1);

        res = DateUtils.isSameInstant(day1, day2);  

        assertEquals(res, false);     
    }

    //41
    public void testSameInstantCalendar1Null()
    {
        try {
            Calendar day2 = Calendar.getInstance();
            DateUtils.isSameInstant(null, day2);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //42
    public void testSameInstantCalendar2Null()
    {
        try {
            Calendar day1 = Calendar.getInstance();
            DateUtils.isSameInstant(day1, null);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //48
    public void testSameInstantCheckCalendar1Larger()
    {
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();
        boolean res;

        day1.setTimeInMillis(2);
        day2.setTimeInMillis(1);

        res = DateUtils.isSameInstant(day1, day2);  

        assertEquals(res, false);  
    }

    //50
    public void testSameLocalTimeCalendar1Null()
    {
        try {
            Calendar day2 = Calendar.getInstance();
            DateUtils.isSameLocalTime(null, day2);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //51
    public void testSameLocalTimeCalendar2Null()
    {
        try {
            Calendar day1 = Calendar.getInstance();
            DateUtils.isSameLocalTime(day1, null);
            fail("Error");
        } catch(IllegalArgumentException e) {}        
    }

    //56...
    public void testSameLocalTimeIndividual()
    {
        Calendar day1;
        Calendar day2;
        boolean res;

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.MILLISECOND, 1);
        day2.set(Calendar.MILLISECOND, 2);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, false); 
        res = DateUtils.isSameLocalTime(day2, day1);
        assertEquals(res, false); 

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.SECOND, 1);
        day2.set(Calendar.SECOND, 2);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, false); 
        res = DateUtils.isSameLocalTime(day2, day1);
        assertEquals(res, false);  

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.MINUTE, 1);
        day2.set(Calendar.MINUTE, 2);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, false); 
        res = DateUtils.isSameLocalTime(day2, day1);
        assertEquals(res, false);  

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.HOUR, 1);
        day2.set(Calendar.HOUR, 2);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, false); 
        res = DateUtils.isSameLocalTime(day2, day1);
        assertEquals(res, false);  

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.DAY_OF_YEAR, 1);
        day2.set(Calendar.DAY_OF_YEAR, 2);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, false); 
        res = DateUtils.isSameLocalTime(day2, day1);
        assertEquals(res, false); 

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.YEAR, 1);
        day2.set(Calendar.YEAR, 2);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, false); 
        res = DateUtils.isSameLocalTime(day2, day1);
        assertEquals(res, false); 

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.ERA, GregorianCalendar.AD);
        day2.set(Calendar.ERA, GregorianCalendar.BC);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, false); 
        res = DateUtils.isSameLocalTime(day2, day1);
        assertEquals(res, false);    

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.ERA, GregorianCalendar.AD);
        day2.set(Calendar.ERA, GregorianCalendar.AD);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, true);

        day1 = Calendar.getInstance();
        day2 = Calendar.getInstance();
        day1.set(Calendar.ERA, GregorianCalendar.BC);
        day2.set(Calendar.ERA, GregorianCalendar.BC);

        res = DateUtils.isSameLocalTime(day1, day2);
        assertEquals(res, true);  
    }

    //62
    public void testSameLTMilSecSame()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC); 
        day1.set(Calendar.MINUTE, 1);
        day1.set(Calendar.HOUR, 1);

        day1.set(Calendar.MILLISECOND, 1);
        day1.set(Calendar.SECOND, 1);           
        
        day2.set(Calendar.DAY_OF_YEAR, 2);
        day2.set(Calendar.YEAR, 2);
        day2.set(Calendar.ERA, GregorianCalendar.AD);        
        day2.set(Calendar.MINUTE, 2);
        day2.set(Calendar.HOUR, 2);

        day2.set(Calendar.MILLISECOND, 1);
        day2.set(Calendar.SECOND, 1);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, false);     

        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC); 
        day1.set(Calendar.MINUTE, 1);
        day1.set(Calendar.HOUR, 1);

        day1.set(Calendar.MILLISECOND, 1);
        day1.set(Calendar.SECOND, 1);           
        
        day2.set(Calendar.DAY_OF_YEAR, 1);
        day2.set(Calendar.YEAR, 1);
        day2.set(Calendar.ERA, GregorianCalendar.BC);        
        day2.set(Calendar.MINUTE, 1);
        day2.set(Calendar.HOUR, 1);

        day2.set(Calendar.MILLISECOND, 2);
        day2.set(Calendar.SECOND, 2);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, false);  
    }

    //69
    public void testSameLTMilSecMinSame()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        //Different
        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC); 
        day1.set(Calendar.HOUR, 1);

        //Same
        day1.set(Calendar.MILLISECOND, 1);
        day1.set(Calendar.SECOND, 1);
        day1.set(Calendar.MINUTE, 1);        
        
        //Different
        day2.set(Calendar.DAY_OF_YEAR, 2);
        day2.set(Calendar.YEAR, 2);
        day2.set(Calendar.ERA, GregorianCalendar.AD);        
        day2.set(Calendar.HOUR, 2);

        //Same
        day2.set(Calendar.MILLISECOND, 1);
        day2.set(Calendar.SECOND, 1);
        day2.set(Calendar.MINUTE, 1);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, false);     

        //Same
        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC); 
        day1.set(Calendar.HOUR, 1);

        //Different
        day1.set(Calendar.MILLISECOND, 1);        
        day1.set(Calendar.SECOND, 1);      
        day1.set(Calendar.MINUTE, 1);     
        
        //Same
        day2.set(Calendar.DAY_OF_YEAR, 1);
        day2.set(Calendar.YEAR, 1);
        day2.set(Calendar.ERA, GregorianCalendar.BC);        
        day2.set(Calendar.HOUR, 1);

        //Different
        day2.set(Calendar.MILLISECOND, 2);
        day2.set(Calendar.SECOND, 2);
        day2.set(Calendar.MINUTE, 2);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, false);  
    }

    //76
    public void testSameLTMilSecMinHorSame()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        //Different
        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC); 

        //Same
        day1.set(Calendar.MILLISECOND, 1);
        day1.set(Calendar.SECOND, 1);
        day1.set(Calendar.MINUTE, 1); 
        day1.set(Calendar.HOUR, 1);       
        
        //Different
        day2.set(Calendar.DAY_OF_YEAR, 2);
        day2.set(Calendar.YEAR, 2);
        day2.set(Calendar.ERA, GregorianCalendar.AD);        

        //Same
        day2.set(Calendar.MILLISECOND, 1);
        day2.set(Calendar.SECOND, 1);
        day2.set(Calendar.MINUTE, 1);
        day2.set(Calendar.HOUR, 1);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, false);     

        //Same
        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC); 

        //Different
        day1.set(Calendar.MILLISECOND, 1);        
        day1.set(Calendar.SECOND, 1);      
        day1.set(Calendar.MINUTE, 1);   
        day1.set(Calendar.HOUR, 1);  
        
        //Same
        day2.set(Calendar.DAY_OF_YEAR, 1);
        day2.set(Calendar.YEAR, 1);
        day2.set(Calendar.ERA, GregorianCalendar.BC);        

        //Different
        day2.set(Calendar.MILLISECOND, 2);
        day2.set(Calendar.SECOND, 2);
        day2.set(Calendar.MINUTE, 2);
        day2.set(Calendar.HOUR, 2);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, false);  
    }

    //83
    public void testSameLTMilSecMinHorDaySame()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        //Different
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC); 

        //Same
        day1.set(Calendar.MILLISECOND, 1);
        day1.set(Calendar.SECOND, 1);
        day1.set(Calendar.MINUTE, 1); 
        day1.set(Calendar.HOUR, 1);       
        day1.set(Calendar.DAY_OF_YEAR, 1);
        
        //Different
        day2.set(Calendar.YEAR, 2);
        day2.set(Calendar.ERA, GregorianCalendar.AD);        

        //Same
        day2.set(Calendar.MILLISECOND, 1);
        day2.set(Calendar.SECOND, 1);
        day2.set(Calendar.MINUTE, 1);
        day2.set(Calendar.HOUR, 1);
        day2.set(Calendar.DAY_OF_YEAR, 1);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(false, res);     

        //Same
        day1.set(Calendar.YEAR, 1);
        day1.set(Calendar.ERA, GregorianCalendar.BC); 

        //Different
        day1.set(Calendar.MILLISECOND, 1);        
        day1.set(Calendar.SECOND, 1);      
        day1.set(Calendar.MINUTE, 1);   
        day1.set(Calendar.HOUR, 1);  
        day1.set(Calendar.DAY_OF_YEAR, 1);
        
        //Same
        day2.set(Calendar.YEAR, 1);
        day2.set(Calendar.ERA, GregorianCalendar.BC);        

        //Different
        day2.set(Calendar.MILLISECOND, 2);
        day2.set(Calendar.SECOND, 2);
        day2.set(Calendar.MINUTE, 2);
        day2.set(Calendar.HOUR, 2);
        day2.set(Calendar.DAY_OF_YEAR, 2);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(false, res);   
    }

    //90
    public void testSameLTMilSecMinHorDayYrSame()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        //Different
        day1.set(Calendar.ERA, GregorianCalendar.BC); 

        //Same
        day1.set(Calendar.MILLISECOND, 1);
        day1.set(Calendar.SECOND, 1);
        day1.set(Calendar.MINUTE, 1); 
        day1.set(Calendar.HOUR, 1);       
        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        
        //Different
        day2.set(Calendar.ERA, GregorianCalendar.AD);        

        //Same
        day2.set(Calendar.MILLISECOND, 1);
        day2.set(Calendar.SECOND, 1);
        day2.set(Calendar.MINUTE, 1);
        day2.set(Calendar.HOUR, 1);
        day2.set(Calendar.DAY_OF_YEAR, 1);
        day2.set(Calendar.YEAR, 1);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(false, res);      

        //Same
        day1.set(Calendar.ERA, GregorianCalendar.BC); 

        //Different
        day1.set(Calendar.MILLISECOND, 1);        
        day1.set(Calendar.SECOND, 1);      
        day1.set(Calendar.MINUTE, 1);   
        day1.set(Calendar.HOUR, 1);  
        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);
        
        //Same
        day2.set(Calendar.ERA, GregorianCalendar.BC);        

        //Different
        day2.set(Calendar.MILLISECOND, 2);
        day2.set(Calendar.SECOND, 2);
        day2.set(Calendar.MINUTE, 2);
        day2.set(Calendar.HOUR, 2);
        day2.set(Calendar.DAY_OF_YEAR, 2);
        day2.set(Calendar.YEAR, 2);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(false, res);  
    }

    //95
    public void testSameLocalTimeEra()
    {
        boolean res;
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();

        //Same
        day1.set(Calendar.MILLISECOND, 1);        
        day1.set(Calendar.SECOND, 1);      
        day1.set(Calendar.MINUTE, 1);   
        day1.set(Calendar.HOUR, 1);  
        day1.set(Calendar.DAY_OF_YEAR, 1);
        day1.set(Calendar.YEAR, 1);

        //Same
        day2.set(Calendar.MILLISECOND, 1);
        day2.set(Calendar.SECOND, 1);
        day2.set(Calendar.MINUTE, 1);
        day2.set(Calendar.HOUR, 1);
        day2.set(Calendar.DAY_OF_YEAR, 1);
        day2.set(Calendar.YEAR, 1);

        day1.set(Calendar.ERA, GregorianCalendar.BC);
        day2.set(Calendar.ERA, GregorianCalendar.AD);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, false);     

        res = DateUtils.isSameLocalTime(day2, day1);

        assertEquals(res, false);       

        day1.set(Calendar.ERA, GregorianCalendar.BC); 
        day2.set(Calendar.ERA, GregorianCalendar.BC);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, true);   

        day1.set(Calendar.ERA, GregorianCalendar.AD); 
        day2.set(Calendar.ERA, GregorianCalendar.AD);

        res = DateUtils.isSameLocalTime(day1, day2);

        assertEquals(res, true); 
    }

    //102
    //Equivalent as Calendar.getClass() is the same for cal1 and cal2

    //104
    //Equivalent as Calendar.getClass() is the same for cal1 and cal2

    //108..
    public void testParseDateNull() throws IllegalArgumentException, ParseException
    {
        try {
            DateUtils.parseDate("test", null);
            fail("Error");
        } catch(IllegalArgumentException e) {}

        try {
            String [] test = new String[] {"test"};
            DateUtils.parseDate(null, test);
            fail("Error");
        } catch(IllegalArgumentException e) {}

        try {
            DateUtils.parseDate(null, null);
            fail("Error");
        } catch(IllegalArgumentException e) {}
    }

    //112 and 113
    /*
        Equivalent as pos.SetIndex is called before pos is used with the mutated value
    */

    //116
    /*
        Equivalent as incrementing i until i < parsePatterns.length is equivalent to i != parsePatterns.length
    */ 

    //121
    /*
        Equivalent as i starts with value 0 and only gets incremented so i == 0 is the same as i <= 0
    */

    //131
    /*
        Equivalent as SimpleDateFormat.parse only returns null to Date if text or pos provided are null which never occurs (text is verified for null at the start of the method)
    */

    //133 
    // Equivalent as pos.getIndex() will never be larger than the string being parsed (str.length())

    //138
    /*
        Equivalent as SimpleDateFormat.parse only returns null to Date if text or pos provided are null which never occurs (text is verified for null at the start of the method)
    */

    //139/140
    /*
        Offset provided for ParseException is not used anywhere and is only used for debug purposes
    */

    //141
    public void testAddNullDate() throws IllegalArgumentException
    {
        try {
            DateUtils.add(null, 1, 1);
            fail("Error");
        } catch(IllegalArgumentException e) {}
    }

    //172
    // Private method. Test round/truncate of MILLISECOND
    public void testModifyTruncateMillis()
    {
        Calendar cal, res;

        cal = Calendar.getInstance();

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 501);
        res = DateUtils.truncate(cal, Calendar.SECOND);
        assertEquals(0, res.get(Calendar.SECOND));
        assertEquals(0, res.get(Calendar.MILLISECOND));

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 500);
        res = DateUtils.truncate(cal, Calendar.SECOND);
        assertEquals(0, res.get(Calendar.SECOND));
        assertEquals(0, res.get(Calendar.MILLISECOND));

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 499);
        res = DateUtils.truncate(cal, Calendar.SECOND);
        assertEquals(0, res.get(Calendar.SECOND));
        assertEquals(0, res.get(Calendar.MILLISECOND));

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 501);
        res = DateUtils.round(cal, Calendar.SECOND);
        assertEquals(1, res.get(Calendar.SECOND));
        assertEquals(0, res.get(Calendar.MILLISECOND));

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 500);
        res = DateUtils.round(cal, Calendar.SECOND);
        assertEquals(1, res.get(Calendar.SECOND));
        assertEquals(0, res.get(Calendar.MILLISECOND));

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 499);
        res = DateUtils.round(cal, Calendar.SECOND);
        assertEquals(0, res.get(Calendar.SECOND));
        assertEquals(0, res.get(Calendar.MILLISECOND));
    }

    //176
    // Private method. Equivalent as both "round" and "truncate" remove the milliseconds field if it's <500 (minor optimization)

    //184
    // Private method. Equivalent as Calendar fields number decreases and the order increases (ie. MILLISECOND = 14 and SECOND = 13)
    // https://docs.oracle.com/javase/7/docs/api/constant-values.html#java.util.Calendar.SECOND

    //191
    // Private method. Test round/truncate of SECOND
    public void testModifyTruncateSecond()
    {
        Calendar cal, res;

        cal = Calendar.getInstance();

        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 31);
        res = DateUtils.truncate(cal, Calendar.MINUTE);
        assertEquals(0, res.get(Calendar.MINUTE));
        assertEquals(0, res.get(Calendar.SECOND));

        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 30);
        res = DateUtils.truncate(cal, Calendar.MINUTE);
        assertEquals(0, res.get(Calendar.MINUTE));
        assertEquals(0, res.get(Calendar.SECOND));

        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 29);
        res = DateUtils.truncate(cal, Calendar.MINUTE);
        assertEquals(0, res.get(Calendar.MINUTE));
        assertEquals(0, res.get(Calendar.SECOND));

        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 31);
        res = DateUtils.round(cal, Calendar.MINUTE);
        assertEquals(1, res.get(Calendar.MINUTE));
        assertEquals(0, res.get(Calendar.SECOND));

        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 30);
        res = DateUtils.round(cal, Calendar.MINUTE);
        assertEquals(1, res.get(Calendar.MINUTE));
        assertEquals(0, res.get(Calendar.SECOND));

        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 29);
        res = DateUtils.round(cal, Calendar.MINUTE);
        assertEquals(0, res.get(Calendar.MINUTE));
        assertEquals(0, res.get(Calendar.SECOND));
    }

    //195
    // Private method. Equivalent as both "round" and "truncate" remove the seconds field if it's <30 (minor optimization)

    //213
    // Private method. Equivalent as Calendar fields number decreases and the order increases (ie. MILLISECOND = 14 and SECOND = 13)
    // https://docs.oracle.com/javase/7/docs/api/constant-values.html#java.util.Calendar.SECOND

    //220
    // Private method. Test round/truncate of MINUTE
    public void testModifyTruncateMinute()
    {
        Calendar cal, res;

        cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 31);
        res = DateUtils.truncate(cal, Calendar.HOUR_OF_DAY);
        assertEquals(0, res.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, res.get(Calendar.MINUTE));

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 30);
        res = DateUtils.truncate(cal, Calendar.HOUR_OF_DAY);
        assertEquals(0, res.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, res.get(Calendar.MINUTE));

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 29);
        res = DateUtils.truncate(cal, Calendar.HOUR_OF_DAY);
        assertEquals(0, res.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, res.get(Calendar.MINUTE));

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 31);
        res = DateUtils.round(cal, Calendar.HOUR_OF_DAY);
        assertEquals(1, res.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, res.get(Calendar.MINUTE));

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 30);
        res = DateUtils.round(cal, Calendar.HOUR_OF_DAY);
        assertEquals(1, res.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, res.get(Calendar.MINUTE));

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 29);
        res = DateUtils.round(cal, Calendar.HOUR_OF_DAY);
        assertEquals(0, res.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, res.get(Calendar.MINUTE));
    }

    //224
    // Private method. Equivalent as both "round" and "truncate" remove the minutes field if it's <30 (minor optimization)

    //242
    // Private method. Equivalent as time only decreases, never increases, so date.getTime() != time is equivalent to date.getTime() > time 
    
    //243
    // Private method. Equivalent as this if condition is just an optimization to only update the date when MILLISECOND, SECOND or MINUTE gets truncated

    //246
    // Private method. Equivalent as roundUp is always calculated after the first initialization

    //249
    // Private method. Equivalent as i only varies between 0 and fields.length, so when i != fields.length it also verifies that i < fields.length

    //254
    // Private method. Equivalent as j only varies between 0 and fields[i].length, so when i != fields[i].length it also verifies that i < fields[i].length

    //265
    // Private method. Equivalent as DateUtils.SEMI_MONTH takes the value 1001 and there are no Calendar fields larger than 1001
    // https://docs.oracle.com/javase/7/docs/api/constant-values.html#java.util.Calendar.SECOND

    //269
    // Private method. Equivalent as the first value for DATE is 1 https://docs.oracle.com/javase/7/docs/api/java/util/Calendar.html#DATE

    //269
    public void testModifySemiMonth()
    {
        Calendar cal, res;

        cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 5);
        cal.set(Calendar.DATE, 27);
        cal.set(Calendar.MONTH, 1);
        res = DateUtils.round(cal, DateUtils.SEMI_MONTH);
        assertEquals(1, res.get(Calendar.DATE));
        assertEquals(2, res.get(Calendar.MONTH));
    }

    //286
    // Private method. Equivalent as "offset" is only used after it was attributed with some other value than its initialization ("boolean offsetSet = false;"" forces it to enter in "if (!offsetSet)", if offsetSet is true then offset has already been assigned too)
    
    //287
    // Private method. Equivalent as "offset" is only used after it was attributed with some other value than its initialization ("boolean offsetSet = false;"" forces it to enter in "if (!offsetSet)", if offsetSet is true then offset has already been assigned too)

    //291
    // Private method. Equivalent as Calendar.DATE has value 5 and no lower value for fields reaches this section

    //304
    // Private method. Equivalent as offset = 15, which corresponds to date 16, doesn't need to be rounded to date 16 (already that value)

    //312
    public void testModifySemiMonthRound4th() throws Exception
    {
        assertEquals(
            dateParser.parse("November 16, 2001"),
            DateUtils.round(dateTimeParser.parse("November 22, 2001 1:23:11.321"), DateUtils.SEMI_MONTH));

        assertEquals(
            dateParser.parse("November 16, 2001"),
            DateUtils.round(dateTimeParser.parse("November 23, 2001 1:23:11.321"), DateUtils.SEMI_MONTH));

        assertEquals(
            dateParser.parse("December 01, 2001"),
            DateUtils.round(dateTimeParser.parse("November 24, 2001 1:23:11.321"), DateUtils.SEMI_MONTH));
    }

    //319
    // Private method. Equivalent as Calendar.HOUR_OF_DAY has value 11 and no lower value reaches this section (MINUTE is 12, SECOND is 13 and MILLISECOND is 14)

    //326
    public void testModifyAMPMRound() throws Exception
    {
        assertEquals(
            dateTimeParser.parse("February 3, 2002 12:00:00.000"),
            DateUtils.round(dateTimeParser.parse("February 3, 2002 12:10:00.000"), Calendar.AM_PM));
    }

    //334
    public void testModifyAMPMRound4th() throws Exception
    {
        assertEquals(
            dateTimeParser.parse("February 3, 2002 12:00:00.000"),
            DateUtils.round(dateTimeParser.parse("February 3, 2002 18:10:00.000"), Calendar.AM_PM));
    }

    //354
    // Private method. Equivalent as "min" will always have value 0, the fields that don't start at 0 such as DAY_OF_YEAR are not processed there

    //363
    public void testModifyGenericRound() throws Exception
    {
        assertEquals(
            dateTimeParser.parse("January 01, 2002 00:00:00.000"),
            DateUtils.round(dateTimeParser.parse("January 16, 2002 12:10:00.000"), Calendar.MONTH));
    }

    //369
    // Private method. Equivalent as offset can only have values [0, infinity], if it's != 0 then it's > 0

    //382
    // Private method. Equivalent, if null is provided for "iterator" field "focus", it's dealt with in iterator(Object focus, int rangeStyle)

    //388
    public void testMonthIteRANGEMONTHMONDAY() throws Exception {
        for(int i = 1; i <= 28; i ++)
        {
            Iterator it = DateUtils.iterator(dateTimeParser.parse("February " + i + ", 2020 12:34:56.789"), DateUtils.RANGE_MONTH_MONDAY);
            assertWeekIterator(it,
                dateParser.parse("January 27, 2020"),
                dateParser.parse("March 1, 2020"));
        }   
        for(int i = 1; i <= 28; i ++)
        {
            Iterator it = DateUtils.iterator(dateTimeParser.parse("February " + i + ", 2020 12:34:56.789"), DateUtils.RANGE_MONTH_SUNDAY);
            assertWeekIterator(it,
                dateParser.parse("January 26, 2020"),
                dateParser.parse("February 29, 2020"));
        }      
    }

    //392
    //Equivalent as RANGE_MONTH_MONDAY has value 6 which is the highest value for rangeStyle

    //422,423
    public void testMonthIteratorStartCutoff() throws Exception {
        Iterator it;

        it = DateUtils.iterator(dateTimeParser.parse("March 1, 2020 12:34:56.789"), DateUtils.RANGE_WEEK_SUNDAY);
            assertWeekIterator(it,
                dateParser.parse("March 1, 2020"),
                dateParser.parse("March 7, 2020")); 

        it = DateUtils.iterator(dateTimeParser.parse("March 1, 2020 12:34:56.789"), DateUtils.RANGE_MONTH_SUNDAY);
            assertWeekIterator(it,
                dateParser.parse("March 1, 2020"),
                dateParser.parse("April 4, 2020")); 
    }

    public void testIteratorObject() {
        try {
            DateUtils.iterator((Object) null, 3);
            fail();
        } catch(Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    public void testIterator() throws Exception {
        Date d1 = dateTimeParser.parse("December 14, 2020 12:34:56.789");
        Date d2 = dateTimeParser.parse("December 15, 2020 12:34:56.789");
        Date d3 = dateTimeParser.parse("December 16, 2020 12:34:56.789");
        Date d4 = dateTimeParser.parse("December 17, 2020 12:34:56.789");
        Date d5 = dateTimeParser.parse("December 18, 2020 12:34:56.789");
        Date d6 = dateTimeParser.parse("December 19, 2020 12:34:56.789");
        Date d7 = dateTimeParser.parse("December 20, 2020 12:34:56.789");

        Iterator it1 = DateUtils.iterator(d1, DateUtils.RANGE_WEEK_CENTER);
        Iterator it2 = DateUtils.iterator(d1, DateUtils.RANGE_WEEK_SUNDAY);
        Iterator it3 = DateUtils.iterator(d2, DateUtils.RANGE_WEEK_SUNDAY);
        Iterator it4 = DateUtils.iterator(d3, DateUtils.RANGE_WEEK_SUNDAY);
        Iterator it5 = DateUtils.iterator(d4, DateUtils.RANGE_WEEK_SUNDAY);
        Iterator it6 = DateUtils.iterator(d5, DateUtils.RANGE_WEEK_SUNDAY);
        Iterator it7 = DateUtils.iterator(d6, DateUtils.RANGE_WEEK_SUNDAY);
        Iterator it8 = DateUtils.iterator(d7, DateUtils.RANGE_WEEK_SUNDAY);

        Iterator it9 = DateUtils.iterator(d1, DateUtils.RANGE_MONTH_SUNDAY);
        Iterator it10 = DateUtils.iterator(d2, DateUtils.RANGE_MONTH_SUNDAY);
        Iterator it11 = DateUtils.iterator(d3, DateUtils.RANGE_MONTH_SUNDAY);
        Iterator it12 = DateUtils.iterator(d4, DateUtils.RANGE_MONTH_SUNDAY);
        Iterator it13 = DateUtils.iterator(d5, DateUtils.RANGE_MONTH_SUNDAY);
        Iterator it14 = DateUtils.iterator(d6, DateUtils.RANGE_MONTH_SUNDAY);
        Iterator it15 = DateUtils.iterator(d7, DateUtils.RANGE_MONTH_SUNDAY);

        Iterator it16 = DateUtils.iterator(d1, DateUtils.RANGE_MONTH_MONDAY);
        Iterator it17 = DateUtils.iterator(d2, DateUtils.RANGE_MONTH_MONDAY);
        Iterator it18 = DateUtils.iterator(d3, DateUtils.RANGE_MONTH_MONDAY);
        Iterator it19 = DateUtils.iterator(d4, DateUtils.RANGE_MONTH_MONDAY);
        Iterator it20 = DateUtils.iterator(d5, DateUtils.RANGE_MONTH_MONDAY);
        Iterator it21 = DateUtils.iterator(d6, DateUtils.RANGE_MONTH_MONDAY);
        Iterator it22 = DateUtils.iterator(d7, DateUtils.RANGE_MONTH_MONDAY);

        Iterator it23 = DateUtils.iterator(d1, DateUtils.RANGE_WEEK_MONDAY);
        Iterator it24 = DateUtils.iterator(d2, DateUtils.RANGE_WEEK_MONDAY);
        Iterator it25 = DateUtils.iterator(d3, DateUtils.RANGE_WEEK_MONDAY);
        Iterator it26 = DateUtils.iterator(d4, DateUtils.RANGE_WEEK_MONDAY);
        Iterator it27 = DateUtils.iterator(d5, DateUtils.RANGE_WEEK_MONDAY);
        Iterator it28 = DateUtils.iterator(d6, DateUtils.RANGE_WEEK_MONDAY);
        Iterator it29 = DateUtils.iterator(d7, DateUtils.RANGE_WEEK_MONDAY);

        Iterator it30 = DateUtils.iterator(d1, DateUtils.RANGE_WEEK_RELATIVE);
        Iterator it31 = DateUtils.iterator(d2, DateUtils.RANGE_WEEK_RELATIVE);
        Iterator it32 = DateUtils.iterator(d3, DateUtils.RANGE_WEEK_RELATIVE);
        Iterator it33 = DateUtils.iterator(d4, DateUtils.RANGE_WEEK_RELATIVE);
        Iterator it34 = DateUtils.iterator(d5, DateUtils.RANGE_WEEK_RELATIVE);
        Iterator it35 = DateUtils.iterator(d6, DateUtils.RANGE_WEEK_RELATIVE);
        Iterator it36 = DateUtils.iterator(d7, DateUtils.RANGE_WEEK_RELATIVE);

        Iterator it37 = DateUtils.iterator(d1, DateUtils.RANGE_WEEK_CENTER);
        Iterator it38 = DateUtils.iterator(d2, DateUtils.RANGE_WEEK_CENTER);
        Iterator it39 = DateUtils.iterator(d3, DateUtils.RANGE_WEEK_CENTER);
        Iterator it40 = DateUtils.iterator(d4, DateUtils.RANGE_WEEK_CENTER);
        Iterator it41 = DateUtils.iterator(d5, DateUtils.RANGE_WEEK_CENTER);
        Iterator it42 = DateUtils.iterator(d6, DateUtils.RANGE_WEEK_CENTER);
        Iterator it43 = DateUtils.iterator(d7, DateUtils.RANGE_WEEK_CENTER);

        // Iterator it = it31;
        // while(it.hasNext()) {
        //     Object o = it.next();
        //     writeToLog(o.toString());
        // }

        assertWeekIterator(it1,
                dateParser.parse("December 11, 2020"),
                dateParser.parse("December 17, 2020"));

        // RANGE_WEEK_SUNDAY
        assertWeekIterator(it2,
                dateParser.parse("December 13, 2020"),
                dateParser.parse("December 19, 2020"));
        assertWeekIterator(it3,
                dateParser.parse("December 13, 2020"),
                dateParser.parse("December 19, 2020"));
        assertWeekIterator(it4,
                dateParser.parse("December 13, 2020"),
                dateParser.parse("December 19, 2020"));
        assertWeekIterator(it5,
                dateParser.parse("December 13, 2020"),
                dateParser.parse("December 19, 2020"));
        assertWeekIterator(it6,
                dateParser.parse("December 13, 2020"),
                dateParser.parse("December 19, 2020"));
        assertWeekIterator(it7,
                dateParser.parse("December 13, 2020"),
                dateParser.parse("December 19, 2020"));
        assertWeekIterator(it8,
                dateParser.parse("December 20, 2020"),
                dateParser.parse("December 26, 2020"));

        // RANGE_MONTH_SUNDAY
        assertWeekIterator(it9,
                dateParser.parse("November 29, 2020"),
                dateParser.parse("January 2, 2021"));
        assertWeekIterator(it10,
                dateParser.parse("November 29, 2020"),
                dateParser.parse("January 2, 2021"));
        assertWeekIterator(it11,
                dateParser.parse("November 29, 2020"),
                dateParser.parse("January 2, 2021"));
        assertWeekIterator(it12,
                dateParser.parse("November 29, 2020"),
                dateParser.parse("January 2, 2021"));
        assertWeekIterator(it13,
                dateParser.parse("November 29, 2020"),
                dateParser.parse("January 2, 2021"));
        assertWeekIterator(it14,
                dateParser.parse("November 29, 2020"),
                dateParser.parse("January 2, 2021"));                                                
        assertWeekIterator(it15,
                dateParser.parse("November 29, 2020"),
                dateParser.parse("January 2, 2021"));

        // RANGE_MONTH_MONDAY
        assertWeekIterator(it16,
                dateParser.parse("November 30, 2020"),
                dateParser.parse("January 3, 2021"));
        assertWeekIterator(it17,
                dateParser.parse("November 30, 2020"),
                dateParser.parse("January 3, 2021"));
        assertWeekIterator(it18,
                dateParser.parse("November 30, 2020"),
                dateParser.parse("January 3, 2021"));
        assertWeekIterator(it19,
                dateParser.parse("November 30, 2020"),
                dateParser.parse("January 3, 2021"));
        assertWeekIterator(it20,
                dateParser.parse("November 30, 2020"),
                dateParser.parse("January 3, 2021"));
        assertWeekIterator(it21,
                dateParser.parse("November 30, 2020"),
                dateParser.parse("January 3, 2021"));
        assertWeekIterator(it22,
                dateParser.parse("November 30, 2020"),
                dateParser.parse("January 3, 2021")); 

        // RANGE_WEEK_MONDAY                                                                                               
        assertWeekIterator(it23,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020")); 
        assertWeekIterator(it24,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020")); 
        assertWeekIterator(it25,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020")); 
        assertWeekIterator(it26,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020")); 
        assertWeekIterator(it27,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020")); 
        assertWeekIterator(it28,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020")); 
        assertWeekIterator(it29,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020"));  

        // RANGE_WEEK_RELATIVE
        assertWeekIterator(it30,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020"));        
        assertWeekIterator(it31,
                dateParser.parse("December 15, 2020"),
                dateParser.parse("December 21, 2020"));    
        assertWeekIterator(it32,
                dateParser.parse("December 16, 2020"),
                dateParser.parse("December 22, 2020"));
        assertWeekIterator(it33,
                dateParser.parse("December 17, 2020"),
                dateParser.parse("December 23, 2020"));    
        assertWeekIterator(it34,
                dateParser.parse("December 18, 2020"),
                dateParser.parse("December 24, 2020")); 
        assertWeekIterator(it35,
                dateParser.parse("December 19, 2020"),
                dateParser.parse("December 25, 2020"));
        assertWeekIterator(it36,
                dateParser.parse("December 20, 2020"),
                dateParser.parse("December 26, 2020"));                                                

        // RANGE_WEEK_CENTER
        assertWeekIterator(it37,
                dateParser.parse("December 11, 2020"),
                dateParser.parse("December 17, 2020")); 
        assertWeekIterator(it38,
                dateParser.parse("December 12, 2020"),
                dateParser.parse("December 18, 2020"));
        assertWeekIterator(it39,
                dateParser.parse("December 13, 2020"),
                dateParser.parse("December 19, 2020"));
        assertWeekIterator(it40,
                dateParser.parse("December 14, 2020"),
                dateParser.parse("December 20, 2020")); 
        assertWeekIterator(it41,
                dateParser.parse("December 15, 2020"),
                dateParser.parse("December 21, 2020")); 
        assertWeekIterator(it42,
                dateParser.parse("December 16, 2020"),
                dateParser.parse("December 22, 2020")); 
        assertWeekIterator(it43,
                dateParser.parse("December 17, 2020"),
                dateParser.parse("December 23, 2020"));                                                                                                        


        // Calendar start = null;
        // Calendar focus = Calendar.getInstance();
        // focus.setTime(d1);
        // start = DateUtils.truncate(focus, Calendar.DATE);
        // writeToLog(start.toString());
        // while (start.get(Calendar.DAY_OF_WEEK) != 8) {
        //     start.add(Calendar.DATE, -1);
        //     writeToLog(start.toString());
        // }
    }
}

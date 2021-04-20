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

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.lang.SystemUtils;


public class LANG_JUDY_D1_StudentTest extends TestCase {
    
    public void testIllegalArgumentException() {

        try {
            /*
            public static boolean isSameDay(Date date1, Date date2):143,"PNC","call new with child class type"
            public static boolean isSameDay(Date date1, Date date2):143,"PNC","call new with child class type"
            public static boolean isSameDay(Date date1, Date date2):143,"PNC","call new with child class type"
            public static boolean isSameDay(Date date1, Date date2):143,"PNC","call new with child class type"
            */
            DateUtils.isSameDay((Date) null, (Date) null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static boolean isSameDay(Calendar cal1, Calendar cal2):167,"PNC","call new with child class type"
            public static boolean isSameDay(Calendar cal1, Calendar cal2):167,"PNC","call new with child class type"
            public static boolean isSameDay(Calendar cal1, Calendar cal2):167,"PNC","call new with child class type"
            public static boolean isSameDay(Calendar cal1, Calendar cal2):167,"PNC","call new with child class type"
            */
            DateUtils.isSameDay((Calendar) null, (Calendar) null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static boolean isSameInstant(Date date1, Date date2):188,"PNC","call new with child class type"
            public static boolean isSameInstant(Date date1, Date date2):188,"PNC","call new with child class type"
            public static boolean isSameInstant(Date date1, Date date2):188,"PNC","call new with child class type"
            public static boolean isSameInstant(Date date1, Date date2):188,"PNC","call new with child class type"
            */
            DateUtils.isSameInstant((Date) null, (Date) null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static boolean isSameInstant(Calendar cal1, Calendar cal2):206,"PNC","call new with child class type"
            public static boolean isSameInstant(Calendar cal1, Calendar cal2):206,"PNC","call new with child class type"
            public static boolean isSameInstant(Calendar cal1, Calendar cal2):206,"PNC","call new with child class type"
            public static boolean isSameInstant(Calendar cal1, Calendar cal2):206,"PNC","call new with child class type"
            */
            DateUtils.isSameInstant((Calendar) null, (Calendar) null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):226,"PNC","call new with child class type"
            public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):226,"PNC","call new with child class type"
            public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):226,"PNC","call new with child class type"
            public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):226,"PNC","call new with child class type"
            */
            DateUtils.isSameLocalTime((Calendar) null, (Calendar) null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Date parseDate(String str, String[] parsePatterns):254,"PNC","call new with child class type
            public static Date parseDate(String str, String[] parsePatterns):254,"PNC","call new with child class type
            public static Date parseDate(String str, String[] parsePatterns):254,"PNC","call new with child class type
            public static Date parseDate(String str, String[] parsePatterns):254,"PNC","call new with child class type
            */
            DateUtils.parseDate(null, null);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        } catch (ParseException ex) {
            fail();
        }

        try {
            /*
            public static Date add(Date date, int calendarField, int amount):399,"PNC","call new with child class type"
            public static Date add(Date date, int calendarField, int amount):399,"EGE","generalization of caught errors"
            public static Date add(Date date, int calendarField, int amount):399,"PNC","call new with child class type"
            public static Date add(Date date, int calendarField, int amount):399,"PNC","call new with child class type"
            public static Date add(Date date, int calendarField, int amount):399,"PNC","call new with child class type"
            */
            DateUtils.add((Date) null, 0, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Date round(Date date, int field):438,"PNC","call new with child class type"
            public static Date round(Date date, int field):438,"PNC","call new with child class type"
            public static Date round(Date date, int field):438,"PNC","call new with child class type"
            public static Date round(Date date, int field):438,"PNC","call new with child class type"
            */
            DateUtils.round((Date) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Calendar round(Calendar date, int field):476,"PNC","call new with child class type"
            public static Calendar round(Calendar date, int field):476,"PNC","call new with child class type"
            public static Calendar round(Calendar date, int field):476,"PNC","call new with child class type"
            public static Calendar round(Calendar date, int field):476,"PNC","call new with child class type"
            */
            DateUtils.round((Calendar) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Date round(Object date, int field):515,"PNC","call new with child class type"
            public static Date round(Object date, int field):515,"PNC","call new with child class type"
            public static Date round(Object date, int field):515,"PNC","call new with child class type"
            public static Date round(Object date, int field):515,"PNC","call new with child class type"
            */
            DateUtils.round((Object) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Date truncate(Date date, int field):545,"PNC","call new with child class type"
            public static Date truncate(Date date, int field):545,"PNC","call new with child class type"
            public static Date truncate(Date date, int field):545,"PNC","call new with child class type"
            public static Date truncate(Date date, int field):545,"PNC","call new with child class type"
            */
            DateUtils.truncate((Date) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Calendar truncate(Calendar date, int field):571,"PNC","call new with child class type"
            public static Calendar truncate(Calendar date, int field):571,"PNC","call new with child class type"
            public static Calendar truncate(Calendar date, int field):571,"PNC","call new with child class type"
            public static Calendar truncate(Calendar date, int field):571,"PNC","call new with child class type"
            */
            DateUtils.truncate((Calendar) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Date truncate(Object date, int field):600,"PNC","call new with child class type"
            public static Date truncate(Object date, int field):600,"PNC","call new with child class type"
            public static Date truncate(Object date, int field):600,"PNC","call new with child class type"
            public static Date truncate(Object date, int field):600,"PNC","call new with child class type"
            */
            DateUtils.truncate((Object) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Iterator iterator(Date focus, int rangeStyle):773,"PNC","call new with child class type"
            public static Iterator iterator(Date focus, int rangeStyle):773,"PNC","call new with child class type"
            public static Iterator iterator(Date focus, int rangeStyle):773,"PNC","call new with child class type"
            public static Iterator iterator(Date focus, int rangeStyle):773,"PNC","call new with child class type"
            */
            DateUtils.iterator((Date) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Iterator iterator(Calendar focus, int rangeStyle):806,"PNC","call new with child class type"
            public static Iterator iterator(Calendar focus, int rangeStyle):806,"PNC","call new with child class type"
            public static Iterator iterator(Calendar focus, int rangeStyle):806,"PNC","call new with child class type"
            public static Iterator iterator(Calendar focus, int rangeStyle):806,"PNC","call new with child class type"
            */
            DateUtils.iterator((Calendar) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }

        try {
            /*
            public static Iterator iterator(Object focus, int rangeStyle):897,"PNC","call new with child class type"
            public static Iterator iterator(Object focus, int rangeStyle):897,"PNC","call new with child class type"
            public static Iterator iterator(Object focus, int rangeStyle):897,"PNC","call new with child class type"
            public static Iterator iterator(Object focus, int rangeStyle):897,"PNC","call new with child class type"
            */
            DateUtils.iterator((Object) null, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            assertEquals(ex.getClass().getCanonicalName(), "java.lang.IllegalArgumentException");
        }
    }
    

    public void testSameDate() {
        GregorianCalendar date1 = new GregorianCalendar(2020, 12, 19, 19, 19, 19);
        GregorianCalendar date2 = new GregorianCalendar(2019, 12, 19, 19, 19, 19);
        GregorianCalendar date3 = new GregorianCalendar(2020, 12, 18, 19, 19, 19);
        GregorianCalendar date4 = new GregorianCalendar(2020, 12, 19, 18, 19, 19);
        GregorianCalendar date5 = new GregorianCalendar(2020, 12, 19, 19, 18, 19);
        GregorianCalendar date6 = new GregorianCalendar(2020, 12, 19, 19, 19, 18);
        GregorianCalendar date7 = new GregorianCalendar(2020, 12, 19, 19, 19, 19);
        date7.setTimeInMillis(date7.getTimeInMillis()+1);

        /*
        public static boolean isSameDay(Calendar cal1, Calendar cal2):169,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameDay(Calendar cal1, Calendar cal2):169,"JIR_Ifgt","replace jump instructions with IFGT (IF_ICMPGT)"
        public static boolean isSameDay(Calendar cal1, Calendar cal2):169,"JIR_Ifgt","replace jump instructions with IFGT (IF_ICMPGT)"
        public static boolean isSameDay(Calendar cal1, Calendar cal2):169,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameDay(Calendar cal1, Calendar cal2):169,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        */
        assertEquals(true, DateUtils.isSameDay(date1, date1));
        assertEquals(false, DateUtils.isSameDay(date1, date2));
        assertEquals(false, DateUtils.isSameDay(date2, date1));
        assertEquals(false, DateUtils.isSameDay(date1, date3));
        assertEquals(false, DateUtils.isSameDay(date3, date1));

        /*
        public static boolean isSameInstant(Date date1, Date date2):190,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameInstant(Calendar cal1, Calendar cal2):208,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        */
        assertEquals(true, DateUtils.isSameInstant(date1.getTime(), date1.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date1.getTime(), date2.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date2.getTime(), date1.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date1.getTime(), date3.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date3.getTime(), date1.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date1.getTime(), date4.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date4.getTime(), date1.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date1.getTime(), date5.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date5.getTime(), date1.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date1.getTime(), date6.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date6.getTime(), date1.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date1.getTime(), date7.getTime()));
        assertEquals(false, DateUtils.isSameInstant(date7.getTime(), date1.getTime()));
        assertEquals(true, DateUtils.isSameInstant(date1, date1));
        assertEquals(false, DateUtils.isSameInstant(date1, date2));
        assertEquals(false, DateUtils.isSameInstant(date2, date1));
        assertEquals(false, DateUtils.isSameInstant(date1, date3));
        assertEquals(false, DateUtils.isSameInstant(date3, date1));
        assertEquals(false, DateUtils.isSameInstant(date1, date4));
        assertEquals(false, DateUtils.isSameInstant(date4, date1));
        assertEquals(false, DateUtils.isSameInstant(date1, date5));
        assertEquals(false, DateUtils.isSameInstant(date5, date1));
        assertEquals(false, DateUtils.isSameInstant(date1, date6));
        assertEquals(false, DateUtils.isSameInstant(date6, date1));
        assertEquals(false, DateUtils.isSameInstant(date1, date7));
        assertEquals(false, DateUtils.isSameInstant(date7, date1));


        /*
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Ifgt","replace jump instructions with IFGT (IF_ICMPGT)",
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Ifgt","replace jump instructions with IFGT (IF_ICMPGT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Ifgt","replace jump instructions with IFGT (IF_ICMPGT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Ifgt","replace jump instructions with IFGT (IF_ICMPGT)",
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Ifgt","replace jump instructions with IFGT (IF_ICMPGT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Ifgt","replace jump instructions with IFGT (IF_ICMPGT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"EOC","replace reference comparison with content comparison (equals) and vice-versa"
        public static boolean isSameLocalTime(Calendar cal1, Calendar cal2):228,"JIR_Iflt","replace jump instructions with IFLT (IF_ICMPLT)"
        */
        assertEquals(true, DateUtils.isSameLocalTime(date1, date1));
        assertEquals(false, DateUtils.isSameLocalTime(date1, date2));
        assertEquals(false, DateUtils.isSameLocalTime(date2, date1));
        assertEquals(false, DateUtils.isSameLocalTime(date1, date3));
        assertEquals(false, DateUtils.isSameLocalTime(date3, date1));
        assertEquals(false, DateUtils.isSameLocalTime(date1, date4));
        assertEquals(false, DateUtils.isSameLocalTime(date4, date1));
        assertEquals(false, DateUtils.isSameLocalTime(date1, date5));
        assertEquals(false, DateUtils.isSameLocalTime(date5, date1));
        assertEquals(false, DateUtils.isSameLocalTime(date1, date6));
        assertEquals(false, DateUtils.isSameLocalTime(date6, date1));
        assertEquals(false, DateUtils.isSameLocalTime(date1, date7));
        assertEquals(false, DateUtils.isSameLocalTime(date7, date1));
    }
}


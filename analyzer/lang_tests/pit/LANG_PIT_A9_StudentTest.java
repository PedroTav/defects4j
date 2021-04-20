package org.apache.commons.lang.time;
import junit.framework.TestCase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LANG_PIT_A9_StudentTest extends TestCase {


    private TimeZone defaultZone;
      DateFormat dateParser = null;
      DateFormat dateTimeParser = null;

      
      Date date1 = null;
      Date date= null;
    

        public void testAddMonths(){
            Date d1 = new GregorianCalendar(1998,8,22,12,00).getTime();
            assertNotNull(DateUtils.addMonths(d1,2)); //kills line 299
        }

        public void testaddMilliseconds(){
            Date d1 = new GregorianCalendar(1996,5,22,12,00).getTime();
            assertNotNull(DateUtils.addMilliseconds(d1,2)); //kills line 383
        }

        public void tests() throws Exception {

            dateParser = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            dateTimeParser = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);

            assertEquals("truncate second-1 failed",
                    dateTimeParser.parse("November 18, 2001 1:23:12.000"),
                    DateUtils.round(dateTimeParser.parse("November 18, 2001 1:23:11.500"), Calendar.SECOND)); //kills line 641
        }
     

        public void testModifyMinute() throws Exception {

            dateParser = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            dateTimeParser = new SimpleDateFormat("MMM dd, yyyy H:mm:ss.SSS", Locale.ENGLISH);

            assertEquals("truncate second-1 failed",
                    dateTimeParser.parse("November 18, 2001 1:23:00.000"),
                    DateUtils.round(dateTimeParser.parse("November 18, 2001 1:23:12.000"), Calendar.MINUTE));
        }

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

}
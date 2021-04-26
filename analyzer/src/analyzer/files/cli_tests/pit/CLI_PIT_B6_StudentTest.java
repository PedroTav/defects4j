package org.apache.commons.cli;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;

/*
>> Generated 128 mutations Killed 121 (95%)
>> Ran 249 tests (1.95 tests per mutation)
17:14:16 PIT >> INFO : Completed in 24 seconds
 */

//equivalent: 611, 799, 809, 879, 923
//unknown: 677

public class CLI_PIT_B6_StudentTest extends TestCase {

    private static final String EOL = System.getProperty("line.separator");

    public void testPrintHelp_382_1() throws Exception {
        Options options = new Options();
        Option dir = OptionBuilder.withDescription( "dir" ).hasArg().create( 'd' );
        options.addOption( dir );

        final PrintStream oldSystemOut = System.out;
        try
        {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final PrintStream print = new PrintStream(bytes);

            // capture this platform's eol symbol
            print.println();
            final String eol = bytes.toString();
            bytes.reset();

            System.setOut(new PrintStream(bytes));

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "dir", options, false);

            assertEquals("usage: dir"+eol+" -d <arg>   dir"+eol,bytes.toString());
        }
        finally
        {
            System.setOut(oldSystemOut);
        }
    }

    public void testPrintHelp_397_2() throws Exception {
        Options options = new Options();
        Option dir = OptionBuilder.withDescription( "dir" ).hasArg().create( 'd' );
        options.addOption( dir );

        final PrintStream oldSystemOut = System.out;
        try
        {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final PrintStream print = new PrintStream(bytes);

            // capture this platform's eol symbol
            print.println();
            final String eol = bytes.toString();
            bytes.reset();

            System.setOut(new PrintStream(bytes));
            String header = "header";
            String footer = "footer";

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "dir", header, options, footer);

            assertEquals("usage: dir"+eol+header+eol+" -d <arg>   dir"+eol+footer+eol,bytes.toString());
        }
        finally
        {
            System.setOut(oldSystemOut);
        }
    }

    public void testPrintHelp_430_2() throws Exception {
        Options options = new Options();
        Option dir = OptionBuilder.withDescription( "dir" ).hasArg().create( 'd' );
        options.addOption( dir );

        final PrintStream oldSystemOut = System.out;
        try
        {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final PrintStream print = new PrintStream(bytes);

            // capture this platform's eol symbol
            print.println();
            final String eol = bytes.toString();
            bytes.reset();

            System.setOut(new PrintStream(bytes));
            String header = "header";
            String footer = "footer";
            int width = 100;

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(width, "dir", header, options, footer);

            assertEquals("usage: dir"+eol+header+eol+" -d <arg>   dir"+eol+footer+eol,bytes.toString());
        }
        finally
        {
            System.setOut(oldSystemOut);
        }
    }

    /**
     * Mutant can't be killed
     * As the options are retrived from a group, from within a HashMap
     * using .values(), they come sorted by key making line 611 redundant
     * and its elimination is possible
     * @throws Exception
     */




    public void testPrintUsage_679_2() throws Exception {
        HelpFormatter hf = new HelpFormatter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);

        String expected = "usage:" + EOL + " app" + EOL +  " usage" +  EOL +" msg";

        hf.printUsage(pw, 6, "app usage msg");
        pw.flush();

        String result = out.toString().trim();
        assertEquals(result, expected, result);
        out.reset();
    }

    /**
     * *799*
     * max = (optBuf.length() > max) ? optBuf.length() : max;
     * changing to >= would get optBuf.length() selected when they are == instead of max
     * since the values are equal, the result is indistinguishable
     */

    /**
     * *809*
     * Since max - optBuf.length() corresponds to the amount of paddind added
     * when max == optBuf.length(), even if it enters the condition, no padding
     * is added, resulting in the same string result
     */

    public void testRenderOptions_831_1() throws Exception
    {
        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        final int leftPad = 1;
        final int descPad = 3;
        final String lpad = hf.createPadding(leftPad);
        final String dpad = hf.createPadding(descPad);
        Options options = null;
        String expected = null;

        options = new Options()
                .addOption("a", false, "aaa")
                .addOption("c", false, "cc")
                .addOption("b", false, "bbb");
        expected =  lpad + "-a" + dpad + "aaa" + EOL +
                lpad + "-b" + dpad + "bbb" + EOL +
                lpad + "-c" + dpad + "cc";
        sb.setLength(0);
        StringBuffer sbRes = hf.renderOptions(sb, 100, options, leftPad, descPad);
        assertEquals("multiple wrapped options", expected, sb.toString());
        assertNotNull(sbRes);
    }

    public void testRenderWrappedText_854_1()
    {
        int width = 100;
        int padding = 0;
        String text = "Thisisatest.";

        StringBuffer sb = new StringBuffer();
        HelpFormatter hp = new HelpFormatter();
        StringBuffer res =  hp.renderWrappedText(sb, width, padding, text);
        assertTrue("cut and wrap", sb.toString().equals(res.toString()));
    }

    public void testRenderWrappedText_858_1()
    {
        int width = 4;
        int padding = 4;
        String text = "This is a";
        String expected = "This" + EOL + " is" + EOL + " a";
        StringBuffer sb = new StringBuffer();
        HelpFormatter hp = new HelpFormatter();
        StringBuffer res =  hp.renderWrappedText(sb, width, padding, text);
        assertTrue("cut and wrap", expected.equals(res.toString()));
    }

    /**
     * 1. and 3. are equivalent -> acho que o 3 nao � equivalente e que o teste de baixo o mata
     * text.length() <= width => pos == -1
     * text.length() > width equivalent to text.length() >= width and to text.length() < width
     * making the condition unnecessary
     */

    // 879 from findWrapPos if (startPos + width >= text.length()) then pos = -1
    // since startPos is 0 when text.length <= width pos = -1 and the function ends therefore in line 879
    // the text.length() is always > width and the condition is always true.
    // therefore the mutant if ((text.length() >= width) is equivalent






    public void testFindWrapPos_907_1() throws Exception
    {
        HelpFormatter hf = new HelpFormatter();

        String text = "This\n is a test.";
        assertEquals("wrap position 0", 5, hf.findWrapPos(text, 16, 0));
        assertEquals("wrap position 1", 5, hf.findWrapPos(text, 4, 0));
    }

    public void testFindWrapPos_908_1() throws Exception
    {
        HelpFormatter hf = new HelpFormatter();

        String text = "This\t is a test.";
        assertEquals("wrap position 0", 5, hf.findWrapPos(text, 16, 0));
        assertEquals("wrap position 1", 5, hf.findWrapPos(text, 4, 0));
    }

    /**
     * 923 1. Equivalent mutant
     * pos >= startPos equivalent to pos > startPos
     * Changing the condition boundary will give change to make pos < startPos
     * So you get the chance to have pos <= startPos instead of pos == startPos
     * Given that it never passes the condition of line 930 in either case
     * the value is ultimately reassigned, making it equivalent
     */


    public void testFindWrapPos_931_1() throws Exception
    {
        HelpFormatter hf = new HelpFormatter();

        String text = "This is a test.";
        assertEquals("wrap position 1", 5, hf.findWrapPos(text, 1, 4));
    }



    public void testPrintUsage_677_3() {
        HelpFormatter hf = new HelpFormatter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);
        hf.printUsage( pw, 10, "Stringtext");
        pw.flush();
        String result = out.toString().trim();
        String expected = "usage:" + EOL + "       Str" + EOL + "       ing" + EOL + "       tex" + EOL + "       t";
        out.reset();
        assertEquals(result, expected, result);
    }

    /* STRONGER
    >> Generated 173 mutations Killed 163 (93%)
    >> Ran 408 tests (2.36 tests per mutation)*/

    // 786

    public void testRenderOptions_786_4() throws Exception
    {
        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        final int leftPad = 1;
        final int descPad = 3;
        final String lpad = hf.createPadding(leftPad);
        final String dpad = hf.createPadding(descPad);
        Options options = null;
        String expected = null;

        Option opt = new Option("a", true, "aaaa aaaa aaaa aaaa aaaa");
        opt.setArgName("");
        options = new Options().addOption(opt);
        expected = lpad + "-a " + dpad + "aaaa aaaa aaaa aaaa aaaa";
        hf.renderOptions(sb, 60, options, leftPad, descPad);
        assertEquals("arg name is empty string", expected, sb.toString());

    }



    // 879

    // 938
    /*
     * the mutant in line 938 is equivalent because the condition pos ==text.length() is never true, therefore replacing
     * it by false is the exact same situation
     * pos = startPos + width, startPos and width are never reassigned during the function execution
     * in line 912 it is checked if startPos + width >= text.length and if this condition is true the function ends
     * returning -1.
     * */


    /*
     * 965 equivalent. replacing  if ((s == null) || (s.length() == 0)) with  if ((s == null) || false) outputs the same result.
     * an empty string since pos = s.length is never > 0 and therefore the program return a substring(0,0) which is equal to s, the empty string.
     * */


    /* ALL
    other params
    --outputFormats XML,HTML, --mutators STRONGER,RETURN_VALS,REMOVE_CONDITIONALS,INLINE_CONSTS,CONSTRUCTOR_CALLS,NON_VOID_METHOD_CALLS,REMOVE_INCREMENTS
    >> Generated 466 mutations Killed 439 (93%)
    >> Ran 1077 tests (2.31 tests per mutation)*/

    public void testDefaultWidth_75_1() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(formatter.defaultWidth, HelpFormatter.DEFAULT_WIDTH);
    }

    public void testPrintHelp_367_1() {
        Options options = new Options();
        Option dir = OptionBuilder.withDescription( "dir" ).hasArg().create( 'd' );
        options.addOption( dir );

        final PrintStream oldSystemOut = System.out;
        try
        {
            final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            final PrintStream print = new PrintStream(bytes);

            // capture this platform's eol symbol
            print.println();
            final String eol = bytes.toString();
            bytes.reset();

            System.setOut(new PrintStream(bytes));

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "dir", options);

            assertEquals("usage: dir"+eol+" -d <arg>   dir"+eol,bytes.toString());
        }
        finally
        {
            System.setOut(oldSystemOut);
        }
    }

    /*
     * 584
     * if(i.hasNext()) { buff.append(" ") } replace with if(true)
     * if i,hasNext() is false it means the iteration is over
     * in this case and " " should not be appended to the end of buff, however, if it does, when calling
     * printWrapped method, which call rtrim the " " in the end of the string is removed, therefore there is no difference
     * and the mutant is equivalent
     * */








    // 938
    /*
     * mutant 938.1 is equivalent because the condition pos ==text.length() is never true, therefore replacing
     * -1 for 0 is exactly the same, since neither of these values is ever returned
     * pos = startPos + width, startPos and width are never reassigned during the function execution
     * in line 912 it is checked if startPos + width >= text.length and if this condition is true the function ends
     * returning -1.
     * */


    // 879 2. replace with true
    // from findWrapPos if (startPos + width >= text.length()) then pos = -1
    // since startPos is 0 when text.length <= width pos = -1 and the function ends therefore in line 879
    // the text.length() is always > width and the condition is always true.
    // therefore the mutant if ((true) is equivalent.


    /*
     * mutant 756 is equivalent
     * it changes the initial value of max from 0 to 1
     * the only case where this would be a problem is if there was no option with optBuff.length >= 1.
     * this occurs when optBuff.length is 0 (this is achieved by setting leftPad, descPad to 0,  defaultOptPrefix to "" and the option in options
     * opt to "". in this conditions the max value is 1 but the actual maximum value is 0.
     * then on line 809 a padding of max - optBuff.length is added, in this case a padding of 1, since optBuff.length < max (0<1)
     *
     * */
    public void testPrintOptions_756_1() throws Exception
    {
        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        hf.defaultOptPrefix = "";
        final int leftPad = 0;
        final int descPad = 0;
        final String lpad = hf.createPadding(leftPad);
        final String dpad = hf.createPadding(descPad);
        Options options = null;
        String expected = null;

        options = new Options().addOption("", false, null);

        expected =  "";
        sb.setLength(0);
        StringBuffer sbRes = hf.renderOptions(sb, 0, options, leftPad, descPad);
        assertEquals(sb.toString(), expected, sb.toString());
        assertNotNull(sbRes);
    }


    public void testRenderOptions_818_4() throws Exception
    {
        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        final int leftPad = 1;
        final int descPad = 3;
        final String lpad = hf.createPadding(leftPad);
        final String dpad = hf.createPadding(descPad);
        Options options = null;
        String expected = null;

        options = new Options()
                .addOption("a", false, null)
                .addOption("c", false, null)
                .addOption("b", false, null);
        expected =  lpad + "-a"   + EOL +
                lpad + "-b"  + EOL +
                lpad + "-c"  ;
        sb.setLength(0);
        StringBuffer sbRes = hf.renderOptions(sb, 100, options, leftPad, descPad);
        assertEquals(sb.toString(), expected, sb.toString());
        assertNotNull(sbRes);
    }




    /**
     * *809* 3. replace  if (optBuf.length() < max) with if (true)
     * since max is the max value of all optBuf.length()s, optBuf.length() is always <= max
     * Since max - optBuf.length() corresponds to the amount of paddind added
     * when max == optBuf.length(), even if it enters the condition, no padding
     * is added, resulting in the same string result
     */


   /* public void testPrintWrapped_907_4() {
        HelpFormatter hf = new HelpFormatter();
        String text1 = "Hello\nWorld";
        String text2 = "Hello\tWorld";
        int width = 12;
        int startPos = 0;
        assertEquals(6, hf.findWrapPos( text1,  width,  startPos));
        assertEquals(6, hf.findWrapPos( text2,  width,  startPos));

    }*/



    // '\n' -> 10
    // '\r' -> 13
    // 33 -> '!'
    public void testFindWrapPos_923_3_4() {
        HelpFormatter hf = new HelpFormatter();
        String text1 = "H\rello World";
        int width1 = 5;
        int startPos1 = 0;
        assertEquals(1, hf.findWrapPos( text1,  width1,  startPos1));

        String text2 = "Hello\nWorld";
        int width2 = 2;
        int startPos2 = 4;
        assertEquals(5, hf.findWrapPos( text2,  width2,  startPos2));

    }

    public void testPrintUsage_677_1() {
        HelpFormatter hf = new HelpFormatter();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);
        hf.printUsage( pw, 10, "String!text");
        pw.flush();
        String result = out.toString().trim();
        String expected = "usage:" + EOL + "       Str" + EOL + "       ing" + EOL + "       !te" + EOL + "       xt";
        out.reset();
        assertEquals(result, expected, result);
    }

    public void testRenderWrapped_908_9()
    {
        int width = 6;
        int padding = 5;
        String text = "This is \ta test";
        String expected = "This" + EOL + "     i" + EOL + "     s"  + EOL + "     a" + EOL + "     t" + EOL + "     e"
                + EOL + "     s" + EOL + "     t";
        StringBuffer sb = new StringBuffer();
        HelpFormatter hp = new HelpFormatter();
        StringBuffer res =  hp.renderWrappedText(sb, width, padding, text);
        assertTrue(res.toString(), expected.equals(res.toString()));
    }


    public void testFindWrapPos_939_3() {
        HelpFormatter hf = new HelpFormatter();
        String text1 = "Hello World";
        int width1 = 0;
        int startPos1 = 0;
        assertEquals(0, hf.findWrapPos( text1,  width1,  startPos1));
    }

}

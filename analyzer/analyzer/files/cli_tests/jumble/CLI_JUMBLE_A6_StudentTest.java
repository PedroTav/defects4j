package org.apache.commons.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;

public class CLI_JUMBLE_A6_StudentTest extends HelpFormatterTest{

    private static final String EOL = System.getProperty("line.separator");

    public void testPrintHelp() {
        try {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(null, 0, null, null, null, 0, 0, null, false);
        } catch( IllegalArgumentException e ) {
            assertEquals("cmdLineSyntax not provided", e.getMessage()); // Optionally make sure you get the correct message, too
        }
    }

    public void testeDefaultWidth(){
        HelpFormatter hf = new HelpFormatter();
        assertEquals(hf.defaultWidth, HelpFormatter.DEFAULT_WIDTH);
    }

    public void testDefaultLeftPad(){ //
        HelpFormatter hf = new HelpFormatter();
        assertEquals(hf.defaultLeftPad, HelpFormatter.DEFAULT_LEFT_PAD);
    }

    public void testDefaultDescPad(){
        HelpFormatter hf = new HelpFormatter();
        assertEquals(hf.defaultDescPad, HelpFormatter.DEFAULT_DESC_PAD);
    }

    public void testDescPaddingTest(){
        HelpFormatter hf = new HelpFormatter();
        hf.setDescPadding(20);
        assertEquals(Long.valueOf(hf.getDescPadding()), Long.valueOf(20));
    }

    public void testPrintHelp2(){
        PrintStream originalOut = System.out;

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));

        Options options = new Options();
        options.addOption( "f", true, "the file" );
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("foobar", "header", options, "footer");

        System.setOut(originalOut);

        assertEquals("usage: foobar" + EOL +
                "header" + EOL +
                " -f <arg>   the file" + EOL +
                "footer" + EOL , outContent.toString());
    }

    public void testSetOptionComparator(){
        HelpFormatter formatter = new HelpFormatter();
        System.out.println(formatter.getOptionComparator());
        NumComparator numComparator = new NumComparator();
        formatter.setOptionComparator(numComparator);
        formatter.setOptionComparator(null);
        Option op1 = new Option("name1", "name1");
        Option op2 = new Option("name1", "name1");
        assertEquals(0, formatter.getOptionComparator().compare(op1, op2));
    }

    public static class NumComparator implements Comparator{

        public int compare(Object o1, Object o2) {
            return 1;
        }
    }

    public void testPrintHelp3(){
        PrintStream originalOut = System.out;

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));

        Options options = new Options();
        options.addOption( "f", true, "the file" );
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(200, "foobar", "header", options, "footer");

        System.setOut(originalOut);

        assertEquals("usage: foobar" + EOL +
                "header" + EOL +
                " -f <arg>   the file" + EOL +
                "footer" + EOL , outContent.toString());
    }

    public void testPrintUsage(){
        Options options = new Options();
        options.addOption( "f", true, "the file" );

        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptSeparator("=");

        StringWriter out = new StringWriter();

        formatter.printUsage(new PrintWriter(out), 80, "t \t3");
        assertEquals("usage: t" + EOL +
                "         3" + EOL, out.toString());
    }

    public void testRenderWrappedText()
    {
        final int width = 5;
        final int padding = 12;
        final String text = "Thisisatest.";
        final String expected = "Thisi" + EOL + " sate" + EOL + " st.";

        final StringBuffer sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected, sb.toString());
    }

    public void testRenderWrappedText2()
    {
        final int width = 5;
        final int padding = 12;
        final String text = "Th\risis\natest. \n";
        final String expected = "Th" + EOL + " isis" + EOL + " ates" + EOL + " t.";

        final StringBuffer sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected, sb.toString());
    }

    public void testRenderWrappedText3()
    {
        final int width = 5;
        final int padding = 4;
        final String text = "Thisisatest";
        final String expected = "Thisi" + EOL + "    s" + EOL + "    a" + EOL + "    t" + EOL + "    e" + EOL + "    s" + EOL + "    t";

        final StringBuffer sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected, sb.toString());
    }

    public void testRenderWrappedText4()
    {
        final int width = 5;
        final int padding = 12;
        final String text = "Th\u000bisis\natest. \n";
        final String expected = "Th\u000Bis" + EOL + " is" + EOL + " ates" + EOL + " t.";

        final StringBuffer sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected, sb.toString());
    }

    public void testPrintHelp4(){
        PrintStream originalOut = System.out;

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));

        Options options = new Options();
        Option op1 = new Option("s", "size", true, "the size");
        op1.setArgName("SIZE");
        Option op2 = new Option("", "age", true, "the age");
        options.addOption( "f", true, "the file" );
        options.addOption(op1);
        options.addOption(op2);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("foobar", options);

        System.setOut(originalOut);

        assertEquals("usage: foobar" + EOL +
                " -,--age <arg>      the age" + EOL +
                " -f <arg>           the file" + EOL +
                " -s,--size <SIZE>   the size" + EOL, outContent.toString());
    }

    public void testPrintHelp5(){
        PrintStream originalOut = System.out;

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));

        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("foobar", options, true);

        System.setOut(originalOut);

        assertEquals("usage: foobar" + EOL + EOL, outContent.toString());
    }

    public void testPrintHelp6(){
        PrintStream originalOut = System.out;

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));

        Options options = new Options();
        Option op1 = new Option("s", "size", true, "the size");
        op1.setArgName("");
        options.addOption(op1);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("foobar", "header", options, "footer");

        System.setOut(originalOut);

        assertEquals("usage: foobar" + EOL +
                "header" + EOL +
                " -s,--size    the size" + EOL +
                "footer" + EOL , outContent.toString());
    }

    public void testPrintOptionGroupUsage2()
    {
        OptionGroup group = new OptionGroup();
        group.addOption(OptionBuilder.create("2b"));
        group.addOption(OptionBuilder.create("4a"));
        group.addOption(OptionBuilder.create("1c"));

        Options options = new Options();
        options.addOptionGroup(group);

        StringWriter out = new StringWriter();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(new PrintWriter(out), 80, "app", options);

        assertEquals("usage: app [-1c | -2b | -4a]" + EOL, out.toString());
    }

    public void testPrintHelp7(){
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(10, formatter.findWrapPos("This is a\n test.", 12, 0));
    }

    public void testRenderWrappedText5()
    {
        final int width = 5;
        final int padding = 12;
        final String text = "Thisisatest.";
        final String expected = "Thisi" + EOL + " sate" + EOL + " st.";

        final StringBuffer sb = new StringBuffer();
        assertNotNull(new HelpFormatter().renderWrappedText(sb, width, padding, text));
    }

    public void testRenderOptions(){
        final StringBuffer sb = new StringBuffer();
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();

        assertNotNull(formatter.renderOptions(sb, 5, options, 1, 2));
    }

    public void testRenderOption2(){
        final StringBuffer sb = new StringBuffer();
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();

        assertNotNull(formatter.renderWrappedText(sb, 200, 7, "usage: foobar"));
    }

}

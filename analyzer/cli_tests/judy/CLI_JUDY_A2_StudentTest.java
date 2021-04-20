package org.apache.commons.cli;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class CLI_JUDY_A2_StudentTest extends TestCase {

    private final PrintStream standardOut = System.out;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private HelpFormatter formatter;

    protected void setUp() throws Exception {
        super.setUp();
        System.setOut(new PrintStream(outputStreamCaptor));
        formatter = new HelpFormatter();
    }

    public void testPrintHelpWith2Parameters() {
        formatter.printHelp("foobar", new Options());
        String result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar", result);
        outputStreamCaptor.reset();

        Options options = new Options().addOption("MyOpt", false, "My description");
        formatter.printHelp("foobar", options);
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\n -MyOpt   My description", result);

        try{
            formatter.printHelp(null, null);
            fail("Missing exception");
        } catch(Exception exception){
            // expected
        }
    }

    /**
     *  Before the introduction of this test case:
     *      "mutantsCount": 1057,
     *      "mutantsKilledCount": 524,
     *   Mutant operator addressed:
     *  "name": "OMR",
     *  "description": "change overloading method"
     */
    public void testPrintHelpWith3Parameters() {
        formatter.printHelp("foobar", new Options(), true);
        String result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar", result);

        outputStreamCaptor.reset();

        Options options = new Options().addOption("MyOpt", false, "My description");
        formatter.printHelp("foobar", options, false);
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\n -MyOpt   My description", result);

        try{
            formatter.printHelp(null, null, false);
            fail("Missing exception");
        } catch(Exception exception){
            // expected
        }
    }

    /** Before the introduction of this test case:
     *      "mutantsCount": 1057,
     *      "mutantsKilledCount": 663,
     *  Mutant operator addressed:
     * "name": "OAC", 743
     * "description": "change order or number of arguments in method invocations"
     */
    public void testPrintHelpWith4Parameters() {
        formatter.printHelp("foobar","Header",  new Options(), "Footer");
        String result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\nHeader\n\nFooter", result);

        outputStreamCaptor.reset();
        // 0 mutants killed
        Options options = new Options().addOption("MyOpt", false, "My description");
        formatter.printHelp("foobar",null,  options, null);
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\n -MyOpt   My description", result);
    }

    /** Before the introduction of this test case:
     *      "mutantsCount": 1057,
     *      "mutantsKilledCount": 712,
     *  Mutant operator addressed:
     * "name": "OAC",
     * "description": "change order or number of arguments in method invocations"
     */
    public void testPrintHelpWith5Parameters() {
        formatter.printHelp("foobar","Header",  new Options(), "Footer", true);
        String result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\nHeader\n\nFooter", result);

        outputStreamCaptor.reset();
        // zero mutants killed
        Options options = new Options().addOption("MyOpt", false, "My description");
        formatter.printHelp("foobar",null,  options, null, false);
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\n -MyOpt   My description", result);

        outputStreamCaptor.reset();
        formatter.printHelp(74,"foobar","Header",  new Options(), "Footer");
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\nHeader\n\nFooter", result);

        outputStreamCaptor.reset();

        formatter.printHelp(Integer.MAX_VALUE,"foobar","Header",  new Options(), "Footer");
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\nHeader\n\nFooter", result);

        outputStreamCaptor.reset();

        /*outputStreamCaptor.reset();
        formatter.printHelp("foobar", 74, "Footer",  "Header", new Options());
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\nHeader\n\nFooter", result);*/

        try{
            formatter.printHelp(Integer.MIN_VALUE,"foobar",null,  new Options(), null);
            fail("Missing exception!");
        }catch (Exception exception){
            // expected
        }
    }

    /** Before the introduction of this test case:
     *      "mutantsCount": 1057,
     *      "mutantsKilledCount": 738,
     *  Mutant operator addressed:
     * "name": "OAC",
     * "description": "change order or number of arguments in method invocations"
     */
    public void testPrintHelpWith6Parameters() {
        formatter.printHelp(74,"foobar","Header",  new Options(), "Footer", true);
        String result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\nHeader\n\nFooter", result);

        outputStreamCaptor.reset();

        formatter.printHelp(Integer.MAX_VALUE,"foobar",null,  new Options(), null, true);
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar", result);
    }

    /** Before the introduction of this test case:
     *      "mutantsCount": 1057,
     *      "mutantsKilledCount": 739,
     *  Mutant operator addressed:
     * "name": "OAC",
     * "description": "change order or number of arguments in method invocations"
     */
    public void testPrintHelpWith8Parameters() {
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 74,"foobar","Header",  new Options(), 1, 3, "Footer");
        pw.flush();
        String result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\nHeader\n\nFooter", result);

        try{
            formatter.printHelp(pw, 74,"foobar","Header",  new Options(), Integer.MAX_VALUE, Integer.MAX_VALUE, "Footer");
            fail("Missing exception");
        }catch (OutOfMemoryError exception){
            // expected
        }

    }

    /** Before the introduction of this test case:
     *      "mutantsCount": 1057,
     *      "mutantsKilledCount": 739,
     *  Mutant operator addressed:
     * "name": "OAC",
     * "description": "change order or number of arguments in method invocations"
     */
    public void testPrintHelpWith9Parameters() {
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 74,"foobar","Header",  new Options(), 1, 3, "Footer", true);
        pw.flush();
        String result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar\nHeader\n\nFooter", result);

        outputStreamCaptor.reset();

        Option option1 = new Option("shortOpt1", "looooong Opt", true, "My option description");
        Option option2 = new Option("shortOpt2", "looooong Opt", true, "My option description");
        option1.setRequired(true);
        option2.setRequired(true);

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        group.setRequired(true);

        Options options = new Options();
        options.addOptionGroup(group);

        Option option3 = new Option("shortOpt3", "looooong Opt", false, "My option description");
        Option option4 = new Option(null, "looooong Opt", true, "My option description");
        option1.setRequired(false);
        option2.setRequired(false);

        OptionGroup group2 = new OptionGroup();
        group.addOption(option3);
        group.addOption(option4);
        group.setRequired(false);

        options.addOptionGroup(group2);

        Option option5 = new Option("shortOpt5", "looooong Opt", true, "My option description");
        option5.setArgName("");
        Option option6 = new Option("shortOpt6", "looooong Opt", true, "My option description");
        option6.setArgName("ArgName");
        option1.setRequired(true);
        option2.setRequired(false);
        options.addOption(option5);
        options.addOption(option6);

        formatter.printHelp(pw, 74,"foobar","Header",  options, 1, 3, "Footer", true);
        pw.flush();
        result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar [--looooong Opt <arg> | -shortOpt1 <arg> | -shortOpt2 <arg>\n" +
                "       | -shortOpt3]  [-shortOpt5] [-shortOpt6 <ArgName>]\n" +
                "Header\n" +
                " -shortOpt1,--looooong Opt <arg>       My option description\n" +
                " -shortOpt2,--looooong Opt <arg>       My option description\n" +
                " -shortOpt5,--looooong Opt             My option description\n" +
                " -shortOpt6,--looooong Opt <ArgName>   My option description\n" +
                "Footer", result);
    }



    public void testRenderWrappedText() {
        StringBuffer sb = new StringBuffer();
        sb = formatter.renderWrappedText(sb, 4, 0 , "testtesttesttesttest");

        assertEquals("test\ntest\ntest\ntest\ntest", sb.toString());

        sb = formatter.renderWrappedText(sb, 4, 3 , "testtesttesttesttest");

        assertEquals("test\n" +
                "test\n" +
                "test\n" +
                "test\n" +
                "testtest\n" +
                "   t\n" +
                "   e\n" +
                "   s\n" +
                "   t\n" +
                "   t\n" +
                "   e\n" +
                "   s\n" +
                "   t\n" +
                "   t\n" +
                "   e\n" +
                "   s\n" +
                "   t\n" +
                "   t\n" +
                "   e\n" +
                "   s\n" +
                "   t", sb.toString());
    }

    public void testPrintUsage() {
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printUsage(pw, 20, "foobar");
        pw.flush();
        String result = outputStreamCaptor.toString().trim();

        assertEquals("usage: foobar", result);

        outputStreamCaptor.reset();

        formatter.printUsage(pw, Integer.MAX_VALUE, "");
        pw.flush();
        result = outputStreamCaptor.toString().trim();

        assertEquals("usage:", result);
    }

    public void testPrintUsage2() {
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printUsage(pw, 20, "foobar", new Options());
        pw.flush();
        String result = outputStreamCaptor.toString().trim();
        assertEquals("usage: foobar", result);

        outputStreamCaptor.reset();
        Option option1 = new Option("shortOpt1", "looooong Opt", true, "My option description");
        option1.setRequired(true);
        option1.setArgName("");

        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.setRequired(true);

        Options options = new Options();
        options.addOptionGroup(group);

        Option option2 = new Option("shortOpt6", "looooong Opt", true, "My option description");
        option2.setArgName("ArgName");
        option2.setRequired(false);
        options.addOption(option2);
        formatter.printUsage(pw, Integer.MAX_VALUE, "", options);
        pw.flush();
        result = outputStreamCaptor.toString().trim();

        assertEquals("usage:  -shortOpt1 [-shortOpt6 <ArgName>]", result);
    }

    public void testPrintOptions() {
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printOptions(pw, 74,new Options(), 1, 3);
        pw.flush();
        String result = outputStreamCaptor.toString().trim();
        assertEquals("", result);
        outputStreamCaptor.reset();


        try{
            formatter.printOptions(pw, 74,null, 1, 3);
            fail("Missing exception");
        }catch (Exception exception){
            // expected
        }
    }

    // 757
    public void testPrintWrapped() {
        // Calling overloading method of printWrapped
        // with 3 parameters:
        PrintWriter pw = new PrintWriter(System.out);
        formatter.printWrapped(pw, 74,"header");
        pw.flush();
        String result = outputStreamCaptor.toString().trim();
        assertEquals("header", result);
        outputStreamCaptor.reset();

        // with 4 parameters:
        pw = new PrintWriter(System.out);
        formatter.printWrapped(pw, 74, 1,"header");
        pw.flush();
        result = outputStreamCaptor.toString().trim();
        assertEquals("header", result);
    }

    public void testRenderOptions() {
        StringBuffer sb = new StringBuffer();
        formatter.renderOptions(sb, 20, new Options(), 10, 10);
        assertEquals("", sb.toString());

        Options options = new Options().addOption("test", false, "a simple description");
        formatter.renderOptions(sb, 60, options, 1, 3);
        assertEquals(" -test   a simple description", sb.toString());

        try {
            formatter.renderOptions(null, Integer.MIN_VALUE, null, Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail("missing exception");
        }catch (Exception exception){
            // expected
        }

        try {
            formatter.renderOptions(null, Integer.MAX_VALUE, null, Integer.MIN_VALUE, Integer.MAX_VALUE);
            fail("missing exception");
        }catch (Exception exception){
            // expected
        }

    }

    public void testTestRenderWrappedText() {
        int width = 4;
        int padding = 0;
        String text = "This is a test text.";
        String expected = "This\nis a\ntest\ntext\n.";

        StringBuffer sb = new StringBuffer();
        formatter.renderWrappedText(sb, width, padding, text);
        assertEquals(expected, sb.toString());
    }


    public void testFindWrapPos() {
        int result = formatter.findWrapPos("fool", 2, 0);
        assertEquals(2, result);

        result = formatter.findWrapPos("\nfool", 2, 0);
        assertEquals(1, result);

        result = formatter.findWrapPos("\tfool", 2, 0);
        assertEquals(1, result);

        result = formatter.findWrapPos("\rfool", 2, 0);
        assertEquals(2, result);

        result = formatter.findWrapPos("fool", 2, 2);
        assertEquals(-1, result);

        result = formatter.findWrapPos("fool", Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(-2, result);

        result = formatter.findWrapPos("this is supposed to be some long testing text.", 4, 4);
        assertEquals(7, result);

        try {
            formatter.findWrapPos(null, Integer.MIN_VALUE, Integer.MIN_VALUE);
            fail("missing exception");
        }catch (Exception exception){
            // expected
        }
    }

    public void testCreatePadding() {
        String result = formatter.createPadding(0);
        assertEquals("", result);

        result = formatter.createPadding(5);
        assertEquals("     ", result);
        try {
            formatter.createPadding(-1);
            fail("missing exception");
        }catch (NegativeArraySizeException exception){
            // expected
        }
    }

    public void testRtrim() {
        String result = formatter.rtrim(null);
        assertEquals(null, result);

        result = formatter.rtrim("");
        assertEquals("", result);

        result = formatter.rtrim("test test               ");
        assertEquals("test test", result);

        result = formatter.rtrim("test test test test                                       ");
        assertEquals("test test test test", result);
    }

    public void testGettersAndSetters() {
        formatter.setArgName("arg name");
        assertEquals("arg name", formatter.getArgName());
        formatter.setDescPadding(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, formatter.getDescPadding());
        formatter.setLeftPadding(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, formatter.getLeftPadding());
        formatter.setLongOptPrefix("---------------------------------------------");
        assertEquals("---------------------------------------------", formatter.getLongOptPrefix());
        formatter.setNewLine("\n");
        assertEquals("\n", formatter.getNewLine());
        formatter.setOptPrefix("----------");
        assertEquals("----------", formatter.getOptPrefix());
        formatter.setSyntaxPrefix("##");
        assertEquals("##", formatter.getSyntaxPrefix());
        formatter.setWidth(10);
        assertEquals(10, formatter.getWidth());
        formatter.setOptionComparator(null);
        assertNotNull(formatter.getOptionComparator());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        System.setOut(standardOut);
    }
}

package org.apache.commons.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.TestCase;

public class CLI_JUMBLE_B3_StudentTest extends TestCase {
    private static final String EOL = System.getProperty("line.separator");

    public void testPrintUsageStudent() throws Exception {
        HelpFormatter helpFormatter = new HelpFormatter();
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(bytesOut);
        helpFormatter.printUsage(printWriter, 30, "app arg1 arg2asdasdasdasdasdasd arg3 arg4");
        printWriter.close();
        String s = "usage: app arg1\n" +
                   "           arg2asdasdasdasdasd\n" +
                   "           asd arg3 arg4";

        assertTrue((s + EOL).equals(bytesOut.toString()));
    }

    /*
    public void testFindWrapPos() throws Exception {
        HelpFormatter helpFormatter = new HelpFormatter();
        String text = "This is a \t tab.";
        // System.out.println(helpFormatter.findWrapPos(text, 8, 0));
        assertEquals(7, helpFormatter.findWrapPos(text, 8, 0));

        //Line 908
        text = "\t tab";
        assertEquals(1, helpFormatter.findWrapPos(text, 10, 0));
        
        //line 924
        char n = '\n';
        text  = "12345678901" + n + "x" + "78901";
        assertEquals(11, helpFormatter.findWrapPos(text, 10, 5));

        n = '\r';
        text  = "12345678901" + n + "x" + "78901";
        assertEquals(11, helpFormatter.findWrapPos(text, 10, 5));

        //line 939
        text  = "1234567890";
        System.out.println("Here");
        System.out.println(helpFormatter.findWrapPos(text, 10, 0));
        assertEquals(5, helpFormatter.findWrapPos(text, 10, 0));
    }
    */

    public void testConstants() throws Exception {
        HelpFormatter helpFormatter = new HelpFormatter();

        // Line 75
        assertEquals(helpFormatter.getWidth(), helpFormatter.DEFAULT_WIDTH);

        // Line 83
        assertEquals(helpFormatter.getLeftPadding(), helpFormatter.DEFAULT_LEFT_PAD);

        // Line 92
        assertEquals(helpFormatter.getDescPadding(), helpFormatter.DEFAULT_DESC_PAD);
    }

    public void testPrintHelp() throws Exception {
        // related to Bugzilla #27635 (CLI-26)
        Option help = new Option("h", "help", false, "print this message");
        Option version = new Option("v", "version", false, "print version information");
        Option newRun = new Option("n", "new", false, "Create NLT cache entries only for new items");
        Option trackerRun = new Option("t", "tracker", false, "Create NLT cache entries only for tracker items");

        Option timeLimit = OptionBuilder.withLongOpt("limit").hasArg().withValueSeparator()
                .withDescription("Set time limit for execution, in mintues").create("l");

        Option age = OptionBuilder.withLongOpt("age").hasArg().withValueSeparator()
                .withDescription("Age (in days) of cache item before being recomputed").create("a");

        Option server = OptionBuilder.withLongOpt("server").hasArg().withValueSeparator()
                .withDescription("The NLT server address").create("s");

        Option numResults = OptionBuilder.withLongOpt("results").hasArg().withValueSeparator()
                .withDescription("Number of results per item").create("r");

        Option configFile = OptionBuilder.withLongOpt("config").hasArg().withValueSeparator()
                .withDescription("Use the specified configuration file").create();

        Options mOptions = new Options();
        mOptions.addOption(help);
        mOptions.addOption(version);
        mOptions.addOption(newRun);
        mOptions.addOption(trackerRun);
        mOptions.addOption(timeLimit);
        mOptions.addOption(age);
        mOptions.addOption(server);
        mOptions.addOption(numResults);
        mOptions.addOption(configFile);

        HelpFormatter formatter = new HelpFormatter();
        final String EOL = System.getProperty("line.separator");

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        formatter.printHelp("commandline", mOptions);

        assertEquals(
                "usage: commandline" + EOL + " -a,--age <arg>       Age (in days) of cache item before being recomputed"
                        + EOL + "    --config <arg>    Use the specified configuration file" + EOL
                        + " -h,--help            print this message" + EOL
                        + " -l,--limit <arg>     Set time limit for execution, in mintues" + EOL
                        + " -n,--new             Create NLT cache entries only for new items" + EOL
                        + " -r,--results <arg>   Number of results per item" + EOL
                        + " -s,--server <arg>    The NLT server address" + EOL
                        + " -t,--tracker         Create NLT cache entries only for tracker items" + EOL
                        + " -v,--version         print version information" + EOL,
                out.toString());

    }

    public void testPrintHelp1() throws Exception {
        // related to Bugzilla #27635 (CLI-26)
        Option help = new Option("h", "help", false, "print this message");
        Option version = new Option("v", "version", false, "print version information");
        Option newRun = new Option("n", "new", false, "Create NLT cache entries only for new items");
        Option trackerRun = new Option("t", "tracker", false, "Create NLT cache entries only for tracker items");

        Option timeLimit = OptionBuilder.withLongOpt("limit").hasArg().withValueSeparator()
                .withDescription("Set time limit for execution, in mintues").create("l");

        Option age = OptionBuilder.withLongOpt("age").hasArg().withValueSeparator()
                .withDescription("Age (in days) of cache item before being recomputed").create("a");

        Option server = OptionBuilder.withLongOpt("server").hasArg().withValueSeparator()
                .withDescription("The NLT server address").create("s");

        Option numResults = OptionBuilder.withLongOpt("results").hasArg().withValueSeparator()
                .withDescription("Number of results per item").create("r");

        Option configFile = OptionBuilder.withLongOpt("config").hasArg().withValueSeparator()
                .withDescription("Use the specified configuration file").create();

        Options mOptions = new Options();
        mOptions.addOption(help);
        mOptions.addOption(version);
        mOptions.addOption(newRun);
        mOptions.addOption(trackerRun);
        mOptions.addOption(timeLimit);
        mOptions.addOption(age);
        mOptions.addOption(server);
        mOptions.addOption(numResults);
        mOptions.addOption(configFile);

        HelpFormatter formatter = new HelpFormatter();
        final String EOL = System.getProperty("line.separator");

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        formatter.printHelp("create", "header", mOptions, "footer");
        assertEquals("usage: create" + EOL + "header" + EOL
                + " -a,--age <arg>       Age (in days) of cache item before being recomputed" + EOL
                + "    --config <arg>    Use the specified configuration file" + EOL
                + " -h,--help            print this message" + EOL
                + " -l,--limit <arg>     Set time limit for execution, in mintues" + EOL
                + " -n,--new             Create NLT cache entries only for new items" + EOL
                + " -r,--results <arg>   Number of results per item" + EOL
                + " -s,--server <arg>    The NLT server address" + EOL
                + " -t,--tracker         Create NLT cache entries only for tracker items" + EOL
                + " -v,--version         print version information" + EOL + "footer" + EOL, out.toString());
    }


    //Line 757
    public void testPrintHelp2() throws Exception {
        // related to Bugzilla #27635 (CLI-26)
        Option help = new Option("h", "help", false, "print this message");
        Option version = new Option("v", "version", false, "print version information");
        Option newRun = new Option("n", "new", false, "Create NLT cache entries only for new items");
        Option trackerRun = new Option("t", "tracker", false, "Create NLT cache entries only for tracker items");

        Option timeLimit = OptionBuilder.withLongOpt("limit").hasArg().withValueSeparator()
                .withDescription("Set time limit for execution, in mintues").create("l");

        Option age = OptionBuilder.withLongOpt("age").hasArg().withValueSeparator()
                .withDescription("Age (in days) of cache item before being recomputed").create("a");

        Option server = OptionBuilder.withLongOpt("server").hasArg().withValueSeparator()
                .withDescription("The NLT server address").create("s");

        Option numResults = OptionBuilder.withLongOpt("results").hasArg().withValueSeparator()
                .withDescription("Number of results per item").create("r");

        Option configFile = OptionBuilder.withLongOpt("config").hasArg().withValueSeparator()
                .withDescription("Use the specified configuration file").create();

        Options mOptions = new Options();
        mOptions.addOption(help);
        mOptions.addOption(version);
        mOptions.addOption(newRun);
        mOptions.addOption(trackerRun);
        mOptions.addOption(timeLimit);
        mOptions.addOption(age);
        mOptions.addOption(server);
        mOptions.addOption(numResults);
        mOptions.addOption(configFile);

        HelpFormatter formatter = new HelpFormatter();
        final String EOL = System.getProperty("line.separator");

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        formatter.printHelp(formatter.getWidth(), "create", "header", mOptions, "footer");
        assertEquals("usage: create" + EOL + "header" + EOL
                + " -a,--age <arg>       Age (in days) of cache item before being recomputed" + EOL
                + "    --config <arg>    Use the specified configuration file" + EOL
                + " -h,--help            print this message" + EOL
                + " -l,--limit <arg>     Set time limit for execution, in mintues" + EOL
                + " -n,--new             Create NLT cache entries only for new items" + EOL
                + " -r,--results <arg>   Number of results per item" + EOL
                + " -s,--server <arg>    The NLT server address" + EOL
                + " -t,--tracker         Create NLT cache entries only for tracker items" + EOL
                + " -v,--version         print version information" + EOL + "footer" + EOL, out.toString());
    }

    public void testRenderOptions() throws Exception {        
        Option age = OptionBuilder.withLongOpt("age").hasArg().withValueSeparator()
        .withDescription("Age (in days) of cache item before being recomputed").create("a");
        age.setArgName("");

        Options mOptions = new Options();
        mOptions.addOption(age);

        HelpFormatter formatter = new HelpFormatter();
        
        StringWriter out = new StringWriter();

        formatter.printOptions(new PrintWriter(out), 80, mOptions, formatter.getLeftPadding(), formatter.getDescPadding());

        assertEquals(" -a,--age    Age (in days) of cache item before being recomputed" + EOL 
        , out.toString());
    }

    //Line 862
    public void testNextLineTabStop() throws Exception {
        HelpFormatter formatter = new HelpFormatter();
        
        StringWriter out = new StringWriter();

        formatter.printWrapped(new PrintWriter(out), 10, 11, "12345678901234567890");

        assertEquals("1234567890" + EOL 
            + " 123456789"  + EOL
            + " 0" + EOL
        , out.toString());
    }
}
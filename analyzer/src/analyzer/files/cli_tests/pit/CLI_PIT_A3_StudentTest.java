package org.apache.commons.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;
import java.io.PrintStream;
import java.lang.StringBuffer;

import junit.framework.TestCase;
import org.junit.*;
import org.junit.Assert;

public class CLI_PIT_A3_StudentTest extends TestCase
{

    private static final String EOL = System.getProperty("line.separator");

    
    //Testing all of the variables in the class
    public void testAllConstants() throws Exception{
        HelpFormatter fm = new HelpFormatter();

        Assert.assertEquals(fm.defaultWidth, HelpFormatter.DEFAULT_WIDTH);
        Assert.assertNotEquals(fm.defaultWidth + 1, HelpFormatter.DEFAULT_WIDTH);
        Assert.assertNotEquals(fm.defaultWidth - 1, HelpFormatter.DEFAULT_WIDTH);


        Assert.assertEquals(fm.defaultLeftPad, HelpFormatter.DEFAULT_LEFT_PAD);
        Assert.assertNotEquals(fm.defaultLeftPad + 1, HelpFormatter.DEFAULT_LEFT_PAD);
        Assert.assertNotEquals(fm.defaultLeftPad - 1, HelpFormatter.DEFAULT_LEFT_PAD);

        Assert.assertEquals(fm.defaultDescPad, HelpFormatter.DEFAULT_DESC_PAD);
        Assert.assertNotEquals(fm.defaultDescPad + 1, HelpFormatter.DEFAULT_DESC_PAD);
        Assert.assertNotEquals(fm.defaultDescPad - 1, HelpFormatter.DEFAULT_DESC_PAD);
    }

    public void testFirstMutatedPrint() throws Exception{
        final PrintStream standardOut= System.out;
        final ByteArrayOutputStream captor = new ByteArrayOutputStream();

        System.setOut(new PrintStream(captor));

        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.defaultWidth = 10;
        formatter.printHelp("teste", options);

        assertEquals(
                "usage:"+EOL+"       tes"+EOL+"       te" + EOL +""+ EOL
                , captor.toString()
        );

        System.setOut(standardOut);

    }


    // public void testFirstMutatedPrintTry2() throws Exception{
    //     final PrintStream standardOut= System.out;
    //     final ByteArrayOutputStream captor = new ByteArrayOutputStream();

    //     System.setOut(new PrintStream(captor));

    //     Options options = new Options();
    //     HelpFormatter formatter = new HelpFormatter();
    //     formatter.defaultWidth = 1;
    //     formatter.printHelp("teste", options);

    //     assertEquals(
    //             "usage:"+EOL+"       tes"+EOL+"       te" + EOL +""+ EOL
    //             , captor.toString()
    //     );

    //     System.setOut(standardOut);

    // }

    

    public void testSecondMutatedPrint1() throws Exception{
        final PrintStream standardOut= System.out;
        final ByteArrayOutputStream captor = new ByteArrayOutputStream();

        System.setOut(new PrintStream(captor));

        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.defaultWidth = 10;
        formatter.printHelp("teste", options, true);

        assertEquals(
                "usage:"+EOL+"       tes"+EOL+"       te" + EOL +""+ EOL
                , captor.toString()
        );

        System.setOut(standardOut);

    }

    

    public void testThirdMutatedPrint() throws Exception{
        final PrintStream standardOut= System.out;
        final ByteArrayOutputStream captor = new ByteArrayOutputStream();

        System.setOut(new PrintStream(captor));

        Options options = new Options();
        options.addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("teste", "Pikachu", options, "raichu");

        String expected = "usage: teste"+EOL + "Pikachu"+ EOL +" -a   aaaa aaaa aaaa aaaa aaaa" + EOL + "raichu"+EOL;

        assertEquals(
                expected
                , captor.toString()
        );

        System.setOut(standardOut);

    }

    public void testFourthMutatedPrint() throws Exception{
        final PrintStream standardOut= System.out;
        final ByteArrayOutputStream captor = new ByteArrayOutputStream();

        System.setOut(new PrintStream(captor));

        Options options = new Options();
        options.addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(formatter.defaultWidth, "teste", "Pikachu", options, "raichu");

        String expected = "usage: teste"+EOL + "Pikachu"+ EOL +" -a   aaaa aaaa aaaa aaaa aaaa" + EOL + "raichu"+EOL;

        assertEquals(
                expected
                , captor.toString()
        );

        System.setOut(standardOut);

    }

    // public void testKillTheSort() throws Exception {

    //     OptionGroup group = new OptionGroup();
    //     group.addOption(OptionBuilder.create("b"));
    //     group.addOption(OptionBuilder.create("a"));

    //     Options options = new Options();
    //     options.addOptionGroup(group);

    //     StringWriter out = new StringWriter();

    //     HelpFormatter formatter = new HelpFormatter();
    //     formatter.printUsage(new PrintWriter(out), 80, "app", options);

    //     String expected = "usage: app [-a | -b]" + EOL;

    //     Assert.assertEquals(expected, out.toString());
    
    // }

    

    public void testFnallyKilledTheSort()
    {
        OptionGroup group = new OptionGroup();
        group.addOption(OptionBuilder.create("a"));
        group.addOption(OptionBuilder.create("c"));
        group.addOption(OptionBuilder.create("b"));

        OptionGroup group2 = new OptionGroup();
        group2.addOption(OptionBuilder.create("d"));
        group2.addOption(OptionBuilder.create("t"));
        group2.addOption(OptionBuilder.create("f"));
        
        group.setRequired(true);

        

        Options options = new Options();
        options.addOptionGroup(group);
        options.addOptionGroup(group2);
        options.addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");
        StringWriter out = new StringWriter();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(new PrintWriter(out), 80, "app", options);

        assertEquals("usage: app -a | -b | -c   [-d | -f | -t]" + EOL, out.toString());
    }

    public void testKillingTheUsage()
    {
        OptionGroup group = new OptionGroup();
        group.addOption(OptionBuilder.create("a"));
        group.addOption(OptionBuilder.create("c"));
        group.addOption(OptionBuilder.create("b"));

        OptionGroup group2 = new OptionGroup();
        group2.addOption(OptionBuilder.create("d"));
        group2.addOption(OptionBuilder.create("t"));
        group2.addOption(OptionBuilder.create("f"));
        
        group.setRequired(true);

        

        Options options = new Options();
        options.addOptionGroup(group);
        options.addOptionGroup(group2);

        StringWriter out = new StringWriter();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(new PrintWriter(out), 20, "12345678901234567890 ");

        assertEquals("usage:"+ EOL + " 1234567890123456789" +EOL+ " 0" +EOL , out.toString());
    }

    public void testKillTheWrapped()
    {
       
        StringBuffer result = new StringBuffer();

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        Option option = new Option("a", true, "aaaa aaaa aaaa aaaa aaaa");
        option.setArgName("");
        options.addOption(option );

        StringBuffer sb = formatter.renderOptions(result, 50, options, 0, 0);

        assertEquals("-a aaaa aaaa aaaa aaaa aaaa" , result.toString());
        assertNotNull(sb);
    }

    public void testKilling800Mutant()
    {
        StringBuffer sb = new StringBuffer();

        HelpFormatter formatter = new HelpFormatter();
        StringBuffer meh = formatter.renderWrappedText(sb, 20, 20, "Bla");

        assertNotNull(meh);
        assertEquals("Bla", meh.toString());
    }

    public void testKilling800Mutant2()
    {
        StringBuffer sb = new StringBuffer();

        HelpFormatter formatter = new HelpFormatter();
        StringBuffer meh = formatter.renderWrappedText(sb, 4, 4, "Blafsdfsdfdsfds");

        assertNotNull(meh);
        assertEquals("Blaf"+EOL+" sdf"+EOL+" sdf"+ EOL+ " dsf"+EOL+" ds", meh.toString());
    }

    public void testKilling800Mutant3()
    {
        // single line padded text 2
   

        String text = "Why's the rum gone pikachu pokemon1";

        int padding = 10;

        int width = 18;

        String expected ="Why's the rum gone" +EOL+ "          pikachu"+EOL+ "          pokemon1";
        
        StringBuffer sb = new StringBuffer();

        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("single line padded text 2", expected, sb.toString());
    }


    public void testFindPosition()
    {

        String text2 = "Test\t";

        int width = 4;

        int tristeza = new HelpFormatter().findWrapPos(text2, width, 0);

        assertEquals(tristeza, 5);
        Assert.assertNotEquals(tristeza, 4);
        Assert.assertNotEquals(tristeza, 6);      
        
        text2 = "Test\n";

        tristeza = new HelpFormatter().findWrapPos(text2, width, 0);

        assertEquals(tristeza, 5);
        Assert.assertNotEquals(tristeza, 4);
        Assert.assertNotEquals(tristeza, 6);  

        text2 = " asdf";

        tristeza = new HelpFormatter().findWrapPos(text2, width, 0);

        assertEquals(tristeza, 4);
        
        text2 = "a aa";

        tristeza = new HelpFormatter().findWrapPos(text2, width, 0);

        assertEquals(tristeza, -1);
        Assert.assertNotEquals(tristeza, 2);

    }

    public void testFindPositionKilling()
    {

        String text2 = "Test";

        int width = 4;

        int tristeza = new HelpFormatter().findWrapPos(text2, width, 0);

        assertEquals(tristeza, -1);
 

    }

    public void testFindPositionKillingFu()
    {

        char car = (char) 11;
        String text2 = "Te"+car+"ste3232";

        int width = 5;

        int tristeza = new HelpFormatter().findWrapPos(text2, width, 0);

        assertEquals(tristeza, 5);


        text2 = "Tes\rte";

        tristeza = new HelpFormatter().findWrapPos(text2, width, 0);

        assertEquals(tristeza, 3);
 
    }

}

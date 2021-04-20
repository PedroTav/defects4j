/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;

import junit.framework.TestCase;

/** 
 * Test case for the HelpFormatter class 
 *
 * @author Slawek Zachcial
 * @author John Keyes ( john at integralsource.com )
 * @author brianegge
 */
public class CLI_JUMBLE_A5_StudentTest extends TestCase
{

    private static final String EOL = System.getProperty("line.separator");

    //Mutant1: org.apache.commons.cli.HelpFormatter:505: CP[30] "cmdLineSyntax not provided" -> "___jumble___"
    public void test1() throws Exception
    {
        try
        {
            HelpFormatter hf = new HelpFormatter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(out);
            int width = 0;
            String cmdLineSyntax = null;
            String header = "";
            Options options = null;
            int leftPad = 0;
            int descPad = 0;
            String footer = "";
            boolean autoUsage = false;

            hf.printHelp(pw, width, cmdLineSyntax, header, options, leftPad, descPad, footer,  autoUsage);
        }
        catch (IllegalArgumentException e)
        {
            String message = "cmdLineSyntax not provided";
            //IllegalArgumentException expected =  new IllegalArgumentException(message);
            assertEquals(message, e.getMessage());
        }
    }

    //Mutant2: org.apache.commons.cli.HelpFormatter:75: 74 (J) -> 75 (K)
    public void test2()
    {
        HelpFormatter hf = new HelpFormatter();
        assertEquals(74, hf.getWidth());
    }

    //Mutant3 HelpFormatter:75: removed assignment to defaultWidth
    public void test3()
    {
        HelpFormatter hf = new HelpFormatter();
        String cmdLineSyntax = "commandline";
        Option help = new Option("h", "help", false, "print this message");
        Options mOptions = new Options();
        mOptions.addOption(help);
        hf.printHelp(cmdLineSyntax, mOptions);

        String expected = "usage: commandline\n" + " -h,--help   print this message";
    }

    //Mutant6: org.apache.commons.cli.HelpFormatter:92: 3 -> 4
    //Mutant7: org.apache.commons.cli.HelpFormatter:92: removed assignment to defaultDescPad
    public void test4()
    {

        HelpFormatter hf = new HelpFormatter();
        assertEquals(3, hf.getDescPadding());
    }

    //Mutant4: org.apache.commons.cli.HelpFormatter:83: 1 -> 0
    //Mutant5: org.apache.commons.cli.HelpFormatter:83: removed assignment to defaultLeftPad
    public void test5()
    {
        HelpFormatter hf = new HelpFormatter();
        assertEquals(1, hf.getLeftPadding());
    }

    //Mutant8: org.apache.commons.cli.HelpFormatter:191: removed assignment to defaultDescPad
    public void test6()
    {
        HelpFormatter hf = new HelpFormatter();
        hf.setDescPadding(50);
        assertEquals(50, hf.getDescPadding());
    }

    //Mutant9: org.apache.commons.cli.HelpFormatter:231: removed assignment to defaultNewLine
    public void test7()
    {
        HelpFormatter hf = new HelpFormatter();
        hf.setNewLine(System.getProperty("line"));
        assertEquals(System.getProperty("line"), hf.getNewLine());
    }

    //Mutant10: org.apache.commons.cli.HelpFormatter:349: removed assignment to optionComparator
    public void test8()
    {
        Options opts = new Options();
        opts.addOption(new Option("a", "first"));
        opts.addOption(new Option("b", "second"));
        opts.addOption(new Option("c", "third"));

        HelpFormatter helpFormatter = new HelpFormatter();
        Comparator optionComparator = helpFormatter.getOptionComparator();
        helpFormatter.setOptionComparator(null);
        assertNotSame(optionComparator, helpFormatter.getOptionComparator());
    }

    //Mutant11: org.apache.commons.cli.HelpFormatter:367: 0 -> 1
    public void test9()
    {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        HelpFormatter hf = new HelpFormatter();
        String cmdLineSyntax = "commandline";
        Option optionA = new Option("a", "first");
        Option optionB = new Option("b", "second");
        Option optionC = new Option("c", "third");
        Options opts = new Options();
        opts.addOption(optionA);
        opts.addOption(optionB);
        opts.addOption(optionC);

        hf.printHelp(cmdLineSyntax, opts);

        System.setOut(oldOut);
        String output = new String(outContent.toByteArray());
        String expected = "usage: commandline\n" +
                " -a   first\n" +
                " -b   second\n" +
                " -c   third\n";
        assertEquals(expected, output);
    }

    //Mutant12: org.apache.commons.cli.HelpFormatter:382: removed method call to printHelp
    public void test10()
    {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        HelpFormatter hf = new HelpFormatter();
        String cmdLineSyntax = "commandline";
        Option help = new Option("h", "help", false, "print this message");
        Options mOptions = new Options();
        mOptions.addOption(help);

        hf.printHelp(cmdLineSyntax, mOptions, false);

        System.setOut(oldOut);
        String output = new String(outContent.toByteArray());
        String expected = "usage: commandline\n" +
                " -h,--help   print this message\n";
        assertEquals(expected, output);
    }

    //Mutant13: org.apache.commons.cli.HelpFormatter:397: 0 -> 1
    //Mutant14: org.apache.commons.cli.HelpFormatter:397: removed method call to printHelp
    //Mutant15: org.apache.commons.cli.HelpFormatter:414: removed method call to printHelp
    public void test11()
    {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        HelpFormatter hf = new HelpFormatter();
        String cmdLineSyntax = "commandline";
        Option help = new Option("h", "help", false, "print this message");
        Options mOptions = new Options();
        mOptions.addOption(help);
        String header = "";
        String footer = "";

        hf.printHelp(cmdLineSyntax, header, mOptions, footer);

        System.setOut(oldOut);
        String output = new String(outContent.toByteArray());
        String expected = "usage: commandline\n" +
                " -h,--help   print this message\n";
        assertEquals(expected, output);
    }

    //Mutant16: org.apache.commons.cli.HelpFormatter:430: 0 -> 1
    //Mutant17: org.apache.commons.cli.HelpFormatter:430: removed method call to printHelp
    //Mutant18: org.apache.commons.cli.HelpFormatter:452: removed method call to flush
    public void test12()
    {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Option optionA = new Option("a", "first");
        Option optionB = new Option("b", "second");
        Option optionC = new Option("c", "third");
        Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        HelpFormatter formatter = new HelpFormatter();
        String header = EOL + "Header";
        String cmdLineSyntax = "commandline";
        String footer = "Footer";
        formatter.printHelp(80, cmdLineSyntax, header, options, footer);

        System.setOut(oldOut);
        String output = new String(outContent.toByteArray());
        String expected = "usage: commandline\n" +
                "\n" +
                "Header\n" +
                " -a   first\n" +
                " -b   second\n" +
                " -c   third\n" +
                "Footer\n";
        assertEquals(expected, output);
    }

    //Mutant20: org.apache.commons.cli.HelpFormatter:677: 1 -> 0
    //Mutant21: org.apache.commons.cli.HelpFormatter:677: 1 -> 0
    //Mutant22: org.apache.commons.cli.HelpFormatter:677: + -> -
    //Mutant23: org.apache.commons.cli.HelpFormatter:679: + -> -
    public void test13(){
        HelpFormatter hf = new HelpFormatter();

        StringWriter out = new StringWriter();
        hf.printUsage(new PrintWriter(out), 20, "teste teste teste");
        System.out.println(out.toString());
        assertEquals("usage: teste teste" + EOL + "             teste"+EOL, out.toString());
    }

    //Mutant19: org.apache.commons.cli.HelpFormatter:611: removed method call to sort
    public void test14(){
        Options opts = new Options();
        opts.addOption(new Option("a", "first"));
        opts.addOption(new Option("b", "second"));
        opts.addOption(new Option("c", "third"));

        OptionGroup group = new OptionGroup();
        group.addOption(OptionBuilder.create("a"));
        group.addOption(OptionBuilder.create("b"));
        group.addOption(OptionBuilder.create("c"));
        opts.addOptionGroup(group);

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setOptionComparator(new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                // reverses the functionality of the default comparator
                Option opt1 = (Option) o1;
                Option opt2 = (Option) o2;
                return opt2.getKey().compareToIgnoreCase(opt1.getKey());
            }
        });

        StringWriter out = new StringWriter();
        helpFormatter.printUsage(new PrintWriter(out), 80, "app", opts);

        assertEquals("usage: app [-c | -b | -a]" + EOL, out.toString());
    }

    //EQUIVALENTE! o optbuff.lenght() vai ser sempre maior ou igual ao max e nunca menor (linha 798). O optbuff.lenght nunca vai ser zero (linha 788)
    //Mutant24: org.apache.commons.cli.HelpFormatter:755: 0 -> 1
    public void test15(){
        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        final int leftPad = 0;
        final int descPad = 3;
        Option optionA = new Option("",true, "first");
        Option optionB = new Option("",true, "second");
        Option optionC = new Option("", true, "third");
        optionC.setArgName("");
        optionA.setArgName("");
        optionB.setArgName("");
        Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        hf.setLongOptPrefix("");
        hf.setOptPrefix("");

        String expected = "    third";
        hf.renderOptions(sb, 60, options, leftPad, descPad);
        assertEquals(expected, sb.toString());
    }

    //Mutant25: org.apache.commons.cli.HelpFormatter:788: 32 ( ) -> 33 (!)
    public void test16(){
        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        final int leftPad = 0;
        final int descPad = 3;
        final String lpad = hf.createPadding(leftPad);
        final String dpad = hf.createPadding(descPad);
        Option optionA = new Option("",true, "first");
        Option optionB = new Option("",true, "second");
        Option optionC = new Option("", true, "third");
        optionC.setArgName("");
        optionA.setArgName("");
        optionB.setArgName("");
        Options options = new Options();
        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);
        hf.setLongOptPrefix("");
        hf.setOptPrefix("");

        String expected = "    third";
        hf.renderOptions(sb, 60, options, leftPad, descPad);
        assertEquals(expected, sb.toString());
    }

    //Mutant29: org.apache.commons.cli.HelpFormatter:859: 1 -> 0
    public void test17(){
        int width = 7;
        int padding = 0;
        String text = "Thisisatest.";
        String expected = "Thisisa" + EOL +
                " test.";
        HelpFormatter hf = new HelpFormatter();
        StringBuffer sb = new StringBuffer();
        hf.renderWrappedText(sb, width, 8, text);
        assertEquals("cut and wrap", expected, sb.toString());
    }

    //Mutant30: org.apache.commons.cli.HelpFormatter:868: 0 -> 1
    //Mutant31: org.apache.commons.cli.HelpFormatter:877: negated conditional
    //Mutant32: org.apache.commons.cli.HelpFormatter:877: 1 -> 0
    //Mutant33: org.apache.commons.cli.HelpFormatter:877: - -> +
    //Mutant34: org.apache.commons.cli.HelpFormatter:879: removed local assignment
    public void test18(){
        int width = 5;
        int padding = 4;
        String text = "aaaa" + EOL +
                "aaaaaa" + EOL;
        String expected = "aaaa" + EOL +
                "    a" + EOL +
                "    a"+ EOL +
                "    a"+ EOL +
                "    a"+ EOL +
                "    a"+ EOL +
                "    a";

        HelpFormatter hf = new HelpFormatter();
        StringBuffer sb = new StringBuffer();
        hf.renderWrappedText(sb, width, padding, text);
        assertEquals(expected, sb.toString());
    }

    //Mutant36: org.apache.commons.cli.HelpFormatter:908: 1 -> 0
    public void test19(){
        HelpFormatter hf = new HelpFormatter();
        int i = hf.findWrapPos("test\n"+"ads", 60, 0);
        assertEquals(5, i);
    }

    //Mutant35: org.apache.commons.cli.HelpFormatter:906: removed local assignment
    public void test20(){
        HelpFormatter hf = new HelpFormatter();
        int i = hf.findWrapPos("test\t", 5, 0);
        assertEquals(5, i);
    }

    //Mutant37: org.apache.commons.cli.HelpFormatter:920: 10 -> 11
    public void test21(){
        HelpFormatter hf = new HelpFormatter();
        String text = "1yh\u000Brs";
        int i = hf.findWrapPos(text, 5, 0);
        assertEquals(5, i);
    }

    //Mutant38: org.apache.commons.cli.HelpFormatter:920: 13 -> 14
    public void test22(){
        HelpFormatter hf = new HelpFormatter();
        String text = "1yh\u000Ers";
        int i = hf.findWrapPos(text, 5, 0);
        assertEquals(5, i);
    }

    // EQUIVALENTE
    // Para fazer return da linha 935 a soma do width e do startPos n�o podem ser igual ou superior ao text.lenght(). Assim sendo, o return do -1 na linha 935 apenas acontece
    // quando o pos (pos = startPos + width;) � igual ao text.length() o que nunca vai acontecer. Na linha 916 e linha 933 o pos vai ser tomar sempre o valor menor ao text.length()
    //Mutant39: org.apache.commons.cli.HelpFormatter:935: -1 -> 1
    public void test23(){
        HelpFormatter hf = new HelpFormatter();
        String text = "testdasdasdasds12345";
        int i = hf.findWrapPos(text, 2, 17);
        System.out.println(text.length());
        System.out.println(i);
        assertEquals(19, i);
    }

    //Mutant26: org.apache.commons.cli.HelpFormatter:830: changed return value (areturn)
    //Mutant27: org.apache.commons.cli.HelpFormatter:852: changed return value (areturn)
    public void test24(){
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        final int leftPad = 1;
        final int descPad = 3;
        final String lpad = hf.createPadding(leftPad);
        final String dpad = hf.createPadding(descPad);
        Options options = null;
        String expected = null;


        options = new Options().addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");
        expected = lpad + "-a" + dpad + "aaaa aaaa aaaa aaaa aaaa";
        sb = hf.renderOptions(sb, 60, options, leftPad, descPad);
        sb2 = hf.renderWrappedText(sb2, 60, 9, " -a   aaaa aaaa aaaa aaaa aaaa");

        System.out.println(sb2.toString());
        System.out.println(sb.toString());
        assertEquals(sb2.toString(), sb.toString());
    }

    //Mutant28: org.apache.commons.cli.HelpFormatter:874: changed return value (areturn)
    public void test25(){
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();
        final int leftPad = 1;
        final int descPad = 3;
        final String lpad = hf.createPadding(leftPad);
        final String dpad = hf.createPadding(descPad);
        Options options = null;
        String expected = null;


        options = new Options().addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");
        expected = lpad + "-a" + dpad + "aaaa aaaa aaaa aaaa aaaa";
        sb = hf.renderOptions(sb, 20, options, leftPad, descPad);
        sb2 = hf.renderWrappedText(sb2, 20, 6, " -a   aaaa aaaa aaaa aaaa aaaa");

        assertEquals(sb2.toString(), sb.toString());
    }
}

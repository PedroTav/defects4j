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

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;

/** 
 * Test case for the HelpFormatter class 
 *
 * @author David Blanquett
 */
public class CLI_MAJOR_A1_StudentTest extends TestCase
{
    private static final String EOL = System.getProperty("line.separator");

    // 3
    public void testFormatterGetsAndSets() {

        // 3
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(4);
        assertEquals(formatter.getDescPadding(),4);

        // 10
        formatter = new HelpFormatter();
        formatter.setOptionComparator(null);
        assertFalse(formatter.getOptionComparator() == null);

        /// ??????
        // Print options
        formatter = new HelpFormatter();
        // falta settar as options...
        Options myOptions = new Options();
        Option option01 = new Option("One",true,"Descriptopn");
        option01.setArgName("");
        option01.setDescription("Desc");
        StringBuffer buffer = new StringBuffer("aaa a");
        formatter.renderOptions(buffer,
                25, myOptions, 1, 1);
        assertEquals(
                "aaa a",
                buffer.toString());

        StringBuffer sb = new StringBuffer();
        HelpFormatter hf = new HelpFormatter();

        hf.defaultDescPad = 0;
        assertEquals(0, hf.getDescPadding());

        hf = new HelpFormatter();
        hf.defaultDescPad = Integer.MAX_VALUE;
        assertEquals(Integer.MAX_VALUE, hf.getDescPadding());

        hf = new HelpFormatter();
        hf.optionComparator = null;
        assertEquals(null, hf.getOptionComparator());

        hf = new HelpFormatter();
        hf.setNewLine("a");
        assertEquals("a", hf.getNewLine());

        hf = new HelpFormatter();
        hf.setArgName("name");
        assertEquals("name", hf.getArgName());

        hf = new HelpFormatter();
        hf.setWidth(0);
        assertEquals(0, hf.getWidth());

    }

    public void testOutputStreamValidations() {
        // 19
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        PrintStream originalOut  = System.out;
        System.setOut(new PrintStream(consoleOutput));

        HelpFormatter hf = new HelpFormatter();

        Option optionA = new Option("a", "first");
        Option optionB = new Option("b", "second");
        Option optionC = new Option("c", "third");
        Options opts = new Options();
        opts.addOption(optionA);
        opts.addOption(optionB);
        opts.addOption(optionC);

        hf.printHelp(80, "foobar", "header", opts,  "footer");
        /*
        usage: foobar
        header
         -a   first
         -b   second
         -c   third
        footer
        */
        assertTrue("Should contain option", consoleOutput.toString().toLowerCase().contains("-a ".toLowerCase())==true);
        System.setOut(originalOut);

    }

    public void testHelpErrorBoundaries(){
        // 28,29 s�o equivalentes
        HelpFormatter hf = new HelpFormatter();

        Option optionA = new Option("a", "first");
        Option optionB = new Option("b", "second");
        Option optionC = new Option("c", "third");
        Options opts = new Options();
        opts.addOption(optionA);
        opts.addOption(optionB);
        opts.addOption(optionC);

        // 30
        try{
            hf.printHelp(80, "", "header", opts,  "footer");
            assertTrue("cmdLineSyntax can't be empty", false);

        }catch(IllegalArgumentException ex){
            assertTrue("cmdLineSyntax can't be empty", true);
        }

        try{
            hf.printHelp(80, "", "header", opts,  "footer");
            assertTrue("cmdLineSyntax can't be null", false);

        }catch(IllegalArgumentException ex){
            assertTrue("cmdLineSyntax can't be null", true);
        }
    }

    public void testHelpValidations(){
        //40
        HelpFormatter hf = new HelpFormatter();
        ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
        PrintStream originalOut  = System.out;
        System.setOut(new PrintStream(consoleOutput));

        Option optionA = new Option("a", "first");
        Option optionB = new Option("b", "second");
        Option optionC = new Option("c", "third");
        Options opts = new Options();
        opts.addOption(optionA);
        opts.addOption(optionB);
        opts.addOption(optionC);

        hf.printHelp(80, "foobar", "", opts,  "footer");

        assertTrue("Should contain option", consoleOutput.toString().toLowerCase().contains("foobar\n -a".toLowerCase())==true);
        System.setOut(originalOut);

        // 52 � requivalembte

        // 54
        consoleOutput = new ByteArrayOutputStream();
        originalOut  = System.out;
        System.setOut(new PrintStream(consoleOutput));

        hf.printHelp(80, "foobar", "header", opts, "");

        assertTrue("Should contain option", consoleOutput.toString().toLowerCase().endsWith("third\n".toLowerCase())==true);
        System.setOut(originalOut);

        // 70
        hf = new HelpFormatter();
        Options options = new Options();
        options.addOption(new Option("A", "1st Option"));
        String expected = "usage: app [-A]\n";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);

        hf.printUsage(pw, 60, "app", options);
        pw.flush();
        assertEquals("simple auto usage", expected, out.toString());
    }

    public void testPrintUsageErrorBoundaries(){
        /// 72
        HelpFormatter hf = new HelpFormatter();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(out);
            hf.printUsage(pw, 60, "app", null);
            pw.flush();
            assertEquals("simple auto usage", true, false); // Assert FAILURE!
        }catch(Exception e){
            assertEquals("simple auto usage", true, true);

        }
    }

    public void testInvertComparator(){

        // 82
        HelpFormatter formatter = new HelpFormatter();

        OptionGroup group = new OptionGroup();
        group.addOption(OptionBuilder.create("a"));
        group.addOption(OptionBuilder.create("b"));
        group.addOption(OptionBuilder.create("c"));
        Options optionsWithGroups = new Options();
        optionsWithGroups.addOptionGroup(group);
        formatter.setOptionComparator(new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                // reverses the functionality of the default comparator
                Option opt1 = (Option) o1;
                Option opt2 = (Option) o2;
                return opt2.getKey().compareToIgnoreCase(opt1.getKey());
            }
        });
        StringWriter outText = new StringWriter();
        formatter.printUsage(new PrintWriter(outText), 80, "app", optionsWithGroups);

        assertEquals("usage: app [-c | -b | -a]" + EOL, outText.toString());

    }

    public void testOptions(){

        //107
        HelpFormatter formatter = new HelpFormatter();

        Option option = new Option("Ai", "AiOptions",false,"desc");
        option.setArgName("");
        Options optionsWithoutGroups = new Options();
        optionsWithoutGroups.addOption(option);

        StringWriter outText = new StringWriter();
        formatter.printUsage(new PrintWriter(outText), 80, "app", optionsWithoutGroups);

        assertEquals("usage: app [-Ai]" + EOL, outText.toString());

        //120

        formatter = new HelpFormatter();

        Options myOptions = new Options();
        Option option01 = new Option("One",true,"Description");
        option01.setArgName("A");
        myOptions.addOption(option01);
        StringBuffer buffer = new StringBuffer();
        formatter.renderOptions(buffer,
                25, myOptions, 1, 1);
        assertEquals(
                " -One <A> Description",
                buffer.toString());

        // 154,2
        formatter = new HelpFormatter();

        myOptions = new Options();
        option01 = new Option("One",true,"Description");
        option01.setArgName("");
        myOptions.addOption(option01);
        buffer = new StringBuffer();
        formatter.renderOptions(buffer,
                25, myOptions, 1, 1);
        assertEquals(
                " -One  Description",
                buffer.toString());

        // 183
        formatter = new HelpFormatter();

        myOptions = new Options();
        option01 = new Option("One",false,null);
        myOptions.addOption(option01);
        buffer = new StringBuffer();
        formatter.renderOptions(buffer,
                25, myOptions, 1, 1);
        assertEquals(
                " -One",
                buffer.toString());

    }

    public void renderWrapValidations(){

        // 194
        int width = 2;
        int padding = 0;
        String text = "a a  ";
        String expected_194 = "a" + EOL +
                "a";

        StringBuffer sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected_194, sb.toString());

        // 200, to 206
        width = 2;
        padding = 3;
        text = "a a  ";
        String expected_200 = "a" + EOL +
                " a";

        sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected_200, sb.toString());

        // 207 208
        width = 2;
        padding = 0;
        text = " a a  ";
        expected_200 = " a" + EOL +
                "a";

        sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected_200, sb.toString());

        // 213
        width = 2;
        padding = 0;
        text = "a b c d";
        expected_200 = "a" + EOL + "b" + EOL + "c" + EOL + "d";
        sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected_200, sb.toString());

        // 216 217 2019
        width = 1;
        padding = 0;
        text = "aaaa bbbb cc d";
        expected_200 = "a\n" +
                "a\n" +
                "a\n" +
                "a\n" +
                "b\n" +
                "b\n" +
                "b\n" +
                "b\n" +
                "c\n" +
                "c\n" +
                "d";
        sb = new StringBuffer();
        new HelpFormatter().renderWrappedText(sb, width, padding, text);
        assertEquals("cut and wrap", expected_200, sb.toString());
    }

    public void testfindWrapPosVariations()    {

        HelpFormatter hf = null;
        String text = "";

        // 240
        // EOF validations
        hf = new HelpFormatter();
        text = "aaaa\n";
        assertEquals("wrap position", 5, hf.findWrapPos(text, 4, 0));

        // 242
        // EOF validations
        hf = new HelpFormatter();
        text = "aaaa\n";
        assertEquals("wrap position", 2, hf.findWrapPos(text, 2, 0));

        /*
        243,FAIL
        244,FAIL
        245,FAIL
        246,FAIL
               */
        hf = new HelpFormatter();

        text = "aaaa";
        assertEquals("wrap position", -2, hf.findWrapPos(text, -2, 0));

        // 240
        hf = new HelpFormatter();
        text = "aaaa\n";
        assertEquals("wrap position", 5, hf.findWrapPos(text, 4, 0));

        // 242
        hf = new HelpFormatter();
        text = "aaaa\n";
        assertEquals("wrap position", 2, hf.findWrapPos(text, 2, 0));

        // now with /t
        // 240
        hf = new HelpFormatter();
        text = "aaaa\t";
        assertEquals("wrap position", 5, hf.findWrapPos(text, 4, 0));

        // 242
        hf = new HelpFormatter();
        text = "aaaa\t";
        assertEquals("wrap position", 2, hf.findWrapPos(text, 2, 0));

        // 253 254
        hf = new HelpFormatter();
        text = "a ";
        assertEquals("wrap position", -2, hf.findWrapPos(text, -2, 0));

        /// 258 255
        text = "aaaaaa\taaaaaa";
        assertEquals("wrap position 4", 7, hf.findWrapPos(text, 6, 0));

        // 273
        hf = new HelpFormatter();
        text = "aa aa\r";
        assertEquals("wrap position", 0, hf.findWrapPos(text, 0, 0));

        // 297
        hf = new HelpFormatter();
        text = "aaaa\ra";
        assertEquals("wrap position", 4, hf.findWrapPos(text, text.length()-1, 0));

        // 295
        hf = new HelpFormatter();
        text = "a\naaaaa";
        assertEquals("wrap position", 6, hf.findWrapPos(text, text.length()-3, 2));

        // 290
        hf = new HelpFormatter();
        text = "a aaaaa";
        assertEquals("wrap position", 1, hf.findWrapPos(text, 0, 1));

    }

    public void testRtrim(){
        // 314-
        assertEquals("", new HelpFormatter().rtrim(" "));
    }
}

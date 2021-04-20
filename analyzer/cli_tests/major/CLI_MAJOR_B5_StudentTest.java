package org.apache.commons.cli;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.StringWriter;

import java.io.FileWriter;
import java.io.IOException;

import java.lang.reflect.*;
import junit.framework.TestCase;

/** 
 * Test case for the HelpFormatter class 
 *
 * @author Slawek Zachcial
 * @author John Keyes ( john at integralsource.com )
 * @author brianegge
 */
public class CLI_MAJOR_B5_StudentTest extends TestCase {

    public void testMut3() {
        int padding = 5;

        HelpFormatter hf = new HelpFormatter();

        hf.setDescPadding(padding);
        assertEquals("padding set", padding, hf.getDescPadding());
    }

    public void testMut5() {
        String newLine = "a";

        HelpFormatter hf = new HelpFormatter();

        hf.setNewLine(newLine);
        assertEquals("newLine set", newLine, hf.getNewLine());

    }
    public void testMut11() {
      class TestComparator implements Comparator
      {
        public int compare(Object o1, Object o2)
        {
            Option opt1 = (Option) o1;
            Option opt2 = (Option) o2;

            return opt1.getKey().compareTo(opt2.getKey());
        }
      }

      Comparator comparator = new TestComparator();

      HelpFormatter hf = new HelpFormatter();
      HelpFormatter hf2 = new HelpFormatter();
      hf.optionComparator = comparator;

      hf.setOptionComparator(null);

      assertEquals("default comparator", hf2.getOptionComparator().getClass().getName(), hf.getOptionComparator().getClass().getName());
    }

    public void testMut15() {
      final PrintStream standardOut = System.out;
      final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      
      HelpFormatter hf = new HelpFormatter();

      int width = 3;
      String cmdLineSyntax = "aa";
      Options options = new Options();
      options.addOption("prt", true, "PRINT MULT");
      boolean autoUsage = true;

      System.setOut(new PrintStream(outputStreamCaptor));
      hf.printHelp(cmdLineSyntax, options, autoUsage);

      String res1 = outputStreamCaptor.toString(); 
      Print.print(""+ res1.length());
      int expectedLength = 48;


      System.setOut(standardOut);
      assertEquals(expectedLength, res1.length());                  
    }

    public void testMut16_17_18() {
      final PrintStream standardOut = System.out;
      final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
      
      HelpFormatter hf = new HelpFormatter();

      int width = 3;
      String cmdLineSyntax = "aa";
      Options options = new Options();
      options.addOption("prt", true, "PRINT MULT");
      String header = "h1";
      String footer = "f1";

      System.setOut(new PrintStream(outputStreamCaptor));
      hf.printHelp(cmdLineSyntax, header, options, footer);

      String res1 = outputStreamCaptor.toString(); 
      Print.print(""+ res1.length());
      int expectedLength = 41;


      System.setOut(standardOut);
      assertEquals(expectedLength, res1.length());                  
    }

    public void testMut19() {
      final PrintStream standardOut = System.out;
      final ByteArrayOutputStream outputStreamCaptor1 = new ByteArrayOutputStream();
      final ByteArrayOutputStream outputStreamCaptor2 = new ByteArrayOutputStream();

      HelpFormatter hf = new HelpFormatter();

      int width = 3;
      String cmdLineSyntax = "aa";
      String header = "h1";
      Options options = new Options();
      options.addOption("prt", true, "PRINT MULT");
      String footer = "f1";

      System.setOut(new PrintStream(outputStreamCaptor1));
      hf.printHelp(width, cmdLineSyntax, header, options, footer, false);
      System.setOut(new PrintStream(outputStreamCaptor2));
      hf.printHelp(width, cmdLineSyntax, header, options, footer);

      String res1 = outputStreamCaptor1.toString(); 
      String res2 = outputStreamCaptor2.toString(); 


      assertEquals(res1, res2);                  

      System.setOut(standardOut);
    }

    public void testMut40_52() {
      StringWriter out    = new StringWriter();
      PrintWriter  pw = new PrintWriter(out);
      
      HelpFormatter hf = new HelpFormatter();

      int width = 80;
      String cmdLineSyntax = "aa";
      String header = "h";
      Options options = new Options();
      options.addOption("prt", true, "PRINT MULT");
      int leftPad = 1;
      int descPad = 1;
      String footer = "f";
      boolean autoUsage = false;

      hf.printHelp(pw, width, cmdLineSyntax, header, options, leftPad, descPad, footer, autoUsage);
      pw.flush();
    
      String res = out.toString(); 
      
      int expectedLength = 37;
      assertEquals(expectedLength, res.length());                  
    }

    public void testMut82() {
      StringWriter out = new StringWriter();
      PrintWriter  pw = new PrintWriter(out);
      
      HelpFormatter hf = new HelpFormatter();

      int width = 80;
      String app = "aa";
      Options options = new Options();
      OptionGroup group = new OptionGroup();
      group.addOption(new Option("prtM", true, "PRINT MULT"));
      group.addOption(new Option("prtS", true, "PRINT SING"));
      group.addOption(new Option("prtC", true, "PRINT SING"));
      options.addOptionGroup(group);

      hf.printUsage(pw, width, app, options);
      pw.flush();
    
      String res = out.toString(); 
      
      String expectedString = "usage: aa [-prtC <arg> | -prtM <arg> | -prtS <arg>]\n";
      assertEquals(expectedString, res);
    }

    public void testMut107() {
      StringWriter out = new StringWriter();
      PrintWriter  pw = new PrintWriter(out);
      
      HelpFormatter hf = new HelpFormatter();

      int width = 80;
      String app = "aa";
      Options options = new Options();
      Option option = new Option(null, true, "PRINT MULT");
      option.setArgs(-1);
      option.setArgName("");
      options.addOption(option);
      
      hf.printUsage(pw, width, app, options);
      pw.flush();
    
      String res = out.toString(); 
      
      Print.print(res + "\n" + res.length());
      int expectedLength = 19;
      assertEquals(expectedLength, res.length());
    }
    
    public void testMut120() {
      StringWriter out = new StringWriter();
      PrintWriter  pw = new PrintWriter(out);
      
      HelpFormatter hf = new HelpFormatter();

      int width = 20;
      String cmdLineSyntax = "asaasd asasdasd";
      
      hf.printUsage(pw, width, cmdLineSyntax);
      pw.flush();
    
      String res = out.toString(); 
      
      Print.print(res + "\n" + res.length());
      int expectedLength = 52;
      assertEquals(expectedLength, res.length());
    }

    
    public void testMut148() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();

      int width = 20;
      int leftPad = 2;
      int descPad = 2;
      Options options = new Options();
      Option option = new Option("a", true, "asd");
      option.setArgName("A");
      options.addOption(option);

      hf.renderOptions(out, width, options, leftPad, descPad);
    
      String res = out.toString(); 
      
      Print.print(res + "\n" + res.length());
      int expectedLength = 13;
      assertEquals(expectedLength, res.length());
    }

    public void testMut152() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();

      int width = 20;
      int leftPad = 2;
      int descPad = 2;
      Options options = new Options();
      Option option = new Option("a", true, "asd");
      option.setArgName("");
      options.addOption(option);

      hf.renderOptions(out, width, options, leftPad, descPad);
    
      String res = out.toString(); 
      
      Print.print(res + "\n" + res.length());
      int expectedLength = 10;
      assertEquals(expectedLength, res.length());
    }

    public void testMut183() {
      StringBuffer out = new StringBuffer(); 
      HelpFormatter hf = new HelpFormatter();

      int width = 100;
      int leftPad = 1;
      int descPad = 3;
      Options options = new Options();
      Option option = new Option("a", true, null);
      options.addOption("a", false, null);
      options.addOption("c", false, null);
      options.addOption("b", false, null);
      
      hf.renderOptions(out, width, options, leftPad, descPad);
    
      String res = out.toString(); 
      
      Print.print(res + "\n" + res.length());
      int expectedLength = 11;
      assertEquals(expectedLength, res.length());
    } 

    public void testMut240() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aa\nasdadadadd";
      int width = 2;
      int startPos = 0;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 3;
      assertEquals(expectedPos, pos);
    }

    
    public void testMut242() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aaaaa\nasdadadadd";
      int width = 3;
      int startPos = 0;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 3;
      assertEquals(expectedPos, pos);
    }

    public void testMut243() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aaaaaasdadadadd";
      int width = -5;
      int startPos = 0;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = -5;
      assertEquals(expectedPos, pos);
    }

    public void testMut247() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aa\tasdadadadd";
      int width = 2;
      int startPos = 0;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 3;
      assertEquals(expectedPos, pos);
    }

    public void testMut251() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aaaa\tasdadadadd";
      int width = 9;
      int startPos = 0;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 5;
      assertEquals(expectedPos, pos);
    }

    public void testMut255() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aaaaa\tasdadadadd";
      int width = 3;
      int startPos = 0;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 3;
      assertEquals(expectedPos, pos);
    }

    public void testMut284() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aaaaa\tasdadadadd";
      int width = 3;
      int startPos = 4;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 7;
      assertEquals(expectedPos, pos);
    }

    public void testMut290() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aaaaaa\nasdadadadd";
      int width = 4;
      int startPos = 4;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 6;
      assertEquals(expectedPos, pos);
    }
    
    public void testMut295() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aaaaaa\rasdadadadd";
      int width = 4;
      int startPos = 4;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 6;
      assertEquals(expectedPos, pos);
    }
    
    public void testMut300() {
      StringBuffer out = new StringBuffer();
      HelpFormatter hf = new HelpFormatter();
      
      String text = "aaaaa\rasdadadadd";
      int width = 4;
      int startPos = 5;

      int pos = hf.findWrapPos(text, width, startPos);

      int expectedPos = 9;
      assertEquals(expectedPos, pos);
    }
}

class Print {
  static void print(String content) {
    try {
    FileWriter myWriter = new FileWriter("filename.txt");
      myWriter.write(content + "\n");
      myWriter.close();
    } catch (IOException e) {}
  }
}
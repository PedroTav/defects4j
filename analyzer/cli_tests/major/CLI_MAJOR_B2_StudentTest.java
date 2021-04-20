package org.apache.commons.cli;
import junit.framework.TestCase;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;
import java.io.PrintStream;

public class CLI_MAJOR_B2_StudentTest extends TestCase
{

  private static final String EOL = System.getProperty("line.separator");

    public void writeToLog(String info){
        try {
          FileWriter myWriter = new FileWriter("/home/joana/Desktop/defects4j/p1_cli/test_logs.txt", true);
          myWriter.append(info);
          myWriter.append("\n");
          myWriter.close();
        } catch (IOException e) {
          e.printStackTrace();
        }   
    }


     // kills 1
     public void testSetWidth() {
      HelpFormatter hf = new HelpFormatter();
      hf.setWidth(5);
      assertEquals(5, hf.getWidth());
    }

    // kills 3
    public void testSetDescPadding() {
        HelpFormatter hf = new HelpFormatter();
        hf.setDescPadding(5);
        assertEquals(5, hf.getDescPadding());
    }

    // kills 5
    public void testSetNewLine() {
        HelpFormatter hf = new HelpFormatter();
        hf.setNewLine("\n \t ---> ");
        assertEquals("\n \t ---> ", hf.getNewLine());
    }

    // kills 19, 40, 52, 120 (intentionally) and 20, 126 (unintentionally)
    public void testPrintHelp() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // printHelp(int width, String cmdLineSyntax, String header, Options options, String footer)
        HelpFormatter hf = new HelpFormatter();
        Options options = new Options().addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");
        hf.printHelp(20, "cmdLineSyntax", "header", options, "footer");

        assertEquals("usage: cmdLineSyntax\nheader\n -a   aaaa aaaa aaaa\n      aaaa aaaa\nfooter\n", outContent.toString());

        outContent.reset();

        PrintWriter pw = new PrintWriter(System.out);
        hf.printHelp(pw, 20, "this is the syntax", "h", options, 2, 3, "f", false);
        pw.flush();
        assertEquals("usage: this is the\n            syntax\nh\n  -a   aaaa aaaa\n       aaaa aaaa\n       aaaa\nf\n", outContent.toString());
    }

    // kills 70, 82, 107
    public void testPrintUsage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        HelpFormatter hf = new HelpFormatter();

        Options options = new Options().addOption("b", false, "bbbb bbbb bbbb bbbb bbbb");
        options.addOption("a", false, "aaaa aaaa aaaa aaaa aaaa");

        PrintWriter pw = new PrintWriter(System.out);
        hf.printUsage(pw, 20, "app", options);
        pw.flush();
        assertEquals("usage: app [-a] [-b]\n", outContent.toString());

        OptionGroup group = new OptionGroup();
        group.addOption(OptionBuilder.create("example"));
        group.addOption(OptionBuilder.create("another"));
        group.addOption(OptionBuilder.create("yet_another"));
        options = new Options().addOptionGroup(group);
        
        outContent.reset();
        hf.printUsage(pw, 20, "app", options);
        pw.flush();
        assertEquals("usage: app [-another\n       | -example |\n       -yet_another]\n", outContent.toString());

        Option optionV = new Option("v", false, "see version");
        optionV.setArgName("");
        options = new Options().addOption(optionV);
        outContent.reset();
        hf.printUsage(pw, 20, "app", options);
        pw.flush();
        assertEquals("usage: app [-v]\n", outContent.toString());
    }

    // kills 148, 149
    public void testPrintOptionsRender() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        HelpFormatter hf = new HelpFormatter();

        PrintWriter pw = new PrintWriter(System.out);
        Option op1 = new Option("v", false, "see version");
        Option op2 = new Option("d", true, "watch directories");
        op2.setArgName("w");
        Options ops = new Options().addOption(op1);
        ops.addOption(op2);

        hf.printOptions(pw, 50, ops, 1, 3);
        pw.flush();

        assertEquals(" -d <w>   watch directories\n -v       see version\n", out.toString());
    }
    
    // kills 183
    public void testRenderOptions() {
        HelpFormatter hf = new HelpFormatter();
        StringBuffer sb = new StringBuffer();
    
        Option op1 = new Option("v", "version", false, "see version");
        Option op2 = new Option("d", "watch", true, "watch directories");
        op2.setArgName("w");
        Option op3 = new Option("c", false, null);

        Options ops = new Options().addOption(op1);
        ops.addOption(op2);
        ops.addOption(op3);

        hf.renderOptions(sb, 40, ops, 3, 1);
        assertEquals("   -c\n   -d,--watch <w> watch directories\n   -v,--version   see version", sb.toString());
    }

    // kills 11
    public void testSetOptionComparator(){
        HelpFormatter hf = new HelpFormatter();
        Comparator cmp = new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                // reverses the fuctionality of the default comparator
                Option opt1 = (Option) o1;
                Option opt2 = (Option) o2;
                return opt2.getKey().compareToIgnoreCase(opt1.getKey());
            }
        };
        hf.setOptionComparator(cmp);
        hf.setOptionComparator(null);
        assertTrue(hf.getOptionComparator() != cmp);
    }
  
    public void testRenderWrappedText(){

      //Mutants 193/200/208/212/216/217/220/225/231

      //Kills Mutant 200
      int width = 3;
      int padding = 4;
      String text = "Hi it is a test.";
      String expected = "Hi" + EOL + " it" + EOL + " is" + EOL + " a" + EOL + " te" + EOL +" st" + EOL +" .";
      StringBuffer sb = new StringBuffer();
      new HelpFormatter().renderWrappedText(sb, width, padding, text);
      assertEquals("render text 1", expected, sb.toString());

      //Mutant 216 and 217
      //Check if the width can be less  
      width = 50;
      padding = 15;
      text = "aaaa     " + EOL +
             "aa  aa     " + EOL +
             "aaaa     " + EOL +
             "a      ";
      expected = "aaaa\n               aa  aa\n               aaaa\n               a";
      
      HelpFormatter hf = new HelpFormatter();
      sb = new StringBuffer();
      new HelpFormatter().renderWrappedText(sb, width, padding, text);
      assertEquals("render text 2", expected, sb.toString());

  
      //Kills Mutants 220 /208
      width = 11;
      padding = 10;
      text = "aaaa" + EOL +
             "aaa" + EOL +
             "aa";
      expected = "aaaa" + EOL +
                 "          a" + EOL +
                 "          a" + EOL +
                 "          a" + EOL +
                 "          a" + EOL +
                 "          a";
      
      sb = new StringBuffer();
      new HelpFormatter().renderWrappedText(sb, width, padding, text);
      assertEquals("render text 3", expected, sb.toString());


      //Mutant 225 and 331
      //Check if pos is < than padding - 1
      width = 20;
      padding = 18;
      text = "aaaa     " + EOL +
             "aa  aa     " + EOL +
             "aaaa     " + EOL +
             "a      ";
      expected = "aaaa\n                  aa\n                  aa\n                  aa\n                  aa\n                  a";
      
      hf = new HelpFormatter();
      sb = new StringBuffer();
      new HelpFormatter().renderWrappedText(sb, width, padding, text);
      writeToLog(sb.toString());
      assertEquals("render text 4", expected, sb.toString());
    
    }

    public void testFindWrapPos() throws Exception
    {
      HelpFormatter hf = new HelpFormatter();

      String text = "Hello \n";
      String text2 = "Hello\n";
      String text3 = "Hello";
      String text4 = "Hello \t";
      String text5 = "Hello\t";
      String text6 = "Hello\b";
      String text7 = " Hello";
      String text8 = "Hello \r";
      String text9 = " Hello\r";

      //Kills Mutant 238 /260/263/265
      assertEquals("wrap position 1", 7, hf.findWrapPos(text, 10, 0));

      //Killed Mutants 240
      assertEquals("wrap position 2", 7, hf.findWrapPos(text, 6, 0));
            
      //Kills Mutants 242/244
      assertEquals("wrap position 3", 2, hf.findWrapPos(text, 2, 0));
            
      //Killed Mutant 243
      assertEquals("wrap position 4", -2, hf.findWrapPos(text3, -2, 0));
            
      //Kills Mutants 247 /254/258
      assertEquals("wrap position 5", 7, hf.findWrapPos(text4, 10, 0));
            
      //Kills Mutant 250
      assertEquals("wrap position 5", 7, hf.findWrapPos(text4, 6, 0));
            
      //Kills Mutants: 253
      assertEquals("wrap position 6", 5, hf.findWrapPos(text4, 5, 0));
      
      //Kills Mutant 297
      assertEquals("wrap position x6", 6, hf.findWrapPos(text8, 6, 0));
        
      //Kills Mutant 300
      assertEquals("wrap position x17", 3, hf.findWrapPos(text7, 3, 0));

      //Mutant 309
      assertEquals("wrap position x18", -1, hf.findWrapPos(text3, 5, 0));
     
    }
    

}

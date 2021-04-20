package org.apache.commons.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Comparator;

import junit.framework.TestCase;

/** 
 * Test class for student written tests
 * @author danrpinho
 */
public class CLI_JUMBLE_D2_StudentTest extends TestCase
{
	public void test7()
    {
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        try
        {	formatter.printHelp(new PrintWriter(out), 80, "", "header", new Options(), 0, 0, "--", false);
            fail("null command line syntax should be rejected");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("cmdLineSyntax not provided", e.getMessage());
        }
    }
	
	public void test14() {
		HelpFormatter formatter = new HelpFormatter();
		assertEquals(74, formatter.defaultWidth);
	}
	
	public void test15() {
		HelpFormatter formatter = new HelpFormatter();
		assertEquals(1, formatter.defaultLeftPad);
	}
	
	public void test16() {
		HelpFormatter formatter = new HelpFormatter();
		assertEquals(3, formatter.defaultDescPad);
	}
	
	public void test59(){
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(new PrintWriter(out), 80, "1 !2 !3!2");
        assertEquals("usage: 1 !2 !3!2\n", out.toString());
    }
	
	public void test71(){
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        
        Options options = new Options();
        Option option = new Option("f", true, "desc");
        option.setArgName("");
        options.addOption(option);
        
        formatter.printOptions(new PrintWriter(out), 80, options, formatter.getLeftPadding(), formatter.getDescPadding());
        assertEquals(" -f    desc\n", out.toString());
    }
	
	public void test64(){
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        
        Options options = new Options();
        Option option = new Option("", false, "");
        options.addOption(option);
        
        formatter.printOptions(new PrintWriter(out), 80, options, 0, formatter.getDescPadding());
        assertEquals("-\n", out.toString());
    }
	
	public void test62(){
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        
        formatter.printUsage(new PrintWriter(out), 10, "function -a e !eo");
        
        assertEquals("usage:\n"
        		+ " function\n"
        		+ " -a e !eo\n", out.toString());
    }
	
	public void test59606191969798(){
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        
        formatter.printUsage(new PrintWriter(out), 10, "function!!e!e!e!eo");
        
        assertEquals("usage:\n"
        		+ "       fun\n"
        		+ "       cti\n"
        		+ "       on!\n"
        		+ "       !e!\n"
        		+ "       e!e\n"
        		+ "       !eo\n", out.toString());
    }
	
	public void test92(){
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        
        formatter.printUsage(new PrintWriter(out), 10, " function -ea -e");
        
        assertEquals("usage:\n"
        		+ "        fu\n"
        		+ "        nc\n"
        		+ "        ti\n"
        		+ "        on\n"
        		+ "        -e\n"
        		+ "        a\n"
        		+ "        -e\n", out.toString());
    }
	
	public void tests105108(){
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        
        formatter.printWrapped(new PrintWriter(out), 10, 1, "fun" + '\t' + "ct" + '\t' + "ion");
        
        assertEquals("fun\n" + " ct\n" + " ion\n", out.toString());
    }
	
	public void tests121123(){
		StringWriter out = new StringWriter();
        HelpFormatter formatter = new HelpFormatter();
        
        char rep1 = 11;
        char rep2 = 14;
        
        formatter.printWrapped(new PrintWriter(out), 10, 1, "123  \n  0" + rep1 +"345" + rep2 + "678901");
        assertEquals("123\n" + " 0345678\n" + " 901\n", out.toString());
    }
	
}
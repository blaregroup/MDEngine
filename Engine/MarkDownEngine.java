/*

	Author
			Suraj Singh Bisht
			Surajsinghbisht054@gmail.com
			www.bitforestinfo.com
			www.blaregroup.com


	========================================================
				MarkDown To HTML Generating Engine
	========================================================


Reference Docs:
		https://docs.oracle.com/javase/7/docs/api/java/lang/String.html
		https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
        https://www.markdownguide.org/extended-syntax/
        https://daringfireball.net/projects/markdown/syntax#header
		

*/

// importing module
import java.io.*;
import java.util.regex.*;




/* Markdown To HTML Generator */
class MarkDownSyntaxParser{
    
    
    /* Patterns */
    private final String EXP_HEADING = "(\n(#+ .+))";
    private final String EXP_GLOBAL  = "(.+)";
    private final String EXP_HRRULER = "(((\n)(---))|((\n)(___))|((\n)(\\*\\*\\*)))"; // horizontal line
    private final String EXP_OL_LIST = "((\n( *)(\\d+)(\\. )(.*))+)";	// order list
    private final String EXP_BLOCK_C = "(\n(```)([^```]+)(```))"; // code block
    private final String EXP_UL_LIST = "((\n( *)([\\-\\+\\*]+ )(.*))+)";  // unorder list
    private final String EXP_HEAD_HR = "((.+)\n((={3,})|(\\-{3,})))"; // ALT heading
    private final String EXP_TABLEBD = "(\n(\\|?)([\\|\\w \\d]+)(\\|?)\n([ \\-\\|\\:]{7,})\n((\\|?)(.+)(\\|?)\n)+)"; // Table
    private final String EXP_INDENTS = "(\n( {4,})(.+))+";
    private final String EXP_BLOCKQ  = "((\n(( &gt;|&gt;)+) (.+))+)";
    
    public String EXP_ALL = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s",
                                          EXP_BLOCK_C,
                                          EXP_HEADING, 
                                          EXP_HRRULER,
                                          EXP_OL_LIST,
                                          EXP_UL_LIST,
                                          EXP_HEAD_HR,
                                          EXP_TABLEBD,
                                          EXP_INDENTS,
                                          EXP_BLOCKQ,
                                          EXP_GLOBAL);
    
    private String inputdata; // Input Raw Data Variable
    private String outputdata="";
    
    // Constructor
    MarkDownSyntaxParser(String data){
        inputdata = escapeHtml(data);
        ExtractData();
    }
    
    /* Extract Data*/
    private void ExtractData(){
        Pattern pattern = Pattern.compile(EXP_ALL);
        Matcher matcher = pattern.matcher(inputdata);
        while(matcher.find()){
            ProcessData(inputdata.substring(matcher.start(), matcher.end()));
          }
    }
    
    /*  Method That Split and Process Tags */
    private void ProcessData(String line){
        /* If Heading Expression Match Then Pass It To Expression Handling Method */
        if (line.matches(EXP_HEADING)){
            outputdata += HeadingProcessor(line);
        
        }/* If Horizontal line Expression Match Then Pass It To Expression Handling Method */
        else if(line.matches(EXP_HRRULER)){
            outputdata += HorizontalProcessor(line);
        
        }/* If Ordered List Expression Match Then Pass It To Expression Handling Method */
        else if (line.matches(EXP_OL_LIST)){
            outputdata += OrderListProcessor(line);
            
        }/* If UnOrdered List Expression Match Then Pass It To Expression Handling Method */
        else if (line.matches(EXP_UL_LIST)){
            outputdata += UnOrderListProcessor(line);
        
        }/* If Block Code Expression Match Then Pass It To Expression Handling Method */
        else if (line.matches(EXP_BLOCK_C)){
            outputdata += CodeProcessor(line);

        }/* If Heading With Horizontal Line Expression Match Then Pass It To Expression Handling Method */
        else if (line.matches(EXP_HEAD_HR)){
            outputdata += HRHeadingProcessor(line);

        }/* If Table Expression Match Then Pass It To Expression Handling Method */
        else if (line.matches(EXP_TABLEBD)){
            outputdata += TableProcessor(line);

        }/* If Inline Code Expression Match Then Pass It To Expression Handling Method */
        
        //else if (line.matches(EXP_INLINES)){
        //    outputdata += InlineProcessor(line);
        //
        //}
        
        
        /* If Indent Code Expression Match Then Pass It To Expression Handling Method */
        else if (line.matches(EXP_INDENTS)){
            outputdata += IndentProcessor(line);

        }/* If Blockquotes Expression Match Then Pass It To Expression Handling Method */
        else if (line.matches(EXP_BLOCKQ)){
            outputdata += BlockQProcessor(line);

        }else{
            /* If Global Expression Match Then Pass It To Expression Handling Method */
            outputdata += GlobalProcessor(line);
        }
    }
    /* Inline Expression Processor */
    private String InlineProcessor(String line){
        System.out.println(line);
        return line;
    }
    /* Indent Expression Processor */
    private String IndentProcessor(String line){
        return line;
    }
    /* Horizontal Line Expression Processor */
    private String HRHeadingProcessor(String line){
        String[] args = line.split("\n", 2);
        return String.format("<br /> <h%d> %s </h%d>  <hr />", 2, args[0], 2);
    }
    /* Table Expression Processor */
    private String TableProcessor(String line){
        
        return line;
    }
    /* BlockQuote Expression Processor */
    private String BlockQProcessor(String line){
        //System.out.println(String.format("Input %s", line));
        String output="\n\n<blockquote>";
        int lastn=1;
        int ttl = 0;
        
        for(String l: line.split("\n")){
            if(l.length()!=0){
            
                String[] args = l.split("(?<=(&gt;| &gt;))");

                if(lastn>(args.length-1)){
                    lastn=args.length-1;
                    ttl -=1;
                   System.out.println(String.format(" Child Close %s %s", args[args.length-1], l));
                    output += String.format("\n%s\n</blockquote>\n", args[args.length-1]);

                }else if(lastn==(args.length-1)){
                    //lastn=args.length-1;
                   System.out.println(String.format(" Continue %s %s", args[args.length-1], l));
                    output += String.format("%s\n", args[args.length-1]);
                    
                }else{
                    lastn=args.length-1;
                    ttl +=1;
                 System.out.println(String.format(" New Child %s %s", args[args.length-1], l));
                
                    output += String.format("\n<blockquote>\n %s", args[args.length-1]);
                }
            }
            
        }
        for(int i=0; i<=ttl; i++){
        output += "\n</blockquote>\n";
        }
        
        
        return output;
    }
    /* Ordered Expression Processor */
    private String OrderListProcessor(String line){
        String[] tmplist = line.split("\n");
        String tmpobj = "";
        
        
        for(int k=1;k<tmplist.length;k++)
        {   
           String[] list = tmplist[k].split(" ",2);
            tmpobj += String.format("<li>%s</li>",list[1]);
            
        }
        

        return String.format("<ol> %s </ol>\n",tmpobj);
    }
    
    /* UnOrder List Processor */
    private String UnOrderListProcessor(String line){
        String[] tmplist = line.split("\n");
        String tmpobj = "";
        

        for(int k=1;k<tmplist.length;k++)
        {   
           String[] list = tmplist[k].split(" ",2);
            tmpobj += String.format("<li>%s</li>",list[1]);
            
        }
        
        return String.format("<ul>%s</ul>\n",tmpobj);
    }
    
    /* Code Processor */
    private String CodeProcessor(String line){
        return String.format("<code> %s </code>\n",line);
    }
    
    /* Horizontal Line */
    private String HorizontalProcessor(String line){
        line = "<br />\n";
        return line;
    }
    
    /* Heading Processor */
    private String HeadingProcessor(String line){
        String[] args = line.split(" ",2);
        return String.format("<h%d> %s </h%d>\n",args[0].length(), args[1],args[0].length());
    }
    
    /* Global Processor */
    private String GlobalProcessor(String line){
        return String.format("%s\n",line);
    }
    
    /*  get HTML data */
    public String getHTML(){
        return outputdata;
    }
    
    /* Escape HTML */
    private String escapeHtml(String unsafe) {
    

        unsafe = unsafe.replaceAll("&", "&amp;");
        unsafe = unsafe.replaceAll("<", "&lt;");
        unsafe = unsafe.replaceAll(">", "&gt;");
        unsafe = unsafe.replaceAll("\"", "&quot;");
        unsafe = unsafe.replaceAll("\'", "&#039;");
    return unsafe;
    }
}



/* MarkDown To HTML Convertion Engine */
public class MarkDownEngine {

	/* Global Variable Configuration */
	private static final String input_file = "markdown_input_file.md";  // Input File Path
	private static final String output_file = "output_html_view.html";  // Output File Path
	private String input_data; // Input Markdown Text
	private String output_data; // Output HTML Source Code
	private MarkDownSyntaxParser converter;

	/* Constructor */
	public MarkDownEngine(){

		// Get Input
		input_data = ReadFileData(input_file);
		//System.out.println(input_data); // Our Input Data as String
		
		converter = new MarkDownSyntaxParser(input_data);
		output_data = converter.getHTML();

		// Write Output
		WriteFileData(output_file, output_data);
	}
	/* Write Data To File */
	private void WriteFileData(String path, String content){
		try{
			FileWriter writer = new FileWriter(path);
			writer.write(content);
			writer.flush();
			writer.close();

		}catch(Exception error){
			System.out.println(error);
			error.printStackTrace();
		}
	}

	/* Read Data from File */
	private String ReadFileData(String path){
		String content=""; // Content
		String line;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while(true){
				line=reader.readLine();
				if (line==null) {
					break;
				}
				content = String.format("%s%s\n", content, line);
			}

		}catch(Exception error){
			System.out.println(error);
			error.printStackTrace();
		}
		return content;
	}



	/* Main Trigger Function */
	public static void main(String[] args){
		MarkDownEngine obj = new MarkDownEngine();
	}
}
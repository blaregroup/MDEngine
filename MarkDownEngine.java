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

*/

// importing module
import java.io.*;



/* Markdown To HTML Generator */
class MarkdownToHTMLGenerator{

	public String convertToHTML(String data){
		return data; // Converted HTML String
	}
}


/* MarkDown To HTML Convertion Engine */
public class MarkDownEngine {

	/* Global Variable Configuration */
	private static final String input_file = "markdown_input_file.md";  // Input File Path
	private static final String output_file = "output_html_view.html";  // Output File Path
	private String input_data; // Input Markdown Text
	private String output_data; // Output HTML Source Code
	private MarkdownToHTMLGenerator converter;

	/* Constructor */
	public MarkDownEngine(){
		// Get Input
		input_data = ReadFileData(input_file);
		//System.out.println(input_data); // Our Input Data as String
		
		converter = new MarkdownToHTMLGenerator();
		output_data = converter.convertToHTML(input_data);

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
		System.out.print(content);
		return content;
	}



	/* Main Trigger Function */
	public static void main(String[] args){
		MarkDownEngine obj = new MarkDownEngine();
	}
}
package mim.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import mim.Parameters;
import mim.seq.SeqImageSearch;

/**
 * Servlet implementation class MIMServletHome
 */
@MultipartConfig() 
@WebServlet("/MIMServletHome")
public class MIMServletHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MIMServletHome() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.print("hello");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);		
	}
	
	/***
	 * Servlet post. It handles the search process.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  		
		// initialize PrinterWriter
		PrintWriter out = response.getWriter();  
		// retrieve filename
		final Part filePart = request.getPart("element_1");
		String filePath = getFileName(filePart);
		File file = new File(filePath);
		String fileName = file.getName(); 
		
		// upload image to server's workspace
		String path = "C:/apache-tomcat-7.0.47/work";
		OutputStream outStream = null;
	    InputStream filecontent = null;
	    try {
	        outStream = new FileOutputStream(new File(path + File.separator
	                + fileName));
	        filecontent = filePart.getInputStream();

	        int read = 0;
	        final byte[] bytes = new byte[1024];

	        while ((read = filecontent.read(bytes)) != -1) {
	            outStream.write(bytes, 0, read);
	        }
	        System.out.println("New file " + fileName + " created at " + path);
	    } catch (FileNotFoundException fne) {
	    	System.out.println("You either did not specify a file to upload or are "
	                + "trying to upload a file to a protected or nonexistent "
	                + "location.");
	    	System.out.println("<br/> ERROR: " + fne.getMessage());
	    } finally {
	        if (outStream != null) {
	            outStream.close();
	        }
	        if (filecontent != null) {
	            filecontent.close();
	        }
	    }
		
		// perform search
		try {
			SeqImageSearch SIS = new SeqImageSearch();
			//SIS.search(fileName);
			SIS.searchByPath(path+"/"+fileName);
			Files.lines(Parameters.RESULTS_HTML.toPath()).forEach(line -> { 
				//System.out.println(line); 
				out.print(line);
				});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (out != null) {
            out.close();
        }
	}
	
	/***
	 * Retrieves the file path from the form's part (element_1)
	 * @param part the form part for the input
	 * @return a string containing just the filepath of the chosen image
	 */
	private String getFileName(final Part part) {
	    for (String content : part.getHeader("content-disposition").split(";")) {
	        if (content.trim().startsWith("filename")) {
	            return content.substring(
	                    content.indexOf('=') + 1).trim().replace("\"", "");
	        }
	    }
	    return null;
	}
}

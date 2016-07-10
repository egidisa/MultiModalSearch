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
import mim.tools.Output;
import multiModal.LuceneIndexSearcher;

/**
 * Servlet implementation class MIMServletHome
 */
@MultipartConfig()
@WebServlet("/MIMServletHome")
public class MIMServletHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private LuceneIndexSearcher lis;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MIMServletHome() {
		super();
		lis = new LuceneIndexSearcher();
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.print("hello");
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		doPost(request, response);
	}

	/***
	 * Servlet post. It handles the search process.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// initialize PrinterWriter
		PrintWriter out = response.getWriter();

		// retrieve form inputs
		String tags = request.getParameter("tags");
		System.err.println("TAGS = " + tags);
		if (tags.equals(""))
			tags = null;
		
		String selectedImage = request.getParameter("selectedImage");
		if( ! selectedImage.equals("-1"))
				selectedImage = selectedImage.split("[/]")[2];
		else
			selectedImage = null;
		
		System.err.println("SELECTED_IMAGE = " + selectedImage);
		
		String fileName = null;
		String uploadedFileNewPath = null;
		
		// retrieve file name of the uploaded file
		final Part filePart = request.getPart("fileUpload");
		System.out.println("PART FILE = " + filePart.getHeader("content-disposition").toString());
		String filePath = getFileName(filePart);
		System.out.println("SERVLET UPLOADED FILE PATH = " + filePath);
		if (! filePath.equals("")) {
			// read uploading file
			File file = new File(filePath);
			fileName = file.getName();

			// save image to server's workspace
			String path = Parameters.SERVER_WORKSPACE;
			
			uploadedFileNewPath =  path+"/"+fileName;
			
			OutputStream outStream = null;
			InputStream filecontent = null;
			try {
				System.out.println("NEW FILE PATH = " + path + "/"+ fileName );
				outStream = new FileOutputStream(new File(uploadedFileNewPath));
				filecontent = filePart.getInputStream();

				int read = 0;
				final byte[] bytes = new byte[1024];

				while ((read = filecontent.read(bytes)) != -1) {
					outStream.write(bytes, 0, read);
				}
				// TODO remove
				System.out.println("New file " + fileName + " created at " + path);
			} catch (FileNotFoundException fne) {
				System.err.println("You either did not specify a file to upload or are "
						+ "trying to upload a file to a protected or nonexistent " + "location.");
				System.err.println("ERROR: " + fne.getMessage());
			} finally {
				if (outStream != null) {
					outStream.close();
				}
				if (filecontent != null) {
					filecontent.close();
				}
			}
		}
		
		// perform search
		try {
			lis.search(tags, uploadedFileNewPath, selectedImage, Parameters.DEEP_LAYER7); //selectedImage
			lis.search(tags, uploadedFileNewPath, selectedImage, Parameters.DEEP_LAYER6);
			Output.searchResultsJoinedToHTML(Parameters.RESULTS_HTML);
			Files.lines(Parameters.RESULTS_HTML.toPath()).forEach(line -> {
				// System.out.println(line);
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
	 * 
	 * @param part
	 *            the form part for the input
	 * @return a string containing just the filepath of the chosen image
	 */
	private String getFileName(final Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}

package ca.topnotchgames;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.*;


//import com.google.appengine.api.datastore.DatastoreService;
//import com.google.appengine.api.datastore.DatastoreServiceFactory;
//import com.google.appengine.api.datastore.Entity;
//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.KeyFactory;



import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
//appengine
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import com.oreilly.servlet.multipart.PartInputStream;

@SuppressWarnings("serial")
public class newfiler extends HttpServlet {


	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		synchronized (this) {

			resp.setContentType("text/html");
			PrintWriter out = resp.getWriter();
			out.println("<html><head><title>Game Filer 1.0</title><style>"
					+ "table{	border-collapse:collapse;}"
					+ "td{border: 1px solid black;" + "padding:20px;}"
					+ "</style></head><body>");
			out.println("<a class=\"home-a\" href=\"/\">  Top Notch Games </a><br /><br />");
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();

			boolean lock = true;

			if (user == null) {
				out.print("<a href=\"");
				out.print(userService.createLoginURL("/newfiler"));
				out.print("\"> login to manage site </a></body></html>");
				return;
			}

			String allowdomain = "topnotchgames.ca";
			String email = user.getEmail();

			if ( email.endsWith(allowdomain)) {
				lock = false;
			}


			out.print("<a href=\"");
			out.print(userService.createLogoutURL("/newfiler"));
			out.print("\">logout " + user.getEmail() + "</a>");

			if (lock==true) {

				out.print("<p>locked</p>");
				out.print("</body></html>");
				return;
			}
			java.util.Date d = new java.util.Date();

			resp.getWriter().println("<FORM ACTION=\"/newfiler\" ENCTYPE=\"multipart/form-data\" METHOD=POST>	Upload File <INPUT TYPE=FILE NAME=secretDocument><br><input type=\"submit\" value=Submit><br></FORM>");
			resp.getWriter().println(d + "</body></html>");

		} // end sycronized
	}// end doGet


	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	
		ServletOutputStream os = resp.getOutputStream();

		MultipartParser parser = new MultipartParser(req, 1000000);

		StringBuilder lines = new StringBuilder();

		Part part;
		while ((part = parser.readNextPart()) != null) {
			String name = part.getName();
			if (name == null) {
				throw new IOException(
						"Malformed input: parameter name missing (known Opera 7 bug)");
			}
			if (part.isParam()) {
				// It's a parameter part, add it to the vector of values
				/*
				 * ParamPart paramPart = (ParamPart) part; String value =
				 * paramPart.getStringValue(); Vector existingValues = (Vector)
				 * parameters.get(name); if (existingValues == null) {
				 * existingValues = new Vector(); parameters.put(name,
				 * existingValues); } existingValues.addElement(value);
				 */
			} else if (part.isFile()) {
				FilePart filePart = (FilePart) part;
				PartInputStream partInput = (PartInputStream) filePart.getInputStream();
			
				Vector dataHolder=importExcelSheet(partInput);
				
				
				StringBuilder xmlsb = new StringBuilder();
				int row = 0;
				int col = 0;
				boolean done = false;
				
				xmlsb.append("<sheet>");
				for ( Object  hssfrow : dataHolder) {
					row++;
					
					// Going forward
					if (row>10 && !done) {
					xmlsb.append("<row num=\"" + row + "\">");
					}
					Vector hssfrowV = (Vector)hssfrow;
					//os.print("\n");
					col = 0;
					for ( Object hssfcell : hssfrowV) {
						col++;
						
						HSSFCell cell = (HSSFCell)hssfcell;
						if (cell.toString().startsWith("Going forward")) {
							done = true;
							xmlsb.delete(xmlsb.lastIndexOf("<row num"), xmlsb.length());
						}
					//	os.print(" -- " + cell.toString());
						if (row>10 && !done) {
						xmlsb.append("<col num=\"" + col + "\">");
						xmlsb.append(escapeTags(cell.toString()));
						xmlsb.append("</col>");
						}
					}
					if (row>10 &&!done) {
					xmlsb.append("</row>");
					}
				}
				xmlsb.append("</sheet>");
				
				//debug
				//os.print(xmlsb.toString());
				
				String output = xsl.transform(xmlsb.toString(), this, "newfile.xsl");
				os.print(output);
			}
		}

		
		

	
	} // end dopost

	
	
	private Vector importExcelSheet(InputStream is )
	{
	    Vector cellVectorHolder = new Vector();
	    try
	    {
	        Workbook workBook = WorkbookFactory.create(is);
	        Sheet sheet = workBook.getSheetAt(1);
	        Iterator rowIter = sheet.rowIterator();

	        while(rowIter.hasNext())
	        {
	            HSSFRow row = (HSSFRow) rowIter.next();
	            Iterator cellIter = row.cellIterator();
	            Vector cellStoreVector=new Vector();

	            while(cellIter.hasNext())
	            {
	                HSSFCell cell = (HSSFCell) cellIter.next();
	                cellStoreVector.addElement(cell);
	            }
	            cellVectorHolder.addElement(cellStoreVector);
	        }
	    }
	    catch (Exception e)
	    {
	        System.out.println(e.getMessage());
	    }
	    return cellVectorHolder;
	}
	
	
	
	
	/**
     * This method takes a string which may contain HTML tags (ie,
     * &lt;b&gt;, &lt;table&gt;, etc) and replaces any
     * '<',  '>' , '&' or '"'
     * characters with respective predefined entity references.
     *
     * @param input The text to be converted.
     * @return The input string with the special characters replaced.
     * */
    static public String escapeTags(final String input) {
        // Check if the string is null, zero length or devoid of special characters
        // if so, return what was sent in.

        if(input == null
            || input.length() == 0
            || (input.indexOf('"') == -1 &&
            input.indexOf('&') == -1 &&
            input.indexOf('<') == -1 &&
            input.indexOf('>') == -1)) {
            return input;
        }

        StringBuilder buf = new StringBuilder(input.length() + 6);
        char ch;

        int len = input.length();
        for(int i=0; i < len; i++) {
            ch = input.charAt(i);
            if (ch > '>') {
                buf.append(ch);
            } else if(ch == '<') {
                buf.append("&lt;");
            } else if(ch == '>') {
                buf.append("&gt;");
            } else if(ch == '&') {
                buf.append("&amp;");
            } else if(ch == '"') {
                buf.append("&quot;");
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
	
	
}


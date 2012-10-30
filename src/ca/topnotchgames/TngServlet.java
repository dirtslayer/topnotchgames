package ca.topnotchgames;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.PartInputStream;

@SuppressWarnings("serial")
public class TngServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
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
				// It's a file part
				FilePart filePart = (FilePart) part;
				PartInputStream partInput = (PartInputStream) filePart.getInputStream();
				//filePart.writeTo(os);
				
				
				long size=0;
			    int read;
			    byte[] buf = new byte[8 * 1024];
			    String bytein = null;
			    
			    while((read =  partInput.read(buf)) != -1) {
			    	
			      	
			      os.println("\n------------------------");	
			      os.println("read: " + read);	
			    	
			     
			      
			      size += read;
			      
			      bytein = new String(buf,0, read);
			      lines.append(bytein);
			      
			      os.println("added:" + bytein);
			      os.println("\n------------/add------------");	
				      
			      
			    }
			    
				

			}
		}
		
		String line = null;
		int start = 0;
		int end = 0;
		while (true) {
			end = lines.indexOf("\n",start);
			if ( end < 0) break;
			
			line = lines.substring(start, end);
			
			os.println("line: " + line);
			start = end + 1;
			
			
			// columns now
			int cstart = 0;
			int cend = 0;
			String col;
			int coln = 0;
			while (true) {
				cend = line.indexOf('\t',cstart);
				if (cend < 0) break;
				col = line.substring(cstart,cend);
				os.println("col " + coln + ": " + col);
				cstart = cend + 1;
				coln++;
				if ( cstart >= line.length()) break;
				
				// fill in tempItem
			}
				
			
			// lookup in db if tempItem is already in there (we done want to clober meta)
			// if it is, adjust quantity, adjust price, updated
			
			if (start >= lines.length()) break;
		}
		
		
		
	}
}

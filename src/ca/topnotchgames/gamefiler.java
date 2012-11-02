package ca.topnotchgames;



import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;




//servlet and java io
import java.io.IOException;



import java.io.PrintWriter;

import java.util.LinkedList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class gamefiler extends HttpServlet {


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
				out.print(userService.createLoginURL("/gamefiler"));
				out.print("\"> login to manage site </a></body></html>");
				return;
			}

			String allowdomain = "topnotchgames.ca";
			String email = user.getEmail();

			if ( email.endsWith(allowdomain)) {
				lock = false;
			}


			out.print("<a href=\"");
			out.print(userService.createLogoutURL("/gamefiler"));
			out.print("\">logout " + user.getEmail() + "</a>");

			if (lock==true) {

				out.print("<p>locked</p>");
				out.print("</body></html>");
				return;
			}
			java.util.Date d = new java.util.Date();

			resp.getWriter().println("<FORM ACTION=\"/gamefiler\" ENCTYPE=\"multipart/form-data\" METHOD=POST>	Upload File <INPUT TYPE=FILE NAME=secretDocument><br><input type=\"submit\" value=Submit><br></FORM>");
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
			//	long size=0;
				int read;
				byte[] buf = new byte[8 * 1024];
				String bytein = null;

				while((read =  partInput.read(buf)) != -1) {
			//		size += read;
					bytein = new String(buf,0, read);
					lines.append(bytein);
				}
			}
		}

		String line = null;
		int start = 0;
		int end = 0;
		int linen=0;

		LinkedList<Item> itemlist = new LinkedList<Item>();
		Item newItem = null;

		while (true) {
			end = lines.indexOf("\n",start);
			if ( end < 0) break;

			line = lines.substring(start, end);

			
			start = end + 1;
			linen++;
			if (linen==1) continue; //loose header
			
			// columns now
			int cstart = 0;
			int cend = 0;
			String col;
			int coln = 0;
			int quantity = 0;
			newItem = new Item();


			while (true) {
				cend = line.indexOf('\t',cstart);
				if (cend < 0) break;
				col = line.substring(cstart,cend);
				if (coln==0) {
					newItem.setGroup(col);
				} else if (coln==3) {
					newItem.setName(escapeTags(col));
				} else if (coln==8) {
					newItem.setPrice(col);
				} else if (coln==20) {
					if  ( 0 < col.length() ) {
						quantity = Integer.parseInt(col);
					} else {
						quantity = 0;
					}
				}

				cstart = cend + 1;
				coln++;
				if ( cstart >= line.length()) break;

			} // end loop column
			
			
			boolean add = true; 
			
			if (    ( quantity < 1 ) ||
					( newItem.getGroup().compareTo("Phone Cases and Accessories") == 0 ) ||		
					( newItem.getGroup().compareTo("NEW Accessories") == 0 ) ||
					( newItem.getGroup().compareTo("USED Accessories") == 0 ) ||
					( newItem.getGroup().compareTo("USED Consoles") == 0 ) ||
					( newItem.getGroup().compareTo("Keychains") == 0 ) ||
					( newItem.getGroup().compareTo("NEW Accessories") == 0 ) ||
					( newItem.getGroup().compareTo("NEW Consoles") == 0 )  )
					{
						add = false;
					}
		
			if (add==true) {
				itemlist.add(newItem);
			}
	
			if (start >= lines.length()) break;
		} // end loop input lines

		
		String cataloguexsl = xsl.transform(itemlist, this, "catalogue.xsl");
		Text dbcataloguexsl = new Text(cataloguexsl);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Key catkey = KeyFactory.createKey("catalogue", "html");
		Entity catalogue = new Entity(catkey);

		catalogue.setProperty("html", dbcataloguexsl);
		
		datastore.put(catalogue);
		
		os.print(dbcataloguexsl.getValue());
		
		//Dao.INSTANCE.persistList(itemlist);

	    //resp.sendRedirect("/catalogue");

	} // end dopost

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

package ca.topnotchgames;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class catalogue extends HttpServlet {
	
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/html");

		ServletOutputStream os = resp.getOutputStream();
		
		List<Item> il = Dao.INSTANCE.listItems();
		
		// may want to parameterize category and pass that somehow ... 
		//Item.printlistXSLT(il, this, os, "catalogue.xsl");
		
		os.print( xsl.transform(il, this, "catalogue.xsl") );
		
	}
	
}

package ca.topnotchgames;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class catalogue extends HttpServlet {
	
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/html");

		ServletOutputStream os = resp.getOutputStream();
		
		//List<Item> il = Dao.INSTANCE.listItems();
		
		// may want to parameterize category and pass that somehow ... 
		//Item.printlistXSLT(il, this, os, "catalogue.xsl");
		
		//os.print( xsl.transform(il, this, "catalogue.xsl") );
		
		//String cataloguexsl = xsl.transform(itemlist, this, "catalogue.xsl");
		//Text dbcataloguexsl = new Text(cataloguexsl);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		Key catkey;
		Entity catalogue;
		Text text;
		
		try {
			catkey = KeyFactory.createKey("catalogue", "html");
			catalogue = datastore.get(catkey);
			text = (Text) catalogue.getProperty("html");
			os.print(text.getValue());
		
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
}

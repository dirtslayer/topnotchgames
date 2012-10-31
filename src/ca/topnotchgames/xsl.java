package ca.topnotchgames;


import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.GenericServlet;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.trans.XPathException;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class xsl {

public static String transform(List<Item> il,GenericServlet host, String style) {
	    	String key = "xsl" + style;
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			syncCache.setErrorHandler(ErrorHandlers
						.getConsistentLogAndContinue(Level.INFO));
			String os = (String)syncCache.get(key);
			if (os != null) {
					return os;
			}

	    	StringBuilder sb = new StringBuilder();
			sb.append("<itemlist>");
			for (Item i:il) {
				sb.append("" + i.toXML() );
			}
			sb.append("</itemlist>");
			StringReader sr = new StringReader(sb.toString());
			
			StringWriter sw = new StringWriter();
			javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(sw);

			
			 try {
		            Templates pss = tryCache(host, style);
		            Transformer transformer = pss.newTransformer();
		            transformer.transform(new StreamSource(sr), result);
			  } catch (Exception e) {
				 e.printStackTrace();
			  }
	    	String toret = sw.getBuffer().toString();	
	    	syncCache.put(key, toret);
	    	return toret;
	    }
	    
		
	 static TransformerFactory factory = initfactory();
		private static TransformerFactory initfactory(){
	        System.setProperty("javax.xml.transform.TransformerFactory",
	                           "net.sf.saxon.TransformerFactoryImpl");
	        return TransformerFactory.newInstance();      
	    }
	    @SuppressWarnings({ "unchecked" })
		private synchronized static Templates tryCache(GenericServlet host, String url) throws TransformerException {
	        String path = host.getServletContext().getRealPath(url);
	        if (path==null) {
	            throw new XPathException("Stylesheet " + url + " not found");
	        }
	       
	       Templates x = (Templates)cache.get(path);
	        if (x==null) {
	            x = factory.newTemplates(new StreamSource(new File(path)));
	            cache.put(path, x);
	        }
	        return x;
	    }
	    @SuppressWarnings("rawtypes")
		public static synchronized void clearCache() {
	        cache = new HashMap(20);
	    }
	    @SuppressWarnings("rawtypes")
		private static HashMap cache = new HashMap(5);
	
}

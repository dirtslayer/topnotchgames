package ca.topnotchgames;


import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.servlet.GenericServlet;

import javax.servlet.ServletOutputStream;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.trans.XPathException;


@Entity
public class Item implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5575242952509062767L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String group;    // col 0
	private String category; // col 1
	private String name; // col 3
	private String price; // col 8
	private String quantity; // col 20
	private String imageurl;
	private String notes;
	
	
	
	static TransformerFactory factory = initfactory();
	private static TransformerFactory initfactory(){
        System.setProperty("javax.xml.transform.TransformerFactory",
                           "net.sf.saxon.TransformerFactoryImpl");
        return TransformerFactory.newInstance();      
    }
    @SuppressWarnings({ "unused", "unchecked" })
	private synchronized static Templates tryCache(GenericServlet host, String url) throws TransformerException {
        String path = host.getServletContext().getRealPath(url);
        if (path==null) {
            throw new XPathException("Stylesheet " + url + " not found");
        }
        //
        // disable cache
        
      //  Templates x = null;
        
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
	private static HashMap cache = new HashMap(20);
	
    public static void printlistXSLT(List<Item> il, GenericServlet host, ServletOutputStream out, String style) {
		StringBuilder sb = new StringBuilder();
		sb.append("<itemlist>");
		for (Item i:il) {
			sb.append("" + i.toXML() );
		}
		sb.append("</itemlist>");
		
		StringReader sr = new StringReader(sb.toString());
		
		  try {
	            Templates pss = tryCache(host, style);
	            Transformer transformer = pss.newTransformer();
	            transformer.transform(new StreamSource(sr), new StreamResult(out));
		  } catch (Exception e) {
			 e.printStackTrace();
		  }
		
		
	}
	
	
	
	
	
	
	
	public Item(String category2, String group2, String imageurl2,
			String name2, String price2, String quantity2, String notes2) {
		this.category = category2;
		this.group = group2;
		this.imageurl = imageurl2;
		this.name = name2;
		this.price = price2;
		this.quantity = quantity2;
		this.notes = notes2;
	}
	
	public static void printlistXML(LinkedList<Item> il, ServletOutputStream os) {
		try {
			for (Item i:il) {
				os.println("" + i.toXML() );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public String toXML() {
		return "<item><id>" + id + "</id><group>" + group + "</group><category>" + category + "</category><name>" +
				name + "</name><price>" + price + "</price><quantity>" + quantity + "</quantity><imageurl>" +
				imageurl + "</imageurl><notes>" + notes + "</notes></item>";
	}
	
	public Item() {
		
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}	
	
	
public void setGroup(String s) {
		this.group = s;
	}
	public String getGroup () {
		return this.group ;
	}
	
public void setCategory (String s) {
		this.category = s;
	}
	public String getCategory () {
		return this.category  ;
	}
	
public void setName(String s) {
		this.name = s;
	}
	public String getName() {
		return this.name ;
	}
	
public void setPrice(String s) {
		this.price = s;
	}
	public String getPrice() {
		return this.price ;
	}
	
public void setQuantity(String s) {
		this.quantity = s;
	}
	public String getQuantity() {
		return this.quantity;
	}
	
public void setImageurl(String s) {
		this.imageurl = s;
	}
	public String getImageurl () {
		return this.imageurl ;
	}
	
	public void setNotes(String s) {
		this.notes = s;
	}
	public String getNotes () {
		return this.notes ;
	}
	

}

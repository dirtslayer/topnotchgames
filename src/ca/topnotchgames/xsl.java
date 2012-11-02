package ca.topnotchgames;


import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import java.util.List;

import javax.servlet.GenericServlet;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.trans.XPathException;

public class xsl {

	public static String transform(List<Item> il,GenericServlet host, String style) {

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
			String path = host.getServletContext().getRealPath(style);
			if (path==null) {
				throw new XPathException("Stylesheet " + style + " not found");
			}
			Templates pss = factory.newTemplates(new StreamSource(new File(path)));
			Transformer transformer = pss.newTransformer();
			transformer.transform(new StreamSource(sr), result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String toret = sw.getBuffer().toString();	
		return toret;
	}


	static TransformerFactory factory = initfactory();
	private static TransformerFactory initfactory(){
		System.setProperty("javax.xml.transform.TransformerFactory",
				"net.sf.saxon.TransformerFactoryImpl");
		return TransformerFactory.newInstance();      
	}

}

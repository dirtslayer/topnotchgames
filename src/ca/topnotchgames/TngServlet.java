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
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.getOutputStream().println("disabled");
	}
}

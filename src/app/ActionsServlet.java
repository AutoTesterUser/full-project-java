package app;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ActionsServlet")
public class ActionsServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
				
		System.out.println("Llegó hasta aca");
		
		String resultado ="Bieen";
		
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.write(resultado);
		
		
	}

}

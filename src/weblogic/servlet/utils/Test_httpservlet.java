package weblogic.servlet.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test_httpservlet extends HttpServlet{

  /**
   * 
   */
  private static final long serialVersionUID = -5476748861742389241L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    out.println("<html><head><title>Hello World!</title></head>");
    out.println("<body>");
    out.println("<h1>Hello World!</h1>");
    out.println("Du bad om skanneforside med journal nr: "+req.getParameter("id"));
    out.println("</body></html>");
    out.close();  }
  

}

package weblogic.servlet.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SfsPdfServlet  extends HttpServlet{

  /**
   * 
   */
  private static final long serialVersionUID = -1909789806006251188L;
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    Context ctx = null;
    //Hashtable ht = new Hashtable();
    //ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
    //ht.put(Context.PROVIDER_URL, "t3://u1804i-0409:8080");
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      //ctx = new InitialContext(ht);
      ctx = new InitialContext();
      javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("jdbc/sfsData");
      conn = ds.getConnection();
      String journalnr = req.getParameter("id");
      stmt = conn.prepareStatement("SELECT\n" + 
          "    *\n" + 
          "FROM\n" + 
          "    presis.mellomlager_skanneforside mYtre\n" + 
          "WHERE\n" + 
          "        journalnr = ?\n" + 
          "    AND odato = (\n" + 
          "        SELECT\n" + 
          "            MAX(odato)\n" + 
          "        FROM\n" + 
          "            presis.mellomlager_skanneforside mIndre\n" + 
          "        WHERE\n" + 
          "            mIndre.journalnr = mYtre.journalnr\n" + 
          "    )");
      stmt.setString(1, journalnr);
      rs = stmt.executeQuery();        
      if (rs.next()) {
        res.setContentType("application/pdf");
        OutputStream out = res.getOutputStream();
        out.write(rs.getBytes("PDF"));
      } else {
       res.setContentType("text/html");
       PrintWriter out = res.getWriter();
       out.println("<html><head><title>Skanneforsider!</title></head>");
       out.println("<body>");
       out.println("Du bad om skanneforside med journal nr: "+ journalnr + ". Fant ingen skanneforder på det journalnummeret!");
       out.println("</body></html>");
       out.close();        
      }
      stmt.close();
      stmt=null;
      conn.close();
      conn=null;
      
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try { 
        ctx.close(); 
      } catch (Exception e) {
         System.err.println("Kunne ikke avslutte context"); }
      try { 
        if (rs != null) rs.close(); 
      } catch (Exception e) {  
        System.err.println("Satan 1"); }
      try { 
        if (stmt != null) stmt.close(); 
      } catch (Exception e) {  
        System.err.println("Satan 2"); }
      try { 
        if (conn != null) conn.close(); 
      } catch (Exception e) {  
        System.err.println("Satan 3"); }
    }
    
  }  

}

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/AppServlet")
public class AppServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String action = request.getParameter("action");
        
        if ("login".equals(action)) {
            // Easy Level: Handling user login
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            if ("admin".equals(username) && "1234".equals(password)) {
                out.println("<h1>Welcome, " + username + "!</h1>");
            } else {
                out.println("<h1>Invalid Credentials</h1>");
            }
        } else if ("searchEmployee".equals(action)) {
            // Medium Level: Fetch employee details from the database
            String empId = request.getParameter("empId");
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/company", "root", "password");
                PreparedStatement ps = con.prepareStatement("SELECT * FROM employees WHERE id = ?");
                ps.setString(1, empId);
                ResultSet rs = ps.executeQuery();
                
                out.println("<h2>Employee Details</h2>");
                while (rs.next()) {
                    out.println("<p>ID: " + rs.getInt("id") + "</p>");
                    out.println("<p>Name: " + rs.getString("name") + "</p>");
                    out.println("<p>Department: " + rs.getString("department") + "</p>");
                }
                con.close();
            } catch (Exception e) {
                out.println("<h3>Error: " + e.getMessage() + "</h3>");
            }
        } else if ("submitAttendance".equals(action)) {
            // Hard Level: Save student attendance in database
            String studentName = request.getParameter("studentName");
            String date = request.getParameter("date");
            String status = request.getParameter("status");
            
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/school", "root", "password");
                PreparedStatement ps = con.prepareStatement("INSERT INTO attendance (student_name, date, status) VALUES (?, ?, ?)");
                ps.setString(1, studentName);
                ps.setString(2, date);
                ps.setString(3, status);
                int i = ps.executeUpdate();
                
                if (i > 0) {
                    out.println("<h2>Attendance recorded successfully!</h2>");
                }
                con.close();
            } catch (Exception e) {
                out.println("<h3>Error: " + e.getMessage() + "</h3>");
            }
        }
    }
}

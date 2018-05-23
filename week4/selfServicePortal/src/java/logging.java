/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author cb-kiruthika
 */
public class logging extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            Users u = new Users();
            User loginUser = new User();

            String type = request.getParameter("type");

            if (type.equals("signin")) {

                loginUser = u.loginUser(request.getParameter("email"), request.getParameter("password"));

                if (loginUser == null) {

                    out.println("<script>alert('Wrong Email/Password')</script>");
                    request.getRequestDispatcher("/index.html").include(request, response);

                } else {

                    HttpSession session = request.getSession();

                    session.setAttribute("firstName", loginUser.firstName);
                    session.setAttribute("lastName", loginUser.lastName);
                    session.setAttribute("email", loginUser.email);
                    session.setAttribute("address", loginUser.address);

                    request.getRequestDispatcher("/info.jsp").include(request, response);
                }

            } else if (type.equals("signup")) {

                loginUser = u.addUser(request.getParameter("first_name"), request.getParameter("last_name"), request.getParameter("email"), request.getParameter("password"));
                if (loginUser == null) {
                    out.println("<script>alert('Try another email');</script>");
                    request.getRequestDispatcher("/index.html").include(request, response);
                } else {
                    //out.println(loginUser.email);
                    HttpSession session = request.getSession();

                    session.setAttribute("firstName", loginUser.firstName);
                    session.setAttribute("lastName", loginUser.lastName);
                    session.setAttribute("email", loginUser.email);
                    session.setAttribute("address", loginUser.address);

                    request.getRequestDispatcher("/info.jsp").include(request, response);
                }

            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(logging.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(logging.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

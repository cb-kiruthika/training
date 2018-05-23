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
public class updating extends HttpServlet {

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
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute("email");

            if (type.equals("add")) {
                String address = request.getParameter("newAddress");
                if (address != "") {
                    u.updateUser(email, address);
                    session.setAttribute("address", address);
                }

                request.getRequestDispatcher("/info.jsp").include(request, response);

            } else if (type.equals("edit")) {

                String newFirstName = request.getParameter("newFirstName");
                String newLastName = request.getParameter("newLastName");
                String address = request.getParameter("newAddress");

                if ((!newFirstName.equals("")) || (!address.equals("")) || (!newLastName.equals(""))) {//check for atleast one input

                    //replace empty inputs
                    if (address.equals("")) {
                        address = (String) session.getAttribute("address");
                    }
                    if (newFirstName.equals("")) {
                        newFirstName = (String) session.getAttribute("firstName");
                    }
                    if (newLastName.equals("")) {
                        newLastName = (String) session.getAttribute("lastName");
                    }

                    u.updateUser(email, newFirstName, newLastName, address);

                    session.setAttribute("firstName", newFirstName);
                    session.setAttribute("lastName", newLastName);
                    session.setAttribute("address", address);
                }

                request.getRequestDispatcher("/info.jsp").include(request, response);

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
            Logger.getLogger(updating.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(updating.class.getName()).log(Level.SEVERE, null, ex);
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

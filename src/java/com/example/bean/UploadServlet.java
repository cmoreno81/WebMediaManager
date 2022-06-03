/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.bean;

import com.example.media.MediaItem;
import com.example.media.MediaManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author cristina
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
@MultipartConfig(location = "/tmp")
public class UploadServlet extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UploadServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        processRequest(request, response);
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
        //processRequest(request, response);
        ServletContext context = getServletContext();
        String realPath = context.getRealPath("fxmedia");
        FileMediaBean fmm = new FileMediaBean(realPath);
        MediaManager mm = fmm.getMediaManager();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Resultados de la carga</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet UploadServlet at " + request.getContextPath() + "</h1>");
        out.println("</body>");
        out.println("</html>");
        Collection<Part> parts = request.getParts();
        if (parts.isEmpty()) {
            out.println("<h2>La solicitud no es un formulario multipart</h2>");
        } else {
            for (Part part : parts) {
                String name = part.getSubmittedFileName();
                try {
                    String contentType = new MimetypesFileTypeMap().getContentType(name);
                    if (contentType.startsWith("image") || contentType.startsWith("video")) {
                        int periodIndex = name.lastIndexOf(".");
                        String fileExtension = name.substring(periodIndex);
                        String title = name.substring(0, periodIndex);
                        MediaItem item = new MediaItem(title, name, new Date());
                        out.println("<br>ID: " + name);
                        out.println("<br>Title: " + title);
                        out.println("<br> Extension: " + fileExtension);
                        mm.createMediaItem(item, part.getInputStream());
                    } else {
                        out.println("<h2> Formato " + contentType + " no admitido. </h2>");
                    }

                    part.delete();
                } catch (IOException ex) {
                    out.println("<br> Error al cargar el archivo " + name + " : " + ex.getMessage());
                }
            }
        }
        out.println("<br><a href='" + getServletContext().getContextPath() + " /MediaController'> Inicio </a>");
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

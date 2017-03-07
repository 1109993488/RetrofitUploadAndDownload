package com.test;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Created by zwl on 2016/9/28.
 */
public class UploadServlet extends HttpServlet {
    File dir = new File("/Users/wo/Desktop/upload");


    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        Part part = request.getPart("photo1");
//        if (part != null) {
//            writeTo(part);
//        }
//        Part part2 = request.getPart("photo2");
//        if (part2 != null) {
//            writeTo(part2);
//        }
        Collection<Part> parts = request.getParts();
        Iterator<Part> iterator = parts.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Part part = iterator.next();
            System.out.println("part->" + (++i) + "  " + part.getHeader("Content-Disposition"));
            writeTo(part);
        }
    }

    //取得上传文件名
    private String getFileName(Part part) {
        String fileName = part.getSubmittedFileName();
        if (fileName == null || fileName.trim().length() == 0) {
            fileName = part.getName();
        }
        return fileName;
    }

    //存储文件
    private void writeTo(Part part) throws IOException, FileNotFoundException {
        String fileName = getFileName(part);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        InputStream in = part.getInputStream();
        OutputStream out = new FileOutputStream(new File(dir, fileName));
        byte[] buffer = new byte[1024];
        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("-------------get----------------");
        doPost(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("-------------post----------------");
        boolean result = false;
        try {
            processRequest(request, response);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(response.getOutputStream());
        if (result) {
            pw.print("{\"result\":0,\"errorInfo\":\"成功\"}");
        } else {
            pw.print("{\"result\":-1,\"errorInfo\":\"失败\"}");
        }
        pw.close();
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

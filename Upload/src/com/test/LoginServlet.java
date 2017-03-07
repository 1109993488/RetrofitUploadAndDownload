package com.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zwl on 2016/9/28.
 */
@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("-------------login---------------");
        boolean result = false;

        if("username".equals(request.getParameter("username"))
                &&"password".equals(request.getParameter("password"))){
            result=true;
        }
        PrintWriter pw = new PrintWriter(response.getOutputStream());
        if (result) {
            pw.print("{\"result\":0,\"errorInfo\":\"成功\"}");
        } else {
            pw.print("{\"result\":-1,\"errorInfo\":\"失败\"}");
        }
        pw.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

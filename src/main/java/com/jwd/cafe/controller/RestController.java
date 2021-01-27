package com.jwd.cafe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwd.cafe.command.Command;
import com.jwd.cafe.command.RequestContext;
import com.jwd.cafe.command.ResponseContext;
import com.jwd.cafe.command.RestCommandFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/rest")
@MultipartConfig(location = "D:\\Epam projects\\cafe\\target\\cafe\\uploads")
public class RestController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestContext requestContext = new RequestContext(req);
        Command command = RestCommandFactory.getInstance().getCommand(requestContext);

        ResponseContext responseContext = command.execute(requestContext);
        responseContext.getSessionAttributes().forEach(req.getSession()::setAttribute);
        resp.getWriter().write(new ObjectMapper().writeValueAsString(responseContext.getRequestAttributes()));
    }
}
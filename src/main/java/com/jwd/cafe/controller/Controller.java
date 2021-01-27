package com.jwd.cafe.controller;

import com.jwd.cafe.command.*;
import com.jwd.cafe.command.impl.LogoutCommand;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebServlet("/cafe")
public class Controller extends HttpServlet {
    private CommandFactory commandFactory;

    @Override
    public void init() throws ServletException {
        commandFactory = CommandFactory.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestContext requestContext = new RequestContext(req);
        Command command = commandFactory.getCommand(requestContext);
        ResponseContext responseContext = command.execute(requestContext);
        responseContext.getRequestAttributes().forEach(req::setAttribute);
        responseContext.getSessionAttributes().forEach(req.getSession()::setAttribute);
        if (command.getClass().isAssignableFrom(LogoutCommand.class)) {
            req.getSession().invalidate();
        }
        ResponseType responseType = responseContext.getResponseType();
        if (responseType.getType().equals(ResponseType.Type.REDIRECT)) {
            resp.sendRedirect(req.getContextPath() + ((RedirectResponse) responseType).getCommand());
        } else {
            req.getRequestDispatcher(((ForwardResponse) responseType).getPage()).forward(req, resp);
        }
    }
}
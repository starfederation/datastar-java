package org.example.servlets;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

/**
 * HtmlServlet loads and serves the HTML file from the resources' directory.
 */
public class HtmlServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/hello-world.html");
        if (inputStream == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found: hello-world.html");
            return;
        }

        resp.setContentType("text/html;charset=UTF-8");

        try (inputStream; OutputStream output = resp.getOutputStream()) {
            inputStream.transferTo(output);
        }
    }
}

package net.borneolink.rest.in;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyHttpHandler {
//    public static void main(String[] args) throws Exception {
//        Server server = new Server(8080);
//        server.setHandler(new CustomHandler());
//        server.start();
//        server.join();
//    }

    public static class CustomHandler extends AbstractHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            if (request.getMethod().equalsIgnoreCase("POST")) {
                // Read POST parameters
                String param1 = request.getParameter("param1");
                String param2 = request.getParameter("param2");

                // Do something with the parameters
                System.out.println("param1: " + param1);
                System.out.println("param2: " + param2);
            }
            if (request.getMethod().equalsIgnoreCase("GET")) {
                // Read POST parameters
                String param1 = request.getParameter("param1");
                String param2 = request.getParameter("param2");

                // Do something with the parameters
                System.out.println("param1: " + param1);
                System.out.println("param2: " + param2);
            }
            // Send a response
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Hello, Jetty!");

            baseRequest.setHandled(true);
        }
    }
}
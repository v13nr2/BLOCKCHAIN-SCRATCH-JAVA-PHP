package net.v13nr2.rest.in;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.v13nr2.cryptocurrency.Wallet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class MyHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        Map<String, Object> params =
                (Map<String, Object>)httpExchange.getAttribute("parameters");

        Object obj = params.get("fnc");

        if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            httpExchange.sendResponseHeaders(204, -1);
            return;
        }
        switch (obj.toString()) {
            case "register":
                Object objEmail = params.get("email");
                Object objPassword = params.get("password");
                handleResponseRegister(httpExchange,obj.toString(), objEmail.toString(),objPassword.toString());
                break;

            default:
        }
    }



    private void handleResponseRegister(HttpExchange httpExchange, String function, String email, String password)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

        System.out.println("Ada request function = "+function);
        Wallet userA = new Wallet();
        userA.regisWallet(email, password);

        String res = email+ " berhasil di registrasi";

        htmlBuilder.append("").
                //append("<body>").
                //append("<h1>").
                //append("Hello ")
                        append(res);
        //.append("</h1>")
        //.append("</body>")
        //.append("</html>");

        // encode HTML content
        //String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());
        String htmlResponse = htmlBuilder.toString();
        // this line is a must
        httpExchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();

    }

}

package net.borneolink.rest.in;

import com.sun.net.httpserver.*;
import net.borneolink.cryptocurrency.Wallet;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class MyHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

        // Menangani Preflight Request (CORS)
        if (method.equalsIgnoreCase("OPTIONS")) {
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
            httpExchange.sendResponseHeaders(204, -1);
            return;
        }

        Map<String, String> params = new HashMap<>();

        // Handling GET request (query parameters)
        if (method.equalsIgnoreCase("GET")) {
            String query = httpExchange.getRequestURI().getQuery();
            params = parseQueryString(query);
        }
        // Handling POST request (form parameters and JSON)
        else if (method.equalsIgnoreCase("POST")) {
            String contentType = httpExchange.getRequestHeaders().getFirst("Content-Type");
            if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
                params = parseFormUrlEncodedRequestBody(httpExchange);
            } else if (contentType != null && contentType.contains("application/json")) {
                params = parseJsonRequestBody(httpExchange);
            } else {
                sendJsonResponse(httpExchange, createErrorResponse("Content-Type harus 'application/x-www-form-urlencoded' atau 'application/json'"), 400);
                return;
            }
        }

        // Debugging: Print semua parameter yang diterima
        System.out.println("Parameter yang diterima: " + params);

        // Validasi parameter `fnc`
        String function = params.get("fnc");
        if (function == null || !function.equals("register")) {
            sendJsonResponse(httpExchange, createErrorResponse("Parameter 'fnc' harus bernilai 'register'"), 400);
            return;
        }

        String email = params.get("email");
        String password = params.get("password");

//        if (email == null || password == null) {
//            sendJsonResponse(httpExchange, createErrorResponse("Parameter 'email' dan 'password' wajib diisi"), 400);
//            return;
//        }

        if (email == null) {
            sendJsonResponse(httpExchange, createErrorResponse("Parameter 'email' wajib diisi"), 400);
            return;
        }

        handleResponseRegister(httpExchange, email, password);
    }

    private void handleResponseRegister(HttpExchange httpExchange, String email, String password) throws IOException {
        System.out.println("Menerima request register untuk: " + email);

        // Simulasi registrasi wallet
        Wallet userA = new Wallet();
        userA.regisWallet(email, password);

        JSONObject responseJson = new JSONObject();
        responseJson.put("status", "success");
        responseJson.put("message", email + " berhasil didaftarkan!");
        sendJsonResponse(httpExchange, responseJson.toString(), 200);
    }

    private void sendJsonResponse(HttpExchange httpExchange, String jsonResponse, int statusCode) throws IOException {
        httpExchange.sendResponseHeaders(statusCode, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    private String createErrorResponse(String message) {
        JSONObject json = new JSONObject();
        json.put("status", "error");
        json.put("message", message);
        return json.toString();
    }

    // Parsing untuk query string (GET)
    private Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
                }
            }
        }
        return params;
    }

    // Parsing untuk form-urlencoded (POST)
    private Map<String, String> parseFormUrlEncodedRequestBody(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = new HashMap<>();
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder body = new StringBuilder();
        String line;

        System.out.println("🔍 Membaca request body...");
        while ((line = br.readLine()) != null) {
            body.append(line);
        }
        br.close();

        // Debugging: Print isi request body sebelum parsing
        System.out.println("📥 Request Body (RAW): " + body.toString());

        if (body.length() == 0) {
            System.out.println("❌ Request body kosong!");
            return params;
        }

        // Parsing form-urlencoded
        String[] pairs = body.toString().split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                params.put(key, value);
            } else {
                System.out.println("⚠️ Gagal memproses pasangan key-value: " + pair);
            }
        }

        return params;
    }

    // Parsing untuk JSON (POST dengan application/json)
    private Map<String, String> parseJsonRequestBody(HttpExchange httpExchange) throws IOException {
        Map<String, String> params = new HashMap<>();
        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder body = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            body.append(line);
        }
        br.close();

        // Debugging: Print isi request body sebelum parsing
        System.out.println("📥 Request Body JSON: " + body.toString());

        if (body.length() == 0) {
            System.out.println("❌ Request body kosong!");
            return params;
        }

        // Parsing JSON body
        JSONObject jsonBody = new JSONObject(body.toString());
        if (jsonBody.has("fnc")) params.put("fnc", jsonBody.getString("fnc"));
        if (jsonBody.has("email")) params.put("email", jsonBody.getString("email"));
        if (jsonBody.has("password")) params.put("password", jsonBody.getString("password"));

        return params;
    }
}

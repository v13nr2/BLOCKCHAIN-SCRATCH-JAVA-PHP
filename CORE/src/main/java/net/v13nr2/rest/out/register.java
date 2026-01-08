package net.v13nr2.rest.out;

import net.v13nr2.rest.configRest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class register {

    public void doRegister(String email, String password){

        int n;
        for(n=0; n == 1; n++){
            String jj = String.valueOf(n);
        }


        configRest Register = new configRest();
        String request = Register.register_url0;

        URL urlToRequest = null;
        try {
            urlToRequest = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlConnection.setDoOutput(true);
        try {
            urlConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty( "x-api-key", new configRest().xApikey0);
        String postParameters = "email="+email+"&password="+password;

        // write out form parameters
        urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
        PrintWriter out = null;
        try {
            out = new PrintWriter(urlConnection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(postParameters);
        out.close();

        // connect
        try {
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                //sb.append(line + "\n");
                sb.append(line);
            }

            //br.close();
            //System.out.println(sb.toString());

            final String HTML = sb.toString();
            Document document = Jsoup.parse(HTML);
            Element table = document.select("table").first();
            String arrayName = table.select("th").first().text();
            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();
            //Elements ttls = table.getElementsByClass("ttl");
            //Elements nfos = table.getElementsByClass("nfo");

            Elements ttls = table.getElementsByTag("th");
            Elements nfos = table.getElementsByTag("td");


            JSONObject jo = new JSONObject();
            for (int i = 0, l = ttls.size(); i < l; i++) {
                String key = ttls.get(i).text();
                String value = nfos.get(i).text();
                jo.put(key, value);
            }
            jsonArr.put(jo);
            jsonObj.put(arrayName, jsonArr);
            System.out.println(jsonObj.toString());


            //System.out.println("DATA = "+status);
            System.out.println("DATA REGISTER WALLET TERKIRIM!");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static String getJSON(String url) {
        HttpsURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();

            con.connect();


            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            return sb.toString();


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

}

package net.borneolink.rest.out;

import net.borneolink.rest.configRest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class peerList {

    public void doBroadcast(String email, String password) {
        //System.out.println(getJSON(new configRest().listNodes_url));

        System.out.println("Broadcast ke tujuan = ");
        //JSONArray jsonArray = new JSONArray(getJSON(new configRest().restNodes_url0));

        JSONArray jsonArray = null;
        try {
            // Memanggil API dan parsing hasilnya ke JSONArray
            jsonArray = new JSONArray(getJSON(new configRest().restNodes_url0));

            // Mengecek apakah array kosong
            if (jsonArray.length() == 0) {
                System.out.println("JSON Array kosong.");
            } else {
                System.out.println("JSON Array berisi: " + jsonArray.toString(2)); // Format agar lebih mudah dibaca
            }

        } catch (JSONException e) {
            System.err.println("Error saat parsing JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Kesalahan tak terduga: " + e.getMessage());
            e.printStackTrace();
        }


        for (int n = 0; n < jsonArray.length(); n++) {

            System.out.println("on lopp :::  ");
            JSONObject tmpjsonobject = jsonArray.getJSONObject(n);
            System.out.println(" node : " + tmpjsonobject.getString("ip"));


            configRest Register = new configRest();
            String request = tmpjsonobject.getString("ip") + new configRest().register_url.toString();

            URL urlToRequest = null;
            try {
                urlToRequest = new URL(request);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) urlToRequest.openConnection();
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
            urlConnection.setRequestProperty("x-api-key", new configRest().xApikey0);
            String postParameters = "email=" + email + "&password=" + password;

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
                System.out.println(sb.toString());

//                final String HTML = sb.toString();
//                Document document = Jsoup.parse(HTML);
//                Element table = document.select("table").first();
//                String arrayName = table.select("th").first().text();
//                JSONObject jsonObj = new JSONObject();
//                JSONArray jsonArr = new JSONArray();
//                //Elements ttls = table.getElementsByClass("ttl");
//                //Elements nfos = table.getElementsByClass("nfo");
//
//                Elements ttls = table.getElementsByTag("th");
//                Elements nfos = table.getElementsByTag("td");
//
//
//                JSONObject jo = new JSONObject();
//                for (int i = 0, l = ttls.size(); i < l; i++) {
//                    String key = ttls.get(i).text();
//                    String value = nfos.get(i).text();
//                    jo.put(key, value);
//                }
//                jsonArr.put(jo);
//                jsonObj.put(arrayName, jsonArr);
//                System.out.println(jsonObj.toString());


                //System.out.println("DATA = "+status);
                System.out.println("DATA REGISTER WALLET TERKIRIM");


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }



    public static String getJSON(String url) {
        //HttpsURLConnection con = null;
        HttpURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpURLConnection) u.openConnection();

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

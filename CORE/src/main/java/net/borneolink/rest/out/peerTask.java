package net.borneolink.rest.out;

import net.borneolink.rest.configRest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class peerTask {

    public boolean doBroadcast(String email, String password, String passPhrase, String uuid, String privateKey, String publicKey){

        System.out.println("Broadcast ke = ");
        JSONArray jsonArray = new JSONArray(getJSON(new configRest().restNodes_url0));
        for(int n=0;n<jsonArray.length();n++)
        {
            JSONObject tmpjsonobject=jsonArray.getJSONObject(n);
            System.out.println(tmpjsonobject.getString("ip"));

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
            String postParameters = "email="+email+"&password="+password+"&passphrase="+passPhrase+"&uuid="+uuid+"&priv="+privateKey+"&pub="+publicKey;

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
                System.out.println("DATA REGISTER WALLET TERKIRIM");


            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

        return true;
    }


    public void doBroadcastBlock(String blockid, String prevHash, String Hash){
        //System.out.println(getJSON(new configRest().listNodes_url));

        System.out.println("Broadcast Block ke = ");
        JSONArray jsonArray = new JSONArray(getJSON(new configRest().restNodes_url0));
        for(int n=0;n<jsonArray.length();n++)
        {
            JSONObject tmpjsonobject=jsonArray.getJSONObject(n);
            System.out.println(tmpjsonobject.getString("ip"));

            String request = tmpjsonobject.getString("ip") + new configRest().block_url_pre.toString();

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
            String postParameters = "prevhash="+prevHash+"&hash="+Hash+"&blockid="+blockid;

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

                System.out.println("DATA BLOCK TERKIRIM");
                System.out.println("");


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public void doBroadcastTransaction(String hash, String usera, String userb, String label, String amount){
        //System.out.println(getJSON(new configRest().listNodes_url));

        //v13System.out.println("Broadcast Transaksi ke = ");
        JSONArray jsonArray = new JSONArray(getJSON(new configRest().restNodes_url0));
        for(int n=0;n<jsonArray.length();n++)
        {
            JSONObject tmpjsonobject=jsonArray.getJSONObject(n);
            //v13 System.out.println(tmpjsonobject.getString("ip"));

            String request = tmpjsonobject.getString("ip") + new configRest().transaction_url.toString();

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
            String postParameters = "hash_id="+hash.toString()+"&user_a_pubkey="+usera.toString()+"&user_b_pubkey="+userb.toString()+"&trxlabel="+label.toString()+"&amount="+amount.toString();

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
                    System.out.println("HTTP STATUS = "
                            + urlConnection.getResponseCode()
                            + " "
                            + urlConnection.getResponseMessage());
                }





            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public void doBroadcastMeter(String pkey, String meter, String gambar, String nama, String alamat, String lat, String lan){
        //System.out.println(getJSON(new configRest().listNodes_url));

        System.out.println("Broadcast Meter ke = ");
        JSONArray jsonArray = new JSONArray(getJSON(new configRest().restNodes_url0));
        for(int n=0;n<jsonArray.length();n++)
        {
            JSONObject tmpjsonobject=jsonArray.getJSONObject(n);
            System.out.println(tmpjsonobject.getString("ip"));

            String request = tmpjsonobject.getString("ip") + new configRest().meter_url.toString();

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
            String postParameters = "pkey="+pkey.toString()+"&meter="+meter.toString()+"&gambar="+gambar.toString()+"&nama="+nama.toString()+"&alamat="+alamat.toString()+"&lan="+lan.toString()+"&lat="+lat.toString();

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
                System.out.println("DATA METER TERKIRIM");


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public static String getJSON(String url) {
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

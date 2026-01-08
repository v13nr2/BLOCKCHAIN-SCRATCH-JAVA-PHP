package net.borneolink.rest.out;

import net.borneolink.rest.configRest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LastHash {
    public String GetLastHash(){
        String outputObj = "broken";
        try {

            URL url = new URL(new configRest().register_url0);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "x-api-key", new configRest().xApikey0);
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode());
            }
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String output = "";
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                outputObj = output;
            }

            //conn.disconnect();

        } catch (Exception e) {
            System.out.println("Exception in NetClientGet Energon:- " + e);
        }

        return outputObj.toString();
    }
}

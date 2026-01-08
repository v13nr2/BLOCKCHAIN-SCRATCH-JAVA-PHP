package net.borneolink.rest;

public class configRest {

    public String xApikey0 = "E52609EA0BACA8B27AFAD0C826D8B5A4";
    public String base_url0 = "http://localhost:8089/node1/index.php";
    public String register_url0 = base_url0 + "/register/add";
    public String listNodes_url0 = base_url0 + "/ipnodes";
    public String restNodes_url0 = base_url0 + "/restnode";
    public String restNodes_url0__ = base_url0 + "/restnode/index_";

    //first api serve, node 1
    //public String base_url0 = "http://161.97.167.161/app";
    public String block_url_lastHash =  base_url0 + "/api/v13nr_preblock/detail";

    //  broadcast loop
    public String register_url = "/register/add";
    public String block_url_pre = "/preblock/add";
    public String transaction_url = "/blockstr/add";
    public String meter_url = "/api/v13nr_station/add";

}

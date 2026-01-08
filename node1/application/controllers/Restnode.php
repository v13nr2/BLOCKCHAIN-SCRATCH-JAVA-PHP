<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Restnode extends CI_Controller {

	/**
	 * Index Page for this controller.
	 *
	 * Maps to the following URL
	 * 		http://example.com/index.php/welcome
	 *	- or - 
	 * 		http://example.com/index.php/welcome/index
	 *	- or -
	 * Since this controller is set as the default controller in
	 * config/routes.php, it's displayed at http://example.com/
	 *
	 * So any other public methods not prefixed with an underscore will
	 * map to /index.php/welcome/<method_name>
	 * @see https://codeigniter.com/userguide3/general/urls.html
	 */
	
	public function index(){
	    	// Membuat array data
                $data = [
                    ["ip" => "http://localhost:8089/BLOCKCHAIN/JAVA/BLOCKCHAIN-SCRATCH-JAVA-PHP/node1/index.php", "hostname" => "host1"],
                    ["ip" => "https://blockchain.jogjaide.web.id/node2", "hostname" => "host2"]
                ];
                
                // Mengencode array menjadi JSON
                $jsonData = json_encode($data);
                
                // Menetapkan header untuk JSON
                header('Content-Type: application/json');
                
                // Menampilkan JSON
                echo $jsonData;
	}
	
	public function add()
	{
	    
	}
}

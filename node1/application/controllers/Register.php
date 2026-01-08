<?php
defined('BASEPATH') OR exit('No direct script access allowed');

class Register extends CI_Controller {

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
	 
	public function randomNumber($length) {
        $result = '';
    
        for($i = 0; $i < $length; $i++) {
            $result .= mt_rand(0, 9);
        }
    
        return $result;
    }
    
	public function add()
	{
// 	    $data = array(
// 	        'email' => $this->input->post('email'),
// 	        'password' => $this->input->post('password')
// 	        );
// 		$this->db->insert('register', $data);
		
// 		echo json_encode(array('success'=> true, 'email' => $this->input->post('email')));
		
		
		
		
	    
	    
		$SQL = "SELECT kata FROM kata ORDER BY RAND() LIMIT 26";
		$query = $this->db->query($SQL);
        $dta = $query->result_array();
        $pp = "";
		foreach($dta as $v){
		    $pp .= $v["kata"].",";
		}	
		//$this->is_allowed('api_v13nr_register_add', false);

		//$this->form_validation->set_rules('email', 'Email', 'trim|required|max_length[100]');
		//$this->form_validation->set_rules('password', 'Password', 'trim|required|max_length[200]');
		
		//if ($this->form_validation->run()) {
            $uuid = $this->randomNumber(5)."-".$this->randomNumber(5)."-".$this->randomNumber(5)."-".$this->randomNumber(5);
			$save_data = [
				'email' => $this->input->post('email'),
				'uuid'  => $this->input->post('uuid'),
				'passp' => $this->input->post('passphrase'),
				'privkey' => $this->input->post('priv'),
				'pubkey' => $this->input->post('pub'),
				'password' => MD5($this->input->post('password')),
			];
			
			$save_v13nr_register = $this->db->insert('register', $save_data);
			
			
			$SQL = "SELECT hash FROM preblock ORDER BY id DESC LIMIT 1";
    		$query = $this->db->query($SQL);
            $dta = $query->row_array();
            echo $dta["hash"];
            
            //echo json_encode(array('success'=> true, 'email' => $this->input->post('email')));
            
// 			if ($save_v13nr_register) {
			    
			    
			    
			                
			                
			                
//                 			$ver_code = sha1(strtotime("now"));
                
//                 			$data['verification_code'] = $ver_code;
                
//                 			//$this->aauth_db->where('email', $email);
//                 			//$this->aauth_db->update($this->config_vars['users'], $data);
                
//                 			$this->load->library('email');
//                 			$this->load->helper('url');
                
//                 			if(isset($this->config_vars['email_config']) && is_array($this->config_vars['email_config'])){
//                 				$this->email->initialize($this->config_vars['email_config']);
//                 			}
                
//                 			$this->email->from("admin@api.energon.id");
//                 			$this->email->set_mailtype('html');
//                 			$this->email->to($this->input->post('email'));
//                 			$this->email->subject("REGISTER DI ENERGON");
//                 			$this->email->message("Catat UUID Anda, Universal Unique ID Yang dipakai di seluruh Sistem ENERGON, yaitu = <br>".$uuid."<br> Silahkan klik Link Aktivasi Berikut " . site_url() . "web/verify/".$this->config_vars['reset_password_link'] . $ver_code );
//                 			$this->email->send();
                			
//                 			$datav = array(
//                 			    'verification_code' => $ver_code
//                 			    );
                			
// 			                $this->db->where('uuid', $uuid);
// 			                 $this->db->update('v13nr_register', $datav);
			                
			    
			    
			    
// 				$this->response([
// 					'status' 	=> true,
// 					'message' 	=> 'Your data has been successfully stored into the database'
// 				], API::HTTP_OK);

// 			} else {
// 				$this->response([
// 					'status' 	=> false,
// 					'message' 	=> cclang('data_not_change')
// 				], API::HTTP_NOT_ACCEPTABLE);
// 			}

// 		} else {
// 			$this->response([
// 				'status' 	=> false,
// 				'message' 	=> 'Validation Errors.',
// 				'errors' 	=> $this->form_validation->error_array()
// 			], API::HTTP_NOT_ACCEPTABLE);
// 		}
	}
	
	
	
}

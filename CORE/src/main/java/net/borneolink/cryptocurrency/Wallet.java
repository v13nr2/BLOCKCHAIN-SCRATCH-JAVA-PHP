package net.borneolink.cryptocurrency;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

import net.borneolink.core.blockchain.BlockChain;
import net.borneolink.rest.out.Passphrase;
import net.borneolink.rest.out.peerTask;
import com.google.gson.*;
import net.borneolink.rest.out.peerList;

public class Wallet {
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public Wallet() {
		KeyPair keyPair = CryptographyHelper.ellipticCurveCrypto();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();

	}
		
	public double calculateBalance() {
		
		double balance = 0;	
		
        for (Map.Entry<String, TransactionOutput> item: BlockChain.UTXOs.entrySet()){
        	TransactionOutput transactionOutput = item.getValue();
            if(transactionOutput.isMine(publicKey)) { 
            	balance += transactionOutput.getAmount() ; 
            }
        } 
        
		return balance;
	}
	
	public Transaction transferMoney(PublicKey receiver, double amount) {
		
		if(calculateBalance() < amount) {
			System.out.println("Invalid transactin because of not enough money...");
			return null;
		}
		
		//we store the inputs for the transaction in this array
		List<TransactionInput> inputs = new ArrayList<TransactionInput>();
		
		//let's find our unspent transactions (the blockchain stores all the UTXOs)
		for (Map.Entry<String, TransactionOutput> item: BlockChain.UTXOs.entrySet()) {
			
			TransactionOutput UTXO = item.getValue();
			
			if(UTXO.isMine(this.publicKey)) {
				inputs.add(new TransactionInput(UTXO.getId()));
			}
		}
		
		//let's create the new transaction
		Transaction newTransaction = new Transaction(publicKey, receiver , amount, inputs);
		//the sender signs the transaction
		newTransaction.generateSignature(privateKey);
		
		return newTransaction;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public String regisWallet(String email, String password){
		//do to PEER LIST
		peerList listpeer = new peerList();
		listpeer.doBroadcast(email, password);
		return email;
	}

	public String regisWalletUUID(String email, String password){
		//do to PEER LIST
		Passphrase PP = new Passphrase();
		UUID uuid = UUID.randomUUID();
		peerTask listPeer = new peerTask();


		HashMap<String, Object> studentHashmap
				= new HashMap<String, Object>();


		boolean aksi = listPeer.doBroadcast(email, password,PP.WordPass(),uuid.toString(), this.getPrivateKey().toString(), new CryptographyHelper().generateHash(this.getPublicKey().toString()));
		if(aksi){

			studentHashmap.put("uuid", uuid.toString());
			studentHashmap.put("publicKey", this.getPublicKey().toString());


			// convert map to JSON string
			String json = new Gson().toJson(studentHashmap);
			return json; //uuid.toString();
		} else {

			studentHashmap.put("uuid", "");
			studentHashmap.put("publicKey", "");


			// convert map to JSON string
			String json = new Gson().toJson(studentHashmap);

			return json; //uuid.toString();
		}
	}
}

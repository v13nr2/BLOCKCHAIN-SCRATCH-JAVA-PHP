package net.borneolink.core.app;

import java.net.InetSocketAddress;
import java.security.Security;

import net.borneolink.cryptocurrency.*;
import net.borneolink.cryptocurrency.*;
import net.borneolink.libs.EnDec;
import net.borneolink.rest.in.MyHttpHandler;
import net.borneolink.rest.out.LastHash;
import net.borneolink.rest.out.peerTask;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import net.borneolink.core.blockchain.Block;
import net.borneolink.core.blockchain.BlockChain;
import net.borneolink.rest.configPeer;
import net.borneolink.rest.in.ParameterFilter;


public class App {

	private static String postParameters;

	public static void main(String[] args) throws Exception {
		boolean hasil;
		//ini security provider :: Bouncy Castle: Lebih flexible dari JCE
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		//create wallets + blockchain + the single miner in the network
		Wallet userA = new Wallet(); //inisialisasi
		Wallet userB = new Wallet();
		Wallet lender = new Wallet();

		int nilaikirim = 250;

		BlockChain chain = new BlockChain(); // mempersiapkan block
		Miner miner = new Miner();	// mendefinisikasn penambang

		Transaction nalarTransaction = new Transaction(lender.getPublicKey(), userA.getPublicKey(), nilaikirim, null);
		nalarTransaction.generateSignature(lender.getPrivateKey());
		nalarTransaction.setTransactionId("0");
		nalarTransaction.outputs.add(new TransactionOutput(nalarTransaction.getReceiver(), nalarTransaction.getAmount(), nalarTransaction.getTransactionId()));
		BlockChain.UTXOs.put(nalarTransaction.outputs.get(0).getId(), nalarTransaction.outputs.get(0));

		//System.out.println("Constructing the genesis block...");
		System.out.println("Konstruksi  block dimulai...");

		LastHash lastHash = new LastHash();
		Block block1 = new Block(lastHash.GetLastHash());

		System.out.println("\nuserA's balance is: " + userA.calculateBalance());
		System.out.println("userB's balance is: " + userB.calculateBalance());
		System.out.println("\nuserA mencoba mengirimkan ("+nilaikirim+" coins) ke userB...");
		hasil = block1.addTransaction(userA.transferMoney(userB.getPublicKey(), nilaikirim));
		if(!hasil){
			return;
		}
		miner.mine(block1,chain);
		System.out.println("\nuserA's balance is: " + userA.calculateBalance());

		System.out.println("Public key userB: "+ new CryptographyHelper().generateHash(userB.getPublicKey().toString()));
		//v13nr

		System.out.println("userB's balance is: " + userB.calculateBalance());


		//broadcast
		peerTask listPeer = new peerTask();
		listPeer.doBroadcastBlock(block1.toString(), block1.getPreviousHash(), block1.getHash());
		listPeer.doBroadcastTransaction(block1.getHash().toString(),userA.getPublicKey().toString(),userB.getPublicKey().toString(), "Transfer", "120");

		System.out.println("Miner's reward: "+miner.getReward());

		configPeer Configpeer = new configPeer();
		HttpServer server = HttpServer.create(new InetSocketAddress(Configpeer.base_port), 0);

		//TES DECRYPT
		EnDec Dec = new EnDec();
		String token = Dec.decryptCBC("+oVxeXKUznlIvlPeYOYcfSLF9GRNgvUnIOQPULh+Hmiui0Ak2X/hPqhESlWZA8GM", "berhasildiregistrasi");
		System.out.println("token: "+token);

		HttpContext context = server.createContext(Configpeer.base_test, new MyHttpHandler());
		context.getFilters().add(new ParameterFilter());
		server.setExecutor(null);
		server.start();
		System.out.println("Server A started on port "+Configpeer.base_port);


	}

}

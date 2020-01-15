package com.andus.common.service.hyperledger;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HyperledgerService {
	
	Contract contractFabcar;
    
    public HyperledgerService() throws Exception {
    	
//    	// Load an existing wallet holding identities used to access the network.
//        Path walletDirectory = Paths.get("hyperledger/wallet");
//        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);
//
//        // Path to a common connection profile describing the network.
//        Path networkConfigFile = Paths.get("hyperledger/config/connection-org1.json");
//
//        // Configure the gateway connection used to access the network.
//        Gateway.Builder builder = Gateway.createBuilder()
//                .identity(wallet, "user1")
//                .networkConfig(networkConfigFile);
//        
//        // Create a gateway connection
//        Gateway gateway = builder.connect();
//        
//        // Obtain a smart contract deployed on the network.
//        Network network = gateway.getNetwork("mychannel");
//        
//        contractFabcar = network.getContract("fabcar");
    }

	public String createCar(String method, String param1) throws Exception {
		
		// Submit transactions that store state to the ledger.
        byte[] createCarResult = contractFabcar.submitTransaction("createCar", "CAR10", "VW", "Polo", "Grey", "Mary");
        
        return new String(createCarResult, StandardCharsets.UTF_8);
	}

	public String queryAllCars(String method, String param1) throws Exception {
		
        // Evaluate transactions that query state from the ledger.
        byte[] queryAllCarsResult = contractFabcar.evaluateTransaction("queryAllCars");
        
        return new String(queryAllCarsResult, StandardCharsets.UTF_8);
	}
}

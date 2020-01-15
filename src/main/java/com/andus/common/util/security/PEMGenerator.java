package com.andus.common.util.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class PEMGenerator {
	
	private static void generator() throws Exception {
		
		//secp256k1( 타원 곡선의 키 페어의 계수 중 하나를 나타내는 스팩)
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        
        //EC 타원 곡선에 대한 키페어제너레이터 생성
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        
        //키페어 제네레이터와 스팩을 이용하여 키 initialize
        g.initialize(ecSpec, new SecureRandom());
        
        KeyPair keypair = g.generateKeyPair();
        PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate();
        
        writePemFile(privateKey, "PRIVATE KEY", "private.pem");
		writePemFile(publicKey, "PUBLIC KEY", "public.pem");
	}
	
	private static void writePemFile(Key key, String description, String filename) throws FileNotFoundException, IOException {
		
		Pem pemFile = new Pem(key, description);
		pemFile.write("hyperledger/wallet/application/" + filename);
	}
	
	public static void main(String[] args) throws Exception {
		generator();
	}
}

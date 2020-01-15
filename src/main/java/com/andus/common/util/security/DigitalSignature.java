package com.andus.common.util.security;

import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

public class DigitalSignature {
	
	public static String getSignature(String plainText) throws Exception {
		
		//sah256과 ECDSA(타원곡선 암호화)를 이용한 서명 객체 인스턴스 얻음
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        
        PEMParser pemParser = new PEMParser(new FileReader(new File("hyperledger/wallet/application/private.pem")));
        PrivateKeyInfo pubParsed = (PrivateKeyInfo)pemParser.readObject();
        PrivateKey prikey = new JcaPEMKeyConverter().getPrivateKey(pubParsed);
        
        //키페어를 이용한 서명 객체 초기화
        ecdsaSign.initSign(prikey);
        
        //원문을 업데이트
        ecdsaSign.update(plainText.getBytes("UTF-8"));
        
        //signature(byte)를 sign()함수를 이용하여 받음
        byte[] signature = ecdsaSign.sign();
        
        //signature를 base64로 인코딩하여 저장
        String sig = Base64.getEncoder().encodeToString(signature);
        
        return sig;
	}
	
	public static boolean getVerify(String plainText, String signature) throws Exception {
		
		//sah256과 ECDSA(타원곡선 암호화)를 이용한 사명 객체 인스턴스 얻음
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");

        //키 스팩을 가져옴  X509EncodedKeySpec은 공개키의 스팩을 가져오기 위해 쓰임 
        PEMParser pemParser = new PEMParser(new FileReader(new File("hyperledger/wallet/application/public.pem")));
        SubjectPublicKeyInfo pubParsed = (SubjectPublicKeyInfo)pemParser.readObject();
        PublicKey pubkey = new JcaPEMKeyConverter().getPublicKey(pubParsed);
        
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pubkey.getEncoded());

        //타원 키패어를 가져옴
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        
        //공개키 스팩을 이용하여 공개키를 가져옴
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        //서명 객체에 공개키로 서명 초기화
        ecdsaVerify.initVerify(publicKey);
        
        //검증하려하는 데이터를 업데이트
        ecdsaVerify.update(plainText.getBytes("UTF-8"));
        
        //주어진 서명을 가지고 최종 검증
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(signature));

        return result;
	}
	
	public static void main(String[] args) throws Exception {
		
		Boolean verify = DigitalSignature.getVerify("Hello", DigitalSignature.getSignature("Hello"));
		
		System.out.println(verify);
	}
}

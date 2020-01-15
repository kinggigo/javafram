package com.andus.common.service.ethereum;

import java.io.IOException;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import com.andus.common.exception.BusinessException;

import org.web3j.protocol.parity.Parity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BlockChainService {
	
	@Value("${blockchain.keystore.password}")
	private String accountPW;
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	Web3j web3j;
	Parity parity;
	
	public BlockChainService(@Value("${blockchain.node.url}") String nodeUrl) {
		web3j = Web3j.build(new HttpService(nodeUrl));
		parity = Parity.build(new HttpService(nodeUrl));
	}

	public BigInteger getAccountBalance(String accountAddr) {
        try {
            if (StringUtils.isNotEmpty(accountAddr)) {
                EthGetBalance ethGetBalance = web3j.ethGetBalance(accountAddr, DefaultBlockParameterName.LATEST).sendAsync().get();

                return Convert.fromWei(String.valueOf(ethGetBalance.getBalance()), Convert.Unit.ETHER).toBigInteger();
            }
        } catch (Exception e) {
            log.warn("wallet balance warn! {}", e.getMessage());
        }
        return BigInteger.ZERO;
    }
	
	public String createAccount() {
        try {
            NewAccountIdentifier accountAuthKey = parity.personalNewAccount(accountPW).sendAsync().get();
            String address = accountAuthKey.getAccountId();

            StringBuilder errorMessage = new StringBuilder();

            if (accountAuthKey.hasError()) {
                Response.Error error = accountAuthKey.getError();
                errorMessage.append(String.valueOf(
                        String.format("account auth key generate error. code=%s, message=%s", error.getCode(), error.getMessage()))
                );
                log.error("{}",errorMessage);
            }

            if (StringUtils.isNotEmpty(errorMessage.toString())) {
                throw new BusinessException("create account error. " + errorMessage.toString());
            }
            return address;

        } catch (ExecutionException | InterruptedException e) {
            log.error("{}", e);
            throw new RuntimeException("create account error. " + e.getMessage());
        }
    }
	
	public String transfer(String accountAddr, String receiver, BigInteger amount) throws ExecutionException, InterruptedException, IOException, CipherException {

        log.info("request transfer amount={}, receiver={}, accountAddr={}", amount, receiver, accountAddr);

        String transactionHash = null;

        if (this.unlockAccount(accountAddr)) {
            String uuid = UUID.randomUUID().toString();
            byte[] bArr = uuid.getBytes("UTF-8");
            String data = bytesToHex(bArr);

            Transaction transaction = new Transaction(accountAddr, null, null, null, receiver, Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger(), data);

            long startTime = System.currentTimeMillis();
            EthSendTransaction transactionRes = parity.ethSendTransaction(transaction).sendAsync().get();
            log.info("[WEB3J] transfer       time={}ms. accountAddr={}, receiver={}, amount={}", String.format("%5d", System.currentTimeMillis() - startTime), accountAddr, receiver, amount);

            if (transactionRes == null) {
            	log.error("transaction response is null");
                throw new RuntimeException("Transaction response null.");
            }

            if (transactionRes.hasError()) {
                Response.Error error = transactionRes.getError();
                String message = "Transaction has error. [" +error.getCode()+", "+ error.getMessage() + "] ";
                log.error("transaction response has error. {}", message);
                throw new RuntimeException(message);
            }

            transactionHash = transactionRes.getTransactionHash();
            log.info("Send transfer completed. {} ======== ({}) =======> {} hash : {}", accountAddr, amount, receiver, transactionHash);
        }
        return transactionHash;
    }
	
	private boolean unlockAccount(String accountAddr) {
        try {        	
            long startTime = System.currentTimeMillis();
            PersonalUnlockAccount personalUnlockAccount = parity.personalUnlockAccount(accountAddr, accountPW).sendAsync().get();
            log.info("[WEB3J] unlock         time={}ms. accountAddr={}", String.format("%5d", System.currentTimeMillis() - startTime), accountAddr);

            if (personalUnlockAccount == null || personalUnlockAccount.hasError()) {
                Response.Error error = personalUnlockAccount.getError();
                log.error("unlock account has error. code={}, message={}", error.getCode(), error.getMessage());
                throw new RuntimeException("unlock account error. code=" + error.getCode() + ", message=" + error.getMessage());
            }

        } catch (RuntimeException e) {
        	log.error("{}", e);
            throw e;

        } catch (Exception var5) {
        	log.info("<XXE_244>Error: account unlocked has error : " + var5.toString());
        }
        return true;
    }
	
	public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return String.valueOf(hexChars);
    }
	
	public String getEthTransaction(String hash) {
        String blockRaw = null;

        try {
            EthTransaction ethTransaction = web3j.ethGetTransactionByHash(hash).send();
            org.web3j.protocol.core.methods.response.Transaction result = ethTransaction.getResult();

            if (result == null) {
                log.warn("Transaction result is null. {}", hash);
                return null;
            }

            if (ethTransaction.hasError()) {
                Response.Error error = ethTransaction.getError();
                log.error("ethTransaction error. code={}, message={}, data={}", error.getCode(), error.getMessage(), error.getData());
                return null;
            }

            blockRaw = result.getBlockNumberRaw();
            log.info("block raw value={}", blockRaw);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return blockRaw;
    }
}
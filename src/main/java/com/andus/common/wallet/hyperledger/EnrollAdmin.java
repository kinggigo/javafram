package com.andus.common.wallet.hyperledger;

import java.nio.file.Paths;
import java.util.Properties;

import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;

public class EnrollAdmin {

        public static void main(String[] args) throws Exception {

                // Create a CA client for interacting with the CA.
                Properties props = new Properties();
                props.put("pemFile",
                        "hyperledger/config/ca.org1.example.com-cert.pem");
                props.put("allowAllHostNames", "true");
                HFCAClient caClient = HFCAClient.createNewInstance("https://10.0.1.10:7054", props);
                CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
                caClient.setCryptoSuite(cryptoSuite);

                // Create a wallet for managing identities
                Wallet wallet = Wallet.createFileSystemWallet(Paths.get("hyperledger/wallet"));

                // Check to see if we've already enrolled the admin user.
                boolean adminExists = wallet.exists("admin");
        if (adminExists) {
            System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("10.0.1.10");
        enrollmentRequestTLS.setProfile("tls");
        Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
        Identity user = Identity.createIdentity("Org1MSP", enrollment.getCert(), enrollment.getKey());
        wallet.put("admin", user);
                System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
        }
}
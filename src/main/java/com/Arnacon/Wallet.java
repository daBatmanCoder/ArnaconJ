package com.Arnacon;

import java.security.SecureRandom;

import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.utils.Numeric;

public class Wallet {

    String mnemonic;
    String privateKey;

    private static final int[] DERIVATION_PATH = {44 | Bip32ECKeyPair.HARDENED_BIT, 60 | Bip32ECKeyPair.HARDENED_BIT, 0 | Bip32ECKeyPair.HARDENED_BIT, 0, 0};

    // Constructor with no private key
    public Wallet() {
        this.mnemonic = generateMnemonic();
        this.privateKey = getPrivateKeyFromMnemonic(this.mnemonic);
    }

    // Constructor with private key - for wallet import
    public Wallet(String privateKey){
        this.mnemonic = generateMnemonic(); // That's not right... 
        this.privateKey = privateKey;
    }

    // Returns the mnemonic 
    public String getPrivateKey() {
        return this.privateKey;
    }

    // Generates a private key  
    static String generatePrivateKey(){
        return getPrivateKeyFromMnemonic(generateMnemonic());
    }

    // Checks if a mnemonic is valid
    static boolean isValidMnemonic(String mnemonic) {
        return MnemonicUtils.validateMnemonic(mnemonic);
    }

    // Generates a mnemonic
    static String generateMnemonic() {
        byte[] entropy = new byte[16]; // 128 bits
        new SecureRandom().nextBytes(entropy);
        return MnemonicUtils.generateMnemonic(entropy);
    }

    // Returns the private key from the mnemonic
    static String getPrivateKeyFromMnemonic(String mnemonic) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, DERIVATION_PATH);
        return Numeric.toHexStringNoPrefix(derivedKeyPair.getPrivateKey());
    }

    // Returns the public key from the private key
    public String getPublicKey() {
        Credentials credentials = Credentials.create(this.privateKey);
        return credentials.getAddress();
    }

    // Returns the wallet credentials
    public Credentials getCredentials() {
        return Credentials.create(this.privateKey);
    }
}

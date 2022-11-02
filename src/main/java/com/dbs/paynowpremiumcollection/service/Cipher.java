package com.dbs.paynowpremiumcollection.service;

import java.io.IOException;
import java.security.NoSuchProviderException;

import org.bouncycastle.openpgp.PGPException;

public interface Cipher {

    String decryptData(String encryptedData) throws IOException, PGPException, IllegalArgumentException;

    String enryptData(String data) throws IOException, PGPException, IllegalArgumentException, NoSuchProviderException;

    String signatureGenerate(String queryParams) throws Exception;
}

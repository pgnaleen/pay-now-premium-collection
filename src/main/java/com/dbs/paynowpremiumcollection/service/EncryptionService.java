package com.dbs.paynowpremiumcollection.service;

import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;
import java.security.NoSuchProviderException;

public interface EncryptionService {
    void encryptFile() throws PGPException, IOException, NoSuchProviderException;
}

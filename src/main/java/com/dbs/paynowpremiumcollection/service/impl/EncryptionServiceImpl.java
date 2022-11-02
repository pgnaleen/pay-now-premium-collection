package com.dbs.paynowpremiumcollection.service.impl;

import com.dbs.paynowpremiumcollection.service.EncryptionService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;

@Service
public class EncryptionServiceImpl implements EncryptionService {
    @Override
    public void encryptFile() throws PGPException, IOException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        PGPFileEncryptionServiceImpl.encryptFile("output_encrypted_file.asc",
                "src/main/resources/Capture.PNG",
                "src/main/resources/0xD89B5951-pub.asc", true, false);
    }
}

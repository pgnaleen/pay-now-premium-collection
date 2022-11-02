package com.dbs.paynowpremiumcollection.service.impl;

import com.dbs.paynowpremiumcollection.service.EncryptionService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    @Value("${dbs.key.pub}")
    private String publicKey;

    @Value("${sl.key.pri}")
    private String privateKey;

    @Value("${dbs.key.pwd}")
    private String passPhase;

    @Override
    public void encryptFile() throws PGPException, IOException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        PGPFileEncryptionServiceImpl.encryptFile("output_encrypted_file.asc",
                "src/main/resources/Capture.PNG",
                "src/main/resources/0xD89B5951-pub.asc", true, false);

        PGPFileEncryptionServiceImpl.decryptFile("output_encrypted_file.asc",
                "src/main/resources/0xD89B5951-sec.asc",
                passPhase.toCharArray(),
                new File("no_need_name_as_encrypted_file_has_name").getName());
    }
}

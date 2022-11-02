package com.dbs.paynowpremiumcollection.controller;

import com.dbs.paynowpremiumcollection.service.impl.EncryptionServiceImpl;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchProviderException;

@RestController
@RequestMapping("api/v1/sg/encrypt-file")
public class EncryptionController {
    private final EncryptionServiceImpl encryptionService;

    public EncryptionController(EncryptionServiceImpl encryptionService) {
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String getEncrypted() throws PGPException, IOException, NoSuchProviderException {
        encryptionService.encryptFile();
        return new String("{\"response\":\"200\"}");
    }
}

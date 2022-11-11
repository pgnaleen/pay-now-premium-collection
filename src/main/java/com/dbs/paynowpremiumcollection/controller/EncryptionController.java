package com.dbs.paynowpremiumcollection.controller;

import com.dbs.paynowpremiumcollection.dto.request.EncryptionRequestDto;
import com.dbs.paynowpremiumcollection.service.EncryptionService;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchProviderException;

@RestController
@RequestMapping("api/v1/sg")
public class EncryptionController {
    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping(value = "/encrypt/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String getEncryptedFile(@Valid @ModelAttribute EncryptionRequestDto requestDto)
            throws PGPException, IOException, NoSuchProviderException {
        encryptionService.encryptFile(requestDto);
        return "{\"response\":\"200\"}";
    }

    @PostMapping(value = "/encrypt")
    public String getEncrypted(@Valid @RequestBody String requestDto)
            throws PGPException, IOException, NoSuchProviderException {
        return encryptionService.encrypt(requestDto);
    }

    @PostMapping(value = "/decrypt/file")
    public String getDecryptedFile() throws PGPException, IOException, NoSuchProviderException {
        encryptionService.decryptFile();
        return "{\"response\":\"200\"}";
    }

    @PostMapping(value = "/decrypt")
    public String getDecrypted(@Valid @RequestBody String requestDto)
            throws PGPException, IOException, NoSuchProviderException {
        return encryptionService.decrypt(requestDto);
    }
}

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
@RequestMapping("api/v1/sg/encrypt")
public class EncryptionController {
    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String getEncryptedFile(@Valid @ModelAttribute EncryptionRequestDto requestDto)
            throws PGPException, IOException, NoSuchProviderException {
        encryptionService.encryptFile(requestDto);
        return "{\"response\":\"200\"}";
    }

    @PostMapping(value = "")
    public String getEncrypted(@Valid @RequestBody String requestDto)
            throws PGPException, IOException, NoSuchProviderException {
        return encryptionService.encrypt(requestDto);
    }

    @GetMapping(value = "/file")
    public String getDecryptedFile() throws PGPException, IOException, NoSuchProviderException {
        encryptionService.decryptFile();
        return "{\"response\":\"200\"}";
    }

    @GetMapping(value = "")
    public String getDecrypted(@Valid @RequestBody String requestDto)
            throws PGPException, IOException, NoSuchProviderException {
        return encryptionService.decrypt(requestDto);
    }

}

package com.dbs.paynowpremiumcollection.controller;

import com.dbs.paynowpremiumcollection.dto.request.EncryptionRequestDto;
import org.bouncycastle.openpgp.PGPException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.dbs.paynowpremiumcollection.service.EncryptionService;

import java.io.IOException;
import java.security.NoSuchProviderException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EncryptionControllerUT {
    @Mock
    EncryptionService encryptionService;

    @Test
    public void getEncrypted_methodTest() throws PGPException, IOException, NoSuchProviderException {
//        when(encryptionService.encryptFile());
        EncryptionController controller = new EncryptionController(encryptionService);
        EncryptionRequestDto requestDto = new EncryptionRequestDto();
        String response = controller.getEncrypted(requestDto);

        assertNotNull(response);
        assertEquals("{\"response\":\"200\"}", response);
    }

    @Test
    public void getDecrypted_methodTest() throws PGPException, IOException, NoSuchProviderException {
        EncryptionController controller = new EncryptionController(encryptionService);
        EncryptionRequestDto requestDto = new EncryptionRequestDto();
        String response = controller.getEncrypted(requestDto);

        assertNotNull(response);
        assertEquals("{\"response\":\"200\"}", response);
    }
}

package com.dbs.paynowpremiumcollection.service;

import com.dbs.paynowpremiumcollection.dto.request.EncryptionRequestDto;
import com.dbs.paynowpremiumcollection.repository.EncryptionInfoRepository;
import com.dbs.paynowpremiumcollection.service.impl.EncryptionServiceImpl;
import org.bouncycastle.openpgp.PGPException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.NoSuchProviderException;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EncryptionServiceImplUT {

    @Mock
    EncryptionInfoRepository encryptionInfoRepository;

    @Mock
    Cipher cipher;

    @Test
    public void encryptFile_methodTest() throws PGPException, IOException, NoSuchProviderException {
        EncryptionServiceImpl encryptionService = new EncryptionServiceImpl(encryptionInfoRepository, cipher);
        EncryptionRequestDto requestDto = new EncryptionRequestDto();

        encryptionService.encryptFile(requestDto);
    }
}

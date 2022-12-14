package com.dbs.paynowpremiumcollection.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Base64;
import java.util.Iterator;

import com.dbs.paynowpremiumcollection.service.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.CharStreams;
import com.dbs.paynowpremiumcollection.pgp.BCPGPEncryptor;
import com.dbs.paynowpremiumcollection.pgp.Encrypt;
import com.dbs.paynowpremiumcollection.pgp.Signature;

import static org.bouncycastle.openpgp.PGPUtil.getDecoderStream;

@Service
public class CipherImpl implements Cipher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CipherImpl.class);

    private String pubKey;
    private String priKey;

    @Value("${dbs.key.pwd}")
    private String pwd;

    @Override
    public String decryptData(final String encryptedData) throws IOException, PGPException {
        return decrypt(new ByteArrayInputStream(encryptedData.getBytes()));
    }


    @Value("${dbs.key.pub}")
    private void setPubKey(String pubKey) {
        this.pubKey = new String(Base64.getDecoder().decode(pubKey));
    }

    @Value("${sl.key.pri}")
    private void setPriKey(String priKey) {
        this.priKey = new String(Base64.getDecoder().decode(priKey));
    }

    private String decrypt(
            final InputStream in
    ) throws IOException, PGPException, IllegalArgumentException {
        Security.addProvider(new BouncyCastleProvider());

        final InputStream ins = getDecoderStream(in);

        final PGPObjectFactory pgpF = new PGPObjectFactory(ins, new BcKeyFingerprintCalculator());
        final PGPEncryptedDataList enc;

        final Object pgpFObj = pgpF.nextObject();
        //
        // the first object might be a PGP marker packet.
        //
        if (pgpFObj instanceof PGPEncryptedDataList) {
            enc = (PGPEncryptedDataList) pgpFObj;
        } else {
            enc = (PGPEncryptedDataList) pgpF.nextObject();
        }

        if (enc == null) {
            throw new IllegalArgumentException("The message is not valid");
        }

        //
        // find the secret key
        //
        final Iterator it = enc.getEncryptedDataObjects();
        PGPPrivateKey sKey = null;
        PGPPublicKeyEncryptedData pbe = null;

        while (sKey == null && it.hasNext()) {
            pbe = (PGPPublicKeyEncryptedData) it.next();
            sKey = findSecretKey(pbe.getKeyID());
        }

        if (sKey == null) {
            throw new IllegalArgumentException("Secret key for message not found.");
        }

        final InputStream clear = pbe.getDataStream(
                new JcePublicKeyDataDecryptorFactoryBuilder()
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                        .build(sKey)
        );

        PGPObjectFactory plainFact = new PGPObjectFactory(clear, new BcKeyFingerprintCalculator());
        Object message = plainFact.nextObject();

        while (message != null) {
            if (message instanceof PGPCompressedData) {
                PGPCompressedData cData = (PGPCompressedData) message;
                plainFact = new PGPObjectFactory(
                        cData.getDataStream(),
                        new BcKeyFingerprintCalculator()
                );

                message = plainFact.nextObject();
            }

            if (message instanceof PGPLiteralData) {
                PGPLiteralData ld = (PGPLiteralData) message;

                InputStream unc = ld.getInputStream();
                String result = null;
                try (Reader reader = new InputStreamReader(unc)) {
                    result = CharStreams.toString(reader);
                }

                return result;
            }

            message = plainFact.nextObject();
        }

        throw new PGPException("Message is not a simple encrypted file - type unknown.");
    }

    private PGPPrivateKey findSecretKey(final long keyID) throws IOException, PGPException {
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                getDecoderStream(
                        new ByteArrayInputStream(priKey.getBytes())
                ),
                new BcKeyFingerprintCalculator()
        );

        PGPSecretKey pgpSecKey = pgpSec.getSecretKey(keyID);

        if (pgpSecKey == null) {
            return null;
        }

        return pgpSecKey.extractPrivateKey(
                new JcePBESecretKeyDecryptorBuilder()
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                        .build(pwd.toCharArray())
        );
    }

    @Override
    public String encryptData(
            final String data
    ) throws IOException, PGPException, IllegalArgumentException, NoSuchProviderException {
        return encrypt(data);
    }

    private String encrypt(String data) throws PGPException, IOException, NoSuchProviderException {
        Encrypt encrypt = new Encrypt();
        encrypt.setArmored(true);
        encrypt.setCheckIntegrity(true);
        encrypt.setPublicKeyFilePath(pubKey);
        encrypt.setSigning(true);
        encrypt.setPrivateKeyFilePath(priKey);
        encrypt.setPrivateKeyPassword(pwd.toCharArray());
        BCPGPEncryptor bcpgpEncryptor = new BCPGPEncryptor(encrypt);

        return bcpgpEncryptor.encryptMessage(data);

    }

    @Override
    public String signatureGenerate(String queryParams)
            throws Exception {
        Encrypt encrypt = new Encrypt();
        encrypt.setArmored(true);
        encrypt.setCheckIntegrity(true);
        encrypt.setPublicKeyFilePath(pubKey);
        encrypt.setSigning(true);
        encrypt.setPrivateKeyFilePath(priKey);
        encrypt.setPrivateKeyPassword(pwd.toCharArray());

        Signature signature = new Signature(encrypt);
        return signature.signatureGenerate(queryParams, priKey);

    }

}

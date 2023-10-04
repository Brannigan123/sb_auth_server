package com.bran.service.auth.config;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Component
@RequiredArgsConstructor
public class AttributeEncryptor implements AttributeConverter<String, String> {

    @Value("${attribute-encryptor.algorithm}")
    private String algorithm;
    @Value("${attribute-encryptor.secret}")
    private String secret;

    private final AtomicReference<Key> keyReference = new AtomicReference<>();
    private final AtomicReference<Cipher> cipherReference = new AtomicReference<>();

    /**
     * Retrieves the key used for encryption and decryption.
     *
     * @return The key used for encryption and decryption.
     */
    private Key getKey() {
        var key = keyReference.get();
        if (key == null) {
            key = new SecretKeySpec(secret.getBytes(), algorithm);
            keyReference.set(key);
        }
        return key;
    }

    /**
     * Retrieves the cipher to be used for encryption and decryption.
     *
     * @return The cipher object to be used.
     * @throws NoSuchAlgorithmException If the specified algorithm is not available.
     * @throws NoSuchPaddingException   If the specified padding mechanism is not
     *                                  available.
     */
    private Cipher getCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        var cipher = cipherReference.get();
        if (cipher == null) {
            cipher = Cipher.getInstance(algorithm);
            cipherReference.set(cipher);
        }
        return cipher;
    }

    /**
     * Converts the given attribute to a database column value.
     *
     * @param attribute the attribute to be converted
     * @return the converted database column value
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null)
            return null;
        try {
            val key = getKey();
            val cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Converts the given database data to the corresponding entity attribute.
     *
     * @param dbData the database data to be converted
     * @return the converted entity attribute as a string
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            val key = getKey();
            val cipher = getCipher();
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
    }
}

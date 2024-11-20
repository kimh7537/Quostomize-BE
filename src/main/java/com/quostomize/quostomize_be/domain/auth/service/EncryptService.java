package com.quostomize.quostomize_be.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EncryptService {

    private final AesBytesEncryptor aesBytesEncryptor;

    public String encryptPhoneNumber(final String rawPhoneNumber) {
        byte[] encrypt = aesBytesEncryptor.encrypt(rawPhoneNumber.getBytes(StandardCharsets.UTF_8));
        return byteArrayToString(encrypt);
    }

    public String decryptPhoneNumber(final String encryptedPhoneNumber) {
        byte[] bytes = stringToByteArray(encryptedPhoneNumber);
        byte[] decrypt = aesBytesEncryptor.decrypt(bytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public String encryptSecondaryAuthCode(final String rawSecondaryAuthCode) {
        byte[] encrypt = aesBytesEncryptor.encrypt(rawSecondaryAuthCode.getBytes(StandardCharsets.UTF_8));
        return byteArrayToString(encrypt);
    }

    public String decryptSecondaryAuthCode(final String encryptedSecondaryAuthCode) {
        byte[] bytes = stringToByteArray(encryptedSecondaryAuthCode);
        byte[] decrypt = aesBytesEncryptor.decrypt(bytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public String encryptResidenceNumber(final String rawResidenceNumber) {
        byte[] encrypt = aesBytesEncryptor.encrypt(rawResidenceNumber.getBytes(StandardCharsets.UTF_8));
        return byteArrayToString(encrypt);
    }

    public String decryptResidenceNumber(final String encryptedResidenceNumber) {
        byte[] bytes = stringToByteArray(encryptedResidenceNumber);
        byte[] decrypt = aesBytesEncryptor.decrypt(bytes);
        return new String(decrypt, StandardCharsets.UTF_8);
    }

    public String byteArrayToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public byte[] stringToByteArray(String byteString) {
        return Base64.getDecoder().decode(byteString);
    }
}

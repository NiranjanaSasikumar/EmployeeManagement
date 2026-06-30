package com.example.EmployeManagement.EmployeeHome;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class EncryptionUtil {

    private static final String SECRET_KEY =
            "1234567890123456";

    public String encrypt(String value) {

        log.info("Encrypting data");

        try {

            if (value == null) {
                return null;
            }

            SecretKeySpec key =
                    new SecretKeySpec(
                            SECRET_KEY.getBytes(
                                    StandardCharsets.UTF_8),
                            "AES");

            Cipher cipher =
                    Cipher.getInstance("AES");

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    key);

            byte[] encrypted =
                    cipher.doFinal(
                            value.getBytes(
                                    StandardCharsets.UTF_8));

            return Base64
                    .getEncoder()
                    .encodeToString(encrypted);

        } catch (Exception e) {

            log.error(
                    "Error occurred while encrypting data",
                    e);

            throw new RuntimeException(
                    "Error while encrypting data",
                    e);
        }
    }

    public String decrypt(String value) {

        log.info("Decrypting data");

        try {

            if (value == null) {
                return null;
            }

            SecretKeySpec key =
                    new SecretKeySpec(
                            SECRET_KEY.getBytes(
                                    StandardCharsets.UTF_8),
                            "AES");

            Cipher cipher =
                    Cipher.getInstance("AES");

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    key);

            byte[] decrypted =
                    cipher.doFinal(
                            Base64.getDecoder()
                                    .decode(value));

            return new String(
                    decrypted,
                    StandardCharsets.UTF_8);

        } catch (Exception e) {

            log.error(
                    "Error occurred while decrypting data",
                    e);

            throw new RuntimeException(
                    "Error while decrypting data",
                    e);
        }
    }
}

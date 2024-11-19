package com.exemplo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "senha123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Senha criptografada: " + encodedPassword);
    }
}

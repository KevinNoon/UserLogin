package com.optimised.backup.data.service;

import jakarta.mail.MessagingException;

import java.io.IOException;

/**
 * Created by Olga on 8/22/2016.
 */
public interface EmailService {
    void sendSimpleMessage(String to,
                           String subject,
                           String text);
    void sendMessageWithAttachment(String to,
                                   String subject,
                                   String text,
                                   String pathToAttachment)
            throws IOException, MessagingException;

}
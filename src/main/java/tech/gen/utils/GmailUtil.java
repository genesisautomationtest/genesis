package tech.gen.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class GmailUtil {

    private static final GmailUtil INSTANCE = new GmailUtil();

    private final String APPLICATION_NAME = "Gmail API";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final String TOKENS_DIRECTORY_PATH = "src/main/resources/tokens";
    private final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private final String USER = "me";
    private Gmail service;

    public static GmailUtil getInstance() {
        return INSTANCE;
    }

    private GmailUtil() {
        service = serviceBuild();
    }

    private Gmail serviceBuild() {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            InputStream in = GmailUtil.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException | GeneralSecurityException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private String getMessageId(String email) {
        String query = String.format("to:%s is:unread", email);
        List<Message> messages = new ArrayList<>();
        try {
            ListMessagesResponse response = service.users().messages().list(USER).setQ(query).execute();
            while (response.getMessages() != null) {
                messages.addAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(USER).setQ(query)
                            .setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return (String) messages.stream().map((Function<Message, Object>) Message::getId).findAny().orElse("");
    }

    public String getMessageBody(String email) {
        String messageBody = "";

        String messageId = "";

        for (int i = 0; i < 10; i++) {
            if (messageId.isEmpty()) messageId = getMessageId(email);
            else break;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (messageId.isEmpty()) throw new IllegalArgumentException("Message id is empty");

        try {
            Message message = service.users().messages().get(USER, messageId).execute();
            MessagePart part = message.getPayload();
            messageBody = StringUtils.newStringUtf8(Base64.decodeBase64(part.getBody().getData()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return messageBody;
    }
}

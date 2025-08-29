// src/main/java/com/example/bfj/BfjJavaQualifierApplication.java
package com.example.bfj;

import com.example.bfj.dto.GenerateWebhookRequest;
import com.example.bfj.dto.GenerateWebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BfjJavaQualifierApplication {
    private static final Logger log = LoggerFactory.getLogger(BfjJavaQualifierApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BfjJavaQualifierApplication.class, args);
    }

    @Bean
    ApplicationRunner run(HiringClient client, AppProperties props) {
        return args -> {
            log.info("App started. Preparing request for {}", props.getRegNo());

            // 1) Generate webhook & token
            GenerateWebhookRequest req =
                    new GenerateWebhookRequest(props.getName(), props.getRegNo(), props.getEmail());
            GenerateWebhookResponse gw = client.generateWebhook(req);

            if (gw == null || gw.getAccessToken() == null) {
                throw new IllegalStateException("Could not obtain accessToken/webhook from generateWebhook");
            }
            log.info("Got webhook: {}, token: {}", gw.getWebhook(), mask(gw.getAccessToken()));

            // 2) Because your regNo's last two digits are EVEN, use Question 2’s SQL. :contentReference[oaicite:5]{index=5}
            String finalSql = client.pickFinalSqlForQuestion2();

            // 3) Store the result (locally) and 4) Submit to webhook
            client.persistSqlToFile(finalSql);
            client.submitSql(gw.getWebhook(), gw.getAccessToken(), finalSql);

            log.info("Done.");
        };
    }

    private static String mask(String token) {
        if (token == null || token.length() < 8) return "****";
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }
}

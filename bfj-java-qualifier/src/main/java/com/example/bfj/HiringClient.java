// src/main/java/com/example/bfj/HiringClient.java
package com.example.bfj;

import com.example.bfj.dto.GenerateWebhookRequest;
import com.example.bfj.dto.GenerateWebhookResponse;
import com.example.bfj.dto.SubmitRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class HiringClient {
    private static final Logger log = LoggerFactory.getLogger(HiringClient.class);
    private static final String GENERATE_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private static final String TEST_WEBHOOK_FALLBACK =
            "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    private final WebClient webClient;

    public HiringClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public GenerateWebhookResponse generateWebhook(GenerateWebhookRequest body) {
        log.info("Calling generateWebhook ...");
        return webClient.post()
                .uri(GENERATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GenerateWebhookResponse.class)
                .block();
    }

    public String pickFinalSqlForQuestion2() {
        // Q2 from "SQL Qwestion 2 JAVA": count younger employees in same department, order by EMP_ID desc
        // Younger => later DOB. Join to department to fetch DEPARTMENT_NAME. :contentReference[oaicite:3]{index=3}
        return """
               SELECT
                   e.EMP_ID,
                   e.FIRST_NAME,
                   e.LAST_NAME,
                   d.DEPARTMENT_NAME,
                   (
                       SELECT COUNT(*) 
                       FROM EMPLOYEE e2
                       WHERE e2.DEPARTMENT = e.DEPARTMENT
                         AND e2.DOB > e.DOB
                   ) AS YOUNGER_EMPLOYEES_COUNT
               FROM EMPLOYEE e
               JOIN DEPARTMENT d
                 ON d.DEPARTMENT_ID = e.DEPARTMENT
               ORDER BY e.EMP_ID DESC;
               """;
    }

    public void persistSqlToFile(String sql) {
        try {
            Path out = Path.of("final-sql-question2.sql");
            Files.writeString(out, sql);
            log.info("Wrote SQL to {}", out.toAbsolutePath());
        } catch (Exception ex) {
            log.warn("Could not write SQL file: {}", ex.getMessage());
        }
    }

    public void submitSql(String webhook, String accessToken, String finalSql) {
        String target = (webhook != null && !webhook.isBlank()) ? webhook : TEST_WEBHOOK_FALLBACK;
        log.info("Submitting SQL to {}", target);

        SubmitRequest body = new SubmitRequest(finalSql);

        // Per the paper, send JWT as the Authorization header *as-is* (no 'Bearer ' prefix). :contentReference[oaicite:4]{index=4}
        String response = webClient.post()
                .uri(target)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(err -> {
                    log.error("Submit failed: {}", err.toString());
                    return Mono.just("ERROR: " + err.getMessage());
                })
                .block();

        log.info("Submission response: {}", response);
    }
}

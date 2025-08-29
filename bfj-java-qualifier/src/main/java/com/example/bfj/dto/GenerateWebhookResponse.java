// src/main/java/com/example/bfj/dto/GenerateWebhookResponse.java
package com.example.bfj.dto;

public class GenerateWebhookResponse {
    private String webhook;
    private String accessToken;

    public String getWebhook() { return webhook; }
    public void setWebhook(String webhook) { this.webhook = webhook; }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
}
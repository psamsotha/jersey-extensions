
package com.underdog.jersey.extension.framework.model;

/**
 *
 * @author Paul Samsotha
 */
public class ClientDetails {
    
    private String clientId;
    private String clientPassHash;
    private String apiKey;
    
    public ClientDetails(String clientId, String clientPassHash) {
        this.clientId = clientId;
        this.clientPassHash = clientPassHash;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientPassHash() {
        return clientPassHash;
    }

    public void setClientPassHash(String clientPassHash) {
        this.clientPassHash = clientPassHash;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

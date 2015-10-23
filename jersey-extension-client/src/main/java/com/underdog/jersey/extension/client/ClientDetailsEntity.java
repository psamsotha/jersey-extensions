
package com.underdog.jersey.extension.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Paul Samsotha
 */
@Entity(name = "ClientDetails")
@Table(name = "CLIENT_DETAILS")
public class ClientDetailsEntity {
    
    @Id
    @Column(name = "CLIENT_ID")
    private String clientId;
    
    @Column(name = "CLIENT_PASS", nullable = false)
    private String password;
    
    @Column(name = "CLIENT_KEY", unique = true)
    private String apiKey;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

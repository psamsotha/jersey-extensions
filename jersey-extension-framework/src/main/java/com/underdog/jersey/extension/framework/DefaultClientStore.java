
package com.underdog.jersey.extension.framework;

import com.underdog.jersey.extension.framework.model.ClientDetails;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Paul Samsotha
 */
final class DefaultClientStore implements ClientStore {
    
    private final Map<String, ClientDetails> clientDb = new ConcurrentHashMap<>(); 
    
    @Override
    public void saveClient(ClientDetails clientDetails) {
        clientDb.put(clientDetails.getClientId(), clientDetails);
    }
    
    @Override
    public ClientDetails getClientByApiKey(String apiKey) {
        if (null == apiKey) return null;
        for (ClientDetails c: clientDb.values()) {
            if (apiKey.equals(c.getApiKey())) {
                return c;
            }
        }
        return null;
    }

    @Override
    public ClientDetails getClientById(String clientId) {
        return clientDb.get(clientId);
    }
}

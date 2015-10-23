package com.underdog.jersey.extension.framework;

import static com.underdog.jersey.extension.framework.TestConstants.CLIENT_ID;
import static com.underdog.jersey.extension.framework.TestConstants.CLIENT_PASS;
import com.underdog.jersey.extension.framework.model.ClientDetails;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Paul Samsotha
 */
public class TestClientStore implements ClientStore {

    private final Map<String, ClientDetails> clientDb = new ConcurrentHashMap<>();
    
    public TestClientStore() {
        clientDb.put(CLIENT_ID, new ClientDetails(CLIENT_ID, CLIENT_PASS));
    }

    @Override
    public void saveClient(ClientDetails clientDetails) {
        clientDb.put(clientDetails.getClientId(), clientDetails);
    }

    @Override
    public ClientDetails getClientByApiKey(String apiKey) {
        if (null == apiKey) {
            return null;
        }
        for (ClientDetails c : clientDb.values()) {
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

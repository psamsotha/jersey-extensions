
package com.underdog.jersey.extension.framework;

import com.underdog.jersey.extension.framework.model.ClientDetails;

/**
 *
 * @author Paul Samsotha
 */
public interface ClientStore {
     
    void saveClient(ClientDetails clientDetails);
    ClientDetails getClientById(String clientId);
    ClientDetails getClientByApiKey(String apiKey);
}

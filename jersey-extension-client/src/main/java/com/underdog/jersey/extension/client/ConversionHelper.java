
package com.underdog.jersey.extension.client;

import com.underdog.jersey.extension.framework.model.ClientDetails;

/**
 *
 * @author Paul Samsotha
 */
public class ConversionHelper {
    
    public static ClientDetailsEntity fromClientDetails(ClientDetails clientDetails) {
        ClientDetailsEntity entity = new ClientDetailsEntity();
        entity.setClientId(clientDetails.getClientId());
        entity.setPassword(clientDetails.getClientPassHash());
        entity.setApiKey(clientDetails.getApiKey());
        return entity;
    }
    
    public static ClientDetails fromClientDetailsEntity(ClientDetailsEntity entity) {
        ClientDetails details = new ClientDetails(entity.getClientId(), entity.getPassword());
        details.setApiKey(entity.getApiKey());
        return details;
    }
}

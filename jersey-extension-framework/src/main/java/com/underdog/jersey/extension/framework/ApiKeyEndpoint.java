
package com.underdog.jersey.extension.framework;

import com.underdog.jersey.extension.framework.model.ClientDetails;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Paul Samsotha
 */
@Path(ApiKeyFeature.DEFAULT_URI)
@Produces(MediaType.APPLICATION_JSON)
public class ApiKeyEndpoint {
    
    @Inject
    private KeyGenerator keyGenerator;
    
    @Inject
    private ClientStore clientStore;
    
    static class ApiKey {
        public String apiKey;
        public ApiKey(){}
        public ApiKey(String apiKey) {
            this.apiKey = apiKey;
        }
    }
    
    public static class ClientPostInfo {
        @FormParam("clientId")
        public String clientId;
        @FormParam("clientPass")
        public String clientPass;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getApiKeyForm(@BeanParam ClientPostInfo clientInfo) {
        return getApiKey(clientInfo);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getApiKeyJson(ClientPostInfo clientInfo) {
        return getApiKey(clientInfo);
    }
    
    private Response getApiKey(ClientPostInfo clientInfo) {
        ClientDetails client = clientStore.getClientById(clientInfo.clientId);
        if (client == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        String hash = noOpHash(clientInfo.clientPass);
        if (!client.getClientPassHash().equals(hash)) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        String apiKey = keyGenerator.generateKey(client.getClientId());
        client.setApiKey(apiKey);
        clientStore.saveClient(client);
        return Response.ok(new ApiKey(apiKey)).build();
    }
    
    public String noOpHash(String password) {
        return password;
    }
}

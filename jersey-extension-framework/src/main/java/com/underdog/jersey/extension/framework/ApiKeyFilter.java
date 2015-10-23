
package com.underdog.jersey.extension.framework;

import com.underdog.jersey.extension.framework.model.ClientDetails;
import java.io.IOException;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

/**
 *
 * @author Paul Samsotha
 */
final class ApiKeyFilter implements ContainerRequestFilter {
    
    private final String authEndpoint;
    private final String apiKeyHeader;
    
    @Inject
    private ClientStore clientStore;
    
    public ApiKeyFilter(String authEndpoint, String apiKeyHeader) {
        this.authEndpoint = authEndpoint;
        this.apiKeyHeader = apiKeyHeader;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String requestUri = requestContext.getUriInfo().getRequestUri().toString();
        if (requestUri.endsWith(authEndpoint)) {
            return;
        }
        
        String keyValue = requestContext.getHeaderString(apiKeyHeader);
        if (keyValue == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        
        ClientDetails client = clientStore.getClientByApiKey(keyValue);
        if (client == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}

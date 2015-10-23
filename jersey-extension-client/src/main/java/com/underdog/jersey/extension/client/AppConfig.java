
package com.underdog.jersey.extension.client;

import com.underdog.jersey.extension.framework.ApiKeyFeature;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 *
 * @author Paul Samsotha
 */
@ApplicationPath("/api")
public class AppConfig extends ResourceConfig {
    
    public AppConfig() {
        register(JpaFeature.class);
        
        property(ApiKeyFeature.API_KEY_HEADER, "X-API-TOKEN");
        property(ApiKeyFeature.API_KEY_URI, "apikey");
        property(ApiKeyFeature.CLIENT_STORE_CLASS, JpaClientStore.class);
        
        register(ApiKeyFeature.class);
        register(DummyResource.class);
        
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
    }
}

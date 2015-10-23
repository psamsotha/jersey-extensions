package com.underdog.jersey.extension.framework;

import static com.underdog.jersey.extension.framework.TestConstants.CLIENT_ID;
import static com.underdog.jersey.extension.framework.TestConstants.CLIENT_PASS;
import com.underdog.jersey.extension.framework.model.ClientDetails;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;


/**
 *
 * @author Paul Samsotha
 */
public class ApiKeyFeatureDefaultsTest extends BaseApiKeyFeatureTest {
    
    public ApiKeyFeatureDefaultsTest() {
        super(ApiKeyFeature.DEFAULT_URI, ApiKeyFeature.DEFAULT_KEY_HEADER);
    }

    @Override
    protected void configureResourceConfig(ResourceConfig config) {
        List<ClientDetails> data = new ArrayList<>();
        data.add(new ClientDetails(CLIENT_ID, CLIENT_PASS));
        config.property(ApiKeyFeature.INIT_CLIENT_DATA, data);
        config.register(new LoggingFilter());
    }
}

package com.underdog.jersey.extension.framework;

import static com.underdog.jersey.extension.framework.TestConstants.CONFIGURED_HEADER;
import static com.underdog.jersey.extension.framework.TestConstants.CONFIGURED_URI;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Paul Samsotha
 */
public class ApiKeyFeatureConfiguredTest extends BaseApiKeyFeatureTest {

    public ApiKeyFeatureConfiguredTest() {
        super(CONFIGURED_URI, CONFIGURED_HEADER);
    }

    @Override
    protected void configureResourceConfig(ResourceConfig config) {
        config.property(ApiKeyFeature.KEY_GEN_CLASS, TestKeyGenerator.class)
                .property(ApiKeyFeature.CLIENT_STORE_CLASS, TestClientStore.class)
                .property(ApiKeyFeature.API_KEY_HEADER, CONFIGURED_HEADER)
                .property(ApiKeyFeature.API_KEY_URI, CONFIGURED_URI)
                .register(new LoggingFilter());
    }
}

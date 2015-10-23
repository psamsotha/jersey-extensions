
package com.underdog.jersey.extension.framework;

import com.underdog.jersey.extension.framework.model.ClientDetails;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.internal.util.PropertiesHelper;
import org.glassfish.jersey.server.model.ModelProcessor;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceModel;

/**
 *
 * @author Paul Samsotha
 */
@ConstrainedTo(RuntimeType.SERVER)
public class ApiKeyFeature implements Feature {

    /**
     * The endpoint URI for the getting API keys. The default is
     * {@code /api-key}.
     */
    public static final String API_KEY_URI = "ApiKeyFeature.endpoint.uri";

    /**
     * The API key header that that client should send. The default is
     * {@code  X-Api-Key}.
     */
    public static final String API_KEY_HEADER = "ApiKeyFeature.key.header";

    /**
     * The property value should be an implementation class of
     * {@code KeyGenerator}. {@code UURIKeyGenerator} is the default is this
     * property is not set.
     */
    public static final String KEY_GEN_CLASS = "ApiKeyFeature.keygen.class";

    /**
     * The property value should be the an implementation class of
     * {@code ClientStore}.
     */
    public static final String CLIENT_STORE_CLASS = "ApiKeyFeature.clientStore.class";

    /**
     * Initial data to be configured with the default client store. The property
     * value should be a {@code List<ClientDetails>}. This list we be used to
     * initialize the in-memory data store.
     */
    public static final String INIT_CLIENT_DATA = "ApiKeyFeature.initial.client.data";

    static final String DEFAULT_URI = "api-key";
    static final String DEFAULT_KEY_HEADER = "X-Api-Key";

    @Override
    public boolean configure(FeatureContext context) {
        Map<String, Object> properties = context.getConfiguration().getProperties();

        final String apiKeyHeader = getValue(properties, API_KEY_HEADER, DEFAULT_KEY_HEADER);
        
        final String apiKeyPath = getValue(properties, API_KEY_URI, DEFAULT_URI);

        context.register(new ApiKeyFilter(apiKeyPath, apiKeyHeader));

        final Resource apiKeyResource = Resource.builder(ApiKeyEndpoint.class).path(apiKeyPath).build();
        context.register(new ApiKeyModelProcessor(apiKeyResource));

        // configure component implementations
        Class<? extends KeyGenerator> generatorCls = getValue(properties, KEY_GEN_CLASS, Class.class);
        generatorCls = generatorCls == null ? UUIDKeyGenerator.class : generatorCls;

        Class<? extends ClientStore> clientStoreCls = getValue(properties, CLIENT_STORE_CLASS, Class.class);
        // Using DefaultClientStore. 
        if (clientStoreCls == null) {
            DefaultClientStore defaultClientStore = new DefaultClientStore();
            List<ClientDetails> db = getValue(properties, INIT_CLIENT_DATA, List.class);
            if (db == null) {
                throw new RuntimeException("Either set ClientStore or init List");
            }
            for (ClientDetails c : db) {
                defaultClientStore.saveClient(c);
            }
            context.register(new ApiKeyBinder(defaultClientStore, generatorCls));
        } else {
            context.register(new ApiKeyBinder(clientStoreCls, generatorCls));
        }

        return true;
    }

    private static class ApiKeyBinder extends AbstractBinder {

        private Class<? extends ClientStore> clientStoreImpl;
        private final Class<? extends KeyGenerator> generatorImpl;
        private ClientStore clientStore;

        public ApiKeyBinder(Class<? extends ClientStore> clientStoreImpl,
                Class<? extends KeyGenerator> generatorImpl) {
            this.clientStoreImpl = clientStoreImpl;
            this.generatorImpl = generatorImpl;
        }

        public ApiKeyBinder(ClientStore clientStoreImpl,
                Class<? extends KeyGenerator> generatorImpl) {
            this.clientStore = clientStoreImpl;
            this.generatorImpl = generatorImpl;
        }

        @Override
        protected void configure() {
            if (clientStore != null) {
                bind(clientStore).to(ClientStore.class);
            } else {
                bind(clientStoreImpl).to(ClientStore.class).in(Singleton.class);
            }
            bind(generatorImpl).to(KeyGenerator.class).in(Singleton.class);
        }
    }

    private static class ApiKeyModelProcessor implements ModelProcessor {

        private final Resource apiKeyResource;

        public ApiKeyModelProcessor(Resource apiKeyResource) {
            this.apiKeyResource = apiKeyResource;
        }

        @Override
        public ResourceModel processResourceModel(ResourceModel resourceModel,
                Configuration config) {
            ResourceModel.Builder builder
                    = new ResourceModel.Builder(resourceModel.getResources(), false);
            builder.addResource(apiKeyResource);
            return builder.build();
        }

        @Override
        public ResourceModel processSubResource(ResourceModel subResourceModel,
                Configuration config) {
            return subResourceModel;
        }
    }

    public static <T> T getValue(Map<String, ?> properties, String key, Class<T> type) {
        return PropertiesHelper.getValue(properties, key, type, null);
    }

    public static <T> T getValue(Map<String, ?> properties, String key, T defaultValue) {
        return PropertiesHelper.getValue(properties, key, defaultValue, null);
    }
}

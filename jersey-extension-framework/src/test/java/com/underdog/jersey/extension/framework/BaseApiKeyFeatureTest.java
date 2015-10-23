package com.underdog.jersey.extension.framework;

import static com.underdog.jersey.extension.framework.TestConstants.CLIENT_ID;
import static com.underdog.jersey.extension.framework.TestConstants.CLIENT_PASS;
import static com.underdog.jersey.extension.framework.TestConstants.MESSAGE;
import java.util.Objects;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Paul Samsotha
 */
public abstract class BaseApiKeyFeatureTest extends JerseyTest {

    protected final String endpointUri;
    protected final String apiKeyHeader;

    public BaseApiKeyFeatureTest(String endpointUri, String apiKeyHeader) {
        Objects.requireNonNull(endpointUri, "endpointUri cannot be null");
        Objects.requireNonNull(apiKeyHeader, "apiKeyHeader cannot be null");

        this.endpointUri = endpointUri;
        this.apiKeyHeader = apiKeyHeader;
    }

    @Path("test")
    public static class TestResource {

        @GET
        @Produces("text/plain")
        public String get() {
            return MESSAGE;
        }
    }
    
    @Override
    public ResourceConfig configure() {
        ResourceConfig config = new ResourceConfig(TestResource.class)
                .register(ApiKeyFeature.class);
        configureResourceConfig(config);
        return config;
    }

    protected abstract void configureResourceConfig(ResourceConfig config);

    /*
     * ApiKeyEndpoint test
     */
    @Test
    public void should_produce_api_key_with_valid_client_info() {
        // application/x-www-form-urlencoded
        Form form = new Form();
        form.param("clientId", CLIENT_ID);
        form.param("clientPass", CLIENT_PASS);
        Response formResponse = target(endpointUri)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form));
        assertEquals(200, formResponse.getStatus());
        ApiKeyEndpoint.ApiKey key = formResponse.readEntity(ApiKeyEndpoint.ApiKey.class);
        assertNotNull(key.apiKey);
        formResponse.close();

        // application/json
        final String json
                = "{"
                + "\"clientId\": \"" + CLIENT_ID + "\", "
                + "\"clientPass\": \"" + CLIENT_PASS + "\""
                + "}";
        Response jsonResponse = target(endpointUri)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(json));
        assertEquals(200, formResponse.getStatus());
        key = jsonResponse.readEntity(ApiKeyEndpoint.ApiKey.class);
        assertNotNull(key.apiKey);
        formResponse.close();
    }

    /*
     * ApiKeyEndpoint test
     */
    @Test
    public void should_get_401_Unauthorized_with_unknown_client() {
        Form form = new Form();
        form.param("clientId", CLIENT_ID + "Wrong!");
        form.param("clientPass", CLIENT_PASS);
        Response formResponse = target(endpointUri)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form));
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),
                formResponse.getStatus());
    }

    /*
     * ApiKeyEndpoint test
     */
    @Test
    public void should_get_401_Unauthorized_with_bad_password() {
        Form form = new Form();
        form.param("clientId", CLIENT_ID);
        form.param("clientPass", CLIENT_PASS + "Wrong!");
        Response formResponse = target(endpointUri)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form));
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),
                formResponse.getStatus());
    }

    /*
     * Feature integration test
     */
    @Test
    public void should_return_401_Unauthorized_for_no_api_key() {
        Response response = target("test").request().get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        response.close();
    }

    /*
     * Feature integration test
     */
    @Test
    public void should_return_200_with_api_key() {
        Form form = new Form();
        form.param("clientId", CLIENT_ID);
        form.param("clientPass", CLIENT_PASS);
        Response keyResponse = target(endpointUri)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form));
        assertEquals(200, keyResponse.getStatus());
        ApiKeyEndpoint.ApiKey key = keyResponse.readEntity(ApiKeyEndpoint.ApiKey.class);
        keyResponse.close();

        Response testResponse = target("test")
                .request()
                .header(apiKeyHeader, key.apiKey)
                .get();
        assertEquals(200, testResponse.getStatus());
        assertEquals(MESSAGE, testResponse.readEntity(String.class));
        testResponse.close();
    }

    /*
     * Feature integration test
     */
    @Test
    public void should_return_401_with_wrong_api_key() {
        Form form = new Form();
        form.param("clientId", CLIENT_ID);
        form.param("clientPass", CLIENT_PASS);
        Response keyResponse = target(endpointUri)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form));
        assertEquals(200, keyResponse.getStatus());
        ApiKeyEndpoint.ApiKey key = keyResponse.readEntity(ApiKeyEndpoint.ApiKey.class);
        keyResponse.close();

        Response testResponse = target("test")
                .request()
                .header(apiKeyHeader, key.apiKey + "Wrong!")
                .get();
        assertEquals(401, testResponse.getStatus());
        testResponse.close();
    }
}

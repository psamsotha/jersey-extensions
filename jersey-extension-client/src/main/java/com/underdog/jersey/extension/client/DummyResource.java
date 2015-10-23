
package com.underdog.jersey.extension.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Paul Samsotha
 */
@Path("dummy")
public class DummyResource {
    
    @GET
    @Produces("text/plain")
    public String getData() {
        return "Dummy Data";
    }
}

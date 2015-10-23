
package com.underdog.jersey.extension.framework;

import java.util.UUID;

/**
 * {@code KeyGenerator implementation that creates a random {@UUID}
 *
 * @author Paul Samsotha
 */
public class UUIDKeyGenerator implements KeyGenerator {

    /**
     * Generated key from {@UUID.randomUUID()}.
     */
    @Override
    public String generateKey(String clientId) {
        return UUID.randomUUID().toString();
    }  
}

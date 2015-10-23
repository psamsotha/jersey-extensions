
package com.underdog.jersey.extension.framework;

import java.util.UUID;

/**
 *
 * @author Paul Samsotha
 */
public class TestKeyGenerator implements KeyGenerator {

    @Override
    public String generateKey(String clientId) {
        return UUID.randomUUID().toString();
    }
}

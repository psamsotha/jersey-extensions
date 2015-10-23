
package com.underdog.jersey.extension.framework;

/**
 *
 * @author Paul Samsotha
 */
public interface KeyGenerator {
    
    /**
     * Generates API key.
     * 
     * @param clientId
     * @return String the created token.
     */
    String generateKey(String clientId);
}

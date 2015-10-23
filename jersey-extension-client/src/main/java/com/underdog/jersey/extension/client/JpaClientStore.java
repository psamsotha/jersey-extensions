
package com.underdog.jersey.extension.client;

import com.underdog.jersey.extension.framework.ClientStore;
import com.underdog.jersey.extension.framework.model.ClientDetails;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Paul Samsotha
 */
public class JpaClientStore implements ClientStore {
    
    @Inject
    private javax.inject.Provider<EntityManager> emProvider;

    @Override
    public void saveClient(ClientDetails clientDetails) {
        ClientDetailsEntity entity = ConversionHelper.fromClientDetails(clientDetails);
        EntityManager em = emProvider.get();
        em.getTransaction().begin();
        em.merge(entity);
        em.getTransaction().commit();
    }

    @Override
    public ClientDetails getClientById(String clientId) {
        final String sql = "select c from ClientDetails c where c.clientId = :clientId";
        EntityManager em = emProvider.get();
         try {
            ClientDetailsEntity entity = em.createQuery(sql, ClientDetailsEntity.class)
                .setParameter("clientId", clientId)
                .getSingleResult();
            return ConversionHelper.fromClientDetailsEntity(entity);
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ClientDetails getClientByApiKey(String apiKey) {
        final String sql = "select c from ClientDetails c where c.apiKey = :apiKey";
        EntityManager em = emProvider.get();
        try {
            ClientDetailsEntity entity = em.createQuery(sql, ClientDetailsEntity.class)
                .setParameter("apiKey", apiKey)
                .getSingleResult();
            return ConversionHelper.fromClientDetailsEntity(entity);
        } catch (NoResultException ex) {
            return null;
        }
    }
}

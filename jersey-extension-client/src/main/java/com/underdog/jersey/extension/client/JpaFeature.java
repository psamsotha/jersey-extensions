package com.underdog.jersey.extension.client;

import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.Immediate;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.ServiceLocatorProvider;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

/**
 *
 * @author Paul Samsotha
 */
public class JpaFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        // enable @Immediate scope so EMF is create when app starts up.
        ServiceLocator locator = ServiceLocatorProvider.getServiceLocator(context);
        ServiceLocatorUtilities.enableImmediateScope(locator);
        context.register(new AbstractBinder(){
            @Override
            protected void configure() {
                bindFactory(EmfFactory.class).to(EntityManagerFactory.class).in(Immediate.class);
                bindFactory(EmFactory.class).to(EntityManager.class).in(RequestScoped.class);
            }
        });
        return true;
    }
    
    public static class EmFactory implements Factory<EntityManager> {
        
        private static final Logger LOG = Logger.getLogger(EmFactory.class.getName());
        
        private final EntityManager em;
        
        @Inject
        public EmFactory(EntityManagerFactory emf) {
            LOG.info("--- creating EntityManager ---");
            em = emf.createEntityManager();
        }

        @Override
        public EntityManager provide() {
            return em;
        }

        @Override
        public void dispose(EntityManager em) {
            LOG.info("--- closing EntityManager ---");
        }
        
    }

    public static class EmfFactory implements Factory<EntityManagerFactory> {
        
        private static final Logger LOG = Logger.getLogger(EmfFactory.class.getName());
        
        private final EntityManagerFactory emf;
        
        @Inject
        private EmfFactory() {
            LOG.info("--- creating EntityManagerFactory ---");
            emf = Persistence.createEntityManagerFactory("api-key-pu");
        }

        @Override
        public EntityManagerFactory provide() {
            return emf;
        }

        @Override
        public void dispose(EntityManagerFactory emf) {
            LOG.info("--- closing EntityManagerFactory ---");
            emf.close();
        }
    }
    
    public static class EmfClosingApplicationListener implements ApplicationEventListener {
        
        private EntityManagerFactory emf;

        @Override
        public void onEvent(ApplicationEvent ae) {
            if (ae.getType() == ApplicationEvent.Type.DESTROY_FINISHED) {
                emf.close();
            }
        }

        @Override
        public RequestEventListener onRequest(RequestEvent re) {
            return null;
        }     
    }
}

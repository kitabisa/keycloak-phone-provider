package cc.coopersoft.keycloak.phone.providers.sender;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import cc.coopersoft.keycloak.phone.providers.spi.MessageSenderService;
import cc.coopersoft.keycloak.phone.providers.spi.MessageSenderServiceProviderFactory;

public class KitajagaMessageSenderServiceProviderFactory implements MessageSenderServiceProviderFactory {

    private Config.Scope config;

    @Override
    public MessageSenderService create(KeycloakSession keycloakSession) {
        return new KitajagaSmsSenderServiceProvider(config,keycloakSession.getContext().getRealm());
    }

    @Override
    public void init(Config.Scope config) {
        this.config = config;
    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return "kitajaga";
    }

}

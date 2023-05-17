package cc.coopersoft.keycloak.phone.providers.sender;

import cc.coopersoft.keycloak.phone.providers.constants.TokenCodeType;
import cc.coopersoft.keycloak.phone.providers.exception.MessageSendException;
import cc.coopersoft.keycloak.phone.providers.spi.MessageSenderService;
import kong.unirest.JsonNode;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.keycloak.Config;
import org.keycloak.models.RealmModel;

public class KitajagaSmsSenderServiceProvider implements MessageSenderService {
    private final Config.Scope config;
    private final RealmModel realm;

    public KitajagaSmsSenderServiceProvider(Config.Scope config, RealmModel realm) {
        this.config = config;
        this.realm = realm;
    }

    @Override
    public void sendSmsMessage(TokenCodeType type, String phoneNumber, String code, int expires, String kind) throws MessageSendException {
        HttpResponse<JsonNode> response = Unirest.post(this.config.get("baseurl") + "/whatsapp")
                .header("accept",  "application/json")
                .header("KJ-SECRET-KEY", this.config.get("secret"))
                .asJson();

        System.out.println(response.getBody().getObject().toString(2));
    }

    @Override
    public void close() {

    }
}

package cc.coopersoft.keycloak.phone.providers.sender;

import cc.coopersoft.keycloak.phone.providers.constants.TokenCodeType;
import cc.coopersoft.keycloak.phone.providers.exception.MessageSendException;
import cc.coopersoft.keycloak.phone.providers.spi.MessageSenderService;
import kong.unirest.JsonNode;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.keycloak.Config;
import org.keycloak.models.RealmModel;
import javax.json.*;

public class KitajagaSmsSenderServiceProvider implements MessageSenderService {
    private final Config.Scope config;
    private final RealmModel realm;

    public KitajagaSmsSenderServiceProvider(Config.Scope config, RealmModel realm) {
        this.config = config;
        this.realm = realm;
    }

    @Override
    public void sendSmsMessage(TokenCodeType type, String phoneNumber, String code, int expires, String kind) throws MessageSendException {

        if (kind == null) {
            kind = "";
        }

        JsonObject bodyObject = Json.createObjectBuilder()
                .add("token_code_type", type.name())
                .add("phone_number", phoneNumber)
                .add("code", code)
                .add("expire_in_second", expires)
                .add("kind", kind)
                .add("realm", this.realm.getDisplayName().toLowerCase())
                .add("channel", this.config.get("channel"))
                .build();

        String composeUrl = this.config.get("baseurl") + "/compose";

        HttpResponse<JsonNode> response = Unirest.post( composeUrl)
                .header("accept",  "application/json")
                .header("content-type", "application/json")
                .header("KJ-SECRET-KEY", this.config.get("secret"))
                .body(bodyObject.toString())
                .asJson();

        String statusCode = Integer.toString(response.getStatus());
        if (!response.isSuccess()) {
            if (response.getBody() != null) {
                JsonNode rBody = response.getBody();
                throw new MessageSendException(response.getStatus(), statusCode, rBody.getObject().getString("error"));
            } else {
                throw new MessageSendException(response.getStatus(), statusCode, "Error from upstream");
            }
        }
    }

    @Override
    public void close() {

    }
}


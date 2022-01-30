package net.lotho.utils.api;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.lotho.Azazel;

import java.util.UUID;

public class Mojang {

    private final Azazel instance;

    public Mojang(Azazel instance) {
        this.instance = instance;
    }

    public void fetchPlayerUUID(String name) throws UnirestException {
        new Thread(() -> {
            HttpResponse<JsonNode> jsonNodeHttpResponse = null;
            String uuid = null;

            try {
                jsonNodeHttpResponse = Unirest.get("https://api.mojang.com/users/profiles/minecraft/" + name.toLowerCase()).asJson();
                uuid = (String) jsonNodeHttpResponse.getBody().getObject().get("id");

                uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

                this.instance.playerUUIDs.put(name, UUID.fromString(uuid));
            } catch (UnirestException | NullPointerException e) {
                this.instance.playerUUIDs.put(name, null);
                e.printStackTrace();
            }
        }).start();
    }

}

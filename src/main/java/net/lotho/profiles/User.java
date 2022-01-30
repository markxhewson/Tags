package net.lotho.profiles;

import lombok.Getter;
import lombok.Setter;
import net.lotho.Azazel;

import java.util.UUID;

@Getter
@Setter
public class User {
    private final Azazel instance = Azazel.getInstance();

    private UserData data;
    private UUID uuid;
    private String activeTag;
    private Integer tokens;

    public User(UUID uuid, String activeTag, Integer tokens) {
        this.uuid = uuid;
        this.activeTag = activeTag;
        this.tokens = tokens;
        this.data = new UserData(uuid, null,0);
    }
}

package tech.mcprison.prison.mines.util;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by DMP9 on 29/01/2017.
 */
public class Miner {
    private String username = "";
    private String uuid = "";
    private boolean autosmelt = false;
    private boolean autoblock = false;

    public Optional<Player> getPlayer() {
        return Prison.get().getPlatform().getPlayer(UUID.fromString(uuid));
    }

    public boolean isUsingAutosmelt() {
        return autosmelt;
    }

    public boolean isUsingAutoblock() {
        return autoblock;
    }

    public static Miner fromPlayer(Player player) {
        Miner miner = new Miner();
        miner.username = player.getName();
        miner.uuid = player.getUUID().toString();
        return miner;
    }

    public void enableAutosmelt() {
        autosmelt = true;
    }

    public void enableAutoblock() {
        autoblock = true;
    }

    public void disableAutosmelt() {
        autosmelt = false;
    }

    public void disableAutoblock() {
        autoblock = false;
    }
}

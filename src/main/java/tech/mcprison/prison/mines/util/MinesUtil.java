package tech.mcprison.prison.mines.util;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.Mines;

import java.util.Optional;

/**
 * Created by DMP9 on 29/01/2017.
 */
public class MinesUtil {
    public static Optional<Miner> getMiner(Player player) {
        for (Miner miner : Mines.get().getPlayers()) {
            if (miner.getPlayer().get().getUUID().toString()
                .equalsIgnoreCase(player.getUUID().toString())) {
                return Optional.of(miner);
            }
        }
        return Optional.empty();
    }

    public static boolean isPlayerMiner(Player player) {
        return getMiner(player).isPresent();
    }

    public static boolean usingAutosmelt(Player player) {
        if (!isPlayerMiner(player)) {
            return false;
        }
        for (Miner miner : Mines.get().getPlayers()) {
            if (miner.getPlayer().get().getUUID().toString()
                .equalsIgnoreCase(player.getUUID().toString()) && miner.isUsingAutosmelt()) {
                return true;
            }
        }
        return false;
    }

    public static boolean usingAutoblock(Player player) {
        if (!isPlayerMiner(player)) {
            return false;
        }
        for (Miner miner : Mines.get().getPlayers()) {
            if (miner.getPlayer().get().getUUID().toString()
                .equalsIgnoreCase(player.getUUID().toString()) && miner.isUsingAutoblock()) {
                return true;
            }
        }
        return false;
    }

    public static void enableAutosmelt(Player player) throws Exception {
        if (!isPlayerMiner(player)) {
            Mines.get().addMiner(Miner.fromPlayer(player));
        }
        Optional<Miner> miner = getMiner(player);
        if (!miner.isPresent()) {
            throw new Exception("Couldn't register the player as a miner");
        }
        miner.get().enableAutosmelt();
    }

    public static void enableAutoblock(Player player) throws Exception {
        if (!isPlayerMiner(player)) {
            Mines.get().addMiner(Miner.fromPlayer(player));
        }
        Optional<Miner> miner = getMiner(player);
        if (!miner.isPresent()) {
            throw new Exception("Couldn't register the player as a miner");
        }
        miner.get().enableAutoblock();
    }

    public static void disableAutosmelt(Player player) throws Exception {
        if (!isPlayerMiner(player)) {
            Mines.get().addMiner(Miner.fromPlayer(player));
        }
        Optional<Miner> miner = getMiner(player);
        if (!miner.isPresent()) {
            throw new Exception("Couldn't register the player as a miner");
        }
        miner.get().disableAutosmelt();
    }

    public static void disableAutoblock(Player player) throws Exception {
        if (!isPlayerMiner(player)) {
            Mines.get().addMiner(Miner.fromPlayer(player));
        }
        Optional<Miner> miner = getMiner(player);
        if (!miner.isPresent()) {
            throw new Exception("Couldn't register the player as a miner");
        }
        miner.get().disableAutoblock();
    }
}

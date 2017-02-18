package tech.mcprison.prison.mines.util;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.mines.Mines;

import java.util.Optional;

/**
 * A class full of handy static methods
 */
public class MinesUtil {
    /**
     * Adds the mines prefix to a message
     * @param message the message to add the prefix to
     * @return the new message
     */
    public static String addPrefix(String message) {
        return Mines.get().getMinesMessages().prefix + message;
    }

    /**
     * Gets a {@link Miner} for a player
     * @param player the player to get a miner for
     * @return an optional of the miner if a miner has been created for a player, {@link Optional#empty()} otherwise
     */
    public static Optional<Miner> getMiner(Player player) {
        for (Miner miner : Mines.get().getPlayers()) {
            if (miner.getPlayer().get().getUUID().toString()
                .equalsIgnoreCase(player.getUUID().toString())) {
                return Optional.of(miner);
            }
        }
        return Optional.empty();
    }

    /**
     * Checks to see if a {@link Miner} is present for the given {@link Player}
     * @param player the player in question
     * @return {@link Optional#isPresent()} on the miner optional
     */
    public static boolean isPlayerMiner(Player player) {
        return getMiner(player).isPresent();
    }

    /**
     * Checks to see if a {@link Player} is using autosmelt
     * @param player the player in question
     * @return true if the player is using autosmelt
     */
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
    /**
     * Checks to see if a {@link Player} is using autoblock
     * @param player the player in question
     * @return true if the player is using autoblock
     */
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

    /**
     * Enables autosmelt for a player
     * @param player the player to enable autosmelt for
     * @throws Exception if, for whatever reason, even after creation of the associated {@link Miner} the optional is still empty
     */
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

    /**
     * Enables autoblock for a player
     * @param player the player to enable autoblock for
     * @throws Exception if, for whatever reason, even after creation of the associated {@link Miner} the optional is still empty
     */
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

    /**
     * Disables autosmelt for a player
     * @param player the player to disable autosmelt for
     * @throws Exception if, for whatever reason, even after creation of the associated {@link Miner} the optional is still empty
     */
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

    /**
     * Disables autoblock for a player
     * @param player the player to disable autoblock for
     * @throws Exception if, for whatever reason, even after creation of the associated {@link Miner} the optional is still empty
     */
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

package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.mines.util.Block;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.store.Jsonable;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DMP9 on 08/01/2017.
 */
public class Mine implements Jsonable<Mine> {
    private int minX, minY, minZ, maxX, maxY, maxZ;
    private double spawnX, spawnY, spawnZ;
    private float pitch, yaw;
    private String worldName, name;

    private List<Block> blocks;

    public Mine(){
        blocks = new ArrayList<>();
    }
    public static Mine load(File path) throws IOException {
        return new Mine().fromFile(path);
    }

    public Mine setBounds(Bounds bounds) {
        minX = bounds.getMin().getBlockX();
        minY = bounds.getMin().getBlockY();
        minZ = bounds.getMin().getBlockZ();
        maxX = bounds.getMax().getBlockX();
        maxY = bounds.getMax().getBlockY();
        maxZ = bounds.getMax().getBlockZ();
        worldName = bounds.getMin().getWorld().getName();
        return this;
    }

    public Mine setSpawn(Location location) {
        spawnX = location.getX();
        spawnY = location.getY();
        spawnZ = location.getZ();
        pitch = location.getPitch();
        yaw = location.getYaw();
        return this;
    }

    public Mine setName(String name) {
        this.name = name;
        return this;
    }

    public Mine setBlocks(HashMap<BlockType, Integer> blockMap) {
        blocks = new ArrayList<>();
        for (Map.Entry<BlockType, Integer> entry : blockMap.entrySet()){
            blocks.add(new Block().create(entry.getKey(),entry.getValue()));
        }
        return this;
    }

    public void save() {
        try {
            toFile(new File(Mines.get().getDataFolder(), "/mines/" + name + ".json"));
            Output.get().logInfo("&aSaved mine " + name);
        } catch (IOException e) {
            Output.get().logError("&cFailed to save mine " + name, e);
        }
    }

    public static Mine load(String json) throws IOException {
        return new Mine().fromJson(json);
    }

    public Mine fromJson(String json) {
        return Prison.get().getGson().fromJson(json, getClass());
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return Prison.get().getPlatform().getWorld(worldName).get();
    }

    public Location getSpawnLocation() {
        return new Location(Prison.get().getPlatform().getWorld(worldName).get(), spawnX, spawnY,
            spawnZ, pitch, yaw);
    }

    public void teleport(Player... players) {
        for (Player p : players) {
            p.teleport(getSpawnLocation());
            p.sendMessage("&bTeleported to mine '&7" + name + "&b'");
        }
    }

    public Bounds getBounds() {
        return new Bounds(
            new Location(Prison.get().getPlatform().getWorld(worldName).get(), (double) minX,
                (double) minY, (double) minZ),
            new Location(Prison.get().getPlatform().getWorld(worldName).get(), (double) maxX,
                (double) maxY, (double) maxZ));
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public Mine fromFile(File file) throws IOException {
        String json = new String(Files.readAllBytes(file.toPath()));
        return fromJson(json);
    }

    public String toJson() {
        return Prison.get().getGson().toJson(this, getClass());
    }

    public void toFile(File file) throws IOException {
        Files.write(file.toPath(), toJson().getBytes());
    }

    private void checkMinMax(){
        // Flip the locations, had some issues with this
        if (maxX < minX){
            if (maxY < minY){
                if (maxZ < minZ){
                    int oldX = minX;
                    int oldY = minY;
                    int oldZ = minZ;
                    minX = maxX;
                    minY = maxY;
                    minZ = maxZ;
                    maxX = oldX;
                    maxY = oldY;
                    maxZ = oldZ;
                }
            }
        }
    }
    public boolean isInMine(Location location){
        if (!location.getWorld().getName().equalsIgnoreCase(worldName)){
            return false;
        }
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (location.getBlockX() == x && location.getBlockY() == y
                        && location.getBlockZ() == z) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInMine(BlockType blockType) {
        for (Block block : blocks){
            if (blockType == block.type){
                return true;
            }
        }
        return false;
    }
    public int area(){
        int out = 0;
        checkMinMax();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    out++;
                }
            }
        }
        return out;
    }
    public boolean reset() {
        try {
            checkMinMax();
            int i = 0;
            List<BlockType> blockTypes = Mines.get().getMines().getRandomizedBlocks(this);
            for (int y = getBounds().getMin().getBlockY(); y <= getBounds().getMax().getBlockY(); y++) {
                for (int x = getBounds().getMin().getBlockX(); x <= getBounds().getMax().getBlockX(); x++) {
                    for (int z = getBounds().getMin().getBlockZ(); z <= getBounds().getMax().getBlockZ(); z++) {
                        new Location(Prison.get().getPlatform().getWorld(worldName).get(), x, y, z)
                            .getBlockAt().setType(blockTypes.get(i));
                        i++;
                    }
                }
            }

            Output.get().logInfo("&aReset mine " + name);
            if (Mines.get().getConfig().asyncReset) {
                try {
                    Mines.get().getMines().generateBlockList(this);
                } catch (Exception e) {
                    Output.get().logWarn("Couldn't generate blocks for mine "+name+" prior to next reset, async reset will be ignored",e);
                }
            }
            return true;
        } catch (Exception e) {
            Output.get().logError("&aFailed to reset mine " + name, e);
            return false;
        }
    }
}

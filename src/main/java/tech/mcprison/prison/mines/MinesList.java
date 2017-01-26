/*
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.mines;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.gui.Action;
import tech.mcprison.prison.gui.Button;
import tech.mcprison.prison.gui.ClickedButton;
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.mines.util.Block;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by DMP9 on 01/01/2017.
 */
public class MinesList implements List<Mine> {
    // Base list
    List<Mine> mines;

    // Declarations

    HashMap<Mine, List<BlockType>> randomizedBlocks;
    int resetCount = 0;

    // NPE
    public MinesList(){
        mines = new ArrayList<>();
        randomizedBlocks = new HashMap<>();
    }

    // Inherited methods -- don't know why I make things so difficult
    public int size() {
        return mines.size();
    }

    public boolean isEmpty() {
        return mines.isEmpty();
    }

    public boolean contains(Object o) {
        return mines.contains(o);
    }

    public Iterator<Mine> iterator() {
        return mines.iterator();
    }

    public Mine[] toArray() {
        return (Mine[]) mines.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return mines.toArray(a);
    }

    public boolean add(Mine c) {
        if (contains(c)) {
            return false;
        } else {
            return mines.add(c);
        }
    }

    public boolean remove(Object c) {
        if (!contains(c)) {
            return false;
        } else {
            return mines.remove(c);
        }
    }

    public boolean containsAll(Collection c) {
        return mines.containsAll(c);
    }

    public boolean addAll(Collection<? extends Mine> c) {
        return mines.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends Mine> c) {
        return mines.addAll(index, c);
    }

    public boolean removeAll(Collection c) {
        return mines.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return mines.retainAll(c);
    }

    public void clear() {
        mines.clear();
    }

    public Mine get(int index) {
        return mines.get(index);
    }

    public Mine set(int index, Mine element) {
        return mines.set(index, element);
    }

    public void add(int index, Mine c) {
        mines.add(index, c);
    }

    public Mine remove(int index) {
        return mines.remove(index);
    }

    public int indexOf(Object c) {
        return mines.indexOf(c);
    }

    public int lastIndexOf(Object c) {
        return mines.lastIndexOf(c);
    }

    public ListIterator<Mine> listIterator() {
        return mines.listIterator();
    }

    public ListIterator<Mine> listIterator(int index) {
        return mines.listIterator(index);
    }

    public MinesList subList(int fromIndex, int toIndex) {
        return (MinesList) mines.subList(fromIndex, toIndex);
    }

    public boolean contains(String name) {
        return select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return c.getName().equalsIgnoreCase(name);
            }

            @Override public void action(Mine c) {

            }
        }).size() > 0;
    }

    // Chain LINQ-style methods
    public MinesList select(MinesFilter filter) {
        MinesList out = new MinesList();
        for (Mine c : this) {
            if (filter.accept(c)) {
                out.add(c);
            }
        }
        return out;
    }

    public MinesList forEach(MinesFilter filter) {
        for (Mine c : this) {
            filter.action(c);
        }
        return this;
    }

    private void uselessVoid() {
    }

    private void selectiveSend(Player x) {
        if (Mines.get().getWorlds().contains(x.getLocation().getWorld().getName().toLowerCase())) {
            x.sendMessage(Mines.get().getConfig().resetMessage);
        }
    }

    private void selectiveSend2(Player x) {
        if (Mines.get().getWorlds().contains(x.getLocation().getWorld().getName().toLowerCase())) {
            x.sendMessage(Mines.get().getConfig().resetWarning
                .replaceAll("%mins%", "" + (resetCount / 60))
                .replaceAll("%seconds%", "" + resetCount));
        }
    }

    // Mine methods
    public TimerTask getTimerTask() {
        return new TimerTask() {
            @Override public void run() {
                if (Mines.get().getConfig().aliveTime == 0) {
                    return;
                }
                if (size() == 0) {
                    return;
                }
                if (resetCount == 0) {
                    reset();
                    if (!Mines.get().getConfig().multiworld) {
                        Prison.get().getPlatform().getOnlinePlayers()
                            .forEach(x -> x.sendMessage(Mines.get().getConfig().resetMessage));
                    } else {
                        Prison.get().getPlatform().getOnlinePlayers()
                            .forEach(x -> selectiveSend(x));
                    }
                    resetCount = Mines.get().getConfig().aliveTime;
                }
                for (int i : Mines.get().getConfig().resetWarningTimes) {
                    if (resetCount == i) {
                        if (!Mines.get().getConfig().multiworld) {
                            Prison.get().getPlatform().getOnlinePlayers().forEach(x -> x
                                .sendMessage(Mines.get().getConfig().resetWarning
                                    .replaceAll("%mins%", "" + (resetCount / 60))
                                    .replaceAll("%seconds%", "" + resetCount)));
                        } else {
                            Prison.get().getPlatform().getOnlinePlayers()
                                .forEach(x -> selectiveSend2(x));
                        }
                    }
                }
                if (resetCount > 0) {
                    resetCount--;
                }
                Output.get().logInfo("Current time until reset: "+resetCount);
            }
        };
    }

    public Mine get(String name) {
        MinesList sublist = select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return c.getName().equalsIgnoreCase(name);
            }

            @Override public void action(Mine c) {

            }
        });
        if (sublist.size() == 0) {
            return null;
        }
        return sublist.get(0);
    }

    public MinesList initialize() {
        Mines.get().setState(MinesState.INITIALIZING);
        mines = new ArrayList<>();
        if (!new File(Mines.get().getDataFolder(), "/mines/").exists()) {
            new File(Mines.get().getDataFolder(), "/mines/").mkdir();
        }
        File[] files = new File(Mines.get().getDataFolder(), "/mines/")
            .listFiles(pathname -> pathname.getName().endsWith(".json"));
        for (File f : files) {
            try {
                Mine m = Mine.load(f);
                add(m);
                Output.get().logInfo("&aLoaded mine " + m.getName());
            } catch (IOException e) {
                Output.get().logError("&cFailed to load mine " + f.getName(), e);
            }
        }
        Mines.get().setState(MinesState.INITIALIZED);
        Output.get().logInfo("&bLoaded " + size() + " mines");
        resetCount = Mines.get().getConfig().aliveTime;
        return this;
    }

    public void save() {
        for (Mine mine : this) {
            mine.save();
        }
    }

    public void reset() {
        MinesFilter resetFilter = new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return true;
            }

            @Override public void action(Mine c) {
                c.reset();
            }
        };
        forEach(resetFilter);
    }

    public void reset(MinesFilter resetFilter) {
        forEach(resetFilter);
    }

    public List<BlockType> getRandomizedBlocks(Mine m) {
        if (!randomizedBlocks.containsKey(m)) {
            generateBlockList(m);
        }
        return randomizedBlocks.get(m);
    }

    public void generateBlockList(Mine m) {
        Bounds bounds = m.getBounds();
        Random random = new Random();
        ArrayList<BlockType> blocks = new ArrayList<>();
        World world = bounds.getMin().getWorld();
        int target = m.area();
        for (int i = 0; i < target; i++) {
            int chance = random.nextInt(101);
            boolean set = false;
            for (Block block : m.getBlocks()) {
                if (chance <= block.chance) {
                    blocks.add(block.type);
                    set = true;
                    break;
                }
                else {
                    chance -= block.chance;
                }
            }
            if (!set) {
                blocks.add(BlockType.AIR);
            }
        }
        randomizedBlocks.put(m, blocks);
    }

    public HashMap<Mine, List<BlockType>> getRandomizedBlocks() {
        return randomizedBlocks;
    }

    boolean override = false;
    MinesFilter guiSelection = new MinesFilter() {
        @Override public boolean accept(Mine c) {
            return true;
        }

        @Override public void action(Mine c) {

        }
    };

    public void setGUIOverride(MinesFilter mines) {
        override = true;
        guiSelection = mines;
    }

    private HashMap<Player, MinesList> players;

    public void addTeleportRule(Player player, MinesList sublist) {
        removeTeleportRule(player);
        if (players == null) {
            players = new HashMap<>();
        }
        players.put(player, sublist);
    }

    public void removeTeleportRule(Player player) {
        players.remove(player);
    }

    public MinesList getTeleportRule(Player player) {
        return players.get(player);
    }

    public boolean canTeleport(Player player, Mine mine) {
        if (getTeleportRule(player) == null){
            return true;
        }
        else {
            return getTeleportRule(player).contains(mine);
        }
    }

    public boolean allowedToMine(Player player,Location location){
        MinesList sublist = select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return c.isInMine(location);
            }

            @Override public void action(Mine c) {

            }
        });
        if (sublist.size() > 1){
            Output.get().logWarn("Potential overlap in mines -- there are "+sublist.size() + " mines at location " + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + " in world "+location.getWorld().getName());
            forEach(x -> Output.get().logWarn(x.getName()));
        }
        if (sublist.select(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return canTeleport(player,c);
            }

            @Override public void action(Mine c) {

            }
        }).size() == 0){
            return true;
        }
        return false;
    }

    public GUI createGUI() {
        GUI g = Prison.get().getPlatform().createGUI(Mines.get().getConfig().guiName, size() <= 9 ?
            9 :
            size() <= 18 ? 18 : size() <= 27 ? 27 : size() <= 36 ? 36 : size() <= 45 ? 45 : 54);
        final int[] i = {0};
        select(guiSelection).forEach(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return false;
            }

            @Override public void action(Mine c) {
                if (i[0] < 55) {
                    g.addButton(i[0]++, new Button(BlockType.GRASS, new Action() {
                        @Override public void run(ClickedButton btn) {
                            c.teleport(btn.getPlayer());
                        }
                    }, "&6" + c.getName(), true));
                }
            }
        });
        return g.build();
    }
}

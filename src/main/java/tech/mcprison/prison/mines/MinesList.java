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
import tech.mcprison.prison.gui.GUI;
import tech.mcprison.prison.internal.World;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.BlockType;
import tech.mcprison.prison.util.Bounds;
import tech.mcprison.prison.util.ChatColor;
import tech.mcprison.prison.util.Location;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

/**
 * Created by DMP9 on 01/01/2017.
 */
public class MinesList implements List<Mine> {
    // Base list
    List<Mine> mines;

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

    public MinesList foreach(MinesFilter filter) {
        for (Mine c : this) {
            filter.action(c);
        }
        return this;
    }

    // Mine methods
    public MinesList initialize() {

        for (File f : new File(Mines.get().getDataFolder(),"/mines/").listFiles(new FileFilter() {
            @Override public boolean accept(File pathname) {
                return pathname.getName().endsWith(".json");
            }
        })) {
            try {
                Mine m = Mine.load(f);
                add(m);
                Output.get().logInfo("Loaded mine "+m.getName());
            } catch (IOException e) {
                Output.get().logError("Failed to load mine "+f.getName(),e);
            }
        }
        return this;
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
        foreach(resetFilter);
    }

    public void reset(MinesFilter resetFilter) {
        foreach(resetFilter);
    }

    HashMap<Mine, List<BlockType>> randomizedBlocks;

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
        int minX = bounds.getMin().getBlockX();
        int minY = bounds.getMin().getBlockY();
        int minZ = bounds.getMin().getBlockZ();
        int maxX = bounds.getMax().getBlockX();
        int maxY = bounds.getMax().getBlockY();
        int maxZ = bounds.getMax().getBlockZ();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    double chance = random.nextDouble();
                    for (Map.Entry<BlockType, Double> entry : m.getBlocks().entrySet()) {
                        if (chance <= entry.getValue()) {
                            world.getBlockAt(new Location(world, x, y, z)).setType(entry.getKey());
                            break;
                        }
                    }
                }
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

    public GUI createGUI() {
        GUI g = Prison.get().getPlatform().createGUI(ChatColor.stripColor("") + "", size() <= 9 ?
            9 :
            size() <= 18 ? 18 : size() <= 27 ? 27 : size() <= 36 ? 36 : size() <= 45 ? 45 : 54);
        final int[] i = {-1};
        select(guiSelection).foreach(new MinesFilter() {
            @Override public boolean accept(Mine c) {
                return false;
            }

            @Override public void action(Mine c) {
                g.addButton(i[0]++, new Button(BlockType.GRASS, new Action() {
                    @Override public void run(GUI gui) {

                    }
                }, "&6" + c.getName(), true));
            }
        });
        return g.build();
    }
}

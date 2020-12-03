package com.fury.game.system.files.loaders.npc;

import com.fury.cache.Revision;
import com.fury.game.GameSettings;
import com.fury.game.system.files.Resources;
import com.fury.util.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

public class MobCombatDefinitionsLoader {
    public static HashMap<Integer, MobCombatDefinitions> definitions = new HashMap<>();
    private final static MobCombatDefinitions DEFAULT_DEFINITION = new MobCombatDefinitions(1, -1, -1, -1, 5, 1, 1, 0, MobCombatDefinitions.MELEE, -1, -1, MobCombatDefinitions.PASSIVE);

    public static void init() {
        long start = System.currentTimeMillis();
        if (GameSettings.PACK)
            loadUnpacked();
        if (new File(Resources.getFile("packedCombatDefinitions")).exists())
            loadPacked();

        if (GameSettings.DEBUG)
            System.out.println("Mob combat definitions loaded in " + (System.currentTimeMillis() - start) + "ms");
    }

    public static MobCombatDefinitions forId(int npcId, Revision revision) {
        MobCombatDefinitions def = definitions.get(npcId);
        if (def == null)
            return DEFAULT_DEFINITION;
        return def;
    }

    @SuppressWarnings("unused")
    private static void loadUnpacked() {
        int count = 0;
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(Resources.getFile("packedCombatDefinitions")));
            BufferedReader in = new BufferedReader(new FileReader(Resources.getFile("unpackedCombatDefinitions")));
            while (true) {
                String line = in.readLine();
                count++;
                if (line == null)
                    break;
                if (line.startsWith("//"))
                    continue;
                String[] split = line.split(" - ", 2);
                if (split.length != 2)
                    throw new RuntimeException("Invalid Mob Combat Definitions line: " + count + ", " + line);
                int id = Integer.parseInt(split[0]);
                String[] parts = split[1].split(" ", 12);
                if (parts.length != 12)
                    throw new RuntimeException("Invalid Mob Combat Definitions line: " + count + ", " + line);
                int hitpoints = Integer.parseInt(parts[0]);
                int attackAnim = Integer.parseInt(parts[1]);
                int defenceAnim = Integer.parseInt(parts[2]);
                int deathAnim = Integer.parseInt(parts[3]);
                int attackDelay = Integer.parseInt(parts[4]);
                int deathDelay = Integer.parseInt(parts[5]);
                int respawnDelay = Integer.parseInt(parts[6]);
                int maxHit = Integer.parseInt(parts[7]);
                int attackStyle;
                if (parts[8].equalsIgnoreCase("MELEE"))
                    attackStyle = MobCombatDefinitions.MELEE;
                else if (parts[8].equalsIgnoreCase("RANGE"))
                    attackStyle = MobCombatDefinitions.RANGE;
                else if (parts[8].equalsIgnoreCase("MAGE"))
                    attackStyle = MobCombatDefinitions.MAGE;
                else if (parts[8].equalsIgnoreCase("SPECIAL"))
                    attackStyle = MobCombatDefinitions.SPECIAL;
                else if (parts[8].equalsIgnoreCase("SPECIAL2"))
                    attackStyle = MobCombatDefinitions.SPECIAL2;
                else
                    throw new RuntimeException("Invalid Mob Combat Definitions line: " + line);
                int attackGfx = Integer.parseInt(parts[9]);
                int attackProjectile = Integer.parseInt(parts[10]);
                int aggressivenessType;
                if (parts[11].equalsIgnoreCase("PASSIVE"))
                    aggressivenessType = MobCombatDefinitions.PASSIVE;
                else if (parts[11].equalsIgnoreCase("AGGRESSIVE"))
                    aggressivenessType = MobCombatDefinitions.AGGRESSIVE;
                else
                    throw new RuntimeException("Invalid Mob Combat Definitions line: " + line);
                out.writeShort(id);
                out.writeShort(hitpoints);
                out.writeShort(attackAnim);
                out.writeShort(defenceAnim);
                out.writeShort(deathAnim);
                out.writeByte(attackDelay);
                out.writeByte(deathDelay);
                out.writeInt(respawnDelay);
                out.writeShort(maxHit);
                out.writeByte(attackStyle);
                out.writeShort(attackGfx);
                out.writeShort(attackProjectile);
                out.writeByte(aggressivenessType);
                definitions.put(id, new MobCombatDefinitions(
                        hitpoints, attackAnim, defenceAnim, deathAnim,
                        attackDelay, deathDelay, respawnDelay, maxHit,
                        attackStyle, attackGfx, attackProjectile,
                        aggressivenessType));
            }
            in.close();
            out.close();
        } catch (Throwable e) {
            Logger.handle(e);
        }
    }

    @SuppressWarnings("unused")
    private static void loadPacked() {
        try {
            RandomAccessFile in = new RandomAccessFile(Resources.getFile("packedCombatDefinitions"), "r");
            FileChannel channel = in.getChannel();
            ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                int id = buffer.getShort() & 0xffff;
                int hitpoints = buffer.getShort() & 0xffff;
                int attackAnim = buffer.getShort() & 0xffff;
                int defenceAnim = buffer.getShort() & 0xffff;
                int deathAnim = buffer.getShort() & 0xffff;
                int attackDelay = buffer.get() & 0xff;
                int deathDelay = buffer.get() & 0xff;
                int respawnDelay = buffer.getInt();
                int maxHit = buffer.getShort() & 0xffff;
                int attackStyle = buffer.get() & 0xff;
                int attackGfx = buffer.getShort() & 0xffff;
                int attackProjectile = buffer.getShort() & 0xffff;
                int agressivenessType = buffer.get() & 0xff;

                if (attackAnim == 65535)
                    attackAnim = -1;
                if (defenceAnim == 65535)
                    defenceAnim = -1;
                if (deathAnim == 65535)
                    deathAnim = -1;
                if (attackGfx == 65535)
                    attackGfx = -1;
                if (attackProjectile == 65535)
                    attackProjectile = -1;
                definitions.put(id, new MobCombatDefinitions(
                        hitpoints, attackAnim, defenceAnim, deathAnim,
                        attackDelay, deathDelay, respawnDelay, maxHit,
                        attackStyle, attackGfx, attackProjectile,
                        agressivenessType));
            }
            channel.close();
            in.close();
        } catch (Throwable e) {
            Logger.handle(e);
        }
    }
}

package com.fury.network.packet.impl;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.core.model.node.entity.actor.figure.Projectile;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.ListWidget;
import com.fury.game.content.global.treasuretrails.ClueConstants;
import com.fury.game.content.global.treasuretrails.ClueScroll;
import com.fury.game.content.global.treasuretrails.ClueTiers;
import com.fury.game.content.global.treasuretrails.TreasureTrails;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.Skills;
import com.fury.game.content.skill.free.dungeoneering.DungeonConstants;
import com.fury.game.content.skill.free.dungeoneering.DungeonPartyManager;
import com.fury.game.content.skill.free.dungeoneering.DungeoneeringRewards;
import com.fury.game.entity.character.combat.CombatConstants;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.entity.character.combat.equipment.EquipmentBonus;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.npc.drops.Drop;
import com.fury.game.entity.character.player.PlayerBackup;
import com.fury.game.entity.character.player.content.BonusManager;
import com.fury.game.entity.character.player.content.LoyaltyProgramme;
import com.fury.game.content.combat.magic.MagicSpellBook;
import com.fury.game.entity.character.player.info.PlayerRights;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.files.plugin.loader.CommandLoader;
import com.fury.game.network.Commands;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.node.entity.actor.figure.ProjectileManager;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDropHandler;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.node.entity.actor.figure.player.Points;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.node.entity.actor.object.TempObjectManager;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.game.system.files.Resources;
import com.fury.game.system.files.loaders.item.WeaponAnimations;
import com.fury.game.system.files.loaders.item.WeaponInterfaces;
import com.fury.game.system.files.logs.LoggedPlayerItem;
import com.fury.game.system.files.logs.PlayerLogs;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.MapBuilder;
import com.fury.game.world.map.clip.ClippingManipulation;
import com.fury.game.world.map.clip.Flags;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.map.region.RegionIndex;
import com.fury.game.world.map.region.RegionIndexing;
import com.fury.game.world.map.region.RegionMap;
import com.fury.game.world.update.flag.Flag;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.network.packet.Packet;
import com.fury.network.packet.PacketListener;
import com.fury.tools.accounts.Utils.SearchUtils;
import com.fury.util.FontUtils;
import com.fury.util.Misc;

import java.io.File;
import java.util.*;


/**
 * This packet listener manages commands a player uses by using the
 * command console prompted by using the "`" char.
 *
 * @author Gabriel Hannason
 */

public class CommandPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        String command = packet.readString();
        CommandLoader.handle(player, command);
        String[] parts = command.toLowerCase().split(" ");
        if (command.contains("\r") || command.contains("\n")) {
            return;
        }
        try {
            switch (player.getRights()) {
                case PLAYER:
                    playerCommands(player, parts, command);
                    break;
                case MODERATOR:
                    playerCommands(player, parts, command);
                    break;
                case ADMINISTRATOR:
                    playerCommands(player, parts, command);
                    administratorCommands(player, parts, command);
                    break;
                case OWNER:
                    playerCommands(player, parts, command);
                    administratorCommands(player, parts, command);
                    ownerCommands(player, parts, command);
                    developerCommands(player, parts, command);
                    break;
                case DEVELOPER:
                    playerCommands(player, parts, command);
                    administratorCommands(player, parts, command);
                    ownerCommands(player, parts, command);
                    developerCommands(player, parts, command);
                    break;
                case SUPPORT:
                    playerCommands(player, parts, command);
                    break;
                case VETERAN:
                    playerCommands(player, parts, command);
                    break;
                default:
                    break;
            }
        } catch (Exception exception) {
            exception.printStackTrace();

            if (player.getRights() == PlayerRights.DEVELOPER) {
                player.message("Error executing that command.");
            } else {
                player.message("Error executing that command.");
            }

        }
    }

    private static void playerCommands(final Player player, String[] command, String wholeCommand) {
        if (wholeCommand.contains("_")) {
            String[] split = wholeCommand.split("_");
            switch (split[0]) {
                case "setCompPreset":
                    int preset = Integer.parseInt(split[1]);
                    int[] colours = new int[7];
                    for (int i = 0; i < 7; i++)
                        colours[i] = Integer.parseInt(split[2 + i]);
                    player.getColourPresets()[preset] = colours;
                    break;
                case "sendcaperecolour":
                    colours = new int[7];
                    for (int i = 0; i < 7; i++)
                        colours[i] = Integer.parseInt(split[1 + i]);
                    player.setCapeRecolours(colours);
                    player.getUpdateFlags().add(Flag.APPEARANCE);
                    break;
                case "capergbcolours":
                    colours = new int[7];
                    for (int i = 0; i < 7; i++)
                        colours[i] = Integer.parseInt(split[1 + i]);
                    player.setRgbColours(colours);
                    break;
            }
        }
        if (command[0].equals("dzone")) {
            if (!player.isDonor()) {
                player.message("You must be a donor to access this area.");
                player.message("For more info about donating talk to Sir Prysin in edgeville.");
            }
        }
        if (command[0].equals("roll") && player.getUsername().equalsIgnoreCase("Greg")) {
            if (player.getClanChatName() == null) {
                player.message("You need to be in a clanchat channel to roll a dice.");
                return;
            } else if (player.getClanChatName().equalsIgnoreCase("help")) {
                player.message("You can't roll a dice in this clanchat channel!");
                return;
            }
            int dice = Integer.parseInt(command[1]);
            player.getMovement().reset();
            player.animate(11900);
            player.graphic(2075);
            ClanChatManager.sendMessage(player.getCurrentClanChat(), "[ClanChat] " + FontUtils.WHITE + player.getUsername() + " just rolled " + FontUtils.COL_END + dice + FontUtils.WHITE + " on the percentile dice." + FontUtils.COL_END);
        }

        //Input from client side
        if (command[0].equalsIgnoreCase("[cn]")) {
            if (player.getInterfaceId() == 40172) {
                ClanChatManager.setName(player, wholeCommand.substring(wholeCommand.indexOf(" ") + 1));
            }
        }
    }

    private static void administratorCommands(final Player player, String[] command, String wholeCommand) {

        //What's the point? Only any use if can set if offline
        /*if (command[0].equals("setpassword")) {
            String syntax = command[1].trim();
            String playerName = wholeCommand.replace(command[0], "").replace(command[1], "").trim();
            Player target = World.getPlayerByName(playerName);
            if (target == null) {
                player.message("Player must be online to set password!");
            } else {
                if(syntax == null || syntax.length() <= 2 || syntax.length() > 15 || !NameUtils.isValidName(syntax)) {
                    player.message("That password is invalid. Please try another password.");
                    return;
                }
                if(syntax.contains("_")) {
                    player.message("Password can not contain underscores.");
                    return;
                }
                String passwordHash = null;
                try {
                    passwordHash = PBKDF2.generatePasswordHash(syntax);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                if(passwordHash != null) {
                    target.setPasswordHash(passwordHash);
                    player.message("Account's password has been successfully changed.");
                } else {
                    player.message("An error occurred. Please try again.");
                }

            }
        }*/

        if (command[0].equals("gold")) {
            Player p = World.getPlayerByName(wholeCommand.substring(5));
            if (p != null) {
                long gold = 0;
                for (Item item : p.getInventory().getItems()) {
                    if (item != null && item.getId() > 0 && item.tradeable())
                        gold += item.getDefinitions().getValue();
                }
                for (Item item : p.getEquipment().getItems()) {
                    if (item != null && item.getId() > 0 && item.tradeable())
                        gold += item.getDefinitions().getValue();
                }
                for (int i = 0; i < 9; i++) {
                    /*for (Item item : p.getBank(i).getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable())
                            gold += item.getDefinitions().getValue();
                    }*/
                }
                gold += p.getMoneyPouch().getTotal();
                player.message(p.getUsername() + " has " + Misc.insertCommasToNumber(String.valueOf(gold)) + " coins.");
            } else
                player.message("Can not find player online.");
        }

        if (wholeCommand.equalsIgnoreCase("rd")) {
            MobDrops.reload();
        }

        if (command[0].equals("sim")) {//Simulate kills
            if (!player.getTimers().getClickDelay().elapsed(30)) {
                player.message("You can only do this once every 30 seconds.");
                return;
            }
            int simCount = Integer.parseInt(command[1]);
            String name = wholeCommand.replaceAll(command[0], "").replaceAll(command[1], "").trim().toLowerCase();
            player.getTimers().getClickDelay().reset();

            NpcDefinition def = NpcDefinition.get(name);
            if (def == null) {
                player.message("Could not find npc \"" + name + "\".");
                return;
            }
            player.getPacketSender().sendInterfaceRemoval();

            String gameModeRate = "[" + player.getDropRate() + "x]";
            String title = "Simulated drops for " + def.name + " " + gameModeRate + (player.getRights() == PlayerRights.DEVELOPER ? " " + def.id : "");

            Drop[] drops = MobDrops.getDrops(def.getId(), def.revision);

            if (drops == null || drops.length <= 0)
                return;

            Map<Integer, Integer> sim = new HashMap<>();
            List<Drop> simDrops = new ArrayList<>();
            for (int i = 0; i < simCount; i++) {
                Drop d = MobDropHandler.getDrop(drops, player);
                if (d == null)
                    continue;
                simDrops.add(d);
                sim.put(d.getItemId(), sim.containsKey(d.getItemId()) ? (sim.get(d.getItemId()) + 1) : 1);
            }

            player.getPacketSender().sendString(8147, "Name Rate (Count)");

            List<String> lines = new ArrayList<>();
            for (Drop drop : drops) {
                if (drop.getMinAmount() != 0) {
                    String simulation = sim.containsKey(drop.getItemId()) && sim.get(drop.getItemId()) != 0 ? String.format("1/%.0f", (double) simCount / sim.get(drop.getItemId())) + " (" + sim.get(drop.getItemId()) + ")" : "";
                    lines.add(" " + new Item(drop.getItemId()).getDefinition().getName() + "    " + simulation);
                }
            }

            ListWidget.display(player, title, "Name Rate (Count)", lines.toArray(new String[lines.size()]));
        }
    }


    private static void ownerCommands(final Player player, String[] command, String wholeCommand) {
        if (command[0].equals("setlevel")) {
            String s = command[1].trim();
            int level = Integer.parseInt(command[2]);
            String playerName = wholeCommand.replace(command[0], "").replace(command[1], "").replace(command[2], "").trim();
            Player target = World.getPlayerByName(playerName);
            if (target == null) {
                player.message("Player must be online to set level!");
            } else {
                player.message("Set player level");
                Skill skill = Skill.forName(s);
                boolean isNew = skill.isNewSkill();
                target.getSkills().setLevel(skill, isNew ? level * 10 : level);
                target.getSkills().setMaxLevel(skill, isNew ? level * 10 : level);
                target.getSkills().setExperience(skill, Skills.getExperienceForLevel(level));

            }
        }
        if (command[0].equals("setxp")) {
            String s = command[1].trim();
            int exp = Integer.parseInt(command[2]);
            String playerName = wholeCommand.replace(command[0], "").replace(command[1], "").replace(command[2], "").trim();
            Player target = World.getPlayerByName(playerName);
            if (target == null) {
                player.message("Player must be online to set level!");
            } else {
                Skill skill = Skill.forName(s);
                int level = Skills.getLevelForExperience(exp, skill == Skill.DUNGEONEERING);
                player.message("Set player level: " + level + "; exp: " + exp);
                boolean isNew = skill.isNewSkill();
                target.getSkills().setLevel(skill, isNew ? level * 10 : level);
                target.getSkills().setMaxLevel(skill, isNew ? level * 10 : level);
                target.getSkills().setExperience(skill, exp);
            }
        }

        if (command[0].contains("pure")) {
            int[] data = new int[]{1153, 10499, 1725, 4587, 1129, 1540, 2497, 7459, 3105, 2550, 9244};
            for (int i = 0; i < data.length; i++) {
                int id = data[i];
                //player.getEquipment().set(new Item(id, id == 9244 ? 500 : 1));
            }
            BonusManager.update(player);
            WeaponInterfaces.assign(player, player.getEquipment().get(Slot.WEAPON));
            WeaponAnimations.update(player);
            player.getEquipment().refresh();
            player.getUpdateFlags().add(Flag.APPEARANCE);
            player.getInventory().clear();
            player.getInventory().add(new Item(1216, 1000), new Item(9186, 1000), new Item(862, 1000), new Item(892, 10000), new Item(4154, 5000), new Item(2437, 1000), new Item(2441, 1000), new Item(2445, 1000), new Item(386, 1000), new Item(2435, 1000));
            player.getSkills().init();
            player.getSkills().setMaxLevel(Skill.ATTACK, 60);
            player.getSkills().setMaxLevel(Skill.STRENGTH, 85);
            player.getSkills().setMaxLevel(Skill.RANGED, 85);
            player.getSkills().setMaxLevel(Skill.PRAYER, 520);
            player.getSkills().setMaxLevel(Skill.MAGIC, 70);
            player.getSkills().setMaxLevel(Skill.CONSTITUTION, 850);
            for (Skill skill : Skill.values()) {
                player.getSkills().restore(skill);
                player.getSkills().setExperience(skill, Skills.getExperienceForLevel(player.getSkills().getMaxLevel(skill)));
            }
        }
        if (command[0].equals("value")) {
            long gold = 0;
            Player p = World.getPlayerByName(wholeCommand.replace(command[0], "").trim());
            if (p != null) {
                for (Item item : p.getInventory().getItems()) {
                    if (item != null && item.getId() > 0 && item.tradeable())
                        gold += item.getDefinitions().getValue() * item.getAmount();
                }
                for (Item item : p.getEquipment().getItems()) {
                    if (item != null && item.getId() > 0 && item.tradeable())
                        gold += item.getDefinitions().getValue() * item.getAmount();
                }
                for (int i = 0; i < 9; i++) {
                    /*for (Item item : p.getBank(i).getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable())
                            gold += item.getDefinitions().getValue() * item.getAmount();
                    }*/
                }
                gold += p.getMoneyPouch().getTotal();
            }
            player.message(p.getUsername() + " has a value of: " + gold);
        }
        if (command[0].equals("cashineco")) {
            long gold = 0, plrLoops = 0;
            for (Player p : GameWorld.getPlayers()) {
                if (p != null && (p.getRights() == PlayerRights.PLAYER)) {
                    for (Item item : p.getInventory().getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable())
                            gold += item.getDefinitions().getValue() * item.getAmount();
                    }
                    for (Item item : p.getEquipment().getItems()) {
                        if (item != null && item.getId() > 0 && item.tradeable())
                            gold += item.getDefinitions().getValue() * item.getAmount();
                    }
                    for (int i = 0; i < 9; i++) {
                        /*for (Item item : p.getBank(i).getItems()) {
                            if (item != null && item.getId() > 0 && item.tradeable())
                                gold += item.getDefinitions().getValue() * item.getAmount();
                        }*/
                    }
                    gold += p.getMoneyPouch().getTotal();
                    plrLoops++;
                }
            }
            player.message("Total gold in economy right now: " + gold + ", went through " + plrLoops + " players items.");
        }
        /*if (command[0].equals("find")) {
            String name = wholeCommand.substring(5).toLowerCase().replaceAll("_", " ");
            player.message("Finding item id for item - " + name);
            boolean found = false;
            for (int i = 0; i < Loader.getTotalItems(Revision.PRE_RS3); i++) {//TODO G.E search algorithm needed
                ItemDefinition def = Loader.getItem(i, Revision.PRE_RS3);
                if(def != null && !def.noted && def.getName() != null && def.getName().toLowerCase().contains(name)) {
                    player.getPacketSender().sendConsoleMessage("Found item with name [" + def.getName().toLowerCase() + "] - id: " + i);
                    found = true;
                }
            }
            if (!found)
                player.getPacketSender().sendConsoleMessage("No item with name [" + name + "] has been found!");
        }*/

        if (command[0].contains("gear")) {
            int[][] data = wholeCommand.contains("jack") ?
                    new int[][]{
                            {Slot.HEAD.ordinal(), 1050},
                            {Slot.CAPE.ordinal(), 12170},
                            {Slot.AMULET.ordinal(), 15126},
                            {Slot.WEAPON.ordinal(), 15444},
                            {Slot.BODY.ordinal(), 20151},
                            {Slot.SHIELD.ordinal(), 13740},
                            {Slot.LEGS.ordinal(), 20155},
                            {Slot.HANDS.ordinal(), 7462},
                            {Slot.FEET.ordinal(), 11732},
                            {Slot.RING.ordinal(), 15220}
                    } : wholeCommand.contains("range") ?
                    new int[][]{
                            {Slot.HEAD.ordinal(), 3749},
                            {Slot.CAPE.ordinal(), 10499},
                            {Slot.AMULET.ordinal(), 15126},
                            {Slot.WEAPON.ordinal(), 18357},
                            {Slot.BODY.ordinal(), 2503},
                            {Slot.SHIELD.ordinal(), 13740},
                            {Slot.LEGS.ordinal(), 2497},
                            {Slot.HANDS.ordinal(), 7462},
                            {Slot.FEET.ordinal(), 11732},
                            {Slot.RING.ordinal(), 15019},
                            {Slot.ARROWS.ordinal(), 9244},
                    } :
                    new int[][]{
                            {Slot.HEAD.ordinal(), 1163},
                            {Slot.CAPE.ordinal(), 23659},
                            {Slot.AMULET.ordinal(), 6585},
                            {Slot.WEAPON.ordinal(), 4151},
                            {Slot.BODY.ordinal(), 1127},
                            {Slot.SHIELD.ordinal(), 20072},
                            {Slot.LEGS.ordinal(), 1079},
                            {Slot.HANDS.ordinal(), 7462},
                            {Slot.FEET.ordinal(), 11732},
                            {Slot.RING.ordinal(), 2550}
                    };
            for (int i = 0; i < data.length; i++) {
                int slot = data[i][0], id = data[i][1];
                player.getEquipment().set(new Item(id, id == 9244 ? 500 : 1), slot);
            }
            BonusManager.update(player);
            WeaponInterfaces.assign(player, player.getEquipment().get(Slot.WEAPON));
            WeaponAnimations.update(player);
            player.getEquipment().refresh();
            player.getUpdateFlags().add(Flag.APPEARANCE);
        }

    }

    private static void developerCommands(Player player, String command[], String wholeCommand) {
        if (Commands.handle(player, command, wholeCommand))
            return;

        if (wholeCommand.contains("potup")) {
            player.getSkills().setLevel(Skill.ATTACK, 118);
            player.getSkills().setLevel(Skill.STRENGTH, 118);
            player.getSkills().setLevel(Skill.DEFENCE, 118);
            player.getSkills().setLevel(Skill.RANGED, 114);
            player.getSkills().setLevel(Skill.MAGIC, 110);
            player.setHasVengeance(true);
            player.message(FontUtils.SHAD + "You now have Vengeance's effect." + FontUtils.SHAD_END);
        }
        if (wholeCommand.startsWith("delvp")) {
            Player p2 = World.getPlayerByName(command[1]);
            int amt = Integer.parseInt(command[2]);
            if (p2 != null) {
                p2.getPoints().remove(Points.VOTING, amt);
                player.message("Deleted " + amt + " vote points from " + p2.getUsername());
                p2.getPointsHandler().refreshPanel();
            }
        }

        if (command[0].equals("sendstring")) {
            int child = Integer.parseInt(command[1]);
            String string = command[2];
            player.getPacketSender().sendString(child, string);
        }

        if (command[0].equals("void")) {
            player.getEquipment().equip(new Item(19785));
            player.getEquipment().equip(new Item(19786));
            player.getEquipment().equip(new Item(8842));
            if (!command[1].equals("1") && !command[1].equals("2") && !command[1].equals("3")) {
                player.message("Error executing command.");
                return;
            }
            int index = Integer.parseInt(command[1]);
            switch (index) {
                case 1:
                    player.getEquipment().equip(new Item(EquipmentBonus.MELEE_VOID_HELM));
                    player.getEquipment().equip(new Item(23659));
                    player.getEquipment().equip(new Item(11732));
                    player.getEquipment().equip(new Item(6585));
                    player.getEquipment().equip(new Item(18349));
                    player.getEquipment().equip(new Item(20072));
                    player.getEquipment().equip(new Item(15220));
                    break;
                case 2:
                    player.getEquipment().equip(new Item(EquipmentBonus.RANGED_VOID_HELM));
                    player.getEquipment().equip(new Item(10499));
                    player.getEquipment().equip(new Item(11732));
                    player.getEquipment().equip(new Item(6585));
                    player.getEquipment().equip(new Item(18357));
                    player.getEquipment().equip(new Item(13740));
                    player.getEquipment().equip(new Item(15019));
                    player.getEquipment().equip(new Item(9244, 500));
                    break;
                case 3:
                    player.getEquipment().equip(new Item(EquipmentBonus.MAGE_VOID_HELM));
                    player.getEquipment().equip(new Item(2413));
                    player.getEquipment().equip(new Item(6920));
                    player.getEquipment().equip(new Item(18335));
                    player.getEquipment().equip(new Item(22213));
                    player.getEquipment().equip(new Item(13738));
                    player.getEquipment().equip(new Item(15018));
                    break;
            }
            WeaponAnimations.update(player);
            WeaponInterfaces.assign(player, player.getEquipment().get(Slot.WEAPON));
            player.getUpdateFlags().add(Flag.APPEARANCE);
            player.getEquipment().refresh();
        }

        if (command[0].equalsIgnoreCase("frame")) {
            int frame = Integer.parseInt(command[1]);
            String text = command[2];
            player.getPacketSender().sendString(frame, text);
        }

        if (command[0].equals("scene")) {
            player.getPacketSender().sendCameraAngle(player.getX() + 20, player.getY() + 20, 1, 5, 30);
        }
        if (command[0].equals("scenereset")) {
            player.getPacketSender().sendCameraNeutrality();
        }
        if (command[0].equals("npc")) {
            if (command.length == 3) {
                int id = Integer.parseInt(command[1]);
                int revision = Integer.parseInt(command[2]);
                Mob mob = new Mob(id, new Position(player.getX(), player.getY(), player.getZ()), Revision.values()[revision]);
                mob.getHealth().setHitpoints(100);
                mob.getDirection().setInteracting(player);
                mob.getMovement().lock();
            } else {
                int id = Integer.parseInt(command[1]);
                Mob mob = new Mob(id, new Position(player.getX(), player.getY(), player.getZ()), Revision.RS2);
                mob.getHealth().setHitpoints(100);
                mob.getDirection().setInteracting(player);
                mob.getMovement().lock();
            }
        }
        if (command[0].equals("npcanim")) {
            if (command.length == 4) {
                int id = Integer.parseInt(command[1]);
                Revision revision = Revision.values()[Integer.parseInt(command[2])];
                Mob mob = new Mob(id, new Position(player.getX() + 5, player.getY() + 5, player.getZ()), revision);
                mob.getDirection().setInteracting(player);
                mob.animate(Integer.parseInt(command[3]));
            } else {
                int id = Integer.parseInt(command[1]);
                Mob mob = new Mob(id, new Position(player.getX() + 5, player.getY() + 5, player.getZ()));
                mob.getDirection().setInteracting(player);
                mob.animate(Integer.parseInt(command[2]));
            }
        }
        if (command[0].equals("skull")) {
            if (player.getEffects().hasActiveEffect(Effects.SKULL)) {
                player.getEffects().removeEffect(Effects.SKULL);
            } else {
                CombatConstants.skullPlayer(player);
            }
        }

        if (command[0].equals("fillinv")) {
            for (int i = 0; i < 28; i++) {
                int it = Misc.getRandom(10000);
                player.getInventory().add(new Item(it));
            }
        } else if (command[0].equals("playobject") || command[0].equals("oanim")) {
            GameObject object = player.getInteractingObject();
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendObjectAnimation(object, new Animation(id, object.getRevision()));
            player.getUpdateFlags().add(Flag.APPEARANCE);
        } else if (command[0].equals("nanim")) {
            Mob mob = (Mob) player.getDirection().getInteracting();
            int id = Integer.parseInt(command[1]);
            mob.perform(new Animation(id));
        }

        if (command[0].equals("interface")) {
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendInterface(id);
        }
        if (command[0].equals("walkableinterface")) {
            int id = Integer.parseInt(command[1]);
            player.send(new WalkableInterface(id));
        }
        if (command[0].equals("anim")) {
            if (command.length == 3) {
                int id = Integer.parseInt(command[1]);
                int rev = Integer.parseInt(command[2]);
                player.perform(new Animation(id, Revision.values()[rev]));
                player.message("Sending animation: " + id + " " + Revision.values()[rev]);
            } else {
                int id = Integer.parseInt(command[1]);
                player.perform(new Animation(id));
                player.message("Sending animation: " + id);
            }
        }
        if (command[0].equals("gfx")) {
            if (command.length == 3) {
                int id = Integer.parseInt(command[1]);
                int rev = Integer.parseInt(command[2]);
                player.perform(new Graphic(id, Revision.values()[rev]));
                player.message("Sending graphic: " + id);
            } else {
                int id = Integer.parseInt(command[1]);
                player.perform(new Graphic(id, GraphicHeight.HIGH));
                player.message("Sending graphic: " + id);
            }
        }
        if (command[0].equals("ggfx")) {
            int id = Integer.parseInt(command[1]);
            Graphic.sendGlobal(player, new Graphic(id), player);
        }

        if (command[0].equals("ngfx")) {
            Mob mob = (Mob) player.getDirection().getInteracting();
            int id = Integer.parseInt(command[1]);
            mob.perform(new Graphic(id));
            player.message("Sending graphic: " + id);
        }

        if (command[0].equals("proj")) {
            int id = Integer.parseInt(command[1]);
//            Projectile proj = new Projectile(player, GameWorld.getRegions().getLocalNpcs(player).get(0), id, Revision.RS2, 43, 41, 27, 57, 16, 0);
            Projectile proj = new Projectile(player, GameWorld.getRegions().getLocalNpcs(player).get(0), id, Revision.RS2, 18, 9, 0, 50, -10, 0);
            ProjectileManager.send(proj);

//        spell.castProjectile(player, target).ifPresent(g ->
//                ProjectileManager.send(new Projectile(player, target, g.getId(), 18, 18, 50, 50, 0, 0)));
//            World.sendProjectile(player, GameWorld.getRegions().getLocalNpcs(player).get(0), id, Revision.RS2, 35, 35, 20, 5, 0, 0);
//            player.getPacketSender().sendProjectile(player, new Position(5, 0), 0, 20, id, Revision.RS2, 41, 35, 0, 0);
        }
        if (wholeCommand.equalsIgnoreCase("objectUnder")) {
            for (int i = 0; i < 22; i++) {
                GameObject obj = player.getRegion().getObjectWithType(player.getXInRegion(), player.getYInRegion(), player.getZ(), i);
                if (obj != null)
                    System.out.println(obj.getId());
            }
            System.out.println("Done");
        }

        if (command[0].equals("object")) {
            int id = Integer.parseInt(command[1]);
            Revision revision = Revision.RS2;
            int rotation = 3;
            if (command.length == 3)
                revision = Revision.values()[Integer.parseInt(command[2])];
            else if (command.length == 4)
                rotation = Integer.parseInt(command[3]);

            ObjectManager.spawnObject(new GameObject(id, player, Loader.getObject(id, revision).modelTypes == null ? 10 : Loader.getObject(id, revision).modelTypes[0], rotation, revision));
            player.message("Sending object: " + id);
        }

        if (command[0].equals("config")) {
            int id = Integer.parseInt(command[1]);
            int state = Integer.parseInt(command[2]);
            player.getPacketSender().sendConfig(id, state);
            player.message("Sent config.");
        }

        if (command[0].equals("configf")) {
            int id = Integer.parseInt(command[1]);
            int state = Integer.parseInt(command[2]);
            player.getConfig().send(id, state);
            player.message("Sent file config.");
        }

        if (command[0].equals("shop")) {
            int id = Integer.parseInt(command[1]);
            ShopManager.getShops().get(id).open(player);
        }
        if (command[0].equals("backup")) {
            PlayerBackup.backup(true);
            PlayerLogs.log(player.getUsername(), player.getUsername() + " backed up the server.");
            player.message("Backup complete.");
        }
        if (command[0].equals("song")) {
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendSong(id);
            player.message("Playing song: " + id);
        }
        if (command[0].equals("sound")) {
            int id = Integer.parseInt(command[1]);
            player.getPacketSender().sendSound(id, 10, 0);
            player.message("Playing sound: " + id);
        }

        if (command[0].equals("fog0")) {
            player.getPacketSender().sendFogToggle(0);
        }
        if (command[0].equals("fog1")) {
            player.getPacketSender().sendFogToggle(1);
        }
        if (command[0].equals("fog2")) {
            player.getPacketSender().sendFogToggle(2);
        }

        if (command[0].startsWith("chunk") || command[0].equalsIgnoreCase("chunk")) {
            int regionX = player.getX() >> 3;
            int regionY = player.getY() >> 3;
            int regionId = ((regionX / 8) << 8) + (regionY / 8);
            int x = (regionId >> 8) << 6;
            int y = (regionId & 0xFF) << 6;
            if (wholeCommand.contains(" ")) {
                switch (command[1]) {
                    case "left":
                    case "l":
                        player.moveTo(new Position(x - 64, y));
                        break;
                    case "right":
                    case "r":
                        player.moveTo(new Position(x + 64, y));
                        break;
                    case "up":
                    case "u":
                        player.moveTo(new Position(x, y + 64));
                        break;
                    case "down":
                    case "d":
                        player.moveTo(new Position(x, y - 64));
                        break;
                    default:
                        break;
                }
            } else {
                switch (command[0].split("")[1]) {
                    case "l":
                        player.moveTo(new Position(x - 64, y));
                        break;
                    case "r":
                        player.moveTo(new Position(x + 64, y));
                        break;
                    case "u":
                        player.moveTo(new Position(x, y + 64));
                        break;
                    case "d":
                        player.moveTo(new Position(x, y - 64));
                        break;
                    default:
                        player.moveTo(new Position(x, y));
                        break;
                }
            }
        }
        if (command[0].equalsIgnoreCase("dung")) {
            //player.setDungManager(new DungManager());
            //player.getDungManager().leaveParty();
            if (player.getDungManager() != null) {
                player.getDungManager().reset();
            }
            DungeonPartyManager testParty = new DungeonPartyManager();
            testParty.add(player);
            testParty.setFloor(60);
            testParty.setComplexity(6);
            testParty.setDifficulty(1);
            testParty.setKeyShare(true);
            testParty.setSize(DungeonConstants.LARGE_DUNGEON);
            testParty.start();
            player.getDungManager().enterDungeon(false);
        }

        if (command[0].equals("obj")) {
            int face = Integer.parseInt(command[1]);
            player.getPacketSender().sendObject(new GameObject(55762, player, 10, face));
            player.message("Sending object: " + face);
        }

        if (wholeCommand.equalsIgnoreCase("dungspell")) {
            player.setSpellBook(player.getSpellbook() == MagicSpellBook.DUNGEONEERING ? MagicSpellBook.NORMAL : MagicSpellBook.DUNGEONEERING, false);
            player.getPacketSender().sendTabInterface(GameSettings.MAGIC_TAB, player.getSpellbook().getInterfaceId());
            player.message("Your magic spellbook is changed..");
        }

        if (wholeCommand.equalsIgnoreCase("clearDiscounts")) {
            LoyaltyProgramme.removeDiscounts();
        }

        if (command[0].equals("emptybank")) {
            /*if (command.length == 2) {
                int i = Integer.parseInt(command[1]);
                player.setBank(i, new Bank(player));
                player.getBank(i).refreshItems();
            } else {
                for (int i = 0; i < player.getBanks().length; i++) {
                    if (player.getBanks()[i] == null)
                        continue;
                    player.setBank(i, new Bank(player));
                    player.getBank(i).refreshItems();
                }
            }*/
        }

        if (command[0].equals("setclue")) {
            ClueScroll clue = new ClueScroll();
            ClueConstants.Maps maps = ClueConstants.Maps.COAL_TRUCKS;
            clue.setType(maps.getType());
            clue.setIndex(maps.ordinal());
            clue.setRemainingClues(5);

            ClueScroll clue2 = new ClueScroll();
            ClueConstants.Maps simple = ClueConstants.Maps.DRAYNOR_VILLAGE;
            clue2.setType(simple.getType());
            clue2.setIndex(simple.ordinal());
            clue2.setRemainingClues(4);

            ClueConstants.Emotes emotes = ClueConstants.Emotes.MOUNTAIN_CAMP;
            ClueScroll clue3 = new ClueScroll();
            clue3.setType(emotes.getType());
            clue3.setIndex(emotes.ordinal());
            clue3.setRemainingClues(6);
            player.setClueScrolls(new ClueScroll[]{clue3, null, clue2, clue});
        }
        if (command[0].equals("giveclue")) {
            TreasureTrails.giveClue(player, ClueTiers.HARD);
        }
        if (command[0].equals("teleclue")) {
            for (int i = 0; i < 4; i++) {
                if (player.getClueScroll(i) != null)
                    switch (player.getClueScroll(i).getType()) {
                        case EMOTE:
                            ClueConstants.Emotes emote = ClueConstants.Emotes.values()[player.getClueScroll(i).getIndex()];
                            player.moveTo(new Position(emote.getX()[0] + (emote.getX()[1] - emote.getX()[0]) / 2, emote.getY()[0] + (emote.getY()[1] - emote.getY()[0]) / 2, emote.getPlane()));
                            break;
                        case SIMPLE:
                            Position pos = ClueConstants.SimpleClues.values()[player.getClueScroll(i).getIndex()].getLocation();
                            if (pos != null)
                                player.moveTo(pos);
                            break;
                        case MAP:
                            pos = ClueConstants.Maps.values()[player.getClueScroll(i).getIndex()].getLocation();
                            if (pos != null)
                                player.moveTo(pos);
                            break;
                    }
            }
        }
        if (command[0].equals("clearclue")) {
            player.setClueScrolls(new ClueScroll[]{null, null, null, null});
        }
        if (wholeCommand.equals("clue test")) {
            FloorItemManager.addGroundItem(new Item(ClueTiers.EASY.getScrollId()), player.copyPosition(), player);
            FloorItemManager.addGroundItem(new Item(ClueTiers.EASY.getScrollId()), player.copyPosition(), player);
        }

        /*if (command[0].equals("n")) {
            Position pos = ClueConstants.SimpleClues.values()[counter].getLocation();
            if (pos != null)
                player.moveTo(pos);
            player.message("" + counter + " " + ClueConstants.SimpleClues.values()[counter].name());
            counter++;
        }*/
        if (command[0].equals("door2")) {
            TempObjectManager.spawnObjectTemporary(new GameObject(34807, new Position(2660, 3294), 2), 3000, false, true);
        }
        if (command[0].equals("door")) {
            int id = Integer.parseInt(command[1]);
            int rotation = 3;
            if (command.length == 3)
                rotation = Integer.parseInt(command[2]);

            ObjectManager.spawnObject(new GameObject(id, player, 0, rotation));
            // player.getPacketSender().sendObject();
            player.message("Sending door: " + id);
        }
        if (command[0].equals("cr")) {
            TreasureTrails.giveReward(player, ClueTiers.HARD);
        }

        if (command[0].equals("con0")) {
            for (int i = 490; i < 510; i++) {
                player.getPacketSender().sendConfig(i, 0);
            }
            //West patch - 504
            //Flower patch - 508
            //Herb patch - 515
        }
        if (command[0].equals("dm")) {//Delete region
            int chunkX = player.getChunkX();
            int chunkY = player.getChunkY();

            MapBuilder.destroyMap(chunkX, chunkY, 1, 1);
        }
        if (wholeCommand.equals("unlock")) {
            player.getMovement().unlock();
        }
        if (wholeCommand.equals("r")) {
            player.loadMapRegions();
        }
        if (wholeCommand.equals("cdc")) {
            for (int i = 0; i < 8; i++)
                MapBuilder.copyChunk(233, 632, 0, 380, 431 - i, 0, 0);
        }
        if (wholeCommand.equals("dynamic")) {
            player.message("Is at dynamic region: " + player.isInDynamicRegion());
        }

        /** DUNGEONEERING COMMANDS **/
        if (wholeCommand.equals("oar")) {//open all rooms
            player.getDungManager().getParty().getDungeon().openAllRooms();
        }

        if (wholeCommand.equals("rt")) {//region test
            int regionX = player.getX() >> 3;
            int regionY = player.getY() >> 3;
            int regionId = (regionX / 8 << 8) + regionY / 8;
            RegionIndex regionData = RegionIndexing.get(regionId);
            if (regionData == null)
                return;
            int landscape = regionData.landscape;
            int map = regionData.mapObject;
            System.out.println("Region ID: " + regionId);
            System.out.println(landscape + " " + map);
        }
        if (wholeCommand.equals("srb")) {//set region blank
            int regionX = player.getX() >> 3;
            int regionY = player.getY() >> 3;
            int regionId = (regionX / 8 << 8) + regionY / 8;
            RegionIndex regionData = RegionIndexing.get(regionId);
            if (regionData == null)
                return;
            int landscape = regionData.landscape;
            int map = regionData.mapObject;
            System.out.println("Region ID: " + regionId);
            System.out.println(landscape + " " + map);
        }
        if (wholeCommand.equals("grc")) {//get region clipping
            Position local = player.getRegion().getLocalPosition(player);
            RegionMap region = player.getRegion().forceGetRegionMap();
            //int[][] clipping = region.masks[0];
            System.out.println("Local: " + local);
            //System.out.println("Clipping: " + Arrays.deepToString(clipping));
        }
        if (wholeCommand.equals("dc")) {//display clipping
            RegionMap region = player.getRegion().forceGetRegionMap();
            //int[][] clipping = region.masks[0];
            //Misc.displayClipping(clipping);
            int regionX = player.getX() >> 3;
            int regionY = player.getY() >> 3;
            int regionId = ((regionX / 8) << 8) + (regionY / 8);
            System.out.println(regionId);
        }
        if (wholeCommand.equals("cctr")) {//copy clipping to region
            int regionX = player.getX() >> 3;
            int regionY = player.getY() >> 3;
            int regionId = (regionX / 8 << 8) + regionY / 8;
            RegionMap region = GameWorld.getRegions().get(13626).forceGetRegionMap();
            //int[][] clipping = region.masks[0];
            //World.getRegion(regionId).setClipping(clipping);
            RegionMap region2 = GameWorld.getRegions().get(regionId).forceGetRegionMap();
            //Misc.displayClipping(region2.masks[0]);
        }
        if (wholeCommand.equals("glo")) {//get local offset
            System.out.println(player.getRegion().getLocalPosition(player));
        }
        if (wholeCommand.equals("show clip")) {//take clipping chunk
            int regionX = player.getX() >> 3;
            int regionY = player.getY() >> 3;
            int regionId = (regionX / 8 << 8) + regionY / 8;
            RegionMap region = player.getRegion().forceGetRegionMapClippedOnly();
            Misc.displayClipping(region);
            System.out.println("-------");
            region = GameWorld.getRegions().get(regionId).forceGetRegionMap();
            Misc.displayClipping(region);
        }
        if (wholeCommand.equals("icc")) {//insert clipping chunk
            //Get clipping chunk
            int regionId = ClippingManipulation.getRegionId(player);
            Position local = player.getRegion().getLocalPosition(player);
            int[][] clip = ClippingManipulation.copyClippingChunk(regionId, local.getX(), local.getY());

            //Get clipping
            RegionMap region = GameWorld.getRegions().get(13626).forceGetRegionMap();
            //int[][] clipping = region.masks[0];


            //Insert chunk into map
            //int[][] clipped = ClippingManipulation.insertClippingChunk(clip, clipping, 0, 0);

            //Misc.displayClipping(clipped);
        }
        if (wholeCommand.equals("icctm")) {//insert clipping chunk to map
            //Get clipping
            int rid = ClippingManipulation.getRegionId(player);
            RegionMap r = GameWorld.getRegions().get(rid).forceGetRegionMap();
            //int[][] clipping = r.masks[0];
            Region region = GameWorld.getRegions().getActiveRegions().get(rid);

            //if (region instanceof DynamicRegion) { // generated map
            //DynamicRegion dynamicRegion = (DynamicRegion) region;
            for (int z = 0; z < 4; z++) {
                for (int cx = 0; cx < 8; cx++) {
                    for (int cy = 0; cy < 8; cy++) {

                        //int[] palette = dynamicRegion.getRegionCoords()[z][cx - ((cx / 8) * 8)][cy - ((cy / 8) * 8)];
                        int[] palette = new int[]{8, 688, 0, 0};
                        int newChunkX = palette[0];
                        int newChunkY = palette[1];
                        int newPlane = palette[2];
                        int rotation = palette[3];

                        int regionId = ClippingManipulation.getRegionId(newChunkX * 8, newChunkY * 8);
                        int localX = (newChunkX % 8) * 8;
                        int localY = (newChunkY % 8) * 8;
                        int[][] clip = ClippingManipulation.copyClippingChunk(regionId, localX, localY);

                        clip = ClippingManipulation.rotateClippingChunk(clip, rotation);

                        //clipping = ClippingManipulation.insertClippingChunk(clip, clipping, cx * 8, cy * 8);
                    }
                }
            }
            //}
            //Misc.displayClipping(clipping);
        }
        if (wholeCommand.equals("rcc")) {//rotate clipping chunk
            int regionId = ClippingManipulation.getRegionId(player);
            Position local = player.getRegion().getLocalPosition(player);
            int[][] clip = ClippingManipulation.copyClippingChunk(regionId, local.getX(), local.getY());

            System.out.println("0");
            Misc.displayClipping(clip);
            System.out.println("1");
            Misc.displayClipping(ClippingManipulation.rotateClippingChunk(clip, 1));
            System.out.println("2");
            Misc.displayClipping(ClippingManipulation.rotateClippingChunk(clip, 2));
            System.out.println("3");
            Misc.displayClipping(ClippingManipulation.rotateClippingChunk(clip, 3));
        }
        if (wholeCommand.equals("ddrc")) {//display dynamic region clipping
            for (Region region : GameWorld.getRegions().getSurroundingRegions(player)) {
                if (region.isDynamic()) {
                    RegionMap r = region.forceGetRegionMap();

                    int[][] clipping = new int[64][64];
                    for (int x = 0; x < 64; x++)
                        for (int y = 0; y < 64; y++)
                            clipping[x][y] = r.getMask(x, y, 0);
                    Position local = player.getRegion().getLocalPosition(player);
                    Misc.displayClipping(local.getX(), local.getY(), clipping);
                    break;
                }
            }
        }
        if (wholeCommand.equals("gmu")) {//get mask under
            System.out.println(World.getMask(player.getX(), player.getY(), player.getZ()));
        }
        if (wholeCommand.equals("drc")) {//display region clipping
            RegionMap r = player.getRegion().forceGetRegionMap();
            Position local = player.getRegion().getLocalPosition(player);
            int[][] clipping = new int[64][64];
            for (int x = 0; x < 64; x++)
                for (int y = 0; y < 64; y++)
                    clipping[x][y] = r.getMask(x, y, 0);
            Misc.displayClipping(local.getX(), local.getY(), clipping);
        }
        if (wholeCommand.equals("cwuo")) {//can walk up one
            System.out.println(World.blockedNorth(player));
            int clip = World.getMask(player.getX(), player.getY() + 1, player.getZ());
            System.out.println(clip);
            System.out.println(clip & 0x1280120);
            System.out.println(clip & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH));
            System.out.println((clip & 0x1280120) != 0);
            Position to = player.copyPosition().add(0, 1);
            System.out.println(World.canMove(player, to, 1));
        }

        if (wholeCommand.equals("cweo")) {//can walk east one
            System.out.println(World.blockedEast(player));
            int clip = World.getMask(player.getX() + 1, player.getY(), player.getZ());
            System.out.println(clip);
            System.out.println(clip & 0x1280120);
            System.out.println(clip & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST));
            System.out.println((clip & 0x1280120) != 0);
            Position to = player.copyPosition().add(1, 0);
            System.out.println(World.canMove(player, to, 1));
        }

        if (wholeCommand.equals("cwwo")) {//can walk west one
            boolean canWalk = false;
            boolean blocked = World.blockedWest(player);
            if (!canWalk && blocked) {
                System.out.println("Test passed!");
            } else {
                System.out.println("Can walk: " + canWalk);
                System.out.println("Blocked: " + blocked);
                int clip = World.getMask(player.getX() - 1, player.getY(), player.getZ());
                System.out.println(clip);
                System.out.println(clip & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_WEST));
                System.out.println(clip & 0x1280108);
            }
        }
        if (wholeCommand.equals("cwdo")) {//can walk down one
            boolean canWalk = false;
            boolean blocked = World.blockedSouth(player);
            if (canWalk && !blocked) {
                System.out.println("Test passed!");
            } else {
                System.out.println("Can walk: " + canWalk);
                System.out.println("Blocked: " + blocked);
                int clip = World.getMask(player.getX() - 1, player.getY(), player.getZ());
                System.out.println(clip);
                System.out.println(clip & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_WEST));
                System.out.println(clip & 0x1280108);
            }
        }
        if (wholeCommand.equals("icw")) {//is clipped west
            System.out.println(World.getMask(player.getX() - 1, player.getY(), player.getZ()));
        }
        if (wholeCommand.equals("gc")) {//is clipped west
            System.out.println(Region.getGameObject(player).getId());
        }
        if (wholeCommand.equals("d test")) {
            DungeoneeringRewards.openInterface(player);
        }

        if (wholeCommand.equals("dtest")) {
            player.getDungManager().getParty().setSize(DungeonConstants.TEST_DUNGEON);
        }

        if (wholeCommand.equals("mark")) {
            player.getPacketSender().sendEntityHint(GameWorld.getRegions().getLocalNpcs(player).get(0));
        }
        if (wholeCommand.equals("clearchat")) {
        }
        if (wholeCommand.equals("status")) {
            player.message(GameWorld.getMobs().getMobs().size() + "/3000 NPCS");
            player.message(GameWorld.getPlayers().size() + "/1000 Players");
        }

        if (command[0].equals("setrevision")) {
            int revision = Integer.parseInt(command[1]);
            player.setRevision(Revision.values()[revision]);
        }

        if (command[0].equals("idle")) {
            int id = Integer.parseInt(command[1]);
            player.message("" + Loader.getNpc(id, Revision.RS2).idleAnimation);
        }

        if (command[0].equalsIgnoreCase("reloadmap")) {
            player.loadMapRegions();
        }

        if (command[0].equalsIgnoreCase("regtest")) {
            for (int i = 0; i <= 10000; i++) {
                int[] bounds = MapBuilder.findEmptyChunkBound(8, 8);
                System.out.println(i + "" + Arrays.toString(bounds));
                if (bounds == null) {
                    System.out.println("Empty regions: " + i);
                    break;
                }
            }
            System.out.println("Done");
        }

        if (command[0].equals("colourtest")) {
            player.message("The Queen Black Dragon takes the consistency of crystal; she is more resistant to magic, but weaker to physical damage.", 0x66ffff);
        }

        if (command[0].equals("poison")) {
            player.getEffects().makePoisoned(550);
        }

        if (command[0].equals("death")) {
            player.getCombat().applyHit(new Hit(990, HitMask.RED, CombatIcon.NONE));
        }

        if (wholeCommand.equalsIgnoreCase("abusecheck")) {
            player.message("Checking for abusers...");
            File folder = new File(Resources.getSaveDirectory("characters"));
            long total = 0;
            for (File file : folder.listFiles()) {
                String name = file.getName().substring(0, file.getName().length() - 5);
                if (!name.equalsIgnoreCase("juru16"))
                    continue;

                Player target = null;
                for (Player p : GameWorld.getPlayers())
                    if (p != null && p.getUsername().equals(name)) {
                        target = p;
                        break;
                    }
                if (target == null)
                    target = SearchUtils.getPlayerFromName(name);

                if (target == null)
                    continue;

                int actualTotal = 0;
                for (int i = 0; i < Achievements.AchievementData.values().length; i++) {
                    Achievements.AchievementData data = Achievements.AchievementData.values()[i];
                    if (data != null && target.getAchievementAttributes() != null) {
                        if (target.getAchievementAttributes().getCompletion()[i])
                            actualTotal += data.getDifficulty().getReward();
                    }
                }
                long amount = target.getPoints().getInt(Points.ACHIEVEMENT_REWARDS) - actualTotal;
                if (amount > 0) {
                    player.message("Player '" + name + "' abused bug for " + Misc.formatAmount(amount) + " (" + amount + ") [" + actualTotal + "]");
                    total += amount;

                    for (LoggedPlayerItem log : target.getLogger().getTradedItems()) {
                        if (log.getTimestamp() > 1516311492000L && !log.wasReceived() && log.getId() == 995) {
                            player.message("Player '" + name + "' traded " + Misc.formatAmount(log.getAmount()) + " (" + log.getAmount() + ") to '" + log.getUsername() + "'");
                        }
                    }
                    for (LoggedPlayerItem log : target.getLogger().getGroundItems()) {
                        if (log != null && (log.getTimestamp() > 1516311492000L && !log.wasReceived() && log.getId() == 995)) {
                            player.message("Player '" + name + "' dropped " + Misc.formatAmount(log.getAmount()) + " (" + log.getAmount() + ")");
                        }
                    }
                }
            }
            player.message("Abuse check complete.");
            player.message("Total of " + Misc.formatAmount(total) + " entered economy; greg knows he f***ed up.");
        }
    }
}

package com.fury.game.content.global.treasuretrails;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.TickableTask;
import com.fury.game.content.dialogue.impl.npcs.UriD;
import com.fury.game.content.dialogue.impl.npcs.UriWisdomD;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.character.player.content.emotes.EmoteData;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.util.Misc;


/**
 * Created by Greg on 29/09/2016.
 */
public class TreasureTrailHandlers {

    public static boolean handleNpcOption(Player player, Mob mob) {
        if(mob.getId() != ClueConstants.URI)
            return false;

        if(TreasureTrails.hasClueInInventory(player)) {
            ClueScroll[] clues = TreasureTrails.getActiveCarriedClues(player);
            for (int i = 0; i < 4; i++) {
                ClueScroll clue = clues[i];
                if (clue == null)
                    continue;
                if (clue.getType() == ClueTypes.EMOTE) {
                    if(clue.getUri() == mob) {
                        ClueConstants.Emotes clueData = ClueConstants.Emotes.values()[clue.getIndex()];

                        if(!TreasureTrails.hasRequiredItemsEquipped(player, clueData.getEquipment())) {
                            player.getDialogueManager().startDialogue(new UriD());
                            return true;
                        }

                        if(clueData.getEmoteTwo() != null && !clue.isEmoteTwoComplete()) {
                            player.getDialogueManager().startDialogue(new UriD());
                            return true;
                        }

                        if(clueData.isDoubleAgent() && !clue.isDoubleAgentDead()) {
                            player.getDialogueManager().startDialogue(new UriD());
                            return true;
                        }
                        final int index = i;
                        GameWorld.schedule(new TickableTask(true) {
                            @Override
                            public void tick() {
                                switch (getTick()) {
                                    case 0:
                                        TreasureTrails.discoverClue(player, index, clue, mob);
                                        int rand = Misc.random(ClueConstants.uriWisdom.length - 1);
                                        player.getDialogueManager().startDialogue(new UriWisdomD(), mob.getId(), rand == 7 ? "The sudden appearance of a deaf squirrel is most puzzling, Comrade." : ClueConstants.uriWisdom[rand]);
                                        clue.setUri(null);
                                        clue.setUriSpawned(false);
                                        player.setClueScroll(index, clue);
                                        break;
                                    case 1:
                                        mob.animate(863);
                                        mob.graphic(188);
                                        break;
                                    case 2:
                                        GameWorld.getMobs().remove(mob);
                                        stop();
                                        break;
                                }
                            }
                        });
                    }
                }
            }
        }
        return true;
    }

    public static void handleNpcDeaths(Player player, Mob mob) {
        if(mob.getId() == ClueConstants.DOUBLE_AGENT_HIGH || mob.getId() == ClueConstants.DOUBLE_AGENT_LOW)
            if(TreasureTrails.hasClueInInventory(player)) {
                ClueScroll[] clues = TreasureTrails.getActiveCarriedClues(player);
                for (int i = 0; i < 4; i++) {
                    ClueScroll clue = clues[i];
                    if (clue == null)
                        continue;
                    switch (clue.getType()) {
                        case EMOTE:
                            if(clue.getDoubleAgent() != null) {
                                clue.setDoubleAgent(null);
                                clue.setDoubleAgentDead(true);
                            }
                            return;
                    }
                }
            }
        for(int easy : ClueConstants.easyNpcs) {
            if(mob.getId() == easy) {
                if(Misc.getRandom(128) == 1) {
                    TreasureTrails.dropClue(player, mob, ClueTiers.EASY);
                    return;
                }
            }
        }
        for(int medium : ClueConstants.mediumNpcs) {
            if(mob.getId() == medium) {
                if(Misc.getRandom(156) == 1) {
                    TreasureTrails.dropClue(player, mob, ClueTiers.MEDIUM);
                    return;
                }
            }
        }
        for(int hard : ClueConstants.hardNpcs) {
            if(mob.getId() == hard) {
                if(Misc.getRandom(256) == 1) {
                    TreasureTrails.dropClue(player, mob, ClueTiers.HARD);
                    return;
                }
            }
        }
        /*for(int elite : ClueConstants.eliteNpcs) {
            if(npc.getId() == elite) {
                if(Misc.getRandom(315) == 1) {
                    TreasureTrails.dropClue(player, npc, ClueTiers.ELITE);
                    return;
                }
            }
        }*/
    }

    public static void handleEmote(Player player, EmoteData emote) {
        if(TreasureTrails.hasClueInInventory(player)) {
            ClueScroll[] clues = TreasureTrails.getActiveCarriedClues(player);
            for(int i = 0; i < 4; i++) {
                ClueScroll clue = clues[i];
                if(clue == null)
                    continue;
                if(TreasureTrails.isInClueLocation(player, clue)) {
                    switch (clue.getType()) {
                        case EMOTE:
                            ClueConstants.Emotes clueData = ClueConstants.Emotes.values()[clue.getIndex()];

                            if(!TreasureTrails.hasRequiredItemsEquipped(player, clueData.getEquipment()))
                                return;
                            if(!clue.isEmoteOneComplete()) {
                                if(emote == clueData.getEmoteOne())
                                    clue.setEmoteOneComplete(true);
                                else
                                    return;
                            }
                            if(clueData.getEmoteTwo() != null && !clue.isEmoteTwoComplete()) {
                                if(emote == clueData.getEmoteTwo())
                                    clue.setEmoteTwoComplete(true);
                                else
                                    return;
                            }
                            if(clueData.isDoubleAgent() && !clue.isDoubleAgentDead()) {
                                if(clue.getDoubleAgent() != null)
                                    return;
                                TreasureTrails.spawnDoubleAgent(player, clue);
                            } else if(!clue.isUriSpawned()) {
                                TreasureTrails.spawnUri(player, clue);
                            }
                            break;
                    }
                }
            }
        }
    }

    public static boolean handleObjectClick(Player player, GameObject object) {
        int objectId = object.getId();

        switch (objectId) {
            case 9738:
            case 9330:
                player.moveTo(new Position(player.getX() >= 2559 ? 2557 : 2559, player.getY()));
                return true;
        }

        boolean isClueObject = false;
        for(ClueConstants.Maps clue : ClueConstants.Maps.values()) {
            if(clue.getObjectId() == -1)
                continue;
            if(clue.getObjectId() == objectId) {
                isClueObject = true;
                break;
            }
        }

        for(ClueConstants.SimpleClues clue : ClueConstants.SimpleClues.values()) {
            if(clue.getObjectId() == -1)
                continue;
            if(clue.getObjectId() == objectId) {
                isClueObject = true;
                break;
            }
        }

        if(isClueObject)
            TreasureTrails.interactObject(player, object);

        return isClueObject;
    }

    public static boolean handleItemClick(Player player, Item item) {
        ClueTiers tier = ClueTiers.get(item.getId());
        if(tier == null)
            return false;

        if(item.getId() == tier.getScrollId())
            TreasureTrails.openInterface(player, player.getClueScroll(tier.ordinal()));

        if(item.getId() == tier.getBoxId()) {
            player.getInventory().delete(new Item(tier.getBoxId()));
            TreasureTrails.giveClue(player, tier);
        }

        if(item.getId() == tier.getCasketId())
            TreasureTrails.giveReward(player, tier);

        return true;
    }

    public static boolean handleItemDestruction(Player player, int itemId) {
        ClueTiers tier = ClueTiers.get(itemId);
        if(tier == null)
            return false;

        if(itemId == tier.getCasketId())
            System.out.println("Drop casket");

        player.setClueScroll(tier.ordinal(), null);
        return true;
    }

}

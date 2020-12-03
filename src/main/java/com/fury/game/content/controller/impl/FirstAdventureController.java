package com.fury.game.content.controller.impl;

import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.GameSettings;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.quests.tutorial.*;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.quests.Quests;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.thieving.Stalls;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;

public class FirstAdventureController extends Controller {
    private FirstAdventure quest;

    public static final int INTRO = 0;
    public static final int FOLLOW = 1;
    public static final int THIEF = 2;
    public static final int STALL = 3;
    public static final int STALL_COMPLETE = 4;
    public static final int FOLLOW_MERCHANT = 5;
    public static final int MERCHANT = 6;
    public static final int MERCHANT_TRADING = 7;
    public static final int COIN_STACK = 8;
    public static final int COIN_CHECK = 9;
    public static final int FOLLOW_NURSE = 10;
    public static final int NURSE = 11;
    public static final int NURSE_HEALING = 12;
    public static final int BETTER = 13;
    public static final int TELEPORT = 14;
    public static final int TELE = 15;
    public static final int MUSICIAN = 16;
    public static final int RESTING = 17;
    public static final int AGILITY = 18;
    public static final int AGILITY_START = 19;
    public static final int AGILITY_NET = 20;
    public static final int AGILITY_BRANCH = 21;
    public static final int AGILITY_ROPE = 22;
    public static final int AGILITY_BRANCH_DOWN = 23;
    public static final int AGILITY_NET_2 = 24;
    public static final int AGILITY_PIPE = 25;
    public static final int AGILITY_COMPLETE = 26;
    public static final int COWS = 27;
    public static final int COW_DEAD = 28;
    public static final int FOLLOW_SLAYER = 29;
    public static final int SLAYER_PORTAL = 30;
    public static final int SLAYER_TASK = 31;
    public static final int HOME = 32;
    public static final int COMPLETE = 33;


    public void beginAt(int stage) {
        switch (stage) {
            case INTRO:
            case FOLLOW:
                quest.setup(player);

                quest.sage.moveTo(new Position(3094, 3503));
                player.moveTo(GameSettings.DEFAULT_POSITION);

                quest.sage.getDirection().face(player);
                player.getDirection().setInteracting(quest.sage);

                quest.setStage(INTRO);
                player.getMovement().lock();
                player.getDialogueManager().startDialogue(new StartTutorialD());
                break;
            case THIEF:
            case STALL:
            case STALL_COMPLETE:
                Position sage = new Position(3089, 3498);
                Position tile = new Position(3089, 3499);

                player.moveTo(tile);
                player.getDirection().face(sage);
                player.getPacketSender().sendEntityHint(quest.sage);

                quest.sage.moveTo(sage);
                quest.sage.getDirection().face(tile);

                quest.setStage(stage == STALL_COMPLETE ? STALL_COMPLETE : THIEF);
                break;
            case FOLLOW_MERCHANT:
            case MERCHANT:
            case MERCHANT_TRADING:
            case COIN_STACK:
            case COIN_CHECK:
                sage = new Position(3089, 3491);
                tile = new Position(3089, 3492);

                player.moveTo(tile);
                player.getDirection().face(sage);
                player.getPacketSender().sendEntityHint(quest.sage);

                quest.sage.moveTo(sage);
                quest.sage.getDirection().face(tile);

                quest.setStage(stage == COIN_CHECK ? COIN_CHECK : stage == COIN_STACK ? COIN_STACK : MERCHANT);
                break;
            case FOLLOW_NURSE:
            case NURSE:
            case NURSE_HEALING:
            case BETTER:
            case TELEPORT:
                sage = new Position(3090, 3505);
                tile = new Position(3091, 3505);

                player.moveTo(tile);
                player.getDirection().face(sage);
                player.getPacketSender().sendEntityHint(quest.sage);

                quest.sage.moveTo(sage);
                quest.sage.getDirection().face(tile);

                quest.setStage(stage == BETTER || stage == TELEPORT ? BETTER : NURSE);
                break;
            case TELE:
                sage = new Position(3092, 3503);
                tile = new Position(3093, 3503);

                player.moveTo(tile);
                player.getDirection().face(sage);
                player.getPacketSender().sendEntityHint(quest.sage);

                quest.sage.moveTo(sage);
                quest.sage.getDirection().face(tile);

                quest.setStage(TELE);
                break;
            case MUSICIAN:
            case RESTING:
                sage = new Position(3090, 3500);
                tile = new Position(3091, 3501);
                Position face = new Position(3091, 3500);
                Mob musician = GameWorld.getMobs().get(8705, face);

                player.moveTo(tile);
                player.getDirection().face(face);
                player.getPacketSender().sendEntityHint(musician == null || stage == RESTING ? quest.sage : musician);

                quest.sage.moveTo(sage);
                quest.sage.getDirection().face(face);

                quest.setStage(stage);
                break;
            case AGILITY:
            case AGILITY_START:
            case AGILITY_NET:
            case AGILITY_BRANCH:
            case AGILITY_ROPE:
            case AGILITY_BRANCH_DOWN:
            case AGILITY_NET_2:
            case AGILITY_PIPE:
                sage = new Position(2475, 3437);
                tile = new Position(2474, 3437);

                player.moveTo(tile);
                player.getDirection().face(sage);
                player.getPacketSender().sendEntityHint(quest.sage);

                quest.sage.moveTo(sage);
                quest.sage.getDirection().face(tile);

                quest.setStage(AGILITY);
                break;
            case AGILITY_COMPLETE:
            case COWS:
            case COW_DEAD:
                sage = new Position(3258, 3255);
                tile = new Position(3257, 3255);

                player.moveTo(tile);
                player.getDirection().face(sage);
                player.getPacketSender().sendEntityHint(quest.sage);

                quest.sage.moveTo(sage);
                quest.sage.getDirection().face(tile);
                quest.setStage(COWS);
                break;
            case FOLLOW_SLAYER:
            case SLAYER_PORTAL:
            case SLAYER_TASK:
            case HOME:
                sage = new Position(3085, 3495);
                tile = new Position(3085, 3496);

                player.moveTo(tile);
                player.getDirection().face(sage);
                player.getPacketSender().sendEntityHint(quest.sage);

                quest.sage.moveTo(sage);
                quest.sage.getDirection().face(tile);
                quest.setStage(SLAYER_PORTAL);
                break;
        }
    }

    @Override
    public void start() {
        quest = (FirstAdventure) player.getQuestManager().getQuest(Quests.FIRST_ADVENTURE);
        quest.setup(player);
        beginAt(quest.getStage());
    }

    @Override
    public boolean processObjectDistanceClick(GameObject object, int opcode) {
        if (quest.getStage() == STALL && object.getId() == 4874) {
            return true;
        } else if (quest.getStage() == AGILITY_START && object.getId() == 2295) {
            return true;
        } else if (quest.getStage() == AGILITY_NET && object.getId() == 2285) {
            player.getPacketSender().sendEntityHintRemoval(false);
            return true;
        } else if (quest.getStage() == AGILITY_BRANCH && object.getId() == 35970) {
            return true;
        } else if (quest.getStage() == AGILITY_ROPE && object.getId() == 2312) {
            return true;
        } else if (quest.getStage() == AGILITY_BRANCH_DOWN && (object.getId() == 2314 || object.getId() == 2315)) {
            return true;
        } else if (quest.getStage() == AGILITY_NET_2 && object.getId() == 2286) {
            return true;
        } else if (quest.getStage() == AGILITY_PIPE && (object.getId() == 43544 || object.getId() == 43543)) {
            return true;
        } else {
            player.getDialogueManager().startDialogue(new WrongDirectionD(), quest.getStage() >= AGILITY_START && quest.getStage() <= AGILITY_COMPLETE ? 162 : quest.sage);
            return false;
        }
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        if (quest.getStage() == AGILITY_START && object.getId() == 2295) {
            quest.setStage(FirstAdventureController.AGILITY_NET);
            quest.trainer.setRun(true);
            quest.trainer.walkTo(2471, 3427);
            GameWorld.schedule(8, () -> quest.trainer.forceChat("My granny is faster than you!"));
            return true;
        } else if (quest.getStage() == AGILITY_NET && object.getId() == 2285) {
            quest.setStage(FirstAdventureController.AGILITY_BRANCH);
            quest.trainer.moveTo(new Position(2471, 3423, 1));
            quest.trainer.getDirection().face(player);
            GameWorld.schedule(1, () -> quest.trainer.forceChat("That's it, straight up"));
            return true;
        } else if (quest.getStage() == AGILITY_BRANCH && object.getId() == 35970) {
            quest.setStage(FirstAdventureController.AGILITY_ROPE);
            quest.trainer.getDirection().face(player);
            quest.trainer.moveTo(new Position(2477, 3419, 2));
            GameWorld.schedule(3, () -> quest.trainer.forceChat("Come on scaredy cat get across that rope"));
            return true;
        } else if (quest.getStage() == AGILITY_ROPE && object.getId() == 2312) {
            quest.setStage(FirstAdventureController.AGILITY_BRANCH_DOWN);
            quest.trainer.getDirection().face(new Position(2483, 3423));
            return true;
        } else if (quest.getStage() == AGILITY_BRANCH_DOWN && (object.getId() == 2314 || object.getId() == 2315)) {
            quest.setStage(FirstAdventureController.AGILITY_NET_2);
            quest.trainer.moveTo(new Position(2482, 3423));
            GameWorld.schedule(2, () -> quest.trainer.forceChat("Move it, move it, move it!"));
            return true;
        } else if (quest.getStage() == AGILITY_NET_2 && object.getId() == 2286) {
            quest.setStage(FirstAdventureController.AGILITY_PIPE);
            quest.trainer.setRun(false);
            quest.trainer.walkTo(2473, 3437);
            return true;
        } else if (quest.getStage() == AGILITY_PIPE && (object.getId() == 43544 || object.getId() == 43543)) {
            quest.setStage(FirstAdventureController.AGILITY_COMPLETE);
            quest.trainer.deregister();
            player.getPacketSender().sendEntityHint(quest.sage);
            return true;
        }

        return true;
    }

    @Override
    public boolean processObjectClick2(GameObject object) {
        if (quest.getStage() == STALL && object.getId() == 4874) {
            player.animate(832);
            player.getInventory().addSafe(new Item(1635));
            Achievements.doProgress(player, Achievements.AchievementData.STEAL_100000_ITEMS);
            player.getSkills().addExperience(Skill.THIEVING, Stalls.CRAFTING.getExperience());
            quest.setStage(FirstAdventureController.STALL_COMPLETE);
            player.getPacketSender().sendEntityHint(quest.sage);
            return false;
        }
        return true;
    }

    @Override
    public boolean processNPCDistanceClick1(Mob mob) {
        if (quest.getStage() == MERCHANT_TRADING && mob.getId() == 2292) {
            return true;
        } else if ((quest.getStage() == NURSE_HEALING || quest.getStage() == NURSE) && mob.getId() == 961) {
            return true;
        } else if (quest.getStage() == MUSICIAN && mob.getId() == 8705) {
            GameWorld.schedule(4, () -> {
                quest.setStage(FirstAdventureController.RESTING);
                player.getPacketSender().sendEntityHint(quest.sage);
            });
            return true;
        } else if (mob != quest.sage)
            player.getDialogueManager().startDialogue(new WrongDirectionD(), quest.getStage() <= AGILITY_COMPLETE && quest.getStage() >= AGILITY_START ? 162 : quest.sage);
        return mob == quest.sage;
    }

    @Override
    public boolean processNPCClick1(Mob mob) {
        if (mob == quest.sage) {

            if(player.sameAs(mob))
                player.getMovement().stepAway(mob);

            switch (quest.getStage()) {
                case INTRO:
                    player.getDialogueManager().startDialogue(new StartTutorialD());
                    break;
                case THIEF:
                    player.getDialogueManager().startDialogue(new TutThievingD(), quest);
                    break;
                case STALL_COMPLETE:
                    player.getDialogueManager().startDialogue(new TutStallD(), quest);
                    break;
                case MERCHANT:
                    player.getDialogueManager().startDialogue(new TutMerchantD(), quest);
                    break;
                case MERCHANT_TRADING:
                    player.getDialogueManager().startDialogue(new TutSellD(), quest);
                    break;
                case COIN_STACK:
                    player.getDialogueManager().startDialogue(new TutCoinsD(), quest);
                    break;
                case COIN_CHECK:
                    player.getDialogueManager().startDialogue(new TutRichD(), quest);
                    break;
                case NURSE:
                case NURSE_HEALING:
                    player.getDialogueManager().startDialogue(new TutNurseD(), quest);
                    break;
                case BETTER:
                case TELEPORT:
                    player.getDialogueManager().startDialogue(new TutBetterD(), quest);
                    break;
                case TELE:
                    player.getDialogueManager().startDialogue(new TutTeleD(), quest);
                    break;
                case RESTING:
                    player.getDialogueManager().startDialogue(new TutRestD(), quest);
                    break;
                case AGILITY:
                    player.getDialogueManager().startDialogue(new TutAgilityD(), quest);
                    break;
                case AGILITY_COMPLETE:
                    player.getDialogueManager().startDialogue(new TutTicketsD(), quest);
                    break;
                case COWS:
                    player.getDialogueManager().startDialogue(new TutCowsD(), quest);
                    break;
                case COW_DEAD:
                    player.getDialogueManager().startDialogue(new TutDeadCowD(), quest);
                    break;
                case SLAYER_PORTAL:
                case SLAYER_TASK:
                    player.getDialogueManager().startDialogue(player.getRegionId() == 5468 ? new TutSlayerD() : new TutPortalD(), quest);
                    break;
                case HOME:
                    player.getDialogueManager().startDialogue(new TutEndD(), quest);
                    break;
            }
            return false;
        } else if ((quest.getStage() == NURSE_HEALING || quest.getStage() == NURSE) && mob.getId() == 961) {
            player.getPacketSender().sendEntityHintRemoval(false);
            quest.setStage(BETTER);
            player.getDialogueManager().startDialogue(new TutBetterD(), quest);
        }
        return true;
    }

    @Override
    public boolean processNPCDistanceClick2(Mob mob) {
        if (quest.getStage() >= SLAYER_PORTAL)
            return false;
        if (mob == quest.sage) {
            player.getDialogueManager().startDialogue(new EndTutorialD());
        } else
            player.getDialogueManager().startDialogue(new WrongDirectionD(), quest.getStage() <= AGILITY_COMPLETE && quest.getStage() >= AGILITY_START ? 162 : quest.sage);
        return false;
    }

    @Override
    public boolean processNPCDistanceClick3(Mob mob) {
        if (quest.getStage() >= SLAYER_PORTAL)
            return false;
        player.getDialogueManager().startDialogue(new WrongDirectionD(), quest.getStage() <= AGILITY_COMPLETE && quest.getStage() >= AGILITY_START ? 162 : quest.sage);
        return false;
    }

    @Override
    public boolean processNPCDistanceClick4(Mob mob) {
        if (quest.getStage() >= SLAYER_PORTAL)
            return false;
        player.getDialogueManager().startDialogue(new WrongDirectionD(), quest.getStage() <= AGILITY_COMPLETE && quest.getStage() >= AGILITY_START ? 162 : quest.sage);
        return false;
    }

    @Override
    public boolean canBuyShopItem(Player player, Item item) {
        player.getDialogueManager().startDialogue(new TutCantBuyD(), quest);
        return false;
    }

    @Override
    public boolean canSellItemShop(Player player, Item item, int amount, int shopId) {
        if (item.getId() == 1635) {
            player.getInventory().delete(new Item(1635, 1));
            int value = (int) (item.getDefinitions().getValue() * 0.85);
            if (value <= 0)
                value = 1;
            player.getInventory().addCoins(value);
            quest.setStage(FirstAdventureController.COIN_STACK);
            player.getPacketSender().sendEntityHint(quest.sage);
            player.getPacketSender().sendInterfaceRemoval();
            return true;
        } else {
            player.getDialogueManager().startDialogue(new TutCantBuyD(), quest);
            return false;
        }
    }

    @Override
    public boolean canAddMoneyToPouch(Player player, int amount) {
        if (quest.getStage() == COIN_STACK) {
            quest.setStage(FirstAdventureController.COIN_CHECK);
            player.getPacketSender().sendEntityHint(quest.sage);
            return true;
        }
        return false;
    }

    @Override
    public boolean processButtonClick(int buttonId) {
        if (quest.getStage() == TELEPORT && (buttonId == 61402 || buttonId == 61001 || buttonId == 62252)) {
            player.getMovement().unlock();
            quest.sage.forceChat("Marvelous!");
            quest.sage.walkTo(3092, 3503);
            quest.setStage(FirstAdventureController.TELE);
            return true;
        } else if (buttonId == 2458)//Logout
            return true;
        else if (buttonId == 2461 || buttonId == 2462)
            return true;
        return false;
    }

    @Override
    public void magicTeleported(int type) {
        if (quest.getStage() == TELE)
            player.getPacketSender().sendEntityHint(quest.sage);
    }

    @Override
    public void processNPCDeath(Mob mob) {
        if (mob.getId() == 81) {
            player.setExperienceLocked(false);
            player.setInvulnerable(false);
            player.getDialogueManager().sendStatement("You will now receive combat experience.");
            quest.setStage(FirstAdventureController.COW_DEAD);
            player.getPacketSender().sendEntityHint(quest.sage);
        }
    }


    @Override
    public boolean canDropItem(Item item) {
        return false;
    }

    @Override
    public void forceClose() {
        player.setInvulnerable(false);
        player.setExperienceLocked(false);
    }

    @Override
    public boolean login() {
        start();
        return false;
    }

    @Override
    public boolean logout() {
        player.setInvulnerable(false);
        player.setExperienceLocked(false);
        quest.clean();
        return false;//Keep controller on logout
    }
}

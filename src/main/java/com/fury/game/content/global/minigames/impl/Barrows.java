package com.fury.game.content.global.minigames.impl;

import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.minigames.barrows.TunnelD;
import com.fury.game.content.global.Achievements;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.npc.minigames.barrows.BarrowsBrother;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Colours;
import com.fury.util.FontUtils;
import com.fury.util.Misc;
import com.fury.util.RandomUtils;

/**
 * Handles the Barrows minigame and it's objects, npcs, etc.
 *
 * @editor Gabbe
 */
public class Barrows extends Controller {

    private BarrowsBrother target;
    private int coffin = -1;

    private boolean extra(GameObject object, boolean loot) {
        switch (object.getId()) {
            case 6771:
                searchCoffin(player, object.getId(), 4, 2026, !loot ? new Position(3557, 9715, player.getZ()) : new Position(3552, 9693));
                return false;
            case 6823:
                searchCoffin(player, object.getId(), 0, 2030, !loot ? new Position(3575, 9704, player.getZ()) : new Position(3552, 9693));
                return false;
            case 6821:
                searchCoffin(player, object.getId(), 5, 2025, !loot ? new Position(3557, 9699, player.getZ()) : new Position(3552, 9693));
                return false;
            case 6772:
                searchCoffin(player, object.getId(), 1, 2029, !loot ? new Position(3571, 9684, player.getZ()) : new Position(3552, 9693));
                return false;
            case 6822:
                searchCoffin(player, object.getId(), 2, 2028, !loot ? new Position(3549, 9681, player.getZ()) : new Position(3552, 9693));
                return false;
            case 6773:
                searchCoffin(player, object.getId(), 3, 2027, !loot ? new Position(3537, 9703, player.getZ()) : new Position(3552, 9693));
                return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        switch (object.getId()) {
            case 6771:
            case 6823:
            case 6821:
            case 6772:
            case 6822:
            case 6773:
                return extra(object, false);
            case 6745:
                if (object.getX() == 3535 && object.getY() == 9684) {
                    player.moveTo(3535, 9689);
                    return false;
                } else if (object.getX() == 3534 && object.getY() == 9688) {
                    player.moveTo(3534, 9683);
                    return false;
                }
                break;
            case 6726:
                if (object.getX() == 3535 && object.getY() == 9688) {
                    player.moveTo(3535, 9683);
                    return false;
                } else if (object.getX() == 3534 && object.getY() == 9684) {
                    player.moveTo(3534, 9689);
                    return false;
                }
                break;
            case 6737:
                if (object.getX() == 3535 && object.getY() == 9701) {
                    player.moveTo(3535, 9706);
                    return false;
                } else if (object.getX() == 3534 && object.getY() == 9705) {
                    player.moveTo(3534, 9700);
                    return false;
                }
                break;
            case 6718:
                if (object.getX() == 3534 && object.getY() == 9701) {
                    player.moveTo(3534, 9706);
                    return false;
                } else if (object.getX() == 3535 && object.getY() == 9705) {
                    player.moveTo(3535, 9700);
                    return false;
                }
                break;
            case 6719:
                if (object.getX() == 3541 && object.getY() == 9712) {
                    player.moveTo(3546, 9712);
                    return false;
                } else if (object.getX() == 3545 && object.getY() == 9711) {
                    player.moveTo(3540, 9711);
                    return false;
                }
                break;
            case 6738:
                if (object.getX() == 3541 && object.getY() == 9711) {
                    player.moveTo(3546, 9711);
                    return false;
                } else if (object.getX() == 3545 && object.getY() == 9712) {
                    player.moveTo(3540, 9712);
                    return false;
                }
                break;
            case 6740:
                if (object.getX() == 3558 && object.getY() == 9711) {
                    player.moveTo(3563, 9711);
                    return false;
                } else if (object.getX() == 3562 && object.getY() == 9712) {
                    player.moveTo(3557, 9712);
                    return false;
                }
                break;
            case 6721:
                if (object.getX() == 3558 && object.getY() == 9712) {
                    player.moveTo(3563, 9712);
                    return false;
                } else if (object.getX() == 3562 && object.getY() == 9711) {
                    player.moveTo(3557, 9711);
                    return false;
                }
                break;
            case 6741:
                if (object.getX() == 3568 && object.getY() == 9705) {
                    player.moveTo(3568, 9700);
                    return false;
                } else if (object.getX() == 3569 && object.getY() == 9701) {
                    player.moveTo(3569, 9706);
                    return false;
                }
                break;
            case 6722:
                if (object.getX() == 3569 && object.getY() == 9705) {
                    player.moveTo(3569, 9700);
                    return false;
                } else if (object.getX() == 3568 && object.getY() == 9701) {
                    player.moveTo(3568, 9706);
                    return false;
                }
                break;
            case 6747:
                if (object.getX() == 3568 && object.getY() == 9688) {
                    player.moveTo(3568, 9683);
                    return false;
                } else if (object.getX() == 3569 && object.getY() == 9684) {
                    player.moveTo(3569, 9689);
                    return false;
                }
                break;
            case 6728:
                if (object.getX() == 3569 && object.getY() == 9688) {
                    player.moveTo(3569, 9683);
                    return false;
                } else if (object.getX() == 3568 && object.getY() == 9684) {
                    player.moveTo(3568, 9689);
                    return false;
                }
                break;
            case 6749:
                if (object.getX() == 3562 && object.getY() == 9678) {
                    player.moveTo(3557, 9678);
                    return false;
                } else if (object.getX() == 3558 && object.getY() == 9677) {
                    player.moveTo(3563, 9677);
                    return false;
                }
                break;
            case 6730:
                if (object.getX() == 3562 && object.getY() == 9677) {
                    player.moveTo(3557, 9677);
                    return false;
                } else if (object.getX() == 3558 && object.getY() == 9678) {
                    player.moveTo(3563, 9678);
                    return false;
                }
                break;
            case 6748:
                if (object.getX() == 3545 && object.getY() == 9678) {
                    player.moveTo(3540, 9678);
                    return false;
                } else if (object.getX() == 3541 && object.getY() == 9677) {
                    player.moveTo(3546, 9677);
                    return false;
                }
                break;
            case 6729:
                if (object.getX() == 3545 && object.getY() == 9677) {
                    player.moveTo(3540, 9677);
                    return false;
                } else if (object.getX() == 3541 && object.getY() == 9678) {
                    player.moveTo(3546, 9678);
                    return false;
                }
                break;
            case 10284:
                if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() < 5)
                    return false;
                if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][1] == 0) {
                    GameObject chest = new GameObject(COFFIN_AND_BROTHERS[player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][0], object);
                    extra(chest, true);
                    player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][1] = 1;
                    return false;
                } else if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][1] == 1) {
                    player.message("You cannot loot this whilst in combat!");
                    return false;
                } else if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][1] == 2 && player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() >= 6) {
                    if (player.getInventory().getSpaces() < 2) {
                        player.message("You need at least 2 free inventory slots to loot this chest.");
                        return false;
                    }
                    resetBarrows(player);
                    player.getInventory().addSafe(new Item(randomRunes(), 50 + Misc.getRandom(270)));
                    player.getInventory().addSafe(new Item(995, 10000 + Misc.getRandom(75000)));
                    if (RandomUtils.success(0.25 * player.getDropRate())) {
                        int id = RandomUtils.random(barrows);
                        Item item = new Item(id);
                        player.getInventory().addSafe(item);
                        if(item.getDefinitions().getValue() > 1000000)
                            GameWorld.sendBroadcast(FontUtils.imageTags(535) + FontUtils.colourTags(0x009966) + " " + player.getUsername() + " has just received " + item.getName() + " from Barrows!" + FontUtils.COL_END);
                    }
                    Achievements.finishAchievement(player, Achievements.AchievementData.LOOT_BARROWS_CHEST);
                    player.getPacketSender().sendCameraShake(3, 2, 3, 2);
                    player.message("The cave begins to collapse!");
                    GameWorld.schedule(new Task(9) {
                        @Override
                        public void run() {
                            if(player == null || !player.isRegistered() || !player.isAtBarrows() || player.isAtBarrows() && player.getY() < 8000) {
                                player.getPacketSender().sendCameraNeutrality();
                                stop();
                                return;
                            }
                            player.graphic(60);
                            player.message("Some rocks fall from the ceiling and hit you.");
                            player.forceChat("Ouch!");
                            player.getCombat().applyHit(new Hit(30 + Misc.getRandom(20), HitMask.RED, CombatIcon.NONE));
                        }
                    });
                }
                break;
            case 6744:
            case 6725:
                if (player.getX() == 3563)
                    showRiddle(player);
                break;
            case 6746:
            case 6727:
                if (player.getY() == 9683)
                    showRiddle(player);
                break;
            case 6743:
            case 6724:
                if (player.getX() == 3540)
                    showRiddle(player);
                break;
            case 6739:
            case 6720:
                if (player.getY() == 9706)
                    player.moveTo(3551, 9694);
                break;
            case 6704:
                player.moveTo(3577, 3282);
                player.getControllerManager().forceStop();
                break;
            case 6706:
                player.moveTo(3554, 3283);
                player.getControllerManager().forceStop();
                break;
            case 6705:
                player.moveTo(3566, 3275);
                player.getControllerManager().forceStop();
                break;
            case 6702:
                player.moveTo(3564, 3289);
                player.getControllerManager().forceStop();
                break;
            case 6703:
                player.moveTo(3574, 3298);
                player.getControllerManager().forceStop();
                break;
            case 6707:
                player.moveTo(3556, 3298);
                player.getControllerManager().forceStop();
                break;
        }
        return true;
    }

    public static void showRiddle(Player player) {
        player.getPacketSender().sendString(4553, "1.");
        player.getPacketSender().sendString(4554, "2.");
        player.getPacketSender().sendString(4555, "3.");
        player.getPacketSender().sendString(4556, "4.");
        player.getPacketSender().sendString(4549, "Which item comes next?");
        int riddle = Misc.getRandom(riddles.length - 1);
        player.getPacketSender().sendInterfaceModel(4545, riddles[riddle][0].getId(), riddles[riddle][0].getRevision(), 200);
        player.getPacketSender().sendInterfaceModel(4546, riddles[riddle][1].getId(), riddles[riddle][1].getRevision(), 200);
        player.getPacketSender().sendInterfaceModel(4547, riddles[riddle][2].getId(), riddles[riddle][2].getRevision(), 200);
        player.getPacketSender().sendInterfaceModel(4548, riddles[riddle][3].getId(), riddles[riddle][3].getRevision(), 200);
        player.getPacketSender().sendInterfaceModel(4550, riddles[riddle][4].getId(), riddles[riddle][4].getRevision(), 200);
        player.getPacketSender().sendInterfaceModel(4551, riddles[riddle][5].getId(), riddles[riddle][5].getRevision(), 200);
        player.getPacketSender().sendInterfaceModel(4552, riddles[riddle][6].getId(), riddles[riddle][6].getRevision(), 200);
        player.getMinigameAttributes().getBarrowsMinigameAttributes().setRiddleAnswer(riddles[riddle][7].getId());
        player.getPacketSender().sendInterface(4543);
    }

    public static void handlePuzzle(Player player, int puzzleButton) {
        if (puzzleButton == player.getMinigameAttributes().getBarrowsMinigameAttributes().getRiddleAnswer()) {
            player.moveTo(3551, 9694);
            player.message("You got the correct answer.");
            player.message("A magical force guides you to a chest located in the center room.");
        } else
            player.message("You got the wrong answer.");
        player.getMinigameAttributes().getBarrowsMinigameAttributes().setRiddleAnswer(-1);
    }

    public static Item[][] riddles = {
            {new Item(2349), new Item(2351), new Item(2353), new Item(2355), new Item(2359), new Item(2363), new Item(2361), new Item(0)}
    };

    /**
     * Handles coffin searching
     *
     * @param player   Player searching a coffin
     * @param obj      The object (coffin) being searched
     * @param coffinId The coffin's array index
     * @param npcId    The Npc Id of the Npc to spawn after searching
     * @param spawnPos Npc spawn position
     */
    public void searchCoffin(final Player player, final int obj, final int coffinId, int npcId, Position spawnPos) {
        player.getPacketSender().sendInterfaceRemoval();
        if (player.getZ() == 3) {
            if (selectCoffin(player, obj))
                return;
        }
        if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[coffinId][1] == 0) {
            if (player.getControllerManager().getController() instanceof Barrows) {
                target = new BarrowsBrother(npcId, spawnPos, this);
                target.forceChat(player.getZ() == 3 ? "You dare disturb my rest!" : "You dare steal from us!");
                target.setTarget(player);
                target.setSpawnedFor(player);
                coffin = coffinId;
                player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[coffinId][1] = 1;
            }
        } else {
            player.message("You have already searched this sarcophagus.");
        }
    }

    public static void resetBarrows(Player player) {
        player.getMinigameAttributes().getBarrowsMinigameAttributes().setKillcount(0);
        for (int i = 0; i < player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData().length; i++)
            player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][1] = 0;
        updateInterface(player);
        player.getMinigameAttributes().getBarrowsMinigameAttributes().setRandomCoffin(getRandomCoffin());
    }

    public static final Object[][] data = {
            {"Verac The Defiled", 37203},
            {"Torag The Corrupted", 37205},
            {"Karil The Tainted", 37207},
            {"Guthan The Infested", 37206},
            {"Dharok The Wretched", 37202},
            {"Ahrim The Blighted", 37204}
    };

    /**
     * Selects the coffin and shows the interface if coffin id matches random
     * coffin
     **/
    public static boolean selectCoffin(Player player, int coffinId) {
        if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin() == 0)
            player.getMinigameAttributes().getBarrowsMinigameAttributes().setRandomCoffin(getRandomCoffin());
        if (COFFIN_AND_BROTHERS[player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin()][0] == coffinId) {
            player.getDialogueManager().startDialogue(new TunnelD());
            return true;
        }
        return false;
    }

    public static int getBarrowsIndex(Player player, int id) {
        int index = -1;
        for (int i = 0; i < player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData().length; i++) {
            if (player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][0] == id) {
                index = i;
            }
        }
        return index;
    }

    public static void updateInterface(Player player) {
        for (int i = 0; i < data.length; i++) {
            boolean killed = player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][1] == 2;
            int colour = killed ? Colours.GREEN : Colours.RED;
            player.getPacketSender().sendString((int) data[i][1], (String) data[i][0], colour);
        }
        player.getPacketSender().sendString(37208, "Killcount: " + player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount());
    }

    public static int runes[] = {4740, 558, 560, 565};

    public static int barrows[] = {4708, 4710, 4712, 4714, 4716, 4718, 4720,
            4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747,
            4749, 4751, 4753, 4755, 4757, 4759};

    public static final int[][] COFFIN_AND_BROTHERS = {{6823, 2030},
            {6772, 2029}, {6822, 2028}, {6773, 2027}, {6771, 2026},
            {6821, 2025}
    };

    public static int getRandomCoffin() {
        return Misc.getRandom(COFFIN_AND_BROTHERS.length - 1);
    }

    public static int randomRunes() {
        return runes[(int) (Math.random() * runes.length)];
    }

    public static int randomBarrows() {
        return barrows[(int) (Math.random() * barrows.length)];
    }

    @Override
    public boolean logout() {
        leave(true);
        return false;
    }

    @Override
    public boolean login() {
        start();
        return false;
    }

    @Override
    public void forceClose() {
        leave(false);
    }

    public void leave(boolean logout) {
        removeController();
        if(!logout)
            player.send(new WalkableInterface(-1));
        if(target != null) {
            target.deregister();
            if(coffin != -1)
                player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[coffin][1] = 0;
        }
    }

    @Override
    public void magicTeleported(int type) {
        forceClose();
    }

    @Override
    public void start() {
        sendInterfaces();
        refresh();
    }

    public void refresh() {
        for (int i = 0; i < data.length; i++) {
            boolean killed = player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[i][1] == 2;
            int colour = killed ? Colours.GREEN : Colours.RED;
            player.getPacketSender().sendString((int) data[i][1], (String) data[i][0], colour);
        }
        player.getPacketSender().sendString(37208, "Killcount: " + player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount());
    }

    @Override
    public void sendInterfaces() {
        if(player.getWalkableInterfaceId() != 37200)
            player.send(new WalkableInterface(37200));
    }

    @Override
    public boolean canAttack(Figure target) {
        if (target instanceof BarrowsBrother && target != this.target) {
            player.message("This isn't your target.");
            return false;
        }
        return true;
    }

    public void killedBrother() {
        setBrotherSlayed(target.getId() >= 14297 ? 6 : target.getId());
        target = null;
        coffin = -1;
    }

    private void setBrotherSlayed(int id) {
        if (id == 58) {
            player.getMinigameAttributes().getBarrowsMinigameAttributes().setKillcount(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() + 1);
            refresh();
            return;
        }

        int arrayIndex = getBarrowsIndex(player, id);
        if (arrayIndex < 0)
            return;
        player.getMinigameAttributes().getBarrowsMinigameAttributes().getBarrowsData()[arrayIndex][1] = 2;
        player.getMinigameAttributes().getBarrowsMinigameAttributes().setKillcount(player.getMinigameAttributes().getBarrowsMinigameAttributes().getKillcount() + 1);
        refresh();
    }
}

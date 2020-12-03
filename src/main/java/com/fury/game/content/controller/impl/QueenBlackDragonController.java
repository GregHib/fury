package com.fury.game.content.controller.impl;

import com.fury.cache.Revision;
import com.fury.engine.task.executor.FadingScreen;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.controller.Controller;
import com.fury.game.content.dialogue.impl.misc.RewardChestD;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.entity.character.npc.impl.queenblackdragon.QueenBlackDragon;
import com.fury.game.entity.character.player.link.transportation.TeleportHandler;
import com.fury.game.entity.character.player.link.transportation.TeleportType;
import com.fury.core.model.item.Item;
import com.fury.game.entity.object.GameObject;
import com.fury.game.network.packet.out.WalkableInterface;
import com.fury.game.node.entity.actor.object.ObjectManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.map.Position;
import com.fury.game.world.map.build.MapBuilder;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.game.world.update.flag.block.graphic.Graphic;

import java.util.concurrent.TimeUnit;

/**
 * The Queen Black Dragon controller.
 *
 * @author Emperor
 */
public final class QueenBlackDragonController extends Controller {

    public static final Position OUTSIDE = new Position(1199, 6499);

    /**
     * The platform steps offsets.
     */
    private static final int[][][] PLATFORM_STEPS =
            {
                    {
                            {88, 86},
                            {88, 87},
                            {88, 88},
                            {88, 89},
                            {88, 90},
                            {88, 91},
                            {89, 91},
                            {89, 90},
                            {89, 89},
                            {89, 88},
                            {89, 87},
                            {89, 86},
                            {90, 86},
                            {90, 87},
                            {90, 88},
                            {90, 89},
                            {90, 90},
                            {90, 91},
                            {91, 91},
                            {91, 90},
                            {91, 89},
                            {91, 88},
                            {91, 87},
                            {92, 87},
                            {92, 88},
                            {92, 89},
                            {92, 90},
                            {92, 91},
                            {93, 91},
                            {93, 90},
                            {93, 89},
                            {93, 88},
                            {94, 88},
                            {94, 89},
                            {94, 90},
                            {94, 91},
                            {95, 91},
                            {95, 90},
                            {95, 89},
                            {96, 89},
                            {96, 90},
                            {96, 91},
                            {97, 91},
                            {97, 90},
                            {98, 90},
                            {98, 91},
                            {99, 91}},
                    {
                            {106, 91},
                            {106, 90},
                            {106, 89},
                            {106, 88},
                            {106, 87},
                            {106, 86},
                            {105, 86},
                            {105, 87},
                            {105, 88},
                            {105, 89},
                            {105, 90},
                            {105, 91},
                            {104, 91},
                            {104, 90},
                            {104, 89},
                            {104, 88},
                            {104, 87},
                            {104, 86},
                            {103, 87},
                            {103, 88},
                            {103, 89},
                            {103, 90},
                            {103, 91},
                            {102, 91},
                            {102, 90},
                            {102, 89},
                            {102, 88},
                            {102, 87},
                            {101, 88},
                            {101, 89},
                            {101, 90},
                            {101, 91},
                            {100, 91},
                            {100, 90},
                            {100, 89},
                            {100, 88},
                            {99, 88},
                            {99, 89},
                            {99, 90},
                            {98, 89}},
                    {
                            {99, 90},
                            {100, 90},
                            {100, 89},
                            {99, 89},
                            {98, 89},
                            {97, 89},
                            {95, 88},
                            {96, 88},
                            {97, 88},
                            {98, 88},
                            {99, 88},
                            {99, 87},
                            {98, 87},
                            {97, 87},
                            {96, 87},
                            {96, 86},
                            {97, 86},
                            {98, 86}}};

    /**
     * The current count of standing on a platform.
     */
    private int platformStand;

    /**
     * The Queen Black Dragon Npc.
     */
    private QueenBlackDragon npc;

    /**
     * The region base.
     */
    private int[] regionBase;

    /**
     * The base location of the region.
     */
    private Position base;

    @Override
    public void start() {
        player.getMovement().lock();
        GameExecutorManager.slowExecutor.execute(() -> {
                regionBase = MapBuilder.findEmptyChunkBound(16, 8);
                base = new Position(regionBase[0] << 3, regionBase[1] << 3, 1);
                MapBuilder.copyAllPlanesMap(176, 792, regionBase[0], regionBase[1], 8, 8);
                MapBuilder.copyAllPlanesMap(160, 760, regionBase[0] + 8, regionBase[1], 8, 8);
                player.getPacketSender().toggleViewDistance(1);
                player.loadMapRegions();
            GameWorld.schedule(2, () -> {
                player.getMovement().lock(1);
                npc = new QueenBlackDragon(player, base.transform(31, 37, 0), base);
                player.perform(new Animation(16754, 20));
                player.getPacketSender().sendGraphic(new Graphic(3447, Revision.PRE_RS3), player);
                player.getDirection().face(npc);
                player.moveTo(base.transform(33, 28, 0));
                player.perform(new Animation(16754, 20, Revision.PRE_RS3));
            });
        });
    }

    @Override
    public boolean processUntouchableObjectClick1(GameObject object) {
        if (object != null) {
            if (object.getId() == 70813) {
                TeleportHandler.teleportPlayer(player, OUTSIDE, TeleportType.NORMAL);
                end(0);
                return false;
            }
            if (object.getId() == 70814) {
                player.message("The gate is locked.");
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean processObjectClick1(GameObject object) {
        if (npc == null) {
            return true;
        }
        if (object.getId() == 70790) {
            if (npc.getPhase() < 5)
                return true;

            player.send(new WalkableInterface(-1));
            player.getMovement().lock();
            FadingScreen.fade(player, () -> {
                player.message("You descend the stairs that appeared when you defeated the Queen Black Dragon.");
                npc.deregister();
                player.setPosition(base.transform(31 + 64, 36, -1));
                player.moveTo(player);
                player.getMovement().unlock();
            });
            return false;
        }
        if (object.getId() == 70815) {
            if (player.getInventory().getSpaces() < npc.getRewards().getItems().length) {
                player.message("You must have " + npc.getRewards().getItems().length + " free inventory slots.");
            } else {
                player.getDialogueManager().startDialogue(new RewardChestD(), npc);
                ObjectManager.spawnObject(new GameObject(70817, object, 10, 0, Revision.PRE_RS3));
            }
            return false;
        }
        if (object.getId() == 70817) {
            player.message("You have already claimed your rewards!");
            return false;
        }
        if (object.getId() == npc.getActiveArtifact().getId()) {
            npc.setSpawningWorms(false);
            npc.setNextAttack(20);
            npc.setActiveArtifact(new GameObject(object.getId() + 1, object, 10, 0, Revision.PRE_RS3));
            npc.getHealth().setHitpoints(npc.getMaxConstitution());
            npc.setCantInteract(false);
            npc.setPhase(npc.getPhase() + 1);
            ObjectManager.spawnObject(npc.getActiveArtifact());
            switch (object.getId()) {
                case 70777:
                    player.getPacketSender().sendInterfaceSpriteUpdate(59578, 1152);
                    ObjectManager.spawnObject(new GameObject(70843, base.transform(24, 21, -1), 10, 0, Revision.PRE_RS3));
                    break;
                case 70780:
                    player.getPacketSender().sendInterfaceSpriteUpdate(59579, 1152);
                    ObjectManager.spawnObject(new GameObject(70845, base.transform(24, 21, -1), 10, 0, Revision.PRE_RS3));
                    break;
                case 70783:
                    player.getPacketSender().sendInterfaceSpriteUpdate(59580, 1152);
                    ObjectManager.spawnObject(new GameObject(70847, base.transform(24, 21, -1), 10, 0, Revision.PRE_RS3));
                    break;
                case 70786:
                    player.getPacketSender().sendInterfaceSpriteUpdate(59581, 1152);
                    ObjectManager.spawnObject(new GameObject(70849, base.transform(24, 21, -1), 10, 0, Revision.PRE_RS3));
                    break;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick2(GameObject object) {
        if (object.getId() == 70815) {
            player.getBank().open();
            return false;
        }
        if (object.getId() == 70817) {
            player.getBank().open();
            return false;
        }
        return true;
    }

    @Override
    public boolean processItemClick1(Item item) {
        if (item.getId() == 24337) {
            if (npc == null) {
                player.message("You cannot do this right now.");
                return false;
            }
            if (!player.getSkills().hasRequirement(Skill.SMITHING, 70, "brandish this bow"))
                return false;
            if (npc.getAnimation().getId() == 16745) {
                player.getMovement().lock(3);
                player.perform(new Animation(16870, Revision.PRE_RS3));
                player.message("You successfully brandish the bow into a usable weapon.");
                player.getInventory().delete(new Item(24337));
                player.getInventory().addSafe(new Item(24338));
            }
            return false;
        }
        return true;
    }

    @Override
    public void process() {
        if (npc == null)
            return;
        if (player.getY() < base.getY() + 28) {
            if (npc.getFinished() || npc.getPhase() == 5) {
                return;
            }
            if (platformStand++ == 3) {
                player.message("You are damaged for standing too long on the raw magical platforms.");
                player.getCombat().applyHit(new Hit(npc, 150, HitMask.RED, CombatIcon.NONE));
                platformStand = 0;
            }
        } else {
            platformStand = 0;
        }
    }

    @Override
    public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
        if (npc != null && nextY < base.getY() + 28) {
            if (npc.getPhase() > 1) {
                for (int[] step : PLATFORM_STEPS[0]) {
                    if (base.getX() + (step[0] - 64) == nextX && base.getY() + (step[1] - 64) == nextY) {
                        return true;
                    }
                }
                if (npc.getPhase() > 2) {
                    for (int[] step : PLATFORM_STEPS[1]) {
                        if (base.getX() + (step[0] - 64) == nextX && base.getY() + (step[1] - 64) == nextY) {
                            return true;
                        }
                    }
                    if (npc.getPhase() > 3) {
                        for (int[] step : PLATFORM_STEPS[2]) {
                            if (base.getX() + (step[0] - 64) == nextX && base.getY() + (step[1] - 64) == nextY) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

	/*@Override
    public boolean processButtonClick(int interfaceId, int componentId, int slotId, int slotId2, int packetId) {
		if (npc == null) {
			return true;
		}
		switch (interfaceId) {
		case 1284:
			switch (componentId) {
			case 8:
				player.getBank(0).addItems(npc.getRewards().toArray(), false);
				npc.getRewards().clear();
				player.message("All the items were moved to your bank.");
				break;
			case 9:
				npc.getRewards().clear();
				player.message("All the items were removed from the chest.");
				break;
			case 10:
				for (int slot = 0; slot < npc.getRewards().toArray().length; slot++) {
					Item item = npc.getRewards().get(slot);
					if (item == null) {
						continue;
					}
					boolean added = true;
					if (item.getDefinitions().isStackable() || item.getAmount() < 2) {
						added = !player.getInventory().isFull();
						if (added) {
							npc.getRewards().toArray()[slot] = null;
						} else {
							player.getInventory().add(item);
						}
					} else {
						for (int i = 0; i < item.getAmount(); i++) {
							Item single = new Item(item.getId());
							if (player.getInventory().isFull()) {
								added = false;
								break;
							} else {
								player.getInventory().add(single);
							}
							npc.getRewards().remove(single);
						}
					}
					if (!added) {
						player.message("You only had enough space in your inventory to accept some of the items.");
						break;
					}
				}
				break;
			case 7:
				Item item = npc.getRewards().get(slotId);
				if (item == null) {
					return true;
				}
				switch (packetId) {
				case 52:
					player.message("It's a " + item.getDefinitions().getName());
					return false;
				case 4:
					npc.getRewards().toArray()[slotId] = null;
					break;
				case 64:
					player.getBank(0).addItems(new Item[]
					{ npc.getRewards().toArray()[slotId] }, false);
					npc.getRewards().toArray()[slotId] = null;
					break;
				case 61:
					boolean added = true;
					if (item.getDefinitions().isStackable() || item.getAmount() < 2) {
						added = !player.getInventory().isFull();
						if (added) {
							npc.getRewards().toArray()[slotId] = null;
						} else {
							player.getInventory().add(item);
						}
					} else {
						for (int i = 0; i < item.getAmount(); i++) {
							Item single = new Item(item.getId());
							if (player.getInventory().isFull()) {
								added = false;
								break;
							} else {
								player.getInventory().add(single);
							}
							npc.getRewards().remove(single);
						}
					}
					if (!added) {
						player.message("You only had enough space in your inventory to accept some of the items.");
						break;
					}
					break;
				default:
					return true;
				}
				break;
			default:
				return true;
			}
			//npc.openRewardChest(false);
			return false;
		}
		return true;
	}*/

    @Override
    public void magicTeleported(int type) {
        end(0);
    }

    @Override
    public boolean sendDeath() {
        player.getMovement().lock(8);
        player.stopAll();
        player.appendDeath(OUTSIDE);
        return false;
    }

    @Override
    public boolean logout() {
        end(1);
        return true;
    }

    @Override
    public void forceClose() {
        end(0);
    }

    /**
     * Ends the controller. 0 - tele 1 - logout
     */
    private void end(int type) {
        player.getPacketSender().toggleViewDistance(0);
        if (type == 0) {
            //player.getInterfaceManager().removeFadingInterface();
            //player.getPacketSender().sendCSVarInteger(184, -1);
        } else {
            player.moveTo(OUTSIDE);
        }
        player.getPacketSender().sendInterfaceSpriteUpdate(59578, 1154);
        player.getPacketSender().sendInterfaceSpriteUpdate(59579, 1154);
        player.getPacketSender().sendInterfaceSpriteUpdate(59580, 1154);
        player.getPacketSender().sendInterfaceSpriteUpdate(59581, 1154);
        player.send(new WalkableInterface(-1));
        //player.getTimersManager().removeTimer(null);
        removeController();
        //if (npc != null)
        //player.getBank(0).addItems(npc.getRewards().toArray(), false);
		/*
		 * 1200 delay because of leaving
		 */
        GameExecutorManager.slowExecutor.schedule(() -> {
            if(regionBase != null)
                MapBuilder.destroyMap(regionBase[0], regionBase[1], 16, 8);
            else
                player.moveTo(OUTSIDE);
        }, 1200, TimeUnit.MILLISECONDS);

    }

    /**
     * Gets the base world tile.
     *
     * @return The base.
     */
    public Position getBase() {
        return base;
    }

    /**
     * Gets the npc.
     *
     * @return The npc.
     */
    public QueenBlackDragon getNpc() {
        return npc;
    }

}
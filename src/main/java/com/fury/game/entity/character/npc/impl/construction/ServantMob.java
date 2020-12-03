package com.fury.game.entity.character.npc.impl.construction;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.Task;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.skill.Skill;
import com.fury.game.content.skill.member.construction.House;
import com.fury.game.content.skill.member.construction.HouseConstants;
import com.fury.game.content.skill.member.construction.HouseConstants.Builds;
import com.fury.game.content.skill.member.construction.HouseConstants.Room;
import com.fury.game.content.skill.member.construction.HouseConstants.Servant;
import com.fury.game.content.skill.member.construction.Sawmill;
import com.fury.game.content.skill.member.construction.Sawmill.Plank;
import com.fury.game.entity.object.GameObject;
import com.fury.game.node.entity.actor.figure.player.handles.Settings;
import com.fury.game.world.FloorItemManager;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.Position;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.game.world.update.flag.block.Gender;
import com.fury.util.Misc;
import com.fury.util.Utils;

@SuppressWarnings("serial")
public class ServantMob extends Mob {

	private Servant servant;
	private Player owner;
	private House house;
	private boolean follow, greetGuests;
	private int[][] checkNearDirs;

	public ServantMob(House house) {
		super(house.getServant().getId(), house.getPortal().transform(1, 0, 0), Revision.RS2);
		servant = house.getServant();
		owner = house.getPlayer();
		this.house = house;
		this.checkNearDirs = Utils.getCoordOffsetsNear(super.getSize());
		if (!owner.getSkills().hasLevel(Skill.CONSTRUCTION, servant.getLevel())) {
			house.setServantOrdinal((byte) -1);
			return;
		}
	}

	public void fire() {
		house.setServantOrdinal((byte) -1);
	}

	public long getBankDelay() {
		return servant.getBankDelay();
	}

	public boolean isFollowing() {
		return follow;
	}

	public void setFollowing(boolean follow) {
		this.follow = follow;
		if (!follow)
			getDirection().setDirection(Direction.NONE);
	}

	public void makeFood(final Builds[] builds) {
		if (house == null)
			return;
		setFollowing(false);
		if (house.isBuildMode()) {
			owner.message("Your servant cannot prepare food while in building mode.");
			return;
		}
		String basicResponse = "I apologise, but I cannot serve " + (owner.getAppearance().getGender() == Gender.MALE ? "Sire" : "Madam") + " without";
		final House.RoomReference kitchen = house.getRoom(Room.KITCHEN), diningRoom = house.getRoom(Room.DINING_ROOM);
		if (kitchen == null) {
			owner.getDialogueManager().sendNPCDialogue(getId(), Expressions.NORMAL, basicResponse + " a proper kichen.");
			return;
		} else if (diningRoom == null) {
			owner.getDialogueManager().sendNPCDialogue(getId(), Expressions.NORMAL, basicResponse + " a proper dinning room.");
			return;
		} else {

			for (Builds build : builds) {
				if (!kitchen.containsBuild(build)) {
					owner.getDialogueManager().sendNPCDialogue(getId(), Expressions.NORMAL, basicResponse + " a " + build.toString().toLowerCase() + ".");
					return;
				}
			}

			if (!diningRoom.containsBuild(HouseConstants.Builds.DINING_TABLE)) {
				owner.getDialogueManager().sendNPCDialogue(getId(), Expressions.NORMAL, basicResponse + " a dinning table");
				return;
			}

			final Position kitchenTile = house.getCenterTile(kitchen);
			final Position diningRoomTile = house.getCenterTile(diningRoom);

			setCantInteract(true);
			house.incrementPaymentStage();

			GameWorld.schedule(new Task(false, 2) {

				private int count = 0, totalCount = 0, index = 0;

				@Override
				public void run() {
					if (!house.isLoaded()) {
						stop();
						return;
					}
					count++;
					if (count == 1) {
						forceChat("I shall return in a moment.");
						animate(858);
						totalCount = (builds.length * 3) + count;
					} else if (count == 2) {
						setPosition(new Position(Utils.getFreeTile(kitchenTile, 2)));
					} else if (totalCount > 0 && index < builds.length) {
						int calculatedCount = totalCount - count;
						Builds build = builds[index];
						if (calculatedCount % 3 == 0) {
							animate(build == Builds.STOVE ? 897 : 3659);
							index++;
						} else if (calculatedCount % 1 == 0)
							setPosition(house.getWorldObjectForBuild(kitchen, build));
					} else if (count == totalCount + 3) {
						setPosition(Utils.getFreeTile(diningRoomTile, 2));
					} else if (count == totalCount + 4 || count == totalCount + 5) {
						GameObject diningTable = house.getWorldObjectForBuild(diningRoom, Builds.DINING_TABLE);
						if (count == totalCount + 4)
							setPosition(diningTable);
						else {
							animate(808);
							int rotation = kitchen.getRotation();
							for (int x = 0; x < (rotation == 1 || rotation == 3 ? 2 : 4); x++)
								for (int y = 0; y < (rotation == 1 || rotation == 3 ? 4 : 2); y++)
									FloorItemManager.addGroundItem(new Item(builds.length == 6 ? 7736 : builds.length == 5 ? house.getServant().getFoodId() : HouseConstants.BEERS[kitchen.getBuildSlot(Builds.BARRELS)]), diningTable.transform(x, y, 0), owner);
							setCantInteract(false);
							stop();
						}
					}
				}
			});

		}
	}

	/**
	 * Types : 0 - Take item from bank, 1 - Logs to Plank, 2 - Notes to Item, 3
	 * - Bank item
	 * 
	 * @param item
	 * @param quantity
	 * @param type
	 */
	public void requestType(int item, int quantity, final byte type) {
		if (World.isUpdating()) {
			owner.getDialogueManager().sendDialogue("Your servant cannot make it to the bank before the system update is complete.");
			return;
		}

		final ItemDefinition defs = Loader.getItem(item);
		int inventorySize = servant.getInventorySize();
		if (!owner.getBank().contains(new Item(defs.isNoted() ? defs.noteId : item)) && type == 0) {
			owner.getDialogueManager().sendNPCDialogue(getId(), Expressions.NORMAL, "It appears you do not have this item in your bank.");
			return;
		} else if (quantity > inventorySize) {
			owner.getDialogueManager().sendNPCDialogue(getId(), Expressions.NORMAL, "I'm sorry to inform you that I can only hold " + inventorySize + "items during a theSession.");
			return;
		}
		setTransformation(1957);
		
		if (type == 1 || type == 2) {
			int amountOwned = owner.getInventory().getAmount(new Item(item));
			if (quantity > amountOwned) {
				quantity = amountOwned;
				if (quantity > inventorySize)
					quantity = inventorySize;
			}
		}
		
		final Plank plank = Sawmill.getPlankForLog(item);
		if (plank != null && type == 1) {
			final int cost = (int) ((plank.getCost() * 0.7) * quantity);
			if (owner.getInventory().getAmount(new Item(995)) < cost) {
				owner.getDialogueManager().sendNPCDialogue(getId(), Expressions.NORMAL, "You do not have enough coins to cover the costs of the sawmill.");
				return;
			}
		}

		if (type == 1 || type == 2) {
			if (type == 2) {
				int freeSlots = owner.getInventory().getSpaces();
				if (quantity > freeSlots)
					quantity = freeSlots;
			} else if (type == 1)
				owner.getInventory().delete(new Item(995, (int) (quantity * (plank.getCost() * 0.7))));
			owner.getInventory().delete(new Item(item, quantity));
		}

		final int completeQuantity = quantity;
		setCantInteract(true);

		if (defs.isNoted())
			item = defs.noteId;
		final int finalItem = item;
		GameWorld.schedule((int) servant.getBankDelay(), () -> {
			setTransformation(servant.getId());
			setCantInteract(false);
			if (!owner.getSettings().getBool(Settings.RUNNING) || !house.isLoaded() || !house.getPlayers().contains(owner)) {
				if (type == 1 || type == 2) {
					owner.getBank().tab().add(new Item(finalItem, completeQuantity));
				}
				return;
			}
			house.incrementPaymentStage();
			if (type == 0) {
				owner.getBank().tab().delete(new Item(finalItem, completeQuantity));
			} else if (type == 1) {
				owner.getInventory().add(new Item(plank.getPlank(), completeQuantity));
			} else if (type == 2) {
				owner.getInventory().add(new Item(finalItem, completeQuantity));
			} else {
				for (int i = 0; i < completeQuantity; i++)
					owner.getBank().tab().add(new Item(finalItem, completeQuantity));
			}
			owner.getDialogueManager().sendNPCDialogue(getId(), Expressions.NORMAL,  type == 3 ? "I have successfully deposited your items into your bank. No longer will the items be at risk from thieves." : "I have returned with the items you asked me to retrieve.");
		});
	}

	public void call() {
		int sizeX = getSizeX();
		int sizeY = getSizeY();
		Position teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final Position tile = new Position(new Position(owner.getX() + checkNearDirs[0][dir], owner.getY() + checkNearDirs[1][dir], owner.getZ()));
			if (World.isTileFree(tile.getX(), tile.getY(), tile.getZ(), sizeX, sizeY)) {
				teleTile = tile;
				break;
			}
		}
		if (teleTile == null) {
			return;
		}
		setPosition(teleTile);
	}

	private void sendFollow() {
		if (getMovement().getLastDirection() != owner.getDirection().getDirection())
			getDirection().setDirection(owner.getDirection().getDirection());
		int sizeX = getSizeX();
		int sizeY = getSizeY();
		int targetSizeX = owner.getSizeX();
		int targetSizeY = owner.getSizeY();
		if (Misc.colides(getX(), getY(), sizeX, sizeY, owner.getX(), owner.getY(), targetSizeX, targetSizeY) && !owner.getMovement().hasWalkSteps()) {
			getMovement().reset();
			if (!getMovement().addWalkSteps(owner.getX() + targetSizeX, getY())) {
				getMovement().reset();
				if (!getMovement().addWalkSteps(owner.getX() - sizeX, getY())) {
					getMovement().reset();
					if (!getMovement().addWalkSteps(getX(), owner.getY() + targetSizeY)) {
						getMovement().reset();
						if (!getMovement().addWalkSteps(getX(), owner.getY() - sizeY)) {
							return;
						}
					}
				}
			}
			return;
		}
		getMovement().reset();
		if (!getCombat().clippedProjectile(owner, true) || !Misc.isOnRange(getX(), getY(), sizeX, sizeY, owner.getX(), owner.getY(), targetSizeX, targetSizeY, 0))
			calcFollow(owner, 2, true, true);
	}

	@Override
	public void processNpc() {
		if (greetGuests && !isWithinDistance(getRespawnTile(), 5)) {
			greetGuests = false;
		}
		if (!follow) {
			super.processNpc();
			return;
		}
		if (!isWithinDistance(owner, 12)) {
			call();
			return;
		}
		sendFollow();
	}

	public boolean isGreetGuests() {
		return greetGuests;
	}

	public void setGreetGuests(boolean greetGuests) {
		this.greetGuests = greetGuests;
	}

	public Servant getServantData() {
		return servant;
	}
}

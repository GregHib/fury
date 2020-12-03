package com.fury.game.entity.character.npc.impl.queenblackdragon


import com.fury.cache.Revision
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.SpeedProjectile
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.container.impl.QBDRewards
import com.fury.game.content.dialogue.impl.misc.RewardChestD
import com.fury.game.content.global.Achievements
import com.fury.game.entity.`object`.GameObject
import com.fury.game.network.packet.out.WalkableInterface
import com.fury.game.node.entity.actor.`object`.ObjectManager
import com.fury.game.node.entity.actor.figure.ProjectileManager
import com.fury.game.node.entity.mob.combat.QueenBlackDragonCombat
import com.fury.game.world.GameWorld
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.Direction
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.game.world.update.flag.block.graphic.GraphicHeight
import com.fury.util.Utils
import java.util.*

/**
 * Represents the Queen Black Dragon.
 *
 * @author Emperor
 */
class QueenBlackDragon
/**
 * Constructs a new `QueenBlackDragon` `Object`.
 *
 * @param attacker The player.
 * @param tile     The world tile to set the queen on.
 * @param base     The dynamic region's base location.
 */
(
        /**
         * The player.
         */
        /**
         * Gets the attacker.
         *
         * @return The attacker.
         */
        val attacker: Player, tile: Position,
        /**
         * The region base location.
         */
        /**
         * Gets the base.
         *
         * @return The base.
         */
        val base: Position) : Mob(15509, tile, Revision.PRE_RS3) {

    /**
     * The queen state.
     */
    /**
     * Gets the state.
     *
     * @return The state.
     */
    /**
     * Opens the reward chest.
     *
     * @param replace
     * If the chest should be replaced with an opened one.
     */
    /*public void openRewardChest(boolean replace) {
		attacker.getInterfaceManager().sendCentralInterface(1284);
		attacker.getPacketSender().sendInterSetItemsOptionsScript(1284, 7, 100, 8, 3, "Take", "Bank", "Discard", "Examine");
		attacker.getPacketSender().sendUnlockIComponentOptionSlots(1284, 7, 0, 10, 0, 1, 2, 3);
		attacker.getPacketSender().sendItems(100, rewards);
		if (replace) {
			World.spawnObject(new WorldObject(70817, 10, 0, base.transform(30, 28, -1)));
		}
	}*/

    /**
     * Sets the state.
     *
     * @param state The state to set.
     */
    var state = QueenState.SLEEPING

    /**
     * The amount of ticks passed.
     */
    /**
     * Gets the amount of ticks.
     *
     * @return The amount of ticks.
     */
    var ticks: Int = 0
        private set

    /**
     * The next attack tick count.
     */
    private var nextAttack: Int = 0

    /**
     * The current attacks.
     */
    private var attacks: Array<QueenAttack>? = null

    /**
     * The current phase.
     */
    /**
     * Gets the phase.
     *
     * @return The phase.
     */
    /**
     * Sets the phase.
     *
     * @param phase The phase to set.
     */
    //Death
    //                World.spawnObject(new GameObject(70837, base.transform(22, 24, -1), 10, 0, Revision.PRE_RS3));
    //                World.spawnObject(new GameObject(70840, base.transform(34, 24, -1), 10, 0, Revision.PRE_RS3));
    var phase: Int = 0
        set(phase) {
            field = phase
            when (phase) {
                1 -> this.attacks = PHASE_1_ATTACKS
                2 -> this.attacks = PHASE_2_ATTACKS
                3 -> this.attacks = PHASE_3_ATTACKS
                4 -> this.attacks = PHASE_4_ATTACKS
                5 -> {
                    setCantInteract(true)
                    for (soul in souls)
                        soul.deregister()
                    for (worm in worms)
                        worm.deregister()
                    ticks = -22
                    prepareRewards()
                    attacker.dialogueManager.startDialogue(RewardChestD(), this)
                    claimRewards(attacker)
                    attacker.message("The enchantment is restored! The Queen Black Dragon falls back into her cursed", 0x33ffff)
                    attacker.message("slumber.", 0x33ffff)
                    Achievements.doProgress(attacker, Achievements.AchievementData.KILL_QBD_100_TIMES)
                }
            }
        }

    /**
     * The list of tortured souls.
     */
    /**
     * Gets the souls.
     *
     * @return The souls.
     */
    val souls: List<TorturedSoul> = ArrayList()

    /**
     * The list of worms.
     */
    private val worms = ArrayList<Mob>()

    /**
     * If the Queen Black Dragon is spawning worms.
     */
    /**
     * Gets the spawningWorms.
     *
     * @return The spawningWorms.
     */
    /**
     * Sets the spawningWorms.
     *
     * @param spawningWorms The spawningWorms to set.
     */
    var isSpawningWorms: Boolean = false
        set(spawningWorms) {
            if (!spawningWorms) {
                perform(Animation(16748, Revision.PRE_RS3))
            }
            field = spawningWorms
        }

    /**
     * The current active artifact.
     */
    /**
     * Gets the activeArtifact.
     *
     * @return The activeArtifact.
     */
    /**
     * Sets the activeArtifact.
     *
     * @param activeArtifact The activeArtifact to set.
     */
    var activeArtifact: GameObject? = null

    /**
     * The rewards container.
     */
    /**
     * Gets the rewards.
     *
     * @return The rewards.
     */
    val rewards = QBDRewards(attacker)

    /**
     * The last amount of hitpoints.
     */
    var lastHitpoints = -1

    var deathPhaseCompleted = 0

    override val maxConstitution: Int
        get() = 6000

    init {
        super.isCantFollowUnderCombat = true
        setCantInteract(true)
        this.nextAttack = 40
        health.hitpoints = maxConstitution
        activeArtifact = GameObject(70776, base.transform(33, 31, 0), 10, 0, Revision.PRE_RS3)
        this.phase = 1
        direction.direction = Direction.NORTH
    }

    override fun processNpc() {
        super.processNpc()
        if (ticks > 5 && !attacker.isInDynamicRegion) {
            deregister()
            return
        }
        if (ticks == -20) {
            switchState(QueenState.DEFAULT)
            switchState(QueenState.SLEEPING)
            super.perform(SLEEP_ANIMATION)
            ObjectManager.removeObject(GameObject(70778, base.transform(33, 31, 0), 10, 0, Revision.PRE_RS3))
            ObjectManager.spawnObject(GameObject(70790, base.transform(31, 29, 0), 10, 0, Revision.PRE_RS3))
            ObjectManager.spawnObject(GameObject(70775, base.transform(31, 29, -1), 10, 0, Revision.PRE_RS3))
        } else if (ticks == -1) {
            return
        }
        ticks++
        if (isSpawningWorms) {
            if (ticks % 10 == 0) {
                spawnWorm()
            }
            return
        } else if (ticks == 5) {
            super.perform(WAKE_UP_ANIMATION)
            attacker.send(WalkableInterface(59575))
        } else if (ticks == 30) {
            setCantInteract(false)
            switchState(QueenState.DEFAULT)
        } else if (ticks == nextAttack) {
            var attack = attacks!![Utils.random(attacks!!.size)]
            var canAttack = false
            while(!canAttack) {
                attack = attacks!![Utils.random(attacks!!.size)]
                canAttack = attack.canAttack(this, attacker)
            }
            this.setNextAttack(attack.attack(this, attacker))
        }
    }

    override fun deregister() {
        for (s in souls) {
            s.deregister()
        }
        for (worm in worms) {
            worm.deregister()
        }
        super.deregister()
    }

    /**
     * Spawns a grotworm.
     */
    fun spawnWorm() {
        perform(Animation(16747, Revision.PRE_RS3))
        attacker.message("Worms burrow through her rotting flesh.")
        val destination = base.transform(28 + Utils.random(12), 28 + Utils.random(6), 0)
        val projectile = WORM.setPositions(this, attacker)
        ProjectileManager.send(projectile)
        if (phase > 4) {
            return
        }
        Graphic.sendGlobal(attacker, Graphic(3142, projectile.delay + 15, GraphicHeight.LOW/*projectile.getEndHeight()*/, Revision.PRE_RS3), destination)
        GameWorld.schedule(Utils.projectileTimeToCycles(projectile.delay) + 1) {
            val worm = Mob(15464, destination, Revision.PRE_RS3)
            worms.add(worm)
            worm.mobCombat!!.target = attacker
        }
    }

    /**
     * Switches the queen state.
     *
     * @param state The state.
     */
    fun switchState(state: QueenState) {
        this.state = state
        if (state.messages.size > 0) {
            for (message in state.messages) {
                attacker.message(message.replace("(", "").replace(")", ""))
            }
        }
        this.setTransformation(state.npcId, Revision.PRE_RS3)
        this.id = state.npcId
        this.spawnedFor = attacker
        when (state) {
            QueenState.DEFAULT -> {
                ObjectManager.spawnObject(GameObject(70822, base.transform(21, 35, -1), 10, 0, Revision.PRE_RS3))
                ObjectManager.spawnObject(GameObject(70818, base.transform(39, 35, -1), 10, 0, Revision.PRE_RS3))
            }
            QueenState.HARDEN -> {
                ObjectManager.spawnObject(GameObject(70824, base.transform(21, 35, -1), 10, 0, Revision.PRE_RS3))
                ObjectManager.spawnObject(GameObject(70820, base.transform(39, 35, -1), 10, 0, Revision.PRE_RS3))
            }
            QueenState.CRYSTAL_ARMOUR -> {
                ObjectManager.spawnObject(GameObject(70823, base.transform(21, 35, -1), 10, 0, Revision.PRE_RS3))
                ObjectManager.spawnObject(GameObject(70819, base.transform(39, 35, -1), 10, 0, Revision.PRE_RS3))
            }
            else -> {
            }
        }
    }

    /**
     * Gets the nextAttack.
     *
     * @return The nextAttack.
     */
    fun getNextAttack(): Int {
        return nextAttack
    }

    /**
     * Sets the nextAttack value (current ticks + the given amount).
     *
     * @param nextAttack The amount.
     */
    fun setNextAttack(nextAttack: Int) {
        this.nextAttack = ticks + nextAttack
    }

    /**
     * Prepares the rewards.
     */
    private fun prepareRewards() {
        rewards.add(Item(537, 5))
        rewards.add(Item(24373, 1 + Utils.random(6)))
        rewards.add(Item(24336, 50 + Utils.random(51)))
        if (!attacker.hasItem(24368)) {
            rewards.add(Item(24368))
        } else if (!attacker.hasItem(24369)) {
            if (Utils.random(10) == 0) {
                rewards.add(Item(24369))
            }
        } else if (!attacker.hasItem(24370)) {
            if (Utils.random(25) == 0) {
                rewards.add(Item(24370))
            }
        } else if (!attacker.hasItem(24371)) {
            if (Utils.random(40) == 0) {
                rewards.add(Item(24371))
            }
        }
        val rewardTable = ArrayList<Item>()
        for (reward in REWARDS) {
            val item = Item(reward[0], reward[1] + Utils.random(reward[2] - reward[1]))
            for (i in 0 until reward[3]) {
                rewardTable.add(item)
            }
        }
        Collections.shuffle(rewardTable)
        for (i in 0 until 1 + Utils.random(3)) {
            rewards.add(rewardTable[Utils.random(rewardTable.size)])
        }
    }

    /**
     * Gets the worms.
     *
     * @return The worms.
     */
    fun getWorms(): List<Mob> {
        return worms
    }

    fun claimRewards(player: Player) {
        if (rewards.spaces == rewards.capacity()) {
            player.message("You have already claimed your rewards!")
            return
        }

        for (item in rewards.items) {
            if (item == null)
                continue

            if (item.id > 24300 && item.id != 24373 && item.id != 24336 && item.id != 24368 || item.id == 11286)
                GameWorld.sendBroadcast(player.username + " has just received a " + item.name + " from the dragonkin coffer!")

            if (item.getDefinition().isStackable || item.amount < 2) {
                player.inventory.addSafe(item)
                rewards.delete(item)
            } else {
                for (i in 0 until item.amount) {
                    val single = Item(item, 1)
                    rewards.move(single, player.inventory)
                }
            }
        }
    }

    companion object {

        /**
         * The attacks for the first phase.
         */
        private val PHASE_1_ATTACKS = arrayOf(FireBreathAttack(), MeleeAttack(), RangeAttack()/*, new FireWallAttack()*/)

        /**
         * The attacks for the second phase.
         */
        private val PHASE_2_ATTACKS = arrayOf(FireBreathAttack(), MeleeAttack(), RangeAttack()/*, new FireWallAttack()*/, ChangeArmour())

        /**
         * The attacks for the third phase.
         */
        private val PHASE_3_ATTACKS = arrayOf(FireBreathAttack(), MeleeAttack(), RangeAttack()/*, new FireWallAttack()*/, ChangeArmour())


        private val WORM = SpeedProjectile(3141, Revision.PRE_RS3, 20, 1, 5, 10, 1)

        /**
         * The attacks for the last phase.
         */
        private val PHASE_4_ATTACKS = arrayOf(// Super fire attack twice, to make it more often.
                FireBreathAttack(), MeleeAttack(), SuperFireAttack(), RangeAttack(),
                //            new FireWallAttack(),
                ChangeArmour())

        /**
         * The rewards (average ratio: 28,5).
         */
        private val REWARDS = arrayOf(intArrayOf(1149, 1, 1, 20), // Dragon helm
                intArrayOf(1305, 1, 1, 25), // Dragon longsword
                intArrayOf(1215, 1, 1, 25), // Dragon dagger
                intArrayOf(1249, 1, 1, 20), // Dragon spear
                intArrayOf(560, 150, 150, 50), // Death rune
                intArrayOf(565, 150, 150, 50), // Blood rune
                intArrayOf(566, 20, 100, 30), // Soul rune
                intArrayOf(9342, 150, 150, 25), // Onyx bolts
                intArrayOf(7937, 1480, 3500, 25), // Pure essence
                intArrayOf(454, 300, 580, 30), // Coal
                intArrayOf(450, 50, 50, 18), // Adamantite
                intArrayOf(452, 30, 100, 14), // Runite ore
                intArrayOf(5316, 1, 1, 25), // Magic seed
                intArrayOf(5300, 5, 5, 25), // Snapdragon seed
                intArrayOf(5321, 3, 3, 25), // Watermelon seed
                intArrayOf(5304, 1, 5, 10), // Torstol seed
                intArrayOf(372, 200, 200, 20), // Raw swordfish
                intArrayOf(15273, 1, 9, 40), // Rocktail
                intArrayOf(6686, 1, 10, 25), // Saradomin brew (2)
                intArrayOf(3025, 1, 10, 25), // Super restore (2)
                intArrayOf(995, 5000, 7000, 150), // Coins
                intArrayOf(995, 50000, 200000, 40), // Coins
                intArrayOf(1514, 90, 120, 26), // Magic logs
                intArrayOf(1516, 150, 500, 30), // Yew logs
                intArrayOf(220, 10, 10, 17), // Grimy torstol
                intArrayOf(2486, 50, 50, 15), // Grimy lantadyme
                intArrayOf(1632, 1, 9, 28), // Uncut dragonstone
                intArrayOf(2366, 1, 1, 15), // Shield left half
                intArrayOf(9194, 30, 30, 15), // Onyx bolt tips
                intArrayOf(24346, 1, 1, 2), // Royal torsion spring
                intArrayOf(24344, 1, 1, 2), // Royal sight
                intArrayOf(24342, 1, 1, 2), // Royal frame
                intArrayOf(24340, 1, 1, 2), // Royal bolt stabiliser
                //{ 24352, 1, 1, 5 }, // Dragonbone upgrade kit
                intArrayOf(24365, 1, 1, 1), // Dragon kiteshield
                intArrayOf(11286, 1, 1, 1))// Draconic visage current: 850

        /**
         * The waking up animation.
         */
        private val WAKE_UP_ANIMATION = Animation(16714, Revision.PRE_RS3)

        /**
         * The sleeping animation.
         */
        private val SLEEP_ANIMATION = Animation(16742, Revision.PRE_RS3)
    }

}
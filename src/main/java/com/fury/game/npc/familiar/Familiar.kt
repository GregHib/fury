package com.fury.game.npc.familiar

import com.fury.cache.def.Loader
import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.mob.Mob
import com.fury.core.model.node.entity.actor.figure.player.Player
import com.fury.game.GameSettings
import com.fury.game.container.impl.BeastOfBurden
import com.fury.game.content.controller.impl.Wilderness
import com.fury.game.content.skill.Skill
import com.fury.game.content.skill.member.slayer.Slayer
import com.fury.game.content.skill.member.summoning.Infusion
import com.fury.game.content.skill.member.summoning.impl.Summoning
import com.fury.game.entity.character.combat.CombatConstants
import com.fury.game.entity.character.player.actions.PlayerCombatAction
import com.fury.game.system.files.loaders.item.ItemConstants
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Area
import com.fury.game.world.map.Position
import com.fury.game.world.update.flag.Flag
import com.fury.game.world.update.flag.block.graphic.Graphic
import com.fury.util.Misc
import com.fury.util.Utils

abstract class Familiar(owner: Player, var pouch: Summoning.Pouches, tile: Position) : Mob(pouch.npcId, tile, true) {
    var owner: Player? = null
        private set
    var ticks: Int = 0
    var trackTimer: Int = 0
    var specialEnergy: Int = 0
    var trackDrain: Boolean = false
    private var scrollCount = 0

    private var checkNearDirs: Array<IntArray>? = null
    private var sentRequestMoveMessage: Boolean = false

    var dead: Boolean = false

    var beastOfBurden: BeastOfBurden? = null
        private set

    val originalId: Int
        get() = pouch.npcId

    override val isDead: Boolean
        get() = dead || super.isDead

    abstract val specialName: String

    abstract val specialDescription: String

    abstract val storeSize: Int

    abstract val specialAttackEnergy: Int

    abstract val specialAttack: SpecialAttack

    val isAggressive: Boolean
        get() = true

    init {
        this.owner = owner
        resetTickets()
        specialEnergy = 60
        if (storeSize > 0)
            beastOfBurden = BeastOfBurden(owner, canDeposit(), storeSize)
        call(true)
        run = true
    }

    fun store() {
        if (beastOfBurden == null)
            return
        beastOfBurden!!.open()
    }

    fun canDeposit(): Boolean {
        return (canStoreEssOnly() || pouch == Summoning.Pouches.THORNY_SNAIL || pouch == Summoning.Pouches.SPIRIT_KALPHITE || pouch == Summoning.Pouches.BULL_ANT || pouch == Summoning.Pouches.SPIRIT_TERRORBIRD || pouch == Summoning.Pouches.WAR_TORTOISE || pouch == Summoning.Pouches.PACK_YAK
                || pouch == Summoning.Pouches.CUB_WORLDBEARER || pouch == Summoning.Pouches.LITTLE_WORLDBEARER || pouch == Summoning.Pouches.NAIVE_WORLDBEARER || pouch == Summoning.Pouches.KEEN_WORLDBEARER || pouch == Summoning.Pouches.BRAVE_WORLDBEARER || pouch == Summoning.Pouches.BRAH_WORLDBEARER || pouch == Summoning.Pouches.NAABE_WORLDBEARER || pouch == Summoning.Pouches.WISE_WORLDBEARER || pouch == Summoning.Pouches.ADEPT_WORLDBEARER || pouch == Summoning.Pouches.SACHEM_WORLDBEARER
                || pouch == Summoning.Pouches.CLAY_FAMILIAR_CLASS_1 || pouch == Summoning.Pouches.CLAY_FAMILIAR_CLASS_2 || pouch == Summoning.Pouches.CLAY_FAMILIAR_CLASS_3 || pouch == Summoning.Pouches.CLAY_FAMILIAR_CLASS_4 || pouch == Summoning.Pouches.CLAY_FAMILIAR_CLASS_5)
    }

    fun canStoreEssOnly(): Boolean {
        return pouch == Summoning.Pouches.ABYSSAL_LURKER || pouch == Summoning.Pouches.ABYSSAL_PARASITE || pouch == Summoning.Pouches.ABYSSAL_TITAN
    }

    fun resetTickets() {
        ticks = (pouch.time / 1000 / 30).toInt()
        trackTimer = 0
    }

    private fun sendFollow() {
        if (direction.interacting !== owner)
            direction.interacting = owner

        if (combat.freezeDelay >= Utils.currentTimeMillis())
            return

        val sizeX = sizeX
        val sizeY = sizeY
        val targetSizeX = owner!!.sizeX
        val targetSizeY = owner!!.sizeY
        if (Misc.colides(x, y, sizeX, sizeY, owner!!.x, owner!!.y, targetSizeX, targetSizeY) && !owner!!.movement.hasWalkSteps()) {
            movement.reset()
            if (!movement.addWalkSteps(owner!!.x + targetSizeX, y)) {
                movement.reset()
                if (!movement.addWalkSteps(owner!!.x - sizeX, y)) {
                    movement.reset()
                    if (!movement.addWalkSteps(x, owner!!.y + targetSizeY)) {
                        movement.reset()
                        if (!movement.addWalkSteps(x, owner!!.y - sizeY)) {
                            return
                        }
                    }
                }
            }
            return
        }
        movement.reset()
        if (!combat.clippedProjectile(owner!!, true) || !Misc.isOnRange(x, y, sizeX, sizeY, owner!!.x, owner!!.y, targetSizeX, targetSizeY, 0))
            movement.addWalkStepsInteract(owner!!.x, owner!!.y, 2, sizeX, sizeY, true)
    }

    override fun processNpc() {
        if (isDead)
            return
        unlockOrb()
        trackTimer++
        if (trackTimer == 50) {
            trackTimer = 0
            ticks--
            if (trackDrain)
                owner!!.skills.drain(Skill.SUMMONING, 1)
            trackDrain = !trackDrain
            when (ticks) {
                2 -> owner!!.message("You have 1 minute before your familiar vanishes.")
                1 -> owner!!.message("You have 30 seconds before your familiar vanishes.")
                0 -> {
                    removeFamiliar()
                    dismissFamiliar(false)
                    return
                }
            }
            refreshLevels()
            sendTimeRemaining()
        }

        refreshSpecialScrolls()

        if (owner!!.isCanPvp && id != originalId) {
            setTransformation(originalId)
            call(false)
            return
        } else if (!owner!!.isCanPvp && id == originalId && pouch != Summoning.Pouches.MAGPIE && pouch != Summoning.Pouches.MEERKATS && pouch != Summoning.Pouches.IBIS && pouch != Summoning.Pouches.BEAVER && pouch != Summoning.Pouches.MACAW && pouch != Summoning.Pouches.FRUIT_BAT) {
            setTransformation(originalId - 1)
            call(false)
            return
        } else if (!isWithinDistance(owner!!, 12)) {
            call(false)
            return
        }
        if (!mobCombat!!.process()) {
            if (isAggressive && owner!!.combat.attackedBy != null && owner!!.combat.attackedByDelay > Misc.currentTimeMillis() && canAttack(owner!!.combat.attackedBy) && Misc.random(25) == 0)
                mobCombat!!.target = owner!!.combat.attackedBy
            else
                sendFollow()
        }
    }

    fun refreshSpecialScrolls() {
        val scrolls = Summoning.calculateScrolls(owner!!)
        if (scrollCount != scrolls) {
            owner!!.packetSender.sendButtonToggle(54050, scrolls > 0 && specialEnergy != 0)
            owner!!.packetSender.sendString(54051, scrolls.toString())
            scrollCount = scrolls
        }
    }

    fun refreshLevels() {
        owner!!.packetSender.sendString(54045, owner!!.skills.getLevel(Skill.SUMMONING).toString() + "/" + owner!!.skills.getMaxLevel(Skill.SUMMONING))
    }

    fun canAttack(target: Figure?): Boolean {
        if (target === this || target === owner)
            return false

        if (!Area.isMulti(target!!))
            return false

        if (target.isPlayer()) {
            val player = target as Player?
            if (!owner!!.isCanPvp || !player!!.isCanPvp)
                return false
        } else if (target.isNpc()) {
            val mob = target as Mob?
            val slayerLevel = Slayer.getLevelRequirement(mob!!.name)
            if (slayerLevel > owner!!.skills.getLevel(Skill.SLAYER)) {
                owner!!.message("You need a slayer level of $slayerLevel to know how to wound this monster.")
                return false
            }

            if (mob.isCantInteract)
                return false

            if (mob.isFamiliar) {
                val familiar = mob as Familiar?
                if (!familiar!!.canAttack(owner))
                    return false

                if (owner!!.controllerManager.controller is Wilderness)
                    CombatConstants.skullPlayer(owner!!)
            } else {
                /*if (!n.canBeAttackFromOutOfArea()
                        && !MapAreas.isAtArea(n.getMapAreaNameHash(), player))
                    return false;*/

                if (PlayerCombatAction.isAttackException(owner, mob))
                    return false
            }
        }
        return !target.isDead && owner!!.controllerManager.canAttack(target)
    }

    fun renewFamiliar(): Boolean {
        if (ticks > 5) {
            owner!!.message("You need to have less than 2:50 remaining before you can renew your familiar.", true)
            return false
        } else if (!owner!!.inventory.contains(Item(pouch.pouchId, 1))) {
            owner!!.message("You need a " + Loader.getItem(pouch.pouchId).getName().toLowerCase() + " to renew your familiar's timer.")
            return false
        }
        resetTickets()
        owner!!.inventory.delete(Item(pouch.pouchId, 1))
        call(true)
        owner!!.message("You use your remaining pouch to renew your familiar.")
        return true
    }

    fun takeBob() {
        if (beastOfBurden == null)
            return
        if (beastOfBurden!!.spaces == storeSize) {
            owner!!.message("Your familiar currently is not carrying any items.")
            return
        }
        beastOfBurden!!.take()
    }

    fun sendTimeRemaining() {
        owner!!.packetSender.sendString(54043, "" + Infusion.getTimer(ticks * 65))
        //        owner.getVarsManager().sendVarOld(1176, ticks * 65);
    }

    fun sendMainConfigs() {
        switchOrb(true)
        //        owner.getVarsManager().sendVarOld(448, pouch.getRealPouchId());// configures
        refreshSpecialEnergy()
        sendTimeRemaining()
        //        owner.getVarsManager().sendVarBit(6051, getSpecialAttackEnergy());// DONE
        //        owner.getPacketSender().sendCSVarString(204, getSpecialName());
        //        owner.getPacketSender().sendCSVarString(205, getSpecialDescription());
        //        owner.getPacketSender().sendCSVarInteger(1436, getSpecialAttack() == SpecialAttack.CLICK ? 1 : 0);
        unlockOrb() // temporary
        sendFollowerDetails() //send interface when u start
    }

    fun sendFollowerDetails() {
        //        owner.getInterfaceManager().setFamiliarInterface(662);
        //        owner.getPacketSender().sendHideIComponent(662, 44, true);
        //        owner.getPacketSender().sendHideIComponent(662, 45, true);
        //        owner.getPacketSender().sendHideIComponent(662, 46, true);
        //        owner.getPacketSender().sendHideIComponent(662, 47, true);
        //        owner.getPacketSender().sendHideIComponent(662, 48, true);
        //        owner.getPacketSender().sendHideIComponent(662, 71, false);
        //        owner.getPacketSender().sendHideIComponent(662, 72, false);
    }

    fun switchOrb(on: Boolean) {
        owner!!.packetSender.sendOrb(3, on)
        if (!on)
            lockOrb()
    }

    fun unlockOrb() {
        //owner.getPacketSender().sendHideIComponent(1428, 15, false);
        sendLeftClickOption(owner)
    }

    fun lockOrb() {
        refreshDefaultPetOptions(owner)
        //owner.getPacketSender().sendHideIComponent(1428, 15, true);
    }

    fun call() {
        if (isDead)
            return
        if (combat.attackedBy != null && combat.attackedByDelay > Misc.currentTimeMillis()) {
            owner!!.message("You can't call your familiar while it under combat.")
            return
        }
        call(false)
    }

    fun call(login: Boolean) {
        val sizeX = sizeX
        val sizeY = sizeY
        if (login) {
            checkNearDirs = Utils.getCoordOffsetsNear(getSize())
            sendMainConfigs()
        } else
            removeTarget()
        var teleTile: Position? = null
        for (dir in 0 until checkNearDirs!![0].size) {
            val tile = Position(Position(owner!!.x + checkNearDirs!![0][dir], owner!!.y + checkNearDirs!![1][dir], owner!!.z))
            if (World.isTileFree(tile.x, tile.y, tile.z, sizeX, sizeY)) { // if found done
                teleTile = tile
                break
            }
        }
        if (login || teleTile != null)
            GameWorld.schedule(1) {
                updateFlags.add(Flag.ENTITY_INTERACTION)
                perform(Graphic(if (definition.size > 1) 1315 else 1314))
            }
        if (teleTile == null) {
            if (!sentRequestMoveMessage) {
                owner!!.message("Your familiar is too large to fit in the area you are standing in. Move into a larger space and try again.")
                sentRequestMoveMessage = true
            }
            return
        }
        sentRequestMoveMessage = false
        moveTo(teleTile)
    }

    fun removeFamiliar() {
        owner!!.familiar = null
    }

    fun dismissFamiliar(logged: Boolean) {
        deregister()
        if (!logged && finished) {
            if (owner!!.familiar == null) {
                owner!!.packetSender.sendTabInterface(GameSettings.SUMMONING_TAB, -1)
                owner!!.packetSender.sendTab(GameSettings.INVENTORY_TAB)
                switchOrb(false)
            }
            if (beastOfBurden != null)
                beastOfBurden!!.drop(copyPosition())
        }
    }

    fun respawnFamiliar(owner: Player) {
        this.owner = owner
        init()
        deserialize()
        call(true)
    }

    abstract fun submitSpecial(`object`: Any): Boolean

    fun depositInventory() {
        if (!beastOfBurden!!.canDeposit()) {
            owner!!.message("You cannot store items in this familiar.")
            return
        }

        if (owner!!.interfaceId != BeastOfBurden.INVENTORY_INTERFACE_ID || beastOfBurden == null)
            return

        var success = false
        for (item in owner!!.inventory.items)
            if (item != null)
                if (canDeposit(item))
                    success = success or owner!!.inventory.move(item, beastOfBurden)

        if (!success)
            beastOfBurden!!.full()
        beastOfBurden!!.refresh()
        owner!!.inventory.refresh()
    }

    fun canDeposit(item: Item): Boolean {
        if (!beastOfBurden!!.canDeposit()) {
            owner!!.message("You cannot store items in this familiar.")
            return false
        } else if (!ItemConstants.isTradeable(item) && item.id != 23194 || item.id == 4049 || canStoreEssOnly() && item.id != 1436 && item.id != 7936 || item.definitions.value * item.amount > 500000) {
            owner!!.message("You cannot store this item.")
            return false
        } else if (item.getDefinition().isStackable) {
            owner!!.message("You cannot store stackable items.")
            return false
        }
        return true
    }

    enum class SpecialAttack {
        ITEM, ENTITY, CLICK, OBJECT
    }

    fun refreshSpecialEnergy() {
        owner!!.packetSender.sendString(54046, specialEnergy.toString() + "/60")
    }

    fun restoreSpecialEnergy(energy: Int) {
        if (specialEnergy >= 60)
            return
        specialEnergy = if (energy + specialEnergy >= 60) 60 else specialEnergy + energy
        refreshSpecialEnergy()
    }

    fun setSpecial(on: Boolean) {
        if (!on)
            owner!!.temporaryAttributes.remove("FamiliarSpec")
        else {
            if (specialEnergy < specialAttackEnergy) {
                owner!!.message("Your special move bar is too low to use this scroll.")
                return
            }
            owner!!.temporaryAttributes["FamiliarSpec"] = java.lang.Boolean.TRUE
        }
    }

    fun drainSpecial(specialReduction: Int) {
        specialEnergy -= specialReduction
        if (specialEnergy < 0) {
            specialEnergy = 0
        }
        refreshSpecialEnergy()
    }

    fun drainSpecial() {
        specialEnergy -= specialAttackEnergy
        refreshSpecialEnergy()
    }

    fun hasSpecialOn(): Boolean {
        if (owner!!.temporaryAttributes.remove("FamiliarSpec") != null) {
            val scrollId = pouch.scrollId
            if (!owner!!.inventory.contains(Item(scrollId, 1))) {
                owner!!.message("You don't have the scrolls to use this move.")
                return false
            }
            if (!isWithinDistance(owner!!, 16)) {
                owner!!.message("Your familiar is too far away to use that scroll, or it cannot see you.")
                return false
            }
            owner!!.inventory.delete(Item(scrollId, 1))
            drainSpecial()
            return true
        }
        return false
    }

    fun setBob(bob: BeastOfBurden) {
        this.beastOfBurden = bob
    }

    companion object {

        /**
         * Orb left clicking
         */
        fun selectLeftOption(player: Player) {
            sendLeftClickOption(player)
            //        player.getInterfaceManager().setFamiliarInterface(1148);
            //        player.getInterfaceManager().openGameTab(InterfaceManager.SUMMONING_TAB);
        }

        fun confirmLeftOption(player: Player) {
            //        player.getInterfaceManager().removeFamiliarInterface();
        }

        fun setLeftclickOption(player: Player, summoningLeftClickOption: Int) {
            //        if (summoningLeftClickOption == player.getSummoningLeftClickOption())
            //            return;
            //        player.setSummoningLeftClickOption(summoningLeftClickOption);
            sendLeftClickOption(player)
        }

        fun sendLeftClickOption(player: Player?) {
            //        player.getVarsManager().sendVarOld(1493, player.getSummoningLeftClickOption());
            //        player.getVarsManager().sendVarOld(1494, player.getSummoningLeftClickOption());
        }

        fun refreshDefaultPetOptions(owner: Player?) {
            //COMP 33 - Call Familiar ONLY
            //COMP 6 - Left click option
            //	owner.getPacketSender().sendHideIComponent(1428, 7, true);
            //	owner.getPacketSender().sendHideIComponent(1428, 6, true);
        }
    }

}

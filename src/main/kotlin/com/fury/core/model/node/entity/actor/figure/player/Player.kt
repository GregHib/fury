package com.fury.core.model.node.entity.actor.figure.player

import com.fury.core.model.item.Item
import com.fury.core.model.node.entity.actor.figure.Figure
import com.fury.core.model.node.entity.actor.figure.combat.magic.CombatSpell
import com.fury.game.GameSettings
import com.fury.game.container.impl.*
import com.fury.game.container.impl.bank.Bank
import com.fury.game.container.impl.equip.Equipment
import com.fury.game.container.impl.equip.Slot
import com.fury.game.container.impl.shop.Shop
import com.fury.game.content.combat.magic.MagicSpellBook
import com.fury.game.content.controller.ControllerManager
import com.fury.game.content.controller.impl.Wilderness
import com.fury.game.content.controller.impl.duel.DuelConfigurations
import com.fury.game.content.dialogue.DialogueFactory
import com.fury.game.content.dialogue.DialogueManager
import com.fury.game.content.dialogue.input.Input
import com.fury.game.content.eco.ge.GrandExchangeSlot
import com.fury.game.content.global.Achievements
import com.fury.game.content.global.Achievements.AchievementAttributes
import com.fury.game.content.global.action.ActionManager
import com.fury.game.content.global.config.ConfigManager
import com.fury.game.content.global.events.christmas.ChristmasPartyCharacters
import com.fury.game.content.global.minigames.MinigameAttributes
import com.fury.game.content.global.minigames.impl.Barrows
import com.fury.game.content.global.minigames.impl.OldDueling
import com.fury.game.content.global.quests.QuestManager
import com.fury.game.content.global.treasuretrails.ClueScroll
import com.fury.game.content.misc.objects.DwarfMultiCannon
import com.fury.game.content.misc.objects.StatueHandler
import com.fury.game.content.skill.Skill
import com.fury.game.content.skill.Skills
import com.fury.game.content.skill.free.dungeoneering.DungManager
import com.fury.game.content.skill.free.dungeoneering.DungeonController
import com.fury.game.content.skill.free.prayer.Prayerbook
import com.fury.game.content.skill.member.construction.House
import com.fury.game.content.skill.member.farming.FarmingManager
import com.fury.game.content.skill.member.slayer.SlayerManager
import com.fury.game.content.skill.member.summoning.Infusion
import com.fury.game.content.skill.member.summoning.Scroll
import com.fury.game.content.skill.member.summoning.impl.Pet
import com.fury.game.entity.`object`.GameObject
import com.fury.game.entity.character.CharacterAnimations
import com.fury.game.entity.character.combat.CombatDefinitions
import com.fury.game.entity.character.combat.effects.Effect
import com.fury.game.entity.character.combat.effects.Effects
import com.fury.game.entity.character.combat.equipment.weapon.CombatSpecial
import com.fury.game.entity.character.combat.equipment.weapon.FightType
import com.fury.game.entity.character.combat.equipment.weapon.WeaponInterface
import com.fury.game.entity.character.combat.prayer.Prayer
import com.fury.game.entity.character.combat.pvp.BountyHunter
import com.fury.game.entity.character.combat.pvp.PlayerKillingAttributes
import com.fury.game.entity.character.combat.range.CombatRangedAmmo.RangedWeaponData
import com.fury.game.entity.character.player.PlayerHandler
import com.fury.game.entity.character.player.actions.ItemMorph
import com.fury.game.entity.character.player.actions.PlayerCombatAction
import com.fury.game.entity.character.player.content.*
import com.fury.game.entity.character.player.content.BankPin.BankPinAttributes
import com.fury.game.entity.character.player.content.LoyaltyProgramme.LoyaltyTitles
import com.fury.game.entity.character.player.content.emotes.EmotesManager
import com.fury.game.entity.character.player.content.objects.PrivateObjectManager
import com.fury.game.entity.character.player.info.DonorStatus
import com.fury.game.entity.character.player.info.GameMode
import com.fury.game.entity.character.player.info.PlayerRelations
import com.fury.game.entity.character.player.info.PlayerRights
import com.fury.game.entity.player.pet.PetManager
import com.fury.game.network.packet.OutgoingPacket
import com.fury.game.network.packet.out.Interface
import com.fury.game.network.packet.out.MapRegion
import com.fury.game.network.packet.out.SendLogout
import com.fury.game.node.entity.actor.figure.mob.extension.misc.Gravestone
import com.fury.game.node.entity.actor.figure.player.*
import com.fury.game.node.entity.actor.figure.player.handles.PointHandler
import com.fury.game.node.entity.actor.figure.player.handles.Settings
import com.fury.game.node.entity.actor.figure.player.handles.SettingsHandler
import com.fury.game.node.entity.actor.figure.player.handles.VariableHandler
import com.fury.game.node.entity.actor.figure.player.misc.MoneyPouch
import com.fury.game.node.entity.actor.figure.player.misc.Stopwatches
import com.fury.game.node.entity.actor.figure.player.misc.redo.DropLog
import com.fury.game.node.entity.actor.figure.player.misc.redo.KillsTracker
import com.fury.game.npc.familiar.Familiar
import com.fury.game.system.communication.ReferAFriend
import com.fury.game.system.communication.clans.ClanChat
import com.fury.game.system.files.loaders.item.WeaponAnimations
import com.fury.game.system.files.loaders.item.WeaponInterfaces
import com.fury.game.system.files.logs.PlayerLogger
import com.fury.game.world.GameWorld
import com.fury.game.world.World
import com.fury.game.world.map.Area
import com.fury.game.world.map.Position
import com.fury.game.world.map.path.RouteEvent
import com.fury.game.world.update.flag.Flag
import com.fury.game.world.update.flag.block.Animation
import com.fury.game.world.update.flag.block.Appearance
import com.fury.game.world.update.flag.block.ChatMessage
import com.fury.network.PlayerSession
import com.fury.network.SessionState
import com.fury.network.packet.PacketSender
import com.fury.util.FontUtils
import com.fury.util.FrameUpdater
import com.fury.util.Misc
import com.fury.util.Utils
import java.util.*

class Player : Figure {

    constructor() : super(GameSettings.DEFAULT_POSITION.copyPosition()) {
        session = Optional.empty()
    }

    constructor(playerIO: PlayerSession) : super(GameSettings.DEFAULT_POSITION.copyPosition()) {
        session = Optional.of(playerIO)
    }

    /* Handlers */

    val trade = Trade(this)
    val prayer = Prayer(this)
    val config = ConfigManager(this)
    val referAFriend = ReferAFriend(this)
    val emotesManager = EmotesManager(this)
    val logger = PlayerLogger(this)
    val questManager = QuestManager(this)
    val bonusManager = BonusManager()
    val pointsHandler = PointsHandler(this)
    val packetSender = PacketSender(this)
    val appearance = Appearance(this)
    val frameUpdater = FrameUpdater()
    val skills = Skills(this)
    val actionManager = ActionManager(this)
    val auraManager = AuraManager()
    val relations = PlayerRelations(this)
    val chatMessages = ChatMessage()
    val dueling = OldDueling(this)
    val slayerManager = SlayerManager(this)
    val summoning = Infusion(this)
    val controllerManager = ControllerManager()
    val save = PlayerSave(this)

    val moneyPouch = MoneyPouch(this)

    val petManager = PetManager(this)
    val points = PointHandler(this)
    val vars = VariableHandler()
    val statue = StatueHandler(this)

    val dungManager = DungManager()
    val dialogueManager = DialogueManager(this)
    val actualBank = Bank(this)
    val herbPouch = HerbPouch(this)
    val gemBag = GemBag(this)
    val inventory = Inventory(this)
    val equipment = Equipment(this)
    val priceChecker = PriceChecker(this)
    val farmingManager = FarmingManager(this)

    val dialogueFactory = DialogueFactory(this)

    val playerKillingAttributes = PlayerKillingAttributes(this)
    val minigameAttributes = MinigameAttributes()
    val bankPinAttributes = BankPinAttributes()
    var achievementAttributes = AchievementAttributes()

    val combatDefinitions = CombatDefinitions(this)

    val timers = Stopwatches()
    val killsTracker = KillsTracker()
    val dropLogs = DropLog()
    val viewport = PlayerViewport(this)
    val notes = NoteHandler(this)
    val settings = SettingsHandler(this)
    val messenger = Messenger(this)

    //Details
    var username: String = ""
    var longUsername: Long? = null
    var passwordHash: String? = null
    var totalPlayTime: Long = 0
    var creationTime: Long = 0

    /*** LONGS  */

    //timestamps & delays

    //TODO won't be needed for new combat system
    var stopAttackDelay: Long = 0

    fun addStopDelay(delay: Long) {
        stopAttackDelay = Utils.currentTimeMillis() + delay * 600
    }

    /*** INSTANCES  */

    //Songs
    private var soundsActive: Boolean = true
    private var musicActive: Boolean = true

    fun soundsActive(): Boolean {
        return soundsActive
    }

    fun musicActive(): Boolean {
        return musicActive
    }

    private var songs: MutableList<Boolean> = ArrayList()
    var playlist = ArrayList<Int>()

    fun getSongs(): List<Boolean> {
        return songs
    }

    fun setSongs(list: MutableList<Boolean>) {
        songs = list
    }

    fun addPlaylistSong(song: Int) {
        playlist.add(song)
    }

    fun removePlaylistSong(index: Int) {
        playlist.removeAt(index)
    }

    fun hasSongUnlocked(index: Int): Boolean {
        return songs[index]
    }

    fun setSong(index: Int, unlocked: Boolean) {
        songs[index] = unlocked
    }

    fun addSong(unlocked: Boolean) {
        songs.add(unlocked)
    }

    //Owned objects
    private var ownedObjectsManagerKeys: LinkedList<String>? = null
    val ownedObjectManagerKeys: LinkedList<String>
        get() {
            if (ownedObjectsManagerKeys == null)
                ownedObjectsManagerKeys = LinkedList()
            return ownedObjectsManagerKeys!!
        }

    fun setOwnedObjectManagerKeys(ownedObjectManagerKeys: Array<String>) {
        val linkedList = LinkedList<String>()
        for (link in ownedObjectManagerKeys)
            linkedList.add(link)
        this.ownedObjectsManagerKeys = linkedList
    }

    //Session
    val session: Optional<PlayerSession>

    //Rights
    var rights = PlayerRights.PLAYER
    private var rightsData: PlayerRightsData? = null
    val rightsId: Int
        get() {
            return when {
                rights.ordinal > 0 -> rights.ordinal
                gameMode.isIronMan -> 11
                isDonor -> 12 + DonorStatus.get(this).ordinal
                else -> 0
            }
        }

    fun getRightsData(): PlayerRightsData {
        if (rightsData == null)
            rightsData = PlayerRightsData(this)
        return rightsData!!
    }

    var gameMode = GameMode.REGULAR

    var characterAnimations = CharacterAnimations()
        set(equipmentAnimation) {
            field = equipmentAnimation.clone()
        }


    var fightType = FightType.UNARMED_PUNCH

    //Prayerbook
    var prayerbook = Prayerbook.NORMAL
        set(value) {
            field = value
            prayer.refresh()
        }

    //Spellbook
    var spellbook = MagicSpellBook.NORMAL
        private set
    fun setSpellBook(book: MagicSpellBook, loggingIn: Boolean): Player {
        if (book == MagicSpellBook.ANCIENT && !loggingIn)
            Achievements.finishAchievement(this, Achievements.AchievementData.SWITCH_ANCIENT_SPELLBOOK)
        else if (book == MagicSpellBook.LUNAR && !loggingIn)
            Achievements.finishAchievement(this, Achievements.AchievementData.SWITCH_LUNAR_SPELLBOOK)
        if (book == MagicSpellBook.DUNGEONEERING)
            isDungeoneeringSpellbook = true
        else if (isDungeoneeringSpellbook)
            isDungeoneeringSpellbook = false

        this.spellbook = book
        return this
    }

    //Loyalty stuff
    var loyaltyTitle = LoyaltyTitles.NONE
    private var loyaltyTick = 0
    var unlockedLoyaltyTitles = BooleanArray(12)
    var boughtLoyaltyTitles = BooleanArray(LoyaltyProgramme.LoyaltyTitles.values().size)
    fun setUnlockedLoyaltyTitle(index: Int) {
        unlockedLoyaltyTitles[index] = true
    }

    fun setBoughtLoyaltyTitle(index: Int) {
        boughtLoyaltyTitles[index] = true
    }

    //Clanchat
    var currentClanChat: ClanChat? = null
    var clanChatName: String? = null

    //Shop
    var shop: Shop? = null
    var isShopping: Boolean = false

    //Interacting
    var interactingObject: GameObject? = null

    var interactingItem: Item? = null
    var playerInteractingOption = PlayerInteractingOption.NONE
    var selectedSkillingItem: Item? = null

    //Cannon
    val decayedCannons: Deque<DwarfMultiCannon.CannonType> = ArrayDeque()
    var isSettingUpCannon: Boolean = false

    //Combat
    var autoCastSpell: CombatSpell? = null
    var castSpell: CombatSpell? = null
    var previousCastSpell: CombatSpell? = null
    var rangedWeaponData: RangedWeaponData? = null
    var combatSpecial: CombatSpecial? = null
    var weapon: WeaponInterface? = null
    @get:JvmName("hasVengeance")
    var hasVengeance: Boolean = false
    var isAutoRetaliate: Boolean = false
    var isAutoCast: Boolean = false
    var isDefensiveCasting = false
    var isCanPvp: Boolean = false
        set(canPvp) {
            field = canPvp
            packetSender.sendInteractionOption(if (canPvp) "Attack" else "null", 2, true)
            updateFlags.add(Flag.APPEARANCE)
        }

    var untradeableDropItem: Item? = null
    var usableObject: Array<Any>? = null

    var resetPosition: Position? = null

    //G.e
    var grandExchangeSlots = arrayOfNulls<GrandExchangeSlot>(6)
    var selectedGeSlot = -1
    var selectedGeItem: Item? = null
    var geQuantity: Int = 0
    var gePricePerItem: Int = 0
    fun setGrandExchangeSlot(index: Int, state: GrandExchangeSlot) {
        this.grandExchangeSlots[index] = state
    }

    //Clue scrolls
    var selectedScroll: Scroll? = null
    var clueScrolls = arrayOfNulls<ClueScroll>(4)
    fun getClueScroll(index: Int): ClueScroll? {
        return if (index >= 0 && index < clueScrolls.size - 1) clueScrolls[index] else null
    }

    fun setClueScroll(index: Int, clue: ClueScroll?) {
        if (index >= 0 && index < clueScrolls.size - 1)
            clueScrolls[index] = clue
    }
    var clueProgress: Int = 0


    //Extra animations
    var skillAnimation: Animation? = null
        set(value) {
            updateFlags.add(Flag.APPEARANCE)
            field = value
        }
    val deathAnimation: Animation
        get() = Animation(836)


    //Construction
    var house: House? = null

    //Summoning
    var familiar: Familiar? = null
    var pet: Pet? = null


    var routeEvent: RouteEvent? = null
    var duelConfigurations: DuelConfigurations? = null

    //Smithing
    var ores = IntArray(2)
    var smithingInterfaceType = 0

    //Max/comp cape interface
    var colourPresets = arrayOf(intArrayOf(-2805961, -5626324, -8643808, -12249823, -66051, -449772, -5342402), intArrayOf(-2805961, -5626324, -8643808, -12249823, -66051, -449772, -5342402), intArrayOf(-2805961, -5626324, -8643808, -12249823, -66051, -449772, -5342402))
    var rgbColours = intArrayOf(64234, 64212, 64189, 61986, 126, 892, 5718)
    var capeRecolours = intArrayOf(64234, 64212, 64189, 61986, 126, 892, 5718)

    //Interfaces
    private val tabInterfaces = IntArray(17)//+ 1 for logout
    var interfaceId: Int = 0
    var dialogueId: Int = 0
    var walkableInterfaceId = -1
    var closeInterfacesEvent: Runnable? = null
    fun openInterface(interfaceId: Int): Boolean {
        if (this.interfaceId == -1 && dialogueId == -1) {
            send(Interface(interfaceId))
            return true
        }

        message("Please close the interface you have open before opening another.")
        return false
    }

    fun getTabInterface(tabId: Int): Int {
        return tabInterfaces[tabId]
    }

    fun setTabInterface(tabId: Int, interfaceId: Int) {
        tabInterfaces[tabId] = interfaceId
    }

    //Dialogue
    var dialogueActionId: Int = 0
    var inputHandling: Input? = null

    //Bonus exp
    var minutesBonusExp = -1
        private set
    var minutesBonusMining = -1
        private set

    fun setMinutesBonusExp(minutesBonusExp: Int, add: Boolean) {
        this.minutesBonusExp = if (add) this.minutesBonusExp + minutesBonusExp else minutesBonusExp
    }

    fun setMinutesBonusMining(minutesBonus: Int, add: Boolean) {
        this.minutesBonusMining = if (add) this.minutesBonusMining + minutesBonus else minutesBonus
    }

    //Items
    var recoilCharges: Int = 0
    var effigy: Int = 0
    var dfsCharges: Int = 0
        private set

    fun incrementDfsCharges(amount: Int) {
        this.dfsCharges += amount
    }

    var currentBookPage: Int = 0//TODO temp attribute
    var furySharkTimer = -1//TODO convert to effect
    var millFilled = 0//TODO temp attribute
        private set

    fun addMillFilled(millFilled: Int) {
        this.millFilled += millFilled
    }

    var jarGeneratorCharge = 100
    private var timerTick = 0
    var gravestone = 0

    //Agility
    var crossedObstacles = BooleanArray(7)
    @get:JvmName("isCrossingObstacle")
    var crossingObstacle: Boolean = false


    //Resting
    @get:JvmName("isSitting")
    var sitting: Boolean = false
        set(value) {
            if (sitting)
                standUp()
            field = value
        }

    private fun standUp() {
        //Reset Standing Animation
        characterAnimations.reset()
        WeaponAnimations.update(this)
        updateFlags.add(Flag.APPEARANCE)
        //Stand Up Animation
        animate(11788)
        //Unlock Walking
        movement.unlock()
    }


    //TODO all can be temp attributes


    var isDying: Boolean = false
        private set

    @get:JvmName("experienceLocked")
    var experienceLocked: Boolean = false
    @get:JvmName("newPlayer")
    var newPlayer: Boolean = false

    @get:JvmName("isInactive")
    var inactive: Boolean = false
    @get:JvmName("didReceiveStarter")
    var receivedStarter: Boolean = false
    var isStunned = false
    @get:JvmName("inDream")
    var inDream = false
    @get:JvmName("hasStarted")
    var started: Boolean = false
    var isDungeoneeringSpellbook = false
        private set
    @get:JvmName("startedDungeoneeringWithRing")
    var dungeoneeringRing: Boolean = false
    var isInvulnerable: Boolean = false

    //Map regions
    @get:JvmName("clientHasLoadedMapRegion")
    var clientLoadedMapRegion: Boolean = false

    override fun loadMapRegions() {
        super.loadMapRegions()
        clientLoadedMapRegion = false

        if (isInDynamicRegion)
            packetSender.sendDynamicGameScene()
        else
            send(MapRegion())
    }

    @set:JvmName("setRegionChange")
    var isChangingRegion: Boolean = false
    var isAllowRegionChangePacket: Boolean = false
    fun loadRegion() {
        val surrounding = GameWorld.regions.getSurroundingRegions(this)

        PrivateObjectManager.refreshObjects(this)//TEMP

        for (region in surrounding) {
            region.sendGameObjects(this)
            region.sendFloorItems(this)

            //Npc Face
            region.getNpcs(z).filter { it != null && !it.combat.isInCombat() }.forEach { it.updateFlags.add(Flag.FACE_POSITION) }

            //Player Face
            region.getPlayers(z).filter { it != null && !it.combat.isInCombat() }.forEach { it.updateFlags.add(Flag.FACE_POSITION) }
        }
    }


    //Tzhaar
    var isCommandViewing: Boolean = false
    var converted: Boolean = false

    //Bank
    val bank: Bank
        get() = if (isCommandViewing && getRightsData().previewBank != null) getRightsData().previewBank else actualBank
    var isCommandBanking = false
    @get:JvmName("isBanking")
    var banking: Boolean = false

    fun setBank(items: Array<Array<Item>>) {
        actualBank.tabCount = items.size
        actualBank.refreshCapacity()
        for (i in items.indices)
            if (actualBank.tabExists(i))
                actualBank.tab(i)!!.items = items[i]
    }

    //Christmas event
    var christmasCharacters = BooleanArray(ChristmasPartyCharacters.values().size)
    var christmasEventStage: Int = 0
    val christmasCharactersFound: Int
        get() {
            var count = 0
            for (character in christmasCharacters)
                if (character)
                    count++

            return count
        }

    fun getChristmasCharacter(index: Int): Boolean {
        return christmasCharacters[index]
    }

    fun setChristmasCharacter(index: Int, christmasCharacters: Boolean) {
        this.christmasCharacters[index] = christmasCharacters
    }

    /*
     * Getters & Setters
     */

    //Donor
    val isDonor: Boolean
        get() = DonorStatus.get(this) != DonorStatus.NONE

    //Yell
    val yellColour: Int
        get() {
            if (rights.ordinal > 0)
                return rights.yellColour
            else if (isDonor)
                return DonorStatus.get(this).yellColour

            return 0
        }

    val yellName: String?
        get() {
            if (rights.ordinal > 0)
                return if (rights == PlayerRights.COMMUNITY_MANAGER)
                    "CM"
                else
                    Misc.uppercaseFirst(rights.name)
            else if (isDonor)
                return Misc.uppercaseFirst(DonorStatus.get(this).name.replace("_DONOR".toRegex(), "").replace("_".toRegex(), " "))

            return null
        }

    val yellDelay: Int
        get() {
            if (rights.ordinal > 0)
                return rights.yellDelay
            else if (isDonor)
                return DonorStatus.get(this).yellDelay
            return -1
        }

    //Prayer
    var prayerDelay: Long
        get() {
            return if (temporaryAttributes["PrayerBlocked"] != null) temporaryAttributes["PrayerBlocked"] as Long else 0L
        }
        set(delay) {
            temporaryAttributes["PrayerBlocked"] = delay + Utils.currentTimeMillis()
            prayer.closeAllPrayers()
        }


    //Locations/activities
    val isAtBarrows: Boolean
        get() = controllerManager.controller is Barrows
    val isInWilderness: Boolean
        get() = controllerManager.controller is Wilderness
    val isDueling: Boolean
        get() = duelConfigurations != null


    //Temp, eventually use kotlin extension functions
    @JvmOverloads
    fun message(message: String, filter: Boolean = false) {
        messenger.send(message, filter)
    }

    @JvmOverloads
    fun message(message: String, colour: Int, filter: Boolean = false) {
        messenger.send(message, colour, filter)
    }

    val dropRate: Double
        get() = gameMode.dropRate + DonorStatus.get(this).dropRate +
                if (equipment.get(Slot.RING).id == 15014) 0.04
                else if (equipment.get(Slot.RING).id == 23643) 0.075
                else if (equipment.get(Slot.RING).id == 15398) 0.055
                else if (Equipment.wearingRingOfWealth(this)) 0.02
                else 0.0

    val isInDungeoneering: Boolean
        get() = controllerManager.controller is DungeonController


    /** Overrides **/
//    override var size = 1
    override var sizeX = 1
    override var sizeY = 1

    override val blockDeflections: Boolean
        get() = false

    override val magePrayerMultiplier: Double
        get() = 0.6

    override val rangePrayerMultiplier: Double
        get() = 0.6

    override val meleePrayerMultiplier: Double
        get() = if (temporaryAttributes.remove("verac_effect") != null && PlayerCombatAction.hasVeracsSet(this)) 1.0 else 0.6

    override val isDead: Boolean
        get() = super.isDead || isDying

    override val maxConstitution: Int
        get() = skills.getMaxLevel(Skill.CONSTITUTION) + equipment.hpIncrease

    override var run: Boolean
        get() {
            val weapon = equipment.get(Slot.WEAPON).id
            val unwalkables = intArrayOf(7671, 7673)
            for (unwalkable in unwalkables)
                if (weapon == unwalkable)
                    return false
            return settings.getBool(Settings.RUNNING)
        }
        set(value) {
            super.run = value
        }

    fun appendDeath(position: Position) {
        GameWorld.schedule(PlayerDeath(this, position))
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Player)
            return false

        val p = other as Player?
        return p!!.index == index || p.username == username
    }

    var setTarget: Boolean = false

    override fun processCharacter() {
        session.ifPresent { it -> it.handleQueuedMessages(false) }

        if(combat is PlayerCombat && setTarget) {
            if(!actionManager.hasAction())
                actionManager.action = PlayerCombatAction(combat.target)
            combat.target(null)
            setTarget = false
        }

        actionManager.process()

        /** SKILLS  */

        controllerManager.process()

        farmingManager.process()

        prayer.processPrayer()

        EnergyHandler.processEnergyRestore(this)
        /** MISC  */

        //TODO move to loyalty.process()
        if (!inactive) {
            if (loyaltyTick >= 6) {
                LoyaltyProgramme.incrementPoints(this)
                loyaltyTick = 0
            }
            loyaltyTick++
        }

        if (timerTick >= 60) {
            packetSender.sendString(39167, FontUtils.ORANGE_2 + "Time played:  " + FontUtils.YELLOW + Misc.getTimePlayed(totalPlayTime + timers.login.elapsed()) + FontUtils.COL_END + FontUtils.COL_END)
            timerTick = 0
        }
        timerTick++

        BountyHunter.sequence(this)

        super.processCharacter()

        if(combat is PlayerCombat && combat.target != null)
            setTarget = true

        if (routeEvent != null && routeEvent!!.process(this))
            routeEvent = null
    }

    override fun reset() {
        super.reset()
        combat.freezeDelay = 0
        settings.set(Settings.SPECIAL_ENERGY, 100)
        settings.isSpecialToggled = false
        CombatSpecial.updateBar(this)
        hasVengeance = false
        effects.resetEffects()
        animate(-1)
        WeaponInterfaces.assign(this, equipment.get(Slot.WEAPON))
        WeaponAnimations.update(this)
        prayer.reset()
        equipment.refresh()
        inventory.refresh()
        skills.reset()
        settings.set(Settings.RUN_ENERGY, 100.0)
        isDying = false
        movement.unlock()
        movement.reset()
        updateFlags.add(Flag.APPEARANCE)
    }

    override fun deregister() {
        if (finished)
            return
        finished = true

        if (!isRegistered)
            return

        PlayerHandler.handleLogout(this)
        GameWorld.players.remove(this)
        World.updateEntityRegion(this)
        GameWorld.cancelTask(this, true)
    }

    override fun register() {
        if (started)
            return
        started = true
        if (!isRegistered)
            GameWorld.players.add(this)
        loadMapRegions()
        checkMulti()
        Gravestone.linkPlayer(this)

        World.updateEntityRegion(this)
    }

    override fun checkMulti() {
        if (!started)
            return
        val isAtMultiArea = isInDynamicRegion || Area.isMulti(this)
        if (isAtMultiArea && !inMulti()) {
            setInMulti(isAtMultiArea)
            packetSender.sendMultiIcon(1)
        } else if (!isAtMultiArea && inMulti()) {
            setInMulti(isAtMultiArea)
            packetSender.sendMultiIcon(0)
        }
    }

    @JvmOverloads
    fun save(force: Boolean = false) {
        if (force)
            save.run()
        else
            session.ifPresent { it ->
                if (it.state != SessionState.LOGGING_IN && it.state != SessionState.LOGGING_OUT)
                    save.run()
            }
    }


    /**
     * TODO sort out these
     */

    @JvmOverloads
    fun logout(force: Boolean = false) {
        if (!canLogout() && !force) {
            return
        }

        session.get().state = SessionState.REQUESTED_LOG_OUT
        lastKnownRegion = null
        isNeedsPlacement = false
        isChangingRegion = false


        send(SendLogout())
        //		setVisible(false);
        GameWorld.queueLogout(this)
    }

    fun canLogout(): Boolean {
        if (combat.attackedByDelay + GameSettings.COMBAT_LOGOUT_TIME > Utils.currentTimeMillis()) {
            message("You can't log out until " + GameSettings.COMBAT_LOGOUT_TIME / 1000 + " seconds after the end of combat.")
            return false
        }

        if (health.hitpoints <= 0 || isSettingUpCannon) {
            message("You cannot log out at the moment.")
            return false
        }

        if (movement.isLocked() && actionManager.action !is ItemMorph && !(movement.isLocked() && save.disabled)) {
            message("You can't log out while performing an action.")
            return false
        }

        return true
    }

    fun busy(): Boolean {
        return interfaceId > 0 || banking || isShopping || trade.inTrade() || dueling.inDuelScreen || settings.isResting
    }

    fun send(encoder: OutgoingPacket) {
        if (!session.isPresent)
            return
        encoder.execute(this)
    }

    //Item checks/player search
    fun hasItemOnThem(itemId: Int): Boolean {
        return if (inventory.contains(Item(itemId))) true else equipment.contains(Item(itemId))
    }

    fun hasItem(itemId: Int): Boolean {
        return hasItem(Item(itemId))
    }

    fun hasItem(item: Item): Boolean {
        if (inventory.contains(item))
            return true

        if (equipment.contains(item))
            return true

        if (actualBank.contains(item))
            return true

        return if (familiar != null && familiar!!.beastOfBurden != null && familiar!!.beastOfBurden!!.contains(item)) true else World.hasGroundItem(item.id, this)
    }

    @JvmOverloads
    fun stopAll(stopWalk: Boolean = true, stopInterfaces: Boolean = true, stopActions: Boolean = true) {
        routeEvent = null
        if (stopInterfaces) {
            packetSender.sendInterfaceRemoval()
            inputHandling = null
        }
        movement.teleporting = false
        selectedSkillingItem = null
        direction.interacting = null
        if (stopWalk)
            movement.reset()
        settings.restingState = 0
        if (stopActions)
            actionManager.forceStop()
    }

    fun setAntipoisonDelay(cycles: Int) {
        effects.startEffect(Effect(Effects.ANTIPOISON, cycles))
    }

    fun kick() {
        if (!username.equals("Greg", ignoreCase = true)) {
            logout()
            deregister()
        }
    }

    override fun toString(): String {
        return "Player: $username ${super.toString()} ${Arrays.toString(equipment.ids)}"
    }
}

package com.fury.network.packet;

import com.fury.network.packet.impl.*;

public class PacketConstants {

	public static final PacketListener[] PACKETS = new PacketListener[257];
	public static int EQUIP_ITEM_OPCODE = 41;

	public static final int ITEM_CONTAINER_OPCODE = 145;
	public static final int NOTE_TEXT_OPCODE = 104;
	public static final int NOTE_COMMAND_OPCODE = 105;
	public final static int PICK_UP_ITEM_ACTION_OPCODE = 236, SECOND_PICK_UP_ITEM_ACTION_OPCODE = 253;
	public static final int ATTACK_NPC = 72, FIRST_CLICK_OPCODE = 155, MAGE_NPC = 131, SECOND_CLICK_OPCODE = 17, THIRD_CLICK_OPCODE = 21, FOURTH_CLICK_OPCODE = 18;
	public static final int ENTER_AMOUNT_OPCODE = 208, ENTER_SYNTAX_OPCODE = 60;
	public final static int USE_ITEM = 122;
	public final static int ITEM_ON_NPC = 57;
	public final static int ITEM_ON_ITEM = 53;
	public final static int ITEM_ON_OBJECT = 192;
	public final static int ITEM_ON_GROUND_ITEM = 25;
	public static final int ITEM_ON_PLAYER = 14;
	public static final int DIALOGUE_OPCODE = 40;
	public static final int ADD_FRIEND_OPCODE = 188;
	public static final int REMOVE_FRIEND_OPCODE = 215;
	public static final int ADD_IGNORE_OPCODE = 133;
	public static final int REMOVE_IGNORE_OPCODE = 74;
	public static final int PRIVATE_MESSAGE_OPCODE = 126;
	public static final int COMMAND_MOVEMENT_OPCODE = 98;
	public static final int GAME_MOVEMENT_OPCODE = 164;
	public static final int MINIMAP_MOVEMENT_OPCODE = 248;
	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70, FOURTH_CLICK = 234, FIFTH_CLICK = 228;
	public static final int THIRD_ITEM_ACTION_OPCODE = 16;
	public static final int PLAYER_OPTION_OPCODE = 73;
	public static final int SECOND_ITEM_ACTION_OPCODE = 75;
	public static final int PLAYLIST_COMMAND_OPCODE = 106;
	public static final int FIRST_ITEM_ACTION_OPCODE = 122;
	public static final int ACCEPT_INVITE_OPCODE = 127;
	public static final int ACCEPT_CHALLENGE_OPCODE = 128;
	public static final int ACCEPT_TRADE_OPCODE = 139;
	public static final int PARTY_INVITATION_OPCODE = 140;
	public static final int MAGIC_ON_GROUNDITEMS_OPCODE = 181;
	public static final int FAMILIAR_ATTACK_NPC_OPCODE = 231;
	public static final int FAMILIAR_ATTACK_PLAYER_OPCODE = 232;
	public static final int MAGIC_ON_ITEMS_OPCODE = 237;
	public static final int MAGIC_ON_PLAYER_OPCODE = 249;
	public static final int MAGIC_ON_OBJECT_OPCODE = 35;
	public static final int SPECIAL_ON_ITEMS_OPCODE = 239;

	static {
		for(int i = 0; i < PACKETS.length; i++)
			PACKETS[i] = new SilencedPacketListener();
		PACKETS[0] = new PingPacketListener();
		PACKETS[2] = new ExamineItemPacketListener();
		PACKETS[3] = new ClientFocusPacketListener();
		PACKETS[4] = PACKETS[230] = new ChatPacketListener();
		PACKETS[5] = new SendClanChatMessagePacketListener();
		PACKETS[6] = new ExamineNpcPacketListener();
		PACKETS[7] = new WithdrawMoneyFromPouchPacketListener();
		PACKETS[8] = new ChangeRelationStatusPacketListener();
		PACKETS[11] = new ChangeAppearancePacketListener();
		PACKETS[41] = new EquipPacketListener();
		PACKETS[87] = new DropItemPacketListener();
		PACKETS[103] = new CommandPacketListener();
		PACKETS[NOTE_COMMAND_OPCODE] = PACKETS[NOTE_TEXT_OPCODE] = new NotesPacketListener();
		PACKETS[121] = new FinalizedMapRegionChangePacketListener();
		PACKETS[129] = new CancelChatBoxPacketListener();
		PACKETS[130] = new CloseInterfacePacketListener();
		PACKETS[185] = new ButtonClickPacketListener();
		PACKETS[202] = new IdleLogoutPacketListener();
		PACKETS[MAGE_NPC] = new NpcOptionPacketListener();
		PACKETS[SECOND_CLICK_OPCODE] = new NpcOptionPacketListener();
		PACKETS[FOURTH_CLICK_OPCODE] = new NpcOptionPacketListener();
		PACKETS[THIRD_CLICK_OPCODE] = new NpcOptionPacketListener();
		PACKETS[210] = new RegionLoadedPacketListener();
		PACKETS[214] = new SwitchItemSlotPacketListener();
		PACKETS[PICK_UP_ITEM_ACTION_OPCODE] = new GroundItemActionPacketListener();
		PACKETS[SECOND_PICK_UP_ITEM_ACTION_OPCODE] = new GroundItemActionPacketListener();
		PACKETS[ATTACK_NPC] = PACKETS[FIRST_CLICK_OPCODE] =
				new NpcOptionPacketListener();
		PACKETS[ENTER_SYNTAX_OPCODE] =
				PACKETS[ENTER_AMOUNT_OPCODE] = new EnterInputPacketListener();
		PACKETS[USE_ITEM] = PACKETS[ITEM_ON_GROUND_ITEM] = PACKETS[ITEM_ON_ITEM] =
				PACKETS[ITEM_ON_NPC] = PACKETS[ITEM_ON_OBJECT] =
				PACKETS[ITEM_ON_PLAYER] = new UseItemPacketListener();
		PACKETS[DIALOGUE_OPCODE] = new DialoguePacketListener();
		PACKETS[ADD_FRIEND_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[REMOVE_FRIEND_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[ADD_IGNORE_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[REMOVE_IGNORE_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[PRIVATE_MESSAGE_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[COMMAND_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[GAME_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[MINIMAP_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[FIRST_CLICK] = PACKETS[SECOND_CLICK] =
				PACKETS[THIRD_CLICK] = PACKETS[FOURTH_CLICK] =
				PACKETS[FIFTH_CLICK] = new ObjectActionPacketListener();
		PACKETS[ITEM_CONTAINER_OPCODE] = new ItemContainerActionPacketListener();
		PACKETS[SECOND_ITEM_ACTION_OPCODE] = PACKETS[THIRD_ITEM_ACTION_OPCODE] = PACKETS[FIRST_ITEM_ACTION_OPCODE] = new ItemActionPacketListener();
		PACKETS[MAGIC_ON_ITEMS_OPCODE] = new MagicOnItemsPacketListener();
		PACKETS[MAGIC_ON_GROUNDITEMS_OPCODE] = new MagicOnItemsPacketListener();
		PACKETS[SPECIAL_ON_ITEMS_OPCODE] = PACKETS[FAMILIAR_ATTACK_NPC_OPCODE] = PACKETS[FAMILIAR_ATTACK_PLAYER_OPCODE] = new SummoningFamiliarPacketListener();
		PACKETS[PARTY_INVITATION_OPCODE] = new PartyInvitationPacketListener();
		PACKETS[MAGIC_ON_PLAYER_OPCODE] = new MagicOnPlayerPacketListener();
		PACKETS[MAGIC_ON_OBJECT_OPCODE] = new MagicOnObjectPacketListener();

		PACKETS[PLAYER_OPTION_OPCODE] = new PlayerOptionPacketListener();
		PACKETS[ACCEPT_TRADE_OPCODE] = new AcceptTradePacketListener();
		PACKETS[ACCEPT_CHALLENGE_OPCODE] = new AcceptChallengePacketListener();
		PACKETS[ACCEPT_INVITE_OPCODE] = new AcceptInvitePacketListener();
		PACKETS[204] = new GESelectItemPacketListener();
		PACKETS[222] = new ClickTextMenuPacketListener();
		PACKETS[229] = new HeightCheckPacketListener();
		PACKETS[PLAYLIST_COMMAND_OPCODE] = new PlaylistPacketListener();
	}
}

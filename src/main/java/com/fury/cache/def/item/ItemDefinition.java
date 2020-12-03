package com.fury.cache.def.item;

import com.fury.cache.Revision;
import com.fury.cache.def.Definition;
import com.fury.cache.def.Loader;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.equipment.ItemBonuses;
import com.fury.game.system.files.loaders.item.ItemDefinitions;

import java.util.HashMap;

/**
 * Created by Greg on 16/01/2017.
 */
public abstract class ItemDefinition extends Definition {
    public int id;
    public String name;
    public int value;
    public int equipSlot;
    public int equipType;
    public int equipType2;
    public boolean members;
    public String[] groundOptions;
    public String[] options;
    public int maleDialogue, femaleDialogue;
    public Revision revision;
    public HashMap<Integer, Object> parameters;
    public int team;
    public boolean stackable;
    public int modelId;
    public boolean noted;
    public boolean lended;
    public boolean binded;
    public int noteId;
    public int notedTemplateId;
    public int lendId;
    public int lendTemplateId;
    public int bindId;
    public int bindTemplateId;
    public int[] stackIds;
    public int[] stackAmounts;
    public Revision[] stackRevisions;

    public int tertiaryFemaleEquipmentModel;
    public int secondaryFemaleModel;
    public int primaryMaleModel;
    public int secondaryMaleDialogueHead;
    public int secondaryFemaleDialogueHead;
    public int primaryMaleDialogueHead;
    public int tertiaryMaleEquipmentModel;
    public int secondaryMaleModel;
    public int primaryFemaleDialogueHead;
    public int primaryFemaleModel;

    public ItemDefinition(int id) {
        this.id = id;
        setDefaults();
    }

    @Override
    public void changeValues() {
        if (notedTemplateId != -1 && noteId != -1)
            toNote();
        if (lendTemplateId != -1 && lendId != -1)
            toLend();
        if (bindTemplateId != -1 && bindId != -1)
            toBind();

        if (id == 5733 && options != null && options.length >= 5)
            options[4] = "Destroy";

        switch (revision) {
            case PRE_RS3:
                switch (id) {
                    case 22985://Christmas wand
                        options = new String[]{null, "Wield", null, null, "Drop"};
                        break;
                    case 20712:
                        name = "Vote Book";
                        stackable = true;

                        options = new String[]{"Read", null, null, null, null};
                        break;
                }
                break;
            case RS2:
                switch (id) {
                    case 14691:
                        name = "PvP Box";
                        options = new String[] {"Open", null, null, null, "Destroy"};
                        break;
                    case 15014:
                        name = "Ring of wealth (i)";
                        break;
                    case 14701://Herb pouch
                        options = new String[] {"Fill", null, "Empty", "Check", "Destroy"};
                        break;
                    case 16:
                        name = "Ironman whistle";
                        options = new String[]{"Blow", null, null, null, "Destroy"};
                        break;
                    case 20712:
                        name = "Vote Book";
                        stackable = true;

                        options = new String[]{"Read", null, null, null, null};
                        break;
                    case 15017:
                        options[4] = "Destroy";
                        break;
                    case 6758:
                        name = "$10 scroll";
                        options = new String[]{"Claim", null, null, null, null};
                        break;
                    case 608:
                        name = "$25 scroll";
                        options = new String[]{"Claim", null, null, null, null};
                        break;
                    case 607:
                        name = "$50 scroll";
                        options = new String[]{"Claim", null, null, null, null};
                        break;
                    case 14808:
                        name = "$150 scroll";
                        options = new String[]{"Claim", null, null, null, null};
                        break;
                }
                break;
            case RS3:
                switch (id) {
                    case 33596:
                        name = "Snowflake";
                        stackable = true;
                        break;
                }
                break;
        }
    }

    public boolean isNoted() {
        return noted;
    }

    public boolean isMembers() {
        return members;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setDefaults() {
        modelId = 0;
        equipType = 0;
        team = -1;
        name = "";
        stackable = false;
        value = 1;
        members = false;
        groundOptions = null;
        options = null;
        maleDialogue = -1;
        femaleDialogue = -1;
        noteId = -1;
        notedTemplateId = -1;
        lendId = -1;
        lendTemplateId = -1;
        bindId = -1;
        bindTemplateId = -1;
    }

    public boolean isBindItem() {
        if (options == null)
            return false;
        for (String option : options) {
            if (option == null)
                continue;
            if (option.equalsIgnoreCase("bind"))
                return true;
        }
        return false;
    }

    public void toNote() {
        ItemDefinition definition = Loader.getItem(notedTemplateId, revision);
        modelId = definition.modelId;
        ItemDefinition realItem = Loader.getItem(noteId, revision);
        members = realItem.members;
        value = realItem.value;
        name = realItem.name;
        stackable = true;
        noted = true;
        parameters = realItem.parameters;
    }

    public void toLend() {
        ItemDefinition realItem = Loader.getItem(lendId, revision);
        team = realItem.team;
        members = realItem.members;
        value = 0;
        name = realItem.name;
        options = new String[5];
        if (realItem.options != null)
            for (int optionIndex = 0; optionIndex < 4; optionIndex++)
                options[optionIndex] = realItem.options[optionIndex];
        options[4] = "Discard";
        groundOptions = realItem.groundOptions;
        parameters = realItem.parameters;
        lended = true;
    }

    public void toBind() {
        ItemDefinition realItem = Loader.getItem(bindId, revision);
        team = realItem.team;
        members = realItem.members;
        value = 0;
        name = realItem.name;
        options = new String[5];
        if (realItem.options != null)
            for (int optionIndex = 0; optionIndex < 4; optionIndex++)
                options[optionIndex] = realItem.options[optionIndex];
        options[4] = "Destroy";
        groundOptions = realItem.groundOptions;
        parameters = realItem.parameters;
        binded = true;
    }

    public String getName() {
        return name;
    }

    public HashMap<Integer, Integer> getSkillRequirements() {
        if (parameters == null)
            return null;
        HashMap<Integer, Integer> skills = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            Integer skill = (Integer) parameters.get(749 + (i * 2));
            if (skill != null) {
                Integer level = (Integer) parameters.get(750 + (i * 2));
                if (level != null)
                    skills.put(skill, level);
            }
        }
        Integer maxedSkill = (Integer) parameters.get(277);
        if (maxedSkill != null)
            skills.put(maxedSkill, id == 19709 ? 120 : 99);

        switch (id) {
            case 14936:
            case 14937:
            case 14938:
            case 14939://Agile top/legs
                skills.clear();
                break;
            case 15241:
                skills.put(Skill.RANGED.ordinal(), 75);
                skills.put(Skill.FIREMAKING.ordinal(), 61);
                break;
            case 21371:
            case 21372:
            case 21373:
            case 21374:
            case 21375://Vine whips
                skills.put(Skill.ATTACK.ordinal(), 85);
                break;
            case 10887://Barrelchest anchor
                skills.put(Skill.ATTACK.ordinal(), 60);
                skills.put(Skill.STRENGTH.ordinal(), 40);
                break;
            case 7460://Rune gloves
                skills.put(Skill.DEFENCE.ordinal(), 34);
                break;
            case 7461:
                skills.put(Skill.DEFENCE.ordinal(), 36);
                break;
            case 7462:
                skills.put(Skill.DEFENCE.ordinal(), 41);
                break;
            case 12674:
            case 12675://Berserker helms
            case 12676:
            case 12677://Charged warrior helms
            case 12672:
            case 12673://Charged archer helms
            case 12678:
            case 12679://Charged farseer helms
                skills.put(Skill.SUMMONING.ordinal(), 30);
                skills.put(Skill.DEFENCE.ordinal(), 45);
                break;
            case 12680:
            case 12681://Charged neitiznot helms
                skills.put(Skill.SUMMONING.ordinal(), 45);
                skills.put(Skill.DEFENCE.ordinal(), 55);
                break;
            case 19784:
            case 22401:
            case 19780: //Korasi's sword
                skills.put(Skill.MAGIC.ordinal(), 80);
                skills.put(Skill.FIREMAKING.ordinal(), 71);
                skills.put(Skill.CONSTRUCTION.ordinal(), 70);
                skills.put(Skill.CRAFTING.ordinal(), 70);
                skills.put(Skill.SMITHING.ordinal(), 70);
                skills.put(Skill.SUMMONING.ordinal(), 55);
                skills.put(Skill.DEFENCE.ordinal(), 25);
                skills.put(Skill.ATTACK.ordinal(), 78);
                skills.put(Skill.STRENGTH.ordinal(), 78);
                break;
            case 20822:
            case 20823:
            case 20824:
            case 20825:
            case 20826://2nd set of primal?
                skills.put(Skill.DEFENCE.ordinal(), 99);
                break;
            case 8846://steel defender
                skills.put(Skill.ATTACK.ordinal(), 5);
                skills.put(Skill.DEFENCE.ordinal(), 5);
                break;
            case 8847://black defender
                skills.put(Skill.ATTACK.ordinal(), 10);
                skills.put(Skill.DEFENCE.ordinal(), 10);
                break;
            case 8848://mithril defender
                skills.put(Skill.ATTACK.ordinal(), 20);
                skills.put(Skill.DEFENCE.ordinal(), 20);
                break;
            case 8849://adamant defender
                skills.put(Skill.ATTACK.ordinal(), 30);
                skills.put(Skill.DEFENCE.ordinal(), 30);
                break;
            case 8850://rune defender
                skills.put(Skill.ATTACK.ordinal(), 40);
                skills.put(Skill.DEFENCE.ordinal(), 40);
                break;
            case 20072://dragon defender
                skills.put(Skill.ATTACK.ordinal(), 60);
                skills.put(Skill.DEFENCE.ordinal(), 60);
                break;
            case 8839:
            case 8840:
            case 8841:
            case 8842:
            case 10611:
            case 11663:
            case 11664:
            case 11665:
            case 11674:
            case 11675:
            case 11676://Void
                skills.put(Skill.DEFENCE.ordinal(), 42);
                skills.put(Skill.CONSTITUTION.ordinal(), 42);
                skills.put(Skill.RANGED.ordinal(), 42);
                skills.put(Skill.ATTACK.ordinal(), 42);
                skills.put(Skill.MAGIC.ordinal(), 42);
                skills.put(Skill.STRENGTH.ordinal(), 42);
                break;
            case 19785:
            case 19786:
            case 19787:
            case 19788:
            case 19789:
            case 19790://Elite void
                skills.put(Skill.ATTACK.ordinal(), 78);
                skills.put(Skill.STRENGTH.ordinal(), 78);
                skills.put(Skill.MAGIC.ordinal(), 80);
                skills.put(Skill.CONSTITUTION.ordinal(), 42);
                skills.put(Skill.RANGED.ordinal(), 42);
                skills.put(Skill.PRAYER.ordinal(), 22);
                break;
        }

        return skills;
    }

    public ItemDefinition morph(int amount) {
        if (amount > 1) {
            int id = -1;
            Revision rev = null;

            for (int index = 0; index < 10; index++) {
                if (amount >= stackAmounts[index] && stackAmounts[index] != 0) {
                    id = stackIds[index];
                    rev = stackRevisions[index];
                }
            }

            if (id != -1)
                return Loader.getItem(id, rev);
        }
        return this;
    }

    public boolean isDestroyItem() {
        if (options == null)
            return false;
        for (String option : options) {
            if (option == null)
                continue;
            if (option.equalsIgnoreCase("destroy"))
                return true;
        }
        return false;
    }

    public int getValue() {
        return ItemDefinitions.forId(id).getValue();
        //return value;
    }

    public int getEquipLookHideSlot() {
        return equipType;
    }

    public int getEquipLookHideSlot2() {
        return equipType2;
    }

    public boolean hideArms() {
        return getEquipLookHideSlot() == 6;
    }

    public boolean hideHair() {
        return getEquipLookHideSlot() == 8;
    }

    public boolean hideJaw() {
        return getEquipLookHideSlot() == 11;
    }

    public boolean leftHanded() {
        return getEquipLookHideSlot() == 5;
    }

    public int getAttackSpeed() {
        if (parameters == null)
            return 4;
        Object attackSpeed = parameters.get(14);
        if (attackSpeed != null && attackSpeed instanceof Integer)
            return (int) attackSpeed;
        return 4;
    }

    public boolean containsOption(int index, String option) {
        if (options == null || options[index] == null || options.length <= index)
            return false;
        return options[index].equals(option);
    }

    public boolean hasOption(String op) {
        if (options == null)
            return false;

        for (String option : options) {
            if (option != null && option.equalsIgnoreCase(op))
                return true;
        }
        return false;
    }

    public String[] getOptions() {
        return options;
    }

    public int[] getBonuses() {
        return ItemBonuses.getItemBonuses(id/*, revision*/);
    }

    public boolean equipable() {
        return hasOption("Wear") || hasOption("Wield");
    }
}

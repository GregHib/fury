package com.fury.game.entity.character.player.content.emotes;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.core.model.item.Item;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.task.TickableTask;
import com.fury.game.container.impl.equip.Slot;
import com.fury.game.content.global.Achievements;
import com.fury.game.content.global.treasuretrails.TreasureTrailHandlers;
import com.fury.game.content.skill.Skill;
import com.fury.game.entity.character.combat.effects.Effects;
import com.fury.game.world.GameWorld;
import com.fury.game.world.update.flag.block.Animation;
import com.fury.game.world.update.flag.block.Direction;
import com.fury.game.world.update.flag.block.graphic.Graphic;
import com.fury.game.world.update.flag.block.graphic.GraphicHeight;
import com.fury.util.Misc;
import com.fury.util.RandomUtils;

public class EmotesManager {

    private long nextEmoteEnd;
    private Player player;

    public EmotesManager(Player player) {
        this.player = player;
    }

    public boolean isDoingEmote() {
        return nextEmoteEnd >= Misc.currentTimeMillis();
    }

    public void setNextEmoteEnd(long delay) {
        nextEmoteEnd = Misc.currentTimeMillis() + delay;
    }

    public boolean handleButton(int button) {
        if (isDoingEmote()) {
            player.message("Please wait till you've finished performing your current emote.");
            return true;
        }

        if(button == 154) {
            Item cape = player.getEquipment().get(Slot.CAPE);
            SkillcapeEmotes capeData = SkillcapeEmotes.forItem(cape);

            if(handleCapeEmotes(capeData))
                return true;
        }

        EmoteData emoteData = EmoteData.forButton(button);

        if(emoteData == null)
            return false;

        TreasureTrailHandlers.handleEmote(player, emoteData);

        emoteData = emoteData.transform(player);

        if(!handleCustomEmotes(emoteData)) {
            if (emoteData.animation != null)
                player.perform(emoteData.animation);
            if (emoteData.graphic != null)
                player.perform(emoteData.graphic);

            if (emoteData.animation != null)
                setNextEmoteEnd(Loader.getAnimation(emoteData.animation.getId(), Revision.RS2).getEmoteTime() * 2);
        }

        return true;
    }

    private boolean handleCapeEmotes(SkillcapeEmotes data) {
        if (data != null) {
            switch (data) {
                case QUEST_POINT:
                case DUNGEONEERING_MASTER:
                case MAX_CAPE:
                case COMPLETIONIST_CAPE:
                case VETERAN_CAPE:
                case CLASSIC_CAPE:
                    //Exceptions
                    break;
                default:
                    Skill skill = Skill.forId(data.ordinal());
                    if(!player.getSkills().hasRequirement(skill, 99, "perform this emote"))
                        return false;
                    break;
            }

            switch (data) {
                case DUNGEONEERING:
                    int rand = RandomUtils.inclusive(2);
                    setNextEmoteEnd(5 * 600);
                    player.animate(13190);
                    player.graphic(2442);
                    GameWorld.schedule(new TickableTask(false) {
                        @Override
                        public void tick() {
                            switch (getTick()) {
                                case 0:
                                    player.setTransformation(rand == 0 ? 11227 : rand == 1 ? 11228 : 11229);
                                    player.animate(rand == 0 ? 13192 : rand == 1 ? 13193 : 13194);
                                    break;
                                case 5:
                                    player.resetTransformation();
                                    stop();
                                    break;
                            }
                        }
                    });
                    break;
                case DUNGEONEERING_MASTER:
                    setNextEmoteEnd(9 * 600);
                    GameWorld.schedule(new TickableTask(true) {
                        Mob enemy1, enemy2, enemy3, enemy4;
                        @Override
                        public void tick() {
                            switch (getTick()) {
                                case 0:
                                    Graphic.sendGlobal(player, new Graphic(2781, GraphicHeight.HIGH), player.copyPosition());
                                    enemy1 = new Mob(-1, player.transform(-1, -1));
                                    break;
                                case 1:
                                    player.setTransformation(11229);
                                    player.animate(14608);
                                    player.getDirection().setInteracting(enemy1);
                                    enemy1.graphic(2777);
                                    enemy2 = new Mob(-1, player.transform(1, -1));
                                    break;
                                case 2:
                                    player.getDirection().setInteracting(null);
                                    player.getDirection().setDirection(Direction.SOUTH);
                                    enemy1.deregister();
                                    player.setTransformation(11228);
                                    player.animate(14609);
                                    player.graphic(2782);
                                    enemy2.graphic(2778);
                                    break;
                                case 3:
                                    enemy3 = new Mob(-1, player.transform(0, -1));
                                    enemy4 = new Mob(-1, player.transform(0, 1));
                                    break;
                                case 4:
                                    enemy2.deregister();
                                    player.setTransformation(11227);
                                    player.animate(14610);
                                    enemy3.graphic(2779);
                                    enemy4.graphic(2780);
                                    break;
                                case 8:
                                    player.graphic(2783);
                                    enemy3.deregister();
                                    enemy4.deregister();
                                    break;
                                case 9:
                                    player.resetTransformation();
                                    player.getPacketSender().sendAnimationReset();
                                    stop();
                                    break;
                            }
                        }
                    });
                    break;
                case MAX_CAPE:
                    setNextEmoteEnd(4 * 600);
                    GameWorld.schedule(new TickableTask(false) {
                        Mob mob;
                        @Override
                        public void tick() {
                            switch (getTick()) {
                                case 0:
                                    mob = new Mob(1224, player.transform(0, 1));
                                    Graphic.sendGlobal(player, new Graphic(2517), mob.copyPosition());
                                    mob.animate(1434);
                                    mob.graphic(1482);
                                    player.animate(1179);
                                    player.getDirection().setInteracting(mob);
                                    mob.getDirection().setInteracting(player);
                                    break;
                                case 4:
                                    mob.animate(1436);
                                    mob.graphic(1486);
                                    player.animate(1180);
                                    break;
                                case 7:
                                    mob.graphic(1498);
                                    player.animate(1181);
                                    break;
                                case 9:
                                    player.animate(1182);
                                    break;
                                case 10:
                                    mob.animate(1448);
                                    break;
                                case 12:
                                    player.animate(1250);
                                    break;
                                case 14:
                                    player.animate(1251);
                                    player.graphic(1499);
                                    mob.animate(1454);
                                    mob.graphic(1504);
                                    break;
                                case 20:
                                    player.animate(1291);
                                    player.graphic(1686);
                                    mob.animate(1440);
                                    break;
                                case 30:
                                    player.getDirection().setInteracting(null);
                                    mob.deregister();
                                    stop();
                                    break;
                            }
                        }
                    });
                    break;
                case COMPLETIONIST_CAPE:
                    setNextEmoteEnd(14 * 600);
                    GameWorld.schedule(new TickableTask(false) {
                        @Override
                        public void tick() {
                            switch (getTick()) {
                                case 0:
                                    player.graphic(307);
                                    player.animate(356);
                                    break;
                                case 2:
                                    player.setTransformation(player.getEquipment().get(Slot.CAPE).getId() == 20771 ? 3372 : 1830);
                                    player.animate(1174);
                                    player.graphic(1443);
//                                    Graphic.sendGlobal(player, new Graphic(1443), player.copyPosition().add(0, 1));
                                    break;
                                case 8:
                                    player.graphic(2776);
                                    break;
                                case 12:
                                    player.resetTransformation();
                                    player.animate(1175);
                                    break;
                                case 14:
                                    stop();
                                    break;
                            }
                        }
                    });
                    break;
                default:
                    player.perform(data.getAnimation());
                    player.perform(data.getGraphic());
                    setNextEmoteEnd(Loader.getAnimation(data.getAnimation().getId(), data.getAnimation().getRevision()).getEmoteTime() * 2);
                    break;
            }
            return true;
        } else
            player.message("You must be wearing a Skillcape in order to use this emote.");
        return true;
    }

    public boolean handleCustomEmotes(EmoteData data) {
        if(data == EmoteData.TASKMASTER) {
            for (Achievements.AchievementData d : Achievements.AchievementData.values()) {
                if (!player.getAchievementAttributes().getCompletion()[d.ordinal()]) {
                    player.message("You must have completed all achievements in order to perform this emote.");
                    return true;
                }
            }
            return true;
        } else if (data == EmoteData.AIR_GUITAR) {
            Achievements.finishAchievement(player, Achievements.AchievementData.AIR_GUITAR);
            return false;
        } else if (data == EmoteData.ENHANCED_YAWN) {
            setNextEmoteEnd(Loader.getAnimation(5313, Revision.RS2).getEmoteTime() * 2);
            GameWorld.schedule(new TickableTask() {
                @Override
                public void tick() {
                    switch (getTick()) {
                        case 0:
                            player.animate(5313);
                            break;
                        case 6:
                            player.graphic(277);
                            break;
                        case 10:
                            player.getPacketSender().sendAnimationReset();
                            stop();
                            break;
                    }
                }
            });
            return true;
        } else if (data == EmoteData.GIVE_THANKS) {
            setNextEmoteEnd(8 * 600);
            GameWorld.schedule(new TickableTask(true) {
                @Override
                public void tick() {
                    switch (getTick()) {
                        case 0:
                            player.animate(10994);
                            player.graphic(86);
                            break;
                        case 1:
                            player.setTransformation(8499);
                            player.animate(10996);
                            break;
                        case 6:
                            player.animate(10995);
                            player.graphic(86);
                            player.resetTransformation();
                            stop();
                            break;
                    }
                }
            });
            return true;
        } else if (data == EmoteData.SEAL_OF_APPROVAL) {
            Achievements.finishAchievement(player, Achievements.AchievementData.SEAL_THE_DEAL);
            setNextEmoteEnd(6 * 600);
            int before = player.getCharacterAnimations().getStandingAnimation();
            player.getCharacterAnimations().setStandingAnimation(15106);
            GameWorld.schedule(new TickableTask(true) {
                @Override
                public void tick() {
                    switch (getTick()) {
                        case 0:
                            player.animate(15104);
                            player.graphic(1287);
                            break;
                        case 1:
                            player.setTransformation(13255 + Misc.randomMinusOne(6));
                            player.animate(15106);
                            break;
                        case 2:
                            player.animate(15108);
                            break;
                        case 5:
                            player.perform(new Animation(15105, Revision.PRE_RS3));
                            player.graphic(1287);
                            player.resetTransformation();
                            player.getCharacterAnimations().setStandingAnimation(before);
                            player.getPacketSender().sendAnimationReset();
                            stop();
                            break;
                    }
                }
            });
            return true;
        }
        return false;
    }

    public boolean handleItem(int item) {
        return handleItem(item, 0);
    }

    public boolean handleItem(int item, int emoteId) {
        if (isDoingEmote()) {
            player.message("Please wait till you've finished performing your current emote.");
            return true;
        }

        EmoteItemData itemData = EmoteItemData.forItem(item);

        if(itemData == null)
            return false;

        switch (itemData) {
            case PRAYER_BOOK:
                if (player.getEffects().hasActiveEffect(Effects.POISON)) {
                    int amount = (int) player.getEffects().getEffectForType(Effects.POISON).getArguments()[2] - 20;
                    player.getSkills().drain(Skill.PRAYER, amount);
                    player.getEffects().removeEffect(Effects.POISON);
                }
                break;
        }

        if(!handleCustomEmotes(itemData)) {
            if (itemData.animation != null) {
                if (emoteId >= itemData.animation.length)
                    emoteId = 0;

                if (emoteId < itemData.animation.length && itemData.animation[emoteId] != null) {
                    setNextEmoteEnd(Loader.getAnimation(itemData.animation[emoteId].getId(), Revision.RS2).getEmoteTime() * 2);
                    player.perform(itemData.animation[emoteId]);
                }
            }

            if (itemData.graphic != null && emoteId < itemData.graphic.length && itemData.graphic[emoteId] != null)
                player.perform(itemData.graphic[emoteId]);
        }

        return true;
    }

    private boolean handleCustomEmotes(EmoteItemData data) {
        if(data == EmoteItemData.SNOW_GLOBE) {
            player.getInventory().add(new Item(11951, player.getInventory().getSpaces()));
            return false;
        } else if(data == EmoteItemData.SPIN_PLATE) {
            setNextEmoteEnd(2 * 600);
            GameWorld.schedule(new TickableTask(false) {
                @Override
                public void tick() {
                    switch (getTick()) {
                        case 0:
                            player.animate(1902);
                            break;
                        case 1:
                            player.animate(Misc.getRandom(1) == 0 ? 1904 : 1906);
                            break;
                        case 2:
                            player.getPacketSender().sendAnimationReset();
                            stop();
                            break;
                    }
                }
            });
            return true;
        }

        return false;
    }
}

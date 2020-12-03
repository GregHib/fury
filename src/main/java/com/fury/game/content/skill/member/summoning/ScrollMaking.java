package com.fury.game.content.skill.member.summoning;

import com.fury.game.content.dialogue.input.impl.EnterAmountToTransform;
import com.fury.game.content.skill.Skill;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.core.model.item.Item;
import com.fury.game.world.GameWorld;

public class ScrollMaking {

    public static boolean scrollInterface(Player p, int buttonId) {
        final Scroll scroll = Scroll.get(buttonId);
        if (scroll == null)
            return false;
        p.setSelectedScroll(scroll);
        p.setInputHandling(new EnterAmountToTransform());
        p.getPacketSender().sendEnterAmountPrompt("Enter amount to transform:");
        return true;
    }

    private static boolean hasRequirements(final Player player, final Scroll scroll) {
        if (scroll == null)
            return false;
        player.getPacketSender().sendClientRightClickRemoval();

        if (!player.getSkills().hasRequirement(Skill.SUMMONING, scroll.getLevelRequired(), "create this scroll"))
            return false;

        if (player.getInventory().contains(new Item(scroll.getPouchId()))) {
            return true;
        } else {
            player.message("You need to have an " + new Item(scroll.getPouchId()).getName() + " to do this.");
            return false;
        }
    }

    public static void transformScrolls(final Player player, final int amount) {
        final Scroll scroll = player.getSelectedScroll();
        if (scroll == null)
            return;
        if (!hasRequirements(player, scroll))
            return;
        player.stopAll();
        player.animate(725);
        player.graphic(1207);
        GameWorld.schedule(2, () -> {
            int x = amount;
            while (x > 0) {
                if (!hasRequirements(player, scroll))
                    break;
                else {
                    player.getInventory().delete(new Item(scroll.getPouchId()));
                    player.getSkills().addExperience(Skill.SUMMONING, (int) scroll.getExp());
                    player.getInventory().add(new Item(scroll.getScrollId(), 10));
                        /*if(scroll == Pouch.SPIRIT_DREADFOWL)
							Achievements.finishAchievement(player, AchievementData.INFUSE_A_DREADFOWL_POUCH);
						else if(pouch == Pouch.STEEL_TITAN) {
							Achievements.doProgress(player, AchievementData.INFUSE_25_TITAN_POUCHES);
							Achievements.doProgress(player, AchievementData.INFUSE_500_STEEL_TITAN_POUCHES);
						}*/
                    x--;
                }
            }
        });
    }
}

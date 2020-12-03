package com.fury.game.content.dialogue.impl.quests.tutorial;

import com.fury.cache.Revision;
import com.fury.cache.def.Loader;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.dialogue.Dialogue;
import com.fury.game.content.global.quests.impl.FirstAdventure;
import com.fury.game.entity.character.combat.CombatIcon;
import com.fury.game.entity.character.combat.Hit;
import com.fury.game.world.update.flag.block.HitMask;
import com.fury.util.Misc;

import java.util.TimerTask;

public class TutRichD extends Dialogue {
    private FirstAdventure quest;

    @Override
    public void start() {
        quest = (FirstAdventure) parameters[0];
        player.getMovement().lock();
        player.getPacketSender().sendEntityHintRemoval(false);
        sendNpc(quest.sage, "You're one of the richest adventurers I know!", "You have " + Misc.formatAmount(player.getMoneyPouch().getTotal()) + " gold pieces!");
    }

    @Override
    public void run(int optionId) {
        end();
    }

    @Override
    public void finish() {
        player.animate(4961);
        player.graphic(824);
        GameExecutorManager.fastExecutor.schedule(new TimerTask() {
            @Override
            public void run() {
                player.getCombat().applyHit(new Hit(player, player.getHealth().getHitpoints() - 10, HitMask.RED, CombatIcon.NONE));
                player.getDialogueManager().startDialogue(new TutHitD(), quest);
            }
        }, Loader.getAnimation(4961, Revision.RS2).getEmoteTime());
    }
}

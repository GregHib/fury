package com.fury.game.content.dialogue.impl.minigames.fightkiln;

import com.fury.cache.Revision;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.content.controller.impl.FightKiln;
import com.fury.game.content.dialogue.Expressions;
import com.fury.game.content.dialogue.DialogueManager;
import com.fury.game.content.dialogue.Dialogue;

import java.util.TimerTask;

/**
 * Created by Jon on 2/12/2017.
 */
public class TokHaarHokD extends Dialogue {
    private int npcId;
    private int type;
    private FightKiln fightKiln;

    @Override
    public void start() {
        type = (Integer) parameters[0];
        npcId = (Integer) parameters[1];
        fightKiln = (FightKiln) parameters[2];
        if (type == 0)
            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Let us talk...");
        else if (type == 1)
            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "So...you accept our challenge.", "Let our sport be glorious. Xill - attack!");
        else if (type == 2)
            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Well fought, " + player.getUsername() + ".", "You are ferocious, but you must fight faster...", "The lava is rising.");
        else if (type == 3)
            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "You must be carved from a rock inpervious to magic...", "You are quite the worthy foe.");
        else if (type == 4)
            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Hurry, " + player.getUsername() + "...", "Kill my brothers before the lava consumes you.");
        else if (type == 7)
            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Amazing! We haven't had such fun in such a long time.", "But now, the real challenge begins...");
        else if (type == 5)
            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "We have thrown many waves at you...", "You have handled yourself like a true Tokhaar.", "You have earned our respect.");
        else if (type == 6)
            player.getDialogueManager().sendNPCDialogue(npcId, Expressions.NORMAL, "You are a Tokhaar... born in a human's body.", "Truly, we have not seen such skill from anyone out of our kiln.");
    }

    @Override
    public void run(int optionId) {
        switch (type) {
            case 0:
                switch (stage) {
                    case -1:
                        stage = 0;
                        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "Let's fight.", "I'd like to speak more about you and your kind.");
                        break;
                    case 0:
                        switch (optionId) {
                            case OPTION_1:
                                stage = 1;
                                player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Do you have any questions on the rules of our engagement?");
                                break;
                            case OPTION_2:
                            default:

                                break;
                        }
                        break;
                    case 1:
                        stage = 2;
                        player.getDialogueManager().sendOptionsDialogue(DialogueManager.DEFAULT_OPTIONS_TITLE, "No, let's just fight.", "What do I get if I beat you?", "What are the rules?");
                        break;
                    case 2:
                        switch (optionId) {
                            case DialogueManager.OPTION_1:
                                stage = 3;
                                player.getDialogueManager().sendPlayerDialogue(Expressions.PLAIN_TALKING, "No let's just fight.");
                                break;
                            case DialogueManager.OPTION_2:
                                break;
                            case DialogueManager.OPTION_3:
                            default:
                                break;
                        }
                        break;
                    case 3:
                        fightKiln.removeTokHaarTok();
                        fightKiln.nextWave();
                        end();
                        break;
                }
                break;
            case 1:
                end();
                break;
            case 2:
                switch (stage) {
                    case -1:
                        stage = 0;
                        player.getPacketSender().sendInterfaceRemoval();
                        GameExecutorManager.fastExecutor.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Our Mej wish to test you...");
                            }
                        }, 3000);
                        break;
                    case 0:
                        end();
                        break;
                }
                break;
            case 3:
                switch (stage) {
                    case -1:
                        stage = 0;
                        player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Ah, the platform is crumbling. Be quick little one - our Ket are comming.");
                        break;
                    case 0:
                        end();
                        break;
                }
                break;
            case 4:
                end();
                break;
            case 7:
                switch (stage) {
                    case -1:
                        stage = 0;
                        player.getDialogueManager().sendPlayerDialogue(Expressions.NORMAL, "The real challenge?");
                        break;
                    case 0:
                        stage = 1;
                        player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Many creatures have entered the kiln over the ages. We remember their shapes.");
                        break;
                    case 1:
                        end();
                        break;
                }
                break;
            case 5:
                switch (stage) {
                    case -1:
                        stage = 0;
                        player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "	Take this cape as a symbol of our -");
                        break;
                    case 0:
                        stage = 1;
                        player.getPacketSender().sendInterfaceRemoval();
                        GameExecutorManager.fastExecutor.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Ah - yes, there is one final challenge...");
                            }
                        }, 3000);
                        break;
                    case 1:
                        end();
                        fightKiln.hideHarAken();
                        GameExecutorManager.fastExecutor.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                fightKiln.removeScene();
                            }
                        }, 3000);
                        break;
                }
                break;

            case 6:
                switch (stage) {
                    case -1:
                        stage = 0;
                        player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "You have done very well. To mark your triumph, accept a trophy from our home.");
                        break;
                    case 0:
                        stage = 1;
                        player.getDialogueManager().sendOptionsDialogue("Choose your reward:", "The TokHaar-Kal", "An uncut onyx");
                        break;
                    case 1:
                        if (optionId == DialogueManager.OPTION_1) {
                            stage = 2;
                            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "The TokHaar-Kal is a powerful cape that will let others see that you have mastered the Fight Kiln. In addition to this, it provides several stat boosts including 8+ strength.");
                        } else {
                            stage = 3;
                            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Onyx is a precious and rare gem that can be crafted into one of several powerful objects, including the coveted Amulet of Fury.");
                        }
                        break;
                    case 2:
                    case 3:
                        stage = (byte) (stage == 2 ? 4 : 5);
                        player.getDialogueManager().sendOptionsDialogue("Accept the " + (stage == 4 ? "TokHaar-Kal" : "uncut onyx") + "?", "Yes.", "No.");
                        break;
                    case 4:
                    case 5:
                        if (optionId == DialogueManager.OPTION_1) {
                            player.getTemporaryAttributes().put("FightKilnReward", stage == 4 ? 0 : 1);
                            stage = 6;
                            player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Let us test our strength again...soon...");
                        } else {
                            stage = 1;
                            player.getDialogueManager().sendOptionsDialogue("Choose your reward:", "The TokHaar-Kal", "An uncut onyx");
                        }
                        break;
                    case 6:
                        stage = 7;
                        player.getDialogueManager().sendNPCDialogue(npcId, Revision.PRE_RS3, Expressions.NORMAL, "Now,leave...before the lava consumes you.");
                        break;
                    case 7:
                        end();
                        break;
                }
                break;
        }

    }

    @Override
    public void finish() {
        if (type == 5)
            fightKiln.unlockPlayer();
        else if (type != 0)
            fightKiln.removeScene();
    }
}


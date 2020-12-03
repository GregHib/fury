package com.fury.game.content.global.quests;

public interface QuestInterface {

    Quests getQuest();

    QuestDifficulty getQuestDifficulty();

    QuestLength getQuestLength();

    int getStages();

    QuestReward getRewards();
}

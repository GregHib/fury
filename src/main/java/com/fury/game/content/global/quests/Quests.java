package com.fury.game.content.global.quests;

import com.fury.game.content.global.quests.impl.FirstAdventure;

public enum Quests {
    FIRST_ADVENTURE("First Adventure", FirstAdventure.class);

    private Class<? extends Quest> aClass;
    private String name;
    Quests(String name, Class<? extends Quest> aClass) {
        this.name = name;
        this.aClass = aClass;
    }

    public Class<? extends Quest> getAClass() {
        return aClass;
    }

    public String getName() {
        return name;
    }
}

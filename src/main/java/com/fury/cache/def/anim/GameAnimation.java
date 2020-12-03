package com.fury.cache.def.anim;

import com.fury.cache.Revision;
import com.fury.cache.def.Definition;

public abstract class GameAnimation extends Definition {
    public GameAnimation(int id) {

    }
    public Revision revision;
    public int frameCount;
    public int primaryFrames[];
    public int secondaryFrames[];
    public int[] durations;
    public int loopOffset;
    public int interleaveOrder[];
    public boolean stretches;
    public int priority;
    public int playerOffhand;
    public int playerMainhand;
    public int maximumLoops;
    public int animationPrecedence;
    public int walkingPrecedence;
    public int replayMode;

    public void setPrecedence() {
        if(frameCount == 0) {
            frameCount = 1;
            primaryFrames = new int[1];
            primaryFrames[0] = -1;
            secondaryFrames = new int[1];
            secondaryFrames[0] = -1;
            durations = new int[1];
            durations[0] = -1;
        }
        if(animationPrecedence == -1)
            if(interleaveOrder != null)
                animationPrecedence = 2;
            else
                animationPrecedence = 0;
        if(walkingPrecedence == -1) {
            if(interleaveOrder != null) {
                walkingPrecedence = 2;
                return;
            }
            walkingPrecedence = 0;
        }
    }

    public int getEmoteTime() {
        if (durations == null)
            return 0;
        int ms = 0;
        for (int i : durations)
            ms += i;
        return ms * 10;
    }

    public int getEmoteClientCycles() {
        if (durations == null)
            return 0;
        int total = 0;
        for (int i = 0; i < durations.length - 3; i++)
            total += durations[i];
        return total;
    }
}

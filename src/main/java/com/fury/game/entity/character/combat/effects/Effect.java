package com.fury.game.entity.character.combat.effects;

import com.fury.game.world.update.flag.block.HitMask;

/**
 * Created by Greg on 18/11/2016.
 */
public class Effect {

    private Effects type;
    private int cycle;
    private Object[] args;

    public Effect(Effects type, int count, Object... args) {
        this.type = type;
        this.cycle = count;
        this.args = args;
    }

    public Effects getType() {
        return type;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public Object[] getArguments() {
        return args;
    }

    public void setArguments(Object[] arguments) {
        if(type == Effects.POISON) {
            arguments[0] = HitMask.valueOf((String) arguments[0]);
            arguments[1] = null;//Graphic
            arguments[2] = (int) (double) arguments[2];
            arguments[3] = (int) (double) arguments[3];
        }
        this.args = arguments;
    }
}
package com.fury.game.tools;

import com.fury.game.world.map.Position;

public class Test {

    public static void main(String[] args) {

        Position source = new Position(2000, 1200);
        Position target = new Position(3400, 3200);

        double value;
        int amount = 100000;
        long start, time;

        //#1
        start = System.nanoTime();
        for(int i = 0; i < amount; i++) {
            value = Math.sqrt((source.getX()-target.getX()) * (source.getX()-target.getX()) + (source.getY()-target.getY())*(source.getY()-target.getY()));
        }
        time = System.nanoTime() - start;
        System.out.println("Complete: " + time + " " + (time/amount));

        //#2
        start = System.nanoTime();
        for(int i = 0; i < amount; i++) {
            value = Math.sqrt(Math.pow(source.getX()-target.getX(), 2) + Math.pow(source.getY()-target.getY(), 2));
        }
        time = System.nanoTime() - start;
        System.out.println("Complete: " + time + " " + (time/amount));

        //#3
        start = System.nanoTime();
        for(int i = 0; i < amount; i++) {
            value = Math.hypot(source.getX()-target.getX(), source.getY() -target.getY());
        }
        time = System.nanoTime() - start;
        System.out.println("Complete: " + time + " " + (time/amount));

    }
}


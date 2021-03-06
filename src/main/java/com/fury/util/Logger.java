package com.fury.util;

public class Logger {

    public static void handle(Throwable throwable) {
        System.out.println("ERROR! THREAD NAME: " + Thread.currentThread().getName());
        throwable.printStackTrace();
    }

    public static void handle(String message, Throwable throwable) {
        System.out.println(message + " thread: " + Thread.currentThread().getName());
        throwable.printStackTrace();
    }

    public static void debug(long processTime) {
        log(Logger.class, "---DEBUG--- start");
        log(Logger.class, "WorldProcessTime: " + processTime);
        log(Logger.class, "---DEBUG--- end");
    }

    public static void log(Object classInstance, Object message) {
        log(classInstance.getClass().getSimpleName(), message);
    }

    public static void log(String className, Object message) {
        String text = "[" + className + "]" + " " + message.toString();
        System.out.println(text);
    }

    private Logger() {

    }
}

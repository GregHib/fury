package com.client;

public class Translator {

    private static final String key = "yruf" + Math.log(2) / 3;

    public static String obfuscate(String s) {
        char[] result = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = (char) (s.charAt(i) + key.charAt(i % key.length()));
        }

        return new String(result);
    }

    public static void main(String[] args) {
        System.out.println(Translator.obfuscate("154"));
        System.out.println(Translator.obfuscate("659"));
        System.out.println(Translator.obfuscate("742"));
        System.out.println(Translator.obfuscate("830"));
        System.out.println(Translator.obfuscate("anim"));
        System.out.println(Translator.obfuscate("object"));
        System.out.println(Translator.obfuscate("item"));
        System.out.println(Translator.obfuscate("npc"));
        System.out.println(Translator.obfuscate("graphic"));
        System.out.println(Translator.obfuscate("frame"));
        System.out.println(Translator.obfuscate("scene"));
        System.out.println(Translator.obfuscate("map"));
        System.out.println(Translator.obfuscate("ns3054498.ip-213-32-6.eu"));
        System.out.println(Translator.obfuscate("127.0.0.1"));
        System.out.println(Translator.obfuscate("43595"));
        System.out.println(Translator.obfuscate("/hwid.dat"));
        System.out.println(Translator.obfuscate("dos:hidden"));
        System.out.println(Translator.obfuscate("user.home"));
    }
}

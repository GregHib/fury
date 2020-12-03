package com.fury.game.system.files.world.single.impl;

import com.fury.game.system.files.Resources;
import com.fury.game.system.files.world.single.TimeStampFile;

import java.util.Arrays;

public class HWID {
    public static final class Bans extends TimeStampFile {
        private static final Bans BANS = new Bans();
        public static Bans get() { return BANS; }

        @Override public String getFile() {
            return Resources.getSaveDirectory("saves") + "world/punishment/hardwarebans.txt";
        }

    }

    public static final class Mutes extends TimeStampFile {
        private static Mutes MUTES = new Mutes();
        public static Mutes get() {
            return MUTES;
        }

        @Override public String getFile() {
            return Resources.getSaveDirectory("saves") + "world/punishment/hardwaremutes.txt";
        }
    }

    public static final class PromotionClaims extends TimeStampFile {
        private static PromotionClaims PROMO_CLAIMS = new PromotionClaims();
        public static PromotionClaims get() {
            return PROMO_CLAIMS;
        }

        public boolean has(String...identifiers) {
            for(String id : identifiers) {
                if(records.containsKey(id))
                    return true;
            }
            return false;
        }

        public void record(long number, String...identifiers) {
            for(String id : identifiers) {
                record(id, number);
            }
        }

        @Override public String getFile() {
            return Resources.getSaveDirectory("saves") + "world\\promotion\\claims.txt";
        }
    }
}

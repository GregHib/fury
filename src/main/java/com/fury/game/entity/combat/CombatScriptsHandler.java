package com.fury.game.entity.combat;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.Figure;
import com.fury.core.model.node.entity.actor.figure.mob.Mob;
import com.fury.game.entity.combat.scripts.Default;
import com.fury.util.Logger;
import com.fury.util.Utils;

import java.util.HashMap;

public class CombatScriptsHandler {
    private static final HashMap<Object, CombatScript> scripts = new HashMap<>();
    private static final CombatScript DEFAULT_SCRIPT = new Default();

    public static final void init() {
        long start = System.currentTimeMillis();
        try {
            Class[] classes = Utils.getClasses("com.fury.game.entity.combat.scripts");
            for (Class c : classes) {
                if (c.isAnonymousClass()) // next
                    continue;
                Object o = c.getConstructor().newInstance();
                if (!(o instanceof CombatScript))
                    continue;
                CombatScript script = (CombatScript) o;
                for (Object key : script.getKeys())
                    scripts.put(key, script);
            }
        } catch (Throwable e) {
            Logger.handle(e);
        }
        if(GameSettings.DEBUG)
            System.out.println("Loaded " + scripts.size() + " combat scripts in: " + (System.currentTimeMillis() - start) + "ms");
    }

    public static int attack(final Mob mob, final Figure target) {
        CombatScript script = scripts.get(mob.getId());
        if (script == null) {
            script = scripts.get(mob.getName());
            if (script == null)
                script = DEFAULT_SCRIPT;
        }
        return script.attack(mob, target);
    }
}

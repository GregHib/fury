package com.fury.cache.def;

import com.fileserver.cache.impl.CacheArchive;
import com.fury.cache.Revision;
import com.fury.cache.def.anim.Animation659;
import com.fury.cache.def.anim.Animation742;
import com.fury.cache.def.anim.GameAnimation;
import com.fury.cache.def.item.ItemDefinition;
import com.fury.cache.def.item.ItemDefinition659;
import com.fury.cache.def.item.ItemDefinition742;
import com.fury.cache.def.item.ItemDefinition830;
import com.fury.cache.def.npc.NpcDefinition;
import com.fury.cache.def.npc.NpcDefinition659;
import com.fury.cache.def.npc.NpcDefinition742;
import com.fury.cache.def.object.ObjectDefinition;
import com.fury.cache.def.object.ObjectDefinition659;
import com.fury.cache.def.object.ObjectDefinition742;
import com.fury.game.GameLoader;
import com.fury.game.GameSettings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

public final class Loader {

    public static HashMap<Class, Object> instances = new HashMap<>();

    public static boolean isLoaded() {
        return loaded;
    }

    public enum Type {
        //Graphic & Frame must be ran after Anim
        OBJECT(ObjectDefinition659.class, ObjectDefinition742.class),
        ITEM(ItemDefinition659.class, ItemDefinition742.class, ItemDefinition830.class),
        NPC(NpcDefinition659.class, NpcDefinition742.class),
        ANIM(Animation659.class, Animation742.class);

        Class[] definitions;

        Type(Class... classes) {
            definitions = classes;
        }

        public String getPrefix() {
            return this.name().toLowerCase();
        }
    }

    private static final Logger logger = Logger.getLogger(GameSettings.NAME);
    private static boolean loaded = false;

    public static void init() {
        CacheArchive archive = GameLoader.getCache().getArchive(2);
        for (Type type : Type.values()) {
            String prefix = type.getPrefix();
            String name = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
            long startTime = System.currentTimeMillis();
            String amount = init(archive, type.definitions, prefix);
            long endTime = System.currentTimeMillis();
            if(GameSettings.DEBUG)
                logger.info(amount + " " + name + "s loaded in " + (endTime - startTime) + "ms");
        }
        loaded = true;
    }

    public static ObjectDefinition getObject(int id, Revision revision) {
        return (ObjectDefinition) forId(Type.OBJECT, id, revision);
    }

    public static ItemDefinition getItem(int id) {//TODO remove
        return getItem(id, Revision.RS2);
    }

    public static ItemDefinition getItem(int id, Revision revision) {
        return (ItemDefinition) forId(Type.ITEM, id, revision);
    }

    public static NpcDefinition getNpc(int id, Revision revision) {
        return (NpcDefinition) forId(Type.NPC, id, revision);
    }

    public static GameAnimation getAnimation(int id, Revision revision) {
        return (GameAnimation) forId(Type.ANIM, id, revision);
    }

    private static Object forId(Type type, int id, Revision revision) {
        try {
            return forId(type.definitions, id, revision);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object forId(Class[] definitions, int id, Revision revision) {
        try {
            Class definition = definitions[revision.ordinal()];
            Object instance = instances.get(definition);
            Method method = getMethod(definition, "forId", int.class);
            return method.invoke(instance, id);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String init(CacheArchive archive, Class[] definitions, String prefix) {
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < definitions.length; i++) {
                Class definition = definitions[i];
                Object instance = instances.get(definition);

                if(instance == null) {
                    Constructor constructor = definition.getConstructor(int.class);
                    instance = constructor.newInstance(0);
                    instances.put(definition, instance);
                }

                Method method = getMethod(definition, "load", CacheArchive.class, String.class);
                method.invoke(instance, archive, prefix + definition.getSimpleName().substring(definition.getSimpleName().length() - 3));


                builder.append(getTotal(definition));
                if (i != definitions.length - 1)
                    builder.append(", ");
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private static Method getMethod(Class<?> definition, String name, Class<?>... parameterTypes) {
        Class<?> tempClass = definition;
        do {
            try {
                Method m = tempClass.getDeclaredMethod(name, parameterTypes);
                return m;
            } catch (NoSuchMethodException e) {
                tempClass = tempClass.getSuperclass();
            }
        } while (tempClass != null);

        throw new RuntimeException("Field '" + name + "' not found on class " + definition + " " + parameterTypes);
    }

    private static Field getField(Class<?> definition, String name) {
        Class<?> tempClass = definition;
        do {
            try {
                Field f = tempClass.getDeclaredField(name);
                return f;
            } catch (NoSuchFieldException e) {
                tempClass = tempClass.getSuperclass();
            }
        } while (tempClass != null);

        throw new RuntimeException("Field '" + name + "' not found on class " + definition);
    }

    public static int getTotalItems(Revision revision) {
        return getTotal(Type.ITEM, revision);
    }

    public static int getTotalNpcs(Revision revision) {
        return getTotal(Type.NPC, revision);
    }

    public static int getTotalObjects(Revision revision) {
        return getTotal(Type.OBJECT, revision);
    }

    public static int getTotalAnimations(Revision revision) {
        return getTotal(Type.ANIM, revision);
    }

    private static int getTotal(Type type, Revision revision) {
        try {
            return getTotal(type.definitions, revision);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getTotal(Class[] definitions, Revision revision) {
        Class definition = definitions[revision.ordinal()];
        return getTotal(definition);
    }

    private static int getTotal(Class definition) {
        try {
            Object instance = instances.get(definition);
            return getField(definition,"total").getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void clearItemsCache() {
        ItemDefinition659.cache = new ItemDefinition659[getTotalItems(Revision.RS2)];
        ItemDefinition742.cache = new ItemDefinition742[getTotalItems(Revision.PRE_RS3)];
        ItemDefinition830.cache = new ItemDefinition830[getTotalItems(Revision.RS3)];
    }

    public static void clearNpcsCache() {
        NpcDefinition659.cache = new NpcDefinition659[getTotalNpcs(Revision.RS2)];
        NpcDefinition742.cache = new NpcDefinition742[getTotalNpcs(Revision.PRE_RS3)];
    }

    public static void clearObjectsCache() {
        ObjectDefinition659.cache = new ObjectDefinition659[getTotalObjects(Revision.RS2)];
        ObjectDefinition742.cache = new ObjectDefinition742[getTotalObjects(Revision.PRE_RS3)];
    }
    /**
     * Stuff to remove
     */

    public static NpcDefinition forId(int id) {
        if(id > getTotalNpcs(Revision.RS2))
            return getNpc(id, Revision.PRE_RS3);
        else
            return getNpc(id, Revision.RS2);
    }


    public static Revision getRevision(int x, int y) {
        int regionId = ((x/8)/8 << 8) + (y/8)/8;
        return Revision.getRevision(regionId);
    }

    public static ObjectDefinition forId(int id, int x, int y) {
        Revision revision = getRevision(x, y);

        return getObject(id, revision);
    }
}

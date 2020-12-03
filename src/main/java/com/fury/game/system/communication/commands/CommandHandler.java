
package com.fury.game.system.communication.commands;

import com.fury.game.GameSettings;
import com.fury.core.model.node.entity.actor.figure.player.Player;
import com.fury.game.system.communication.commands.impl.owner.FindCommand;
import com.fury.game.system.communication.commands.impl.regular.ForumCommand;
import com.fury.game.system.communication.commands.impl.regular.HighscoresCommand;
import com.fury.game.system.communication.commands.impl.regular.PromoCommand;
import com.fury.util.Misc;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandHandler {

    private static Reflections reflections;
    private static Set<Class<? extends Command>> commands;

    public static void init() {
        long startup = System.currentTimeMillis();
        reflections = new Reflections("com.fury.game.system.communication.commands.impl");
        commands = reflections.getSubTypesOf(Command.class);
        if(GameSettings.DEBUG)
            System.out.println("Loaded " + commands.size() + " commands in " + (System.currentTimeMillis() - startup) + "ms");
    }

    public static String[] getCommands(Player player) {
        try {
            List<String> commandsList = new ArrayList<>();
            for (Class<? extends Command> command : commands) {

                Command instance = command.getConstructor().newInstance();

                boolean hasRights = getRights(player, command, instance);
                if (hasRights && !(instance instanceof PromoCommand)) {
                    String className = command.getSimpleName();
                    String format = getFormat(command, instance);
                    className = className.substring(0, className.length() - 7);
                    commandsList.add(className + " - ::" + (format != null ? format : className.toLowerCase()));
                }
            }
            Collections.sort(commandsList);
            return commandsList.toArray(new String[commandsList.size()]);
        } catch (Exception e) {
            return null;
        }
    }

    public static void handle(Player player, String text) {
        try {
            for (Class<? extends Command> command : commands) {

                Command instance = command.getConstructor().newInstance();

                String prefix = getPrefix(command, instance);
                if(prefix == null || ((prefix.endsWith(" ") || instance instanceof FindCommand || instance instanceof ForumCommand || instance instanceof HighscoresCommand) && text.startsWith(prefix)) || (text.equals(prefix))) {
                    //if has rights
                    boolean hasRights = getRights(player, command, instance);
                    if(hasRights) {
                        //get pattern
                        Pattern pattern = getPattern(command, instance);
                        if(pattern == null && prefix != null) {
                            //process if has no required values
                            process(player, new String[0], command, instance);
                            break;
                        } else if(pattern != null) {
                            //check matches format
                            Matcher matcher = pattern.matcher(text);
                            if (matcher.matches()) {
                                //process using values
                                String[] values = Misc.getValues(matcher);
                                process(player, values, command, instance);
                                break;
                            } else if(prefix != null) {
                                //tell user correct format
                                String format = getFormat(command, instance);
                                if(format != null)
                                    player.message("Please use the format: " + format);
                                break;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            player.message("Error executing that command");
        }
    }

    private final static void process(Player player, String[] values, Class<? extends Command> command, Command instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = command.getDeclaredMethod("process", Player.class, String[].class);
        method.invoke(instance, player, values);
    }

    private final static boolean getRights(Player player, Class<? extends Command> command, Command instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = command.getDeclaredMethod("rights", Player.class);
        return (boolean) method.invoke(instance, player);
    }

    private final static String getPrefix(Class<? extends Command> command, Command instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = command.getDeclaredMethod("prefix");
        return (String) method.invoke(instance);
    }

    private final static String getFormat(Class<? extends Command> command, Command instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = command.getDeclaredMethod("format");
        return (String) method.invoke(instance);
    }

    private final static Pattern getPattern(Class<? extends Command> command, Command instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = command.getDeclaredMethod("pattern");
        return (Pattern) method.invoke(instance);
    }

    private final static boolean matches(Class<? extends Command> command, Command instance, String input) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = command.getDeclaredMethod("matches", String.class);
        return (boolean) method.invoke(instance, input);
    }
}
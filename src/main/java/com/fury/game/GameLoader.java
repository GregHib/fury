package com.fury.game;

import com.fileserver.FileServer;
import com.fileserver.cache.CacheLoader;
import com.fury.Stopwatch;
import com.fury.cache.def.Loader;
import com.fury.core.action.ActionManager;
import com.fury.core.engine.GameEngine;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.container.impl.shop.ShopManager;
import com.fury.game.content.controller.ControllerHandler;
import com.fury.game.content.eco.ge.GrandExchangeOffers;
import com.fury.game.content.global.Scoreboards;
import com.fury.game.content.global.events.christmas.ChristmasEvent;
import com.fury.game.entity.character.combat.equipment.ItemBonuses;
import com.fury.game.entity.character.player.PlayerBackup;
import com.fury.game.entity.character.player.content.LoyaltyProgramme;
import com.fury.game.entity.combat.CombatScriptsHandler;
import com.fury.game.files.plugin.PluginLoader;
import com.fury.game.node.entity.actor.figure.mob.drops.MobDrops;
import com.fury.game.system.communication.clans.ClanChatManager;
import com.fury.game.system.files.loaders.item.*;
import com.fury.game.system.files.loaders.npc.MobBonuses;
import com.fury.game.system.files.loaders.npc.MobCombatDefinitionsLoader;
import com.fury.game.system.files.loaders.npc.MobSpawns;
import com.fury.game.system.files.world.WorldFileHandler;
import com.fury.game.system.mysql.MySQLController;
import com.fury.game.world.GameWorld;
import com.fury.game.world.World;
import com.fury.game.world.map.region.Region;
import com.fury.game.world.map.region.RegionIndexing;
import com.fury.network.channel.ChannelPipelineHandler;
import com.fury.network.security.ConnectionHandler;
import com.fury.util.DataBuilder;
import com.fury.util.Misc;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Credit: lare96, Gabbe
 */
public final class GameLoader {

    private final ExecutorService serviceLoader = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());
    private final GameEngine engine;
    private final int port;
    private static final CacheLoader cacheLoader = new CacheLoader();
    private Stopwatch stopwatch;

    public GameLoader(int port) {
        this.port = port;
        this.engine = new GameEngine();
    }

    public void init() {
        Preconditions.checkState(!serviceLoader.isShutdown(), "The bootstrap has been bound already!");
        stopwatch = Stopwatch.start();
        executeServiceLoad();
        engine.startAsync().awaitRunning();
        serviceLoader.shutdown();
    }

    public void finish() throws IOException, InterruptedException {
        if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
            throw new IllegalStateException("The background service load took too long!");

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(loopGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelPipelineHandler()).bind(port).syncUninterruptibly();
    }

    private static int executeNumber = 0;

    private Runnable wrap(Runnable run) {
        return () -> {
            long start = System.nanoTime();
            run.run();

            if(GameSettings.DEBUG)
                System.out.println((executeNumber++) + " in " + ((System.nanoTime() - start) / (1000 * 1000)) + "ms");
        };
    }

    private void executeServiceLoad() {
        serviceLoader.execute(wrap(DataBuilder::init));
        serviceLoader.execute(wrap(() -> {
            try {
                cacheLoader.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        //JAGGRAB
        if (GameSettings.JAGGRAB_ENABLED)
            serviceLoader.execute(wrap(() -> {
                try {
                    FileServer.init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }));

        serviceLoader.execute(wrap(Loader::init));
        serviceLoader.execute(wrap(PluginLoader::init));
        if (GameSettings.MYSQL_ENABLED)
            serviceLoader.execute(wrap(MySQLController::init));
        serviceLoader.execute(wrap(GameExecutorManager::init));
        serviceLoader.execute(wrap(ConnectionHandler::init));

        serviceLoader.execute(wrap(RegionIndexing::init));
        serviceLoader.execute(wrap(ItemDefinitions::init));
        serviceLoader.execute(wrap(GrandExchangeOffers::init));
        serviceLoader.execute(wrap(Scoreboards::init));
        serviceLoader.execute(wrap(ClanChatManager::init));
        serviceLoader.execute(wrap(CombatScriptsHandler::init));

        serviceLoader.execute(wrap(() -> WeaponInterfaces.parseInterfaces().load()));
        serviceLoader.execute(wrap(() -> ShopManager.parseShops().load()));

        serviceLoader.execute(wrap(LoyaltyProgramme::init));
        serviceLoader.execute(wrap(MobBonuses::init));
        serviceLoader.execute(wrap(MobDrops::init));
        serviceLoader.execute(wrap(MobSpawns::init));
        serviceLoader.execute(wrap(ItemSpawns::init));

        if(ChristmasEvent.active)
            serviceLoader.execute(wrap(ChristmasEvent::init));

        serviceLoader.execute(wrap(MobCombatDefinitionsLoader::init));//180ms

        serviceLoader.execute(wrap(ItemWeights::init));//104ms
        serviceLoader.execute(wrap(ItemExamines::init));//22ms
        serviceLoader.execute(wrap(ItemBonuses::init));//293ms

        serviceLoader.execute(wrap(ControllerHandler::init));//42ms
        serviceLoader.execute(wrap(WorldFileHandler::init));
        serviceLoader.execute(wrap(ActionManager::init));

        serviceLoader.execute(wrap(GameLoader::addCleanMemoryTask));
        serviceLoader.execute(wrap(GameLoader::addServerTime));
        serviceLoader.execute(wrap(GameLoader::addSavingTask));
        serviceLoader.execute(wrap(GameLoader::addDonorCheck));
        serviceLoader.execute(GameLoader::addBackupTask);
        if (GameSettings.MYSQL_ENABLED)
            serviceLoader.execute(wrap(GameLoader::addHighscoreSavingTask));

        serviceLoader.execute(wrap(GameWorld.INSTANCE::run));
    }

    public GameEngine getEngine() {
        return engine;
    }

    public static CacheLoader getCache() {
        return cacheLoader;
    }

    private static void addSavingTask() {
        GameExecutorManager.slowExecutor.scheduleWithFixedDelay(() -> World.saveAll(true), 120, 120, TimeUnit.SECONDS);
    }

    private static void addBackupTask() {
        GameExecutorManager.slowExecutor.scheduleWithFixedDelay(PlayerBackup::backup, 30, 30, TimeUnit.MINUTES);
    }

    private static void addHighscoreSavingTask() {
        GameExecutorManager.slowExecutor.scheduleWithFixedDelay(() -> {
            World.updateHighscores();
            World.updateOnlineCount();
        }, 45, 60, TimeUnit.SECONDS);
    }

    private static void addDonorCheck() {
        GameExecutorManager.slowExecutor.scheduleWithFixedDelay(World::checkDonorPoints, 240, 60, TimeUnit.SECONDS);
    }

    private static void addCleanMemoryTask() {
        GameExecutorManager.slowExecutor.scheduleWithFixedDelay(() -> cleanMemory(Runtime.getRuntime().freeMemory() < GameSettings.MIN_FREE_MEM_ALLOWED), 0, 10, TimeUnit.MINUTES);
    }

    private static void addServerTime() {
        GameExecutorManager.slowExecutor.scheduleWithFixedDelay(() -> {
            String time = Misc.getCurrentServerTime();
            World.updateServerTime(time);
            World.handleScheduledEvents(time);
        }, 60, 60, TimeUnit.SECONDS);
    }

    private static void cleanMemory(boolean force) {
        if (force) {
            Loader.clearItemsCache();
            Loader.clearNpcsCache();
            Loader.clearObjectsCache();
            for (Region region : GameWorld.getRegions().getActiveRegions().values())
                region.unloadMap();
        }
        GameExecutorManager.fastExecutor.purge();
        System.gc();
    }
}
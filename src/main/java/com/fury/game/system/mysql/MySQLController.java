package com.fury.game.system.mysql;

import com.fury.game.GameSettings;
import com.fury.game.system.mysql.impl.Store;
import com.fury.game.system.mysql.motivote.Motivote;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @author Gabriel Hannason
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MySQLController {
	
	public static final ExecutorService SQL_SERVICE = Executors.newSingleThreadExecutor();   
	
	public static void toggle() {
		if(GameSettings.MYSQL_ENABLED) {
			MySQLProcessor.terminate();
			VOTE.terminate();
			CONTROLLER = null;
			DATABASES = null;
			GameSettings.MYSQL_ENABLED = false;
		} else if(!GameSettings.MYSQL_ENABLED) { 
			init();
			GameSettings.MYSQL_ENABLED = true;
		}
	}

	private static MySQLController CONTROLLER;
	private static Store STORE = new Store();
	private static Motivote VOTE;
	
	public static void init() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch(Exception e) {
			e.printStackTrace();
		}
		CONTROLLER = new MySQLController();
	}

	public static MySQLController getController() {
		return CONTROLLER;
	}

	public static Store getStore() {
		return STORE;
	}

	public enum Database {
		HIGHSCORES,
		VOTE,
	}

	/* NON STATIC CLASS START */

	private static MySQLDatabase[] DATABASES = new MySQLDatabase[2];

	public MySQLDatabase getDatabase(Database database) {
		return DATABASES[database.ordinal()];
	}

	public MySQLController() {
		long startup = System.currentTimeMillis();
		/* DATABASES */
		DATABASES = new MySQLDatabase[]{
				new MySQLDatabase("164.132.46.176", 3306, "furyps_hiscore", "furyps_hadmin", "hiscore!123"),
				new MySQLDatabase("164.132.46.176", 3306, "furyps_vote", "furyps_vadmin", "VOTE!123"),
		};
		System.out.println("Connected to SQL Database " + (System.currentTimeMillis() - startup) + "ms");
		startup = System.currentTimeMillis();
		/* VOTING */
//		VOTE = new Motivote(new Voting(), GameSettings.WEBSITE + "/vote/", "bec68b04");
//		VOTE.start();

		if(GameSettings.DEBUG)
			System.out.println("Loaded mysql services " + (System.currentTimeMillis() - startup) + "ms");
		/*
		 * Start the process
		 */
		MySQLProcessor.process();
	}

	private static class MySQLProcessor {

		private static boolean running;
		
		private static void terminate() {
			running = false;
		}

		public static void process() {
			if(running) {
				return;
			}
			running = true;
			SQL_SERVICE.submit(() -> {
                try {
                    while(running) {
                        if(!GameSettings.MYSQL_ENABLED) {
                            terminate();
                            return;
                        }
                        for(MySQLDatabase database : DATABASES) {

                            if(!database.active) {
                                continue;
                            }

                            if(database.connectionAttempts >= 5) {
                                database.active = false;
                            }

                            Connection connection = database.getConnection();
                            try {
                                connection.createStatement().execute("/* ping */ SELECT 1");
                            } catch (Exception e) {
                                database.createConnection();
                            }
                        }
                        Thread.sleep(25000);
                    }
                } catch(Throwable e) {
                    e.printStackTrace();
                }
            });
		}
	}
}

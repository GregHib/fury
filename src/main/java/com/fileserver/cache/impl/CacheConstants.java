package com.fileserver.cache.impl;


import com.fileserver.cache.CacheLoader;

public class CacheConstants {

	/**
	 * Represents the id of the configurations cache.
	 */
	public static final int CONFIG_INDEX = 0;

	public static final int MODEL_INDEX = 1;//ITEM Def
	public static final int ANIMATION_INDEX = 2;//OBJ DEF
	public static final int MIDI_INDEX = 3;
	public static final int MAP_INDEX = 4;//Npc DEF
	public static final int MODEL_639_INDEX = 5;
	public static final int MODEL_154_INDEX = 6;
	public static final int MODEL_742_INDEX = 7;
	public static final int MAP_639_INDEX = 8;
	public static final int MAP_154_INDEX = 9;
	public static final int MAP_742_INDEX = 10;
	public static final int ANIM_639_INDEX = 11;
	public static final int ANIM_154_INDEX = 12;
	public static final int ANIM_742_INDEX = 13;
	public static final int BASE_639_INDEX = 14;
	public static final int BASE_154_INDEX = 15;
	public static final int BASE_742_INDEX = 16;
	public static final int SPRITES = 17;

	/**
	 * Represents the id of the title screen archive.
	 */
	public static final int TITLE_ARCHIVE = 1;

	/**
	 * Represents the id of the configurations archive.
	 */
	public static final int CONFIG_ARCHIVE = 2;

	/**
	 * Represents the id of the interface archive.
	 */
	public static final int INTERFACE_ARCHIVE = 3;

	/**
	 * Represents the id of the media and sprite archive.
	 */
	public static final int MEDIA_ARCHIVE = 4;

	/**
	 * Represents the id of the manifest archive.
	 */
	public static final int MANIFEST_ARCHIVE = 5;

	/**
	 * Represents the id of the textures archive.
	 */
	public static final int TEXTURES_ARCHIVE = 6;

	/**
	 * Represents the id of the word archive - user for storing profane or
	 * illegal words not allowed to be spoken in-game.
	 */
	public static final int WORD_ARCHIVE = 7;

	/**
	 * Represents the id of the sound and music archive.
	 */
	public static final int SOUND_ARCHIVE = 8;

	/**
	 * Represents the maximum amount of archives within this file system.
	 */
	public static final int MAXIMUM_ARCHIVES = 9;

	/**
	 * Represents the maximum amount of indices within this file system.
	 */
	public static final int MAXIMUM_INDICES = 256;

	/**
	 * Represents the prefix of this {@link CacheLoader}s main cache files.
	 */
	public static final String DATA_PREFIX = "main_file_cache.dat";

	/**
	 * Represents the prefix of this {@link CacheLoader}s index files.
	 */
	public static final String INDEX_PREFIX = "main_file_cache.idx";
	
	/**
	 * The preload files.
	 * 
	 * Client will download these after
	 * downloading crcs.
	 */
	public static final String[] PRELOAD_FILES = {
			/*"images.zip", */"main_file_sprites.idx", "main_file_sprites.dat"
	};
	
}

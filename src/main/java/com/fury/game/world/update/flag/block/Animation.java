package com.fury.game.world.update.flag.block;

import com.fury.cache.Revision;

/**
 * This file manages an figure's animation which should be performed.
 * 
 * @author relex lawl
 * 
 */

public class Animation {

	/**
	 * Animation constructor for figure to perform.
	 * @param id		The id of the animation figure should perform.
	 * @param delay		The delay which to wait before figure performs animation.
	 */
	public Animation(int id, int delay) {
		this(id, delay, Revision.RS2);
	}

	public Animation(int id, int delay, Revision revision) {
		if(id >= 15275 && revision != Revision.PRE_RS3)//TODO remove
			revision = Revision.PRE_RS3;
		this.id = id;
		this.delay = delay;
		this.revision = revision;
	}
	
	/**
	 * Animation constructor for figure to perform.
	 * @param id	The id of the animation figure should perform.
	 */
	public Animation(int id) {
		this(id, Revision.RS2);
	}

	public Animation(int id, Revision revision) {
		this(id, 0, revision);
	}

	public Revision getRevision() {
		return revision;
	}

	public void setRevision(Revision revision) {
		this.revision = revision;
	}

	private Revision revision;
	
	/**
	 * The animation's id.
	 */
	private int id;
	
	/**
	 * Gets the animation's id.
	 * @return	id.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the animation's id.
	 * @param id	Id to set animation's id to.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * The delay in which to perform the animation.
	 */
	private int delay;
	
	/**
	 * Gets the animation's performance delay.
	 * @return	delay.
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Sets the animation's performance delay.
	 * @param delay		Value to set delay to.
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
}

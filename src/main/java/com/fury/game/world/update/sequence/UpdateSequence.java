package com.fury.game.world.update.sequence;

import com.fury.core.model.node.entity.actor.figure.Figure;

public interface UpdateSequence<T extends Figure> {
	/**
	 * The first stage of the update sequence that executes processing code and
	 * prepares the figure for updating.
	 *
	 * @param t
	 * the figure that is being prepared for updating.
	 */
	public void executePreUpdate(T t);
	/**
	 * The main stage of the update sequence that performs parallelized updating
	 * on the figure.
	 *
	 * @param t
	 * the figure that is being updated.
	 */
	public void executeUpdate(T t);
	/**
	 * The last stage of the update sequence that resets the figure and prepares
	 * it for the next cycle.
	 *
	 * @param t
	 * the figure that is being reset for the next cycle.
	 */
	public void executePostUpdate(T t);
}
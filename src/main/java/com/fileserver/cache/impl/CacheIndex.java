package com.fileserver.cache.impl;

import java.nio.ByteBuffer;

/**
 * Represents an index within some {@link Cache}.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class CacheIndex {

	/**
	 * The length of the index.
	 */
	private final int length;

	/**
	 * The id of the index.
	 */
	private final int id;

	/**
	 * Constructs a new {@link CacheIndex} with the expected length and id. This
	 * constructor is marked {@code private} and should not be modified to be
	 * invoked directly, use {@link CacheIndex#decode(ByteBuffer)} instead.
	 * @param length The length of the index.
	 * @param id     The id of the index.
	 */
	private CacheIndex(int length, int id) {
		this.length = length;
		this.id = id;
	}

	/**
	 * Decodes an {@link CacheIndex} from the specified {@link ByteBuffer}.
	 * @param buffer The {@link ByteBuffer} to get the index from.
	 * @return The decoded index.
	 */
	public static CacheIndex decode(ByteBuffer buffer) {
		int length = getMedium(buffer);
		int id = getMedium(buffer);

		return new CacheIndex(length, id);
	}

	/**
	 * Gets a 24-bit medium integer from the specified {@link ByteBuffer}, this method does not mark the ByteBuffers
	 * current position.
	 * @param buffer The ByteBuffer to read from.
	 * @return The read 24-bit medium integer.
	 */
	public static int getMedium(ByteBuffer buffer) {
		return (buffer.getShort() & 0xFFFF) << 8 | buffer.get() & 0xFF;
	}

	/**
	 * Returns the id of this index.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the length of this index.
	 */
	public int getLength() {
		return length;
	}

}
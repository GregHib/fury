package com.fileserver.cache.impl;

import com.google.common.base.Preconditions;

import java.nio.ByteBuffer;

/**
 * Represents a sector within some {@link Cache}.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class CacheSector {

	/**
	 * The id of the index this sector is within.
	 */
	private final int indexId;

	/**
	 * This sectors chunk.
	 */
	private final int chunk;

	/**
	 * The next index within this sector.
	 */
	private final int nextIndexId;

	/**
	 * The id of the cache this sector is in.
	 */
	private final int cacheId;

	/**
	 * Constructs a new {@link CacheSector} with the expected index id, chunk, next
	 * index id and cache id. This constructor is marked {@code private} and
	 * should not be modified to be invoked directly, use
	 * {@link CacheSector#decode(ByteBuffer, byte[], int, int)} instead.
	 * @param indexId     The id of the index this sector is within.
	 * @param chunk       This sectors chunk.
	 * @param nextIndexId The next index within this sector.
	 * @param cacheId     The id of the cache this sector is in.
	 */
	private CacheSector(int indexId, int chunk, int nextIndexId, int cacheId) {
		this.indexId = indexId;
		this.chunk = chunk;
		this.nextIndexId = nextIndexId;
		this.cacheId = cacheId;
	}

	/**
	 * Decodes a {@link CacheSector} from the specified {@link ByteBuffer}.
	 * @param buffer The {@link ByteBuffer} to get the sector from.
	 * @param data   The expected data within the sector.
	 * @param offset The expected offset of the sector.
	 * @param length The expected length of the sector.
	 * @return The decoded sector.
	 */
	public static CacheSector decode(int id, ByteBuffer buffer, byte[] data, int offset, int length) {
		int indexId = id <= 0xffff ? buffer.getShort() & 0xFFFF : buffer.getInt();
		int chunk = buffer.getShort() & 0xFFFF;
		int nextIndexId = (buffer.getShort() & 0xFFFF) << 8 | buffer.get() & 0xFF;//Medium
		int cacheId = buffer.get() & 0xFF;
		buffer.get(data, offset, length);
		return new CacheSector(indexId, chunk, nextIndexId, cacheId);
	}

	/**
	 * Tests whether or not this sector is valid.
	 * @param cacheId The cache id to test.
	 * @param indexId The index id to test.
	 * @param chunk   The chunk id to test.
	 */
	public void check(int cacheId, int indexId, int chunk) {
		Preconditions.checkArgument(this.cacheId == cacheId, this.cacheId + " " + cacheId);
		Preconditions.checkArgument(this.indexId == indexId, this.indexId + " " + indexId);
		Preconditions.checkArgument(this.chunk == chunk, this.chunk + " " + chunk);
	}

	/**
	 * Returns the id of the index this sector is within.
	 */
	public int getIndexId() {
		return indexId;
	}

	/**
	 * Returns this sectors chunk.
	 */
	public int getChunk() {
		return chunk;
	}

	/**
	 * Returns the next index within this sector.
	 */
	public int getNextIndexId() {
		return nextIndexId;
	}

	/**
	 * Returns the id of the cache this sector is in.
	 */
	public int getCacheId() {
		return cacheId;
	}

}
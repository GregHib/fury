package com.fileserver.cache.impl;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * Represents a {@link CacheSector} and {@link CacheIndex} cache.
 * 

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.nio.ByteBuffer;
import @author Artem Batutin <artembatutin@gmail.com>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class Cache {
	/**
	 * Represents the size of a index file.
	 * Calculating the total size of a index file. the total size may be that of a {@code long}.
	 */
	public static final int INDEX_SIZE = 6;

	/**
	 * Represents the size of a {@link CacheSector}s header.
	 * Calculating the total size of the sector header. the total size may be that of a {@code long}.
	 */
	public static final int SECTOR_HEADER_SIZE = 8;

	public static final int EXPANDED_SECTOR_HEADER_SIZE = 10;

	/**
	 * Represents the size of a {@link CacheSector}s header.
	 * Calculating the total size of the sector header. the total size may be that of a {@code long}
	 */
	public static final int SECTOR_SIZE = 520;

	public static final int BLOCK_SIZE = 512;

	public static final int EXPANDED_BLOCK_SIZE = 510;

	/**
	 * A {@link ByteBuffer} allocated to {@link #SECTOR_SIZE}.
	 * <p>
	 * This byte buffer is used to read index and sector data from their
	 * respective byte channels.
	 * </p>
	 */
	private final ByteBuffer buffer = ByteBuffer.allocate(SECTOR_SIZE);

	/**
	 * A byte channel that contains a series of variable-length bytes which
	 * represent a sector.
	 */
	private final SeekableByteChannel sectorChannel;

	/**
	 * A byte channel that contains a series of variable-length bytes which
	 * represent a index.
	 */
	private final SeekableByteChannel indexChannel;

	/**
	 * Represents the id of this {@link Cache}.
	 */
	private final int id;

	/**
	 * Constructs a new {@link Cache} with the specified sector and index
	 * channels and id.
	 * @param sectorChannel The cache sectors byte channel.
	 * @param indexChannel  The cache sectors index channel.
	 * @param id            This caches id.
	 */
	public Cache(SeekableByteChannel sectorChannel, SeekableByteChannel indexChannel, int id) {
		this.sectorChannel = sectorChannel;
		this.indexChannel = indexChannel;
		this.id = ++id;
	}

	/**
	 * Gets a {@link ByteBuffer} of data within this cache for the specified
	 * index id.
	 * @param indexId The file id to get.
	 * @return A wrapped byte buffer of the specified files data, never
	 * {@code null}.
	 * @throws IOException If some I/O exception occurs.
	 */
	public ByteBuf get(int indexId) throws IOException {
		CacheIndex index = readIndex(indexId);

		if(index == null)
			return null;

		int headerLen = indexId <= 0xffff ? SECTOR_HEADER_SIZE : EXPANDED_SECTOR_HEADER_SIZE;
		int blockLen = indexId <= 0xffff ? BLOCK_SIZE : EXPANDED_BLOCK_SIZE;
		long position = (long) index.getId() * headerLen;

		Preconditions.checkArgument(sectorChannel.size() >= position + SECTOR_SIZE);

		byte[] data = new byte[index.getLength()];
		int next = index.getId();
		int offset = 0;

		for(int chunk = 0; offset < index.getLength(); chunk++) {
			int read = Math.min(index.getLength() - offset, blockLen);

			CacheSector sector = readSector(indexId, next, data, offset, read);
			sector.check(id, indexId, chunk);

			next = sector.getNextIndexId();
			offset += read;
		}

		return Unpooled.wrappedBuffer(data);
	}

	public byte[] decompress(int indexId) throws IOException {
		if (indexId * IDX_BLOCK_LEN + IDX_BLOCK_LEN > indexChannel.size())
			return null;
		CacheIndex index = readIndex(indexId);

		if(index == null)
			return null;

		int headerLen = indexId <= 0xffff ? SECTOR_HEADER_SIZE : EXPANDED_SECTOR_HEADER_SIZE;
		int blockLen = indexId <= 0xffff ? BLOCK_SIZE : EXPANDED_BLOCK_SIZE;
		long position = (long) index.getId() * headerLen;

		Preconditions.checkArgument(sectorChannel.size() >= position + SECTOR_SIZE);

		byte[] data = new byte[index.getLength()];
		int next = index.getId();
		int offset = 0;

		for(int chunk = 0; offset < index.getLength(); chunk++) {
			int read = Math.min(index.getLength() - offset, blockLen);

			CacheSector sector = readSector(indexId, next, data, offset, read);

			if (indexId != sector.getIndexId() || chunk != sector.getChunk() || id != sector.getCacheId())
				return null;
			if (sector.getNextIndexId() < 0 || sector.getNextIndexId() > sectorChannel.size() / TOTAL_BLOCK_LEN)
				return null;

			sector.check(id, indexId, chunk);

			next = sector.getNextIndexId();
			offset += read;
		}

		return data;
	}

	/**
	 * Reads an {@link CacheIndex} for the specified {@code indexId} and returns the
	 * decoded data.
	 * @param indexId The id of the index to read.
	 * @return The decoded index.
	 * @throws IOException If some I/O exception occurs.
	 */
	private CacheIndex readIndex(int indexId) throws IOException {
		long position = (long) indexId * INDEX_SIZE;

		buffer.clear().limit(INDEX_SIZE);
		indexChannel.position(position);
		indexChannel.read(buffer);
		buffer.flip();

		CacheIndex index = CacheIndex.decode(buffer);

		if (index.getLength() < 0 || index.getLength() > Integer.MAX_VALUE)
			return null;

		if (index.getId() <= 0 || index.getId() > sectorChannel.size() / TOTAL_BLOCK_LEN)
			return null;

		return index;
	}

	/**
	 * Reads a {@link CacheSector} for the specified {@code sectorId} and returns the
	 * decoded data.
	 * @param sectorId The id of the sector to read.
	 * @param data     The sectors data.
	 * @param offset   The sectors data offset.
	 * @param length   The length of the sectors data.
	 * @return The decoded sector.
	 * @throws IOException If some I/O exception occurs.
	 */
	private CacheSector readSector(int indexId, int sectorId, byte[] data, int offset, int length) throws IOException {
		long position = (long) sectorId * SECTOR_SIZE;

		buffer.clear().limit(length + (indexId <= 0xffff ? SECTOR_HEADER_SIZE : EXPANDED_SECTOR_HEADER_SIZE));
		sectorChannel.position(position);
		sectorChannel.read(buffer);
		buffer.flip();

		return CacheSector.decode(indexId, buffer, data, offset, length);
	}

	public synchronized boolean writeFile(int length, byte[] data, int index) {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		return writeFile(buffer, index, length, true) ? true : writeFile(buffer, index, length, false);
	}

	public long getFileCount() {
		try {
			if (indexChannel != null) {
				return (indexChannel.size() / IDX_BLOCK_LEN);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	private static final int IDX_BLOCK_LEN = 6;
	private static final int HEADER_LEN = 8;
	private static final int EXPANDED_HEADER_LEN = 10;
	private static final int BLOCK_LEN = 512;
	private static final int EXPANDED_BLOCK_LEN = 510;
	private static final int TOTAL_BLOCK_LEN = HEADER_LEN + BLOCK_LEN;

	private synchronized boolean writeFile(ByteBuffer data, int file, int size, boolean exists) {
		try {
			int block;
			if (exists) {
				if (file * IDX_BLOCK_LEN + IDX_BLOCK_LEN > indexChannel.size())
					return false;

				buffer.position(0).limit(IDX_BLOCK_LEN);
				indexChannel.position(file * IDX_BLOCK_LEN);
				indexChannel.read(buffer);
				buffer.flip().position(3);
				block = getMediumInt(buffer);

				if (block <= 0 || block > this.sectorChannel.size() / TOTAL_BLOCK_LEN)
					return false;
			} else {
				block = (int) (this.sectorChannel.size() + TOTAL_BLOCK_LEN - 1) / TOTAL_BLOCK_LEN;
				if (block == 0)
					block = 1;
			}

			buffer.position(0);
			putMediumInt(buffer, size);
			putMediumInt(buffer, block);
			buffer.flip();
			indexChannel.position(file * IDX_BLOCK_LEN);
			indexChannel.write(buffer);

			int remaining = size;
			int chunk = 0;
			int blockLen = file <= 0xffff ? BLOCK_LEN : EXPANDED_BLOCK_LEN;
			int headerLen = file <= 0xffff ? HEADER_LEN : EXPANDED_HEADER_LEN;
			while (remaining > 0) {
				int nextBlock = 0;
				if (exists) {
					buffer.position(0).limit(headerLen);
					this.sectorChannel.position(block * TOTAL_BLOCK_LEN);
					this.sectorChannel.read(buffer);
					buffer.flip();

					int currentFile = file <= 0xffff ? (buffer.getShort() & 0xffff) : buffer.getInt();
					int currentChunk = buffer.getShort() & 0xffff;
					nextBlock = getMediumInt(buffer);
					int currentIndex = buffer.get() & 0xff;

					if (file != currentFile || chunk != currentChunk || id != currentIndex)
						return false;
					if (nextBlock < 0 || nextBlock > this.sectorChannel.size() / TOTAL_BLOCK_LEN)
						return false;
				}

				if (nextBlock == 0) {
					exists = false;
					nextBlock = (int) ((this.sectorChannel.size() + TOTAL_BLOCK_LEN - 1) / TOTAL_BLOCK_LEN);
					if (nextBlock == 0)
						nextBlock = 1;
					if (nextBlock == block)
						nextBlock++;
				}

				if (remaining <= blockLen)
					nextBlock = 0;

				buffer.position(0).limit(TOTAL_BLOCK_LEN);
				if(file <= 0xffff)
					buffer.putShort((short) file);
				else
					buffer.putInt(file);
				buffer.putShort((short) chunk);
				putMediumInt(buffer, nextBlock);
				buffer.put((byte) id);

				int blockSize = remaining > blockLen ? blockLen : remaining;
				data.limit(data.position() + blockSize);
				buffer.put(data);
				buffer.flip();

				this.sectorChannel.position(block * TOTAL_BLOCK_LEN);
				this.sectorChannel.write(buffer);
				remaining -= blockSize;
				block = nextBlock;
				chunk++;
			}

			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	private static void putMediumInt(ByteBuffer buffer, int val) {
		buffer.put((byte) (val >> 16));
		buffer.put((byte) (val >> 8));
		buffer.put((byte) val);
	}
	private static int getMediumInt(ByteBuffer buffer) {
		return ((buffer.get() & 0xff) << 16) | ((buffer.get() & 0xff) << 8) | (buffer.get() & 0xff);
	}
}
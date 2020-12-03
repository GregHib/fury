package com.fileserver.cache.impl;

import com.google.common.base.Preconditions;
import com.fileserver.cache.CacheLoader;
import com.fileserver.util.CompressionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;


/**
 * Represents an archive within the {@link Cache}.
 * <p>
 * An archive contains varied amounts of {@link CacheArchiveSector}s which contain
 * compressed file system data.
 * </p>
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class CacheArchive {

	/**
	 * A {@link Map} of {@link CacheArchiveSector} hashes to {@link CacheArchiveSector}s.
	 */
	private final Map<Integer, CacheArchiveSector> sectors;

	/**
	 * Constructs a new {@link CacheArchive} with the specified {@link Map} of
	 * {@link CacheArchiveSector}s.
	 * @param sectors The {@link Map} of sectors within this archive.
	 */
	private CacheArchive(Map<Integer, CacheArchiveSector> sectors) {
		this.sectors = sectors;
	}

	/**
	 * Decodes the data within this {@link CacheArchive}.
	 * @param data The encoded data within this archive.
	 * @return Returns an {@link CacheArchive} with the decoded data, never
	 * {@code null}.
	 * @throws IOException If some I/O exception occurs.
	 */
	public static CacheArchive decode(ByteBuf data) throws IOException {
		int length = getMedium(data);
		int compressedLength = getMedium(data);

		byte[] uncompressedData = data.array();

		if(compressedLength != length) {
			uncompressedData = CompressionUtil.unbzip2Headerless(data.array(), Cache.INDEX_SIZE, compressedLength);
			data = Unpooled.wrappedBuffer(uncompressedData);
		}

		int total = data.readShort() & 0xFF;
		int offset = data.readerIndex() + total * 10;

		Map<Integer, CacheArchiveSector> sectors = new HashMap<>(total);
		for(int i = 0; i < total; i++) {
			int hash = data.readInt();
			length = getMedium(data);
			compressedLength = getMedium(data);

			byte[] sectorData = new byte[length];

			if(length != compressedLength) {
				sectorData = CompressionUtil.unbzip2Headerless(uncompressedData, offset, compressedLength);
			} else {
				System.arraycopy(uncompressedData, offset, sectorData, 0, length);
			}

			sectors.put(hash, new CacheArchiveSector(Unpooled.wrappedBuffer(sectorData), hash));
			offset += compressedLength;
		}

		return new CacheArchive(sectors);
	}

	/**
	 * Gets a 24-bit medium integer from the specified {@link ByteBuffer}, this method does not mark the ByteBuffers
	 * current position.
	 * @param buffer The ByteBuffer to read from.
	 * @return The read 24-bit medium integer.
	 */
	public static int getMedium(ByteBuf buffer) {
		return (buffer.readShort() & 0xFFFF) << 8 | buffer.readByte() & 0xFF;
	}

	/**
	 * Retrieves an {@link Optional<CacheArchiveSector>} for the specified hash.
	 * @param hash The archive sectors hash.
	 * @return The optional container.
	 */
	private Optional<CacheArchiveSector> getSector(int hash) {
		return Optional.ofNullable(sectors.get(hash));
	}

	/**
	 * Retrieves an {@link Optional<CacheArchiveSector>} for the specified name.
	 * @param name The archive sectors name.
	 * @return The optional container.
	 */
	private Optional<CacheArchiveSector> getSector(String name) {
		int hash = hash(name);
		return getSector(hash);
	}

	public static int hash(String string) {
		return _hash(string.toUpperCase());
	}

	/**
	 * Hashes a {@code String} using Jagex's algorithm, this method should be
	 * used to convert actual names to hashed names to lookup files within the
	 * {@link CacheLoader}.
	 * <p>
	 * <p>
	 * This method should <i>only</i> be used internally, it is marked
	 * deprecated as it does not properly hash the specified {@code String}. The
	 * functionality of this method is used to create a proper {@code String}
	 * {@link #hash(String) <i>hashing method</i>}. The scope of this method has
	 * been marked as {@code private} to prevent confusion.
	 * </p>
	 * @param string The string to hash.
	 * @return The hashed string.
	 * @deprecated This method should only be used internally as it does not
	 * correctly hash the specified {@code String}. See the note
	 * below for more information.
	 */
	private static int _hash(String string) {
		return IntStream.range(0, string.length()).reduce(0, (hash, index) -> hash * 61 + string.charAt(index) - 32);
	}

	/**
	 * Returns the data within the {@link CacheArchiveSector} for the specified
	 * {@code String} name.
	 * @param name The name of the {@link CacheArchiveSector}.
	 * @return The data within the {@link CacheArchiveSector} or nothing, this method
	 * fails-fast if no {@link CacheArchiveSector} exists for the specified
	 * {@code name}.
	 */
	public ByteBuf getData(String name) {
		Optional<CacheArchiveSector> optionalData = getSector(name);
		Preconditions.checkArgument(optionalData.isPresent());
		CacheArchiveSector dataSector = optionalData.get();
		return dataSector.getData();
	}

	public byte[] get(String name) {
		return getData(name).array();
	}

}
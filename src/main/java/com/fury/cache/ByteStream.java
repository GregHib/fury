package com.fury.cache;

public class ByteStream {

	private byte[] buffer;
	public int offset;

	public ByteStream(byte[] buffer)
	{
		this.buffer = buffer;
		this.offset = 0;
	}

	public void skip(int length)
	{
		offset += length;
	}

	public void setOffset(int position)
	{
		offset = position;
	}
	
	public void setOffset(long position)
	{
		offset = (int) position;
	}

	public int length()
	{
		return buffer.length;
	}
	
	public byte getByte()
	{
		return getRemaining() > 0 ? buffer[offset++] : 0;
	}

	public int getUByte() {
		return getByte() & 0xff;
	}

	public int getRemaining() {
		return offset < buffer.length ? buffer.length - offset : 0;
	}

	public int getShort() {
		int val = (getByte() << 8) + getByte();
		if(val > 32767)
            		val -= 0x10000;
		return val;
	}

	public int getUShort() {
        return (getUByte() << 8) + getUByte();
    }
	
	public int getInt()
	{
		return (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8) + getUByte();
	}
	
	public long getLong()
	{
		return (getUByte() << 56) + (getUByte() << 48) + (getUByte() << 40) + (getUByte() << 32) + (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8) + getUByte();
	}

	public int getSmart() {
		try {
			// checks current without modifying position
			if (offset >= buffer.length) {
				return buffer[buffer.length - 1] & 0xFF;
			}
			int value = buffer[offset] & 0xFF;

			if (value < 128) {
				return getUByte();
			} else {
				return getUShort() - 32768;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return getUShort() - 32768;
		}
	}

	public int getUSmart2() {
		int baseVal = 0;
		int lastVal = 0;

		while ((lastVal = getSmart()) == 32767) {
			baseVal += 32767;
		}

		return baseVal + lastVal;
	}

	public int getUSmart()
	{
        int i = buffer[offset] & 0xff;
        if (i < 128) {
        	return getUByte();
        } else {
        	return getUShort() - 32768;
        }
    }
	public int readSmart2() {
		int i = buffer[offset] & 0xff;
		int i_33_ = readUnsignedSmart();
		while (i_33_ == 32767) {
			i_33_ = readUnsignedSmart();
			i += 32767;
		}
		i += i_33_;
		return i;
	}

	public int readUnsignedSmart() {
		int i = buffer[offset] & 0xff;
		if (i >= 128) {
			return -32768 + getUShort();
		}
		return getUByte();
	}

	public int getMedium()
	{
		offset += 3;
		return ((buffer[offset - 3] & 0xff) << 16) | ((buffer[offset - 2] & 0xff) << 8) | (buffer[offset - 1] & 0xff);
	}

	public String getNString() {
		int i = offset;
		while(buffer[offset++] != 0) ;
		return new String(buffer, i, offset - i - 1);
    }

	public byte[] getBytes()
    {
        int i = offset;
        while(buffer[offset++] != 10) ;
        byte abyte0[] = new byte[offset - i - 1];
        System.arraycopy(buffer, i, abyte0, i - i, offset - 1 - i);
        return abyte0;
    }
	
	public byte[] read(int length)
	{
		byte[] b = new byte[length];
		for (int i = 0; i < length; i++)
			b[i] = buffer[offset++];
		return b;
	}

	public int getBigSmart() {
		if ((buffer[offset] ^ 0xffffffff) <= -1) {
			int value = getUShort();
			if (value == 32767) {
				return -1;
			}
			return value;
		}
		return getInt() & 0x7fffffff;
	}

	public String getString() {
		int i = offset;

		while (buffer[offset++] != 10) {
		}

		return new String(buffer, i, offset - i - 1);
	}
}
	
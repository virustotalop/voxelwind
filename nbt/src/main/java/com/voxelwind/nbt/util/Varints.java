package com.voxelwind.nbt.util;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Varints {
    private static final int MSB = 0x80
            , REST = 0x7F
            , MSBALL = ~REST;

    public static void encodeUnsigned(int num, ByteBuf output) {
        while ((num & MSBALL) != 0) {
            output.writeByte((num & 0xFF) | MSB);
            num >>>= 7;
        }
        output.writeByte(num & REST);
    }

    public static void encodeUnsigned(int num, ByteBuffer output) {
        while ((num & MSBALL) != 0) {
            output.put((byte) ((num & 0xFF) | MSB));
            num >>>= 7;
        }
        output.put((byte) (num & REST));
    }

    public static int decodeUnsigned(ByteBuf input) throws IOException {
        int res = 0, shift = 0, b;
        while (((b = input.readByte()) & MSB) == 0) {
            res += (b & REST) << shift;
            shift += 7;
            if (shift > 35) {
                throw new IllegalArgumentException("Provided VarInt is not valid.");
            }
        }
        return res;
    }

    public static int decodeUnsigned(ByteBuffer input) throws IOException {
        int res = 0, shift = 0, b;
        while (((b = input.get()) & MSB) == 0) {
            res += (b & REST) << shift;
            shift += 7;
            if (shift > 35) {
                throw new IllegalArgumentException("Provided VarInt is not valid.");
            }
        }
        return res;
    }

    public static void encodeSigned(int num, ByteBuf output) throws IOException {
        encodeUnsigned((num << 1) ^ (num >> 31), output);
    }

    public static void encodeSigned(int num, ByteBuffer output) throws IOException {
        encodeUnsigned((num << 1) ^ (num >> 31), output);
    }

    public static int decodeSigned(ByteBuf input) throws IOException {
        int n = decodeUnsigned(input);
        return (n << 1) ^ (n >> 31);
    }

    public static int decodeSigned(ByteBuffer input) throws IOException {
        int n = decodeUnsigned(input);
        return (n << 1) ^ (n >> 31);
    }
}

package com.voxelwind.server.network.mcpe;

import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class McpeUtil {
    private McpeUtil() {

    }

    public static void writeLELengthString(ByteBuf buffer, String string) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(string, "string");
        buffer.order(ByteOrder.LITTLE_ENDIAN).writeInt(string.length());
        ByteBufUtil.writeUtf8(buffer, string);
    }

    public static String readLELengthString(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");

        int length = (buffer.order(ByteOrder.LITTLE_ENDIAN).readInt());
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeVector3i(ByteBuf buf, Vector3i vector3i) {
        writeVector3i(buf, vector3i, true);
    }

    public static void writeVector3i(ByteBuf buf, Vector3i vector3i, boolean yIsByte) {
        buf.writeInt(vector3i.getX());
        if (yIsByte) {
            buf.writeByte(vector3i.getY());
        } else {
            buf.writeInt(vector3i.getY());
        }
        buf.writeInt(vector3i.getZ());
    }

    public static Vector3i readVector3i(ByteBuf buf) {
        return readVector3i(buf, true);
    }

    public static Vector3i readVector3i(ByteBuf buf, boolean yIsByte) {
        int x = buf.readInt();
        int y;
        if (yIsByte) {
            y = buf.readByte();
        } else {
            y = buf.readInt();
        }
        int z = buf.readInt();
        return new Vector3i(x, y, z);
    }

    public static void writeVector3f(ByteBuf buf, Vector3f Vector3f) {
        buf.writeFloat(Vector3f.getX());
        buf.writeFloat(Vector3f.getY());
        buf.writeFloat(Vector3f.getZ());
    }

    public static Vector3f readVector3f(ByteBuf buf) {
        double x = buf.readFloat();
        double y = buf.readFloat();
        double z = buf.readFloat();
        return new Vector3f(x, y, z);
    }
}

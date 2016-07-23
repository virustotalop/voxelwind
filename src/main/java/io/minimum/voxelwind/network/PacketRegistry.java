package io.minimum.voxelwind.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import io.minimum.voxelwind.network.raknet.RakNetPackage;
import io.minimum.voxelwind.network.raknet.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class PacketRegistry {
    private static final BiMap<Integer, Class<? extends RakNetPackage>> PACKAGE_BY_ID = ImmutableBiMap.<Integer, Class<? extends RakNetPackage>>builder()
            .put(0x00, ConnectedPingPacket.class)
            .put(0x01, UnconnectedPingPacket.class)
            .put(0x03, ConnectedPongPacket.class)
            .put(0x05, OpenConnectionRequest1Packet.class)
            .put(0x06, OpenConnectionResponse1Packet.class)
            .put(0x07, OpenConnectionRequest2Packet.class)
            .put(0x08, OpenConnectionResponse2Packet.class)
            .put(0x09, ConnectionRequestPacket.class)
            .put(0x10, ConnectionResponsePacket.class)
            .put(0x1c, UnconnectedPongPacket.class)
            .build();

    private PacketRegistry() {

    }

    public static RakNetPackage tryDecode(ByteBuf buf) {
        int id = buf.readByte();
        Class<? extends RakNetPackage> pkgClass = PACKAGE_BY_ID.get(id);
        if (pkgClass == null)
            return null;

        RakNetPackage netPackage;
        try {
            netPackage = pkgClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to create packet instance", e);
        }

        netPackage.decode(buf);
        return netPackage;
    }

    public static Integer getId(RakNetPackage pkg) {
        return PACKAGE_BY_ID.inverse().get(pkg.getClass());
    }

    public static ByteBuf tryEncode(RakNetPackage pkg) {
        Integer id = PACKAGE_BY_ID.inverse().get(pkg.getClass());
        if (id == null) {
            throw new RuntimeException("Package does not exist");
        }

        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        buf.writeByte(id);
        pkg.encode(buf);

        return buf;
    }
}

package com.voxelwind.server.network.handler;

import com.voxelwind.server.network.mcpe.packets.*;

public interface NetworkPacketHandler {
    void handle(McpeLogin packet);

    void handle(McpeClientMagic packet);

    void handle(McpeRequestChunkRadius packet);

    void handle(McpePlayerAction packet);

    void handle(McpeAnimate packet);
}

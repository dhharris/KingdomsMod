package net.kingdomsmod.common;

import java.util.UUID;

public class KingdomsModUtils {
    public static String getPlayerName(UUID playerUUID) {
        return KingdomsMod.server.getPlayerList().getPlayerByUUID(playerUUID).getName().getString();
    }
}

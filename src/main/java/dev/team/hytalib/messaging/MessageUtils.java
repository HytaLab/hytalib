package dev.team.hytalib.messaging;

import java.util.Collection;

public final class MessageUtils {

    private static String prefix = "[Hytalib] ";

    private MessageUtils() {}

    public static void setPrefix(String p) { prefix = p == null ? "" : p; }

    public static String format(String message) {
        if (message == null) return "";
        return message.replace("&", "§");
    }

    public static void send(Player player, String message) {
        player.sendMessage(prefix + format(message));
    }

    public static void broadcast(Collection<Player> players, String message) {
        String fm = prefix + format(message);
        for (Player p : players) p.sendMessage(fm);
    }
}

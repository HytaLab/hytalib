package dev.team.hytalib.messaging;

public interface Player {
    String getName();
    void sendMessage(String message);
    void teleport(String location);
}
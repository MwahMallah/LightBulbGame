package org.vut_ija_project.ija.Model.common;

import org.vut_ija_project.ija.Model.game.Game;

import java.util.*;
import java.util.stream.Collectors;

public class GameNode {
    private final Position position;
    private Type type;
    private Set<Side> connectedSides = new HashSet<>();
    private Game game;
    private boolean isPowered;

    public GameNode(Game game, Type type, Position position, Side... sides) {
        this.type = type;
        this.position = position;
        this.game = game;
        if (sides != null)
            Collections.addAll(connectedSides, sides);
        isPowered = false;
    }

    public void connect(Side side) {
        connectedSides.add(side);
    }

    public enum Type {
        Empty("E"), Link("L"), Power("P"), Bulb("B");

        private String shortName;

        Type(String shortName) {
            this.shortName = shortName;
        }

        public String getShortName() {
            return shortName;
        }
    };

    public Position getPosition() {
        return position;
    }

    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }

    public Set<Side> getConnectedSides() {
        return connectedSides;
    }

    public boolean isPower() {
        return type == Type.Power;
    }
    public boolean isBulb() {
        return type == Type.Bulb;
    }
    public boolean isLink() {
        return type == Type.Link;
    }
    public boolean isEmpty() {return type == Type.Empty;}
    

    public void turn() {
        connectedSides = connectedSides.stream().map(Side::turn).collect(Collectors.toSet());
    }
    public void rotateRandomly() {
        var numOfRotation = new Random().nextInt(4);
        for (int i = 0; i < numOfRotation; i++) {
            turn();
        }
    }
    public boolean containsConnector(Side side) {
        return connectedSides.contains(side);
    }
    public void setPowered(boolean isPowered) { this.isPowered = isPowered; }
    public boolean isPowered() {return this.isPowered; }

    @Override
    public String toString() {
        return "{" +
                type.getShortName() +
                "[" +
                position.getRow() +
                "@" +
                position.getCol() +
                "]" +
                "[" +
                connectedSides
                        .stream()
                        .sorted(Comparator.comparingInt(Side::getOrder))
                        .map(Side::toString)
                        .collect(Collectors.joining(",")) +
                "]" +
                "}";
    }

    public boolean isUnderPower() {
        return isPowered;
    }
}

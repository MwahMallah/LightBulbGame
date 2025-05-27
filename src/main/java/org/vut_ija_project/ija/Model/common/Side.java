package org.vut_ija_project.ija.Model.common;

public enum Side {
    NORTH(0), EAST(1), SOUTH(2), WEST(3);

    private int order;

    Side(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public Side turn() {
        return getSide(SOUTH, EAST, WEST, NORTH);
    }
    public Side turnBack() {
        return getSide(NORTH, WEST, EAST, SOUTH);
    }

    private Side getSide(Side fromEast, Side fromNorth, Side fromSouth, Side fromWest) {
        switch (this) {
            case EAST -> {
                return fromEast;
            }
            case NORTH -> {
                return fromNorth;
            }
            case SOUTH -> {
                return fromSouth;
            }
            case WEST -> {
                return fromWest;
            }
        }
        return null;
    }

    public Side opposite() {
        return getSide(WEST, SOUTH, NORTH, EAST);
    }
}

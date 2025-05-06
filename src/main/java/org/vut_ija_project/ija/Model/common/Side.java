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
        switch (this) {
            case EAST -> {
                return SOUTH;
            }
            case NORTH -> {
                return EAST;
            }
            case SOUTH -> {
                return WEST;
            }
            case WEST -> {
                return NORTH;
            }
        }
        return null;
    }

    public Side opposite() {
        switch (this) {
            case EAST -> {
                return WEST;
            }
            case NORTH -> {
                return SOUTH;
            }
            case SOUTH -> {
                return NORTH;
            }
            case WEST -> {
                return EAST;
            }
        }

        return null;
    }
}

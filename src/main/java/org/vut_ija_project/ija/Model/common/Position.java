package org.vut_ija_project.ija.Model.common;

import java.util.Objects;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Position getAdjacent(Side side) {
        switch (side) {
            case NORTH -> {
                return new Position(row - 1, col);
            }
            case EAST -> {
                return new Position(row, col + 1);
            }
            case SOUTH -> {
                return new Position(row + 1, col);
            }
            case WEST -> {
                return new Position(row, col - 1);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

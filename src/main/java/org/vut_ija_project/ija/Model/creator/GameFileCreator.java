package org.vut_ija_project.ija.Model.creator;

import org.apache.commons.csv.CSVPrinter;
import org.vut_ija_project.ija.Model.common.GameNode;
import org.vut_ija_project.ija.Model.common.Position;
import org.vut_ija_project.ija.Model.game.Game;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.vut_ija_project.ija.Model.common.Side;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class GameFileCreator {
    private static Game game;

    private enum ObjectType {
        LINK,
        BULB,
        EMPTY,
        POWER,
        NOT_FOUND
    }
    public static Game createGameFromSource(File source) throws RuntimeException
    {
        game = null;

        try (Reader fileReader = new FileReader(source);
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader())) {
            boolean isFirst = true;
            for (CSVRecord record : csvParser) {
                if (isFirst) {
                    if (!Objects.equals(record.get("type"), "g"))
                        throw new RuntimeException("there is no Game specifier");

                    int row = Integer.parseInt(record.get("row"));
                    int col = Integer.parseInt(record.get("col"));
                    game = Game.create(col, row);
                    isFirst = false;
                } else {
                    ObjectType type = getType(record.get("type"));
                    if (type == ObjectType.NOT_FOUND) throw new RuntimeException("Unknown type: " + record.get("type"));
                    int row = Integer.parseInt(record.get("row"));
                    int col = Integer.parseInt(record.get("col"));
                    var sides = Arrays.stream(record.get("sides").split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(s -> Side.valueOf(s.toUpperCase()))
                            .collect(Collectors.toSet());

                    addObjectToGame(type, row, col, sides);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return game;
    }

    private static void addObjectToGame(ObjectType type, int row, int col, Set<Side> sides) {
        Side[] sideArray = sides.toArray(new Side[0]);
        Position position = new Position(row, col);

        switch (type) {
            case BULB -> game.createBulbNode(position, sideArray);
            case LINK -> game.createLinkNode(position, sideArray);
            case POWER -> game.createPowerNode(position, sideArray); // или выбери нужную сторону
            default -> {}
        }
    }

    private static ObjectType getType(String type) {
        return switch (type) {
            case "E" -> ObjectType.EMPTY;
            case "L" -> ObjectType.LINK;
            case "B" -> ObjectType.BULB;
            case "P" -> ObjectType.POWER;
            default -> ObjectType.NOT_FOUND;
        };
    }

    public static void saveGame(Game game, File file) {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("type", "row", "col", "sides");
        List<GameNode> gameNodes = Arrays.stream(game.getNodes()).flatMap(Arrays::stream).toList();

        try (FileWriter fileWriter = new FileWriter(file);
             CSVPrinter printer = new CSVPrinter(fileWriter, format)) {
            printer.printRecord("g", game.rows(), game.cols(), "SOUTH");

            for (var gameNode : gameNodes) {
                String type = gameNode.getType().getShortName();
                int row = gameNode.getPosition().getRow();
                int col = gameNode.getPosition().getCol();

                // List of connectedSides
                String connectedSides = gameNode.getConnectedSides().stream()
                        .map(Side::name)
                        .collect(Collectors.joining(","));

                printer.printRecord(type, row, col, connectedSides);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

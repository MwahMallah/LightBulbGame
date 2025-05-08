package org.vut_ija_project.ija.Model.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import org.vut_ija_project.ija.Common.Events.Event;
import org.vut_ija_project.ija.Common.Events.FinishedGameEvent;
import org.vut_ija_project.ija.Common.Events.GameSetEvent;
import org.vut_ija_project.ija.Common.Events.NewGameEvent;
import org.vut_ija_project.ija.Common.Events.PowerEvent.NodePoweredEvent;
import org.vut_ija_project.ija.Common.Events.PowerEvent.NodeUnpoweredEvent;
import org.vut_ija_project.ija.Common.Publisher;
import org.vut_ija_project.ija.Common.Subscriber;
import org.vut_ija_project.ija.Model.common.GameNode;
import org.vut_ija_project.ija.Model.common.Position;
import org.vut_ija_project.ija.Model.common.Side;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game implements Publisher {
    private final GameNode[][] nodes;
    private static Game currentGame = null;
    private List<Subscriber> subscribers;
    private Game(int rows, int cols) {
        subscribers = new ArrayList<>();
        nodes = new GameNode[rows][cols];
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                nodes[i][j] = new GameNode(this, GameNode.Type.Empty, new Position(i, j));
            }
        }
    }

    public GameNode[][] getNodes() {
        return nodes;
    }

    public static Game create(int rows, int cols) {
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("Rows or Columns can't be less than 0");

        return new Game(rows, cols);
    }

    public static void createGameFromTemplate(Game newGame) {
        var prevGame = currentGame;
        currentGame = newGame;
        prevGame.publishGameChanged(newGame);
        currentGame.init();
    }

    public static Game getGame() {
        if (currentGame == null) {
            synchronized (Game.class) {
                if (currentGame == null) {
                    currentGame = create(10, 10);
                }
            }
        }

        return currentGame;
    }

    public int rows() {
        return nodes.length;

    }
    public int cols() {
        return nodes[0].length;
    }

    public GameNode node(Position p) {
        if (!isInField(p))
            return null;
        return nodes[p.getRow()][p.getCol()];
    }

    private GameNode createGameNode(GameNode.Type type, Position p, Side... sides) {
        if (!isInField(p))
            return null;

        if (!node(p).isEmpty())
            return null;

        var newGameNode = new GameNode(this, type, p, sides);
        nodes[p.getRow()][p.getCol()] = newGameNode;
        return newGameNode;
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public GameNode createBulbNode(Position p, Side... sides) {
        return createGameNode(GameNode.Type.Bulb, p, sides);
    }

    //if another power node exists return null
    private boolean otherPowerNodeExists() {
        return Arrays.stream(nodes).flatMap(Arrays::stream).anyMatch(GameNode::isPower);
    }
    public GameNode createPowerNode(Position p, Side... sides) {
        if (otherPowerNodeExists())
            return null;
        return createGameNode(GameNode.Type.Power, p, sides);
    }

    public GameNode createLinkNode(Position p, Side... sides) {
        return createGameNode(GameNode.Type.Link, p, sides);
    }

    public void rotate(Position p) {
        var node = nodes[p.getRow()][p.getCol()];
        node.turn();
        updateLight();
    }

    private void publishGameEndedEvent() {
        publishEvent(new FinishedGameEvent());
    }

    private boolean checkGameIsFinished() {
        List<GameNode> bulbs = Arrays.stream(nodes)
                .flatMap(Arrays::stream)
                .filter(GameNode::isBulb)
                .toList();

        return !bulbs.isEmpty() && bulbs.stream().allMatch(GameNode::isPowered);
    }

    public void fillBoardRandomly(int nBulbs, int nLinks) {
        List<GameNode> placedNodes = new ArrayList<>();
        Random rand = new Random();

        //place generator
        Position genPos = getRandomEmptyPosition();
        var power = createPowerNode(genPos, Side.SOUTH);

        Set<Position> used = new HashSet<>();
        // Generate path to lamp
        for (int i = 0; i < nBulbs; i++) {
            GameNode current = power;
            used.add(current.getPosition());

            int pathLen = 2 + rand.nextInt(4); // max length of link
            GameNode last = null;
            var dir = Side.SOUTH;

            for (int j = 0; j < pathLen && nLinks > 0; j++) {
                if (dir == null) break;

                var nextPos = current.getPosition().getAdjacent(dir);
                var nextNode = createLinkNode(nextPos);
                if (nextNode == null) {
                    j--;
                    dir = getRandomFreeDirection(current, used);
                    continue;
                }

                nextNode.connect(dir.opposite());

                System.out.println("Created: " + nextNode);
                current.connect(dir);
                placedNodes.add(nextNode);
                used.add(nextPos);
                current = nextNode;
                nLinks--;
                last = current;
                dir = getRandomFreeDirection(current, used);
            }

            if (last != null) {
                last.setType(GameNode.Type.Bulb);
                System.out.println("Changed to: " + last);
            }
        }

        fillLeftOverLinks(nLinks, placedNodes, used);
        rotateNodesRandomly(placedNodes);

        publishGameSet();
        init();
    }

    private void rotateNodesRandomly(List<GameNode> placedNodes) {
        for (var placedNode : placedNodes) {
            placedNode.rotateRandomly();
        }
    }

    private void fillLeftOverLinks(int nLinks, List<GameNode> placedNodes, Set<Position> used) {
        Random rand = new Random();
        int attempts = 0;
        while (nLinks > 0 && attempts < 500) {
            attempts++;
            GameNode base = placedNodes.get(rand.nextInt(placedNodes.size()));
            var dir = getRandomFreeDirection(base, used);
            if (dir == null) continue;

            var newPos = base.getPosition().getAdjacent(dir);
            if (node(newPos) == null || !node(newPos).isEmpty()) continue;

            var newLink = createLinkNode(newPos);
            System.out.println("Created: " + newLink);
            newLink.connect(dir.opposite());
            base.connect(dir);
            placedNodes.add(newLink);
            nLinks--;
        }
    }

    private Position getRandomEmptyPosition() {
        Random rand = new Random();
        while (true) {
            var randomRow = rand.nextInt(rows());
            var randomCol = rand.nextInt(cols());

            Position pos = new Position(randomRow, randomCol);
            if (node(pos).isEmpty()) {
                return pos;
            }
        }
    }

    private Side getRandomFreeDirection(GameNode from, Set<Position> used) {
        List<Side> available = new ArrayList<>();
        for (var s : Side.values()) {
            var adj = from.getPosition().getAdjacent(s);
            if ((node(adj) == null || node(adj).isEmpty()) && (used == null || !used.contains(adj)))
                available.add(s);
        }
        if (available.isEmpty()) return null;
        return available.get(new Random().nextInt(available.size()));
    }

    private void updateLight() {
        turnOffLights();
        List<GameNode> nodesToPower = getNodesToPower();
        powerNodesWithAnimation(nodesToPower);
    }

    private void powerNodesWithAnimation(List<GameNode> nodesToPower) {
        Timeline timeline = new Timeline();
        int delay = 100;

        for (int i = 0; i < nodesToPower.size(); i++) {
            GameNode node = nodesToPower.get(i);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * delay), e -> {
                node.setPowered(true);
                publishNodePowered(node);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.setOnFinished(e -> {
            if (checkGameIsFinished()) {
                publishGameEndedEvent();
            }
        });

        timeline.play();
    }

    private List<GameNode> getNodesToPower() {
        Set<Position> visited = new HashSet<>();
        var queue = getGenerators(visited);
        List<GameNode> nodesToPower = new ArrayList<>();

        while (!queue.isEmpty()) {
            GameNode current = queue.poll();

            for (Side side : current.getConnectedSides()) {
                Position neighborPos = current.getPosition().getAdjacent(side);
                GameNode neighbor = node(neighborPos);

                if (neighbor == null || visited.contains(neighborPos))
                    continue;

                if (neighbor.getConnectedSides().contains(side.opposite())) {
                    visited.add(neighborPos);
                    queue.add(neighbor);
                    nodesToPower.add(neighbor);
                }
            }
        }

        return nodesToPower;
    }

    private void turnOffLights() {
        for (GameNode[] gameNodes : nodes) {
            for (GameNode node : gameNodes) {
                if (node != null) {
                    node.setPowered(false);
                    publishNodeUnpowered(node);
                }
            }
        }
    }

    private void publishGameSet() {
        publishEvent(new GameSetEvent());
    }
    private void publishNodeUnpowered(GameNode node) {
        publishEvent(new NodeUnpoweredEvent(node));
    }

    private void publishNodePowered(GameNode node) {
        publishEvent(new NodePoweredEvent(node));
    }

    private void publishGameChanged(Game newGame) {
        publishEvent(new NewGameEvent(newGame));
    }

    public void publishEvent(Event event) {
        List<Subscriber> snapshot = new ArrayList<>(subscribers);
        for (var subscriber : snapshot) {
            if (subscriber.supports(event.getType())) {
                subscriber.onEvent(event);
            }
        }
    }

    public void init() {
        updateLight();
    }
    private boolean isInField(Position p) {
        return  p.getRow() >= 0 && p.getRow() < rows() &&
                p.getCol() >= 0 && p.getCol() < cols();
    }

    private Queue<GameNode> getGenerators(Set<Position> visitedPositions) {
        Queue<GameNode> generators = new LinkedList<>();

        for (GameNode[] row : nodes) {
            for (GameNode node : row) {
                if (node != null && node.isPower()) {
                    generators.add(node);
                    if (visitedPositions != null)
                        visitedPositions.add(node.getPosition());
                }
            }
        }

        return generators;
    }
}


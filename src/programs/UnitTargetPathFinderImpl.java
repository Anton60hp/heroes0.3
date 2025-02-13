package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        Set<String> unitPositions = getAliveUnitPositions(attackUnit, targetUnit, existingUnitList);

        int[][] distances = new int[WIDTH][HEIGHT];
        initDistances(distances);
        distances[startX][startY] = 0;
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] predecessors = new Edge[WIDTH][HEIGHT];

        PriorityQueue<EdgeDistance> priorityQueue = new PriorityQueue<>(getAcscendingEdgeDistanceComparator());
        priorityQueue.add(new EdgeDistance(startX, startY, 0));

        while (!priorityQueue.isEmpty()) {
            EdgeDistance current = priorityQueue.poll();
            int currentX = current.getX();
            int currentY = current.getY();

            if (visited[currentX][currentY]) {
                continue;
            }
            visited[currentX][currentY] = true;

            if (currentX == targetUnit.getxCoordinate() && currentY == targetUnit.getyCoordinate()) {
                break;
            }

            for (int[] direction : DIRECTIONS) {
                int newX = currentX + direction[0];
                int newY = currentY + direction[1];

                if (isValid(newX, newY, unitPositions, targetUnit)) {
                    int newDistance = distances[currentX][currentY] + 1;
                    if (newDistance < distances[newX][newY]) {
                        distances[newX][newY] = newDistance;
                        predecessors[newX][newY] = new Edge(currentX, currentY);
                        priorityQueue.add(new EdgeDistance(newX, newY, newDistance));
                    }
                }
            }
        }

        List<Edge> path = new ArrayList<>();

        if (predecessors[targetX][targetY] == null) {
            return path;
        }

        while (targetX != startX || targetY != startY) {
            path.add(new Edge(targetX, targetY));
            Edge predecessor = predecessors[targetX][targetY];
            targetX = predecessor.getX();
            targetY = predecessor.getY();
        }

        path.add(new Edge(startX, startY));

        Collections.reverse(path);
        return path;
    }

    private static Set<String> getAliveUnitPositions(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        Set<String> unitPositions = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit != attackUnit && unit != targetUnit && unit.isAlive()) {
                unitPositions.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }
        return unitPositions;
    }

    private static void initDistances(int[][] distances) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                distances[x][y] = Integer.MAX_VALUE;
            }
        }
    }

    private static Comparator<EdgeDistance> getAcscendingEdgeDistanceComparator() {
        return Comparator.comparingInt(EdgeDistance::getDistance);
    }

    private static boolean isValid(int x, int y, Set<String> unitPositions, Unit targetUnit) {
        if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
            return false;
        }
        if (unitPositions.contains(x + "," + y) && !(x == targetUnit.getxCoordinate() && y == targetUnit.getyCoordinate())) {
            return false;
        }
        return true;
    }
}

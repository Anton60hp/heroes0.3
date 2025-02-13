package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    private static final int MAX_UNITS_PER_TYPE = 11;
    private static final int GRID_WIDTH = 3;
    private static final int GRID_HEIGHT = 21;

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        unitList.sort(getDescendingEfficiencyComparator());
        List<Unit> armyUnits = new ArrayList<>();
        int armyPoints = 0;
        Iterator<Edge> shuffledEdgesIterator = getRandomEdgesIterator(); 
       
        for (Unit unit : unitList) {
            for (int i = 0; (i < MAX_UNITS_PER_TYPE) && (armyPoints + unit.getCost() <= maxPoints); i++) {
                armyUnits.add(createUnitClone(unit, i, shuffledEdgesIterator));
                armyPoints += unit.getCost();
            }
        }
        Army army = new Army(armyUnits);
        army.setPoints(armyPoints);
        return army;
    }
  
    private static Comparator<Unit> getDescendingEfficiencyComparator() {
        return (a, b) -> {
            double totalStatsToCostRatioForA = (double) (a.getBaseAttack() + a.getHealth()) / a.getCost();
            double totalStatsToCostRatioForB = (double) (b.getBaseAttack() + b.getHealth()) / b.getCost();
            return Double.compare(totalStatsToCostRatioForB, totalStatsToCostRatioForA);
        };
    }

  
    private static Unit createUnitClone(Unit unit, int index, Iterator<Edge> shuffledEdgesIterator) {
        Edge edge = shuffledEdgesIterator.next();
        return new Unit(
                unit.getName() + ' ' + (index + 1),
                unit.getUnitType(),
                unit.getHealth(),
                unit.getBaseAttack(),
                unit.getCost(),
                unit.getAttackType(),
                unit.getAttackBonuses(),
                unit.getDefenceBonuses(),
                edge.getX(),
                edge.getY());
    }

   
    private static Iterator<Edge> getRandomEdgesIterator() {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_HEIGHT; j++) {
                edges.add(new Edge(i, j));
            }
        }

        Collections.shuffle(edges);
        return edges.iterator();
    }
}
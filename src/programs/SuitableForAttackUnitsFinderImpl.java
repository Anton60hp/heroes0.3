package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            int minRange = isLeftArmyTarget ? 0 : 24;
            int maxRange = isLeftArmyTarget ? 2 : 26;
            Optional<Unit> suitableUnit = findExtremeUnit(row, minRange, maxRange, isLeftArmyTarget);
            suitableUnit.ifPresent(suitableUnits::add);
        }
        return suitableUnits;
    }

    private Optional<Unit> findExtremeUnit(List<Unit> row, int minRange, int maxRange, boolean findMax) {
        Unit extremeUnit = null;
        int extremeCoordinate = findMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Unit unit : row) {
            if (unit != null && unit.isAlive() && unit.getxCoordinate() >= minRange && unit.getxCoordinate() <= maxRange) {
                if ((findMax && unit.getxCoordinate() > extremeCoordinate) ||
                        (!findMax && unit.getxCoordinate() < extremeCoordinate)) {
                    extremeCoordinate = unit.getxCoordinate();
                    extremeUnit = unit;
                }
            }
        }

        return Optional.ofNullable(extremeUnit);
    }
}

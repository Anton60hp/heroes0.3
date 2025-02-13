package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита.

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> playerUnits = filterAliveUnits(playerArmy);
        List<Unit> computerUnits = filterAliveUnits(computerArmy);

        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            PriorityQueue<Unit> playerQueue = createSortedUnitQueue(playerUnits);
            PriorityQueue<Unit> computerQueue = createSortedUnitQueue(computerUnits);

            while (!playerQueue.isEmpty() || !computerQueue.isEmpty()) {
                moveAndAttack(playerQueue);
                moveAndAttack(computerQueue);
            }

            playerUnits = filterAliveUnits(playerArmy);
            computerUnits = filterAliveUnits(computerArmy);
        }
    }

    private PriorityQueue<Unit> createSortedUnitQueue(List<Unit> units) {
        PriorityQueue<Unit> queue = new PriorityQueue<>(Comparator.comparingInt(Unit::getBaseAttack).reversed());
        queue.addAll(units);
        return queue;
    }

    private List<Unit> filterAliveUnits(Army army) {
        return army.getUnits().stream()
                .filter(Unit::isAlive)
                .toList();
    }

    private List<Unit> getAliveUnits(Army army) {
        return army.getUnits().stream()
                .filter(Unit::isAlive)
                .toList();
    }

    private void moveAndAttack(PriorityQueue<Unit> queue) throws InterruptedException {
        while (!queue.isEmpty()) {
            Unit unit = queue.poll();
            if (unit.isAlive()) {
                performAttack(unit);
                return;
            }
        }
    }

    private void performAttack(Unit attacker) throws InterruptedException {
        Unit target = attacker.getProgram().attack();
        printBattleLog.printBattleLog(attacker, target);
    }
}
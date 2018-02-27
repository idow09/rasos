package rasos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

public class RoundHandler {
    private static final int COMPUTATION_TIMEOUT_MILLIS = 500;
    private final Map<Integer, Player> players;
    private final Attacker attacker;
    private final Reinforcer reinforcer;
    private final RiskLogger logger;
    private final ExecutorService executor;

    RoundHandler(Player playerA, Player playerB, Reinforcer reinforcer, Attacker attacker, ExecutorService executor, RiskLogger logger) {
        this.players = new HashMap<>(2);
        playerA.setPlayerId(1);
        playerB.setPlayerId(2);
        players.put(1, playerA);
        players.put(2, playerB);
        this.reinforcer = reinforcer;
        this.attacker = attacker;
        this.executor = executor;
        this.logger = logger;
    }

    public void playOneRound(Board board) {
        logger.logRoundStart();
        reinforce(board);
        attack(board);
        logger.logRoundEnd(board);
    }

    private void reinforce(Board board) {
        for (int id = 1; id <= 2; id++) {
            int quota = board.getPlayerCellCount(id);
            Iterable<ReinforcementMove> moves = getReinforcementMoves(players.get(id), board, quota);
            reinforcer.apply(board, moves, quota, id);
        }
    }

    private void attack(Board board) {
        Iterable<AttackMove> movesA = getAttackMoves(players.get(1), board);
        Iterable<AttackMove> movesB = getAttackMoves(players.get(2), board);
        //noinspection unchecked
        attacker.apply(board, movesA, movesB);
    }

    private Iterable<ReinforcementMove> getReinforcementMoves(Player player, Board board, int quota) {
        try {
            AtomicReference<Iterable<ReinforcementMove>> moves = new AtomicReference<>();
            executor.submit(() -> moves.set(player.onReinforcement(board, quota)))
                    .get(COMPUTATION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            requireNonNull(moves.get());
            return moves.get();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Iterable<AttackMove> getAttackMoves(Player player, Board board) {
        try {
            AtomicReference<Iterable<AttackMove>> moves = new AtomicReference<>();
            executor.submit(() -> moves.set(player.onAttack(board)))
                    .get(COMPUTATION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
            requireNonNull(moves.get());
            return moves.get();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}

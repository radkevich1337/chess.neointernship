package neointernship.chess.game.gameplay.figureactions;

import neointernship.chess.game.gameplay.figureactions.patterns.potential.IPotentialBasicPatterns;
import neointernship.chess.game.gameplay.figureactions.patterns.potential.PotentialBasicPatterns;
import neointernship.chess.game.gameplay.figureactions.patterns.real.IRealBasicPatterns;
import neointernship.chess.game.gameplay.figureactions.patterns.real.RealBasicPatterns;
import neointernship.chess.game.gameplay.moveaction.commands.allow.CastlingCommand;
import neointernship.chess.game.gameplay.moveaction.commands.allow.CheckCommand;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.figure.piece.King;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;
import neointernship.chess.game.story.StoryGame;
import neointernship.web.client.AI.BeatenBoard;

import java.util.*;

public class PossibleActionList implements IPossibleActionList {
    private final IMediator mediator;
    private IBoard board;
    private IStoryGame storyGame;

    private final IPotentialBasicPatterns potentialPatterns;
    private final IRealBasicPatterns realPatterns;

    private final Map<Figure, Collection<IField>> realFigureActions;
    private final Map<Figure, Collection<IField>> potentialFigureAction;

    private static int count = 0;

    public PossibleActionList(final IBoard board,
                              final IMediator mediator,
                              final IStoryGame storyGame) {
        this.mediator = mediator;
        this.board = board;
        this.storyGame = storyGame;

        this.potentialPatterns = new PotentialBasicPatterns(mediator, board, storyGame);
        this.realPatterns = new RealBasicPatterns(mediator, board, storyGame);

        this.realFigureActions = new HashMap<>();
        this.potentialFigureAction = new HashMap<>();
    }

    public PossibleActionList(IPossibleActionList possibleActionList) {
        this.mediator = new Mediator(possibleActionList.getMediator());
        this.board = possibleActionList.getBoard();
        this.storyGame = new StoryGame(possibleActionList.getStoryGame());

        this.potentialPatterns = new PotentialBasicPatterns(mediator, board, storyGame);
        this.realPatterns = new RealBasicPatterns(mediator, board, storyGame);

        this.realFigureActions = new HashMap<>();
        for (Figure figure : possibleActionList.getRealFigureActions().keySet()) {
            realFigureActions.put(figure, possibleActionList.getPotentialFigureAction().get(figure));
        }

        this.potentialFigureAction = new HashMap<>();
        for (Figure figure : possibleActionList.getPotentialFigureAction().keySet()) {
            potentialFigureAction.put(figure, possibleActionList.getPotentialFigureAction().get(figure));
        }
    }

    @Override
    public void updatePotentialLists() {
        potentialFigureAction.clear();

        for (Figure figure : mediator.getFigures()) {
            potentialFigureAction.put(figure, new ArrayList<>());

            potentialFigureAction.get(figure).addAll(Intermediary.getList(figure, potentialPatterns));
        }
    }

    @Override
    public void updatePotentialLists(Color color) {
        for (Figure figure : mediator.getFigures(color)) {
            potentialFigureAction.put(figure, new ArrayList<>());

            potentialFigureAction.get(figure).addAll(Intermediary.getList(figure, potentialPatterns));
        }
    }

    @Override
    public Collection<IField> getRealList(Figure figure) {
        return realFigureActions.get(figure);
    }

    @Override
    public void updateRealLists() {
        CheckCommand checkCommand;
        int checks;
        updatePotentialLists();
        realFigureActions.clear();
        BeatenBoard.init(board);

        for (Figure figure : mediator.getFigures()) {
            IMediator mediator = new Mediator(this.mediator);
            mediator.deleteConnection(mediator.getField(figure));
            IPossibleActionList possibleActionList = new PossibleActionList(board, mediator, storyGame);
            possibleActionList.updatePotentialLists();

            checkCommand = new CheckCommand(board, mediator);
            checks = checkCommand.getCountChecks(figure.getColor(), possibleActionList);

            realFigureActions.put(figure, new ArrayList<>());

            if (checks == 1 || figure.getClass() == King.class) {
                Collection<IField> figures = realPatterns.getRealMoveList(
                        figure,
                        potentialFigureAction.get(figure));

                realFigureActions.get(figure).addAll(figures);
                BeatenBoard.addBeatenFields(figures, mediator, figure.getColor());
            } else if (checks == 0) {
                realFigureActions.get(figure).addAll(potentialFigureAction.get(figure));
                BeatenBoard.addBeatenFields(potentialFigureAction.get(figure), mediator, figure.getColor());
            }
        }
    }

    @Override
    public Collection<IField> getPotentialList(Figure figure) {
        return potentialFigureAction.get(figure);
    }

    @Override
    public IMediator getMediator() {
        return mediator;
    }

    @Override
    public IBoard getBoard() {
        return board;
    }

    @Override
    public IPotentialBasicPatterns getPotentialPatterns() {
        return potentialPatterns;
    }

    @Override
    public IRealBasicPatterns getRealPatterns() {
        return realPatterns;
    }

    @Override
    public Map<Figure, Collection<IField>> getRealFigureActions() {
        return realFigureActions;
    }

    @Override
    public Map<Figure, Collection<IField>> getPotentialFigureAction() {
        return potentialFigureAction;
    }

    public Map<Figure, List<Figure>> getAttackList() {
        return Intermediary.getAttackList();
    }

    @Override
    public IStoryGame getStoryGame() {
        return storyGame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PossibleActionList that = (PossibleActionList) o;
        return Objects.equals(realFigureActions, that.realFigureActions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediator, realFigureActions);
    }
}

package neointernship.chess.game.gameplay.moveaction.commands.allow;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.gameplay.moveaction.commands.IMoveCommand;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;
import neointernship.chess.game.story.StoryGame;
import neointernship.web.client.communication.message.TurnStatus;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Реализация хода в нормальной ситуации
 */
public class AllowMoveCommand implements IMoveCommand {

    private final IMediator mediator;
    private final IPossibleActionList possibleActionList;
    private final IBoard board;

    private final IStoryGame storyGame;

    private final Queue<IAllowCommand> commandQueue;


    public AllowMoveCommand(final IMediator mediator,
                            final IPossibleActionList possibleActionList,
                            final IBoard board,
                            final IStoryGame storyGame) {
        this.mediator = mediator;
        this.possibleActionList = possibleActionList;
        this.board = board;

        this.storyGame = storyGame;

        // не менять последовательность добавления в очередь! Важна именно эта последовательность
        // каждая последующая команда уверена в том, что раз дошли до неё, значит остальные проверки
        // дали отрицательный результат
        commandQueue = new LinkedList<>();
        commandQueue.add(new TransformationAfterCommand(board, mediator));
        commandQueue.add(new TransformationBeforeCommand(board, mediator));
        commandQueue.add(new AttackCommand(board, mediator));
        commandQueue.add(new CastlingCommand(board, mediator));
        commandQueue.add(new AisleTakeCommand(board, mediator));
        commandQueue.add(new MoveCommand(board, mediator));
    }

    public AllowMoveCommand(AllowMoveCommand allowMoveCommand) {
        this.mediator = new Mediator(allowMoveCommand.mediator);
        this.possibleActionList = new PossibleActionList((PossibleActionList) allowMoveCommand.possibleActionList);
        this.board = allowMoveCommand.board;
        this.storyGame = new StoryGame((StoryGame) allowMoveCommand.storyGame);
        this.commandQueue = new LinkedList<>();
        commandQueue.add(new TransformationAfterCommand(board, mediator));
        commandQueue.add(new TransformationBeforeCommand(board, mediator));
        commandQueue.add(new AttackCommand(board, mediator));
        commandQueue.add(new CastlingCommand(board, mediator));
        commandQueue.add(new AisleTakeCommand(board, mediator));
        commandQueue.add(new MoveCommand(board, mediator));
    }


    @Override
    public TurnStatus execute(final IAnswer answer) {
        final IField startField = board.getField(answer.getStartX(), answer.getStartY());
        final Figure startFigure = mediator.getFigure(startField);

        final IField finishField = board.getField(answer.getFinalX(), answer.getFinalY());
        storyGame.update(startFigure);

        final IAllowCommand currentCommand = getCommand(startField, finishField);
        currentCommand.execute(answer); // делаю ход

        return currentCommand.getTurnStatus();
    }

    public IAllowCommand getCommand(final IField startField, final IField finishField) {
        for (IAllowCommand command : commandQueue) {
            if (command.check(startField, finishField)) return command;
        }
        return null;
    }


}

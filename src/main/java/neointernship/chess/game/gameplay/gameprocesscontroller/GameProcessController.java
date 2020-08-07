package neointernship.chess.game.gameplay.gameprocesscontroller;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.moveaction.MoveCorrectnessValidator;
import neointernship.chess.game.gameplay.moveaction.commands.IMoveCommand;
import neointernship.chess.game.gameplay.moveaction.movesrepository.MovesRepository;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.MoveState;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.story.IStoryGame;
import neointernship.web.client.communication.message.TurnStatus;

public class GameProcessController implements IGameProcessController {

    private final MovesRepository movesRepository;
    private final MoveCorrectnessValidator moveCorrectnessValidator;

    private TurnStatus turnStatus;

    public GameProcessController(final IMediator mediator,
                                 final IPossibleActionList possibleActionList,
                                 final IBoard board,
                                 final IStoryGame storyGame) {

        movesRepository = new MovesRepository(mediator, possibleActionList, board, storyGame);
        moveCorrectnessValidator = new MoveCorrectnessValidator(mediator, possibleActionList, board);
    }

    public GameProcessController(IGameProcessController gameProcessController) {
        this.movesRepository = new MovesRepository(gameProcessController.getMovesRepository());
        this.moveCorrectnessValidator = new MoveCorrectnessValidator(gameProcessController.getMoveCorrectnessValidator());
        this.turnStatus = gameProcessController.getTurnStatus();
    }

    @Override
    public void makeTurn(final Color color, final IAnswer answer) {
        final MoveState moveState = moveCorrectnessValidator.check(color, answer);
        final IMoveCommand moveCommand = movesRepository.getCommand(moveState);
        turnStatus = moveCommand.execute(answer);
    }

    @Override
    public TurnStatus getTurnStatus() {
        return turnStatus;
    }

    @Override
    public MovesRepository getMovesRepository() {
        return movesRepository;
    }

    @Override
    public MoveCorrectnessValidator getMoveCorrectnessValidator() {
        return moveCorrectnessValidator;
    }
}

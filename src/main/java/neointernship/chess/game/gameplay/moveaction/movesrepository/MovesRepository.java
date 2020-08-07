package neointernship.chess.game.gameplay.moveaction.movesrepository;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.moveaction.commands.IMoveCommand;
import neointernship.chess.game.gameplay.moveaction.commands.RestrictMoveCommand;
import neointernship.chess.game.gameplay.moveaction.commands.allow.AllowMoveCommand;
import neointernship.chess.game.model.enums.MoveState;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.story.IStoryGame;

import java.util.HashMap;

public class MovesRepository {
    private final HashMap<MoveState, IMoveCommand> actionCommandMap;
    private final IMoveCommand allowMoveCommand;
    private final IMoveCommand restrictCommand;

    public MovesRepository(final IMediator mediator,
                           final IPossibleActionList possibleActionList,
                           final IBoard board,
                           final IStoryGame storyGame) {

        allowMoveCommand = new AllowMoveCommand(mediator, possibleActionList, board, storyGame);
        restrictCommand = new RestrictMoveCommand(mediator, possibleActionList, board);

        actionCommandMap = new HashMap<MoveState, IMoveCommand>() {
            {
                put(MoveState.ALLOWED, allowMoveCommand);
                put(MoveState.RESTRICT, restrictCommand);
            }
        };
    }

    public MovesRepository(MovesRepository movesRepository) {
        this.allowMoveCommand = new AllowMoveCommand((AllowMoveCommand) movesRepository.allowMoveCommand);
        this.restrictCommand = new RestrictMoveCommand((RestrictMoveCommand) movesRepository.restrictCommand);
        this.actionCommandMap = new HashMap<MoveState, IMoveCommand>(){
            {
                put(MoveState.ALLOWED, allowMoveCommand);
                put(MoveState.RESTRICT, restrictCommand);
            }
        };
    }

    public IMoveCommand getCommand(final MoveState state) {
        return actionCommandMap.get(state);
    }
}

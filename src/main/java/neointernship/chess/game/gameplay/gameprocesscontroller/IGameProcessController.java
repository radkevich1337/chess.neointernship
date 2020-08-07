package neointernship.chess.game.gameplay.gameprocesscontroller;

import neointernship.chess.game.gameplay.moveaction.MoveCorrectnessValidator;
import neointernship.chess.game.gameplay.moveaction.movesrepository.MovesRepository;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.Color;
import neointernship.web.client.communication.message.TurnStatus;

public interface IGameProcessController {
    void makeTurn(final Color color, final IAnswer answer);

    TurnStatus getTurnStatus();

    MovesRepository getMovesRepository();

    MoveCorrectnessValidator getMoveCorrectnessValidator();
}

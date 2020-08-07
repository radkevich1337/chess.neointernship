package neointernship.chess.game.gameplay.gamestate.controller.draw;

import neointernship.chess.game.gameplay.gamestate.state.GameState;
import neointernship.chess.game.gameplay.gamestate.state.IGameState;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.story.IStoryGame;

import java.util.Collection;
import java.util.HashSet;

public class DrawStateController {

    private final IMediator mediator;

    private final Collection<IDrawController> drawControllers;
    private EnumGameState actualState;

    public DrawStateController(final IMediator mediator,
                               final IStoryGame storyGame) {
        this.mediator = mediator;

        this.drawControllers = new HashSet<>();
        drawControllers.add(new DrawOnlyKing());
        drawControllers.add(new DrawFewFigure());
        drawControllers.add(new DrawRepetitionPosition());
        drawControllers.add(new DrawFiftyStep(storyGame));

        actualState = EnumGameState.ALIVE;
    }

    public DrawStateController(DrawStateController drawStateController) {
        this.mediator = new Mediator(drawStateController.mediator);
        this.actualState = drawStateController.actualState;

        this.drawControllers = new HashSet<>();

        for (IDrawController drawController : drawStateController.drawControllers) {
            if (drawController.getClass() != DrawFiftyStep.class) {
                drawControllers.add(drawController);
            } else {
                drawControllers.add(new DrawFiftyStep((DrawFiftyStep) drawController));
            }
        }
    }

    public void update() {
        for (final IDrawController drawController : drawControllers) {
            if (drawController.isDraw(mediator)) {
                actualState = drawController.getState();
            }
        }
    }


    public IGameState getState() {
        return new GameState(actualState, Color.BOTH);
    }
}

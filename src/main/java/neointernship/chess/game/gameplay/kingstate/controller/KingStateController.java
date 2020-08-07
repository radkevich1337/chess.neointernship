package neointernship.chess.game.gameplay.kingstate.controller;

import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.kingstate.subscriber.IKingStateSubscriber;
import neointernship.chess.game.gameplay.kingstate.update.KingIsAttackedComputation;
import neointernship.chess.game.gameplay.kingstate.update.KingStateDefineLogic;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.KingState;
import neointernship.chess.game.model.mediator.IMediator;

import java.util.ArrayList;
import java.util.HashMap;

public class KingStateController implements IKingStateController {

    private final ArrayList<IKingStateSubscriber> subscribersList;

    private final HashMap<Color, KingState> kingStateMap;
    private final KingIsAttackedComputation kingIsAttackedComputation;
    private final KingStateDefineLogic kingStateDefineLogic;

    public KingStateController(final IPossibleActionList possibleActionList,
                               final IMediator mediator) {
        kingStateMap = new HashMap<Color, KingState>() {{
            put(Color.WHITE, KingState.FREE);
            put(Color.BLACK, KingState.FREE);
        }};

        subscribersList = new ArrayList<>();

        kingIsAttackedComputation = new KingIsAttackedComputation(possibleActionList, mediator);
        kingStateDefineLogic = new KingStateDefineLogic();
    }

    public KingStateController(IKingStateController kingStateController) {
        this.kingStateMap = kingStateController.getKingStateMap();
        this.kingIsAttackedComputation = new KingIsAttackedComputation(kingStateController.getKingIsAttackedComputation());
        this.kingStateDefineLogic = kingStateController.getKingStateDefineLogic();

        this.subscribersList = new ArrayList<>();
        subscribersList.addAll(kingStateController.getSubscribersList());
    }

    @Override
    public void addToSubscriber(IKingStateSubscriber subscriber) {
        subscribersList.add(subscriber);
    }

    @Override
    public KingState getKingState(Color color) {
        return kingStateMap.get(color);
    }

    @Override
    public void update(final Color activeColor) {
        boolean kingIsAttacked = kingIsAttackedComputation.kingIsAttacked(activeColor);

        KingState newState = kingStateDefineLogic.getState(kingIsAttacked);

        kingStateMap.replace(activeColor, newState);
    }

    @Override
    public ArrayList<IKingStateSubscriber> getSubscribersList() {
        return subscribersList;
    }

    @Override
    public HashMap<Color, KingState> getKingStateMap() {
        return kingStateMap;
    }

    @Override
    public KingIsAttackedComputation getKingIsAttackedComputation() {
        return kingIsAttackedComputation;
    }

    @Override
    public KingStateDefineLogic getKingStateDefineLogic() {
        return kingStateDefineLogic;
    }
}

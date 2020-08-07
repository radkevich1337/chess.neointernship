package neointernship.chess.game.gameplay.kingstate.controller;

import neointernship.chess.game.gameplay.kingstate.subscriber.IKingStateSubscriber;
import neointernship.chess.game.gameplay.kingstate.update.KingIsAttackedComputation;
import neointernship.chess.game.gameplay.kingstate.update.KingStateDefineLogic;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.KingState;

import java.util.ArrayList;
import java.util.HashMap;

public interface IKingStateController {
    void update(final Color color);

    void addToSubscriber(IKingStateSubscriber subscriber);

    KingState getKingState(final Color color);

    ArrayList<IKingStateSubscriber> getSubscribersList();

    HashMap<Color, KingState> getKingStateMap();

    KingIsAttackedComputation getKingIsAttackedComputation();

    KingStateDefineLogic getKingStateDefineLogic();
}

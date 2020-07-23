package neointernship.web.client.communication.message;


import neointernship.web.client.communication.message.reaction.model.*;

import java.util.HashMap;

public class MessageReactionForModel {
    private final HashMap<ClientCodes, IMessageCode> messageReaction;

    public MessageReactionForModel() {
        this.messageReaction = new HashMap<>();
        initMessageReaction(messageReaction);
    }

    private void initMessageReaction(final HashMap<ClientCodes, IMessageCode> messageReaction) {
        //НУЖНО ОПРЕДЕЛИТЬСЯ С КЛАССАМИ И/ИЛИ ВОССТАНОВИТЬ ЭТИ КЛАССЫ
        /*messageReaction.put(MessageCode.CONNECT, new MessageCodeConnect());
        messageReaction.put(MessageCode.DRAW, new MessageCodeDraw());
        messageReaction.put(MessageCode.ERROR, new MessageCodeError());
        messageReaction.put(MessageCode.LOSE, new MessageCodeLose());
        messageReaction.put(MessageCode.MOVE_FIGURE, new MessageCodeMoveFigure());
        messageReaction.put(MessageCode.OK, new MessageCodeOk());
        messageReaction.put(MessageCode.PICK_FIGURE, new MessageCodePickFigure());
        messageReaction.put(MessageCode.WIN, new MessageCodeWin());*/
        messageReaction.put(ClientCodes.TURN, new MessageCodeTurn());
        messageReaction.put(ClientCodes.ERROR_TURN, new MessageCodeErrorTurn());
        messageReaction.put(ClientCodes.INIT_GAME, new MessageCodeInitGame());
        messageReaction.put(ClientCodes.UPDATE, new MessageCodeUpdate());
        messageReaction.put(ClientCodes.NAME, new MessageCodeInfo());
    }

    public IMessageCode get(final ClientCodes clientCodes) {
        return messageReaction.get(clientCodes);
    }
}

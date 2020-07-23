package neointernship.web.client.communication.message.reaction.view;

import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.web.client.communication.data.initgame.IInitGame;
import neointernship.web.client.communication.exchanger.InitGameExchanger;
import neointernship.web.client.communication.exchanger.MessageExchanger;
import neointernship.web.client.communication.message.IMessage;
import neointernship.web.client.communication.message.Message;
import neointernship.web.client.communication.message.ClientCodes;
import neointernship.web.client.controller.Controller;
import neointernship.web.client.player.IPlayer;
import neointernship.web.client.view.View;

public class InitGameView implements IMessageCodeView {
    @Override
    public void execute(IPlayer player) throws InterruptedException {
        final IInitGame initGame = InitGameExchanger.exchange(null);
        final IMediator mediator = initGame.getMediator();
        final IBoard board = initGame.getBoard();
        final Color color = initGame.getColor();
        player.init(mediator, board, color);
        final IMessage mes = new Message(ClientCodes.OK);
        MessageExchanger.exchange(mes);
    }

    public static class Client{
        /**
         * Главная функция, начало работы клиента
         *
         * @param args аргументы командной строки
         */
        public static void main(final String[] args) {
            startView();
            startController();
        }

        private static void startView() {
            final Runnable view = new View();
            new Thread(view).start();
        }

        private static void startController() {
            final Runnable controller = new Controller();
            new Thread(controller).start();
        }
    }
}
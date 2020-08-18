package neointernship.web.server.lobby;

import neointernship.chess.game.console.ConsoleBoardWriter;
import neointernship.chess.game.gameplay.activecolorcontroller.ActiveColorController;
import neointernship.chess.game.gameplay.figureactions.IPossibleActionList;
import neointernship.chess.game.gameplay.figureactions.PossibleActionList;
import neointernship.chess.game.gameplay.gamestate.state.IGameState;
import neointernship.chess.game.gameplay.loop.GameLoop;
import neointernship.chess.game.gameplay.loop.IGameLoop;
import neointernship.chess.game.model.answer.AnswerSimbol;
import neointernship.chess.game.model.answer.IAnswer;
import neointernship.chess.game.model.enums.ChessType;
import neointernship.chess.game.model.enums.Color;
import neointernship.chess.game.model.enums.EnumGameState;
import neointernship.chess.game.model.enums.EnumMatchResult;
import neointernship.chess.game.model.figure.factory.Factory;
import neointernship.chess.game.model.figure.factory.IFactory;
import neointernship.chess.game.model.figure.piece.Figure;
import neointernship.chess.game.model.mediator.IMediator;
import neointernship.chess.game.model.mediator.Mediator;
import neointernship.chess.game.model.playmap.board.Board;
import neointernship.chess.game.model.playmap.board.IBoard;
import neointernship.chess.game.model.playmap.board.figuresstartposition.FiguresStartPositionRepository;
import neointernship.chess.game.model.playmap.field.Field;
import neointernship.chess.game.model.playmap.field.IField;
import neointernship.chess.game.story.IStoryGame;
import neointernship.chess.game.story.StoryGame;
import neointernship.chess.logger.ErrorLoggerServer;
import neointernship.chess.logger.GameLogger;
import neointernship.chess.logger.MediatorLogger;
import neointernship.chess.statistics.Statistics;
import neointernship.web.client.communication.data.endgame.EndGame;
import neointernship.web.client.communication.data.endgame.IEndGame;
import neointernship.web.client.communication.data.initgame.IInitGame;
import neointernship.web.client.communication.data.initgame.InitGame;
import neointernship.web.client.communication.data.transformation.TransformationDto;
import neointernship.web.client.communication.data.turn.TurnDto;
import neointernship.web.client.communication.data.update.IUpdate;
import neointernship.web.client.communication.data.update.Update;
import neointernship.web.client.communication.message.*;
import neointernship.web.client.communication.serializer.*;
import neointernship.web.server.Server;
import neointernship.web.server.connection.controller.ActiveConnectionController;
import neointernship.web.server.connection.userconnection.UserConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import static neointernship.web.server.send.SendMessage.send;

public class Lobby extends Thread {
    private final IBoard board;
    private final IFactory figureFactory;
    private final IMediator mediator;
    private final IPossibleActionList possibleActionList;
    private final IStoryGame storyGame;

    private final int lobbyId;
    private final ChessType chessTypes;
    private final FiguresStartPositionRepository figuresStartPositionRepository;
    private final Character FIELD_CHAR_EMPTY = '.';

    private final IGameLoop gameLoop;

    private final Server server;
    private final ActiveConnectionController connectionController;
    private ActiveColorController activeColorController;

    public Lobby(final UserConnection firstUserConnection, final UserConnection secondUserConnection,
                 final int lobbyId, final Server server, final ChessType chessType) {
        this.lobbyId = lobbyId;
        this.server = server;

        activeColorController = new ActiveColorController();
        this.connectionController = new ActiveConnectionController(
                firstUserConnection,
                secondUserConnection,
                activeColorController);

        board = new Board();
        figureFactory = new Factory();
        mediator = new Mediator();
        storyGame = new StoryGame(mediator);
        possibleActionList = new PossibleActionList(board, mediator, storyGame);

        this.chessTypes = chessType;
        figuresStartPositionRepository = new FiguresStartPositionRepository();

        //TODO:
        GameLogger.addLogger(lobbyId);
        MediatorLogger.addLogger(lobbyId);

        gameLoop = new GameLoop(mediator, possibleActionList, board, activeColorController, storyGame);

        GameLogger.getLogger(lobbyId).logStartGame(firstUserConnection.getName(), secondUserConnection.getName());

        initGameMap();
    }

    private void initGameMap() {
        final Character[][] figuresRepository = figuresStartPositionRepository.getStartPosition(chessTypes, "");
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                final IField field = board.getField(i, j);

                final Character currentChar = figuresRepository[i][j];
                if (currentChar != FIELD_CHAR_EMPTY) {
                    final Color color = currentChar < 'a' ? Color.WHITE : Color.BLACK;
                    final Figure figure = figureFactory.createFigure(currentChar, color);

                    mediator.addNewConnection(field, figure);
                }
            }
        }
        possibleActionList.updateRealLists();
    }

    private void sendInitInfo() {
        for (final UserConnection userConnection : connectionController.getConnections()) {
            final BufferedWriter out = userConnection.getOut();
            final IMessage msg = new Message(ClientCodes.INIT_GAME);
           // mediator.updateMediator();
            final IInitGame initGame = new InitGame(mediator, board, userConnection.getColor());

            try {
                send(out, MessageSerializer.serialize(msg));
                send(out, InitGameSerializer.serialize(initGame));
            } catch (final IOException e) {
                ErrorLoggerServer.logException(e);
            }
        }
    }

    private void sendUpdatedMediator(final IAnswer answer, final TurnStatus turnStatus) {
        for (final UserConnection userConnection : connectionController.getConnections()) {
            final BufferedWriter out = userConnection.getOut();
            final IMessage msg = new Message(ClientCodes.UPDATE);
            final IUpdate update = new Update(answer, turnStatus);

            try {
                send(out, MessageSerializer.serialize(msg));
                send(out, UpdateSerializer.serialize(update));
            } catch (final IOException e) {
                ErrorLoggerServer.logException(e);
            }
        }
    }

    private void sendEndGame(final EnumGameState enumGameState, final Color color) {
        for (final UserConnection userConnection : connectionController.getConnections()) {
            final BufferedWriter out = userConnection.getOut();
            final IMessage msg = new Message(ClientCodes.END_GAME);

            final IEndGame endGame = new EndGame(enumGameState, color);

            try {
                send(out, MessageSerializer.serialize(msg));
                send(out, EndGameSerializer.serialize(endGame));
            } catch (final IOException e) {
                ErrorLoggerServer.logException(e);
            }
        }
    }

    private IAnswer transformation(final IAnswer answer, final TurnStatus turnStatus,
                                   final BufferedReader in, final BufferedWriter out) throws IOException {
        sendUpdatedMediator(answer, turnStatus);

        final Message message1 = new Message(ClientCodes.TRANSFORMATION);
        send(out, MessageSerializer.serialize(message1));

        String string = in.readLine();
        TransformationDto transformationDto = TransformationSerializer.deserialize(string);
        char symbol = transformationDto.getFigureChar();

        return new AnswerSimbol(answer.getFinalX(), answer.getFinalY(),
                answer.getFinalX(), answer.getFinalY(), symbol);

    }

    @Override
    public void run() {
        sendInitInfo();
        final ConsoleBoardWriter boardWriter = new ConsoleBoardWriter(mediator, board);
        MessageDto answerMsg = null;
        UserConnection connection = null;

        while (gameLoop.isAlive()) {
            connectionController.update();
            connection = connectionController.getCurrentConnection();
            MediatorLogger.getLogger(lobbyId).logMediatorAction(mediator, board);

            TurnStatus turnStatus = null;
            IAnswer answer = null;

            do {
                try {
                    final BufferedWriter out = connection.getOut();
                    final IMessage message = new Message(ClientCodes.TURN);
                    send(out, MessageSerializer.serialize(message));

                    final BufferedReader in = connection.getIn();
                    final String msg = in.readLine();
                    answerMsg = MessageSerializer.deserialize(msg);

                    if (answerMsg.getClientCodes() == ClientCodes.END_GAME) break;

                    final String turnString = in.readLine();
                    final TurnDto turnDto = AnswerSerializer.deserialize(turnString);
                    turnDto.validate();
                    answer = turnDto.getAnswer();

                    turnStatus = makeTurn(answer);

                    if (turnStatus == TurnStatus.TRANSFORMATION_BEFORE) {
                        answer = transformation(answer, turnStatus, in, out);
                        turnStatus = makeTurn(answer);
                    }

                    GameLogger.getLogger(lobbyId).logPlayerMoveAction(connection.getColor(),
                            mediator.getFigure(new Field(answer.getFinalX(), answer.getFinalY())),
                            new Field(answer.getStartX(), answer.getStartY()),
                            new Field(answer.getFinalX(), answer.getFinalY()), turnStatus);
                } catch (final Exception e) {
                    ErrorLoggerServer.logException(e);
                }

            } while (turnStatus == TurnStatus.ERROR);
            if (answerMsg.getClientCodes() == ClientCodes.END_GAME) break;

            sendUpdatedMediator(answer, turnStatus);
            boardWriter.printBoard();
            /*try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }

        gameLoop.getMatchResult();

        IGameState gameState = gameLoop.getMatchResult();

        if (answerMsg.getClientCodes() == ClientCodes.END_GAME) {
            gameState.updateValue(EnumGameState.RESIGNATION, connection.getColor());
        }

        sendEndGame(gameState.getValue(), gameLoop.getMatchResult().getColor());
        GameLogger.getLogger(lobbyId).logEndGame(gameState.getValue());

        setStatistics(connection, gameState.getValue());
        downService();
    }

    public void setStatistics(UserConnection connection, final EnumGameState enumGameState) {
        final EnumMatchResult matchResult = EnumMatchResult.getEnumMatchResult(enumGameState);
        final String nameFirstPlayer = connection.getName();
        final Color colorFirstPlayer = connection.getColor();

        connectionController.update();
        connection = connectionController.getCurrentConnection();

        final String nameSecondPlayer = connection.getName();
        final Color colorSecondPlayer = connection.getColor();
        try {
            Statistics.setStatistics(nameFirstPlayer, colorFirstPlayer, matchResult,
                    nameSecondPlayer, colorSecondPlayer, EnumMatchResult.swapEnumMatchResult(matchResult));
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    public TurnStatus makeTurn(final IAnswer answer) {
        return gameLoop.doIteration(answer);
    }

    /**
     * закрытие сервера, удаление себя из списка нитей
     */
    private void downService() {
        for (int i = 0; i < 2; i++) {
            connectionController.update();
            try {
                final UserConnection currentConnection = connectionController.getCurrentConnection();
                if (!currentConnection.getSocket().isClosed()) {
                    currentConnection.getSocket().close();
                    currentConnection.getOut().close();
                    currentConnection.getIn().close();
                    server.deleteLobby(this);
                }
            } catch (final IOException ignored) {
            }
        }
    }
}
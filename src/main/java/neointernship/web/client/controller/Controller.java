package neointernship.web.client.controller;

import neointernship.chess.game.model.enums.Color;
import neointernship.chess.logger.ErrorLoggerClient;
import neointernship.web.client.GUI.Input.Input;
import neointernship.web.client.communication.message.ClientCodes;
import neointernship.web.client.communication.message.MessageDto;
import neointernship.web.client.communication.message.ModelMessageReaction;
import neointernship.web.client.communication.serializer.MessageSerializer;
import neointernship.web.client.player.APlayer;
import neointernship.web.client.player.RandomBot;
import neointernship.web.client.player.Player;
import neointernship.web.client.player.PlayerType;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Controller {
    private Input input;
    private BufferedReader in = null;
    private BufferedWriter out = null;
    private Socket socket;
    private Connection connection;
    private ModelMessageReaction modelMessageReaction;
    private APlayer player;
    private boolean endGame = false;

    public void start() throws InterruptedException {
        input = new Input();
        modelMessageReaction = new ModelMessageReaction(socket);

        startConnection();
        initPlayer();

        while (!endGame) {
            try {
                final String jsonMessage = in.readLine();
                final MessageDto messageDto = MessageSerializer.deserialize(jsonMessage);
                messageDto.validate();
                modelMessageReaction.get(messageDto.getClientCodes()).execute(player, in, out);

                if (messageDto.getClientCodes() == ClientCodes.END_GAME) {
                    endGame = true;
                }
            } catch (final Exception e) {
                ErrorLoggerClient.getLogger(player.getName()).logException(e);
            }
        }
    }

    private void initPlayer() throws InterruptedException {
        final HashMap<PlayerType, APlayer> players = new HashMap<>();


        final PlayerType playerType = input.getPlayerType();
        String name = null;
        if (playerType == PlayerType.HUMAN) {
            name = input.getUserName().trim();
            final Color color = input.getColor();
            player = new Player(color, name, input);
        } else {
            name = "random bot";
            player = new RandomBot(Color.BOTH, name, input);
        }

        ErrorLoggerClient.addLogger(name);
    }

    private void startConnection() {
        connection = new Connection();
        connection.connection();

        socket = connection.getSocket();

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (final IOException e) {
            ErrorLoggerClient.getLogger(player.getName()).logException(e);
        }
    }
}

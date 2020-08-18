package neointernship;

import neointernship.chess.game.model.enums.Color;
import neointernship.chess.logger.ErrorLoggerClient;
import neointernship.web.client.GUI.Input.InputVoid;
import neointernship.web.client.communication.message.ClientCodes;
import neointernship.web.client.communication.message.MessageDto;
import neointernship.web.client.communication.message.ModelMessageReaction;
import neointernship.web.client.communication.serializer.MessageSerializer;
import neointernship.web.client.controller.Connection;
import neointernship.web.client.player.APlayer;
import neointernship.web.client.player.Bots.BotOne;
import neointernship.web.client.player.Bots.BotTwo;
import neointernship.web.client.player.RandomBot;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

public class ControllerBot implements Runnable {

    private BufferedReader in = null;
    private BufferedWriter out = null;
    private Socket socket;
    private Connection connection;
    private ModelMessageReaction modelMessageReaction;
    private APlayer player;
    private boolean endGame = false;
    private String botType;
    private Color color;

    private String name;
    LocalTime gameTime;
    private int i = 0;

    public ControllerBot(int i, String botType, Color color) {
        name = "bot" + i % 2;
        this.i = i;
        this.botType = botType;
        this.color = color;
    }

    @Override
    public void run() {
        LocalTime startTime = LocalTime.now();
        modelMessageReaction = new ModelMessageReaction(socket);

        startConnection();

        initPlayer();

        ErrorLoggerClient.addLogger(player.getName());

        while (!endGame) {
            try {
                final String jsonMessage = in.readLine();
                final MessageDto messageDto = MessageSerializer.deserialize(jsonMessage);
                messageDto.validate();
                modelMessageReaction.get(messageDto.getClientCodes()).execute(player, in, out);
                if (messageDto.getClientCodes() == ClientCodes.END_GAME) endGame = true;
            } catch (final Exception e) {
                //ErrorLoggerClient.getLogger(player.getName()).logException(e);
                e.printStackTrace();
            }
        }
        gameTime = LocalTime.ofSecondOfDay(LocalTime.now().toSecondOfDay() - startTime.toSecondOfDay());
        System.out.println(gameTime);
    }

    private void initPlayer() {
        if (botType == "One") {
            player = new BotOne(color, name, new InputVoid());
        } else if (botType == "Two") {
            player = new BotTwo(color, name, new InputVoid());
        } else {
            player = new RandomBot(color, name, new InputVoid());
        }
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

package neointernship.web.client.communication.message.reaction.model;

import neointernship.web.client.communication.data.initinfo.IInitInfo;
import neointernship.web.client.communication.exchanger.MessageExchanger;
import neointernship.web.client.communication.exchanger.InfoExchanger;
import neointernship.web.client.communication.message.IMessage;
import neointernship.web.client.communication.message.Message;
import neointernship.web.client.communication.serializer.InfoSerializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class InfoModel implements IMessageCodeModel {
    @Override
    public void execute(final IMessage message, final BufferedReader in, final BufferedWriter out) throws Exception {
        MessageExchanger.exchange(message);
        final IInitInfo name = InfoExchanger.exchange(null);

        final IMessage mes = new Message(MessageCode.INFO);
        out.write(SerializerForMessage.serializer(mes) + "\n");
        out.flush();

        out.write(InfoSerializer.serialize(name) + "\n");
        out.flush();
    }
}
package com.velware.ak3r;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ReadSelectionKeyHandler implements SelectionKeyHandler {


    @Override
    public void handle(SelectionKey key, ByteBuffer buffer) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();

        buffer.clear();
        int read;
        while ((read = channel.read(buffer)) > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.limit()];
            buffer.get(bytes);
            sb.append(new String(bytes));
            buffer.clear();
        }
        String msg;
        if (read < 0) {
            msg = key.attachment() + " left the chat.\n";
            channel.close();
        } else {
            msg = sb.toString();
        }

        ByteBuffer msgBuf = ByteBuffer.wrap(msg.getBytes());
        if (key.isValid() && key.channel() instanceof SocketChannel) {
            channel.write(msgBuf);
            msgBuf.rewind();
        }
    }
}

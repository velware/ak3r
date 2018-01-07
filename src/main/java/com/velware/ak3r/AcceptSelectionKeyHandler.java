package com.velware.ak3r;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class AcceptSelectionKeyHandler implements SelectionKeyHandler{

    private final static Logger LOG = LoggerFactory.getLogger(AcceptSelectionKeyHandler.class);

    private Selector selector;

    public AcceptSelectionKeyHandler(Selector selector){
        this.selector = selector;
    }

    @Override
    public void handle(SelectionKey key, ByteBuffer buffer) throws IOException {
        SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
        String address = (new StringBuilder( client.socket().getInetAddress().toString() )).append(":").append( client.socket().getPort() ).toString();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ, address);
        client.write(ByteBuffer.wrap(("Welcome ["+address+"]!\n").getBytes()));
        LOG.info("accepted connection from: "+address);
    }
}

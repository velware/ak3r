package com.velware.ak3r;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    private static final String POISON_PILL = "POISON_PILL";
    private ServerSocketChannel channel;
    private Selector selector;
    private String hostname;
    private int port;
    private ByteBuffer buffer;
    private SelectionKeyHandler readSelectionKeyHandler;
    private SelectionKeyHandler acceptSelectionKeyHandler;

    public NioServer(String hostname, int port){
        this.hostname = hostname;
        this.port = port;

        try {
            this.selector = Selector.open();
            this.channel = ServerSocketChannel.open();
            this.channel.bind(new InetSocketAddress(this.hostname, this.port));
            this.channel.configureBlocking(false);
            this.channel.register(selector, SelectionKey.OP_ACCEPT);
            this.buffer = ByteBuffer.allocate(256);
            readSelectionKeyHandler = new ReadSelectionKeyHandler();
            acceptSelectionKeyHandler = new AcceptSelectionKeyHandler(selector);
        }catch (IOException ioe){
            System.err.print(ioe.toString());
        }
    }

    public void start() throws IOException {
        while (true) {
            this.selector.select();
            Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {

                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    acceptSelectionKeyHandler.handle(key, this.buffer);
                }

                if (key.isReadable()) {
                    readSelectionKeyHandler.handle(key,this.buffer);
                }
                iter.remove();
            }
        }
    }

}

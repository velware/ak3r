package com.velware.ak3r;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public interface SelectionKeyHandler {

    public void handle(SelectionKey key, ByteBuffer buffer) throws IOException;

}

package com.larpclient;

import java.io.IOException;

public interface SkyEffect extends AutoCloseable {
   void render(BloomSettings var1) throws IOException;

   void reload();

   @Override
   void close();
}

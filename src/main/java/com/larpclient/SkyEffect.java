package com.larpclient;

import java.io.IOException;

public interface SkyEffect extends AutoCloseable {
   void render(bl var1) throws IOException;

   void reload();

   @Override
   void close();
}

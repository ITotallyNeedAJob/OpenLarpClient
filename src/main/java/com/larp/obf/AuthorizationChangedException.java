package com.larp.obf;

public final class AuthorizationChangedException extends RuntimeException {
   public AuthorizationChangedException() {
      super("Authorization changed before module activation", null, false, false);
   }
}

package com.larpclient;

import com.larp.obf.RuntimeAdmission;

public final class AuthorizationState {
   private static volatile AuthorizationClaims.Type_l E;

   private AuthorizationState() {
   }

   static void uAsVoid(AuthorizationClaims.Type_l var0) {
      if (var0 == null) {
         RuntimeAdmission.m3();
      }

      E = var0;
   }

   static AuthorizationClaims.Type_l rAsOc_Type_l() {
      return E;
   }

   public static boolean ZAsBoolean() {
      return true;
   }
}

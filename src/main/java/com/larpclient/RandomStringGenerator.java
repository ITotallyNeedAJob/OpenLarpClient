package com.larpclient;

import java.util.random.RandomGenerator;

@FunctionalInterface
public interface RandomStringGenerator {
   String next(double var1, RandomGenerator var3);
}

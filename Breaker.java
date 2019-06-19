package com.goldengekko.lhr.util.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class Breaker {
    public static final String FI_HUB = "Fi-Hub";
    public static final String COMARCH = "Comarch";

    @Autowired
    Circuit circuit;

    public static class guard {
        static final Map<String , Circuit> thirdPartyToCircuit = new HashMap<>();

        static {
            thirdPartyToCircuit.put(FI_HUB, new Circuit());
            thirdPartyToCircuit.put(COMARCH, new Circuit());
        }
    }
}

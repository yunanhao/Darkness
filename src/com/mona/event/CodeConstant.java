package com.mona.event;

public class CodeConstant {
    private static int zero = 0;
    public static final int CollisionCheck = zero++;

    static {
        Handler.bind(CollisionCheck, new CollisionCheckRunner());
    }
}

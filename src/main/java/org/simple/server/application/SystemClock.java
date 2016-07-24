package org.simple.server.application;

import java.util.Date;

public class SystemClock implements IClock{

    @Override
    public long getTimestamp() {
        return new Date().getTime();
    }
}

package it;

import org.simple.server.application.IClock;

import java.util.Date;

class TestClock implements IClock {

    private long mCurrentTime = 0;


    @Override
    public long getTimestamp() {
        if (mCurrentTime == 0)
            return new Date().getTime();
        else
            return mCurrentTime;
    }

    public void setTime(long time) {
        mCurrentTime = time;
    }

    public void reset() {
        mCurrentTime = 0;
    }

}

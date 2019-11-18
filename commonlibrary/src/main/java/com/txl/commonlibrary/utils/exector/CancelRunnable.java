package com.txl.commonlibrary.utils.exector;

public abstract class CancelRunnable implements Runnable {

    private boolean cancel = false;

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public void run() {
        if(cancel){
            return;
        }
        realRun();
    }

    protected abstract void realRun();
}

package com.txl.commonlibrary.utils.exector;

public abstract class CancelRunnable implements Runnable {


    public static CancelRunnable createCancelRunnable(CancelRunnable cancelRunnable){
        CancelRunnable temp = cancelRunnable;
        if(cancelRunnable != null){
            temp.setCancel(true);
            temp = null;
        }
        try {
            temp = cancelRunnable.getClass().newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return temp;
    }

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

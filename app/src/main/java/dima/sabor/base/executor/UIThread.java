package dima.sabor.base.executor;


import android.os.Looper;
import android.os.Handler;

public class UIThread implements PostExecutionThread {

    private final Handler handler;

    public UIThread() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }

}
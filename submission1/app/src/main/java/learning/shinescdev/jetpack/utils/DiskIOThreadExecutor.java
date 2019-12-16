package learning.shinescdev.jetpack.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskIOThreadExecutor implements Executor {
    private final Executor mDiskIO;

    DiskIOThreadExecutor(){
        this.mDiskIO = Executors.newSingleThreadExecutor();
    }
    @Override
    public void execute(Runnable command) {
        mDiskIO.execute(command);
    }
}

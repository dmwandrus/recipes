package mil.navair.iframework.common.utilities;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 *
 * @author mweigel - Taken from JAVA Docs
 *
 * The SerialExecutor will serially execute runnable tasks supplied by a target
 * class implementing the Executor interface. Multiple tasks are queued and
 * executed sequentially or in series.
 */
public class SerialExecutor implements Executor {

    final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
    final Executor executor;
    @SuppressWarnings("PackageVisibleField")
    Runnable active;

    public SerialExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public synchronized void execute(final Runnable r) {

        // Wrap this Runnable r in a new Runnable
        // that can schedule next.
        tasks.offer(new Runnable() {
            @Override
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });

        if (active == null) {
            scheduleNext();
        }
    }

    @SuppressWarnings("NestedAssignment")
    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
    }
}
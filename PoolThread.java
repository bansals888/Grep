package Mypackage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class PoolThread extends Thread {

    private LinkedBlockingQueue<Runnable> taskQueue = null;
    volatile private boolean       isStopped = false;
    public AtomicInteger Count;
    public PoolThread(LinkedBlockingQueue<Runnable> queue,AtomicInteger Count){
        taskQueue = queue;
        this.Count=Count;
    }
    public void run(){
        while(!isStopped()){
            try {
                Runnable runnable;
                runnable= (Runnable) taskQueue.take();
                runnable.run();
                Count.addAndGet(-1);

            } catch(Exception e){
                //log or otherwise report exception,
                //but keep pool thread alive.
            }

        }
    }
    public synchronized void doStop(){
            isStopped = true;
            this.interrupt(); //break pool thread out of dequeue() call.
    }
    public synchronized boolean isStopped(){
        return isStopped;
    }
}
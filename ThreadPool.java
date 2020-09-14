package Mypackage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    private LinkedBlockingQueue<Runnable> taskQueue = null;
    private List<PoolThread> threads = new ArrayList<PoolThread>();
    volatile private boolean isStopped = false;
    public AtomicInteger Count=new AtomicInteger ();
    public ThreadPool(int noOfThreads){
        taskQueue = new LinkedBlockingQueue<Runnable>();

        for(int i=0; i<noOfThreads; i++){
            threads.add(new PoolThread(taskQueue,Count));
        }
        for(PoolThread thread : threads){
            thread.start();
        }
    }
    public synchronized void  execute(Runnable task) throws Exception{
        if(this.isStopped) throw
                new IllegalStateException("ThreadPool is stopped");
        Count.addAndGet(1);
        this.taskQueue.put(task);
    }
    public void stop(){
        while (taskQueue.size()>0||Count.get()>0){

        }
        this.isStopped = true;


        for(PoolThread thread : threads){
            thread.doStop();
        }

    }

}
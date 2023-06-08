package Service;

import Properties.States;

public class Scheduler extends Thread {//进程调度：FIFO
    @Override
    public void run() {
        System.out.println("线程启动");
        while (true) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!ManageProcess.getReadyList().isEmpty() && !ManageProcess.isFull()){//fifo策略进程调度，先来先服务，从就绪队列中最前方装入运行队列
                System.out.println("符合条件");
                Process process = ManageProcess.getReadyList().poll();
                System.out.println("Readylist出列");
                ManageProcess.insert(ManageProcess.getRunList(),process);
                process.getPcb().setState(States.PROCESS_RUN);
                System.out.println("RunList入列");
                process.schedule();
                System.out.println("进程启动");
            }
        }
    }
}

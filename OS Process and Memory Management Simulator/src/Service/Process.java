package Service;

import Properties.PCB;
import Properties.States;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static Service.ManageProcess.getBlockList;

public class Process {//进程控制
    private PCB pcb;

    private Timer t;
    private ProcessStop task;
    private long startTime;
    private long pauseTime;
    private Date date = new Date();

    public Process(PCB pcb) {
        this.pcb = pcb;
        creat();
    }

    public void creat(){//创建进程
        pcb.setMark(true);
        pcb.setPid(ManagePCB.getPID());
        if(ManageMemory.allocateMemory(pcb)){//分配内存
            pcb.setState(States.PROCESS_READY);
            ManageProcess.insert(ManageProcess.getProcesses(),this);
            ManageProcess.insert(ManageProcess.getReadyList(),this);
        }
        else{
            pcb.setState(States.PROCESS_BLOCK);
            ManageProcess.insert(ManageProcess.getProcesses(),this);
            ManageProcess.insert(ManageProcess.getBlockList(),this);
        }
    }

    public void kill(){//撤销进程
        pcb.setSched(false);
        if(pcb.getState().equals(States.PROCESS_RUN)){//运行状态撤销
            pcb.setSched(true);
            ManageProcess.getRunList().remove(ManageProcess.getProcess(pcb));
            ManageMemory.retrieveMemory(this.pcb);
        }
        if(pcb.getState().equals(States.PROCESS_READY)){//就绪状态撤销
            ManageProcess.getReadyList().remove(ManageProcess.getProcess(pcb));
            ManageMemory.retrieveMemory(this.pcb);
        }
        if(pcb.getState().equals(States.PROCESS_BLOCK)){//阻塞状态撤销
            ManageProcess.getBlockList().remove(ManageProcess.getProcess(pcb));
            ManageMemory.retrieveMemory(this.pcb);
        }
        ManagePCB.retrievePCB(this.pcb);
        ManageProcess.getProcesses().remove(this);
    }

    public void block(){//阻塞进程
        //Timer Stop
        date.setTime(System.currentTimeMillis());
        pauseTime = date.getTime();
        pcb.setTime(pcb.getTime() - (pauseTime - startTime));
        t.cancel();
        task.cancel();
        //退出RunList
        ManageProcess.getRunList().remove(this);
        //——>BlockList
        ManageProcess.insert(getBlockList(),this);
        //State->Block
        pcb.setState(States.PROCESS_BLOCK);
    }

    public void wakeup(){//唤醒进程
        ManageProcess.getBlockList().remove(this);
        pcb.setState(States.PROCESS_READY);
        ManageProcess.insert(ManageProcess.getReadyList(),this);
    }

    public void schedule(){//时间分配
        t = new Timer();
        task = new ProcessStop(this);
        date.setTime(System.currentTimeMillis());
        startTime = date.getTime();
        t.schedule(task,pcb.getTime());
    }

    public PCB getPcb() {
        return pcb;
    }

    public Timer getT() {
        return t;
    }

    public ProcessStop getTask() {
        return task;
    }

    public void setT(Timer t) {
        this.t = t;
    }

    public void setTask(ProcessStop task) {
        this.task = task;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(long pauseTime) {
        this.pauseTime = pauseTime;
    }

    @Override
    public String toString() {
        return pcb.toString();
    }
}

class ProcessStop extends TimerTask {
    private Process process;

    public ProcessStop(Process process) {
        this.process = process;
    }

    @Override
    public void run() {
        process.kill();
    }

}




package Service;

import Properties.FreeBlock;
import Properties.PCB;

import java.util.*;

public class ManageProcess {//进程队列
    //四个static数组保存数据
    private static List<Process> processes = Collections.synchronizedList(new ArrayList<>());//进程列表
    private static Queue<Process> ReadyList = new LinkedList<>();//就绪队列
    private static Queue<Process> RunList = new LinkedList<>();//运行队列
    private static Queue<Process> BlockList = new LinkedList<>();//阻塞队列

    //两个插入函数，往static数组插入内容
    public static void insert(Queue<Process> List,Process process){
        List.offer(process);
    }

    public static void insert(List<Process> List,Process process){
        List.add(process);
    }


    public static Process getProcess(PCB pcb){
        for(Process a:processes){
            if(a.getPcb() == pcb)return a;
        }
        return null;
    }
    public static Queue<Process> getReadyList() {
        return ReadyList;
    }

    public static Queue<Process> getRunList() {
        return RunList;
    }
    public static boolean isFull(){//cpu最多同时处理三个进程
        if(RunList.size() == 3)return true;
        return false;
    }

    public static Queue<Process> getBlockList() {
        return BlockList;
    }

    public static List<Process> getProcesses() {
        return processes;
    }

}

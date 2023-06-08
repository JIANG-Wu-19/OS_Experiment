package Service;

import Properties.FreeBlock;
import Properties.PCB;

import java.util.*;

public class ManageProcess {//���̶���
    //�ĸ�static���鱣������
    private static List<Process> processes = Collections.synchronizedList(new ArrayList<>());//�����б�
    private static Queue<Process> ReadyList = new LinkedList<>();//��������
    private static Queue<Process> RunList = new LinkedList<>();//���ж���
    private static Queue<Process> BlockList = new LinkedList<>();//��������

    //�������뺯������static�����������
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
    public static boolean isFull(){//cpu���ͬʱ������������
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

package Service;

import Properties.PCB;


public class ManagePCB {//PCB分配
    private static int PID = 0;
    private static PCB[] pcbs = new PCB[]{new PCB(),new PCB(),new PCB(),new PCB(),new PCB(),new PCB(),new PCB(),new PCB(),new PCB(),new PCB()};


    public static PCB getFreePCB(){//获取空闲PCB
        for(PCB a:pcbs){
            if(a.getMark() == false)return a;
        }
        return null;
    }

    public static PCB getPCB(int pid){
        for(PCB a:pcbs){
            if(a.getPid() == pid)return a;
        }
        return null;
    }

    public static void retrievePCB(PCB pcb){
        pcb.setMark(false);
    }
    public static int getPID(){
        return PID++;
    }


}

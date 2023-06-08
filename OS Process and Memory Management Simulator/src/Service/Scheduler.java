package Service;

import Properties.States;

public class Scheduler extends Thread {//���̵��ȣ�FIFO
    @Override
    public void run() {
        System.out.println("�߳�����");
        while (true) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(!ManageProcess.getReadyList().isEmpty() && !ManageProcess.isFull()){//fifo���Խ��̵��ȣ������ȷ��񣬴Ӿ�����������ǰ��װ�����ж���
                System.out.println("��������");
                Process process = ManageProcess.getReadyList().poll();
                System.out.println("Readylist����");
                ManageProcess.insert(ManageProcess.getRunList(),process);
                process.getPcb().setState(States.PROCESS_RUN);
                System.out.println("RunList����");
                process.schedule();
                System.out.println("��������");
            }
        }
    }
}

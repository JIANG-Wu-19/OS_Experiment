package View;

import Properties.FreeBlock;
import Properties.PCB;
import Service.Process;
import Service.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Console {
    static JTextArea outputTextArea = new JTextArea();
    static JTextField inputTextField = new JTextField();

    public static void main(String[] args) {
        JFrame frame = new JFrame("���̹������ڴ����ģ�����");
        outputTextArea.setFont(new Font("����", Font.BOLD, 20));
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(Color.cyan);

        inputTextField.setBackground(Color.lightGray);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(inputTextField, BorderLayout.SOUTH);

        outputTextArea.append("--------------���̹������ڴ����ģ�����--------------\n");
        outputTextArea.append("--------------createproc ʱ�� �ڴ��С:�ύ��ҵ����---\n--------------killproc ���̺�:��ֹ����---------------\n--------------iostartproc ���̺�:������������--------\n--------------iofinishproc ���̺�:�������̻�������----\n--------------psproc:��ʾ���н���״̬����------------\n--------------mem:��ʾ�ڴ�ռ�ʹ�������Ϣ------------\n\n");

        inputTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = inputTextField.getText();
                inputTextField.setText("");
                process(content);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(500, 200, 800, 600);
        frame.setVisible(true);
        new Scheduler().start();
    }

    private static void process(String command) {
        // �жϲ���
        // ��������
        outputTextArea.append(command+'\n');
        if (command.startsWith("createproc ")) {
            // �õ����̹�������ʱ��������ڴ��С
            String[] contents = command.split(" ");
            // ��ȡ����PCB
            PCB pcb = ManagePCB.getFreePCB();
            // ʧ�ܵ����
            if (pcb == null) {
                outputTextArea.append("��������ʧ�ܣ�û�п��е�PCB" + "\n");
                return;
            }
            // �ɹ��ʹ����½���
            pcb.setTime(Integer.parseInt(contents[1]) * 1000);
            pcb.setSize(Integer.parseInt(contents[2]));
            new Process(pcb);
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // ��ֹ����
        else if (command.startsWith("killproc ")) {
            String[] contents = command.split(" ");
            PCB pcb = ManagePCB.getPCB(Integer.parseInt(contents[1]));
            if (pcb == null) {
                outputTextArea.append("�ý��̲�����" + "\n");
                return;
            }
            ManageProcess.getProcess(pcb).kill();
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // ��������
        else if (command.startsWith("iostartproc ")) {
            String[] contents = command.split(" ");
            PCB pcb = ManagePCB.getPCB(Integer.parseInt(contents[1]));
            if (pcb == null) {
                outputTextArea.append("�ý��̲�����" + "\n");
                return;
            }
            // �ж��Ƿ񱻵���
            if (ManageProcess.getProcess(pcb) == null) {
                outputTextArea.append("�ý���δ�����ȣ���������ʧ��" + "\n");
                return;
            }
            ManageProcess.getProcess(pcb).block();
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // ���ѽ���
        else if (command.startsWith("iofinishproc ")) {
            String[] contents = command.split(" ");
            PCB pcb = ManagePCB.getPCB(Integer.parseInt(contents[1]));
            if (pcb == null) {
                outputTextArea.append("�ý��̲�����" + "\n");
                return;
            }
            ManageProcess.getProcess(pcb).wakeup();
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // ��ʾ���н���״̬
        else if (command.equals("psproc")) {
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // ��ʾ�ڴ�ռ�ʹ�����
        else if (command.equals("mem")) {
            ManageMemory.getFreeBlocksList().sort((o1, o2) -> {
                if (o1.getIndex() < o2.getIndex()) return -1;
                return 1;
            });

            ManageProcess.getProcesses().sort((o1, o2) -> {
                if (o1.getPcb().getMemoryAddress() < o2.getPcb().getMemoryAddress()) return -1;
                return 1;
            });

            int i = 0, j = 0, k = ManageProcess.getProcesses().size(), t = ManageMemory.getFreeBlocksList().size();
            List<Process> processes = ManageProcess.getProcesses();
            List<FreeBlock> freeBlocksList = ManageMemory.getFreeBlocksList();
            while (i < k && j < t) {
                if (processes.get(i).getPcb().getMemoryAddress() < freeBlocksList.get(j).getIndex()) {
                    outputTextArea.append(processes.get(i).getPcb().getMemoryAddress() + " --- " + (processes.get(i).getPcb().getMemoryAddress() + processes.get(i).getPcb().getSize() - 1) + "  �����" + processes.get(i).getPcb().getPid() + "�Ž���" + "\n");
                    i++;
                } else {
                    outputTextArea.append(freeBlocksList.get(j).getIndex() + " --- " + (freeBlocksList.get(j).getIndex() + freeBlocksList.get(j).getLength() - 1) + "����" + "\n");
                    j++;
                }
            }
            if (i < k) {
                while (i < k) {
                    outputTextArea.append(processes.get(i).getPcb().getMemoryAddress() + " --- " + (processes.get(i).getPcb().getMemoryAddress() + processes.get(i).getPcb().getSize() - 1) + "  �����" + processes.get(i).getPcb().getPid() + "�Ž���" + "\n");
                    i++;
                }
            }
            if (j < t) {
                while (j < t) {
                    outputTextArea.append(freeBlocksList.get(j).getIndex() + " --- " + (freeBlocksList.get(j).getIndex() + freeBlocksList.get(j).getLength() - 1) + "  ����" + "\n");
                    j++;
                }
            }
        }
        else if (command.equals("look")) {
            System.out.println(ManageMemory.getFreeBlocksList().toString());
        }
        else {
            outputTextArea.append("�޷�ʶ�������" + "\n");
        }
    }
}

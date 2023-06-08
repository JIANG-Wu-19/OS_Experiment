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
        JFrame frame = new JFrame("进程管理与内存分配模拟程序");
        outputTextArea.setFont(new Font("宋体", Font.BOLD, 20));
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(Color.cyan);

        inputTextField.setBackground(Color.lightGray);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(inputTextField, BorderLayout.SOUTH);

        outputTextArea.append("--------------进程管理与内存分配模拟程序--------------\n");
        outputTextArea.append("--------------createproc 时间 内存大小:提交作业命令---\n--------------killproc 进程号:终止进程---------------\n--------------iostartproc 进程号:阻塞进程命令--------\n--------------iofinishproc 进程号:阻塞进程唤醒命令----\n--------------psproc:显示所有进程状态命令------------\n--------------mem:显示内存空间使用情况信息------------\n\n");

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
        // 判断操作
        // 创建进程
        outputTextArea.append(command+'\n');
        if (command.startsWith("createproc ")) {
            // 得到进程估计运行时间和所需内存大小
            String[] contents = command.split(" ");
            // 获取空闲PCB
            PCB pcb = ManagePCB.getFreePCB();
            // 失败的情况
            if (pcb == null) {
                outputTextArea.append("创建进程失败，没有空闲的PCB" + "\n");
                return;
            }
            // 成功就创建新进程
            pcb.setTime(Integer.parseInt(contents[1]) * 1000);
            pcb.setSize(Integer.parseInt(contents[2]));
            new Process(pcb);
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // 终止进程
        else if (command.startsWith("killproc ")) {
            String[] contents = command.split(" ");
            PCB pcb = ManagePCB.getPCB(Integer.parseInt(contents[1]));
            if (pcb == null) {
                outputTextArea.append("该进程不存在" + "\n");
                return;
            }
            ManageProcess.getProcess(pcb).kill();
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // 阻塞进程
        else if (command.startsWith("iostartproc ")) {
            String[] contents = command.split(" ");
            PCB pcb = ManagePCB.getPCB(Integer.parseInt(contents[1]));
            if (pcb == null) {
                outputTextArea.append("该进程不存在" + "\n");
                return;
            }
            // 判断是否被调度
            if (ManageProcess.getProcess(pcb) == null) {
                outputTextArea.append("该进程未被调度，阻塞操作失败" + "\n");
                return;
            }
            ManageProcess.getProcess(pcb).block();
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // 唤醒进程
        else if (command.startsWith("iofinishproc ")) {
            String[] contents = command.split(" ");
            PCB pcb = ManagePCB.getPCB(Integer.parseInt(contents[1]));
            if (pcb == null) {
                outputTextArea.append("该进程不存在" + "\n");
                return;
            }
            ManageProcess.getProcess(pcb).wakeup();
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // 显示所有进程状态
        else if (command.equals("psproc")) {
            outputTextArea.append(ManageProcess.getProcesses().toString() + "\n");
        }
        // 显示内存空间使用情况
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
                    outputTextArea.append(processes.get(i).getPcb().getMemoryAddress() + " --- " + (processes.get(i).getPcb().getMemoryAddress() + processes.get(i).getPcb().getSize() - 1) + "  分配给" + processes.get(i).getPcb().getPid() + "号进程" + "\n");
                    i++;
                } else {
                    outputTextArea.append(freeBlocksList.get(j).getIndex() + " --- " + (freeBlocksList.get(j).getIndex() + freeBlocksList.get(j).getLength() - 1) + "空闲" + "\n");
                    j++;
                }
            }
            if (i < k) {
                while (i < k) {
                    outputTextArea.append(processes.get(i).getPcb().getMemoryAddress() + " --- " + (processes.get(i).getPcb().getMemoryAddress() + processes.get(i).getPcb().getSize() - 1) + "  分配给" + processes.get(i).getPcb().getPid() + "号进程" + "\n");
                    i++;
                }
            }
            if (j < t) {
                while (j < t) {
                    outputTextArea.append(freeBlocksList.get(j).getIndex() + " --- " + (freeBlocksList.get(j).getIndex() + freeBlocksList.get(j).getLength() - 1) + "  空闲" + "\n");
                    j++;
                }
            }
        }
        else if (command.equals("look")) {
            System.out.println(ManageMemory.getFreeBlocksList().toString());
        }
        else {
            outputTextArea.append("无法识别的命令" + "\n");
        }
    }
}

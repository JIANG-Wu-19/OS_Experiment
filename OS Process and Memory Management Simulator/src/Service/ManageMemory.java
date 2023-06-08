package Service;

import Properties.FreeBlock;
import Properties.PCB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageMemory {//内存管理
    private static int Memory[] = new int[100];
    private static List<FreeBlock> freeBlocksList = Collections.synchronizedList(new ArrayList<FreeBlock>());//空闲内存块
    static {
        freeBlocksList.add(new FreeBlock(0,Memory.length));
    }

    public static boolean allocateMemory(PCB pcb) {//分配内存，可变分区，最佳适应算法
        //对升序排序，挑选最合适空闲块
        freeBlocksList.sort((o1, o2) -> {
            if (o1.getLength() < o2.getLength()) return -1;
            return 1;
        });

        boolean flag = false;
        for (int i = 0; i < freeBlocksList.size(); i++) {
            FreeBlock a = freeBlocksList.get(i);
            if (a.getLength() < pcb.getSize()) continue;
            pcb.setMemoryAddress(a.getIndex());
            if (a.getLength() == pcb.getSize()) {
                freeBlocksList.remove(a);
                flag = true;
                break;
            }
            a.setIndex(a.getIndex() + pcb.getSize());
            a.setLength(a.getLength() - pcb.getSize());
            flag = true;
            break;
        }
        return flag;
    }

    public static void retrieveMemory(PCB pcb) {//释放内存
        freeBlocksList.sort((o1, o2) -> {
            if (o1.getIndex() < o2.getIndex()) return -1;
            return 1;
        });
        //回收内存

        if (freeBlocksList.size() == 0) {
            freeBlocksList.add(new FreeBlock(pcb.getMemoryAddress(), pcb.getSize()));
            return;
        }

        int i = 0;
        FreeBlock a;
        for (; i < freeBlocksList.size(); i++) {
            a = freeBlocksList.get(i);
            if (a.getIndex() > pcb.getMemoryAddress()) break;
        }

        System.out.println("第" + i + "个空闲块");
        if (i == 0) {
            FreeBlock back = freeBlocksList.get(i);
            if (back.getIndex() == pcb.getMemoryAddress() + pcb.getSize()) {
                back.setIndex(pcb.getMemoryAddress());
                back.setLength(pcb.getSize() + back.getLength());
            } else {
                freeBlocksList.add(new FreeBlock(pcb.getMemoryAddress(), pcb.getSize()));
            }
            return;
        }

        if (i >= freeBlocksList.size()) {
            FreeBlock front = freeBlocksList.get(i - 1);
            if (front.getIndex() + front.getLength() == pcb.getMemoryAddress()) {
                front.setLength(front.getLength() + pcb.getSize());
            } else {
                freeBlocksList.add(new FreeBlock(pcb.getMemoryAddress(), pcb.getSize()));
            }
            return;
        }

        FreeBlock front = freeBlocksList.get(i - 1);
        FreeBlock back = freeBlocksList.get(i);
        if (front.getIndex() + front.getLength() == pcb.getMemoryAddress() && back.getIndex() == pcb.getMemoryAddress() + pcb.getSize()) {
            front.setLength(front.getLength() + back.getLength() + pcb.getSize());
            freeBlocksList.remove(back);
        } else if (front.getIndex() + front.getLength() == pcb.getMemoryAddress()) {
            front.setLength(front.getLength() + pcb.getSize());
        } else if (back.getIndex() == pcb.getMemoryAddress() + pcb.getSize()) {
            back.setIndex(pcb.getMemoryAddress());
            back.setLength(pcb.getSize() + back.getLength());
        } else {
            freeBlocksList.add(new FreeBlock(pcb.getMemoryAddress(), pcb.getSize()));
        }
    }

    public static int[] getMemory() {
        return Memory;
    }

    public static List<FreeBlock> getFreeBlocksList() {
        return freeBlocksList;
    }
}

package Properties;

public class PCB {//进程控制块
    private int pid;//进程标识符
    private String state;//进程状态
    private long time;//进程运行时间
    private int size;//进程所需内存大小
    private int priority;//进程优先级
    private boolean mark;//进程是否被标记
    private int memoryAddress;//进程内存地址
    private boolean Sched;//进程是否被调度

    //构造函数
    public PCB(){
        mark = false;
    }
    
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean getMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public int getMemoryAddress() {
        return memoryAddress;
    }

    public void setMemoryAddress(int memoryAddress) {
        this.memoryAddress = memoryAddress;
    }

    public boolean isSched() {
        return Sched;
    }

    public void setSched(boolean sched) {
        Sched = sched;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "{" +
                "pid=" + pid +
                ", state='" + stateToString(state) + '\'' +
                ", memoryAddress=" + memoryAddress +
                ", time=" + time/1000 +
                '}'+'\n';
    }

    private String stateToString(String state){//将状态码转换为状态
        switch(state){
            case "3" : return "Ready";
            case "4" : return "Block";
            case "5" : return "Run";
            default:return "Unknow";
        }
    }
}

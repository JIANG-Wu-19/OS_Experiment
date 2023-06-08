package Properties;

//进程状态
public interface States {
    String PROCESS_CREATE = "1";//创建
    String PROCESS_REVOKE = "2";//撤销
    String PROCESS_READY = "3";//就绪
    String PROCESS_BLOCK = "4";//阻塞
    String PROCESS_RUN = "5";//运行
}

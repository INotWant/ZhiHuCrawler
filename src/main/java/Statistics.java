public class Statistics {


    private int demandNum;
    private int totalNum = 0;
    private static Statistics statistics;

    private Statistics(int demandNum) {
        this.demandNum = demandNum;
    }

    public static Statistics getInstance(int demandNum) {
        if (statistics == null) {
            synchronized (Statistics.class) {
                if (statistics == null)
                    statistics = new Statistics(demandNum);
            }
        }
        return statistics;
    }

    public static Statistics getInstance() {
        if (statistics == null)
            throw new RuntimeException("Statistics 为空，请先使用 getInstance(int) 创建！");
        return statistics;
    }

    public synchronized int getTotalNum() {
        return totalNum;
    }

    public synchronized void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public synchronized void increase() {
        ++totalNum;
    }

    public synchronized boolean compareDemandNum(){
        return totalNum <= demandNum;
    }
}

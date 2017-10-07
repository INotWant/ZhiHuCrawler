package ZhiHuTopics.manager;

/**
 * {@link Statistics} 类用于统计已经爬取的 question 的数目，并提供方法判断是否达到所需数目。【注：这里统计的所有指定 topic 下总 question 的数目】
 * 此类被设计为单例模式。
 *
 * @author kissx
 */

public class Statistics {


    private int demandNum;
    private int totalNum = 0;
    private static Statistics statistics;

    private Statistics(int demandNum) {
        this.demandNum = demandNum;
    }

    /**
     * 获取 Statistics 对象，需初始化 demandNum（所需爬取的 question 总数）
     * 【注：使用“双重判断”实现单例】
     *
     * @param demandNum 所需数目
     * @return Statistics 对象
     */
    public static Statistics getInstance(int demandNum) {
        if (statistics == null) {
            synchronized (Statistics.class) {
                if (statistics == null)
                    statistics = new Statistics(demandNum);
            }
        }
        return statistics;
    }

    /**
     * 当且仅当 Statistics getInstance(int demandNum) 方法调用后，才会返回非 NULL 对象。
     *
     * @return Statistics 对象
     */
    public static Statistics getInstance() {
        return statistics;
    }

    public synchronized int getTotalNum() {
        return totalNum;
    }

    public synchronized void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * 已爬取 Question 数目加 1
     */
    public synchronized void increase() {
        ++totalNum;
    }

    /**
     * 判断是否达到所需数目
     */
    public synchronized boolean compareDemandNum() {
        return totalNum < demandNum;
    }
}

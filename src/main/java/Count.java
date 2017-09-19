public class Count {
    private int totalNum;

    public synchronized int getTotalNum() {
        return totalNum;
    }

    public synchronized void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public synchronized void increase(){
        ++totalNum;
    }


}

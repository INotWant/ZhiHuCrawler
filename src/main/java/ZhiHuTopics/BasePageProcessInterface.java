package ZhiHuTopics;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

public interface BasePageProcessInterface {

    void contentProcess(Page page);
    Html jsonProcess(Page page);
    void addTargetRequests(Html html,Page page);
    void startUpSpider(Html html,String fileName,String topic_id);
}
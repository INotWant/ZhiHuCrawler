package ZhiHuTopics.PageProcessor;

import ZhiHuTopics.other.SpiderManage;
import ZhiHuTopics.other.Statistics;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author kissx on 2017/9/18.
 */
public class ZhihuMorePageProcessor extends BasePageProcessImp implements PageProcessor {

    private String topic_id;

    public ZhihuMorePageProcessor(String topic_id) {
        this.topic_id = topic_id;
    }

    private Site site = Site.me()
            .setRetrySleepTime(10)
            .setSleepTime(1000)
            .setTimeOut(10000)
            .setCharset("UTF-8");

    @Override
    public void process(Page page) {
        String URL_QUESTION = "https://www\\.zhihu\\.com/question/.*";
        if (page.getUrl().regex(URL_QUESTION).match()) {
            page.putField("标题", page.getHtml().xpath("//h1[@class=\"QuestionHeader-title\"]/text()").get());
            List<String> list = page.getHtml().xpath("//div[@class=\"NumberBoard-value\"]/text()").all();
            if (list != null && list.size() >= 2) {
                page.putField("关注者", list.get(0));
                page.putField("浏览人数", list.get(1));
                if (Statistics.getInstance() != null) {
                    Statistics statistics = Statistics.getInstance();
                    statistics.increase();
                    if (!statistics.compareDemandNum())
                        SpiderManage.getInstance().deleteAll();
                }
            } else
                page.setSkip(true);
        } else {
            Html html = jsonProcess(page);
            List<String> urlList = html.xpath("//a[@class=\"question_link\"]/@href").all();
            List<String> newUrlList = new ArrayList<>();
            for (String urlStr : urlList) {
                urlStr = "https://www.zhihu.com" + urlStr;
                newUrlList.add(urlStr);
            }
            page.addTargetRequests(newUrlList);
            List<String> list = html.xpath("//div[@class=\"feed-item feed-item-hook  folding\"]/@data-score").all();
            if (list != null && list.size() > 0 && topic_id != null) {
                String offset = list.get(list.size() - 1);
                try {
                    // 下面拼接的 URL 是能够爬取指定 topic 下指定位置下 question 的关键
                    Request request = new Request("https://www.zhihu.com/node/TopicFeedList?method=next&params=" + URLEncoder.encode("{\"offset\":" + offset + ",\"topic_id\":" + topic_id + ",\"feed_type\":\"smart_feed\"}", "utf-8"));
                    request.setMethod(HttpConstant.Method.POST);
                    page.addTargetRequest(request);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}

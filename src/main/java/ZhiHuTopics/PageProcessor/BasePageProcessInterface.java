package ZhiHuTopics.PageProcessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;

/**
 * {@link BasePageProcessInterface} 接口，声明了常见页面处理方法。
 * 【注：个别方法由于当时考虑不全，感觉不应该抽出来。可，不知后面需求，故没有改。不影响最终结果】（已删）
 *
 * @author kissx
 */
public interface BasePageProcessInterface {

    /**
     * 对返回内容为 json 的处理
     *
     * @param page page 页面
     * @return Html
     */
    Html jsonProcess(Page page);

}
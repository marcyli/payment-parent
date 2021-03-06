package cn.aposoft.ecommerce.wechat.service;

import cn.aposoft.ecommerce.wechat.config.BaseWechatConfig;
import cn.aposoft.ecommerce.wechat.config.WechatPubPropertiesConfig;
import cn.aposoft.ecommerce.wechat.httpclient.HttpRequestUtil;
import cn.aposoft.ecommerce.wechat.httpclient.HttpRequestUtilImpl;
import cn.aposoft.ecommerce.wechat.service.impl.BasePaymentServiceImpl;
import cn.aposoft.ecommerce.wechat.service.middle.PaymentServiceImpl;
import org.junit.Before;

/**
 * @author code
 * @Title: BaseAppTest
 * @Copyright: Copyright (c) 2017
 * @Description: <br>
 * @Company: www.qdingnet.com
 * @Created on 2018/8/19下午8:57
 */
public class BaseAppTest {

    protected static String pkcs12="/Users/yujinshui/Desktop/千丁互联/项目资料/微信公众号信息/wechatpub.properties";
    public static BaseWechatConfig config = new WechatPubPropertiesConfig(pkcs12,"UTF-8");
//    public static BaseWechatConfig config = new WechatPubPropertiesConfig();

    private HttpRequestUtil httpClientUtl = null;
    protected BasePaymentService basePaymentService = null;
    protected PaymentService paymentService = null;

    @Before
    public void config() {

        httpClientUtl = HttpRequestUtilImpl.getInstance(config);
        basePaymentService = new BasePaymentServiceImpl(httpClientUtl);
        paymentService = new PaymentServiceImpl(config, basePaymentService);
    }
}

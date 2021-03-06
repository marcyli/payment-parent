package cn.aposoft.ecommerce.wechat.service.middle;

import cn.aposoft.ecommerce.wechat.beans.protocol.close_protocol.WechatCloseResData;
import cn.aposoft.ecommerce.wechat.beans.protocol.downloadbill_protocol.WechatDownloadBillResData;
import cn.aposoft.ecommerce.wechat.beans.protocol.pay_protocol.WeChatPayResData;
import cn.aposoft.ecommerce.wechat.beans.protocol.pay_query_protocol.WechatPayQueryResData;
import cn.aposoft.ecommerce.wechat.beans.protocol.refund_protocol.WeChatRefundResData;
import cn.aposoft.ecommerce.wechat.beans.protocol.refund_query_protocol.WechatRefundQueryResData;
import cn.aposoft.ecommerce.wechat.config.BaseWechatConfig;
import cn.aposoft.ecommerce.wechat.config.WechatPubPropertiesConfig;
import cn.aposoft.ecommerce.wechat.enums.SignTypeEnum;
import cn.aposoft.ecommerce.wechat.enums.UrlEnum;
import cn.aposoft.ecommerce.wechat.exceptions.VerifySignFailException;
import cn.aposoft.ecommerce.wechat.exceptions.WechatConfigNullException;
import cn.aposoft.ecommerce.wechat.params.*;
import cn.aposoft.ecommerce.wechat.service.BasePaymentService;
import cn.aposoft.ecommerce.wechat.service.PaymentService;
import cn.aposoft.ecommerce.wechat.tencent.WechatSignature;
import cn.aposoft.ecommerce.wechat.tencent.WechatUtil;
import cn.aposoft.ecommerce.wechat.util.LogPortal;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 支付接口的实现类
 *
 * @author code
 * @Title: PaymentServiceImpl
 * @Copyright: Copyright (c) 2017
 * @Description: <br>
 * @Company: www.qdingnet.com
 * @Created on 2018/8/19下午6:01
 */
public class PaymentServiceImpl implements PaymentService, AutoCloseable {
    BaseWechatConfig config;
    BasePaymentService basePaymentService;

    public PaymentServiceImpl(BaseWechatConfig config, BasePaymentService basePaymentService) {
        this.config = config;
        this.basePaymentService = basePaymentService;
    }

    @Override
    public WeChatPayResData pay(OrderParams orderParams) throws Exception {
        if (config == null) {
            config = new WechatPubPropertiesConfig();
        } else {
            checkWechatConfig(config, UrlEnum.PAY_URL);
        }
        //检查完成没有问题，开始发起HTTP请求
        return basePaymentService.pay(orderParams, config);
    }

    protected void checkWechatConfig(BaseWechatConfig config, UrlEnum urlEnum) throws Exception {
        if (StringUtils.isEmpty(config.getAppID())) {
            throw new WechatConfigNullException("appId is null");
        }
        if (StringUtils.isEmpty(config.getMchID())) {
            throw new WechatConfigNullException("mchId is null");
        }
        if (StringUtils.isEmpty(config.getKey())) {
            throw new WechatConfigNullException("key is null");
        }
        if (StringUtils.isEmpty(config.getSubMchId())) {
            throw new WechatConfigNullException("subMchId is null");
        }

        switch (urlEnum) {
            case PAY_URL:
                if (StringUtils.isEmpty(config.getPayUrl())
                        || StringUtils.isEmpty(config.getNotifyUrl())) {
                    throw new WechatConfigNullException("PAY_URL is null");
                }
                break;
            case REFUND_URL:
                if (StringUtils.isEmpty(config.getRefundUrl())) {
                    throw new WechatConfigNullException("REFUND_URL is null");
                }
                break;
            case CLOSE_ORDER_URL:
                if (StringUtils.isEmpty(config.getCloseOrderUrl())) {
                    throw new WechatConfigNullException("CLOSE_ORDER_URL is null");
                }
                break;
            case REFUND_QUERY_URL:
                if (StringUtils.isEmpty(config.getOrderQueryUrl())) {
                    throw new WechatConfigNullException("REFUND_QUERY_URL is null");
                }
                break;
            case DOWNLOAD_BILL_URL:
                if (StringUtils.isEmpty(config.getOrderQueryUrl())) {
                    throw new WechatConfigNullException("DOWNLOAD_BILL_URL is null");
                }
                break;
            case ORDER_QUERY_URL:
                if (StringUtils.isEmpty(config.getOrderQueryUrl())) {
                    throw new WechatConfigNullException("ORDER_QUERY_URL is null");
                }
                break;
            case NOTIFY_URL:
                if (StringUtils.isEmpty(config.getOrderQueryUrl())) {
                    throw new WechatConfigNullException("NOTIFY_URL is null");
                }

        }
    }

    @Override
    public WechatPayQueryResData query(OrderQueryParams orderQueryParams) throws Exception {
        if (config == null) {
            config = new WechatPubPropertiesConfig();
        } else {
            checkWechatConfig(config, UrlEnum.ORDER_QUERY_URL);
        }
        //检查完成没有问题，开始发起HTTP请求
        return basePaymentService.query(orderQueryParams, config);
    }

    @Override
    public WechatCloseResData closeOrder(CloseOrderParams params) throws Exception {
        if (config == null) {
            config = new WechatPubPropertiesConfig();
        } else {
            checkWechatConfig(config, UrlEnum.CLOSE_ORDER_URL);
        }
        //检查完成没有问题，开始发起HTTP请求
        return basePaymentService.closeOrder(params, config);
    }

    @Override
    public WeChatRefundResData refund(RefundParams refund) throws Exception {
        if (config == null) {
            config = new WechatPubPropertiesConfig();
        } else {
            checkWechatConfig(config, UrlEnum.REFUND_URL);
        }
        //检查完成没有问题，开始发起HTTP请求
        return basePaymentService.refund(refund, config);
    }

    @Override
    public WechatRefundQueryResData refundQuery(RefundQueryParams params) throws Exception {
        if (config == null) {
            config = new WechatPubPropertiesConfig();
        } else {
            checkWechatConfig(config, UrlEnum.REFUND_QUERY_URL);
        }
        //检查完成没有问题，开始发起HTTP请求
        return basePaymentService.refundQuery(params, config);
    }

    @Override
    public List<WechatDownloadBillResData> downloadBill(DownloadBillParams params) throws Exception {
        if (config == null) {
            config = new WechatPubPropertiesConfig();
        } else {
            checkWechatConfig(config, UrlEnum.DOWNLOAD_BILL_URL);
        }
        //检查完成没有问题，开始发起HTTP请求
        return basePaymentService.downloadBill(params, config);
    }

    @Override
    public boolean verifySign(String xml, SignTypeEnum signType) {
        try {
            return WechatSignature.verifySign(WechatUtil.xmlToMap(xml), config.getKey(), signType);
        } catch (Exception e) {
            LogPortal.error("签名校验失败,xml=[{}],[{}]", xml, e);
            return false;
        }
    }

    @Override
    public void close() throws Exception {
        basePaymentService.close();
    }
}

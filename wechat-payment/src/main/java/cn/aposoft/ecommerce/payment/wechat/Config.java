/**
 * 
 */
package cn.aposoft.ecommerce.payment.wechat;

/**
 * 固定参数相关
 * <p>
 * {@link https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1}
 * 
 * @author Jann Liu
 *
 */
public interface Config {
	/**
	 * 单一主机最大并发连接数:默认为2,这里进行动态配置,避免高并发时,因此导致支付阻塞.
	 * 
	 * @return
	 * @author Yujinshui
	 * @time 2016年2月2日 下午12:03:20
	 */
	String connectionsPerRoute();

	/**
	 * 认证证书位置
	 * 
	 * @return
	 * @author Yujinshui
	 */
	String pkcs12();

	/**
	 * 签名所使用的key
	 * 
	 * @return key值
	 */
	String key();

	/**
	 * 公众号应用id
	 * 
	 * @return 应用id字符串
	 */
	String appId();

	/**
	 * 商户id
	 * 
	 * @return 商户id字符串
	 */
	String mchId();

	/**
	 * 用户标识 openid 否 String(128) oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
	 * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。企业号请使用【
	 * 企业号OAuth2.0接口】获取企业号内成员userid，再调用【企业号userid转openid接口】进行转换
	 */
	String openid();

	/**
	 * 支付url
	 * 
	 * @return 支付url字符串
	 */
	String url();

	/**
	 * 退款URL
	 * 
	 * @return 关闭订单地址
	 */
	String refundUrl();

	/**
	 * 关闭订单地址
	 * 
	 * @return 关闭订单地址
	 */
	String closeOrderUrl();

	/**
	 * 下载订单对账单地址
	 * 
	 * @return 下载对账单地址
	 */
	String downloadBillUrl();

	/**
	 * 退款查询地址
	 * 
	 * @return 下载对账单地址
	 */
	String refundQueryUrl();

	/**
	 * 支付成功通知的URL
	 * 
	 * @return URL字符串
	 */
	String notifyUrl();

	/**
	 * 订单查询URL
	 * 
	 * @return
	 * @author Yujinshui
	 * @time 2015年10月24日 下午9:01:34
	 */
	String orderQueryUrl();

}

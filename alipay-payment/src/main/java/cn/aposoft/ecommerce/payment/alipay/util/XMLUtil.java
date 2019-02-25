package cn.aposoft.ecommerce.payment.alipay.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtil {
	private static Logger logger = Logger.getLogger(XMLUtil.class);

	private XMLUtil() {

	}

	/**
	 * 将返回字符串解析为map类型[通用xml解析方式，微信xml尚未测试]
	 * <p>
	 * request标签部分不进行任何解析，仅解析除request标签以外的部分<br>
	 * 
	 * <pre>
		<?xml version="1.0" encoding="utf-8"?><br>
		<alipay><br>
		<is_success>T</is_success><br>
			<request><br>
				<param name="sign">22373a2f4d5c9284adf0f076fdbce6fc</param><br>
				<param name="_input_charset">utf-8</param><br>
				<param name="product_code">QR_CODE_OFFLINE</param><br>
				<param name="subject">测试商品</param><br>
				<param name="total_fee">0.1</param><br>
				<param name="sign_type">MD5</param><br>
				<param name="service">alipay.acquire.precreate</param><br>
				<param name="seller_id">208812817</param><br>
				<param name="partner">208812817</param><br>
				<param name="out_trade_no">F6D8D840890B11E59840FC1C7E19F601</param><br>
			</request><br>
		<response><br>
			<alipay><br>
				<detail_error_code>TRADE_HAS_SUCCESS</detail_error_code><br>
				<detail_error_des>交易已经支付</detail_error_des><br>
				<out_trade_no>F6D8D840890B11E59840FC1C7E19F601</out_trade_no><br>
				<result_code>FAIL</result_code><br>
			</alipay><br>
		</response><br>
		<sign>a62dcca516b9f7dd933b130fc9eb409a</sign><br>
		<sign_type>MD5</sign_type><br>
		</alipay>
	 * </pre>
	 * 
	 * @param xml
	 *            输入xml字符串
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @author Yujinshui
	 * @time 2015年11月13日 下午2:42:11
	 */
	public static Map<String, String> getMapFromXML(String xml)
			throws SAXException, IOException, ParserConfigurationException {
		// 构建xml解析工厂
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		String FEATURE = null;
		try {
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all XML entity attacks are prevented
			// Xerces 2 only - http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(FEATURE, true);

			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities 
			FEATURE = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(FEATURE, false);

			// Xerces 1 - http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 - http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities 
			FEATURE = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(FEATURE, false);

			// Disable external DTDs as well
			FEATURE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(FEATURE, false);

			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);

			// And, per Timothy Morgan: "If for some reason support for inline DOCTYPEs are a requirement, then 
			// ensure the entity settings are disabled (as shown above) and beware that SSRF attacks
			// (http://cwe.mitre.org/data/definitions/918.html) and denial 
			// of service attacks (such as billion laughs or decompression bombs via "jar:") are a risk."

			// remaining parser logic
		} catch (ParserConfigurationException e) {
			// This should catch a failed setFeature feature
			logger.info("ParserConfigurationException was thrown. The feature '" +
			FEATURE + "' is probably not supported by your XML processor.");
		}
		catch (SAXException e) {
			// On Apache, this should be thrown when disallowing DOCTYPE
			logger.warning("A DOCTYPE was passed into the XML document");
		}
		catch (IOException e) {
			// XXE that points to a file that doesn't exist
			logger.error("IOException occurred, XXE may still possible: " + e.getMessage());
		}
		// 构建具体的xml解析器
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		InputStream is = getStringStream(xml);
		// 获得当前xml的文档
		Document document = documentBuilder.parse(is);
		// 获得当前文档的根元素
		Element root = document.getDocumentElement();
		Map<String, String> map = new HashMap<String, String>();
		recurrenceXml(map, root);
		return map;
	}

	/**
	 * 递归解析xml的方法
	 * 
	 * @param map
	 * @param element
	 * @author Yujinshui
	 * @time 2015年11月13日 下午2:42:50
	 */
	private static void recurrenceXml(Map<String, String> map, Element element) {
		// System.out.print("<" + element.getNodeName());
		NodeList childNodes = element.getChildNodes();
		// 得到当前节点上的所有属性
		/*************** 获取节点属性部分暂不启用 *******************/
		// NamedNodeMap nodeMap = element.getAttributes();
		// if (null != nodeMap) {
		// for (int j = 0; j < nodeMap.getLength(); j++) {
		// Attr attr = (Attr) nodeMap.item(j);
		// // 属性的名称
		// String attrName = attr.getNodeName();
		// // 属性的值
		// String attrValue = attr.getNodeValue();
		// // System.out.print(" " + attrName + "=\"" + attrValue + "\"");
		// }
		// }
		// System.out.print(">");
		/*************** 获取节点属性部分暂不启用 *******************/
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node childNode = childNodes.item(i);
			// 得到当前节点下的节点
			short nodeType = childNode.getNodeType();
			// 当前类型是元素类型
			if (nodeType == Node.ELEMENT_NODE) {
				// 继续递归
				recurrenceXml(map, (Element) childNode);
			}
			// 当前元素是文本
			else if (nodeType == Node.TEXT_NODE) {
				// 递归出口
				// System.out.print(childNode.getNodeValue());
				map.put(element.getNodeName(), childNode.getNodeValue());
			}
		}
		// System.out.println("</" + element.getNodeName() + ">");

	}

	/**
	 * 字符串转输入流
	 * 
	 * @param sInputString
	 * @return
	 */
	public static InputStream getStringStream(String sInputString) {
		ByteArrayInputStream tInputStringStream = null;
		if (sInputString != null && !sInputString.trim().equals("")) {
			byte[] bytes;
			try {
				bytes = sInputString.getBytes("UTF-8");
				tInputStringStream = new ByteArrayInputStream(bytes);
			} catch (UnsupportedEncodingException e) {
				logger.error("将字符串转换为输入流失败：" + e.getMessage(), e);
				e.printStackTrace();
			}

		}
		return tInputStringStream;
	}
}

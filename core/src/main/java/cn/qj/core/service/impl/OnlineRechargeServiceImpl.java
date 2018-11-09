package cn.qj.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thoughtworks.xstream.XStream;

import cn.qj.core.consts.SysConst;
import cn.qj.core.entity.OnlineRecharge;
import cn.qj.core.repository.OnlineRechargeRepository;
import cn.qj.core.service.OnlineRechargeService;
import cn.qj.core.util.HttpServletContext;
import cn.qj.core.util.StringUtil;
import lombok.Setter;

/**
 * 线上充值服务实现
 * 
 * @author Qiujian
 * @date 2018/11/05
 */
@Service
@ConfigurationProperties("pay.wechat")
public class OnlineRechargeServiceImpl implements OnlineRechargeService {

	@Autowired
	private OnlineRechargeRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	@Setter
	private String appId;
	@Setter
	private String mchId;
	@Setter
	private String mchKey;
	@Setter
	private String notifyUrl;
	@Setter
	private String wechatPayUrl;

	@Override
	public OnlineRecharge save(OnlineRecharge onlineRecharge) {
		onlineRecharge.setCreateTime(new Date());
		onlineRecharge.setTransStatus(OnlineRecharge.TRANS_IN);
		return repository.save(onlineRecharge);
	}

	@Override
	public OnlineRecharge get(Long id) {
		return repository.findOne(id);
	}

	@Override
	public void pay(OnlineRecharge recharge) {
		// 更新充值状态为交易中
		recharge.setTransStatus(OnlineRecharge.TRANS_IN);
		repository.saveAndFlush(recharge);

		// 根据渠道进行交易
		if (OnlineRecharge.WECHAT_PAY_CHANNEL.equals(recharge.getChannel())) {
			wechatPay(recharge.getId(), recharge.getAmount());
		} else if (OnlineRecharge.ALIPAY_CHANNEL.equals(recharge.getChannel())) {
			aliPay(recharge.getId(), recharge.getAmount());
		}

	}

	private void wechatPay(Long id, BigDecimal amount) {
		// 单位分
		amount = amount.multiply(new BigDecimal("100"));
		amount = amount.stripTrailingZeros();

		// 获取配置参数
		String payDesc = "平台充值";
		// 组装请求报文
		SortedMap<String, String> param = new TreeMap<>();
		param.put("appid", appId);
		param.put("mch_id", mchId);
		param.put("device_info", HttpServletContext.getCurrentLoginInfo().getId().toString());
		param.put("nonce_str", SysConst.MD5.encodePassword(Math.random() + "", null));
		param.put("body", payDesc);
		param.put("out_trade_no", id.toString());
		param.put("total_fee", amount.toString());
		param.put("spbill_create_ip", "192.168.3.31");
		param.put("notify_url", notifyUrl);
		param.put("trade_type", "MWEB");
		param.put("sign", StringUtil.signStr(param, mchKey));

		try {
			// 把请求报文转换为Xml字符串
			String xmlStr = StringUtil.map2XmlStr(param);
			// 发送报文到微信支付
			ResponseEntity<String> responseEntity = restTemplate.exchange(wechatPayUrl, HttpMethod.POST,
					new HttpEntity<String>(xmlStr), String.class);
			String bodyXmlStr = responseEntity.getBody();
			System.out.println(bodyXmlStr);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void aliPay(Long id, BigDecimal amount) {
		// TODO Auto-generated method stub

	}

}

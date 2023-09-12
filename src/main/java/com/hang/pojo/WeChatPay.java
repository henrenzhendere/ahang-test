package com.hang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ahang
 * @create 2023/6/26 15:26
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
 public class WeChatPay {

    /*
    * 微信分配的公众账号ID（企业号corpid即为此appid）
    * */
    private String appId;

    /*
    * 微信支付分配的商户号
    * */
    private String mchId;

    /*
    * 随机字符串，不长于32位。推荐随机数生成算法
    * */
    private String nonceStr;

//    签名，详见签名生成算法
    private String sign;

    /*
    * 商品简单描述，该字段须严格按照规范传递，
    *  具体请见参数规定
    */
    private String body;

    /*
    商户系统内部订单号，要求32个字符内（最少6个字符），
    只能是数字、大小写字母
    且在同一个商户号下唯一。详见商户订单号
     */
    private String outTradeNo;

    //订单总金额，单位为分 ，最小单位1
    private int totalFee;


    /*
    * 必须传正确的用户端IP,支持ipv4、ipv6格式，
    获取方式详见获取用户ip指引
    *
    * */
    private String spbillCreateIp;

    /*
    * 接收微信支付异步通知回调地址，
    通知url必须为直接可访问的url，不能携带参数。
    * */
    private String notifyUrl;

//    H5支付的交易类型为MWEB
    private String tradeType;

    private String sceneInfo;
}

package com.hang.service.impl;

import com.alibaba.fastjson.JSON;
import com.hang.Res.ReturnRes;
import com.hang.mapper.ChargeMapper;
import com.hang.pojo.AliPay;
import com.hang.pojo.Charge;
import com.hang.pojo.Order;
import com.hang.pojo.User;
import com.hang.service.ChargeService;
import com.hang.utils.HttpClientUtil;
import com.hang.utils.OrderNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Ahang
 * @create 2023/7/27 14:01
 */

@Service
public class ChargeServiceImpl implements ChargeService {
    @Autowired
    private ChargeMapper chargeMapper;
    @Override
    public User queryPresentCharge(int userId) {
        User charge = chargeMapper.queryPresentCharge(userId);
        return charge;
    }

    @Override
    public List<Charge> chargeInfo(int userId) {
        List<Charge> chargeInfoList = chargeMapper.chargeInfo(userId);
        return chargeInfoList;
    }

    @Override
    public int insertChargeInfo(Charge charge) {
        int i = chargeMapper.insertChargeInfo(charge);
        return i;
    }

    @Override
    public String payByAli(Charge charge) {
        ReturnRes res;
        String out_trade_no = String.valueOf(charge.getChargeNo());
        BigDecimal total_amount = BigDecimal.valueOf(charge.getCharge());
        String subject = "qq";
        String return_url = "http://localhost:8080/myWallet";// 支付成功前端页面
        String notify_url = "http://localhost:8082/charge/updateChargeFinish";// 支付成功后后端要跳转的
        String quit_url = "http://localhost:8082/showAllGoods";
        String url = "http://localhost:8083/alipay/pay";
        AliPay aliPay = new AliPay();

        aliPay.setOut_trade_no(out_trade_no);
        aliPay.setTotal_amount(total_amount);
        aliPay.setSubject(subject);
        aliPay.setReturn_url(return_url);
        aliPay.setNotify_url(notify_url);
        aliPay.setQuit_url(quit_url);

        String jsonStr = JSON.toJSONString(aliPay);
        String data = HttpClientUtil.doPostJson1(url, jsonStr);
        return data;
    }

    @Override
    public int updateFinishTime(Charge charge) {
        int i = chargeMapper.updateFinishTime(charge);
        if (i==1){
            return i;
        }
        chargeMapper.delChargeInfo(charge.getChargeNo());
        return 0;
    }

    @Override
    public int delChargeInfo(String chargeNo) {
        int i = chargeMapper.delChargeInfo(chargeNo);
        return i;
    }

    @Override
    public int queryUserId(String chargeNo) {
        int i = chargeMapper.queryUserId(chargeNo);
        return i;
    }

    @Override
    public Charge preChargeInfo(String chargeNo) {

        Charge charge = chargeMapper.preChargeInfo(chargeNo);
        return charge;
    }

    @Override
    @Transactional
    public int updateStatus(String chargeNo,BigDecimal charge,String payTime) {
        int i = chargeMapper.updateStatus(chargeNo);
        if (i==1){
            int userId = chargeMapper.queryUserId(chargeNo);
            User user = chargeMapper.queryPresentCharge(userId);
            Integer userMoney = user.getMoney();
            userMoney = userMoney+charge.intValue();
            Charge charge1 = new Charge();
            charge1.setChargeNo(chargeNo);
            charge1.setTotal(userMoney);
            charge1.setFinishTime(payTime);
            int k1 = chargeMapper.updateFinishTime(charge1);
            int k2 = chargeMapper.updateVersionInt(userId, userMoney);
            return k1+k2;
        }
        return 0;
    }

}

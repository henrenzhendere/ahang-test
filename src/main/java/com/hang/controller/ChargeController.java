package com.hang.controller;

import com.hang.Req.ChargeReq;
import com.hang.Res.AliPayRes;
import com.hang.Res.R;
import com.hang.Res.ReturnRes;
import com.hang.pojo.Charge;
import com.hang.pojo.User;
import com.hang.service.ChargeService;
import com.hang.utils.JedisUtils;
import com.hang.utils.OrderNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Ahang
 * @create 2023/7/27 11:33
 */

@RestController
@RequestMapping("/charge")
public class ChargeController {
    @Autowired
    private ChargeService chargeService;
    @PostMapping("/presentCharge")
    public R queryCharge(HttpServletRequest req){
        String token = req.getHeader("token");
        JedisUtils jedis = new JedisUtils();
        String userName ="";
        Map<String, String> tokenMap = jedis.hgetAll(token);
        Iterator<String> iterator = tokenMap.keySet().iterator();
        while (iterator.hasNext()) {
            userName = iterator.next();
        }
        int userId = Integer.valueOf(jedis.hget(token, userName));
        User user = chargeService.queryPresentCharge(userId);
        Integer money = user.getMoney();

        List<Charge> chargeInfoList = chargeService.chargeInfo(userId);
        R<Object> res = new R<>();
        Map<Object, Object> map = new HashMap<>();
        map.put("money",money);
        map.put("chargeInfoList",chargeInfoList);
        res.setData(map);
        return res;
    }

    /*
    * 调接口
    * */
    @PostMapping("prepareInsert")
    public ReturnRes prepareInsert(@RequestBody ChargeReq req, HttpServletRequest request){
        String flag = req.getFlag();
        Integer charge = req.getCharge();
        String token = request.getHeader("token");
        String url = "http://localhost:8082/charge/insertOrderByAli?charge="+charge+"&"+"flag="+flag+"&"+"token="+token;
        ReturnRes res = new ReturnRes();
        res.setCode("1");
        res.setData(url);
        return res;
    }

    /*
     * 生成订单
     * */
    @GetMapping("/insertOrderByAli")
    public String insertChargeByAli(@RequestParam int charge, @RequestParam String flag,@RequestParam String token) throws IOException {
        JedisUtils jedis = new JedisUtils();
        String userName ="";
        Map<String, String> tokenMap = jedis.hgetAll(token);
        Iterator<String> iterator = tokenMap.keySet().iterator();
        while (iterator.hasNext()) {
            userName = iterator.next();
        }
        int userId = Integer.valueOf(jedis.hget(token, userName));
//       日期
        Date date = new Date();
        String createTime = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss").format(date);
        Charge chargeInfo = new Charge();
        int total = 0;
        chargeInfo.setCharge(charge);
        chargeInfo.setCreateTime(createTime);
        chargeInfo.setUserId(userId);
        chargeInfo.setTotal(total);
        chargeInfo.setChargeNo(OrderNumberGenerator.generateOrderNumber());
        chargeService.insertChargeInfo(chargeInfo);
        if (flag.equals("ali")) {
            //调用支付宝接口
            String data = chargeService.payByAli(chargeInfo);
            System.out.println("调用支付宝接口完成");
            return data;
        }
        return "11";
    }


    @PostMapping("/updateChargeFinish")
    public R updateChargeFinish(@RequestBody AliPayRes Alires) throws IOException {
        R res = new R();
        BigDecimal charge = Alires.getReceiptAmount();
        String chargeNo = Alires.getOut_trade_no();
        String payTime = Alires.getPayTime();
        int i = chargeService.updateStatus(chargeNo, charge, payTime);
        if (i!=2){
            chargeService.updateStatus(chargeNo,charge,payTime);
        }
        res.setMessage("success");
        return res;
    }
}

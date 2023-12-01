package com.issue;

import com.alibaba.fastjson2.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

public class JsonPathTest {
    
    
    @Test
    public void multiClassSum() {
        JSONObject json = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(new JSONObject().fluentPut("price", 10L));
        jsonArr.add(new JSONObject().fluentPut("price", 10.1f));
        jsonArr.add(new JSONObject().fluentPut("price", 10.1d));
        jsonArr.add(new JSONObject().fluentPut("price", BigInteger.valueOf(10L)));
        jsonArr.add(new JSONObject().fluentPut("price", BigDecimal.valueOf(10.1d)));
        
        json.put("arr", jsonArr);
        // 这一步eval会提示 not support operation 。
        // 原因是 com.alibaba.fastjson2.JSONPathSegment$SumSegment#add方法类型判断不够全面,无法支持Floag，Double，BigInteger,BigDecimal
        Object o = JSONPath.eval(json, "$.arr[*].price.sum()");
        
        Assert.assertEquals(o.toString(),"60.3");
    }
    
    @Test
    public void integerSum() {
        JSONObject json = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        jsonArr.add(new JSONObject().fluentPut("price", 10L));
        jsonArr.add(new JSONObject().fluentPut("price", 10L));
        
        json.put("arr", jsonArr);
        //由于此时 price都是long类型sum正常
        Object o = JSONPath.eval(json, "$.arr[*].price.sum()");
        
        Assert.assertEquals(o.toString(),"20");
    }
}

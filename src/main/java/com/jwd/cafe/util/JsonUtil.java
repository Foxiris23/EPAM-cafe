package com.jwd.cafe.util;

import com.jwd.cafe.constant.RequestConstant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    public static Map<Integer, Integer> jsonToCart(String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray(RequestConstant.CART);
        Map<Integer, Integer> cart = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            cart.put(Integer.parseInt(jsonArray.getJSONObject(i).getString(RequestConstant.ID)),
                    Integer.parseInt(jsonArray.getJSONObject(i).getString(RequestConstant.AMOUNT)));
        }
        return cart;
    }
}

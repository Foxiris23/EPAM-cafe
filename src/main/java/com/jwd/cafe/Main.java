package com.jwd.cafe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jwd.cafe.config.FlywayConfig;
import com.jwd.cafe.exception.DaoException;
import com.jwd.cafe.exception.EmailException;
import org.flywaydb.core.Flyway;

public class Main {
    public static void main(String[] args) throws DaoException, ClassNotFoundException, JsonProcessingException, EmailException {
        Flyway flyway = new Flyway(FlywayConfig.getInstance());
        flyway.migrate();
//        System.out.println(SCryptUtil.scrypt("Mm12345", 16, 16, 16));
//        String json = "{\"cart\": [{\"id\": \"1\", \"amount\": \"2\"}, {\"id\": \"2\", \"amount\": \"3\"}]}";
//        JSONObject jsonObject = new JSONObject(json);
//        JSONArray jsonArray = jsonObject.getJSONArray("cart");
//        for (int i = 0; i < jsonArray.length(); i++) {
//            System.out.println(jsonArray.getJSONObject(i).get("id") + ": " + jsonArray.getJSONObject(i).get("amount"));
//        }
//        Pattern pattern = Pattern.compile("[А-Яа-яa-zA-Z,\\-;'\"\\s]+[.?!]");
//        Matcher matcher = pattern.matcher("Качество программных средств во многом зависит от их кодов. Например, чем сложнее программа, тем ниже ее надежность и сопровождаемость. Поэтому при оценке качества программ обычно оценивается и их сложность.");
//        while(matcher.find()){
//            System.out.println(matcher.group());
//            System.out.println();
//        }
    }
}
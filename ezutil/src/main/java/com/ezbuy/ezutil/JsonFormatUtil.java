package com.ezbuy.ezutil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class JsonFormatUtil {

    /**
     * 将JSON字符串格式化成JSON结构，方便查看
     *
     * @param json json字符串
     * @return 普通字符串（便于查看）
     */
    public static String format(String json) {
        if (json == null || json.isEmpty()) {
            return "";
        }
        String message = "";
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(4);
            } else {
                message = json;
            }
        } catch (Exception e) {
            message = json;
        }
        return message;
    }

    /**
     * 将JSON字符串格式化成JSON结构，方便查看
     * 过滤掉没有用的字段
     *
     * @param json    json字符串
     * @param filters 过滤掉的字段，不需要打印
     * @return 普通字符串（便于查看）
     */
    public static String formatWithFilter(String json, List<String> filters) {
        if (json == null || json.isEmpty()) {
            return "";
        }
        String message = "";
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = filter(new JSONObject(json), filters);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                message = jsonArray.toString(4);
            } else {
                message = json;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = json;
        }
        return message;
    }

    private static JSONObject filter(JSONObject jsonObject, List<String> filters) throws JSONException {
        JSONObject dest = new JSONObject();
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (filters.contains(key)) {
                continue;
            }
            Object o = jsonObject.get(key);
            Object parseObj = parseObj(o, filters);
            dest.put(key, parseObj);
        }
        return dest;
    }

    private static Object parseObj(Object o, List<String> filters) throws JSONException {
        if (o instanceof JSONObject) {
            JSONObject value = (JSONObject) o;
            JSONObject filterValue = filter(value, filters);
            return filterValue;
        } else if (o instanceof JSONArray) {
            JSONArray array = (JSONArray) o;
            JSONArray jsonElements = new JSONArray();
            for (int i = 0; i < array.length(); i++) {
                Object item = array.get(i);
                Object parseItem = parseObj(item, filters);
                jsonElements.put(parseItem);
            }
            return jsonElements;
        } else {
            return o;
        }
    }
}

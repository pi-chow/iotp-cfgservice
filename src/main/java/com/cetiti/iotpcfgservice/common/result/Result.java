package com.cetiti.iotpcfgservice.common.result;

import com.cetiti.ddapv2.iotplatform.common.BaseResultCode;
import xiaojian.toolkit.base.ResultCode;

import java.util.HashMap;
import java.util.Map;

/**
 * result entity.
 *
 * @author weiyinglei
 */
public class Result extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public Result() {
        put("code", 0);
    }

    public static Result error() {
        return error(BaseResultCode.SERVER_ERROR);
    }

    public static Result error(String msg) {
        return error(BaseResultCode.SERVER_ERROR.getResultCode(), BaseResultCode.SERVER_ERROR.getDescription());
    }

    public static Result error(int code, String msg) {
        Result result = new Result();
        result.put("code", code);
        result.put("msg", msg);
        return result;
    }

    public static Result error(ResultCode resultCode) {
        Result result = new Result();
        result.put("code", resultCode.getResultCode());
        result.put("msg", resultCode.getDescription());
        return result;
    }

    public static Result ok(String msg) {
        Result result = new Result();
        result.put("msg", msg);
        return result;
    }

    public static Result ok(Map<String, Object> map) {
        Result result = new Result();
        result.putAll(map);
        return result;
    }

    public static Result ok() {
        return new Result();
    }

    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
package com.example.demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result implements Serializable {

    private static int SUCCESS = 200;
    private static int FAIL = 400;

    private int code;
    private String msg;

    public static Result ok(){
        return new Result(SUCCESS, "处理成功");
    }

    public static Result error(){
        return new Result(FAIL, "处理失败");
    }

}

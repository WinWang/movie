package com.winwang.movie.pojo.base;


import com.winwang.movie.common.Constant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResObject<T> {
    private int code;
    private String message;
    private T result;

    public static ResObject<Object> successResult() {
        return result(Constant.CODE_SUCCESS, Constant.MSG_SUCCESS);
    }

    public static ResObject<Object> result(int resCode, String resMessage) {
        ResObject<Object> result = new ResObject<>();
        result.setCode(resCode);
        result.setMessage(resMessage);
        return result;
    }


    public static void setSucecss(ResObject res) {
        res.setCode(Constant.CODE_SUCCESS);
        res.setMessage(Constant.MSG_SUCCESS);
    }

    public static void setFail(ResObject res) {
        res.setCode(Constant.CODE_FAILURE);
        res.setMessage(Constant.MSG_FAILURE);
    }


}

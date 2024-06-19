package io.github.xucancould.vo;

import io.github.talelin.autoconfigure.bean.Code;

import io.github.xucancould.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

/**
 * created by Xu on 2024/3/26 17:28.
 */
public class CreatedVo extends UnifyResponseVO<String> {


    public CreatedVo() {
        super(Code.CREATED.getCode());
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public CreatedVo(int code) {
        super(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public CreatedVo(String message) {
        super(message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }

    public CreatedVo(int code, String message) {
        super(code, message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.CREATED.value());
    }


    @Override
    public String toString() {
        return super.toString();
    }
}

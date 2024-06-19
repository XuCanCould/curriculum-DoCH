package io.github.xucancould.vo;

import io.github.talelin.autoconfigure.bean.Code;
import io.github.xucancould.common.util.ResponseUtil;
import org.springframework.http.HttpStatus;

/**
 * created by Xu on 2024/3/28 18:29.
 */
public class DeletedVO extends UnifyResponseVO<String> {
    public DeletedVO() {
        super(Code.DELETED.getCode());
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.OK.value());
    }
    public DeletedVO(int code) {
        super(code);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.OK.value());
    }
    public DeletedVO(String message) {
        super(message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.OK.value());
    }

    public DeletedVO(int code, String message) {
        super(code, message);
        ResponseUtil.setCurrentResponseHttpStatus(HttpStatus.OK.value());
    }
    @Override
    public String toString() {
            return super.toString();
        }

}
package com.bay.mall.product.exception;

import com.bay.common.exception.ExCodeEnum;
import com.bay.common.exception.RRException;
import com.bay.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MallExceptionControllerAdvice
 * @Description 集中处理所有异常
 * @Author baymux
 * @Date 2020/5/30 14:53
 * @Vsrsion 1.0
 **/
@ControllerAdvice(basePackages = "com.bay.mall.product.controller")
@Slf4j
@RestController
public class MallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题:{}, 异常类型:{}", e.getMessage(), e.getClass());

        Map<String, String> errorMap = new HashMap<>(16);
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return R.error(ExCodeEnum.VALID_EXCEPTION.getCode(), ExCodeEnum.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }


    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {

        return R.error(ExCodeEnum.UNKNOW_EXCEPTION.getCode(), ExCodeEnum.UNKNOW_EXCEPTION.getMsg());
    }

}

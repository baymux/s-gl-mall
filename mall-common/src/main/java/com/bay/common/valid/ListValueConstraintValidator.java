package com.bay.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName ListValueConstraintValidator
 * @Description TODO
 * @Author baymux
 * @Date 2020/5/30 16:23
 * @Vsrsion 1.0
 **/
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    /**
     * 初始化
     * @param constraintAnnotation
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] values = constraintAnnotation.values();

        for (int value : values) {
            set.add(value);
        }
    }

    /**
     * 判断是否校验成功
     * @param value
     * @param context
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }

}

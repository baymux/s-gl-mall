package com.bay.common.constant;

/**
 * @ClassName ProductConstant
 * @Description TODO
 * @Author baymux
 * @Date 2020/5/31 13:05
 * @Vsrsion 1.0
 **/
public class ProductConstant {

    public enum AttrEnum{
        ATTR_TYPE_BASE(1, "基本属性","base"),
        ATTR_TYPE_SALE(0, "销售属性","sale");

        AttrEnum(int code, String msg, String path) {
            this.code = code;
            this.msg = msg;
            this.path = path;
        }

        private int code;
        private String msg;
        private String path;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }


    public enum StatusrEnum{
        MEW_SPU(0, "新建",""),
        UP_SPU(1, "商品上架",""),
        DOWN_SPU(2, "商品下架","");

        StatusrEnum(int code, String msg, String path) {
            this.code = code;
            this.msg = msg;
            this.path = path;
        }

        private int code;
        private String msg;
        private String path;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}

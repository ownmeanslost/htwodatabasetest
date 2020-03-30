/**
 * 自定义校验
 */
function initValidate() {
    /**
     * input的value和隐藏数据不能为空
     */
    $.validator.addMethod("valAndHideDataNotEmpty", function (value, element, params) {
        try {
            var ids = $(element).attr("checkidarr");
            var flag = true;
            if (ids) {
                var idArr = ids.split(",");
                $.each(idArr, function (index, value) {
                    var val = $("#" + value).val();
                    if (!val) {
                        flag = false;
                    }
                });
            }
            return !!value && flag;
        } catch (e) {
            return false;
        }
    }, '不能为空');

    /**
     * value和隐藏数据要么都为空，要么都不为空
     */
    $.validator.addMethod("valAndHideDataAllEmptyOrAllNotEmpty", function (value, element, params) {
        try {
            var ids = $(element).attr("checkidarr");
            if (!ids) {
                //配置了valAndHideDataAllEmptyOrAllNotEmpty但是未设置checkidarr直接报错
                return false;
            }
            //以value为基准进行判断
            var flag = "0";
            if (value) {
                flag = "1";
            }
            var checkFlag = true;
            var idArr = ids.split(",");
            $.each(idArr, function (index, value) {
                var val = $("#" + value).val();
                var valFlag = "0";
                if (val) {
                    valFlag = "1";
                }
                if (flag != valFlag) {
                    checkFlag = false;
                }
            });
            return checkFlag;
        } catch (e) {
            return false;
        }
    }, '不能为空');

    /**
     * 整数位最大长度位13位，小数位最大长度8位的非负浮点数
     */
    $.validator.addMethod("isDecimal", function (value, element) {
        var reg = /(^0(\.\d{1,8})?$|^[1-9]\d{0,12}(\.\d{1,8})?$)/;
        value = value.trim();
        if (!value || value.match(reg)) {
            return true;
        } else {
            return false;
        }
    }, "请输入整数位最大长度位13位，小数位最大长度8位的非负浮点数");

    $.validator.addMethod("decimal", function (value, element, params) {
        var reg = new RegExp("(^0(\\.\\d{1,8})?$|^[1-9]\\d{0,12}(\\.\\d{1," + params + "})?$)");
        value = value.trim();
        if (!value || value.match(reg)) {
            return true;
        } else {
            return false;
        }
    }, "请输入整数位最大长度位13位，小数位最大长度3位的非负浮点数");

    $.validator.addMethod("chkPrice", function (value, element, params) {
        var adjustPrice = isNaN(parseFloat($('#' + params).val())) ? 0 : parseFloat($('#' + params).val());
        return adjustPrice >= 0 ? true : false;
    });

    $.validator.addMethod("ckQtyNullAndEnough", function (value, element, params) {
        if (!value) {
            return false;
        }
        var comparedQty = $('#' + params).val();
        if (parseFloat(value) > parseFloat(comparedQty)) {
            return false;
        }
        return true;
    });

    $.validator.addMethod("greaterthanZero", function (value, element, params) {
        if (!value) {
            return false;
        }
        if (parseFloat(value) <= parseFloat(0)) {
            return false;
        }
        return true;
    }, "输入值需大于0");

    /**
     * 校验金额 ： 仅校验整数位最大长度不能超过13位
     */
    $.validator.addMethod("isAmtDecimal", function (value, element, params) {
        var reg = /(^0(\.\d*)?$|^[1-9]\d{0,12}(\.\d*)?$)/;
        value = value.trim();
        if (!value || value.match(reg)) {
            return true;
        } else {
            return false;
        }
    }, "输入数值过大");

    /**
     * 不允许输入半角
     */
    $.validator.addMethod("notSupportHalfAngleOfSymbols", function (value, element, params) {
        //半角，参考https://www.cnblogs.com/moqiutao/p/6869794.html?utm_source=itdadao&utm_medium=referral
        if (value.match(/[()]/g)) {
            return false;
        }
        return true;
    }, '');

    /**
     * 全角括号要成对出现
     */
    $.validator.addMethod("braceMatching", function (value, element, params) {
        var flag = 0;
        for (i = 0; i < value.length; i++) {
            var cc = value.charAt(i);
            if (cc == '（') {
                flag++;
            } else if (cc == '）') {
                flag--;
            }
            //flag=1说明出现了一次正括号，falg=2说明出现了两次正括号
            if (flag > 1 || flag < 0) {
                return false;
            }
        }
        //最后flag必须为0，以反括号首尾
        if (flag != 0) {
            return false;
        }
        return true;
    }, '');

    /**
     *  销售订单物品：按辅助单位预留时，订购数量(辅助)>0,按基本单位预留时>=0 ；整数位最大长度位13位，小数位最大长度8位的非负浮点数
     */
    $.validator.addMethod("isDecimalOrZero", function (value, element, params) {
        if(!value){
            return false;
        }
        //按辅助单位预留时不允许为0
        var reg = /(^0(\.\d{1,8})?$|^[1-9]\d{0,12}(\.\d{1,8})?$)/;
        value = value.trim();
        if (!value || value.match(reg)) {
            return true;
        } else {
            return false;
        }

    }, "请输入数字");
    /**
     * 输入正整数
     */
    $.validator.addMethod("valueIsIntegerAndGt0", function (value, element, params) {
        if (!(/(^[1-9]\d*$)/.test(value))) {
            return false;
        }
        return true;
    }, "输入值需为正整数");
}
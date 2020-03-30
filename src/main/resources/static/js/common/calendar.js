define(["i18n"], function () {
    /* 星期 */
    var weeks = [
        "日",
        "一",
        "二",
        "三",
        "四",
        "五",
        "六"
    ];
    /* 月 */
    var months = [
        "一月",
        "二月",
        "三月",
        "四月",
        "五月",
        "六月",
        "七月",
        "八月",
        "九月",
        "十月",
        "十一月",
        "十二月"
    ];
    /* 定义日历中的快捷按钮 */
    var shortcut_buttons = {};
    shortcut_buttons['今天'] = [moment(), moment()];
    shortcut_buttons['昨天'] = [moment().subtract(1, 'days'), moment().subtract(1, 'days')];
    shortcut_buttons['最近七日'] = [moment().subtract(6, 'days'), moment()];
    shortcut_buttons['最近30日'] = [moment().subtract(29, 'days'), moment()];
    shortcut_buttons['最近3个月'] = [moment().subtract(3, 'month'), moment()];
    shortcut_buttons['最近一年'] = [moment().subtract(365, 'days'), moment()];
    return {
        /* 获取开始时间 */
        getStartDate: function (id) {
            var date = $("#" + id).val();
            if (date) {
                var dateArray = date.split("~");
                if (dateArray.length === 2) {
                    return dateArray[0].trim();
                }
            }
            return null;
        },
        /* 获取截止时间 */
        getEndDate: function (id) {
            var date = $("#" + id).val();
            if (date) {
                var dateArray = date.split("~");
                if (dateArray.length === 2) {
                    return dateArray[1].trim();
                }
            }
            return null;
        },

        /* 初始化开始时间和截止时间的控件 */
        initDoubleDate: function (id) {
            $("#" + id).daterangepicker({
                startDate: false,
                endDate: false,
                timePicker: false, /* 是否显示小时和分钟 */
                timePickerIncrement: 1, /* 时间的增量，单位为分钟 */
                timePicker12Hour: false, /* 是否使用24小时制来显示时间 */
                "autoApply": false, /* 选中后自动关闭控件窗口 */
                format: "YYYY-MM-DD", /* 日期格式 */
                singleDatePicker: false,
                separator: "~",
                "locale": {
                    daysOfWeek: weeks,
                    monthNames: months,
                    applyLabel: '确定',
                    cancelLabel: '取消'
                },
                "applyClass": 'blue',
                "dateLimit": {
                    "days": 2000 /* 时间的最大选择范围 */
                },
                ranges: shortcut_buttons
            })
        },

        /* 初始化单个时间的日历控件 */
        initSingleDate: function (id) {
            var startDate = moment("00:00", "hh:mm");

            $("#" + id).daterangepicker({
                startDate:false,
                endDate: false,
                timePicker: true, /* 是否显示小时和分钟 */
                timePickerIncrement: 1, /* 时间的增量，单位为分钟 */
                timePicker12Hour: false, /* 是否使用24小时制来显示时间 */
                "autoApply": false, /* 选中后自动关闭控件窗口 */
                format: "YYYY-MM-DD HH:mm", /* 日期格式 */
                singleDatePicker: true,
                separator: "~",
                "locale": {
                    daysOfWeek: weeks,
                    monthNames: months,
                    applyLabel: '确定',
                    cancelLabel: '取消'
                },
                "applyClass": 'blue',
                "dateLimit": {
                    "days": 180 /* 时间的最大选择范围 */
                },
                // ranges: shortcut_buttons
            }).on('cancel.daterangepicker', function (ev, picker) {
                //清空按钮回调
                //当前日历控件重置
                picker.setStartDate(startDate);
                picker.setEndDate(startDate);
            });
        },
        /* 初始化单个时间的日历控件 默认当天23:59s*/
        initSingleDateEnd: function (id) {
            var startDate = moment("23:59", "hh:mm");

            $("#" + id).daterangepicker({
                startDate: startDate,
                endDate: false,
                timePicker: true, /* 是否显示小时和分钟 */
                timePickerIncrement: 1, /* 时间的增量，单位为分钟 */
                timePicker12Hour: false, /* 是否使用24小时制来显示时间 */
                "autoApply": false, /* 选中后自动关闭控件窗口 */
                format: "YYYY-MM-DD HH:mm", /* 日期格式 */
                singleDatePicker: true,
                separator: "~",
                "locale": {
                    daysOfWeek: weeks,
                    monthNames: months,
                    applyLabel: '确认',
                    cancelLabel: '清空'
                },
                "applyClass": 'blue',
                "dateLimit": {
                    "days": 180 /* 时间的最大选择范围 */
                },
            }).on('cancel.daterangepicker', function (ev, picker) {
                //清空按钮回调
                //当前日历控件重置
                picker.setStartDate(startDate);
                picker.setEndDate(startDate);
            });
        },
        /* 获取单个时间 */
        getSingleDate: function (id) {
            return $("#" + id).val().trim();
        }
    };
});
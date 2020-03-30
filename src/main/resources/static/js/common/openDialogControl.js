var openDialog = {
    chooseDepartmentTreeUrl: base + "/purchasingUnit/initChoosePurchasingUnitTree",
    /* 部门 */
    initChoosePurchasingUnitTree: function (id, callBackMethod) {
        var queryCondition = '?jquerySelectorId=' + id;
        $('body').on('click', '#' + id + ':not(.disabled)', function () {
            layer.open({
                type: 2,
                title: "选择采购单位",
                shade: 0.001, //遮罩透明度
                maxmin: false, //允许全屏最小化
                anim: 5, //0-6的动画形式，-1不开启
                area: ['500px', '500px'],
                skin: 'skin-searchMore',
                content: openDialog.chooseDepartmentTreeUrl + queryCondition
            });
        }).on('initData', '#' + id + ':not(.disabled)', function (e, data) {
            if (callBackMethod) {
                callBackMethod(data);
            }
        }).on('initCheck', '#' + id + ':not(.disabled)', function (e, data) {
            //data为tree对象
            /*默认勾选*/

        });
    }

};


/**
 * Created by jun.liu on 2018-01-05.
 */
var commonUtil = {

    html2Escape: function (sHtml) {
        return sHtml.replace(/[<>&"]/g, function (c) {
            return {'<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;'}[c];
        });
    },
    /**
     *
     * @param url 新tab的url
     * @param title 新tab的标签名
     * @param menuId 打开tab的唯一标识，一个标识同一时刻仅能打开一个
     */
    openNewTab: function (url, title, menuId, closeId, replaceId) {
        if (closeId == null) {
            closeId = "";
        }else{
            var _that = top.$("#"+closeId).find("i");
            commonUtil.closeIframe(_that);
        }
        if (replaceId == null) {
            replaceId = "";
        }
        if(url){
            commonUtil.fn_addtabiframe(menuId, title, url);
        }
    },

    fn_addtabiframe: function (menuId, title, url) {
        if ($('#tabList li',parent.document).length < 40) {
            var add_li = $('<li menuId=' + menuId +  ' id=' + menuId + '><span>' + title + '</span><i class="anticon icon-close"></i></li>');
            var add_ifame = $('<div class="tab-content" menuId=' + menuId + '><iframe src="' + url + '"></iframe></div>');
            $('#tabList',parent.document).append(add_li);
            $('#tabIframe',parent.document).append(add_ifame);
            $('.more',parent.document).show();
            //切换栏显示8-22
            $('.window-wrap',parent.document).addClass('active');
            commonUtil.fn_tabOverflow($('#tabList li',parent.document).length - 1);
            $('#tabList li[menuId=' + menuId + ']',parent.document).click();

        } else {
            layer.open({
                type: 1,//Page层类型
                area: ['400px', ''],
                title: '',
                closeBtn: 0,
                btn: "确定",
                skin: 'skin-searchMore',
                shade: 0.001,//遮罩透明度
                maxmin: false, //允许全屏最小化
                anim: 5,//0-6的动画形式，-1不开启
                content: '<p class="layer-tips"><i class="anticon icon-exclamationcircle fs17 brown"></i>您打开的页面过多，请先关闭！</p>',
                btn1: function () {
                },
            })
        }
    },

    closeIframe : function(_that){
        var index = $(_that).parent().index(), activeNum = 0;
        if ($(_that).closest('li').hasClass('active')) {
            var i = '<i class="anticon icon-close">';
            var next = $(_that).parent().next().length;
            var prev = $(_that).parent().prev().length;
            //中间 || 第一个
            if (next && (prev || !prev)) {
                $(_that).parent().next().addClass("active");
                $('#tabIframe .tab-content').eq(index + 1).show();
                activeNum = index + 1;
            }
            //最后一个
            else if (!next && prev) {
                $(_that).parent().prev().addClass("active");
                $('#tabIframe .tab-content').eq(index - 1).show();
                activeNum = index - 1;
            }
            //只有一个
            else if (!next && !prev) {
                // alert("不能关闭");
                // return false;
                $('.more').hide();
                //切换栏隐藏8-22
                $('.window-wrap').removeClass('active');
            }
        }
        layer.close(layer.tips())
        $(_that).parent().remove();
        $('#tabIframe .tab-content').eq(index).remove();
        //tcrm.fn_tabOverflow(activeNum)
        commonUtil.fn_tabOverflow($('#tabList li.active').index());
    },
    fn_tabOverflow: function (number) {
        // 切换栏宽度优化8-22
        var total = 0, arrAll = $('#tabList li',parent.document), windowWidth = $('.window-wrap',parent.document).outerWidth(true) - 70;
        $('#tabList li',parent.document).show();
        $('.more .more-list',parent.document).empty();
        for (var i = number; i >= 0; i--) {
            var _width = arrAll.eq(i).outerWidth(true)
            total += _width
            if (total > windowWidth) {
                arrAll.eq(i).hide();
                arrAll.eq(number).next('li').hide();
            }
        };
        if (total <= windowWidth && number < arrAll.length - 1) {
            for (var i = number + 1; i < arrAll.length; i++) {
                var _width = arrAll.eq(i).outerWidth(true);
                total += _width;
                if (total > windowWidth) {
                    arrAll.eq(i).hide();
                }
            };
        }
        $('#tabList li:hidden',parent.document).each(function (index, item) {
            var clone = item.outerHTML.replace('<i class="anticon icon-close"></i>', "");
            $('.more .more-list',parent.document).append(clone).find('li').show()
        });
    },
    // ajax请求的公共方法，主要是对提示信息类的样式做了统一的处理（isbatch:true表示列表页面的批量操作，当此参数为true时需要传length用于提示信息）
    update: function (url, data, grid, isbatch, title) {
        var method = function () {
            // 发送请求
            commonUtil.sendRequest(url, data, grid, isbatch);
        }
        this.commonConfirmTips(method, title);
    },
    // 详情页单笔数据简单ajax操作
    detailPageSingleEasyAjax : function(url,data,title,callback){
        var method = function(){
            // 发送请求
            commonUtil.sendSingleEasyAjax(url,data,callback);
        };
        this.commonConfirmTips(method,title);
    },
    // 发送单笔简单ajax请求
    sendSingleEasyAjax : function(url,data,callback){
        var index = layer.load(0, {shade: 0.001});
        $.ajax({
            url: url,
            type: 'POST',
            dateType: 'json',
            contentType: "application/json;charset=utf-8",
            data:JSON.stringify(data) ,
            success: function (data) {
                layer.close(index);
                if(data.code == 0){
                    if(data.result != null && data.result.length > 0){
                        // 处理失败
                        var method = function(){
                            if(data.result[0].code && data.result[0].code == -2){
                                //如数据已经被删除，停留页面不做任何处理
                            }else {
                                //其他失败，刷新页面
                                var loadIndex = layer.load(0, { shade: 0.001 });
                                window.location.reload();
                            }
                        };
                        commonUtil.commonErrorTips(data.result[0].text,method);
                    }else{
                        // 处理成功，有回调执行回调，无回调刷新页面
                        var method = function(){
                            if($.isFunction(callback)){
                                callback(data);
                            }else {
                                var loadIndex = layer.load(0, { shade: 0.001 });
                                window.location.reload();
                            }
                        };
                        commonUtil.commonSuccessTips(method);
                    }
                }else{
                    // 发生了异常，给出异常信息
                    commonUtil.commonErrorTips(data.text);
                }
            },
            error: function () {
                layer.close(index);
                commonUtil.commonErrorTips();
            }
        });
    },
    // 发送ajax请求
    sendRequest: function (url, data, grid, isbatch) {
        //显示loading
        var index = layer.load(0, {shade: 0.001});
        $.ajax({
            url: url,
            type: 'POST',
            dateType: 'json',
            contentType: "application/json;charset=utf-8",
            data: JSON.stringify(data),
            success: function (data) {
                layer.close(index);
                if (isbatch) {
                    commonUtil.batchOperateSuccess(data, grid);
                } else {
                    commonUtil.singleOperateSuccess(data, grid);
                }
            },
            error: function () {
                layer.close(index);
                commonUtil.commonErrorTips();
            }
        });
    },
    // 批量操作请求成功
    batchOperateSuccess: function (data, grid) {
        if (data.code == 0 && data.result.length == 0) {
            // 批量操作全部处理成功
            layer.open({
                type: 1,//Page层类型
                area: ['400px', ''],
                title: '',
                closeBtn: 0,
                btn: ["确定"],
                skin: 'skin-searchMore',
                shade: 0.001,//遮罩透明度
                maxmin: false, //允许全屏最小化
                anim: 5,//0-6的动画形式，-1不开启
                content: '<p class="layer-tips"><i class="anticon icon-checkcircle fs17 green"></i>' + data.text + '</p>',
                btn1: function () {
                    grid.gridQuery();
                },
            })
        } else if (data.code == 0 && data.result.length != 0) {
            // 批量操作部分处理成功
            commonUtil.addErrorHtml();
            var trs = "";
            $.each(data.result, function (n, value) {
                trs += value.text + "<br>";
            });
            $("#batchOperateDetailMessage").html(trs);
            $("#batchOperateMessage").html(data.text);
            layer.open({
                type: 1,//Page层类型
                area: ['550px', ''],
                title: '',
                closeBtn: 0,
                btn: ["确定"],
                skin: 'skin-searchMore layer-btn',
                shade: 0.001,//遮罩透明度
                maxmin: false, //允许全屏最小化
                anim: 5,//0-6的动画形式，-1不开启
                content: $('.layer-openError'),
//                btn1:function(){
//                	commonUtil.copy($("#batchOperateDetailMessage").text());
//                	grid.gridQuery();
//                },
                btn1: function () {
                    grid.gridQuery();
                },
            })
        } else {
            // 批量操作请求成功但发生了异常
            var method = function () {
                grid.gridQuery();
            }
            commonUtil.commonErrorTips(data.text, method);
        }
    },
    // 单条操作请求成功
    singleOperateSuccess: function (data, grid) {
        if (data.code == 0) {
            // result != null则表明此单条操作与批量操作共用同一个接口
            if (data.result != null && data.result.length != 0) {
                // 单条操作处理失败
                var method = function () {
                    // 失败了点确认要不要处理？
                    if (grid) {
                        grid.gridQuery();
                    }
                }
                commonUtil.commonErrorTips(data.result[0].text, method);
            } else {
                // 单条操作处理成功
                var method = function () {
                    if (grid) {
                        grid.gridQuery();
                    }
                }
                commonUtil.commonSuccessTips(method);
            }
        } else {
            // 单条操作请求成功但发生了异常
            var method = function () {
                if (grid) {
                    grid.gridQuery();
                }
            }
            commonUtil.commonErrorTips(data.text, method);
        }
    },
    addErrorHtml: function () {
        if ($(".layer-openError").length == 0) {
            $("body").append('<div class="layer-openError" style="display: none;">' +
                '<div class="open-cont"><div class="open-content">' +
                '<i class="anticon icon-closecircle"></i><span class="open-import" id="batchOperateMessage"></span>' +
                '</div></div><div class="open-detail" id="batchOperateDetailMessage">' +
                '</div></div>');
        }
    },
    copy: function (str) {
        var save = function (e) {
            e.clipboardData.setData('text/plain', str);
            e.preventDefault();
        }
        document.addEventListener('copy', save);
        document.execCommand('copy');
        document.removeEventListener('copy', save);
        layer.msg('<i class="anticon icon-checkcircle fs17 green"></i>复制成功！', {skin: "skin-msg", time: "1500"});
    },
    //统一的错误提示信息 
    commonErrorTips: function (message, method) {
        if (message == null) {
            message = "网络繁忙，请稍后重试！";
        }
        layer.open({
            type: 1,//Page层类型
            area: ['400px', ''],
            title: '',
            closeBtn: 0,
            btn: ["确定"],
            skin: 'skin-searchMore',
            shade: 0.001,//遮罩透明度
            maxmin: false, //允许全屏最小化
            anim: 5,//0-6的动画形式，-1不开启
            content: '<p class="layer-tips"><i class="anticon icon-closecircle fs17 red"></i>' + message + '</p>',
            btn1: function () {
                if (method) {
                    method();
                }
            },
        })
    },
    //统一的带确认、取消按钮的询问弹窗 
    commonConfirmTips: function (method, message) {
        layer.open({
            type: 1, //page层
            area: ['400px', 'auto'],
            title: '',
            closeBtn: 0,
            shadeClose: false,
            btn: ['确定', '取消'],
            shade: 0.01, //遮罩透明度
            moveType: 1, //拖拽风格，0是默认，1是传统拖动
            shift: -1, //0-6的动画形式，-1不开启
            content: '<p class="layer-tips"><i class="anticon icon-questioncircle fs17 blue"></i>' + message + '</p>',
            skin: "skin-searchMore",
            yes: function (index) {
                layer.close(index);
                if (method) {
                    method();
                }
            }
        })
    },
    //统一的单条操作成功提示信息
    commonSuccessTips: function (method, message) {
        if (message == null) {
            message = '操作成功';
        }
        layer.msg("<i class='anticon icon-checkcircle fs17 green'></i>" + message, {
            skin: "skin-msg",
            time: "1500"
        }, function () {
            if (method) {
                method();
            }
        });
    },
    getListPageHeight: function () {
        var _height = $('.tab').outerHeight(true) * $('.tab').length + $('.form-list').outerHeight(true) + $('.operation').outerHeight(true) + $('.box-detail').outerHeight(true);
        return $(window).height() - _height - 15;
    },
    // 重算列表页面table高度以自适应浏览器大小
    resizeHeight: function (table) {
        $(window).resize(function () {
            table.bootstrapTable('resetView', {
                height: commonUtil.getListPageHeight()
            });
        });
    },
    saveParams: function () {
        var saveData = {};
        $('input.saveParam').each(function (index, item) {
            if ($(this).attr('id')) {
                saveData[$(this).attr('id')] = $(this).val();
            }
        });
        return JSON.stringify(saveData);
    },
    fn_getParameter:function(paraStr, url){
        var result = "";
        //获取URL中全部参数列表数据
        var str = "&" + url.split("?")[1];
        var paraName = paraStr + "=";
        //判断要获取的参数是否存在
        if(str.indexOf("&"+paraName)!=-1)
        {
            //如果要获取的参数到结尾是否还包含“&”
            if(str.substring(str.indexOf(paraName),str.length).indexOf("&")!=-1){
                //得到要获取的参数到结尾的字符串
                var TmpStr=str.substring(str.indexOf(paraName),str.length);
                //截取从参数开始到最近的“&”出现位置间的字符
                result=TmpStr.substr(TmpStr.indexOf(paraName),TmpStr.indexOf("&")-TmpStr.indexOf(paraName));
            }else{
                result=str.substring(str.indexOf(paraName),str.length);
            }
        }else{
            result="";
        }
        return (result.replace("&","").split("=")[1]);
        //调用方法：var 变量名 = getParameter("要获取的参数名", URL地址)
    }
}
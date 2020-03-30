var tcrm, that;
require(["public", "layer", "validator"], function () {
    layer.config({
        path: base + '/static/css/common/'
    });
    $(function () {
        // 新增自定义的校验规则
        $.validator.addMethod("newPasswordValidate", function (value, element, params) {
            if ($("#newPassword1").val() != $("#newPassword2").val()) {
                return false;
            }
            return true;
        }, '两次新密码不一样');
        $.validator.addMethod("passwordValidate", function (value, element, params) {
            if ($("#newPassword1").val() == $("#password").val()) {
                return false;
            }
            return true;
        }, '新密码不能与旧密码相同');
        tcrm = {
            init: function () {
                this.fn_iframeHeight();
                this.fn_tabs();
                this.fn_more();
                this.fn_layer();
                this.fn_hoverShow();
                that = this;
                $(window).resize(function () {
                    that.fn_tabOverflow($('#tabList li.active').index());
                    that.fn_iframeHeight();
                });
            },
            fn_hoverShow: function () {
                $('body').on({
                    mouseenter: function () {
                        if ($(this).find('span')[0].offsetWidth < $(this).find('span')[0].scrollWidth) {
                            layer.tips($(this).find('span').text(), $(this), {tips: [2, '#108ee9'], time: 0});
                        }
                        ;
                    },
                    mouseleave: function () {
                        layer.closeAll('tips');
                    },
                }, "#tabList li,.more-list li");
            },
            fn_addtabiframe: function (menuId, title, url) {
                if ($('#tabList li').length < 40) {
                    var add_li = $('<li menuId=' + menuId + '><span>' + title + '</span><i class="anticon icon-close"></i></li>');
                    var add_ifame = $('<div class="tab-content" menuId=' + menuId + '><iframe src="' + url + '"></iframe></div>');
                    $('#tabList').append(add_li);
                    $('#tabIframe').append(add_ifame);
                    $('.more').show();
                    //切换栏显示8-22
                    $('.window-wrap').addClass('active');
                    that.fn_tabOverflow($('#tabList li').length - 1);

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
            fn_tabs: function () {
                $('body').on('click', '.main-menu a', function (e) {
                    e.stopPropagation();
                    var url = $(this).attr('data-href');
                    if (url) {
                        var menuId = $(this).attr('mid');
                        if(!menuId){
                            var mydate = new Date();
                            menuId =  mydate.getTime();
                        }
                        if (!$('#tabList li[menuId=' + menuId + ']').length) {
                            var title = $(this).text();
                            that.fn_addtabiframe(menuId, title, url);
                        } else {
                            that.fn_tabOverflow($('#tabList li[menuId=' + menuId + ']').index());
                        }
                        $('#tabList li[menuId=' + menuId + ']').click();
                    }
                });
            },
            fn_iframeHeight: function () {
                //tabIframe高度优化8-22
                var _height = $(window).height() - $('.rightside .head').outerHeight(true) - $('.rightside .window-wrap').outerHeight(true);
                $('#tabIframe').css('height', _height)
            },
            fn_detail: function (url, title, menuId, removeTab, replaceTab) {
                // 1-5
                if (url) {
                    //menuId = menuId.replace(/%/g,'');
                    if (!$('#tabList li[menuId=' + menuId + ']').length) {
                        that.fn_addtabiframe(menuId, title, url);
                    } else {
                        that.fn_tabOverflow($('#tabList li[menuId=' + menuId + ']').index());
                    }
                    $('#tabList li[menuId=' + menuId + ']').click();
                }
                ;
                if (removeTab) {
                    var closeTable = $('#tabList li[menuId=' + removeTab + ']');
                    if (closeTable.length > 0) {
                        if (closeTable.has('i.icon-close').length) {
                            closeTable.find('i.icon-close').click();
                        } else {
                            $('#tabIframe .tab-content').eq(closeTable.index()).remove();
                            closeTable.remove();
                            tcrm.fn_tabOverflow($('#tabList li.active').index());
                        }
                    }
                }
                if (replaceTab) {
                    var arr = replaceTab.split(',');
                    $('#tabList li[menuId=' + arr[0] + ']').attr('menuId', arr[1]).find('span').html(arr[2]);
                    $('#tabIframe .tab-content[menuId=' + arr[0] + ']').attr('menuId', arr[1]);
                    if (arr[3]) {
                        // replaceTab url没有的时候just更新tab名称，满足销售特殊需求
                        $('#tabIframe .tab-content[menuId=' + arr[1] + ']').find('iframe').attr('src', arr[3]);
                    }
                    tcrm.fn_tabOverflow($('#tabList li.active').index());
                }
            },
            fn_tabOverflow: function (number) {
                // 切换栏宽度优化8-22
                var total = 0, arrAll = $('#tabList li'), windowWidth = $('.window-wrap').outerWidth(true) - 70;
                $('#tabList li').show();
                $('.more .more-list').empty();
                for (var i = number; i >= 0; i--) {
                    var _width = arrAll.eq(i).outerWidth(true)
                    total += _width
                    if (total > windowWidth) {
                        arrAll.eq(i).hide();
                        arrAll.eq(number).next('li').hide();
                    }
                }
                ;
                if (total <= windowWidth && number < arrAll.length - 1) {
                    for (var i = number + 1; i < arrAll.length; i++) {
                        var _width = arrAll.eq(i).outerWidth(true)
                        total += _width;
                        if (total > windowWidth) {
                            arrAll.eq(i).hide();
                        }
                    }
                    ;
                }
                $('#tabList li:hidden').each(function (index, item) {
                    var clone = item.outerHTML.replace('<i class="anticon icon-close"></i>', "");
                    $('.more .more-list').append(clone).find('li').show()
                });
            },
            fn_more: function () {
                $("body").on("click", ".more .more-list li", function () {
                    var menuId = $(this).attr('menuid'),
                        number = $('#tabList li[menuId=' + menuId + ']').click().index();
                    tcrm.fn_tabOverflow(number);
                })
                $("body").on("click", ".more-action a", function () {
                    var state = $(this).attr('close'), othis = $('#tabList li.active');
                    switch (state) {
                        case "now":
                            othis.find('i.icon-close').click();
                            break;
                        case 'other':
                            othis.siblings('li').remove();
                            $('#tabIframe .tab-content:hidden').remove();
                            $('.more-list').empty();
                            break;
                        case 'all':
                            $('#tabList').empty();
                            $('#tabIframe').empty();
                            $('.more').hide();
                            //切换栏隐藏 8-22
                            $('.window-wrap').removeClass('active');
                            break;
                    }
                })
            },
            fn_layer: function () {
                // 修改密码
                $("body").on("click", ".resetpsw", function () {
                    layer.open({
                        type: 1, //page层
                        area: ['550px', 'auto'],
                        title: "修改密码",
                        shadeClose: false,
                        btn: ['确定', '取消'],
                        shade: 0.01, //遮罩透明度
                        moveType: 1, //拖拽风格，0是默认，1是传统拖动
                        shift: -1, //0-6的动画形式，-1不开启
                        content: $('.layer-resetpsw'),
                        skin: "skin-searchMore",
                        yes: function (index) {
                            validateInit();
                            if ($("#passwordForm").valid()) {
                                modifyPassword(index);
                            }
                        }
                    })
                });
            }
        }
        tcrm.init();
    })

})
require(["scmmessage"], function () {
    //message.init("10.0.53.200:8080",$("#accountCode").val());
})

function logout() {
    layer.open({
        title: "提示",
        btn: ["确认", "取消"],
        shade: 0.01, //遮罩透明度
        content: "是否确认注销？",
        yes: function (index, layero) {
            //通知站内信注销会话
            messageIframe.postMessage('logout', '*');

            setTimeout(function () {
                logoutPath = $("#logoutPath").attr('data');
                window.location.href = logoutPath + "/logout";
            }, 1000);

        },
        btn2: function (index, layero) {
            return false;
        }
    })
}

function modifyPassword(index) {
    var vo = {
        password: $("#password").val(),
        newPassword1: $("#newPassword1").val(),
        newPassword2: $("#newPassword2").val(),
    };
    var loadIndex = layer.load(0, {shade: 0.001});
    $.ajax({
        url: base + "/modifyPassword",
        type: 'POST',
        dateType: 'json',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(vo),
        success: function (data) {
            layer.close(loadIndex);
            if (data.code == 0) {
                layer.close(index);
                layer.msg("<i class='anticon icon-checkcircle fs17 green'></i>" + data.text, {
                    skin: "skin-msg",
                    time: "1500"
                });
            } else {
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
                    content: '<p class="layer-tips"><i class="anticon icon-closecircle fs17 red"></i>' + data.text + '</p>',
                    btn1: function () {
                    },
                })
            }
        },
        error: function (request) {
            layer.close(loadIndex);
            var msg = "修改密码失败";
            if (request.status == 403) {
                msg = "无修改权限"
            }
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
                content: '<p class="layer-tips"><i class="anticon icon-closecircle fs17 red"></i>' + msg + '</p>',
                btn1: function () {
                },
            })
        }
    });
}

function validateInit() {
    // 对表单中不可为空的字段进行定义，对于输入框的长度定义写在ftl中
    $("#passwordForm").validate({
        rules: {
            password: {
                required: true,
            },
            newPassword1: {
                required: true,
                passwordValidate: true,
            },
            newPassword2: {
                required: true,
                newPasswordValidate: true,
            },
        },
        messages: {
            password: {
                required: "必填项不能为空",
            },
            newPassword1: {
                required: '必填项不能为空',
            },
            newPassword2: {
                required: '必填项不能为空',
            }
        },
    });
}

function messageUrlHandler(id, content, url) {
    if (url) {
        if (!$('#tabList li[menuId=' + id + ']').length) {
            tcrm.fn_addtabiframe(id, content, url);
        } else {
            tcrm.fn_tabOverflow($('#tabList li[menuId=' + id + ']').index());
        }
        $('#tabList li[menuId=' + id + ']').click();
    }
    ;
}
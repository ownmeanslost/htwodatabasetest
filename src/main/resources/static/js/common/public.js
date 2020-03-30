var public = {
    // 下拉
    fn_dropdown: function () {
        $(".selectHover").hover(function () {
            $(this).find("ul").stop(true, true).slideDown(300);
        }, function () {
            $(this).find("ul").stop(true, true).slideUp(300);
        });
        $('.subselects').on('click', '.subselect-hasarrow', function () {
            if ($(this).parent().hasClass('active')) {
                $(this).parent().removeClass('active');
            } else {
                $(this).parent().addClass('active');
            }
        })
    },
    // 窗口tab
    fn_windowTab: function () {
        //点击
        $("body").on("click", 'ul.window li', function (e) {
            // var len = $(this).find("i").length;
            // if (!len) {
            //     var i = '<i class="anticon icon-close">';
            //     $(this).addClass("active").append(i).siblings().removeClass("active").find("i").remove();
            // }
            $(this).addClass("active").siblings().removeClass("active");
        });

        //关闭
        $("body").on("click", "ul.window > li > i", function (e) {
            e.stopPropagation();
            var index = $(this).parent().index(), activeNum = 0;
            if ($(this).closest('li').hasClass('active')) {
                var i = '<i class="anticon icon-close">';
                var next = $(this).parent().next().length;
                var prev = $(this).parent().prev().length;
                //中间 || 第一个
                if (next && (prev || !prev)) {
                    $(this).parent().next().addClass("active");
                    $('#tabIframe .tab-content').eq(index + 1).show();
                    activeNum = index + 1;
                }
                //最后一个
                else if (!next && prev) {
                    $(this).parent().prev().addClass("active");
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
            $(this).parent().remove();
            $('#tabIframe .tab-content').eq(index).remove();
            //tcrm.fn_tabOverflow(activeNum)
            tcrm.fn_tabOverflow($('#tabList li.active').index());
        });
    },

    // 导航tab
    fn_navTab: function () {

        $(document).on('click', '#tabList li', function () {
            // var index = $(this).index();
            // $(this).addClass('active').siblings('li').removeClass('active')
            // $('#tabIframe .tab-content').hide().eq(index).show()

            var menuid = $(this).attr('menuid');
            var index = $(this).index();
            $(this).addClass('active').siblings('li').removeClass('active')
            $('#tabIframe .tab-content').hide()
            $('#tabIframe .tab-content[menuid=' + menuid + ']').show()
        })

        $(document).on('click', '.tab>ul>li', function () {
            var index = $(this).index();
            $(this).addClass('active').siblings('li').removeClass('active')
            $(this).parents('.tab-wrap').find('.tab-content-inner').hide().eq(index).show();
            public.fn_viewImgCont();
        })
        $(window).resize(function () {
            public.fn_viewImgCont();
        })
    },


    // 左侧菜单
    fn_sideBar: function () {
        // 展开收起
        $("body").on("click", ".change", function (e) {
            $('.fixed-table-header').hide();
            $('.fixed-table-body>table').css({'marginTop': 0})
            $('.fixed-table-body thead tr').css('borderBottom', '1px  solid #f4f4f4')
            $('.fixed-table-body').css({'marginBottom': 36})
            if ($(this).hasClass("icon-menufold")) {
                $(this).removeClass("icon-menufold").addClass("icon-menuunfold");
                $(".leftside").animate({
                    width: 42,
                }, function () {
                    $('.fixed-table-header').show();
                    $('.fixed-table-body thead tr').css('borderBottomWidth', 0)
                    $('.fixed-table-body').css({'marginBottom': 0})
                    $('#table-parent').bootstrapTable('resetWidth');

                    $('.fixed-table-body>table').css({'marginTop': -36})
                });

                $(".logo").addClass("minilogo");
                $(".main-menu").addClass("mini-menu");
                $(".main-menu>li>a").addClass("js-sub-menu-toggle-degree1")
                $(".js-sub-menu-toggle.on").next('.sub-menu').hide();


            } else {
                $(this).removeClass("icon-menuunfold").addClass("icon-menufold");
                $(".leftside").animate({
                    width: 204
                }, function () {
                    $('.fixed-table-header').show();
                    $('.fixed-table-body thead tr').css('borderBottomWidth', 0)
                    $('.fixed-table-body').css({'marginBottom': 0})
                    $('#table-parent').bootstrapTable('resetWidth');
                    $('.fixed-table-body>table').css({'marginTop': -36})
                });
                //  $('.fixed-table-header>table').animate({
                //     width: wid1,
                // });

                $(".logo").removeClass("minilogo");
                $(".main-menu").removeClass("mini-menu");
                $(".main-menu>li>a").removeClass("js-sub-menu-toggle-degree1")
                $(".js-sub-menu-toggle.on").next('.sub-menu').show();

            }
        });

        // mini菜单hover状态
        // var timer;
        $("body").on("mouseenter", ".mini-menu li >.sub-menu", function (e) {

            $(this).siblings('.js-sub-menu-toggle').addClass('subactive');
        });

        $("body").on("mouseleave", ".mini-menu li >.sub-menu", function (e) {
            $(this).siblings('.js-sub-menu-toggle').removeClass('subactive');
        });
        //超出显示
        $('body').on({
            mouseenter: function () {
                if ($(this)[0].offsetWidth < $(this)[0].scrollWidth - 8) {
                    var top = $(this).offset().top;
                    setTimeout(function () {
                        public.fn_layerTop(top)
                    }, 0)
                    if ($(this).has('input').length != 0) {
                        layer.tips(public.fn_replace($(this).find('input').val()), $(this), {
                            tips: [2, '#108ee9'],
                            time: 0
                        });
                        return;
                    }
                    layer.tips(public.fn_replace($(this).text()), $(this), {tips: [2, '#108ee9'], time: 0});
                }
                ;
            },
            mouseleave: function () {
                layer.closeAll('tips');
            },
        }, "table td,table td .th-inner,table th:not(.sortableth) .th-inner");
        $('body').on({
            mouseenter: function () {
                if ($(this)[0].offsetWidth < $(this)[0].scrollWidth - 1) {
                    if ($(this).has('input').length != 0) {
                        layer.tips(public.fn_replace($(this).find('input').val()), $(this), {
                            tips: [2, '#108ee9'],
                            time: 0
                        });
                        return;
                    }
                    layer.tips(public.fn_replace($(this).text()), $(this), {tips: [2, '#108ee9'], time: 0});
                }
                ;
            },
            mouseleave: function () {
                layer.closeAll('tips');
            },
        }, "table th.sortableth .sortable");
        // 左侧下拉菜单折叠
        $("body").on("click", ".main-menu .js-sub-menu-toggle:not('.js-sub-menu-toggle-degree1')", function (e) {
            e.preventDefault();
            $(this).toggleClass("on");
            $(this).parent().find('> .sub-menu').stop(true, true).slideToggle(300);
            //下拉菜单其他关闭8-22
            $(this).parent().siblings().find('.js-sub-menu-toggle.on').removeClass('on').next('.sub-menu').stop(true, true).slideToggle(300);
        });

        // 左侧下拉菜单点击状态
        // $("body").on("click", ".main-menu .sub-menu >li", function(e) {
        //     e.preventDefault();
        //     if (!$(this).find(">a").hasClass("js-sub-menu-toggle")) {
        //         $(this).parents('.main-menu').find('a').removeClass('active');
        //         $(this).find('> a').addClass('active');
        //     }
        // });
        //左侧下拉菜单点击状态 8-22
        $("body").on("click", ".main-menu li a:not(.js-sub-menu-toggle)", function (e) {
            e.preventDefault();
            $(this).parents('.main-menu').find('a').removeClass('active');
            $(this).addClass('active')
        });
    },
    fn_layerTop: function (thisTop) {
        var layerTips = $('.layui-layer-tips:visible').css('width', 'auto'),
            layerHeight = layerTips.outerHeight(true),
            height = $(window).height();
        if (layerHeight > height - thisTop) {
            var layerTop = (height - layerHeight) / 2;
            if (layerHeight < height) {
                layerTips.css({top: layerTop}).find('.layui-layer-content i').css({'top': thisTop - layerTop});
            } else {
                layerTips.css({top: 0,}).find('.layui-layer-content i').css({'top': thisTop});
            }
        }
    },
    // 控制左侧菜单高度8-22
    fn_sideBarHeight: function () {
        var _height = $(window).height() - $('.logo-wrap').outerHeight(true) - 10;
        $('.main-menu').css('height', _height)
    },
    // 窗口改变
    fn_resize: function () {
        $(window).resize(function () {
            public.fn_sideBarHeight();
        });
    },
    // 下拉菜单
    fn_select: function () {
        // 自动匹配
        $("body").on("click", ".autocomplete .scm-input-wrap:not(.disabled,.alwaysDisabled)", function (e) {
            e.stopPropagation();
            var that = this, parents = $(this).parents('.autocomplete'),
                suggestions = parents.find('.autocomplete-suggestions');
            $(this).toggleClass("on");
            if (!$(this).hasClass('on')) {
                $(this).find('input').blur();
                parents.removeClass('on')
                suggestions.hide();
            } else {
                $(this).find('input').focus();
                parents.addClass('on')
                suggestions.show();
            }
        })
        $("body").on("click", ".autocomplete .scm-input-wrap:not(.disabled,.alwaysDisabled) input", function (e) {
            e.stopPropagation();
            $(this).parent().addClass("on");
        })
        //点击下拉展开
        $("body").on("click", ".selectshowAction:not(.disabled,.alwaysDisabled)", function (e) {
            $('.packageSelector').addClass('hide').prev('.selectshow').removeClass('on');
            $('.selector-item').removeClass('on').find('.selector-list').addClass('hide');
            // e.stopPropagation()
            e.preventDefault();
            $('.selector-item.on .selector-list').addClass('hide');
            $('.selector-item.on').removeClass("on");
            var _this = $(this).next("ul")[0]
            $('.select').not(_this).slideUp(300)
            $('.selectshowAction').not(this).removeClass("on")
            $(this).toggleClass("on");
            $(this).next("ul").stop(true, true).slideToggle(300);
            if ($(this).parents('td').length != 0) {
                $(this).parents('td').css({"overflow": "visible"})
            }
            return false;
        });

        $("body").on("click", 'ul.select:not(#setting) li', function (e) {
            // $("ul.select:not(#setting)").delegate("li", "click", function(e) {
            var text = $(this).text(), liDatevalue = $(this).data('value');
            if ($(this).parents('.group-select').length != 0) {
                $(this).parent().prev(".selectshowAction").find(".scm-input").val(text).toggleClass("on").data('value', liDatevalue).change();
            } else {
                $(this).parent().prev(".selectshowAction").find("span").text(text).toggleClass("on").data('value', liDatevalue);
            }
            $(this).parent().stop(true, true).slideToggle(300);
        });
        //点击其他地方消失
        $(document).on("click", function (e) {
            $('ul.select').slideUp(300)
            $('.selectshowAction,.autocomplete .scm-input-wrap').removeClass("on")
        })

    },

    // input显示删除图标
    fn_inputClose: function () {
        var clear = '<i class="anticon icon-closecircle">';
        $(document).on({
            mouseenter: function () {
                $(this).parents('.group').find(".icon-closecircle").remove()
                var val = $(this).find('input').val();
                if (val) {
                    if ($(this).closest('.autocomplete').find('ul').is(':visible')) {
                        return;
                    }
                    // $(this).addClass('iconhide').removeClass('selectshow')
                    $(this).find('input').before(clear);
                    $(this).find('.icon-closecircle').show();
                    // if($(this).parents('.group-link').length){
                    $(this).find('input').addClass('hasClose');
                    // }
                }
            },
            mouseleave: function () {
                $(this).removeClass('iconhide').find('input').prev(".icon-closecircle").remove();
                var parent = $(this).parents('.group-select');
                $(this).find('input.hasClose').removeClass('hasClose');
                if (parent) {
                    parent.find('.scm-input-wrap').addClass('selectshow')
                }

            },
        }, ".scm-input-wrap:not(.hideClean,.disabled,.alwaysDisabled)");
        $('body').on('click', '.group .scm-input-wrap .icon-closecircle', function (e) {
            e.stopPropagation();
            // $(this).siblings('input').val('').data('value',"").change();
            // autocomplete优化 8-22
            $(this).siblings('input').change().val('').data('value', "").data("code", "");
            var autocompleteInput = $(this).siblings('input:text[autocomplete]');
            if (autocompleteInput.length) {
                autocompleteInput.autocomplete().clear();
            }
            if ($(this).closest('td').length) $(this).siblings('input').blur();
            $(this).parents('.scm-input-wrap').removeClass('iconhide').next('.autocomplete-suggestions').hide();
            var parent = $(this).parents('.group-select')
            var that = $(this)
            $(this).remove()
            if (parent) {
                parent.find('.scm-input-wrap').addClass('selectshow')
            }
        });
        //联动
        $('body').on({
            mouseenter: function (e) {
                if (!$(e.target).parents('.selector-list').length) {
                    $(this).find(".icon-closecircle").remove()
                    if (!$(this).find('.selector-name').hasClass('df-color')) {
                        $(this).prepend(clear).addClass('iconshow');
                    }
                }
            },
            mouseleave: function () {
                $(this).find(".icon-closecircle").remove();
                $(this).removeClass('iconshow');
            },
        }, ".selector-item:not(.hideClean,.disabled,.alwaysDisabled)");
        $('body').on('click', '.selector-item .icon-closecircle', function (e) {
            e.stopPropagation();
            var selector_name = $(this).siblings('.selector-name'),
                selector_item = $(this).parents('.selector-item');
            if (selector_item.hasClass('province')) {
                selector_name.html('请选择省份').siblings('.input-price').val("").change();
                $(this).parents('.city-picker-selector').find('.city .selector-name').html('请选择城市').addClass('df-color forbid').siblings('.input-price').val("").change();
                $(this).parents('.city-picker-selector').find('.district .selector-name').html('请选择区县').addClass('df-color forbid').siblings('.input-price').val("").change();
            } else if (selector_item.hasClass('city')) {
                selector_name.html('请选择城市').siblings('.input-price').val("").change();
                $(this).parents('.city-picker-selector').find('.district .selector-name').html('请选择区县').addClass('df-color forbid').siblings('.input-price').val("").change();
            } else if (selector_item.hasClass('district')) {
                selector_name.html('请选择区县').siblings('.input-price').val("").change();
            }
            ;
            selector_name.addClass('df-color')
            selector_item.removeClass('iconshow')
            $(this).remove();
        });
    },

    //编辑保存值
    fn_saveData: function () {
        function getData() {
            var odiv = $(this).closest('table'),
                oheaderTh = $(this).closest('.fixed-table-container').find('.fixed-table-header table tr:eq(0) th'),
                otr = $(this).closest('tr'),
                otd = $(this).closest('td'),
                otdindex = otd.index(),
                oid = otr.data('uniqueid'),
                odata = odiv.bootstrapTable('getRowByUniqueId', oid),
                ofield = oheaderTh.eq(otdindex).data('field');
            if (otd.has('.select').length) {
                odata[ofield] = otd.find('input').data('value');
            } else {
                odata[ofield] = otd.find('input').val();
            }
        };
        $('body').on('blur', '.fixed-table-body td input', getData);
        $('body').on('click', '.fixed-table-body td .select li,.fixed-table-body td .autocomplete-suggestions li,.fixed-table-body td .packageSelector li.caller', getData)
    },
    // table操作按钮下拉
    fn_tableselect: function () {
        $(document).on('click', '.tableselect .icon-down', function (e) {
            e.stopPropagation()
            var _this = $(this).parents('.tableselect')[0]
            var _this1 = $(this).parents('.tableselect').find('.tableselect-bottom')[0]
            $('.tableselect').not(_this).removeClass("active")
            $('.tableselect-bottom').not(_this1).slideUp(300)
            $(this).parents('.tableselect').toggleClass('active')
            $(this).parents('.tableselect').find('.tableselect-bottom').stop(true, true).slideToggle(300)
        })
        //点击其他地方消失
        $(document).on("click", function (e) {
            $('.tableselect-bottom').slideUp(300)
            $('.tableselect').removeClass("active")
        })
    },
    //汉化
    fn_chinese: function () {

        // 日历汉化
        $.daterangepicker = function (pickerSelect, obj) {
            var defaults = {
                //minDate: '01/01/2012', //最小时间
                //maxDate: moment().subtract(-1, 'days'), //最大时间 今天
                timePicker: false, //是否显示小时和分钟
                timePickerIncrement: 1, //时间的增量，单位为分钟
                timePicker12Hour: false, //是否使用24小时制来显示时间
                "autoApply": false, // 选中后自动关闭控件窗口
                format: "YYYY-MM-DD", // 日期格式
                singleDatePicker: false, //设置成单日历
                separator: "~",
                "locale": {
                    daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
                    monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
                    applyLabel: '确定',
                    cancelLabel: '清空'
                },
                "applyClass": 'blue',
                "dateLimit": {
                    "days": 2000 // 时间的最大选择范围。
                },

                ranges: {
                    '今日': [moment(), moment()],
                    '昨日': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                    '最近7日': [moment().subtract(6, 'days'), moment()],
                    // '这周': [moment().startOf('week'), moment().endOf('week')],
                    '最近30日': [moment().subtract(29, 'days'), moment()],
                    '近三个月': [moment().subtract(3, 'month'), moment()],
                    '最近1年': [moment().subtract(365, 'days'), moment()],
                }
            };
            if (obj) {
                for (var key in obj) {
                    var val = obj[key];
                    defaults[key] = val;
                }
            }
            $(pickerSelect).daterangepicker(defaults)
        };
        // 日历汉化
        $.datepicker = function (pickerSelect, obj) {
            var defaults = {
                "locale": {
                    daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
                    monthNames: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
                },
                startDate: moment().startOf('day'),
                format: "YYYY-MM-DD", // 日期格式
                // endDate: new Date(),
                "applyClass": 'btn-blue',
                singleDatePicker: true, //设置成单日历
            };
            if (obj) {
                for (var key in obj) {
                    var val = obj[key];
                    defaults[key] = val;
                }
            }
            $(pickerSelect).daterangepicker(defaults, function (a, b, c) {
                console.log(a, b, c)
            });
        };
    },
    fn_numFormatter: function (s, n) {
        if (!s) {
            s = "0"
        }
        ;
        n = n >= 0 && n <= 20 ? n : 2;
        s = parseFloat((s + '').replace(/[^\d\.-]/g, '')).toFixed(n) + '';
        var l = s.split('.')[0].split('').reverse(),
            r = s.split('.')[1],
            b = r ? '.' + r : '';
        var t = '';
        for (var i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? ',' : '');
        }
        return t.split('').reverse().join('') + b;
    },
    fn_getParameter: function (paraStr, url) {
        var result = "";
        //获取URL中全部参数列表数据   
        var str = "&" + url.split("?")[1];
        var paraName = paraStr + "=";
        //判断要获取的参数是否存在   
        if (str.indexOf("&" + paraName) != -1) {
            //如果要获取的参数到结尾是否还包含“&”   
            if (str.substring(str.indexOf(paraName), str.length).indexOf("&") != -1) {
                //得到要获取的参数到结尾的字符串   
                var TmpStr = str.substring(str.indexOf(paraName), str.length);
                //截取从参数开始到最近的“&”出现位置间的字符   
                result = TmpStr.substr(TmpStr.indexOf(paraName), TmpStr.indexOf("&") - TmpStr.indexOf(paraName));
            } else {
                result = str.substring(str.indexOf(paraName), str.length);
            }
        } else {
            result = "";
        }
        return (result.replace("&", "").split("=")[1]);
        //调用方法：var 变量名 = getParameter("要获取的参数名", URL地址)    
    },
    fn_layertips: function () {
        $("body").on("mouseover", ".has-error .scm-input-wrap.control-label input,.has-error .scm-input-wrap.control-label textarea", function () {
            layer.tips(public.fn_replace($(this).next("label.error").html()), $(this), {tips: [2, '#f97979'], time: 0});
        })
        $("body").on("mouseout", ".has-error .scm-input-wrap input,.has-error .scm-input-wrap textarea,.city-picker-validate .has-error .selector-name", function () {
            layer.close(layer.tips())
        })
        $("body").on("mouseover", ".city-picker-validate .has-error .selector-name", function () {
            layer.tips(public.fn_replace($(this).siblings("label.selector-item-label").html()), $(this), {
                tips: [2, '#f97979'],
                time: 0
            });
        })
        $('body').on('click', '.scm-input-wrap:not(.disabled,.alwaysDisabled) .icon-calendar', function (e) {
            if ($(this).siblings('input.datepicker').length) {
                $(this).siblings('input.datepicker').click();
            }
        })
    },
    fn_citypicker: function () {
        $('body').on('click', '.selector-item:not(.disabled)', function () {
            if ($(this).find('.selector-name').hasClass('forbid')) {
                return;
            }
            $(this).addClass('on').find('.selector-list').removeClass('hide');
        });
        $('.city-picker-selector').on('click', '.province .selector-list li', function () {
            var that = this;
            setTimeout(function () {
                $(that).parents('.city-picker-selector').find('.district .selector-name').addClass('df-color forbid')
            }, 0)

        })
    },
    fn_clone: function (obj) {
        var dataType = function (o) {
            var s = Object.prototype.toString.call(o);
            return s.match(/\[object (.*?)\]/)[1].toLowerCase();
        };
        var result = {}, objClass = dataType(obj);
        if (objClass === "object") {
            result = {};
        } else if (objClass === "array") {
            result = [];
        } else {
            return obj;
        }
        // 遍历obj对象的每一个属性
        for (var key in obj) {
            var val = obj[key];
            if (dataType(val) === "object") {
                result[key] = arguments.callee(val);
            } else if (dataType(val) === "array") {
                result[key] = arguments.callee(val);
            } else {
                result[key] = obj[key];
            }
        }
        return result;
    },
    fn_checkout: function () {
        $('body').on('click', '.selectionBox .mycheckbox', function () {
            if (!$(this).parents('.disabled,.alwaysDisabled').length) {
                if ($(this).hasClass('active')) {
                    $(this).removeClass('active')
                } else {
                    $(this).addClass('active')
                }
            }
        });
        $('body').on('click', '.selectionBox .mycheckradio', function () {
            if (!$(this).parents('.disabled,.alwaysDisabled').length) {
                $(this).closest('.selectionBox').find('.mycheckradio').removeClass('active');
                $(this).addClass('active');
            }
        })
    },
    fn_selectorDefault: function (obj, val) {
        var objUl = $(obj).parent().next('ul.select');
        objUl.find('li').each(function (index, item) {
            var dataValue = $(this).data('value'),
                text = $(this).text();
            if (val == dataValue || val == text) {
                $(obj).val(text);
                $(obj).data("value", dataValue);
            }
        })
    },
    fn_replace: function (val) {
        return val ? val.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;").replace(/'/g, "&#39;") : "";
    },
    fn_viewImgCont: function () {
        var viewImgCont = $('.viewImgCont');
        if (viewImgCont.length) {
            if (viewImgCont[0].offsetWidth < viewImgCont[0].scrollWidth) {
                viewImgCont.addClass('active')
            } else {
                viewImgCont.removeClass('active')
            }
        }
    },
    fn_copy: function (str) {
        var save = function (e) {
            e.clipboardData.setData('text/plain', str);
            e.preventDefault();
        }
        document.addEventListener('copy', save);
        document.execCommand('copy');
        document.removeEventListener('copy', save);
    },
    iframeBridge: function (obj) {
        var strOpen = obj.open && obj.open.length ? obj.open.join(',') : ",,",
            strClose = obj.close || "",
            strReplace = obj.replace && obj.replace.length ? obj.replace.join(',') : "";
        //框架同一域名下的bridge.html
        var iframeBridge_src = document.referrer.substring(0, document.referrer.lastIndexOf("\/") + 1);//需要修改
        if (typeof (exec_obj) == 'undefined') {
            exec_obj = document.createElement('iframe');
            exec_obj.name = 'tmp_frame';
            exec_obj.src = iframeBridge_src + 'bridge.html?' + Math.random() + "#" + strOpen + "~" + strClose + "~" + strReplace;
            exec_obj.style.display = 'none';
            document.body.appendChild(exec_obj);
        } else {
            exec_obj.src = iframeBridge_src + 'bridge.html?' + Math.random() + "#" + strOpen + "~" + strClose + "~" + strReplace;
        }
    },
    init: function (a) {
        this.fn_dropdown();
        this.fn_sideBar();
        this.fn_windowTab();
        this.fn_navTab();
        this.fn_sideBarHeight();
        this.fn_resize();
        this.fn_select();
        this.fn_inputClose();
        this.fn_tableselect();
        this.fn_chinese();
        this.fn_saveData();
        this.fn_layertips();
        this.fn_citypicker();
        this.fn_checkout();
        this.fn_viewImgCont();
    }
};

$(function (a) {
    public.init(a);
})

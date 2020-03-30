require(["public", "bootstrap-table-CN", "layer", "ztreecore", "ztreeexcheck"],
    function () {
        layer.config({
            path: base + '/static/css/common/'
        });

        function filter(node) {
            //return (node.level == 2 && node.checked == true);
            return (node.checked == true && node.getCheckStatus().half == false)
        }

        var PAGE_406_W01_index = parent.layer.getFrameIndex(window.name);
        var jquerySelectorId = $("#406_W01_jquerySelectorId_hidden").val();
        $(function () {
            //页面
            var page = {
                init: function () {
                    //初始化树
                    this.ztree();
                    this.initTree();
                    //确定按钮绑定事件
                    this.bindChoose();
                    //查询按钮绑定事件
                    this.bindSearch();
                    //清除按钮绑定事件
                    this.bindClear();
                },
                bindSearch: function () {
                    $('#406_W01_search_btn').on('click', function () {
                        var data = {};
                        var code = $('#406_W01_code_input').val().trim();
                        if (code) {
                            data.code = code;
                        }
                        page.initTree(data);
                    })
                },
                bindClear: function () {
                    $('#406_W01_clear_btn').on('click', function () {
                        $('#406_W01_code_input').val("");
                    })
                },
                bindChoose: function () {
                    $('#406_W01_choose_btn').on('click', function () {
                        //存放权限树勾选的结果(数组，传给父页面的时候转化为json)
                        var leArray = new Array();

                        var treeObj = $.fn.zTree.getZTreeObj("purchasingUnitTreTree");
                        // var nodes =  treeObj.getCheckedNodes();
                        //过滤节点为选中的节点 不包含父节点
                        var nodes = treeObj.getNodesByFilter(filter);
                        //把选中的值转化为json传给父页面，父页面的变量是父页面的全局变量
                        if (nodes != null && nodes.length > 0) {
                            //单选 非数组
                            parent.$('#' + jquerySelectorId).trigger("initData", nodes[0]);
                            parent.layer.close(PAGE_406_W01_index);
                        } else {
                            commonUtil.commonErrorTips("请选择采购单位", null);
                            return false;
                        }
                    })
                },

                initTree: function () {
                    var zNodes = '';
                    $.ajax({
                        url: base + "/purchasingUnit/findPurchasingUnitTree",
                        data: {},
                        type: "post",
                        async: false,
                        contentType: "application/json",
                        success: function (data) {
                            zNodes = data;
                        },
                        dataType: "json"
                    });

                    $.fn.zTree.init($("#purchasingUnitTreTree"), setting, zNodes);
                    //根据权限节点勾选，给上层节点打勾

                    //触发初始化打勾
                    var zTreeObj = $.fn.zTree.getZTreeObj("purchasingUnitTreTree");
                    parent.$('#' + jquerySelectorId).trigger("initCheck", zTreeObj);
                },

                ztree: function () {
                    setting = {
                        treeId: "purchasingUnitTreTree",
                        view: {
                            selectedMulti: false,
                            showLine: false,
                            showIcon: false,
                            fontCss: getFontCss
                        },
                        check: {
                            enable: true,
                            chkStyle: zTreeChkStyle(),//单选框 多选框
                            radioType: "all"
                        },
                        callback: {
                            //模块节点展开时，下面挂的节点全部展开
                            onExpand: zTreeOnExpand
                        }

                    };

                    function zTreeChkStyle() {
                        return "checkbox";
                    }

                    function zTreeOnExpand(event, treeId, treeNode) {
                        //模块节点level是1（根是0）
                        if (treeNode.level == 0) {
                            var zTree = $.fn.zTree.getZTreeObj("purchasingUnitTreTree");
                            zTree.expandNode(treeNode, true, true, true);
                        }
                    }

                    function focusKey(e) {
                        if (key.hasClass("empty")) {
                            key.removeClass("empty");
                        }
                    }

                    function blurKey(e) {
                        if (key.get(0).value === "") {
                            key.addClass("empty");
                        }
                    }

                    function getFontCss(treeId, treeNode) {
                        return (!!treeNode.highlight) ? {
                            color: "#23527c",
                            "font-weight": "bold"
                        } : {
                            color: "#333",
                            "font-weight": "normal"
                        };
                    }

                }

            };
            page.init();
        })
    }
);


require.config({
    paths: {
        "sockjs": base + "/static/js/common/sockjs",
        "scmmessage": base + "/static/js/common/scmmessage",
        "jquery": base + "/static/js/common/jquery-3.2.1.min",
        "bootstrap": base + "/static/js/common/bootstrap.min",
        "bootstrap-table": base + "/static/js/common/bootstrap-table",
        "bootstrap-table-CN": [base + '/static/js/common/bootstrap-table-zh-CN'],
        "moment": base + "/static/js/common/moment.min",
        "daterangepicker": base + "/static/js/common/daterangepicker",
        "calendarInit": [base + '/static/js/common/calendar'],
        "layer": base + "/static/js/common/layer.min",
        "public": base + "/static/js/common/public",
        "i18n": base + "/static/js/common/jquery.i18n.properties.min",
        "colResizable": [base + '/static/js/ued/colResizable-1.6'],
        "jquery-validate": [base + '/static/js/common/jquery-validate'],
        "ztreecore": [base + '/static/js/ztree/jquery.ztree.core.min'],
        "ztreeexcheck": [base + '/static/js/ztree/jquery.ztree.excheck.min'],
        "validator": [base + '/static/js/common/validator'],
        "fileinput": [base + '/static/js/common/fileinput'],
        "fileinput-zh":[base + '/static/js/common/fileinput-zh'],
    },
    shim: {
        "jquery": {
            exports: ['jQuery', '$']
        },
        "jquery-validate": {
            deps: ["jquery"]
        },
        "validator": {
            deps: ["jquery", "jquery-validate"]
        },
        "calendarInit": {
            deps: ["jquery"]
        },
        "bootstrap": {
            deps: ["jquery"]
        },
        "fileinput": {
            deps: ["jquery"]
        },
        "fileinput-zh": {
            deps: ["jquery","fileinput"]
        },
        "bootstrap-table": {
            deps: ["jquery", "bootstrap"]
        },
        "moment": {
            deps: ["jquery"],
        },
        "bootstrap-table-CN": {
            deps: ["jquery", "bootstrap-table"]
        },
        "daterangepicker": {
            deps: ["jquery", "moment"],
            exports: "daterangepicker"
        },
        "layer": {
            deps: ["jquery"],
            exports: "layer"
        },
        "ztreecore": {
            deps: ["jquery"]
        },
        "ztreeexcheck": {
            deps: ["jquery", "ztreecore"]
        },
        "asDatepicker": {
            deps: ["jquery"],
        },
        "public": {
            deps: ["jquery", "bootstrap-table", "moment", "daterangepicker"]
        },
        "i18n": {
            deps: ["jquery"],
        },
        "sockjs": {
            deps: ["jquery"],
            exports: "sockjs"
        },
        "scmmessage": {
            deps: ["jquery", "sockjs"],
            exports: "scmmessage"
        }
    }
});

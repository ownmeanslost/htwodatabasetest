<#assign base=request.contextPath />
<script>
    var base = "${base!''}";
    var locale = "zh";
</script>

<#--CSS文件-->
<link rel="stylesheet" href="${base}/static/css/common/bootstrap.css">
<link rel="stylesheet" href="${base}/static/css/common/bootstrap-table.css">
<link rel="stylesheet" href="${base}/static/css/common/iconfont.min.css">
<link rel="stylesheet" href="${base}/static/css/common/scmstrap.css">
<link rel="stylesheet" href="${base}/static/css/common/detail.css">
<link rel="stylesheet" href="${base}/static/css/common/toastr.min.css">
<link rel="stylesheet" href="${base}/static/css/common/fileinput.css">

<#--JS文件-->
<script src="${base}/static/js/common/require.js"></script>
<script src="${base}/static/js/common/require.config.js"></script>
<script src="${base}/static/js/common/commonValidate.js"></script>

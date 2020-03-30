<!DOCTYPE html>
<html>
<head>
    <#include "common/common.ftl">
    <meta charset="utf-8">
    <title>胖丁2号</title>
    <style type="text/css">
        .transferManage-layer-tips{
            text-align: left;
            font-size: 12px;
            color: #333;
            padding: 0 40px;
        }
        .transferManage-layer-over-auto{
            overflow: auto;
        }
    </style>
</head>
<body>

<!-- 附件上传弹窗 -->
<div class="layer-transferManager">
    <div class="layer-transferManager-cont" >
        <input type="file" class="txt_file" name="files" multiple >
    </div>
</div>
<span id ="result"></span>


</body>
<#--引入的本业务相关的文件--->
<script src="${base}/static/js/transferManagerAudit.js"></script>
<script src="${base}/static/js/common/commonUtil.js"></script>
</html>
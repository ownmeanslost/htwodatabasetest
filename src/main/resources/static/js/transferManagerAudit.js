require(["public", "bootstrap-table-CN",  "layer","fileinput-zh"], function() {
    layer.config({
        path: base + '/static/css/common/'
    });
    $(function() {
        var page = {
            init: function() {

                this.file();
            },
            file:function(){
                $('.txt_file').fileinput({
                    language: 'zh',
                    uploadUrl: base+"/uploadFile",
                    allowedFileExtensions: ['xlsx','xls'], //接收的文件后缀
                    dropZoneEnabled: true, //是否显示拖拽区域
                    //showUpload: true, //是否显示上传按钮
                    // showCaption: false, //是否显示标题
                    browseClass: "btn btn-blue", //按钮样式
                    //minImageWidth: 50, //图片的最小宽度
                    //minImageHeight: 50,//图片的最小高度
                    //maxImageWidth: 1000,//图片的最大宽度
                    //maxImageHeight: 1000,//图片的最大高度
                    maxFileSize: 102400,//单位为kb，如果为0表示不限制文件大小
                    minFileCount: 1,
                    maxFileCount: 1 //表示允许同时上传的最大文件个数
                    //enctype: 'multipart/form-data',
                    //validateInitialCount: true,
                    //previewFileIcon: "",
                   //msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！"
                }).on("fileuploaded", function(event, data,previewId) {
                    //成功回调
                    if(data.response) {
                       $("#result").text(data.response.text)
                    }

                })
            }
        };

        page.init();
    })

});

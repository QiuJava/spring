/*
* 黑名单处理
* */
angular.module('inspinia',['angularFileUpload']).controller('twiceBlackHandleCtrl',function($scope,$rootScope,$state,$filter,$log,$http,$stateParams,$compile,$uibModal,i18nService,SweetAlert,FileUploader,$location){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.info = {};
    $scope.record = {};
    $scope.real = false;
    $scope.recordDetail = {};

    $scope.uploaderImgListMax = 10;
    //批量上传
    var uploaderImgList = $scope.uploaderImgList = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: $scope.uploaderImgListMax,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，限制最多上传个数
    uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length <=10;
        }
    });
    //过滤格式
    uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            if ('|jpg|png|jpeg|'.indexOf(type)==-1){
                $scope.notice("仅支持jpg、png上传");
            }
            return '|jpg|png|jpeg|'.indexOf(type) !== -1;
        }
    });
    //过滤图片大小
    uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>10*1024*1024){
                $scope.notice("上传图片不能大于10M");
            }
            return item.size < 10*1024*1024 ;
        }
    });
    uploaderImgList.onBeforeUploadItem = function(fileItem) {// 上传前的回调函数，在里面执行提交
        $scope.completeAllImgs = "uploading";
        console.log("图片开始上传[onBeforeUploadItem]");
        console.log("queue.length["+this.queue.length+"];uploaderImgListMax["+$scope.uploaderImgListMax+"]");
    };
    $scope.tall = "";
    uploaderImgList.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            var itemIndex = this.getIndexOfItem(fileItem);
            $("#imageUL li").eq(itemIndex).attr("yun_name",response.url);
            console.log("图片上传完成一项[onSuccessItem]" + response.url);
        }
    };

    $scope.completeAllImgs = "start";
    uploaderImgList.onBeforeUpload = function(){
        console.log("图片开始上传[onBeforeUpload]");
        $scope.completeAllImgs = "uploading";
    };
    uploaderImgList.onCompleteAll = function(){
        console.log("所有图片上传完成[onCompleteAll]");
        $scope.completeAllImgs = "end";
    };
    $scope.submitting = false;
    $scope.submit = function () {
        if ($scope.submitting) {
            return;
        }
        if (isBlank($scope.info.replyRemark)) {
            $scope.notice("回复内容不能为空!");
            return;
        }
        if (!isBlank($scope.info.replyRemark)) {
            if ($scope.info.replyRemark.length > 300) {
                $scope.notice("回复内容不能超过300个字。")
                return;
            }
        }
        if("uploading"===$scope.completeAllImgs){
            $scope.notice("还有图片未上传完成,请稍候再试!");
            return;
        }

        $scope.commitData();
    }

    $scope.commitData = function () {
        $scope.tall = "";
        $("#imageUL li").each(function () {
            var yunName = $(this).attr("yun_name");
            if(yunName==="undefined" || typeof (yunName) ==="undefined"){
            }else{
                $scope.tall+= yunName+",";
            }
        });
       var orderNo= $stateParams.orderNo;
       var replyFilesName = $scope.tall.substring(0, $scope.tall.length - 1);
       var replyRemark;
            if ( $scope.info.replyRemark == undefined){
                replyRemark = null;
            }else {
                replyRemark= $scope.info.replyRemark
            }
        /*var data={orderNo:orderNo,replyFilesName:replyFilesName,replyRemark:replyRemark}*/
        $http.post('riskHandle/handle', 'orderNo=' + orderNo + '&&replyFilesName=' +replyFilesName +"&&replyRemark="+replyRemark,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(
            function (data) {
                if (data.header.succeed) {
                    $scope.notice('回复成功');
                    $location.url('/riskControlManagement/riskHandle/blackDataQuery');
                }else {
                    $scope.notice('回复失败');
                }
            }
        );
    }
    //参数非空判断
    isBlank = function (param) {
        if(param=="" || param==null ){
            return true;
        }else{
            return false;
        }
    }
})
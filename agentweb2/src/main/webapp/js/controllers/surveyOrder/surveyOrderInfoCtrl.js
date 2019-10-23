/**
 * 调单详情
 */
angular.module('inspinia',['angularFileUpload']).controller('surveyOrderInfoCtrl',function($scope,$state,$http,$stateParams,FileUploader,i18nService){

    i18nService.setCurrentLang('zh-cn');
    $scope.info = {};
    $scope.record = {};
    $scope.replyResult=initData["ORDER_REPLY_RESULT"];
    $scope.transStatuses=initData["TRANS_STATUS"];
    $scope.orderServices=initData["ORDER_SERVICE_CODE"];
    $scope.dealStatuses=[{text:"未处理",value:'0'},{text:"已处理",value:'1'},{text:"已处理",value:'2'},{text:"已处理",value:'3'},
        {text:"已处理",value:'4'},{text:"已处理",value:'5'},{text:"已处理",value:'6'},{text:"已处理",value:'7'},
        {text:"已回退",value:'8'}];
    $scope.replyStatuses=[{text:"未回复",value:'0'},{text:"未回复（下级已提交）",value:'1'},{text:"已回复",value:'2'},{text:"逾期未回复",value:'3'},
        {text:"逾期未回复（下级已提交）",value:'4'},{text:"逾期已回复",value:'5'}];
    $scope.orderInfo = function () {
        $http.get('surveyOrder/info?id='+$stateParams.id).success(
            function (data) {
                $scope.info=data.order;
        });
    }

    //查询回复
    $scope.getRecord = function () {
        $http.get('reply/getRecord?orderNo='+$stateParams.orderNo).success(
            function (data) {
                $scope.record=data.record;
                $scope.replyList=data.replyList;
                if($scope.replyList!="" && $scope.replyList!=null){
                    if($scope.replyList.length>0){
                        $scope.replyTitle="回复记录,"+$scope.replyList.length+"条";
                    }
                }
            }
        );
    }
    $scope.getRecord();


    //上传,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload/fileUploadAll.do',
        queueLimit: 6,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        autoUpload:false,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 6;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };


    /**
     *下载文件
     */
    $scope.updateFile=function (name) {
        location.href="upload/updateFile?name="+name;
    };


    $scope.openReplyModal = function(){
        $('#replyModal').modal('show');
    };
    $scope.closeDeductModal = function(){
        $('#replyModal').modal('hide');
    };

    //参数非空判断
    isBlank = function (param) {
        if(param=="" || param==null ){
            return true;
        }else{
            return false;
        }
    }

});


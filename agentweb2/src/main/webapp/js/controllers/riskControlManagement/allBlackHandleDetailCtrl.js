/*
* 黑名单处理
* */
angular.module('inspinia',['angularFileUpload']).controller('allBlackHandleDetailCtr',function($scope,$rootScope,$state,$filter,$log,$http,$stateParams,$compile,$uibModal,i18nService,SweetAlert,FileUploader,$location){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文blackHandleDetailCtrl
    $scope.info = {};
    $scope.record = {};
    $scope.dealReplyRecords={};


    $http.get('riskHandle/newestanswer?orderNo='+$stateParams.orderNo)
        .success(function(data){
            if(!data){
                return;
            }
            if (isBlank(data)) {
                $scope.notice("触发风控规则信息失败");
                return;
            }
            $scope.record=data
        })
    $http.get('riskHandle/handleDetail?orderNo='+$stateParams.orderNo)
        .success(function(result){
            $scope.dealRecord = result.dealRecord;
            $scope.replyRecord = result.replyRecord;
            $scope.dealReplyRecords = result.dealReplyRecords;
            $scope.deal = result.deal;
        })

    $scope.openReplyModal = function(){
        $('#replyModal').modal('show');
    };
    $scope.closeReplyModal = function(){
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
})
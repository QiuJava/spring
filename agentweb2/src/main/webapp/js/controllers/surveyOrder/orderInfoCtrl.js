/**
 * 交易订单详情
 */
angular.module('inspinia',['angularFileUpload']).controller('orderInfoCtrl',function($scope,$state,$http,$stateParams,FileUploader,i18nService){

    i18nService.setCurrentLang('zh-cn');

    $scope.info={};
    $scope.transTypeSelect = [{text:"快捷",value:'quickPay'}];
    $scope.transStatusSelect = [{text:"初始化",value:'0'},{text:"交易中",value:'1'},{text:"交易成功",value:'2'},{text:"交易失败",value:'3'},{text:"未知",value:'4'}];
    $scope.cardTypeSelect = [{text:"借记卡",value:'DEBIT'},{text:"信用卡",value:'CREDIT'},{text:"零钱",value:'CFT'}];
    $scope.show=true;
    $scope.query = function () {
        var orderServiceCode = $stateParams.orderServiceCode;
        if(orderServiceCode == "3"){
            $scope.show = false;
            $http.get('surveyOrder/selectTradeOrderDetail?orderNo='+$stateParams.orderNo)
                .success(function(data) {
                    if(data.status){
                        $scope.info = data.info;
                    }else{
                        $scope.notice(data.msg);
                    }
                });
        }else{
            $http.get('transInfoAction/queryInfoDetailForSurveyOrder?id='+$stateParams.orderNo)
                .success(function(largeLoad) {
                    if(!largeLoad.bols){
                        $scope.notice(largeLoad.msg);
                    }else{
                        $scope.infoDetail=largeLoad.tt;
                        $scope.pcb=largeLoad.pcb;
                        $scope.pcb1=largeLoad.pcb1;
                        $scope.aa1=largeLoad.aa1;
                        $scope.aa2=largeLoad.aa2;
                    }
                });
        }
    }
    $scope.query();

});


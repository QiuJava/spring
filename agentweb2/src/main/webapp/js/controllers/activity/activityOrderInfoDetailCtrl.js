angular.module('inspinia').controller('activityOrderInfoDetailCtrl',function($scope,$http,$state,$stateParams,SweetAlert,$compile,$uibModal,i18nService){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.loadImg = false;
    $scope.infoDetail={};
    $scope.total = {};
    $scope.settlementMethods=[{text:'T1',value:'1'},{text:'T0',value:'0'}];
    $scope.couponCodes= [{text:'购买鼓励金',value:'6'}];

    $http.post("activityOrder/actOrderInfo","id="+$stateParams.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
        console.log(data)
        $scope.infoDetail=data.infoDetail;
        $scope.couponInfo=data.couponInfo;
        $scope.settleInfo=data.settleInfo;
    })

})









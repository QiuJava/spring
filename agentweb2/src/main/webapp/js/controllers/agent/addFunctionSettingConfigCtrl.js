angular.module('inspinia').controller("addFunctionSettingConfigCtrl", function ($scope, $location, $rootScope, $http, $state, $stateParams, i18nService, SweetAlert, $uibModal) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.functionNumber = $stateParams.functionNumber;
    if (!$scope.functionNumber){
        $location.path("/agent/functionSetting");
        return;
    }
    $scope.agentList = [];
    //代理商
    $http.post("agentInfo/selectDirectChildren")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });

    // $scope.getAgent = function(){
    //     $http({
    //         url: "agentFunction/findAgentInfoByAgentNo?agentNo="+$scope.agentNo
    //     }).success(function (data) {
    //         $scope.notice(data.message);
    //         if (!data.success){
    //             $scope.notice(data.message);
    //         } else{
    //             $scope.info.agentName = data.data.agentName;
    //         }
    //     });
    // };

    $scope.commit = function () {
        if(!$scope.agentNo){
            $scope.notice("请选择二级代理商");
            return;
        }
        $http({
            url: "agentFunction/insertSubAgentFunctionConfig?agentNo="+$scope.agentNo
                + "&functionNumber=" + $scope.functionNumber
        }).success(function (data) {
            debugger;
            if (!data.success){
                $scope.notice(data.message);
            }else{
                $scope.notice("添加成功");
            }
        });
    };
});
angular.module('inspinia',['uiSwitch']).controller("functionSettingCtrl", function($scope,$rootScope, $http, $state, $stateParams,i18nService,SweetAlert, $uibModal) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.columnDefs = [
        {field: 'functionNumber',displayName: '功能编号',width: 120,pinnable: false,sortable: false},
        {field: 'functionName',displayName: '功能名称',width: 400,pinnable: false,sortable: false},
        {field: 'functionValue',displayName: '开启状态',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
            '<switch class="switch-s switch-boss" ng-model="row.entity.functionValue" ng-change="grid.appScope.switchAgentFunction(row)" /></span>'
        },
        {field: 'action',displayName: '设置代理商',width: 180,pinnable: false,sortable: false,editable:true,
            cellTemplate: "<span ng-show='row.entity.functionValue'><a  ui-sref='agent.functionSettingConfig({functionNumber:row.entity.functionNumber})'>设置</a></span>"
        }
    ];
	// $scope.agentGrid = {
	// 	data : "agentData",
     //    enableHorizontalScrollbar: true,        //横向滚动条
     //    enableVerticalScrollbar : true,  		//纵向滚动条
	// 	rowHeight:40,
     //    columnDefs: $scope.columnDefs
	// };
    $scope.agentGrid = {
        data : "agentData",
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        rowHeight:40,
        columnDefs: $scope.columnDefs
    };

    $scope.query = function () {
        $http({
            url: 'agentFunction/listAgentFunctionRule'
        }).success(function (data) {
            if(data.success){
                $scope.agentData = data.data;
            }else{
                $scope.notice(data.message);
            }
        }).error(function () {

        });
    };
    $scope.query();

	$scope.switchAgentFunction = function(row){
        var switchText = row.entity.functionValue ? "确定开启" : "确定关闭";
        SweetAlert.swal({
            title: switchText,
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true
        },function (isConfirm) {
            if (isConfirm) {
                var functionValue = row.entity.functionValue ? 1 : 0;
                var functionNumber = row.entity.functionNumber;
                $http({
                    url: 'agentFunction/switchAgentFunction?functionValue=' + functionValue + "&functionNumber=" + functionNumber,
                    method: 'POST'
                }).success(function (data) {
                    if(!data.success){
                        $scope.notice(data.message);
                        row.entity.functionValue = !row.entity.functionValue;
                    }else {
                        $scope.query();
                    }
                }).error(function (data) {
                    $scope.notice("服务器异常,更新状态失败.");
                    row.entity.functionValue = !row.entity.functionValue;
                })
            } else {
                row.entity.functionValue = !row.entity.functionValue;
            }
        });
    }
});
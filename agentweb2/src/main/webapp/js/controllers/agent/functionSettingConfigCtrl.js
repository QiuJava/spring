angular.module('inspinia').controller("functionSettingConfigCtrl", function ($scope, $location, $rootScope, $http, $state, $stateParams, i18nService, SweetAlert, $uibModal) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.pageNo = 1;
    $scope.pageSize = 10;
    $scope.baseInfo = {};
    $scope.functionNumber = $stateParams.functionNumber;
    if (!$scope.functionNumber){
        $location.path("/agent/functionSetting");
        return;
    }
    $scope.columnDefs = [
        {field: 'agentNo', displayName: '代理商编号', pinnable: false, sortable: false},
        {field: 'agentName', displayName: '代理商名称', pinnable: false, sortable: false},
        {
            field: 'action', displayName: '设置代理商', pinnable: false, sortable: false, editable: true,
            cellTemplate: "<span><a ng-click='grid.appScope.deleteAgentFunction(row)'>删除</a></span>"
        }
    ];
    $scope.agentGrid = {
        data: "agentData",
        paginationPageSize: 10,                  //分页数量
        paginationPageSizes: [10, 20, 50, 100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        rowHeight: 40,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.pageNo = newPage;
                $scope.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function () {
        $http({
            url: "agentFunction/listAgentFunctionConfig?pageNo=" + $scope.pageNo
                    +"&pageSize="+$scope.pageSize + "&functionNumber=" + $scope.functionNumber,
            method: 'POST',
            data: $scope.baseInfo
        }).success(function (data) {
            if(data.success){
                $scope.agentData = data.data;
                $scope.agentGrid.totalItems = data.count;
            }else{
                $scope.notice(data.message);
            }
        })
    };

    $scope.query();

    $scope.deleteAgentFunction = function (row) {
        SweetAlert.swal({
            title: "确认删除",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true
        },function (isConfirm) {
            if (isConfirm) {
                $http({
                    url: 'agentFunction/deleteSubAgentFunctionConfig?functionNumber=' + $scope.functionNumber,
                    method: 'POST',
                    data: [row.entity.agentNo]
                }).success(function (data) {
                    if(!data.success){
                        $scope.notice(data.message);
                    }else {
                        $scope.query();
                    }
                }).error(function (data) {
                    $scope.notice("服务器异常,更新状态失败.");
                })
            }
        });
    };
    $scope.batchDelete = function () {
        var selectedRow = $scope.gridApi.selection.getSelectedRows();
        if (!selectedRow || selectedRow.length == 0){
            $scope.notice("请选择需要删除的数据.");
            return;
        }
        var agentNoList = selectedRow.map(function (item) {
            return item.agentNo;
        });
        var text = "您已选择 " + agentNoList.length + " 条数据,确实要删除吗?";
        SweetAlert.swal({
            title: "确认删除",
            type: "warning",
            text: text,
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true
        },function (isConfirm) {
            if (isConfirm) {
                $http({
                    url: 'agentFunction/deleteSubAgentFunctionConfig?functionNumber=' + $scope.functionNumber,
                    method: 'POST',
                    data: agentNoList
                }).success(function (data) {
                    if(!data.success){
                        $scope.notice(data.message);
                    }else {
                        $scope.query();
                    }
                }).error(function (data) {
                    $scope.notice("服务器异常,更新状态失败.");
                })
            }
        });
        console.log("%o", $scope.agentGrid);
    }
});
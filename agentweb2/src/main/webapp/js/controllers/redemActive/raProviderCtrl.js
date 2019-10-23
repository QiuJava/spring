/**
 * 欢乐返商户查询
 */
angular.module('inspinia').controller('raProviderCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
    // ============= start 变量定义 ==================
    // 查询分页
    $scope.page = {
        pageNo : 1,
        pageSize: 10
    };
    // 查询条件
    $scope.info  = {

    };
    $scope.updateServiceCostData = {};
    $scope.providerGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'agentNo',displayName: '所属服务商',pinnable: false,sortable: false},
            {field: 'agentName',displayName: '所属服务商名称',pinnable: false,sortable: false},
            {field: 'shareRate',displayName: '入账比例',pinnable: false,sortable: false,cellTemplate:'<span ng-show="row.entity.shareRate != null">{{row.entity.shareRate}}%</span>'},
            {field: 'oemFee',displayName: '积分兑换成本',pinnable: false,sortable: false,cellTemplate:'<span ng-show="row.entity.oemFee != null">{{row.entity.oemFee}}</span>'},
            {field: 'receiveShareRate',displayName: '商户收款入账比例',pinnable: false,sortable: false,cellTemplate:'<span ng-show="row.entity.receiveShareRate != null">{{row.entity.receiveShareRate}}%</span>'},
            {field: 'repaymentShareRate',displayName: '信用卡还款入账比例',pinnable: false,sortable: false,cellTemplate:'<span ng-show="row.entity.repaymentShareRate != null">{{row.entity.repaymentShareRate}}%</span>'},
            {field: 'action',displayName: '操作',pinnable: false,sortable: false,cellTemplate:'<span class="lh30">' +
            '<a ng-show="row.entity.shareRate != null && row.entity.parentId === $root.entityId" ng-click="grid.appScope.showUpdateServiceCostModal(row.entity)">修改</a>' +
            '</span>'}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.page.pageNo = newPage;
	          	$scope.page.pageSize = pageSize;
	            $scope.listProvider();
	        });
            $scope.listProvider();
        }
	 };
    // ============= end 变量定义 ==================

    // ============= start 事件定义 ==================
    // 查询按钮
    $scope.listProvider = function () {
        $http({
            url: 'redemProviderAction/listProvider?pageNo='+$scope.page.pageNo +"&pageSize=" + $scope.page.pageSize,
            method: 'POST',
            data: $scope.info
        }).success(function (data) {
            $scope.providerGrid.data =data.data;
            $scope.providerGrid.totalItems = data.count;
        });
    };

    // 重置按钮
    $scope.resetForm = function () {
        $scope.info = {};
    };

    $scope.openRedemActive = function () {
        var selectedRows = $scope.gridApi.selection.getSelectedRows();
        if (selectedRows && selectedRows.length === 0){
            $scope.notice("必须选择需要开通的服务商.");
            return;
        }
        var agentNoList = [];
        angular.forEach(selectedRows, function (item) {
            agentNoList.push(item.agentNo);
        });
        $http({
            url: 'redemProviderAction/openRedemActive',
            method: 'POST',
            data: agentNoList
        }).success(function (data) {
            if (data.success){
                $scope.notice("操作成功");
                $scope.listProvider();
            }else{
                $scope.notice(data.message);
            }
        });
    };
    $scope.showUpdateServiceCostModal = function (data) {
        if(data.rate === null || data.singleAmount === null){
            $scope.notice("该服务商未开通超级还款,不能修改.");
            return;
        }
        $scope.updateServiceCostData = {
            agentNo: data.agentNo,
            agentName: data.agentName,
            shareRateStr: data.shareRate,
            oemFeeStr: data.oemFee,
            receiveShareRate:data.receiveShareRate,
            repaymentShareRate:data.repaymentShareRate
        };
        $("#updateServiceCostModal").modal('show');

    };
    $scope.updateServiceCost = function () {
        $http({
            url: 'redemProviderAction/updateServiceCost',
            method: 'POST',
            data: $scope.updateServiceCostData
        }).success(function (data) {
            if (data.success){
                $scope.notice("操作成功");
                $scope.hideAllModel();
                $scope.listProvider();
            }else{
                $scope.notice(data.message);
            }
        });
    };
    $scope.hideAllModel = function () {
        $scope.updateServiceCostData = {};
        $("#updateServiceCostModal").modal('hide');
    };

    // ============= end 事件定义 ==================


    // ============= start 方法定义 ==================
    // ============= end 方法定义 ==================

    // ============= start 初始化 ==================
    // ============= end 初始化 ====================

});



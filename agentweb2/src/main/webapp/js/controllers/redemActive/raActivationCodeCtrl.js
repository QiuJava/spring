/**
 * 激活码查询
 */
angular.module('inspinia').controller('raActivationCodeCtrl',function($scope,$http,$state,$stateParams,i18nService){
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
        agentNo:$scope.$root.entityId
    };
    // 回收激活码
    $scope.recoveryActivation = {
        startId: 0,
        endId: 0
    };
    // 下发激活码
    $scope.divideActivation = {
        agentNo: '',
        startId: 0,
        endId: 0
    };
    // 激活码状态
    $scope.activationCodeStatus = [
        {value: '0', text: '已入库'},
        {value: '1', text: '已分配'},
        {value: '2', text: '已激活'}
    ];
    $scope.agentList = [];
    $scope.agentChildrenList = [];
    $scope.activeCodeUrl = {
        prefix: "",
        url:""
    };
	$scope.activationCodeGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'id',displayName: '激活码编号',pinnable: false,width: 180,sortable: false},
            // {field: 'uuidCode',displayName: '激活码',pinnable: false,width: 280,sortable: false},
            {field: 'agentNo',displayName: '所属服务商',pinnable: false,width: 180,sortable: false},
            // {field: 'agentName',displayName: '所属服务商名称',pinnable: false,width: 180,sortable: false},
            // {field: 'oneAgentNo',displayName: '一级服务商',pinnable: false,width: 180,sortable: false},
            // {field: 'oneAgentName',displayName: '一级服务商名称',pinnable: false,width: 180,sortable: false},
            {field: 'merNo',displayName: '用户编号',pinnable: false,width: 180,sortable: false},
            // {field: 'unifiedMerchantName',displayName: '用户昵称',pinnable: false,width: 180,sortable: false},
            {field: 'status',displayName: '激活码状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.activationCodeStatus)},
            {field: 'activateTime',displayName: '激活时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
            '<div class="lh30">'
            // +'<a class="lh30" ng-click="grid.appScope.liquidation(row.entity)"'
            // +' ng-show="row.entity.status==2 && row.entity.liquidationStatus!=1 && grid.appScope.hasPermit(\'activity.liquidation\') ">清算核算</a>'
            +'<a class="lh30" ng-click="grid.appScope.showActiveCodeUrlModal(row.entity)">激活码</a>'
            +'<a class="lh30" ng-show="row.entity.status == 1 && row.entity.parentId === $root.entityId" ng-click="grid.appScope.recoverySingleActivationCode(row.entity)"> | 回收</a>'
            +'</div>'
            }
        ],
        onRegisterApi: function(gridApi) {                
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.page.pageNo = newPage;
	          	$scope.page.pageSize = pageSize;
	            $scope.listActivationCode();
	        });
            $scope.listActivationCode();
        }
	 };
    // ============= end 变量定义 ==================

    // ============= start 事件定义 ==================
    // 查询按钮
	$scope.listActivationCode = function () {
        $http({
            url: 'redemActivationCodeAction/listActivationCode?pageNo='+$scope.page.pageNo +"&pageSize=" + $scope.page.pageSize,
            method: 'POST',
            data: $scope.info
        }).success(function (data) {
            if (data.success){
                $scope.activationCodeGrid.data =data.data;
                $scope.activationCodeGrid.totalItems = data.count;
            }else{
                $scope.notice(data.message);
            }
        });
    };

	// 重置按钮
	$scope.resetForm = function () {
        $scope.info = {
            agentNo:$scope.$root.entityId
        };
    };

	$scope.showDivideActivationCodeModal = function () {
	    $scope.divideActivation = {
            agentNo: '',
            startId: 0,
            endId: 0,
            expectCount: 0,
            message: '',
            actualCount: 0,
            showActualCount: false
        };
        $("#autocomplete_select1_chosen").css("width","180px");
        $('#divideActivationCodeModal').modal('show');
    };
    $scope.divideActivationChange = function () {
        $scope.divideActivation.expectCount = 0;
        if(!/^\d{12}$/.test($scope.divideActivation.startId + "")){
            $scope.divideActivation.message = "开始激活码编号格式有误.";
            return;
        }
        if(!/^\d{12}$/.test($scope.divideActivation.endId + "")){
            $scope.divideActivation.message = "结束激活码编号格式有误.";
            return;
        }
        if ($scope.divideActivation.endId < $scope.divideActivation.startId){
            $scope.divideActivation.message = "结束激活码不能大于开始激活码编号.";
            return;
        }
        $scope.divideActivation.message = "";
        $scope.divideActivation.expectCount = $scope.divideActivation.endId - $scope.divideActivation.startId + 1;
    };
    $scope.showActiveCodeUrlModal = function (data) {
        $scope.activeCodeUrl.url = $scope.activeCodeUrl.prefix + data.uuidCode;
        $('#activeCodeUrlModal').modal('show');
    };
    $scope.hideAllModel = function () {
        $('#divideActivationCodeModal').modal('hide');
        $("#recoveryActivationCodeModal").modal('hide');
        $('#activeCodeUrlModal').modal('hide');
        $scope.listActivationCode();
    };

	$scope.divideActivationCode = function () {
        $http({
            url: 'redemActivationCodeAction/divideActivationCode?startId='+$scope.divideActivation.startId
                                            + "&endId="+$scope.divideActivation.endId
                                            + "&agentNo=" + $scope.divideActivation.agentNo,
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.notice("操作成功,下发成功 " + data.data + " 条. 失败 " + ($scope.divideActivation.expectCount - data.data) + " 条.");
                // $scope.divideActivation.showActualCount = true;
                // $scope.divideActivation.actualCount = data.data;
                $scope.hideAllModel();
            }else{
                $scope.notice(data.message);
            }
        });

    };
    $scope.showRecoveryActivationCodeModal = function () {
        $scope.recoveryActivation = {
            startId: 0,
            endId: 0,
            message: '',
            actualCount: 0,
            expectCount: 0,
            showActualCount: false
        };
        $("#recoveryActivationCodeModal").modal('show');
    };

    $scope.recoveryActivationChange = function () {
        $scope.recoveryActivation.expectCount = 0;
        if(!/^\d{12}$/.test($scope.recoveryActivation.startId + "")){
            $scope.recoveryActivation.message = "开始激活码编号格式有误.";
            return;
        }
        if(!/^\d{12}$/.test($scope.recoveryActivation.endId + "")){
            $scope.recoveryActivation.message = "结束激活码编号格式有误.";
            return;
        }
        if ($scope.recoveryActivation.endId < $scope.recoveryActivation.startId){
            $scope.recoveryActivation.message = "结束激活码不能大于开始激活码编号.";
            return;
        }
        $scope.recoveryActivation.message = "";
        $scope.recoveryActivation.expectCount = $scope.recoveryActivation.endId - $scope.recoveryActivation.startId + 1;
    };
    $scope.recoveryActivationCode = function () {
        $http({
            url: 'redemActivationCodeAction/recoveryActivation?startId='+$scope.recoveryActivation.startId
            + "&endId="+$scope.recoveryActivation.endId,
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.notice("操作成功,回收成功 " + data.data + " 条. 失败 " + ($scope.recoveryActivation.expectCount - data.data) + " 条.");
                // $scope.recoveryActivation.showActualCount = true;
                // $scope.recoveryActivation.actualCount = data.data;
                $scope.hideAllModel();
            }else{
                $scope.notice(data.message);
            }
        });
    };
    $scope.recoverySingleActivationCode = function (data) {
        $http({
            url: 'redemActivationCodeAction/recoveryActivation?startId='+data.id
            + "&endId="+data.id,
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.notice("操作成功");
                $scope.hideAllModel();
            }else{
                $scope.notice(data.message);
            }
        });
    };
    // ============= end 事件定义 ==================


    // ============= start 方法定义 ==================
    // ============= end 方法定义 ==================

    // ============= start 初始化 ==================

    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + '-' + msg[i].agentName});
            }
        });
    $http.post("agentInfo/selectSelfAndDirectChildren")
        .success(function(msg){
            for(var i=0; i<msg.length; i++){
                if (msg[i].agentNo === $scope.$root.entityId){
                    continue;
                }
                $scope.agentChildrenList.push({value:msg[i].agentNo,text:msg[i].agentNo + '-' +msg[i].agentName});
            }
        });
    $http({
        url: 'redemActivationCodeAction/getPreActivationCodeUrl',
        method: 'POST'
    }).success(function (data) {
        $scope.activeCodeUrl.prefix = data.success && data.data || "";
    })
    // ============= end 初始化 ====================

});



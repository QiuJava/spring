/**
 * 结算订单管理
 */
angular.module('inspinia').controller('settleOrderCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.statusSelect = [{text:"未结算",value:'0'},{text:"结算中",value:'1'},{text:"已结算",value:'2'},{text:"结算失败",value:'3'}];
	$scope.serviceSelect = [{text:"还款",value:'repayPlan'},{text:"提现",value:'withdraw'}];
	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {status:'', service:''};
	$scope.sumAmount = '';

	$scope.columnDefs = [
		{field: 'orderNo',displayName: '订单ID',width: 150,pinnable: false,sortable: false},
		{field: 'merNo',displayName: '用户编号',width: 150,pinnable: false,sortable: false},
		{field: 'nickname',displayName: '昵称',width: 150,pinnable: false,sortable: false},
		{field: 'accName',displayName: '姓名',width: 150,pinnable: false,sortable: false},
		{field: 'mobileNo',displayName: '手机号',width: 150,pinnable: false,sortable: false},
		{field: 'status',displayName: '结算状态',width: 120,pinnable: false,sortable: false,
			cellFilter:"formatDropping:"+ angular.toJson($scope.statusSelect)},
//		{field: 'service',displayName: '订单类型',width: 120,pinnable: false,sortable: false,
//			cellFilter:"formatDropping:"+ angular.toJson($scope.serviceSelect)},
		{field: 'amount',displayName: '出款金额',cellFilter:"currency:''",width: 150,pinnable: false,sortable: false},
		{field: 'bankOrderNo',displayName: '上游订单号',width: 150,pinnable: false,sortable: false},
		{field: 'createTime',displayName: '时间',width: 150,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<a class="lh30" ng-show="row.entity.status==3 && grid.appScope.hasPermit(\'repaySettleOrder.againPayment\')" '
			+ 'ng-click="grid.appScope.againPayment(row.entity.orderNo)">再次出款</a>'}
	];

	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
		columnDefs: $scope.columnDefs,
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
		}
	};

	$scope.query = function () {
		$http({
			url: 'repaySettleOrder/selectSettleOrderByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
			data: $scope.baseInfo,
			method:'POST'
		}).success(function (msg) {
			if (!msg.status){
				$scope.notice(msg.msg);
				return;
			}
			$scope.myGrid.data = msg.page.result;
			$scope.myGrid.totalItems = msg.page.totalCount;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
		});
		$http({
			url: 'repaySettleOrder/countSettleOrderByParam',
			data: $scope.baseInfo,
			method:'POST'
		}).success(function (msg) {
			if (!msg.status){
				$scope.notice(msg.msg);
				return;
			}
			$scope.sumAmount = msg.sumAmount;
		}).error(function (msg) {
			$scope.notice('服务器异常,请稍后再试.');
		});
	};

	// 再次出款
	$scope.againPayment = function (orderNo) {
		SweetAlert.swal({
			title: "确定出款吗？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http({
						url: 'repaySettleOrder/againPayment',
						data: orderNo,
						method:'POST'
					}).success(function (msg) {
						$scope.notice(msg.msg);
						if (msg.status){
							$scope.query();
						}
					}).error(function (msg) {
						$scope.notice('服务器异常,请稍后再试.');
					});
				}
			});
	}

	// 导出
	$scope.exportInfo = function () {
		SweetAlert.swal({
			title: "确定导出吗？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					location.href="repaySettleOrder/exportInfo?baseInfo="+angular.toJson($scope.baseInfo);
				}
			});
	}

	$scope.resetForm = function () {
		$scope.baseInfo = {status:'', service:''};
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});
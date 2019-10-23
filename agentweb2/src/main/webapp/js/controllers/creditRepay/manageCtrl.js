/**
 * 用户管理
 */
angular.module('inspinia').controller('manageCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {agentNo:'',
		sEnterTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
		eEnterTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
		/*sCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
		eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
	};

	$scope.merAccountSelect = [{text:"未开户",value:'0'},{text:"已开户",value:'1'}];
	$scope.agentList = [];
	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setStartEnterTime=function(setTime){
		$scope.baseInfo.sEnterTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
		//$scope.baseInfo.sCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setEndEnterTime=function(setTime){
		$scope.baseInfo.eEnterTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
		//$scope.baseInfo.eCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setStartCreateTime=function(setTime){
		//$scope.baseInfo.sEnterTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
		$scope.baseInfo.sCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setEndCreateTime=function(setTime){
		//$scope.baseInfo.eEnterTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
		$scope.baseInfo.eCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	//代理商下拉列表查询tgh418
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNode,text:msg[i].agentNo+ " " + msg[i].agentName});
            }
        });

	$scope.columnDefs = [
		{field: 'merchantNo',displayName: '用户编号',width: 150,pinnable: false,sortable: false},
		{field: 'nickname',displayName: '昵称',width: 150,pinnable: false,sortable: false},
		{field: 'mobileNo',displayName: '手机号',width: 150,pinnable: false,sortable: false},
		{field: 'userName',displayName: '姓名',width: 150,pinnable: false,sortable: false},
		{field: 'agentName',displayName: '服务商名称',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo',displayName: '服务商编号',width: 150,pinnable: false,sortable: false},
		
		/*{field: 'agentNo1',displayName: '身份',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '上一级编号',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '上一级身份',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '上二级编号',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '上二级身份',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '代理状态',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '商户编号',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '进件状态',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '直属服务商',width: 150,pinnable: false,sortable: false},
		{field: 'agentNo1',displayName: '一级服务商',width: 150,pinnable: false,sortable: false},*/
		
		{field: 'proMerNo',displayName: '收款商户号',width: 150,pinnable: false,sortable: false},
		{field: 'merAccount',displayName: '开户状态',width: 150,pinnable: false,sortable: false,
			cellFilter:"formatDropping:"+ angular.toJson($scope.merAccountSelect)},
		{field: 'enterTime',displayName: '入驻时间',width: 150,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'createTime',displayName: '激活时间',width: 150,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<a class="lh30" ui-sref="creditRepay.userDetail({merchantNo:row.entity.merchantNo})" target="_black">详情</a>'}
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
		if (!($scope.baseInfo.sEnterTime && $scope.baseInfo.eEnterTime)){
			$scope.notice("入驻日期不能为空，请重新选择");
			return
		}
		$http({
			url: 'repayMerchant/selectRepayMerchantByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
	};
	
	
	// 批量入账
	$scope.batchOpenAccount = function () {
		selectedRows = $scope.gridApi.selection.getSelectedRows();
		if(selectedRows==null || selectedRows.length==0){
			$scope.notice('请选择要开户的条目');
			return;
    	}
		validIds = [];
		angular.forEach(selectedRows,function(data){
			if (data.merAccount == '0') {
				validIds[validIds.length] = data.merchantNo;
			}
    	});
		if (validIds == null || validIds.length == 0) {
			$scope.notice('请选择未开户的条目');
			return;
		}
		SweetAlert.swal({
			title: "批量开户",
			text: "选中条目中未开户的数据有 " + validIds.length + " 条，是否确定开户？",
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
						url: 'repayMerchant/batchOpenAccount',
						data: validIds,
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

	$scope.resetForm = function () {
		$scope.baseInfo = {agentNo:'',
			sEnterTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
			eEnterTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			/*sEnterTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
			eEnterTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			sCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
			eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
		};
	}

	$scope.changeAgentNode = function () {
        $scope.disabledMerchantType = !$scope.baseInfo.agentNode;
    };
    
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});
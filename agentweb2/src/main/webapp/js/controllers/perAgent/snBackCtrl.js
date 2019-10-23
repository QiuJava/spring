/**
 * sn回拨记录
 */
angular.module('inspinia',['angularFileUpload']).controller('snBackCtrl',function($scope,$rootScope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {agentNo:''};
	$scope.receiveUserTypeList = [{text:"机构",value:'1'},{text:"大盟主",value:'2'},{text:"盟主",value:'3'}];
	$scope.statusList = [{text:"等待接收",value:'0'},{text:"回拨成功",value:'1'},{text:"拒绝接收",value:'2'},{text:"已取消",value:'3'}];

    $scope.csrfData = $("meta[name=_csrf]").attr('content');
    $scope.loadImg = false;
    $scope.showBatchSendButton = $scope.$root.entityAgentLevel;

    $scope.query = function () {
        $http({
            url: 'perAgent/selectSnBackByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if(msg.status){
                $scope.myGrid.data = msg.page.result;
                $scope.myGrid.totalItems = msg.page.totalCount;
            }else {
                $scope.notice(msg.msg);

            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.query();

	$scope.columnDefs = [
		{field: 'orderNo',displayName: '回拨单号',width: 150,pinnable: false,sortable: false},
        {field: 'backCount',displayName: '回拨数量',width:150},
        {field: 'userCode',displayName: '回拨盟主编号',width:150},
        {field: 'receiveUserCode',displayName: '接收盟主编号',width:150},
        {field: 'receiveUserType',displayName: '接收盟主类型',width:150
        	,cellFilter:"formatDropping:"+ angular.toJson($scope.receiveUserTypeList)
        },
		{field: 'beLongUserCode',displayName: '所属机构编号',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '回拨状态',width: 150,pinnable: false,sortable: false
        	,cellFilter:"formatDropping:"+ angular.toJson($scope.statusList)
        },
        {field: 'createTime',displayName: '回拨日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'lastUpdateTime',displayName: '处理日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 200,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                "<span ><a ng-click='grid.appScope.showDeliver(row)'>SN列表</a></span>"+
				"<span ng-show='row.entity.status == 0 && row.entity.userCodeStatus == 1 ' ><a ng-click='grid.appScope.showReceiveDeliver(row)'> | 接收</a></span>"
				+"<span ng-show='row.entity.status == 0 && row.entity.userCodeStatus == 1 ' ><a ng-click='grid.appScope.refuse(row)'> | 拒绝</a></span>"
				+"<span ng-show='row.entity.status == 0 && row.entity.userCodeStatus == 0 ' ><a ng-click='grid.appScope.cancelBack(row)'> | 取消回拨</a></span>"
//				"<span ><a ng-click='grid.appScope.showReceiveDeliver(row)'> | 接收</a></span>"
//				+"<span ><a ng-click='grid.appScope.refuse(row)'> | 拒绝</a></span>"
//				+"<span ><a ng-click='grid.appScope.cancelBack(row)'> | 取消回拨</a></span>"
		}
	];

	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
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


	$scope.hideAllModel = function () {
        $("#receiveIdModel").modal('hide');
    };
    $scope.hideDevModel = function () {
        $("#expressage").modal('hide');
    };
    
    $scope.resetForm = function () {
		$scope.baseInfo = {agentNo:''};
	}
    
    //SN列表
    $scope.showDeliver=function(row) {
    	$scope.getList(row,"expressage");
    }
    //接收
    $scope.orderNo = "";
    $scope.revecied=function(orderNo) {
    	//确定接收
    	$http({
            url: 'perAgent/updateStatus?orderNo='+orderNo+'&status=1',
            method:'POST'
        }).success(function (msg) {
            $scope.notice(msg.msg);
            $scope.hideAllModel();
            $scope.query();
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    }
    $scope.showReceiveDeliver=function(row) {
    	$scope.getList(row,"receiveIdModel");
    	$scope.orderNo = row.entity.orderNo;
    }

    //拒绝
    $scope.refuse=function(row){
        SweetAlert.swal({
                title: "您正在进行拒绝接收操作，是否继续？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                	$http({
                        url: 'perAgent/updateStatus?orderNo='+row.entity.orderNo+'&status=2',
                        method:'POST'
                    }).success(function (msg) {
                        $scope.notice(msg.msg);
                        $scope.query();
                    }).error(function (msg) {
                        $scope.notice('服务器异常,请稍后再试.');
                    });
                }
            });
    };
    //取消回拨
    $scope.cancelBack=function(row){
    	SweetAlert.swal({
    		title: "您正在进行取消回拨操作，是否继续？",
    		showCancelButton: true,
    		confirmButtonColor: "#DD6B55",
    		confirmButtonText: "确定",
    		cancelButtonText: "取消",
    		closeOnConfirm: true,
    		closeOnCancel: true
    	},
    	function (isConfirm) {
    		if (isConfirm) {
    			$http({
    				url: 'perAgent/updateStatus?orderNo='+row.entity.orderNo+'&status=3',
    				method:'POST'
    			}).success(function (msg) {
    				$scope.notice(msg.msg);
    				$scope.query();
    			}).error(function (msg) {
    				$scope.notice('服务器异常,请稍后再试.');
    			});
    		}
    	});
    };
    $scope.totalCount = 0;
    $scope.getList=function(row,modelId) {
        $http({
            url: 'perAgent/selectSnByOrder?orderNo='+row.entity.orderNo,
            method:'POST'
        }).success(function (msg) {
            if(msg){
                $scope.myDevGrid.data = msg;
                $scope.totalCount = msg.length;
                $("#" + modelId).modal('show');
            }else {
                $scope.notice("查询失败");
            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.myDevGrid = {
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [{field: 'sn',displayName: 'SN号',width: 600,pinnable: false,sortable: false}]
    };
    $scope.showReceiveIdModel=function(row) {
    	$http({
    		url: 'perAgent/selectSnByOrder?orderNo='+row.entity.orderNo,
    		method:'POST'
    	}).success(function (msg) {
    		if(msg){
    			$scope.myDevGrid.data = msg;
    			$scope.totalCount = msg.length;
    			$("#receiveIdModel").modal('show');
    		}else {
    			$scope.notice("查询失败");
    		}
    	}).error(function (msg) {
    		$scope.notice('服务器异常,请稍后再试.');
    	});
    };


    $scope.exportSnBack=function(){
        if($scope.baseInfo.creatTimeStart > $scope.baseInfo.creatTimeEnd || $scope.baseInfo.lastUpdateTimeStart > $scope.baseInfo.lastUpdateTimeEnd){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    location.href="perAgent/exportSnBack?baseInfo="+encodeURIComponent(angular.toJson($scope.baseInfo));
                }
            });
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
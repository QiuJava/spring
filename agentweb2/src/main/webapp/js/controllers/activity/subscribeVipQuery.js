/**
 * 调单查询
 */
angular.module('inspinia').controller('subscribeVipQuery',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$rootScope,$location){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	// 支付方式
    $scope.paymentTypes=[{text:"支付宝",value:'alipay'},{text:"微信",value:'wechat'},{text:"刷卡",value:'byCard'}];

	$scope.info = {
        createTimeStart:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
    };

    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.info.createTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndTime=function(setTime){
        $scope.info.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    $scope.agent = [];
    //代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });

    $scope.query = function () {
        if (!( $scope.info.createTimeStart &&  $scope.info.createTimeEnd )){
            $scope.notice("交易日期不能为空");
            return
        }
        $http({
            url: 'subscribeVip/selectByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.info,
            method:'POST'
        }).success(function (data) {
            if (!data.status){
                $scope.notice(data.msg);
                return;
            }
            $scope.myGrid.data = data.page.result;
            $scope.myGrid.totalItems = data.page.totalCount;
        }).error(function (data) {
            $scope.notice(data.msg);
        });
    };
    $scope.query();

	$scope.columnDefs = [
        {field: 'orderNo',displayName: '业务订单编号',width: 150,pinnable: false,sortable: false},
        {field: 'merchantName',displayName: '商户名称',width: 150,pinnable: false,sortable: false},
        {field: 'merchantNo',displayName: '商户编号',width: 150,pinnable: false,sortable: false},
        {field: 'mobilephone',displayName: '商户手机号',width: 150,pinnable: false,sortable: false},
        {field: 'agentName',displayName: '所属代理商',width: 150,pinnable: false,sortable: false},
        {field: 'name',displayName: '服务名称',width: 150,pinnable: false,sortable: false},
        {field: 'amount',displayName: '交易金额',width: 150,pinnable: false,sortable: false},
        {field: 'time',displayName: '有效期（天）',width: 150,pinnable: false,sortable: false},
        {field: 'paymentType',displayName: '支付方式',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.paymentTypes)},
        {field: 'paymentOrderNo',displayName: '支付订单号',width: 150,pinnable: false,sortable: false},
        {field: 'createTime',displayName: '交易时间',width: 180,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
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


	$scope.resetForm = function () {
		$scope.info = {
            createTimeStart:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
	}

	//导出
    $scope.import=function(){
	    if (!( $scope.info.createTimeStart &&  $scope.info.createTimeEnd )){
            $scope.notice("交易日期不能为空");
            return
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
                    //只能用get请求
                    location.href="subscribeVip/export?order="+encodeURIComponent(angular.toJson($scope.info));
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
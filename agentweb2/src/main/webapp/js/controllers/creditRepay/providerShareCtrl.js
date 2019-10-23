/**
 * 服务商分润订单查询
 */
angular.module('inspinia').controller('providerShareCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
    $scope.agentList = [];
    $scope.info = {
        profitMerNo: $scope.$root.entityId,
        profitType: '',
        bool:'1',
        orderStatus: '',
        completeTimeBegin: moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        completeTimeEnd: moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')
	};
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.info.completeTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndTime=function(setTime){
        $scope.info.completeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    $scope.pageNo = 1;
    $scope.pageSize = 10;
    var defaultSunProfitDate = {
        allShareAmount: '0.00',
        selfShareAmount: '0.00',
        childShareAmount: '0.00'
    };
    $scope.sunProfitDate = defaultSunProfitDate;
    $scope.bools = [{text:'包含',value:'1'},{text:'不包含',value:'0'}];
    $scope.statusSelect=[{text:"全部",value:''},{text:"初始化",value:'0'},{text:"未执行",value:'1'},{text:"还款中",value:'2'},{text:"还款成功",value:'3'},{text:"还款失败",value:'4'},{text:"挂起",value:'5'},{text:"终止",value:'6'}];

    $http.post("agentInfo/selectAllInfo").success(function(msg){
        for(var i=0; i<msg.length; i++){
            $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentName});
        }
    });
    $scope.profitTypeList = [
        {value: "", text: "全部"},
        {value: "1", text: "分期还款"},
        {value: "2", text: "全额还款"},
        {value: "3", text: "保证金"},
        {value: "4", text: "完美还款"}
    ];
    $scope.columnDefs = [
        {field: 'profitNo',displayName: '分润流水号',width: 120,pinnable: false,sortable: false},
        {field: 'profitMerNo',displayName: '服务商编号',width: 120,pinnable: false,sortable: false},
        {field: 'agentName',displayName: '服务商名称',width: 120,pinnable: false,sortable: false},
        {field: 'profitType',displayName: '订单类型',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.profitTypeList)},
        {field: 'shareAmount',displayName: '分润',width: 120,pinnable: false,sortable: false},
        {field: 'toProfitAmount',displayName: '产生分润金额',width: 120,pinnable: false,sortable: false},
        {field: 'repayAmount',displayName: '任务金额',width: 120,pinnable: false,sortable: false},
        {field: 'ensureAmount',displayName: '保证金',width: 120,pinnable: false,sortable: false},
        {field: 'repayFee',displayName: '服务费',width: 120,pinnable: false,sortable: false},
        {field: 'successPayAmount',displayName: '已消费总额',width: 120,pinnable: false,sortable: false},
        {field: 'successRepayAmount',displayName: '已还款总额',width: 120,pinnable: false,sortable: false},
        {field: 'actualPayFee',displayName: '实际消费手续费',width: 180,pinnable: false,sortable: false},
        {field: 'actualWithdrawFee',displayName: '实际还款手续费',width: 180,pinnable: false,sortable: false},
        {field: 'batchNo',displayName: '关联还款订单',width: 200,pinnable: false,sortable: false},
        {field: 'merchantNo',displayName: '订单用户',width: 200,pinnable: false,sortable: false},
        {field: 'orderStatus',displayName: '订单状态',width: 200,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.statusSelect)},
    	// {field: 'orderCreateTime',displayName: '订单时间',width: 180,pinnable: false,sortable: false,
         //    cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'completeTime',displayName: '终态时间',width: 180,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
    ];
    $scope.profitGrid = {
        data : "profitDate",
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        rowHeight:40,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.pageNo = newPage;
                $scope.pageSize = pageSize;
                $scope.listRepayProfitDetail();
            });
        }
    };

    $scope.clear = function () {
        $scope.info = {
            profitMerNo: $scope.$root.entityId,
            profitType: '',
            bool:'1',
            orderStatus: '',
            completeTimeBegin: moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
            completeTimeEnd: moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')
        };
    };
    
    $scope.listRepayProfitDetail = function () {
		if ($scope.info.minShareAmount &&
			$scope.info.maxShareAmount &&
			($scope.info.maxShareAmount - $scope.info.minShareAmount) < 0){
			$scope.notice("最小分润金额不能大于最大分润金额");
			return;
		}
        if(!(  $scope.info.completeTimeBegin &&   $scope.info.completeTimeEnd)){
            $scope.notice("终态日期不能为空");
            return
        }
		$http({
			url: 'repayProfitDetail/listRepayProfitDetail?pageNo=' + $scope.pageNo + '&pageSize=' + $scope.pageSize,
			method: 'POST',
			data: $scope.info
    	}).success(function (data) {
            $scope.sunProfitDate = defaultSunProfitDate;
			if (data.success){
                $scope.profitDate = data.data.resultList;
                $scope.profitGrid.totalItems = data.count;
			}else{
                $scope.notice(data.message);
			}
        }).error(function (data) {
            $scope.notice("服务器异常,请稍后再试");
        })
    };

    $scope.calcShareAmount = function () {
        if ($scope.info.minShareAmount &&
            $scope.info.maxShareAmount &&
            ($scope.info.maxShareAmount - $scope.info.minShareAmount) < 0){
            $scope.notice("最小分润金额不能大于最大分润金额");
            return;
        }
        $http({
            url: 'repayProfitDetail/calcShareAmount',
            method: 'POST',
            data: $scope.info
        }).success(function (data) {
            if (data.success){
                $scope.sunProfitDate = data.data;
            }else{
                $scope.sunProfitDate = defaultSunProfitDate;
                $scope.notice(data.message);
            }
        }).error(function (data) {
            $scope.sunProfitDate = defaultSunProfitDate;
            $scope.notice("服务器异常,请稍后再试");
        })
    };

    $scope.exportRepayProfitDetail = function () {
        if(!(  $scope.info.completeTimeBegin &&   $scope.info.completeTimeEnd)){
            $scope.notice("终态日期不能为空");
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
                    location.href="repayProfitDetail/exportRepayProfitDetail?"+$.param($scope.info);
                }
            });
    };
});
/**
 * 欢乐返活跃商户活动查询
 */
angular.module('inspinia').controller('happyBackActivityMerchantCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert){
	//数据源
	i18nService.setCurrentLang('zh-cn');
    $scope.disabledMerchantType = true;
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.baseInfo = {targetStatus:"",rewardAccountStatus:"",deductStatus:"",agentNode:"",bool:1};
    $scope.bools=[{text:"是",value:1},{text:"否",value:0}];
    $scope.resetForm=function(){
        $scope.baseInfo = {targetStatus:"",rewardAccountStatus:"",deductStatus:"",agentNode:""};
    };
	//达标状态
    $scope.targetStatusList = [{text:'全部',value:''},{text:'未达标',value:'0'},{text:'已达标',value:'1'}];
    //奖励入账状态
    $scope.rewardAccountStatusList = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];
    //扣款状态
    $scope.deductStatusList = [{text:'全部',value:''},{text:'未扣款',value:'0'},{text:'已扣款',value:'1'},{text:'已发起预调账',value:'2'},{text:'待扣款',value:'3'}];

	$scope.agent = [{text:"全部",value:""}];

    //代理商编号/名称下拉列表
    $http.post("agentInfo/selectAllInfo")
   	.success(function(msg){
   			//响应成功
   	   	for(var i=0; i<msg.length; i++){
   	   		$scope.agent.push({value:msg[i].agentNode,text:msg[i].agentNo+ " " + msg[i].agentName});
   	   	}
   	});

	//查询
	$scope.query=function(){
        $http.post('happyBackActivityMerchant/selectHappyBackActivityMerchant',"baseInfo="+angular.toJson($scope.baseInfo)
            +"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(!data){
                    $scope.notice("没有查询到数据");
                    return;
                }else{
                    $scope.activityGrid.data = data.result;
                }
            })
        $http.post('happyBackActivityMerchant/countMoney',"baseInfo="+angular.toJson($scope.baseInfo)
            ,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.rewardAmountTotalEd = data.rewardAmountTotalEd == null ? "0.00" : data.rewardAmountTotalEd;//奖励已入账
                $scope.rewardAmountTotal = data.rewardAmountTotal == null ? "0.00" : data.rewardAmountTotal;//奖励未入账
                $scope.deductAmountTotalEd = data.deductAmountTotalEd == null ? "0.00" : data.deductAmountTotalEd;//已扣款
                $scope.deductAmountTotal = data.deductAmountTotal == null ? "0.00" : data.deductAmountTotal;//待扣款
                $scope.deductAmountTotalAdvance = data.deductAmountTotalAdvance == null ? "0.00" : data.deductAmountTotalAdvance;//发起预调账
            })
	};
    $scope.query();
    $scope.columnDefs = [
        {field: 'activeOrder',displayName: '激活订单号',pinnable: false,width: 180,sortable: false},
        {field: 'activeTime',displayName: '激活日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'targetStatus',displayName: '奖励达标状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.targetStatusList)},
        {field: 'rewardAmount',displayName: '奖励金额(元)',pinnable: false,width: 180,sortable: false},
        {field: 'rewardAccountStatus',displayName: '奖励入账状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.rewardAccountStatusList)},
        {field: 'targetTime',displayName: '奖励达标日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'rewardAccountTime',displayName: '奖励入账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'deductAmount',displayName: '扣款金额(元)',pinnable: false,width: 180,sortable: false},
        {field: 'deductStatus',displayName: '扣款状态',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.deductStatusList)},
        {field: 'deductTime',displayName: '扣款/调账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'merchantNo',displayName: '所属商户编号',pinnable: false,width: 150,sortable: false},
        {field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 150,sortable: false},
        {field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 150,sortable: false},
    ];
	$scope.activityGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
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

	$scope.changeAgentNode = function () {
        $scope.disabledMerchantType = !$scope.baseInfo.agentNode;
    };

	//导出
	 $scope.exportExcel=function(){
	        SweetAlert.swal({
	            title: "确认导出？",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	if($scope.activityGrid.data==null || $scope.activityGrid.data.length==0){
		            		$scope.notice("没有可导出的数据");
		       			 	return;
		       		 	} else {
				       		 // location.href="happyBackActivityMerchant/exportExcel.do?baseInfo="+angular.toJson($scope.baseInfo);
                            $scope.exportInfoClick("happyBackActivityMerchant/exportExcel.do",{"baseInfo":angular.toJson($scope.baseInfo)});
		       		 	}
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
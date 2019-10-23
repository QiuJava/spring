/**
 * 查询业务产品
 */
angular.module('inspinia',['angularFileUpload']).controller('activityCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.discountStatus = [{text:'全部',value:-1},{text:'未扣回',value:0},{text:'已扣回',value:1}];
	$scope.baseInfo = {merchantN:"",agentNode:"",status:"",discountStatus:-1,checkStatus:"",
		activeTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
		activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
		merchantType:"1"};
	$scope.disabledMerchantType = true;
	//代理商下拉列表查询tgh418
	$scope.agent = [{text:"全部",value:""}];
	 $http.post("agentInfo/selectAllInfo")
   	 .success(function(msg){
   			//响应成功
   	   	for(var i=0; i<msg.length; i++){
   	   		$scope.agent.push({value:msg[i].agentNode,text:msg[i].agentNo+ " " + msg[i].agentName});
   	   	}
   	});

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setActiveTimeStart=function(setTime){
		$scope.baseInfo.activeTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setActiveTimeEnd=function(setTime){
		$scope.baseInfo.activeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	//查询
	$scope.query=function(){
		if (!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd )){
			$scope.notice("激活日期日期不能为空，请重新选择");
			return
		}
		$http.post('activityDetail/selectActivityDetail.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(data){
					if(!data)
						return;
					$scope.activityGrid.data =data.result; 
					$scope.activityGrid.totalItems = data.totalCount;
		})
	}
	$scope.query();
	$scope.activityGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'id',displayName: '序号',pinnable: false,width: 180,sortable: false},
            {field: 'activeOrder',displayName: '激活流水号',pinnable: false,width: 180,sortable: false},
            {field: 'cashOrder',displayName: '提现流水号',pinnable: false,width: 180,sortable: false},
            {field: 'activeTime',displayName: '激活时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'merchantName',displayName: '商户名称',pinnable: false,width: 180,sortable: false},
            {field: 'enterTime',displayName: '进件时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'transTotal',displayName: '交易金额',pinnable: false,width: 180,sortable: false},
            {field: 'merchantFee',displayName: '交易手续费',pinnable: false,width: 180,sortable: false},
            {field: 'frozenAmout',displayName: '冻结金额',pinnable: false,width: 180,sortable: false},
            {field: 'cashTime',displayName: '提现时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'status',displayName: '活动状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.activityStatus)},
            {field: 'targetAmout',displayName: '活动任务金额',pinnable: false,width: 180,sortable: false},
            {field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 180,sortable: false},
            {field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 180,sortable: false},
            // {field: 'parentName',displayName: '上级代理商名称',pinnable: false,width: 180,sortable: false},
            // {field: 'parentNo',displayName: '上级代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'checkStatus',displayName: '核算状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.checkStatus)},
            {field: 'discountStatus',displayName: '是否扣回',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.discountStatus)},
	     
        ],
        onRegisterApi: function(gridApi) {                
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	     });
        }
	 };
	$scope.query();
	//未激活时,隐藏激活时间
	$scope.hideActivityTime = function(){
		if($scope.baseInfo.status==1){
			$scope.activityTimeHide = true;
		} else {
			$scope.activityTimeHide = false;
		}
	}
		
	$scope.showT = false;
	$scope.showTrue = function(){
		$scope.showT = true;
	}
	//reset
	$scope.resetForm=function(){
		$scope.baseInfo = {merchantN:"",agentNode:"",status:"",discountStatus:-1,checkStatus:"",
			activeTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
			activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			merchantType: "1"};
        $scope.disabledMerchantType = true;
	}

    $scope.changeAgentNode = function () {
        $scope.disabledMerchantType = !$scope.baseInfo.agentNode;
    };
	//导出
	 $scope.exportExcel=function(){
		 if (!($scope.baseInfo.activeTimeStart && $scope.baseInfo.activeTimeEnd )){
			 $scope.notice("激活日期日期不能为空，请重新选择");
			 return
		 }
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
				       		 location.href="activityDetail/exportExcel.do?"
				       		 + "baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
							 + $scope.activityGrid.totalItems;
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
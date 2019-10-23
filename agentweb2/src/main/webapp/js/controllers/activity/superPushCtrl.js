/**
 * 超级推活动
 */
angular.module('inspinia',['angularFileUpload']).controller('superPushCtrl',function($scope,$rootScope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.baseInfo = {merchantNo:"",merchantName:"",agentNo:"",agentName:"",mobilephone:"",oneMerchantNo:"",twoMerchantNo:"",threeMerchantNo:"",oneLevelId:""};
	
//	//代理商下拉列表查询tgh418
//	$scope.agent = [{text:'全部',value:""}];
//	 $http.post("agentInfo/selectAllInfo")
//   	 .success(function(msg){
//   			//响应成功
//   	   	for(var i=0; i<msg.length; i++){
//   	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
//   	   	}
//   	});
	 
	//查询
	$scope.query=function(){
		$http.post('superPushAction/selectSuperPush.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(data){
					if(!data)
						return;
					$scope.activityGrid.data =data.result; 
					$scope.activityGrid.totalItems = data.totalCount;
					console.log($scope.activityGrid.data);
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
            {field: 'id',displayName: '序号',width: 50,pinnable: false,sortable: false,cellTemplate: "<span>{{rowRenderIndex + 1}}</span>"},
            {field: 'merchantName',displayName: '商户名称',pinnable: false,width: 150,sortable: false},
            {field: 'merchantNo',displayName: '商户编号',pinnable: false,width: 150,sortable: false},
            {field: 'mobilephone',displayName: '手机号',pinnable: false,width: 100,sortable: false},
            {field: 'agentName',displayName: '代理商',pinnable: false,width: 150,sortable: false},
            {field: 'oneMerchantNo',displayName: '上一级商户编号',pinnable: false,width: 150,sortable: false},
            {field: 'twoMerchantNo',displayName: '上二级商户编号',pinnable: false,width: 150,sortable: false},
            {field: 'threeMerchantNo',displayName: '上三级商户编号',pinnable: false,width: 150,sortable: false},
//            {field: 'totalAmount',displayName: '收益总额(元)',pinnable: false,width: 150,sortable: false},
            {field: 'balance',displayName: '账户余额(元)',pinnable: false,width: 150,sortable: false},
            {field: 'action',displayName: '操作',width: 200,pinnable: false,sortable: false,editable:true,cellTemplate:
	         	'<a class="lh30" ui-sref="active.superPushMerchantDetail({mertId:row.entity.merchantNo})">商户详情</a>'
	            +'<a class="lh30" ui-sref="active.superPushCashDetail({mertId:row.entity.merchantNo})"> |提现详情</a>'
            	+'<a class="lh30" ui-sref="active.superPushShareDetail({mertId:row.entity.merchantNo})"> |分润详情</a>'
	        	}
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

	$scope.showT = false;
	$scope.showTrue = function(){
		$scope.showT = true;
	}
	$scope.resetForm=function(){
		$scope.baseInfo = {merchantNo:"",merchantName:"",agentNo:"",agentName:"",mobilephone:"",oneMerchantNo:"",twoMerchantNo:"",threeMerchantNo:"",oneLevelId:""};
	}
	
});
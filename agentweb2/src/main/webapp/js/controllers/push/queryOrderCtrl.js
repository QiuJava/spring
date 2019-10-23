angular.module('inspinia').controller("queryOrderCtrl",function($scope, $http, $state, $stateParams, i18nService) {
	
	i18nService.setCurrentLang('zh-cn');
	
	$scope.orderStatus = [{text:"全部",value:-1},{text:"待抢",value:1},{text:"再次待抢",value:2},{text:"待核查",value:3},{text:"待进件",value:4},{text:"无效单",value:5}];
	$scope.types = [{text:"全部",value:-1},{text:"推荐人代理商",value:1},{text:"公共池抢单",value:2},{text:"公司业务",value:3}];
	var s_time = new Date().valueOf() + (-6 * 24 * 60 * 60 * 1000);
	$scope.info = {"order_status":-1,"order_user_type":-1,"s_order_time":s_time,"e_order_time":new Date(),"s_create_time":s_time,"e_create_time":new Date()};
	
	$scope.query = function() {
	   $http.post('push/querySupertuiOrderList.do',
       		 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
       		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
       ).success(function(data){
            $scope.orderDate = data.result;
			 $scope.orderGrid.totalItems = data.totalCount;//总记录数
       }).error(function(){
       }); 
		
	};
	
	$scope.query();
	
	$scope.orderGrid = {
	        data: 'orderDate',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        enableHorizontalScrollbar: 0,        //去掉滚动条
	        enableVerticalScrollbar : 0, 
	        columnDefs: [
                {field: 'order_id',displayName: '订单ID',width: 120,pinnable: false,sortable: false},
                {field: 'bp_name',displayName: '业务产品',width: 180,pinnable: false,sortable: false},
                {field: 'lawyer',displayName: '被推荐人姓名',width: 120,pinnable: false,sortable: false},
                {field: 'mobilephone',displayName: '被推荐人手机号',width: 120,pinnable: false,sortable: false},
                {field: 'merchant_name',displayName: '推荐商户名称',width: 120,pinnable: false,sortable: false},
                {field: 'order_user_type',displayName: '接单人类型',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.types)},
                {field: 'j_agent_name',displayName: '接单代理商名称',width: 180,pinnable: false,sortable: false},
                {field: 'order_status',displayName: '订单状态',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.orderStatus)},
                {field: 'mbp_id',displayName: '进件编号',width: 120,pinnable: false,sortable: false},
	            {field: 'action',displayName: '操作',width: 180,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<a class='lh30' ui-sref='push.orderDetail({id:row.entity.id})'>详情</a>"}
	        ],
	        onRegisterApi: function(gridApi) {	//选中行配置
	            $scope.gridApi = gridApi;
	            $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row,event) {
	               if(row){
	                  $scope.testRow = row.entity;
	               }
	            })
	        }
	 };
});
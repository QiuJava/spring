angular.module('inspinia').controller("queryRuleCtrl",function($scope, $http, $state, $stateParams, i18nService) {
	
	i18nService.setCurrentLang('zh-cn');
	
	$scope.info = {};
	
	$scope.query = function() {
	        $http.post('push/querySupertuiRuleList.do',
	        		 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
	        		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
	        ).success(function(data){
	             $scope.ruleDate = data.result;
				 $scope.ruleGrid.totalItems = data.totalCount;//总记录数
	        }).error(function(){
	        }); 
	};
	
	$scope.query();
	
	$scope.ruleGrid = {
	        data: 'ruleDate',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        enableHorizontalScrollbar: 0,        //去掉滚动条
	        enableVerticalScrollbar : 0, 
	        columnDefs: [
                {field: 'id',displayName: '编号',width: 120,pinnable: false,sortable: false},
                {field: 'bpId',displayName: '业务产品',width: 180,pinnable: false,sortable: false},
                {field: 'recommendedAmount',displayName: '推荐人奖励金额',width: 120,pinnable: false,sortable: false},
                {field: 'orderAgentFee',displayName: '代理商接单费用',width: 120,pinnable: false,sortable: false},
                {field: 'efficientDate',displayName: '生效日期',width: 120,pinnable: false,sortable: false,cellFilter: "date:'yyyy-MM-dd'"},
                {field: 'disabledDate',displayName: '失效日期',width: 120,pinnable: false,sortable: false,cellFilter: "date:'yyyy-MM-dd'"},
                {field: 'grabOrderTime',displayName: '抢单时间',width: 120,pinnable: false,sortable: false},
                {field: 'feedbackTime',displayName: '反馈时间',width: 120,pinnable: false,sortable: false},
	            {field: 'action',displayName: '操作',width: 180,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	"<a ui-sref='push.editRule({id:row.entity.id})'>修改</a> | <a ui-sref='push.ruleDetail({id:row.entity.id})'>详情</a>"}
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
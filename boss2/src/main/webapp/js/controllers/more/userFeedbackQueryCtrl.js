/**
 * 用户反馈问题查询
 */
angular.module('inspinia').controller("userFeedbackQueryCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,$document) {
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	
	$scope.problemTypes=[{text:"全部",value:""}];
	$scope.userTypes=[{text:"全部",value:""},{text:"代理商",value:1},{text:"商户",value:2}]
	$scope.info={problemType:"",userType:"",userName:"",mobilephone:"",title:""};
	$scope.userProblemData=[];
	
	//问题类型
	 $http.post("userFeedbackProblemAction/selectAllProblemInfo")
  	 .success(function(msg){
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.problemTypes.push({value:msg[i].problemType,text:msg[i].typeName});
  	   	}
  	});
	
	//查询
	$scope.queryFunc=function(){
		$http.post("userFeedbackProblemAction/selectAllInfo","info=" + angular.toJson($scope.info)+"&pageNo="+
				$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(data.bols){
				$scope.userProblemData=data.page.result;
				$scope.userFeedbackProblem.totalItems = data.page.totalCount;
			}
		});
	}
	$scope.queryFunc();
	//清空
	$scope.resetForm=function(){
		$scope.info={problemType:"",userType:"",userName:"",mobilephone:"",title:""};
	}
	
	//列表
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.userFeedbackProblem = {
		        data: 'userProblemData',
		        paginationPageSize:10,                  //分页数量
		        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
		        useExternalPagination: true,		  //开启拓展名
		        columnDefs: [
	                {field: 'id',displayName: 'ID',width: 120,pinnable: false,sortable: false},
	                {field: 'userType',displayName: '反馈用户分类',width: 150,pinnable: false,sortable: false,cellFilter:"userTypeas"},
		            {field: 'userName',displayName: '反馈用户',width: 150,pinnable: false,sortable: false},
		            {field: 'mobilephone',displayName: '反馈用户手机号',width: 150,pinnable: false,sortable: false},
		            {field: 'typeName',displayName: '问题类别',width:150,pinnable: false,sortable: false},
		            {field: 'title',displayName: '标题',width: 180,pinnable: false,sortable: false},
		            {field: 'submitTime',displayName: '提交时间',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		            {field: 'id',displayName: '操作',width: 130,pinnedRight: true,sortable: false,editable:true,
		            	cellTemplate:'<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'userFeedback.detail\')" ui-sref="sys.userFeedbackDetail({id:row.entity.id})">详情</a>'
		            }
		        ],
		        onRegisterApi: function(gridApi) {                
		            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		            	$scope.paginationOptions.pageNo = newPage;
		            	$scope.paginationOptions.pageSize = pageSize;
		            	$scope.queryFunc();
		            });
		        }
		 };

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.queryFunc();
			}
		})
	});

}).filter('userTypeas', function () {
	return function (value) {
		switch(value) {
			case "1" :
				return "代理商";
				break;
			case "2" :
				return "商户";
				break;
		}
	}
})
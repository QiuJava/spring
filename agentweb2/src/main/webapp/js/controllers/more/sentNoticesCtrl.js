/**
 * 下发公告查询
 */
angular.module('inspinia').controller("sentNoticesCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService) {
	//数据源
	$scope.bool=[{text:"是",value:"1"},{text:"否",value:"0"}];
	$scope.targetTypes=[{text:"不限",value:'0'},{text:"商户",value:'1'},{text:"代理商",value:'2'}];
	$scope.statusTypes=[{text:"全部",value:'0'},{text:"待下发",value:'1'},{text:"正常",value:'2'}];
	$scope.targetTypesStr = angular.toJson($scope.targetTypes);
	$scope.statusTypesStr = angular.toJson($scope.statusTypes);
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.baseInfo = {sysType:'0',status:'0',createTimeBegin:null,createTimeEnd:null,issuedTimeBegin:null,issuedTimeEnd:null};
	$scope.queryFunc = function(){
		$http.post("notice/selectByParam.do","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
				$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			$scope.result=data.result;
			$scope.noticeListGrid.totalItems = data.totalCount;
		});
	}
	
    $scope.noticeListGrid = {
	        data: 'result',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        enableHorizontalScrollbar: true,        //横向滚动条
            enableVerticalScrollbar : true,  		//纵向滚动条 
	        columnDefs: [
                {field: 'ntId',displayName: '通告ID',width: 120,pinnable: false,sortable: false},
	            {field: 'title',displayName: '标题',width: 150,pinnable: false,sortable: false},
	            {field: 'status',displayName: '状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusTypesStr},
	            {field: 'createTime',displayName: '创建时间',width:150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'issuedTime',displayName: '下发时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	            {field: 'action',displayName: '操作',width: 180,pinnable: false,sortable: false,editable:true,cellTemplate:
	            	'<a class="lh30" ng-show="grid.appScope.hasPermit(\'more.noticeDetail\')" ui-sref="more.noticeDetail({id:row.entity.ntId})">详情</a><span ng-switch on="row.entity.status"><span ng-switch-when="1"> | <a class="lh30" ng-show="grid.appScope.hasPermit(\'more.modifyNotice\')" ui-sref="more.modifyNotice({id:row.entity.ntId})">修改</a>' +
	            	' | <a class="lh30" ng-show="grid.appScope.hasPermit(\'more.deliverNotice\')" ui-sref="more.deliverNotice({id:row.entity.ntId})">下发</a></span><span ng-switch-default> | <label class="lh30" style="color:#ccc;">修改</label> | <label class="lh30" style="color:#ccc;">下发</label></span></span>'}
	        ],
	        onRegisterApi: function(gridApi) {                
	            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	            	$scope.paginationOptions.pageNo = newPage;
	            	$scope.paginationOptions.pageSize = pageSize;
	            	$scope.queryFunc();
	            });
	        }
	 };
    $scope.queryFunc();
    //reset
	$scope.resetForm=function(){
		$scope.baseInfo= {sysType:'0',status:'0',createTimeBegin:null,createTimeEnd:null,issuedTimeBegin:null,issuedTimeEnd:null};
	}
});

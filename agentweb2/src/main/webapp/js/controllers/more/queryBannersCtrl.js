/**
 * 查询banner
 */
angular.module('inspinia').controller("queryBannersCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService) {
	//数据源
	$scope.bool=[{text:"是",value:"1"},{text:"否",value:"0"}];
	$scope.statusTypes=[{text:"全部",value:'2'},{text:"开启",value:'1'},{text:"关闭",value:'0'}];
	$scope.statusTypesStr = angular.toJson($scope.statusTypes);
	$scope.query = {bannerStatus:'2'};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.queryFunc = function(){	
		$http.post('banner/selectByCondition.do',"query=" + angular.toJson($scope.query)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				  {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function(msg){
			$scope.banners = msg.result;
			$scope.bannerListGrid.totalItems = msg.totalCount;
		}).error(function(){
		})
	};
	$scope.bannerListGrid = {
        data: 'banners',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条 
        columnDefs: [
            {field: 'bannerId',displayName: 'ID',width: 120,pinnable: false,sortable: false},
            {field: 'bannerName',displayName: 'banner名称',width: 120,pinnable: false,sortable: false},
            {field: 'onlineTime',displayName: '上线时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'offlineTime',displayName: '下线时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'weight',displayName: '权重',width:150,pinnable: false,sortable: false},
            {field: 'bannerStatus',displayName: '状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusTypesStr},
            {field: 'action',displayName: '操作',width: 180,pinnable: false,sortable: false,editable:true,cellTemplate:
            	'<a  class="lh30"ng-show="grid.appScope.hasPermit(\'more.bannerDetail\')" ui-sref="more.bannerDetail({id:row.entity.bannerId})">详情</a> | <a ng-show="grid.appScope.hasPermit(\'more.modifyBanner\')" ui-sref="more.modifyBanner({id:row.entity.bannerId})">修改</a>' +
            	'<a class="lh30" ng-show="grid.appScope.hasPermit(\'switchStatus\')" ng-click="grid.appScope.switchStatus(row.entity.bannerId,row.entity.bannerStatus)"> | <span ng-switch on="row.entity.bannerStatus"><span ng-switch-when="0">开启</span>'+
            	'<span  class="lh30"ng-switch-when="1">关闭</span></a>'}
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
	//修改banner状态
	$scope.switchStatus = function(bannerId,bannerStatus){
		var param = {'bannerId':bannerId,"bannerStatus":bannerStatus};
		var data = {"banner":param};
		$http.post('banner/switchStatus.do', angular.toJson(data)).success(function(msg){
			if(msg.status){
				for(var i=0; i<$scope.banners.length; i++){
					if(bannerId == $scope.banners[i].bannerId){
						if(bannerStatus == '0'){
							$scope.banners[i].bannerStatus = '1';
						}
						if(bannerStatus == '1'){
							$scope.banners[i].bannerStatus = '0';
						}
					}
				}
			} else{
				$scope.notice(msg.msg);
			}
		}).error(function(){
		})
	};
	//reset
	$scope.resetForm=function(){
		$scope.query= {bannerStatus:'2'};
	}
});

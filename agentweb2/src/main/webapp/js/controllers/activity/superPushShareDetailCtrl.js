/**
 * 超级推分润详情
 */

angular.module('inspinia').controller('superPushShareDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal,SweetAlert){
	
	//数据源
	$scope.businessTypes=[];
	$scope.info={mertId:$stateParams.mertId,sDate:"",eDate:""};
	$scope.rates=[];
	$scope.query = function(){
		$http.post('superPushAction/selectShareDetail.do',"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(data){
					if(!data){
						return;
					}
					$scope.rates=data.result;
					console.log($scope.rates)
		});
	}
	$scope.query();
	$scope.gridOptions={
		data:"rates",
		paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
		rowHeight:40,
		columnDefs: [
 	         {field: 'id',displayName: '序号',width: 150,pinnable: false,sortable: false,cellTemplate: "<span>{{rowRenderIndex + 1}}</span>"},
 	         {field: 'transTime',displayName: '统计日期',width:150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
 	         {field: 'merchantNo',displayName: '下级商户编号',width:150,pinnable: false,sortable: false},
 	         {field: 'merchantName',displayName: '下级商户名称',width:150,pinnable: false,sortable: false},
 	         {field: 'level',displayName: '下级商户级别',width:150,pinnable: false,sortable: false},
 	         {field: 'share',displayName: '贡献分润金额',width:150,pinnable: false,sortable: false},
 	         {field: 'rule',displayName: '分润比例',width:150,pinnable: false,sortable: false},
 	         {field: 'amount',displayName: '交易金额',width:150,pinnable: false,sortable: false},
 	     ],
 	    onRegisterApi: function(gridApi) {              
	         $scope.gridApi = gridApi;
	         $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	         });
	     }
	};
	//导出信息//打开导出终端模板
	 $scope.exportInfo=function(){
		 if($scope.info.sdate > $scope.info.edate){
				$scope.notice("起始时间不能大于结束时间");
				return;
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
			            	location.href="superPushAction/exportTransInfo.do?info="+angular.toJson($scope.info);
			            }
		        });
	 }
});

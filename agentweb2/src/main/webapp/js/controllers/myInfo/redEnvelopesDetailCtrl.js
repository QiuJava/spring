/**
 * 红包明细 tgh
 */
angular.module('inspinia').controller('redEnvelopesDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService, SweetAlert){
	$scope.dictList = {};
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$scope.transTypes=[{text:"全部",value:""},{text:"发红包",value:"0"},{text:"抢红包",value:"1"},{text:"红包分润",value:"2"},{text:"过期余额回收",value:"3"},
						{text:"其他账户转入",value:"4"},{text:"转出其他账户",value:"5"},{text:"风控关闭红包",value:"6"},{text:"风控打开关闭的红包",value:"7"}
						,{text:"区域代理收益",value:"8"},{text:"买入区域",value:"9"},{text:"区域代理交易分润",value:"10"},{text:"转让区域代理收益",value:"11"}
						,{text:"卖出区域",value:"12"}];
	$scope.operationTypeList=[{text:"全部",value:""},{text:"增加",value:"增加"},{text:"减少",value:"减少"}];
	$scope.accountAllInfo = [];
	$scope.info = {type:"",startCreateDate:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
    		endCreateDate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',transType:"",operationType:""};
	$scope.resetForm = function(){
		$scope.info = {type:"",startCreateDate:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
	    		endCreateDate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',transType:"",operationType:""};
	}
	
	$scope.query=function(){
		var data={"type":$scope.info.type,"startCreateDate":$scope.info.startCreateDate,"endCreateDate":$scope.info.endCreateDate,
				"transType":$scope.info.transType,"operationType":$scope.info.operationType};
		$http.post(
			'myInfo/selectRedEnvelopesDetails',
			 "info="+angular.toJson(data)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(result.flag){
				console.log(result)
				$scope.accountAllInfo = result.list; 
				$scope.info.type = result.list[0].type;
				if (result.list) {
					if (result.list[0].type=0) {
						$scope.info.type='平台';
					}else if (result.list[0].type=1) {
						$scope.info.type='品牌商';
					}else if (result.list[0].type=2) {
						$scope.info.type='个人';
					}
				}
				$scope.totalAmount = result.totalAmount; 
				$scope.orderGrid.totalItems = result.page.totalCount;
			}else{
				$scope.notice(result.msg);
			}
		});
	}
	$scope.query();
	$scope.transType=[{text:"发红包",value:"0"},{text:"抢红包",value:"1"},{text:"红包分润",value:"2"},{text:"过期余额回收",value:"3"},
						{text:"其他账户转入",value:"4"},{text:"转出其他账户",value:"5"},{text:"风控关闭红包",value:"6"},{text:"风控打开关闭的红包",value:"7"}
						,{text:"区域代理收益",value:"8"},{text:"买入区域",value:"9"},{text:"区域代理交易分润",value:"10"},{text:"转让区域代理收益",value:"11"}
						,{text:"卖出区域",value:"12"}];
	//表格数据
	$scope.columns = [
	                  	{ field: 'type',displayName:'账户类别',cellFilter:"formatDropping:[{text:'平台',value:'0'},{text:'品牌商',value:'1'},{text:'个人',value:'2'}]"},
 						{ field: 'redOrderId',displayName:'对应红包ID'},
 						{ field: 'createDate',displayName:'记账时间',width:150,cellTemplate:"<div>{{row.entity.createDate | date:'yyyy-MM-dd'}} {{row.entity.createDate | date:'HH:mm:ss'}}</div>"},
 						{ field: 'transType',displayName:'变动类型',width:180,cellFilter:"formatDropping:" + angular.toJson($scope.transType)},
 						{ field: 'operationType',displayName:'操作'},
 						{ field: 'transAmount',displayName:'金额'},
 					];
	$scope.orderGrid ={
		 data:'accountAllInfo',
		 paginationPageSize:10,                  //分页数量
	     paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	     useExternalPagination: true,
	     columnDefs:$scope.columns,
		  onRegisterApi: function(gridApi) {                
	          $scope.gridApi = gridApi;
	          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	          	$scope.query();
	          });
	      }
	};
    $scope.exportInfo=function(){
        if($scope.exportingSharePreDay){
            $scope.notice("正在导出数据,请不要频繁点击导出按钮.");
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
                    $scope.exportingSharePreDay = true;
                    setTimeout(function () {
                        $scope.exportingSharePreDay = false;
                    }, 5000);
                    location.href="myInfo/exportRedEnvelopesDetails?info="+angular.toJson($scope.info);
                }
            });
    };
});
angular.module('inspinia').controller('acqMerchantQueryCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$filter,i18nService, SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	$scope.merchantTypes=[{text:"全部",value:"-1"},{text:"个体收单商户",value:"1"},{text:"企业收单商户",value:"2"}];
	$scope.auditStates=[{text:"全部",value:"-1"},{text:"待审核",value:"1"},{text:"审核通过",value:"2"},{text:"审核不通过",value:"3"}];
	$scope.info={auditState:"-1",merchantType:"-1",intoSource:"",
		intoStartTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
		intoEndTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
	/*	auditStartTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
		auditEndTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
*/
		};
	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setIntoStartTime=function(setTime){
		$scope.info.intoStartTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setIntoEndTime=function(setTime){
		$scope.info.intoEndTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setAuditStartTime=function(setTime){
		$scope.info.auditStartTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setAuditEndTime=function(setTime){
		$scope.info.auditEndTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	$scope.intos = [];
	//代理商
	 $http.post("merchantInfo/intos")
   	 .success(function(data){
   		 $scope.intos.push({value:"",text:"全部"});
   			//响应成功
   	   	for(var i=0; i<data.length; i++){
   	   		$scope.intos.push({value:data[i].sysValue,text:data[i].sysName});
   	   	}
   	});
	 
	$scope.intoPage = function(){
		$state.go('merchant.addAcqMer');
	} 
	 
	//查询
	$scope.selectInfo=function(){
		if (!($scope.info.intoStartTime && $scope.info.intoEndTime)){
			$scope.notice("进件日期不能为空，请重新选择");
			return

		}
		$http({
			url: 'merchantInfo/acqMerInfoList.do?pageNo='+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			method: 'POST',
			data: $scope.info
		}).success(function (page) {
            $scope.gridOptions.data = page.result;
            $scope.gridOptions.totalItems = page.totalCount;
  
        });
		
	};
	// 加载时调用查询
	$scope.selectInfo();
	 
	//清空
	$scope.clear=function(){
		$scope.info={auditState:"-1",merchantType:"-1",intoSource:"",acqIntoNo:"",merchantName:"",legalPerson:"",
			intoStartTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
			intoEndTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			auditStartTime:"",auditEndTime:""
		};
	};
	
	$scope.columnDefs = [                           //表格数据
               		         { field: 'acqIntoNo',displayName:'收单进件编号',width:170},
               		         { field: 'merchantName',displayName:'商户名称',width:180 },
               		         { field: 'merchantType',displayName:'商户类型',width:120,cellFilter:"formatDropping:[{text:'个体收单商户',value:'1'},{text:'企业收单商户',value:'2'}]"},
               		         { field: 'auditStatus',displayName:'审核状态',width:100,cellFilter:"formatDropping:[{text:'待审核',value:'1'},{text:'审核通过',value:'2'},{text:'审核不通过',value:'3'}]"},
               		         { field: 'legalPerson',displayName:'法人姓名',width:100},
               		         { field: 'intoSourceName',displayName:'进件来源',width:120},
               		         { field: 'createTime',displayName:'进件日期',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
               		         },
               		         { field: 'auditTime',displayName:'审核日期',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
               		         },
               		         
               		         { field: 'id',displayName:'操作',width:100,
               		        	 cellTemplate:
               		        		 '<div ng-switch on="row.entity.auditStatus" style="display:inline-block">'
               			    			+'<div ng-switch-when="3">'
               						       + '<a ng-show="grid.appScope.hasPermit(\'merchant.acqMerUpdate\') " ui-sref="merchant.acqMerUpdate({id:row.entity.id})">编辑 | </a>'
               						       + '<a ng-show="grid.appScope.hasPermit(\'merchant.queryAcqMerDetail\')" ui-sref="merchant.queryAcqMerDetail({id:row.entity.id})">详情</a>'
               			    			+'</div>'
               			    			+'<div ng-switch-default>'
                                       		 + '<a ng-show="grid.appScope.hasPermit(\'merchant.queryAcqMerDetail\')" ui-sref="merchant.queryAcqMerDetail({id:row.entity.id})">详情</a>'
               		    				+'</div>'
               			    		+'</div>'
               		        		
               		         }
               		      ];
    if ($scope.agentType == 11) {
    	$scope.columnDefs.splice(7,1);
    }
    
	 $scope.gridOptions={                           //配置表格
		      paginationPageSize:10,                  //分页数量
		      paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		      useExternalPagination: true,  
		      columnDefs:$scope.columnDefs,
			  onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectInfo();
		          });
		      }
		};
})
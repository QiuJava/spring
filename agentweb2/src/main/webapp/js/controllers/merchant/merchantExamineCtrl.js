/**
 * 审核查询
 */
angular.module('inspinia').controller('merchantExamineCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.IsYN=[{text:"全部",value:-1},{text:"是",value:0},{text:"否",value:1}];
	$scope.termianlTypes=[{text:"全部",value:0}];
	$scope.info={sTime:"",eTime:"",mbpId:"",merchantExamineState:1,merchantNo:"",agentName:"",agentNode:-1,productType:"-1",termianlType:"-1",mobilephone:"",activityType:""};
	i18nService.setCurrentLang('zh-cn');
	
	//机具活动类型229tgh====v
	$scope.activityTypes=[{text:"全部",value:""}];
	$scope.activityType=[];
     for(var i=0; i<$scope.activityTypess.length; i++){
             $scope.activityTypes.push({value:$scope.activityTypess[i].sys_value,text:$scope.activityTypess[i].sys_name});
             $scope.activityType.push({value:$scope.activityTypess[i].sys_value,text:$scope.activityTypess[i].sys_name});
     }
     $scope.activityTypesStr = angular.toJson($scope.activityType);
	//==============^
	
	//业务产品
	$http.get('businessProductDefine/selectAllInfo.do')
	.success(function(largeLoad) {
		if(!largeLoad)
			return
		$scope.productTypes=largeLoad;
		$scope.productTypes.splice(0,0,{bpId:"-1",bpName:"全部"});
	});
	
	//机具类型
	$http.get('hardwareProduct/selectAllInfo.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
		$scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
	})
	
	//模糊查询
	$scope.selectInfos=function(){
		$http.post(
			'merchantBusinessProduct/selectByStatusParam',
			 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			//响应成功
			$scope.gridOptions.data = result.result; 
			$scope.gridOptions.totalItems = result.totalCount;
		});
	}
	
	//清空
	$scope.clear=function(){
		$scope.info={sTime:"",eTime:"",mbpId:"",merchantNo:"",merchantExamineState:2,agentName:"",agentNode:-1,productType:"-1",termianlType:"-1",mobilephone:""};
	}
	$scope.agent = [{text:"全部",value:""}];
	//代理商
	 $http.post("agentInfo/selectAllInfo.do")
  	 .success(function(msg){
  			//响应成功
  			//$scope.agent.push({value:null,text:'全部'});
  		 	for(var i=0; i<msg.length; i++){
	    		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
	    	}
  	});
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
//	$http.get('merchantBusinessProduct/selectAllByStatusInfo')
//	.success(function(data){
//		if(!data)
//			return;
//		$scope.gridOptions.data =data.result ; 
//		$scope.gridOptions.totalItems = data.totalCount; 
//	})
	
	 $scope.gridOptions={                           //配置表格
			  paginationPageSize:10,                  //分页数量
			  paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
			  useExternalPagination: true,  
		      columnDefs:[                           //表格数据
//		         { field: 'id',displayName:'商户进件编号'},
//		         { field: 'teamId',displayName:'所属组织',
//		        	 cellFilter:"cTypeStrFilter"
//		         },
		         { field: 'merchantNo',displayName:'商户编号'},
		         { field: 'merchantName',displayName:'商户简称' },
		         { field: 'mobilePhone',displayName:'商户手机号' },
		         { field: 'bpName',displayName:'业务产品' },
//		         { field: 'agentName',displayName:'代理商名称' },
		         { field: 'status',displayName:'状态',
		        	 cellFilter:"formatDropping:[{text:'待一审',value:1},{text:'待平台审核',value:2},{text:'审核失败',value:3},{text:'正常',value:4},{text:'关闭',value:0}]"
		         },
		         { field: 'merCreateTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
		         },
		         { field: 'activityType',displayName:'机具活动类型' ,
		        	 cellFilter:"formatDropping:"+$scope.activityTypesStr
		         },
		         { field: 'id',displayName:'操作',
		        	 cellTemplate:'<a ui-sref="merchant.MerExamineDetail({mertId:row.entity.id})">审核</a>'
		         }
		      ],
		      onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectInfos();
		          });
		      }
		};
	$scope.selectInfos();
})
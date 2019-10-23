/**
 * nodeAgentNo
 * 商户迁移
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('merchantMigrateCtrl',function(i18nService,$scope,$http,$timeout,$state,$stateParams,$compile,$filter,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={agentNo:"-1",nodeAgentNo:"-1",createPerson:"",checkStatus:"-1",sTime:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
			eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',oaNo:""};
	$scope.agentNo = [{text:"全部",value:"-1"}];
	$scope.nodeAgentNo = [{text:"全部",value:"-1"}];
	$scope.checkStatus=[{text:"全部",value:"-1"},{text:"待审核",value:"1"},{text:"审核通过",value:"2"},{text:"审核失败",value:"3"},
	                     {text:"迁移成功",value:"4"},{text:"迁移失败",value:"6"},{text:"撤销迁移",value:"5"},{text:"部分成功",value:"7"}];
	
	$scope.query=function(){
		if($scope.info.sTime != "" && $scope.info.eTime != ""){
			if($scope.info.sTime>$scope.info.eTime){
				$scope.notice("申请开始时间应小于截止时间!");
				return;
			}
		}
		/**
		 * 日期处理，变更后传入后日期格式导致转换异常，现处理成yyyy-MM-dd HH:mm:ss
		 */
		 var sTimeString = $filter('date')($scope.info.sTime, "yyyy-MM-dd hh:mm:ss");  
		 var eTimeString = $filter('date')($scope.info.eTime, "yyyy-MM-dd hh:mm:ss");  
		 if(sTimeString._i){
			 $scope.info.sTime = $filter('date')(sTimeString._i, "yyyy-MM-dd HH:mm:ss");
		 }
		 if(eTimeString._i){
			 $scope.info.eTime = $filter('date')(eTimeString._i, "yyyy-MM-dd HH:mm:ss");
		 }
		 /**
		  * end
		  */
		var data = {
				"param" : $scope.info,
				"pageNo" : $scope.paginationOptions.pageNo,
				"pageSize" : $scope.paginationOptions.pageSize
			};
		$http.post('merchantMigrate/findMirate',angular.toJson(data)).success(function(result){
			$scope.gridOptions.data = result.page.result;  
			$scope.gridOptions.totalItems = result.page.totalCount;
		}).error(function(){
			$scope.notice("查询商户迁移信息失败,请联系管理员");
		});
		
		/*
		$http({
			method:"get",
			url:"merchantMigrate/findMirate?param="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			headers:{'Content-Type':'application/x-www-form-urlencoded;charset=utf-8'}
		}).success(function(result){
			$scope.gridOptions.data = result.page.result;  
			$scope.gridOptions.totalItems = result.page.totalCount;
		}).error(function(){
			$scope.notice("查询商户迁移信息失败,请联系管理员");
		});
		*/
	}
	$scope.query();
	/**
	 * 加载商户迁移记录
	 
	$http.post('merchantMigrate/findMirate?param=')
	.success(function(result){
		$scope.gridOptions.data = result; 
	});
	*/
	$scope.getNodeAgent=function(){
		$scope.nodeAgentNo = [{text:"全部",value:"-1"}];
		$scope.info.nodeAgentNo = "-1";
		if($scope.info.agentNo !="-1"){
			$http({
				method:"get",
				url:"merchantMigrate/findNodeAgent?agentNo="+$scope.info.agentNo,
				headers: {'Content-Type': 'application/x-www-form-urlencoded' } 
			}).success(function(result){
				for(var i=0; i<result.length; i++){
		    		$scope.nodeAgentNo.push({value:result[i].agentNo,text:result[i].agentName});
		    	}
			}).error(function(){
				$scope.notice("加载所属代理商失败,请联系管理员");
			});
		}
	}
	
	//清空
	$scope.clear=function(){
		$scope.nodeAgentNo = [{text:"全部",value:"-1"}];
		$scope.info={agentNo:"-1",nodeAgentNo:"-1",createPerson:"",checkStatus:"-1",sTime:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',oaNo:""};
	}
	
	$scope.chexiao = function(id){
		SweetAlert.swal({
            title: "撤销后系统将不在再对商户进行迁移，是否继续撤销？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post("merchantMigrate/cheXiaoMigrate?id="+id.id)
	   		  	 	.success(function(msg){
	   		  	 		$scope.notice(msg.msg);
	   		  	 		if(msg.flag == "true"){
	   		  	 			$state.transitionTo('merchant.migrate',null,{reload:true});
	   		  	 		}
	   		  	 	});
	            }
        });
	}
	
	//代理商
	 $http.post("merchantMigrate/getAllAgentInfo")
  	 .success(function(msg){
  			//响应成功
  		 	for(var i=0; i<msg.length; i++){
	    		$scope.agentNo.push({value:msg[i].agentNo,text:msg[i].agentName});
	    	}
  	});
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	
	
	 $scope.gridOptions={                           //配置表格
			  paginationPageSize:10,                  //分页数量
			  paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
			  useExternalPagination: true,  
		      columnDefs:[                           //表格数据
		         { field: 'id',displayName:'申请单号',width:100},
		         { field: 'createPerson',displayName:'申请人',width:100},
		         { field: 'createTime',displayName:'申请时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
		         { field: 'oneAgentName',displayName:'目标一级代理商',width:150},
		         { field: 'nodeAgentName',displayName:'目标直属代理商',width:150},
		         { field: 'oaNo',displayName:'关联OA单号',width:150},
		         { field: 'checkStatus',displayName:'状态',width:100,cellFilter: 'formatDropping:[{text:"全部",value:"-1"},{text:"待审核",value:"1"},{text:"审核通过",value:"2"},{text:"审核失败",value:"3"},{text:"迁移成功",value:"4"},{text:"迁移失败",value:"6"},{text:"撤销迁移",value:"5"},{text:"部分成功",value:"7"}]'},
		         { field: 'checkPerson',displayName:'审核人',width:100},
		         { field: 'id',displayName:'操作',width:150,pinnedRight:true,cellTemplate:
		        	 '<div  class="lh30"><a ui-sref="merchant.migrateDetail({id:row.entity.id})">详情</a><h ng-if="row.entity.checkStatus==1">  |  <a  ui-sref="merchant.migrateCheck({id:row.entity.id})">审核</a></h><h ng-if="row.entity.checkStatus==1 ||row.entity.checkStatus==2">  | <a ng-click="grid.appScope.chexiao({id:row.entity.id})">撤销</a></h></div>'
		         }
		      ],
		      onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		          	$scope.query();
		          });
		      }
		};
});

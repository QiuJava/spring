angular.module('inspinia').controller('terActivityCheckQueryCtrl',function($scope,$rootScope,$state,$filter,$log,$http,$stateParams,$compile,$uibModal,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//下拉框数据源
	$scope.terminalStates=[{text:"全部",value:""},{text:"已分配",value:1},{text:"已使用",value:2}];
	$scope.statusList=[{text:"全部",value:""},{text:"未激活",value:0},{text:"已激活",value:1}];
	$scope.standardStatusList=[{text:"全部",value:""},{text:"已达标",value:1},{text:"考核中",value:2},{text:"未达标",value:0}];
	$scope.sortList=[{text:"默认排序",value:""},{text:"考核剩余天数从高到低",value:"DESC"},{text:"考核剩余天数从低到高",value:"ASC"}];
	$scope.hasChildList = [{text:'是',value:'1'},{text:'否',value:'0'}];
    $scope.loginUserType =$rootScope.loginUserType;
    $scope.agentType =$rootScope.agentType;
	$scope.activityTypes=[{text:"全部",value:""}];
        for(var i=0; i<$scope.activityTypess.length; i++){
            if ($scope.oemType === 'PERAGENT'){
            	if($scope.activityTypess[i].sys_name=="欢乐返"
				||$scope.activityTypess[i].sys_name=="注册返300"){
                    $scope.activityTypes.push({value:$scope.activityTypess[i].sys_value,text:$scope.activityTypess[i].sys_name});
				}
            }else{
                $scope.activityTypes.push({value:$scope.activityTypess[i].sys_value,text:$scope.activityTypess[i].sys_name});
			}
        }

	$http.get('hardwareProduct/selectAllInfoByPn.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes = result;
		$scope.termianlTypes.splice(0,0,{hpId:"",typeName:"全部"});
	})

	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	//清空
	$scope.clear=function(){
		$scope.info={snStart:"",snEnd:"",openStatus:"",agentNo:$rootScope.entityId,type:"",activityTypeNo:"",
			standardStatus:"",status:"",sort:"",hasChild:"1",agentNoTwo:""};
	};
	$scope.clear();
	$scope.gridOptions={                           //配置表格
		data:'tdata',
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,                //分页数量
		columnDefs:[                           //表格数据
			{field: 'sn',displayName: 'SN号',width: 150,sortable: false,editable:true,cellTemplate:
					'<a class="lh30" target="_blank" ui-sref="terminalQueryDetail({termId:row.entity.id})">{{row.entity.sn}}</a>'},
			{ field: 'type',displayName:'硬件产品种类',width:150},
			{ field: 'bpName',displayName:'业务产品',width:150},
			{ field: 'agentNoTwo',displayName:'二级代理商编号',width:150},
			{ field: 'agentNameTwo',displayName:'二级代理商名称',width:150},
			{ field: 'agentNo',displayName:'所属代理商编号',width:150},
			{ field: 'agentName',displayName:'所属代理商名称' ,width:150},
			{ field: 'checkTime',displayName:'考核日期',width: 150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
			{ field: 'dueDays',displayName:'考核剩余天数',width: 150},
			{ field: 'status',displayName:'机具激活状态',width: 150,cellFilter:"terminalFormatDropping:"+angular.toJson($scope.statusList)},
			{ field: 'openStatus',displayName:'机具状态',width: 150,cellFilter:"terminalFormatDropping:"+angular.toJson($scope.terminalStates)},
			{ field: 'activityTypeNo',displayName:'欢乐返子类型',width: 150},
		],
		onRegisterApi: function(gridApi) {                //选中行配置
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.selectInfo();
			});
		}
	};
	//导出
    $scope.exportInfo = function () {
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
					$scope.exportInfoClick("terminalInfo/exportTerActivityCheck.do",{"info":angular.toJson($scope.info)});
				}
			});
	};
	//子类型
	$scope.checkActivityCode = function(){
		$scope.typeNos=[{value:"",text:"全部"}];
		$http.post("agentInfo/getAgentActivity").success(function (data) {
			if(data.status){
				for(var i=0; i<data.list.length; i++){
					$scope.typeNos.push({value:data.list[i].activityTypeNo,text:data.list[i].activityTypeName});
				}
			}
		})
	};
	$scope.checkActivityCode();

	 //模糊条件查询
	 $scope.selectInfo=function(){
		 $http.post("terminalInfo/selectTerActivityCheck.do",
				 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(result){
				//响应成功
			 	if(!result.result){
					return;
				}
				$scope.tdata = result.result;
				$scope.gridOptions.totalItems = result.totalCount;
		});
	 }

	//业务产品
	$scope.productTypes = [];
	$http.get('businessProductDefine/selectAllInfo.do').success(function(largeLoad) {
			if(!largeLoad)
				return;
			$scope.productTypes = largeLoad;
			$scope.productTypes.splice(0,0,{bpId:"-1",bpName:"全部"});
		});
	 
	//下拉模糊查询tgh227====
	$scope.agent = [];
	$http.post("agentInfo/selectAllInfo").success(function(msg){
 			//响应成功
 	   	for(var i=0; i<msg.length; i++){
 	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
 	   	}
 	});

	/**
	 * 查询当前登录代理商下所有二级代理商
	 */
	// $scope.agentTwo = [];
	// $http.get('agentInfo/selectAllInfoBelong')
	// 	.success(function(result){
	// 		if (result){
	// 			for(var i=0; i<result.length; i++){
	// 				$scope.agentTwo.push({value:result[i].agentNoTwo,text:result[i].agentNameTwo});
	// 			}
	// 		}
	// 	});

	$scope.dtr= true;
	$scope.agt_ch = function(){
		if(!$scope.baseInfo.agentName){
			$scope.dtr= true;
		}else{
			$scope.dtr= false;
		}
	}
});


/**
 * 机具查询
 */
angular.module('inspinia').controller('threeTerminalQueryCtrl',function($scope,$rootScope,$state,$filter,$log,$http,$stateParams,$compile,$uibModal,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//下拉框数据源
	$scope.bools=[{text:"是",value:1},{text:"否",value:0}];
	$scope.terminalStates=[{text:"全部",value:-1},{text:"已分配",value:1},{text:"已使用",value:2}];
	var agentId = -1;
    $scope.loginUserType =$rootScope.loginUserType;
    $scope.agentType =$rootScope.agentType;
    $scope.untiedFlag = false;
    $scope.bindFlag = false;
    $scope.showButton=function(){
    	$http.post('agentFunction/showButton').success(function(result){
			$scope.untiedFlag = result.untiedFlag;
			$scope.bindFlag = result.bindFlag;
    	})
    }
    $scope.showButton();

	$scope.activityTypes=[{text:"全部",value:""}];
    // if ($scope.$root.oemType === "SQIANBAO"){
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
    // }else{
    //     $scope.activityTypes.push({value: '7',text:"欢乐返"});
    //     $scope.activityTypes.push({value: '8',text:"欢乐返-循环送"});
    //     $scope.activityTypes.push({value: '11',text:"注册返"});
    // }

	$http.get('hardwareProduct/threeSelectByCondition.do')
	.success(function(result){
		if(!result)
			return;
		$scope.termianlTypes=result;
		$scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
	})

    // 当前登录代理商业务范围是否有设置
    $scope.showSelect = true;
    $scope.initTeamId = "-1";
    $scope.merTeams = [{text:"全部",value:"-1"},{text:"盛钱包",value:"200010"},{text:"盛POS",value:"100070"}];
    $scope.getAccesTeamId = function (){
        $http.get('userAction/getAccessTeamId').success(function (result) {
            if(result.status){
                $scope.info.merTeamId = result.accessTeamId;
                $scope.initTeamId = result.accessTeamId;
            }else {
                $scope.showSelect = false;
            }
        });
    }
    $scope.getAccesTeamId();
    /*$scope.selectMerTeams = function () {
        $http.get('teamInfo/getTeams')
            .success(function(data) {
                if(!data){
                    return;
                }
                $scope.merTeams=data;
                $scope.merTeams.splice(0,0,{value:"-1",text:"全部"});

                $http.get('userAction/getAccessTeamId').success(function (result) {
                    if(result.status){
                        $scope.info.merTeamId = result.accessTeamId;
                        $scope.initTeamId = result.accessTeamId;
                    }else {
                        $scope.showSelect = false;
                    }
                });
            });
    }
    $scope.selectMerTeams();*/

	$http.get('terminalInfo/getCurrentAgent.do')
	.success(function(data){
		if(!data)
			return;
		agentId = data.agentId;
	})
	
	//如果是人人代理,且不是一级,不显
	if ($rootScope.agentType == 11 && $scope.entityAgentLevel != 1) {
		//$scope.levelStatus = 1;
	}
	
	$scope.teamEntryIdList = [{text:'全部',value:''}];
	 $scope.getTeamEntryIdList= function(){
	    	var merTeamId = $scope.info.merTeamId;
	    	$http.get('teamInfoEntry/getTeamEntryIdList?merTeamId='+merTeamId).success(function (result) {
				if(result.status && result.data.length>0){
					var d = result.data;
					for (var i = 0; i < d.length; i++) {
						var team = d[i];
						$scope.teamEntryIdList.push({text:team.teamEntryName,value:team.teamEntryId});
						
					}
				}else {
					$scope.teamEntryIdList = [{text:'全部',value:''}];
				}
				$scope.info.teamEntryId='';
			});
	    }
	
	$scope.info={teamEntryId:'',merTeamId:"-1",snStart:"",snEnd:"",sn:"",merchantName:"",openStatus:-1,agentName:$rootScope.entityId,type:"-1",psamNo:"",psamNo1:"",bool:1,activityType:"",userCode:"",realName:""};
	 $scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$http.get('terminalInfo/threeSelectByCondition.do')
	.success(function(data){
		if(!data)
			return;
		$scope.tdata = data.result; 
		$scope.gridOptions.totalItems = data.totalCount;
		angular.forEach(data.result, function(data){
			//isSend: 0:详情   1：下发   2：回收
			if (data.openStatus!=1) {
				data.isSend = 0;
			} else if (data.agentNo==agentId) {
				//当前代理商，操作栏显示机具下发按钮
				data.isSend = 1;

			} else {
				var t_agentNo = data.agentNo;   //20
				var t_agentNode = data.agentNode;  //0-10-20
				var reg=new RegExp(agentId+"-"+t_agentNo+"-$");   //是否以10-20结尾
				 
				if (reg.test(t_agentNode)) {
					//直接下级，操作栏显示机具回收
					data.isSend = 2;
				} else {
					//隔代下级或更下级，操作栏显示详情
					data.isSend = 0;
				}
			}
		});
	})
    if ($rootScope.agentType == 11) {
        $scope.gridOptions={                           //配置表格
            data:'tdata',
            paginationPageSize:10,                  //分页数量
            paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
            useExternalPagination: true,                //分页数量
            enableHorizontalScrollbar: 0,        //去掉滚动条
            enableVerticalScrollbar : 0,         //去掉滚动条
            columnDefs:[                           //表格数据
                { field: 'id',displayName:'序号'},
                { field: 'sn',displayName:'机具SN号'},
                { field: 'merchantName',displayName:'商户简称' },
                { field: 'agentName',displayName:'所分配代理商' },
                { field: 'userCode',displayName:'所属盟主编号' },
                { field: 'realName',displayName:'所属盟主姓名' },
                { field: 'terminalId',displayName:'终端号' },
                { field: 'openStatus',displayName:'机具状态',
                    cellFilter:"formatDropping:[{text:'已入库',value:0},{text:'已分配',value:1},{text:'已使用',value:2}]"
                },
                { field: 'startTime',displayName:'启用时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
                },
                { field: 'activityType',displayName:'机具活动类型',cellFilter:"terminalFormatDropping:"+angular.toJson($scope.activityTypes)}//,
                /*{  field: 'id',displayName:'操作',
                    cellTemplate:
                   '<div class="lh30">'
                    +'<a ng-show="grid.appScope.hasPermit(\'terminalQueryDetail\')" ui-sref="terminalQueryDetail({termId:row.entity.id})">详情</a>'
                    +'<a ng-show="grid.appScope.hasPermit(\'sendTerminal\') &&row.entity.openStatus==1&& row.entity.status==1" ng-click="grid.appScope.sendTerminal(row.entity)"> | 机具下发</a>'
                    +'<a ng-show="grid.appScope.hasPermit(\'recoveryTerminal\') && row.entity.openStatus==1&& row.entity.status==2 && '+$scope.loginUserType+'==1" ng-click="grid.appScope.recoveryTerminal(row.entity)"> | 机具回收</a>'
                    +'</div>'


                }*/
            ],
            onRegisterApi: function(gridApi) {                //选中行配置
                $scope.gridApi = gridApi;
                //全选
                $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                    if(rows[0].isSelected){
                        $scope.testRow = rows[0].entity;
                        for(var i=0;i<rows.length;i++){
                            rowList[rows[i].entity.id]=rows[i].entity;
                        }
                    }else{
                        rowList={};
                    }
                })
                //单选
                $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
                    if(row.isSelected){
                        $scope.testRow = row.entity;
                        rowList[row.entity.id]=row.entity;
                    }else{
                        delete rowList[row.entity.id];
                    }
                })
                $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.paginationOptions.pageNo = newPage;
                    $scope.paginationOptions.pageSize = pageSize;
                    $scope.selectInfo();
                    rowList={};
                });
            }
        };
    }else{
        $scope.gridOptions={                           //配置表格
            data:'tdata',
            paginationPageSize:10,                  //分页数量
            paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
            useExternalPagination: true,                //分页数量
            enableHorizontalScrollbar: 0,        //去掉滚动条
            enableVerticalScrollbar : 0,         //去掉滚动条
            columnDefs:[                           //表格数据
                { field: 'id',displayName:'序号'},
                { field: 'sn',displayName:'机具SN号'},
                { field: 'merchantName',displayName:'商户简称' },
                { field: 'merGroup',displayName:'商户组织',width:110},
                { field: 'agentName',displayName:'所分配代理商' },
                { field: 'terminalId',displayName:'终端号' },
                { field: 'openStatus',displayName:'机具状态',
                    cellFilter:"formatDropping:[{text:'已入库',value:0},{text:'已分配',value:1},{text:'已使用',value:2}]"
                },
                { field: 'startTime',displayName:'启用时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
                },
                { field: 'activityType',displayName:'机具活动类型',cellFilter:"terminalFormatDropping:"+angular.toJson($scope.activityTypes)},
               /* { field: 'id',displayName:'操作',width: 240,
                    cellTemplate:
                    '<div class="lh30" ng-if="row.entity.isSend==0">'
                    +'<a ng-show="row.entity.openStatus==2 && row.entity.agentType != 11 && grid.appScope.untiedFlag && row.entity.belongAgent" ng-click="grid.appScope.untiedById(row.entity.id)">解绑 | </a>'
                    +'<a ng-show="row.entity.openStatus==1 && row.entity.agentType != 11 && grid.appScope.bindFlag" ng-click="grid.appScope.bindingTerminal(row.entity.id,row.entity.sn)">绑定 | </a>'
                    +'<a ng-show="grid.appScope.hasPermit(\'terminalQueryDetail\')" ui-sref="terminalQueryDetail({termId:row.entity.id})">详情</a>'
                    +'</div>'
                    +'<div class="lh30" ng-if="row.entity.isSend==1">'
                    +'<a ng-show="row.entity.openStatus==2 && row.entity.agentType != 11 && grid.appScope.untiedFlag && row.entity.belongAgent" ng-click="grid.appScope.untiedById(row.entity.id)">解绑 | </a>'
                    +'<a ng-show="row.entity.openStatus==1 && row.entity.agentType != 11 && grid.appScope.bindFlag" ng-click="grid.appScope.bindingTerminal(row.entity.id,row.entity.sn)">绑定 | </a>'
                    +'<a ng-show="grid.appScope.hasPermit(\'terminalQueryDetail\')" ui-sref="terminalQueryDetail({termId:row.entity.id})">详情</a>'
                    +'<a ng-show="grid.appScope.hasPermit(\'sendTerminal\')  && row.entity.status != 1" ng-click="grid.appScope.sendTerminal(row.entity)"> | 机具下发</a>'
                    +'</div>'
                    +'<div class="lh30" ng-if="row.entity.isSend==2">'
                    +'<a ng-show="row.entity.openStatus==2 && row.entity.agentType != 11 && grid.appScope.untiedFlag && row.entity.belongAgent" ng-click="grid.appScope.untiedById(row.entity.id)">解绑 | </a>'
                    +'<a ng-show="row.entity.openStatus==1 && row.entity.agentType != 11 && grid.appScope.bindFlag" ng-click="grid.appScope.bindingTerminal(row.entity.id,row.entity.sn)">绑定 | </a>'
                    +'<a ng-show="grid.appScope.hasPermit(\'terminalQueryDetail\')" ui-sref="terminalQueryDetail({termId:row.entity.id})">详情</a>'
                    +'<a ng-show="grid.appScope.hasPermit(\'recoveryTerminal\') && (row.entity.agentType != 11 || '+$scope.loginUserType+'==1)" ng-click="grid.appScope.recoveryTerminal(row.entity)"> | 机具回收</a>'
                    +'</div>'

                }*/
            ],
            onRegisterApi: function(gridApi) {                //选中行配置
                $scope.gridApi = gridApi;
                //全选
                $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                    if(rows[0].isSelected){
                        $scope.testRow = rows[0].entity;
                        for(var i=0;i<rows.length;i++){
                            rowList[rows[i].entity.id]=rows[i].entity;
                        }
                    }else{
                        rowList={};
                    }
                })
                //单选
                $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
                    if(row.isSelected){
                        $scope.testRow = row.entity;
                        rowList[row.entity.id]=row.entity;
                    }else{
                        delete rowList[row.entity.id];
                    }
                })
                $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.paginationOptions.pageNo = newPage;
                    $scope.paginationOptions.pageSize = pageSize;
                    $scope.selectInfo();
                    rowList={};
                });
            }
        };
    }
	

	 var rowList={};
	 //清空
	 $scope.clear=function(){
		 $scope.teamEntryIdList = [{text:'全部',value:''}];
		 $scope.info={teamEntryId:'',merTeamId:$scope.initTeamId,snStart:"",snEnd:"",sn:"",merchantName:"",openStatus:-1,agentName:$rootScope.entityId,type:"-1",psamNo:"",psamNo1:"",bool:1,activityType:""};
	 }
	//查询当前登录代理商所有的商户
	    $scope.merchantInfos = null;
		$http.get('terminalInfo/selectAllMerchantInfo.do')
		.success(function(result){
			if(!result){
				return;
			}
			$scope.merchantInfos = result;
		})
		$scope.merId = null;
		$scope.onChange=function(val){
			$scope.info.merchant_name = val;
			angular.forEach($scope.merchantInfos, function(data){
				if (val == data.merchant_name) {
					$scope.merId = data.merchant_no;
					$scope.getMbpByMerId();
				}
			});
		}
	//解绑
	$scope.untiedById=function(id){
		var modalScope = $scope.$new();
		modalScope.id=id;
		var modalInstance = $uibModal.open({
			templateUrl : 'views/terminal/untied.html',  //指向上面创建的视图
			controller : 'terminalQueryUnbundlingByIdCtrl',// 初始化模态范围
			scope:modalScope
		})
		modalScope.modalInstance=modalInstance;
		modalInstance.result.then(function(selectedItem){
			$http.post("terminalInfo/untiedById.do", "id="+id, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				//响应成功
				if(msg.status){
	 				$scope.notice(msg.msg);
	 				$scope.selectInfo();
				}else{
					$scope.notice(msg.msg);
				}
			});
		},function(){
			$log.info('取消: ' + new Date())
		})
	};
	
	//绑定机具
	$scope.bindingTerminal=function(id,sn){
		$scope.info.merchant_name = "";
		$scope.info.merchant_no = "";
		$scope.merId = "";
		$scope.info.merBpId = "";
		var modalScope = $scope.$new();
		modalScope.id=id;
		var modalInstance = $uibModal.open({
			templateUrl : 'views/terminal/bindingTerminal.html',  //指向上面创建的视图
			controller : 'bindingTerminalCtrl',// 初始化模态范围
			scope:modalScope
		})
		modalScope.modalInstance=modalInstance;
		modalInstance.result.then(function(selectedItem){
			var data={"merNo":$scope.merId,"sn":sn,"id":id,"bpId":$scope.info.merBpId};
			$http.post("terminalInfo/bindingTerminal.do", "param="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	   		 .success(function(msg){
				//响应成功
				if(msg.status){
	 				$scope.notice(msg.msg);
	 				$scope.selectInfo();
				}else{
					$scope.notice(msg.msg);
				}
			});
			$scope.info.merchant_name = "";
		},function(){
			$log.info('取消: ' + new Date())
		})
	}
	
	$scope.getMbpByMerId=function(){
		 $scope.merBPLists = [];
		 $scope.info.merBpId = -1;
		 $http.post('terminalInfo/getMbpByMerId','merId='+$scope.merId,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(datas){
			 if(datas && datas.result){
				 var a = [];
				 if(datas.list && datas.list.length){
					 angular.forEach(datas.list, function(data){
						 a.push({value:data.bpId,text:data.bpName || '业务产品'});
					 });
				 }else{
					 $scope.notice("该商户没有业务产品");
				 }
				 $scope.merBPLists = a;
				 $scope.info.merBpId = -1;
			 }else{
				 $scope.notice(datas && datas.msg ? datas.msg : "获取商户业务产品列表失败");
			 }
		 });
	}
	 //模糊条件查询
	 $scope.selectInfo=function(){
		 $http.post("terminalInfo/threeSelectByCondition.do",
				 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(result){
				//响应成功
			 	if(!result.result)
			 		return;
				$scope.tdata = result.list.result; 
				$scope.gridOptions.totalItems = result.list.totalCount;
				
				angular.forEach($scope.tdata, function(data){
					//isSend: 0:详情   1：下发   2：回收
					if (data.openStatus!=1) {
						data.isSend = 0;
					} else if (data.agentNo==agentId) {
						//当前代理商，操作栏显示机具下发按钮
						data.isSend = 1;
					} else {
						var t_agentNo = data.agentNo;   //20
						var t_agentNode = data.agentNode;  //0-10-20
						var reg=new RegExp(agentId+"-"+t_agentNo+"-$");   //是否以10-20结尾
						 
						if (reg.test(t_agentNode)) {
							//直接下级，操作栏显示机具回收
							data.isSend = 2;
						} else {
							//隔代下级或更下级，操作栏显示详情
							data.isSend = 0;
						}
					}
				});
		});
	 }
	 
	 //机具下发
	 $scope.sendTerminal=function(tobj){
		 var num=0;
		 var disList=[];
		 if (typeof(tobj)=='undefined') {
			 disList=angular.copy(rowList);
			 for(index in disList ){
                 if($rootScope.agentType == 11){
                     if(disList[index].status!=1 ){
                         delete disList[disList[index].id];
                         continue;
                     }
                 }else{
                     if(disList[index].openStatus!="1" || disList[index].isSend!=1){
                         delete disList[disList[index].id];
                         continue;
                     }
                 }

				 num++;
			 }
		 } else {
			 num = 1;
			 disList[0] = tobj;
		 }
		 if(num!=0){

             if($rootScope.agentType == 11){
                 var modalScope = $scope.$new();
                 modalScope.pauser = [];
                 $http.get("perAgent/selectChildPaUser.do")//当前代理商的所有下级代理商
                     .success(function(msg){
                         //响应成功
                         for(var i=0; i<msg.paUserList.length; i++){
                             modalScope.pauser.push({value:msg.paUserList[i].userCode,text:msg.paUserList[i].realName+"("+msg.paUserList[i].userCode+")"});
                         }
                     });
                 modalScope.info={number:num,userCode:-1};
                 var modalInstance = $uibModal.open({
                     animation: true,
                     templateUrl: 'adminPerAgentModalContentBatchOne.html',
                     controller: 'distributionTerminalCtrl',
                     size: "lg",
                     scope:modalScope
                 });
                 modalScope.modalInstance=modalInstance;
                 modalInstance.result.then(function(selectedItem){
                     var list=[];
                     var s=0;
                     for(index in disList){
                         if(s==num){
                             break;
                         }else{
                             list[s]=disList[index];
                             if (list[s].callbackLock == 1) {
                     			$scope.notice("机具被锁定,不能下发");
                     			return;
                     		 }
                             s++;
                         }
                     }
                     var data={"userCode":selectedItem.info.userCode,"list":list};
                     if(selectedItem.info.userCode!=-1){
                         $http.post("terminalInfo/distributionTerminal.do",
                             "param="+angular.toJson(data)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
                             {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                             .success(function(datas){
                                 //响应成功
                                 if(datas.result){
                                     $scope.tdata = datas.object.result;
                                     $scope.gridOptions.totalItems = datas.object.totalCount;
                                     angular.forEach($scope.tdata, function(data){
                                         //isSend: 0:详情   1：下发   2：回收
                                         if (data.openStatus!=1) {
                                             data.isSend = 0;
                                         } else if (data.agentNo==agentId) {
                                             //当前代理商，操作栏显示机具下发按钮
                                             data.isSend = 1;
                                         } else {
                                             var t_agentNo = data.agentNo;   //20
                                             var t_agentNode = data.agentNode;  //0-10-20
                                             var reg=new RegExp(agentId+"-"+t_agentNo+"-$");   //是否以10-20结尾

                                             if (reg.test(t_agentNode)) {
                                                 //直接下级，操作栏显示机具回收
                                                 data.isSend = 2;
                                             } else {
                                                 //隔代下级或更下级，操作栏显示详情
                                                 data.isSend = 0;
                                             }
                                         }
                                     });
                                     $scope.notice("操作成功");
                                 }else{
                                     $scope.notice("操作失败");
                                 }
                             });
                     }else{
                     }
                 },function(){
                     $log.info('取消: ' + new Date())
                 })
             }else{
                 var modalScope = $scope.$new();
                 modalScope.agent = [];
                 $http.get("agentInfo/selectChildAgentByAgentNo.do?agentNo="+agentId)
                     .success(function(msg){
                         //响应成功
                         for(var i=0; i<msg.agentList.length; i++){
                             modalScope.agent.push({value:msg.agentList[i].agentNo,text:msg.agentList[i].agentName});
                         }
                     });
                 modalScope.info={number:num,agentNo:-1};
                 var modalInstance = $uibModal.open({
                     animation: true,
                     templateUrl: 'adminModalContent.html',
                     controller: 'distributionTerminalCtrl',
                     size: "lg",
                     scope:modalScope
                 });
                 modalScope.modalInstance=modalInstance;
                 modalInstance.result.then(function(selectedItem){
                     var list=[];
                     var s=0;
                     for(index in disList){
                         if(s==num){
                             break;
                         }else{
                             list[s]=disList[index];
                             s++;
                         }
                     }
                     var data={"agentNo":selectedItem.info.agentNo,"list":list};
                     if(selectedItem.info.agentNo!=-1){
                         $http.post("terminalInfo/distributionTerminal.do",
                             "param="+angular.toJson(data)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
                             {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                             .success(function(datas){
                                 //响应成功
                                 if(datas.result){
                                     $scope.tdata = datas.object.result;
                                     $scope.gridOptions.totalItems = datas.object.totalCount;
                                     angular.forEach($scope.tdata, function(data){
                                         //isSend: 0:详情   1：下发   2：回收
                                         if (data.openStatus!=1) {
                                             data.isSend = 0;
                                         } else if (data.agentNo==agentId) {
                                             //当前代理商，操作栏显示机具下发按钮
                                             data.isSend = 1;
                                         } else {
                                             var t_agentNo = data.agentNo;   //20
                                             var t_agentNode = data.agentNode;  //0-10-20
                                             var reg=new RegExp(agentId+"-"+t_agentNo+"-$");   //是否以10-20结尾

                                             if (reg.test(t_agentNode)) {
                                                 //直接下级，操作栏显示机具回收
                                                 data.isSend = 2;
                                             } else {
                                                 //隔代下级或更下级，操作栏显示详情
                                                 data.isSend = 0;
                                             }
                                         }
                                     });
                                     $scope.notice("操作成功");
                                 }else{
                                     $scope.notice("操作失败");
                                 }
                             });
                     }else{
                     }
                 },function(){
                     $log.info('取消: ' + new Date())
                 })
			 }

		 } else {
			 $scope.notice("当前选择的机具不能下发");
		 }
	}
	 //批量下发=======================================
	 $scope.info.totalNum = "";
	 $scope.sendTerminalBatch=function(tobj){
		 var num=0;
		 var disList=[];
		 if (typeof(tobj)=='undefined') {
			 disList=angular.copy(rowList);
			 for(index in disList ){
                 if($rootScope.agentType == 11){
                	 if (disList[index].callbackLock == 1) {
             			$scope.notice("机具被锁定,不能下发");
             			return;
             		}
                     if(disList[index].status!=1 ){
                         delete disList[disList[index].id];
                         continue;
                     }

                 }else{
                     if(disList[index].openStatus!="1" || disList[index].isSend!=1){
                         delete disList[disList[index].id];
                         continue;
                     }

                 }
                 num++;

			 }
		 } else {
			 num = 1;
			 disList[0] = tobj;
		 }
//		 if(num!=0){
		 if($rootScope.agentType == 11){
             var modalScope = $scope.$new();
             modalScope.pauser = [];
             $http.get("perAgent/selectChildPaUser.do")//当前代理商的所有下级代理商
                 .success(function(msg){
                     //响应成功
                     for(var i=0; i<msg.paUserList.length; i++){
                         modalScope.pauser.push({value:msg.paUserList[i].userCode,text:msg.paUserList[i].realName+"("+msg.paUserList[i].userCode+")"});
                     }
                 });
             modalScope.info={number:num,userCode:-1};
             var modalInstance = $uibModal.open({
                 animation: true,
                 templateUrl: 'adminPerAgentModalContentBatch.html',
                 controller: 'distributionTerminalCtrl',
                 size: "lg",
                 scope:modalScope
             });
             modalScope.modalInstance=modalInstance;

             modalInstance.result.then(function(selectedItem){
                 $scope.info.totalNum = selectedItem.totalNum;
                 var list=[];
                 var s=0;
                 for(index in disList){
                     if(s==num){
                         break;
                     }else{
                         list[s]=disList[index];
                         s++;
                     }
                 }
                 var data={"sendType":"paUser","userCode":selectedItem.info.userCode,"list":list,"snStart1":selectedItem.info.snStart1,"snEnd1":selectedItem.info.snEnd1};
                 if(selectedItem.info.userCode!=-1){
                     $http.post("terminalInfo/distributionTerminalBatch.do",
                         "param="+angular.toJson(data)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
                         {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                         .success(function(datas){
                             //响应成功
                             if(datas.result){
                                 $scope.tdata = datas.object.result;
                                 $scope.gridOptions.totalItems = datas.object.totalCount;
                                 angular.forEach($scope.tdata, function(data){
                                     //isSend: 0:详情   1：下发   2：回收
                                     if (data.openStatus!=1) {
                                         data.isSend = 0;
                                     } else if (data.agentNo==agentId) {
                                         //当前代理商，操作栏显示机具下发按钮
                                         data.isSend = 1;
                                     } else {
                                         var t_agentNo = data.agentNo;   //20
                                         var t_agentNode = data.agentNode;  //0-10-20
                                         var reg=new RegExp(agentId+"-"+t_agentNo+"-$");   //是否以10-20结尾

                                         if (reg.test(t_agentNode)) {
                                             //直接下级，操作栏显示机具回收
                                             data.isSend = 2;
                                         } else {
                                             //隔代下级或更下级，操作栏显示详情
                                             data.isSend = 0;
                                         }
                                     }
                                 });
                                 $scope.notice(datas.message);
                             }else{
                                 $scope.notice(datas.message);
                             }
                         });
                 }else{
                 }
             },function(){
                 $log.info('取消: ' + new Date())
             })

		 }else{
             var modalScope = $scope.$new();
             modalScope.agent = [];
             $http.get("agentInfo/selectChildAgentByAgentNo.do?agentNo="+agentId)//当前代理商的所有下级代理商
                 .success(function(msg){
                     //响应成功
                     for(var i=0; i<msg.agentList.length; i++){
                         modalScope.agent.push({value:msg.agentList[i].agentNo,text:msg.agentList[i].agentName});
                     }
                 });
             modalScope.info={number:num,agentNo:-1};
             var modalInstance = $uibModal.open({
                 animation: true,
                 templateUrl: 'adminModalContentBatch.html',
                 controller: 'distributionTerminalCtrl',
                 size: "lg",
                 scope:modalScope
             });
             modalScope.modalInstance=modalInstance;
             modalInstance.result.then(function(selectedItem){
                 $scope.info.totalNum = selectedItem.totalNum;
                 var list=[];
                 var s=0;
                 for(index in disList){
                     if(s==num){
                         break;
                     }else{
                         list[s]=disList[index];
                         s++;
                     }
                 }
                 var data={"agentNo":selectedItem.info.agentNo,"list":list,"snStart1":selectedItem.info.snStart1,"snEnd1":selectedItem.info.snEnd1};
                 if(selectedItem.info.agentNo!=-1){
                     $http.post("terminalInfo/distributionTerminalBatch.do",
                         "param="+angular.toJson(data)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
                         {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                         .success(function(datas){
                             //响应成功
                             if(datas.result){
                                 $scope.tdata = datas.object.result;
                                 $scope.gridOptions.totalItems = datas.object.totalCount;
                                 angular.forEach($scope.tdata, function(data){
                                     //isSend: 0:详情   1：下发   2：回收
                                     if (data.openStatus!=1) {
                                         data.isSend = 0;
                                     } else if (data.agentNo==agentId) {
                                         //当前代理商，操作栏显示机具下发按钮
                                         data.isSend = 1;
                                     } else {
                                         var t_agentNo = data.agentNo;   //20
                                         var t_agentNode = data.agentNode;  //0-10-20
                                         var reg=new RegExp(agentId+"-"+t_agentNo+"-$");   //是否以10-20结尾

                                         if (reg.test(t_agentNode)) {
                                             //直接下级，操作栏显示机具回收
                                             data.isSend = 2;
                                         } else {
                                             //隔代下级或更下级，操作栏显示详情
                                             data.isSend = 0;
                                         }
                                     }
                                 });
                                 $scope.notice(datas.message);
                             }else{
                                 $scope.notice(datas.message);
                             }
                         });
                 }else{
                 }
             },function(){
                 $log.info('取消: ' + new Date())
             })
		 }

//		 } else {
//			 $scope.notice("当前选择的机具不能下发");
//		 }
	 }
	//机具回收
	$scope.recoveryTerminal=function(tobj){
		//var disList=angular.copy(rowList);
	 	var num=0;
	 	var disList=[];
		if (typeof(tobj)=='undefined') {
			disList=angular.copy(rowList);
            if($rootScope.agentType == 11){
                for(index in disList ){
                    if(disList[index].status!=2){
                        delete disList[disList[index].id];
                        continue;
                    }
                    num++;
                }
            }else{
                for(index in disList ){
                    if(disList[index].openStatus!="1"|| disList[index].isSend!=2){
                        delete disList[disList[index].id];
                        continue;
                    }
                    num++;
                }
            }

		} else {
			 num = 1;
			 disList[0] = tobj;
		}
		if (num > 0) {
			var modalScope = $scope.$new();
			var modalInstance = $uibModal.open({
				templateUrl : 'views/terminal/solutionModal.html',  //指向上面创建的视图
				controller : 'recoveryTerminalCtrl',// 初始化模态范围
				scope:modalScope
			})
			modalScope.modalInstance=modalInstance;
			modalInstance.result.then(function(selectedItem){  
				var list=[];
				var s=0;
				for(index in disList){
					if(s==num){
						break;
					}else{
						list[s]=disList[index];
						if (list[s].callbackLock == 1) {
							$scope.notice("机具被锁定,不能回收");
							return;
						}
						s++;
					}
				}
				var data={"list":list};
				$http.post("terminalInfo/recoveryTerminal.do",
						"param="+angular.toJson(data)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function(datas){
							//响应成功
							if(datas.result){
								$scope.tdata = datas.object.result; 
								$scope.gridOptions.totalItems = datas.object.totalCount;
								angular.forEach($scope.tdata, function(data){
									//isSend: 0:详情   1：下发   2：回收
									if (data.openStatus!=1) {
										data.isSend = 0;
									} else if (data.agentNo==agentId) {
										//当前代理商，操作栏显示机具下发按钮
										data.isSend = 1;

									} else {
										var t_agentNo = data.agentNo;   //20
										var t_agentNode = data.agentNode;  //0-10-20
										var reg=new RegExp(agentId+"-"+t_agentNo+"-$");   //是否以10-20结尾
										 
										if (reg.test(t_agentNode)) {
											//直接下级，操作栏显示机具回收
											data.isSend = 2;
										} else {
											//隔代下级或更下级，操作栏显示详情
											data.isSend = 0;
										}
									}
								});
								$scope.notice("操作成功");
							}else{
								$scope.notice("操作失败");
							}
						});
				},function(){
			    $log.info('取消: ' + new Date())
			})
		} else {
			$scope.notice("当前选择的机具不能回收");
		}
	}
	rowList=rowList;
	
	//下拉模糊查询tgh227====
	$scope.agent = [];
	$http.post("agentInfo/selectConfigInfo")
 	 .success(function(msg){
 		//$scope.agent.push({value:"",text:"全部"});
 			//响应成功
 	   	for(var i=0; i<msg.length; i++){
 	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
 	   	}
 	});

    $scope.userCodes = [{text:"全部",value:""}];
    $scope.userNames = [{text:"全部",value:""}];
    $http.post("terminalInfo/selectUserCodeLists")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
            	if(msg[i]!=null){
                    $scope.userCodes.push({value:msg[i].userCode,text:msg[i].userCode});
                    $scope.userNames.push({value:msg[i].realName,text:msg[i].realName});
				}

               // $.alert("666");
            }
        });
//	$scope.dtr= true;
//	$scope.agt_ch = function(){
//			if(!$scope.info.agentName){
//				$scope.dtr= true;
//			}else{
//				$scope.dtr= false;
//			}
//		}
	//========
	
	//批量下发机具
    $scope.exportKey = function(){
    	$("#exportKeyModal").modal("show");
    }
    $scope.cancel = function(){
    	$("#exportKeyModal").modal("hide");
    }
	
});

angular.module('inspinia').controller('recoveryTerminalCtrl',function($scope){
	$scope.title="机具回收";
	$scope.message="确定回收机具吗？";
	$scope.solutionModalClose=function(){
		$scope.modalInstance.dismiss();
	}
	 
	$scope.solutionModalOk=function(){
		$scope.modalInstance.close($scope);
	}
});

/////////////////////////////////
angular.module('inspinia').controller('distributionTerminalCtrl',function($scope,$uibModalInstance){
	 $scope.solutionModalClose=function(){
		 $uibModalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $uibModalInstance.close($scope);
		 $scope.gridApi.selection.clearSelectedRows();//20170223清空选择
	 }
	 
	 //批量下发机具台数统计
	 $scope.totalNum = 0;
	 $scope.errorMsg = "";
	 $scope.snChange = function(){
		 var reg = /^(.*?)(\d+)$/;
		 if(!($scope.info.snStart1 &&  $scope.info.snEnd1)){
			 return;
		 }
		 var startExec = reg.exec($scope.info.snStart1);
		 var endExec = reg.exec($scope.info.snEnd1);
		 if($scope.info.snStart1.length!=$scope.info.snEnd1.length || !startExec || !endExec || startExec[1] != endExec[1]){
			 $scope.errorMsg = "输入格式错误";
			 return;
		 }else{
			 $scope.errorMsg = "";
		 }
		 $scope.totalNum = endExec[2] - startExec[2] + 1;
		 
	 }
	 
//	 var reg = /^(.*?)(\d+)$/;
//	 if (selectedItem.info.snStart1.length!=selectedItem.info.snEnd1.length&&reg.exec(selectedItem.info.snEnd1)!=null&&reg.exec(selectedItem.info.snStart1)!=null
//			 &&reg.exec(selectedItem.info.snEnd1)[1]!=reg.exec(selectedItem.info.snStart1)[1]) {
//		 $scope.notice("输入格式错误");
//		 return;
//	}
//	 selectedItem.info.totalNum=reg.exec(selectedItem.info.snEnd1)[2]-reg.exec(selectedItem.info.snStart1)[2]+1;
//	 $scope.info.totalNum = selectedItem.info.totalNum;
//	 console.log(selectedItem.info.snEnd1)
//	 console.log(selectedItem.info.totalNum)
//	 console.log(reg.exec(selectedItem.info.snEnd1)[1])
});
angular.module('inspinia').controller('terminalQueryUnbundlingByIdCtrl',function($scope){
//	$scope.title="机具解绑";
	$scope.message="您正在进行解绑操作,解绑成功后的机具可被重新分配,是否继续操作?";
	 $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
		 $scope.gridApi.selection.clearSelectedRows();//20170223清空选择
	 }
});
angular.module('inspinia').controller('bindingTerminalCtrl',function($scope){
//	$scope.title="机具解绑";
//	$scope.message="您正在进行解绑操作,解绑成功后的机具可被重新分配,是否继续操作?";
	$scope.solutionModalClose=function(){
		$scope.modalInstance.dismiss();
	}
	
	$scope.solutionModalOk=function(){
		$scope.modalInstance.close($scope);
		$scope.gridApi.selection.clearSelectedRows();//20170223清空选择
	}
});

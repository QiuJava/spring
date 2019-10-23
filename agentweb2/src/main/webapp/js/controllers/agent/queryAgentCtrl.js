angular.module('inspinia',['uiSwitch']).controller("queryAgentCtrl", function($scope,$rootScope, $http, $state, $stateParams,i18nService,SweetAlert, $uibModal) {
//	$scope.level = [{text:'全部',value:""},{text:'1',value:1},{text:'2',value:2},{text:'3',value:3},{text:'4',value:4},
//	                {text:'5',value:5},{text:'6',value:6},{text:'7',value:7},{text:'8',value:8}];
	$scope.baseInfo = {hasSub:0,bool:'1',agentName:$rootScope.entityId};
	$scope.statusStr = '[{text:"关闭进件",value:"0"},{text:"正常",value:"1"},{text:"冻结",value:"2"}]';
	$scope.agentData = [];
	$scope.activityTypeNoS =  '[{text:"关闭进件",value:"0"},{text:"正常",value:"1"},{text:"冻结",value:"2"}]';

	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.bools = [{text:'包含',value:'1'},{text:'不包含',value:'0'},{text:'只显示直属下级',value:'2'}];
    $scope.showAgentWebShareSwitch = false;
    $scope.showAgentWebPromotionSwitch = false;
    $scope.showAgentWebCashBackSwitch = false;
    $scope.updateShareLevelData = {create_user:'', user_code:'', after_level:'',agentNo:''};
    $scope.types = true;
    if ($scope.oemType == 'REDEMACTIVE') {
    	$scope.types = false;
	}
    $scope.columnDefs = [
        {field: 'agentNo',displayName: '代理商编号',width: 120,pinnable: false,sortable: false},
        {field: 'agentName',displayName: '代理商名称',width: 120,pinnable: false,sortable: false},
        {field: 'profitSwitch',displayName: '分润日结功能',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
            '<switch class="switch-s switch-boss" ng-model="row.entity.profitSwitch" ng-change="grid.appScope.updateProfitSwitch(row)" /></span>'
        },
        {field: 'promotionSwitch',displayName: '代理商推广功能',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
                '<switch class="switch-s switch-boss" ng-model="row.entity.promotionSwitch" ng-change="grid.appScope.updatePromotionSwitch(row)" /></span>'
        },
        {field: 'cashBackSwitch',displayName: '欢乐返返现功能',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
                '<switch class="switch-s switch-boss" ng-model="row.entity.cashBackSwitch" ng-change="grid.appScope.updateCashBackSwitch(row)" /></span>'
        },
        {field: 'status',displayName: '状态',width:150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusStr},
        {field: 'fullPrizeSwitch',displayName: '满奖功能',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
        	'<switch ng-show="row.entity.parentId=='+$rootScope.entityId+' && row.entity.agentType != 11 && row.entity.showFullPrizeSwitch " class="switch-s switch-boss" ng-model="row.entity.fullPrizeSwitch" ng-change="grid.appScope.updateFullPrizeSwitch(row)" /></span>'
        },
        {field: 'notFullDeductSwitch',displayName: '不满扣功能',width:150,pinnable: false,sortable: false,editable:true,cellTemplate:
        	'<switch ng-show="row.entity.parentId=='+$rootScope.entityId+' && row.entity.agentType != 11 && row.entity.showNotFullDeductSwitch " class="switch-s switch-boss" ng-model="row.entity.notFullDeductSwitch" ng-change="grid.appScope.updateNotFullDeductSwitch(row)" /></span>'
        },
        {field: 'createDate',displayName: '创建时间',width:150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        // {field: 'agentLevel',displayName: '层级',width:150,pinnable: false,sortable: false},
        {field: 'parentId',displayName: '上级代理商编号',width:150,pinnable: false,sortable: false},
        {field: 'parentName',displayName: '上级代理商名称',width:150,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 240,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        '<a class="lh30" ng-show="grid.appScope.hasPermit(\'agent.agentDetail\')" ui-sref="agent.agentDetail({id:row.entity.agentNo,teamId:row.entity.teamId})">详情</a>'
        +'<span class="lh30">' +
        '<a ng-show="grid.appScope.hasPermit(\'agent.editAgents\') && row.entity.modifyFlag" ui-sref="agent.editAgent({agentNo:row.entity.agentNo,teamId:row.entity.teamId})" > | 修改</a></span>'
        //+'<span class="lh30"><a ng-show="grid.appScope.hasPermit(\'agent.agentDetele\') && row.entity.modifyFlag" ng-click="grid.appScope.deleteAgent(row.entity.agentNo)"> | 删除</a></span>'
        +'<a ng-show="row.entity.modifyFlag" ng-click="grid.appScope.restPwd({agentNo:row.entity.agentNo,mobilephone:row.entity.mobilephone,teamId:row.entity.teamId})"> | 密码重置</a>'
//        +'<a class="lh30" ng-show="row.entity.agentType == 11" ng-click="grid.appScope.toChangeShareLevel(row)"> | 调整分润等级</a>'
        }
    ];
	$scope.agentGrid = {
		data : "agentData",
		paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
		rowHeight:40,
        columnDefs: $scope.columnDefs,
	     onRegisterApi: function(gridApi) {              
	         $scope.gridApi = gridApi;
	         $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	         });
	     }
	};
	$http.post("sysDict/findAgentWebSwitch.do")
        .success(function (msg) {
            $scope.showAgentWebShareSwitch = msg.data && msg.data.agentWebShareSwitch;
            $scope.showAgentWebPromotionSwitch = msg.data && msg.data.agentWebPromotionSwitch;
            $scope.showAgentWebCashBackSwitch = msg.data && msg.data.agentWebCashBackSwitch;
            if(!$scope.showAgentWebShareSwitch && !$scope.showAgentWebPromotionSwitch&& !$scope.showAgentWebCashBackSwitch){
                $scope.agentGrid.columnDefs[2].visible = false;
                $scope.agentGrid.columnDefs[3].visible = false;
            }else if (!$scope.showAgentWebShareSwitch){
            	$scope.agentGrid.columnDefs[2].visible = false;
            }else if (!$scope.showAgentWebPromotionSwitch){
            	$scope.agentGrid.columnDefs[3].visible = false;
            }else if (!$scope.showAgentWebCashBackSwitch){
            	$scope.agentGrid.columnDefs[4].visible = false;
            }
        });

	//修改分润等级
    $scope.toChangeShareLevel = function (row) {
        $scope.updateShareLevelData.after_level="";
        var level = row.entity.shareLevel;
        $scope.shareLevelSelect = [];
        for(var i=1; i<= $scope.maxAgentLevel; i++){
                if(i <= level){
                    $scope.shareLevelSelect.push({text:"Lv"+i,value:i,show:true});
                }else {
                    $scope.shareLevelSelect.push({text:"Lv"+i,value:i});
                }
			}
        $scope.updateShareLevelData.user_code = row.entity.userCode;
        $scope.updateShareLevelData.agentNo = row.entity.agentNo;
        $scope.shareLevel = row.entity.shareLevel;
        $("#changeShareLevel").modal('show');
    };
    $scope.updateShareLevel = function () {
    	if($scope.updateShareLevelData.after_level == ''){
    		alert("请选择分润等级");
    		return;
		}
		if($scope.updateShareLevelData.after_level <= $scope.shareLevel){
    		alert("调整后的分润等级必须大于原来的分润等级");
    		return;
		}
        $http({
            url: 'perAgent/updateShareLevel',
            method: 'POST',
            data: $scope.updateShareLevelData
        }).success(function (data) {
            if (data.status){
                $scope.notice("操作成功");
                $scope.hideAllModel();
                $scope.query();
            }else{
                $scope.notice(data.msg);
            }
        });
    };
    
	$scope.batchUpdateProfitSwitch = function () {

        var currentSelection = $scope.gridApi.selection.getSelectedRows();
        if(currentSelection.length == 0){
            $scope.notice("请选择要批量修改分润日结功能的代理商.");
            return;
        }else{

            $scope.profitSwitchAgent = [];
            for(var key in currentSelection){
                $scope.profitSwitchAgent.push(currentSelection[key].agentNo);
            }
        }

        $scope.userModalInstance = $uibModal.open({
            templateUrl: "views/agent/batchUpdateProfitSwitch.html",
            show: true,
            scope: $scope,
            controller: 'batchUpdateProfitSwitchCtrl'
        });
        // $scope.userModalInstance = uibModalInstance;
        //
        // $scope.userModalInstance.result.then(function(){
        //     debugger;
        // }, function(){
        //     debugger;
        // });

    };


    $scope.batchUpdatePromotionSwitch = function () {

        var currentSelection = $scope.gridApi.selection.getSelectedRows();
        alert(currentSelection);
        console.log("fefewfwefwfwefaw");
        if(currentSelection.length == 0){
            $scope.notice("请选择要批量修改代理商推广功能的代理商.");
            return;
        }else{

            $scope.promotionSwitchAgent = [];
            for(var key in currentSelection){
                alert(currentSelection[key]);
                $scope.promotionSwitchAgent.push(currentSelection[key].agentNo);
            }
        }

        $scope.userModalInstance = $uibModal.open({
            templateUrl: "views/agent/batchUpdatePromotionSwitch.html",
            show: true,
            scope: $scope,
            controller: 'batchUpdatePromotionSwitchCtrl'
        });
    };


    $scope.batchUpdateCashBackSwitch = function () {

        var currentSelection = $scope.gridApi.selection.getSelectedRows();
        if(currentSelection.length == 0){
            $scope.notice("请选择要批量修改欢乐返返现功能的代理商.");
            return;
        }else{

            $scope.cashBackSwitchAgent = [];
            for(var key in currentSelection){
                $scope.cashBackSwitchAgent.push(currentSelection[key].agentNo);
            }
        }

        $scope.userModalInstance = $uibModal.open({
            templateUrl: "views/agent/batchUpdateCashBackSwitch.html",
            show: true,
            scope: $scope,
            controller: 'batchUpdateCashBackSwitchCtrl'
        });
    };
    
    $scope.updateFullPrizeSwitch=function(row){
        var agentNo = row.entity.agentNo;
        var fullPrizeSwitchText = row.entity.fullPrizeSwitch ? "开启代理商("+ agentNo +")满奖？"
                                                        : "关闭代理商("+ agentNo +")满奖？";
        var text = row.entity.fullPrizeSwitch ? "": "该代理商("+ agentNo +")及所有下级代理商满奖功能都会一起关闭!";
        SweetAlert.swal({
                title: fullPrizeSwitchText,
                type: "warning",
                text: text,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },function (isConfirm) {
                if (isConfirm) {
                    var data = {
                    	fullPrizeSwitch: row.entity.fullPrizeSwitch ? 1 : 0,
                        agentNo: row.entity.agentNo
                    };
                    $http({
                        url: 'agentInfo/updateFullPrizeSwitch',
                        method: 'POST',
                        data: data
                    }).success(function (data) {
                        $scope.notice(data.message);
                        if(!data.success){
                            row.entity.fullPrizeSwitch = !row.entity.fullPrizeSwitch;
                        }else {
                            $scope.query();
                        }
                    }).error(function (data) {
                        $scope.notice("服务器异常,更新状态失败.");
                        row.entity.fullPrizeSwitch = !row.entity.fullPrizeSwitch;
                    })
                } else {
                    row.entity.fullPrizeSwitch = !row.entity.fullPrizeSwitch;
                }
            });
    };

    $scope.updateNotFullDeductSwitch=function(row){
        var agentNo = row.entity.agentNo;
        var notFullDeductSwitchText = row.entity.notFullDeductSwitch ? "开启代理商("+ agentNo +")不满扣？"
                                                        : "关闭代理商("+ agentNo +")不满扣？";
        var text = row.entity.notFullDeductSwitch ? "": "该代理商("+ agentNo +")及所有下级代理商不满扣功能都会一起关闭!";
        SweetAlert.swal({
                title: notFullDeductSwitchText,
                type: "warning",
                text: text,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },function (isConfirm) {
                if (isConfirm) {
                    var data = {
                    	notFullDeductSwitch: row.entity.notFullDeductSwitch ? 1 : 0,
                        agentNo: row.entity.agentNo
                    };
                    $http({
                        url: 'agentInfo/updateNotFullDeductSwitch',
                        method: 'POST',
                        data: data
                    }).success(function (data) {
                        $scope.notice(data.message);
                        if(!data.success){
                            row.entity.notFullDeductSwitch = !row.entity.notFullDeductSwitch;
                        }else {
                            $scope.query();
                        }
                    }).error(function (data) {
                        $scope.notice("服务器异常,更新状态失败.");
                        row.entity.notFullDeductSwitch = !row.entity.notFullDeductSwitch;
                    })
                } else {
                    row.entity.notFullDeductSwitch = !row.entity.notFullDeductSwitch;
                }
            });
    };

    $scope.updateProfitSwitch=function(row){
        var agentNo = row.entity.agentNo;
        var profitSwitchText = row.entity.profitSwitch ? "开启代理商("+ agentNo +")日结分润？"
                                                        : "关闭代理商("+ agentNo +")日结分润？";
        var text = row.entity.profitSwitch ? "": "该代理商("+ agentNo +")及所有下级代理商分润日结功能都会一起关闭!";
        SweetAlert.swal({
                title: profitSwitchText,
                type: "warning",
                text: text,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },function (isConfirm) {
                if (isConfirm) {
                    var data = {
                        profitSwitch: row.entity.profitSwitch ? 1 : 0,
                        agentNo: row.entity.agentNo
                    };
                    $http({
                        url: 'agentInfo/updateProfitSwitch',
                        method: 'POST',
                        data: data
                    }).success(function (data) {
                        $scope.notice(data.message);
                        if(!data.success){
                            row.entity.profitSwitch = !row.entity.profitSwitch;
                        }else {
                            $scope.query();
                        }
                    }).error(function (data) {
                        $scope.notice("服务器异常,更新状态失败.");
                        row.entity.profitSwitch = !row.entity.profitSwitch;
                    })
                } else {
                    row.entity.profitSwitch = !row.entity.profitSwitch;
                }
            });
    };
    $scope.updatePromotionSwitch=function(row){
        var agentNo = row.entity.agentNo;
        var promotionSwitchText = row.entity.promotionSwitch ? "开启代理商("+agentNo+")推广功能？"
                                                            : "关闭代理商("+agentNo+")推广功能？";
        var text = row.entity.promotionSwitch ? "" : "该代理商("+agentNo+")及所有下级代理商推广功能都会一起关闭!";
        SweetAlert.swal({
                title: promotionSwitchText,
                type: "warning",
                text: text,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },function (isConfirm) {
                if (isConfirm) {
                    var data = {
                        promotionSwitch: row.entity.promotionSwitch ? 1 : 0,
                        agentNo: row.entity.agentNo
                    };
                    $http({
                        url: 'agentInfo/updatePromotionSwitch',
                        method: 'POST',
                        data: data
                    }).success(function (data) {
                        $scope.notice(data.message);
                        if(!data.success){
                            row.entity.promotionSwitch = !row.entity.promotionSwitch;
                        }else {
                            $scope.query();
                        }
                    }).error(function (data) {
                        $scope.notice("服务器异常,更新状态失败.");
                        row.entity.promotionSwitch = !row.entity.promotionSwitch;
                    })
                } else {
                    row.entity.promotionSwitch = !row.entity.promotionSwitch;
                }
            });
    };

    $scope.updateCashBackSwitch=function(row){
        var agentNo = row.entity.agentNo;
        var cashBackSwitchText = row.entity.cashBackSwitch ? "开启代理商("+agentNo+")欢乐返返现功能？"
            : "关闭代理商("+agentNo+")欢乐返返现功能？";
        var text = row.entity.cashBackSwitch ? "" : "该代理商("+agentNo+")及所有下级代理商欢乐返返现功能都会一起关闭!";
        SweetAlert.swal({
            title: cashBackSwitchText,
            type: "warning",
            text: text,
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true
        },function (isConfirm) {
            if (isConfirm) {
                var data = {
                    cashBackSwitch: row.entity.cashBackSwitch ? 1 : 0,
                    agentNo: row.entity.agentNo
                };
                $http({
                    url: 'agentInfo/updateCashBackSwitch',
                    method: 'POST',
                    data: data
                }).success(function (data) {
                    $scope.notice(data.message);
                    if(!data.success){
                        row.entity.cashBackSwitch = !row.entity.cashBackSwitch;
                    }else {
                        $scope.query();
                    }
                }).error(function (data) {
                    $scope.notice("服务器异常,更新状态失败.");
                    row.entity.cashBackSwitch = !row.entity.cashBackSwitch;
                })
            } else {
                row.entity.cashBackSwitch = !row.entity.cashBackSwitch;
            }
        });
    };

	$scope.agentL = _Dlevel;
	if($scope.agentL==1){
		$scope.agentGrid.columnDefs[5].visible = true;
	}else{
		$scope.agentGrid.columnDefs[5].visible = false;
	}
	
	//删除代理商
	$scope.deleteAgent = function(agentNo){
		var r=confirm("确定删除带代理商？");
		if (r==true){
			$http.post('agentInfo/delAgent',agentNo).success(function(msg){
				if(msg.status){
					$scope.notice(msg.msg);
				}else{
					$scope.notice(msg.msg);
				}
				$scope.query();
			}).error(function(){
			}); 
		}
	}
	$scope.myGrid = {
			paginationPageSize:10,                  //分页数量
			paginationPageSizes: [10,20,50,100],	//切换每页记录数
			useExternalPagination: true,		  //开启拓展名
			enableHorizontalScrollbar: true,        //横向滚动条
			enableVerticalScrollbar : true,  		//纵向滚动条
			columnDefs: $scope.columnDefs,
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					$scope.paginationOptions.pageNo = newPage;
					$scope.paginationOptions.pageSize = pageSize;
					$scope.query();
				});
			}
		};
	/*修改分润等级
	 	$scope.queryParAgent = function () {
        $http({
            url: 'perAgent/selectPaUserByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.myGrid.data = msg.page.result;
            $scope.myGrid.totalItems = msg.page.totalCount;
            $scope.userTotal = msg.userTotal;
            $scope.merUserTotal = msg.merUserTotal;
            $scope.updateShareLevelData.create_user = msg.loginUserCode;
            //分润等级最高可调级别
			$scope.maxAgentLevel = msg.maxAgentLevel;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    if ($rootScope.agentType == 11) {
    	$scope.queryParAgent();
	}
    $scope.updateShareLevel = function () {
    	if($scope.updateShareLevelData.after_level == ''){
    		alert("请选择分润等级");
    		return;
		}
		if($scope.updateShareLevelData.after_level <= $scope.shareLevel){
    		alert("调整后的分润等级必须大于原来的分润等级");
    		return;
		}
        $http({
            url: 'perAgent/updateShareLevel',
            method: 'POST',
            data: $scope.updateShareLevelData
        }).success(function (data) {
            if (data.status){
                $scope.notice("操作成功");
                $scope.hideAllModel();
                $scope.query();
            }else{
                $scope.notice(data.msg);
            }
        });
        $scope.hideAllModel();
    };*/
    $scope.hideAllModel = function () {
        $("#changeShareLevel").modal('hide');
    };
	
	$scope.query = function(){
		$http.post('agentInfo/queryAgentInfoList',"baseInfo=" + angular.toJson($scope.baseInfo) + "&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(msg){
			$scope.agentL = msg.agentLevel;
			$scope.agentData = msg.page.result;
			$scope.agentGrid.totalItems = msg.page.totalCount;
			angular.forEach(msg.page.result,function(data,index){
				data.curLevel = msg.agentLevel;
				
			});
			if ($scope.agentData.length == 0) {
				var d = msg.curAgentInfo;
				if (!d.showFullPrizeSwitch && d.showNotFullDeductSwitch) {
					$scope.agentGrid.columnDefs[6].visible = false;
				}else if(d.showFullPrizeSwitch && !d.showNotFullDeductSwitch){
					$scope.agentGrid.columnDefs[7].visible = false;
				}else if(!d.showFullPrizeSwitch && !d.showNotFullDeductSwitch){
					$scope.agentGrid.columnDefs[6].visible = false;
					$scope.agentGrid.columnDefs[7].visible = false;
				}
			}
			for ( var index in $scope.agentData) {
				var d = $scope.agentData[index];
				if (!d.showFullPrizeSwitch && d.showNotFullDeductSwitch) {
					$scope.agentGrid.columnDefs[6].visible = false;
					break;
				}else if(d.showFullPrizeSwitch && !d.showNotFullDeductSwitch){
					$scope.agentGrid.columnDefs[7].visible = false;
					break;
				}else if(!d.showFullPrizeSwitch && !d.showNotFullDeductSwitch){
					$scope.agentGrid.columnDefs[6].visible = false;
					$scope.agentGrid.columnDefs[7].visible = false;
					break;
				}
			};
		}).error(function(){
		});
	};
	$scope.query();
	//reset
	$scope.resetForm=function(){
		$scope.baseInfo= {hasSub:0,bool:'1',agentName:$rootScope.entityId, profitSwitch: ''};
	};
	
	//20170227tgh======
	$scope.agent = [];
	//代理商
	 $http.post("agentInfo/selectAllInfo")
  	 .success(function(msg){
  			//响应成功
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
  	   	}
  	});
	 //所有直属下级代理商
//	 $scope.belongAgent = [];
//	 $http.post("agentInfo/selectAllInfoBelong")
//	 .success(function(msg){
//		 //响应成功
//		 for(var i=0; i<msg.length; i++){
//			 $scope.belongAgent.push({value:msg[i].agentNo,text:msg[i].agentName});
//		 }
//	 });
	 $scope.dtr= true;
	 $scope.agt_ch = function(){
			if(!$scope.baseInfo.agentName){
				$scope.dtr= true;
			}else{
				$scope.dtr= false;
			}
		}
	 
	//密码重置tgh227
    $scope.restPwd=function(param){
        SweetAlert.swal({
            title: "确认密码重置？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post("agentInfo/restPwd.do",angular.toJson(param))
	        		.success(function(msg){
	        			$scope.notice(msg.msg);
	        		}).error(function(){
	        		});
	            }
        });
    };
}).controller("batchUpdatePromotionSwitchCtrl", function($scope,$rootScope, $http, $state, $stateParams,i18nService,SweetAlert, $uibModal) {
    // 批量修改代理商推广功能
    $scope.cancel = function(){
        $scope.userModalInstance.dismiss();
        delete $scope.userModalInstance;
    };
    $scope.subBatchUpdatePromotionSwitch = function () {
        console.log($scope.promotionSwitchAgent.length);
        var data = {
            agentNo: $scope.promotionSwitchAgent,
            promotionSwitch: Number.parseInt($scope.promotionSwitch)
        };

        $http({
            url: 'agentInfo/batchUpdatePromotionSwitch',
            method: 'POST',
            data: data
        }).success(function (data) {
            $scope.notice(data.message);
            $scope.cancel();
            $scope.gridApi.selection.clearSelectedRows();
            $scope.query();
        }).error(function (data) {
            $scope.notice("服务器异常,更新状态失败.");
        })
    };
}).controller("batchUpdateProfitSwitchCtrl", function($scope,$rootScope, $http, $state, $stateParams,i18nService,SweetAlert, $uibModal) {
    // 批量修改代理商分润日结功能
    $scope.cancel = function(){
        $scope.userModalInstance.dismiss();
        delete $scope.userModalInstance;
    };
    $scope.subBatchUpdateProfitSwitch = function () {
        console.log($scope.profitSwitchAgent.length);
        var data = {
            agentNo: $scope.profitSwitchAgent,
            profitSwitch: Number.parseInt($scope.profitSwitch)
        };

        $http({
            url: 'agentInfo/batchUpdateProfitSwitch',
            method: 'POST',
            data: data
        }).success(function (data) {
            $scope.notice(data.message);
            $scope.cancel();
            $scope.gridApi.selection.clearSelectedRows();
            $scope.query();
        }).error(function (data) {
            $scope.notice("服务器异常,更新状态失败.");
        })
    };
}).controller("batchUpdateCashBackSwitchCtrl", function($scope,$rootScope, $http, $state, $stateParams,i18nService,SweetAlert, $uibModal) {
    // 批量修改代理商欢乐返返现功能
    $scope.cancel = function(){
        $scope.userModalInstance.dismiss();
        delete $scope.userModalInstance;
    };
    $scope.subBatchUpdateCashBackSwitch = function () {
        console.log($scope.cashBackSwitchAgent.length);
        var data = {
            agentNo: $scope.cashBackSwitchAgent,
            cashBackSwitch: Number.parseInt($scope.cashBackSwitch)
        };

        $http({
            url: 'agentInfo/batchUpdateCashBackSwitch',
            method: 'POST',
            data: data
        }).success(function (data) {
            $scope.notice(data.message);
            $scope.cancel();
            $scope.gridApi.selection.clearSelectedRows();
            $scope.query();
        }).error(function (data) {
            $scope.notice("服务器异常,更新状态失败.");
        })
    };
});
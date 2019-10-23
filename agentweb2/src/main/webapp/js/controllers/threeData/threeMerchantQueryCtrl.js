angular.module('inspinia').controller('threeMerchantQueryCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$filter,i18nService, SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	$scope.IsYN=[{text:"是",value:0},{text:"否",value:1}];
	$scope.quickPaymentList=[{text:"全部",value:""},{text:"是",value:"1"},{text:"否",value:"0"}];
	$scope.merchantStates=[{text:"全部",value:""},{text:"待一审",value:"1"},{text:"待平台审核",value:"2"},{text:"审核失败",value:"3"},{text:"正常",value:"4"},{text:"关闭",value:"0"}];
	$scope.info={teamEntryId:'',quickPayment: "", riskStatus:'',recommendedSource:"",mbpId:"",merchantNo:"",merchantExamineState:"",agentName:$rootScope.entityId,agentNode:0,merTeamId:"-1",termianlType:"-1",mobilephone:"",activityType:"",
		sTime:moment(new Date().getTime() - 182 * 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
        eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
		};
	$scope.riskStatusaa=[{text:"全部",value:""},{text:"正常",value:1},{text:"不进不出",value:3},{text:"只进不出",value:2}]
    $scope.activityTypes=[{text:"全部",value:""},{text:"是",value:1},{text:"否",value:0}];
	if ($scope.oemType === "ZYFPAY" || $scope.oemType === "YABPAY" || $scope.oemType === "YLSTCZB"){
        $scope.recommendedSources=[
        	{text:"全部",value:""},
			{text:"正常注册",value:0},
			// {text:"微创业",value:1},
			{text:"代理商分享",value:2}
			];
	}else if($scope.oemType === "ZHZFPAY"){
        $scope.recommendedSources=[
            {text:"全部",value:""},
            {text:"正常注册",value:0},
            // {text:"微创业",value:1},
            // {text:"代理商分享",value:2}
        ];
	}else if($scope.oemType === "ZHFPLUSPAY"){
        $scope.recommendedSources=[
            {text:"全部",value:""},
            {text:"正常注册",value:0},
            // {text:"微创业",value:1},
            {text:"代理商分享",value:2}
        ];
	}else{
        $scope.recommendedSources=[{text:"全部",value:""},{text:"正常注册",value:0},{text:"微创业",value:1},{text:"代理商分享",value:2}];
	}

	$scope.replaceStatus = false;

	$scope.activityShow=[];
    for(var i=0; i<$scope.activityTypess.length; i++){
            $scope.activityShow.push({value:$scope.activityTypess[i].sys_value,text:$scope.activityTypess[i].sys_name});
    }
	$scope.activityTypesStr = angular.toJson($scope.activityShow);

	
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
	// 当前登录代理商业务范围是否有设置
	$scope.showSelect = true;
	$scope.initTeamId = "-1";
	$scope.merTeams = [{text:"全部",value:"-1"},{text:"盛钱包",value:"200010"},{text:"盛POS",value:"100070"}];
	$scope.getAccesTeamId = function (){
		$http.get('userAction/getAccessTeamId').success(function (result) {
			if(result.status){
				$scope.info.merTeamId = result.accessTeamId;
				$scope.initTeamId = result.accessTeamId;
				alert(initTeamId);
			}else {
				$scope.showSelect = false;
			}
		});
	}
	$scope.getAccesTeamId();


	$scope.agent = [];
	//代理商
	 $http.post("agentInfo/selectConfigInfo")
   	 .success(function(msg){
   		//$scope.agent.push({value:"",text:"全部"});
   			//响应成功
   	   	for(var i=0; i<msg.length; i++){
   	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
   	   	}
   	});
	//查询
	$scope.selectInfo=function(){
		/*$scope.replaceStatus = false; 
		$http.get('merchantBusinessProduct/showReplace.do')
		.success(function(msg){
			if(msg.status){
				$scope.replaceStatus = true; 
			}else{
				$scope.replaceStatus = false; 
			}
		});*/
		$http({
			url: 'merchantBusinessProduct/threeMerchantQuery.do?pageNo='+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			method: 'POST',
			data: $scope.info
		}).success(function (result) {
            $scope.gridOptions.data = result.data;
            $scope.gridOptions.totalItems = result.count;
            $scope.selectMechant();
  
        });
	};
	//$scope.selectInfo();
	// 导出
	/*$scope.exportInfo = function () {
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
                location.href="merchantBusinessProduct/exportMerchantInfo?"+$.param($scope.info);
            }
        });
    };*/
	
	 
	//309tgh汇总商户
	$scope.selectMechant=function(){
		$http.post('merchantBusinessProduct/threeSelectMechant.do',
				 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(!data){
				$scope.gridOptions.totalMerchant = 0;
				return;
			}
			$scope.gridOptions.totalMerchant =data; 
		})
	};
	// $scope.selectMechant();
	//清空
	$scope.clear=function(){
		$scope.teamEntryIdList = [{text:'全部',value:''}];
		$scope.info={teamEntryId:'',merTeamId:$scope.initTeamId,quickPayment:'', riskStatus:'',recommendedSource:"",
            sTime:moment(new Date().getTime() - 182 * 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
            eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			mbpId:"",merchantNo:"",merchantExamineState:"",agentName:$rootScope.entityId,agentNode:0,termianlType:"-1",mobilephone:"",activityType:""};
		$scope.changeMerGroups($rootScope.entityId);
	};
    isVerifyTime = true;
    $scope.merchantOrMobileChange = function(){
        isVerifyTime = !($scope.info.merchantNo || $scope.info.mobilephone);
	};

    setBeginTime=function(setTime){
        $scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    };

    setEndTime=function(setTime){
        $scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    };
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$scope.sBpId = "";
	$scope.merchantNo = "";
	//更改业务产品
	/*$scope.getGroupBpInfo = function(entity){
		$http.get("merchantBusinessProduct/showGroupBpInfo?bpId="+entity.bpId+"&merchantNo="+entity.merchantNo)
			.success(function(msg){
				if(msg.status){
					$scope.sBpId = entity.bpId;
					$scope.merchantNo = entity.merchantNo;
					$scope.productTypeGroup = msg.list;
					$("#groupBpInfo").modal("show");
				} else{
					$scope.notice(msg.msg);
				}
			}).error(function(){
				$scope.notice("服务异常");
			})
	}*/
	$scope.cancel=function(){
		$('#groupBpInfo').modal('hide');
	}
	$scope.replaceGroupBpInfo = function(entity){
		if($scope.bpId == undefined){
			alert("请选择更换的业务产品");
			return;
		}
		var data = {
				"sBpId" : $scope.sBpId,
				"merchantNo":$scope.merchantNo,
				"bpId":$scope.bpId			
		};
	
		/* $http.post("merchantBusinessProduct/replaceBusinessProduct",angular.toJson(data))
			.success(function(data){
				if(data.status){
					$scope.notice(data.msg);
					$("#groupBpInfo").modal("hide");
					$scope.selectInfo();
				}else{
					$scope.notice(data.msg);
					$scope.submitting = false;
				}
			});*/
	};

	
	$scope.columnDefs = [                           //表格数据
               		        /* { field: 'id',displayName:'商户进件编号',width:120},
               		         { field: 'teamId',displayName:'所属组织',width:80,
               		        	 cellFilter:"cTypeStrFilter"
               		         },*/
               		         { field: 'merchantNo',displayName:'商户编号',width:160},
               		         { field: 'merchantName',displayName:'商户简称',width:150 },
               		         { field: 'mobilePhone',displayName:'商户手机号',width:110},
               		         { field: 'merGroup',displayName:'商户组织',width:110},
               		         //{ field: 'bpName',displayName:'业务产品',width:100 },
               		         { field: 'status',displayName:'状态',width:100,
               		        	 cellFilter:"formatDropping:[{text:'待一审',value:1},{text:'待平台审核',value:2},{text:'审核失败',value:3},{text:'正常',value:4},{text:'关闭',value:0}]"
               		         },
               		         { field: 'riskStatus',displayName:'商户冻结状态',width:100,
               		        	 cellFilter:"formatDropping:[{text:'正常',value:1},{text:'只进不出',value:2},{text:'不进不出',value:3}]"
               		         },
               		         { field: 'merCreateTime',displayName:'创建时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
               		         },
               				 { field: 'recommendedSource',displayName:'推广来源',width:180 ,
               					 cellFilter:"formatDropping:" + angular.toJson($scope.recommendedSources),
               				 },
               				  { field: 'quickPayment',displayName:'绑定快捷支付卡',width:180 ,
                                     cellTemplate:
               						  "<div ng-show='row.entity.quickPayment === 0'>否</div>" +
                                         "<div ng-show='row.entity.quickPayment > 0'>是</div>"
               				 },
               		       /*  { field: 'id',displayName:'操作',width:200,
               		        	 cellTemplate:
               		        		 '<div ng-switch on="row.entity.status" style="display:inline-block">'
               			    			+'<div ng-switch-when="3">'
                                        		+ '<a ng-show="grid.appScope.hasPermit(\'merchant.queryMerDetail\')" ui-sref="merchant.queryMerDetail({mertId:row.entity.id})">详情</a>'
               								+ '<span ng-show="grid.appScope.hasPermit(\'merchant.MerUpdate\') && (row.entity.agentNo == $root.entityId || row.entity.oneAgentNo == $root.entityId)"> | </span>'
               						       + '<a ng-show="grid.appScope.hasPermit(\'merchant.MerUpdate\') && (row.entity.agentNo == $root.entityId || row.entity.oneAgentNo == $root.entityId)" ui-sref="merchant.MerUpdate({mertId:row.entity.id})">修改</a>'
               			    			+'</div>'
               			    			+'<div ng-switch-default>'
                                       		 + '<a ng-show="grid.appScope.hasPermit(\'merchant.queryMerDetail\')" ui-sref="merchant.queryMerDetail({mertId:row.entity.id})">详情</a>'
               		    				+'</div>'
               			    		+'</div>'
               			    		+ '  <a ng-show="grid.appScope.replaceStatus&&row.entity.status ==\'4\'&&row.entity.showReplace ==\'1\'"  ng-click="grid.appScope.getGroupBpInfo(row.entity)"> | 更改业务产品</a>'
               		        		
               		         }*/
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
}).filter('setxingxing', function () {
	return function (value) {
		if(value){
			var v = value.substring(7,11);
			return "*********"+v;
		}
}
});
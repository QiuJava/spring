/**
 * 查询业务产品
 */
angular.module('inspinia',['angularFileUpload']).controller('threeHappyBackCtrl',function($scope,$rootScope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	  $scope.teamEntryIdList = [{text:'全部',value:''}];
	$scope.baseInfo = {teamEntryId:'',merTeamId:"-1",merchantN:"",agentNode:$rootScope.entityId,status:"",discountStatus:-1,checkStatus:"",
        activeTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'),
        activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'),
        isStandard: '',repeatRegister:'',
        minCumulateTransAmount:'', maxCumulateTransAmount:'',
        minStandardTime:'', maxStandardTime:'',
        minMinusAmountTime:'', maxMinusAmountTime:'',
        minAddAmountTime:'', maxAddAmountTime:'',
		activityCode:"",liquidationStatus:"",accountCheckStatus:"",merchantType:"1"};
	$scope.disabledMerchantType = true;
	//活动类型
    if ($scope.oemType === 'ZHZFPAY'){
        $scope.subjectTypes = [{text:'全部',value:''},{text:'欢乐返',value:'009'}];
    }else{
        $scope.subjectTypes = [{text:'全部',value:''},{text:'欢乐返-循环送',value:'008'},{text:'欢乐返',value:'009'}];
	}
    
  
	 $scope.getTeamEntryIdList= function(){
	    	var merTeamId = $scope.baseInfo.merTeamId;
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
				$scope.baseInfo.teamEntryId='';
			});
	    }

	$scope.settleStatus = [{text:'全部',value:''},{text:'同意',value:'1'},{text:'不同意',value:'2'},{text:'未核算',value:'3'}];
	$scope.activityStatus = [{text:'全部',value:''},{text:'未激活',value:'1'},{text:'已激活',value:'2'},{text:'已返鼓励金',value:'6'},{text:'已扣款',value:'7'},{text:'预调账已发起',value:'8'},{text:'已奖励',value:'9'}];
	$scope.isStandards = [{text:'全部',value:''},{text:'未达标',value:'0'},{text:'已达标',value:'1'}];
	$scope.accountTypeList = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];//直属下级入账状态
    $scope.repeatRegisters = [{text:'全部',value:''},{text:'否',value:'0'},{text:'是',value:'1'},{text:'所有重复商户',value:'2'}];

	//代理商下拉列表查询tgh418
	$scope.agent = [];
	$scope.currentLoginAgentIsFirstLevel = true;
    $scope.totalData = {
        totalTransTotal: '0.00',
        totalEmptyAmount: '0.00',
        totalAdjustmentAmount: '0.00',
        totalFullAmount: '0.00'
	};
	 $http.post("agentInfo/selectConfigInfo")
   	 .success(function(msg){
   			//响应成功
   	   	for(var i=0; i<msg.length; i++){
   	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentNo+ " " + msg[i].agentName});
   	   	}
   	});


	// 当前登录代理商业务范围是否有设置
	$scope.showSelect = true;
	$scope.initTeamId = "-1";
	$scope.merTeams = [{text:"全部",value:"-1"},{text:"盛钱包",value:"200010"},{text:"盛POS",value:"100070"}];
	$scope.getAccesTeamId = function (){
		$http.get('userAction/getAccessTeamId').success(function (result) {
			if(result.status){
				$scope.baseInfo.merTeamId = result.accessTeamId;
				$scope.initTeamId = result.accessTeamId;
			}else {
				$scope.showSelect = false;
			}
		});
	}
	$scope.getAccesTeamId();

	//查询
	$scope.query=function(){
		$http.post('activityDetail/threeSelectTotalMoney.do',"baseInfo="+angular.toJson($scope.baseInfo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(data){
					if(!data){
						$scope.notice("没有查询到数据");
						return;
					}else{
						$scope.totalData = data.totalData;
						$http.post('activityDetail/threeSelectHappyBackDetail.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
								$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
								.success(function(data){
									if(!data){
										$scope.notice("没有查询到数据");
										return;
									}
									$scope.activityGrid.data =data.result; 
									$scope.activityGrid.totalItems = data.totalCount ;
									$scope.totalCount=data.totalCount;
								})
					}
		})
	};
    $scope.columnDefs = [
        {field: 'id',displayName: '序号',pinnable: false,width: 180,sortable: false},
        {field: 'activeOrder',displayName: '激活流水号',pinnable: false,width: 180,sortable: false},
        {field: 'activeTime',displayName: '激活时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'activityCode',displayName: '活动类型',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.subjectTypes)},
       // {field: 'activityTypeNo',displayName: '欢乐返子类型',pinnable: false,width: 150,sortable: false},
        {field: 'merchantName',displayName: '商户名称',pinnable: false,width: 150,sortable: false},
        {field: 'merchantNo',displayName: '商户编号',pinnable: false,width: 150,sortable: false},
		{field: 'merGroup',displayName: '商户组织',pinnable: false,width: 150,sortable: false},
        {field: 'agentName',displayName: '所属代理商名称',pinnable: false,width: 150,sortable: false},
        {field: 'agentNo',displayName: '所属代理商编号',pinnable: false,width: 150,sortable: false},
        {field: 'repeatRegister',displayName: '是否重复注册',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:[{text:'否',value:'0'},{text:'是',value:'1'}]"},
        {field: 'enterTime',displayName: '进件时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'transTotal',displayName: '交易金额',pinnable: false,width: 150,sortable: false},
//        {field: 'merchantFee',displayName: '手续费',pinnable: false,width: 180,sortable: false},
        {field: 'cumulateTransAmount',displayName: '累计交易金额',pinnable: false,width: 150,sortable: false},
        {field: 'minOverdueTime',displayName: '交易累计截止日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'overdueTime',displayName: '交易扣款累计截止日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'cumulateAmountMinus',displayName: '累计交易（扣）',pinnable: false,width: 150,sortable: false},
        {field: 'cumulateAmountAdd',displayName: '累计交易（奖）',pinnable: false,width: 150,sortable: false},
        {field: 'currentAgentNo',displayName: '当前代理商编号',pinnable: false,width: 150,sortable: false},
        {field: 'cashBackAmount',displayName: '当前代理商返现金额',pinnable: false,width: 180,sortable: false},
        {field: 'emptyAmount',displayName: '代理商未满扣N元',pinnable: false,width: 180,sortable: false},
        {field: 'fullAmount',displayName: '代理商满奖M元',pinnable: false,width: 180,sortable: false},
//        {field: 'cashBackAmount',displayName: '返现金额',pinnable: false,width: 180,sortable: false},
//        {field: 'emptyAmount',displayName: '未满扣N元',pinnable: false,width: 180,sortable: false},
//        {field: 'fullAmount',displayName: '满奖M元',pinnable: false,width: 180,sortable: false},
        {field: 'cashBackSwitch',displayName: '当前代理商返现开关状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'关闭',value:'0'},{text:'打开',value:'1'}]"},
        {field: 'entryStatus',displayName: '当前代理商返现入账状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'未入账',value:'0'},{text:'已入账',value:'1'}]"},
        {field: 'directAgentNo',displayName: '直属下级代理商编号',pinnable: false,width: 180,sortable: false},
        {field: 'directAgentName',displayName: '直属下级代理商名称',pinnable: false,width: 180,sortable: false},
        {field: 'directCashBackSwitch',displayName: '直属下级返现开关状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'关闭',value:'0'},{text:'打开',value:'1'}]"},
        {field: 'directCashBackAmount',displayName: '直属下级返现金额',pinnable: false,width: 180,sortable: false},
        {field: 'directEntryStatus',displayName: '直属下级返现入账状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'未入账',value:'0'},{text:'已入账',value:'1'}]"},
        {field: 'status',displayName: '活动状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.activityStatus)},
        
        {field: 'isStandard',displayName: '奖励是否达标',pinnable: false,width: 180,sortable: false, cellFilter:"formatDropping:" + angular.toJson($scope.isStandards)},
        {field: 'standardTime',displayName: '奖励达标时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'minusAmountTime',displayName: '扣款时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'addAmountTime',displayName: '奖励时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        
        // {field: 'oneAgentName',displayName: '一级代理商名称',pinnable: false,width: 180,sortable: false},
        // {field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 180,sortable: false},
        {field: 'liquidationStatus',displayName: '清算核算状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.settleStatus)},
//        {field: 'liquidationTime',displayName: '清算操作时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'accountCheckStatus',displayName: '账务核算状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.settleStatus)},
//        {field: 'accountCheckTime',displayName: '账务操作时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
    ];
	$scope.activityGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
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

	/*$http({
		url: 'agentInfo/getLoginAgentInfo',
		method: "POST"
	}).success(function (data) {
        $scope.currentLoginAgentIsFirstLevel = data && data.success && data.data && data.data.agentLevel == 1;
		if(! $scope.currentLoginAgentIsFirstLevel){
            $scope.columnDefs.splice(17, 2);
            $scope.columnDefs.splice(24, 4);
		}
    });*/
	$scope.changeAgentNode = function () {
        $scope.disabledMerchantType = !$scope.baseInfo.agentNode;
    };
	//未激活时,隐藏激活时间
	$scope.hideActivityTime = function(){
		if($scope.baseInfo.status==1){
			$scope.activityTimeHide = true;
		} else {
			$scope.activityTimeHide = false;
		}
	};
	//reset
	$scope.resetForm=function(){
		$scope.teamEntryIdList = [{text:'全部',value:''}];
		$scope.baseInfo = {teamEntryId:'',merTeamId: $scope.initTeamId,merchantN:"",agentNode:$rootScope.entityId,status:"",discountStatus:-1,checkStatus:"",
            activeTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'),
            activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'),
            isStandard: '',
            minCumulateTransAmount:'',maxCumulateTransAmount:'',
            minStandardTime:'', maxStandardTime:'',
            minMinusAmountTime:'', maxMinusAmountTime:'',
            minAddAmountTime:'', maxAddAmountTime:'',
				activityCode:"",liquidationStatus:"",accountCheckStatus:"", merchantType: "1"};
        $scope.disabledMerchantType = true;
	};
	
    //欢乐返活动子类型
 /*   $scope.checkActivityCode = function(activityCode){
        $scope.typeNos=[];
        if(activityCode=="008"){
            $scope.typeNos=$scope.typeNos1;
        }else if(activityCode=="009"){
            $scope.typeNos=$scope.typeNos2;
        }
    };
    $scope.checkActivityCode1 = function(){
        $scope.typeNos1=[];
        $http.post("activityDetail/queryByactivityTypeNoList","008").success(function (data) {
            if(data.status){
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos1.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeName});
                }
            }
        })
    };
    $scope.checkActivityCode1();
    $scope.checkActivityCode2 = function(){
        $scope.typeNos2=[];
        $http.post("activityDetail/queryByactivityTypeNoList","009").success(function (data) {
            if(data.status){
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos2.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeName});
                }
            }
        })
    };
    $scope.checkActivityCode2();*/
	
	//导出
	 $scope.exportExcel=function(){
	        SweetAlert.swal({
	            title: "确认导出？",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	if($scope.activityGrid.data==null || $scope.activityGrid.data.length==0){
		            		$scope.notice("没有可导出的数据");
		       			 	return;
		       		 	} else {
				       		 location.href="activityDetail/exportHappyBack.do?"
				       		 + "baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
							 + $scope.activityGrid.totalItems;
		       		 	}
		            }
	        });
	    };

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
	
});
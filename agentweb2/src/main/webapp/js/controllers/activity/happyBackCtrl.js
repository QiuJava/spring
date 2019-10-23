/**
 * 查询业务产品
 */
angular.module('inspinia',['angularFileUpload']).controller('happyBackCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.baseInfo = {merTeamId:"-1",merchantN:"",agentNode:"",status:"",discountStatus:-1,checkStatus:"",
        activeTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'),
        activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'),
        isStandard: '',repeatRegister:'',billingStatus:'',
        minBillingTime:'', maxBillingTime:'',
        minCumulateTransAmount:'', maxCumulateTransAmount:'',
        minStandardTime:'', maxStandardTime:'',
        minMinusAmountTime:'', maxMinusAmountTime:'',
        minAddAmountTime:'', maxAddAmountTime:'',
		activityCode:"",liquidationStatus:"",accountCheckStatus:"",merchantType:"1",
        activeTimeStart:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
       /* liquidationTimeStart:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        liquidationTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        minStandardTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        maxStandardTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        minMinusAmountTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        maxMinusAmountTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        minAddAmountTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        maxAddAmountTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        minBillingTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        maxBillingTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
	};
    // //是否校验时间
    // isVerifyTime = 1;//校验：1，不校验：0
    // setBeginTime=function(setTime){
    //     $scope.baseInfo.activeTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.liquidationTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.minStandardTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.minMinusAmountTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.minAddAmountTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.minBillingTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    // }
    //
    // setEndTime=function(setTime){
    //     $scope.baseInfo.activeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.liquidationTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.maxStandardTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.maxMinusAmountTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.maxAddAmountTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    //     $scope.baseInfo.maxBillingTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    // }


    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setActiveTimeStart=function(setTime){
        $scope.baseInfo.activeTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setActiveTimeEnd=function(setTime){
        $scope.baseInfo.activeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setLiquidationTimeStart=function(setTime){
        $scope.baseInfo.liquidationTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setLiquidationTimeEnd=function(setTime){
        $scope.baseInfo.liquidationTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setMinStandardTime=function(setTime){
        $scope.baseInfo.minStandardTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setMaxStandardTime=function(setTime){
        $scope.baseInfo.maxStandardTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setMinMinusAmountTime=function(setTime){
        $scope.baseInfo.maxMinusAmountTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setMaxMinusAmountTime=function(setTime){
        $scope.baseInfo.maxMinusAmountTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setMinAddAmountTime=function(setTime){
        $scope.baseInfo.minAddAmountTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setMaxAddAmountTime=function(setTime){
        $scope.baseInfo.maxAddAmountTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setMinBillingTime=function(setTime){
        $scope.baseInfo.minBillingTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");


    }

    setMaxBillingTime=function(setTime){
       $scope.baseInfo.maxBillingTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    $scope.startActiveTimeFocus = function (event) {
        var d5222=$dp.$('d5222');
        var verifyTime = $scope.baseInfo.status !== "1";
        $(event.target).unbind('click').bind('click', function () {
            WdatePicker({
                skin: 'whyGreen',
                dateFmt: 'yyyy-MM-dd HH:mm:ss',
                changed: verifyTimeFun(1, '#d5221', '#d5222', 365, verifyTime, setActiveTimeStart, setActiveTimeEnd),
                onpicked: function () {
                    d5222.focus();
                },
                maxDate: '#F{$dp.$D(\'d5222\')}'
            })
        }).trigger('click');

    };
    $scope.endActiveTimeFocus = function (event) {
        var verifyTime = $scope.baseInfo.status !== "1";
        $(event.target).unbind('click').bind('click', function () {
            WdatePicker({
                skin: 'whyGreen',
                dateFmt: 'yyyy-MM-dd HH:mm:ss',
                changed: verifyTimeFun(2, '#d5221', '#d5222', 365, verifyTime, setActiveTimeStart, setActiveTimeEnd),
                minDate: '#F{$dp.$D(\'d5221\')}'
            });
        }).trigger('click');
    };

	$scope.disabledMerchantType = true;
	//活动类型
    if ($scope.oemType === 'ZHZFPAY'){
        $scope.subjectTypes = [{text:'全部',value:''},{text:'欢乐返',value:'009'}];
    }else{
        $scope.subjectTypes = [{text:'全部',value:''},{text:'欢乐返-循环送',value:'008'},{text:'欢乐返',value:'009'}];
	}

	$scope.settleStatus = [{text:'全部',value:''},{text:'同意',value:'1'},{text:'不同意',value:'2'},{text:'未核算',value:'3'}];
	$scope.activityStatus = [{text:'全部',value:''},{text:'未激活',value:'1'},{text:'已激活',value:'2'},{text:'已返鼓励金',value:'6'},{text:'已扣款',value:'7'},{text:'预调账已发起',value:'8'},{text:'已奖励',value:'9'}];
	$scope.isStandards = [{text:'全部',value:''},{text:'未达标',value:'0'},{text:'已达标',value:'1'}];
	$scope.accountTypeList = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];//直属下级入账状态
    $scope.repeatRegisters = [{text:'全部',value:''},{text:'否',value:'0'},{text:'是',value:'1'},{text:'所有重复商户',value:'2'}];
    $scope.billingStatus = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];//入账状态
	//代理商下拉列表查询tgh418
	$scope.agent = [{text:"全部",value:""}];
	$scope.currentLoginAgentIsFirstLevel = true;
    $scope.totalData = {
        totalTransTotal: '0.00',
        totalEmptyAmount: '0.00',
        totalAdjustmentAmount: '0.00',
        totalFullAmount: '0.00'
	};
	 $http.post("agentInfo/selectAllInfo")
   	 .success(function(msg){
   			//响应成功
   	   	for(var i=0; i<msg.length; i++){
   	   		$scope.agent.push({value:msg[i].agentNode,text:msg[i].agentNo+ " " + msg[i].agentName});
   	   	}
   	});

    // 当前登录代理商业务范围是否有设置
    $scope.showSelect = true;
    $scope.initTeamId = "-1";
    $scope.selectMerTeams = function () {
        $http.get('teamInfo/getTeams')
            .success(function(data) {
                if(!data){
                    return;
                }
                $scope.merTeams=data;
                $scope.merTeams.splice(0,0,{value:"-1",text:"全部"});

                $http.get('userAction/getAccessTeamId').success(function (result) {
                    if(result.status){
                        $scope.baseInfo.merTeamId = result.accessTeamId;
                        $scope.initTeamId = result.accessTeamId;
                    }else {
                        $scope.showSelect = false;
                    }
                });
            });
    }
    $scope.selectMerTeams();

	//查询
	$scope.query=function(){
	    
        var notAllowActiveTimeIsNull = !(($scope.baseInfo.status === "1") || (!!$scope.baseInfo.activeOrder) || (!!$scope.baseInfo.merchantN));
        var activeTimeIsNull = !$scope.baseInfo.activeTimeStart || !$scope.baseInfo.activeTimeEnd;

        if (notAllowActiveTimeIsNull && activeTimeIsNull) {
            $scope.notice("激活日期日期不能为空");
            return;
        }
        if($scope.paginationOptions.pageNo==1){
            $http.post('activityDetail/selectTotalMoney.do',"baseInfo="+angular.toJson($scope.baseInfo),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(!data){
                        $scope.notice("没有查询到数据");
                        return;
                    }else{
                        $scope.totalData = data.totalData;
                    }
                })
        }
        $http.post('activityDetail/selectHappyBackDetail.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
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
	};
    $scope.columnDefs = [
        {field: 'id',displayName: '序号',pinnable: false,width: 180,sortable: false},
        {field: 'activeOrder',displayName: '激活流水号',pinnable: false,width: 180,sortable: false},
        {field: 'activeTime',displayName: '激活时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'activityCode',displayName: '活动类型',pinnable: false,width: 150,sortable: false,cellFilter:"formatDropping:"+ angular.toJson($scope.subjectTypes)},
        {field: 'activityTypeNo',displayName: '欢乐返子类型',pinnable: false,width: 150,sortable: false},
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
        {field: 'billingStatus',displayName: '入账状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:[{text:'未入账',value:'0'},{text:'已入账',value:'1'}]"},
        {field: 'billingTime',displayName: '入账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
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
        {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<div class="lh30">'
			+'<a class="lh30" ng-show=" row.entity.agentLevel == 1 && row.entity.agentType!= 11 " ' 
			+'ui-sref="active.happyBackDetail({hId:row.entity.id})" target="_black">详情</a>'
			+'</div>'
        }
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

	
	$http({
		url: 'agentInfo/getLoginAgentInfo',
		method: "POST"
	}).success(function (data) {
		$scope.curAgentType = data.data.agentType;
		console.debug($scope.curAgentType);
        $scope.currentLoginAgentIsFirstLevel = data && data.success && data.data && data.data.agentLevel == 1;
		if(! $scope.currentLoginAgentIsFirstLevel){
            $scope.columnDefs.splice(17, 2);
            $scope.columnDefs.splice(25, 4);
		}
    });
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
		$scope.baseInfo = {merTeamId:  $scope.initTeamId,merchantN:"",agentNode:"",status:"",discountStatus:-1,checkStatus:"",
            activeTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD')+' 00:00:00',
            activeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            isStandard: '',
            minCumulateTransAmount:'',maxCumulateTransAmount:'',
            minStandardTime:'', maxStandardTime:'',
            minMinusAmountTime:'', maxMinusAmountTime:'',
            minAddAmountTime:'', maxAddAmountTime:'',
				activityCode:"",liquidationStatus:"",accountCheckStatus:"", merchantType: "1"};
        $scope.disabledMerchantType = true;
	};
	
    //欢乐返活动子类型
    $scope.checkActivityCode = function(activityCode){
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
    $scope.checkActivityCode2();
	
	//导出
	 $scope.exportExcel=function(){
         var notAllowActiveTimeIsNull = !(($scope.baseInfo.status === "1") || (!!$scope.baseInfo.activeOrder) || (!!$scope.baseInfo.merchantN));
         var activeTimeIsNull = !$scope.baseInfo.activeTimeStart || !$scope.baseInfo.activeTimeEnd;

         if (notAllowActiveTimeIsNull && activeTimeIsNull) {
             $scope.notice("激活日期日期不能为空");
             return;
         }
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
                        $scope.exportInfoClick("activityDetail/exportHappyBack.do?pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
                            + $scope.activityGrid.totalItems,
                            {"baseInfo":angular.toJson($scope.baseInfo)});
                         // location.href="activityDetail/exportHappyBack.do?"
                         // + "baseInfo="+encodeURI(angular.toJson($scope.baseInfo))+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
                         // + $scope.activityGrid.totalItems;
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
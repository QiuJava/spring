/**
 * 我的信息-我的账户 控制器
 * @author xyf1
 */

angular.module('inspinia').controller('myInfoAccountCtrl',function($scope,$http,$rootScope,$state,$stateParams,$compile,$filter,i18nService, SweetAlert){
	$scope.dictList = {};
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$scope.operations=[{text:"全部",value:""},{text:"增加",value:"credit"},{text:"减少",value:"debit"},{text:"冻结",value:"freeze"},{text:"解冻",value:"unFreeze"}]
	$scope.status=[{text:"全部",value:""},{text:"未结算",value:"1"},{text:"已结算",value:"2"},{text:"结算失败",value:"3"}];

	$scope.hasSafePhone = false;//是否设置安全手机
	$scope.hasSafePassword = false;//是否设置安全密码
	$scope.safePassword = "";//资金密码
	$scope.safePasswordFlag = $rootScope.safePasswordFlag;//用于判断提现是否需要资金密码

	$scope.transTypes = [];
	$http.get('myInfo/selectTransType.do').success(function(data){
		var dataArr = data.data;
		for (var i = 0; i < dataArr.length; i++) {
			$scope.transTypes.push(dataArr[i]);
		}
	});
	
	$http.get('myInfo/getPhone.do').success(function(msg){
		  if(msg.safephone!=null && msg.safephone.length==11){
			  $scope.hasSafePhone = true;
			  $scope.hasSafePassword = msg.hasSafePassword;
		  };
	 });
	
	
	
	/*// 不同oem公共拥有的下拉选项
    var defaultTransType = [
        {text:"T0冲正撤销",value:"000000"},
        {text:"代理商分润调账",value:"000023"},
        // {text:"欢乐送财务核算记账",value:"000027"},
        // {text:"欢乐送三个月内没有刷满活动金额",value:"000029"},
        {text:"代理商分润入账",value:"000031"},
        {text:"代理商分润提现",value:"000032"},
        // {text:"欢乐返循环送-清算核算入账",value:"000037"},
        {text:"欢乐返财务核算入账",value:"000039"},
        {text:"活动补贴账号代理商提现出账",value:"000028"},
        {text:"欢乐返未完成最低任务扣款",value:"000060"},
        {text:"欢乐返完成任务奖励",value:"000061"},
        {value: "000035",text:"上级代理商分润下级代理商"},
        {value: "000043",text:"代理商活动补贴账户提现出账（新）"},
        {value: "000062",text:"代理商分润的提现"},
        {value: "000087",text:"活动补贴账户调账"},
        {value: "000112",text:"聚合平台购买机具给专员经理银行家补贴分配"},
        {value: "000113",text:"聚合平台机具激活后给专员经理银行家补贴分配"},
        {value: "000114",text:"聚合平台转机具款给组织品牌商"},
        {value: "000116",text:"聚合平台刷卡交易分润分配"}
    ];
    var otherTransType = [
        {text:"超级推分润提现",value:"000033"},
        {value: "000040",text:"微创业代理商分润入账"},
        {value: "000041",text:"邀请好友有奖入账"},
        {value: "000042",text:"欢乐送-财务核算记账（新）"},
        {value: "000044",text:"欢乐送代理商分润账户扣款"},
        {value: "000053",text:"代理商还款分润提现"},
        {value: "000067",text:"红包账户提现"},
        {value: "000068",text:"分润账户账户发红包"}
    ];

    // 根据不同的oem增加各自的下拉选项
    if ($scope.oemType === "SQIANBAO"){	// 盛钱包
        $scope.transTypes = defaultTransType.concat(otherTransType);
        $scope.transTypes.push({text:"欢乐送财务核算记账",value:"000027"});
        $scope.transTypes.push({text:"欢乐送三个月内没有刷满活动金额",value:"000029"});
        $scope.transTypes.push({text:"欢乐返循环送-清算核算入账",value:"000037"});
	}else if($scope.oemType === "DIANFU"){		// 点付
        $scope.transTypes =  defaultTransType.concat(otherTransType);;
	}else if($scope.oemType === "ZHZFPAY"){
        $scope.transTypes = defaultTransType;
	}else if($scope.oemType === "REDEM"){		// 积分兑换
        $scope.transTypes = [
            {text:"T0冲正撤销",value:"000000"},
            {text:"积分兑换分润",value:"000071"},
            {text:"积分兑换代理商提现",value:"000073"}
		];
	}else if($scope.oemType === "REDEMACTIVE"){
        $scope.transTypes = [
            {text:"T0冲正撤销",value:"000000"},
            {value: "000075",text:"积分兑换（激活版）分润"},
            {value: "000077",text:"积分兑换（激活版）代理商提现"}
        ];
	}else if($scope.oemType === "PERAGENT"){
    	$scope.transTypes = [
            {text:"固定收益",value:"000031"},
            {text:"大盟主固定收益",value:"000035"},
            {text:"交易分润",value:"000090"},
            {text:"管理津贴",value:"000091"},
            {text:"成长津贴",value:"000092"},
            {text:"王者奖金",value:"000103"},
            {text:"荣耀奖金",value:"000093"},
            {text:"机具分润",value:"000102"},
            {text:"欢乐返机构返大盟主",value:"000104"},
            {text:"升级大盟主分润账户转移",value:"000106"},
            {text:"升级大盟主机具款项账户转移",value:"000107"},
            {text:"活动补贴转分润",value:"000108"},
            {text:"活动补贴转分润账户",value:"000109"}
		]
		if($scope.$root.entityAgentLevel!=1){
            $scope.transTypes.splice(0,1,{text:"固定收益",value:"000035"});
            $scope.transTypes.splice(1,1);
		}
	}else if($scope.oemType === "YPOSPAY"){ 
        $scope.transTypes = defaultTransType;
	}else{                                      // 中和付 中和宝
        $scope.transTypes =  defaultTransType.concat(otherTransType);;
        $scope.transTypes.push({text:"欢乐返循环送-清算核算入账",value:"000037"});
	}*/
	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setsTime=function(setTime){
		$scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	seteTime=function(setTime){
		$scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
    
    $scope.switchStatus = 1;
	$scope.accountAllInfo = [];
	
	//账户类型查询
	$scope.accountTypes=[{text:"全部",value:""}];
	$scope.subjectNos=[];
	//debugger;
     for(var i=0; i<$scope.subjectNoss.length; i++){
    	 if ($scope.oemType === "PERAGENT") {//超级盟主不显示活动补贴
    		 if ($scope.subjectNoss[i].sys_value != '224106') {
    			 $scope.accountTypes.push({value:$scope.subjectNoss[i].sys_value,text:$scope.subjectNoss[i].sys_name});
    			 $scope.subjectNos.push({value:$scope.subjectNoss[i].sys_value,text:$scope.subjectNoss[i].sys_name});
			}
		}else{
			$scope.accountTypes.push({value:$scope.subjectNoss[i].sys_value,text:$scope.subjectNoss[i].sys_name});
			$scope.subjectNos.push({value:$scope.subjectNoss[i].sys_value,text:$scope.subjectNoss[i].sys_name});
		}
     }
     if ($scope.oemType === "YPOSPAY")  {
         $scope.accountTypes = [
         	{text: "代理商分润", value: "224105"},
             {text: "活动补贴账户", value: "224106"},
             // {text: "信用卡分润", value: "224114"},
             // {text: "超级银行家分润", value: "224116"},
             // {text: "超级兑帐号", value: "224120"},
             // {text: "积分兑换（激活版）代理商", value: "224123"},
             // {text: "信用卡管家账户", value: "224121"},
             // {text: "机具款项账户", value: "224124"}
         ];
	 }

     $scope.defaultBalance = {
         balance: '0',
         avaliBalance: '0',
         controlAmount: '0'
	 };
    $scope.myAccount = {balanceToday : 0};
    // 收款账户余额信息
    $scope.account = $.extend({}, $scope.defaultBalance);
    // 欢乐返余额信息
    $scope.happyAccount = $.extend({}, $scope.defaultBalance);
    // 积分兑换余额信息
    $scope.redemBalance = $.extend({}, $scope.defaultBalance);
    // 积分兑换(激活版)余额信息
    $scope.redemActiveBalance = $.extend({}, $scope.defaultBalance);
    // 信用卡还款余额信息
    $scope.replayAccount = $.extend({}, $scope.defaultBalance);
    // 银行家余额信息
    $scope.replayAccountSuperBank = $.extend({}, $scope.defaultBalance);
    //人人代理相关
    $scope.perAgentAccountService = $.extend({}, $scope.defaultBalance);
    $scope.terminalAccountService = $.extend({}, $scope.defaultBalance);

	$scope.getAccountBalance = function(){
		$http({
			method:'post',
			url:'myInfo/account.do'
		}).success(function(d){
			if(d.success){
				var data = d.data;
                $scope.redemBalance = $.extend({}, $scope.defaultBalance, data.redemBalance);
                $scope.redemActiveBalance = $.extend({}, $scope.defaultBalance, data.redemActiveBalance);
                $scope.replayAccount = $.extend({}, $scope.defaultBalance, data.replayAccount);
                $scope.replayAccountSuperBank = $.extend({}, $scope.defaultBalance, data.replayAccountSuperBank);
                $scope.account = $.extend({}, $scope.defaultBalance, data.account);
                $scope.happyAccount = $.extend({}, $scope.defaultBalance, data.happyAccount);
                $scope.creditAccount = $.extend({}, $scope.defaultBalance, data.creditAccount);
                $scope.terminalAccount = $.extend({}, $scope.defaultBalance, data.terminalAccount);

                $scope.hasReplayAccount = data.hasReplayAccount;
				$scope.myAccount.service = data.accountService;
                $scope.myAccount.happlyService = data.account2Service;
                $scope.myAccount.todaySuperPushAmount = data.todaySuperPushAmount;
                $scope.myAccount.todayCreditAmount = data.todayCreditAmount;
				$scope.profitSwitch = data.profitSwitch;//分润日结功能
                $scope.agentInfo = data.agentInfo;
                $scope.isOpen = data.agentInfo.isOpen;
                $scope.myAccount.balanceToday = data.profitAmount || '0';
                
                $scope.happyTixianSwitch = data.happyTixianSwitch;
                $scope.defaultStatus = data.defaultStatus;
                $scope.cashBackSwitch = data.cashBackSwitch == 1 ? "打开" : "关闭";
                $scope.retainAmount = data.retainAmount;
                if ($rootScope.agentType == 11) {
                	$scope.account.avaliBalance = $scope.account.avaliBalance - $scope.retainAmount;
                	if ($scope.account.avaliBalance <= 0) {
                		$scope.account.avaliBalance = 0;
					}
				}

                //人人代理相关
				$scope.perAgentAccountService.service = data.perAgentAccountService;
				$scope.terminalAccountService.service = data.terminalAccountService;
				$scope.selectAccountInfo();
			} else {
				$scope.notice(d.message);
			}
		});
	};
	$scope.getAccountBalance();
	$scope.money = $scope.account.avaliBalance;
	$scope.info = {transType:"",sTime:moment(new Date().getTime()).format('YYYY-MM-DD') + ' 00:00:00',eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			ioType:"",subjectNo:""};
	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setBeginTime=function(setTime){
		$scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

	}

	setEndTime=function(setTime){
		$scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

	}
	
	$scope.reset = function(){
		$scope.info = {transType:"",
			sTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
			eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			ioType:"",subjectNo:""};
	};
	
	//红包余额查询
	$scope.selectBalance=function(){
		$http.post('myInfo/selectBalance').success(function(result){
			$scope.redBalance = result.redBalance;
		});
	};
	$scope.selectBalance();
    /**
     * 红包余额提现,默认全部提现
     */
    $scope.withdrawRedBalance=function(){
    	if($scope.safePasswordFlag && ((!$scope.hasSafePhone || !$scope.hasSafePassword))){
			$("#safeModel").modal("show");
			return ;
		}
		if (($scope.safePasswordFlag) && !$scope.safePassword){
			$scope.notice("请输入资金密码");
			return;
		}
		//校验资金密码是否正确
		// $scope.checkSafePassword();
		$scope.submitting = true;
		$http.post('myInfo/withdrawRedBalance?redBalance=' + $scope.redBalance).success(function(result){
			$scope.submitting = false;
			$scope.notice(result.msg);
			$scope.selectBalance();
            $scope.safePassword = "";
		});
    };
	$scope.selectAccountInfo=function(){
		if(!($scope.info.sTime &&$scope.info.eTime)){
			$scope.notice("记账时间不能为空，请重新选择");
			return;
		}
		var data={"transType":$scope.info.transType,"sTime":$scope.info.sTime,"eTime":$scope.info.eTime,"ioType":$scope.info.ioType,"subjectNo":$scope.info.subjectNo,
				"pageNo":$scope.paginationOptions.pageNo,"pageSize":$scope.paginationOptions.pageSize};
		$http.post(
			'myInfo/getAccountTranInfo',
			 "info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(result.bols){
				$scope.accountAllInfo = result.list; 
				$scope.accountGrid.totalItems = result.total;
			}else{
				$scope.notice(result.msg);
			}
		});
	};
	//表格数据
	$scope.columns = [
		{ field: 'subjectNo',displayName:'账户类别',cellFilter:"formatDropping:"+angular.toJson($scope.subjectNos)},
		{ field: 'transOrderNo',displayName:'订单编号'},
		{ field: 'recordDate',displayName:'记账时间',width:200,cellTemplate:"<div class='ui-grid-cell-contents'>{{row.entity.recordDate | date:'yyyy-MM-dd'}} {{row.entity.recordTime | date:'HH:mm:ss'}}</div>"},
		{ field: 'transTypeCn',displayName:'交易类型',width:180},
		{ field: 'debitCreditSide',displayName:'操作',cellFilter:"formatDropping:"+angular.toJson($scope.operations)},
		{ field: 'recordAmount',displayName:'金额'},
		{ field: 'balance',displayName:'账户余额' },
		{ field: 'avaliBalance',displayName:'可用余额' },
		{ field: 'summaryInfo',displayName:'摘要' }
	];

	$scope.accountGrid ={
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
				$scope.selectAccountInfo();
			});
		}
	};

	//=========== start 提现公用函数 ======================
	$scope.withdrawCashFunction = function (modalId, withdrawCashUrl) {
		return {
			showModal: function () {
				if($scope.safePasswordFlag && ((!$scope.hasSafePhone || !$scope.hasSafePassword))){
					$("#safeModel").modal("show");
					return ;
				}
				if(modalId=='replayAccountMode'&&$scope.agentInfo.accountType==2){
                    if ($scope.agentInfo.idCardNo == null || $scope.agentInfo.idCardNo == '') {
                        $("#idCardModel").modal("show");
                        return;
                    }
				}
				
                $scope.money = 0;
                $scope.getSingleNumAmount();
				$("#" + modalId).modal("show");
            },
			hideModal: function () {
                $scope.money = 0;
                $("#" + modalId).modal("hide");
            },
			takeAll: function (balance) {
                $scope.money = balance.avaliBalance;
            },
            withdrawCash: function (balance) {
				var self = this;
                if (!$scope.money){
                    $scope.notice("请填入有效提现金额");
                    return;
                }
                if (balance.avaliBalance - $scope.money < 0) {
                    $scope.notice("账户余额不足");
                    return;
                }
				if ($scope.safePasswordFlag && !$scope.safePassword){
					$scope.notice("请输入资金密码");
					return;
				}
				if($scope.safePasswordFlag){
					$scope.entryPassword();
				}else{
					$scope.params = {"money":$scope.money,"safePassword":""}
				}
                $http({
                    url: withdrawCashUrl + '?money=' + $scope.money + '&safePassword=' + $scope.newPasword,
                    method: 'POST'
                }).success(function (data) {
                    if (data.success){
						balance.avaliBalance = balance.avaliBalance - $scope.money;
                        $scope.notice("提现操作提交成功");
                        self.hideModal();
                    }else{
                    	if("请先设置安全手机"==data.message){
							self.hideModal();
                    		setTimeout(function () {
        						$("#safeModel").modal("show");
        						}, 1000);
                    	}else{
                    		$scope.notice(data.message);
							if (data.message != "资金密码不正确,请重新输入"){
								self.hideModal();
							}
                        }
                    }
                    $scope.safePassword = "";
                });
            }
		}
    };
	// 积分兑换账户余额
    $scope.redemWithdrawCash = $scope.withdrawCashFunction("redemBalanceModel", "myInfo/takeRedemBalance");
    // 积分兑换激活版账户余额
    $scope.redemActiveWithdrawCash = $scope.withdrawCashFunction("redemActiveBalanceModel", "myInfo/takeRedemActiveBalance");
    // 信用卡分润账户余额
    $scope.replayWithdrawCash = $scope.withdrawCashFunction("replayAccountMode", "myInfo/takeReplayBalance");
    // 银行家分润提现
    $scope.superBankWithdrawCash = $scope.withdrawCashFunction("replayAccountSuperBankMode", "myInfo/takeReplayBalanceSuperBank");
    // 信用卡管家账户余额提现
    $scope.creditWithdrawCash = $scope.withdrawCashFunction("creditAccountModel", "myInfo/takeCreditBalance");

    //=========== end 提现公用函数 ======================
    
	//欢乐送补贴提现 tgh331
	$scope.cashAll = function(){//全部提现 全部提现金额 = 可用余额 - 留存金额
		$scope.money = $scope.happyAccount.avaliBalance - $scope.retainAmount;
		if ($scope.money <= 0) {
			$scope.notice("余额不足,没有可提现金额");
			return;
		}
		$scope.money = $scope.money.toFixed(2);
	};
	//提现手续费
	$scope.getSingleNumAmount = function(){
		$http.post(
				'myInfo/getSingleNumAmount',"money="+$scope.money,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(msg){
			$scope.singleNumAmount = msg.singleNumAmount;//欢乐送提现手续费
			$scope.singleNumAmount1 = msg.singleNumAmount1;//账户余额提现手续费
			$scope.singleNumAmountSuperBank = msg.singleNumAmountSuperBank;//超级银行家分润提现手续费
            $scope.perAgentFee = msg.perAgentFee;//人人代理税费费率
            $scope.perAgentProfitSingleAmount = msg.perAgentProfitSingleAmount;//人人代理分润单笔提现手续费
            $scope.perAgentProfitTotalFee = msg.perAgentProfitTotalFee;//人人代理分润提现总手续费
            $scope.perAgentActivitySingleAmount = msg.perAgentActivitySingleAmount;//人人代理活动单笔提现手续费
            $scope.perAgentActivitylTotalFee = msg.perAgentActivitylTotalFee;//人人代理活动提现总手续费
            $scope.perAgentTerminalSingleAmount = msg.perAgentTerminalSingleAmount;//人人代理机具提现手续费
		});
	};
	$scope.getSingleNumAmount();
	//是否显示提现按钮
	$http.post('myInfo/isShowButton').success(function(msg){
		$scope.flag = msg.flag;
	});
	$scope.takeCashModel = function(){
		if($scope.safePasswordFlag && ((!$scope.hasSafePhone || !$scope.hasSafePassword))){
			$("#safeModel").modal("show");
			return ;
		}

		if ($scope.happyTixianSwitch != '0') {
			$scope.notice("系统正在执行入账，请稍后再试！");
			return;
		}
        $scope.money=0;
        $scope.getSingleNumAmount();
		$("#activityModel").modal("show");
	};
	$scope.activityModelCancel = function(){
	    $scope.money = 0;
		$("#activityModel").modal("hide");
	};
	$scope.takeBalanceMode = function(){
		if($scope.safePasswordFlag && ((!$scope.hasSafePhone || !$scope.hasSafePassword))){
			$("#safeModel").modal("show");
			return ;
		}
		
		$scope.money=0;
        $scope.getSingleNumAmount();
		$("#balanceModel").modal("show");
	};
	
	$scope.goSetSafePhone = function(){
		$("#safeModel").modal("hide");
		setTimeout(function () {
		$state.transitionTo('myInfo.safeSet',{type:1},{reload:false});
		}, 1000);
		
		
	};
	$scope.goSetSafePhoneCance = function(){
		$("#safeModel").modal("hide");
	};
	
	$scope.takePerAgentBalanceMode = function(){
		
		/*超级盟主要求不加
		if(!$scope.hasSafePhone){
			$("#safeModel").modal("show");
			return ;
		}*/
		
		
		$scope.money=0;
        $scope.getSingleNumAmount();
		$("#perAgentBalanceModel").modal("show");
	};
	$scope.takeTerminalBalanceMode = function(){
		
		if($scope.safePasswordFlag && ((!$scope.hasSafePhone || !$scope.hasSafePassword))){
			$("#safeModel").modal("show");
			return ;
		}
		
		$scope.money=0;
        $scope.getSingleNumAmount();
		$("#terminalAccountModel").modal("show");
	};
	$scope.takeBalanceModelCancel = function(){
        $scope.money = 0;
		$("#balanceModel").modal("hide");
	};
	$scope.takePerAgentBalanceModelCancel = function(){
        $scope.money = 0;
		$("#perAgentBalanceModel").modal("hide");
	};
	$scope.terminalBalanceModelCancel = function(){
        $scope.money = 0;
		$("#terminalAccountModel").modal("hide");
	};
	$scope.takeHapplyBackModel = function(){
		$("#happlyBackModel").modal("show");
	};
	$scope.takeHapplyBackModelCancel = function(){
        $scope.money = 0;
		$("#happlyBackModel").modal("hide");
	};
	$scope.updateAllTerActivity = function(){
		//确定提交提现
		var data = $scope.money;
		if (data <= 0) {
			$scope.notice("请填入有效提现金额");
			return;
		}
		if ($scope.myAccount.happlyBalance - (data + $scope.retainAmount) < 0) {
			$scope.notice("账户余额不足");
			return;
		}
		if ($scope.safePasswordFlag && !$scope.safePassword){
			$scope.notice("请输入资金密码");
			return;
		}
		//校验资金密码是否正确
		if ($scope.safePasswordFlag){
			$scope.entryPassword();
		}else{
			$scope.params = {"money":$scope.money,"safePassword":""}
		}
		$http.post("myInfo/withDrawCash","info="+angular.toJson($scope.params),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if (data.status) {
					$scope.notice(data.msg);
					$("#activityModel").modal("hide");
					$scope.myAccount.happlyBalance = $scope.myAccount.happlyBalance - $scope.money;
				}else{
					if(data.needset){
						if (data.msg != "资金密码不正确,请重新输入"){
							$("#activityModel").modal("hide");
						}
						setTimeout(function () {
							$("#safeModel").modal("show");
						}, 1000);
					}else{
						$scope.notice(data.msg);
						if (data.msg != "资金密码不正确,请重新输入"){
							$("#activityModel").modal("hide");
						}
					}
				}
                $scope.safePassword = "";
			});
	};
	//账户余额提现tgh411
	$scope.cashAllBalance = function(){//全部提现
		$scope.money = $scope.account.avaliBalance.toFixed(2);
		if($scope.money <= 0){
			$scope.money = 0;
			$scope.notice("账户余额不足");
			return;
		}
	};
	//焦点获取手续费
	$scope.take = function(){
		$scope.getSingleNumAmount();
	};
	$scope.takeBalance = function(){
		//确定提交提现
		var data = $scope.money;
		if (data <= 0) {
			$scope.notice("请填入有效提现金额");
			return;
		}
		if ($scope.account.avaliBalance - data < 0) {
			$scope.notice("账户余额不足");
			return;
		}
		if ($scope.safePasswordFlag && !$scope.safePassword){
			$scope.notice("请输入资金密码");
			return;
		}
		if ($scope.safePasswordFlag){
			$scope.entryPassword();
		}else{
			$scope.params = {"money":$scope.money,"safePassword":""}
		}
		$http.post("myInfo/takeBalance","info="+angular.toJson($scope.params),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if (data.status) {
					$scope.notice(data.msg);
					$("#balanceModel").modal("hide");
					$scope.account.avaliBalance = $scope.account.avaliBalance - $scope.money;
				}else{
					if(data.needset){
						if (data.msg != "资金密码不正确,请重新输入"){
							$("#balanceModel").modal("hide");
						}
						setTimeout(function () {
							$("#safeModel").modal("show");
						}, 1000);
					}else{
						$scope.notice(data.msg);
						if (data.msg != "资金密码不正确,请重新输入"){
							$("#balanceModel").modal("hide");
						}
					}
				}
                $scope.safePassword = "";
			});
	};

	/**
	 * 加密传送
	 */
	$scope.entryPassword = function(){
		var encrypt = new JSEncrypt();
		encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ9s1qlOyv9qpuaTqauW6fUftzE50rVk3yVPZwv1aO1Ch/XSEz76xCwkyvqpaqceRXrPpdBmO5+ruJ+I8osOHo7L5GWEOcMOO+8izp9hXKBBrmRMD4Egpn00k9DhVIEKp/vyddZPS/doxB8onhN6poTJDLdFLFVEicMf52caN9GQIDAQAB");
		$scope.newPasword = encrypt.encrypt($scope.safePassword);
		$scope.params = {"money":$scope.money,"safePassword":$scope.newPasword}
	}

	function sleep(milliseconds) {
	     setTimeout(function(){
	         var start = new Date().getTime(); 
	         while ((new Date().getTime() - start) < milliseconds){
	         }
	     },0);
	}
	
	//人人代理分润账户余额
	$scope.takePerAgentBalance = function(){
		//确定提交提现
		var data = $scope.money;
		if (data <= 0) {
			$scope.notice("请填入有效提现金额");
			return;
		}
		if ($scope.account.avaliBalance - data < 0) {
			$scope.notice("账户余额不足");
			return;
		}
		if ($scope.safePasswordFlag && !$scope.safePassword){
			$scope.notice("请输入资金密码");
			return;
		}
		if($scope.safePasswordFlag){
			$scope.entryPassword();
		}else{
			$scope.params = {"money":$scope.money,"safePassword":""}
		}
		$http.post("myInfo/takeBalance","info="+angular.toJson($scope.params),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(data){
				if (data.status) {
					$scope.notice(data.msg);
					$("#perAgentBalanceModel").modal("hide");
					$scope.account.avaliBalance = $scope.account.avaliBalance - $scope.money;
				}else{
					$scope.notice(data.msg);
					if (data.msg != "资金密码不正确,请重新输入"){
						$("#perAgentBalanceModel").modal("hide");
					}
				}
                $scope.safePassword = "";
			});
	};

    /**
	 * 机具款项余额提现
     */
    $scope.takeTerminalBalance = function(){
        //确定提交提现
        var data = $scope.money;
        if (data <= 0) {
            $scope.notice("请填入有效提现金额");
            return;
        }
        if ($scope.terminalAccount.avaliBalance - data < 0) {
            $scope.notice("账户余额不足");
            return;
        }
		if ($scope.safePasswordFlag && !$scope.safePassword){
			$scope.notice("请输入资金密码");
			return;
		}
		if($scope.safePasswordFlag){
			$scope.entryPassword();
		}else{
			$scope.params = {"money":$scope.money,"safePassword":""}
		}
        $http.post("myInfo/takeTerminalBalance","info="+angular.toJson($scope.params),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if (data.status) {
                    $scope.notice(data.msg);
                    $("#terminalAccountModel").modal("hide");
                    $scope.terminalAccount.avaliBalance = $scope.terminalAccount.avaliBalance - $scope.money;
                }else{
                    $scope.notice(data.msg);
                    if (data.msg != "资金密码不正确,请重新输入"){
						$("#terminalAccountModel").modal("hide");
					}
                }
                $scope.safePassword = "";
            });
    };
    $scope.terminalTakeAll = function () {
        $scope.money = $scope.terminalAccount.avaliBalance + "";
        if ($scope.money <= 0) {
            $scope.money = 0;
            $scope.notice("账户余额不足");
            return;
        }
    };

    $scope.exportingSharePreDay = false;
    $scope.exportInfo=function(){
		if(!($scope.info.sTime &&$scope.info.eTime)){
			$scope.notice("记账时间不能为空，请重新选择");
			return;
		}
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
                    location.href="myInfo/exportAccountTranInfo?"+$.param($scope.info);
                }
            });
    };

    $scope.idCardNo=null;
    $scope.bankNames="";
    $scope.subSize=0;
    $scope.isRepeatStatus=false;
    $scope.isRepeatStatus2=false;
    $scope.idCardModel = function(){
        $("#idCardModel").modal("show");
    };

    $scope.idCardModelCancel = function(){
        $scope.idCardNo=null;
        $("#idCardModel").modal("hide");
    };

    $scope.repeatCardAuth3 = function(){
        $scope.isRepeatStatus=false;
        $scope.isRepeatStatus2=false;
    };

    //三要素验证
    $scope.cardAuth3 = function(){
        //确定提交提现
        var data = {"info":$scope.agentInfo}
        if ($scope.idCardNo == null || $scope.idCardNo == '') {
            $scope.notice("身份证号不能为空");
            return;
        }
        $scope.agentInfo.idCardNo=$scope.idCardNo;
        var data={"info":$scope.agentInfo}
        $http.post("myInfo/cardAuth3",angular.toJson(data))
            .success(function(data) {
                if (data.status) {
                    $scope.notice(data.msg);
                    $("#idCardModel").modal("hide");
                }else{
                    $scope.agentInfo.idCardNo=null;
                    $scope.notice(data.msg);
                }
                $scope.bankNames=data.bankNames;
                $scope.isRepeatStatus=data.isRepeatStatus;
                $scope.isRepeatStatus2=data.isRepeatStatus2;
            });
    };
});
angular.module('inspinia').controller('userInfoSuperBankDetailCtrl',function($scope,$rootScope,$state,$filter,$log,$http,$stateParams,$compile,$uibModal,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.bools=[{text:"是",value:1},{text:"否",value:0}];
	$scope.debitCreditSides=[{text:"减少",value:"debit"},{text:"增加",value:"credit"},{text:"冻结",value:"freeze"},{text:"解冻",value:"unfreeze"},{text:"全部",value:""}]
	$scope.info={debitCreditSide:""};
	//清空
	$scope.clear=function(){
    	$scope.info={debitCreditSide:""};
	}
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$http.post('superBank/selectDetail.do?userId='+$stateParams.userId)
	.success(function(data) {
		if(!data){
			$scope.notice(data.msg);
			return;
		}
		$scope.data = data.userInfoSuperBank;
		$scope.userCard = data.userCard;
		$scope.agentCount = data.agentCount;
		$scope.notAgentCount = data.notAgentCount;
		if($scope.data.accountStatus != '1'){
			return;
		}
		// $scope.selectAccountInfo();
		//账户查询
		var merDate={"userCode":$stateParams.userCode};
		$http.post('superBank/getAccountInfo',angular.toJson(merDate))
				.success(function(data){
					if(data.bols){
						$scope.accountBalances.data = data.alist;
					}else{
						$scope.notice(data.msg);
					}
				})
	});
	//账户明细
	$scope.accountDetail={                           //配置表格
			paginationPageSize:10,                  //分页数量
			paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
			useExternalPagination: true,
			enableHorizontalScrollbar: 1,        //横向滚动条
			enableVerticalScrollbar : 1,
			columnDefs:[                           //表格数据
				{ field: 'recordDate',displayName:'记账时间',
					cellTemplate:"<div class='lh30'>{{row.entity.recordDate | date:'yyyy-MM-dd'}} {{row.entity.recordTime | date:'HH:mm:ss'}}</div>"
				},
				{ field: 'debitCreditSide',displayName:'操作',
					cellFilter:"formatDropping:[{text:'减少',value:'debit'},{text:'增加',value:'credit'},{text:'冻结',value:'freeze'},{text:'解冻',value:'unFreeze'}]"
				},
				{ field: 'recordAmount',displayName:'金额' },
//				{ field: 'balance',displayName:'余额',width:150 },
				{ field: 'avaliBalance',displayName:'可用余额' },
//		         { field: '',displayName:'摘要',width:150 },
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					$scope.paginationOptions.pageNo = newPage;
					$scope.paginationOptions.pageSize = pageSize;
					$scope.selectAccountInfo();
				});
			}
		};
	$scope.accountBalances={                           //配置表格
//			data:'accountBalances',
			columnDefs:[                           //表格数据
				{ field: 'accountNo',displayName:'账号'},
				{ field: 'balance',displayName:'余额'},
//				{ field: 'avaliBalance',displayName:'可用余额' ,width:150}
//				,
				{ field: 'controlAmount',displayName:'冻结金额' },
//				{ field: 'settlingAmount',displayName:'结算中余额',width:150 },
				//{ field: 'preFreezeAmount',displayName:'预冻结余额',width:150 },
			]
	};
	//账户明细
	$scope.selectAccountInfo=function(){
		if($scope.info.sdate > $scope.info.edata){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		var data={"debitCreditSide":$scope.info.debitCreditSide,"merNo":$stateParams.userCode,"sdate":$scope.info.sdate,"edata":$scope.info.edata,"operation":$scope.info.operation,
			"pageNo":$scope.paginationOptions.pageNo,"pageSize":$scope.paginationOptions.pageSize};
		$http.post(
				'superBank/getAccountTranInfo',
				"info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(result.bols){
				$scope.accountDetail.data = result.list;
				$scope.accountDetail.totalItems = result.total;
			}else{
				$scope.notice(result.msg);
			}
		});

	};
});

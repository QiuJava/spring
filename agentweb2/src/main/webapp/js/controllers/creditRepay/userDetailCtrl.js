/**
 * 商户详情查看
 */
angular.module('inspinia').controller('userDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){

	//数据源
	$scope.info={};
	$scope.imageList={};
	$scope.debitCardList={};
	$scope.creditCardList={};
	$http.get('repayMerchant/queryMerchantDetailByMerchantNo?merchantNo='+$stateParams.merchantNo)
		.success(function(data) {
			if(data.status){
				$scope.info = data.info;
				$scope.imageList = data.imageList;
				$scope.debitCardList = data.debitCardList;
				$scope.creditCardList = data.creditCardList;
			}else{
				$scope.notice(data.msg);
			}
		});

	$http.get('repayMerchant/queryAccountAmount?merchantNo='+$stateParams.merchantNo)
		.success(function(data) {
			if(data.status){
				$scope.accountBalance.data = data.accountInfo;
			}else{
				$scope.notice(data.msg);
			}
		});

	$scope.accountBalance={	//配置表格
//		data:'accountInfo',
		columnDefs:[                           //表格数据
			// { field: 'accountNo',displayName:'账号',width:230},
			// { field: 'avaliBalance',displayName:'余额',cellFilter:"currency:''",width:150},
			// { field: 'freezeAmount',displayName:'冻结余额',width:150 ,cellFilter:"currency:''",cellTemplate:'<span class="checkbox">{{row.entity.freezeAmount === null ? "0.00" : row.entity.freezeAmount }}</span>'}
            { field: 'balanceNo',displayName:'账号'},
            { field: 'balance',displayName:'余额',cellFilter:"currency:''"},
            { field: 'freezeAmount',displayName:'冻结余额',cellFilter:"currency:''"}
			]
	};
//	$scope.paginationOptions=angular.copy($scope.paginationOptions);

});
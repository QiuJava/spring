/**
 * 商户详情查看
 */

angular.module('inspinia').controller('superPushMerchantDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
	
	//数据源
	$scope.merchantInfo = [];
	$scope.superPush = [];
	$scope.terminalInfoList = [];
	$scope.merchantCardInfo = [];
	$http.get('superPushAction/selectMerchantDetail.do?mertId='+$stateParams.mertId)
	.success(function(data) {
//		if(data.bols){
//			if(data.listmr==null||data.listmsq==null||data.serviceMgr==null||data.mi==null||data.mbp==null||data.listel==null||data.listmri==null){
//				$scope.notice("数据为空");
//				return;
//			}else{
				$scope.merchantInfo = data.merchantInfo;
				//商户状态：0：商户关闭；1：正常；2 冻结
				switch ($scope.merchantInfo.status) {
				case '0':
					$scope.merchantInfo.status='商户关闭';
					break;
				case '1':
					$scope.merchantInfo.status='正常';
					break;
				case '2':
					$scope.merchantInfo.status='冻结';
					break;

				default:$scope.merchantInfo.status='';
					break;
				}
				//商户类型:1-个人，2-个体商户，3-企业商户
				switch ($scope.merchantInfo.merchantType) {
				case '1':
					$scope.merchantInfo.merchantType='个人';
					break;
				case '2':
					$scope.merchantInfo.merchantType='个体商户';
					break;
				case '3':
					$scope.merchantInfo.merchantType='企业商户';
					break;

				default:$scope.merchantInfo.merchantType='';
					break;
				}
				$scope.superPush = data.superPush;
				$scope.terminalInfoList = data.terminalInfo;
				$scope.merchantCardInfo=data.merchantCardInfo;
				//账户类型:1-对公,2-对私
				switch ($scope.merchantCardInfo.accountType) {
				case '1':
					$scope.merchantCardInfo.accountType='对公';
					break;
				case '2':
					$scope.merchantCardInfo.accountType='对私';
					break;

				default:$scope.merchantCardInfo.accountType='';
					break;
				}
//			}
//		}else{
//			$scope.notice(data.msg);
//		}
	});
});


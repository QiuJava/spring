/**
 * 超级银行家贷款订单详情
 */
angular.module('inspinia').controller('agentOrderDetailCtrl',function($scope, $http, $state,$stateParams){
    $http({
        url:"superBank/orderDetail?orderNo=" + $stateParams.orderNo,
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
            if (result.data.orgBonusConf != null) {
            	$scope.baseInfo.orgBonusConf = result.data.orgBonusConf + "%";
			}
            switch (result.data.loanType) {
			case "1":
				 $scope.baseInfo.loanType = "有效注册";
				break;
			case "2":
				$scope.baseInfo.loanType = "有效借款";
				break;
			case "3":
				$scope.baseInfo.loanType = "授信成功";
				break;
			default:
				break;
			}
            switch (result.data.profitType) {
            case "1":
            	$scope.baseInfo.profitType = "固定奖金";
            	break;
            case "2":
            	$scope.baseInfo.profitType = "按比例发放";
            	break;
            default:
            	break;
            }
        } else {
           $scope.notice(result.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    })


});
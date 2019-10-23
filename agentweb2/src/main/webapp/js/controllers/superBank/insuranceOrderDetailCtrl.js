/**
 * 超级银行家保险订单详情
 */
angular.module('inspinia').controller('insuranceOrderDetailCtrl',function($scope, $http, $state,$stateParams){
    $http({
        url:"superBank/insuranceOrderDetail?orderNo=" + $stateParams.orderNo,
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
            if (result.data.orgBonusConf != null) {
            	$scope.baseInfo.orgBonusConf = result.data.orgBonusConf + "%";
			}
            switch (result.data.productType) {
			case "1":
				 $scope.baseInfo.productType = "健康险";
				break;
			case "2":
				$scope.baseInfo.productType = "人寿险";
				break;
			case "3":
				$scope.baseInfo.productType = "车险";
				break;
			case "4":
				$scope.baseInfo.productType = "财产险";
				break;
			default:
				break;
			}
            switch (result.data.status) {
                case "1":
                    $scope.baseInfo.status = "已创建";
                    break;
                case "2":
                    $scope.baseInfo.status = "待支付";
                    break;
                case "3":
                    $scope.baseInfo.status = "待审核";
                    break;
                case "4":
                    $scope.baseInfo.status = "已授权";
                    break;
                case "5":
                    $scope.baseInfo.status = "订单成功";
                    break;
                case "6":
                    $scope.baseInfo.status = "订单失败";
                    break;
                case "7":
                    $scope.baseInfo.status = "已办理过";
                    break;
                case "9":
                    $scope.baseInfo.status = "已关闭";
                    break;
                default:
                    break;
            }
            switch (result.data.productType) {
                case "1":
                    $scope.baseInfo.productType = "健康险";
                    break;
                case "2":
                    $scope.baseInfo.productType = "人寿险";
                    break;
                case "3":
                    $scope.baseInfo.productType = "车险";
                    break;
                case "4":
                    $scope.baseInfo.productType = "财产险";
                    break;
                default:
                    break;
            }
            switch (result.data.bonusSettleTime) {
                case "1":
                    $scope.baseInfo.bonusSettleTime = "实时";
                    break;
                case "2":
                    $scope.baseInfo.bonusSettleTime = "周结";
                    break;
                case "3":
                    $scope.baseInfo.bonusSettleTime = "月结";
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
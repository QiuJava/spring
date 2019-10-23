/**
 * 银行家开发配置
 */
angular.module('inspinia').controller('developmentConfigurationCtrl',function($scope, $http, $state,$stateParams){
	$http({
        url:"superBank/selectDevelopmentConfiguration",
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
            $scope.firstPage = $scope.baseInfo.firstPage;
            $scope.creditCard = $scope.baseInfo.creditCard;
            $scope.queryProgress = $scope.baseInfo.queryProgress;
            $scope.handleLoan = $scope.baseInfo.handleLoan;
            $scope.queryOrder = $scope.baseInfo.queryOrder;
            $scope.peccancy = $scope.baseInfo.peccancy;
            $scope.repayCard = $scope.baseInfo.repayCard;
            $scope.merchantReceivables = $scope.baseInfo.merchantReceivables;
            
            $scope.notifyUrl = $scope.baseInfo.notifyUrl;
            $scope.batchOrderNotifyUrl = $scope.baseInfo.batchOrderNotifyUrl;
            $scope.getUserInfoUrl = $scope.baseInfo.getUserInfoUrl;
            $scope.openAppUrl = $scope.baseInfo.openAppUrl;
            $scope.openMerchantKey = $scope.baseInfo.openMerchantKey;
            
            $scope.isEnable4 = $scope.baseInfo.isEnable4;
            $scope.isEnable6 = $scope.baseInfo.isEnable6;
            $scope.isEnable7 = $scope.baseInfo.isEnable7;
            $scope.isEnable8 = $scope.baseInfo.isEnable8;
            $scope.isEnableFirstPage = $scope.baseInfo.isEnableFirstPage;
            $scope.isEnableCreditCard = $scope.baseInfo.isEnableCreditCard;
            $scope.isEnableQueryOrder = $scope.baseInfo.isEnableQueryOrder;
            $scope.isEnablePersonalInfomation = $scope.baseInfo.isEnablePersonalInfomation;
            $scope.isEnableQueryProgress = $scope.baseInfo.isEnableQueryProgress;
        } else {
           $scope.notice(result.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    })

	$scope.testMobilePage = 'http://tyts.ygs001.com/superbank-mobile';
	$scope.testOpenPage = 'http://qbh5.ygs001.com/superbank-open';

	$scope.productionMobilePage = 'http://dmb.olvip.vip/superbank-mobile';
	$scope.productionOpenPage = 'http://open.olvip.vip/superbank-open';

    //复制链接
    $scope.copyFunction = function(value){
    	var Url2 = "";
    	switch (value) {
		case 1:
			Url2 = document.getElementById("superBankId").innerText;
			break;
		case 2:
			Url2 = document.getElementById("creditCardId").innerText;
			break;
		case 3:
			Url2 = document.getElementById("queryProgressId").innerText;
			break;
		case 4:
			Url2 = document.getElementById("handleLoanId").innerText;
			break;
		case 5:
			Url2 = document.getElementById("queryOrderId").innerText;
			break;
		case 6:
			Url2 = document.getElementById("peccancyId").innerText;
			break;
		case 7:
			Url2 = document.getElementById("repayCardId").innerText;
			break;
		case 8:
			Url2 = document.getElementById("merchantReceivablesId").innerText;
			break;
		case 9:
			Url2 = document.getElementById("callBackId").value;
			break;
		case 10:
			Url2 = document.getElementById("orderResultId").value;
			break;
		case 11:
			Url2 = document.getElementById("appId").value;
			break;
		case 12:
			Url2 = document.getElementById("merchantKeyId").value;
			break;
		case 13:
			Url2 = document.getElementById("userInfoId").value;
			break;
		case 14:
			Url2 = document.getElementById("testEnvId").innerText;
			break;
		case 15:
			Url2 = document.getElementById("testEnvId2").innerText;
			break;
		case 16:
			Url2 = document.getElementById("productionEnvId").innerText;
			break;
		case 17:
			Url2 = document.getElementById("productionEnvId2").innerText;
			break;
		default:
			break;
		}
        var oInput = document.createElement('input');
        oInput.value = Url2;
        document.body.appendChild(oInput);
        oInput.select(); // 选择对象
        document.execCommand("Copy"); // 执行浏览器复制命令
        oInput.className = 'oInput';
        oInput.style.display='none';
//        alert('复制成功');
    }
});
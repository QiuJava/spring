/**
 * 首页详情
 */
angular.module('inspinia',[]).controller("indexCtrl", function($scope, $http, $location, $stateParams,$window) {

	$http.get('getNewNotice.do').success(function(msg){
	    if(msg.data && msg.data.length > 0){
            $scope.baseInfo = msg.data[0];
            $scope.listNotice = msg.data.slice(1,5);
        }
	})
    $scope.num = 0
    $scope.getUnDealOrder = function () {
        $http.get('surveyOrder/getUnDealOrder').success(function (data) {
            var urges = data.surveyUrgeRecords;
            $scope.overdueOrders = data.overdueOrders;
            if(data.num != null && data.num != ""){
                $scope.num = data.num;
            }
            if(urges!=null&&urges!=""){
                for(var i=0;i<urges.length;i++){
                    $scope.orderNotice(urges[i]);
                }
            }
        });
    }


}).filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});
/**
 * 登录 控制器
 * @author xyf1
 */

angular.module('inspinia').controller('loginCtrl',function($scope,$http,$state,$stateParams,$compile,$filter){
	$scope.account = {userName: 'username', 'password': 'password'};
	$scope.login = function(){
		$http({  
		   method:'post',  
		   url:'logout.do',  
		   data:$scope.account,  
		   headers:{'Content-Type': 'application/x-www-form-urlencoded'},  
		   transformRequest: function(obj) {  
		     var str = [];  
		     for(var p in obj){  
		       str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));  
		     }  
		     return str.join("&");  
		   }  
		}).success(function(req){  
		});
	};
//	$scope.login = function(){
//		$http.post("perform_login.do", $scope.account).success(function(req){
//		});
//		return false;
//	};
});
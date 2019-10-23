/**
 * 安全设置
 */
angular.module('inspinia').controller('safeSetCtrl',function($rootScope,$scope,$http,$state,$stateParams,$compile,$filter,i18nService,$interval){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.info = {};
    $scope.submitting = false;
	$scope.paracont = "立即获取";
	$scope.paraevent = true;
	$scope.form1 = true;
	$scope.form2 = false;
	$scope.form3 = false;
	$scope.hasSafePhone = $rootScope.hasSafePhone;

	//设置安全手机页面
	$scope.showSafePhone = function () {
		if ($scope.hasSafePhone) {
			$scope.form1 = false;
			$scope.form2 = false;
			$scope.form3 = true;
			return;
		}
		$scope.form1 = true;
		$scope.form2 = false;
		$scope.form3 = false;
		return;
	}
	//设置安全密码页面
	$scope.showSafePassword = function () {
		if (!$scope.hasSafePhone) {
			$scope.notice("请先设置安全手机号码");
			return;
		}
		//跳转页面
		$scope.form1 = false;
		$scope.form2 = true;
		$scope.form3 = false;
		$scope.getInputNames();
		return;
	}
	$scope.getInputNames = function(){
		$("input[name^='name']").each(function(){
			$(this).keyup(function(){
				if($(this).val().length < 1){
					$(this).prev().focus();
				}else{
					if($(this).val().length >= 1){
						$(this).next().focus();
					}
				}
			});
		});
	}

	//设置安全密码
	$scope.params = {};
	$scope.updateSafePassword = function () {
		$scope.password = $scope.name1+$scope.name2+$scope.name3+$scope.name4+$scope.name5+$scope.name6;
		var encrypt = new JSEncrypt();
		encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ9s1qlOyv9qpuaTqauW6fUftzE50rVk3yVPZwv1aO1Ch/XSEz76xCwkyvqpaqceRXrPpdBmO5+ruJ+I8osOHo7L5GWEOcMOO+8izp9hXKBBrmRMD4Egpn00k9DhVIEKp/vyddZPS/doxB8onhN6poTJDLdFLFVEicMf52caN9GQIDAQAB");
		$scope.newPasword =  encrypt.encrypt($scope.password);
		$scope.params = {"password":$scope.newPasword};
		$http.post('myInfo/updateSafePassword',"info="+angular.toJson($scope.params)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if (result != null && result.status){
				$scope.notice(result.msg);
				$state.transitionTo('myInfo.account',null,{reload:false});
				return;
			} else {
				$scope.notice(result.msg);
				return;
			}
		});
	}
	if ($scope.hasSafePhone) {
		$scope.form1 = false;
		$scope.form2 = false;
		$scope.form3 = true;
	}

	$scope.cancle = function () {
		$state.transitionTo('myInfo.account',null,{reload:false});
		return;
	}

	var second = 59;
	var timePromise;    
		function interval(){    
	      if(second<=0){    
	        $interval.cancel(timePromise);    
	        timePromise = undefined; 
	        $scope.paracont = "立即获取";
	        second= 59;	
	      }else{    
	        $scope.paracont = second + "秒后可重发";    
	        second--;  
	      }    
	    }  
	    $scope.sendphonecode = function() { 
	    	if(timePromise){
	    		return ;
	    	}	
	    	if(!validateTel($scope.info.phone) ){
	    		 $scope.notice("请输入正确的手机号");
	    		 return ;
	    	}
	    	
	    	if($scope.info.imgNum==undefined || $scope.info.imgNum==null || $scope.info.imgNum.length!=4){
	    		$scope.notice("请输入正确的图形验证码");
	    		return ;
	    	}
            timePromise = $interval(interval, 1000, 100); //表示每一秒执行一次，执行100次  
            
            //发送验证码
    		$http.post('mycode/sendMsg',"checkNum="+angular.toJson($scope.info)
    				,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
    			).success(function(result){
    				if(result.status){
    	                //console.log(result);
    				}else{
    					$interval.cancel(timePromise);    
    				    timePromise = undefined; 
    				    $scope.paracont = "立即获取";
    				    second= 60;	
    					$scope.notice(result.msg);
    					$scope.changeImg();
    				}
    			});
            
	    };  
	    $scope.changeImg =function () {   
	        var imgSrc = $("#imgObj");    
	        var src = imgSrc.attr("src");        
	        imgSrc.attr("src", chgUrl(src));
	    }
	    function chgUrl(url) {
	        var timestamp = (new Date()).valueOf();
	        url = url.substring(0, 20);
	        if ((url.indexOf("&") >= 0)) {
	            url = url + "×tamp=" + timestamp;
	        } else {
	            url = url + "?timestamp=" + timestamp;
	        }
	        return url;
	    }
	    var TEL_REGEXP =/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
	    function validateTel (tel){
	          if(TEL_REGEXP.test(tel)){
	            return true;
	          }
	          return false;
	    }
	    
	    $scope.reset = function(){
			$scope.info = {};
		}
	    
	    $scope.changeImg();   
	 $scope.submitting = false;
	 $scope.submit=function(){
		 	if($scope.info.phoneNum==undefined || $scope.info.phoneNum==null || $scope.info.phoneNum.length!=4){
		 		$scope.notice("请输入正确的短信验证码");
	    		return ;
		 	}
		 	
	        $scope.submitting = true;
	    	$http.post('mycode/savePhone',"info="+angular.toJson($scope.info)
    				,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
    			).success(function(result){
    				if(result.status){
    					$scope.notice(result.msg);
    				}else{
    					$scope.notice(result.msg);
    					$scope.changeImg();
    					$scope.submitting = false;
    				}
    			});
	    	$scope.submitting = false;
	    	$scope.form1 = false;
	    	$scope.form2 = true;
	    	$scope.form3 = false;
		 	$scope.getInputNames();
	    };
});
/**
 * 修改安全密码
 */
angular.module('inspinia').controller('safePasswordCtrl',function($rootScope,$scope,$http,$state,$stateParams,$compile,$filter,i18nService,$interval){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.info = {};
    $scope.submitting = false;
	$scope.paracont = "立即获取";
	$scope.paraevent = true;
	$scope.hasSafePhone = false;//是否设置安全手机
	$scope.hasSafePassword = false;//是否设置安全密码
	$scope.flag = false;

	$http.get('myInfo/getPhone.do').success(function(msg){
		if(msg.safephone!=null && msg.safephone.length==11){
			$scope.info.phone = msg.safephone;
			$scope.hasSafePhone = msg.safephone;
			$scope.hasSafePassword = msg.hasSafePassword;
			//如果手机没设置,跳转到立即设置页面
			if (!$scope.hasSafePhone){
				$("#safePasswordModel").modal("show");
				return;
			}else if ($scope.hasSafePhone && !$scope.hasSafePassword) {
				$scope.flag = true;
				return;
			}
		}else {
			$("#safePasswordModel").modal("show");
			return;
		}
		;
	});

	//修改安全密码
	$scope.updateParams = {};
	$scope.updateSafePassword = function () {
		if (!$scope.oldName1 || !$scope.oldName2 || !$scope.oldName3 || !$scope.oldName4 || !$scope.oldName5 || !$scope.oldName6){
			$scope.notice("请正确输入原密码");
			return;
		}
		if (!$scope.newName1 || !$scope.newName2 || !$scope.newName3 || !$scope.newName4 || !$scope.newName5 || !$scope.newName6){
			$scope.notice("请输入6位数字密码");
			return;
		}
		$scope.oldPassword = $scope.oldName1+$scope.oldName2+$scope.oldName3+$scope.oldName4+$scope.oldName5+$scope.oldName6;
		$scope.newPassword = $scope.newName1+$scope.newName2+$scope.newName3+$scope.newName4+$scope.newName5+$scope.newName6;
		var encrypt = new JSEncrypt();
		encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ9s1qlOyv9qpuaTqauW6fUftzE50rVk3yVPZwv1aO1Ch/XSEz76xCwkyvqpaqceRXrPpdBmO5+ruJ+I8osOHo7L5GWEOcMOO+8izp9hXKBBrmRMD4Egpn00k9DhVIEKp/vyddZPS/doxB8onhN6poTJDLdFLFVEicMf52caN9GQIDAQAB");
		$scope.oldPasswordParams =  encrypt.encrypt($scope.oldPassword);
		$scope.newPasswordParams =  encrypt.encrypt($scope.newPassword);
		$scope.updateParams = {"oldPassword":$scope.oldPasswordParams,"password":$scope.newPasswordParams};
		$http.post('myInfo/updateSafePassword',"info="+angular.toJson($scope.updateParams)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if (result != null && result.status){
				$scope.notice(result.msg);
				$state.reload();
				return;
			} else {
				$scope.notice(result.msg);
				return;
			}
		});
	}

	//设置安全密码
	$scope.params = {};
	$scope.saveSafePassword = function () {
		$scope.info.password = $scope.password1 + $scope.password2 + $scope.password3 + $scope.password4 + $scope.password5 + $scope.password6;
		if($scope.info.phoneNum==undefined || $scope.info.phoneNum==null || $scope.info.phoneNum.length!=4){
			$scope.notice("请输入正确的短信验证码");
			return ;
		}
		$scope.password = $scope.info.password
		var encrypt = new JSEncrypt();
		encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ9s1qlOyv9qpuaTqauW6fUftzE50rVk3yVPZwv1aO1Ch/XSEz76xCwkyvqpaqceRXrPpdBmO5+ruJ+I8osOHo7L5GWEOcMOO+8izp9hXKBBrmRMD4Egpn00k9DhVIEKp/vyddZPS/doxB8onhN6poTJDLdFLFVEicMf52caN9GQIDAQAB");
		$scope.newPasword =  encrypt.encrypt($scope.password);
		$scope.params = {"password":$scope.newPasword};
		$http.post('myInfo/updateSafePassword',"info="+angular.toJson($scope.params)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if (result != null){
				$scope.notice(result.msg);
				$state.reload();
				return;
			} else {
				$scope.notice("系统异常");
				return;
			}
		});
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

		if($scope.info.imgNum==undefined || $scope.info.imgNum==null || $scope.info.imgNum.length!=4){
			$scope.notice("请输入正确的图形验证码");
			return ;
		}
		timePromise = $interval(interval, 1000, 100); //表示每一秒执行一次，执行100次

		//发送验证码
		$http.post('mycode/sendOldMsg',"checkNum="+angular.toJson($scope.info)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(!result.status){
				$interval.cancel(timePromise);
				timePromise = undefined;
				$scope.paracont = "立即获取";
				second= 59;
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

	$scope.reset = function(){
		$scope.info = {};
	}

	$scope.changeImg();
	$scope.goSetSafePhone = function(){
		$("#safePasswordModel").modal("hide");
		setTimeout(function () {
			$state.transitionTo('myInfo.safeSet',{type:1},{reload:false});
		}, 1000);
	};
	$scope.showForm = true;
	$scope.goSetSafePhoneCance = function(){
		$scope.showForm = false;
		$("#safePasswordModel").modal("hide");
		setTimeout(function () {
			$state.go('service.addService',{type:1},{reload:false});
		},1000);
	};

	$("input[name^='oldName']").each(function(){
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
	$("input[name^='newName']").each(function(){
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
	$("input[name^='password']").each(function(){
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
    $("input[name^='forgotNewName']").each(function(){
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
	// ============== start 开始找回忘记的资金密码 =================
    $scope.forgotSafe = {
    	imageCodeType : "forgotSafePassword",
		imageId : $scope.info.phone + (+new Date()),
		imageCode: "",
		smsCode: "",
        password1: "",
        password2: "",
        password3: "",
        password4: "",
        password5: "",
        password6: "",
        timePromise: undefined,
		smsCodeCountDown: 59
    };
    $scope.forgotSafePassword = function () {
        $scope.changeImgCode();
        $scope.forgotSafe.smsCode = "";
        $scope.forgotSafe.password1 = "";
        $scope.forgotSafe.password2 = "";
        $scope.forgotSafe.password3 = "";
        $scope.forgotSafe.password4 = "";
        $scope.forgotSafe.password5 = "";
        $scope.forgotSafe.password6 = "";
        $("#forgotSafePasswordModel").modal("show");
    };
    $scope.changeImgCode = function () {
        $scope.forgotSafe.imageCode = "";
        $scope.forgotSafe.imageId = Math.round(Math.random() * 1000000) + "" + (+new Date());
    };
    $scope.sendForgotSafePasswordSmsCode = function () {
        if ($scope.forgotSafe.timePromise) {
            return;
        }
        if (!$scope.forgotSafe.imageCode) {
            $scope.notice("请先输入图形验证码");
            return;
        }
        $scope.forgotSafe.smsCodeCountDown = 59;
        $scope.forgotSafe.timePromise = $interval(function () {
            debugger;
            if (!$scope.forgotSafe.timePromise || $scope.forgotSafe.smsCodeCountDown <= 0) {
                $interval.cancel($scope.forgotSafe.timePromise);
                $scope.forgotSafe.smsCodeCountDown = 59;
                $scope.forgotSafe.timePromise = undefined;
                return;
            }
            $scope.forgotSafe.smsCodeCountDown--;
        }, 1000, 100);
        $http.get('myInfo/sendForgotSafePasswordSmsCode/'+ $scope.forgotSafe.imageId + "/" + $scope.forgotSafe.imageCode)
			.success(function(data){
                $scope.notice(data.message);
                if (!data.success) {
                    $scope.changeImgCode();
                }
        });
    };
    $scope.hideForgotSafePassword = function () {
        $("#forgotSafePasswordModel").modal("hide");
    };
    $scope.findBackForgotSafePassword = function () {

        let password = [$scope.forgotSafe.password1,
			$scope.forgotSafe.password2,
			$scope.forgotSafe.password3,
			$scope.forgotSafe.password4,
			$scope.forgotSafe.password5,
			$scope.forgotSafe.password6
		].join("");
        if (!$scope.forgotSafe.smsCode) {
            $scope.notice("请先输入短信验证码");
            return;
        }
        if (password === '' || !/\d{6}/.test(password)) {
            $scope.notice("请输入6位数字密码");
            return;
        }

        var encrypt = new JSEncrypt();
        encrypt.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ9s1qlOyv9qpuaTqauW6fUftzE50rVk3yVPZwv1aO1Ch/XSEz76xCwkyvqpaqceRXrPpdBmO5+ruJ+I8osOHo7L5GWEOcMOO+8izp9hXKBBrmRMD4Egpn00k9DhVIEKp/vyddZPS/doxB8onhN6poTJDLdFLFVEicMf52caN9GQIDAQAB");
        password =  encrypt.encrypt(password);
        $http.get('myInfo/findBackForgotSafePassword/'+ $scope.forgotSafe.smsCode  + "?password=" + password)
            .success(function(data){
                $scope.notice(data.message);
                if (data.success) {
                    $scope.hideForgotSafePassword();
                }
            });
    }
    // ============== end 开始找回忘记的资金密码 =================
});
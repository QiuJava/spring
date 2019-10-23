/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('userUpdateAgentCtrl', function ($scope, $rootScope, $http, $state, $stateParams, $compile, $uibModal, $log, i18nService, SweetAlert) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    var merchantNo = $state.params.merchantNo;

    $scope.agent = {
        userName: "",
        email: "",
        mobileUsername: "",
        phone: "",
        linkName: "",
        saleName: "",
        agentArea: "",
        address: "",
        accountName: "",
        accountType: 2,
        bankName: "",
        accountNo: "",
        cnapsNo: "",
        ordparProShare: "20",
        goldparProShare: "30",
        diamparProShare: "40",
        agentFee:"",
        oemFee:""
    };
    $scope.address = {
        province: [],
        city: [],
        area: []
    };
    $scope.account = {
        province: [],
        city: [],
        bankNames: [],
        accountList: []
    };
    $scope.submiting = false;
    $http({
        url: 'redemMerchant/queryUpdateMerchantInfo?merchantNo=' + $state.params.merchantNo,
        method: 'POST'
    }).success(function (data) {
        if (data.success){
            $scope.agent = data.data;
            $scope.getCities();
            $scope.getAreas();
            $scope.getCities(true);
            $scope.getCities(false);
            $scope.getHangName();
            $scope.getPosCnaps();
        }else{
            $scope.notice(data.message);
        }
    })

    $scope.getAreaList = function (name, type, callback) {
        if (name == null || name == 'undefined') {
            return;
        }
        $http.post('areaInfo/getAreaByName.do', 'name=' + name + '&&type=' + type,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (data) {
            callback(data);
        });
    };

    //省
    $scope.getAreaList(0, "p", function (data) {
        $scope.address.province = data;
        $scope.account.province = data;
    });

    //市
    $scope.getCities = function (isAccountInfo) {
        $scope.getAreaList(isAccountInfo ? $scope.agent.accountProvince : $scope.agent.province, "", function (data) {
            if (isAccountInfo){
                $scope.account.city = data;
                $scope.account.bankNames = [];
            }else {
                $scope.address.city = data;
            }
        });
    };
    //县
    $scope.getAreas = function () {
        $scope.getAreaList($scope.agent.city, "", function (data) {
            $scope.address.area = data;
        });
    };

    $scope.accountNoFlag = false;
    $scope.getPosCnaps = function () {
        if ($scope.agent.accountType == 1) {	// 如果是对公帐号,则不校验
            $scope.accountNoFlag = false;
            return;
        }
        if ($scope.agent.accountNo == null || $scope.agent.accountNo == ""
            || $scope.agent.bankName == null || $scope.agent.bankName == "") {
            $scope.accountNoFlag = true;
            return;
        }
        if ($scope.agent.accountCity == null || $scope.agent.accountCity == "") {
            return;
        }
        $scope.accountNoFlag = false;
        var data = {
            "pris": $scope.agent.accountProvince,
            "cityName": $scope.agent.accountCity,
            "backName": $scope.agent.bankName
        };
        $http.post("merchantInfo/selectCnaps", angular.toJson(data))
            .success(function (data) {
                if (!data.bols) {
                    $scope.notice(data.msg);
                } else {
                    $scope.account.bankNames = data.list;
                }
            });
    };

    $scope.getHangName = function () {
        $scope.agent.bankName = null;
        if ($scope.agent.accountType == 1) {	// 如果是对公帐号,则不校验
            $scope.accountNoFlag = false;
            return;
        }
        if ($scope.agent.accountNo == null || $scope.agent.accountNo == "") {
            $scope.accountNoFlag = true;
            return;
        }
        $scope.accountNoFlag = false;
        var data = {"accountNo": $scope.agent.accountNo}
        $http.post("merchantInfo/getBackName", angular.toJson(data))
            .success(function (msg) {
                if (msg.bols) {
                    $scope.account.accountList = msg.lists;
                    $scope.accountNoFlag = false;
                    $scope.agent.bankName = msg.lists[0].bankName;
                    if ($scope.agent.accountProvince != null && $scope.agent.accountProvince != "" && $scope.agent.accountCity != null && $scope.agent.accountCity != "") {
                        $scope.getPosCnaps();
                    }
                } else {
                    $scope.accountNoFlag = true;
                }
            });
    };
    
    $scope.submit = function () {
        $scope.submiting = true;
        $http({
            url: 'redemMerchant/userUpdateAgent',
            data: $scope.agent,
            method: 'POST'
        }).success(function (data) {
            $scope.submiting = false;
            if (data.success){
                $scope.notice("修改成功");
                $state.transitionTo('redemption.user',null,{reload:true});
            }else{
                $scope.notice(data.message);
            }
        })
    }
});


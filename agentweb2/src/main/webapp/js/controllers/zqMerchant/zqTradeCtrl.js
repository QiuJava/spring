/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('zqTradeCtrl',function($scope,$filter,$q,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.queryZQTransOrdering = false;
    $scope.exportZQTransOrdering = false;

    $scope.info = {
        startTransTime : $filter('date')(new Date(),'yyyy-MM-dd') + " 00:00:00",
        endTransTime : $filter('date')(new Date(),'yyyy-MM-dd') + " 23:59:59"
    };

    $scope.select = {
        transType: [],
        transStatus: [],
        payMethod: []
    };
    $scope.pageNo = 1;
    $scope.pageSize = 10;
    // ============= init ===================

    initGridOption();
    // 交易状态,交易类型加载完成之后,在配置查询结果表格
    initSelectOption().then(function () {
        initGridOptionColumnDefs();
    });

    // ============= function ===============
    /**
     * 清空按钮
     */
    $scope.clear = function () {
        $scope.info = {
            startTransTime : $filter('date')(new Date(),'yyyy-MM-dd') + " 00:00:00",
            endTransTime : $filter('date')(new Date(),'yyyy-MM-dd') + " 23:59:59"
        };
    };

    /**
     * 导出操作
     */
    $scope.exportZQTransOrder = function () {
        if($scope.info.startTransTime && $scope.info.startTransTime.format){
            $scope.info.startTransTime = $scope.info.startTransTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.endTransTime && $scope.info.endTransTime.format){
            $scope.info.endTransTime = $scope.info.endTransTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if(!($scope.info.startTransTime && $scope.info.endTransTime)){
            $scope.notice("查询条件交易时间不能为空，请选择要查询的交易时间！");
            return;
        }
        if($scope.info.startTransTime && $scope.info.endTransTime &&
            $scope.info.startTransTime > $scope.info.endTransTime){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        if($scope.exportZQTransOrdering){
            $scope.notice("正在导出数据,请不要频繁点击导出按钮.");
            return;
        }
        SweetAlert.swal({
            title: "确认导出？",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true
        },
        function (isConfirm) {
            if (isConfirm) {
                $scope.exportZQTransOrdering = true;
                location.href="zqMerchantAction/exportZQTransOrder?"+$.param($scope.info);
                setTimeout(function () {
                    $scope.exportZQTransOrdering = false;
                }, 10000)
            }
        });
    };

    /**
     * 查询操作
     * @param orderNo
     */
    $scope.queryZQTransOrder = function (orderNo) {
        if($scope.info.startTransTime && $scope.info.startTransTime.format){
            $scope.info.startTransTime = $scope.info.startTransTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.endTransTime && $scope.info.endTransTime.format){
            $scope.info.endTransTime = $scope.info.endTransTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if(!($scope.info.startTransTime && $scope.info.endTransTime)){
            $scope.notice("查询条件交易时间不能为空，请选择要查询的交易时间！");
            return;
        }
        if($scope.info.startTransTime > $scope.info.endTransTime){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        if($scope.queryZQTransOrdering){
            $scope.notice("数据查询中,请不要频繁点击查询按钮.");
            return;
        }
        $scope.queryZQTransOrdering = true;
        $http({
            url: 'zqMerchantAction/queryZQTransOrder?pageNo=' + $scope.pageNo + "&pageSize=" + $scope.pageSize,
            method: 'POST',
            data: $scope.info
        }).success(function (data) {
            $scope.gridOptions.data = data.result;
            $scope.gridOptions.totalItems = data.totalCount;
            $scope.queryZQTransOrdering = false;
        }).error(function (data) {
            $scope.notice("服务器异常.");
            $scope.queryZQTransOrdering = false;
        });
    };

    /**
     * 小票信息
     */
    $scope.tradeDetailInfo = function (row) {
        var modalScope = $scope.$new(); 	//相当于$scope
        modalScope.row = row;
        var modalInstance = $uibModal.open({
            templateUrl : 'views/zqMerchant/zqTradeDetailInfo.html?v='+new Date().getTime(),  //指向上面创建的视图
            controller : 'zqTradeDetailInfoCtrl',// 初始化模态范围
            scope:modalScope,
            size:'lg'
        });
        modalScope.modalInstance = modalInstance;
        // modalInstance.result.then(function(selectedItem){
        //     //确认操作\
        //     //$state.go('trade');
        // },function(){
        //     $log.info('取消: ' + new Date())
        // })
    };

    /**
     * 初始化下拉框选项
     */
    function initSelectOption() {
        var deferred = $q.defer();
        $http.post("transInfoAction/getBoxInfo").success(function (msg) {
            if (!msg.bols) {
                $scope.notice("查询出错");
            } else {
                //交易状态
                for (var i = 0; i < msg.tranStatus.length; i++) {
                    $scope.select.transStatus.push({
                        text: msg.tranStatus[i].sysName,
                        value: msg.tranStatus[i].sysValue
                    });
                }
                //交易类型
                for (var i = 0; i < msg.transType.length; i++) {
                    $scope.select.transType.push({
                        value: msg.transType[i].sysValue,
                        text: msg.transType[i].sysName
                    });
                }
                for (var i = 0; i < msg.payMethod.length; i++) {
                    $scope.select.payMethod.push({
                        value: msg.payMethod[i].sysValue,
                        text: msg.payMethod[i].sysName
                    });
                }
            }
            deferred.resolve();
        }).error(function () {
            deferred.resolve();
        });
        return deferred.promise;
    }

    /**
     * 初始化表格配置
     */
    function initGridOption() {
        $scope.gridOptions = {                           //配置表格
            paginationPageSize: 10,                  //分页数量
            paginationPageSizes: [10, 20, 50, 100],	  //切换每页记录数
            useExternalPagination: true,
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;
                gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.pageNo = newPage;
                    $scope.pageSize = pageSize;
                    $scope.queryZQTransOrder();
                });
            }
        };
    }

    function initGridOptionColumnDefs() {
        $scope.gridOptions.columnDefs = [
            {field: 'merchantName', displayName: '商户名称', width: 160},
            {field: 'mobilephone', displayName: '手机号', width: 160},
            {field: 'unionpayMerNo', displayName: '银联报备商户编号', width: 160},
            {field: 'accountNo', displayName: '交易卡号', width: 160},
            {field: 'transAmount', displayName: '金额(元)', width: 160},
            {
                field: 'transType', displayName: '交易类型', width: 160,
                cellFilter: "formatDropping:" + angular.toJson($scope.select.transType)
            },
            {
                field: 'transStatus', displayName: '交易状态', width: 160,
                cellFilter: "formatDropping:" + angular.toJson($scope.select.transStatus)
            },
            {
                field: 'payMethod', displayName: '支付类型', width: 160,
                cellFilter: "formatDropping:" + angular.toJson($scope.select.payMethod)
            },
            {field: 'channelCode', displayName: '通道名称', width: 160},
            {field: 'transTime', displayName: '交易时间', width: 180, cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {
                field: 'orderNo', displayName: '操作', pinnedRight: true, width: 200,
                cellTemplate: '<a class="lh30" ng-show="row.entity.transStatus == \'SUCCESS\'" ng-click="grid.appScope.tradeDetailInfo(row)">小票查询</a>'
            }
        ];
    }
}).controller('zqTradeDetailInfoCtrl',function($scope,$q,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.solutionModalClose=function(){
        $scope.modalInstance.dismiss();
    };
    
    $scope.solutionModalOk = function () {
        var str =  $("#tradeInfo").html();
        var printStyle =  $("#printStyle").html();
        var oPop = window.open('','oPop');
        oPop.document.write("<style>"+printStyle+"</style>" + str);
        setTimeout(function () {
            oPop.print();
            oPop.close();
        }, 1000);
    };

    $http.get('transInfoAction/queryInfoDetail?id='+$scope.row.entity.orderNo)
        .success(function(largeLoad) {
            if(!largeLoad.bols){
                $scope.notice(largeLoad.msg);
            }else{
                $scope.infoDetail=largeLoad.tt;
                $scope.pcb=largeLoad.pcb;
                $scope.pcb1=largeLoad.pcb1;
                $scope.aa1=largeLoad.aa1;
                $scope.aa2=largeLoad.aa2;
            }
        });


});

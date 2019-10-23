/**
 * Mpos商户交易数据汇总
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('mposMerchantTradeCountCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout){
    // 数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.resetForm = function () {
        $scope.baseInfo = {v2MerchantCode:"",v2MerchantPhone:"",orgId:-1,
            /*registerDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            registerDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',*/
            snNo:"",userCode:"",userName:"",phone:""
        };
    }
    $scope.resetForm();

    $scope.baseInfo = {
        registerDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        registerDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.baseInfo.registerDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.baseInfo.registerDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }
    $scope.columnDefs = [
        {field: 'v2MerchantCode',displayName: 'V2商户编号',width: 200,pinnable: false,sortable: false},
        {field: 'v2MerchantName',displayName: 'V2商户名称',width: 150,pinnable: false,sortable: false},
        {field: 'v2MerchantPhone',displayName: '商户手机号',width: 150,pinnable: false,sortable: false},
        {field: 'snNo',displayName: '进件机具SN',width: 150,pinnable: false,sortable: false},
        {field: 'registerDate',displayName: '进件时间',width: 150,pinnable: false,sortable: false,
            cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
        },
        {field: 'userCode',displayName: '所属采购者ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '所属采购者姓名',width: 150,pinnable: false,sortable: false},
        {field: 'phone',displayName: '采购者手机号',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'totalTradeAmount',displayName: '累计交易金额',width: 150,pinnable: false,sortable: false,
            cellFilter: 'currency:"￥ "'
        },
        {field: 'monthTradeAmount',displayName: '本月交易金额',width: 150,pinnable: false,sortable: false,
            cellFilter: 'currency:"￥ "'
        },
        {field: 'near30TradeAmount',displayName: '近30天交易金额',width: 120,pinnable: false,sortable: false,
            cellFilter: 'currency:"￥ "'
        }
    ];

    $scope.orderGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/selectMposMerchantTradeCountList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.orderGrid.data = result.data.result;
            $scope.orderGrid.totalItems = result.data.totalCount;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.query();

    //获取所有的银行家组织
    $scope.orgInfoList = [{orgId:-1,orgName:"全部"}];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:-1,orgName:"全部"});
            } else {
                $scope.notice("获取组织信息异常");
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();

    // 导出
    $scope.exportInfo = function () {
        SweetAlert.swal({
                title: "确定导出吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    location.href="superBank/exportMposMerchantTradeCount?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };


    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});
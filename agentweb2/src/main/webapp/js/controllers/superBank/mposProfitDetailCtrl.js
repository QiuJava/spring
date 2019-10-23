/**
 * mpos分润明细查询
 */
angular.module('inspinia').controller('mposProfitDetailCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //订单类型:1:代理授权；2:信用卡申请 3:收款 4:信用卡还款 5:贷款
    //订单状态:1:已创建；2:待支付；3:待审核 4:已授权 5:订单成功 6:订单失败  9:回收站
    $scope.statusList = [{text:"全部",value:""},{text:"已创建",value:"1"},{text:"待支付",value:"2"},
        {text:"待审核",value:"3"},{text:"已授权",value:"4"},{text:"已完成",value:"5"},
        {text:"订单失败",value:"6"},{text:"回收站",value:"9"}];//订单状态
    $scope.orderTypeList = [{text:"全部",value:""},{text:"购机补贴",value:"101"},{text:"Mpos激活奖励",value:"102"},{text:"Mpos激活返现",value:"103"},{text:"Mpos交易",value:"104"}];//订单类型
    $scope.userTypeList = [{text:"全部",value:""},{text:"专员",value:"20"},{text:"经理",value:"30"}
        ,{text:"银行家",value:"40"},{text:"平台",value:"60"},{text:"OEM",value:"50"}
    ];//用户身份
    $scope.accountStatusList = [{text:"全部",value:""},{text:"未记账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];//记账状态
    $scope.settleCycleList = [{text:"全部",value:""},{text:"T0",value:"T0"},{text:"T1",value:"T1"}];//结算周期

    $scope.resetForm = function () {
        $scope.baseInfo = {status:"",orderType:"",
            /*createDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
        };
        clearSubCondition();
    }
    $scope.resetForm();
    $scope.baseInfo = {
        createDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.baseInfo.createDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.baseInfo.createDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }
    $scope.condition = {conditionStatus: false, conditionMsg: '全部条件'};
    $scope.changeCondition = function(){
        $scope.condition.conditionStatus = !$scope.condition.conditionStatus;
        if($scope.condition.conditionStatus){
            //清空子菜单的条件
            clearSubCondition();
            $scope.condition.conditionMsg = '清空收起';
        } else {
            $scope.condition.conditionMsg = '全部条件';
        }
    }
    function clearSubCondition(){
        $scope.baseInfo.userType = "";
        $scope.baseInfo.accountStatus = "";
        $scope.baseInfo.orgId = -1;
        $scope.baseInfo.openProvince = "全部";
        $scope.baseInfo.openCity = "全部";
        $scope.baseInfo.openRegion = "全部";
        $scope.baseInfo.remark = null;
        $scope.baseInfo.shareUserRemark = null;
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";
    }

    //省市区
    $scope.list = LAreaDataBaidu;
    $scope.c = function () {
        $scope.selected2 = "";
        $scope.selected3 = "";
    };

    $scope.c2 = function () {
        $scope.selected3 = "";
    };


    $scope.columnDefs = [
        {field: 'id',displayName: '分润明细ID',width: 200,pinnable: false,sortable: false},
        {field: 'orgId',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '品牌商名称',width: 150,pinnable: false,sortable: false},
        {field: 'orderType',displayName: '订单类型',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '订单状态',width: 150,pinnable: false,sortable: false},
        {field: 'orderNo',displayName: '订单编号',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'shareNickName',displayName: '贡献人昵称',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserName',displayName: '贡献人名称',width: 150,pinnable: false,sortable: false},
        {field: 'shareUserPhone',displayName: '贡献人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'totalProfit',displayName: '总奖金',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '收益人姓名',width: 140,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '收益人ID',width: 140,pinnable: false,sortable: false},
        {field: 'userType',displayName: '收益人身份',width: 140,pinnable: false,sortable: false},
        {field: 'userProfit',displayName: '收益人分润',width: 140,pinnable: false,sortable: false},
        {field: 'profitLevel',displayName: '当前分润层级',width: 140,pinnable: false,sortable: false},
        {field: 'createDateStr',displayName: '创建时间',width: 150,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 120,pinnable: false,sortable: false},
        { field: 'openProvince',displayName:'省',width:150 },
        { field: 'openCity',displayName:'市',width:150 },
        { field: 'openRegion',displayName:'区',width:150 },
        { field: 'remark',displayName:'备注',width:150 },
        { field: 'shareUserRemark',displayName:'贡献人备注',width:150 },
        // {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        // '<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'superBank.repayOrderDetail\')" '
        // + 'ui-sref="superBank.repayOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
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
        if ($scope.selected != undefined && $scope.selected != null) {
            $scope.baseInfo.openProvince = $scope.selected.name;
            if ($scope.selected2 != undefined && $scope.selected2 != null) {
                $scope.baseInfo.openCity = $scope.selected2.name;
                if ($scope.selected3 != undefined && $scope.selected3 != null) {
                    $scope.baseInfo.openRegion = $scope.selected3.name;
                } else {
                    $scope.baseInfo.openRegion = "";
                }
            } else {
                $scope.baseInfo.openCity = "";
                $scope.baseInfo.openRegion = "";
            }
        } else {
            $scope.baseInfo.openProvince = "";
            $scope.baseInfo.openCity = "";
            $scope.baseInfo.openRegion = "";
        }
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/mposProfitDetail?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.orderGrid.data = result.data.page.result;
            $scope.orderGrid.totalItems = result.data.page.totalCount;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    //获取所有的银行家组织
    $scope.orgInfoList = [];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:-1,orgName:"全部"});
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
                    location.href="superBank/mposProfitDetailExprort?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
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
/**
 * 代理商授权订单查询
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('agentOrderCtrl',function($scope,$rootScope,$http,i18nService,$document,SweetAlert,$timeout){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //订单类型:1代理授权；2信用卡申请 3收款 4信用卡还款
    $scope.statusList = [{text:"全部",value:""},{text:"待支付",value:"2"},{text:"待审核",value:"3"},{text:"已授权",value:"4"},{text:"回收站",value:"9"}];//订单状态
    $scope.accountStatusList = [{text:"全部",value:""},{text:"未记账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}];//记账状态
    $scope.userTypeList = [{text:"全部",value:""},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}];//用户类型
    $scope.refundMsgList = [{text:"全部",value:""},{text:"当月办信用卡",value:"20"},{text:"当年办信用卡",value:"30"}];//退款原由
    $scope.resetForm = function () {
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";
        $scope.baseInfo = {orderType:"1",status:"",orgId:-1,oneUserCode:"",refundStatus:"",refundMsg:"",
        	accountStatus:"",oneUserType:"",twoUserType:"",thrUserType:"",fouUserType:"",openProvince:"",openCity:"",openRegion:"",remark:"",
            /*createDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
        };
    }
    $scope.resetForm();
    $scope.baseInfo = {
        createDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        payDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        payDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.baseInfo.createDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.baseInfo.payDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.baseInfo.createDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.baseInfo.payDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }
    $scope.entityAgentLevel = $rootScope.entityAgentLevel;
	$scope.isAgent = $rootScope.isAgent;
	$scope.show = 0;
	//显示隐藏所有条件
	$scope.showConditions = function(){
		if (!$scope.show) {
			$scope.show = 1;
		}else{
			$scope.show = 0;
		}
	}
	
    $scope.columnDefs = [
        {field: 'orderNo',displayName: '订单ID',width: 80,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'secondUserNode',displayName:'二级代理节点',width:120 },
        {field: 'status',displayName: '订单状态',width: 150,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"待支付",value:"待支付"},{text:"待审核",value:"待审核"},{text:"已授权",value:"已完成"},{text:"回收站",value:"回收站"}]'},
        {field: 'refundStatus',displayName: '已达标退款',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:[{text:'是',value:1},{text:'否',value:0}]"},
        {field: 'refundDate',displayName: '退款时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'refundMsg',displayName: '退款原由',width: 150,pinnable: false,sortable: false,cellaFilter:"formatDropping:[{text:'当月办信用卡',value:0},{text:'当年办信用卡',value:1}]"},
        {field: 'incomeType',displayName: '退款订单号',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '贡献人ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '贡献人名称',width: 140,pinnable: false,sortable: false},
        {field: 'sharePhone',displayName: '贡献人手机号',width: 140,pinnable: false,sortable: false},
        {field: 'price',displayName: '售价',width: 140,pinnable: false,sortable: false},
        {field: 'totalBonus',displayName: '发放总奖金',width: 140,pinnable: false,sortable: false},
        {field: 'createDate',displayName: '创建时间',width: 120,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'payDate',displayName: '支付时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'payOrderNo',displayName: '关联支付订单',width: 180,pinnable: false,sortable: false},
        {field: 'oneUserCode',displayName: '一级编号',width: 120,pinnable: false,sortable: false},
        {field: 'oneUserName',displayName: '一级名称',width: 150,pinnable: false,sortable: false},
        {field: 'oneUserType',displayName: '一级身份',width: 150,pinnable: false,sortable: false},
        {field: 'oneUserProfit',displayName: '一级分润',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserCode',displayName: '二级编号',width: 120,pinnable: false,sortable: false},
        {field: 'twoUserName',displayName: '二级名称',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserType',displayName: '二级身份',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserProfit',displayName: '二级分润',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserCode',displayName: '三级编号',width: 120,pinnable: false,sortable: false},
        {field: 'thrUserName',displayName: '三级名称',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserType',displayName: '三级身份',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserProfit',displayName: '三级分润',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserCode',displayName: '四级编号',width: 120,pinnable: false,sortable: false},
        {field: 'fouUserName',displayName: '四级名称',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserType',displayName: '四级身份',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserProfit',displayName: '四级分润',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '品牌商名称',width: 150,pinnable: false,sortable: false},
        {field: 'orgProfit',displayName: '品牌商分润',width: 150,pinnable: false,sortable: false},
        {field: 'plateProfit',displayName: '品牌代理成本',width: 150,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '记账状态',width: 150,pinnable: false,sortable: false},
        {field: 'openProvince',displayName:'省',width:150 },
        {field: 'openCity',displayName:'市',width:150 },
        {field: 'openRegion',displayName:'区',width:150 },
        {field: 'remark',displayName:'备注',width:150 },
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
        '<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.agentOrderDetail\')" '
        + 'ui-sref="superBank.agentOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
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
        $http({
            url: 'superBank/orderManager?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
            $scope.orderMainSum = result.data.orderMainSum;
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
    //异步获取用户
    $scope.userInfoList = [{userCode: "", userName: "全部"}];
    var oldValue="-1";
    var timeout="";
    function getUserInfoList(value){
        var newValue=value;
        if(newValue != oldValue) {
            if (timeout)
                $timeout.cancel(timeout);
            timeout = $timeout(function () {
                $http({
                    url: "superBank/selectUserInfoList",
                    data: "userCode=" + value,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    method: "POST"
                }).success(function (result) {
                    if (result.status) {
                        if (result.data.length == 0) {
                            $scope.userInfoList = [{userCode: "", userName: "全部"}];
                        } else {
                            $scope.userInfoList = result.data;
                            $scope.userInfoList.unshift({userCode: "", userName: "全部"});
                        }
                        oldValue = value;
                    } else {
                        $scope.notice(result.msg);
                    }
                }).error(function () {
                    $scope.notice("系统异常，请稍候再试");
                })
            }
            , 800);
        }
    }
    $scope.getUserInfoList = getUserInfoList;
    $scope.getUserInfoList("");
    //
    // 导出
    $scope.exportInfo = function () {
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
                    location.href="superBank/exportAgentOrder?baseInfo=" +angular.toJson($scope.baseInfo);
                }
            });
    };
    //省市区
    $scope.list = LAreaDataBaidu;
    $scope.c = function () {
        $scope.selected2 = "";
        $scope.selected3 = "";
    };

    $scope.c2 = function () {
        $scope.selected3 = "";
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
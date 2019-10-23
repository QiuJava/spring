/**
 * Mpos激活查询
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('mposActiveOrderCtrl',function($scope,$http,i18nService,$document,SweetAlert,$timeout){
    // 数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    // 活动状态
    $scope.statusList = [{text:"全部",value:""},
        {text:"未激活",value:"1"},
        {text:"已激活",value:"2"},
        {text:"已返补贴",value:"3"}
    ];

    // 补贴入账状态
    $scope.accountStatusList = [
        {text:"待入账",value:"0"},
        {text:"已记账",value:"1"},
        {text:"记账失败",value:"2"}
    ];

    $scope.userTypeList = [{text:"全部",value:""},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}];//用户身份

    $scope.resetForm = function () {
        $scope.baseInfo = {snNo:"",status:"",orgId:-1,v2MerchantCode:"", userCode : "",userName:"",phone:"",orderNo:"",
            /*registerDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            registerDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',*/
        };
        clearSubCondition();
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
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";

        $scope.baseInfo.oneUserCode = "";
        $scope.baseInfo.twoUserCode = "";
        $scope.baseInfo.thrUserCode = "";
        $scope.baseInfo.fouUserCode = "";
        $scope.baseInfo.oneUserName = "";
        $scope.baseInfo.twoUserName = "";
        $scope.baseInfo.thrUserName = "";
        $scope.baseInfo.fouUserName = "";
        $scope.baseInfo.oneUserType = "";
        $scope.baseInfo.twoUserType = "";
        $scope.baseInfo.thrUserType = "";
        $scope.baseInfo.fouUserType = "";
        $scope.baseInfo.provinceName = "全部";
        $scope.baseInfo.cityName = "全部";
        $scope.baseInfo.districtName = "全部";
        $scope.baseInfo.remark = "";
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
        {field: 'orderNo',displayName: '订单编号',width: 200,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '所属采购者ID',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '所属采购者姓名',width: 150,pinnable: false,sortable: false},
        {field: 'phone',displayName: '采购者手机号',width: 150,pinnable: false,sortable: false},
        {field: 'v2MerchantCode',displayName: 'V2商户编号',width: 200,pinnable: false,sortable: false},
        {field: 'v2MerchantName',displayName: 'V2商户名称',width: 150,pinnable: false,sortable: false},
        {field: 'v2MerchantPhone',displayName: '商户手机号',width: 150,pinnable: false,sortable: false},
        {field: 'snNo',displayName: '进件机具SN',width: 150,pinnable: false,sortable: false},
        {field: 'typeName',displayName: '设备类型',width: 200,pinnable: false,sortable: false},
        {field: 'createDate',displayName: '创建时间',width: 150,pinnable: false,sortable: false,
            cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
        },
        {field: 'registerDate',displayName: '进件时间',width: 150,pinnable: false,sortable: false,
            cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
        },
        {field: 'status',displayName: '活动状态',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope">{{ (grid.appScope.statusList | filter : {"value" : row.entity.status})[0].text }}</div>'
        },
        {field: 'activeDate',displayName: '激活时间',width: 150,pinnable: false,sortable: false,
            cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
        },
        {field: 'activeReturnBonus',displayName: '激活返现金额',width: 150,pinnable: false,sortable: false,
            cellFilter: 'currency:"￥ "'
        },
        {field: 'totalBonus',displayName: '激活奖金包',width: 150,pinnable: false,sortable: false,
            cellFilter: 'currency:"￥ "'
        },
        {field: 'accountStatus',displayName: '补贴入账状态',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope">{{ (grid.appScope.accountStatusList | filter : {"value" : row.entity.accountStatus})[0].text }}</div>'
        },
        {field: 'completeDate',displayName: '补贴发放时间',width: 150,pinnable: false,sortable: false,
            cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
        },
        {field: 'oneUserCode',displayName: '一级编号',width: 150,pinnable: false,sortable: false},
        {field: 'oneUserName',displayName: '一级名称',width: 150,pinnable: false,sortable: false},
        {field: 'oneUserType',displayName: '一级身份',width: 180,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope">{{ (grid.appScope.userTypeList | filter : {"value" : row.entity.oneUserType})[0].text }}</div>'
        },
        {field: 'oneUserProfit',displayName: '一级分润',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserCode',displayName: '二级编号',width: 180,pinnable: false,sortable: false},
        {field: 'twoUserName',displayName: '二级名称',width: 180,pinnable: false,sortable: false},
        {field: 'twoUserType',displayName: '二级身份',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope">{{ (grid.appScope.userTypeList | filter : {"value" : row.entity.twoUserType})[0].text }}</div>'
        },
        {field: 'twoUserProfit',displayName: '二级分润',width: 120,pinnable: false,sortable: false},
        {field: 'thrUserCode',displayName: '三级编号',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserName',displayName: '三级名称',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserType',displayName: '三级身份',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope">{{ (grid.appScope.userTypeList | filter : {"value" : row.entity.thrUserType})[0].text }}</div>'
        },
        {field: 'thrUserProfit',displayName: '三级分润',width: 120,pinnable: false,sortable: false},
        {field: 'fouUserCode',displayName: '四级编号',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserName',displayName: '四级名称',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserType',displayName: '四级身份',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope">{{ (grid.appScope.userTypeList | filter : {"value" : row.entity.fouUserType})[0].text }}</div>'
        },
        {field: 'fouUserProfit',displayName: '四级分润',width: 120,pinnable: false,sortable: false},
        {field: 'orgProfit',displayName: '组织分润',width: 150,pinnable: false,sortable: false,
            cellFilter: 'currency:"￥ "'
        },
        {field: 'provinceName',displayName: '省',width: 150,pinnable: false,sortable: false},
        {field: 'cityName',displayName: '市',width: 150,pinnable: false,sortable: false},
        {field: 'districtName',displayName: '区',width: 150,pinnable: false,sortable: false},
        {field: 'remark',displayName: '备注',width: 120,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                '<a class="lh30" target="_blank"  ng-show="grid.appScope.hasPermit(\'superBank.mposActiveOrderDetail\')" '
                + 'ui-sref="superBank.mposActiveOrderDetail({orderNo:row.entity.orderNo})">详情</a>'}
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
            $scope.baseInfo.provinceName = $scope.selected.name;
            if ($scope.selected2 != undefined && $scope.selected2 != null) {
                $scope.baseInfo.cityName = $scope.selected2.name;
                if ($scope.selected3 != undefined && $scope.selected3 != null) {
                    $scope.baseInfo.districtName = $scope.selected3.name;
                } else {
                    $scope.baseInfo.districtName = "";
                }
            } else {
                $scope.baseInfo.cityName = "";
                $scope.baseInfo.districtName = "";
            }
        } else {
            $scope.baseInfo.provinceName = "";
            $scope.baseInfo.cityName = "";
            $scope.baseInfo.districtName = "";
        }
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/selectMposActiveOrderList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
            $scope.mposActiveOrderSum = result.data.mposActiveOrderSum;
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
                    location.href="superBank/exportMposActiveOrder?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
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
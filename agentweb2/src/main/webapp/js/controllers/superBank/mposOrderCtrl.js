/**
 * Mpos订单查询
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('mposOrderCtrl',function($scope,$http,i18nService,$document,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusList = [{text:"全部",value:""},{text:"待付款",value:"1"},{text:"待发货",value:"2"},{text:"待收货",value:"3"},{text:"已收货",value:"4"},{text:"已关闭",value:"9"}];//订单状态
    $scope.shipperList = [{text:"全部",value:""},{text:"平台发货",value:"1"},{text:"组织发货",value:"2"}];//订单发货方
    $scope.shipWayList = [{text:"全部",value:""},{text:"快递配送",value:"1"},{text:"线下自提",value:"2"}];// 配送方式
    $scope.payMethodList = [{text:"全部",value:""},{text:"微信",value:"1"},{text:"支付宝",value:"2"},{text:"快捷",value:"3"},{text:"红包账户",value:"4"},{text:"分润账户",value:"5"}];// 支付方式
    $scope.payStatusList = [{text:"全部",value:""},{text:"未支付",value:"1"},{text:"已支付",value:"2"}];// 支付状态
    $scope.toOrgStatusList = [{text:"全部",value:""},{text:"未转账",value:"1"},{text:"已转账",value:"2"},{text:"转账失败",value:"3"}];// 货款转账状态
    $scope.goodStatusList = [{text:"全部",value:""},{text:"上架",value:"1"},{text:"下架",value:"2"}];// 商品分类

    $scope.checked = true;
    $scope.snNos = "";

    $scope.resetForm = function () {
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";
        $scope.baseInfo = {provinceName:"全部",cityName:"全部",districtName:"全部",status:"",orgId:-1,orderNo :"",shipper:"",goodNo:"",toOrgStatus:"",goodStatus:"",
            goodTitle:"",shipWay:"",userPhone:"", userCode:"",payMethod:"",payStatus:"",payOrderNo:"",
            /* createDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
           shipDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            shipDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            payDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            payDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            receiveDateStart:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            receiveDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
            shipDateStart:"",
            shipDateEnd:"",
            payDateStart:"",
            payDateEnd:"",
            receiveDateStart:"",
            receiveDateEnd:""
        };
    }
    $scope.resetForm();
    $scope.baseInfo = {
        createDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        createDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        shipDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        shipDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        payDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        payDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        receiveDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        receiveDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',

    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.baseInfo.createDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.baseInfo.shipDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.baseInfo.payDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.baseInfo.receiveDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.baseInfo.createDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.baseInfo.shipDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.baseInfo.payDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.baseInfo.receiveDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

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
        {field: 'orderNo',displayName: '订单编号',width: 80,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '采购者编号',width: 150,pinnable: false,sortable: false},
        {field: 'userName',displayName: '采购者名称',width: 150,pinnable: false,sortable: false},
        {field: 'goodNo',displayName: '商品编号',width: 150,pinnable: false,sortable: false},
        {field: 'imgUrl',displayName: '商品图片',width: 150,pinnable: false,sortable: false,cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.imgUrl" ng-src="{{row.entity.imgUrl}}" />'},
        {field: 'goodTitle',displayName: '商品标题',width: 150,pinnable: false,sortable: false},
        {field: 'typeName',displayName: '所在商品种类',width: 150,pinnable: false,sortable: false},
        {field: 'productType',displayName: '设备类型',width: 150,pinnable: false,sortable: false},
        {field: 'goodSinglePrice',displayName: '商品销售价',width: 150,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'buyNum',displayName: '购买数量(台)',width: 150,pinnable: false,sortable: false},
        {field: 'goodTotalPrice',displayName: '商品总价',width: 150,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'totalPrice',displayName: '订单总金额',width: 140,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'shipFee',displayName: '运费',width: 140,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'shipWay',displayName: '配送方式',width: 140,pinnable: false,sortable: false,cellFilter:'formatDropping:[{value:"1",text:"快递配送"},{value:"2",text:"线下自提"}]'},
        {field: 'needShipFee',displayName: '是否包邮',width: 150,pinnable: false,sortable: false,cellFilter:'formatDropping:[{value:"1",text:"是"},{value:"2",text:"否"}]'},
        {field: 'shipper',displayName: '订单发货方',width: 150,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"平台发货",value:"1"},{text:"组织发货",value:"2"}]'},
        {field: 'receiverName',displayName: '收货人姓名',width: 150,pinnable: false,sortable: false},
        {field: 'receiverPhone',displayName: '收货人手机号',width: 150,pinnable: false,sortable: false},
        {field: 'receiverAddr',displayName: '收货人地址',width: 120,pinnable: false,sortable: false},
        {field: 'remark',displayName: '下单备注',width: 150,pinnable: false,sortable: false},
        {field: 'createDate',displayName: '创建时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'status',displayName: '订单状态',width: 120,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"待付款",value:"1"},{text:"待发货",value:"2"},{text:"待收货",value:"3"},{text:"已收货",value:"4"},{text:"已关闭",value:"9"}]'},
        {field: 'payStatus',displayName: '支付状态',width: 140,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"未支付",value:"1"},{text:"已支付",value:"2"}]'},
        {field: 'payMethod',displayName: '支付方式',width: 150,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"微信",value:"1"},{text:"支付宝",value:"2"},{text:"快捷",value:"3"},{text:"红包账户",value:"4"},{text:"分润账户",value:"5"}]'},
        {field: 'payDate',displayName: '支付时间',width: 120,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'payChannelNo',displayName: '支付商户号',width: 150,pinnable: false,sortable: false},
        {field: 'payChannelName',displayName: '支付通道',width: 150,pinnable: false,sortable: false},
        {field: 'payOrderNo',displayName: '关联支付流水号',width: 150,pinnable: false,sortable: false},
        {field: 'receiveDate',displayName: '确认收货时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'toOrgAmount',displayName: '转组织货款金额',width: 150,pinnable: false,sortable: false,cellFilter:'currency:""'},
        {field: 'toOrgStatus',displayName: '货款转账状态',width: 150,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"未转账",value:"1"},{text:"已转账",value:"2"},{text:"转账失败",value:"3"}]'},
        {field: 'totalBonus',displayName: '采购机具总奖励包',width: 180,pinnable: false,sortable: false},
        {field: 'oneUserCode',displayName: '一级编号',width: 180,pinnable: false,sortable: false},
        {field: 'oneUserName',displayName: '一级名称',width: 150,pinnable: false,sortable: false},
        {field: 'oneUserType',displayName: '一级身份',width: 180,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"普通用户",value:"10"},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}]'},
        {field: 'oneUserProfit',displayName: '一级分润',width: 180,pinnable: false,sortable: false},
        {field: 'twoUserCode',displayName: '二级编号',width: 180,pinnable: false,sortable: false},
        {field: 'twoUserName',displayName: '二级名称',width: 150,pinnable: false,sortable: false},
        {field: 'twoUserType',displayName: '二级身份',width: 180,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"普通用户",value:"10"},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}]'},
        {field: 'twoUserProfit',displayName: '二级分润',width: 180,pinnable: false,sortable: false},
        {field: 'thrUserCode',displayName: '三级编号',width: 180,pinnable: false,sortable: false},
        {field: 'thrUserName',displayName: '三级名称',width: 150,pinnable: false,sortable: false},
        {field: 'thrUserType',displayName: '三级身份',width: 180,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"普通用户",value:"10"},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}]'},
        {field: 'thrUserProfit',displayName: '三级分润',width: 180,pinnable: false,sortable: false},
        {field: 'fouUserCode',displayName: '四级编号',width: 180,pinnable: false,sortable: false},
        {field: 'fouUserName',displayName: '四级名称',width: 150,pinnable: false,sortable: false},
        {field: 'fouUserType',displayName: '四级身份',width: 180,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"普通用户",value:"10"},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}]'},
        {field: 'fouUserProfit',displayName: '四级分润',width: 180,pinnable: false,sortable: false},
        {field: 'orgProfit',displayName: '组织分润',width: 180,pinnable: false,sortable: false},
        {field: 'accountStatus',displayName: '奖励入账状态',width: 150,pinnable: false,sortable: false,cellFilter:'formatDropping:[{text:"待入账",value:"0"},{text:"已记账",value:"1"},{text:"记账失败",value:"2"}]'},
        {field: 'completeDate',displayName: '奖励入账时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'provinceName',displayName:'采购者所属省',width:150 },
        {field: 'cityName',displayName:'采购者所属市',width:150 },
        {field: 'districtName',displayName:'采购者所属区',width:150 },
        {field: 'userRemark',displayName:'采购者备注',width:150 },
        {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.mposOrderDetail\')" ui-sref="superBank.mposOrderDetail({orderNo:row.entity.orderNo})">订单详情</a>'
                +'&nbsp;&nbsp;&nbsp;  <a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.mposOrderShip\') && (row.entity.status == \'2\') && (row.entity.shipper == 2)" ui-sref="superBank.mposOrderShip({orderNo:row.entity.orderNo})">发货</a>'
                +'&nbsp;&nbsp;&nbsp;  <a class="lh30" ng-show="grid.appScope.hasPermit(\'superBank.mposOrderConsignShip\') && (row.entity.status == \'2\') && (row.entity.shipper == 2)" ng-click="grid.appScope.consignShip(row.entity.orderNo,row.entity.status,row.entity.shipper)">委托平台发货</a>'}
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
            //行选中事件
            $scope.gridApi.selection.on.rowSelectionChanged($scope,function(row,event){
                if(row.isSelected){

                    $scope.baseInfo.pageAll=false;
                    $scope.baseInfo.countAll=false;
                }
                var selectList = $scope.gridApi.selection.getSelectedRows();
                if(selectList!=null&&selectList.length>0){
                    $scope.checked=false;
                }else{
                    $scope.checked=true;
                }
            });
            //全选事件
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
                if(row){
                    if(row[0].isSelected){
                        $scope.baseInfo.pageAll=false;
                        $scope.baseInfo.countAll=false;
                    }
                }
                var selectList = $scope.gridApi.selection.getSelectedRows();
                if(selectList!=null&&selectList.length>0){
                    $scope.checked=false;
                }else{
                    $scope.checked=true;
                }
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
            url: 'superBank/selectMposOrderList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
            $scope.mposOrderSum = result.data.mposOrderSum;
            if($scope.mposOrderSum.orderCount==null){
                $scope.mposOrderSum.orderCount=0
            }
            if($scope.mposOrderSum.orderAmountCount==null){
                $scope.mposOrderSum.orderAmountCount=0
            }
            if($scope.mposOrderSum.goodCount==null){
                $scope.mposOrderSum.goodCount=0
            }
            $scope.checked=true;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };




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
                    location.href="superBank/exportMposOrder?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };

    //委托发货
    $scope.consignShip = function(data,status,shipper){
        var selectList = $scope.gridApi.selection.getSelectedRows();

        if(data!=null){
            if(status==2 && shipper==2) {
                $scope.snNos = data;
            }
        }else {
            if (selectList != null && selectList.length > 0) {
                for (var i = 0; i < selectList.length; i++) {
                    var _status = selectList[i].status;
                    var _shipper = selectList[i].shipper;
                    var snNo = selectList[i].orderNo;
                    if (_status==2 && _shipper==2) {
                        if ($scope.snNos != "") {
                            $scope.snNos = $scope.snNos + "," + snNo;
                        } else {
                            $scope.snNos = snNo;
                        }
                    }
                }
            }
        }
        $('#consignShipModal').modal('show');
    }

    $scope.consignShipCancel = function(){
        $('#consignShipModal').modal('hide');
    }

    $scope.consignShipStatus = false;
    $scope.consignShipSubmit = function (){
        $scope.consignShipStatus = true;
        if($scope.snNos==""){
            $scope.notice('没有可委托的订单');
            $scope.consignShipStatus = false;
            return false;
        }
        $http({
            url: 'superBank/mposOrderConsignShip?snNos=' + $scope.snNos,
            method:'POST'
        }).success(function (result) {
            $scope.consignShipStatus = false;
            if (!result.status){
                $scope.notice(result.msg);
                return false;
            }else{
                $scope.notice(result.msg);
                $scope.consignShipCancel();
                $scope.query();
            }


        }).error(function () {
            $scope.consignShipStatus = false;
            $scope.notice('服务器异常,请稍后再试.');
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
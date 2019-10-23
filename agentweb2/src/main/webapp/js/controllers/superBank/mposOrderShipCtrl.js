/**
 * 超级银行家Mpos订单发货
 */
angular.module('inspinia').controller('mposOrderShipCtrl',function($scope, $http, $state,$stateParams) {


    $scope.shipExpressList = [{text: "EMS"}, {text: "申通快递"}, {text: "圆通速递"}, {text: "韵达速递"}, {text: "中通快递"}, {text: "天天快递"}, {text: "百世快递"}, {text: "宅急送"}, {text: "全峰快递"}, {text: "德邦"}, {text: "邮政包裹信件"}, {text: "中铁物流"}, {text: "AAE全球专递"}, {text: "安能物流"}, {text: "京东物流"}, {text: "全晨物流"}, {text: "万象物流"}, {text: "百世快运"}, {text: "顺丰快递"}, {text: "其他"}]

    $scope.columnDefs = [
        {field: 'snNo',displayName: 'SN号',width: 300,pinnable: false,sortable: false},
        {field: 'productTypeName',displayName: '硬件产品种类',width: 300,pinnable: false,sortable: false}
    ];
    $scope.checked = true;
    $scope.resetForm = function (){
        $scope.machines = {snStart:"",snEnd:"",productType:"",count:0,status:"2"};
    };
    $scope.resetForm();

    $scope.machinesGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : false,  		//纵向滚动条
//                 		rowHeight:35,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.queryMachines();
            });
            //行选中事件
            $scope.gridApi.selection.on.rowSelectionChanged($scope,function(row,event){
                if(row.isSelected){

                    $scope.machinesGrid.pageAll=false;
                    $scope.machinesGrid.countAll=false;
                }
                $scope.selectList = $scope.gridApi.selection.getSelectedRows();
                if($scope.selectList!=null&&$scope.selectList.length>0){
                    $scope.checked=false;
                }else{
                    $scope.checked=true;
                }
            });
            //全选事件
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
                if(row){
                    if(row[0].isSelected){
                        $scope.machinesGrid.pageAll=false;
                        $scope.machinesGrid.countAll=false;
                    }
                }
                $scope.selectList = $scope.gridApi.selection.getSelectedRows();
                if($scope.selectList!=null&&$scope.selectList.length>0&&$scope.selectList.length==$scope.baseInfo.buyNum){
                    $scope.checked=false;
                }else{
                    $scope.checked=true;
                }
            });
        }
    };


    $scope.query = function () {
        $http({
            url: "superBank/mposOrderDetail?orderNo=" + $stateParams.orderNo,
            method: "GET"
        }).success(function (result) {
            if (result.status) {
                if (result.data.status != '2') {
                    $scope.notice("订单状态不是待发货状态");
                    $state.transitionTo('superBank.mposOrder',null,{reload:true});
                    return false;
                }
                if(result.data.shipper == 1){
                    $scope.notice("该订单已委托平台发货");
                    $state.transitionTo('superBank.mposOrder',null,{reload:true});
                    return false;
                }
                if(!(result.data.shipper == 2 &&  result.data.orgId == $scope.entityId)){
                    $scope.notice("订单发货方不属于当前组织，不能发货");
                    $state.transitionTo('superBank.mposOrder',null,{reload:true});
                    return false;
                }
                $scope.baseInfo = result.data;

            } else {
                $scope.notice(result.msg);
                $state.transitionTo('superBank.mposOrder',null,{reload:true});
                return false;
            }
        }).error(function () {
            $scope.notice("服务器异常，请稍后再试");
        });
    }
    $scope.query();


    $scope.ship = function(){
        if($scope.baseInfo.shipWay == 1){
            if($scope.baseInfo.shipExpress==null || $scope.baseInfo.shipExpress==""){
                $scope.notice('请选择快递公司');
                return false;
            }
            if($scope.baseInfo.shipExpressNo==null || $scope.baseInfo.shipExpressNo==""){
                $scope.notice('请填写物流编号');
                return false;
            }
        }
        $scope.baseInfo.orderNo=$stateParams.orderNo;
        $scope.submitting = true;
        $http({
            url: 'superBank/mposOrderShip',
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.notice(result.msg);
            $state.transitionTo('superBank.mposOrder',null,{reload:true});
        }).error(function () {
            $scope.submitting = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    }

    $scope.productTypeList=[];
    $scope.getProductTypeList = function(){
        $http({
            url:"superBank/getMposProductTypeListAll",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.productTypeList = msg.data;
                $scope.productTypeList.unshift({id:'',typeName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取硬件设备类型列表异常");
        });
    };
    $scope.getProductTypeList();


    $scope.queryMachines = function () {
        $scope.queryMachinesSubmitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/selectMposMachinesList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.machines,
            method:'POST'
        }).success(function (result) {
            $scope.queryMachinesSubmitting = false;
            $scope.loadImg = false;
            $scope.checked=true;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.machinesGrid.data = result.data.page.result;
            $scope.machinesGrid.totalItems = result.data.page.totalCount;
        }).error(function () {
            $scope.queryMachinesSubmitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };


    //批量机具下发modal
    $scope.chooseSn = function(){
        $('#chooseSnModal').modal('show');
    }

    $scope.chooseSnModalCancel = function(){
        $('#chooseSnModal').modal('hide');
    }

    $scope.batchIssuedSnStartBlur=function(){
        $scope.batchIssued.count = $scope.getSnCount($scope.batchIssued.snStart,$scope.batchIssued.snEnd);
    };
    $scope.batchIssuedSnEndBlur=function(){
        $scope.batchIssued.count = $scope.getSnCount($scope.batchIssued.snStart,$scope.batchIssued.snEnd);
    };

    $scope.chooseSnStatus = false;
    $scope.chooseSnModalSubmit = function (){
        $scope.chooseSnStatus = true;
        $scope.baseInfo.machinesSnNo = null;
        if($scope.selectList!=null&&$scope.selectList.length>0){
            if($scope.selectList.length!=$scope.baseInfo.buyNum){
                $scope.notice('发货机具数量与订单购买台数不相符');
            }else {
                for (var i = 0; i < $scope.selectList.length; i++) {
                    var snNo = $scope.selectList[i].snNo;
                    if ($scope.baseInfo.machinesSnNo != null) {
                        $scope.baseInfo.machinesSnNo = $scope.baseInfo.machinesSnNo + "," + snNo;
                    } else {
                        $scope.baseInfo.machinesSnNo = snNo;
                    }

                }
            }
        }
        $scope.chooseSnStatus = false;
        $('#chooseSnModal').modal('hide');
    };


});
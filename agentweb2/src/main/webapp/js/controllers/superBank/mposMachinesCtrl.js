/**
 * Mpos机具管理
 */
angular.module('inspinia',['angularFileUpload']).controller('mposMachinesCtrl',function($scope, $http,i18nService, $state,FileUploader,SweetAlert){

    i18nService.setCurrentLang('zh-cn');

    //状态  1-已入库 2-已分配 3-已发货 4-已启用 5-已激活
    $scope.statusList = [{value:"",text:"全部"},{value:"1",text:"已入库"},{value:"2",text:"已分配"},{value:"3",text:"已发货"},{value:"4",text:"已启用"},{value:"5",text:"已激活"}];

    $scope.batchIssued={count:0};
    $scope.issued={};
    $scope.recycling={};

    $scope.checked = true;

    $scope.resetForm = function (){
        $scope.baseInfo = {status:"",orgId:"-1"};
    };
    $scope.resetForm();
    $scope.baseInfo = {
        enabledDateStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        enabledDateEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.baseInfo.enabledDateStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.baseInfo.enabledDateEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    $scope.columnDefs = [
        {field: 'snNo',displayName: 'SN号',width: 200,pinnable: false,sortable: false},
        {field: 'productTypeName',displayName: '硬件产品种类',width: 150,pinnable: false,sortable: false},
        {field: 'v2MerchantCode',displayName: 'V2商户编号',width: 150,pinnable: false,sortable: false},
        {field: 'v2MerchantName',displayName: 'V2商户简称',width: 150,pinnable: false,sortable: false},
        {field: 'purchaserUserCode',displayName: '采购者编号',width: 150,pinnable: false,sortable: false},
        {field: 'purchaserUserPhone',displayName: '采购者手机号',width: 150,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'status',displayName: '机具状态',width: 150,pinnable: false,sortable: false,cellFilter:'formatDropping:[{value:"1",text:"已入库"},{value:"2",text:"已分配"},{value:"3",text:"已发货"},{value:"4",text:"已启用"},{value:"5",text:"已激活"}]'},
        {field: 'shipDate',displayName: '发货时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'enabledDate',displayName: '启用时间',width: 150,pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'orderNo',displayName: '订单编号',width: 150,pinnable: false,sortable: false}

    ];

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
        if(($scope.baseInfo.snStart!=null&&$scope.baseInfo.snStart!='')&&($scope.baseInfo.snEnd!=null&&$scope.baseInfo.snEnd!='')){
            if($scope.baseInfo.snStart.length!=$scope.baseInfo.snEnd.length) {
                $scope.notice("起始SN号与结束SN号位数不一致");
                return;
            }
        }
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/selectMposMachinesList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }
            $scope.machinesGrid.data = result.data.page.result;
            $scope.machinesGrid.totalItems = result.data.page.totalCount;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };



    //组织列表
    $scope.orgList=[];
    $scope.orgIssuedList=[];
    $scope.getOrgList = function(){
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgList = msg.data;
                $scope.orgIssuedList = msg.data.concat();
                $scope.issued={orgId:msg.data[0].orgId};
                $scope.orgList.unshift({orgId:'-1',orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
    };
    $scope.getOrgList();



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
                    location.href="superBank/mposMachinesExport?baseInfo=" +encodeURI(encodeURI(angular.toJson($scope.baseInfo)));
                }
            });
    };




});
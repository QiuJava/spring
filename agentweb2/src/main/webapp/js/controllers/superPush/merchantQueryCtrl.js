/**
 * 系统管理-系统菜单 控制器
 * 
 * @author xyf1
 */

angular.module('inspinia').controller('superPushMerchantQueryCtrl',function($scope, $http, $state, $stateParams, i18nService, $compile, $filter, $uibModal, $log,uiGridConstants, $httpParamSerializer, $httpParamSerializerJQLike) {
	i18nService.setCurrentLang('zh-cn');


    $scope.agentList = [];
    $scope.merchantStates=[{text:"待一审",value:"1"},{text:"待平台审核",value:"2"},{text:"审核失败",value:"3"},{text:"正常",value:"4"},{text:"资料待完善",value:"999"}];
    $scope.baseInfo = {
        agentNo: '',
        status: '',
        startCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        endCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.baseInfo.startCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.baseInfo.endCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }
	$scope.paginationOptions = {
        pageNo : 1,
        pageSize : 10
    };
    $scope.columnDefs = [
        {field: 'merchantName',displayName: '商户名称',width: 120,pinnable: false,sortable: false},
        {field: 'merchantNo',displayName: '商户编号',width: 130,pinnable: false,sortable: false},
        {field: 'mobilephone',displayName: '手机号',width: 120,pinnable: false,sortable: false},
        {field: 'status',displayName: '状态',width: 120,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.merchantStates)},
        {field: 'createTime',displayName: '注册时间',width: 150,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'agentName',displayName: '直属代理商名称',width: 120,pinnable: false,sortable: false},
        {field: 'agentNo',displayName: '直属代理商编号',width: 120,pinnable: false,sortable: false},
        // {field: '',displayName: '一级代理商名称',width: 120,pinnable: false,sortable: false},
        {field: 'oneLevelId',displayName: '一级代理商编号',width: 120,pinnable: false,sortable: false},
        {field: 'oneMerchantNo',displayName: '上一级商户编号',width: 130,pinnable: false,sortable: false},
        {field: 'twoMerchantNo',displayName: '上二级商户编号',width: 130,pinnable: false,sortable: false},
        {field: 'threeMerchantNo',displayName: '上三级商户编号',width: 130,pinnable: false,sortable: false},
        {field: 'action',displayName: '操作',width: 180,pinnable: false,sortable: false,editable:true,cellTemplate:
            '<a class="lh30" ui-sref="superPush.superPushMerchantDetail({userId: row.entity.userId })">详情</a>'
        }
    ];
    $scope.merchantGrid = {
        data : "merchantData",
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        rowHeight:40,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.querySuperPushMerchant();
            });
        }
    };
    //代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });

    $scope.querySuperPushMerchant = function () {
        if (!($scope.baseInfo.startCreateTime && $scope.baseInfo.endCreateTime)){
            $scope.notice("注册日期不能为空，请重新选择");
            return
        }
        $http({
            url: 'superPushAction/listSuperPushMerchant?pageNo=' + $scope.paginationOptions.pageNo +"&size=" + $scope.paginationOptions.pageSize,
            method: 'POST',
            data: $scope.baseInfo
        }).success(function (data) {
            if (data.success){
                $scope.merchantData = data.data;
                $scope.merchantGrid.totalItems = data.count;
            }else{
                $scope.notice(data.message);
            }
        }).error(function () {
            $scope.notice("服务器异常,请稍后再试");
        });
    };
    
    $scope.resetForm = function () {
        $scope.baseInfo = {
            agentNo: '',
            status: '',
            startCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
            endCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };

});
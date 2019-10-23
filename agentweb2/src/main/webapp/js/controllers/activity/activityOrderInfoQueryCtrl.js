angular.module('inspinia').controller('activityOrderInfoQueryCtrl',function($scope,$http,$state,$filter,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.loadImg = false;
    $scope.myDate  = new Date();
    var date = $filter('date')($scope.myDate,'yyyy-MM-dd');
    $scope.total = {};
    $scope.couponCodes= [{text:'购买鼓励金',value:'6'}];
    $scope.clear = function () {
        $scope.info={startTime:date+" 00:00:00",endTime:date + " 23:59:59",couponCode:"6",transStatus:""};
    }
    $scope.info = {
        startTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
    };
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.info.startTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndTime=function(setTime){
        $scope.info.endTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    $scope.clear();
    $scope.gridOptions = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量//配置表格
        columnDefs:[                        //表格数据
            {field: 'id', displayName: '序号',width:80 },
            { field: 'orderNo',displayName:'业务订单编号',width:160 },
            { field: 'merchantNo',displayName:'商户编号',width:130 },
            { field: 'merchantName',displayName:'商户名称',width:130 },
            { field: 'agentName',displayName:'所属代理商',width:130 },
            { field: 'mobileNo',displayName:'商户手机号',width:120 },
            { field: 'transAmount',displayName:'交易金额',width:90 },
            { field: 'couponCode',displayName:'订单类型',width:90,
                cellFilter:"formatDropping:"+angular.toJson($scope.couponCodes)},
            { field: 'transStatus',displayName:'订单状态',width:100 ,
                cellFilter:"formatDropping:"+angular.toJson($scope.transStatusAll)},
            { field: 'merAccNo',displayName:'收款商户',width:130 },
            { field: 'transTime',displayName:'交易时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:100},
            { field: 'payOrderNo',displayName:'支付订单号',width:100},
            { field: 'remark',displayName:'备注',width:100},
            { field: 'id',displayName:'操作',pinnedRight:true,width:130,cellTemplate:'<a ui-sref="active.activityOrderInfoDetail({id:row.entity.id})" >详情</a>' }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    $scope.query = function () {
        if (!( $scope.info.startTime &&  $scope.info.endTime)){
            $scope.notice("交易日期不能为空");
            return
        }
        $scope.loadImg = true;
        $http.post("activityOrder/actOrderInfoQuery","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
            $scope.loadImg = false;
            $scope.gridOptions.data = data.page.result;
            $scope.gridOptions.totalItems = data.page.totalCount;
        })

        $http.post("activityOrder/actOrderInfoCount","info="+angular.toJson($scope.info),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
            $scope.loadImg = false;
            if(data.total!=null){
                $scope.total.transAmount = data.total.transAmount;
            }else{
                $scope.total.transAmount='';
            }

        })

    }

    //获取代理商
    $scope.agentList = [];
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });
    //条件查询代理商

    $scope.export = function () {
        if (!( $scope.info.startTime &&  $scope.info.endTime)){
            $scope.notice("交易日期不能为空");
            return
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
                    location.href="activityOrder/actOrderInfoExport?info="+angular.toJson($scope.info);
                }
            });

    }

})









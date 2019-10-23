/**
 * 出款订单查询
 */
angular.module('inspinia').controller('queryCreditRepayOrderCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,SweetAlert,$log,i18nService,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.statusSelect=[{text:"全部",value:''},{text:"未执行",value:'1'},{text:"还款中",value:'2'},{text:"还款成功",value:'3'},{text:"还款失败",value:'4'},{text:"挂起",value:'5'},{text:"终止",value:'6'},{text:"逾期待还",value:'7'}];
    $scope.billingStatusSelect=[{text:"全部",value:''},{text:"未记账",value:'0'},{text:"发起记账失败",value:'1'},{text:"记账成功",value:'2'},{text:"记账失败",value:'3'}];
    $scope.repayTypeList = [{value: "", text: "全部"},
        {value: "1", text: "分期还款"},
        {value: "2", text: "全额还款"},
        {value: "3", text: "完美还款"}
        ];

    $scope.info={agentNo:$scope.$root.entityId, bool:'1',batchNo:"",merchantNo:"",mobileNo:"",status:"",minRepayAmount:"",maxRepayAmount:"",
        minEnsureAmount:"",maxEnsureAmount:"",minRepayFee:"",maxRepayFee:"",billingStatus:"",
        repayType:"",
        createTimeBegin:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
       /* completeTimeBegin:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        completeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setCreateTimeBegin=function(setTime){
        $scope.info.createTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        //$scope.info.completeTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setCreateTimeEnd=function(setTime){
        $scope.info.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        //$scope.info.completeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setCompleteTimeBegin=function(setTime){
        //$scope.info.createTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.info.completeTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setCompleteTimeEnd=function(setTime){
        //$scope.info.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
        $scope.info.completeTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    $scope.statusStr=angular.toJson($scope.statusSelect);
    $scope.billingStatusStr=angular.toJson($scope.billingStatusSelect);
    $scope.bools = [{text:'包含',value:'1'},{text:'不包含',value:'0'}];

    $scope.repayAmountAll=0;
    $scope.ensureAmountAll=0;
    $scope.repayFeeAll=0;
    $scope.ensureAmountFreezingAll=0;

    //清空
    $scope.clear=function(){
        $scope.info={agentNo:$scope.$root.entityId, bool:'1', batchNo:"",merchantNo:"",mobileNo:"",status:"",minRepayAmount:"",maxRepayAmount:"",
            minEnsureAmount:"",maxEnsureAmount:"",minRepayFee:"",maxRepayFee:"",billingStatus:"",
            repayType:"",
            createTimeBegin:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
           /* completeTimeBegin:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
            completeTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'**/
        };
    };

    $scope.agentList = [];
    //代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }
        });

    $scope.query=function(){
        if($scope.info.minRepayAmount!=""
            && $scope.info.maxRepayAmount!=""
            && $scope.info.minRepayAmount-$scope.info.maxRepayAmount>0){
            $scope.notice("任务金额最小值不能大于最大值");
            return;
        }
        if($scope.info.minEnsureAmount!=""
            && $scope.info.maxEnsureAmount!=""
            && $scope.info.minEnsureAmount-$scope.info.maxEnsureAmount>0){
            $scope.notice("保证金最小值不能大于最大值");
            return;
        }if($scope.info.minRepayFee!=""
                && $scope.info.maxRepayFee!=""
                && $scope.info.minRepayFee-$scope.info.maxRepayFee>0){
            $scope.notice("服务费最小值不能大于最大值");
            return;
        }
        if(!($scope.info.createTimeBegin &&  $scope.info.createTimeEnd )){
            $scope.notice("起始时间不能为空，请重新选择");
            return;
        }
        $http.post("creditRepayOrder/listCreditRepayOrder.do","baseInfo=" + angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.success){
                    $scope.gridData=data.data.list;
                    $scope.gridOptions.totalItems = data.count;
                    if(data.data.orderAll != null){
                        $scope.repayAmountAll=data.data.orderAll.repayAmountAll;
                        $scope.ensureAmountAll=data.data.orderAll.ensureAmountAll;
                        $scope.repayFeeAll=data.data.orderAll.repayFeeAll;
                        $scope.ensureAmountFreezingAll=data.data.orderAll.ensureAmountFreezingAll;
                    }else{
                        $scope.repayAmountAll = "0.00";
                        $scope.ensureAmountAll = "0.00";
                        $scope.repayFeeAll = "0.00";
                        $scope.ensureAmountFreezingAll = "0.00";
                    }
                }else{
                    $scope.notice(data.message);
                }
            });
        };
    //$scope.query();手动查询

    $scope.gridOptions={                           //配置表格
        data: 'gridData',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'batchNo',displayName:'订单ID',width:180 },
            { field: 'merchantNo',displayName:'用户编号',width:180 },
            { field: 'nickname',displayName:'昵称',width:180 },
            { field: 'userName',displayName:'姓名',width:180 },
            { field: 'mobileNo',displayName:'手机号',width:180 },
            { field: 'agentName',displayName:'代理商名称',width:180 },
            { field: 'status',displayName:'订单状态',cellFilter:"formatDropping:"+$scope.statusStr,width:150},
            { field: 'repayAmount',displayName:'任务金额',width:180,cellFilter:"currency:''" },
            { field: 'ensureAmount',displayName:'保证金',width:180, cellFilter:"currency:''" },
            { field: 'repayFee',displayName:'服务费',width:180,cellFilter:"currency:''" },
            { field: 'successPayAmount',displayName:'已消费总额',width:180,cellFilter:"currency:''" },
            { field: 'successRepayAmount',displayName:'已还款总额',width:180,cellFilter:"currency:''" },
            { field: 'actualPayFee',displayName:'实际消费手续费',width:180,cellFilter:"currency:''" },
            { field: 'actualWithdrawFee',displayName:'实际还款手续费',width:180,cellFilter:"currency:''" },
            { field: 'repayNum',displayName:'还款期数',width:180},
            { field: 'accountNo',displayName:'还款卡号',width:180},
            { field: 'bankName',displayName:'还款银行',width:180},
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'repayBeginTime',displayName:'开始时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'repayEndTime',displayName:'结束时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'completeTime',displayName:'终态时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'mission',displayName:'任务',width:180 },
            { field: 'repayType',displayName:'订单类型',width:180,cellFilter:"formatDropping:"+angular.toJson($scope.repayTypeList)},
            { field: 'billingStatus',displayName:'记账状态',cellFilter:"formatDropping:"+$scope.billingStatusStr,width:150},
            { field: 'id',displayName:'操作',pinnedRight:true,width:120,
                cellTemplate:'<a class="checkbox" ui-sref="creditRepay.repayOrderDetail({id:row.entity.batchNo})" target="_black" >详情</a>'
            }
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

    $scope.import=function(){
        if($scope.info.minRepayAmount!=""
            && $scope.info.maxRepayAmount!=""
            && $scope.info.minRepayAmount-$scope.info.maxRepayAmount>0){
            $scope.notice("任务金额最小值不能大于最大值");
            return;
        }
        if($scope.info.minEnsureAmount!=""
            && $scope.info.maxEnsureAmount!=""
            && $scope.info.minEnsureAmount-$scope.info.maxEnsureAmount>0){
            $scope.notice("保证金最小值不能大于最大值");
            return;
        }if($scope.info.minRepayFee!=""
            && $scope.info.maxRepayFee!=""
            && $scope.info.minRepayFee-$scope.info.maxRepayFee>0){
            $scope.notice("服务费最小值不能大于最大值");
            return;
        }
        if(!($scope.info.createTimeBegin &&  $scope.info.createTimeEnd )){
            $scope.notice("起始时间不能为空，请重新选择");
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
                    debugger;
                    location.href="creditRepayOrder/exportCreditRepayOrder?baseInfo="+encodeURIComponent(angular.toJson($scope.info));
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
})
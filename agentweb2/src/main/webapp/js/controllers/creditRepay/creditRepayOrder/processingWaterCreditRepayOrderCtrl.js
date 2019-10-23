/**
 * 出款订单查询
 */
angular.module('inspinia').controller('processingWaterCreditRepayOrderCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,SweetAlert,$log,i18nService,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.planStatusSelect=[{text:"全部",value:''},{text:'未执行',value:'0'},{text:'执行中',value:'1'},{text:'执行成功',value:'2'},{text:'执行失败',value:'3'}];
    $scope.planTypeSelect=[{text:"全部",value:''},{text:"给用户还款",value:'IN'},{text:"用户消费",value:'OUT'}];
    $scope.planStatusStr=angular.toJson($scope.planStatusSelect);
    $scope.planTypeStr=angular.toJson($scope.planTypeSelect);
    $scope.info={planNo:"",planStatus:"",batchNo:"",merchantNo:"",minPlanAmount:"",maxPlanAmount:"",accountNo:"",planTimeBegin:"",planTimeEnd:"",
        planTimeBegin:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        planTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
    };
    //清空
    $scope.clear=function(){
        $scope.info={planNo:"",planStatus:"",batchNo:"",merchantNo:"",minPlanAmount:"",maxPlanAmount:"",accountNo:"",
            planTimeBegin:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
            planTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    }
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.info.planTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndTime=function(setTime){
        $scope.info.planTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    $scope.query=function(){
        if($scope.info.minPlanAmount!=""
            && $scope.info.maxPlanAmount!=""
            && $scope.info.minPlanAmount-$scope.info.maxPlanAmount>0){
            $scope.notice("任务金额最小值不能大于最大值");
            return;
        }
      /*  if($scope.info.planTimeBegin!=""
            && $scope.info.planTimeEnd!=""
            && $scope.info.planTimeBegin>$scope.info.planTimeEnd){
            $scope.notice("计划时间不能大于结束时间");
            return;
        }*/
        if(!($scope.info.planTimeBegin && $scope.info.planTimeEnd )){
            $scope.notice("计划时间不能为空，请重新选择");
            return;
        }
        $http.post("creditRepayOrder/selectDetail.do","baseInfo="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                if(msg.status){
                    $scope.result = msg.page.result;
                    $scope.gridOptions.totalItems = msg.page.totalCount;
                }else{
                    $scope.notice(msg.msg);
                }
            });
    }
    //$scope.query();手动查询

    $scope.gridOptions={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'planNo',displayName:'任务流水号',width:180 },
            { field: 'planAmount',displayName:'金额',width:180,cellFilter:"currency:''" },
            { field: 'accountNo',displayName:'还款卡号',width:180},
            { field: 'batchNo',displayName:'来源订单号',width:180 },
            { field: 'merchantNo',displayName:'用户编号',width:180 },
            { field: 'planType',displayName:'类型',cellFilter:"formatDropping:"+$scope.planTypeStr,width:150},
            { field: 'planStatus',displayName:'状态',cellFilter:"formatDropping:"+$scope.planStatusStr,width:150},
            { field: 'resMsg',displayName:'错误信息',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'planTime',displayName:'计划时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'bak1',displayName:'备注',width:180 }
            //,{ field: 'bak2',displayName:'备注2',width:180 }
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
        if($scope.info.minPlanAmount!=""
            && $scope.info.maxPlanAmount!=""
            && $scope.info.minPlanAmount-$scope.info.maxPlanAmount>0){
            $scope.notice("任务金额最小值不能大于最大值");
            return;
        }
       /* if($scope.info.planTimeBegin!=""
            && $scope.info.planTimeEnd!=""
            && $scope.info.planTimeBegin>$scope.info.planTimeEnd){
            $scope.notice("计划时间不能大于结束时间");
            return;
        }*/
        if(!($scope.info.planTimeBegin && $scope.info.planTimeEnd )){
            $scope.notice("计划时间不能为空，请重新选择");
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
                    location.href="creditRepayOrder/exportDetailAllInfo?baseInfo="+encodeURIComponent(angular.toJson($scope.info));
                }
            });
    }

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
})
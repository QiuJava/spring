/**
 * 分润月报
 */
angular.module('inspinia').controller('shareReportQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.accountStatusSelect=[{text:'全部',value:""},{text:'入账成功',value:1},{text:'入账失败',value:2},
        {text:'未入账',value:3}];
    $scope.accountStatusStr=angular.toJson($scope.accountStatusSelect);

    //清空
    $scope.clear=function(){
        $scope.info={accountStatus:"",
            accountTimeBegin:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
            accountTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        };
    };
    $scope.clear();

    $scope.info = {
        accountTimeBegin:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        accountTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.info.accountTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.info.accountTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    $scope.clearOrderTotal=function(){
        $scope.total={shareAmountTotal:0,shareAmountAccTotal:0,shareAmountNoAccTotal:0};
    };
    $scope.clearOrderTotal();

    //产品列表
    $scope.prodNoList=[{value:"",text:"全部"}];
    $http.post("safeOrder/getSafeConfigList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.prodNoList.push({value:list[i].proCode,text:list[i].proCode});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'billMonth',displayName:'保单月份',width:180 },
            { field: 'oneAgentNo',displayName:'一级代理商编号',width:180 },
            { field: 'oneAgentName',displayName:'一级代理商名称',width:180 },
            { field: 'totalAmount',displayName:'保险订单总金额',width:180,cellFilter:"currency:''" },
            { field: 'totalCount',displayName:'保单数',width:180 },
            { field: 'shareRate',displayName:'代理商分润百分比(单位:%)',width:180 },
            { field: 'shareAmount',displayName:'代理商分润金额',width:180,cellFilter:"currency:''" },
            { field: 'accountStatus',displayName:'分润入账状态',width:120,cellFilter:"formatDropping:" +  $scope.accountStatusStr },
            { field: 'accountTime',displayName:'入账时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180}
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
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        if (!(  $scope.info.accountTimeBegin &&   $scope.info.accountTimeEnd)){
            $scope.notice("入账日期不能为空");
            return
        }
        $scope.loadImg = true;
        $http.post("shareReport/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                    $scope.total=data.total;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };

    // 导出
    $scope.exportInfo = function () {
        if (!(  $scope.info.accountTimeBegin &&   $scope.info.accountTimeEnd)){
            $scope.notice("入账日期不能为空");
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
                    location.href = "shareReport/exportDetail?info=" + encodeURI(angular.toJson($scope.info));
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
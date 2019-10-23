/**
 * 红包领取查询
 */
angular.module('inspinia').controller('queryRedEnvelopesReceiveCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,SweetAlert,$log,i18nService,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.statusSelect=[{text:"全部",value:''},{text:"待领取",value:'0'},{text:"已领取",value:'1'},
        {text:"平台回收",value:'2'},{text:"原路退回",value:'3'}];
    
//    $scope.busTypeSelect= $scope.redBusTypes;
//    $scope.busTypeStr=angular.toJson($scope.busTypeSelect);
    $scope.busTypeSelect=[{text:"全部",value:''},{text:"个人发红包",value:'0'},{text:"信用卡奖励",value:'2'}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.pushTypeSelect=$scope.redPushTypes;
    $scope.pushTypeStr=angular.toJson($scope.pushTypeSelect);

    $scope.receiveTypeSelect=$scope.redReceiveTypes;
    $scope.receiveTypeStr=angular.toJson($scope.receiveTypeSelect);


    $scope.allCount=0;
    $scope.amountCount=0;

    //查询所有银行家组织
    $scope.orgInfoList = [];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:"",orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("redEnvelopesReceive/selectByParam","info=" + angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.allCount=data.page.totalCount;
                    $scope.gridOptions.totalItems = data.page.totalCount;
                    if(data.sunOrder!=null){
                        $scope.amountCount=data.sunOrder.amountCount;
                    }
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
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
            { field: 'id',displayName:'红包领取ID',width:180 },
            { field: 'redOrderId',displayName:'红包ID',width:180 },
            { field: 'status',displayName:'领取状态',cellFilter:"formatDropping:"+$scope.statusStr,width:150},
            { field: 'pushType',displayName:'发放人类型',cellFilter:"formatDropping:"+$scope.pushTypeStr,width:150},
            { field: 'receiveType',displayName:'接收人数类型',cellFilter:"formatDropping:"+$scope.receiveTypeStr,width:150},
            { field: 'busType',displayName:'业务类型',cellFilter:"formatDropping:[{text:'个人发红包',value:'0'},{text:'信用卡奖励',value:'2'}]",width:150},
//            { field: 'confId',displayName:'红包配置ID',width:180 },
            { field: 'orderNo',displayName:'关联业务订单ID',width:180 },
            { field: 'orgName',displayName:'发放人组织名称',width:180 },
            { field: 'pushUserCode',displayName:'发红包用户ID',width:180 },
            { field: 'pushUserName',displayName:'发红包用户姓名',width:180 },
            { field: 'pushUserPhone',displayName:'发红包手机号',width:180 },
            { field: 'getUserCode',displayName:'领取用户ID',width:180 },
            { field: 'getUserName',displayName:'领取用户姓名',width:180 },
            { field: 'getUserPhone',displayName:'领取用户手机号',width:180 },
            { field: 'amount',displayName:'领取金额',width:180,cellFilter:"currency:''" },
            { field: 'getDate',displayName:'领取时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180}
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


    //清空
    $scope.clear=function(){
        $scope.info={status:"",pushType:"",receiveType:"",busType:"",orgId:"",
            getDateMin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            getDateMax:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    }
    $scope.clear();

    $scope.import=function(){
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
                    location.href="redEnvelopesReceive/exportInfo?info="+encodeURI(angular.toJson($scope.info));
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
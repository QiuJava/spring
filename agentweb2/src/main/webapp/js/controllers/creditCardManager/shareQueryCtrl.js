/**
 * 分润查询
 */
angular.module('inspinia').controller('creditShareQueryCtrl',function($scope,$rootScope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
    //中文
    i18nService.setCurrentLang('zh-cn');

    $scope.paginationOptions = {pageNo : 1,pageSize : 10};
    $scope.bools = [{text:'否',value:'0'},{text:'是',value:'1'}];
    $scope.orderTypes = [{text:"全部",value:""},{text:'会员服务费',value:'1'},{text:'其他',value:'2'}];
    $scope.enterStatus = [{text:"全部",value:""},{text:'未入账',value:'1'},{text:'已入账',value:'2'},{text:'入账失败',value:'3'}];
    $scope.statusStr = '[{text:"未入账",value:"1"},{text:"已入账",value:"2"},{text:"入账失败",value:"3"}]';
    $scope.typeStr = '[{text:"会员服务费",value:"1"},{text:"其他",value:"2"}]';
    $scope.info={agentNo:"",
        bool:"1",
        sCreateTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD'+' 00:00:00'),
        eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        orderType:"",
        enterStatus:"",
        userId:"",
        orderNo:"",
        sCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.info.sCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.info.eCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }
    $scope.shareTotalMoney = 0;
    $scope.tradeTotalMoney = 0;

    $scope.agent = [{text: "全部", value: ""}];
    //代理商
    $http.post("creditCardManager/selectAllAgentInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });

    $scope.columnDefs = [
        {field: 'createDate',displayName: '分润创建时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'shareCash',displayName: '分润金额',width: 150,cellFilter:"currency:''",pinnable: false,sortable: false},
        {field: 'sharePercentage',displayName: '分润百分比',width: 150,pinnable: false,cellTemplate:'<span class="lh30" ng-show="row.entity.sharePercentage!=null">'+
            '{{row.entity.sharePercentage}}%</span>',sortable: false},
        {field: 'enterStatus',displayName: '入账状态',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusStr},
        {field: 'shareAgentName',displayName: '分润代理商名称',width: 150,pinnable: false,sortable: false},
        {field: 'shareAgentNo',displayName: '分润代理商编号',width: 150,pinnable: false,sortable: false},
        {field: 'relatedOrderNo',displayName: '关联订单号',width: 170,pinnable: false,sortable: false},
        {field: 'orderCash',displayName: '订单金额',width: 150,cellFilter:"currency:''",pinnable: false,sortable: false},
        {field: 'orderType',displayName: '订单类型',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.typeStr},
        {field: 'userId',displayName: '用户ID',width: 170,pinnable: false,sortable: false},
        {field: 'belongAgentNo',displayName: '所属代理商编号',width: 150,pinnable:false,sortable: false},
        {field: 'belongAgentName',displayName: '所属代理商名称',width: 150,pinnable:false,sortable: false},
        {field: 'enterDate',displayName: '入账日期',width: 150,pinnable:false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
    ];

    $scope.myGrid = {
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

    /*查询*/
    $scope.shareQuerying = false;
    $scope.query = function () {
        if($scope.shareQuerying){
            $scope.notice("正在查询数据中,请稍后...");
            return;
        }
        if(!($scope.info.sCreateTime && $scope.info.eCreateTime)){
            $scope.notice("查询条件创建时间不能为空，请选择要查询的创建时间！");
            return;
        }
        if($scope.info.sCreateTime>$scope.info.eCreateTime){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        $scope.shareQuerying = true;
        $http.post('creditCardManager/selectShareByParam',"baseInfo=" + angular.toJson($scope.info) + "&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
            $scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (msg) {
            $scope.shareQuerying = false;
            $scope.myGrid.data = msg.page.result;
            console.log(msg.shareTotalMoney)
            $scope.shareTotalMoney =msg.shareTotalMoney;
            $scope.tradeTotalMoney =msg.tradeTotalMoney;

            $scope.myGrid.totalItems = msg.page.totalCount;
        }).error(function (msg) {
            $scope.shareQuerying = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    /*清空*/
    $scope.clear=function(){
        $scope.info={agentNo:"",
            bool:"1",
            sCreateTime:"",
            eCreateTime:"",
            orderType:"",
            enterStatus:"",
            userId:"",
            orderNo:"",
            sCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
            eCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
        };
    }

    /*导出信息*/
    $scope.exporting = false;
    $scope.exportInfo=function(){
        if(!($scope.info.sCreateTime && $scope.info.eCreateTime)){
            $scope.notice("查询条件创建时间不能为空，请选择要查询的创建时间！");
            return;
        }
        if($scope.info.sCreateTime>$scope.info.eCreateTime){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        if($scope.exporting){
            $scope.notice("正在导出数据,请不要频繁点击导出按钮.");
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
                    $scope.exporting = true;
                    setTimeout(function () {
                        $scope.exporting = false;
                    }, 5000);
                    location.href="creditCardManager/exportShareInfo?baseInfo="+angular.toJson($scope.info);
                }
            });
    }


});

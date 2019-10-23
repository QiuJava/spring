/**
 * 我的信息-每日分润报表 控制器
 * @author tgh415
 */

angular.module('inspinia').controller('tradeShareCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.info = {
    	pageNo: 1,
		pageSize: 10,
        agentNo: $scope.entityId,
        isDeductionFee: '',
        startTransDate:moment(new Date().getTime()).format('YYYY-MM-DD') + ' 00:00:00',
        endTransDate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
	};
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.info.startTransDate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setEndTime=function(setTime){
        $scope.info.endTransDate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }
    $scope.agent= [];
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });

    $scope.reset = function(){
        // debugger;
        var agentNo = $scope.info.agentNo;
        var pageSize = $scope.info.agentNo;
        $scope.info = {
            agentNo: agentNo,
            pageSize: pageSize,
            agentNo: $scope.entityId,
            isDeductionFee: '',
            startTransDate:moment(new Date().getTime()).format('YYYY-MM-DD') + ' 00:00:00',
            endTransDate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };

    };
    var t = Date.now();  
    
    function sleep(d){  
        while(Date.now - t <= d);  
    }

    $scope.submitting = false;
    $scope.selectTradeShareInfo=function(){
        if(!( $scope.info.startTransDate &&  $scope.info.endTransDate)){
            $scope.notice("交易日期不能为空")
        }
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        formatDate();
    	$http({
			url: 'myInfo/profitDaySettleDetailList',
			method: 'POST',
			data: $scope.info
		}).success(function(result){
            if(result.status){
                $scope.data = result.data.list;
                $scope.tradeShareGrid.totalItems = result.data.total;
            }else{
                $scope.notice(result.msg);
            }
        });
        $scope.loadImg = false;
    	$scope.submitting = false;
    };
    
    $scope.exportTransing = false;
    $scope.export=function(){
        if(!( $scope.info.startTransDate &&  $scope.info.endTransDate)){
            $scope.notice("交易日期不能为空")
        }
        formatDate();
        //window.location.href = "myInfo/exportProfitDaySettleDetailList?" + $.param($scope.info);
        $scope.exportInfoClick("myInfo/exportProfitDaySettleDetailList",{"info":angular.toJson($scope.info)});
    };
    
    function formatDate() {
        if($scope.info.startAgentProfitGroupTime && $scope.info.startAgentProfitGroupTime.format){
            $scope.info.startAgentProfitGroupTime = $scope.info.startAgentProfitGroupTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.endAgentProfitGroupTime && $scope.info.endAgentProfitGroupTime.format){
            $scope.info.endAgentProfitGroupTime = $scope.info.endAgentProfitGroupTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.startTransDate && $scope.info.startTransDate.format){
            $scope.info.startTransDate = $scope.info.startTransDate.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.endTransDate && $scope.info.endTransDate.format){
            $scope.info.endTransDate = $scope.info.endTransDate.format("YYYY-MM-DD HH:mm:ss");
        }
    }
    $scope.tradeShareGrid ={
    	 data:'data',
    	 paginationPageSize:10,                  //分页数量
         paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
         useExternalPagination: true,
         columnDefs:[
    				{ field: 'plateOrderNo',displayName:'交易订单号',width:150},
    				{ field: 'transTime',displayName:'交易时间',width:150},
    				{ field: 'hardwareProductName',displayName:'硬件产品种类',width:150},
    				{ field: 'businessProductName',displayName:'业务产品',width:150},
    				/*{ field: 'superPush',displayName:'超级推商户',width:150},
    				{ field: 'payMethod',displayName:'交易类型',width:150},*/
    				// { field: 'serviceName',displayName:'服务类型',width:150},
    				{ field: 'cardType',displayName:'卡类型',width:150},
    				{ field: 'merchantNo',displayName:'商户编号',width:150},
    				{ field: 'merchantName',displayName:'商户名称',width:150},
    				{ field: 'agentNo',displayName:'代理商编号',width:150},
    				{ field: 'agentName',displayName:'代理商名称',width:150},
    				{ field: 'transAmount',displayName:'交易金额',width:150},
    				{ field: 'merchantRate',displayName:'交易商户扣率',width:150},
    				{ field: 'merchantFee',displayName:'交易商户手续费',width:150},
    				// { field: 'saleName',displayName:'收单机构',width:150},
    				// { field: 'acqOutCost',displayName:'收单服务成本',width:150},
    				{ field: 'agentShareAmount',displayName:'交易代理商分润',width:150},
    				{ field: 'merCashFee',displayName:'商户提现手续费',width:150},
    				{ field: 'deductionFee',displayName:'抵扣提现手续费',width:150},
    				{ field: 'cashAgentShareAmount',displayName:'提现代理商分润',width:150},
    				// { field: 'aaa',displayName:'出款通道',width:150},
    				// { field: 'daiCost',displayName:'代付成本',width:150}
    				// { field: 'dianCost',displayName:'垫资成本',width:150},
    				// { field: 'aaa',displayName:'对账状态',width:150},
    				// { field: 'agentProfitCollectionStatus',displayName:'代理商分润汇总状态',width:150},
    				// { field: 'collectionBatchNo',displayName:'汇总批次号',width:150}
    			],
       onRegisterApi: function(gridApi) {                //选中行配置
           $scope.gridApi = gridApi;
           $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
               $scope.info.pageNo = newPage;
               $scope.info.pageSize = pageSize;
               $scope.selectTradeShareInfo();
           });
//           $scope.selectTradeShareInfo();
       }
    };
});
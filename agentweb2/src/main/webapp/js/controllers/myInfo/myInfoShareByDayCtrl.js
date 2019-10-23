/**
 * 我的信息-每日分润报表 控制器
 * @author tgh415
 */

angular.module('inspinia').controller('myInfoShareByDayCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService, SweetAlert){
	
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$scope.status=[{text:"全部",value:""},{text:"已入账",value:"ENTERACCOUNTED"},{text:"未入账",value:"NOENTERACCOUNT"}];
	
	$scope.myAccount = {balance: 0, balanceToday: 0};
	$scope.accountAllInfo = [];
	$scope.defaultCollectionData = {
        "sumOpenBackAmount": 0.00,
        "sumAdjustTotalShareAmount": 0.00,
        "sumPreTransCashAmount": 0.00,
        "sumAdjustTransShareAmount": 0.00,
        "sumPreTransShareAmount": 0.00,
        "sumRiskSubAmount": 0.00,
        "sumMerCashFee": 0.00,
        "sumAdjustTransCashAmount": 0.00,
        "sumTerminalFreezeAmount": 0.00,
        "sumTransTotalAmount": 0.00,
        "sumFreezeAmount": 0.00,
        "sumAdjustAmount": 0.00,
		"sumRealEnterShareAmount": 0.00
    };
	$scope.collectionData = $scope.defaultCollectionData;

    $scope.collectionFreezeOrAdjust = {
        freeze: {
            sumFreezeAmount: 0.00,
            freezedAmount: 0.00,
            needFreezeAmount: 0.00
        },
        adjust:{
            sumAdjustAmount: 0.00,
            adjustedAmount: 0.00,
            needAdjustAmount:0.00
        },
        fristAgent: false
    };
	$scope.info = {sTime:moment(new Date().getTime()).format('YYYY-MM-DD') + ' 00:00:00',eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			statu:"",selectAgentNo: ''};
	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setBeginTime=function(setTime){
		$scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

	}

	setEndTime=function(setTime){
		$scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

	}
    $scope.agent= [{"text": "全部", "value": ""}];

    $http.post("myInfo/findAgentProfitCollection")
        .success(function(msg){
        	if (msg.success){
				if(msg.data && msg.data.freeze){
                    $scope.collectionFreezeOrAdjust.freeze = msg.data.freeze;
				}
                if(msg.data && msg.data.adjust){
                    $scope.collectionFreezeOrAdjust.adjust = msg.data.adjust;
                }
                if(msg.data && msg.data.agent){
                    $scope.collectionFreezeOrAdjust.fristAgent = (msg.data.agent.oneLevelId == msg.data.agent.agentNo);
                }
			}
        });

    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });
	$scope.reset = function(){
		$scope.info = {
			sTime:moment(new Date().getTime()).format('YYYY-MM-DD') + ' 00:00:00',
			eTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			statu:"",selectAgentNo: ''};
	};


	$scope.selectAccountInfo=function(){
		if(!($scope.info.sTime && $scope.info.eTime)){
			$scope.notice("交易日期不能为空");
			return;
		}
		var data={"sTime":$scope.info.sTime,"eTime":$scope.info.eTime,"statu":$scope.info.statu,
            "selectAgentNo":$scope.info.selectAgentNo,"pageNo":$scope.paginationOptions.pageNo,
			"pageSize":$scope.paginationOptions.pageSize};
		$http.post(
			'myInfo/selectAllShareList',
			 "info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(result.bols){
				$scope.data = result.list;
                $scope.accountGrid.totalItems = result.total;
                $scope.collectionData =	result.collection || $scope.defaultCollectionData;
			}else{
				$scope.notice(result.msg);
			}
		});
	};

    $scope.exportingSharePreDay = false;
    $scope.exportInfo=function(){
        if(!($scope.info.sTime && $scope.info.eTime)){
            $scope.notice("交易日期不能为空");
            return;
        }
        if($scope.exportingSharePreDay){
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
                    $scope.exportingSharePreDay = true;
                    setTimeout(function () {
                        $scope.exportingSharePreDay = false;
                    }, 5000);
                    location.href="myInfo/exportSharePreDay?"+$.param($scope.info);
                }
            });
    };


	$scope.accountGrid ={
		 data:'data',
		 paginationPageSize:10,                  //分页数量
	     paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	     useExternalPagination: true,
	     columnDefs:[                           //表格数据
					{ field: 'transDate',displayName:'交易日期',width:150,
						cellTemplate:"<div>{{row.entity.transDate | date:'yyyy-MM-dd'}}</div>"
					},
					{ field: 'agentNo',displayName:'代理商编号',width:150},
					{ field: 'agentName',displayName:'代理商名称',width:150},
					{ field: 'transTotalAmount',displayName:'交易总金额',width:150},
					{ field: 'transTotalNum',displayName:'交易总笔数',width:150},
					{ field: 'cashTotalNum',displayName:'提现总笔数',width:150},
					{ field: 'merFee',displayName:'商户交易手续费',width:150},
					{ field: 'merCashFee',displayName:'商户提现手续费' ,width:150},
					{ field: 'preTransShareAmount',displayName:'原交易分润' ,width:150},
				    { field: 'preTransCashAmount',displayName:'原提现分润' ,width:150},
				    // { field: 'openBackAmount',displayName:'开通返现',width:150},
				    // { field: 'rateDiffAmount',displayName:'费率差异' ,width:150},
				    // { field: 'merMgAmount',displayName:'商户管理费' ,width:150},
				    // { field: 'riskSubAmount',displayName:'风控扣款' ,width:150},
				    // { field: 'bailSubAmount',displayName:'保证金扣除',width:150},
				    // { field: 'otherAmount',displayName:'其他',width:100},
				    { field: 'adjustAmount',displayName:'调账金额',width:100},
				    { field: 'adjustTransShareAmount',displayName:'调整后交易分润',width:150 },
				    { field: 'adjustTransCashAmount',displayName:'调整后提现分润' ,width:150},
				    { field: 'adjustTotalShareAmount',displayName:'调整后总分润',width:150},
				    { field: 'realEnterShareAmount',displayName:'实际到账分润',width:150},
				    // { field: 'terminalFreezeAmount',displayName:'机具款冻结',width:150},
				    // { field: 'otherFreezeAmount',displayName:'其他冻结',width:150},
				    { field: 'freezeAmount',displayName:'冻结金额',width:150},
				    { field: 'enterAccountTime',displayName:'入账时间',width:150,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
				    { field: 'enterAccountStatus',displayName:'入账状态',width:150,
				    	cellFilter:"formatDropping:"+angular.toJson($scope.status)
				    }
				],
				 onRegisterApi: function(gridApi) {                //选中行配置
			         $scope.gridApi = gridApi;
			         //全选
			         $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
			            if(rows[0].isSelected){
			               $scope.testRow = rows[0].entity;
			               for(var i=0;i<rows.length;i++){
			            	   rowList[rows[i].entity.id]=rows[i].entity;
			               }
			            }else{
			            	rowList={};
			            }
			         })
			         //单选
			         $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
			            if(row.isSelected){
			               $scope.testRow = row.entity;
			               rowList[row.entity.id]=row.entity;
			            }else{
			            	delete rowList[row.entity.id];
			            }
			         })
			         $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				          	$scope.paginationOptions.pageNo = newPage;
				          	$scope.paginationOptions.pageSize = pageSize;
				            $scope.selectAccountInfo();
				            rowList={};
				     });
			      }
	};
});
angular.module('inspinia').controller('distributionTerminalCtrl',function($scope,$uibModalInstance){
	 $scope.solutionModalClose=function(){
		 $uibModalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $uibModalInstance.close($scope);
		 $scope.gridApi.selection.clearSelectedRows();//清空选择
	 }
});

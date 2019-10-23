/**
 * 调单查询
 */
angular.module('inspinia').controller('surveyOrderQueryCtrl',function($scope,$http,$state,$stateParams,i18nService,
                                                                      $document,SweetAlert,$rootScope,$location){
	//数据源
	i18nService.setCurrentLang('zh-cn');
    $scope.entityAgentLevel = $rootScope.entityAgentLevel;
	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.info = {merTeamId:"-1",
        createTimeStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
       /* replyEndTimeStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
        replyEndTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setCreateTimeStart=function(setTime){
        $scope.info.createTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setCreateTimeEnd=function(setTime){
        $scope.info.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }
    setReplyEndTimeStart=function(setTime){
        $scope.info.replyEndTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    setReplyEndTimeEnd=function(setTime){
       $scope.info.replyEndTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

    }

    $scope.statusSelect=[{text:"是",value:'1'},{text:"否",value:'0'}];

    //调单类型、业务类型、交易状态、处理状态、支付方式、回复状态
	$scope.orderTypes=initData["ORDER_TYPE_CODE"];
	$scope.orderServices=initData["ORDER_SERVICE_CODE"];
    $scope.transStatuses=initData["TRANS_STATUS"];
    //$scope.dealStatuses=initData["ORDER_DEAL_STATUS"];
    /*$scope.dealStatuses=[{text:"未处理",value:'0'},{text:"部分提供",value:'1'},{text:"持卡人承认交易",value:'2'},{text:"全部提供",value:'3'},
        {text:"无法提供",value:'4'},{text:"逾期部分提供",value:'5'},{text:"逾期全部提供",value:'6'},{text:"逾期未回",value:'7'},
        {text:"已回退",value:'8'}];*/
    $scope.dealStatuses=[{text:"未处理",value:'0'},{text:"已处理",value:'1'},{text:"已处理",value:'2'},{text:"已处理",value:'3'},
        {text:"已处理",value:'4'},{text:"已处理",value:'5'},{text:"已处理",value:'6'},{text:"已处理",value:'7'},
        {text:"已回退",value:'8'}];
    $scope.queryDealStatuses=[{text:"未处理",value:'0'},{text:"已处理",value:'1'}, {text:"已回退",value:'8'}];
    $scope.payMethods=initData["PAY_METHOD_TYPE"];
    //$scope.replyStatuses=initData["REPLY_STATUS_CODE"];
    $scope.replyStatuses=[{text:"未回复",value:'0'},{text:"未回复（下级已提交）",value:'1'},{text:"已回复",value:'2'},{text:"逾期未回复",value:'3'},
        {text:"逾期未回复（下级已提交）",value:'4'},{text:"逾期已回复",value:'5'}];
    $scope.agent=initAgent;

    $http.get('merchantInfo/merTeams.do')
        .success(function(data) {
            if(!data){ return;
            }
            $scope.merTeams=data;
            $scope.merTeams.splice(0,0,{merTeamId:"-1",appName:"全部"});
        });

    $scope.query = function () {
        if(!($scope.info.createTimeStart && $scope.info.createTimeEnd)){
            $scope.notice("发起日期不能为空");
            return;
        }
        $http({
            url: 'surveyOrder/selectOrderByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.info,
            method:'POST'
        }).success(function (data) {
            if (!data.status){
                $scope.notice(data.msg);
                return;
            }
            $scope.myGrid.data = data.page.result;
            $scope.myGrid.totalItems = data.page.totalCount;
        }).error(function (data) {
            $scope.notice(data.msg);
        });
    };
    $scope.query();

	$scope.columnDefs = [
        {field: 'orderTypeCode',displayName: '类型',width: 120,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.orderTypes)},
		/*{field: 'transOrderNo',displayName: '订单编号',width: 180,pinnable: false,sortable: false,
            cellTemplate:'<a class="lh30" ui-sref="orderInfo({orderNo:row.entity.transOrderNo,database:row.entity.transOrderDatabase})">{{row.entity.transOrderNo}}</a>'},*/
		{field: 'transOrderNo',displayName: '订单编号',width: 180,pinnable: false,sortable: false,
            cellTemplate:'<a class="lh30" ' +
            'ng-click="grid.appScope.toOrderInfo(row.entity.transOrderNo,row.entity.transOrderDatabase,row.entity.orderServiceCode)">{{row.entity.transOrderNo}}</a>'},
        {field: 'orderServiceCode',displayName: '业务类型',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.orderServices)},
		{field: 'acqReferenceNo',displayName: '系统参考号',width: 150,pinnable: false,sortable: false},
        {field: 'createTime',displayName: '发起时间',width: 180,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'replyEndTime',displayName: '截止回复时间',width: 180,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'replyStatus',displayName: '回复状态',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.replyStatuses)},
        {field: 'dealStatus',displayName: '处理状态',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.dealStatuses)},
		{field: 'urgeNum',displayName: '催单次数',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<a class="lh30" ng-show="grid.appScope.canShow()" ng-click="grid.appScope.showUrge(row.entity.orderNo)">{{row.entity.urgeNum}}</a>'},
        {field: 'merchantNo',displayName: '商户编号',width: 150,pinnable: false,sortable: false},
        {field: 'merGroup',displayName: '商户组织',width: 150,pinnable: false,sortable: false},
		{field: 'transAccountNo',displayName: '交易卡号',width: 150,pinnable: false,sortable: false},
		{field: 'transAmount',displayName: '交易金额',width: 150,pinnable: false,sortable: false},
        {field: 'transTime',displayName: '交易时间',width: 180,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'agentName',displayName: '所属代理商',width: 150,pinnable: false,sortable: false},
        {field: 'payMethod',displayName: '交易方式',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.payMethods)},
        {field: 'transStatus',displayName: '交易状态',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.transStatuses)},
		{field: 'action',displayName: '操作',width: 180,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<a class="lh30"  ui-sref="riskControlManagement.surveyOrderInfo({id:row.entity.id,orderNo:row.entity.orderNo,from:-1})">查看</a>'+
            '<a class="lh30"  ng-show="row.entity.canReply==1" style="margin-left: 10px" ' +
            'ui-sref="riskControlManagement.orderReply({id:row.entity.id,orderNo:row.entity.orderNo})">回复</a>' +
            '<a class="lh30"  ng-show="row.entity.canCheck==1" style="margin-left: 10px" ' +
            'ui-sref="riskControlManagement.replyEdit({id:row.entity.id,orderNo:row.entity.orderNo,type:2})">审核</a>' +
            '<a class="lh30"  ng-show="row.entity.canEdit==1" style="margin-left: 10px" ' +
            'ui-sref="riskControlManagement.replyEdit({id:row.entity.id,orderNo:row.entity.orderNo,type:1})">修改</a>'}
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



    $scope.hideAllModel = function () {
        $("#changeShareLevel").modal('hide');
    };
	
	$scope.resetForm = function () {
		$scope.info = {
            createTimeStart:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
	}

	//导出
    $scope.import=function(){
        if(!($scope.info.createTimeStart && $scope.info.createTimeEnd)){
            $scope.notice("发起日期不能为空");
            return;
        }
     /*   if($scope.info.lastUpdateTimeStart!=""
            && $scope.info.lastUpdateTimeEnd!=""
            && $scope.info.lastUpdateTimeStart>$scope.info.lastUpdateTimeEnd){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }*/
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
                    //只能用get请求
                    location.href="surveyOrder/export?order="+encodeURIComponent(angular.toJson($scope.info));
                }
            });
    };

	$scope.toOrderInfo = function (orderNo, database,orderServiceCode) {
        if(database == "old"){
            $scope.show=false;
            $scope.notice("此订单数据已超过查询时限，暂不提供查询功能。")
            return;
        }
        $location.url('/orderInfo/'+orderNo+"/"+orderServiceCode);

    }

    $scope.canShow = function () {
        if($scope.entityAgentLevel == 1){
            return true;
        }
        return false;
    }

	//催单弹框
    $scope.urgeRecordList=[];
	$scope.showUrge = function (orderNo) {
	    $http.get('surveyOrder/urgeRecordList?orderNo='+orderNo).success(
	        function (data) {
               $scope.urgeRecordList = data.urgeRecordList;
            });
        $('#myModal').modal('show');
    }

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});
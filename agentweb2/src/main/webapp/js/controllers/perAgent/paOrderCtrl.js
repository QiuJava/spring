/**
 * 机具申购订单
 */
angular.module('inspinia',['angularFileUpload']).controller('paOrderCtrl',function($scope,$rootScope,$http,$state,$stateParams,i18nService,$document,SweetAlert,FileUploader){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {agentNo:''};
	$scope.statusSelect = [{text:"未认证",value:'0'},{text:"已认证",value:'1'}];
	$scope.sendTypeSelect = [{text:"快递配送",value:'1'},{text:"线下自提",value:'2'}];
	$scope.accStatusSelect = [{text:"未入账",value:'NOENTERACCOUNT'},{text:"已入账",value:'ENTERACCOUNTED'}];
	$scope.entryStatusSelect = [{text:"未入账",value:'0'},{text:"已入账",value:'1'}];
	$scope.isPlatformSelect = [{text:"机构发货",value:'0'},{text:"平台发货",value:'1'},{text:"大盟主发货",value:'2'},{text:"盟主发货",value:'3'}];
	$scope.transChannelSelect = [{text:"微信",value:'wx'},{text:"支付宝",value:'zfb'},{text:"快捷支付",value:'kj'}];
	$scope.orderStatusSelect = [{text:"待发货",value:'0'},{text:"待付款",value:'1'},{text:"已发货",value:'2'},{text:"已关闭",value:'4'}];

    $scope.csrfData = $("meta[name=_csrf]").attr('content');
    $scope.loadImg = false;
    $scope.showBatchSendButton = $scope.$root.entityAgentLevel;

    $scope.receivedTotal=0;
    $scope.loginUserType =$rootScope.loginUserType;
    $scope.totalReceivedAmount=0;
    $scope.waitSendTotal=0;
    $scope.totalWaitSendAmount=0;
	$scope.agentList = [];
	//代理商下拉列表查询tgh418
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNode,text:msg[i].agentNo+ " " + msg[i].agentName});
            }
        });
    var mUserType=0;
    $scope.query = function () {
        $http({
            url: 'paOrder/selectPaOrderByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if(msg.status){
                $scope.receivedTotal=msg.receivedTotal.receivedTotal;
                //$rootScope.loginUserType =msg.loginUserType;
                //$scope.loginUserType =msg.loginUserType;
                //mUserType=msg.loginUserType;
                $scope.totalReceivedAmount=msg.receivedTotal.totalReceivedAmount;
                $scope.waitSendTotal=msg.waitSendTotal.waitSendTotal;
                $scope.totalWaitSendAmount=msg.waitSendTotal.totalWaitSendAmount;
                $scope.accountedTotal = msg.accountedTotal;
                $scope.unAccountTotal = msg.unAccountTotal;
                $scope.shareAccountedTotal = msg.shareAccountedTotal;
                $scope.shareUnAccountTotal = msg.shareUnAccountTotal;
                $scope.terminalTotal = msg.terminalTotal;
                $scope.myGrid.data = msg.page.result;
                $scope.myGrid.totalItems = msg.page.totalCount;
            }else {
                $scope.notice(msg.msg);

            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.query();

	$scope.columnDefs = [
		{field: 'orderNo',displayName: '订单编号',width: 150,pinnable: false,sortable: false},
        {field: 'img',displayName: '申购商品图片',width:180,
            cellTemplate:'' +
            '<div ng-show="row.entity.img!=null"' +
            '<a href="{{row.entity.img}}" fancybox rel="group">' +
            '<img style="width:70px;height:35px;" ng-src="{{row.entity.img}}"/>' +
            '</a>' +
            '</div>'},
        {field: 'gName',displayName: '申购商品名称',width:180},
        {field: 'color',displayName: '颜色',width:150},
        {field: 'size',displayName: '尺码',width:150},
        {field: 'price',displayName: '销售单价(元)',width:150},
		{field: 'num',displayName: '购买数量',width: 150,pinnable: false,sortable: false},
        {field: 'yunfei',displayName: '运费',width:100,cellTemplate:'<div>包邮</div>'},
        {field: 'totalAmount',displayName: '订单金额',width: 150,pinnable: false,sortable: false},
        {field: 'goodsTotal',displayName: '机具款项金额',width:180},
        {field: 'entryStatus',displayName: '机具款项入账状态',width: 180,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.entryStatusSelect)},
        {field: 'entryTime',displayName: '机具款项入账日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'shareAmount',displayName: '机具分润金额',width:180},
        {field: 'accStatus',displayName: '机具分润入账状态',width: 180,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.accStatusSelect)},
        {field: 'accTime',displayName: '机具分润入账日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'userName',displayName: '申购盟主姓名',width: 150,pinnable: false,sortable: false},
		{field: 'userCode',displayName: '申购盟主编号',width: 150,pinnable: false,sortable: false},
        {field: 'orderStatus',displayName: '订单状态',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.orderStatusSelect)},
        {field: 'sendType',displayName: '订单类型',width: 180,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.sendTypeSelect)},
        {field: 'isPlatform',displayName: '发货方',width: 180,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.isPlatformSelect)},
		{field: 'receiver',displayName: '收件人',width: 150,pinnable: false,sortable: false},
		{field: 'receiverMobile',displayName: '联系方式',width: 150,pinnable: false,sortable: false},
		{field: 'receiverAddress',displayName: '收货地址',width: 150,pinnable: false,sortable: false},
        {field: 'transChannel',displayName: '支付方式',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.transChannelSelect)},
        {field: 'createTime',displayName: '下单日期',width: 180,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'transTime',displayName: '支付日期',width: 180,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'sendTime',displayName: '发货日期',width: 180,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'remark',displayName: '备注',width: 150,pinnable: false,sortable: false},
//        {field: 'receiptDate',displayName: '确认收货日期',width: 180,pinnable: false,sortable: false,
//            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                "<span ng-show='(row.entity.isPlatform==0 && row.entity.orderStatus==0 && "+$scope.loginUserType+"==1)||(row.entity.isPlatform==2 && row.entity.orderStatus==0 && "+$scope.loginUserType+"==2)'><a class=\"lh30\" ui-sref=\"perAgent.deliver({num: row.entity.num,userCode: row.entity.userCode,orderNo:row.entity.orderNo})\" \">发货</a></span>" +
                "<span ng-show='((row.entity.orderStatus==2||row.entity.orderStatus==3))'><a ng-click='grid.appScope.showDeliver(row)'>发货信息" +
                "</a></span>"}
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


    var uploader = $scope.uploader = new FileUploader({
        url: 'paOrder/sendTerBatch',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });


    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    }

    $scope.submit=function(){
    	if(uploader.queue.length == 0){
            alert("请上传批量发货文件");
            return;
		}
        $scope.loadImg = true;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            if(!response.result){
                $scope.loadImg = false;
            	if(response.status){
                    $("#batchSendResult").modal("show");
                    $scope.errorCount=response.errorCount;
                    $scope.successCount=response.successCount;
                    if($scope.errorCount>0){
                        $scope.errorList=response.errorList;
                    }
				}else{
                    $scope.notice(response.msg);
				}

            }else{
                $scope.loadImg = false;
                $scope.notice(response.msg);

            }
        };
        return false;
    }

    $scope.sendResultGrid = {
        data: 'errorList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'orderNo',displayName: '订单编号',width:100},
            {field: 'transportCompany',displayName: '快递公司',width:200},
            {field: 'postNo',displayName: '快递单号',width:200},
            {field: 'errorMsg',displayName: '原因',width:200}
        ]
    };

    $scope.closeBatchSendResult=function () {
        $('#batchSendResult').modal('hide');
        $("#sendTerminalBatch").modal("hide");
        $scope.loadImg = false;
    }

    $scope.resetForm = function () {
		$scope.baseInfo = {agentNo:''};
	}

	$scope.changeAgentNode = function () {
        $scope.disabledMerchantType = !$scope.baseInfo.agentNode;
    };

	//批量发货弹框
    $scope.toSendTerminalBatch = function () {
        $("#sendTerminalBatch").modal('show');

    };
    $scope.sendTerminalBatch = function () {
        alert("确定");

    };
    $scope.hideAllModel = function () {
        $("#expressage").modal('hide');
        $("#sendTerminalBatch").modal('hide');
    };
    $scope.hideDevModel = function () {
        $("#expressage").modal('hide');
    };

    $scope.orderPostNo="";
    $scope.orderTransportCompany="";
    $scope.devOrderNo="";
    //展示发货信息
    /*$scope.devPaginationOptions = {pageNo : 1,pageSize : 10};
    $scope.myDevGrid.data = [];
    $scope.myDevGrid.totalItems = 0;*/
    $scope.devPaginationOptions = {pageNo : 1,pageSize : 10};
    $scope.showDeliver = function (row) {
        //alert(row.entity.orderNo);
        $scope.orderPostNo=row.entity.postNo;
        if($scope.orderPostNo==null||$scope.orderPostNo=="null"){
            $scope.orderPostNo="";
        }
        $scope.devOrderNo=row.entity.orderNo;

        $scope.orderTransportCompany=row.entity.transportCompany;
        if($scope.orderTransportCompany==null||$scope.orderTransportCompany=="null"){
            $scope.orderTransportCompany="";
        }
        $scope.getSendMsg(row.entity.orderNo);
    };

    $scope.getSendMsg = function (orderNo) {
        $http({
            url: 'paOrder/selectSendTerminal?pageNo='+$scope.devPaginationOptions.pageNo+'&pageSize='+$scope.devPaginationOptions.pageSize+"&orderNo="+orderNo,
            method:'POST'
        }).success(function (msg) {
            if(msg.status){
                //$scope.myDevGrid.data = msg.terLists;
                //$scope.myDevGrid.totalItems = 0;
                $scope.myDevGrid.data = msg.page.result;
                $scope.myDevGrid.totalItems = msg.page.totalCount;
                $("#expressage").modal('show');
            }else {
                $scope.notice(msg.msg);

            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };


    $scope.devColumnDefs = [

        {field: 'sn',displayName: 'SN号',width: 150,pinnable: false,sortable: false},
        {field: 'typeName',displayName: '硬件产品种类',width: 150,pinnable: false,sortable: false}

    ];

    $scope.myDevGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
//		rowHeight:35,
        columnDefs: $scope.devColumnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.devPaginationOptions.pageNo = newPage;
                $scope.devPaginationOptions.pageSize = pageSize;
                $scope.getSendMsg($scope.devOrderNo);
            });
        }
    };

    //导出
    $scope.import=function(){
        /*if($scope.baseInfo.createTimeBegin!=""
            && $scope.baseInfo.createTimeEnd!=""
            && $scope.baseInfo.createTimeBegin>$scope.baseInfo.createTimeEnd){
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
                    location.href="paOrder/exportOrder?baseInfo="+encodeURIComponent(angular.toJson($scope.baseInfo));
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
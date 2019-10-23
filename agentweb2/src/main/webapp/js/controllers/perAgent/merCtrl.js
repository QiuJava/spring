/**
 * 盟主商户查询
 */
angular.module('inspinia').controller('merCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {userCode:'',realName:'',mobile:'',merchantNo:'',startCreateTime:'',endCreateTime:'',startActTime:'',endActTime:''};
	$scope.statusSelect = [{text:"未认证",value:'0'},{text:"已认证",value:'1'}];
	$scope.parentTypes = [{text:"盟主",value:'3'},{text:"大盟主",value:'2'},{text:"机构",value:'1'}];
	//$scope.grades = [{text:"普通盟主",value:'0'},{text:"黄金盟主",value:'1'},{text:"铂金盟主",value:'2'},{text:"黑金盟主",value:'3'},{text:"钻石盟主",value:'4'}];
    $scope.updateShareLevelData = {create_user:'', user_code:'', after_level:''};
    $scope.userTypes = [{text:"盟主",value:'3'},{text:"大盟主",value:'2'}];
    $scope.merTypeList = [{text:"已注册未认证商户",value:'0'},{text:"已认证未绑定机具商户",value:'1'},{text:"已绑机具未激活商户",value:'2'},
                          {text:"已激活商户",value:'3'},{text:"由商户成为盟主的商户",value:'4'}];
    $scope.userTotal=0;
    $scope.merTotal=0;
    $scope.agentUserTotal=0;
    $scope.shareLevelSelect = [];
    $scope.shareLevel=1;

    $scope.query = function () {
        $http({
            url: 'perMer/selectPaMerByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.myGrid.data = msg.page.result;
            $scope.myGrid.totalItems = msg.page.totalCount;
            $scope.userTotal = msg.userTotal;
            $scope.merTotal = msg.merTotal;
            $scope.merUserTotal = msg.merUserTotal;
            //分润比例最高可调级别
			//$scope.maxAgentLevel = msg.maxAgentLevel;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.query();

	$scope.columnDefs = [
		{field: 'merchantNo',displayName: '商户编号',width: 150,pinnable: false,sortable: false},
		{field: 'merchantName',displayName: '商户名称',width: 150,pinnable: false,sortable: false},
		{field: 'merType',displayName: '商户类型',width: 150,pinnable: false,sortable: false},
		{field: 'mobilePhone',displayName: '商户手机号码',width: 150,pinnable: false,sortable: false},
        {field: 'userCode',displayName: '所属盟主编号',width: 150,pinnable: false,sortable: false},
        {field: 'realName',displayName: '所属盟主姓名',width: 150,pinnable: false,sortable: false},
        {field: 'nickName',displayName: '所属盟主昵称',width: 150,pinnable: false,sortable: false},
		{field: 'mobile',displayName: '所属盟主手机',width: 150,pinnable: false,sortable: false},
		{field: 'createTime',displayName: '注册日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'actTime',displayName: '激活日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}

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

	//获取分润下拉选项
    $scope.shareLevelSelect = [];
    $scope.shareLevelList = function () {
        $http.get('perAgent/shareLevelList').success(
            function (data) {
                if (data.status){
                    $scope.shareLevelSelect = data.shareLevelList;
                }else{
                    $scope.notice(data.msg);
                }
            }
        );
    }

	//修改分润比例
    $scope.toChangeShareLevel = function (row) {
        $scope.updateShareLevelData.after_level="";
        //var level = row.entity.shareLevel;
        /*for(var i=1; i<= $scope.maxAgentLevel; i++){
                if(i <= level){
                    $scope.shareLevelSelect.push({text:"Lv"+i,value:i,show:true});
                }else {
                    $scope.shareLevelSelect.push({text:"Lv"+i,value:i});
                }
			}*/

        $scope.updateShareLevelData.user_code = row.entity.userCode;
        $scope.shareLevel = row.entity.shareLevel;
        $("#changeShareLevel").modal('show');

    };

    $scope.updateShareLevel = function () {
    	if($scope.updateShareLevelData.after_level == ''){
    		alert("请选择分润比例");
    		return;
		}
		if($scope.updateShareLevelData.after_level <= $scope.shareLevel){
    		alert("调整后的分润比例必须大于原来的分润比例");
    		return;
		}
        $http({
            url: 'perAgent/updateShareLevel',
            method: 'POST',
            data: $scope.updateShareLevelData
        }).success(function (data) {
            if (data.status){
                $scope.notice("操作成功");
                $scope.hideAllModel();
                $scope.query();
            }else{
                $scope.notice(data.msg);
            }
        });
    };

    $scope.hideAllModel = function () {
        $("#changeShareLevel").modal('hide');
    };
	
	$scope.resetForm = function () {
		$scope.baseInfo = {agentNo:''};
	}

	$scope.changeAgentNode = function () {
        $scope.disabledMerchantType = !$scope.baseInfo.agentNode;
    };

	//导出
    $scope.import=function(){
        if($scope.baseInfo.createTimeBegin!=""
            && $scope.baseInfo.createTimeEnd!=""
            && $scope.baseInfo.createTimeBegin>$scope.baseInfo.createTimeEnd){
            $scope.notice("起始时间不能大于结束时间");
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
                    //只能用get请求
                	location.href="perMer/exportMerInfo?baseInfo="+encodeURIComponent(angular.toJson($scope.baseInfo));
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
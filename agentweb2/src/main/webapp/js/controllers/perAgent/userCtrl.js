/**
 * 盟主管理
 */
angular.module('inspinia').controller('userCtrl',function($scope,$http,$state,$stateParams,i18nService,$document,SweetAlert,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {agentNo:''};
	$scope.statusSelect = [{text:"未认证",value:'0'},{text:"已认证",value:'1'}];
	$scope.parentTypes = [{text:"盟主",value:'3'},{text:"大盟主",value:'2'},{text:"机构",value:'1'}];
	//$scope.grades = [{text:"普通盟主",value:'0'},{text:"黄金盟主",value:'1'},{text:"铂金盟主",value:'2'},{text:"黑金盟主",value:'3'},{text:"钻石盟主",value:'4'}];
    $scope.updateShareLevelData = {create_user:'', user_code:'', after_level:''};
    $scope.userTypes = [{text:"盟主",value:'3'},{text:"大盟主",value:'2'}];
    $scope.userTotal=0;
    $scope.bigUserTotal=0;
    $scope.agentUserTotal=0;
    $scope.shareLevelSelect = [];
    $scope.shareLevel=1;

    $scope.query = function () {
        $http({
            url: 'perAgent/selectPaUserByParam?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
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
            $scope.bigUserTotal = msg.bigUserTotal;
            $scope.merUserTotal = msg.merUserTotal;
            $scope.agentStatus = msg.agentStatus;
            $scope.selectStatus();
            //分润比例最高可调级别
			//$scope.maxAgentLevel = msg.maxAgentLevel;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.query();

	$scope.columnDefs = [
		{field: 'userCode',displayName: '用户编号',width: 150,pinnable: false,sortable: false},
		{field: 'realName',displayName: '用户姓名',width: 150,pinnable: false,sortable: false},
		{field: 'nickName',displayName: '昵称',width: 150,pinnable: false,sortable: false},
        {field: 'userType',displayName: '用户类型',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.userTypes)},
		{field: 'merTotal',displayName: '直营商户（家）',width: 150,pinnable: false,sortable: false},
		{field: 'allyTotal',displayName: '发展盟主（名）',width: 150,pinnable: false,sortable: false},
		{field: 'allMerTotal',displayName: '盟友商户激活数',width: 150,pinnable: false,sortable: false},
		{field: 'mobile',displayName: '注册手机',width: 150,pinnable: false,sortable: false},
        /*{field: 'grade',displayName: '盟主称号',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.grades)},*/
		{field: 'levelShow',displayName: '标准分润比例',width: 150,pinnable: false,sortable: false},
	    {field: 'vipLevelShow',displayName: 'VIP分润比例',width: 150,pinnable: false,sortable: false},
		{field: 'parentId',displayName: '所属上级编号',width: 150,pinnable: false,sortable: false},
		{field: 'parentName',displayName: '所属上级名称',width: 150,pinnable: false,sortable: false},
        {field: 'parentType',displayName: '所属上级类型',width: 150,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.parentTypes)},
		{field: 'isBindCard',displayName: '是否认证',width: 150,pinnable: false,sortable: false,
			cellFilter:"formatDropping:"+ angular.toJson($scope.statusSelect)},
		{field: 'createTime',displayName: '注册日期',width: 180,pinnable: false,sortable: false,
			cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 300,pinnedRight:true,sortable: false,editable:true,cellTemplate:
			'<a class="lh30" ng-show="row.entity.isDirect==1 && row.entity.agentStatus == 1" ng-click="grid.appScope.toChangeShareLevel(row)">调整标准分润比例</a> '
			+'<a class="lh30" ng-show="row.entity.userType == 3 && grid.appScope.entityAgentLevel == 1 && row.entity.parentType == 1" ui-sref="agent.addAgent({mobilephone:row.entity.mobile,userCode:row.entity.userCode})"> | 升级成大盟主</a>'
			/*+'<a class="lh30" ng-show="row.entity.userType == 3 && grid.appScope.entityAgentLevel == 1 && row.entity.canProfitChange == 0 && grid.appScope.oneAgentStatus == 1" ng-click="grid.appScope.updateCanProfitChange(row)"> | 允许调比例</a>'
			+'<a class="lh30" ng-show="row.entity.userType == 3 && grid.appScope.entityAgentLevel == 1 && row.entity.canProfitChange == 1 && grid.appScope.oneAgentStatus == 1" ng-click="grid.appScope.updateCanProfitChange(row)"> | 取消调比例</a>'*/
			}
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
	
	$scope.updateCanProfitChange=function(row){
		$scope.canProfitChange = 0;
		if (row.entity.canProfitChange == 0) {
			$scope.canProfitChange = 1;
		}
		var data={"canProfitChange":$scope.canProfitChange,"userCode":row.entity.userCode}
		$http.post("perAgent/updateCanProfitChange.do",angular.toJson(data))
			.success(function(result) {
				if (result) {
					$scope.query();
				}
		});
	}
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
    $scope.oneAgentStatus = null;
    $scope.selectStatus = function () {
    	$http.get('perAgent/selectStatus.do').success(
    			function (data) {
    				$scope.oneAgentStatus = data;
    			}
    	);
    }
    $scope.selectStatus();

    //修改标准分润比例
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
    		$scope.notice("请选择标准分润比例");
    		return;
		}
		if($scope.updateShareLevelData.after_level <= $scope.shareLevel){
			$scope.notice("调整后的标准分润比例必须大于原来的标准分润比例");
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
                    location.href="perAgent/exportPerAgentUser?baseInfo="+encodeURIComponent(angular.toJson($scope.baseInfo));
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
/**
 * 机具发货
 */
angular.module('inspinia').controller('deliverCtrl',function($scope,$rootScope,$state,$filter,$log,$http,$stateParams,$compile,$uibModal,i18nService,$document,$q){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {};
	$scope.info={transportCompany:"",sn:"",snText:""};
    $scope.loginUserType = $rootScope.loginUserType;
	$scope.statusSelect = [{text:"未认证",value:'0'},{text:"已认证",value:'1'}];
    $scope.sendData={terminalInfo:"请选择发货机具"};
    $scope.SNResult=[];
    var rowList = [];
    var promises = [];
    $scope.hardTypeList = [];
    var hpPromise=$q.defer();
    promises.push(hpPromise.promise);
    
	$scope.columnDefs = [
        {field: 'id', displayName: '序号', pinnable: false, width: 80},
        {field: 'sn',displayName: 'SN号',width: 150,pinnable: false,sortable: false},
		{field: 'typeName',displayName: '硬件产品种类',width: 150,pinnable: false,sortable: false},
		{field: 'activityTypeValues',displayName: '机具活动类型',width: 150,pinnable: false,sortable: false},

	];

    $scope.query = function () {
        $http({
            url: 'paOrder/selectTerminal?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.SNGrid.data = msg.page.result;
            $scope.SNGrid.totalItems = msg.page.totalCount;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    $scope.SNGrid = {
//          data: 'result',
          paginationPageSize:10,                  //分页数量
          paginationPageSizes: [10,20,50,100,500],	  //切换每页记录数
          useExternalPagination: true,		    //开启拓展名
          enableHorizontalScrollbar: true,        //横向滚动条
          enableVerticalScrollbar : true,  		//纵向滚动条
          columnDefs: $scope.columnDefs,
          onRegisterApi: function(gridApi) {
              $scope.gridApi = gridApi;
              //全选
              $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                  if(rows[0].isSelected){
                      $scope.testRow = rows[0].entity;
                      for(var i=0;i<rows.length;i++){
                          rowList[rows[i].entity.id]=rows[i].entity;
                      }
                      $scope.addList(rowList);
                  }else{
                      rowList={};
                      for(var i=0;i<rows.length;i++){
                          $scope.delteData(rows[i].entity);
                      }
                  }
              })
              //单选
              $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
                  if(row.isSelected){
                      $scope.testRow = row.entity;
                      rowList[row.entity.id]=row.entity;
                      $scope.addData(row.entity);
                  }else{
                      delete rowList[row.entity.id];
                      $scope.delteData(row.entity);
                  }
              })
              gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                  $scope.paginationOptions.pageNo = newPage;
                  $scope.paginationOptions.pageSize = pageSize;
                  $scope.query();
              });
          },
          isRowSelectable: function(row){ // 选中行
              if($scope.SNResult != null && $scope.SNResult.length>0){
                  for(var i=0;i<$scope.SNResult.length;i++){
                      if(row.entity.sn==$scope.SNResult[i].sn){
                          row.grid.api.selection.selectRow(row.entity);
                      }
                  }
                }
          }
    };

$q.all(promises).then(function(){
    // 获取数据完成了
    promises = [];
    SNGrid();
    initSNResultGrid();
});
$scope.SNResultGrid = {
        data: 'SNResult',
//        paginationPageSize:10,                  //分页数量
//		paginationPageSizes: [10,20,50,100,500],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        onRegisterApi: function(gridApi) {
            $scope.SNResultGridApi = gridApi;
        },
    };
	initSNResultGrid();
	function initSNResultGrid(){
	    $scope.SNResultGrid.columnDefs = [                           //表格数据
//	        {field: 'num', displayName: '序号',width: 80,sortable: false,cellTemplate: "<span>{{rowRenderIndex + 1}}</span>"},
	        {field: 'id', displayName: '序号',width: 80},
	        {field: 'sn', displayName: 'SN号',width: 130},
	        {field: 'typeName',displayName:'硬件产品种类',width:130},
	        {field: 'activityTypeValues', displayName: '机具活动类型',width: 130},
	        {field: 'action',displayName: '操作',width:80,cellTemplate: '<a class="lh30" ng-click="grid.appScope.delteData(row.entity)">移除</a> '}
	        ]
	}
	$scope.addList = function(rowList){
	    if(rowList!=null){
	        for(var i in rowList){
	            if(rowList[i]!=null&&rowList[i]!=""){
	                if($scope.checkData($scope.SNResult,rowList[i],null)){
	                    $scope.SNResult.push({
	                    	id:rowList[i].id,
	                        sn:rowList[i].sn,
	                        typeName:rowList[i].typeName,
	                        activityTypeValues:rowList[i].activityTypeValues
	                    });
	                }
	            }
	        }
        }
    };

    $scope.addData = function(row){
        if(row!=null&&row!=""){
            if($scope.checkData($scope.SNResult,row,null)){
                $scope.SNResult.push({
                	id:row.id,
                    sn:row.sn,
                    typeName:row.typeName,
                    activityTypeValues:row.activityTypeValues
                });
            }
        }
    };

    $scope.checkData = function(dataList,info,oldInfo){
        if(dataList!=null&&dataList.length>0){
            for(var i=0;i<dataList.length;i++){
                var item=dataList[i];
                if(oldInfo!=null){
                    if(item.sn==oldInfo.sn){
                        continue;
                    }
                }
                if(item.sn==info.sn){
                    return false;
                }
            }
        }
        return true;
    };
    $scope.delteData = function(row){
        if(row!=null&&row!=""){
            for(var j=0;j<$scope.SNResult.length;j++){
                var dateItem=$scope.SNResult[j];
                if(row.sn==dateItem.sn){
                    $scope.SNResult.splice(j, 1);
                }
            }
            for(var j=0;j<$scope.SNGrid.data.length;j++){
                var dateItem=$scope.SNGrid.data[j];
                if(row.sn==dateItem.sn){
                    $scope.gridApi.selection.unSelectRow($scope.SNGrid.data[j]);
                }
            }
        }
    };
    var rowList={};
    //更新页面的发货信息
    $scope.updateDeliverInfo = function () {
    	var terminalNum = $stateParams.num;
        var sns = "[";
        var num = 0;
        var disList=angular.copy($scope.SNResult);
        for(index in disList){
            num++;
            sns += "{'sn':'" + disList[index].sn + "'},"
        }
        sns = sns.substring(0,sns.length-1);
        sns += "]";
        if(num == 0){
        	alert("请选择需要发货的机具");
        	return;
		}
		if(num != terminalNum){
			$scope.notice("当前已选择" + num + "台，机具台数与订购数量不符，请确认选择的机具台数。");
            return;
		}
        $scope.sendData.terminalTotal=num;
        $scope.sendData.terminalInfo = "已选择了" + $scope.sendData.terminalTotal + "台机具";
        $scope.sendData.sns = sns;
        $("#expressage").modal('hide');
    };


   /* $http.get('hardwareProduct/selectAllInfo.do')
        .success(function(result){
            if(!result)
                return;
            $scope.termianlTypes=result;
            $scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
        })*/


    $http.get('sysDict/listSysDictGroup.do?keyName=AGENT_EXPRESS')
        .success(function(result){
            if(!result)
                return;
            $scope.expresses=result.data;
            $scope.expresses.splice(0,1);
        })

	//填写发货信息
    $scope.toDeliver = function (row) {
        $scope.paginationOptions = {pageNo : 1,pageSize : 10};
//        $scope.SNGrid.data = [];
//        $scope.SNGrid.totalItems = 0;
//        $scope.index = 0;
        $("#expressage").modal('show');

    };

    $scope.loadImg = false;
    $scope.toSend = function () {
    	if($scope.loginUserType==1&&($scope.sendData.transportCompany == '' || $scope.sendData.transportCompany == null)){
    		alert("请选择快递公司");
    		return;
		}
		if($scope.loginUserType==1&&($scope.sendData.postNo == '' || $scope.sendData.postNo == null)){
    		alert("请填写物流单号");
    		return;
		}
		if($scope.sendData.terminalInfo=="请选择发货机具"){
    	    alert("请选择发货机具");
    	    return;
        }
        $scope.sendData.userCode = $stateParams.userCode;
        $scope.sendData.orderNo = $stateParams.orderNo;
        $scope.loadImg = true;
        $http({
            url: 'paOrder/toSend',
            method: 'POST',
            data: $scope.sendData
        }).success(function (data) {
            if (data.status){
                $scope.loadImg = false;
                $scope.notice("确认发货成功");
                $scope.sendData={};
                $scope.hideAllModel();
                $scope.query();
                $scope.returnToOrder();
            }else{
                $scope.loadImg = false;
                $scope.notice(data.msg);
            }
        });
    };

    $scope.hideAllModel = function () {
        $("#expressage").modal('hide');
        $scope.SNGrid.data = [];
        $scope.SNGrid.totalItems = 0;
    };

    $scope.returnToOrder = function () {
        $state.go('perAgent.order',{});
    };


    $scope.resetForm = function () {
		$scope.baseInfo = {agentNo:''};
	}
    //返回
    $scope.cancelSNModel=function(){
    	if ($scope.sendData.terminalInfo == "请选择发货机具") {
    		$scope.SNGrid.data=[];
    		$scope.SNGrid.totalItems = 0;
    		$scope.SNResult=[];
    		rowList={};
		}
        $('#expressage').modal('hide');
    }
    //确认选择
    /*$scope.confirmSNModel=function(){
        $('#expressage').modal('hide');
        $scope.info.sn="";
        if($scope.SNResult!=null&&$scope.SNResult.length>0){
            for(var i in $scope.SNResult){
                $scope.info.sn+=$scope.SNResult[i].sn+",";
            }
        }
        if($scope.info.sn!=""){
            $scope.info.sn=$scope.info.sn.substr(0,$scope.info.sn.length-1)
            $scope.info.snText='已选择'+$scope.info.sn.split(",").length+'台机具';
        }else{
            $scope.info.snText="";
        }

    }*/


	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});
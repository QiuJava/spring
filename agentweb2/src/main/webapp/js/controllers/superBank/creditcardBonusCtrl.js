/**
 * 信用卡配置
 */
angular.module('inspinia').controller('creditcardBonusCtrl',function($scope,$http,i18nService,$document,$window){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.resetForm = function () {
        $scope.creditCardConf = {sourceId:-1,orgId:'-1'};//查询条件的form
        $scope.creditCard = {sourceId:-1};//新增数据的form
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'orgId',displayName: '组织ID',width: 80,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织名称',width: 150,pinnable: false,sortable: false},
        {field: 'bankName',displayName: '银行',width: 150,pinnable: false,sortable: false},
        {field: 'isOnlyone',displayName: '首次办卡才有奖励',width: 140,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2"><label ng-show="row.entity.isOnlyone==0">否</label><label ng-show="row.entity.isOnlyone==1">是</label></div><div class="lh30" ng-show="row.entity.action==2"><select ng-model="row.entity.isOnlyone"><option selected="selected" value="0">否</option><option value="1">是</option></select></div>'},
//        {field: 'isOnlyone',displayName: '是否首次办卡奖励',width: 140,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2"><label ng-show="row.entity.isOnlyone==0">否</label><label ng-show="row.entity.isOnlyone==1">是</label></div><div class="lh30" ng-show="row.entity.action==2"><select ng-model="row.entity.isOnlyone"><option selected="selected" value="0">否</option><option value="1">是</option></select></div>'},
        {field: 'bankBonus',displayName: '银行给品牌总奖金',width: 140,cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.bankBonus}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.bankBonus"/></div>'},
//        {field: 'orgCost',displayName: '品牌成本(公司收入)',width: 150,cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.orgCost}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.orgCost"/></div>'},
        {field: 'orgPushCost',displayName: '品牌发放总奖金',width: 140,cellFilter:"currency:''",cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.orgPushCost}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="height:28px;width:100px;text-align:center;" ng-model="row.entity.orgPushCost"/></div>'},
//        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
//        	cellTemplate:
//        	'<div  class="lh30" ng-show="row.entity.action!=2"><input type="hidden" ng-model="row.entity.sourceId"/><input type="hidden" ng-model="row.entity.id"/><input type="hidden" ng-model="row.entity.action" /><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')"  ng-click="grid.appScope.creditCardConfEdit(row.entity)">编辑</a></div>'
//        	+
//        	'<div  class="lh30" ng-show="row.entity.action==2"><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')" ng-click="grid.appScope.creditCardUpd(row.entity)">保存</a></div>'
//        }
    ];
    
    $scope.orgInfoGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  	//开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
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

    
    $scope.changeOps = function (op) {
		 angular.forEach($scope.banksListInAdd, function(item){
			 if(item.id==op){
				 $scope.creditCard.bankBonus=item.bankBonus;
	     	}
	     });
    };
    
    $scope.banksListInAdd = [];
    $scope.banksList = [];
    $scope.orgInfoList = [];
    
    //获取银行列表
    $scope.getBanks = function(){
    	 $http({
             url:"superBank/banksList",
             method:"POST"
         }).success(function(msg){
             if(msg.status){
                 $scope.banksList = msg.data;
                 $scope.banksListInAdd = msg.data.concat();
                 $scope.creditCard={sourceId:msg.data[0].id,bankBonus:msg.data[0].bankBonus};
                 $scope.banksList.unshift({id:-1,bankName:'全部'});
             }
         }).error(function(){
             $scope.notice("获取银行列表异常");
         });
    };
    $scope.getBanks();
    
    //获取组织
    $scope.getOrgList = function(){
   	 $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:'-1',orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织列表异常");
        });
   };
   $scope.getOrgList();
    
    //编辑配置
    $scope.creditCardConfEdit = function(entity){
    	 entity.action=2;
    };
    
    //更新
    $scope.creditCardUpd = function(entity){
    	 var data = {
    			 	"id" : entity.id,
    				"sourceId":entity.sourceId,
    				"orgCost":entity.orgCost,
    				"orgPushCost":entity.orgPushCost,
    				"bankBonus":entity.bankBonus,
    				"isOnlyone":entity.isOnlyone
    		};
    	 $http.post("superBank/updateCreditCardConf",angular.toJson(data))
    		.success(function(data){
    			if(data.status){
    				$scope.notice(data.msg);
    				entity.action=1;
    			}else{
    				$scope.notice(data.msg);
    				$scope.submitting = false;
    			}
    		});
    };
    
    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        
        $http({
            url: 'superBank/getCreditCardConf?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.creditCardConf,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.orgInfoGrid.data = msg.data.result;
            $scope.orgInfoGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addBank = function(){
    	$scope.creditCard.isOnlyone = true;
    	$("#addBank").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addBank").modal("hide");
    };
    
    //新增
    $scope.saveBank = function(){
    	var data = {
 			 	"sourceId" : $scope.creditCard.sourceId,
 				"orgCost":$scope.creditCard.orgCost,
 				"orgPushCost":$scope.creditCard.orgPushCost,
 				"bankBonus":$scope.creditCard.bankBonus,
 				"isOnlyone":$scope.creditCard.isOnlyone
 		};
 	 $http.post("superBank/addCredtiCardConf",angular.toJson(data))
 		.success(function(data){
 			if(data.status){
 				alert("新增成功");
 				$scope.resetForm();
 				$("#addBank").modal("hide");
 				$scope.query();
 			}else{
 				$scope.notice(data.msg);
 				$scope.submitting = false;
 			}
 		});
    };
    
    
    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});
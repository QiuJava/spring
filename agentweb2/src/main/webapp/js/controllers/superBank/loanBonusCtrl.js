/**
 * 贷款总奖金配置
 */
angular.module('inspinia').controller('loanBonusCtrl',function($scope,$http,i18nService,$document,$window){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.resetForm = function () {
        $scope.conf = {sourceId:'-1',orgId:'-1'};
    };
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'orgId',displayName: 'ID',width: 80,pinnable: false,sortable: false},
        {field: 'orgName',displayName: '组织',width: 150,pinnable: false,sortable: false},
        {field: 'companyName',displayName: '贷款机构',width: 150,pinnable: false,sortable: false},
        {field: 'loanBonus',displayName: '贷款机构给品牌总奖金扣率－放贷',width: 230,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.loanBonus}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.loanBonus"/>%</div>'},
        {field: 'registerBonus',displayName: '贷款机构给品牌总奖金－注册',width: 230,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.registerBonus}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="text-align:center;width:50px;" ng-model="row.entity.registerBonus"/></div>'},
//        {field: 'orgCostLoan',displayName: '品牌成本-放贷',width: 160,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.orgCostLoan}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.orgCostLoan"/>%</div>'},
//        {field: 'orgCostReg',displayName: '品牌成本-注册',width: 160,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.orgCostReg}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="text-align:center;width:50px;" ng-model="row.entity.orgCostReg"/></div>'},
        {field: 'orgPushLoan',displayName: '品牌发放总奖金扣率-放贷',width: 200,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">N*{{row.entity.orgPushLoan}}%</div><div class="lh30" ng-show="row.entity.action==2">N*<input style="text-align:center;width:50px;" ng-model="row.entity.orgPushLoan"/>%</div>'},
        {field: 'orgPushReg',displayName: '品牌发放总奖金-注册',width: 180,cellTemplate:'<div class="lh30" ng-show="row.entity.action!=2">{{row.entity.orgPushReg}}</div><div class="lh30" ng-show="row.entity.action==2"><input style="text-align:center;width:50px;" ng-model="row.entity.orgPushReg"/></div>'},
//        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,
//        	cellTemplate:
//        	'<div  class="lh30" ng-show="row.entity.action!=2"><input type="hidden" ng-model="row.entity.sourceId"/><input type="hidden" ng-model="row.entity.id"/><input type="hidden" ng-model="row.entity.action" /><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')"  ng-click="grid.appScope.creditCardConfEdit(row.entity)">编辑</a></div>'
//        	+
//        	'<div  class="lh30" ng-show="row.entity.action==2"><a ng-show="grid.appScope.hasPermit(\'activity.editActivityHardWare\')" ng-click="grid.appScope.creditCardUpd(row.entity)">保存</a></div>'
//        }
    ];
    
    $scope.loanBonusGrid = {
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

    $scope.loanCompanies = [];
    $scope.companiesInAdd = [];
    $scope.orgInfoList = [];
    
    $scope.changeOps = function (sourceId) {
    	angular.forEach($scope.companiesInAdd, function(item){
			 if(item.id==sourceId){
				 $scope.loan.registerBonus = item.registerBonus;
				 $scope.loan.loanBonus = item.loanBonus;
	     	}
	     });
   };
    
    
    //获取机构列表
    $scope.getLoanCompanies = function(){
    	 $http({
             url:"superBank/getLoanList",
             method:"POST"
         }).success(function(msg){
             if(msg.status){
                 $scope.loanCompanies = msg.data;
                 $scope.companiesInAdd = msg.data.concat();
                 $scope.loan = {sourceId:msg.data[0].id,registerBonus:msg.data[0].registerBonus,loanBonus:msg.data[0].loanBonus};
                 $scope.loanCompanies.unshift({id:'-1',companyName:"全部"});
             }
         }).error(function(){
             $scope.notice("获取机构列表异常");
         });
    };
    $scope.getLoanCompanies();
    
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
    				"loanBonus":entity.loanBonus,
    				"registerBonus":entity.registerBonus,
    				"orgCostLoan":entity.orgCostLoan,
    				"orgCostReg":entity.orgCostReg,
    				"orgPushLoan":entity.orgPushLoan,
    				"orgPushReg":entity.orgPushReg
    		};
    	 $http.post("superBank/updateLoanConf",angular.toJson(data))
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
            url: 'superBank/loanConfList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.conf,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
                $scope.notice(msg.msg);
                return;
            }
            $scope.loanBonusGrid.data = msg.data.result;
            $scope.loanBonusGrid.totalItems = msg.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addLoan = function(){
    	$("#addLoan").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addLoan").modal("hide");
    };
    
   
    
    //新增
    $scope.saveLoanCompany = function(){
    	 var data = {
 			 	"sourceId" : $scope.loan.sourceId,
 				"loanBonus":$scope.loan.loanBonus,
 				"registerBonus":$scope.loan.registerBonus,
 				"orgCostLoan":$scope.loan.orgCostLoan,
 				"orgCostReg":$scope.loan.orgCostReg,
 				"orgPushLoan":$scope.loan.orgPushLoan,
 				"orgPushReg":$scope.loan.orgPushReg
 		};
 	 $http.post("superBank/addLoanCompany",angular.toJson(data))
 		.success(function(data){
 			if(data.status){
 				//$scope.notice(data.msg);
 				alert("新增成功");
 				$scope.cancel();
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
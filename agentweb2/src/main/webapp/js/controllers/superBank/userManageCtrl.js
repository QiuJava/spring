/**
 * 用户管理
 */

angular.module('inspinia').controller('userManageCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$filter,i18nService, SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.payBacks=[{text:"全部",value:""},{text:"已退款",value:"1"},{text:"未退款",value:"0"}];
	$scope.isPayList=[{text:"全部",value:""},{text:"已缴费",value:"1"},{text:"未缴费",value:"0"}];
	$scope.info = {userId:'',phone:"",orgId:"",userCode:"",userType:"",topOneCode:"",topTwoCode:"",topThreeCode:"",receiveUserNo:"",repaymentUserNo:"",
			payBack:"",hasAccount:"",userName:"",isPay:"",
			startTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
			endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
			startPayDate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
			endPayDate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
			};

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setBeginTime=function(setTime){
		$scope.info.startTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
		$scope.info.startPayDate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

	}

	setEndTime=function(setTime){
		$scope.info.endTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
		$scope.info.endPayDate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

	}
	//清空
	$scope.clear=function(){
        $scope.selected = "";
        $scope.selected2 = "";
        $scope.selected3 = "";
		$scope.info={userCode:'',phone:"",orgId:"",userCode:"",userType:"",topOneCode:"",topTwoCode:"",topThreeCode:"",receiveUserNo:"",repaymentUserNo:"",
				startTime:"",endTime:"",payBack:"",hasAccount:"",userName:"",isPay:"",
				/*startTime:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
				endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'*/
				};
	}
	
	$scope.entityAgentLevel = $rootScope.entityAgentLevel;
	$scope.isAgent = $rootScope.isAgent;
	//初始化,条件查询
	$scope.ordinaryUserNum = 0;
    $scope.commissionerUserNum = 0; 
    $scope.managerUserNum = 0; 
    $scope.bankerUserNum = 0; 
    $scope.agentUserNum = 0;
	$scope.selectInfo=function(){
		$scope.submitting = true;
        $scope.loadImg = true;
        if ($scope.selected != undefined && $scope.selected != null) {
            $scope.info.openProvince = $scope.selected.name;
            if ($scope.selected2 != undefined && $scope.selected2 != null) {
                $scope.info.openCity = $scope.selected2.name;
                if ($scope.selected3 != undefined && $scope.selected3 != null) {
                    $scope.info.openRegion = $scope.selected3.name;
                } else {
                    $scope.info.openRegion = "";
                }
            } else {
                $scope.info.openCity = "";
                $scope.info.openRegion = "";
            }
        } else {
            $scope.info.openProvince = "";
            $scope.info.openCity = "";
            $scope.info.openRegion = "";
        }
        $http.post('superBank/selectTotal.do',
        		"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(result){
			$scope.ordinaryUserNum = result.ordinaryUserNum;
			$scope.commissionerUserNum = result.commissionerUserNum;
			$scope.managerUserNum = result.managerUserNum;
			$scope.bankerUserNum = result.bankerUserNum;
			$scope.agentUserNum = result.commissionerUserNum + result.managerUserNum + result.bankerUserNum;
			$http.post('superBank/selectByCondions.do',
					 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
					 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(result){
				$scope.submitting = false;
	            $scope.loadImg = false;
				if(!result){
					$scope.notice("数据为空");
					return;
				}
				$scope.gridOptions.data = result.result;
	            $scope.gridOptions.totalItems = result.totalCount;
			}).error(function () {
	            $scope.submitting = false;
	            $scope.loadImg = false;
	            $scope.notice("系统异常，请稍后再试");
	        });
		});
	}
	
	// 导出
	$scope.exportUserInfoSuperBank = function () {
        if ($scope.selected != undefined && $scope.selected != null) {
            $scope.info.openProvince = $scope.selected.name;
            if ($scope.selected2 != undefined && $scope.selected2 != null) {
                $scope.info.openCity = $scope.selected2.name;
                if ($scope.selected3 != undefined && $scope.selected3 != null) {
                    $scope.info.openRegion = $scope.selected3.name;
                } else {
                    $scope.info.openRegion = "";
                }
            } else {
                $scope.info.openCity = "";
                $scope.info.openRegion = "";
            }
        } else {
            $scope.info.openProvince = "";
            $scope.info.openCity = "";
            $scope.info.openRegion = "";
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
                location.href="superBank/exportUserInfoSuperBank?info="+angular.toJson($scope.info);
            }
        });
    };
    
     // $scope.selectInfo();
	 $scope.gridOptions={                           //配置表格
		      paginationPageSize:10,                  //分页数量
		      paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		      useExternalPagination: true,  
		      columnDefs:[                           //表格数据
		         { field: 'userCode',displayName:'用户ID',width:100},
		         { field: 'userName',displayName:'用户姓名',width:100},
		         { field: 'orgName',displayName:'所属组织',width:100 },
		         { field: 'secondUserNode',displayName:'二级代理节点',width:120 },
		         { field: 'nickName',displayName:'昵称',width:120 },
		         { field: 'phone',displayName:'手机号',width:150 },
		         { field: 'weixinCode',displayName:'微信号',width:150 },
		         { field: 'totalAmount',displayName:'总收益',width:150 },
		         { field: 'userType',displayName:'代理身份',width:150,
		        	 cellFilter:"formatDropping:[{text:'普通用户',value:10},{text:'专员',value:20},{text:'经理',value:30},{text:'银行家',value:40}]"
		         },
//		         { field: 'payBack',displayName:'是否退款',width:100,
//		        	 cellFilter:"formatDropping:[{text:'已退款',value:'1'},{text:'未退款',value:'0'}]"
//		         },
		         { field: 'hasAccount',displayName:'是否开户',width:100,
		        	 cellFilter:"formatDropping:[{text:'是',value:'1'},{text:'否',value:'0'}]"
		         },
		         { field: 'createDate',displayName:'入驻时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"' },
		         { field: 'repaymentUserNo',displayName:'超级还用户编号',width:150 },
		         { field: 'receiveUserNo',displayName:'收款商户编号',width:150 },
		         { field: 'topOneCode',displayName:'一级代理ID',width:150 },
		         { field: 'topTwoCode',displayName:'二级代理ID',width:150 },
		         { field: 'topThreeCode',displayName:'三级代理ID',width:150 },
		         { field: 'openProvince',displayName:'省',width:150 },
		         { field: 'openCity',displayName:'市',width:150 },
		         { field: 'openRegion',displayName:'区',width:150 },
		         { field: 'remark',displayName:'备注',width:150 },
		         { field: 'action',displayName:'操作',width:200,pinnedRight:true,sortable: false,editable:true,
		        	 cellTemplate:
		        		 '<a ng-show="grid.appScope.hasPermit(\'superBank.userInfoSuperBank\')" ui-sref="superBank.userInfoSuperBank({userId:row.entity.userId,userCode:row.entity.userCode})">详情 </a>'
		        		 +'<a ng-show="grid.appScope.hasPermit(\'superBank.openTwoAgent\') && row.entity.isAgent==1 && row.entity.statusAgent==1 && row.entity.topOneCode && !row.entity.topTwoCode" ng-click="grid.appScope.openTwoAgent(row.entity.phone,row.entity.orgId,row.entity.secondUserNode,row.entity.userName)">| 开二级代理后台</a>'
		         }
		      ],
			  onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectInfo();
		          });
		      }
		};
	 
	 //查询所有银行家组织
	 $scope.orgInfoList = [];
	 $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:"",orgName:"全部"});
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    
    //显示隐藏所有条件
	$scope.showConditions = function(){
		if (!$scope.show) {
			$scope.show = 1;
		}else{
			$scope.show = 0;
		}
	}
    $scope.getOrgInfoList();
    
    //开二级代理后台
    $scope.openTwoAgent=function(phone,orgId,secondUserNode,userName){
    	var data={"phone":phone,"secondUserNode":secondUserNode,"orgId":orgId,"userName":userName};
    	$http.post("superBank/openTwoAgent",
				"info="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(datas){
					$scope.notice(datas.msg);
				});
    	
    }

    //省市区
    $scope.list = LAreaDataBaidu;
    $scope.c = function () {
        $scope.selected2 = "";
        $scope.selected3 = "";
    };

    $scope.c2 = function () {
        $scope.selected3 = "";
    };



});
/**
 * 商户详情查看
 */

angular.module('inspinia').controller('merchantDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
	
	//数据源
	$scope.businessTypes=[];
	$scope.rates=[];
	$scope.quotas=[];
	$scope.info={};
	$scope.mbp=[];
	$scope.merExa=[];
	$scope.listMri=[];
	$scope.listMris=[];
	$scope.serviceStatus=[];
	$scope.terminal=[];
	$http.post('merchantBusinessProduct/selectDetailInfo.do?ids='+$stateParams.mertId)
	.success(function(data) {
		if(!data.success){
			$scope.notice(data.message);
			return;
		}
		var largeLoad = data.data;
		if(largeLoad.bols){
			if(largeLoad.listmr==null||largeLoad.listmsq==null||largeLoad.serviceMgr==null||largeLoad.mi==null||largeLoad.mbp==null||largeLoad.listel==null||largeLoad.listmri==null){
				$scope.notice("数据为空");
				return;
			}else{
				$scope.rates=largeLoad.listmr;
				$scope.quotas=largeLoad.listmsq;
				$scope.info=largeLoad.mi;
				$scope.terminal=largeLoad.tis;
				if($scope.info.address==""){
					$scope.info.address=null;
				}
				$scope.mbp=largeLoad.mbp;
				$scope.merExa=largeLoad.listel;
				$scope.listMri=largeLoad.listmri;
				$scope.listMris=largeLoad.listmris;
				$scope.serviceStatus=largeLoad.serviceMgr;
			}
		}else{
			$scope.notice(largeLoad.msg);
		}
	});
	$scope.merchantTypes=function(data){
		if(data==1){
			return '个人';
		}
		if(data==2){
			return '个体商户';
		}
		if(data==3){
			return '企业商户';
		}
	};
	
	$scope.accountTypes=function(data){
		if(data==1){
			return '对公';
		}
		if(data==2){
			return '对私';
		}
	}
	
	$scope.rateTypes=[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"}
	,{text:"单笔阶梯扣率",value:"5"}]
	$scope.merchantRateList={
		data:'rates',
		columnDefs:[
		    {field:'serviceName',displayName:'服务名称',width:120,pinnable:false,sortable:false}, 
		    {field:'cardType',displayName:'银行卡种类',width:120,pinnable:false,sortable:false,
		    	cellFilter: "formatDropping:cardType"
		    },
		    {field:'holidaysMark',displayName:'节假日标志',width:80,pinnable:false,sortable:false,
		    	cellFilter: "formatDropping:holidays"
		    },
		    {field:'rateType',displayName:'费率方式',width:220,pinnable:false,sortable:false,
		    	cellFilter: "formatDropping:rateTypes"
		    },
		    {field:'oneRate',displayName:'一级代理商管控费率',width:260,pinnable:false,sortable:false},
		    {field:'merRate',displayName:'商户费率',width:260,pinnable:false,sortable:false}
//		    {field:'fixedMark',displayName:'固定标志',width:120,pinnable:false,sortable:false,
//		    	cellFilter: "formatDropping:fixedMark"
//		    }
		]
	};
	
	$scope.merchantQuotaList={
		data:'quotas',
		columnDefs:[
            {field:'serviceName',displayName:'服务名称',width:120,pinnable:false,sortable:false}, 
            {field:'cardType',displayName:'银行卡种类',width:120,pinnable:false,sortable:false,
            	cellFilter: "formatDropping:cardType"
            }, 
            {field:'holidaysMark',displayName:'节假日标志',width:80,pinnable:false,sortable:false,
            	cellFilter: "formatDropping:holidays"
            }, 
            {field:'singleDayAmount',displayName:'单日最大交易金额(元）',width:200,pinnable:false,sortable:false,cellFilter:"currency"}, 
            {field:'singleMinAmount',displayName:'单笔最小交易额（元）',width:200,pinnable:false,sortable:false,cellFilter:"currency"}, 
            {field:'singleCountAmount',displayName:'单笔最大交易额（元）',width:200,pinnable:false,sortable:false,cellFilter:"currency"}, 
            {field:'singleDaycardAmount',displayName:'单日单卡最大交易额（元）',width:200,pinnable:false,sortable:false,cellFilter:"currency"}, 
            {field:'singleDaycardCount',displayName:'单日单卡最大交易笔数（笔）',width:200,pinnable:false,sortable:false}
//            {field:'fixedMark',displayName:'固定标志',width:120,pinnable:false,sortable:false,
//            	cellFilter: "formatDropping:fixedMark"
//            }
		]
	};
	
	$scope.serviceStatusMgr={
		data:'serviceStatus',
		columnDefs:[
		       {field:'serviceName',displayName:'服务名称',width:200,pinnable:false,sortable:false},
	           {field:'status',displayName:'服务状态',width:200,pinnable:false,sortable:false,
		    	   cellFilter: "formatDropping:[{text:'关闭',value:'0'},{text:'开通',value:'1'}]"
	           }, 
		]
	};
	
	$scope.merchantRecords={
		data:'merExa',
		columnDefs:[
	           {field:'openStatus',displayName:'状态',width:120,pinnable:false,sortable:false,
	        	   cellFilter: "formatDropping:[{text:'审核失败',value:'2'},{text:'审核成功',value:'1'}]"
	           },
	           {field:'examinationOpinions',displayName:'内容',width:200,pinnable:false,sortable:false}, 
	           {field:'createTime',displayName:'时间',width:200,pinnable:false,sortable:false,
	        	   cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
	           }, 
	           {field:'realName',displayName:'操作员',width:200,pinnable:false,sortable:false}
			]
	};
	
	$scope.merchantDetailed={
			data:'listMri',
			columnDefs:[
			       {field:'itemName',displayName:'进件要求项名称',width:200,pinnable:false,sortable:false}, 
			       {field:'content',displayName:'资料',width:200,pinnable:false,sortable:false,
			    	   cellTemplate:'<div ng-switch on="$eval(\'row.entity.exampleType\')">'
						+'<div ng-switch-when="2">'
							+'<div>'
								+'<a ui-sref="{{COL_FIELD}}">{{$eval(\'row.entity.itemName\')}} 附件下载</a>'
							+'</div>'
						+'</div>'
						+'<div ng-switch-when="3">'
							+'<div>{{COL_FIELD}}</div>'
						+'</div>'
						+'</div>'
			       },     
		           {field:'status',displayName:'状态',width:120,pinnable:false,sortable:false,
			    	   cellFilter: "formatDropping:[{text:'待审核',value:0},{text:'审核通过',value:1},{text:'审核失败',value:2}]"
		           },
		           {field:'auditTime',displayName:'审核通过时间',width:200,pinnable:false,sortable:false,
		        	   cellFilter: "date:'yyyy-MM-dd'"
		           }
				]
	};
	
	
	//查看进件资料
	$scope.updateMriInfo=function(pp,id,status,content,name){
			var modalScope = $scope.$new();
			 modalScope.id=id;
			 modalScope.pp=1;
			 if(pp==1){
				 modalScope.dd=0; 
			 }else{
				 modalScope.dd=1;
			 }
			 modalScope.type=1;
			 modalScope.name=name;
			 modalScope.img=2;
			 modalScope.content=content;
			 var modalInstance = $uibModal.open({
	            templateUrl : 'views/merchant/merchantUpdateModal.html',  //指向上面创建的视图
	            controller : 'merchantModalCtrl222',// 初始化模态范围
	            scope:modalScope,
	            size:'lg'
	        })
	        modalScope.modalInstance=modalInstance;
	        modalInstance.result.then(function(selectedItem){  
	        },function(){
	            $log.info('取消: ' + new Date())
	        })
		
	}
	
});

angular.module('inspinia').controller('merchantModalCtrl222',function($scope,$stateParams,$http){
	 $scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
});
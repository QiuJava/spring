/**
 * 机具申请查询
 */
angular.module('inspinia').controller('termianlApplyQueryCtrl',function($scope,$state,$filter,$log,$http,$stateParams,$compile,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

	$scope.info={status:"",merAccount:"",isBindParam:""};
	$scope.statuss=[{text:"全部",value:""},{text:"已处理",value:1},{text:"待直属处理",value:0},{text:"待一级处理",value:2}];
	$scope.merAccounts=[{text:"全部",value:""},{text:"是",value:1},{text:"否",value:0}];
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

    // $scope.agentList = [];
    // //代理商
    // $http.post("agentInfo/selectAllInfo")
    //     .success(function(msg){
    //         //响应成功
    //         for(var i=0; i<msg.length; i++){
    //             $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentName});
    //         }
    //     });
	//查询
	$scope.selectInfo=function(){
		if($scope.info.sTime>$scope.info.eTime){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		$http({
			url: 'terminalApplyAction/selectAllInfo?pageNo='+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			method: 'POST',
			data: $scope.info
		}).success(function(result){
            if(!result.success){
                return;
            }
            //响应成功]
            $scope.gridOptions.data = result.data;
            $scope.gridOptions.totalItems = result.count;
        });
	};
	//清空
	$scope.clear=function(){
		$scope.info={merAccount:"",status:"",mobilephone:"",agentName:"",isBindParam:""};
	}
	$scope.selectInfo();
	 $scope.gridOptions={                           //配置表格
		      paginationPageSize:10,                  //分页数量
		      paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		      useExternalPagination: true,
			  enableHorizontalScrollbar: 1,        //横向滚动条
			  enableVerticalScrollbar : 1,  		//纵向滚动条
		      columnDefs:[                           //表格数据
		         { field: 'merchantNo',displayName:'商户编号',width:150},
		         { field: 'merchantName',displayName:'商户名称' ,width:150},
		         { field: 'mobilephone',displayName:'商户手机号',width:150 },
		         { field: 'agentName',displayName:'代理商名称',width:150 },
		         { field: 'status',displayName:'状态',width:150,
		        	 cellFilter:"formatDropping:" + angular.toJson($scope.statuss)
		         },
                  { field: 'sn',displayName:'机具SN号',width:150 },
                  { field: 'bind',displayName:'是否绑定机具',width:150,
                      cellTemplate: '<span >{{row.entity.bind ? "是" : "否"}}</span>'
                  },
				  {field: 'bind',displayName:'是否强制下发机具',width:150,
                      cellTemplate: '<span >{{row.entity.sn ? "是" : "否"}}</span>'
                  },
		         { field: 'createTime',displayName:'申请时间',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'
		         },
		         { field: 'id',displayName:'操作',width:230,pinnedRight:true,cellTemplate:
		        	 '<a class="lh30" ui-sref="merchant.termianlApplyDetail({id:row.entity.id})">详情</a>' +
                     '<span ng-show="(row.entity.status == \'0\' && $root.entityId == row.entity.agentNo) || (row.entity.status == \'2\' && $root.entityId == row.entity.oneLevelId)">' +
					 '<a class="lh30" ui-sref="merchant.dealWithTermianlApply({id:row.entity.id})"> | 处理</a>' +
					 '</span>'

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
	
})

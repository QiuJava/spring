/*
* 黑名单处理
* */
angular.module('inspinia').controller('blackHandleQueryCtrl',function($scope,$rootScope,$state,$filter,$log,$http,$stateParams,$compile,$uibModal,i18nService,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //下拉框数据源
    $scope.merLastDealStatuses=[{text:"全部",value:-1},{text:"未处理",value:1},{text:"已处理",value:2},{text:"已解冻",value:3}];
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setCreateTimeStart=function(setTime){
        $scope.info.createTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setCreateTimeEnd=function(setTime){
        $scope.info.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    //清空
    $scope.resetForm=function(){
        $scope.info={merLastDealStatus:-1,merchantNo:"",merchantName:"",agentNo:"",agentName:""};
    };
    $scope.resetForm();
    $scope.tdata=[];
    $scope.blackHandleGrid={                           //配置表格
        data:'tdata',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'orderNo',displayName:'订单编号',width:150,visible:false},
            { field: 'merchantNo',displayName:'商户编号',width:150},
            { field: 'merchantName',displayName:'商户名称',width:150 },
            { field: 'agentNo',displayName:'所属代理商编号',width:150 },
            { field: 'agentName',displayName:'所属代理商名称',width:150 },
            /*商户触犯风控规则，已列入黑名单*/
            { field: 'blackCreateRemark',displayName:'内容',width:150
               /* cellFilter:"formatDropping:[{text:'商户触犯风控规则，已列入黑名单'},{text:'商户触犯风控规则，已列入黑名单',value:''}]"*/},
            { field: 'merLastDealStatus',displayName:'状态',width:150,
                cellFilter:"formatDropping:[{text:'未处理',value:0},{text:'未处理',value:1},{text:'已处理',value:2},{text:'已解冻',value:3}]"
            },
            { field: 'createTime',displayName:'创建日期',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"' },
            { field: 'merLastDealTime',displayName:'最后处理日期',width:150,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"' },
            { field: 'riskLastDealTime',displayName:'平台最后回复日期',width:150 ,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            { field: 'id',displayName:'操作',pinnedRight:true,width:240,
                cellTemplate:
                    '<div class="lh30"  >'
                    +'<a ng-show="grid.appScope.hasPermit(\'riskControlManagement.blackHandle\') && row.entity.merLastDealStatus <= 1 && !row.entity.firstRecord;" ui-sref="riskControlManagement.blackHandle({orderNo:row.entity.orderNo})">处理</a>'
                    +'<a ng-show=" grid.appScope.hasPermit(\'riskControlManagement.blackHandle\') && row.entity.merLastDealStatus <= 1 && row.entity.firstRecord;" ui-sref="riskControlManagement.blackHandleDetail({orderNo:row.entity.orderNo})">处理</a>'
                    +'<a ng-show=" grid.appScope.hasPermit(\'riskControlManagement.blackHandle\') && row.entity.merLastDealStatus >1" ui-sref="riskControlManagement.allBlackHandleDetail({orderNo:row.entity.orderNo})">详情</a>'
                    +'</div>'
            }
        ],

        onRegisterApi: function(gridApi) {                //选中行配置
            $scope.gridApi = gridApi;
            //全选
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                if(rows[0].isSelected){
                    $scope.testRow = rows[0].entity;
                    for(var i=0;i<rows.length;i++){
                        rowList[rows[i].entity.id]=rows[i].entity;
                    }
                }else{
                    rowList={};
                }
            })
            //单选
            $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
                if(row.isSelected){
                    $scope.testRow = row.entity;
                    rowList[row.entity.id]=row.entity;
                }else{
                    delete rowList[row.entity.id];
                }
            })
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.selectInfo();
                rowList={};
            });
        }
    };
    //模糊条件查询
    $scope.selectInfo=function(){
        $http.post("riskHandle/blackDataQuery",
            "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
           {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(result){
                //响应成功
                if(!result.result){
                    $scope.notice(result.msg)
                    return;
                }

                $scope.tdata = result.list.result;
                $scope.blackHandleGrid.totalItems = result.list.totalCount;

            });
    }

})
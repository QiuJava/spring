/**
 * 排行榜查询
 */
angular.module('inspinia').controller('rankingRecordCtrl',function($scope,$rootScope,$http,$state,$stateParams,i18nService,$document,SweetAlert){
    //中文
    i18nService.setCurrentLang('zh-cn');

    $scope.paginationOptions = {pageNo : 1,pageSize : 10};
    $scope.rankingTypes = [{text:"全部",value:""},{text:"周榜",value:"0"},{text:"月榜",value:"1"},{text:"年榜",value:"2"}];
    $scope.statusList = [{text:"全部",value:""},{text:"未发放",value:"1"},{text:"已发放",value:"2"}];
    $scope.rankingTypeStr='[{text:"周榜",value:"0"},{text:"月榜",value:"1"},{text:"年榜",value:"2"}]';
    $scope.statusStr = '[{text:"未生成",value:"0"},{text:"未发放",value:"1"},{text:"已发放",value:"2"}]';
    $scope.dataTypes = [{value:"",text:"全部"},{value:"0",text:"收益金额"},{value:"1",text:"会员数"},{value:"2",text:"用户数"}];
    $scope.dataTypesStr = '[{value:"",text:"全部"},{value:"0",text:"收益金额"},{value:"1",text:"会员数"},{value:"2",text:"用户数"}]';
    
    $scope.resetForm = function () {
        $scope.baseInfo = {rankingNo:"",rankingType:"",status:"",
            rankingName:"",ruleNo:"",dataType:""
        };
    }
    $scope.resetForm();

    $scope.columnDefs = [
        {field: 'rankingNo',displayName: '排行榜编号',width: 150,pinnable: false,sortable: false},
        {field: 'batchNo',displayName: '期号',width: 150,pinnable: false,sortable: false},
        {field: 'ruleNo',displayName: '排行榜规则编号',width: 150,pinnable: false,sortable: false},
        {field: 'rankingName',displayName: '排行榜名称',width: 150,pinnable: false,sortable: false},
        {field: 'rankingType',displayName: '统计周期',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.rankingTypeStr},
        {field: 'dataType',displayName: '统计数据',width: 150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.dataTypesStr},
        {field: 'orgName',displayName: '所属组织',width: 150,pinnable: false,sortable: false},
        {field: 'pushNum',displayName: '本期获奖人数',width: 170,pinnable: false,sortable: false},
        {field: 'pushTotalAmount',displayName: '本期奖金',width: 150,cellFilter:"currency:''",pinnable: false,sortable: false},
        {field: 'pushRealNum',displayName: '实发人数',width: 150,pinnable: false,sortable: false},
        {field: 'pushRealAmount',displayName: '实发奖金',width: 170,cellFilter:"currency:''",pinnable: false,sortable: false},
        {field: 'status',displayName: '榜单状态',width: 150,pinnable:false,sortable: false,cellFilter:"formatDropping:" + $scope.statusStr},
        {field: 'createDate',displayName: '榜单生成时间',width: 150,pinnable:false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'startDate',displayName: '统计开始时间',width: 150,pinnable:false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'endDate',displayName: '统计截止时间',width: 150,pinnable:false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                '<a class="lh30"  ui-sref="superBank.rankingRecordDetail({id:row.entity.id})">详情</a>'}    ];

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

    /*查询*/

    $scope.rankingQuerying = false;
    $scope.query = function () {
        if($scope.rankingQuerying){
            $scope.notice("正在查询数据中,请稍后...");
            return;
        }

        $scope.rankingQuerying = true;
        $http({
            url: 'superBank/selectRankingRecord?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            $scope.rankingQuerying = false;
            $scope.myGrid.data = msg.data.page.result;
            $scope.peopleCount=0;
            $scope.totalMoneyCount=0;
            if(msg.data.sum!=null) {
                $scope.peopleCount = msg.data.sum.pushRealNumCount;
                $scope.totalMoneyCount = msg.data.sum.pushRealAmountCount;
            }
            $scope.myGrid.totalItems = msg.data.page.totalCount;
        }).error(function (msg) {
            $scope.rankingQuerying = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };





});

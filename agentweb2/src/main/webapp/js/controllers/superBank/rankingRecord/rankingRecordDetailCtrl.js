/**
 * 排行榜详情
 */
angular.module('inspinia').controller('rankingRecordDetailCtrl',function($scope,$http,$stateParams,i18nService,$document){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusType =[{text:"未生成",value:"0"},{text:"未发放",value:"1"},{text:"已发放",value:"2"}];
    $scope.rankingTypes =[{text:"周榜",value:"0"},{text:"月榜",value:"1"},{text:"年榜",value:"2"}];


    $scope.columnDefs = [
        {field: 'rankingIndex',displayName: '排名',width: 100,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.rankingIndex}}</div>'},
        {field: 'userName',displayName: '用户姓名',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.userName}}</div>'},
        {field: 'deNickName',displayName: '微信昵称',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.deNickName}}</div>'},
        {field: 'userCode',displayName: '用户ID',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.userCode}}</div>'},
        {field: 'userTotalAmount',displayName: '统计总额',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.userTotalAmount}}</div>'},
        {field: 'isRank',displayName: '是否获奖',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.isRank|formatDropping: [{text:"否",value:"0"},{text:"是",value:"1"}]}}</div>'},
        {field: 'rankingLevel',displayName: '获奖等级',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.rankingLevel}}</div>'},
        {field: 'rankingAmount',displayName: '获奖金额',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.rankingAmount | currency:""}}</div>'},
        {field: 'status',displayName: '用户发放状态',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.status|formatDropping:[{text:"未发放",value:"0"},{text:"已发放",value:"1"},{text:"已移除",value:"2"}]}}</div>'},
        {field: 'removeTime',displayName: '移除时间',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.removeTime | date:"yyyy-MM-dd HH:mm:ss"}}</div>'},
        {field: 'pushTime',displayName: '发放时间',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.pushTime | date:"yyyy-MM-dd HH:mm:ss"}}</div>'},
        {field: 'remark',displayName: '说明',width: 150,pinnable: false,sortable: false,
            cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope" ng-style="{color:(row.entity.status == \'2\')?\'#c1c0c0\':\'#5a5a5a\'}">{{row.entity.remark}}</div>'}
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

    $http({
        url:"superBank/getRankingRecordById?recordId="+$stateParams.id,
        method:"GET"
    }).success(function(result){
        if (result.status){
            $scope.baseInfo = result.data;
        } else {
            $scope.notice(result.msg);
        }
    }).error(function () {
        $scope.notice("服务器异常，请稍后再试");
    });


    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'superBank/queryRankingRecordDetail?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize+'&recordId='+$stateParams.id,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                return;
            }

            $scope.peopleCount =result.data.peopleCount;
            $scope.userTotalAmountCount =result.data.userTotalAmountCount;

            $scope.myGrid.data = result.data.rankingRecordDetailInfoList;
            $scope.myGrid.totalItems = result.data.page.totalCount;
        }).error(function () {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   $scope.query();


    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});
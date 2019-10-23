angular.module('inspinia').controller("agentDetailCtrl", function ($scope, $http, $state, $stateParams, uiGridConstants, i18nService, $log, $uibModal, $compile) {
    $scope.merRateStr = '[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"}]';
    $scope.statusStr = '[{text:"正常",value:1},{text:"关闭",value:0}]';
    $scope.type = [{text: '个人', value: 1}, {text: '个体商户', value: 2}, {text: '企业商户', value: 3}];
    //活动类型
    if ($scope.oemType === 'ZHZFPAY') {
        $scope.subjectTypes = [{text: '全部', value: ''}, {text: '欢乐返', value: '009'}];
    } else {
        $scope.subjectTypes = [{text: '全部', value: ''}, {text: '欢乐返-循环送', value: '008'}, {text: '欢乐返', value: '009'}];
    }
    $scope.frStr = '[{text:"每笔固定收益金额",value:"1"},{text:"每笔收益率",value:"2"},{text:"每笔收益率带保底封顶",value:"3"},{text:"每笔收益率+每笔固定收益金额",value:"4"},' +
        '{text:"商户费率与代理商成本差额百分比分润",value:"5"},{text:"商户费率与代理商成本差额阶梯分润",value:"6"}]';
    $scope.businessProduct = {
        agentProducts: [],           // 代理商代理的业务产品信息
        agentTeamInfo: [],          // 代理商所代理业务产品的组织信息
        teamIds: [],                // 业务产品所拥有的组织id
        groupByTeamId: {},          // 所有业务产品--按组织id分组
        groupByTeamIdGridList:{},   // 所有业务产品--按组织id分组--表格
        gridColumnDefs: [
            {field: 'key1', displayName: '业务产品ID', width: 150},
            {field: 'key3', displayName: '业务产品名称', width: 150},
            {field: 'key5', displayName: '类型', width: 150, cellFilter: "formatDropping:" + angular.toJson($scope.type)},
            {field: 'key2', displayName: '状态', width: 150, cellFilter: "formatDropping:" + $scope.statusStr},
            {field: 'key6', displayName: '群组号', width: 200},
            {
                field: 'key7',
                displayName: '是否允许单独申请',
                width: 200,
                cellFilter: "formatDropping:" + angular.toJson($scope.bool)
            }
        ]
    };
    $scope.shareRule = {
        shareRuleData: [],              // 后台返回的所有分润成本数据
        agentTeamIds: [],               // 分润设置信息--组织id集合
        groupByTeamId: {},              // 分润设置按组织分类
        groupByTeamIdGridList:{},       // 分润设置按组织分类之后表格集合
        defaultGrid: {},
        gridColumnDefs: [
            {field: 'bpName', displayName: '业务产品名称', width: 150},
            {field: 'serviceName', displayName: '服务名称', width: 200},
            {
                field: 'serviceType', displayName: '服务种类', width: 150,
                cellTemplate:
                    '<div class="lh30" ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001"><span ng-bind="row.entity.serviceType | serviceTypeFilter"/></div>'
                    + '<div class="lh30" ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001"><span ng-bind="row.entity.serviceType2 | serviceTypeFilter"/>-提现</div>'
            },
            {field: 'cardType', displayName: '银行卡种类', width: 150, cellFilter: "formatDropping:" + $scope.cardTypeStr},
            {
                field: 'holidaysMark',
                displayName: '节假日标志',
                width: 150,
                cellFilter: "formatDropping:" + $scope.holidaysStr
            },
            {
                field: 'cost', displayName: '代理商成本', width: 150, cellTemplate:
                //如果不是体现服务，也就是交易服务，后面显示%
                    '<div ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001" style="width:98%;height:98%;"> '
                    + '{{row.entity[col.field]}}'
                    + '</div>'
                    //如果是体现服务，后面显示单位元
                    + '<div ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001" style="width:98%;height:98%;"> '
                    + '{{row.entity[col.field]}} <span ng-show="row.entity[col.field]">元</span>'
                    + '</div>'
            },
            {
                field: 'shareProfitPercent',
                displayName: '代理商固定分润百分比',
                width: 180,
                cellTemplate: '<span class="lh30" ng-show="row.entity.shareProfitPercent!=null">' +
                    '{{row.entity.shareProfitPercent}}%</span>'
            },
            // {field: 'ladderRate',displayName: '阶梯分润比例',width: 300},
            {field: 'efficientDate', displayName: '生效日期', width: 200, cellFilter: 'date:"yyyy-MM-dd"'},
            {field: 'efficientDate', displayName: '当前是否生效', width: 180, cellFilter: "compareDateFilter"}
        ]
    };
    $scope.happyBack = {
        showStatus: true,               // 展示欢乐返
        activityTypeNoAndTeamId: {},    // 欢乐返子类型和组织id的关系
        agentActivities: [],            // 当前新增代理商拥有的欢乐返子类型
        groupByTeamId: {},              // 按组织分类
        groupByTeamIdGridList:{},       // 按组织分类之后表格集合
        teamIds: [],                    // 子类型包含的所有teamIds
        defaultGrid: {},
        gridColumnDefs: [
            {field: 'activityTypeNo', displayName: '欢乐返子类型编号', width: 180},
            {field: 'activityTypeName', displayName: '欢乐返子类型名称', width: 150},
            {
                field: 'activityCode',
                displayName: '欢乐返类型',
                width: 150,
                cellFilter: "formatDropping:" + angular.toJson($scope.subjectTypes)
            },
            {field: 'transAmount', displayName: '交易金额', width: 150},
            {
                field: 'cashBackAmount', displayName: '返现金额', width: 150,
                cellTemplate:
                    '<div style="width:98%;height:98%;"> '
                    + '<span ng-bind="row.entity[col.field]" />'
                    + ' 元'
                    + '</div>'
            },
            {
                field: 'taxRate', displayName: '返现比例', width: 200,
                cellTemplate:
                    '<div style="width:98%;height:98%;"> '
                    + '<span ng-bind="row.entity[col.field]*100" />'
                    + ' %'
                    + '</div>'
            },
            {
                field: 'repeatRegisterAmount', displayName: '重复注册返现金额', width: 150,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.repeatRegisterAmount!=null"> '
                    + '<span ng-bind="row.entity[col.field]" />'
                    + ' 元'
                    + '</div>'
            },
            {
                field: 'repeatRegisterRatio', displayName: '重复注册返现比例', width: 200,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.repeatRegisterRatio!=null"> '
                    + '<span ng-bind="row.entity[col.field]*100" />'
                    + ' %'
                    + '</div>'
            },
            {
                field: 'fullPrizeAmount', displayName: '首次注册满奖金额', width: 150,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showFullPrizeAmount"> '
                    + '<span ng-bind="row.entity[col.field]" />'
                    + ' 元'
                    + '</div>'
            },
            {
                field: 'notFullDeductAmount', displayName: '首次注册不满扣金额', width: 150,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showNotFullDeductAmount"> '
                    + '<span ng-bind="row.entity[col.field]" />'
                    + ' 元'
                    + '</div>'
            },
            {
                field: 'repeatFullPrizeAmount', displayName: '重复注册满奖金额', width: 150,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showFullPrizeAmount"> '
                    + '<span ng-bind="row.entity[col.field]" />'
                    + ' 元'
                    + '</div>'
            },
            {
                field: 'repeatNotFullDeductAmount', displayName: '重复注册不满扣金额', width: 150,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showNotFullDeductAmount"> '
                    + '<span ng-bind="row.entity[col.field]" />'
                    + ' 元'
                    + '</div>'
            },
        ]
    };
    $scope.happyBack.defaultGrid = {
        data: [],
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        columnDefs: $scope.happyBack.gridColumnDefs,
    };
    /**
     * 生成业务产品表格
     */
    $scope.generateBusinessProductGrid = function (agentProducts) {
        $scope.businessProduct.teamIds = [];
        let agentBpData = agentProducts || [];
        $scope.businessProduct.agentProducts = agentProducts;

        agentBpData.forEach(item => {
            if (!$scope.businessProduct.groupByTeamId[item.teamId]) {
                $scope.businessProduct.groupByTeamId[item.teamId] = [];
            }
            $scope.businessProduct.groupByTeamId[item.teamId].push(item);
        });
        angular.forEach($scope.businessProduct.groupByTeamId, (item, teamId) => {
            $scope.businessProduct.teamIds.push(teamId);
            $scope.businessProduct.groupByTeamIdGridList[teamId] = {
                data: item,
                columnDefs: $scope.businessProduct.gridColumnDefs
            };
        });
        $scope.businessProduct.teamIds.sort();
    };
    /**
     * 生成分润成本表格
     * @param shareRules
     */
    $scope.generateShareRuleGrid = function (shareRules) {
        shareRules = shareRules || [];
        $scope.shareRule.shareRuleData = shareRules;

        $scope.shareRule.agentTeamIds = [];
        $scope.shareRule.groupByTeamId = {};
        $scope.shareRule.groupByTeamIdGridList = {};
        shareRules.forEach(item => {
            if (!$scope.shareRule.groupByTeamId[item.teamId]) {
                $scope.shareRule.groupByTeamId[item.teamId] = [];
            }
            $scope.shareRule.groupByTeamId[item.teamId].push(item);
        });
        angular.forEach($scope.shareRule.groupByTeamId, (item, teamId) => {
            $scope.shareRule.agentTeamIds.push(teamId);
            $scope.shareRule.groupByTeamIdGridList[teamId] = {
                data: item,
                columnDefs: $scope.shareRule.gridColumnDefs
            };
        });
        $scope.shareRule.agentTeamIds.sort();
        $scope.shareRule.defaultGrid = {
            data: [],
            columnDefs: $scope.shareRule.gridColumnDefs
        }
    };

    /**
     * 生成欢乐返表格
     * @param happyBackList
     */
    $scope.generateHappyBackGrid = function (data) {
        $scope.happyBack.agentActivities = (data && data.happyBackList) || [];
        $scope.happyBack.activityTypeNoAndTeamId = (data && data.activityTypeNoAndTeamId) || {};
        $scope.happyBack.showStatus = $scope.happyBack.agentActivities.length !== 0;
        if ($scope.happyBack.agentActivities.length === 0){
            return;
        }
        let agentActivity = $scope.happyBack.agentActivities[0];
        if (!agentActivity.showFullPrizeAmount && agentActivity.showNotFullDeductAmount) {
            $scope.happyBack.gridColumnDefs.splice(8, 1);
            $scope.happyBack.gridColumnDefs.splice(9, 1);
        } else if (!agentActivity.showNotFullDeductAmount && agentActivity.showFullPrizeAmount) {
            $scope.happyBack.gridColumnDefs.splice(9, 1);
            $scope.happyBack.gridColumnDefs.splice(10, 1);
        } else if (!agentActivity.showFullPrizeAmount && !agentActivity.showNotFullDeductAmount) {
            $scope.happyBack.gridColumnDefs.splice(8, 4);
        }
        $scope.happyBack.groupByTeamId = {};
        $scope.happyBack.teamIds = [];
        $scope.happyBack.agentActivities.forEach(agentActivity => {
            let teamIds = $scope.happyBack.activityTypeNoAndTeamId[agentActivity.activityTypeNo] || [];
            teamIds.forEach(teamId => {
                // 所代理的业务产品没有包含该组织,则跳过
                if ($scope.businessProduct.teamIds.indexOf(teamId) === -1) {
                    return;
                }
                if (!$scope.happyBack.groupByTeamId[teamId]) {
                    $scope.happyBack.groupByTeamId[teamId] = [];
                }
                $scope.happyBack.groupByTeamId[teamId].push(agentActivity);
                if ($scope.happyBack.teamIds.indexOf(teamId) === -1) {
                    $scope.happyBack.teamIds.push(teamId);
                }
            })
        });
        $scope.happyBack.teamIds.sort();
        angular.forEach($scope.happyBack.groupByTeamId, (data, teamId) => {
            $scope.happyBack.groupByTeamIdGridList[teamId] = {
                data: data,
                enableHorizontalScrollbar: true,        //横向滚动条
                enableVerticalScrollbar: true,  		//纵向滚动条
                columnDefs: $scope.happyBack.gridColumnDefs
            }
        })
    };

    $scope.agent = {};
    $scope.teamType = [];
    $scope.showStatus = true;
    $http.post('agentInfo/queryAgentInfoDetail', {
        "agentNo": $stateParams.id,
        "teamId": $stateParams.teamId
    }).success(function (msg) {
        if (msg.status) {
            $scope.agent = msg.agentInfo;
            $scope.businessProduct.agentTeamInfo = msg.agentTeamInfo;
            $scope.generateBusinessProductGrid(msg.agentProducts);
            $scope.generateShareRuleGrid(msg.agentShare);
            $scope.generateHappyBackGrid(msg);
        } else if (msg.msg) {
            $scope.notice(msg.msg);
        }
    }).error(function () {});

}).filter('compareDateFilter', function () {
    return function (value) {
        var date = new Date().getTime();
        if (date < value) {
            return "否";
        }
        if (date >= value) {
            return "是";
        }
    }
});
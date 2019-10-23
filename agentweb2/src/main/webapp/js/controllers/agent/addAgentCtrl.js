angular.module('inspinia', ['angularFileUpload']).controller("addAgentCtrl", function ($scope, $rootScope, $http, $state, $stateParams, uiGridConstants, i18nService, FileUploader, $log, $uibModal, $compile, $timeout) {
    i18nService.setCurrentLang('zh-cn');
    $scope.agent = {
        agentName: "",
        isOem: 0,
        email: "",
        mobilephone: "",
        phone: "",
        linkName: "",
        saleName: "",
        invest: 0,
        investAmount: 0,
        agentArea: "",
        address: "",
        accountName: "",
        accountType: 2,
        bankName: "",
        accountNo: "",
        cnapsNo: ""
    };
    $scope.type = [{text: '个人', value: 1}, {text: '个体商户', value: 2}, {text: '企业商户', value: 3}];
    //活动类型
    if ($scope.oemType === 'ZHZFPAY') {
        $scope.subjectTypes = [{text: '全部', value: ''}, {text: '欢乐返', value: '009'}];
    } else {
        $scope.subjectTypes = [{text: '全部', value: ''}, {text: '欢乐返-循环送', value: '008'}, {text: '欢乐返', value: '009'}];
    }
//    $scope.showIdCardNo = "1";//用来区别新增页面和修改页面,控制是否显示身份证号
    $scope.agentType = $rootScope.agentType;
    $scope.fr = [{text: '固定分润比例', value: '5'}];
    /**
     * 业务产品相关变量
     */
    $scope.businessProduct = {
        unchecked: false,           // 没有选择业务产品
        oldSelectedBpId: [],        // 上次选中的业务产品
        allData: [],                // 当前登陆代理商代理的所有业务产品
        groupByTeamId: {},          // 当前登陆代理商代理的所有业务产品--按组织id分组
        groupByTeamIdGridList:{},   // 当前登陆代理商代理的所有业务产品--按组织id分组--表格
        groupByTeamIdGridApi: {},   // 当前登陆代理商代理的所有业务产品--按组织id分组--表格api
        teamInfo: {},               // 当前登陆代理商代理的所有业务产品--组织信息
        teamIds: [],
        gridColumnDefs: [           // 业务产品表格列表
            {field: 'key1', displayName: '业务产品ID', width: 150},
            {field: 'key3', displayName: '业务产品名称', width: 150},
            {field: 'key5', displayName: '类型', width: 150, cellFilter: "formatDropping:" + angular.toJson($scope.type)},
            {field: 'key6', displayName: '群组号', width: 200},
            {field: 'key7', displayName: '是否允许单独申请', width: 200, cellFilter: "formatDropping:" + angular.toJson($scope.bool)},
        ]
    };
    /**
     * 分润设置相关变量
     */
    $scope.shareRule = {
        shareRuleData: [],          // 选中业务产品后,后台查出来的所有分润设置信息
        groupByTeamId: {},          // 分润设置信息--按组织分类--并且合并(借记卡/贷记卡 微信/支付宝)之后的数据
        teamIds: [],                // 分润设置信息--组织id集合
        groupByTeamIdGridList:{},   // 分润设置按组织分类之后表格集合
        showBatchSetShare: false,
        batchSetOption: 'shareProfitPercent',         // 批量设置选项
        batchSetValue: '',          // 批量设置值
        gridColumnDefs: [           // 分润设置表格列信息
            // {field: 'id', displayName: '测试id', width: 200},
            // {field: 'shareIds', displayName: '测试shareIds', width: 200},
            {field: 'bpName', displayName: '业务产品名称', width: 200},
            {field: 'serviceName', displayName: '服务名称', width: 200},
            {
                field: 'serviceType', displayName: '服务种类', width: 150,
                cellTemplate:
                    '<div class="lh30" ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001"><span ng-bind="row.entity.serviceType | serviceTypeFilter"/></div>'
                    + '<div class="lh30" ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001"><span ng-bind="row.entity.serviceType2 | serviceTypeFilter"/>-提现</div>'

            },
            {field: 'cardType', displayName: '银行卡种类', width: 120, cellFilter: "formatDropping:" + $scope.cardTypeStr},
            {
                field: 'holidaysMark',
                displayName: '节假日标志',
                width: 120,
                cellFilter: "formatDropping:" + $scope.holidaysStr
            },
            {
                field: 'cost', displayName: '代理商成本', width: 130,
                cellTemplate:
                //如果不是体现服务，也就是交易服务，后面显示%
                    '<div ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001" style="width:98%;height:98%;"> '
                    + '<input type="text" ng-blur="grid.appScope.checkInputRate(row.entity)" style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
                    + '%'
                    + '</div>'
                    //如果是体现服务，后面显示单位元
                    + '<div ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001" style="width:98%;height:98%;"> '
                    + '<input type="text" ng-blur="grid.appScope.checkInputRate(row.entity)" style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
                    + '元'
                    + '</div>'
            },
            {
                field: 'shareProfitPercent', displayName: '代理商固定分润百分比', width: 450,
                cellTemplate:
                    '<div class="col-sm-10 checkbox" ng-show="row.entity.profitType==5" style="float:left;width:330px;">{{row.entity.shareProfitPercent}}%'
                    + '</div>'
                    + '<div class="col-sm-10 checkbox" ng-show="row.entity.profitType==6" style="width:330px; padding:4px;">{{row.entity.ladderRate}}'
                    + '</div>'
                    + '<button ng-click="grid.appScope.ladderFr(row)" class="btn " type="button" >修改分润比例</button>'
            },
        ],
        defaultGrid: {},
        // 微信/支付宝-主扫/被扫/B扫C
        wechatAndZfbServerType: [10002,10003,10005,10006,10011,10012]
    };
    /**
     * 分润成本设置没有勾选业务产品默认表格
     */
    $scope.shareRule.defaultGrid = {
        data: [],
        columnDefs: $scope.shareRule.gridColumnDefs
    };

    /**
     * 欢乐返子类型相关变量
     */
    $scope.happyBack = {
        showStatus: true,               // 展示欢乐返
        activityTypeNoAndTeamId: {},    // 欢乐返子类型和组织id的关系
        parentActivities: [],           // 登陆代理商拥有的欢乐返子类型
        agentActivities: [],            // 当前新增代理商拥有的欢乐返子类型
        groupByTeamId: {},              // 按组织分类
        groupByTeamIdGridList:{},       // 按组织分类之后表格集合
        teamIds: [],                    // 子类型包含的所有teamIds
        showTeamIds:[],                 // 需要展示的子类型的组织id
        defaultGrid: {},
        showBatchSetShare:  false,      // 展示批量设置项
        showFullPrizeAmount: true,      // 展示满奖
        showNotFullDeductAmount:true,   // 展示不满扣
        disJoinIn: false,                  // 不参与欢乐活动
        paramSetMaxValue: false,        //  活动参数设置为最大值
        gridColumnDefs: [
            {
                field: 'select', displayName: '', width: '30', cellTemplate:
                    '<input type="checkbox" ng-disabled="row.entity.disabled" ng-model="row.entity.select" ng-checked="row.entity.select"/>'
            },
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
                    '<div style="width:98%;height:98%;" > ' +
                    '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" ' +
                    'ng-blur="grid.appScope.checkCashBackAmount(row)" ' +
                    ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" requird/>'+
                    '元' + '</div>'
            },
            {
                field: 'taxRate', displayName: '返现比例', width: 200,
                cellTemplate:
                    '<div style="width:98%;height:98%;"> '
                    + '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" ng-blur="grid.appScope.checkTaxRate(row)"'
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" requird/>'
                    + '%'
                    + '</div>'
            },
            {
                field: 'repeatRegisterAmount', displayName: '重复注册返现金额', width: 150,
                cellTemplate:
                    '<div style="width:98%;height:98%;" > '
                    + '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" ng-blur="grid.appScope.checkRepeatRegisterAmount(row)" '
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" requird/>'
                    + '元'
                    + '</div>'
            },
            {
                field: 'repeatRegisterRatio', displayName: '重复注册返现比例', width: 200,
                cellTemplate:
                    '<div style="width:98%;height:98%;"> '
                    + '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" ng-blur="grid.appScope.checkRepeatRegisterRatio(row)"'
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" requird/>'
                    + '%'
                    + '</div>'
            },
            {
                field: 'fullPrizeAmount', displayName: '首次注册满奖金额(选填)', width: 250,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showFullPrizeAmount" > '
                    + '<input type="text"  ng-disabled="grid.appScope.disableHappyBackInput(grid, row)"'
                    // +'<input type="number" '
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
                    + '元'
                    + '</div>'
            },
            {
                field: 'notFullDeductAmount', displayName: '首次注册不满扣金额(选填)', width: 250,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showNotFullDeductAmount"> '
                    + '<input type="text"  ng-disabled="grid.appScope.disableHappyBackInput(grid, row)"'
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" />'
                    + '元'
                    + '</div>'
            },
            {
                field: 'repeatFullPrizeAmount', displayName: '重复注册满奖金额(选填)', width: 250,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showFullPrizeAmount"> '
                    + '<input type="text"  ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" '
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" />'
                    + '元'
                    + '</div>'
            },
            {
                field: 'repeatNotFullDeductAmount', displayName: '重复注册不满扣金额(选填)', width: 250,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showNotFullDeductAmount"> '
                    + '<input type="text"  ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" '
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" />'
                    + '元'
                    + '</div>'
            }
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
    $scope.generateBusinessProductGrid = function (data) {
        let parentProducts = (data && data.parentProducts || []);
        let teamInfo = (data && data.teamInfo || {});
        $scope.businessProduct.teamInfo = teamInfo;
        $scope.businessProduct.allData = parentProducts || [];
        $scope.businessProduct.teamIds = [];
        // 遍历出业务产品所属的业务组织id集合
        angular.forEach(teamInfo, (value, key) => {
            $scope.businessProduct.teamIds.push(key);
        });
        $scope.businessProduct.teamIds.sort();
        // 然后将业务产品按组织分类
        angular.forEach(parentProducts, function (item) {
            if (!$scope.businessProduct.groupByTeamId[item.teamId]) {
                $scope.businessProduct.groupByTeamId[item.teamId] = [];
            }
            $scope.businessProduct.groupByTeamId[item.teamId].push(item);
        });
        // 根据已经"按组织分类好的业务产品"生成对应的表格数据
        angular.forEach($scope.businessProduct.groupByTeamId, function (item, teamId) {
            $scope.businessProduct.groupByTeamIdGridList[teamId] = {
                data: item,
                enableHorizontalScrollbar: true,        //横向滚动条
                enableVerticalScrollbar: true,  		//纵向滚动条
                columnDefs: $scope.businessProduct.gridColumnDefs,
                onRegisterApi: function (gridApi) {
                    $scope.businessProduct.groupByTeamIdGridApi[teamId] = gridApi;
                    //单选的时候，将同组的业务产品做到：同时选中、同时取消
                    $scope.businessProduct.groupByTeamIdGridApi[teamId].selection.on.rowSelectionChangedBatch($scope, function (row) {
                        $scope.businessProduct.unchecked = ($scope.getSelectedBusinessProduct().length === 0);
                    });
                    $scope.businessProduct.groupByTeamIdGridApi[teamId].selection.on.rowSelectionChanged($scope, function (row) {
                        if (row.isSelected) {
                            angular.forEach(item, function (item) {
                                if (row.entity.key6 != null && item.key6 == row.entity.key6 && item.key7 == 1) {
                                    row.grid.api.selection.selectRow(item);
                                }
                            });
                        }else {
                            angular.forEach(row.grid.rows, function (item) {
                                if (row.entity.key6 != null && item.entity.key6 == row.entity.key6 && row.entity.key7 == 1) {
                                    item.isSelected = false;
                                }
                            });
                        }
                        $scope.businessProduct.unchecked = ($scope.getSelectedBusinessProduct().length === 0);
                    })
                }
            }
        });
    };
    /**
     * 批量设置分润
     */
    $scope.batchSetShare = function () {
        // 批量设置分润比例
        if ($scope.shareRule.batchSetOption === 'shareProfitPercent') {
            let batchSetValue = parseFloat($scope.shareRule.batchSetValue);
            if (isNaN(batchSetValue) || batchSetValue > 100 || batchSetValue < 0) {
                $scope.notice("分润比例批量设置取值在0~100之间");
                return;
            }
            let count = 0;
            angular.forEach($scope.shareRule.groupByTeamId, shareData => {
                shareData.filter(item => !!item.cost).forEach(item => {
                    item.shareProfitPercent = $scope.shareRule.batchSetValue;
                    count++;
                })
            });
            $scope.notice("成功设置" + count + "条分润");
        }else {
            $scope.notice("请选择批量设置项")
        }
    };
    /**
     * 获取多个表格选中的业务产品信息
     * @returns {Array}
     */
    $scope.getSelectedBusinessProduct = function () {
        let allSelectedRows = [];
        angular.forEach($scope.businessProduct.groupByTeamIdGridApi, item => {
            let selectedRows = (item.selection && item.selection.getSelectedRows()) || [];
            allSelectedRows.push(...selectedRows);
        });
        return allSelectedRows;
    };

    /**
     * 根据后台返回的分润信息,绘制分润设置表格
     * 1. 先按组织分组
     * 2. 再按业务产品,微信支付宝等信息合并展示
     */
    $scope.generateShareRuleGrid = function (shareRuleData) {
        shareRuleData = shareRuleData || [];
        $scope.shareRule.shareRuleData = shareRuleData;
        $scope.shareRule.groupByTeamId = {};
        $scope.shareRule.teamIds = [];
        $scope.shareRule.groupByTeamIdGridList = {};
        if (shareRuleData.length === 0) {
            return;
        }
        // 服务分润俺组织分组
        let groupByTeamId = {};
        angular.forEach(shareRuleData, item => {
            if (!groupByTeamId[item.teamId]) {
                groupByTeamId[item.teamId] = [];
            }
            groupByTeamId[item.teamId].push(JSON.parse(JSON.stringify(item)));
        });
        //    服务分润合并
        // 1. 微信支付宝交易合并成一组
        // 2. 微信支付宝提现合并成一组
        // 3  其他类型按照借记卡贷记卡合并成一组
        angular.forEach(groupByTeamId, (shareRules, teamId) => {
            angular.forEach(shareRules, shareRule => {
                // 判断该分润信息是否已经合并过
                if (shareRule.isMerged) {
                    return;
                }
                if (!$scope.shareRule.groupByTeamId[teamId]) {
                    $scope.shareRule.groupByTeamId[teamId] = [];
                }
                let shareIds = shareRules.filter(item => {
                    // 如果是微信支付宝服务(业务产品一样,服务类型为支付宝或微信)
                    if ($scope.shareRule.wechatAndZfbServerType.indexOf(shareRule.serviceType) >= 0) {
                        return item.bpId === shareRule.bpId && $scope.shareRule.wechatAndZfbServerType.indexOf(item.serviceType) >= 0;
                    }
                    // 如果是微信支付宝提现服务(业务产品一样,对应的交易服务类型为支付宝或微信)
                    else if ($scope.shareRule.wechatAndZfbServerType.indexOf(shareRule.serviceType2) >= 0) {
                        return item.bpId === shareRule.bpId &&  $scope.shareRule.wechatAndZfbServerType.indexOf(item.serviceType2) >= 0;
                    }
                    // 其他服务(业务产品一样,服务类型一样)
                    else {
                        return item.bpId === shareRule.bpId && item.serviceType === shareRule.serviceType && item.serviceType2 === shareRule.serviceType2;
                    }
                }).map(item => {
                    // 过滤后的数据标记为合并过,并返回自增id(页面生成的id,用于标记分润数据的唯一标识)
                    item.isMerged = true;
                    return item.id;
                });
                shareRule.shareIds = shareIds;
                // 如果合并条数大于等于2条,需要修改展示名字
                if (shareIds.length >= 2) {
                    if ($scope.shareRule.wechatAndZfbServerType.indexOf(shareRule.serviceType) >= 0) {
                        shareRule.serviceType = "微信/支付宝";
                        shareRule.serviceName = "微信/支付宝 支付";
                    } else if ($scope.shareRule.wechatAndZfbServerType.indexOf(shareRule.serviceType2) >= 0) {
                        shareRule.serviceType2 = "微信/支付宝";
                        shareRule.serviceName = "微信/支付宝 关联提现";
                    }else{
                        let tempShareRules = shareRules.filter(item => shareIds.indexOf(item.id) >= 0);
                        let cardTypes = tempShareRules.map(item => item.cardType);
                        if($.unique(cardTypes).length >= 2){
                            shareRule.cardType = "3";
                        }
                        let holidaysMarks = tempShareRules.map(item => item.holidaysMark);
                        if ($.unique(holidaysMarks).length >= 2) {
                            shareRule.holidaysMark = "3";
                        }
                    }
                }
                $scope.shareRule.groupByTeamId[teamId].push(shareRule);
            })
        });

        // 绘制服务分润表格
        angular.forEach($scope.shareRule.groupByTeamId, (item, teamId) => {
            $scope.shareRule.groupByTeamIdGridList[teamId] = {
                data: item,
                enableHorizontalScrollbar: true,        //横向滚动条
                enableVerticalScrollbar: true,  		//纵向滚动条
                columnDefs: $scope.shareRule.gridColumnDefs
            };
            $scope.shareRule.teamIds.push(teamId);
        });
        $scope.shareRule.teamIds.sort();
    };
    /**
     * 获取需要提交的分润信息
     * @returns {Array}
     */
    $scope.getShareRuleData = function () {
        let gridShareData = [];
        let shareRuleData = [];
        angular.forEach($scope.shareRule.groupByTeamId, item => {
            gridShareData.push(...item);
        });
        $scope.shareRule.shareRuleData.forEach(shareRule => {
            let filter = gridShareData.filter(item => item.shareIds.indexOf(shareRule.id) >= 0) ;
            let cost = parseFloat(filter && filter[0] && filter[0].cost);
            // 如果分润成本填写不正确,则不提交
            if (isNaN(cost)) {
                return;
            }
            shareRule.cost = cost;
            shareRule.shareProfitPercent = filter && filter[0] && filter[0].shareProfitPercent || 100;
            shareRuleData.push(shareRule);
        });
        return shareRuleData;
    };

    /**
     * 解析欢乐返子类型数据
     */
    $scope.parseHappyBackData = function () {
        $scope.happyBack.agentActivities = JSON.parse(JSON.stringify($scope.happyBack.parentActivities));
        $scope.happyBack.groupByTeamId = {};
        $scope.happyBack.teamIds = [];
        $scope.happyBack.agentActivities.forEach(item => {
            $scope.happyBackParamSet(item);
            item.teamIds = [];
            // 如果子类型的组织不在业务产品组织访问类,则不显示
            let teamIdIndexes = ($scope.happyBack.activityTypeNoAndTeamId[item.activityTypeNo] || [])
                .filter(teamId => $scope.businessProduct.teamIds.indexOf(teamId) >= 0);
            if (teamIdIndexes.length === 0) {
                return;
            }
            // 按组织id分类
            ($scope.happyBack.activityTypeNoAndTeamId[item.activityTypeNo] || [])
                .filter(teamId => $scope.businessProduct.teamIds.indexOf(teamId) >= 0)
                .forEach(teamId => {
                    if (!$scope.happyBack.groupByTeamId[teamId]) {
                        $scope.happyBack.groupByTeamId[teamId] = [];
                    }
                    if (item.teamIds.indexOf(teamId) < 0) {
                        item.teamIds.push(teamId);
                        item.teamIds.sort();
                    }
                    $scope.happyBack.groupByTeamId[teamId].push(item);
                });
        });
        angular.forEach($scope.happyBack.groupByTeamId, (item, teamId) => {
            $scope.happyBack.teamIds.push(teamId);
        });
        $scope.happyBack.teamIds.sort();
    };
    /**
     * 生成欢乐返相关表格
     */
    $scope.generateHappyBackGrid = function () {
        $scope.happyBack.groupByTeamIdGridList = {};
        $scope.happyBack.showTeamIds = [];
        // 如果业务产品没有选择,则展示所有的欢乐返子类型
        let selectedBusinessProduct = $scope.getSelectedBusinessProduct();
        if (selectedBusinessProduct.length === 0) {
            $scope.happyBack.showTeamIds.push(...$scope.happyBack.teamIds);
        }else {
            // 否则展示所选业务产品对应的组织的欢乐返子类型
            let bpTeamIds = selectedBusinessProduct.map(item => item.teamId);
            let showTeamIds = $scope.happyBack.teamIds.filter(item => bpTeamIds.indexOf(item) >= 0);
            $scope.happyBack.showTeamIds.push(...showTeamIds);
        }
        $scope.happyBack.showTeamIds.sort();
        if ($scope.happyBack.showTeamIds.length === 0) {
            return;
        }
        angular.forEach($scope.happyBack.groupByTeamId, (item, teamId) => {
            // 改组织如果不展示,直接返回
            if ($scope.happyBack.showTeamIds.indexOf(teamId) < 0) {
                return;
            }
            $scope.happyBack.groupByTeamIdGridList[teamId] = {
                data: item,
                enableHorizontalScrollbar: true,        //横向滚动条
                enableVerticalScrollbar: true,  		//纵向滚动条
                columnDefs: $scope.happyBack.gridColumnDefs,
                teamId: teamId
                // ,
                // onRegisterApi: function (gridApi) {                //选中行配置
                //     gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                //         row.entity.select = row.isSelected;
                //     });
                //     gridApi.selection.on.rowSelectionChangedBatch($scope,function(rows){
                //         rows.forEach(row => row.entity.select = row.isSelected);
                //         gridApi.
                //     });
                //     $timeout(function () {
                //         gridApi.selection.selectAllRows();
                //     })
                // }
            };
        });
    };
    /**
     * 欢乐子类型如果第二次以上(包含第二次)出现的话,则input不让填写
     */
    $scope.disableHappyBackInput = function (grid, row) {
        let map = row.entity.teamIds.map(teamId => $scope.happyBack.showTeamIds.indexOf(teamId)).filter(item => item >= 0);
        return $scope.happyBack.showTeamIds.indexOf(grid.options.teamId) > Math.min.apply(null, map);
    };
    /**
     * 欢乐返子类型参数设置为最大值(跟当前登陆代理商一致)
     */
    $scope.happyBackParamSetMaxValue = function () {
        if ($scope.happyBack.paramSetMaxValue) {
            $scope.happyBack.agentActivities.filter(item => item.select)
                .forEach(agentActivity => {
                    let parentActivity = $scope.happyBack.parentActivities.filter(item => item.activityTypeNo === agentActivity.activityTypeNo);
                    $scope.happyBackParamSet(agentActivity, parentActivity && parentActivity[0]);
                })
        }else {
            $scope.happyBack.agentActivities.filter(item => item.select)
                .forEach(agentActivity => {
                    $scope.happyBackParamSet(agentActivity);
                })
        }
    };
    $scope.happyBackParamSet = function (activity, defaultValue) {
        activity.taxRate = (defaultValue && defaultValue.taxRate * 100) || (!!defaultValue ? 0 : '');
        activity.cashBackAmount = (defaultValue && defaultValue.cashBackAmount) || (!!defaultValue ? 0 : '');
        activity.repeatRegisterRatio = (defaultValue && defaultValue.repeatRegisterRatio * 100) || (!!defaultValue ? 0 : '');
        activity.repeatRegisterAmount = (defaultValue && defaultValue.repeatRegisterAmount) || (!!defaultValue ? 0 : '');
        activity.fullPrizeAmount = (defaultValue && defaultValue.fullPrizeAmount) || (!!defaultValue ? 0 : '');
        activity.notFullDeductAmount = (defaultValue && defaultValue.notFullDeductAmount) || (!!defaultValue ? 0 : '');
        activity.repeatFullPrizeAmount = (defaultValue && defaultValue.repeatFullPrizeAmount) || (!!defaultValue ? 0 : '');
        activity.repeatNotFullDeductAmount = (defaultValue && defaultValue.repeatNotFullDeductAmount) || (!!defaultValue ? 0 : '');
    };
    //拓展进来的代理商,判断是否有分润,如果没有分润就提示:请先联系上级代理为您配置正确的分润及活动参数,并跳转到主页
    if ($rootScope.registType == 1) {
        $http.get('agentInfo/getAgentShareList').success(function (data) {
            if (data != null && !data.status) {
                $scope.notice(data.msg);
                $state.go("service.addService");
                return;
            }
        });
    }
    //升级成大盟主
    $scope.agent.userCode = $stateParams.userCode;
    var data = {"mobilephone": $stateParams.mobilephone, "userCode": $stateParams.userCode}
    if ($stateParams.mobilephone != null && $stateParams.userCode != null && $stateParams.mobilephone != "" && $stateParams.userCode != "") {
        $http.post("perAgent/upgradePerAgent.do", angular.toJson(data)).success(function (result) {
            if (result.paUserInfo) {
                $scope.agent.mobilephone = result.paUserInfo.mobile;
                $scope.agent.accountName = result.paUserInfo.realName;
                $scope.agent.agentName = result.paUserInfo.agentName;
                $scope.agent.idCardNo = result.paUserInfo.idCardNo;
            }
            if (result.paUserCard) {
                $scope.agent.accountNo = result.paUserCard.account;
                $scope.address = result.paUserCard.address;
                $scope.agent.accountProvince = $scope.address.substring(0, $scope.address.indexOf("-"));
                $scope.agent.accountCity = $scope.address.substring($scope.address.indexOf("-") + 1, $scope.address.lastIndexOf("-"));
                $scope.getAreaList();
                $scope.getHangName();
                $scope.getCitiess();
                $scope.getAreaLists();
                $scope.getCities();
                $scope.getAreas();
                $scope.getPosCnaps();
                $scope.agent.cnapsNo = result.paUserCard.cnaps;
                $scope.agent.bankName = result.paUserCard.bank_branch_name;
            }
        });
    }

    //post查询
    $scope.getAreaList = function (name, type, callback) {
        if (name == null || name == "undefine") {
            return;
        }
        $http.post('areaInfo/getAreaByName.do', 'name=' + name + '&&type=' + type,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (data) {
            callback(data);
        }).error(function () {
        });
    };
    //省
    $scope.getAreaList(0, "p", function (data) {
        $scope.provinceGroup = data;
    });
    //市
    $scope.getCities = function () {
        $scope.getAreaList($scope.agent.province, "", function (data) {
            $scope.cityGroup = data;
        });
    };
    //县
    $scope.getAreas = function () {
        $scope.getAreaList($scope.agent.city, "", function (data) {
            $scope.areaGroup = data;
        });
    };

    $http.get('businessProductDefine/selectProductByAgent.do').success(function (msg) {
        $scope.generateBusinessProductGrid(msg);
        $scope.selectHappyBackList();
        $timeout(function () {
            angular.forEach($scope.businessProduct.groupByTeamIdGridApi, function (item, teamId) {
                $scope.businessProduct.groupByTeamIdGridApi[teamId].selection.selectAllRows();
                $scope.businessProduct.unchecked = ($scope.getSelectedBusinessProduct().length === 0);
            });
        })
    });

    $scope.selectHappyBackList = function () {
        $http.post('agentInfo/selectHappyBackList')
            .success(function (data) {
                $scope.happyBack.activityTypeNoAndTeamId = (data && data.data && data.data.activityTypeNoAndTeamIdMap) || {};
                $scope.happyBack.parentActivities = (data && data.data && data.data.agentActivities) || [];
                $scope.happyBack.showStatus = ($scope.happyBack.parentActivities.length > 0);
                $scope.parseHappyBackData();
                if ($scope.happyBack.showStatus) {
                    let activity = $scope.happyBack.parentActivities[0];
                    $scope.happyBack.showFullPrizeAmount = activity.showFullPrizeAmount;
                    $scope.happyBack.showNotFullDeductAmount = activity.showNotFullDeductAmount;
                    if (!activity.showFullPrizeAmount && activity.showNotFullDeductAmount) {
                        $scope.happyBack.gridColumnDefs.splice(9, 1);
                        $scope.happyBack.gridColumnDefs.splice(10, 1);
                    } else if (!activity.showNotFullDeductAmount && activity.showFullPrizeAmount) {
                        $scope.happyBack.gridColumnDefs.splice(10, 1);
                        $scope.happyBack.gridColumnDefs.splice(11, 1);
                    } else if (!activity.showFullPrizeAmount && !activity.showNotFullDeductAmount) {
                        $scope.happyBack.gridColumnDefs.splice(9, 4);
                    }
                }
            }).error(function (data) {
            $scope.notice("服务器异常,请稍后重试!");
        });
    };

    /**
     * 校验设置金额是否满足“不高于上级获得返现金额”，获得的返现金额=返现金额*税额百分比
     */
    $scope.checkCashBackAmount = function (row) {
        let cashBackAmount = parseFloat(row.entity.cashBackAmount);
        let taxRate = parseFloat(row.entity.taxRate) / 100;
        if (isNaN(cashBackAmount)) {
            $scope.notice("返现金额不能为空,且只能填写数字");
        }else if (!isNaN(taxRate)) {
            let parentBackAmount = $scope.happyBack.parentActivities
                .filter(item => item.activityTypeNo === row.entity.activityTypeNo)
                .filter(item => !isNaN(parseFloat(item.cashBackAmount)) && !isNaN(parseFloat(item.taxRate)))
                .map(item => parseFloat(item.cashBackAmount) * parseFloat(item.taxRate));
            if (parentBackAmount.length !== 0 && cashBackAmount * taxRate > parentBackAmount[0]) {
                $scope.notice("下级获得的返现金额不能高于上级");
            }
        }
    };
    $scope.checkTaxRate = function (row) {
        let taxRate = parseFloat(row.entity.taxRate) / 100;
        let cashBackAmount = parseFloat(row.entity.cashBackAmount);
        if (isNaN(taxRate)) {
            $scope.notice("返现比例不能为空,且只能填写数字");
        }else if (!isNaN(cashBackAmount)) {
            let parentBackAmount = $scope.happyBack.parentActivities
                .filter(item => item.activityTypeNo === row.entity.activityTypeNo)
                .filter(item => !isNaN(parseFloat(item.cashBackAmount)) && !isNaN(parseFloat(item.taxRate)))
                .map(item => parseFloat(item.cashBackAmount) * parseFloat(item.taxRate));
            if (parentBackAmount.length !== 0 && cashBackAmount * taxRate > parentBackAmount[0]) {
                $scope.notice("下级获得的返现金额不能高于上级");
            }
        }
    };
    $scope.checkRepeatRegisterAmount = function (row) {
        let repeatRegisterAmount = parseFloat(row.entity.repeatRegisterAmount);
        let repeatRegisterRatio = parseFloat(row.entity.repeatRegisterRatio) / 100;
        if (isNaN(repeatRegisterAmount)) {
            $scope.notice("重复注册返现金额不能为空,且只能填写数字");
        }else if (!isNaN(repeatRegisterRatio)) {
            let parentBackAmount = $scope.happyBack.parentActivities
                .filter(item => item.activityTypeNo === row.entity.activityTypeNo)
                .filter(item => !isNaN(parseFloat(item.repeatRegisterAmount)) && !isNaN(parseFloat(item.repeatRegisterRatio)))
                .map(item => parseFloat(item.repeatRegisterAmount) * parseFloat(item.repeatRegisterRatio));
            if (parentBackAmount.length !== 0 && repeatRegisterAmount * repeatRegisterRatio > parentBackAmount[0]) {
                $scope.notice("下级获得的重复注册返现金额不能高于上级");
            }
        }
    };

    $scope.checkRepeatRegisterRatio = function (row) {
        let repeatRegisterRatio = parseFloat(row.entity.repeatRegisterRatio) / 100;
        let repeatRegisterAmount = parseFloat(row.entity.repeatRegisterAmount);
        if (isNaN(repeatRegisterRatio)) {
            $scope.notice("重复注册返现比例不能为空");
        }else if (!isNaN(repeatRegisterAmount)) {
            let parentBackAmount = $scope.happyBack.parentActivities
                .filter(item => item.activityTypeNo === row.entity.activityTypeNo)
                .filter(item => !isNaN(parseFloat(item.repeatRegisterAmount)) && !isNaN(parseFloat(item.repeatRegisterRatio)))
                .map(item => parseFloat(item.repeatRegisterAmount) * parseFloat(item.repeatRegisterRatio));
            if (parentBackAmount.length !== 0 && repeatRegisterAmount * repeatRegisterRatio > parentBackAmount[0]) {
                $scope.notice("下级获得的重复注册返现金额不能高于上级");
            }
        }
    };

    //欢乐返活动
    $scope.happyBackListGrid = {
        data: 'happyBackList',
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        columnDefs: $scope.happyBack.gridColumnDefs
    };

    //进入设置新分润的模块时
    $scope.ladderFr = function (row) {
        if (row.entity.cost == "" || row.entity.cost == null) {
            $scope.notice("请先填写代理商成本");
            return;
        }

        $('#ladderFrModel').modal('show');
        $scope.info = {
            entity: angular.copy(row.entity),
            row: row,
            m1: 0,
            m2: "",
            m3: "",
            m4: "",
            m5: "",
            m6: "",
            m7: "",
            m8: "",
            m9: "无穷大"
        };
        if (row.entity.ladderRate != null) {
            var strArr = row.entity.ladderRate.toString().split("<");
            $scope.info.m2 = parseInt(strArr[0].split("%")[0]);
            $scope.info.m3 = parseInt(strArr[1].split("%")[0]);
            $scope.info.m4 = parseInt(strArr[2].split("%")[0]);
            $scope.info.m5 = parseInt(strArr[3].split("%")[0]);
            $scope.info.m6 = parseInt(strArr[4].split("%")[0]);
            $scope.info.m7 = parseInt(strArr[5].split("%")[0]);
            $scope.info.m8 = parseInt(strArr[6].split("%")[0]);
        }
    };
    //点击确定时,保存阶梯分润并在表格显示
    $scope.addShare = function (row) {
        $('#ladderFrModel').modal('hide');
        row.entity.profitType = $scope.info.entity.profitType;
        row.entity.shareProfitPercent = $scope.info.entity.shareProfitPercent;
        row.entity.ladderRate = $scope.info.m2 + "%<" + $scope.info.m3 + "<" + $scope.info.m4 + "%<" + $scope.info.m5 + "<" +
            $scope.info.m6 + "%<" + $scope.info.m7 + "<" + $scope.info.m8 + "%";
    };

    $scope.getShareList = function () {
        //如果没有勾选业务产品，则不能发送请求
        let products = $scope.getSelectedBusinessProduct();
        if (products.length === 0) {
            $scope.generateShareRuleGrid();
            return false;
        }
        //如果选中的业务产品没有发生改变，就不会发送请求
        let ids = products.map(item => item.key1);
        if (ids.join(',') === $scope.businessProduct.oldSelectedBpId.join(',')) {
            return false;
        }
        $scope.businessProduct.oldSelectedBpId = [...ids];
        $http.post('agentInfo/getAgentServices', angular.toJson(ids))
            .success(function (data, status, headers, config) {
                let shareData = [];
                angular.forEach(data.rates, function (data, index) {
                    if (data.allowIndividualApply == 1) {
                        let index = shareData.length;
                        shareData[index] = {
                            id: index,
                            teamId: data.teamId,
                            serviceId: data.serviceId,
                            serviceName: data.serviceName,
                            cardType: data.cardType,
                            rateType: data.rateType,
                            singleNumAmount: data.singleNumAmount,
                            rate: data.rate,
                            holidaysMark: data.holidaysMark,
                            profitType: '5',
                            bpId: data.bpId,
                            bpName: data.bpName,
                            serviceType: data.serviceType,
                            serviceType2: data.serviceType2,
                            shareProfitPercent: '100'
                        };
                    }
                });
                $scope.generateShareRuleGrid(shareData);
            }).error(function (data, status, headers, config) {
        });
    };
    $scope.submit = function () {
        //获取选中业务产品的ID数组
        $scope.submiting = true;
        var products = $scope.getSelectedBusinessProduct();
        var ids = [];
        if (products.length > 0) {
            for (var i = 0; i < products.length; i++) {
                ids[i] = products[i].key1;
            }
        }

        let happyBackList = [];
        // 如果不参与欢乐返活动,则不需要提交欢乐返配置
        if ($scope.happyBack.disJoinIn) {
            happyBackList = [];
        }else {
            //获取选中的欢乐返活动
            happyBackList = $scope.happyBack.agentActivities
                // 必须是选中的
                .filter(item => item.select)
                // 必须是对应组织id有展示出来的
                .filter(item => item.teamIds.filter(teamId => $scope.happyBack.showTeamIds.indexOf(teamId) >= 0).length > 0);
        }


        var data = {
            "agentInfo": $scope.agent,
            "bpData": ids,
            "shareData": $scope.getShareRuleData(),
            "happyBackList": happyBackList
        };
        $http.post('agentInfo/saveAgentInfo', angular.toJson(data)).success(function (msg) {
            if (msg.status) {
                $scope.notice('添加成功');
                $timeout(() => {
                    $state.transitionTo('agent.addAgent', null, {reload: true})
                }, 500);
            } else {
                $scope.submiting = false;
                $scope.notice(msg.msg || "添加失败");
            }
        }).error(function () {
            $scope.submiting = false;
        });
    };

    //自动关联银行信息
    //银行支行
    $scope.adds = {};
    $scope.getAreaLists = function (name, type, callback) {
        if (name == null || name == "undefine") {
            return;
        }
        $http.post('areaInfo/getAreaByName', 'name=' + name + '&&type=' + type,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (data) {
            callback(data);
        }).error(function () {
        });
    };

    //省，加载页面时自动触发
    $scope.getAreaLists(0, "p", function (data) {
        $scope.provinceGroups = data;
    });
    //市，页面上ng-change生时触发
    $scope.getCitiess = function () {
        $scope.areaGroups = [];
        $scope.getAreaLists($scope.agent.accountProvince, "", function (data) {
            //tgh316处理市问题
            angular.forEach(data, function (item) {
                if (item.name != null && item.name != "" && item.name.substr(item.name.length - 1, 1) === '市') {
                    item.name = item.name.substring(0, item.name.length - 1);
                }
            });
            $scope.cityGroups = data;
        });
        $scope.bankNames = [];
    };
    $scope.bankNames = [];
    //支行
    $scope.getPosCnaps = function () {
        if ($scope.agent.accountType == 1) {	// 如果是对公帐号,则不校验
            return;
        }
        if ($scope.agent.accountNo == null || $scope.agent.accountNo == ""
            || $scope.agent.bankName == null || $scope.agent.bankName == "") {
            return;
        }
        if ($scope.agent.accountCity == null || $scope.agent.accountCity == "") {
            return;
        }
        var data = {
            "pris": $scope.agent.accountProvince,
            "cityName": $scope.agent.accountCity,
            "backName": $scope.agent.bankName
        };
        $http.post("merchantInfo/selectCnaps", angular.toJson(data))
            .success(function (data) {
                if (!data.bols) {
                    $scope.notice(data.msg);
                } else {
                    $scope.bankNames = data.list;
                }
            });
    };

    $scope.getHangName = function () {
        $scope.agent.bankName = null;
        if ($scope.agent.accountType == 1) {	// 如果是对公帐号,则不校验
            return;
        }
        if ($scope.agent.accountNo == null || $scope.agent.accountNo == "") {
            return;
        }
        $http.post("merchantInfo/getBackName", angular.toJson({"accountNo": $scope.agent.accountNo}))
            .success(function (msg) {
                if (msg.bols) {
                    $scope.accountList = msg.lists;
                    $scope.agent.bankName = msg.lists[0].bankName;
                    if ($scope.agent.accountProvince != null && $scope.agent.accountProvince != "" && $scope.agent.accountCity != null && $scope.agent.accountCity != "") {
                        $scope.getPosCnaps();
                    }
                }
            });
    };
    $scope.defineSet = function () {
        var setPro = $scope.agent.setpro;
        var setValue = $scope.agent.setvalue;
        if (setPro == "" || setPro == undefined) {
            $scope.notice("请选择设置项");
            return;
        }
        if (setValue == "" || setValue == undefined) {
            $scope.notice("请输入设定值");
            return;
        }

        $scope.happyBack.agentActivities.forEach(function (e) {
            e[setPro] = setValue;
        })
    };
});

angular.module('inspinia').controller('agentLadderModalCtr', function ($scope, $stateParams, $http) {
    $scope.solutionModalClose = function () {
        $scope.modalInstance.dismiss();
    };

    $scope.solutionModalOk = function () {
        $scope.modalInstance.close($scope);
    }
});

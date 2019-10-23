angular.module('inspinia', ['angularFileUpload', 'uiSwitch']).controller("editAgentCtrl", function ($scope, $http, $state, $timeout,$stateParams, SweetAlert, uiGridConstants, i18nService, FileUploader, $log, $uibModal, $compile, $interval) {
    $scope.merRateStr = '[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"}]';
    $scope.statusStr = [{text: "正常", value: 1}, {text: "关闭", value: 1}];
    $scope.type = [{text: '个人', value: 1}, {text: '个体商户', value: 2}, {text: '企业商户', value: 3}];
    //活动类型
    if ($scope.oemType === 'ZHZFPAY') {
        $scope.subjectTypes = [{text: '全部', value: ''}, {text: '欢乐返', value: '009'}];
    } else {
        $scope.subjectTypes = [{text: '全部', value: ''}, {text: '欢乐返-循环送', value: '008'}, {text: '欢乐返', value: '009'}];
    }
    /**
     * 业务产品相关变量
     */
    $scope.businessProduct = {
        agentBpIds: [],             // 编辑代理商代理商的已经代理业务产品id集合
        bpIdNotSetShare:[],         // 还没有设置分润的业务产品id集合
        groupByTeamId: {},          // 所有业务产品--按组织id分组
        teamIds: [],                // 业务产品的组织id
        groupByTeamIdGridList:{},   // 所有业务产品--按组织id分组--表格
        parentBpData: [],           //  父级代理商所代理的所有业务产品
        parentTeamInfo: {},         // 当前代理商所代理业务产品的组织
        lastCheckedBpId: [],        // 上次选中的业务产品
        unchecked: false,
        gridColumnDefs: [
            {
                field: 'isProxyed', displayName: '', width: '30', cellTemplate:
                    '<input type="checkbox" ng-disabled="row.entity.disabled" ng-change="grid.appScope.selectionRow(row)" ng-model="row.entity.isProxyed" ng-checked="row.entity[col.field]"/>'
            },
            {field: 'key1', displayName: '业务产品ID', width: 180},
            {field: 'key3', displayName: '业务产品名称', width: 180},
            {field: 'key5', displayName: '类型', width: 180, cellFilter: "formatDropping:" + angular.toJson($scope.type)},
            {
                field: 'key2', displayName: '状态', width: 180,
                cellTemplate:
                    '<span ng-show="row.entity.showStatusSwitch"><switch class="switch switch-s" ng-model="row.entity.key2" ng-change="grid.appScope.switchProduct(row)" /></span>'
            },
            {field: 'key6', displayName: '群组号', width: 200},
            {
                field: 'key7',
                displayName: '是否允许单独申请',
                width: 200,
                cellFilter: "formatDropping:" + angular.toJson($scope.bool)
            }
        ]
    };

    /**
     * 分润设置
     */
    $scope.shareRule = {
        shareRuleData: [],              // 后台返回的所有分润成本数据,包含已经有值和没有值的
        the1stShareRuleLength: 0,       // 编辑代理商已经代理业务产品后的分润列表长度,第一次请求时的长度
        updateShareData: [],            // 后台还没有设置值的分润信息
        the1stUpdateShareRuleLength: 0, // 第一请求后台,代理商已经代理业务产品但还没有设置分润的成本个数
        agentTeamIds: [],               // 分润设置信息--组织id集合
        groupByTeamId: {},              // 分润设置按组织分类
        groupByTeamIdGridList:{},       // 分润设置按组织分类之后表格集合
        showBatchSetShare: false,
        batchSetOption: 'shareProfitPercent',         // 批量设置选项
        batchSetValue: '',          // 批量设置值
        gridColumnDefs: [
            {field: 'bpName', displayName: '业务产品名称', width: 200},
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
                    + '<input type="text" ng-blur="grid.appScope.checkInputRate(row.entity)" style="width:80%;height:98%;" ng-show="row.entity.isNewShare" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
                    + '<span ng-show="row.entity.isNewShare" >%</span>'
                    + '<span ng-show="!row.entity.isNewShare" >{{row.entity[col.field]}}</span>'
                    + '</div>'
                    //如果是体现服务，后面显示单位元
                    + '<div ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001" style="width:98%;height:98%;"> '
                    + '<input type="text" ng-blur="grid.appScope.checkInputRate(row.entity)"  style="width:80%;height:98%;" ng-show="row.entity.isNewShare" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
                    + '<span ng-show="!row.entity.isNewShare" >{{row.entity[col.field]}}</span>'
                    + '元'
                    + '</div>'
            },
            {
                field: 'shareProfitPercent', displayName: '代理商固定分润百分比', width: 450, cellTemplate:
                    '<div class="col-sm-10 checkbox" style="float:left;width:330px;">{{row.entity.shareProfitPercent}}' +
                    '<span ng-show="row.entity.shareProfitPercent">%</span>' + '</div>'
                    + '<button ng-click="grid.appScope.ladderFr(row)" ng-show="row.entity.isNewShare" class="btn " type="button" >修改分润比例</button>'
            },
            {
                field: 'temp', displayName: '操作', width: 130, pinnedRight: true, cellTemplate:
                    '<div ng-show="row.entity.isOldShare">' +
                    '<a ng-click="grid.appScope.editShare(row.entity)"   >修改分润</a>' +
                    '</div>'
            }
        ]
    };
    $scope.uibTabsetActive = 'baseInfo';
    /**
     * 欢乐返子类型相关配置
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
            {
                field: 'status', displayName: '允许更改活动', width: 180,
                cellTemplate:
                    '<span ng-show="row.entity.showStatusSwitch"><switch class="switch switch-s" ng-model="row.entity.status"  ng-change="grid.appScope.switchHappyBack(row)" /></span>'
            },
            {field: 'transAmount', displayName: '交易金额', width: 150},
            {
                field: 'cashBackAmount', displayName: '返现金额', width: 150,
                cellTemplate:
                    '<div style="width:98%;height:98%;" > '
                    + '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" ng-blur="grid.appScope.checkCashBackAmount(row)" '
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" requird/>'
                    + '元'
                    + '</div>'
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
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showFullPrizeAmount"> '
                    + '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" '
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
                    + '元'
                    + '</div>'
            },
            {
                field: 'notFullDeductAmount', displayName: '首次注册不满扣金额(选填)', width: 250,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showNotFullDeductAmount"> '
                    + '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" '
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" />'
                    + '元'
                    + '</div>'
            },
            {
                field: 'repeatFullPrizeAmount', displayName: '重复注册满奖金额(选填)', width: 250,
                cellTemplate:
                    '<div style="text:98%;height:98%;" ng-show="row.entity.showFullPrizeAmount" > '
                    + '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" '
                    + ' style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]" />'
                    + '元'
                    + '</div>'
            },
            {
                field: 'repeatNotFullDeductAmount', displayName: '重复注册不满扣金额(选填)', width: 250,
                cellTemplate:
                    '<div style="width:98%;height:98%;" ng-show="row.entity.showNotFullDeductAmount" > '
                    + '<input type="text" ng-disabled="grid.appScope.disableHappyBackInput(grid, row)" '
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
        columnDefs: $scope.happyBack.gridColumnDefs
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
            $scope.shareRule.updateShareData.filter(item => !!item.cost)
                .forEach(item => {
                    item.shareProfitPercent = $scope.shareRule.batchSetValue;
                    count++;
                });
            $scope.notice("成功设置" + count + "条分润");
        }else {
            $scope.notice("请选择批量设置项")
        }
    };
    /**
     * 生成业务产品分组表格
     * @param data 后台接口返回数据
     */
    $scope.generateBusinessProductGrid = function (data) {
        let parentBpData = (data && data.parentProducts) || [];
        let agentBpData = (data && data.agentProducts) || [];
        $scope.businessProduct.parentTeamInfo =  (data && data.parentTeamInfo) || {};

        // 当前编辑代理商代理过的业务产品id集合
        let agentBpIds = agentBpData.map(item => item.key1);
        $scope.businessProduct.unchecked = (agentBpIds.length === 0);
        $scope.businessProduct.agentBpIds = agentBpIds;
        // 默认全部没有代理
        parentBpData.forEach(item => item.isProxyed = false);
        // 过滤编辑的代理商已经代理过的业务产品
        parentBpData.filter(item => agentBpIds.indexOf(item.key1) >= 0)
            .forEach(item => {
                item.isProxyed = true;              // 表格勾选框选中状态
                item.showStatusSwitch = true;
                item.disabled = true;               // 编辑代理商已经代理了这个业务产品,不能取消勾选了
                let agentBp = agentBpData.filter(temp => temp.key1 === item.key1);
                item.key2 = agentBp && agentBp[0] && agentBp[0].key2;  // 代理商代理业务产品的状态 agent_business_product => status
            });

        $scope.businessProduct.parentBpData = parentBpData;
        // 按组织分类
        parentBpData.forEach(item => {
            if (!$scope.businessProduct.groupByTeamId[item.teamId]) {
                $scope.businessProduct.groupByTeamId[item.teamId] = [];
            }
            $scope.businessProduct.groupByTeamId[item.teamId].push(item);
        });
        $scope.businessProduct.teamIds = [];
        // 绘制表格数组
        angular.forEach($scope.businessProduct.groupByTeamId, (item, teamId) => {
            $scope.businessProduct.teamIds.push(teamId);
            $scope.businessProduct.groupByTeamIdGridList[teamId] = {
                data: item,
                columnDefs: $scope.businessProduct.gridColumnDefs
            };
            // 在生成业务产品表格结构时,也生成对应的分润成本表格数据,
            // 然后在数据更新时,只改变data的内容
            $scope.shareRule.groupByTeamIdGridList[teamId] = {
                data: [],
                enableHorizontalScrollbar: true,        //横向滚动条
                enableVerticalScrollbar: true,  		//纵向滚动条
                columnDefs:$scope.shareRule.gridColumnDefs
            };
        });
        $scope.businessProduct.teamIds.sort();
    };

    /**
     * 解析第一次请求后台分润数据
     * @param data 后台接口返回数据
     */
    $scope.pasreThe1stShareRuleData = function (agentShare) {
        $scope.shareRule.shareRuleData = agentShare || [];
        $scope.shareRule.shareRuleData.forEach(item => {
            if (item.profitType) {
                item.isOldShare = true;
            } else {
                item.isOldShare = false;
                item.isNewShare = true;
                item.profitType = 5;
                item.shareProfitPercent = "100";
                $scope.shareRule.updateShareData.push(item);
                if ($scope.businessProduct.bpIdNotSetShare.indexOf(item.bpId) < 0) {
                    $scope.businessProduct.bpIdNotSetShare.push(item.bpId);
                }
            }
        });
        $scope.shareRule.the1stShareRuleLength = $scope.shareRule.shareRuleData.length;
        $scope.shareRule.the1stUpdateShareRuleLength = $scope.shareRule.updateShareData.length;
        $scope.generateShareRuleGrid();
    };

    /**
     * 新增代理业务产品后增加分润成本
     */
    $scope.addShareRuleData = function (shareData) {
        shareData = shareData || [];
        $scope.shareRule.shareRuleData.splice($scope.shareRule.the1stShareRuleLength);
        $scope.shareRule.updateShareData.splice($scope.shareRule.the1stUpdateShareRuleLength);
        shareData.forEach(item => {
            item.profitType = 5;
            item.shareProfitPercent = '100';
            item.isNewShare = true;
            $scope.shareRule.shareRuleData.push(item);
            $scope.shareRule.updateShareData.push(item);
        });
        $scope.generateShareRuleGrid();
    };

    /**
     * 生成分润成本表格-按组织id分组
     */
    $scope.generateShareRuleGrid = function () {
        $scope.shareRule.agentTeamIds = [];
        $scope.shareRule.groupByTeamId = {};
        $scope.shareRule.shareRuleData.forEach(item => {
            if (!$scope.shareRule.groupByTeamId[item.teamId]) {
                $scope.shareRule.groupByTeamId[item.teamId] = [];
            }
            $scope.shareRule.groupByTeamId[item.teamId].push(item);
            if ($scope.shareRule.agentTeamIds.indexOf(item.teamId) < 0) {
                $scope.shareRule.agentTeamIds.push(item.teamId);
            }
        });
        $scope.shareRule.agentTeamIds.sort();
        angular.forEach($scope.shareRule.groupByTeamId, (item, teamId) => {
            $scope.shareRule.groupByTeamIdGridList[teamId].data = item;
        })
    };

    /**
     * 解析欢乐返子类型数据
     */
    $scope.parseHappyBackData = function () {
        $scope.happyBack.groupByTeamId = {};
        $scope.happyBack.teamIds = [];
        $scope.happyBack.agentActivities.forEach(item => {
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
        $scope.uibTabsetActive = "happyBackInfo";
        $scope.happyBack.groupByTeamIdGridList = {};
        $scope.happyBack.showTeamIds = [];
        // 如果业务产品没有选择,则展示所有的欢乐返子类型
        let selectedBusinessProduct = $scope.businessProduct.parentBpData.filter(item => item.isProxyed);
        $scope.businessProduct.unchecked = selectedBusinessProduct.length === 0;
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
            };
        });
    };
    $scope.disableHappyBackInput = function (grid, row) {
        let map = row.entity.teamIds.map(teamId => $scope.happyBack.showTeamIds.indexOf(teamId)).filter(item => item >= 0);
        return $scope.happyBack.showTeamIds.indexOf(grid.options.teamId) > Math.min.apply(null, map);
    };
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
    $scope.teamType = [];
    $scope.agent = {};
    $scope.info = {};
    $scope.paracont = "立即获取";
    $scope.paraevent = true;
    $scope.isnew = true;

    var second = 59;
    var timePromise;

    function interval() {
        if (second <= 0) {
            $interval.cancel(timePromise);
            timePromise = undefined;
            $scope.paracont = "立即获取";
            second = 59;
        } else {
            $scope.paracont = second + "秒后可重发";
            second--;
        }
    }

    $scope.sendphonecode = function () {
        if (timePromise) {
            return;
        }

        if ($scope.info.imgNum == undefined || $scope.info.imgNum == null || $scope.info.imgNum.length != 4) {
            $scope.notice("请输入正确的图形验证码");
            return;
        }
        timePromise = $interval(interval, 1000, 100); //表示每一秒执行一次，执行100次

        //设置服务
        $scope.info.agentNo = $stateParams.agentNo;
        //发送验证码
        $http.post('mycode/sendOldMsgLevel', "checkNum=" + angular.toJson($scope.info)
            , {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function (result) {
            if (result.status) {
                console.log(result);
            } else {
                $interval.cancel(timePromise);
                timePromise = undefined;
                $scope.paracont = "立即获取";
                second = 59;
                $scope.notice(result.msg);
                $scope.changeImg();
            }
        });

    };
    $scope.changeImg = function () {
        var imgSrc = $("#imgObj");
        var src = imgSrc.attr("src");
        imgSrc.attr("src", chgUrl(src));
    }

    function chgUrl(url) {
        var timestamp = (new Date()).valueOf();
        url = url.substring(0, 20);
        if ((url.indexOf("&") >= 0)) {
            url = url + "×tamp=" + timestamp;
        } else {
            url = url + "?timestamp=" + timestamp;
        }
        return url;
    }

    var TEL_REGEXP = /^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;

    function validateTel(tel) {
        if (TEL_REGEXP.test(tel)) {
            return true;
        }
        return false;
    }

    $scope.levelSafeModelCancel = function () {
        $("#levelSafeModel").modal("hide");
    };

    $scope.goSetSafePhoneCance = function () {
        $("#safeModel").modal("hide");
    };

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

    $http.post('agentInfo/editAgentInfoDetail', angular.toJson({
        "agentNo": $stateParams.agentNo,
        "teamId": $stateParams.teamId
    })).success(function (msg) {
        if (!msg.status) {
            $scope.notice(msg.msg);
            return;
        }
        $scope.generateBusinessProductGrid(msg);
        $scope.pasreThe1stShareRuleData(msg.agentShare);
        $scope.appLogoHide = !msg.agentInfo.clientLogo;
        $scope.webLogoHide = !msg.agentInfo.managerLogo;
        $scope.happyBack.showStatus = (msg.happyBackList || []).length !== 0;
        $scope.happyBack.agentActivities = msg.happyBackList || [];
        $scope.happyBack.parentActivities = msg.parentHappyBackList || [];
        $scope.happyBack.activityTypeNoAndTeamId = msg.activityTypeNoAndTeamId || [];
        $scope.parseHappyBackData();

        if ($scope.happyBack.showStatus) {
            let agentActivity = $scope.happyBack.agentActivities[0];
            $scope.happyBack.showFullPrizeAmount = agentActivity.showFullPrizeAmount;
            $scope.happyBack.showNotFullDeductAmount = agentActivity.showNotFullDeductAmount;
            if (!agentActivity.showFullPrizeAmount && agentActivity.showNotFullDeductAmount) {
                $scope.happyBack.gridColumnDefs.splice(10, 1);
                $scope.happyBack.gridColumnDefs.splice(11, 1);
            } else if (!agentActivity.showNotFullDeductAmount && agentActivity.showFullPrizeAmount) {
                $scope.happyBack.gridColumnDefs.splice(11, 1);
                $scope.happyBack.gridColumnDefs.splice(12, 1);
            } else if (!agentActivity.showFullPrizeAmount && !agentActivity.showNotFullDeductAmount) {
                $scope.happyBack.gridColumnDefs.splice(10, 4);
            }
        }
        if (!msg.showStatusSwitchColumn) {
            $scope.happyBack.gridColumnDefs.splice(4, 1);
        }
        $scope.agent = msg.agentInfo;
        if ($scope.agent && $scope.agent.accountCity && $scope.agent.accountCity.substr($scope.agent.accountCity.length - 1, 1) === "市") {
            $scope.agent.accountCity = $scope.agent.accountCity.substr(0, $scope.agent.accountCity.length - 1);
        }
        $scope.getCities();
        $scope.getAreas();
        $scope.getCitiess();
        $scope.getHangName();
        $scope.getPosCnaps();

    }).error(function () {});

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
    }

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
            $scope.bankNames = [];
        });
    };
    $scope.bankNames = [];
    //支行
    $scope.getPosCnaps = function () {
        if ($scope.agent.accountType == 1) {	// 如果是对公帐号,则不校验
            return;
        }
        if ($scope.agent.accountNo == null || $scope.agent.accountNo == ""
            || $scope.agent.bankName == null || $scope.agent.bankName == ""
            || $scope.agent.accountCity == null || $scope.agent.accountCity == "") {
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
        if ($scope.agent.accountType == 1) {	// 如果是对公帐号,则不校验
            return;
        }
        if ($scope.agent.accountNo == null || $scope.agent.accountNo == "") {
            return;
        }
        var data = {"accountNo": $scope.agent.accountNo}
        $http.post("merchantInfo/getBackName", angular.toJson(data))
            .success(function (msg) {
                if (msg.bols) {
                    $scope.accountList = msg.lists;
                    angular.forEach(msg.lists, function (data) {
                        if ($scope.agent.bankName == data.bankName) {

                        } else {
                            $scope.agent.bankName = msg.lists[0].bankName;
                        }
                    });
                    $scope.agent.bankName = msg.lists[0].bankName;
                    if ($scope.agent.accountProvince != null && $scope.agent.accountProvince != "" && $scope.agent.accountCity != null && $scope.agent.accountCity != "") {
                        $scope.getPosCnaps();
                    }
                }
            });
    };

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

    $scope.amountNotNull = function (amount) {
        if (amount == null || amount === '') {
            $scope.notice("金额不能为空");
        }
    };
    $scope.defineSet = function () {
        var setPro = $scope.agent.setpro;
        var setValue = $scope.agent.setvalue;
        if (setPro === "" || setPro === undefined) {
            $scope.notice("请选择设置项");
            return;
        }
        if (setValue === "" || setValue === undefined) {
            $scope.notice("请输入设定值");
            return;
        }

        $scope.happyBack.agentActivities.forEach(function (e) {
            e[setPro] = setValue;
        })

    };

    $scope.selectionRow = function (row) {
        if (row.entity.isProxyed) {
            row.grid.rows.map(item => item.entity)
                .filter(item => row.entity.key6 != null && item.key6 === row.entity.key6 && item.key7 == 1)
                .forEach(item => item.isProxyed = true);
        } else {
            row.grid.rows.map(item => item.entity)
                .filter(item => row.entity.key6 != null && item.key6 === row.entity.key6 && row.entity.key7 == 1 && item.disabled != true)
                .forEach(item => item.isProxyed = false);
        }
    };

    //业务产品的开启、关闭进件
    $scope.switchProduct = function (row) {
        SweetAlert.swal({
                title: row.entity.key2 ? "确定开启?" : "确定关闭?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true,
                text: row.entity.key2 ? "" : "该代理商已有商户进件业务产品" + row.entity.key3 + "，请通知该代理商更改此类存量商户的业务产品，否则交易可能会产生负分润，是否继续关闭？"
            },
            function (isConfirm) {
                if (isConfirm) {
                    var param = {
                        "bpId": row.entity.key1,
                        "agentNo": $stateParams.agentNo,
                        "status": row.entity.key2 ? "1" : "0"
                    };
                    $http.post('agentInfo/updateAgentProStatus?' + $.param(param)).success(function (msg) {
                        if (msg.success) {
                            $scope.notice("操作成功！");
                        } else {
                            $scope.notice(msg.message);
                            row.entity.key2 = !row.entity.key2;
                        }
                    }).error(function () {
                        $scope.notice("操作失败！")
                        row.entity.key2 = !row.entity.key2;
                    });
                } else {
                    row.entity.key2 = !row.entity.key2;
                }
            });
    };

    //欢乐返活动是否启用
    $scope.switchHappyBack = function (row) {
        SweetAlert.swal({
                title: row.entity.status ? "确定开启?" : "确定关闭?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url: 'agentInfo/switchHappyBack?id=' + row.entity.id + '&status=' + row.entity.status + '&agentNode=' + row.entity.agentNode + '&activityTypeNo=' + row.entity.activityTypeNo,
                        method: 'GET'
                    }).success(function (data) {
                        if (!data.status) {
                            $scope.notice(data.msg);
                            row.entity.status = !row.entity.status;
                        }
                    }).error(function (data) {
                        $scope.notice("服务器异常,更新状态失败.");
                        row.entity.status = !row.entity.status;
                    })
                } else {
                    row.entity.status = !row.entity.status;
                }
            });
    };

    $scope.fr = [{text: '固定分润比例', value: 5}];

    //进入修改旧分润模块时，需要携带数据：服务的基本信息、原分润的历史记录
    $scope.editShare = function (record) {
        $('#editShareModel').modal('show');
        $scope.isSubmitSharing = false;
        $scope.shareInfo = record;
        $scope.newShareInfo = {efficientDate: "", profitType: 5, shareProfitPercent: 100};
        $scope.queryShare(record);
    };
    //点击确定时，发送请求到后台，提交新的分润的信息
    $scope.submitShare = function (record) {
        var efficientDate = $scope.newShareInfo.efficientDate;
        if (efficientDate <= new Date().getTime() + 5 * 60 * 1000) {
            $scope.notice("生效日期必须大于当前日期！");
            return;
        }
        $scope.isSubmitSharing = true;
        $scope.newShareInfo.shareId = record.id;
        $scope.newShareInfo.serviceType = record.serviceType;
        $scope.newShareInfo.serviceId = record.serviceId;
        $scope.newShareInfo.cardType = record.cardType;
        $scope.newShareInfo.holidaysMark = record.holidaysMark;
        $http.post('agentInfo/addNewShare', angular.toJson($scope.newShareInfo)).success(function (msg) {
            if (msg.status) {
                $scope.notice(msg.msg);
                $scope.queryShare(record);
            } else {
                $scope.notice(msg.msg);
            }
            $scope.isSubmitSharing = false;
        }).error(function () {
            $scope.notice("新的分润添加失败!");
            $scope.isSubmitSharing = false;
        })
    };

    //进入设置新分润的模块时
    $scope.ladderFr = function (row) {
        $('#ladderFrModel').modal('show');
        $scope.info = {entity: angular.copy(row.entity), row: row};
    };
    $scope.addShare = function (row) {
        $('#ladderFrModel').modal('hide');
        row.entity.profitType = $scope.info.entity.profitType;
        row.entity.shareProfitPercent = $scope.info.entity.shareProfitPercent;
    };
    $scope.shareHistoryInfo = [];
    //查询分润的历史记录
    $scope.queryShare = function (record) {
        $scope.shareHistoryInfo = [];
        $http.post('agentInfo/queryNewShareList', record.id).success(function (msg) {
            $scope.shareHistoryInfo[0] = $.extend({}, record, {
                effectiveStatus: 1
            });
            angular.forEach(msg, function (data, index) {
                $scope.shareHistoryInfo[$scope.shareHistoryInfo.length + index] = $.extend({}, data, {
                    effectiveStatus: 0
                });
            });
        }).error(function () {
        })
    };

    //删除分润
    $scope.deleteShare = function (row) {
        var efficientDate = row.entity.efficientDate;
        if (efficientDate <= new Date().getTime()) {
            $scope.notice("生效日期必须大于当前日期！");
            return;
        }
        if (confirm("你确定删除吗？")) {
            $http.post('agentInfo/delNewShare', row.entity.id).success(function (msg) {
                if (msg.status) {
                    $scope.notice("删除成功！");
                    $scope.queryShare($scope.shareInfo);
                } else {
                    $scope.notice("删除失败！");
                }
            }).error(function () {
                $scope.notice("删除失败！");
            })
        }
    };

    //分润的历史记录
    $scope.shareHistory = {
        data: "shareHistoryInfo",
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        columnDefs: [
            {
                field: 'id', displayName: '操作', width: 130,
                cellTemplate: '<a ng-click="grid.appScope.deleteShare(row)" ng-show="row.entity.efficientDate>' + new Date().getTime() + '"'
                    + '>删除</a>'
            },
            {field: 'efficientDate', displayName: '生效日期', width: 150, cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {
                field: 'profitType',
                displayName: '分润方式',
                width: 250,
                cellFilter: "formatDropping:" + angular.toJson($scope.fr)
            },
            // {field: 'income',displayName: '代理商收益',width: 150},
            {
                field: 'cost', displayName: '代理商成本', width: 150, cellTemplate:
                //如果不是体现服务，也就是交易服务，后面显示%
                    '<div ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001" style="width:98%;height:98%;"> '
                    + '{{row.entity[col.field]}}'
                    // + '<span ng-show="row.entity.effectiveStatus == 0">%</span>'
                    + '</div>'
                    //如果是体现服务，后面显示单位元
                    + '<div ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001" style="width:98%;height:98%;"> '
                    + '{{row.entity[col.field]}} 元'
                    + '</div>'
            },
            {
                field: 'shareProfitPercent',
                displayName: '代理商固定分润百分比',
                width: 180,
                cellTemplate: '<span ng-show="row.entity.shareProfitPercent!=null">' +
                    '{{row.entity.shareProfitPercent}}%</span>'
            },
            {
                field: 'effectiveStatus',
                displayName: '当前是否生效',
                width: 150,
                cellFilter: "formatDropping:" + angular.toJson($scope.bool)
            },
        ]
    };

    $scope.getShareList = function () {
        $scope.uibTabsetActive = "shareRuleInfo";
        //如果没有勾选业务产品，则不能发送请求
        var parentProducts = $scope.businessProduct.parentBpData;
        $scope.businessProduct.unchecked = parentProducts.filter(item => item.isProxyed).length === 0;
        let checkedBpId = parentProducts.filter(item => item.isProxyed && !item.disabled).map(item => item.key1);
        // 如果上次选择的业务产品和这次一样, 则不发送请求
        if ($scope.businessProduct.lastCheckedBpId.join(",") === checkedBpId.join(",")) {
            return false;
        }
        $scope.businessProduct.lastCheckedBpId = checkedBpId;
        // 如果当前没有选中新的业务产品,直接删除之前已经加的服务成本
        if (checkedBpId && checkedBpId.length === 0) {
            $scope.addShareRuleData([]);
            return;
        }
        $http({
            url: 'agentInfo/getNewAgentServicesByBpId?agentNo=' + $stateParams.agentNo,
            method: 'POST',
            data: checkedBpId
        }).success(function (data) {
            $scope.addShareRuleData(data.data);
        }).error(function () {});
    };

    /**
     * 获取要提交的数据
     */
    $scope.getSubmitData = function (updateType) {
        updateType = updateType || 'default';
        //获取选中业务产品的ID数组
        var parentProducts = $scope.businessProduct.parentBpData;
        let checkedBpId = parentProducts.filter(item => item.isProxyed && !item.disabled).map(item => item.key1);
        $scope.submitting = true;
        // bpIds = bpIds.concat($scope.bpIdNotSetShare);//合并
        // 已经设置分润成本的数据
        let updateShareData = $scope.shareRule.updateShareData.filter(item => item.cost !== undefined && item.cost !== '' && item.cost !== null) || [];
        if (updateType === 'setSafePhone'){       // 设置安全手机
            if ($scope.info.phoneNum === undefined || $scope.info.phoneNum == null || $scope.info.phoneNum.length !== 4) {
                $scope.notice("请输入正确的短信验证码");
                return;
            }
            if ($scope.info.imgNum === undefined || $scope.info.imgNum == null || $scope.info.imgNum.length !== 4) {
                $scope.notice("请输入正确的图形验证码");
                return;
            }
        }
        let happyBackList = [];
        if (!$scope.happyBack.disJoinIn) {
            //获取选中的欢乐返活动
            happyBackList = $scope.happyBack.agentActivities
            // 必须是选中的
                .filter(item => item.select)
                // 必须是对应组织id有展示出来的
                .filter(item => item.teamIds.filter(teamId => $scope.happyBack.showTeamIds.indexOf(teamId) >= 0).length > 0);
        }
        return {
            "info": $scope.info,
            "agentInfo": $scope.agent,
            "bpData": checkedBpId,
            "shareData": updateShareData,
            "happyBackList": happyBackList,
            'updateType': updateType
        };
    };
    /**
     * 全部提交
     */
    $scope.submit = function (updateType) {
        let data = $scope.getSubmitData(updateType);
        $scope.updateAgent(data);
    };

    $scope.updateAgent = function (data) {
        $scope.levelSafeModelCancel();
        $scope.goSetSafePhoneCance();
        $http.post('agentInfo/updateAgent', angular.toJson(data)).success(function (msg) {
            if (msg.status) {
                $scope.notice(msg.msg);
                $scope.submitting = false;
                $timeout(() => $state.go('agent.queryAgent', null, {reload: true}), 500);
            } else {
                if (msg.msg === "safephone") {
                    if (msg.safephone != null && msg.safephone.length == 11) {
                        $scope.info.oldphone = msg.safephone;
                        $("#levelSafeModel").modal("show");
                        $scope.info.imgNum = "";
                        $scope.info.phoneNum = "";
                        $scope.changeImg();
                    } else if(data.updateType === 'default'){
                        $("#safeModel").modal("show");
                    }
                    $scope.submitting = false;
                } else {
                    $scope.notice(msg.msg);
                    $scope.submitting = false;
                }
            }
        }).error(function () {
            $scope.submitting = false;
        });
    };

    $scope.selectBaseInfo = function () {
        $scope.uibTabsetActive = 'baseInfo';
    };
    $scope.selectBpInfo = function () {
        $scope.uibTabsetActive = 'bpInfo';
    };
    /**
     * 仅保存单页
     */
    $scope.saveCurrentPage = function () {
        let data = $scope.getSubmitData();
        if ($scope.uibTabsetActive === 'bpInfo') {
            // 仅仅保存业务产品,则分润信息和欢乐返数据不提交
            data.shareData = [];
            data.happyBackList = [];
        } else if ($scope.uibTabsetActive === 'shareRuleInfo') {
            // 仅仅保存分润信息,则欢乐返数据不提交(业务产品数据要提交)
            data.happyBackList = [];
        } else if ($scope.uibTabsetActive === 'happyBackInfo') {
            // 仅仅保存欢乐返数据,则业务产品,分润信息不提交
            data.bpData = [];
            data.shareData = [];
        } else {
            // 仅仅保存代理商基本信息,则业务产品,分润信息和欢乐返数据不提交
            data.bpData = [];
            data.shareData = [];
            data.happyBackList = [];
        }
        $scope.updateAgent(data);
    };
});

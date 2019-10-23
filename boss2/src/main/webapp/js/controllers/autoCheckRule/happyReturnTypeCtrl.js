/**
 * 欢乐返子类型
 */
angular.module('inspinia',['uiSwitch','infinity.angular-chosen']).controller('happyReturnTypeCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal,$timeout){
i18nService.setCurrentLang('zh-cn');

    $scope.info = {};
    $scope.types = [{text:"欢乐返",value:"009"}];
    $scope.paginationOptions = {
        pageNo: 1,
        pageSize: 10
    };

    $scope.hlfMer=[];
    //活跃商户类型
    $http.get('activity/selectHlfActivityMerchantRuleAllInfo')
        .success(function(result){
            if(!result)
                return;
            //响应成功
            for(var i=0; i<result.length; i++){
                $scope.hlfMer.push({value:result[i].ruleId,text:result[i].ruleId + " " + result[i].ruleName});
            }
        });
    //活跃商户类型
    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        $scope.hlfMeres = [];
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $http.post('activity/selectHlfActivityMerchantRuleAllInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length>0) {
                                for(var i=0; i<response.data.length; i++){
                                    $scope.hlfMeres.push({value:response.data[i].ruleId,text:response.data[i].ruleId + " " + response.data[i].ruleName});
                                }
                            }
                            $scope.hlfMer = $scope.hlfMeres;
                            oldValue = value;
                        });
                },800);
        }
    };

    // 欢乐返子类型的表格
    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'activityTypeNo',displayName:'欢乐返子类型编号',width:170},
            { field: 'activityTypeName',displayName:'欢乐返子类型名称',width:150},
            { field: 'activityCode',displayName:'欢乐返类型',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.types)},
            { field: 'ruleId',displayName:'活跃商户活动ID',width:150,
                cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.ruleId==null?"未参与":row.entity.ruleId}}</div>'},
            { field: 'ruleName',displayName:'活跃商户活动名称',width:150},
            { field: 'transAmount',displayName:'交易金额',width:150},
            { field: 'cashBackAmount',displayName:'返现金额',width:170},
            { field: 'repeatRegisterAmount',displayName:'重复注册返现金额',width:170},
            { field: 'emptyAmount',displayName:'首次注册不满扣(元)',width:170},
            { field: 'fullAmount',displayName:'首次注册满奖(元)',width:170},
            { field: 'repeatEmptyAmount',displayName:'重复注册不满扣(元)',width:170},
            { field: 'repeatFullAmount',displayName:'重复注册满奖(元)',width:170},
            { field: 'updateAgentStatus',displayName:'允许代理商更改',width:200,cellTemplate:
                    '<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.updateAgentStatus" ng-change="grid.appScope.updateAgentStatus(row)" /></span>'
            },
            { field: 'remark',displayName:'备注',width:100},
            { field: 'action',displayName:'操作',width:150,
                cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.editHappyReturnType\')" ng-click="grid.appScope.editHappyReturnType(row.entity.activityTypeNo)">修改</a>'+
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.deleteHappyReturnType\')" ng-click="grid.appScope.deleteHappyReturnType(row.entity.activityTypeNo)"> | 删除</a>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function () {
        $http({
            url: 'activity/selectHappyReturnType?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (data) {
            if(data.status){
                $scope.gridOptions.data = data.page.result;
                $scope.gridOptions.totalItems = data.page.totalCount;
                console.log(data.page.result);
                $scope.gridOptions.totalItems = data.page.totalCount;
            }else{
                $scope.notice(data.msg);
            }
        }).error(function(){
        });
    };
    $scope.query();

    $scope.clear = function () {
        $scope.info = {};
    };

	//修改
    $scope.editHappyReturnType = function(activityTypeNo){
        $http.post("activity/queryByActivityHardwareType",activityTypeNo).success(function (data) {
            if(data.status){
                $scope.happyReturnType = data.param;
                $("#activityCode").attr("disabled","disabled");
                $("#hardWardAddModel").modal("show");
                $("#activityTypeNo").show();
            }
        })
    };

	//添加
    $scope.hardWardAddModel = function(){
        $("#hardWardAddModel").modal("show");
        $("#activityCode").removeAttr("disabled");
        $("#activityTypeNo").hide();
        $scope.happyReturnType={hardId:"",price:"",targetAmout:"",cashBackAmount:"",emptyAmount:"",ruleId:"",
            fullAmount:"",cashLastAllyAmount:"",cashLastTeamAmount:"",repeatRegisterAmount:"",
            emptyAmount:"",fullAmount:"",repeatEmptyAmount:"",repeatFullAmount:""};
    };
	//返回
    $scope.cancel=function(){
        $scope.happyReturnType={};
        $('#hardWardAddModel').modal('hide');
    };

    //校验唯一性
    $scope.checkUnique = function(){
        if($scope.happyReturnType.activityTypeName){
            $http.post("activity/queryByActivityTypeName",$scope.happyReturnType).success(function (data) {
                if(data.status){
                    $scope.notice("欢乐返子类型名称已存在！");
                    $scope.happyReturnType.activityTypeName=data.param.activityTypeName;
                }
            })
        }
    };

	//新增欢乐返子类型
    $scope.saveHappyReturnType = function(){
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.happyReturnType.activityCode == undefined || $scope.happyReturnType.activityCode == null || $scope.happyReturnType.activityCode==""){
            $scope.notice("请选择欢乐返类型!");
            return;
        }
        if($scope.happyReturnType.activityTypeName == null || $scope.happyReturnType.activityTypeName==""){
            $scope.notice("欢乐返子类型名称不能为空!");
            return;
        }
        if($scope.happyReturnType.transAmount==null || $scope.happyReturnType.transAmount===""){
            $scope.notice("交易金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.transAmount!=0&&!isNum.test($scope.happyReturnType.transAmount)){
                $scope.notice("交易金额格式不正确!");
                return;
            }
        }

        if($scope.happyReturnType.activityCode!='010'){
            if($scope.happyReturnType.cashBackAmount==null || $scope.happyReturnType.cashBackAmount===""){
                $scope.notice("返现金额不能为空!");
                return;
            }else{
                if($scope.happyReturnType.cashBackAmount!=0&&!isNum.test($scope.happyReturnType.cashBackAmount)){
                    $scope.notice("返现金额格式不正确!");
                    return;
                }
            }
        }else {
            if($scope.happyReturnType.cashLastAllyAmount==null || $scope.happyReturnType.cashLastAllyAmount===""){
                $scope.notice("返上级盟主金额不能为空!");
                return;
            }else{
                if($scope.happyReturnType.cashLastAllyAmount!=0&&!isNum.test($scope.happyReturnType.cashLastAllyAmount)){
                    $scope.notice("返上级盟主金额格式不正确!");
                    return;
                }
            }
            if($scope.happyReturnType.cashLastTeamAmount==null || $scope.happyReturnType.cashLastTeamAmount===""){
                $scope.notice("返上级机构金额不能为空!");
                return;
            }else{
                if($scope.happyReturnType.cashLastTeamAmount!=0&&!isNum.test($scope.happyReturnType.cashLastTeamAmount)){
                    $scope.notice("返上级机构金额格式不正确!");
                    return;
                }
            }
        }
        if($scope.happyReturnType.repeatRegisterAmount==null || $scope.happyReturnType.repeatRegisterAmount===""){
            $scope.notice("重复注册返现金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.repeatRegisterAmount!=0&&!isNum.test($scope.happyReturnType.repeatRegisterAmount)){
                $scope.notice("重复注册返现金额格式不正确!");
                return;
            }
        }
        if($scope.happyReturnType.emptyAmount==null || $scope.happyReturnType.emptyAmount===""){
            $scope.notice("首次注册不满扣N值金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.emptyAmount!=0 && !isNum.test($scope.happyReturnType.emptyAmount)){
                $scope.notice("首次注册不满扣N值金额格式不正确!");
                return;
            }
        }
        if($scope.happyReturnType.fullAmount==null || $scope.happyReturnType.fullAmount===""){
            $scope.notice("首次注册满奖M值金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.fullAmount!=0 && !isNum.test($scope.happyReturnType.fullAmount)){
                $scope.notice("首次注册满奖M值金额格式不正确!");
                return;
            }
        }

        if($scope.happyReturnType.repeatEmptyAmount==null || $scope.happyReturnType.repeatEmptyAmount===""){
            $scope.notice("重复注册不满扣N值金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.repeatEmptyAmount!=0 && !isNum.test($scope.happyReturnType.repeatEmptyAmount)){
                $scope.notice("重复注册不满扣N值金额格式不正确!");
                return;
            }
        }
        if($scope.happyReturnType.repeatFullAmount==null || $scope.happyReturnType.repeatFullAmount===""){
            $scope.notice("重复注册满奖M值金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.repeatFullAmount!=0 && !isNum.test($scope.happyReturnType.repeatFullAmount)){
                $scope.notice("重复注册满奖M值金额格式不正确!");
                return;
            }
        }

        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        if($scope.happyReturnType.activityTypeNo == undefined || $scope.happyReturnType.activityTypeNo == null || $scope.happyReturnType.activityTypeNo==""){
            $http.post("activity/addHappyReturnType",$scope.happyReturnType)
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $("#saveHappyReturnType").modal("hide");
                        $scope.cancel();
                        $scope.query();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
            $scope.clear = function () {
                $scope.info = {};
            };
        }else{
            $http.post("activity/editHappyReturnType",$scope.happyReturnType)
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $("#saveHappyReturnType").modal("hide");
                        $scope.cancel();
                        $scope.query();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
            $scope.clear = function () {
                $scope.info = {};
            };
        }

    };

    //删除欢乐返子类型
    $scope.deleteHappyReturnType = function(activityTypeNo){
        SweetAlert.swal({
                title: "确认删除欢乐返子类型?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('activity/deleteHappyReturnType',activityTypeNo)
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.query();
                        }).error(function(msg){
                        $scope.notice(msg.msg);
                    });
                }
            });
    };

    $scope.updateAgentStatus=function(row){
        if(row.entity.updateAgentStatus){
            $scope.serviceText = "确定开启？";
        } else {
            $scope.serviceText = "确定关闭？";
        }
        SweetAlert.swal({
                title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(row.entity.updateAgentStatus==true){
                        row.entity.updateAgentStatus=1;
                    } else if(row.entity.updateAgentStatus==false){
                        row.entity.updateAgentStatus=0;
                    }
                    $http.post("activity/updateAgentStatusSwitch",row.entity)
                        .success(function(data){
                            if(data.status){
                                $scope.notice("操作成功！");
                                $scope.query();
                            }else{
                                if(row.entity.status==true){
                                    row.entity.status = false;
                                } else {
                                    row.entity.status = true;
                                }
                                $scope.notice("操作失败！");
                            }
                        })
                        .error(function(data){
                            if(row.entity.status==true){
                                row.entity.status = false;
                            } else {
                                row.entity.status = true;
                            }
                            $scope.notice("服务器异常")
                        });
                } else {
                    if(row.entity.status==true){
                        row.entity.status = false;
                    } else {
                        row.entity.status = true;
                    }
                }
            });

    };

    $scope.export = function () {
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
                    $scope.exportInfoClick("activity/exportActivityHardwareType",{"info" : angular.toJson($scope.info)});
                }
            });
    }

    //console.log();
    //转换日期格式
    Date.prototype.format = function(fmt) {
        var o = {
            "M+" : this.getMonth()+1,			//月份
            "d+" : this.getDate(),				//日
            "h+" : this.getHours(),				//小时
            "m+" : this.getMinutes(),			//分
            "s+" : this.getSeconds(),			//秒
            "q+" : Math.floor((this.getMonth()+3)/3),	//季度
            "S"  : this.getMilliseconds()		//毫秒
        };
        if(/(y+)/.test(fmt)) {
            fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        }
        for(var k in o) {
            if(new RegExp("("+ k +")").test(fmt)){
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
            }
        }
        return fmt;
    };

});

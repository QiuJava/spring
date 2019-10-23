/**
 * 调单回复
 */
angular.module('inspinia',['angularFileUpload']).controller('replyEditCtrl',function($scope,$state,$http,$stateParams,FileUploader,$location){

    $scope.info = {};
    $scope.record = {};
    $scope.transStatuses=initData["TRANS_STATUS"];
    $scope.replyResult=initData["ORDER_REPLY_RESULT"];
    $scope.dealStatuses=[{text:"未处理",value:'0'},{text:"已处理",value:'1'},{text:"已处理",value:'2'},{text:"已处理",value:'3'},
        {text:"已处理",value:'4'},{text:"已处理",value:'5'},{text:"已处理",value:'6'},{text:"已处理",value:'7'},
        {text:"已回退",value:'8'}];
    $scope.replyStatuses=[{text:"未回复",value:'0'},{text:"未回复（下级已提交）",value:'1'},{text:"已回复",value:'2'},{text:"逾期未回复",value:'3'},
        {text:"逾期未回复（下级已提交）",value:'4'},{text:"逾期已回复",value:'5'}];
    $scope.orderInfo = function () {
        $http.get('surveyOrder/info?id='+$stateParams.id).success(
            function (data) {
                $scope.info=data.order;
        });
    }

    //是否可以修改
    $scope.flag = true;
    $scope.editFlag = function () {
        if(null == $scope.record.id){
            return false;
        }
        return $scope.flag;
    }

    $scope.showEditButton = true;
    $scope.initHtml = function () {
        if($stateParams.type == 1){
            $scope.flag = false;
            $scope.showEditButton = false;
        }
    }
    $scope.initHtml();

    $scope.type = $stateParams.type;
    $scope.edit = function () {
        $scope.flag = false;
        $scope.type = 1;
    }

    //查询回复
    $scope.fileNum = 0;
    $scope.getRecord = function () {
        $http.get('reply/getRecord?orderNo='+$stateParams.orderNo).success(
            function (data) {
                $scope.record=data.record;
                $scope.oldTemplateList=data.record.fileList;
                if($scope.oldTemplateList!=null){
                    $scope.fileNum = $scope.oldTemplateList.length;
                }
                $scope.getCitiess();
            }
        );
    }
    $scope.getRecord();
    
    $scope.getAreaLists=function(name,type,callback){
        if(name == null || name=="undefine"){
            return;
        }
        $http.post('areaInfo/getAreaByName','name='+name+'&&type='+type,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
            callback(data);
        }).error(function(){
        });
    }
    //省，加载页面时自动触发
    $scope.getAreaLists(0,"p",function(data){
        $scope.provinceGroups = data;
    });
    //市，页面上ng-change生时触发
    $scope.getCitiess = function() {
        $scope.areaGroups=[];
        $scope.getAreaLists($scope.record.province,"",function(data){
            $scope.cityGroups = data;
        });
    }


    //上传,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload/fileUploadAll.do',
        queueLimit: 6-$scope.fileNum,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        autoUpload:false,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 6-$scope.fileNum;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };


    $scope.delete = function ($event, item) {
        var idx = $scope.oldTemplateList.indexOf(item);
        $scope.oldTemplateList.splice(idx, 1);
        if($scope.oldTemplateList!=null){
            $scope.fileNum = $scope.oldTemplateList.length;
        }

    }

    //提交回复
    $scope.submitting = false;
    $scope.submit = function () {
        if($scope.submitting){
            return;
        }
        if(isBlank($scope.record.replyResult) || isBlank($scope.record.merName) || isBlank($scope.record.province) ||
            isBlank($scope.record.city) || isBlank($scope.record.transAddress) | isBlank($scope.record.merMobile)){
            $scope.notice("必要参数不能为空!");
            return;
        }
        if(!isBlank($scope.record.replyRemark)){
            if($scope.record.replyRemark.length > 100){
                $scope.notice("调单回复不能超过100个字。")
                return;
            }
        }
        $scope.tall="";
        if($scope.uploader.queue.length>0){
            uploader.onSuccessItem = function(fileItem,response, status, headers) {
                // 上传成功后的回调函数，在里面执行提交
                if (response.url != null) { // 回调参数url
                    if($scope.tall!=null){
                        if($scope.tall==""){
                            $scope.tall=response.url+",";
                        }else{
                            $scope.tall=$scope.tall+response.url+",";
                        }
                    }
                }
            };
            uploader.onCompleteAll = function() {
                $scope.commitData();
            };
            $scope.uploader.uploadAll();
        }else {
            $scope.commitData();
        }
    }

    $scope.commitData = function () {
        if($scope.oldTemplateList!=null){
            for(var i=0;i<$scope.oldTemplateList.length;i++){
                $scope.tall=$scope.tall+$scope.oldTemplateList[i]+",";
            }
        }
        $scope.record.replyFilesName=$scope.tall.substring(0,$scope.tall.length-1);
        $http.post('reply/confirm?type='+$scope.type, $scope.record).success(
            function (data) {
                if(data.status){
                    $scope.notice('提交成功');
                    $location.url("/riskControlManagement/surveyOrder");
                }else {
                    $scope.notice(data.msg);
                }
        });
    }

    /**
     *下载文件
     */
    $scope.updateFile = function (name) {
        location.href = "upload/updateFile?name=" + name;
    };


    //参数非空判断
    isBlank = function (param) {
        if(param=="" || param==null ){
            return true;
        }else{
            return false;
        }
    }

});


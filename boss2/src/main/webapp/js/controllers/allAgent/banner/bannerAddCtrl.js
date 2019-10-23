/**
 * banner新增
 */
angular.module('inspinia',['angularFileUpload']).controller('bannerAllAgentAddCtrl',function($scope,$http,i18nService,$state,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.applyTypeSelect = [{text:"全部",value:""},{text:"公众号+App",value:"1"},{text:"公众号",value:"2"},{text:"App",value:"3"}];
    $scope.applyTypeStr=angular.toJson($scope.applyTypeSelect);

    $scope.postionTypeSelect = [{text:"全部",value:""},{text:"首页",value:"1"}];
    $scope.postionTypeStr=angular.toJson($scope.postionTypeSelect);

    //清空
    $scope.clear=function(){
        $scope.addInfo={oemNo:"",applyType:"3",postionType:""};
    };
    $scope.clear();

    //组织列表
    $scope.oemList=[];
    $http.post("awardParam/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].brandCode,text:list[i].brandName});
                    }
                }
            }
        });


    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.svaeBanner(response.url);
        }
    };

    $scope.submitting = false;
    //新增banner
    $scope.addBanner = function(){
        if($scope.submitting){
            return;
        }
        if(uploaderImg.queue.length<1){
            $scope.notice("图片不能为空!");
            return;
        }
        if($scope.addInfo.upTime==null||$scope.addInfo.upTime==""
            ||$scope.addInfo.downTime==null||$scope.addInfo.downTime==""){
            $scope.notice("上/下线时间不能为空!");
            return;
        }
        $scope.submitting = true;
        uploaderImg.uploadAll();// 上传消息图片
    };

    $scope.svaeBanner=function (url) {
        $scope.infoSub = angular.copy($scope.addInfo);
        $scope.infoSub.imgUrl=url;
        if($scope.infoSub.link!=null){
            $scope.infoSub.link=encodeURIComponent($scope.infoSub.link);
        }
        $http.post("bannerAllAgent/addBanner",
            "info="+angular.toJson($scope.infoSub),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('allAgent.banner',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    }


});
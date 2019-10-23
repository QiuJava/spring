/**
 * 新增商户迁移
 */
angular.module('inspinia', ['angularFileUpload']).controller('addMerchantMigrateCtrl',function(i18nService,$scope,$http,$timeout,$state,$stateParams,FileUploader,$window){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={agentNo:"-1",agentNode:"-1",oaNo:""};
	$scope.agentNo = [{text:"全部",value:"-1"}];
	$scope.agentNode = [{text:"全部",value:"-1"}];
	var opts = {agentNo:"",agentNode:"",goSn:"2",oaNo:""};
	var uploader = $scope.uploader = new FileUploader({
        url: 'merchantMigrate/addMigrate',
        formData:[opts],
        queueLimit: 1,   //文件个数 
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
	
	//过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
        	
            return this.queue.length < 1;
        }
    });
	
	//代理商
	$http.post("merchantMigrate/getAllAgentInfo").success(function(msg){
	 	for(var i=0; i<msg.length; i++){
    		$scope.agentNo.push({value:msg[i].agentNo,text:msg[i].agentName});
    	}
 	});
	 
	$scope.getNodeAgent=function(){
		$scope.agentNode = [{text:"全部",value:"-1"}];
		$scope.info.agentNode = "-1";
		if($scope.info.agentNo !="-1"){
			$http({
				method:"get",
				url:"merchantMigrate/findNodeAgent?agentNo="+$scope.info.agentNo,
				headers: {'Content-Type': 'application/x-www-form-urlencoded' } 
			}).success(function(result){
				for(var i=0; i<result.length; i++){
		    		$scope.agentNode.push({value:result[i].agentNo,text:result[i].agentName});
		    	}
			}).error(function(){
				$scope.notice("加载所属代理商失败,请联系管理员");
			});
		}
	}
	
	
	$scope.addMerchantMigrate = function(){
		if($scope.info.agentNo=="-1"){
			$scope.notice("请选择目标一级代理商"+$scope.info.agentNo);
			return;
		}
		
		if($scope.info.agentNode == "-1"){
			$scope.notice("请选择目标直属代理商"+$scope.info.agentNode);
			return;
		}
		
		if($scope.info.oaNo!="" && $scope.info.oaNo.length>30){
			$scope.notice("关联OA单号超过系统限制30位，本项非必填项");
			return;
		}
		
		if($scope.uploader.queue[0] == null){
			$scope.notice("你若上传附件，我必感激涕零");
			return;
		}
		
		var fileNmaes = uploader.queue[0].file.name.substring(uploader.queue[0].file.name.lastIndexOf("."));
		if(fileNmaes != ".xls" && fileNmaes != ".xlsx"){
			$scope.notice(fileNmaes+"请上传后缀名为 .xls 或 .xlsx 的有效Excel文件!");
			return false;
		}
		$scope.uploader.queue[0].formData[0].agentNo=$scope.info.agentNo;
		$scope.uploader.queue[0].formData[0].agentNode=$scope.info.agentNode;
		$scope.uploader.queue[0].formData[0].oaNo=$scope.info.oaNo;
		$scope.uploader.uploadAll();//上传
		$scope.uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
	   		$scope.notice(response.addMsg);
	   		/*
	    if(response.result){
	    	$scope.notice(response.msg);
	    	$state.transitionTo('merchant.migrate',null,{reload:true});
	    }else{
	    	$scope.notice("====="+response.msg);
	    }
	    */
		};	
		return false;
	}
	
	
	
})
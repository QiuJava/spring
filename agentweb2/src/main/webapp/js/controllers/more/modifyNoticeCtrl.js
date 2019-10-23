/**
 * 修改公告
 */
angular.module('inspinia',['angularFileUpload']).controller("modifyNoticeCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
	//数据源
	$scope.sysType=[{text:"商户",value:'1'},{text:"代理商",value:'2'}];
	$scope.team = [{text:'全部',value:'0'},{text:'直营',value:'1'},{text:'非直营',value:'2'}];
	$scope.agentTypes = [{text:'所有代理商',value:'7'},{text:'一级代理商',value:'8'}];
	$scope.fileFlagHide = false;	//“附件”，修改公告时，显示“点击下载”
	$scope.imgFlagHide = false;
	var uploadFileFlag = true;		//是否可以提交数据，默认是可以提交的，当有文件准备上传时，为false，上传完成后置为true
	var uploadImgFlag = true;	
	var removeFileFlag = false;	//是否删除附件，默认为false
	var removeImgFlag = false; 		//是否删除图片
	
	$http.get('notice/selectInfoById/'+$stateParams.id).success(function(msg){
		$scope.noticeInfo = msg.data;
		//如果附件为空，则隐藏
		if(msg.data.attachment == null){
			$scope.fileFlagHide = true;
		}
		//如果消息图片为空，则隐藏
		if(msg.data.messageImg == null){
			$scope.imgFlagHide = true;
		}
		if(msg.data.agentNo == '0'){
			$scope.baseInfo.agentBusiness = '0';
		}
	});
	
	
	// 上传附件,定义控制器路径
	var uploaderFile = $scope.uploaderFile = new FileUploader({
		url : 'upload/fileUpload.do'
	});
	// 过滤长度，只能上传一个
	uploaderFile.filters.push({
		name : 'imageFilter',
		fn : function(item, options) {
			return this.queue.length < 1;
		}
	});
	// 过滤格式
	uploaderFile.filters.push({name : 'imageFilter',fn : function(
		item /* {File|FileLikeObject} */,options) {
			var name = '|'+ item.name.slice(item.name.lastIndexOf('.') + 1)+ '|';
			return '|jpg|png|jpeg|bmp|gif|doc|docx|pdf|ppt|xls|xlsx|'.indexOf(name) !== -1;
		}
	});
	uploaderFile.onAfterAddingFile = function(fileItem) {
		uploadFileFlag = false;
		$scope.fileFlagHide = true;
	}
	uploaderFile.removeFromQueue = function(value){
		uploadFileFlag = true;
		if($scope.noticeInfo.attachment != null){
			$scope.fileFlagHide = false;
		}
		
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}

	//删除附件的点击事件，removeFileFlag为true表示数据提交之前清除附件的数据
	//并隐藏附件
	$scope.removeAnnex = function(){
		removeFileFlag = true;
		$scope.fileFlagHide = true;
	}
	//删除图片
	$scope.removeImg = function(){
		removeImgFlag = true;
		$scope.imgFlagHide = true;
	}
	
	//上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数 
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
         fn: function(item /*{File|FileLikeObject}*/, options) {
             var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
             return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
         }
     });
     uploaderImg.onAfterAddingFile = function(fileItem) {
    	 uploadImgFlag = false;
    	 $scope.imgFlagHide = true;
 	}
 	uploaderImg.removeFromQueue = function(value){
 		uploadImgFlag = true;
 		var index = this.getIndexOfItem(value);
         var item = this.queue[index];
         if (item.isUploading) item.cancel();
         this.queue.splice(index, 1);
         item._destroy();
         this.progress = this._getTotalProgress();
 	}
	
	 $scope.submit = function(){
		 $scope.submitting = true;
		 if(removeFileFlag){	//如果清除附件，设置附件地址为null
	    		delete $scope.noticeInfo.attachment;
	    	}
	    	if(removeImgFlag){
	    		delete $scope.noticeInfo.messageImg;
	    	}
		 //1.没有需要上传的
		 if(uploadFileFlag && uploadImgFlag){
			 $scope.submitData();
		 }
		 //2.只有消息图片需要上传
		 else if(uploadFileFlag && !uploadImgFlag){
			uploaderFile.uploadAll();// 上传宣传图片
			uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.noticeInfo.messageImg = response.url;
					$scope.submitData();
				}
	        }
		 }
		 //3.只有附件需要上传
		 else if(!uploadFileFlag && uploadImgFlag){
			 uploaderImg.uploadAll();
			 uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { 
					$scope.noticeInfo.attachment = response.url;
					$scope.submitData();
				}
	        }
		 }
		 //4.消息图片和附件都需要上传
		 else if(!uploadFileFlag && !uploadImgFlag){
			 uploaderFile.uploadAll();// 上传宣传图片
				uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
					if (response.url != null) { // 回调参数url
						$scope.noticeInfo.messageImg = response.url;
						uploaderImg.uploadAll();
						 uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
							if (response.url != null) { 
								$scope.noticeInfo.attachment = response.url;
								$scope.submitData();
							}
				        }
					}
		        }
		 }
	 }
	
    $scope.submitData = function(){
    	$http.post("notice/saveNotice.do",angular.toJson($scope.noticeInfo))
		.success(function(msg){
			$scope.notice(msg.msg);
			if(msg.status){
				$state.transitionTo('more.sentNotices',null,{reload:true});
			}
			$scope.submitting = false;
		}).error(function(){
			$scope.submitting = false;
		});
    }
});


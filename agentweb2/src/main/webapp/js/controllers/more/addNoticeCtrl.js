/**
 * 添加公告
 */
angular.module('inspinia',['angularFileUpload']).controller("addNoticeCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
	//数据源
	$scope.sysType=[{text:"商户",value:'1'},{text:"代理商",value:'2'}];
	$scope.noticeInfo = {title:null,content:null,attachment:null,sysType:'2',receiveType:'7'};
	//用来决定公告接受对象为代理商时的两个标志位
	$scope.agentTypes = [{text:'所有下级代理商',value:'7'},{text:'直接下级代理商',value:'8'}];
	var uploaderFileFlag = true;	//是否可以提交数据，默认是可以提交的，当有文件准备上传时，为false，上传完成后置为true
	$scope.fileFlagHide = true;		//“附件”，新增公告时，隐藏“点击下载”
	$scope.imgFlagHide = true;
	
	//上传图片,定义控制器路径
	var uploaderImgFlag = true;
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
    	 uploaderImgFlag = false;
 	}
 	uploaderImg.removeFromQueue = function(value){
 		uploaderImgFlag = true;
 		var index = this.getIndexOfItem(value);
         var item = this.queue[index];
         if (item.isUploading) item.cancel();
         this.queue.splice(index, 1);
         item._destroy();
         this.progress = this._getTotalProgress();
 	}
	
	// 上传附件,定义控制器路径
 	var uploaderFileFlag = true;
 	  var uploaderFile = $scope.uploaderFile = new FileUploader({
 	        url: 'upload/fileUpload.do',
 	        queueLimit: 1,   //文件个数 
 	    });
 	    //过滤长度，只能上传一个
 	    uploaderFile.filters.push({
 	        name: 'imageFilter',
 	        fn: function(item, options) {
 	            return this.queue.length < 1;
 	        }
 	    });
 	    //过滤格式
 	     uploaderFile.filters.push({
 	         name: 'imageFilter',
 	         fn: function(item /*{File|FileLikeObject}*/, options) {
 	             var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
 	             return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
 	         }
 	     });
 	     uploaderFile.onAfterAddingFile = function(fileItem) {
 	    	 uploaderFileFlag = false;
 	 	}
 	 	uploaderFile.removeFromQueue = function(value){
 	 		uploaderFileFlag = true;
 	 		var index = this.getIndexOfItem(value);
 	         var item = this.queue[index];
 	         if (item.isUploading) item.cancel();
 	         this.queue.splice(index, 1);
 	         item._destroy();
 	         this.progress = this._getTotalProgress();
 	 	}

	var uploader = $scope.uploader = new FileUploader({
		url : 'upload/fileUpload.do'
	});
	// 过滤长度，只能上传一个
	uploader.filters.push({
		name : 'imageFilter',
		fn : function(item, options) {
			return this.queue.length < 1;
		}
	});
	// 过滤格式
	uploader.filters.push({name : 'imageFilter',fn : function(
		item /* {File|FileLikeObject} */,options) {
			var name = '|'+ item.name.slice(item.name.lastIndexOf('.') + 1)+ '|';
			return '|jpg|png|jpeg|bmp|gif|doc|docx|pdf|ppt|xls|xlsx|'.indexOf(name) !== -1;
		}
	});
	uploader.onAfterAddingFile = function(fileItem) {
		uploaderFileFlag = false;
	}
	uploader.removeFromQueue = function(value){
		uploaderFileFlag = true;
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}
	
	 $scope.submit = function(){
		 $scope.submitting = true;
	 		//1.没有等待上传的
	 		if(uploaderImgFlag && uploaderFileFlag){
	 			$scope.submitData();
	 		}
	 		//2.有消息图片
	 		if(!uploaderImgFlag && uploaderFileFlag){
	 			uploaderImg.uploadAll();// 上传消息图片
	 			uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
					if (response.url != null) { // 回调参数url
						$scope.noticeInfo.messageImg= response.url;
						alert($scope.noticeInfo);
						$scope.submitData();
					}
		        }
	 		}
	 		//3.消息图片没有，附件有
			if(uploaderImgFlag && !uploaderFileFlag){
				//有上传消息图片等待上传
				uploaderFile.uploadAll();// 上传附件
				uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
					if (response.url != null) { // 回调参数url
						$scope.noticeInfo.attachment = response.url;
						$scope.submitData();
					}
		        }
			}
			//4.消息图片有，附件有
			if(!uploaderImgFlag && !uploaderFileFlag){
				uploaderImg.uploadAll();// 上传消息图片
				uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
					if (response.url != null) { // 回调参数url
						$scope.noticeInfo.messageImg= response.url;
						uploaderFile.uploadAll();// 上传附件
						uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
							if (response.url != null) { // 回调参数url
								$scope.noticeInfo.attachment = response.url;
								$scope.submitData();
							}
						}
					}
		        }
			}
	 }
	
    $scope.submitData = function(){
    	$http.post("notice/addNotice.do",angular.toJson($scope.noticeInfo))
		.success(function(msg){
			if(msg.success){
				$scope.notice(msg.msg);
				$scope.submitting = false;
				$state.transitionTo('more.addNotice',null,{reload:true});
			} else {
				$scope.notice(msg.msg);
				$scope.submitting = false;
			}
		}).error(function(){
			$scope.submitting = false;
		});
    }
});

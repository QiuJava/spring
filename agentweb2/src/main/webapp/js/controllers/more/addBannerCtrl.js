/**
 * 添加App Banner
 */
angular.module('inspinia',['angularFileUpload']).controller("addBannerCtrl",function($scope, $http, $state, $stateParams, FileUploader) {
	//数据源
	$scope.baseInfo = {bannerAttachment:null,onlineTime : null,
			offlineTime : null};//使用之前需要初始化
	$scope.imgFlagHide = true;	//新增banner时，隐藏图片
	var uploadFlag = true;		//是否可以提交数据，默认是可以提交的，当有文件准备上传时，为false，上传完成后置为true
    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数 
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
     uploader.filters.push({
         name: 'imageFilter',
         fn: function(item /*{File|FileLikeObject}*/, options) {
             var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
             return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
         }
     });
     uploader.onAfterAddingFile = function(fileItem) {
 		uploadFlag = false;
 	}
 	uploader.removeFromQueue = function(value){
 		uploadFlag = true;
 		var index = this.getIndexOfItem(value);
         var item = this.queue[index];
         if (item.isUploading) item.cancel();
         this.queue.splice(index, 1);
         item._destroy();
         this.progress = this._getTotalProgress();
 	}
    	 
 	$scope.submit = function(){
 		if(uploadFlag){
 			$scope.submitData();
 		} else {
 			uploader.uploadAll();//上传
 			uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
 			     if(response.url != null){
 			    	$scope.baseInfo.bannerAttachment = response.url;
 			    	$scope.submitData();
 			     }
 			}
 		}
 	}
 	
	$scope.submitData = function(){
		var data = {"banner":$scope.baseInfo};
		$http.post('banner/saveBanner.do',data).success(function(msg) {
			 $scope.notice(msg.msg);
		}).error(function(){
		});
    }
	
});

/**
 * 收单商户导入
 */
angular.module('inspinia',['angularFileUpload']).controller('acqSingleImportCtrl',function($scope,$state,$http,$stateParams,FileUploader,$window){

	
	var aa = [];
	//上传图片,定义控制器路径


    var uploader = $scope.uploader = new FileUploader({
        url: 'transInfoAction/acqSingleImport',
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
    
     $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
    	 uploader.clearQueue();
     }

     $scope.commit=function(){
    	 uploader.uploadAll();//上传
    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
             $scope.notice(response.msg);
		 };	
		 return false;
     }
})
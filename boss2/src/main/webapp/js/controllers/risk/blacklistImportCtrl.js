angular.module('inspinia',['angularFileUpload']).controller('blacklistimportCtrl',function($scope,$state,$http,$stateParams,FileUploader,$window){

	$scope.acqOrgs=[];
	$scope.resultStr="";
	var aa = [];
	//上传图片,定义控制器路径
	var opts = {acqOrg:""};
	if(_parameterName)
		opts[_parameterName] = _token;
    var uploader = $scope.uploader = new FileUploader({
        url: 'riskRollAction/importBlacklist',
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
    
     $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
    	 uploader.clearQueue();
     }

     $scope.commit=function(){
    	 
    	 if(uploader.queue.length==0){
    		 $scope.notice("请选择上传文件");
    		 return ;
    	 };
    	 
    	 $scope.submitting = true;
    	 
    	 uploader.uploadAll();//上传
    	 uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
		     if(response.result){
		    	 
		    	 $("#resultModel").modal("show");
		    	 $scope.resultStr = response.msg;
		    	 $scope.submitting = false;
		    	 //$state.transitionTo('risk.blacklistQuery',null,{reload:true});
		     }else{
		    	 $("#resultModel").modal("show");
		    	 $scope.resultStr = response.msg;
		    	 $scope.submitting = false;
		    	//$scope.notice(response.msg);
		     }
		 };	
		 return false;
     }
     
     $scope.cancel=function(){
    		$scope.hardWare={hardId:"",price:"",targetAmout:""};
    		$('#resultModel').modal('hide');
    	}
     
})
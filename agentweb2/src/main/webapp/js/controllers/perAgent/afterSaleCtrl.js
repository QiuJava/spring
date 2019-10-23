/**
 * 申购售后订单
 */
angular.module('inspinia',['ngFileUpload']).controller('afterSaleCtrl',function($scope,$rootScope,$http,$state,$stateParams,
		i18nService,$document,SweetAlert,Upload){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {agentNo:''};
	$scope.resetForm = function () {
		$scope.baseInfo = {agentNo:''};
	}
	$scope.saleTypeList = [{text:"收到的商品存在损坏",value:'1'},{text:"发错货/漏发货",value:'2'},{text:"未收到货",value:'3'},{text:"其他",value:'4'}];
	$scope.statusList = [{text:"待机构处理",value:'0'},{text:"已处理",value:'2'},{text:"已取消",value:'3'}];
	$scope.handlers = [{text:"机构",value:'1'},{text:"平台",value:'2'}];

	$scope.waitHandleTotal = 0;
    $scope.waitHandleTotalTreeDays = 0;
    $scope.waitHandleTotalSevenDays = 0;
    $scope.query = function () {
        $http({
            url: 'perAgent/selectPaAfterSale?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if(msg.status){
                $scope.waitHandleTotal = msg.waitHandleTotal;
                $scope.waitHandleTotalTreeDays = msg.waitHandleTotalTreeDays;
                $scope.waitHandleTotalSevenDays = msg.waitHandleTotalSevenDays;
                $scope.myGrid.data = msg.page.result;
                $scope.myGrid.totalItems = msg.page.totalCount;
            }else {
                $scope.notice(msg.msg);

            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.query();

	$scope.columnDefs = [
		{field: 'orderNo',displayName: '售后编号',width: 150,pinnable: false,sortable: false},
		{field: 'payOrder',displayName: '关联订单编号',width: 150,pinnable: false,sortable: false},
		{field: 'saleType',displayName: '售后类型',width:150, cellFilter:"formatDropping:" + angular.toJson($scope.saleTypeList)},
		{field: 'applyDesc',displayName: '售后说明',width:150},
		{field: 'applyImg',displayName: '图片凭证',width:250,
            cellTemplate:'<div style="text-align:center;">' +
            '<div style="overflow:hidden; display:inline-block;">' +
            '<div ng-repeat="item in row.entity.applyImg.split(\',\')" style="float: left;">'+
            '<a href="{{row.entity.applyImg1}}" ng-show="row.entity.applyImg1!=null" fancybox rel="group"><img style="width:70px;height:35px;" ng-src="{{row.entity.applyImg1}}"/></a>' +
            '<a href="{{row.entity.applyImg2}}" ng-show="row.entity.applyImg2!=null" fancybox rel="group"><img style="width:70px;height:35px;" ng-src="{{row.entity.applyImg2}}"/></a>' +
            '<a href="{{row.entity.applyImg3}}" ng-show="row.entity.applyImg3!=null" fancybox rel="group"><img style="width:70px;height:35px;" ng-src="{{row.entity.applyImg3}}"/></a>' +
            '</div></div></div>'},
        {field: 'status',displayName: '售后状态',width:150, cellFilter:"formatDropping:" + angular.toJson($scope.statusList)},
        {field: 'handler',displayName: '处理人',width:150, cellFilter:"formatDropping:" + angular.toJson($scope.handlers)},
        {field: 'dealDesc',displayName: '处理结果',width:150},
        {field: 'dealImg',displayName: '图片凭证',width:250,
            cellTemplate:'<div style="text-align:center;">' +
            '<div style="overflow:hidden; display:inline-block;">' +
            '<div ng-repeat="item in row.entity.dealImg.split(\',\')" style="float: left;">'+
            '<a href="{{row.entity.dealImg1}}" ng-show="row.entity.dealImg1!=null" fancybox rel="group"><img style="width:70px;height:35px;" ng-src="{{row.entity.dealImg1}}"/></a>' +
            '<a href="{{row.entity.dealImg2}}" ng-show="row.entity.dealImg2!=null" fancybox rel="group"><img style="width:70px;height:35px;" ng-src="{{row.entity.dealImg2}}"/></a>' +
            '<a href="{{row.entity.dealImg3}}" ng-show="row.entity.dealImg3!=null" fancybox rel="group"><img style="width:70px;height:35px;" ng-src="{{row.entity.dealImg3}}"/></a>' +
            '</div></div></div>'},
        {field: 'applyTime',displayName: '提交日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'dealTime',displayName: '处理日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		{field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                "<span ng-show='row.entity.status == 0'><a ng-click='grid.appScope.showDeliver(row)'>立即处理 </a></span>"}
	];
	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: $scope.columnDefs,
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
		}
	};
	
	//==============================================
	var fileList={};
	var delParent;
	var defaults = {
		fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
		fileSize         : 1024 * 1024 * 3                 // 上传文件的大小 3M
	};
	var arrayObj = new Array();
	//点击图片的文本框
	$(".file").change(function(){	 
		var idFile = $(this).attr("id");
		var file = document.getElementById(idFile);
		var imgContainer = $(this).parents(".z_photo"); //存放图片的父亲元素
		fileList = file.files; //获取的图片文件
		arrayObj.push(fileList);
		var input = $(this).parent();//文本框的父亲元素
		var imgArr = [];
		//遍历得到的图片文件
		var numUp = imgContainer.find(".up-section").length;
		var totalNum = numUp + fileList.length;  //总的数量
		if(fileList.length > 3 || totalNum > 3 ){
			alert("上传图片数目不可以超过3个，请重新选择");  //一次选择上传超过3个 或者是已经上传和这次上传的到的总数也不可以超过3个
		}else if(numUp < 3){
			fileList = $scope.validateUp(fileList);
			for(var i = 0;i<fileList.length;i++){
			 var imgUrl = window.URL.createObjectURL(fileList[i]);
			     imgArr.push(imgUrl);
			 var $section = $("<section class='up-section fl loading'>");
			 var $zfile = $(".z_file");
			 	$zfile.before($section);
			 var $span = $("<span class='up-span'>");
			     $span.appendTo($section);
			
		     var $img0 = $("<img class='close-upimg'>").on("click",function(event){
				    event.preventDefault();
					event.stopPropagation();
					delParent = $(this).parent();
					dindex = $(this).parent().index();
					arrayObj.splice(dindex,1)
					delParent.remove();
					numUp = imgContainer.find(".up-section").length;
					if(numUp < 3){
						$(".z_file div").show();
					}
				});   
				$img0.attr("src","img/imgclose.png").appendTo($section);
		     var $img = $("<img class='up-img up-opcity'>");
		         $img.attr("src",imgArr[i]);
		         $img.appendTo($section);
		     var $p = $("<p class='img-name-p'>");
		         $p.html(fileList[i].name).appendTo($section);
		     var $input = $("<input id='taglocation' name='taglocation' value='' type='hidden'>");
		         $input.appendTo($section);
		     var $input2 = $("<input id='tags' name='tags' value='' type='hidden'/>");
		         $input2.appendTo($section);
		   }
		}
		setTimeout(function(){
             $(".up-section").removeClass("loading");
		 	 $(".up-img").removeClass("up-opcity");
		 },450);
		 numUp = imgContainer.find(".up-section").length;
		if(numUp >= 3){
			$(this).parent().hide();
		}
	});
	$scope.validateUp=function(files){
		var arrFiles = [];//替换的文件数组
		for(var i = 0, file; file = files[i]; i++){
			//获取文件上传的后缀名
			var newStr = file.name.split("").reverse().join("");
			if(newStr.split(".")[0] != null){
					var type = newStr.split(".")[0].split("").reverse().join("");
					if(jQuery.inArray(type, defaults.fileType) > -1){
						// 类型符合，可以上传
						if (file.size >= defaults.fileSize) {
							alert(file.size);
							alert('您这个"'+ file.name +'"文件大小过大');	
						} else {
							// 在这里需要判断当前所有文件中
							arrFiles.push(file);	
						}
					}else{
						alert('您这个"'+ file.name +'"上传类型不符合');	
					}
				}else{
					alert('您这个"'+ file.name +'"没有类型, 无法识别');	
				}
		}
		return arrFiles;
	}
	
	$scope.updateNowAfterSale=function() {
    	if ($scope.baseInfo.dealDesc && $scope.baseInfo.dealDesc.length > 200) {
			$scope.notice("处理结果不能超过200字");
			return;
		}
    	$scope.picLength = $(".up-section") && $(".up-section").length;
    	if ($scope.picLength > 3) {
    		$scope.notice("最多只能上传三张图片");
    		return;
		}
        Upload.upload({
			url: "upload/fileUploads.do",
			data: {file:arrayObj}
		}).success(function (data) {
			$scope.dealImg1="";
			$scope.dealImg2="";
			$scope.dealImg3="";
			if (data.str.length >= 1) {
				$scope.dealImg1 = data.str[0];
			}
			if (data.str.length >= 2) {
				$scope.dealImg2 = data.str[1];
			}
			if (data.str.length >= 3) {
				$scope.dealImg3 = data.str[2];
			}
			$scope.info={"orderNo":$scope.orderNo,"dealDesc":$scope.baseInfo.dealDesc,"dealImg1":$scope.dealImg1,"dealImg2":$scope.dealImg2,"dealImg3":$scope.dealImg3};
	    	$http({
	            url: 'perAgent/updateNowAfterSale.do',
	            data: $scope.info,
	            method:'POST'
	        }).success(function (msg) {
	            if(!msg.status){
	                $scope.notice(msg.msg);
	                return;
	            }
	            $scope.query();
	        }).error(function (msg) {
	            $scope.notice('服务器异常,请稍后再试.');
	        });
	    	arrayObj = [];
	    	$(".up-section").remove();
	    	$(".z_file div").show();
			$("#showDeliver").modal('hide');
		}).error(function () {
		      $scope.notice("图片上传失败~~~~~");
		      $scope.submiting=false;
		});
    }
 	$scope.hideDevModel = function () {
        $("#showDeliver").modal('hide');
    };
    //=================================================

    $scope.orderNo = "";
    $scope.showDeliver = function (row) {
    	$scope.orderNo = row.entity.orderNo;
    	$scope.dealImg1 = row.entity.dealImg1;
    	$scope.dealImg2 = row.entity.dealImg2;
    	$scope.dealImg3 = row.entity.dealImg3;
    	$("#showDeliver").modal('show');
    };
	$scope.changePic1=function(row){
		$scope.applyImg1=row.entity.applyImg1;
		$('#applyImg1').modal('show');
    }
	$scope.changePic2=function(row){
		$scope.applyImg2=row.entity.applyImg2;
		$('#applyImg2').modal('show');
	}
	$scope.changePic3=function(row){
		$scope.applyImg3=row.entity.applyImg3;
		$('#applyImg3').modal('show');
	}
	$scope.changePic11=function(row){
		$scope.dealImg1=row.entity.dealImg1;
		$('#dealImg1').modal('show');
	}
	$scope.changePic22=function(row){
		$scope.dealImg2=row.entity.dealImg2;
		$('#dealImg2').modal('show');
	}
	$scope.changePic33=function(row){
		$scope.dealImg3=row.entity.dealImg3;
		$('#dealImg3').modal('show');
	}
	
	$scope.hidePicture1=function(){
		$('#applyImg1').modal('hide');
	}
	$scope.hidePicture2=function(){
		$('#applyImg2').modal('hide');
	}
	$scope.hidePicture3=function(){
		$('#applyImg3').modal('hide');
	}
	$scope.hidePicture11=function(){
		$('#dealImg1').modal('hide');
	}
	$scope.hidePicture22=function(){
		$('#dealImg2').modal('hide');
	}
	$scope.hidePicture33=function(){
		$('#dealImg3').modal('hide');
	}


    //导出
    $scope.exportAfterSale=function(){
        if($scope.baseInfo.applyTimeBegin > $scope.baseInfo.applyTimeEnd || $scope.baseInfo.dealTimeBegin > $scope.baseInfo.dealTimeEnd){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
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
                    location.href="perAgent/exportAfterSale?baseInfo="+encodeURIComponent(angular.toJson($scope.baseInfo));
                }
            });
    };

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
});

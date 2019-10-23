/**
 * 商户进件
 */
var version = '12.0.0';

angular.module('inspinia',['ngFileUpload']).controller("acqMerchantAddCtrl", function($scope, $http, $state, $filter,$stateParams,Upload,i18nService,$timeout) {
	
	i18nService.setCurrentLang('zh-cn');
	// 进件类型
	$scope.mTypes=[{text:"个体收单商户",value:"1"},{text:"企业收单商户",value:"2"}];
	// 账户类型
	$scope.itemType=[{text:"对公",value:"2"},{text:"对私",value:"1"}];
	$scope.merTypes = [];
	$scope.listService=[];
	$scope.info ={merchantType:"1", agentNo:$scope.entityId,accountType:"1"};
	$scope.rates=[];
	$scope.quotas=[];
	$scope.mbp={};
	$scope.listItem=[];
	$scope.shuzu=[{sn:""}];
	
	// 根据收单商户类型来获取进件项
	$scope.getFileList = function(data){
		$http.post('merchantInfo/getIntoList.do',"merchantType="+data,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data) {
			$scope.listItem = data;
			$scope.listItem.forEach(function (item) { item.valid = false; })
		});
	}
	
	$scope.getFileList($scope.info.merchantType);
	
	// 获取一级经营范围
	$http.get('merchantInfo/queryMerType')
	.success(function(data) {
		if(!data)
			return
		$scope.merTypes=data;
		$scope.info.oneScope=data[0].sysValue
		$scope.serType($scope.info.oneScope);
	});
	
	// 获取二级经营范围
	$scope.serType=function(id){
		$scope.merMcc=[];
		//商户Mcc
	 	$http.post("merchantInfo/queryMerMcc","ids="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(data){
				//响应成功
			if(data.length>0){
		 		for(var i=0; i<data.length; i++){
		 			$scope.merMcc.push({value:data[i].sysValue,text:data[i].sysName});
		    	}
		    	$scope.info.twoScope=data[0].sysValue;
		 	}
	 	});
		
	}

	   $scope.getAreaList=function(name,type,callback){
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
	   $scope.getAreaList(0,"p",function(data){
          $scope.provinceGroup = data;
	   });
	   //市，页面上ng-change生时触发
	   $scope.getCities = function() {
		   $scope.areaGroup=[];
           $scope.getAreaList($scope.info.province,"",function(data){
                   $scope.cityGroup = data;
           });
	   }
	   //县，页面上ng-change生时触发
	   $scope.getAreas = function() {
           $scope.getAreaList($scope.info.city,"",function(data){
                   $scope.areaGroup = data;
           });
	   }
	   
	   
	   //银行支行
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
           $scope.getAreaLists($scope.info.accountProvince,"",function(data){
                   $scope.cityGroups = data;
           });
	   }
	   //县，页面上ng-change生时触发
	   $scope.getAreass = function() {
           $scope.getAreaLists($scope.info.accountCity,"",function(data){
                   $scope.areaGroups = data;
           });
	   }
	   //支行
	   $scope.bankNames=[];
	   $scope.getPosCnaps = function() {
		   if(isNaN($scope.info.bankNo)){
			   $scope.notice("请输入开户账号");
			   return;
		   }
		   var data={"pris":$scope.info.accountProvince,"cityName":$scope.info.accountCity,"ppName":$scope.info.accountDistrict,"backName":$scope.info.accountBank}
		   $http.post("merchantInfo/selectCnaps",angular.toJson(data))
			.success(function(data) {
				if(!data.bols){
					$scope.notice(data.msg);
				}else{
					$scope.bankNames=data.list;
				}
			});
	   }
	   $scope.getUnionAccountNo=function(zhihang){
		   $scope.info.lineNumber=zhihang;
	   }
	   
	   $scope.cards=[];
	   $scope.getHangName=function(){
		   if(isNaN($scope.info.bankNo)){
			   $scope.notice("请输入开户账号");
			   return;
		   }
		   var data={"accountNo":$scope.info.bankNo}
		   $http.post("merchantInfo/getBackName",angular.toJson(data))
			.success(function(largeLoad) {
				if(largeLoad.bols){
					$scope.cards=largeLoad.lists;
					$scope.info.accountBank=largeLoad.lists[0].bankName;
				}else{
					$scope.notice(largeLoad.msg);
				}
			});
	   }
	
	
	$scope.getList=function(){
		var is=0;
		for(s in fileList){
			if (fileList[s]) {
				is++;
			}
		}
		// 文件
		var fileLen =  is;
		// 要求上传的图片数
		var itemLen = $scope.listItem.length;
		// 图片上传限制
		// 个体收单商户资质文件需要全填
		if ("1" == $scope.info.merchantType) {
			if(fileLen!=itemLen){
				$scope.notice("请添加正确的图片~~~~~");
				$scope.submiting=false;
				return;
			}
		} else {
			// 租赁合同或产权证明(非必填) 29
			var id29 = fileList[29];
			// 开户许可证 23 与银行卡正反面 24 11可只上传其中一项， 无特别说明其它均为必填    itemId
			var id23 = fileList[23];
			var id24 = fileList[24];
			var id11 = fileList[11];
			if (id29 == null && id23 == null) {
				if(fileLen != (itemLen-2)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29== null && id23 != null) {
				if(fileLen != (itemLen-1)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29!= null && id23 == null) {
				if(fileLen != (itemLen-1)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29!== null && id23 != null) {
				if(fileLen != (itemLen-0)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else {
				$scope.notice("请添加正确的图片~~~~~");
				$scope.submiting=false;
				return;
			}
			
/*			if (id29 == null && id23 == null && id24 !=null && id11 != null) {
				if(fileLen != (itemLen-2)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29== null && id23 != null && id24 !=null && id11 == null) {
				if(fileLen != (itemLen-2)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29== null && id23 != null && id24 !=null && id11 != null) {
				if(fileLen != (itemLen-1)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29 == null && id23 != null && id24 ==null && id11 != null) {
				if(fileLen != (itemLen-2)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29 == null && id23 != null && id24 ==null && id11 == null) {
				if(fileLen != (itemLen-3)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29 != null && id23 == null && id24 !=null && id11 != null) {
				if(fileLen != (itemLen-1)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29 != null && id23 != null && id24 ==null && id11 == null) {
				if(fileLen != (itemLen-2)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29 != null && id23 != null && id24 !=null && id11 == null) {
				if(fileLen != (itemLen-1)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29 != null && id23 != null && id24 ==null && id11 != null) {
				if(fileLen != (itemLen-1)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else if (id29 != null && id23 != null && id24 !=null && id11 != null) {
				if(fileLen != (itemLen-0)){
					$scope.notice("请添加正确的图片~~~~~");
					$scope.submiting=false;
					return;
				}
			}else {
				$scope.notice("请添加正确的图片~~~~~");
				$scope.submiting=false;
				return;
			}*/	
		}
		Upload.upload({
			url: "upload/fileUploads.do",
			data: {file:fileList}
		}).success(function (data) {
				if(data.str.length!=fileLen){
					$scope.notice("图片上传失败~~~~~");
					$scope.submiting=false;
					return;
				}
				var i =0;
				var content=[];
				for(s in data.str){
					content[i]=data.str[s];
					i++;
				}
				
				// 支行名称
				for (var i = 0; i < $scope.bankNames.length; i++) {
					var bankInfo = $scope.bankNames[i];
					if (bankInfo.cnapsNo == $scope.info.bankBranch) {
						$scope.info.bankBranch = bankInfo.bankName;
					}
				}
				
			   var data={"content":content,"info":$scope.info};
			   $http.post("merchantInfo/saveAcqMerchantInfo",data)
				.success(function(result){
					if(result.status){
						$scope.notice(result.msg);
						$scope.info.bankBranch = $scope.info.lineNumber;
						// 保存成功 跳转到收单进件列表
						$state.go('merchant.queryAcqMer');
					}else{
						$scope.notice(result.msg);
						$scope.info.bankBranch = $scope.info.lineNumber;
						$scope.submiting=false;
					}
				});	
		}).error(function () {
		      $scope.notice("图片上传失败~~~~~");
		      $scope.submiting=false;
		});
			
	}
	
	//图片上传
	$scope.$watch('files', function () {
	    $scope.selectImage($scope.files);
	});
	var num=0;
	var fileList={};
	$scope.selectImage = function (file,id) {
		if (!id) {
			return ;
		}
		fileList[id]=file;
		// 没选图片
	    if (!file || !file.length) {
	    	fileList[id]= null;
	        return;
	    }
	};
	//提交
	$scope.commitMerInfo=function(){
		$scope.submiting=true;
		$scope.getList();
	} 

	
});

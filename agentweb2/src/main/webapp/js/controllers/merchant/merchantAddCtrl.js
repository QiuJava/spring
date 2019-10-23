/**
 * 商户进件
 */
var version = '12.0.0';

angular.module('inspinia',['ngFileUpload']).controller("merchantAddCtrl", function($scope, $http, $state, $filter,$stateParams,Upload,i18nService,$timeout) {
	
	i18nService.setCurrentLang('zh-cn');
	$scope.mTypes=[{text:"个人",value:"1"},{text:"个体商户",value:"2"},{text:"企业商户",value:"3"}];
	$scope.itemType=[{text:"对公",value:"1"},{text:"对私",value:"2"}];
	$scope.twoCodeHide = true;
	$scope.merTypes = [];
	$scope.listService=[];
	$scope.info ={merchantType:"1", agentName:$scope.entityName};
	$scope.adds={};
	$scope.rates=[];
	$scope.quotas=[];
	$scope.asd={oppend:""};
	$scope.mbp={};
	$scope.merExa=[];
	$scope.listMri=[];
	$scope.listItem=[];
	$scope.defaultListItem=[];
	$scope.shuzu=[{sn:""}];
	$scope.item={accountType:"2"};
	$scope.address={};
	var fls=0;
	$scope.serMerchantType=function(merId){
		//业务产品
		$http.post("merchantInfo/selectBpdInfoByAgentNo","ids="+angular.toJson(merId),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(largeLoad) {
			if(!largeLoad)
				return;
			$scope.productTypes=largeLoad;
			if(largeLoad.length>0){
				$scope.mbp.bpId=largeLoad[0].bpId;
				$scope.infos();
			}else{
				$scope.notice("没有业务产品~~~~~");
				$scope.rates=[];
				$scope.quotas=[];
				$scope.listService=[];
				$scope.listItem = [];
			}
		});
	}
	
	$scope.serMerchantType(1);
	$http.get('merchantInfo/queryMerType')
	.success(function(largeLoad) {
		if(!largeLoad)
			return
		$scope.merTypes=largeLoad;
		$scope.info.businessType=largeLoad[0].sysValue
		$scope.serType($scope.info.businessType);
	});
	
	
	$scope.serType=function(id){
		$scope.merMcc=[];
		//商户Mcc
	 	$http.post("merchantInfo/queryMerMcc","ids="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		 .success(function(msg){
				//响应成功
			if(msg.length>0){
		 		for(var i=0; i<msg.length; i++){
		 			$scope.merMcc.push({value:msg[i].sysValue,text:msg[i].sysName});
		    	}
		    	$scope.info.industryType=msg[0].sysValue;
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
           $scope.getAreaLists($scope.adds.province,"",function(data){
                   $scope.cityGroups = data;
           });
	   }
	   //县，页面上ng-change生时触发
	   $scope.getAreass = function() {
           $scope.getAreaLists($scope.adds.city,"",function(data){
                   $scope.areaGroups = data;
           });
	   }
	   $scope.bankNames=[];
	   //支行
	   $scope.getPosCnaps = function() {
		   if(isNaN($scope.item.accountNo)){
			   $scope.notice("请输入开户账号");
			   return;
		   }
		   var data={"pris":$scope.adds.province,"cityName":$scope.adds.city,"ppName":$scope.adds.district,"backName":$scope.item.accountHangName}
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
		   $scope.item.unionAccountNo=zhihang;
		   angular.forEach($scope.bankNames, function (item) {
	            if (zhihang == item.cnapsNo){
	            	$scope.item.bankName = item.bankName;
	            }
	        });
	   }
	   
	   $scope.cards=[];
	   $scope.getHangName=function(){
		   if(isNaN($scope.item.accountNo)){
			   $scope.notice("请输入开户账号");
			   return;
		   }
		   var data={"accountNo":$scope.item.accountNo}
		   $http.post("merchantInfo/getBackName",angular.toJson(data))
			.success(function(largeLoad) {
				if(largeLoad.bols){
					$scope.cards=largeLoad.lists;
					$scope.item.accountHangName=largeLoad.lists[0].bankName;
				}else{
					$scope.notice(largeLoad.msg);
				}
//				$scope.item.accountHangName=largeLoad.name;
			});
	   }
	 //终端操作
	/*$scope.addTermianl=function(){
		$scope.shuzu.push({sn:""});
	}
	$scope.delTermianl=function(idx){
	    $scope.shuzu.splice(idx,1);
	}*/
	
	
	$scope.getList=function(){

        if($scope.info.merchantType=="1"||$scope.info.merchantType=="2"){
        	if($scope.info.lawyer!=$scope.item.accountName){
                $scope.notice("法人姓名和开户名不一致，请重新输入");
                $scope.submiting=false;
                return;
			}
        }
		if($scope.rates.length<=0||$scope.listService.length<=0){
			$scope.infos();
		}
		var data={"info":$scope.info,"bpId":$scope.mbp.bpId,"shuzu":$scope.shuzu,"card":$scope.item.accountNo};
		$http.post('merchantInfo/checkMerchantInfo',"infos="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(largeLoad) {
			if(!largeLoad.bols){
				$scope.notice(largeLoad.msg);
				$scope.submiting=false;
			}else{
				var dds={"card":$scope.info.idCardNo,"name":$scope.item.accountName,"accountNo":$scope.item.accountNo,"cnapsNo":$scope.item.unionAccountNo + ""};
				$http.post('merchantInfo/checkBankNameIDCard',angular.toJson(dds))
				.success(function(largeLoad) {
					if(data.bols=="f"){
						$scope.notice(largeLoad.msg);
						$scope.submiting=false;
					}else{
						$scope.submiting=true;
						if($scope.rates.length==0||$scope.quotas.length==0||$scope.listService.length==0){
							$scope.notice("请填写完整的信息~~~~~~~");
							$scope.submiting=false;
							return;
						}
						if(fls!=0){
							fls=0;
							return;
						}
						var files=[];
						var is=0;
						for(s in fileList){
							files[is]=fileList[s][0];
							is++;
						}
						if(files.length!=$scope.listItem.length){
							$scope.notice("请添加正确的图片~~~~~");
							$scope.submiting=false;
							return;
						}
						Upload.upload({
							url: "upload/fileUploads.do",
							data: {file:fileList}
						}).success(function (data) {
								if(data.str.length!=$scope.listItem.length){
									$scope.notice("图片上传失败~~~~~");
									$scope.submiting=false;
									return;
								}
								var i =0;
								var nums=[];
								var content=[];
								for(s in fileList){
									nums[i]=s;
									content[i]=data.str[i];
									i++;
								}
							   $scope.item.accountAddress=$scope.adds.province+"-"+$scope.adds.city+"-"+$scope.adds.district;
							   var data={"shuzu":$scope.shuzu,"nums":nums,"content":content,"item":$scope.item,"infos":$scope.info,"listMsr":$scope.rates,"listMsq":$scope.quotas,"mbp":$scope.mbp,"listService":$scope.listService};
							   $http.post("merchantInfo/insertMerchantInfo",data)
								.success(function(result){
									if(result.bols){
										$scope.notice(result.msg);
										$state.go('merchant.queryMer');
									}else{
										$scope.notice(result.msg);
										$scope.submiting=false;
									}
								});	
						}).error(function () {
						      $scope.notice("图片上传失败~~~~~");
						      $scope.submiting=false;
						});
					}
				})
				
			}
		})
	}
	
	var quotasbz=[];
	// 	//查询费率限额之类的信息
	$scope.infos=function(){
		$http.post('merchantInfo/queryAgentQuotaRate.do',"bpId="+angular.toJson($scope.mbp.bpId),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(largeLoad) {
			fileList={};
			if(!largeLoad.bols){
				$scope.listItem = [];
				$scope.notice(largeLoad.msg);
				return;
			}
			$scope.rates=largeLoad.listmr;
			$scope.quotas=largeLoad.listmsq;
			$scope.listService=largeLoad.listService;
			$scope.listItem = largeLoad.listItem;
			$scope.defaultListItem = largeLoad.listItem;
			quotasbz=angular.copy(largeLoad.listmsq);

		});
	}
	
	
	
	
	$scope.rateTypes=[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"}
	,{text:"单笔阶梯扣率",value:"5"}];
	$scope.merchantRateList={
			data:'rates',
			columnDefs:[
			    {field:'serviceName',displayName:'服务名称',width:150,pinnable:false,sortable:false}, 
			    {field:'cardType',displayName:'银行卡种类',width:120,pinnable:false,sortable:false,
			    	cellFilter: "formatDropping:"+$scope.cardTypeStr
			    },
			    {field:'holidaysMark',displayName:'节假日标志',width:120,pinnable:false,sortable:false,
			    	cellFilter: "formatDropping:"+$scope.holidaysStr
			    },
			    {field:'rateType',displayName:'费率方式',width:220,
			    	cellTemplate:'<select disabled="disabled" class="input-sm form-control input-s-sm inline" ng-model="row.entity[col.field]" ng-options="x.value as x.text for x in grid.appScope.rateTypes"/>'
			    		
			    },
			    {field:'oneMerRate',displayName:'一级代理商管控费率',width:260,pinnable:false,sortable:false},
			    {field:'merRate',displayName:'商户费率',width:260,pinnable:false,sortable:false,
			    	cellTemplate:
			    		'<div ng-switch on="$eval(\'row.entity.fixedRate\')">'
				    		+'<div ng-switch-when=0>'
								+'<input style="width:98%;height:98%;" ng-blur="grid.appScope.judgeRate(row.entity.serviceId,row.entity.cardType,row.entity.holidaysMark)" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
							+'</div>'
							+'<div ng-switch-when=1>'
								+'<p ng-bind="row.entity[col.field]"/>'
							+'</div>'
						+'</div>'
			    },
			    {field:'fixedRate',displayName:'固定标志',width:150,pinnable:false,sortable:false,
	            	cellFilter: "formatDropping:[{text:'固定',value:1},{text:'不固定',value:0}]"

			    }
			]
		};
	
	var ddser='<div ng-switch on="$eval(\'row.entity.fixedQuota\')">'
		+'<div ng-switch-when=0>'
			+'<input style="width:98%;height:98%;" ng-blur="grid.appScope.checkQuota(row.entity.singleDayAmount,row.entity.singleMinAmount,row.entity.singleCountAmount,row.entity.singleDaycardAmount,row.entity.singleDaycardCount,row.entity.serviceId,row.entity.cardType,row.entity.holidaysMark)" class="ui-widget input" ng-readonly="false" ng-model="row.entity[col.field]"/>'
		+'</div>'
		+'<div ng-switch-when=1>'
			+'<p ng-bind="row.entity[col.field]"/>'
		+'</div>'
	+'</div>';
	
	$scope.merchantQuotaList={
			data:'quotas',
			columnDefs:[
	            {field:'serviceName',displayName:'服务名称',width:150,pinnable:false,sortable:false}, 
	            {field:'cardType',displayName:'银行卡种类',width:120,pinnable:false,sortable:false,
	            	cellFilter: "formatDropping:"+$scope.cardTypeStr
	            }, 
	            {field:'holidaysMark',displayName:'节假日标志',width:120,pinnable:false,sortable:false,
	            	cellFilter: "formatDropping:"+$scope.holidaysStr
	            }, 
	            {field:'singleDayAmount',displayName:'单日最大交易金额(元）',width:200,pinnable:false,sortable:false,
	            	cellTemplate:ddser
	            }, 
	            {field:'singleMinAmount',displayName:'单笔最小交易额（元）',width:200,pinnable:false,sortable:false,
	            	cellTemplate: ddser
	            },
	            {field:'singleCountAmount',displayName:'单笔最大交易额（元）',width:200,pinnable:false,sortable:false,
	            	cellTemplate: ddser
	            },
	            {field:'singleDaycardAmount',displayName:'单日单卡最大交易额（元）',width:200,pinnable:false,sortable:false,
	            	cellTemplate: ddser
	            }, 
	            {field:'singleDaycardCount',displayName:'单日单卡最大交易笔数（笔）',width:230,pinnable:false,sortable:false,
	            	cellTemplate:ddser
	            }, 
	            {field:'fixedQuota',displayName:'固定标志',width:150,pinnable:false,sortable:false,
	            	cellFilter: "formatDropping:[{text:'固定',value:1},{text:'不固定',value:0}]"
			    }
			]
		};
	
	
	//图片上传
	$scope.$watch('files', function () {
	    $scope.selectImage($scope.files);
	});
	var num=0;
	var fileList={};
	$scope.selectImage = function (files,id) {
		if(null != files){
			if(files[0].size > 6*1024*1024){
				$scope.notice("上传图片不能大于6M");
				return;
			}
			for (var i = 0; i < $scope.listItem.length; i++) {
				if($scope.listItem[i].itemId == id){
					$scope.listItem[i].photoAddressUrl = files[0];
				}
			}
		}
		if(!id)
			return;
		fileList[id]=files;
	    if (!files || !files.length) {
	        return;
	    }
	};
	$scope.itemImage=[];
	//提交
	$scope.commitMerInfo=function(){
		$scope.submiting=true;
		$scope.getList();
	} 

	
	//服务限额判断
	$scope.checkQuota=function(singleDayAmount,singleMinAmount,singleCountAmount,singleDaycardAmount,singleDaycardCount,serviceId,cardId,did){
		for(var i =0;i<quotasbz.length;i++){
			if(quotasbz[i].serviceId==serviceId && quotasbz[i].cardType==cardId && quotasbz[i].holidaysMark==did){
				$scope.submiting=false;
				fls=0;
				if(singleDayAmount==" "||singleCountAmount==" "||singleDaycardAmount==" "||singleDaycardAmount==" "||singleMinAmount==" "){
					$scope.notice("请输入正确的金额或数字");
					fls=1;
					return;
				}
				if(isNaN(singleDayAmount)||isNaN(singleCountAmount)||isNaN(singleDaycardAmount)||isNaN(singleDaycardCount)||isNaN(singleMinAmount)){
					$scope.notice("请输入正确的金额或数字");
					fls=1;
					return;
				}
				var str="";
				if(parseFloat(quotasbz[i].singleDayAmount)<parseFloat(singleDayAmount)){
					str="单日最大交易金额必须小于等于当前金额="+quotasbz[i].singleDayAmount;
					fls+=1;
				}
				if(parseFloat(quotasbz[i].singleCountAmount)<parseFloat(singleCountAmount)){
					str="单笔最大交易额必须小于等于当前金额="+quotasbz[i].singleCountAmount;
					fls+=1;
				}
				if(parseFloat(quotasbz[i].singleMinAmount)<parseFloat(singleMinAmount)){
					str="单笔最小交易额必须小于等于当前金额="+quotasbz[i].singleMinAmount;
					fls+=1;
				}
				if(parseFloat(quotasbz[i].singleDaycardAmount)<parseFloat(singleDaycardAmount)){
					str="单日单卡最大交易额必须小于等于当前金额="+quotasbz[i].singleDaycardAmount;
					fls+=1;
				}
				if(parseFloat(quotasbz[i].singleDaycardCount)<parseFloat(singleDaycardCount)){
					str="单日单卡最大交易笔数必须小于等于当前笔数="+quotasbz[i].singleDaycardCount;
					fls+=1;
				}
				if(str!=""){
					$scope.notice(str);
				}
			}
		}
		
	}
	
	$scope.judgeRate=function(id,cc,hh){
		$scope.submiting=false;
		for(var i=0;i<$scope.rates.length;i++){
			if($scope.rates[i].serviceId==id && $scope.rates[i].cardType==cc && $scope.rates[i].holidaysMark==hh){
				if($scope.rates[i].oneMerRate==$scope.rates[i].merRate){
					continue;
				}else{
					if($scope.rates[i].rateType==1){
						if(!judgeOne($scope.rates[i].merRate,$scope.rates[i].oneMerRate))
							$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
						else
							fls=0;
					}
					if($scope.rates[i].rateType==2){
						if(!judgeTwo($scope.rates[i].merRate,$scope.rates[i].oneMerRate))
							$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
						else
							fls=0;
					}
					if($scope.rates[i].rateType==3){
						var str1=new Array();
						str1=$scope.rates[i].merRate.split("~");
						
						var str2=new Array();
						str2=$scope.rates[i].oneMerRate.split("~");
						if(str1.length!=3){
							$scope.notice("费率格式有误");
						}else{
							if(judgeOne(str1[0],str2[0])){
								if(judgeTwo(str1[1],str2[1])){
									if(judgeOne(str1[2],str2[2])){
										fls=0;
									}else{
										$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
									}
								}else{
									$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
								}
							}else{
								$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
							}
						}	
						
					}
					if($scope.rates[i].rateType==4){
						
						var str1=new Array();
						str1=$scope.rates[i].merRate.split("+");
						
						var str2=new Array();
						str2=$scope.rates[i].oneMerRate.split("+");
						
						if(str1.length!=2){
							$scope.notice("费率格式有误");
						}else{
							if(judgeTwo(str1[0],str2[0])){
								if(judgeOne(str1[1],str2[1])){
									fls=0;
								}else{
									$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
								}
							}else{
								$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
							}
						}	
						
					}
					if($scope.rates[i].rateType==5){
						var str1=new Array();
						str1=$scope.rates[i].merRate.split("<");
						
						var str2=new Array();
						str2=$scope.rates[i].oneMerRate.split("<");
						
						if(str1.length%2==0){
							$scope.notice("费率格式有误");
						}else{
							
							if(str1.length==3){
								if(judgeTwo(str1[0],str2[0])){
									if(judgeOne(str1[1],str2[1])){
										if(judgeTwo(str1[2],str2[2])){
											fls=0;
										}else{
											$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
										}
									}else{
										$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
									}
								}else{
									$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
								}
							}
							
							if(str1.length==5){
								if(judgeTwo(str1[0],str2[0])){
									if(judgeOne(str1[1],str2[1])){
										if(judgeTwo(str1[2],str2[2])){
											if(judgeOne(str1[3],str2[3])){
												if(judgeTwo(str1[4],str2[4])){
													fls=0;
												}else{
													$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
												}
											}else{
												$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
											}
										}else{
											$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
										}
									}else{
										$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
									}
								}else{
									$scope.notice("费率格式有误或费率小于一级费率=",$scope.rates[i].merRate);
								}
							}
							
						}
						
					}
					
				}
			}
				
		}
	}
	
	//针对每笔固定金额
	function judgeOne(merRate,oneMerRate){
		var mer=new Array();
		mer=merRate.split(".");
		if(isNaN(merRate)){
			fls=1;
			return false;
		}
		if(mer.length!=2){
			fls+=1;
			return false;
		}else if(mer[1].length>2){
			fls+=1;
			return false;
		}else if(parseFloat(merRate)<parseFloat(oneMerRate)){
			fls+=1;
			return false;
		}
		fls+=0;
		return true;
	}
	
	//针对每笔扣率
	function judgeTwo(merRate,oneMerRate){
		var mer=new Array();
		mer=merRate.split(".");
		if(isNaN(merRate.substring(0,merRate.lastIndexOf("%")))){
			fls=1;
			return false; 
		}
		if(merRate.lastIndexOf("%")==-1){
			fls+=1;
			return false;
		}else if(mer[1].length>3){
			fls+=1;
			return false;
		}else if(mer.length!=2){
			fls+=1;
			return false;
		}else if(parseFloat(merRate.substring("%"))<parseFloat(oneMerRate.substring("%"))){
			fls+=1;
			return false;
		}
		fls+=0;
		return true;
	}
	
});

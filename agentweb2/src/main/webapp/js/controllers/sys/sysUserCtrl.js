/**
 * 系统管理-系统用户 控制器
 * 
 * @author xyf1
 */

angular.module('inspinia').controller(
		'sysUserCtrl',
		function($scope, $http, $state, $stateParams, i18nService, $compile,
				$filter, $uibModal, $log, uiGridConstants, $httpParamSerializerJQLike, 
				$q, SweetAlert,$rootScope) {
			
			i18nService.setCurrentLang('zh-cn');
			
			$scope.statusTypes = [ {text : '有效', value : '1'}, {text : '无效', value : '0'} ];
			$scope.includeChildren = [ {text : '包含', value : '1'}, {text : '不包含', value : '0'} ];
			$scope.statusTypesStr = JSON.stringify($scope.statusTypes);
			$scope.statusTypes2 = angular.copy($scope.statusTypes);
			$scope.statusTypes2.unshift({text:'不限'});
			$scope.teamIds = [];
			$scope.teamIdsStr = JSON.stringify($scope.teamIds);
			$scope.appRoleTypes = [{text:'销售员',value: '0'},{text:"管理员",value:'1'}];
			$scope.roles = [{id:-1,text:'none',value:'none'}];
			$scope.rolesStr = JSON.stringify($scope.roles);
			$scope.appRoles = JSON.stringify([{text:'销售员',value: '0'},{text:"管理员",value:'1'},{text:'店员',value:'2'}]);
			$scope.query = {hasChildren: "1"};

			$scope.paginationOptions={pageNo:1,pageSize:10};
			
			// 准备数据时使用的所有promise
			var promises = [];
			
			// 准备
			var teamsPromise=$q.defer();
			promises.push(teamsPromise.promise);
			$http.get("teamInfo/queryTeamName.do").success(function(data){
				$scope.teamIds = [];
				if(data && data.status){
					angular.forEach(data.teamInfo, function(item){
						$scope.teamIds.push({text: item.teamName, value:''+item.teamId});
					});
					$scope.teamIdsStr = JSON.stringify($scope.teamIds);
				}
				teamsPromise.resolve();
				delete teamsPromise;
			});
			var rolesPromise=$q.defer();
			promises.push(rolesPromise.promise);
			$http.get("roleAction/findAllRoles.do?usable=1").success(function(data){
				$scope.roles = [];
				if(data && data.length){
					angular.forEach(data, function(item){
						$scope.roles.push({text: item.roleName, value:''+item.id, id:''+item.id});
					});
					$scope.rolesStr = JSON.stringify($scope.roles);
				}
				rolesPromise.resolve();
				delete rolesPromise;
			});
			var myRolesPromise=$q.defer();
			promises.push(myRolesPromise.promise);
			$http.get("userAction/getRolesByUserId.do?userId="+$scope.userid).success(function(data){
				$scope.myRoles = [];
				if(data && data.length){
					angular.forEach(data, function(item){
						var r = item.shiroRole;
						//$scope.myRoles.push({text: r.roleName, value:''+r.id, id:''+r.id});
					});
				}
				myRolesPromise.resolve();
				delete myRolesPromise;
			});
//			var agentsPromise=$q.defer();
//			promises.push(agentsPromise.promise);
//			$http.get("agentInfo/selectAllInfo").success(function(data){
//				$scope.agents = [];
//				if(data && data.length){
//					angular.forEach(data, function(item){
//						$scope.agents.push({text: item.roleName, value:''+item.id, id:''+item.id});
//					});
//					$scope.agentsStr = JSON.stringify($scope.agents);
//				}
//				agentsPromise.resolve();
//				delete agentsPromise;
//			});
			
			$scope.paginationOptions=angular.copy($scope.paginationOptions);
			// 用户表的基本配置。字段配置因为要等待数据的获取，往后放。这里设置基本配置，以避免界面报错
			$scope.userListGrid = {
					data: 'users',
					paginationPageSize:10,                  //分页数量
			        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
			        useExternalPagination: true,		  //拓展名
			        useExternalSorting: true,
			        enableHorizontalScrollbar: true,        //横向滚动条
			        enableVerticalScrollbar : true,  		//纵向滚动条 
			        onRegisterApi: function(gridApi) {                
			            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
			            	$scope.paginationOptions.pageNo = newPage;
			            	$scope.paginationOptions.pageSize = pageSize;
			            	$scope.queryFunc();
			            });
			            gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
			            	$scope.sortColumns = sortColumns;
			            	$scope.queryFunc();
			            });
			        }
			};

			$scope.queryFunc = function() {
				delete $scope.query.sort;
				if($scope.sortColumns && $scope.sortColumns.length){
					$scope.query.sort = $scope.sortColumns[0].name;
					$scope.query.sortdir = $scope.sortColumns[0].sort.direction == uiGridConstants.DESC ? 'desc' : 'asc';
				}
				$scope.query.page = $scope.paginationOptions.pageNo;
				$scope.query.pageSize = $scope.paginationOptions.pageSize;
				$http({
					method : "post",
					url : "userAction/selectByCondition.do",
					data : $scope.query 
				}).success(function(data) {
					$scope.users = data.result;
					$scope.userListGrid.totalItems = data.totalCount;
				})
			};
			$scope.resetForm=function(){
				$scope.query= {hasChildren : "1"};
			}
			
			// 角色树配置
			$scope.roleTreeConfig = {
				'plugins' : [ 'core', 'types', 'checkbox' ],
				types : {
					'default' : { icon : 'fa fa-user' }
				},
				version: 1
			};
			$scope.reCreateRoleTree = function(reparse) {
				$scope.roleTreeConfig.version++;
			};
			
			// 显示修改、新增对话框
			$scope.showModify = function(user){
				function openModal(){
					var uibModalInstance = $uibModal.open({
						templateUrl: "views/sys/editUser.html",
						show: true,
						scope: $scope,
						size:'lg',
						controller: 'sysUserModalCtrl'
					});
					$scope.userModalInstance = uibModalInstance;
					uibModalInstance.result.then(function(){
						$scope.queryFunc();
					}, function(){
						delete $scope.userModalInstance;
						$log.info('取消: ' + new Date())
					});
				}
				$scope.currUser = angular.copy(user) || {status:"1",teamId:$scope.teamId,userEntityInfos:[{manage:'0'}]};
				var mydata = $scope.currUser1 = {};

				$scope.isNotSameEntity = false;
				if($scope.currUser.id){
					mydata.url = "userAction/updateUser.do";
					mydata.title = "修改用户";
//					if($scope.currUser.userEntityInfos
//						&& $scope.currUser.userEntityInfos[0]
//						&& $scope.entityId == $scope.currUser.userEntityInfos[0].entityId){
					if($scope.userid==$scope.currUser.userEntityInfos[0].userId || $scope.currUser.userEntityInfos[0].isAgent=='1'){
						// 下级代理商的用户，只能修改密码和有效状态
						$scope.isNotSameEntity = true;
						openModal();
					}else{
						$http.get("userAction/getRolesByUserId.do?userId="+$scope.currUser.userId).success(function(data){
							angular.forEach($scope.roles, function(d2){
								var found = false;
								angular.forEach(data, function(d){
									if(d.roleId == d2.id)
										found = true;
								});
								if(!d2.state){
									d2.state = {};
								}
								d2.state.selected = found;
							});
							openModal();
						});
					}
				}else{
					angular.forEach($scope.roles, function(d){
						if(d.state){
							d.state.selected = false;
						}
					});
					mydata.url = "userAction/addUser.do";
					mydata.title = "新增用户";
					openModal();
				}
			};
			
			$scope.deleteUser = function(user){
				$scope.typeDel = 'user';
				$scope.idDel = user.userId;
				$scope.nameDel = user.userName;
				var aa = $('#myModal').modal('show');
			};

			/*// 是否显示业务范围按钮
			$scope.showButton = function (user) {
				if($rootScope.entityId == user.userEntityInfos[0].entityId){
					return false;
				}else {
					return true;
				}

            }
*/
            // 展示业务范围修改弹窗
            $scope.baseInfo={userId:'',teamId:''};
			$scope.showTeams = function (user) {
                $http.get('merchantInfo/merTeams.do')
                    .success(function(data) {
                        if(!data){ return;}
                        $scope.merTeams=data;
                        $scope.merTeams.splice(0,0,{merTeamId:"-1",appName:"全部"});
                    });
                var oldTeamId = user.userEntityInfos[0].accessTeamId;
                if(oldTeamId != null){
                    $scope.merTeamId = oldTeamId;
				}else {
                    $scope.merTeamId="-1";
				}
                $scope.baseInfo.userId = user.userId;
                $('#teams').modal('show');
            }


            //修改业务范围
			$scope.changeTeamId = function () {
				if($scope.merTeamId=="-1"){
                    $scope.notice("请选择业务");
                    return;
				}
				$scope.baseInfo.teamId = $scope.merTeamId;
                $http({
                    url: 'userAction/changeAccessTeamId',
                    data: $scope.baseInfo,
                    method:'POST'
                }).success(function (data) {
                	if(data.status){
                		$scope.notice("提交成功")
                        $scope.queryFunc();
					}else {
                		$scope.notice(data.msg);
					}
                }).error(function () {
                });
                $('#teams').modal('hide');
            }

            // 关闭窗口
            $scope.closeTeams = function () {
                $('#teams').modal('hide');
            }

			
			$scope.deleteModalClose = function(){
				if('user' == $scope.typeDel){
					$http({  
						   method:'post',  
						   url: "userAction/deleteUser.do",  
						   data:$httpParamSerializerJQLike({userId: $scope.idDel}),  
						   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},  
						})
					.success(function(data){
						if(data && data.status){
							$('#myModal').modal('hide');
							$scope.queryFunc();
							$scope.notice(data.msg);
						}else{
							$scope.notice((data && data.msg) ? data.msg : "删除功能失败");
						}
					});
				}
			}
			
			$q.all(promises).then(function(){
				// 获取数据完成了
				promises = [];
				// 用户表的栏目配置
				$scope.userListGrid.columnDefs= [
					{field: 'userId',displayName: '用户ID'},
					{field: 'userName',displayName: '用户名称'},
					{field: 'mobilephone',displayName: '手机号'},
					{field: 'email',displayName: '邮箱'},
					{field: 'userEntityInfos[0].manage',displayName: 'APP角色',cellFilter:"formatDropping:" + $scope.appRoles},
					{field: 'roleName',displayName: '角色名称'},
					{field: 'status',displayName: '状态',cellFilter:"formatDropping:" + $scope.statusTypesStr},
					{field: 'teamId',displayName: '组织',cellFilter:"formatDropping:" + $scope.teamIdsStr},
					{field: 'createTime',displayName: '创建时间',cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
					{field: 'action',displayName: '操作',width: 180,enableSorting:false,cellTemplate:
						'<div class="lh30" class="ui-grid-cell-contents">' +
						'<a ng-click="grid.appScope.showModify(row.entity)">修改</a>' +
						' <a ng-click="grid.appScope.deleteUser(row.entity)" ng-show="row.entity.userEntityInfos[0].isAgent==0?true:false"> |删除</a>' +
                        ' <a ng-show="row.entity.userEntityInfos[0].isAgent==0" ng-click="grid.appScope.showTeams(row.entity)">|业务范围</a>' +
						'</div>'
					}
				];

				//$rootScope.entityId != row.entity.userEntityInfos[0].entityId

				angular.forEach($scope.myRoles, function(d1){
					var found = false;
					angular.forEach($scope.roles, function(d2){
						if(d1.id == d2.id){
							found = true;
						}
					});
					if(!found){
						$scope.roles.push(d1);
					}
				});
				$scope.queryFunc();
			});
						
		});

angular.module('inspinia').controller(
		'sysUserModalCtrl',
		function($scope, $http, $state, $stateParams, i18nService, $compile,
				$filter, $uibModal, $q, $timeout, SweetAlert) {
			//通用的判断提交数据参数，包含判断返回值是否成功的函数
			var saveItemDefaultOpt = {checker: function(data){
				return data && data.status;
			}};
			//通用的提交数据函数
			$scope.saveItem = function(options){
				var opt = angular.extend({}, saveItemDefaultOpt, options || {});
				$http({  
					   method:'post',  
					   url:opt.url,  
					   data: opt.data,  
					})
				.success(function(data){
					if(opt.checker(data)){
						if(opt.successCB){
							opt.successCB(data);
						}
						$scope.notice((data && data.msg) ? data.msg : opt.title+"成功");
					}else{
						$scope.notice((data && data.msg) ? data.msg : opt.title+"失败");
					}
				});
			};
			$scope.saveUser = function(){
				if(!$scope.isNotSameEntity){
					if($scope.roleTreeInstance)
						$scope.currUser.roleIds = $scope.roleTreeInstance.jstree(true).get_checked();
					else{
						$scope.currUser.roleIds = $('div[tree=roleTreeInstance] li[aria-selected=true][id]')
							.map(function(){
								return $(this).attr('id');
							}).get();
						if ($scope.currUser.roleIds.length !== 1){
                            $scope.notice("角色必须只能选中一个.");
							return;
						}
					}
				}else
					delete $scope.currUser.roleIds;
				$scope.currUser.manage = $scope.currUser.userEntityInfos[0].manage;
				$scope.saveItem({
					url:$scope.currUser1.url,
					data:$scope.currUser,
					title:$scope.currUser1.title,
					successCB:function(){
						$scope.userModalInstance.close();
						delete $scope.userModalInstance;
					}
				});
			}
			
			$timeout(function(){
				$scope.reCreateRoleTree();
			});
			
			//密码重置
		    $scope.resetPwd=function(){
		    	var userId = $scope.currUser.userId;
		        SweetAlert.swal({
		            title: "确认密码重置？",
//		            text: "",
		            type: "warning",
		            showCancelButton: true,
		            confirmButtonColor: "#DD6B55",
		            confirmButtonText: "提交",
		            cancelButtonText: "取消",
		            closeOnConfirm: true,
		            closeOnCancel: true },
			        function (isConfirm) {
			            if (isConfirm) {
			            	$http.post("userAction/resetPwd.do","userId="+userId,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			        		.success(function(msg){
			        			$scope.notice(msg.msg);
			        		}).error(function(){
			        		});
			            }
		        });
		    };
		    
		    $scope.cancel = function(){
		    	$scope.userModalInstance.dismiss();
		    	delete $scope.userModalInstance;
		    };
		});

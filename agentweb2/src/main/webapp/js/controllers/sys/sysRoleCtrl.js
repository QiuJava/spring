/**
 * 系统管理-系统角色 控制器
 * 
 * @author xyf1
 */

angular.module('inspinia').controller(
		'sysRoleCtrl',
		function($scope, $http, $state, $stateParams, i18nService, $compile,
				$filter, $uibModal, $log, uiGridConstants, $httpParamSerializerJQLike,SweetAlert) {
			i18nService.setCurrentLang('zh-cn');
			
			$scope.baseInfo = {};
			$scope.roles = [];
			
			//清空查询条件
			$scope.resetForm = function(){
				$scope.baseInfo = {};
			}
			
			$scope.refreshRoleList = function(){
				$http.post("roleAction/findRoles.do", "baseInfo=" + angular.toJson($scope.baseInfo) + "&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
						$scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function(data){
					$scope.roles = data.result;
					$scope.roleGrid.totalItems = data.totalCount;
				})
			};
			
			$scope.paginationOptions=angular.copy($scope.paginationOptions);
			$scope.roleGrid = {
				data: 'roles',
				enableSorting: true,
				paginationPageSize: 10,
				paginationPageSizes: [10, 20, 50, 100],
				useExternalPagination: true,
				columnDefs: [
		            {field: 'roleName', displayName: '角色名称',width:200},
		            {field: 'roleCode',displayName: '角色编码',width:200},
		            {field: 'roleRemake', displayName: '角色说明',width:400},
		            {field: 'options', displayName: '操作',width:240, cellTemplate:
		            	 '<a  ng-click="grid.appScope.showEditRole(row.entity)">修改</a>'
		                +'<a  ui-sref="sys.sysRoleRight({id:row.entity.id,roleName:row.entity.roleName})"> | 菜单</a>'
		                +'<a  ng-show="row.entity.canDelete" ng-click="grid.appScope.deleteRole(row.entity)"> | 删除</a>'
		            }
		        ],
		        onRegisterApi: function(gridApi){
		        	$scope.gridApi = gridApi;
		        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
		        		$scope.paginationOptions.pageNo = newPage;
		        		$scope.paginationOptions.pageSize = pageSize;
		        		$scope.refreshRoleList();
		        	});
		        }
			};
			
			$scope.showNewRole = function(){
				$scope.newRole = {};
				var uibModalInstance = $uibModal.open({
//					controller: "sysRoleModalCtrl",
					templateUrl: "views/sys/newRole.html",
					show: true,
					scope: $scope
				});
				$scope.newRoleModalInstance = uibModalInstance;
				uibModalInstance.result.then(function(){
					delete $scope.newRoleModalInstance;
					$scope.refreshRoleList();
				}, function(){
					delete $scope.newRoleModalInstance;
					$log.info('取消: ' + new Date())
				});
			};
			
			$scope.showEditRole = function(entity){
				//$scope.currRole = entity;
				var entityJson = angular.toJson(entity);
				$scope.currRole = angular.fromJson(entityJson);
				var uibModalInstance = $uibModal.open({
//					controller: "sysRoleModalCtrl",
					templateUrl: "views/sys/saveRole.html",
					show: true,
					scope: $scope
				});
				$scope.newRoleModalInstance = uibModalInstance;
				uibModalInstance.result.then(function(){
					delete $scope.newRoleModalInstance;
					$scope.refreshRoleList();
				}, function(){
					delete $scope.newRoleModalInstance;
					$log.info('取消: ' + new Date())
				});
			};
			
			var saveItemDefaultOpt = {checker: function(data){
				return data && data.status;
			}};
			$scope.saveItem = function(options){
				var opt = angular.extend({}, saveItemDefaultOpt, options || {});
				$http({  
					   method:'post',  
					   url:opt.url,  
					   data: $httpParamSerializerJQLike(opt.data),  
					   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},  
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
			
			$scope.saveRole = function(){
				$scope.currRole.roleId = 'role:'+$scope.currRole.id;
				$scope.saveItem({
					url:'roleAction/updateRole.do',
					data:$scope.currRole,
					title:'修改角色',
					successCB:function(){
						$scope.newRoleModalInstance.close();
					}
				});
			};
			$scope.saveNewRole = function(){
				$scope.newRole.roleId = 'role:'+$scope.newRole.id;
				$scope.saveItem({
					url: 'roleAction/addRole.do',
					data: $scope.newRole,
					title: '新增角色',
					successCB: function(){
						$scope.newRoleModalInstance.close();
						delete $scope.newRoleModalInstance;
					}
				});
				return false;
			};
			
			$scope.deleteRole = function(role){
				SweetAlert.swal({
	                title: "确定要删除"+role.roleName+"吗？",
	                type: "warning",
	                showCancelButton: true,
	                confirmButtonColor: "#DD6B55",
					confirmButtonText: "确定",
					cancelButtonText: "取消",
	            },function (isConfirm) {
	            	if(!isConfirm)
	            		return;
	            	$http({
						method:'post',
						url:'roleAction/deleteRole.do',
						data:$httpParamSerializerJQLike({roleId:role.id}),
						headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}
					}).success(function(data){
						if(data && data.status){
							$scope.refreshRoleList();
						}else{
							$scope.notice(data.msg);
						}
					});
	            });
			};
			
//			if(!$scope.inited){
//				$scope.inited = true;
			
				$scope.refreshRoleList();
//			}
		});

angular.module('inspinia').controller(
		'sysRoleModalCtrl',
		function($scope, $http, $state, $stateParams, i18nService, $compile,
				$filter, $uibModal, $log, uiGridConstants, $httpParamSerializerJQLike) {

			var saveItemDefaultOpt = {checker: function(data){
				return data && data.status;
			}};
			$scope.saveItem = function(options){
				var opt = angular.extend({}, saveItemDefaultOpt, options || {});
				$http({  
					   method:'post',  
					   url:opt.url,  
					   data: $httpParamSerializerJQLike(opt.data),  
					   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},  
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
			
			$scope.saveRole = function(){
				$scope.currRole.roleId = 'role:'+$scope.currRole.id;
				$scope.saveItem({
					url:'roleAction/updateRole.do',
					data:$scope.currRole,
					title:'修改角色',
					successCB:function(){
						$scope.newRoleModalInstance.close();
					}
				});
			};
			$scope.saveNewRole = function(){
				$scope.newRole.roleId = 'role:'+$scope.newRole.id;
				$scope.saveItem({
					url: 'roleAction/addRole.do',
					data: $scope.newRole,
					title: '新增角色',
					successCB: function(){
						$scope.newRoleModalInstance.close();
						delete $scope.newRoleModalInstance;
					}
				});
				return false;
			};
		});
		
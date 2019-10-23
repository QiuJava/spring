/**
 * 系统管理-系统角色 控制器
 * 
 * @author xyf1
 */

angular.module('inspinia').controller(
		'sysRoleRightCtrl',
		function($scope, $http, $state, $stateParams, i18nService, $compile,
				$filter, $uibModal, $log, uiGridConstants, $httpParamSerializerJQLike,$timeout) {
			i18nService.setCurrentLang('zh-cn');
			
			$scope.roleId = $stateParams.id;	//当前的角色ID
			$scope.roleName =  $stateParams.roleName; // 当前角色的名称
			$scope.showPages = [];				//当前显示菜单所对应的页面操作
			$scope.treeConfig = {
				'plugins' : [ 'types' ],
				types : {
					'default' : { icon : 'fa fa-default' },
					folder: { icon : 'fa fa-folder' },
					leaf : { icon : 'fa fa-leaf' }
				},
				version: 1
			};
			$scope.reCreateTree = function(reparse) {
				$scope.treeConfig.version++;
			};
			$scope.refreshTree = function(){
				$http.get("sysAction/menuTree.do").success(function(data) {
					var parents = [];
					function formatSysMenuData(data) {
						data.text = data.menuName;
						if (data.children && data.children.length) {
							parents.push(data.id);
							angular.forEach(data.children, formatSysMenuData);
							parents.pop();
							data.type = "folder";
							data.state = { opened : true };
						} else {
							data.type = "leaf";
							delete data.children;
						}
						return data;
					}
					angular.forEach(data, formatSysMenuData);
					$scope.sysMenus = data;
					$scope.currMenu = data[0];
					if(!$scope.currMenu.state)
						$scope.currMenu.state = {};
					$scope.currMenu.state.selected=true;
					$scope.refreshPermits();
					$scope.reCreateTree();
				});
			};
			$scope.changedCB = function(ev, data) {
				$scope.checkFlag = false;
				if (data.action != "select_node")
					return;
				$scope.$apply(function() {
					var item = data.node.original;
					$scope.currMenu = item;
					$scope.refreshPermits();
				});
			};
			
			$scope.refreshPermits = function(){
				$scope.showPages = [];
			
				if(!$scope.roleId || !$scope.currMenu.id)
					return;
				$log.info('refreshPermits');
				var param = {menuId: $scope.currMenu.id, roleId: 'role:'+$scope.roleId};
				$http({  
				   method:'post',  
				   url: "roleAction/findRolePrivilege.do",  
				   data: $httpParamSerializerJQLike(param),  
				   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},  
				}).success(function(data){
					param.permits = data;
					$scope.permitBlock = param;
					$scope.showPages = $scope.permitBlock.permits;
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
			
			$scope.savePermits = function(){
				if(!$scope.roleId || !$scope.currMenu.id || !$scope.permitBlock)
					return;
				var param = {roleId: 'roleId:'+$scope.roleId,menuId: $scope.permitBlock.menuId,rigthCode:[]};
				angular.forEach($scope.permitBlock.permits, function(data){
					if(data.state.selected){
						param.rigthCode.push(data.rigthCode);
					}
				});
				param.rigthCode = param.rigthCode.join(',');
				$scope.saveItem({
					url: "roleAction/saveRoleRigth.do",
					data: param,
					title: '保存权限',
					successCB: function(){
					}
				});
			};
			
//			if(!$scope.inited){
//				$scope.inited = true;
			
				$scope.refreshTree();
//			}
				$scope.checkFlag = false;
				//全选
				$scope.checkAll = function(){
					if($scope.checkFlag){
						angular.forEach($scope.showPages,function(data){
							data.state.selected = true;
						})
					} else {
						angular.forEach($scope.showPages,function(data){
							data.state.selected = false;
						})
					}
					
				}
		});

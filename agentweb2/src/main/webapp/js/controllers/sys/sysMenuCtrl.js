/**
 * 系统管理-系统菜单 控制器
 * 
 * @author xyf1
 */

angular.module('inspinia').controller('sysMenuCtrl',
		function($scope, $http, $state, $stateParams, i18nService, $compile, $filter, $uibModal, $log,uiGridConstants, $httpParamSerializer, $httpParamSerializerJQLike) {
			i18nService.setCurrentLang('zh-cn');
			
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
			}

			var openedIds = {};
			
			$scope.openNodeCB = function(ev, data){
				openedIds[data.node.id]=1;
			}
			$scope.closeNodeCB = function(ev, data){
				delete openedIds[data.node.id];
			}
			
			$scope.changedCB = function(ev, data) {
				if (data.action != "select_node")
					return;
				$scope.$apply(function() {
					var item = data.node.original;
					$scope.currMenu = item;
					$scope.refreshPageList();
				});
			}

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
							if (!$scope.currMenu) {
								data.state = { opened : true };
							}else{
								if($scope.currMenuParents.indexOf(data.id)>=0){
									data.state = { opened : true };
								}
							}
						} else {
							if (!$scope.currMenu) {
								$scope.currMenu = data;
								$scope.currMenuParents = parents;
								data.state = { selected : true };
							}
							data.type = "leaf";
							delete data.children;
						}
						if($scope.currMenu && data.id == $scope.currMenu.id){
							if(!data.state)
								data.state = {};
							data.state.selected = true;
						}
						if(openedIds[data.id]){
							if(!data.state)
								data.state = {};
							data.state.opened=true;
						}
						return data;
					}
					angular.forEach(data, formatSysMenuData);
					$scope.sysMenus = data;
					
					$scope.reCreateTree(false);
					$scope.refreshPageList();
				});
			};
			
			$scope.saveMenu = function(){
				$scope.currMenu.menuId = $scope.currMenu.id;
				$http({  
					   method:'post',  
					   url:"sysAction/updateMenu.do",  
					   data: $httpParamSerializerJQLike($scope.currMenu),  
					   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
					})
				.success(function(data){
					if(data && data.state){
						$scope.refreshTree();
						$scope.notice(data.msg);
					}else{
						$scope.notice((data && data.msg) ? data.msg : "修改失败");
					}
				});
			};
			
			$scope.showNewMenu = function(){
				$scope.newMenu = {parentId: $scope.currMenu.id, menuLevel: $scope.currMenu.menuLevel+1, menuType: 'menu'};
				var uibModalInstance = $uibModal.open({
					templateUrl: "views/sys/newMenu.html",
					show: true,
					scope: $scope
				});
				$scope.newMenuModalInstance = uibModalInstance;
				uibModalInstance.result.then(function(){
					$scope.refreshTree();
				}, function(){
					delete $scope.newMenuModalInstance;
					$log.info('取消: ' + new Date())
				});
			}
			
			$scope.saveNewMenu=function(){
				$http({  
					   method:'post',  
					   url:"sysAction/addMenu.do",  
					   data:$httpParamSerializerJQLike($scope.newMenu),  
					   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}
					})
				.success(function(data){
					if(data && data.state){
						$scope.newMenuModalInstance.close();
						delete $scope.newMenuModalInstance;
						$scope.notice(data.msg);
					}else{
						$scope.notice((data && data.msg) ? data.msg : "添加菜单失败");
					}
				});
			}
			
			$scope.refreshTree();
			
			$scope.refreshPageList = function(){
				$scope.gridOptions.data = [];
				$http({  
					   method:'post',  
					   url:"sysAction/findMenuPageList.do",
					   data:$httpParamSerializerJQLike({menuId: $scope.currMenu.id}),  
					   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}
					})
				.success(function(data){
					if(data && data.length){
						$scope.gridOptions.data = data;
					}
				});
			}
			
			$scope.gridOptions={                           //配置表格
					enableSorting:true,
				 	  paginationPageSize:10,                  //分页数量
			          paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
//			          useExternalPagination: true,                //分页数量
				      enableHorizontalScrollbar: 0,        //去掉滚动条
				      enableVerticalScrollbar : 0,         //去掉滚动条
				      columnDefs:[                           //表格数据
//				         { field: 'id',displayName:'序号'},
				         { field: 'menuName',displayName:'名称'},
				         { field: 'menuCode',displayName:'功能编号' },
				         { field: 'menuUrl',displayName:'功能路径' },
				         { field: 'orderNo',displayName:'排序号', width: 80 ,sort: {
				             direction: uiGridConstants.ASC,
				             priority: 1
				           } },
				         { field: 'id',displayName:'操作', width: 80, enableSorting: false,
				        	 cellTemplate: '<div class="lh30" class="ui-grid-cell-contents"><a ng-show="grid.appScope.hasPermit(\'sysMenu:updateFunction\')" ng-click="grid.appScope.showPageModify(row.entity)">修改</a>  |  <a ng-show="grid.appScope.hasPermit(\'sysMenu:deleteFunction\')" ng-click="grid.appScope.pageDelete(row.entity.id, row.entity.menuName)">删除</a></div>'
				         }
				      ],
//				      onRegisterApi: function(gridApi) {                //选中行配置
//				         $scope.gridApi = gridApi;
//				         //全选
//				         $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
//				            if(rows[0].isSelected){
//				               $scope.testRow = rows[0].entity;
//				               for(var i=0;i<rows.length;i++){
//				            	   rowList[rows[i].entity.id]=rows[i].entity;
//				               }
//				            }else{
//				            	rowList={};
//				            }
//				         })
//				         //单选
//				         $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
//				            if(row.isSelected){
//				               $scope.testRow = row.entity;
//				               rowList[row.entity.id]=row.entity;
//				            }else{
//				            	delete rowList[row.entity.id];
//				            }
//				         })
//				         $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
//					          	$scope.paginationOptions.pageNo = newPage;
//					          	$scope.paginationOptions.pageSize = pageSize;
//					            $scope.pageSelect();
//					     });
//				      }
				};
			
			$scope.showPageModify = function(pageData){
				$scope.newPage = pageData;
				if(!$scope.newPage){
					$scope.newPage = {};
					$scope.newPage.isNew = true;
					$scope.newPage.btnText = "新增功能";
				}else{
					$scope.newPage.isNew = false;
					$scope.newPage.btnText = "保存功能";
				}
				$scope.newPage.parentId = $scope.currMenu.id;
				
				var uibModalInstance = $uibModal.open({
					templateUrl: "views/sys/newOrUpdatePage.html",
					show: true,
					scope: $scope
				});
				$scope.newPageModalInstance = uibModalInstance;
				uibModalInstance.result.then(function(){
					$scope.refreshPageList();
				}, function(){
					$log.info('取消: ' + new Date())
				});
			};
			

			$scope.saveNewPage = function() {
				var postData = {
					menuName : $scope.newPage.menuName,
					menuCode : $scope.newPage.menuCode,
					menuUrl : $scope.newPage.menuUrl,
					orderNo : $scope.newPage.orderNo,
				};
				var postUrl;
				if($scope.newPage.isNew){
					postUrl = "sysAction/addFunction.do";
					postData.menuId = $scope.currMenu.id;
				}else{
					postUrl = "sysAction/updateFunction.do";
					postData.id = $scope.newPage.id;
				}
				$http({  
					   method:'post',  
					   url: postUrl,  
					   data:$httpParamSerializerJQLike(postData),  
					   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}
					})
				.success(function(data){
					if(data && data.state){
						$scope.newPageModalInstance.close();
						delete $scope.newPageModalInstance;
						$scope.notice(data.msg);
					}else{
						$scope.notice((data && data.msg) ? data.msg : ($scope.newPage.isNew ? "添加功能失败" : "修改功能失败"));
					}
				});
			};
			
			$scope.pageDelete = function(id, name){
				$scope.typeDel = 'page';
				$scope.idDel = id;
				$scope.nameDel = name;
				var aa = $('#myModal').modal('show');
			}
			
			$scope.deleteModalClose = function(){
				if('page' == $scope.typeDel){
					$http({  
						   method:'post',  
						   url: "sysAction/deleteFunction.do",  
						   data:$httpParamSerializerJQLike({menuId: $scope.idDel}),  
						   headers:{'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}
						})
					.success(function(data){
						if(data && data.state){
							$('#myModal').modal('hide');
							$scope.refreshPageList();
							$scope.notice(data.msg);
						}else{
							$scope.notice((data && data.msg) ? data.msg : "删除功能失败");
						}
					});
				}
			}
		});
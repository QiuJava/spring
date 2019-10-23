angular
		.module('inspinia')
		.controller(
				'TradeSumInfoQueryCtrl',
				function($scope, $rootScope, $http, $state, $stateParams,
						$compile, $filter, i18nService, SweetAlert) {
					i18nService.setCurrentLang('zh-cn'); // 设置语言为中文
					// 数据源
					$scope.info = {
						teamId:"-1",
						startTime : moment(
								new Date().getTime() - 24 * 60 * 60 * 1000)
								.format('YYYY-MM-DD'),
						endTime : moment(new Date().getTime()).format(
								'YYYY-MM-DD')
					}

					// 当前登录代理商业务范围是否有设置
					$scope.showSelect = true;
					$scope.initTeamId = "-1";
					$scope.teams = [{text:"全部",value:"-1"},{text:"盛钱包",value:"200010"},{text:"盛POS",value:"100070"}];
					$scope.getAccesTeamId = function (){
						$http.get('userAction/getAccessTeamId').success(function (result) {
							if(result.status){
								$scope.info.teamId = result.accessTeamId;
								$scope.initTeamId = result.accessTeamId;
							}else {
								$scope.showSelect = false;
							}
						});
					}
					$scope.getAccesTeamId();

					// 查询
					$scope.selectInfo = function() {
						$http.get('userAction/getAccessTeamId').success(function (result) {
							if(result.status){
								$scope.info.teamId = result.accessTeamId;
								$scope.initTeamId = result.accessTeamId;
							}else {
								$scope.showSelect = false;
							}

							$http(
								{
									url : 'TradeSumInfo/query?pageNo='
										+ $scope.paginationOptions.pageNo
										+ "&pageSize="
										+ $scope.paginationOptions.pageSize,
									method : 'POST',
									data : $scope.info
								}).success(function(data) {
								$scope.gridOptions.data = data.result;
								$scope.gridOptions.totalItems = data.totalCount;
								$scope.tradeSum();

							})
						});

					}
					$scope.selectInfo();

					// 导出
					$scope.exportInfo = function() {
						SweetAlert.swal({
							title : "确认导出？",
							showCancelButton : true,
							confirmButtonColor : "#DD6B55",
							confirmButtonText : "提交",
							cancelButtonText : "取消",
							closeOnConfirm : true,
							closeOnCancel : true
						}, function(isConfirm) {
							if (isConfirm) {
								//location.href = "TradeSumInfo/export?info=" +angular.toJson($scope.info);
								$scope.exportInfoClick("TradeSumInfo/export",{"info":angular.toJson($scope.info)});
							}
						})
					}

					$scope.tradeSum = function() {
						$http(
								{
									url : 'TradeSumInfo/tradeSum',
									method : 'POST',
									data : $scope.info
								}).success(function(data) {
							
									if (!data) {
										$scope.gridOptions.tradeSum = 0;
										return;
									}
									$scope.gridOptions.tradeSum = data;

						})
					}
					// 清空
					$scope.clear = function() {
						$scope.info = {
							teamId:$scope.initTeamId,
							startTime : moment(
									new Date().getTime() - 24 * 60 * 60 * 1000)
									.format('YYYY-MM-DD'),
							endTime : moment(new Date().getTime()).format(
									'YYYY-MM-DD')
						}
					}

					$scope.columnDefs = [ {
						field : 'createTime',
						displayName : '统计日期',
						width : 100,
						cellFilter : 'date:"yyyy-MM-dd"'
					}, {
						field : 'branch',
						displayName : '机构',
						width : 150
					}, {
						field : 'oneLevel',
						displayName : '三方1级',
						width : 150
					}, {
						field : 'twoLevel',
						displayName : '三方2级',
						width : 150
					}, {
						field : 'threeLevel',
						displayName : '三方3级',
						width : 150
					}, {
						field : 'fourLevel',
						displayName : '三方4级',
						width : 150
					}, {
						field : 'fiveLevel',
						displayName : '三方5级',
						width : 150
					}, {
						field : 'tradeSum',
						displayName : '交易量（元）',
						width : 120
					}, {
						field : 'merSum',
						displayName : '商户总数',
						width : 120
					}, {
						field : 'activateSum',
						displayName : '激活总数',
						width : 120
					}, {
						field : 'machinesStock',
						displayName : '机具库存',
						width : 120
					}, {
						field : 'unusedMachines',
						displayName : '未使用机具',
						width : 130
					}, {
						field : 'expiredNotActivated',
						displayName : '到期未激活机具',
						width : 200
					} ];

					$scope.gridOptions = { // 配置表格
						paginationPageSize : 10, // 分页数量
						paginationPageSizes : [ 10, 20, 50, 100 ], // 切换每页记录数
						useExternalPagination : true,
						columnDefs : $scope.columnDefs,
						onRegisterApi : function(gridApi) {
							$scope.gridApi = gridApi;
							gridApi.pagination.on
									.paginationChanged(
											$scope,
											function(newPage, pageSize) {
												$scope.paginationOptions.pageNo = newPage;
												$scope.paginationOptions.pageSize = pageSize;
												$scope.selectInfo();
											});
						}
					};
				}).filter('setxingxing', function() {
			return function(value) {
				if (value) {
					var v = value.substring(7, 11);
					return "*********" + v;
				}
			}
		});
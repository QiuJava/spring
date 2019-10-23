function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider, KeepaliveProvider, $httpProvider) {
    // Configure Idle settings
    IdleProvider.idle(5); // in seconds
    IdleProvider.timeout(120); // in seconds
    $urlRouterProvider.otherwise("/service/addService");

    var csrfHeadName = $("meta[name=_csrf_header]").attr('content');
    var csrfData = $("meta[name=_csrf]").attr('content');
    $httpProvider.defaults.headers.common[csrfHeadName] = csrfData;
    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    $httpProvider.interceptors.push(function ($q, $rootScope) {
        return {
            responseError: function (rejection) {
                if (rejection.status === 401) {
                    var defer = $q.defer();
                    $rootScope.$emit('responseError401', rejection, defer, $httpProvider.defaults.headers.common, csrfHeadName);
                    return defer.promise;
                }
                return $q.reject(rejection);
            }
        };
    });

    $ocLazyLoadProvider.config({
        // Set to true if you want to see what and when is dynamically loaded
        debug: false,
        modules: [
            {
                name: 'ngGrid',
                files: ['js/plugins/nggrid/ng-grid.css', 'js/plugins/nggrid/ng-grid-2.0.3.min.js'],
                cache: true
            }, {
                name: 'datePicker',
                files: ['css/plugins/datapicker/angular-datapicker.css', 'js/plugins/datapicker/angular-datepicker.js'],
                cache: true
            }, {
                name: 'ui.select',
                files: ['js/plugins/ui-select/select.min.js', 'css/plugins/ui-select/select.min.css'],
                cache: true
            }, {
                name: 'colorpicker.module',
                files: ['css/plugins/clockpicker/clockpicker.css', 'js/plugins/clockpicker/clockpicker.js',
                    'css/plugins/colorpicker/colorpicker.css', 'js/plugins/colorpicker/bootstrap-colorpicker-module.js'],
                cache: true
            }, {
                name: 'localytics.directives',
                files: ['css/plugins/chosen/chosen.css', 'js/plugins/chosen/chosen.jquery.js', 'js/plugins/chosen/chosen.js'],
                cache: true
            }, {
                name: 'oitozero.ngSweetAlert',
                files: ['js/plugins/sweetalert/sweetalert.min.js', 'css/plugins/sweetalert/sweetalert.css'
                    , 'js/plugins/sweetalert/angular-sweetalert.min.js']
            }, {
                name: 'fileUpload',
                files: ['js/angular/angular-file-upload.min.js']
            }, {
                name: 'ngfileupload',
                files: ['js/plugins/filejs/ng-file-upload.min.js']
            }, {
                name: 'angular-summernote',
                files: ['js/plugins/summernote/angular-summernote.min.js', 'css/plugins/summernote/summernote.css']
            }, {
                name: 'summernote',
                files: ['js/plugins/summernote/summernote.min.js']
            }, {
                name: 'ngJsTree',
                files: ['js/plugins/jsTree/jstree.min.js', 'js/plugins/jsTree/ngJsTree.min.js', 'css/plugins/jsTree/style.min.css'],
                cache: true
            }, {
                name: 'ui-switch',
                files: ['css/plugins/angular-ui-switch/angular-ui-switch.css', 'js/plugins/angular-ui-switch/angular-ui-switch.js']
            }, {
                name: 'fancybox',
                files: ['css/plugins/fancybox/jquery.fancybox.css', 'js/plugins/fancybox/jquery.fancybox.pack.js']
            }, {//时间控件
                name: 'My97DatePicker',
                files: ['js/plugins/My97DatePicker/WdatePicker.js']
            }, {
                name: 'infinity-chosen',
                files: ['css/plugins/chosen/chosen.css', 'js/plugins/chosen/chosen.jquery.js', 'js/plugins/chosen/infinity-angular-chosen.js'],
                cache: true
            }
        ]
    });
    $stateProvider
    /* 登录 */
        .state('login', {
            url: '/login',
            views: {
                '': {templateUrl: 'views/login/login.html'}
            },
            data: {pageTitle: '登录'},
            controller: "loginCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/login/loginCtrl.js']
                    });
                }]
            }
        })

        /*服务管理*/
        .state('service', {
            abstract: true,
            url: "/service",
            templateUrl: "views/common/content.html",
        })
        .state('service.addService', {
            url: "/addService",
            data: {pageTitle: '服务种类增加'},
            templateUrl: "views/index.html",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/login/indexCtrl.js?ver=' + verNo]
                    });
                }]
            }
//            resolve: {
//                loadPlugin: function ($ocLazyLoad) {
//                	deps:['$ocLazyLoad',function ($ocLazyLoad){
//            			return $ocLazyLoad.load({
//            				name:'inspinia',
//            				files: ['js/plugins/iCheck/icheck.min.js']
//            			});
//            		}]
////                    return $ocLazyLoad.load([
////                        {
////                            files: ['css/plugins/iCheck/custom.css','js/plugins/iCheck/icheck.min.js','js/controllers/login/indexCtrl.js']
////                        }
////                    ]);
//                }
//            }
        })


        /*商户管理*/
        .state('merchant', {
            abstract: true,
            url: "/merchant",
            templateUrl: "views/common/content.html",
        })

        .state('merchant.addMer', {
            url: "/addMer",
            data: {pageTitle: '商户进件'},
            views: {
                '': {
                    templateUrl: 'views/merchant/merchantAdd.html'
                },
                'merchantBase@merchant.addMer': {
                    templateUrl: 'views/merchant/merchantBaseInfo.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('colorpicker.module');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ngfileupload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantAddCtrl.js']
                    });
                }]
            }
        })


        .state('merchant.addAcqMer', {
            url: "/addAcqMer",
            data: {pageTitle: '收单商户进件'},
            views: {
                '': {
                    templateUrl: 'views/merchant/acqMerchantAdd.html'
                },
                'acqMerchantBase@merchant.addAcqMer': {
                    templateUrl: 'views/merchant/acqMerchantBaseInfo.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('colorpicker.module');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ngfileupload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/acqMerchantAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.queryAcqMer', {
            url: "/queryAcqMer",
            templateUrl: "views/merchant/acqMerchantQuery.html",
            data: {pageTitle: '查询商户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/acqMerchantQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

         .state('merchant.queryAcqMerDetail', {
            url: "/queryAcqMerDetail/:id",
            templateUrl: "views/merchant/acqMerchantQueryDetail.html",
            data: {pageTitle: '收单商户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/acqMerchantDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('merchant.acqMerUpdate', {
            url: "/acqMerUpdate/:id",
            data: {pageTitle: '修改收单商户商户'},
            views: {
                '': {
                    templateUrl: 'views/merchant/acqMerchantUpdate.html'
                },
                'acqMerchantBase@merchant.acqMerUpdate': {
                    templateUrl: 'views/merchant/acqMerchantBaseInfo.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                	$ocLazyLoad.load('ngGrid')
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('colorpicker.module');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ngfileupload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/acqMerchantUpdateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('merchant.queryMer', {
            url: "/queryMer",
            templateUrl: "views/merchant/merchantQuery.html",
            data: {pageTitle: '查询商户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantQueryCtrl.js']
                    });
                }]
            }
        })

        .state('merchant.queryMerDetail', {
            url: "/queryMerDetail/:mertId",
            templateUrl: "views/merchant/merchantQueryDetail.html",
            data: {pageTitle: '商户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantDetailCtrl.js']
                    });
                }]
            }
        })

        .state('merchant.MerUpdate', {
            url: "/MerUpdate/:mertId",
            templateUrl: "views/merchant/merchantUpdate.html",
            data: {pageTitle: '修改商户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantUpdateCtrl.js']
                    });
                }]
            }
        })
        .state('merchant.auditMer', {
            url: "/auditMer",
            templateUrl: "views/merchant/merchantExamine.html",
            data: {pageTitle: '商户审核'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantExamineCtrl.js']
                    });
                }]
            }
        })
        .state('merchant.MerExamineDetail', {
            url: "/MerExamineDetail/:mertId",
            templateUrl: "views/merchant/merchantExamineDetail.html",
            data: {pageTitle: '商户审核详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/merchantExamineDetailCtrl.js']
                    });
                }]
            }
        })
        .state('merchant.termianlApplyQuery', {
            url: "/termianlApplyQuery",
            templateUrl: 'views/merchant/termianlApplyQuery.html',
            data: {pageTitle: '机具申请查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    // $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/termianlApplyQueryCtrl.js']
                    });
                }]
            }
        })
        .state('merchant.termianlApplyDetail', {
            url: "/termianlApplyDetail/:id",
            templateUrl: 'views/merchant/termianlApplyDetail.html',
            data: {pageTitle: '机具申请详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/termianlApplyDetailCtrl.js']
                    });
                }]
            }
        })
        .state('merchant.dealWithTermianlApply', {
            url: "/dealWithTermianlApply/:id",
            templateUrl: 'views/merchant/dealWithTermianlApply.html',
            data: {pageTitle: '处理机具申请'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/merchant/dealWithTermianlApplyCtrl.js']
                    });
                }]
            }
        })
        //========================================================================
        /*信用卡还款管理*/
        .state('creditRepay', {
            abstract: true,
            url: "/creditRepay",
            templateUrl: "views/common/content.html"
        })
        /*用户管理*/
        .state('creditRepay.manage', {
            url: "/manage",
            templateUrl: "views/creditRepay/manage.html",
            data: {pageTitle: '用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/manageCtrl.js']
                    });
                }]
            }
        })

        /*用户详情*/
        .state('creditRepay.userDetail', {
            url: "/userDetail/:merchantNo",
            templateUrl: "views/creditRepay/userDetail.html",
            data: {pageTitle: '商户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/userDetailCtrl.js']
                    });
                }]
            }
        })
        /*还款订单查询*/
        .state('creditRepay.repayOrder', {
            url: "/repayOrder",
            templateUrl: "views/creditRepay/creditRepayOrder/queryCreditRepayOrder.html",
            data: {pageTitle: '还款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    // $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/queryCreditRepayOrderCtrl.js']
                    });
                }]
            }
        })

        /*还款订单详情界面*/
        .state('creditRepay.repayOrderDetail', {
            url: "/repayOrderDetail/:id",
            data: {pageTitle: '出款订单详细信息'},
            views: {
                '': {
                    templateUrl: 'views/creditRepay/creditRepayOrder/detailCreditRepayOrder.html'
                },
                'detailCreditRepayOrderCommon@creditRepay.repayOrderDetail': {
                    templateUrl: 'views/creditRepay/creditRepayOrder/detailCreditRepayOrderCommon.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/detailCreditRepayOrderCtrl.js']
                    });
                }]
            }
        })

        /*还款订单处理流水*/
        .state('creditRepay.repayBill', {
            url: "/repayBill",
            templateUrl: "views/creditRepay/creditRepayOrder/processingWaterCreditRepayOrder.html",
            data: {pageTitle: '还款订单处理流水'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/processingWaterCreditRepayOrderCtrl.js']
                    });
                }]
            }
        })
        /*还款异常订单*/
        .state('creditRepay.repayException', {
            url: "/repayException",
            templateUrl: "views/creditRepay/creditRepayOrder/abnormalCreditRepayOrder.html",
            data: {pageTitle: '还款异常订单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/abnormalCreditRepayOrderCtrl.js']
                    });
                }]
            }
        })

        /*异常还款订单详情界面*/
        .state('creditRepay.abnormalRepayOrderDetail', {
            url: "/abnormalRepayOrderDetail/:id",
            data: {pageTitle: '出款订单详细信息'},
            views: {
                '': {
                    templateUrl: 'views/creditRepay/creditRepayOrder/abnormalDetailCreditRepayOrder.html'
                },
                'detailCreditRepayOrderCommon@creditRepay.abnormalRepayOrderDetail': {
                    templateUrl: 'views/creditRepay/creditRepayOrder/detailCreditRepayOrderCommon.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/detailCreditRepayOrderCtrl.js']
                    });
                }]
            }
        })

        /*服务商查询*/
        .state('creditRepay.provider', {
            url: "/provider",
            templateUrl: "views/creditRepay/provider.html",
            data: {pageTitle: '服务商查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/providerCtrl.js']
                    });
                }]
            }
        })

        /*服务商分润查询*/
        .state('creditRepay.providerShare', {
            url: "/providerShare",
            templateUrl: "views/creditRepay/providerShare.html",
            data: {pageTitle: '服务商分润'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/providerShareCtrl.js']
                    });
                }]
            }
        })

        .state('creditRepay.activationCode', {
            url: "/activationCode",
            templateUrl: "views/creditRepay/activationCode.html",
            data: {pageTitle: '激活码管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/activationCodeCtrl.js']
                    });
                }]
            }
        })
        /*分润预调帐明细查询*/
        .state('creditRepay.profitAdvance', {
            url: "/creditRepayOrder/profitAdvanceQuery",
            templateUrl: "views/creditRepay/profitAdvance.html",
            data: {pageTitle: '分润预调帐明细'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/profitAdvanceCtrl.js']
                    });
                }]
            }
        })
        //========================================================================
        /*代理商*/
        .state('agent', {
            abstract: true,
            url: "/agent",
            templateUrl: "views/common/content.html"
        })
        .state('agent.addAgent', {
            url: "/addAgent/:mobilephone/:userCode",
            data: {pageTitle: '新增代理商'},
            views: {
                '': {templateUrl: "views/agent/addAgent.html"},
                'agentBase@agent.addAgent': {templateUrl: "views/agent/agentBase.html"}
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/addAgentCtrl.js?var+'+verNo]
                    });
                }]
            }
        })
        .state('agent.queryAgent', {
            url: "/queryAgent",
            templateUrl: "views/agent/queryAgent.html",
            data: {pageTitle: '查询代理商'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/queryAgentCtrl.js']
                    });
                }]
            }
        })
        .state('agent.agentDetail', {
            url: '/agentDetail/:id/:teamId',
            views: {
                '': {templateUrl: 'views/agent/agentDetail.html'},
                'agentDetailBase@agent.agentDetail': {templateUrl: "views/agent/agentDetailBase.html"}
            },
            data: {pageTitle: '代理商详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/agentDetailCtrl.js']
                    })
                }]
            }
        })
        .state('agent.editAgent', {
            url: "/editAgent/:agentNo/:teamId",
            views: {
                '': {templateUrl: "views/agent/editAgent.html"},
                'agentBase@agent.editAgent': {templateUrl: "views/agent/agentBase.html"}
            },
            data: {pageTitle: '修改代理商'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/editAgentCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        // 下级代理商 功能管控设置
        .state('agent.functionSetting', {
            url: "/functionSetting",
            data: {pageTitle: '功能管控设置'},
            templateUrl: 'views/agent/functionSetting.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/functionSettingCtrl.js']
                    });
                }]
            }
        })
        .state('agent.functionSettingConfig', {
            url: '/functionSettingConfig/:functionNumber',
            templateUrl: 'views/agent/functionSettingConfig.html',
            data: {pageTitle: '功能管控配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/functionSettingConfigCtrl.js']
                    })
                }]
            }
        })
        .state('agent.addFunctionSettingConfig', {
            url: '/addFunctionSettingConfig/:functionNumber',
            templateUrl: 'views/agent/addFunctionSettingConfig.html',
            data: {pageTitle: '添加功能管控配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/agent/addFunctionSettingConfigCtrl.js']
                    })
                }]
            }
        })

        /*交易查询*/
        .state('trade', {
            url: "/trade",
            data: {pageTitle: '交易查询'},
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@trade': {
                    templateUrl: 'views/trade/tradeQuery.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
//                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/queryTradeCtrl.js']
                    });
                }]
            }

        })
        .state('tradeQueryDetail', {
            url: "/tradeQueryDetail",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@tradeQueryDetail': {
                    templateUrl: 'views/trade/tradeQueryDetail.html'
                }
            },
            data: {pageTitle: '交易详细信息'},
        })

        // 三方数据查询管理=========================================================================
        .state('threeData', {
            abstract: true,
            url: "/threeData",
            templateUrl: "views/common/content.html"
        })
         /*三方数据查询交易查询*/
        .state('threeData.trade', {
            url: "/trade",
            data: {pageTitle: '三方交易查询'},
            templateUrl: "views/threeData/threeTradeQuery.html",
           /* views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@threeTrade': {
                    templateUrl: 'views/threeData/threeTradeQuery.html'
                }
            },*/
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
//                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/threeData/threeQueryTradeCtrl.js']
                    });
                }]
            }

        })

         // 三方数据查询查询商户
        .state('threeData.merchant', {
            url: "/merchant",
            templateUrl: "views/threeData/threeMerchantQuery.html",
            data: {pageTitle: '三方商户查询'},
           /* views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@threeMerchant': {
                    templateUrl: 'views/threeData/threeMerchantQuery.html'
                }
            },*/
           // controller: "threeMerchantQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/threeData/threeMerchantQueryCtrl.js']
                    });
                }]
            }
        })

          // 三方数据查询 交易汇总查询
        .state('threeData.TradeSumInfo', {
            url: "/TradeSumInfo",
            templateUrl: "views/threeData/TradeSumInfoQuery.html",
            data: {pageTitle: '交易汇总报表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/threeData/TradeSumInfoQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

         .state('threeData.happyBack', {
            url: "/happyBack",
            templateUrl: 'views/threeData/threeHappyBack.html',
            data: {pageTitle: '欢乐返查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/threeData/threeHappyBackCtrl.js']
                    });
                }]
            }
        })

        // 三方数据查询查询机具
        .state('threeData.machine', {
            url: "/machine",
            templateUrl: "views/threeData/threeTerminalQuery.html",
            data: {pageTitle: '三方机具查询'},
           /* views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@threeMachine': {
                    templateUrl: 'views/threeData/threeTerminalQuery.html'
                }
            },*/
           // controller: "threeMerchantQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/threeData/threeTerminalQueryCtrl.js']
                    });
                }]
            }
        })
        /*商户交易查询*/
        .state('tradeByMerchant', {
            url: "/tradeByMerchant",
            data: {pageTitle: '商户交易查询'},
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@tradeByMerchant': {
                    templateUrl: 'views/trade/tradeQueryByMerchant.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/trade/queryTradeByMerchantCtrl.js']
                    });
                }]
            }

        })
        /*信用卡管家 一级*/
        .state('creditCardManager', {
            abstract: true,
            url: "/creditCardManager",
            templateUrl: "views/common/content.html"
        })
        /*信用卡管家 二级 分润查询*/
        .state('creditCardManager.shareQuery', {
            url: "/shareQuery",
            templateUrl: "views/creditCardManager/shareQuery.html",
            data: {pageTitle: '分润查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditCardManager/shareQueryCtrl.js']
                    });
                }]
            }
        })

        /*人人代理 一级*/
        .state('perAgent', {
            abstract: true,
            url: "/perAgent",
            templateUrl: "views/common/content.html"
        })
        /*人人代理 二级 盟主管理*/
        .state('perAgent.user', {
            url: "/user",
            templateUrl: "views/perAgent/user.html",
            data: {pageTitle: '盟主管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/perAgent/userCtrl.js']
                    });
                }]
            }
        })

        /*人人代理 二级 盟主商户查询*/
        .state('perAgent.perMer', {
            url: "/perMer",
            templateUrl: "views/perAgent/mer.html",
            data: {pageTitle: '盟主商户查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/perAgent/merCtrl.js']
                    });
                }]
            }
        })
        /*人人代理 二级 机具申购订单*/
        .state('perAgent.order', {
            url: "/order",
            templateUrl: "views/perAgent/paOrder.html",
            data: {pageTitle: '机具物料申购订单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/perAgent/paOrderCtrl.js']
                    });
                }]
            }
        })
        /*人人代理 机具申购订单发货页面*/
        .state('perAgent.deliver', {
            url: "/deliver/:num/:userCode/:orderNo",
            templateUrl: "views/perAgent/deliver.html",
            data: {pageTitle: '机具下发'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/perAgent/deliverCtrl.js']
                    });
                }]
            }
        })
        .state('perAgent.afterSale', {
            url: "/afterSale",
            templateUrl: "views/perAgent/afterSale.html",
            data: {pageTitle: '申购售后订单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ngfileupload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/perAgent/afterSaleCtrl.js']
                    });
                }]
            }
        })
        .state('perAgent.paCashBackDetail', {
        	url: "/paCashBackDetail",
        	templateUrl: "views/perAgent/paCashBackDetail.html",
        	data: {pageTitle: '盟友活动返现明细'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        			$ocLazyLoad.load('localytics.directives');
        			$ocLazyLoad.load('ui-switch');
        			$ocLazyLoad.load('My97DatePicker');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/perAgent/paCashBackDetailCtrl.js']
        			});
        		}]
        	}
        })
        .state('perAgent.snBack', {
        	url: "/snBack",
        	templateUrl: "views/perAgent/snBack.html",
        	data: {pageTitle: '申购售后订单'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        			$ocLazyLoad.load('localytics.directives');
        			$ocLazyLoad.load('ui-switch');
        			$ocLazyLoad.load('My97DatePicker');
        			$ocLazyLoad.load('fileUpload');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/perAgent/snBackCtrl.js']
        			});
        		}]
        	}
        })


/*
        //调单查询
        .state('surveyOrder', {
            url: "/surveyOrder",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@surveyOrder': {
                    templateUrl: 'views/surveyOrder/surveyOrderQuery.html'
                }
            },
            data: {pageTitle: '调单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/surveyOrderQueryCtrl.js']
                    });
                }]
            }
        })
        /!*调单回复*!/
        .state('orderReply', {
            url: "/orderReply/:id/:orderNo",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@orderReply': {
                    templateUrl: 'views/surveyOrder/orderReply.html'
                }
            },
            data: {pageTitle: '调单回复'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/orderReplyCtrl.js']
                    });
                }]
            }
        })
        /!*调单回复修改*!/
        .state('replyEdit', {
            url: "/replyEdit/:id/:orderNo/:type",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@replyEdit': {
                    templateUrl: 'views/surveyOrder/replyEdit.html'
                }
            },
            data: {pageTitle: '调单回复'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/replyEditCtrl.js']
                    });
                }]
            }
        })
        /!*调单详情*!/
        .state('surveyOrderInfo', {
            url: "/surveyOrderInfo/:id/:orderNo/:from",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@surveyOrderInfo': {
                    templateUrl: 'views/surveyOrder/surveyOrderInfo.html'
                }
            },
            data: {pageTitle: '调单回复'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/surveyOrderInfoCtrl.js']
                    });
                }]
            }
        })
        /!*订单详情*!/
        .state('orderInfo', {
            url: "/orderInfo/:orderNo/:orderServiceCode",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@orderInfo': {
                    templateUrl: 'views/surveyOrder/orderInfo.html'
                }
            },
            data: {pageTitle: '交易订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/orderInfoCtrl.js']
                    });
                }]
            }
        })*/



        /*机具管理*/
        .state('terminal', {
            abstract: true,
            url: "/terminal",
            templateUrl: "views/common/content.html"
        })
        .state('terminal.terminal', {
            url: "/terminal.terminal",
            templateUrl: "views/terminal/terminalQuery.html",
        // .state('terminal.terminal', {
        //     url: "/terminal.terminal",
        //     views: {
        //         // '': {
        //         //     templateUrl: 'views/common/singleContent.html'
        //         // },
        //         'mainView@terminal': {
        //             templateUrl: 'views/terminal/terminalQuery.html'
        //         }
        //     },
            data: {pageTitle: '机具管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalQueryCtrl.js']
                    });
                }]
            }
        })
        .state('terminal.activityCheck', {
            url: "/terminal.activityCheck",
            templateUrl: "views/terminal/terActivityCheckQuery.html",
            data: {pageTitle: '活动考核机具'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terActivityCheckQueryCtrl.js']
                    });
                }]
            }
        })
        .state('terminalQueryDetail', {
            url: "/terminalQueryDetail/:termId",
            views: {
                '': {
                    templateUrl: 'views/common/singleContent.html'
                },
                'mainView@terminalQueryDetail': {
                    templateUrl: 'views/terminal/terminalQueryDetail.html'
                }
            },
            data: {pageTitle: '机具详细信息'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/terminal/terminalDetailCtrl.js']
                    });
                }]
            }

        })

        /*我的信息*/
        .state('myInfo', {
            abstract: true,
            url: "/myInfo",
            templateUrl: "views/common/content.html",
        })
        .state('myInfo.info', {
            url: "/info",
            views: {
                '': {templateUrl: 'views/myInfo/agentDetail.html'},
                'agentDetailBase@myInfo.info': {templateUrl: "views/myInfo/agentDetailBase.html"}
            },
            data: {pageTitle: '我的信息'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/myInfoInfoCtrl.js']
                    });
                }]
            }
        })
        .state('myInfo.account', {
            url: "/account",
            templateUrl: "views/myInfo/account.html",
            data: {pageTitle: '我的账户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/myInfoAccountCtrl.js']
                    });
                }]
            }
        })
        //银行家收支明细
        .state('myInfo.redEnvelopesDetail', {
            url: "/redEnvelopesDetail",
            templateUrl: "views/myInfo/redEnvelopesDetail.html",
            data: {pageTitle: '我的账户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/redEnvelopesDetailCtrl.js']
                    });
                }]
            }
        })
        /*每日分润报表tgh411*/
        .state('myInfo.shareByDay', {
            url: "/shareByDay",
            templateUrl: "views/myInfo/shareByDay.html",
            data: {pageTitle: '我的账户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/myInfoShareByDayCtrl.js']
                    });
                }]
            }
        })
        /*交易分润明细*/
        .state('myInfo.tradeShare', {
            url: "/myInfo/tradeShare",
            templateUrl: "views/myInfo/tradeShare.html",
            data: {pageTitle: '我的账户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/tradeShareCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*预冻结明细查询*/
        .state('myInfo.preliminaryFreezeQuery', {
            url: "/myInfo/preliminaryFreezeQuery",
            templateUrl: "views/myInfo/preliminaryFreezeQuery.html",
            data: {pageTitle: '我的账户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/preliminaryFreezeQueryCtrl.js']
                    });
                }]
            }
        })
        /*解冻明细查询*/
        .state('myInfo.unFreezeQuery', {
            url: "/myInfo/unFreezeQuery",
            templateUrl: "views/myInfo/unFreezeQuery.html",
            data: {pageTitle: '我的账户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/unFreezeQueryCtrl.js']
                    });
                }]
            }
        })
          //修改安全手机
    /*    .state('myInfo.safePhone', {
            url: "/myInfo/safePhone",
            templateUrl: "views/myInfo/safePhone.html",
            data: {pageTitle: '修改安全手机'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/safePhoneCtrl.js']
                    });
                }]
            }
        })*/
        //修改安全手机
        .state('myInfo.newsafePhone', {
            url: "/myInfo/newsafePhone/:type",
            templateUrl: "views/myInfo/newsafePhone.html",
            data: {pageTitle: '修改安全手机'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/newsafePhoneCtrl.js']
                    });
                }]
            }
        })
        .state('myInfo.safePhone', {
            url: "/myInfo/safePhone",
            templateUrl: "views/myInfo/oldsafePhone.html",
            data: {pageTitle: '修改安全手机'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/oldsafePhoneCtrl.js']
                    });
                }]
            }
        })
        .state('myInfo.safeSet', {
            url: "/myInfo/safeSet",
            templateUrl: "views/myInfo/safeSet.html",
            data: {pageTitle: '安全设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/safeSetCtrl.js']
                    });
                }]
            }
        })
        .state('myInfo.safePassword', {
            url: "/myInfo/safePassword",
            templateUrl: "views/myInfo/safePassword.html",
            data: {pageTitle: '修改安全密码'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/safePasswordCtrl.js']
                    });
                }]
            }
        })

        /*预调账明细查询*/
        .state('myInfo.preliminaryAdjustQuery', {
            url: "/myInfo/preliminaryAdjustQuery",
            templateUrl: "views/myInfo/preliminaryAdjustQuery.html",
            data: {pageTitle: '我的账户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/preliminaryAdjustQueryCtrl.js']
                    });
                }]
            }
        })

          /*活动预调账明细*/
        .state('myInfo.happyBackNotFullDeductDetail', {
            url: "/myInfo/happyBackNotFullDeductDetailt",
            templateUrl: "views/myInfo/happyBackNotFullDeductDetail.html",
            data: {pageTitle: '我的账户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                	$ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/myInfo/happyBackNotFullDeductDetailCtrl.js']
                    });
                }]
            }
        })

        /*超级推管理*/
        .state('push', {
            abstract: true,
            url: "/push",
            templateUrl: "views/common/content.html",
        })
        .state('push.addRule', {
            url: "/addRule",
            templateUrl: "views/push/addRule.html",
            data: {pageTitle: '添加超级推规则'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/push/addRuleInfoCtrl.js']
                    });
                }]
            }
        })
        .state('push.managerRule', {
            url: "/managerRule",
            templateUrl: "views/push/queryRule.html",
            data: {pageTitle: '超级推规则管理'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/push/queryRuleInfoCtrl.js']
                    });
                }]
            }
        })
        .state('push.editRule', {
            url: "/editRule/:id",
            templateUrl: "views/push/editRuleInfo.html",
            data: {pageTitle: '修改超级推规则'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/push/editRuleInfoCtrl.js']
                    });
                }]
            }
        })
        .state('push.ruleDetail', {
            url: "/ruleDetail/:id",
            templateUrl: "views/push/ruleDetail.html",
            data: {pageTitle: '超级推规则详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/push/ruleDetailCtrl.js']
                    });
                }]
            }
        })
        .state('push.queryOrder', {
            url: "/queryOrder",
            templateUrl: "views/push/queryOrder.html",
            data: {pageTitle: '超级推订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/push/queryOrderCtrl.js']
                    });
                }]
            }
        })
        .state('push.orderDetail', {
            url: "/orderDetail/:id",
            templateUrl: "views/push/orderDetail.html",
            data: {pageTitle: '超级推订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/push/orderDetailCtrl.js']
                    });
                }]
            }
        })

        /*更多内容*/
        .state('more', {
            abstract: true,
            url: "/more",
            templateUrl: "views/common/content.html",
        })
        .state('more.receivedNotices', {
            url: "/receivedNotices",
            templateUrl: "views/more/receivedNotices.html",
            data: {pageTitle: '收到的公告'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    // $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/receivedNoticesCtrl.js']
                    });
                }]
            }
        })
        .state('more.addNotice', {
            url: "/addNotice",
            views: {
                '': {
                    templateUrl: 'views/more/addNotice.html'
                },
                'noticeEditCommon@more.addNotice': {
                    templateUrl: 'views/more/noticeEditCommon.html'
                }
            },
            data: {pageTitle: '新增下发公告'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/addNoticeCtrl.js']
                    });
                }]
            }
        })
        .state('more.noticeDetail', {
            url: "/noticeDetail/:id",
            data: {pageTitle: '公告详情'},
            views: {
                '': {
                    templateUrl: 'views/more/noticeDetail.html'
                },
                'noticeCommon@more.noticeDetail': {
                    templateUrl: 'views/more/noticeCommon.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/noticeDetailCtrl.js']
                    });
                }]
            }
        })
        .state('more.rnoticeDetail', {
            url: "/rnoticeDetail/:id",
            data: {pageTitle: '公告详情'},
            views: {
                '': {
                    templateUrl: 'views/more/rnoticeDetail.html'
                },
                'noticeCommon@more.rnoticeDetail': {
                    templateUrl: 'views/more/noticeCommon.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/noticeDetailCtrl.js']
                    });
                }]
            }
        })
        .state('more.modifyNotice', {
            url: "/modifyNotice/:id",
            data: {pageTitle: '修改公告'},
            views: {
                '': {
                    templateUrl: 'views/more/modifyNotice.html'
                },
                'noticeEditCommon@more.modifyNotice': {
                    templateUrl: 'views/more/noticeEditCommon.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/modifyNoticeCtrl.js']
                    });
                }]
            }
        })
        .state('more.deliverNotice', {
            url: "/deliverNotice/:id",
            data: {pageTitle: '下发公告'},
            views: {
                '': {
                    templateUrl: 'views/more/deliverNotice.html'
                },
                'noticeCommon@more.deliverNotice': {
                    templateUrl: 'views/more/noticeCommon.html'
                }
            },
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker')
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/deliverNoticeCtrl.js']
                    });
                }]
            }
        })
        .state('more.sentNotices', {
            url: "/sendNotices",
            templateUrl: "views/more/sentNotices.html",
            data: {pageTitle: '下发公告查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/sentNoticesCtrl.js']
                    });
                }]
            }
        })
        .state('more.addBanner', {
            url: "/addBanner",
            views: {
                '': {
                    templateUrl: 'views/more/addBanner.html'
                },
                'bannerCommon@more.addBanner': {
                    templateUrl: 'views/more/bannerCommon.html'
                }
            },
            data: {pageTitle: '新增代理商APP banner'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/addBannerCtrl.js']
                    });
                }]
            }
        })
        .state('more.queryBanners', {
            url: "/queryBanners",
            templateUrl: "views/more/queryBanners.html",
            data: {pageTitle: '代理商APP banner查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/queryBannersCtrl.js']
                    });
                }]
            }
        })

        .state('more.bannerDetail', {
            url: "/bannerDetail/:id",
            templateUrl: "views/more/bannerDetail.html",
            data: {pageTitle: '代理商App banner详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/bannerDetailCtrl.js']
                    });
                }]
            }
        })
        .state('more.modifyBanner', {
            url: "/modifyBanner/:id",
            views: {
                "": {templateUrl: "views/more/modifyBanner.html"},
                "bannerCommon@more.modifyBanner": {templateUrl: "views/more/bannerCommon.html"}
            },
            data: {pageTitle: '代理商App 修改banner'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/more/modifyBannerCtrl.js']
                    });
                }]
            }
        })

        /*系统管理*/
        .state('sys', {
            abstract: true,
            url: "/sys",
            templateUrl: "views/common/content.html",
        })
        .state('sys.sysMenu', {
            url: "/sysMenu",
            templateUrl: "views/sys/sysMenu.html",
            data: {pageTitle: '系统菜单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngJsTree');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sys/sysMenuCtrl.js']
                    });
                }]
            }
        })
        .state('sys.sysRole', {
            url: "/sysRole",
            templateUrl: "views/sys/sysRole.html",
            data: {pageTitle: '系统角色'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sys/sysRoleCtrl.js', 'views/sys/sysRole.css']
                    });
                }]
            }
        })
        .state('sys.sysRoleRight', {
            url: "/sysRole/Right/:id/:roleName",
            templateUrl: "views/sys/sysRoleRight.html",
            data: {pageTitle: '系统角色编辑菜单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sys/sysRoleRightCtrl.js', 'views/sys/sysRole.css']
                    });
                }]
            }
        })
        .state('sys.sysUser', {
            url: "/sysUser",
            templateUrl: "views/sys/sysUser.html",
            data: {pageTitle: '系统用户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sys/sysUserCtrl.js']
                    });
                }]
            }
        })
        .state('sys.sysUserPermits', {
            url: "/sysUserPermits/:id",
            templateUrl: "views/sys/sysUser.html",
            data: {pageTitle: '系统用户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngJsTree');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/sys/sysUserPermitsCtrl.js']
                    });
                }]
            }
        })
        //======tgh=============================================================
        .state('active', {
            abstract: true,
            url: "/active",
            templateUrl: "views/common/content.html",
        })
        // === 激活码管理
        // .state('active.activationCode', {
        //     url: "/activationCode",
        //     templateUrl: 'views/activity/activationCode.html',
        //     data: {pageTitle: '激活码管理'},
        //     // controller: "activationCodeCtrl",
        //     resolve: {
        //         loadPlugin: function ($ocLazyLoad) {
        //             $ocLazyLoad.load('ngGrid');
        //             $ocLazyLoad.load('ui.select');
        //             $ocLazyLoad.load('oitozero.ngSweetAlert');
        //             $ocLazyLoad.load('My97DatePicker');
        //             $ocLazyLoad.load('localytics.directives');
        //         },
        //         deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        //             return $ocLazyLoad.load({
        //                 name: 'inspinia',
        //                 files: ['js/controllers/activity/activationCodeCtrl.js']
        //             });
        //         }]
        //     }
        // })
        .state('active.activity', {
            url: "/activity",
            templateUrl: 'views/activity/activity.html',
            data: {pageTitle: '欢乐送业务查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    // $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityCtrl.js']
                    });
                }]
            }
        })
        // ====== 赠送记录查询 ===========
        .state('active.giftsRecord', {
            url: "/giftsRecord",
            templateUrl: 'views/activity/giftsRecord.html',
            data: {pageTitle: '赠送记录查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/giftsRecordCtrl.js']
                    });
                }]
            }
        })
        // 商户收益查询
        .state('active.merchantIncome', {
            url: "/merchantIncome",
            templateUrl: 'views/activity/merchantIncome.html',
            data: {pageTitle: '商户收益查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/merchantIncomeCtrl.js']
                    });
                }]
            }
        })
        //  .state('active.superPush', {
        //     url: "/superPush",
        //     templateUrl: 'views/activity/superPush.html',
        //     data: {pageTitle: '超级推活动查询'},
        //     controller: "superPushCtrl",
        //     resolve: {
        //     loadPlugin: function ($ocLazyLoad) {
        //     		$ocLazyLoad.load('datePicker');
        //             $ocLazyLoad.load('localytics.directives');
        //             $ocLazyLoad.load('oitozero.ngSweetAlert');
        //             $ocLazyLoad.load('fileUpload');
        //         },
        //         deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        //             return $ocLazyLoad.load({
        //                 name: 'inspinia',
        //                 files: ['js/controllers/activity/superPushCtrl.js']
        //             });
        //         }]
        //     }
        // })
        // .state('active.superPushMerchantDetail', {
        //    url: "/superPushMerchantDetail/:mertId",
        //    templateUrl: 'views/activity/superPushMerchantDetail.html',
        //    data: {pageTitle: '超级推商户详情'},
        //    controller: "superPushMerchantDetailCtrl",
        //    resolve: {
        // 	   loadPlugin: function ($ocLazyLoad) {
        // 		   $ocLazyLoad.load('datePicker');
        // 		   $ocLazyLoad.load('localytics.directives');
        // 		   $ocLazyLoad.load('oitozero.ngSweetAlert');
        // 		   $ocLazyLoad.load('fileUpload');
        // 	   },
        // 	   deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        // 		   return $ocLazyLoad.load({
        // 			   name: 'inspinia',
        // 			   files: ['js/controllers/activity/superPushMerchantDetailCtrl.js']
        // 		   });
        // 	   }]
        //    }
        // })
        // .state('active.superPushCashDetail', {
        //    url: "/superPushCashDetail/:mertId",
        //    templateUrl: 'views/activity/superPushCashDetail.html',
        //    data: {pageTitle: '超级推提现详情'},
        //    controller: "superPushCashDetailCtrl",
        //    resolve: {
        // 	   loadPlugin: function ($ocLazyLoad) {
        // 		   $ocLazyLoad.load('datePicker');
        // 		   $ocLazyLoad.load('localytics.directives');
        // 		   $ocLazyLoad.load('oitozero.ngSweetAlert');
        // 		   $ocLazyLoad.load('fileUpload');
        // 	   },
        // 	   deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        // 		   return $ocLazyLoad.load({
        // 			   name: 'inspinia',
        // 			   files: ['js/controllers/activity/superPushCashDetailCtrl.js']
        // 		   });
        // 	   }]
        //    }
        // })
        // .state('active.superPushShareDetail', {
        //    url: "/superPushShareDetail/:mertId",
        //    templateUrl: 'views/activity/superPushShareDetail.html',
        //    data: {pageTitle: '超级推分润详情'},
        //    controller: "superPushShareDetailCtrl",
        //    resolve: {
        // 	   loadPlugin: function ($ocLazyLoad) {
        // 		   $ocLazyLoad.load('My97DatePicker');
        // 		   $ocLazyLoad.load('localytics.directives');
        // 		   $ocLazyLoad.load('oitozero.ngSweetAlert');
        // 		   $ocLazyLoad.load('fileUpload');
        // 	   },
        // 	   deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        // 		   return $ocLazyLoad.load({
        // 			   name: 'inspinia',
        // 			   files: ['js/controllers/activity/superPushShareDetailCtrl.js']
        // 		   });
        // 	   }]
        //    }
        // })
        //购买记录查询
        .state('active.activityOrderInfoQuery', {
            url: "/activityOrderInfoQuery",
            templateUrl: "views/activity/activityOrderInfoQuery.html",
            data: {pageTitle: '购买记录查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityOrderInfoQueryCtrl.js']
                    });
                }]
            }
        })
        //购买记录详情
        .state('active.activityOrderInfoDetail', {
            url: "/activityOrderInfoDetail/:id",
            templateUrl: 'views/activity/activityOrderInfoDetail.html',
            data: {pageTitle: '购买记录详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/activityOrderInfoDetailCtrl.js']
                    });
                }]
            }
        })
        //欢乐返查询
        .state('active.happyBack', {
            url: "/happyBack",
            templateUrl: 'views/activity/happyBack.html',
            data: {pageTitle: '欢乐返查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/happyBackCtrl.js']
                    });
                }]
            }
        })
         .state('active.happyBackDetail', {
            url: "/happyBackDetail/:hId",
            templateUrl: "views/activity/happyBackDetail.html",
            data: {pageTitle: '欢乐返商户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/happyBackDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //VIP优享订单查询
        .state('active.subscribeVipQuery', {
            url: "/subscribeVipQuery",
            templateUrl: "views/activity/subscribeVipQuery.html",
            data: {pageTitle: 'VIP优享订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/subscribeVipQuery.js']
                    });
                }]
            }
        })
        //欢乐返活跃商户活动查询
        .state('active.happyBackActivityMerchant', {
            url: "/happyBackActivityMerchant",
            templateUrl: "views/activity/happyBackActivityMerchant.html",
            data: {pageTitle: '欢乐返活跃商户活动查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/activity/happyBackActivityMerchantCtrl.js']
                    });
                }]
            }
        })
        /*直清商户*/
        .state('zqMerchant', {
            abstract: true,
            url: "/zqMerchant",
            templateUrl: "views/common/content.html",
        })
        .state('zqMerchant.zqQueryMerchant', {
            url: "/zqMerchant/zqQueryMerchant",
            templateUrl: "views/zqMerchant/zqQueryMerchant.html",
            data: {pageTitle: '商户查询'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "zqQueryMerchantCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngJsTree');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/zqMerchant/zqQueryMerchantCtrl.js']
                    });
                }]
            }
        })
        .state('zqMerchant.zqTrade', {
            url: "/zqMerchant/zqTrade",
            templateUrl: "views/zqMerchant/zqTrade.html",
            data: {pageTitle: '交易查询'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "zqTradeCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngJsTree');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/zqMerchant/zqTradeCtrl.js']
                    });
                }]
            }
        })
        /*超级推管理*/
        .state('superPush', {
            abstract: true,
            url: "/superPush",
            templateUrl: "views/common/content.html"
        })
        .state('superPush.merchantQuery', {
            url: "/superPush/merchantQuery",
            templateUrl: "views/superPush/merchantQuery.html",
            data: {pageTitle: '商户查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superPush/merchantQueryCtrl.js']
                    });
                }]
            }
        })
        .state('superPush.superPushMerchantDetail', {
            url: "/superPushMerchantDetail/:userId",
            templateUrl: 'views/superPush/superPushMerchantDetail.html',
            data: {pageTitle: '微创业商户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superPush/superPushMerchantDetailCtrl.js']
                    });
                }]
            }
        })
        .state('superPush.shareQuery', {
            url: "/superPush/shareQuery",
            templateUrl: "views/superPush/shareQuery.html",
            data: {pageTitle: '分润查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superPush/shareQueryCtrl.js']
                    });
                }]
            }
        })
        .state('superPush.invitePrizesMerchant', {
            url: "/superPush/invitePrizesMerchant",
            templateUrl: "views/superPush/invitePrizesMerchant.html",
            data: {pageTitle: '邀请有奖查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superPush/invitePrizesMerchantCtrl.js']
                    });
                }]
            }
        })
        //超级银行家 用户管理
        .state('superBank', {
            abstract: true,
            url: "/superBank",
            templateUrl: "views/common/content.html"
        })
        .state('superBank.userManage', {
            url: "/userManage",
            templateUrl: 'views/superBank/userManage.html',
            data: {pageTitle: '用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/userManageCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.userInfoSuperBank', {
            url: "/userInfoSuperBank/:userId/:userCode",
            templateUrl: "views/superBank/userInfoSuperBankDetail.html",
            data: {pageTitle: '用户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
//                    $ocLazyLoad.load('ngGrid');
//                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/userInfoSuperBankDetailCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.agentOrder', {
            url: "/agentOrder",
            templateUrl: "views/superBank/agentOrder.html",
            data: {pageTitle: '代理授权订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/agentOrderCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.agentOrderDetail', {
            url: "/agentOrderDetail/:orderNo",
            templateUrl: 'views/superBank/agentOrderDetail.html',
            data: {pageTitle: '代理商授权订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/agentOrderDetailCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.creditOrder', {
            url: "/creditOrder",
            templateUrl: "views/superBank/creditOrder.html",
            data: {pageTitle: '办理信用卡订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/creditOrderCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.creditOrderDetail', {
            url: "/creditOrderDetail/:orderNo",
            templateUrl: 'views/superBank/creditOrderDetail.html',
            data: {pageTitle: '办理信用卡订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/creditOrderDetailCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.loanOrder', {
            url: "/loanOrder",
            templateUrl: "views/superBank/loanOrder.html",
            data: {pageTitle: '贷款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/loanOrderCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.loanOrderDetail', {
            url: "/loanOrderDetail/:orderNo",
            templateUrl: 'views/superBank/loanOrderDetail.html',
            data: {pageTitle: '贷款订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/loanOrderDetailCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.receiveOrder', {
            url: "/receiveOrder",
            templateUrl: "views/superBank/receiveOrder.html",
            data: {pageTitle: '收款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/receiveOrderCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.collectOrderDetail', {
            url: "/collectOrderDetail/:orderNo",
            templateUrl: 'views/superBank/receiveOrderDetail.html',
            data: {pageTitle: '收款订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/receiveOrderDetailCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.repayOrder', {
            url: "/repayOrder",
            templateUrl: "views/superBank/repayOrder.html",
            data: {pageTitle: '还款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/repayOrderCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.repayOrderDetail', {
            url: "/repayOrderDetail/:orderNo",
            templateUrl: 'views/superBank/repayOrderDetail.html',
            data: {pageTitle: '还款订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/repayOrderDetailCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.openCreditOrder', {
            url: "/openCreditOrder",
            templateUrl: "views/superBank/openCreditOrder.html",
            data: {pageTitle: '开通信用卡还款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/openCreditOrderCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.openCreditDetail', {
            url: "/openCreditDetail/:orderNo",
            templateUrl: 'views/superBank/openCreditDetail.html',
            data: {pageTitle: '开通信用卡还款订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/openCreditDetailCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.profitDetailOrder', {
            url: "/profitDetailOrder",
            templateUrl: "views/superBank/profitDetailOrder.html",
            data: {pageTitle: '订单分润明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    // $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/profitDetailOrderCtrl.js']
                    });
                }]
            }
        })
        //贷款奖金管理
        .state('superBank.loanBonus', {
            url: '/loanConfList',
            templateUrl: "views/superBank/loanBonus.html",
            data: {pageTitle: '贷款总奖金计算表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/loanBonusCtrl.js']
                    });
                }]
            }
        })
        //银行家OEM信息
        .state('superBank.OEMQuery', {
            url: "/OEMQuery",
            templateUrl: "views/superBank/OEMQuery.html",
            data: {pageTitle: '银行家OEM信息'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/OEMQueryCtrl.js']
                    });
                }]
            }
        })
        //信用卡奖金配置
        .state('superBank.creditcardBonus', {
            url: '/getCreditCardConf',
            templateUrl: "views/superBank/creditcardBonus.html",
            data: {pageTitle: '信用卡奖励表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/creditcardBonusCtrl.js']
                    });
                }]
            }
        })
        //银行家 排行榜管理
        .state('superBank.rankingRecord', {
            url: "/rankingRecord",
            templateUrl: "views/rankingRecord/rankingRecord.html",
            data: {pageTitle: '排行榜管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');

                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/rankingRecord/rankingRecordCtrl.js']
                    });
                }]
            }
        })
        //银行家 排行榜 用户奖金发放记录
        .state('superBank.rankingPushRecord', {
            url: "/rankingPushRecord",
            templateUrl: "views/rankingRecord/rankingPushRecord.html",
            data: {pageTitle: '排行榜管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');

                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/rankingRecord/rankingPushRecordCtrl.js']
                    });
                }]
            }
        })
        //银行家 排行榜 详细
        .state('superBank.rankingRecordDetail', {
            url: "/rankingRecordDetail/:id",
            templateUrl: "views/rankingRecord/rankingRecordDetail.html",
            data: {pageTitle: '排行榜管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');

                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/rankingRecord/rankingRecordDetailCtrl.js']
                    });
                }]
            }
        })
        //银行家 保险订单查询
        .state('superBank.insuranceOrder', {
            url: "/insuranceOrder",
            templateUrl: "views/superBank/insuranceOrder.html",
            data: {pageTitle: '保险订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/insuranceOrderCtrl.js']
                    });
                }]
            }
        })
        //银行家 保险订单详情
        .state('superBank.insuranceOrderDetail', {
            url: "/insuranceOrderDetail/:orderNo",
            templateUrl: 'views/superBank/insuranceOrderDetail.html',
            data: {pageTitle: '保险订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/insuranceOrderDetailCtrl.js']
                    });
                }]
            }
        })
        //Mpos 机具管理
        .state('superBank.mposMachines',{
            url: '/superBank/mposMachines',
            templateUrl: "views/superBank/mposMachines.html",
            data: {pageTitle: 'Mpos机具管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/mposMachinesCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Mpos 订单查询
        .state('superBank.mposOrder',{
            url: '/superBank/mposOrder',
            templateUrl: "views/superBank/mposOrder.html",
            data: {pageTitle: 'Mpos订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js','js/controllers/superBank/mposOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Mpos 订单详情
        .state('superBank.mposOrderDetail',{
            url: '/superBank/mposOrderDetail/:orderNo',
            templateUrl: "views/superBank/mposOrderDetail.html",
            data: {pageTitle: 'Mpos订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/mposOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Mpos 订单发货
        .state('superBank.mposOrderShip',{
            url: '/superBank/mposOrderShip/:orderNo',
            templateUrl: "views/superBank/mposOrderShip.html",
            data: {pageTitle: 'Mpos订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/mposOrderShipCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Mpos 分润明细
        .state('superBank.mposProfitDetail',{
            url: '/superBank/mposProfitDetail',
            templateUrl: "views/superBank/mposProfitDetail.html",
            data: {pageTitle: 'Mpos分润明细'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js','js/controllers/superBank/mposProfitDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Mpos 激活查询
        .state('superBank.mposActiveOrder',{
            url: '/superBank/mposActiveOrder',
            templateUrl: "views/superBank/mposActiveOrder.html",
            data: {pageTitle: 'Mpos激活查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js','js/controllers/superBank/mposActiveOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        // Mpos激活信息详情界面
        .state('superBank.mposActiveOrderDetail', {
            url: "/superBank/mposActiveOrderDetail/:orderNo",
            templateUrl: 'views/superBank/mposActiveOrderDetail.html',
            data: {pageTitle: 'Mpos激活信息详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/mposActiveOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Mpos 交易查询
        .state('superBank.mposTradeOrder',{
            url: '/superBank/mposTradeOrder',
            templateUrl: "views/superBank/mposTradeOrder.html",
            data: {pageTitle: 'Mpos交易查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js','js/controllers/superBank/mposTradeOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        // Mpos交易信息详情界面
        .state('superBank.mposTradeOrderDetail', {
            url: "/superBank/mposTradeOrderDetail/:orderNo",
            templateUrl: 'views/superBank/mposTradeOrderDetail.html',
            data: {pageTitle: 'Mpos交易信息详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/mposTradeOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Mpos 商户交易数据汇总
        .state('superBank.mposMerchantTradeCount',{
            url: '/superBank/mposMerchantTradeCount',
            templateUrl: "views/superBank/mposMerchantTradeCount.html",
            data: {pageTitle: 'Mpos商户交易数据汇总'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/mposMerchantTradeCountCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //-------- start 积分兑换-激活版 ----------------------
        .state('redemptionActive', {
            abstract: true,
            url: "/redemptionActive",
            templateUrl: "views/common/content.html"
        })
        /*激活码管理*/
        .state('redemptionActive.raActiveCode', {
            url: "/raActiveCode",
            templateUrl: "views/redemActive/raActivationCode.html",
            data: {pageTitle: '激活码管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redemActive/raActivationCodeCtrl.js']
                    });
                }]
            }
        })
        /*用户管理*/
        .state('redemptionActive.raUser', {
            url: "/raUser",
            templateUrl: "views/redemActive/raUserQuery.html",
            data: {pageTitle: '积分兑换-激活版用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redemActive/raUserQueryCtrl.js']
                    });
                }]
            }
        })
        .state('redemption.raUserDetails', {
            url: "/user/raUserDetails/:merchantNo",
            templateUrl: "views/redemActive/raUserDetails.html",
            data: {pageTitle: '用户详情'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "userDetailsCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redemActive/raUserDetailsCtrl.js']
                    });
                }]
            }
        })
        /*服务商查询*/
        .state('redemptionActive.raAgent', {
            url: "/raAgent",
            templateUrl: "views/redemActive/raProvider.html",
            data: {pageTitle: '服务商查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redemActive/raProviderCtrl.js']
                    });
                }]
            }
        })
        /*积分兑换订单查询*/
        .state('redemptionActive.raDeclareOrder', {
            url: "/raDeclareOrder",
            templateUrl: "views/redemActive/raDeclareOrder.html",
            data: {pageTitle: '积分兑换订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redemActive/raDeclareOrderCtrl.js']
                    });
                }]
            }
        })
        /*商户收款订单查询*/
        .state('redemptionActive.raReceiveOrder', {
            url: "/raReceiveOrder",
            templateUrl: "views/redemActive/raReceiveOrder.html",
            data: {pageTitle: '商户收款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redemActive/raReceiveOrderCtrl.js']
                    });
                }]
            }
        })
        /*信用卡还款订单查询*/
        .state('redemptionActive.raRepayOrder', {
            url: "/raRepayOrder",
            templateUrl: "views/redemActive/raRepayOrder.html",
            data: {pageTitle: '信用卡还款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redemActive/raRepayOrderCtrl.js']
                    });
                }]
            }
        })
        /*订单分润明细查询*/
        .state('redemptionActive.raShare', {
            url: "/raShare",
            templateUrl: "views/redemActive/raShareQuery.html",
            data: {pageTitle: '订单分润明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redemActive/raShareQueryCtrl.js']
                    });
                }]
            }
        })
        //-------- end 积分兑换-激活版 ----------------------
        /**
         * 银行家开发配置
         */
        .state('superBank.developmentConfiguration', {
            url: '/developmentConfiguration',
            templateUrl: "views/superBank/developmentConfiguration.html",
            data: {pageTitle: '开发配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/developmentConfigurationCtrl.js']
                    });
                }]
            }
        })
        //银行家红包
        .state('red', {
            abstract: true,
            url: "/red",
            templateUrl: "views/common/content.html"
        })
        /*红包领取查询*/
        .state('red.redEnvelopesReceive', {
            url: "/redEnvelopesReceive",
            templateUrl: "views/superBank/redEnvelopes/queryRedEnvelopesReceive.html",
            data: {pageTitle: '红包领取查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/redEnvelopes/queryRedEnvelopesReceiveCtrl.js']
                    });
                }]
            }
        })
        /*红包发放查询*/
        .state('red.redEnvelopesGrant', {
            url: "/redEnvelopesGrant",
            templateUrl: "views/superBank/redEnvelopes/queryRedEnvelopesGrant.html",
            data: {pageTitle: '红包发放查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/redEnvelopes/queryRedEnvelopesGrantCtrl.js']
                    });
                }]
            }
        })
        .state('red.redEnvelopesGrantDetail', {
            url: "/redEnvelopesGrantDetail/:id",
            templateUrl: "views/superBank/redEnvelopes/redEnvelopesGrantDetail.html",
            data: {pageTitle: '/红包详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/redEnvelopes/redEnvelopesGrantDetailCtrl.js']
                    });
                }]
            }
        })
        // 积分兑换
        .state('redemption', {
            abstract: true,
            url: "/redemption",
            templateUrl: "views/common/content.html",
        })
        .state('redemption.user', {
            url: "/user",
            templateUrl: "views/redem/userQuery.html",
            data: {pageTitle: '用户管理'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "userQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redem/userQueryCtrl.js']
                    });
                }]
            }
        })
        .state('redemption.userDetails', {
            url: "/user/userDetails/:merchantNo",
            templateUrl: "views/redem/userDetails.html",
            data: {pageTitle: '用户详情'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "userDetailsCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redem/userDetailsCtrl.js']
                    });
                }]
            }
        })
        .state('redemption.userAddAgent', {
            url: "/user/addAgent/:merchantNo",
            templateUrl: "views/redem/userAddAgent.html",
            data: {pageTitle: '积分兑换新增二级代理商'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "userAddAgentCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redem/userAddAgentCtrl.js']
                    });
                }]
            }
        })
        .state('redemption.userUpdateAgent', {
            url: "/user/updateAgent/:merchantNo",
            templateUrl: "views/redem/userUpdateAgent.html",
            data: {pageTitle: '积分兑换修改二级代理商'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "userUpdateAgentCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redem/userUpdateAgentCtrl.js']
                    });
                }]
            }
        })
        .state('redemption.activityOrder', {
            url: "/activityOrder",
            templateUrl: "views/redem/activityOrder.html",
            data: {pageTitle: '授权订单查询'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "activityOrderCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redem/activityOrderCtrl.js']
                    });
                }]
            }
        })
        .state('redemption.declareOrder', {
            url: "/declareOrder",
            templateUrl: "views/redem/declareOrder.html",
            data: {pageTitle: '积分兑换订单查询'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "declareOrderCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redem/declareOrderCtrl.js']
                    });
                }]
            }
        })
        .state('redemption.share', {
            url: "/share",
            templateUrl: "views/redem/shareQuery.html",
            data: {pageTitle: '分润订单查询'},
            // 不需要配置controller,否则controller 会被执行两次
            // controller: "shareQueryCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/redem/shareQueryCtrl.js']
                    });
                }]
            }
        })
        //彩票查询
        .state('superBank.lotteryOrder', {
            url: '/lotteryOrder',
            templateUrl: "views/superBank/lotteryOrder.html",
            data: {pageTitle: '彩票订单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/lotteryOrderCtrl.js']
                    });
                }]
            }
        })
        .state('superBank.lotteryOrderDetail', {
            url: '/superBank/lotteryOrderDetail/:orderNo',
            templateUrl: "views/superBank/lotteryOrderDetail.html",
            data: {pageTitle: '彩票订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/lotteryOrderDetailCtrl.js']
                    });
                }]
            }
        })
        /*征信订单查询*/
        .state('superBank.inquiryOrder', {
            url: "/superBank/inquiryOrder",
            templateUrl: "views/superBank/creditOrderInquiry.html",
            data: {pageTitle: '征信订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/creditOrderInquiryCtrl.js']
                    });
                }]
            }
        })
        /*征信订单详情界面*/
        .state('superBank.inquiryOrderDetail', {
            url: "/superBank/inquiryOrderDetail/:orderNo",
            templateUrl: 'views/superBank/inquiryOrderDetail.html',
            data: {pageTitle: '征信订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/inquiryOrderDetailCtrl.js']
                    });
                }]
            }
        })
        /*积分兑换订单查询*/
        .state('superBank.superExchangeOrder', {
            url: "/superBank/superExchangeOrder",
            templateUrl: "views/superBank/superExchangeOrder.html",
            data: {pageTitle: '积分兑换订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/superExchangeOrderCtrl.js']
                    });
                }]
            }
        })
        /*积分兑换订单详情界面*/
        .state('superBank.superExchangeOrderDetail', {
            url: "/superBank/superExchangeOrderDetail/:orderNo",
            templateUrl: 'views/superBank/superExchangeOrderDetail.html',
            data: {pageTitle: '积分兑换订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/superExchangeOrderDetailCtrl.js']
                    });
                }]
            }
        })
        //违章代缴订单查询
        .state('superBank.carOrder', {
            url: '/superBank/findCarOrder',
            templateUrl: "views/superBank/carOrder.html",
            data: {pageTitle: '违章订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/map.js', 'js/controllers/superBank/carOrderCtrl.js']
                    });
                }]
            }
        })
        //违章代缴订单详情
        .state('superBank.carOrderDetail', {
            url: "/superBank/carOrderDetail/:orderNo",
            templateUrl: "views/superBank/carOrderDetail.html",
            data: {pageTitle: '违章代缴订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/carOrderDetailCtrl.js']
                    });
                }]
            }
        })

        /** 账户资金损失险 start **/
        .state('safe', {
            abstract: true,
            url: "/safe",
            templateUrl: "views/common/content.html",
        })
        .state('safe.safeOrder', {
            url: "/safeOrder",
            templateUrl: 'views/capitalInsurance/order/safeOrderQuery.html',
            data: {pageTitle: '保险订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/order/safeOrderQueryCtrl.js']
                    });
                }]
            }
        })
        .state('safe.safeShare', {
            url: "/safeShare",
            templateUrl: 'views/capitalInsurance/report/shareReportQuery.html',
            data: {pageTitle: '分润月报'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/capitalInsurance/report/shareReportQueryCtrl.js']
                    });
                }]
            }
        })
    /** 账户资金损失险 end **/
        //风控管理
        .state('riskControlManagement', {
            abstract: true,
            url: "/riskControlManagement",
            templateUrl: "views/common/content.html",
        })
        .state('riskControlManagement.handle', {
            url: "/riskHandle/blackDataQuery",
            templateUrl: 'views/riskControlManagement/blackHandleOrder.html',
            data: {pageTitle: '黑名单列表'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/riskControlManagement/blackHandleOrderCtr.js']
                    });
                }]
            }
        })
        .state('riskControlManagement.blackHandle', {
            url: "/riskHandle/handle/:orderNo",
            templateUrl: 'views/riskControlManagement/blackHandle.html',
            data: {pageTitle: '黑名单初次处理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ngfileupload');

                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/riskControlManagement/blackHandleCtr.js']
                    });
                }]
            }
        })
        .state('riskControlManagement.twiceBlackHandle', {
            url: "/riskHandle/twiceHandle/:orderNo",
            templateUrl: 'views/riskControlManagement/twiceBlackHandle.html',
            data: {pageTitle: '黑名单再次处理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ngfileupload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/riskControlManagement/twiceBlackHandleCtr.js']
                    });
                }]
            }
        })
        .state('riskControlManagement.blackHandleDetail', {
            url: "/riskHandle/handleDetail/:orderNo",
            templateUrl: 'views/riskControlManagement/blackHandleDetail.html',
            data: {pageTitle: '黑名单回复详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ngfileupload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/riskControlManagement/blackHandleDetailCtrl.js']

                    });
                }]
            }
        })
        .state('riskControlManagement.allBlackHandleDetail', {
            url: "/riskHandle/allBlackHandleDetail/:orderNo",
            templateUrl: 'views/riskControlManagement/allBlackHandleDetail.html',
            data: {pageTitle: '黑名单所有回复详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ngfileupload');
                    $ocLazyLoad.load('fancybox');

                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/riskControlManagement/allBlackHandleDetailCtrl.js']

                    });
                }]
            }
        })
        .state('riskControlManagement.surveyOrder', {
            url: "/surveyOrder",
            templateUrl: 'views/surveyOrder/surveyOrderQuery.html',
            data: {pageTitle: '调单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/surveyOrderQueryCtrl.js']
                    });
                }]
            }
        })
        /*调单回复*/
        .state('riskControlManagement.orderReply', {
            url: "/orderReply/:id/:orderNo",
            templateUrl: 'views/surveyOrder/orderReply.html',
            data: {pageTitle: '调单回复'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/orderReplyCtrl.js']
                    });
                }]
            }
        })
        /*调单回复修改*/
        .state('riskControlManagement.replyEdit', {
            url: "/replyEdit/:id/:orderNo/:type",
            templateUrl: 'views/surveyOrder/replyEdit.html',
            data: {pageTitle: '调单回复'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/replyEditCtrl.js']
                    });
                }]
            }
        })
        /*调单详情*/
        .state('riskControlManagement.surveyOrderInfo', {
            url: "/surveyOrderInfo/:id/:orderNo/:from",
            templateUrl: 'views/surveyOrder/surveyOrderInfo.html',
            data: {pageTitle: '调单回复'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/surveyOrder/surveyOrderInfoCtrl.js']
                    });
                }]
            }
        })
            /*/!*订单详情*!/*/
        .state('orderInfo', {
                url: "/orderInfo/:orderNo/:orderServiceCode",
                views: {
                    '': {
                        templateUrl: 'views/common/singleContent.html'
                    },
                    'mainView@orderInfo': {
                        templateUrl: 'views/surveyOrder/orderInfo.html'
                    }
                },
                data: {pageTitle: '交易订单详情'},
                resolve: {
                    loadPlugin: function ($ocLazyLoad) {
                        $ocLazyLoad.load('fileUpload');
                    },
                    deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: 'inspinia',
                            files: ['js/controllers/surveyOrder/orderInfoCtrl.js']
                        });
                    }]
                }
            })

}


angular.module('inspinia')
    .config(config)
    .run(function ($rootScope, $state) {
        $rootScope.$state = $state;
    });


(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('result-data', {
            parent: 'entity',
            url: '/result-data',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ResultData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/result-data/result-data.html',
                    controller: 'ResultDataController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('result-data-detail', {
            parent: 'entity',
            url: '/result-data/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ResultData'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/result-data/result-data-detail.html',
                    controller: 'ResultDataDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ResultData', function($stateParams, ResultData) {
                    return ResultData.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'result-data',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('result-data-detail.edit', {
            parent: 'result-data-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result-data/result-data-dialog.html',
                    controller: 'ResultDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResultData', function(ResultData) {
                            return ResultData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('result-data.new', {
            parent: 'result-data',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result-data/result-data-dialog.html',
                    controller: 'ResultDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                value: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('result-data', null, { reload: 'result-data' });
                }, function() {
                    $state.go('result-data');
                });
            }]
        })
        .state('result-data.edit', {
            parent: 'result-data',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result-data/result-data-dialog.html',
                    controller: 'ResultDataDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResultData', function(ResultData) {
                            return ResultData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('result-data', null, { reload: 'result-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('result-data.delete', {
            parent: 'result-data',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result-data/result-data-delete-dialog.html',
                    controller: 'ResultDataDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ResultData', function(ResultData) {
                            return ResultData.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('result-data', null, { reload: 'result-data' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dict', {
            parent: 'entity',
            url: '/dict',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Dicts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dict/dicts.html',
                    controller: 'DictController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('dict-detail', {
            parent: 'entity',
            url: '/dict/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Dict'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dict/dict-detail.html',
                    controller: 'DictDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Dict', function($stateParams, Dict) {
                    return Dict.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dict',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dict-detail.edit', {
            parent: 'dict-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict/dict-dialog.html',
                    controller: 'DictDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dict', function(Dict) {
                            return Dict.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dict.new', {
            parent: 'dict',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict/dict-dialog.html',
                    controller: 'DictDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                code: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dict', null, { reload: 'dict' });
                }, function() {
                    $state.go('dict');
                });
            }]
        })
        .state('dict.edit', {
            parent: 'dict',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict/dict-dialog.html',
                    controller: 'DictDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dict', function(Dict) {
                            return Dict.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dict', null, { reload: 'dict' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dict.delete', {
            parent: 'dict',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict/dict-delete-dialog.html',
                    controller: 'DictDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dict', function(Dict) {
                            return Dict.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dict', null, { reload: 'dict' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

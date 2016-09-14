(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dict-kind', {
            parent: 'entity',
            url: '/dict-kind',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DictKinds'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dict-kind/dict-kinds.html',
                    controller: 'DictKindController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('dict-kind-detail', {
            parent: 'entity',
            url: '/dict-kind/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DictKind'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dict-kind/dict-kind-detail.html',
                    controller: 'DictKindDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DictKind', function($stateParams, DictKind) {
                    return DictKind.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dict-kind',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dict-kind-detail.edit', {
            parent: 'dict-kind-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict-kind/dict-kind-dialog.html',
                    controller: 'DictKindDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DictKind', function(DictKind) {
                            return DictKind.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dict-kind.new', {
            parent: 'dict-kind',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict-kind/dict-kind-dialog.html',
                    controller: 'DictKindDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dict-kind', null, { reload: 'dict-kind' });
                }, function() {
                    $state.go('dict-kind');
                });
            }]
        })
        .state('dict-kind.edit', {
            parent: 'dict-kind',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict-kind/dict-kind-dialog.html',
                    controller: 'DictKindDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DictKind', function(DictKind) {
                            return DictKind.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dict-kind', null, { reload: 'dict-kind' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dict-kind.delete', {
            parent: 'dict-kind',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dict-kind/dict-kind-delete-dialog.html',
                    controller: 'DictKindDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DictKind', function(DictKind) {
                            return DictKind.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dict-kind', null, { reload: 'dict-kind' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

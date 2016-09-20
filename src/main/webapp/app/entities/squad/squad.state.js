(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('squad', {
            parent: 'entity',
            url: '/squad',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Squads'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/squad/squads.html',
                    controller: 'SquadController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('squad-detail', {
            parent: 'entity',
            url: '/squad/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Squad'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/squad/squad-detail.html',
                    controller: 'SquadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Squad', function($stateParams, Squad) {
                    return Squad.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'squad',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('squad-detail.edit', {
            parent: 'squad-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/squad/squad-dialog.html',
                    controller: 'SquadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Squad', function(Squad) {
                            return Squad.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('squad.new', {
            parent: 'squad',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/squad/squad-dialog.html',
                    controller: 'SquadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('squad', null, { reload: 'squad' });
                }, function() {
                    $state.go('squad');
                });
            }]
        })
        .state('squad.edit', {
            parent: 'squad',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/squad/squad-dialog.html',
                    controller: 'SquadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Squad', function(Squad) {
                            return Squad.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('squad', null, { reload: 'squad' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('squad.delete', {
            parent: 'squad',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/squad/squad-delete-dialog.html',
                    controller: 'SquadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Squad', function(Squad) {
                            return Squad.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('squad', null, { reload: 'squad' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('division-event', {
            parent: 'entity',
            url: '/division-event',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DivisionEvents'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division-event/division-events.html',
                    controller: 'DivisionEventController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('division-event-detail', {
            parent: 'entity',
            url: '/division-event/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DivisionEvent'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division-event/division-event-detail.html',
                    controller: 'DivisionEventDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DivisionEvent', function($stateParams, DivisionEvent) {
                    return DivisionEvent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'division-event',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('division-event-detail.edit', {
            parent: 'division-event-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division-event/division-event-dialog.html',
                    controller: 'DivisionEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DivisionEvent', function(DivisionEvent) {
                            return DivisionEvent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('division-event.new', {
            parent: 'division-event',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division-event/division-event-dialog.html',
                    controller: 'DivisionEventDialogController',
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
                    $state.go('division-event', null, { reload: 'division-event' });
                }, function() {
                    $state.go('division-event');
                });
            }]
        })
        .state('division-event.edit', {
            parent: 'division-event',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division-event/division-event-dialog.html',
                    controller: 'DivisionEventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DivisionEvent', function(DivisionEvent) {
                            return DivisionEvent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('division-event', null, { reload: 'division-event' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('division-event.delete', {
            parent: 'division-event',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division-event/division-event-delete-dialog.html',
                    controller: 'DivisionEventDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DivisionEvent', function(DivisionEvent) {
                            return DivisionEvent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('division-event', null, { reload: 'division-event' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('division-event.games', {
            parent: 'entity',
            url: '/division-event/{id}/games',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/division-event/division-event-games.html',
                    controller: 'DivisionEventGameController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('association.dash', {
            parent: 'association',
            url: '/{id}/dashboard',
            data: {
                authorities: ['ROLE_USER'],
            },
           views: {
                'content@': {
                    templateUrl: 'app/dashboard/association/association-index.html',
                    controller: 'AssociationDashboardController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

            }
        }).state('association.division', {
            parent: 'association',
            url: '/{id}/division',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/association/divisions.html',
                    controller: 'AssociationDivisionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        }).state('association-division-new', {
            parent: 'association.division',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/dashboard/association/division-dialog.html',
                    controller: 'AssociationDivisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                createAt: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('association.division', null, { reload: 'association.division' });
                }, function() {
                    $state.go('association.division');
                });
            }]
        }).state('association-division-detail', {
            parent: 'association.division',
            url: '/{did}',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/association/division-detail.html',
                    controller: 'DivisionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Division', function($stateParams, Division) {
                    return Division.get({id : $stateParams.did}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'division',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('association-division-detail.edit', {
            parent: 'association.division',
            url: '/{did}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/dashboard/association/division-dialog.html',
                    controller: 'DivisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Division', function(Division) {
                            return Division.get({id : $stateParams.did}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('association-division-delete', {
            parent: 'association.division',
            url: '/{did}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division/division-delete-dialog.html',
                    controller: 'DivisionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Division', function(Division) {
                            return Division.get({id : $stateParams.did}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('association.division', null, { reload: 'association.division' });
                }, function() {
                    $state.go('^');
                });
            }]
            
        })
        .state('association.division-event', {
            parent: 'association',
            url: '/{id}/division-events',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/association/division-events.html',
                    controller: 'AssociationDivisionEventController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        }).state('association-division-event-new', {
            parent: 'association.division-event',
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
                    $state.go('association.division-event', null, { reload: 'association.division-event' });
                }, function() {
                    $state.go('association.division-event');
                });
            }]
        })
        .state('association-division-event-detail', {
            parent: 'association.division-event',
            url: '/{did}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DivisionEvent'
            },
            views: {
                'content@': {
                    templateUrl: 'app/dashboard/association/division-event-detail.html',
                    controller: 'DivisionEventDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DivisionEvent', function($stateParams, DivisionEvent) {
                    return DivisionEvent.get({id : $stateParams.did}).$promise;
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
        .state('association-division-event-detail.edit', {
            parent: 'association.division-event',
            url: '/{did}/edit',
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
                            return DivisionEvent.get({id : $stateParams.did}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('association-division-event-delete', {
            parent: 'association.division-event',
            url: '/{did}/delete',
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
                            return DivisionEvent.get({id : $stateParams.did}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('association.division-event', null, { reload: 'association.division-event' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('association.dashedit', {
            parent: 'association.dash',
            url: '/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/association/association-dialog.html',
                    controller: 'AssociationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Association', function(Association) {
                            return Association.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('association.dash', null, { reload: 'association.dash' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('association.dash-club-regist', {
            parent: 'association.dash',
            url: '/club-signin',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/dashboard/association/club-signin-dialog.html',
                    controller: 'ClubSigninDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Association', function(Association) {
                            return Association.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('association.dash', null, { reload: 'association.dash' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

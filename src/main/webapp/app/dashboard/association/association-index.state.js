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
            url: '/{id}',
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
                    return Division.get({id : $stateParams.id}).$promise;
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
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/division/division-dialog.html',
                    controller: 'DivisionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Division', function(Division) {
                            return Division.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
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
                    templateUrl: 'app/entities/division-event/division-events.html',
                    controller: 'DivisionEventController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        }).state('association.dashedit', {
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

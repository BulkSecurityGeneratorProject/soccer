(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('timeslot', {
            parent: 'entity',
            url: '/timeslot',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Timeslots'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/timeslot/timeslots.html',
                    controller: 'TimeslotController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('timeslot-detail', {
            parent: 'entity',
            url: '/timeslot/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Timeslot'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/timeslot/timeslot-detail.html',
                    controller: 'TimeslotDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Timeslot', function($stateParams, Timeslot) {
                    return Timeslot.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'timeslot',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('timeslot-detail.edit', {
            parent: 'timeslot-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/timeslot/timeslot-dialog.html',
                    controller: 'TimeslotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Timeslot', function(Timeslot) {
                            return Timeslot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('timeslot.new', {
            parent: 'timeslot',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/timeslot/timeslot-dialog.html',
                    controller: 'TimeslotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                round: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('timeslot', null, { reload: 'timeslot' });
                }, function() {
                    $state.go('timeslot');
                });
            }]
        })
        .state('timeslot.edit', {
            parent: 'timeslot',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/timeslot/timeslot-dialog.html',
                    controller: 'TimeslotDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Timeslot', function(Timeslot) {
                            return Timeslot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('timeslot', null, { reload: 'timeslot' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('timeslot.delete', {
            parent: 'timeslot',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/timeslot/timeslot-delete-dialog.html',
                    controller: 'TimeslotDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Timeslot', function(Timeslot) {
                            return Timeslot.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('timeslot', null, { reload: 'timeslot' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

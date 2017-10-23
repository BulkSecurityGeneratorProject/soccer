(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('venue', {
            parent: 'entity',
            url: '/venue',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Venues'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/venue/venues.html',
                    controller: 'VenueController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('venue-detail', {
            parent: 'entity',
            url: '/venue/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Venue'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/venue/venue-detail.html',
                    controller: 'VenueDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Venue', function($stateParams, Venue) {
                    return Venue.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'venue',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('venue-detail.edit', {
            parent: 'venue-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/venue/venue-dialog.html',
                    controller: 'VenueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Venue', function(Venue) {
                            return Venue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('venue.new', {
            parent: 'venue',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/venue/venue-dialog.html',
                    controller: 'VenueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                shortName: null,
                                createAt: null,
                                address: null,
                                city: null,
                                province: null,
                                town: null,
                                country: null,
                                zip: null,
                                telephone: null,
                                latlng: null,
                                picture: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('venue', null, { reload: 'venue' });
                }, function() {
                    $state.go('venue');
                });
            }]
        })
        .state('venue.edit', {
            parent: 'venue',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/venue/venue-dialog.html',
                    controller: 'VenueDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Venue', function(Venue) {
                            return Venue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('venue', null, { reload: 'venue' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('venue.delete', {
            parent: 'venue',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/venue/venue-delete-dialog.html',
                    controller: 'VenueDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Venue', function(Venue) {
                            return Venue.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('venue', null, { reload: 'venue' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

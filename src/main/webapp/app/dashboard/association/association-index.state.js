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

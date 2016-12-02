(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('club.dash', {
            parent: 'club',
            url: '/{id}/dashboard',
            data: {
                authorities: ['ROLE_USER'],
            },
           views: {
                'content@': {
                    templateUrl: 'app/dashboard/club/club-index.html',
                    controller: 'ClubDashboardController',
                    controllerAs: 'vm'
                }
            },
            resolve: {

            }
        }).state('club.dashedit', {
            parent: 'club.dash',
            url: '/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/club/club-dialog.html',
                    controller: 'ClubDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Club', function(Club) {
                            return Club.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('club.dash', null, { reload: 'club.dash' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

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
        });
    }

})();

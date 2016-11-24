(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.association', {
            parent: 'public',
            url: '/public/association/{id}',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/association/association.html',
                    controller: 'PublicAssociationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        });
    }

})();

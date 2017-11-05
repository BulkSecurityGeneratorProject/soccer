(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('public.table', {
            parent: 'public',
            url: '/public/tables',
            data: {
                authorities: ['ROLE_USER'],
            },
            views: {
                'content@': {
                    templateUrl: 'app/public/table/table.html',
                    controller: 'PublicTableController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                
            }
        });
    }

})();

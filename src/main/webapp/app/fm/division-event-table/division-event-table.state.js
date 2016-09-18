(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('hello', {
            parent: 'entity',
            url: '/table/1',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DivisionEventTable'
            },
            views: {
                'content@': {
                    templateUrl: 'app/fm/division-event-table/division-event-tables.html',
                    controller: 'DivisionEventTableController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        });
    }

})();

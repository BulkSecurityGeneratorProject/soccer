(function() {
    'use strict';

    angular
        .module('soccerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('division-event.table', {
            parent: 'division-event',
            url: '/{id}/table',
            data: {
                authorities: ['ROLE_USER'],
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

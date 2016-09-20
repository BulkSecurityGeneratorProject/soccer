'use strict';

describe('Controller Tests', function() {

    describe('Squad Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSquad, MockGame, MockTeam;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSquad = jasmine.createSpy('MockSquad');
            MockGame = jasmine.createSpy('MockGame');
            MockTeam = jasmine.createSpy('MockTeam');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Squad': MockSquad,
                'Game': MockGame,
                'Team': MockTeam
            };
            createController = function() {
                $injector.get('$controller')("SquadDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:squadUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

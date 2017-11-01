'use strict';

describe('Controller Tests', function() {

    describe('Lineup Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLineup, MockPlayer, MockTeam, MockDivisionEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLineup = jasmine.createSpy('MockLineup');
            MockPlayer = jasmine.createSpy('MockPlayer');
            MockTeam = jasmine.createSpy('MockTeam');
            MockDivisionEvent = jasmine.createSpy('MockDivisionEvent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Lineup': MockLineup,
                'Player': MockPlayer,
                'Team': MockTeam,
                'DivisionEvent': MockDivisionEvent
            };
            createController = function() {
                $injector.get('$controller')("LineupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:lineupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

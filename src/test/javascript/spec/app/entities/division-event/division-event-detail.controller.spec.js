'use strict';

describe('Controller Tests', function() {

    describe('DivisionEvent Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDivisionEvent, MockSeason, MockDivision, MockTeam;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDivisionEvent = jasmine.createSpy('MockDivisionEvent');
            MockSeason = jasmine.createSpy('MockSeason');
            MockDivision = jasmine.createSpy('MockDivision');
            MockTeam = jasmine.createSpy('MockTeam');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DivisionEvent': MockDivisionEvent,
                'Season': MockSeason,
                'Division': MockDivision,
                'Team': MockTeam
            };
            createController = function() {
                $injector.get('$controller')("DivisionEventDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:divisionEventUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

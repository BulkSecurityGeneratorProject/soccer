'use strict';

describe('Controller Tests', function() {

    describe('Timeslot Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTimeslot, MockDict, MockDivisionEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTimeslot = jasmine.createSpy('MockTimeslot');
            MockDict = jasmine.createSpy('MockDict');
            MockDivisionEvent = jasmine.createSpy('MockDivisionEvent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Timeslot': MockTimeslot,
                'Dict': MockDict,
                'DivisionEvent': MockDivisionEvent
            };
            createController = function() {
                $injector.get('$controller')("TimeslotDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:timeslotUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

'use strict';

describe('Controller Tests', function() {

    describe('Referee Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockReferee, MockAssociation, MockDict;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockReferee = jasmine.createSpy('MockReferee');
            MockAssociation = jasmine.createSpy('MockAssociation');
            MockDict = jasmine.createSpy('MockDict');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Referee': MockReferee,
                'Association': MockAssociation,
                'Dict': MockDict
            };
            createController = function() {
                $injector.get('$controller')("RefereeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:refereeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

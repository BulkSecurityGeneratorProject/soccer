'use strict';

describe('Controller Tests', function() {

    describe('Division Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDivision, MockDict, MockAssociation, MockRankingRule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDivision = jasmine.createSpy('MockDivision');
            MockDict = jasmine.createSpy('MockDict');
            MockAssociation = jasmine.createSpy('MockAssociation');
            MockRankingRule = jasmine.createSpy('MockRankingRule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Division': MockDivision,
                'Dict': MockDict,
                'Association': MockAssociation,
                'RankingRule': MockRankingRule
            };
            createController = function() {
                $injector.get('$controller')("DivisionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:divisionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});

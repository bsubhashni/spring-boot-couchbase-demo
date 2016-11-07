var demoApp = angular.module('demoApp', ['mainModule']);

angular.module("mainModule", ["ui.router", "uiGmapgoogle-maps"])
    .config(function($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state("search", {
                "url": "/search",
                "templateUrl": "templates/search.html",
                "controller": "mainController",
                "cache": false
            })
            .state("results", {
                "templateUrl": "templates/results.html",
                "controller": "resultsController",
                "params": {
                    searchResultsByZip: null
                },
                "cache": false
            })
            .state("maps", {
                "templateUrl" : "templates/maps.html",
                "controller" : "mapsController",
                "cache": false
            });
        $urlRouterProvider.otherwise("search");
    })

.controller("mainController", function($scope, $http, $state, $stateParams) {
    $scope.searchByZip = function (zip) {
        $http(
            {
                method: "GET",
                url: "/api/searchByZip",
                params: {
                    zip: zip
                }
            }
        ).success(function (response) {
            $state.go('results', {'searchResultsByZip' : response});

        }).error(function(error) {
                console.log(JSON.stringify(error));
        });

    };

    $scope.searchByMaps = function () {
        $state.go('maps');
    };
}).controller("resultsController", function($scope, $http, $state, $stateParams) {
    $scope.searchResultsByZip =  $stateParams["searchResultsByZip"] || {};
}).controller("mapsController", function ($scope, $http, uiGmapGoogleMapApi) {
    uiGmapGoogleMapApi.then(function(maps) {
        $scope.lastBounds = {};
        $scope.map = {
            center: {
                latitude: 7.0933,
                longitude: 79.9989
            },
            draggable: true,
            zoom: 15,
            events: {
                idle: function(map) {
                    var bounds = {};
                    bounds["topLeftLat"] = map.getBounds().getNorthEast().lat();
                    bounds["topLeftLong"] = map.getBounds().getNorthEast().lng();
                    bounds["bottomRightLat"] = map.getBounds().getSouthWest().lat();
                    bounds["bottomRightLong"] = map.getBounds().getSouthWest().lng();
                    $scope.lastBounds = bounds;
                    $http(
                        {
                            method: "GET",
                            url: "/api/searchByLocation",
                            params: {
                                boundsArr: $scope.lastBounds
                            }
                        }
                    ).success(function (response) {

                    }).error(function(error) {
                    });

                }
            }
        };

        // map options
        $scope.options = {
            scrollwheel: false,
            panControl: true,
            rotateControl: true,
            scaleControl: true,
            streetViewControl: true
        };
    });

    // map marker
    $scope.marker = {
        id: 0,
        coords: {
            latitude:  7.0933,
            longitude: 79.9989
        },
        options: {
            draggable: false,
            title: 'The KVK Blog',
            animation: 1 // 1: BOUNCE, 2: DROP
        }
    };
});


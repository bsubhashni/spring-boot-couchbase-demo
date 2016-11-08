var demoApp = angular.module('demoApp', ['mainModule']);

angular.module("mainModule", ["ui.router", "uiGmapgoogle-maps", "nemLogging"])
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
            if(response.length == 0) {
                alert("No results found");
            } else {
                $state.go('results', {'searchResultsByZip': response});
            }
        }).error(function(error) {
            alert("No results found");
            console.log(JSON.stringify(error));
        });

    };
    $scope.searchByMaps = function () {
        $state.go('maps');
    };
}).controller("resultsController", function($scope, $http, $state, $stateParams) {
    $scope.searchResultsByZip =  $stateParams["searchResultsByZip"] || {};
}).controller("mapsController", function ($scope, $http, uiGmapGoogleMapApi, uiGmapIsReady) {
    $scope.markers = [];

    uiGmapIsReady.promise()
        .then(function (maps) {
            $scope.markers = [];
        });

    uiGmapGoogleMapApi.then(function(maps) {
        $scope.lastBounds = {};

        $scope.windowOptions = {
            show: false
        };

        $scope.onMarkerClick = function (marker, eventName, model) {
            $scope.windowOptions.show = !$scope.windowOptions.show;
            $scope.selectedCoords = model.coords;
            $scope.info = model.info + '[score:' + model.score + ']';
        };

        $scope.closeClick = function () {
            $scope.windowOptions.show = false;
        };


        $scope.map = {
            center: {
                latitude: 37.773972,
                longitude: -122.431297
            },
            draggable: true,
            zoom: 15,
            events: {
                idle: function(map) {
                    $scope.markers = [];
                    $http(
                        {
                            method: "GET",
                            url: "/api/searchByArea",
                            params: {
                                "y2": map.getBounds().getNorthEast().lat(),
                                "x2": map.getBounds().getNorthEast().lng(),
                                "y1": map.getBounds().getSouthWest().lat(),
                                "x1": map.getBounds().getSouthWest().lng(),
                            }
                        }
                    ).success(function (response) {
                        response.forEach(function (restaurant) {
                                $scope.markers.push({
                                    id: restaurant.business_id,
                                    coords: {
                                        latitude: restaurant.latitude,
                                        longitude: restaurant.longitude
                                    },
                                    info: restaurant.name,
                                    score: restaurant.inspectionScore
                                });
                                }
                           );
                    }).error(function(error) {
                        alert("No results found");
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

});


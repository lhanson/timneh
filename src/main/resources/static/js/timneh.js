angular.module('timneh', [ 'ngRoute', 'ngStorage', 'angular-jwt' ])

	.config(function($routeProvider, $httpProvider) {
		$routeProvider.when('/', {
			templateUrl : 'home.html',
			controller : 'home',
			controllerAs: 'controller'
		}).when('/login', {
			templateUrl : 'loginForm.html',
			controller : 'navigation',
			controllerAs: 'controller'
		}).otherwise('/');
		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

		$httpProvider.interceptors.push(['$q', '$location', '$localStorage', 'jwtHelper',
			function ($q, $location, $localStorage, jwtHelper) {
				return {
					'request': function (config) {
						config.headers = config.headers || {};
						if ($localStorage.token) {
							if (jwtHelper.isTokenExpired($localStorage.token)) {
								delete $localStorage.token;
							} else {
								config.headers.Authorization = 'Bearer ' + $localStorage.token;
							}
						}
						return config;
					},
					'responseError': function (response) {
						console.log("Got response error", response);
						return $q.reject(response);
					}
				};
		}]);
	})

	.controller('home', ['$http', '$localStorage', '$scope', function($http, $localStorage, $scope) {
		$scope.authenticated = $localStorage.token;
		if ($scope.authenticated) {
			$http.get('/user').then(function(response) {
				console.log('user data:', $scope.user);
				$scope.user = response.data
			});
		}
	}])

	.controller('navigation', function($rootScope, $http, $location, $localStorage, $scope, $route) {
		var self = this;
		var authenticate = function(credentials, callback) {
			var auth = window.btoa(credentials.username + ':' + credentials.password);
			$http({
				method: 'POST',
				url: '/login',
				headers: {"Authorization": "Basic " + auth}
			}).then(function(response) {
					$localStorage.token = response.data;
					callback && callback();
				}, function() {
					callback && callback();
				});
			};

		self.credentials = {};
		self.login = function() {
			authenticate(self.credentials, function() {
				if ($localStorage.token) {
					$location.path("/");
					self.error = false;
				} else {
					$location.path("/login");
					self.error = true;
				}
			});
		};
		self.profile = function() {
			$location.path("/profile");
		};
		self.logout = function() {
			delete $localStorage.token;
			$location.path("/");
			$scope.authenticated = false;
			$route.reload();
		};
		$scope.authenticated = $localStorage.token;
	});

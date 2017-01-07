angular
	.module('timneh', [ 'ngRoute', 'ngStorage', 'angular-jwt' ])

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
								console.log('Deleting expired token');
								delete $localStorage.token;
							} else {
								console.log('Sending token');
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
		$scope.authenticated = Boolean($localStorage.token);
		console.log('Home controller, authenticated', $scope.authenticated);
		if ($scope.authenticated) {
			$http.get('/user').then(function(response) {
				$scope.user = response.data
				console.log('user data:', $scope.user);
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
		$scope.authenticated = Boolean($localStorage.token);
		console.log("Initialized navigation controller, authenticated", $scope.authenticated);
	});

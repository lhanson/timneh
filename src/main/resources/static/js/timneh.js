angular.module('timneh', [ 'ngRoute', 'ngStorage' ])

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

		$httpProvider.interceptors.push(['$q', '$location', '$localStorage', function ($q, $location, $localStorage) {
			return {
				'request': function (config) {
					config.headers = config.headers || {};
					if ($localStorage.token) {
						config.headers.Authorization = 'Bearer ' + $localStorage.token;
					}
					return config;
				},
				'responseError': function (response) {
					if (response.status === 401 || response.status === 403) {
						$location.path('/signin');
					}
					return $q.reject(response);
				}
			};
		}]);
	})

	.controller('home', ['$http', '$localStorage', '$scope', function($http, $localStorage, $scope) {
		$scope.authenticated = $localStorage.token;
		var self = this;
		if ($localStorage.token) {
			$http.get('/now').then(function(response) {
				self.localDateTime = response.data;
			});
		}
	}])

	.controller('navigation', function($rootScope, $http, $location, $localStorage, $scope, $route) {
		var self = this;
		var authenticate = function(credentials, callback) {
			 $http.post('login', credentials)
				.then(function(response) {
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
		self.logout = function() {
			delete $localStorage.token;
			$location.path("/");
			$scope.authenticated = false;
			$route.reload();
		}
		$scope.authenticated = $localStorage.token;
	});

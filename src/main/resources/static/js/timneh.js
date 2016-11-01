angular.module('timneh', [ 'ngRoute'/*, 'ngStorage'*/ ])

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
	})

	.factory('userService', function() {
		return { }
	})

	.controller('home', function($http, userService) {
		var self = this;
		if (userService.token) {
			$http.get('/now').then(function(response) {
				self.localDateTime = response.data;
			});
			self.username = userService.username;
		}
	})

	.controller('navigation', function($rootScope, $http, $location, userService/*, $localStorage*/) {
		var self = this;
		var authenticate = function(credentials, callback) {
			 $http.post('login', credentials)
				.then(function(response) {
					userService.token = response.data;
					$rootScope.authenticated = (userService.token != undefined);
					$http.defaults.headers.common.Authorization = 'Bearer ' + userService.token;
					callback && callback();
				}, function() {
					$rootScope.authenticated = false;
					callback && callback();
				});
			};

		self.credentials = {};
		self.login = function() {
			authenticate(self.credentials, function() {
				if ($rootScope.authenticated) {
					$location.path("/");
					self.error = false;
				} else {
					$location.path("/login");
					self.error = true;
				}
			});
		};
		self.logout = function() {
			$rootScope.authenticated = false;
			userService.token = null;
			$location.path("/");
		}
	});

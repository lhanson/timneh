angular.module('timneh', [ 'ngRoute' ])

	.config(function($routeProvider, $httpProvider) {
		console.log('Configuring AngularJS app');
		$routeProvider.when('/', {
			templateUrl : 'home.html',
			controller : 'home',
			controllerAs: 'controller'
		}).when('/login', {
			templateUrl : 'login.html',
			controller : 'navigation',
			controllerAs: 'controller'
		}).otherwise('/');

		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	})

	.factory('userService', function() {
		console.log('Creating userService');
		var userServiceInstance = { };
		return userServiceInstance;
	})

	.controller('home', function($http, userService) {
		var self = this;
		console.log('Creating home controller');
		if (userService.authenticated) {
			$http.get('/now').then(function(response) {
				self.localDateTime = response.data;
			})
			self.username = userService.username
		}
	})

	.controller('navigation', function($rootScope, $http, $location, userService) {
		var self = this;
		console.log('Creating navigation controller');
		var authenticate = function(credentials, callback) {
			console.log("Authenticating");
			var headers = credentials
				? {authorization : "Basic " + btoa(credentials.username + ":" + credentials.password) }
				: {};

			$http.get('user', {headers : headers}).then(function(response) {
				console.log('GET /user, response:', response);
				if (response.data.name) {
					userService.username = response.data.name;
					userService.authenticated = true;
					$rootScope.authenticated = true;
				} else {
					$rootScope.authenticated = false;
				}
				callback && callback();
			}, function() {
				$rootScope.authenticated = false;
				callback && callback();
			});
		}

		authenticate();

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
			$http.post('logout', {}).finally(function() {
				$rootScope.authenticated = false;
				$location.path("/");
			});
		}
	});

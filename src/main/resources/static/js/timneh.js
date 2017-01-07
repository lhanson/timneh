angular
	.module('timneh', [ 'ngRoute', 'angular-jwt' ])
	.constant('timneh_config', {
		'token_name': 'jwt_token'
	})
	.run(function(authManager) {
		console.log('Checking auth on refresh');
		authManager.checkAuthOnRefresh();
	})
	.config(function($routeProvider, $httpProvider, jwtOptionsProvider, timneh_config) {
		$routeProvider.when('/', {
			templateUrl : 'home.html',
			controller : 'home',
			controllerAs: 'controller'
		}).when('/login', {
			templateUrl : 'loginForm.html',
			controller : 'navigation',
			controllerAs: 'controller'
		}).otherwise('/');

		jwtOptionsProvider.config({
			tokenGetter: ['options', function(options) {
				// Send the token if we're not merely requesting templates
				if (!options || options.url.substr(options.url.length - 5) != '.html') {
					console.log('Loading token');
					return localStorage.getItem(timneh_config.token_name);
				}
			}]
		});

		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

		$httpProvider.interceptors.push('jwtInterceptor');
	})

	.controller('home', ['$http', '$scope', 'timneh_config', function($http, $scope) {
		console.log('Home controller, authenticated', $scope.isAuthenticated);
		if ($scope.isAuthenticated) {
			$http.get('/user').then(function(response) {
				$scope.user = response.data
				console.log('user data:', $scope.user);
			});
		}
	}])

	.controller('navigation', function($rootScope, $http, $location, $scope, $route, authManager, timneh_config) {
		var self = this;
		var authenticate = function(credentials, callback) {
			var auth = window.btoa(credentials.username + ':' + credentials.password);
			$http({
				method: 'POST',
				url: '/login',
				headers: {"Authorization": "Basic " + auth}
			}).then(function(response) {
					localStorage.setItem(timneh_config.token_name, response.data);
					callback && callback();
				}, function() {
					callback && callback();
				});
			};

		self.credentials = {};
		self.login = function() {
			authenticate(self.credentials, function() {
				if (localStorage.getItem(timneh_config.token_name)) {
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
			console.log("Logging out", authManager);
			localStorage.removeItem(timneh_config.token_name);
			authManager.unauthenticate();
			$location.path("/");
			$route.reload();
		};
		console.log("Initialized navigation controller, authenticated", $scope.isAuthenticated);
	});

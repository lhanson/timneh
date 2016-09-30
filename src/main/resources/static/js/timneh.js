angular.module('timneh', [])
	.controller('home', function($http) {
		var self = this;
		$http.get('/now').then(function(response) {
			self.localDateTime = response.data;
		})
		self.greeting = {id: 'xxx', content: 'Hello World!'}
	})

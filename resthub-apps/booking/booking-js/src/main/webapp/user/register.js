define([ 'resthub.controller', 'repositories/user.repository', 'jquery.json' ], function(Controller, UserRepository) {
		$.widget("booking.userRegister", $.ui.controller, {
			options : {
				template : 'user/register.html'
			},
			_init : function() {

				document.title = 'Register';
				this._render();

				var self = this;
				$('#register-button').bind('click', function() {
					var user = {
						email : $('input[name=email]').val(),
						username : $('input[name=username]').val(),
						password : $('input[name=password]').val(),
						fullname : $('input[name=name]').val()
					};
					UserRepository.save($.proxy(self, '_userRegistered'), $.toJSON(user));
				});
			},
			_userRegistered : function(user) {
				$.publish('user-registered', user);
			}
		});
});

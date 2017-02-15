scout.objectFactories = $.extend(scout.objectFactories, {
	'LoginBox' : function() {
		return new tasks.SpringSecurityLoginBox();
	}
});

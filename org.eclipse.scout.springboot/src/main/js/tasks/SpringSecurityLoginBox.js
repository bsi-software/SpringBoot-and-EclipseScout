tasks.SpringSecurityLoginBox = function(opts) {
	tasks.SpringSecurityLoginBox.parent.call(this, opts);
};
scout.inherits(tasks.SpringSecurityLoginBox, scout.LoginBox);

tasks.SpringSecurityLoginBox.prototype.render = function($parent) {
	tasks.SpringSecurityLoginBox.parent.prototype.render.call(this, $parent);
	this.$user.attr('name', 'username');
	this.$password.attr('name', 'password');

	this.$user.focus();
};

tasks.SpringSecurityLoginBox.prototype.data = function() {
	return this.$form.serialize();
}

tasks.SpringSecurityLoginBox.prototype._onPostDone = function(data) {
	if (data.indexOf("LoginFailed") !== -1) {
		this._onPostFailImpl(null, null, null);
	} else {
		this.redirect(data);
	}
};
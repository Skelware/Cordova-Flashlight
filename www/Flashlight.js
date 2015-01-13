cordova.addConstructor(function Flashlight() {
	var _enabled = false;
	var _ready = false;

	this.isSupported = function(callback) {
		cordova.exec(function(available) {
			success(!!available);
		}, function() {
			success(false);
		}, 'Flashlight', 'isSupported', []);
	}

	this.isReady = function() {
		return _ready;
	};

	this.isEnabled = function() {
		return _enabled;
	};

	this.setEnabled = function(enabled, callback) {
		if (_enabled == enabled) {
			return this;
		}
		_enabled = enabled;
		cordova.exec(callback, callback, 'Flashlight', enabled ? 'enable'
				: 'disable', []);
		return this;
	};

	this.toggle = function(callback) {
		return this.setEnabled(!_enabled, callback);
	};

	this.request = function(callback) {
		cordova.exec(function() {
			_ready = true;
			callback.apply(window.plugins.flashlight, arguments);
		}, function() {
			_ready = false;
			callback.apply(window.plugins.flashlight, arguments);
		}, 'Flashlight', 'request', []);
	};

	this.release = function(callack) {
		cordova.exec(function() {
			_ready = false;
			callback.apply(window.plugins.flashlight, arguments);
		}, callback, 'Flashlight', 'release', []);
	};

	this.enable = this.setEnabled.bind(this, true);
	this.disable = this.setEnabled.bind(this, false);

	this.request();

	if (!window.plugins) {
		window.plugins = {};
	}

	window.plugins.flashlight = this;
	return this;
});

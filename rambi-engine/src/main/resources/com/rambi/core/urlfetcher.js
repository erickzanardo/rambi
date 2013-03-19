importClass(com.google.appengine.api.urlfetch.URLFetchServiceFactory);

var utils = importModule('com/rambi/core/utils.js', 'utils');

function urlfetcher() {
	return new function() {
		this.get = function(url) {
			var urlFetchService = URLFetchServiceFactory.getURLFetchService();
			var resp = urlFetchService.fetch(new java.net.URL(utils.jsonToJavaType(url)));
			if (resp != null) {
				var str = new java.lang.String(resp.getContent());
				return utils.javaToJsonType(str);
			}
			return null;
		}
	};
}
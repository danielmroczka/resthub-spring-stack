define([ 'resthub.repository' ], function(Repository) {

	return Repository.extend("BookingRepository", {
		
		root : 'api/booking/'

	}, {});
});

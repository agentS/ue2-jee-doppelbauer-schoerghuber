import React from "react";

class ConnectionSearch extends React.Component {
	render() {
		return (
			<form className="needs-validation">
				<div className="mb-3">
					<label htmlFor="txtDepartureStation">Departure station</label>
					<input id="txtDepartureStation" type="text" className="form-control"/>
				</div>
				<div className="mb-3">
					<label htmlFor="txtArrivalStation">Arrival station</label>
					<input id="txtArrivalStation" type="text" className="form-control"/>
				</div>
				<hr className="mb-4"/>
				<button className="btn btn-primary btn-lg btn-block" type="submit">Search for connections</button>
			</form>
		);
	}
};

export default ConnectionSearch;

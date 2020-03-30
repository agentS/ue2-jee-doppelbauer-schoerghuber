import React from "react";
import { withRouter, RouteComponentProps, Link } from "react-router-dom";

import { Card } from "react-bootstrap";

import { TimetableApi, RailwayStationDestinationsDto } from "../api/timetable";

interface DiscoveryProperties extends RouteComponentProps {
	timetableApi: TimetableApi;
	departureStationId: number;
}

interface DiscoveryState {
	destinations?: RailwayStationDestinationsDto;
}

class Discovery extends React.Component<DiscoveryProperties, DiscoveryState> {
	constructor(properties: DiscoveryProperties) {
		super(properties);
		this.state = {
			destinations: undefined
		};
	}

	async componentDidMount() {
		await this.fetchDestinations(this.props.departureStationId);
	}

	async componentDidUpdate(previousProperties: DiscoveryProperties) {
		if (this.props !== previousProperties) {
			await this.fetchDestinations(this.props.departureStationId);
		}
	}

	async fetchDestinations(destinationStationId: number) {
		const destinations =
			await this.props.timetableApi.destinationsFromIdGet({id: destinationStationId});
		this.setState({ destinations: destinations });
	}

	render() {
		if (this.state.destinations !== undefined && this.state.destinations !== null) {
			return (
				<div className="mt-4">
					<h3>Origin station: {this.state.destinations.origin?.name}</h3>
					<hr className="mb-4"/>
					{this.state.destinations.destinations?.map(destination => (
						<Card key={destination.id} body className="mb-2">
							<h5>{destination.name}</h5>
							<Link to={`/connection/from/${this.state.destinations?.origin?.id}/to/${destination.id}`}>Find a connection</Link>
						</Card>
					))}
				</div>
			);
		} else {
			return (<h3>Loading destinations</h3>);
		}
	}
}

export default withRouter(Discovery);

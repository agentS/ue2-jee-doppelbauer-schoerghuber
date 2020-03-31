import React from "react";
import { Form, FormGroup, Button } from "react-bootstrap";
import { withRouter, RouteComponentProps } from "react-router-dom";

import { TimetableApi, RailwayStationDto } from "../api/timetable";

import RailwayStationSuggestionList from "./RailwayStationSuggestionList";

interface DiscoverySearchProperties extends RouteComponentProps {
	timetableApi: TimetableApi
}

interface DiscoverySearchState {
	departureStationName: string,
	deparureStationSuggestions: Array<RailwayStationDto>,
	departureStation?: RailwayStationDto
}

class DiscoverySearch extends React.Component<DiscoverySearchProperties, DiscoverySearchState> {
	constructor(properties: DiscoverySearchProperties) {
		super(properties);
		this.state = {
			departureStationName: "",
			deparureStationSuggestions: [],
			departureStation: undefined
		};
	}

	async updateDepartureRailwayStationNameSuggestions(event: React.ChangeEvent<HTMLInputElement>) {
		const suggestions = await this.autocompleteRailwayStationName(event);
		this.setState({ deparureStationSuggestions: suggestions });
	}

	async autocompleteRailwayStationName(event: React.ChangeEvent<HTMLInputElement>) {
		const searchTerm = event.target.value;
		if (searchTerm !== "") {
			return await this.props.timetableApi.railwayStationSearchSearchTermGet({searchTerm: searchTerm});
		} else {
			return [];
		}
	}

	switchToDiscoveryView() {
		if (this.state.departureStation !== undefined) {
			this.props.history.push("/discovery/" + this.state.departureStation.id);
		}
	}

	render() {
		return (
			<Form>
				<FormGroup controlId="fgDepartureStation">
					<Form.Label>Departure station</Form.Label>
					<Form.Control type="text" placeholder="Departure station name"
						onChange={(event: any) => this.updateDepartureRailwayStationNameSuggestions(event as React.ChangeEvent<HTMLInputElement>)}/>
				</FormGroup>
				<FormGroup controlId="fgDepartureStationSuggestions">
					<RailwayStationSuggestionList
						railwayStations={this.state.deparureStationSuggestions}
						selectedStation={this.state.departureStation}
						onStationSelected={(station) => this.setState({ departureStation: station })}/>
				</FormGroup>
				<hr className="mb-4"/>
				<Button variant="primary" type="submit" className="btn-block"
					disabled={
						(this.state.departureStation === undefined)
						|| (this.state.departureStation === null)
					}
					onClick={() => this.switchToDiscoveryView()}>
					Search for destinations
				</Button>
			</Form>
		);
	}
};

export default withRouter(DiscoverySearch);

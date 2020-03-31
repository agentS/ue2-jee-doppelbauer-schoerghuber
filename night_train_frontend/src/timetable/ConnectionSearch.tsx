import React from "react";
import { Form, FormGroup, Button } from "react-bootstrap";
import { withRouter, RouteComponentProps } from "react-router-dom";

import { TimetableApi, RailwayStationDto } from "../api/timetable";

import RailwayStationSuggestionList from "./RailwayStationSuggestionList";

interface ConnectionSearchProperties extends RouteComponentProps {
	timetableApi: TimetableApi
}

interface ConnectionSearchState {
	departureStationName: string,
	arrivalStationName: string,
	deparureStationSuggestions: Array<RailwayStationDto>,
	departureStation?: RailwayStationDto,
	arrivalStationSuggestions: Array<RailwayStationDto>,
	arrivalStation?: RailwayStationDto
}

class ConnectionSearch extends React.Component<ConnectionSearchProperties, ConnectionSearchState> {
	constructor(properties: ConnectionSearchProperties) {
		super(properties);
		this.state = {
			departureStationName: "",
			arrivalStationName: "",
			deparureStationSuggestions: [],
			departureStation: undefined,
			arrivalStationSuggestions: [],
			arrivalStation: undefined
		};
	}

	async updateDepartureRailwayStationNameSuggestions(event: React.ChangeEvent<HTMLInputElement>) {
		const suggestions = await this.autocompleteRailwayStationName(event);
		this.setState({ deparureStationSuggestions: suggestions });
	}

	async updateArrivalRailwayStationNameSuggestions(event: React.ChangeEvent<HTMLInputElement>) {
		const suggestions = await this.autocompleteRailwayStationName(event);
		this.setState({ arrivalStationSuggestions: suggestions });
	}

	async autocompleteRailwayStationName(event: React.ChangeEvent<HTMLInputElement>) {
		const searchTerm = event.target.value;
		if (searchTerm !== "") {
			return await this.props.timetableApi.railwayStationSearchSearchTermGet({searchTerm: searchTerm});
		} else {
			return [];
		}
	}

	switchToConnectionDisplay() {
		if (
			(this.state.departureStation !== undefined)
			&& (this.state.arrivalStation !== undefined)
			&& (this.state.departureStation !== null)
			&& (this.state.arrivalStation !== null)
		) {
			this.props.history.push(`/connection/from/${this.state.departureStation.id}/to/${this.state.arrivalStation.id}`);
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
				<FormGroup controlId="fgArrivalStation">
					<Form.Label>Arrival station</Form.Label>
					<Form.Control type="text" placeholder="Arrival station name"
						onChange={(event: any) => this.updateArrivalRailwayStationNameSuggestions(event as React.ChangeEvent<HTMLInputElement>)}/>
				</FormGroup>
				<FormGroup controlId="fgDepartureStationSuggestions">
					<RailwayStationSuggestionList
						railwayStations={this.state.arrivalStationSuggestions}
						selectedStation={this.state.arrivalStation}
						onStationSelected={(station) => this.setState({ arrivalStation: station })}/>
				</FormGroup>
				<hr className="mb-4"/>
				<Button variant="primary" type="submit" className="btn-block"
					disabled={
						(this.state.departureStation === undefined)
						|| (this.state.arrivalStation === undefined)
						|| (this.state.departureStation === null)
						|| (this.state.arrivalStation === null)
					}
					onClick={() => this.switchToConnectionDisplay()}
				>
					Search for connections
				</Button>
			</Form>
		);
	}
};

export default withRouter(ConnectionSearch);

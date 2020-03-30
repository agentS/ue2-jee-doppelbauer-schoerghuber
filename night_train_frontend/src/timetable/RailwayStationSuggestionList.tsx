import React from "react";

import { RailwayStationDto } from "../api/timetable";
import { ListGroup } from "react-bootstrap";

export interface IRailwayStatonSelected {
	(station: RailwayStationDto): void;
}

interface RailwayStationSuggestionListProperties {
	railwayStations: Array<RailwayStationDto>,
	selectedStation?: RailwayStationDto,
	onStationSelected: IRailwayStatonSelected 
};

class RailwayStationSuggestionList extends React.Component<RailwayStationSuggestionListProperties, {}> {
	render() {
		return (
			<ListGroup>
				{this.props.railwayStations
					.map(station => (
						<ListGroup.Item key={station.id}
							onClick={() => this.props.onStationSelected(station)}
							active={station.id === this.props.selectedStation?.id}>
							{station.name}
						</ListGroup.Item>
					)
				)}
			</ListGroup>
		);
	}
}

export default RailwayStationSuggestionList;

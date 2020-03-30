import React from "react";
import { Card, Accordion, Button, Table } from "react-bootstrap";

import moment from "moment";

import { TimetableApi, RailwayStationConnectionDto } from "../api/timetable";

interface ConnectionDisplayProperties {
	timetableApi: TimetableApi;
	departureStationId: number;
	arrivalStationId: number;
};

interface ConnectionDisplayState {
	connection: Array<RailwayStationConnectionDto>;
};

const MOMENT_TIME_PARSING_FORMAT = "HH:mm:ss";

class ConnectionDisplay extends React.Component<ConnectionDisplayProperties, ConnectionDisplayState> {
	constructor(properties: ConnectionDisplayProperties) {
		super(properties);
		this.state = {
			connection: []
		};
	}

	async componentDidMount() {
		await this.fetchConnection();
	}

	async componentDidUpdate(previousProperties: ConnectionDisplayProperties) {
		if (this.props !== previousProperties) {
			await this.fetchConnection();
		}
	}

	async fetchConnection() {
		const connection = await this.props.timetableApi.destinationsFromOriginIdToDestinationIdGet({
			originId: this.props.departureStationId,
			destinationId: this.props.arrivalStationId
		});
		this.setState({connection: connection});
	}

	getTotalTravelDuration(): number {
		const travelTimes = this.state.connection.map(hop => moment(hop.departureTime, MOMENT_TIME_PARSING_FORMAT));
		travelTimes.push(moment(
			this.state.connection[this.state.connection.length - 1].arrivalTime,
			MOMENT_TIME_PARSING_FORMAT
		));
		let totalTravelDays = 0;
		for (let index = 0; index < (travelTimes.length - 1); ++index) {
			if (travelTimes[index + 1].diff(travelTimes[index]) < 0) {
				++totalTravelDays;
			}
		}
		return totalTravelDays;
	}

	render() {
		if (this.state.connection.length > 0) {
			return (
				<div className="mt-4">
					<Table striped bordered>
						<thead>
							<tr>
								<th>Stop name</th>
								<th>Arrival time</th>
								<th>Departure time</th>
							</tr>
						</thead>
						<tbody>
							<tr key={this.state.connection[0].departureStation?.id}>
								<td>{this.state.connection[0].departureStation?.name}</td>
								<td>&nbsp;</td>
								<td>{this.state.connection[0].departureTime}</td>
							</tr>
							<tr key={this.state.connection[this.state.connection.length - 1].arrivalStation?.id}>
								<td>{this.state.connection[this.state.connection.length - 1].arrivalStation?.name}</td>
								<td>
									{this.state.connection[this.state.connection.length - 1].arrivalTime}
									{this.getTotalTravelDuration() > 0 ? `+${this.getTotalTravelDuration()} days` : ""}
								</td>
								<td>&nbsp;</td>
							</tr>
						</tbody>
					</Table>
					<Accordion>
						<Card>
							<Card.Header>
								<Accordion.Toggle as={Button} variant="link" eventKey="0">
									Toggle stops
								</Accordion.Toggle>
							</Card.Header>
							<Accordion.Collapse eventKey="0">
								<Card.Body>
									<Table striped bordered>
										<thead>
											<tr>
												<th>Stop name</th>
												<th>Arrival time</th>
												<th>Departure time</th>
											</tr>
										</thead>
										<tbody>
											<tr key={this.state.connection[0].departureStation?.id}>
												<td>{this.state.connection[0].departureStation?.name}</td>
												<td>&nbsp;</td>
												<td>{this.state.connection[0].departureTime}</td>
											</tr>
											{this.state.connection.map((hop, index) => (
												<tr key={hop.arrivalStation?.id}>
													<td>{hop.arrivalStation?.name}</td>
													<td>{hop.arrivalTime}</td>
													<td>{index < (this.state.connection.length - 1) ? this.state.connection[index + 1].departureTime : ""}</td>
												</tr>
											))}
										</tbody>
									</Table>
								</Card.Body>
							</Accordion.Collapse>
						</Card>
					</Accordion>
					<Button variant="primary" className="btn-block mt-2">Book ticket</Button>
				</div>
			);
		} else {
			return (<h3>Loading connection</h3>);
		}
	}
}

export default ConnectionDisplay;

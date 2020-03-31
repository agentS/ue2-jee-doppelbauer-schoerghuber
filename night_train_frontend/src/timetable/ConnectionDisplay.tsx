import React from "react";
import { Card, Accordion, Button, Table, Form } from "react-bootstrap";
import { withRouter, RouteComponentProps } from "react-router-dom";

import moment from "moment";

import { MOMENT_ISO_DATE_FORMAT, MOMENT_TIME_PARSING_FORMAT } from "../Constants";

import { TimetableApi, RailwayStationConnectionDto } from "../api/timetable";
import { BookingApi, TrainCarType } from "../api/booking";

interface ConnectionDisplayProperties extends RouteComponentProps {
	timetableApi: TimetableApi;
	bookingApi: BookingApi;
	departureStationId: number;
	arrivalStationId: number;
};

interface ConnectionDisplayState {
	connection: Array<RailwayStationConnectionDto>;
	trainCarType: TrainCarType;
	departureDay: Date;
};

class ConnectionDisplay extends React.Component<ConnectionDisplayProperties, ConnectionDisplayState> {
	constructor(properties: ConnectionDisplayProperties) {
		super(properties);
		this.state = {
			connection: [],
			trainCarType: TrainCarType.SLEEPER,
			departureDay: moment().startOf("day").toDate()
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

	updateDepartureDate(event: React.ChangeEvent<HTMLInputElement>) {
		const departureDay = moment(event.target.value, MOMENT_ISO_DATE_FORMAT);
		if (departureDay.diff(moment().startOf("day"), "days") >= 0) {
			this.setState({ departureDay: departureDay.toDate() });
		}
	}

	async bookTicket() {
		const bookingResponse = await this.props.bookingApi.apiBookingsPost({
			bookingRequestDto2: {
				originId: this.props.departureStationId,
				destinationId: this.props.arrivalStationId,
				trainCarType: this.state.trainCarType,
				journeyStartDate: this.state.departureDay
			}
		});
		this.props.history.push(`/booking/${bookingResponse.bookingId}`);
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
					<Form className="mt-2" onSubmit={(event: React.FormEvent) => { event.preventDefault(); this.bookTicket(); }}>
						<Form.Group controlId="fgCarType">
							<Form.Check inline label="Sleeper car" type="radio" id="cbSleeper" name="cbCarType"
								checked={this.state.trainCarType === TrainCarType.SLEEPER}
								onChange={() => this.setState({ trainCarType: TrainCarType.SLEEPER })}/>
							<Form.Check inline label="Couchette car" type="radio" id="cbCouchette" name="cbCarType"
								checked={this.state.trainCarType === TrainCarType.COUCHETTE}
								onChange={() => this.setState({ trainCarType: TrainCarType.COUCHETTE })}/>
							<Form.Check inline label="Seat car" type="radio" id="cbSeat" name="cbCarType"
								checked={this.state.trainCarType === TrainCarType.SEAT}
								onChange={() => this.setState({ trainCarType: TrainCarType.SEAT })}/>
						</Form.Group>
						<Form.Group controlId="fgDepartureDate">
							<Form.Label>Departure date</Form.Label>
							<Form.Control type="date"
								value={moment(this.state.departureDay).format(MOMENT_ISO_DATE_FORMAT)}
								onChange={(event: React.ChangeEvent<HTMLInputElement>) => this.updateDepartureDate(event)}/>
						</Form.Group>
						<Button variant="primary" type="submit" className="btn-block mt-2">
							Book ticket
						</Button>
					</Form>
				</div>
			);
		} else {
			return (<h3>Loading connection</h3>);
		}
	}
}

export default withRouter(ConnectionDisplay);
